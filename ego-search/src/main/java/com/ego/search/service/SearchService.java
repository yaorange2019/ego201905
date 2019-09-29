package com.ego.search.service;

import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.Sku;
import com.ego.item.pojo.SpuBO;
import com.ego.search.client.GoodsClient;
import com.ego.search.pojo.Goods;
import com.ego.search.pojo.SearchRequest;
import com.ego.search.repository.GoodsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/9/29
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Service
public class SearchService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private GoodsRepository goodsRepository;
    /**
     * 通过spuBO创建一个goods
     *
     * @param spuBO
     * @return
     */
    public Goods buildGoods(SpuBO spuBO) {
        Goods result = new Goods();
        result.setAll(spuBO.getTitle()+"  "+spuBO.getCategoryNames().replaceAll("/"," ")+"   "+spuBO.getBrandName());
        result.setBrandId(spuBO.getBrandId());
        result.setCid1(spuBO.getCid1());
        result.setCid2(spuBO.getCid2());
        result.setCid3(spuBO.getCid3());
        result.setCreateTime(spuBO.getCreateTime());
        result.setId(spuBO.getId());
        result.setSubTitle(spuBO.getSubTitle());
        //根据spuId获取对应的skus
        List<Sku> skuList = goodsClient.querySkusBySpuId(spuBO.getId()).getBody();
        List<Long> prices = new ArrayList<>(skuList.size());

        List<Map<String, Object>> skus = new ArrayList<>();

        for (Sku sku : skuList) {
            prices.add(sku.getPrice());
            //Sku-->Map(id,title,image,price)
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("image", sku.getImages().split(",")[0]);
            skuMap.put("price", sku.getPrice());

            skus.add(skuMap);
        }
        //将skus-->json
        try {
            String skuJson = objectMapper.writeValueAsString(skus);
            result.setSkus(skuJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setPrice(prices);

        //用于存储当前spu可搜索的规格参数
        Map<String, Object> specsMap = new HashMap<>();
        String specifications = goodsClient.querySpecificationBySpuId(spuBO.getId()).getBody();
        //解析规格json
        try {
            List<Map<String,Object>> specList = objectMapper.readValue(specifications,new TypeReference<List<Map<String,Object>>>(){});

            specList.forEach(spec->{
                List<Map<String,Object>> params = (List<Map<String,Object>>)spec.get("params");
                params.forEach(param->{
                    Boolean searchable = (Boolean) param.get("searchable");
                    if(searchable!=null && searchable)
                    {
                        //获取k 和  v
                        String k = (String) param.get("k");
                        Object v = param.get("v");

                        specsMap.put(k, v);
                    }
                });
            });
            result.setSpecs(specsMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public PageResult<Goods> page(SearchRequest searchRequest) {
        String key = searchRequest.getKey();
        Integer page = searchRequest.getPage();
        if (StringUtils.isBlank(key)) {
            return null;
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //设置查询条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("all",searchRequest.getKey()).operator(Operator.AND));
        //设置指定字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","subTitle","skus"},null));
        //设置分页查询
        queryBuilder.withPageable(PageRequest.of(page-1,searchRequest.getSize()));

        //执行查询
        Page<Goods> pageInfo = goodsRepository.search(queryBuilder.build());

        return new PageResult<>(pageInfo.getTotalElements(),Long.valueOf(pageInfo.getTotalPages()),pageInfo.getContent());
    }
}
