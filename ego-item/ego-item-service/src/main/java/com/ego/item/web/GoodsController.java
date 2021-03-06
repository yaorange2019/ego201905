package com.ego.item.web;

import com.ego.common.pojo.CartDto;
import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.Sku;
import com.ego.item.pojo.SpuBO;
import com.ego.item.pojo.SpuDetail;
import com.ego.item.pojo.Stock;
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

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody SpuBO spuBO){
        goodsService.update(spuBO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId") Long spuId) {
        SpuDetail result = goodsService.findSpuDetailBySpuId(spuId);

        return ResponseEntity.ok(result);
    }


    @GetMapping("sku/{skuId}")
    public ResponseEntity<Sku> querySkuBySkuId(@PathVariable("skuId") Long skuId){
        Sku sku = goodsService.findSkuBySkuId(skuId);
        if(sku==null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sku);
    }

    /**
     * 减库存
     * @param cartDtos
     * @return
     */
    @PostMapping("stock/decrease")
    public ResponseEntity<Void> decreaseStock(@RequestBody List<CartDto> cartDtos){
        goodsService.decreaseStock(cartDtos);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("stock/skuIds")
    public ResponseEntity<List<Stock>> queryStockList(@RequestParam("skuIds") List<Long> skuIds){
        List<Stock> result = goodsService.findStockList(skuIds);
        return ResponseEntity.ok(result);
    }

    @GetMapping("stock/{skuId}")
    public ResponseEntity<Stock> queryStockBySkuId(@PathVariable("skuId") Long skuId){
        Stock result = goodsService.getStockBySkuId(skuId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("stock/seckill/decrease")
    public ResponseEntity<Void> decreaseSeckillStock(@RequestBody CartDto cartDTO){
        goodsService.decreaseSeckillStock(cartDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
