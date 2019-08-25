package com.itheima.travel.dao;

import com.itheima.travel.entity.Favorite;

import java.util.List;
import java.util.Map;

/**
 收藏有关的DAO
 */
public interface IFavoriteDao {

    Favorite findFavoriteByRidAndUserId(int rid, int uid);

    /**
     向收藏表中添加1条记录
     */
    int addFavorite(Favorite favorite);

    /**
     更新收藏的次数
     */
    int updateRouteFavoriteNum(int rid);

    /**
     查询某个用户所有收藏线路的条数
     */
    int  getCount(int uid);

    /**
     查询某个用户所有收藏的线路一页数据
     @param uid 用户的主键
     @param current 当前第几页
     @param size 每页大小
     */
    List<Map<String, Object>> findFavoriteListByPage(int uid, int current, int size);
}
