package com.ego.item.web;

import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.Brand;
import com.ego.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/9/25
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
//    pageNo=1&pageSize=5&sortBy=id&descending=false&key=
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> page(
            @RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo,
            @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
            @RequestParam(value = "sortBy")String sortBy,
            @RequestParam(value = "descending",defaultValue = "true")Boolean descending,
            @RequestParam(value = "key")String key
    )
    {
        PageResult<Brand> result = brandService.page(pageNo, pageSize, sortBy, descending, key);
        if(result==null || result.getItems()==null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }


    @PostMapping
    public ResponseEntity<Void> save(Brand brand, @RequestParam("cids") Long[] cids)
    {
        brandService.save(brand, cids);

        return ResponseEntity.ok().build();
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandListByCid(@PathVariable("cid") Long cid) {
        List<Brand> result = brandService.findListByCid(cid);

        return ResponseEntity.ok(result);
    }
}
