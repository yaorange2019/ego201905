package com.ego.cart.controller;

import com.ego.cart.pojo.Cart;
import com.ego.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/10/12
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num) {
        cartService.addCart(skuId, num);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Cart>> queryCarts()
    {
        List<Cart> cartList = cartService.findCarts();
        if(cartList==null)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(cartList);
    }

    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num)
    {
        cartService.updateNum(skuId,num);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") Long skuId)
    {
        cartService.deleteCart(skuId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
