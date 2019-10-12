package com.ego.goods.controller;

import com.ego.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/10/8
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/item/{spuId}.html")
    public String item(@PathVariable("spuId")Long spuId, Model model)
    {
        //加载模型数据
        Map<String, Object> map = goodsService.loadModel(spuId);
        //传递模型数据
        model.addAllAttributes(map);
        //生成静态页面
//        goodsService.buildStaticHtml(spuId,map);
        return "item";
    }

}
