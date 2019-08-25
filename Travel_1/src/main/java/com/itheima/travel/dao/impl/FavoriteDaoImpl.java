package com.itheima.travel.dao.impl;

import com.itheima.travel.dao.BaseDao;
import com.itheima.travel.dao.IFavoriteDao;
import com.itheima.travel.entity.Favorite;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;
import java.util.Map;

/**
 收藏DAO的实现类 */
public class FavoriteDaoImpl extends BaseDao implements IFavoriteDao {
    @Override
    public Favorite findFavoriteByRidAndUserId(int rid, int uid) {
        try {
            return template.queryForObject("select * from tab_favorite where uid=? and rid=?",
                    new BeanPropertyRowMapper<>(Favorite.class), uid, rid);
        } catch (DataAccessException e) {
            //e.printStackTrace();
            return null;
        }
    }

    /**
     向收藏表中添加1条记录@param favorite
     */
    @Override
    public int addFavorite(Favorite favorite) {
        //第一个是rid,第二个是uid
        return template.update("insert into tab_favorite values(?, now(), ?)",
                favorite.getRoute().getRid(), favorite.getUser().getUid());
    }

    /**
     更新收藏的次数@param rid
     */
    @Override
    public int updateRouteFavoriteNum(int rid) {
        return template.update("update tab_route set count=count+1 where rid=?", rid);
    }

    /**
     查询某个用户所有收藏线路的条数@param uid
     */
    @Override
    public int getCount(int uid) {
        return template.queryForObject("select count(*) from tab_favorite  where uid=?", int.class, uid);
    }

    /**
     查询某个用户所有收藏的线路一页数据@param uid 用户的主键
     @param current 当前第几页
     @param size 每页大小
     */
    @Override
    public List<Map<String, Object>> findFavoriteListByPage(int uid, int current, int size) {
        //返回多条记录，每个元素是Map，使用这个方法
        return template.queryForList("select * from tab_favorite f inner join tab_route r on r.rid = f.rid where uid=? order by f.date desc limit ?,?",
                uid, (current - 1) * size, size);
    }
}
