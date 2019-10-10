package com.ego.search.listener;

import com.ego.item.pojo.SpuBO;
import com.ego.search.client.GoodsClient;
import com.ego.search.pojo.Goods;
import com.ego.search.repository.GoodsRepository;
import com.ego.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ego.create.web.queue", durable = "true"),
            exchange = @Exchange(
                    value = "ego.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
    public void updateEs(Long spuId){
        //根据spuid 保存或者更新
        SpuBO spuBO = goodsClient.queryGoodsById(spuId).getBody();
        Goods goods = searchService.buildGoods(spuBO);
        goodsRepository.save(goods);
    }

}
