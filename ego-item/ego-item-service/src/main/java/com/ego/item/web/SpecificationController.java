package com.ego.item.web;

import com.ego.item.pojo.Category;
import com.ego.item.pojo.Specification;
import com.ego.item.service.CategoryService;
import com.ego.item.service.SpecificationService;
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
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specService;

    @GetMapping("{cid}")
    public ResponseEntity<String> querySpecByCid(@PathVariable("cid") Long cid) {
        Specification specification = specService.findByCid(cid);

        if(specification==null)
        {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(specification.getSpecifications());
    }


    @PostMapping
    public ResponseEntity<Void> save(Specification specification)
    {
        specService.save(specification);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
