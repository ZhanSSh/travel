package com.itheima.travel.dao;

import com.itheima.travel.entity.Route;
import com.itheima.travel.entity.RouteImg;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 线路数据访问层 */
public class RouteDao extends BaseDao {

    /**
     显示国内游(某个分类)，收藏人数最多的6条
     */
    public List<Route> findMostFavoriteRouteByCid(int cid) {
        return template.query("select * from tab_route where cid=? order by count desc limit 0,6",
                new BeanPropertyRowMapper<>(Route.class), cid);
    }

    /**
     查询某个分类总线路条数
     */
    public int getCountByCid(int cid, String rname) {
        //参数：SQL语句，返回类型，占位符值
        return template.queryForObject("select count(*) from tab_route where cid=? and rname like ?",
                int.class, cid, "%" + rname + "%");
    }

    /**
     查询某个分类下线路一页数据
     @param cid 分类外键
     @param current 当前页
     @param size 每页大小
     */
    public List<Route> getRoutesByPage(int cid, int current, int size, String rname) {
        //计算每一页的起始行数
        int begin = (current - 1) * size;
        return template.query("select * from tab_route where cid=? and rname like ? limit ?,?",
                new BeanPropertyRowMapper<>(Route.class), cid, "%" + rname + "%", begin, size);
    }

    /**
     查询1条线路的详细信息，三张表连接查询
     一条记录封装成一个Map，键是：字段名，值是：记录的值
     */
    public Map<String, Object> findRouteByRid(int rid) {
        //参数1：SQL语句，参数2：占位符值。如果查询不到记录，也会抛出异常。
        try {
            return template.queryForMap("select * from tab_category c inner join tab_route r on c.cid = r.cid inner join tab_seller s on r.sid = s.sid where rid=?", rid);
        } catch (DataAccessException e) {
            e.printStackTrace(); //输出异常
            return null;
        }
    }

    /**
     查询这条线路的对应的图片
     */
    public List<RouteImg> findRouteImgsByRid(int rid) {
        return template.query("select * from tab_route_img where rid=?", new BeanPropertyRowMapper<>(RouteImg.class), rid);
    }

    /**
     查询总记录数
     */
    public int getCountByFavoriteRank(Map<String,String> condition) {
        //创建一个集合，封装占位符的值
        List<Object> list = new ArrayList<>();

        //创建SQL语句
        StringBuilder sql = new StringBuilder("select count(*) from tab_route where 1=1 ");
        //判断查询条件：如果这个键不为空，而且也不等于空字符串，就把它做为查询的条件
        String rname = condition.get("rname");
        //不为空，而且也不等于空字符串。模糊查询线路名称
        if (rname!=null && !"".equals(rname.trim())) {
            sql.append(" and rname like ?");
            list.add("%" + rname + "%");  //向集合中添加了一个真实的值
        }
       /* //最小值
        String startPrice = condition.get("startPrice");
        if (startPrice!=null && !"".equals(startPrice.trim())) {
            sql.append(" and price >=?");
            list.add(startPrice);
        }
        //最大值
        String endPrice = condition.get("endPrice");
        if (endPrice!=null && !"".equals(endPrice.trim())) {
            sql.append(" and price <=?");
            list.add(endPrice);
        }*/

        //转成数组
        Object[] args = list.toArray();

        System.out.println(sql);
        System.out.println(list);

        return template.queryForObject(sql.toString(), int.class, args);
    }

    /**
     收藏排行榜降序排序，显示一页数据
     @param current 当前页
     @param size 页面大小
     @param condition 封装三个查询条件
     rname = "双飞4天", startPrice="2000", endPrice="4000"
     */
    public List<Route> getRoutesFavoriteRankByPage(int current, int size, Map<String,String> condition) {
        //创建一个集合，封装占位符的值
        List<Object> list = new ArrayList<>();

        //创建SQL语句
        StringBuilder sql = new StringBuilder("select * from tab_route where 1=1 ");
        //判断查询条件：如果这个键不为空，而且也不等于空字符串，就把它做为查询的条件
        String rname = condition.get("rname");
        //不为空，而且也不等于空字符串。模糊查询线路名称
        if (rname!=null && !"".equals(rname.trim())) {
            sql.append(" and rname like ?");
            list.add("%" + rname + "%");  //向集合中添加了一个真实的值
        }
       /* //最小值
        String startPrice = condition.get("startPrice");
        if (startPrice!=null && !"".equals(startPrice.trim())) {
            sql.append(" and price >=?");
            list.add(startPrice);
        }
        //最大值
        String endPrice = condition.get("endPrice");
        if (endPrice!=null && !"".equals(endPrice.trim())) {
            sql.append(" and price <=?");
            list.add(endPrice);
        }*/
        //分页功能加上
        sql.append(" order by count desc limit ?,?");
        list.add((current - 1) * size);
        list.add(size);


        //将一个集合转成Object数组
        Object[] args = list.toArray();
        System.out.println(sql);
        System.out.println(list);
        //将StringBuilder转成String。占位符传入一个Object[]数组
        return template.query(sql.toString(),new BeanPropertyRowMapper<>(Route.class), args);
    }
}
