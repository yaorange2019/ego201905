package com.ego.search.dto;

import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.Brand;
import com.ego.item.pojo.Category;
import com.ego.search.pojo.Goods;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/9/30
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Data
public class SearchResponse extends PageResult<Goods> {

    private List<Category> categories;// 分类过滤条件

    private List<Brand> brands;// 品牌过滤条件

    private List<Map<String,Object>> specs; // 规格参数过滤条件

    public SearchResponse() {
    }

    public SearchResponse(Long total, Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}
