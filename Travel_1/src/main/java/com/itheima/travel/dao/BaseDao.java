package com.itheima.travel.dao;

import com.itheima.travel.utils.DataSourceUtils;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 所有DAO类父类
 可以把一些公共的代码放在父类，子类继承就可以使用
 */
public class BaseDao {

    //得到模板对象
    protected JdbcTemplate template = new JdbcTemplate(DataSourceUtils.getDataSource());

}
