package com.ego.item.service;

import com.ego.item.mapper.SpecificationMapper;
import com.ego.item.pojo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/9/27
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Service
public class SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    public Specification findByCid(Long cid) {
        return specificationMapper.selectByPrimaryKey(cid);
    }

    @Transactional
    public void save(Specification specification) {
        specificationMapper.insertSelective(specification);
    }
}
