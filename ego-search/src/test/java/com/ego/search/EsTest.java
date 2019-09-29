package com.ego.search;

import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.SpuBO;
import com.ego.search.client.GoodsClient;
import com.ego.search.pojo.Goods;
import com.ego.search.repository.GoodsRepository;
import com.ego.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/9/29
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EsTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SearchService searchService;
    @Autowired
    private GoodsRepository goodsRepository;
    @Test
    public void testCreateIndex(){
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
    }

    @Test
    public void importData()
    {
        int size = 100;
        int count = 0;
        int pageNo = 1;
        do {
            //分页查询数据
            PageResult<SpuBO> pageResult = goodsClient.page(pageNo, size, null, null, "", true).getBody();

            List<SpuBO> items = pageResult.getItems();

            count = items.size();

            List<Goods> goodsList = new ArrayList<>(count);

            //将SpuBO-->Goods
            for (SpuBO spuBO : items) {
                try{
                    Goods goods = searchService.buildGoods(spuBO);
                    goodsList.add(goods);
                }catch (Exception e)
                {
                    System.out.println("spu:"+spuBO.getId()+"转换出现问题");
                    e.printStackTrace();
                }

            }
            goodsRepository.saveAll(goodsList);
            pageNo++;
        }while (count==100);


    }
}
