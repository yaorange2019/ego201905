package com.ego.item.api;

import com.ego.common.pojo.CartDto;
import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.Sku;
import com.ego.item.pojo.SpuBO;
import com.ego.item.pojo.Stock;
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
@RequestMapping("goods")
public interface GoodsApi {
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuBO>> page(
            @RequestParam(value = "page",defaultValue = "1")Integer pageNo,
            @RequestParam(value = "rows",defaultValue = "5")Integer pageSize,
            @RequestParam(value = "sortBy",required = false)String sortBy,
            @RequestParam(value = "descending",defaultValue = "true",required = false)Boolean descending,
            @RequestParam(value = "key")String key,
            @RequestParam(value = "saleable")Boolean saleable
    );


    @PostMapping
    public ResponseEntity<Void> save(@RequestBody SpuBO spuBO);

    @GetMapping("skus")
    public ResponseEntity<List<Sku>> querySkusBySpuId(@RequestParam("spuId") Long spuId);

    @GetMapping("specification")
    public ResponseEntity<String> querySpecificationBySpuId(@RequestParam("spuId")Long spuId);

    @GetMapping("spuBO/{spuId}")
    public ResponseEntity<SpuBO> queryGoodsById(@PathVariable("spuId") Long spuId);
    @GetMapping("sku/{skuId}")
    public ResponseEntity<Sku> querySkuBySkuId(@PathVariable("skuId") Long skuId);

    /**
     * 减库存
     * @param cartDTOS
     */
    @PostMapping("stock/decrease")
    void decreaseStock(@RequestBody List<CartDto> cartDTOS);


    /**
     * 根据skuIds查询库存
     * @param skuIds
     * @return
     */
    @GetMapping("stock/skuIds")
    public ResponseEntity<List<Stock>> queryStockList(@RequestParam("skuIds") List<Long> skuIds);

    @GetMapping("stock/{skuId}")
    public ResponseEntity<Stock> queryStockBySkuId(@PathVariable("skuId") Long skuId);

    /**
     * 减秒杀库存
     * @param cartDTO
     */
    @PostMapping("stock/seckill/decrease")
    void decreaseSeckillStock(@RequestBody CartDto cartDTO);
}
