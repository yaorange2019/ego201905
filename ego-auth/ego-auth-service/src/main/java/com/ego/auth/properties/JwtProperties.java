package com.ego.auth.properties;

import com.ego.auth.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/10/11
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Data
@ConfigurationProperties(prefix = "ego.jwt")
public class JwtProperties {
    private String secret;
    private String pubKeyPath;
    private String priKeyPath;
    private Integer expire;
    private String  cookieName;
    private Integer cookieMaxAge;


    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct
    public void  init() throws Exception {

        //判断pubKeyPath & priKeyPath是否存在
        File f1 = new File(pubKeyPath);
        File f2 = new File(priKeyPath);
        //如果不存在
        if(!f1.exists()||!f2.exists())
        {
            try {
                //通过秘钥生成私钥和公钥
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //初始化publicKey & privateKey
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }
}
