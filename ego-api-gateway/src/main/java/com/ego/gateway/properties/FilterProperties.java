package com.ego.gateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/10/11
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@ConfigurationProperties(prefix = "ego.filter")
public class FilterProperties {
    private List<String> allowPaths;


    public List<String> getAllowPaths() {
        return allowPaths;
    }

    public void setAllowPaths(List<String> allowPaths) {
        this.allowPaths = allowPaths;
    }
}
