package com.ego.cart.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/10/14
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Component
public class CartListener {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ego.cart.delete.queue", durable = "true"),
            exchange = @Exchange(
                    value = "ego.cart.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"cart.delete"}))
    public void deleteCart(Map<String, Object> map){
        BoundHashOperations<String, Object, Object> carts = stringRedisTemplate.boundHashOps("ego:cart:uid:" + map.get("userId"));
        Long[] skuIds = (Long[])map.get("skuIds");
        for (Long skuId : skuIds) {
            carts.delete(skuId.toString());
        }

    }
}
