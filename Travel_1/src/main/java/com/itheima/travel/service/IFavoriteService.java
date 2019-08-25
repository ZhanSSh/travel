package com.itheima.travel.service;

import com.itheima.travel.entity.Favorite;
import com.itheima.travel.entity.PageBean;
import com.itheima.travel.entity.User;

/**
 收藏，业务层接口
 */
public interface IFavoriteService {

    /**
     通过rid和uid判断用户是否收藏了这条线路
     @param rid 线路id
     @param uid 用户的id
     @return true 已经收藏
     */
    boolean isFavoriteByRidAndUserId(int rid, int uid);

    /**
     添加收藏
     */
    void addFavorite(int rid, User user);

    /**
     封装PageBean中所有的属性
     */
    PageBean<Favorite> getPageBean(int uid, int current);

}
