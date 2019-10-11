package com.ego.user.controller;

import com.ego.user.pojo.User;
import com.ego.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/10/10
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> check(@PathVariable("data") String data, @PathVariable("type") Integer type) {
        Boolean result = userService.check(data, type);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/send")
    public ResponseEntity<Void> send(@RequestParam("phone") String phone) {
        userService.sendCode(phone);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid User user, String code) {
        boolean result = userService.register(user, code);
        if (result == false) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("user/{username}/{password}")
    ResponseEntity<User> findUserByUP(@PathVariable("username") String username,
                                      @PathVariable("password") String password){

        User user = userService.findUserByUP(username, password);
        if(user==null)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }
}
