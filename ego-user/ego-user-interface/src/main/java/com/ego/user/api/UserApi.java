package com.ego.user.api;

import com.ego.user.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/10/11
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
public interface UserApi {

    @GetMapping("user/{username}/{password}")
    ResponseEntity<User> findUserByUP(@PathVariable("username") String username,
                                      @PathVariable("password") String password);

}
