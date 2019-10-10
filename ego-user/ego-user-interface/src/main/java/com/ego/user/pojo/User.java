package com.ego.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/10/10
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Data
@Table(name = "tb_user")
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    @NotNull
    @Length(min = 4,max = 20,message = "用户名长度必须在4-20之间")
    private String username;// 用户名

    @NotNull
    @JsonIgnore
    private String password;// 密码
    @NotNull
    @Pattern(regexp = "^1[356789]\\d{9}$", message = "手机号格式不正确")
    private String phone;// 电话

    private Date created;// 创建时间

}
