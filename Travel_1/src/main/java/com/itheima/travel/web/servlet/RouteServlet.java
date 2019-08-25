package com.itheima.travel.web.servlet;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
线路控制器
 */
@WebServlet(name = "RouteServlet", urlPatterns = "/route")
public class RouteServlet extends BaseServlet {

    private RouteService routeService = new RouteService();
    //收藏的业务层
    private IFavoriteService favoriteService = new FavoriteServiceImpl();

    //查询首页精选线路
    private void findMostFavoriteRoutesByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.得到cid的值
        int cid = Integer.parseInt(request.getParameter("cid"));

        //2.调用业务层查询6条线路
        List<Route> routeList = routeService.findMostFavoriteRouteByCid(cid);

        //3.转成JSON打印到浏览器
        String json = mapper.writeValueAsString(routeList);

        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(json);
    }

    //查询1页分页数据
    private void findRouteListByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.得到cid和current
        int cid = Integer.parseInt(request.getParameter("cid"));  //得到分类id
        int current = Integer.parseInt(request.getParameter("current"));  //得到当前第几页
        String rname = request.getParameter("rname");  //如果没有值，设置成空串
        if (rname == null) {
            rname = "";
        }

        //2.调用业务层得到PageBean对象
        PageBean<Route> pageBean = routeService.getPageBean(cid, current, rname);

        //3.将PageBean对象转成JSON打印
        String json = mapper.writeValueAsString(pageBean);

        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(json);
    }

    /*
    通过rid，查询route对象，显示详情
     */
    private void findRouteByRid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.得到rid值
        int rid = Integer.parseInt(request.getParameter("rid"));

        //2.调用业务层得到route对象
        Route route = routeService.findRouteByRid(rid);

        //3.转成JSON打印
        String json = mapper.writeValueAsString(route);

        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(json);
    }

    /**
     通过rid，判断当前用户是否收藏了这些线路
     */
    private void isFavoriteByRid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.得到rid
        int rid = Integer.parseInt(request.getParameter("rid"));

        //2.得到会话域中用户对象
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        //3.判断会话域中用户对象是否为空，如果为空，表示没有登录。打印false
        if (user == null) {
            out.print(false);
        }
        else {
            //4.如果不为空，得到uid
            int uid = user.getUid();
            //5.调用业务层代码，返回true或false，如果没有收藏返回false，否则返回true
            boolean favorite = favoriteService.isFavoriteByRidAndUserId(rid, uid);
            out.print(favorite);
        }
    }


    /**
    查询收藏排行榜1页数据
     */
    private void findRoutesFavoriteRank(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        //1.得到第几页
        int current = Integer.parseInt(request.getParameter("current"));
        //得到查询的三个条件
        String rname = request.getParameter("rname");
        String startPrice = request.getParameter("startPrice");
        String endPrice = request.getParameter("endPrice");
        Map<String,String> condition = new HashMap<>();
        condition.put("rname", rname);
        condition.put("startPrice", startPrice);
        condition.put("endPrice", endPrice);

        //2.调用业务层得到PageBean
        PageBean<Route> pageBean = routeService.getPageBeanByFavoriteRank(current, condition);

        //3.转成JSON打印
        String json = mapper.writeValueAsString(pageBean);
        out.print(json);
    }
}
