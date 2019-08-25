package com.itheima.travel.service;

import com.itheima.travel.dao.RouteDao;
import com.itheima.travel.entity.*;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;
import java.util.Map;

/**
 线路的业务层
 */
public class RouteService {

    private RouteDao routeDao = new RouteDao();

    /**
     查询某个分类，首页精选线路
     */
    public List<Route> findMostFavoriteRouteByCid(int cid) {
        return routeDao.findMostFavoriteRouteByCid(cid);
    }

    /**
     查询一页数据，封装成PageBean
     @param cid 分类id
     @param current 当前页
     @param rname 模糊查询线路名
     */
    public PageBean<Route> getPageBean(int cid, int current,String rname) {
        //创建PageBean对象
        PageBean<Route> pageBean = new PageBean<>();

        //封装current和size属性
        pageBean.setCurrent(current);
        pageBean.setSize(3);  //每页3条

        //封装数据库中查询出来的2个属性
        int count = routeDao.getCountByCid(cid, rname);  //总条数
        List<Route> routes = routeDao.getRoutesByPage(cid, current, pageBean.getSize(), rname);
        pageBean.setCount(count);
        pageBean.setData(routes);

        //其它的属性是计算出来的
        //返回pageBean对象
        return pageBean;
    }

    /**
     将DAO中2个方法得到的所有数据，封装成一个Route对象
     */
    public Route findRouteByRid(int rid) {
        //1.调用DAO，得到三张表连接，得到Map对象
        Map<String, Object> map = routeDao.findRouteByRid(rid);

        //2.创建线路，分类，商家三个对象
        Route route = new Route();
        Category category = new Category();
        Seller seller = new Seller();

        //3.将Map数据复制给线路，分类，商家. ctrl+alt+t
        try {
            BeanUtils.populate(route,map);
            BeanUtils.populate(category,map);
            BeanUtils.populate(seller,map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //4.将分类和商家的对象赋值给线路对象
        route.setCategory(category);
        route.setSeller(seller);

        //5.调用DAO中另一个方法，得到这条线路的图片，赋值给线路属性
        List<RouteImg> routeImgList = routeDao.findRouteImgsByRid(rid);
        route.setRouteImgList(routeImgList);

        //6.返回Route对象
        return route;
    }

    /**
     查询收藏排行榜，封装成PageBean对象
     */
    public PageBean<Route> getPageBeanByFavoriteRank(int current, Map<String,String> condition) {
        //1.创建PageBean对象
        PageBean<Route> pageBean = new PageBean<>();

        //2.设置四个属性
        pageBean.setSize(8);
        //当前页，页面大小，一页数据，总记录数
        pageBean.setCurrent(current);

        List<Route> routes = routeDao.getRoutesFavoriteRankByPage(current, pageBean.getSize(),condition);
        pageBean.setData(routes);

        int count = routeDao.getCountByFavoriteRank(condition);
        pageBean.setCount(count);

        //3.返回PageBean对象
        return pageBean;
    }
}
