package com.itheima.travel.service.impl;

import com.itheima.travel.dao.IFavoriteDao;
import com.itheima.travel.dao.impl.FavoriteDaoImpl;
import com.itheima.travel.entity.Favorite;
import com.itheima.travel.entity.PageBean;
import com.itheima.travel.entity.Route;
import com.itheima.travel.entity.User;
import com.itheima.travel.service.IFavoriteService;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoriteServiceImpl implements IFavoriteService {

    //依赖于dao
    private IFavoriteDao favoriteDao = new FavoriteDaoImpl();

    /**
     通过rid和uid判断用户是否收藏了这条线路
     @param rid 线路id
     @param uid 用户的id
     @return true 已经收藏
     */
    @Override
    public boolean isFavoriteByRidAndUserId(int rid, int uid) {
        return favoriteDao.findFavoriteByRidAndUserId(rid, uid) != null;
    }

    /**
     添加收藏@param rid
     @param user
     */
    @Override
    public void addFavorite(int rid, User user) {
        Favorite favorite = new Favorite();

        //创建Route对象和User对象
        Route route = new Route();
        route.setRid(rid);

        //设置favorite属性
        favorite.setRoute(route);
        favorite.setUser(user);

        //1.调用DAO向收藏表中添加一条记录
        favoriteDao.addFavorite(favorite);

        //2.更新线路表中count
        favoriteDao.updateRouteFavoriteNum(rid);
    }

    /**
     封装PageBean中所有的属性@param uid
     @param current
     */
    @Override
    public PageBean<Favorite> getPageBean(int uid, int current) {
        PageBean<Favorite> pageBean = new PageBean<>();
        pageBean.setSize(4);
        pageBean.setCurrent(current);

        //将List<Map>重新封装成List<Favorite>

        //1. 创建List<Favorite>对象
        List<Favorite> favorites = new ArrayList<>();

        //2. 遍历List<Map>集合，将每个Map封装成Favorite和Route
        List<Map<String, Object>> mapList = favoriteDao.findFavoriteListByPage(uid, current, pageBean.getSize());
        for (Map<String, Object> map : mapList) {
            Favorite favorite = new Favorite();
            Route route = new Route();
            try {
                BeanUtils.populate(favorite, map);
                BeanUtils.populate(route, map);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //3. 将Route赋值到Favorite
            favorite.setRoute(route);
            //4. 将Favorite添加到List<Favorite>集合中
            favorites.add(favorite);
        }

        //将封装好的List，再赋值给PageBean的data属性
        pageBean.setData(favorites);

        //设置总条数
        int count = favoriteDao.getCount(uid);
        pageBean.setCount(count);

        return pageBean;
    }
}
