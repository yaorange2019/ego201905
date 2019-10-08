package com.ego.item.web;

import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.Sku;
import com.ego.item.pojo.SpuBO;
import com.ego.item.pojo.SpuDetail;
import com.ego.item.service.GoodsService;
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
 * @create 2019/9/25
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@RestController
@RequestMapping("goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuBO>> page(
            @RequestParam(value = "page",defaultValue = "1")Integer pageNo,
            @RequestParam(value = "rows",defaultValue = "5")Integer pageSize,
            @RequestParam(value = "sortBy",required = false)String sortBy,
            @RequestParam(value = "descending",defaultValue = "true",required = false)Boolean descending,
            @RequestParam(value = "key")String key,
            @RequestParam(value = "saleable")Boolean saleable
    )
    {
        PageResult<SpuBO> result = goodsService.page(pageNo, pageSize, sortBy, descending, key,saleable);
        if(result==null || result.getItems()==null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }


    @PostMapping
    public ResponseEntity<Void> save(@RequestBody SpuBO spuBO)
    {
        goodsService.save(spuBO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("skus")
    public ResponseEntity<List<Sku>> querySkusBySpuId(@RequestParam("spuId") Long spuId)
    {
        List<Sku> result = goodsService.findSkusBySpuId(spuId);
        return ResponseEntity.ok(result);
    }


    @GetMapping("specification")
    public ResponseEntity<String> querySpecificationBySpuId(@RequestParam("spuId")Long spuId){
        SpuDetail spuDetail = goodsService.findSpuDetailBySpuId(spuId);
        if(spuDetail==null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail.getSpecifications());
    }

    @GetMapping("spuBO/{spuId}")
    public ResponseEntity<SpuBO> queryGoodsById(@PathVariable("spuId") Long spuId)
    {
        SpuBO result = goodsService.findSpuBOById(spuId);
        if(result==null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
