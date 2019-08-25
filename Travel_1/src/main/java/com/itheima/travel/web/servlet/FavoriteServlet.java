package com.itheima.travel.web.servlet;

import com.itheima.travel.entity.Favorite;
import com.itheima.travel.entity.PageBean;
import com.itheima.travel.entity.Route;
import com.itheima.travel.entity.User;
import com.itheima.travel.service.IFavoriteService;
import com.itheima.travel.service.RouteService;
import com.itheima.travel.service.impl.FavoriteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "FavoriteServlet", urlPatterns = "/favorite")
public class FavoriteServlet extends BaseServlet {

    private IFavoriteService favoriteService = new FavoriteServiceImpl();
    private RouteService routeService = new RouteService();

    //添加收藏
    private void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        //1.得到提交rid
        int rid = Integer.parseInt(request.getParameter("rid"));

        //2.从会话域中得到User对象
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        //3.如果User对象为空，打印false，结束访问
        if (user == null) {
            out.print(false);   //判断是否为false
        } else {
            //4.不为空，调用业务层添加收藏
            favoriteService.addFavorite(rid, user);

            //5.查询route表中count的值，打印给浏览器
            Route route = routeService.findRouteByRid(rid);
            out.print(route.getCount());  //收藏数量
        }
    }

    /*
      查询某个用户一页收藏线路
     */
    private void findFavoriteByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        //1.得到当前是第几页
        int current = Integer.parseInt(request.getParameter("current"));

        //从会话域中得到uid，判断用户是否为空，如果为空表示会话过期。打印false给浏览器
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            out.print(false);
        } else {
            //得到uid
            int uid = user.getUid();
            //2.调用业务层得到PageBean对象
            PageBean<Favorite> pageBean = favoriteService.getPageBean(uid, current);
            //3.转成JSON打印
            String json = mapper.writeValueAsString(pageBean);
            out.print(json);
        }
    }
}
