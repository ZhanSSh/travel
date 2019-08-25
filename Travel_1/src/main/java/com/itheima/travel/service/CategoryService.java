package com.itheima.travel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.travel.dao.CategoryDao;
import com.itheima.travel.entity.Category;
import com.itheima.travel.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/*
分类业务层
 */
public class CategoryService {

    private CategoryDao categoryDao = new CategoryDao();

    /**
     查询所有分类，返回JSON字符串
     */
    public String findAllCategory() {
        //1. 从redis中得到数据
        Jedis jedis = JedisUtils.getJedis();
        String categories = jedis.get("categories");

        //2. 如果redis中没有，从mysql从得到数据
        if (categories == null) {
            List<Category> categoryList = categoryDao.findAllCategory();
            //3. 将数据转成JSON放入redis
            try {
                categories = new ObjectMapper().writeValueAsString(categoryList);
                jedis.set("categories", categories);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        //4. 关闭redis，返回json字符串
        jedis.close();
        return categories;
    }

}
