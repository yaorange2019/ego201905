package com.ego.item.api;

import com.ego.item.pojo.Category;
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
@RequestMapping("category")
public interface CategoryApi {
    @GetMapping("list")
    public ResponseEntity<List<Category>> list(@RequestParam("pid") Long pid) ;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Category category);

    @PutMapping
    public ResponseEntity<Void> update(@RequestParam("id") Long id, @RequestParam("name") String name) ;

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id")Long id);

    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryListByIds(@RequestParam("ids") List<Long> ids);
}
