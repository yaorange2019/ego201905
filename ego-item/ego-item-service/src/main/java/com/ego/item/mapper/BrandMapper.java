package com.ego.item.mapper;

import com.ego.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {
    @Insert("insert into tb_category_brand(category_id,brand_id) values(#{cid},#{bid})")
    int insertCategoryBrand(@Param("bid") Long id, @Param("cid") Long cid);

    @Select("select b.* from tb_brand b join tb_category_brand cb on b.id = cb.brand_id where cb.category_id = #{cid}")
    List<Brand> findListByCid(@Param(("cid")) Long cid);
}
