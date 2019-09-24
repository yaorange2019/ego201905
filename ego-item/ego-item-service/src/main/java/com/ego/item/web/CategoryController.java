package com.ego.item.web;

import com.ego.item.pojo.Category;
import com.ego.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/9/24
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public ResponseEntity<List<Category>> list(@RequestParam("pid") Long pid) {
        //根据pid查询对应子类别list
        List<Category> categoryList = categoryService.findListByPid(pid);

        if(categoryList==null)
        {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(categoryList);
    }


    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Category category)
    {
        categoryService.save(category);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping
    public ResponseEntity<Void> update(@RequestParam("id") Long id, @RequestParam("name") String name) {

        categoryService.updateName(id, name);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //http://api.ego.com/api/item/category/1
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id")Long id)
    {
        categoryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }



}
