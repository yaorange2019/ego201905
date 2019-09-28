package com.ego.item.service;

import com.ego.common.pojo.PageResult;
import com.ego.item.mapper.BrandMapper;
import com.ego.item.pojo.Brand;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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
@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;


    public PageResult<Brand> page(Integer pageNo,
                                  Integer pageSize,
                                  String sortBy,
                                  Boolean descending,
                                  String key) {


        PageHelper.startPage(pageNo, pageSize);


        Example example = new Example(Brand.class);
        //关键字模糊查询
        if(StringUtils.isNotBlank(key))
        {
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("name","%"+key+"%");
        }
        //排序
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy +" "+(descending?"desc":"asc"));
        }

        Page<Brand> pageInfo = (Page<Brand>) brandMapper.selectByExample(example);

        return new PageResult<>(pageInfo.getTotal(),pageInfo.getResult());
    }

    @Transactional
    public void save(Brand brand, Long[] cids) {
        //保存品牌
        brandMapper.insert(brand);
        //保存中间表(维护关系)
        if(cids!=null)
        {
            for (Long cid : cids) {
                brandMapper.insertCategoryBrand(brand.getId(), cid);
            }
        }
    }

    public List<Brand> findListByCid(Long cid) {
        return  brandMapper.findListByCid(cid);
    }
}
