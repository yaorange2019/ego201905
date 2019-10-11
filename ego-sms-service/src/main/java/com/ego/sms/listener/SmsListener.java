package com.ego.sms.listener;

import com.ego.sms.utils.SmsUtils;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/10/10
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Component
public class SmsListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ego.sms.queue", durable = "true"),
            exchange = @Exchange(value = "ego.sms.exchange",
                    ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}))
    public void sendSms(Message message, Channel channel) throws IOException {

        //拆分数据
        String data = new String(message.getBody(), "utf-8");

        String[] split = data.split("&&&");
        String phone = split[0];
        String code = split[1];

        //调用阿里大于短信平台发送短信

        SmsUtils.sendSms(phone, code);

        //手动确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    }

}
