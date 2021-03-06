package com.itheima.travel.entity;

import java.io.Serializable;
import java.sql.Date;

/**
 * 收藏实体类
 */
public class Favorite implements Serializable {
    private Date date; //收藏时间
    private Route route;//旅游线路对象
    private User user;//所属用户

    @Override
    public String toString() {
        return "Favorite{" +
                "date=" + date +
                ", route=" + route +
                ", user=" + user +
                '}';
    }

    /**
     * 无参构造方法
     */
    public Favorite() {
    }

    /**
     * 有参构造方法
     * @param route
     * @param date
     * @param user
     */
    public Favorite(Route route, Date date, User user) {
            this.route = route;
            this.date = date;
            this.user = user;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
