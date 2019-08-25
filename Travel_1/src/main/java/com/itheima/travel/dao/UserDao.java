package com.itheima.travel.dao;

import com.itheima.travel.entity.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class UserDao extends BaseDao {

    /**
     添加一个用户
     */
    public int addUser(User user) {
        return template.update("insert into tab_user values(null,?,?,?,?,?,?,?)",
                user.getUsername(), user.getPassword(), user.getName(),user.getBirthday(),
                user.getSex(),user.getTelephone(),user.getEmail());
    }

    /**
     通过用户名查询指定用户是否存在
     */
    public User findUserByName(String username) {
        try {
            return template.queryForObject("select * from tab_user where username=?", new BeanPropertyRowMapper<>(User.class),
                    username);
        } catch (DataAccessException e) {
            return null;  //如果不存在，返回null
        }
    }
}
