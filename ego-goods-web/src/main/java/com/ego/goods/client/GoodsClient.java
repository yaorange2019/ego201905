package com.ego.goods.client;

import com.ego.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/10/8
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
