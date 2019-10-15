package com.ego.item.service;

import com.ego.common.enums.ExceptionEnum;
import com.ego.common.exception.PayException;
import com.ego.common.pojo.CartDto;
import com.ego.common.pojo.PageResult;
import com.ego.item.mapper.*;
import com.ego.item.pojo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/9/27
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<SpuBO> page(Integer pageNo, Integer pageSize, String sortBy, Boolean descending, String key, Boolean saleable) {

        //分页助手
        PageHelper.startPage(pageNo, pageSize);
        //设置查询条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%")
                    .orLike("subTitle", "%" + key + "%");
        }

        if(saleable!=null)
        {
            criteria.andEqualTo("saleable", saleable);
        }

        //设置排序条件
        example.setOrderByClause(sortBy+" "+(descending?"desc":"asc"));

        //查询
        Page<Spu> pageInfo = (Page<Spu>)spuMapper.selectByExample(example);

        //查询出每个spu的类别名字，品牌名字
        List<SpuBO> result = pageInfo.stream().map(spu -> {
            SpuBO spuBO = new SpuBO();
            //复制
            BeanUtils.copyProperties(spu,spuBO);
            String categoryName = "";
            String brandName = brandMapper.selectByPrimaryKey(spu.getBrandId()).getName();
            List<Category> categoryList = categoryMapper.selectByIdList(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

            for (Category category : categoryList) {
                categoryName += category.getName() + "/";
            }

            spuBO.setCategoryNames(categoryName);
            spuBO.setBrandName(brandName);
            return spuBO;
        }).collect(Collectors.toList());

        return new PageResult<SpuBO>(pageInfo.getTotal(),result);
    }

    @Transactional
    public void save(SpuBO spuBO) {
        //新增spu
        spuBO.setCreateTime(new Date());
        spuBO.setLastUpdateTime(spuBO.getCreateTime());

        spuMapper.insertSelective(spuBO);

        Long spuId = spuBO.getId();

        //新增spu detail
        SpuDetail spuDetail = spuBO.getSpuDetail();
        spuDetail.setSpuId(spuId);
        spuDetailMapper.insertSelective(spuBO.getSpuDetail());
        //新增skus
        List<Sku> skus = spuBO.getSkus();
        for (Sku sku : skus) {
            sku.setSpuId(spuId);
            sku.setCreateTime(spuBO.getCreateTime());
            sku.setLastUpdateTime(spuBO.getCreateTime());
            skuMapper.insertSelective(sku);
            Stock stock = sku.getStock();
            //新增stock
            stock.setSkuId(sku.getId());
            stockMapper.insertSelective(stock);
        }

        //发消息到mq
        amqpTemplate.convertAndSend("item.insert", spuBO.getId());
    }

    @Transactional
    public void update(SpuBO spuBO) {
        //1.修改spu
        spuBO.setLastUpdateTime(new Date());
        spuMapper.updateByPrimaryKeySelective(spuBO);
        //2.修改spu detail
        SpuDetail spuDetail = spuBO.getSpuDetail();
        spuDetailMapper.updateByPrimaryKeySelective(spuDetail);
        //3.刪除以前的sku以及stock
        Sku skuExample = new Sku();
        skuExample.setSpuId(spuBO.getId());
        List<Sku> skuList = skuMapper.select(skuExample);
        skuList.forEach(s -> {
            skuMapper.deleteByPrimaryKey(s);
            stockMapper.deleteByPrimaryKey(s.getId());
        });

        //4.添加sku&stock
        if (spuBO.getSkus() != null && spuBO.getSkus().size() > 0) {
            spuBO.getSkus().forEach(sku -> {
                sku.setId(null);
                sku.setCreateTime(spuBO.getCreateTime());
                sku.setLastUpdateTime(spuBO.getCreateTime());
                sku.setSpuId(spuBO.getId());
                skuMapper.insertSelective(sku);

                sku.getStock().setSkuId(sku.getId());
                stockMapper.insertSelective(sku.getStock());
            });
        }

        //发消息到mq
        amqpTemplate.convertAndSend("item.update", spuBO.getId());

    }

  public List<Sku> findSkusBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);

        return skuMapper.select(sku);
    }

    public SpuDetail findSpuDetailBySpuId(Long spuId) {
        return spuDetailMapper.selectByPrimaryKey(spuId);
    }

    public SpuBO findSpuBOById(Long spuId) {
        SpuBO spuBO = new SpuBO();
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        BeanUtils.copyProperties(spu, spuBO);

        //spuDetail
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        spuBO.setSpuDetail(spuDetail);

        //skus
        Sku sku = new Sku();
        sku.setSpuId(spuId);

        List<Sku> skus = skuMapper.select(sku);
        spuBO.setSkus(skus);

        //类名名字
        String categoryName="";
        List<Category> categoryList = categoryMapper.selectByIdList(Arrays.asList(spuBO.getCid1(), spuBO.getCid2(), spuBO.getCid3()));
        for (Category category : categoryList) {
            categoryName += category.getName() + "/";
        }
        spuBO.setCategoryNames(categoryName);

        spuBO.setBrandName(brandMapper.selectByPrimaryKey(spuBO.getBrandId()).getName());

        return spuBO;
    }

    public Sku findSkuBySkuId(Long skuId) {
        return skuMapper.selectByPrimaryKey(skuId);
    }
    @Transactional
    public void decreaseStock(List<CartDto> cartDtos) {
        for (CartDto cartDto : cartDtos) {
            int count = stockMapper.decreaseStock(cartDto.getSkuId(), cartDto.getNum());
            if (count != 1) {
                throw new PayException(ExceptionEnum.STOCK_NOT_ENOUGH);
            }
        }
    }


    public List<Stock> findStockList(List<Long> skuIds) {
        return stockMapper.selectByIdList(skuIds);
    }

    public Stock getStockBySkuId(Long skuId) {
        return stockMapper.selectByPrimaryKey(skuId);
    }

    @Transactional
    public void decreaseSeckillStock(CartDto cartDto) {
        int count = stockMapper.decreaseSeckillStock(cartDto.getSkuId(), cartDto.getNum());
        if (count != 1) {
            throw new PayException(ExceptionEnum.STOCK_NOT_ENOUGH);
        }
    }
}
