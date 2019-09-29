package com.ego.search.repository;

import com.ego.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/9/29
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Repository
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
