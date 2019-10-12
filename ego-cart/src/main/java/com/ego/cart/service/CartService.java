package com.ego.cart.service;

import com.ego.auth.entity.UserInfo;
import com.ego.cart.client.GoodsClient;
import com.ego.cart.interceptor.LoginInterceptor;
import com.ego.cart.pojo.Cart;
import com.ego.common.utils.JsonUtils;
import com.ego.item.pojo.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/10/12
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Service
public class CartService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    private final  static String PREFIX = "ego:cart:uid:";

    public void addCart(Long skuId, Integer num) {
        //获取用户id
        UserInfo userinfo = LoginInterceptor.getUserinfo();

        //查询对应用户的购物车
        BoundHashOperations<String, Object, Object> carts = stringRedisTemplate.boundHashOps(PREFIX + userinfo.getId());
        //判断是否有购物车
        if (carts != null)
        {
            //有->判断是否已经存在该购物项
            if(carts.hasKey(skuId.toString()))
            {
                //有->修改数量
                String json = (String) carts.get(skuId.toString());
                //将json->对象(cart)
                Cart cart = JsonUtils.parse(json, Cart.class);
                cart.setNum(cart.getNum()+num);
                //写入redis
                carts.put(skuId.toString(),JsonUtils.serialize(cart));
            }
            else
            {
               addCartItem(carts,skuId,num,userinfo);
            }
        }

        System.out.println();
    }

    private void addCartItem(BoundHashOperations<String, Object, Object> carts, Long skuId, Integer num, UserInfo userinfo) {
        //没有->新增购物项
        Cart cart  = new Cart();
        Sku sku = goodsClient.querySkuBySkuId(skuId).getBody();
        cart.setNum(num);
        cart.setImage(sku.getImages().split(",")[0]);
        cart.setTitle(sku.getTitle());
        cart.setOwnSpec(sku.getOwnSpec());
        cart.setPrice(sku.getPrice());
        cart.setUserId(userinfo.getId());
        cart.setSkuId(skuId);

        //将cart->json
        String json = JsonUtils.serialize(cart);
        //存入redis
        carts.put(skuId.toString(), json);
    }

    public List<Cart> findCarts() {
        UserInfo userinfo = LoginInterceptor.getUserinfo();
        BoundHashOperations<String, Object, Object> carts = stringRedisTemplate.boundHashOps(PREFIX + userinfo.getId());
        return carts.values().stream()
                .map(json->JsonUtils.parse((String) json,Cart.class))
                .collect(Collectors.toList());
    }

    public void updateNum(Long skuId, Integer num) {
        UserInfo userinfo = LoginInterceptor.getUserinfo();
        BoundHashOperations<String, Object, Object> carts = stringRedisTemplate.boundHashOps(PREFIX + userinfo.getId());

        //根据skuId查询到购物项
        String json = (String) carts.get(skuId.toString());
        Cart cart = JsonUtils.parse(json, Cart.class);
        //更新数量
        cart.setNum(num);
        //写入redis
        carts.put(skuId.toString(),JsonUtils.serialize(cart));
    }

    public void deleteCart(Long skuId) {
        UserInfo userinfo = LoginInterceptor.getUserinfo();
        BoundHashOperations<String, Object, Object> carts = stringRedisTemplate.boundHashOps(PREFIX + userinfo.getId());

        carts.delete(skuId.toString());
    }
}
