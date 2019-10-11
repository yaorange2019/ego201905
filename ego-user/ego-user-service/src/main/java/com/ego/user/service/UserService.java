package com.ego.user.service;

import com.ego.common.utils.CodecUtils;
import com.ego.common.utils.NumberUtils;
import com.ego.user.mapper.UserMapper;
import com.ego.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/10/10
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Slf4j
@Service
public class UserService implements RabbitTemplate.ConfirmCallback {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    //告诉spring每次给我一个新的实例
//    @Lookup
//    protected   RabbitTemplate rabbitTemplate(){
//        return  null;
//    };


    public Boolean check(String data, Integer type) {

        User user = new User();
        if(type==1)
        {
            //检测用户名
            user.setUsername(data);
        }
        else if(type==2)
        {
            //检测手机
            user.setPhone(data);
        }

        return userMapper.selectOne(user)==null?true:false;
    }

    /**
     * 发送验证码
     * @param phone
     */
    public void sendCode(String phone) {
        //1.生成验证码
        String code = NumberUtils.generateCode(6);
        //2.保存到redis(5分钟内有效)
        stringRedisTemplate.opsForValue().set(phone, code,5, TimeUnit.MINUTES);
        //3.异步发送短信(mq)
        String data = phone+"&&&"+code;
        Message message = MessageBuilder.withBody(data.getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("utf-8").setMessageId(data).build();

        CorrelationData correlationData = new CorrelationData(phone);

        //一个rabbitTemplate只能有一个回调，因此要保证每次拿到的模板是不同对象
//        RabbitTemplate rabbitTemplate  = this.rabbitTemplate();
        rabbitTemplate.setMandatory(true);
        //mq收到消息后，会执行该回调方法
//        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
//            @Override
//            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//                log.info("MQ成功接收到手机号[{}]消息",correlationData.getId());
//            }
//        });
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.convertAndSend("sms.verify.code",message,correlationData);
    }

    @Transactional
    public boolean register(User user, String code) {
        Boolean result = false;
        //校验验证码
            //查询到redis中的验证码
        String redisCode = stringRedisTemplate.opsForValue().get(user.getPhone());
        if (StringUtils.isNotBlank(code) && code.equals(redisCode)) {
            //保存用户
            user.setCreated(new Date());

            //对密码加密
            String enPass = CodecUtils.passwordBcryptEncode(user.getUsername(), user.getPassword());
            user.setPassword(enPass);
            userMapper.insert(user);
            //删除redis验证码
            stringRedisTemplate.delete(user.getPhone());

            result = true;
        }
        return result;
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("MQ成功接收到手机号[{}]消息",correlationData.getId());
    }

    public User findUserByUP(String username, String password) {

        User user = new User();
        user.setUsername(username);
        user = userMapper.selectOne(user);
        if(user==null)
        {
            return null;
        }
        //判断密码是否正确

        Boolean result = CodecUtils.passwordConfirm(username+password, user.getPassword());

        return result?user:null;
    }
}
