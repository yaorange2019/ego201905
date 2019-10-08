package com.ego.item.api;

import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/9/29
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@RequestMapping("brand")
public interface BrandApi {
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> page(
            @RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo,
            @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
            @RequestParam(value = "sortBy")String sortBy,
            @RequestParam(value = "descending",defaultValue = "true")Boolean descending,
            @RequestParam(value = "key")String key
    );


    @PostMapping
    public ResponseEntity<Void> save(Brand brand, @RequestParam("cids") Long[] cids);

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandListByCid(@PathVariable("cid") Long cid) ;

    @GetMapping("list/ids")
    public ResponseEntity<List<Brand>> queryListByIds(@RequestParam("ids") List<Long> ids);
}
