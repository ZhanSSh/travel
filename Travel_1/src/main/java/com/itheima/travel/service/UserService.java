package com.itheima.travel.service;

import com.itheima.travel.dao.UserDao;
import com.itheima.travel.entity.User;
import com.itheima.travel.exception.CustomerErrorMsgException;
import com.itheima.travel.utils.Md5Utils;

public class UserService {

    private UserDao userDao = new UserDao();

    /**
     注册
     */
    public int register(User user) {
        //注册前要对密码进行加密
        String password = user.getPassword();  //得到原来的密码
        //加密以后再放回到user对象中
        String md5 = Md5Utils.getMd5(password);
        user.setPassword(md5);

        //调用DAO方法写入到数据库
        return userDao.addUser(user);
    }

    /**
     判断用户是否存在
     @return 如果存在就返回true
     */
    public boolean isUserExists(String username) {
        //通过用户名查询，如果不为空就表示用户已经存在
        return userDao.findUserByName(username) != null;
    }

    /**
     实现登录，如果有异常表示登录失败
     */
    public User login(String username, String password) throws CustomerErrorMsgException {
        //1.判断用户名是否存在
        User user = userDao.findUserByName(username);

        //2.不存在就抛出自定义异常
        if (user == null) {
            throw new CustomerErrorMsgException("用户名不存在");
        }

        //3.存在就判断密码是否正确
        String pwd = user.getPassword();
        //对用户提交密码使用md5加密
        String md5 = Md5Utils.getMd5(password);
        if (!pwd.equals(md5)) {
            //4.如果密码不正确，抛出自定义异常
            throw new CustomerErrorMsgException("密码错误");
        }

        //5.如果密码正确，表示登录成功，返回user对象
        return user;
    }

}
