package com.ego.item.mapper;

import com.ego.item.pojo.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface StockMapper extends Mapper<Stock>, SelectByIdListMapper<Stock,Long> {
    @Update("update tb_stock set stock = stock - #{num} where sku_id = #{skuId} and stock >= #{num}")
    int decreaseStock(@Param("skuId") Long skuId, @Param("num") Integer num);

    @Update("update tb_stock set seckill_stock = seckill_stock - #{num} where sku_id = #{skuId} and seckill_stock >= #{num}")
    int decreaseSeckillStock(@Param("skuId") Long skuId, @Param("num") Integer num);
}
