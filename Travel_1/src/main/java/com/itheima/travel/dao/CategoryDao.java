package com.itheima.travel.dao;

import com.itheima.travel.entity.Category;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

/**
 分类的DAO
 */
public class CategoryDao extends BaseDao {

    /**
     查询所有的分类
     */
    public List<Category> findAllCategory() {
        return template.query("select * from tab_category order by cid",new BeanPropertyRowMapper<>(Category.class));
    }
}
