package com.ego.goods.listener;

import com.ego.goods.service.GoodsService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/10/9
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Component
public class GoodsListener {

    @Autowired
    private GoodsService goodsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ego.create.web.queue", durable = "true"),
            exchange = @Exchange(
                    value = "ego.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
    public void createStaticHtml(Long spuId){
        //重新创建静态页面
        Map<String, Object> map = goodsService.loadModel(spuId);
        goodsService.buildStaticHtml(spuId,map);
    }

}
