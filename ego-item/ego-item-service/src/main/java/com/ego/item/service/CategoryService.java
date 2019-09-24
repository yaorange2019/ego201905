package com.ego.item.service;

import com.ego.item.mapper.CategoryMapper;
import com.ego.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> findListByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    @Transactional
    public void save(Category category) {
        categoryMapper.insertSelective(category);
    }
    @Transactional
    public void updateName(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        categoryMapper.updateByPrimaryKeySelective(category);
    }
    @Transactional
    public void deleteById(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
