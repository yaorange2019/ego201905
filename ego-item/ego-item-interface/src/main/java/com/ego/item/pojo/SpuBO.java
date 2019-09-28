package com.ego.item.pojo;

import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/9/27
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Data
public class SpuBO extends Spu {

    //业务属性
    @Transient
    private String categoryNames;
    @Transient
    private String brandName;
    @Transient
    private List<Sku> skus;
    @Transient
    private SpuDetail spuDetail;


}
