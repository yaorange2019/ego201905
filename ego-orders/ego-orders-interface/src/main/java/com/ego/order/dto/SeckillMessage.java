package com.ego.order.dto;

import com.ego.auth.entity.UserInfo;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: yaorange
 * @Time: 2018-11-15 20:19
 * @Feature: 秒杀信息
 */
@Data
@ToString
public class SeckillMessage implements Serializable {
    /**
     * 用户信息
     */
    private UserInfo userInfo;

    /**
     * 秒杀商品实际价格
     */
    private Long skuId;

    /**
     * 秒杀商品价格
     */
    private Long seckillPrice;

    /**
     * 秒杀商品实际价格
     */
    private Long price;

    private String title;

    private String image;

    public SeckillMessage() {
    }

    public SeckillMessage(UserInfo userInfo, Long skuId, Long seckillPrice, Long price) {
        this.userInfo = userInfo;
        this.skuId = skuId;
        this.seckillPrice = seckillPrice;
        this.price = price;
    }

    public SeckillMessage(UserInfo userInfo, Long skuId, Long seckillPrice, Long price, String title, String image) {
        this.userInfo = userInfo;
        this.skuId = skuId;
        this.seckillPrice = seckillPrice;
        this.price = price;
        this.title = title;
        this.image = image;
    }
}
