package com.itheima.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.travel.entity.User;
import com.itheima.travel.exception.CustomerErrorMsgException;
import com.itheima.travel.service.UserService;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 用于处理用户模块 */
@WebServlet(name = "UserServlet", urlPatterns = "/user")
public class UserServlet extends BaseServlet {

    private UserService userService = new UserService();

    /**
     判断用户登录状态
     */
    private void getLoginUserData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.得到会话域
        HttpSession session = request.getSession();

        //2.判断会话域中是否有用户信息
        User user = (User) session.getAttribute("user");

        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        //3.如果有得到用户对象，转成JSON打印给浏览器
        if (user != null) {
            String json = new ObjectMapper().writeValueAsString(user);
            out.print(json);
        } else {
            //4.如果没有打印false给浏览器
            out.print(false);
        }
    }

    /**
     注册的方法
     */
    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();

        //1.判断验证码是否正确
        //从会话域中得到验证码
        HttpSession session = request.getSession();
        String vcode = (String) session.getAttribute("vcode");
        //只用一次
        session.removeAttribute("vcode");
        //得到用户提交的验证码
        String check = request.getParameter("check");
        if (check.equalsIgnoreCase(vcode)) {  //相等
            //2.判断用户名是否存在
            String username = request.getParameter("username");
            if (userService.isUserExists(username)) {  //用户存在
                out.print("用户名已经存在");
            } else {
                //3.上面验证都通过，调用业务层将用户写到数据库
                User user = new User();
                Map<String, String[]> map = request.getParameterMap();
                try {
                    BeanUtils.populate(user, map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //注册
                int row = userService.register(user);
                //4.注册成功，返回success字符串
                if (row > 0) {
                    out.print("success");
                } else {
                    throw new RuntimeException("注册失败");
                }
            }

        } else { //不相等
            out.print("验证码错误");
        }
    }

    /**
     登录
     */
    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();

        //1.判断验证码是否正确
        HttpSession session = request.getSession();
        String vcode = (String) session.getAttribute("vcode");
        session.removeAttribute("vcode");

        //得到用户提交的验证码
        String check = request.getParameter("check");
        if (!check.equalsIgnoreCase(vcode)) {
            out.print("验证码错误");
        } else {
            //2.判断用户名和密码是否正确
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            try {
                //4.如果正确就将用户信息保存在会话域中，跳转到index.html
                User user = userService.login(username, password);
                session.setAttribute("user", user);
                out.print("success");
            } catch (CustomerErrorMsgException e) {
                //3.如果不正确就将错误信息打印到浏览器，表示登录失败
                out.print(e.getMessage());
            }

        }

    }

    /**
     退出
     */
    private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //让会话域过期
        request.getSession().invalidate();
        //重定向到login.html页面
        response.sendRedirect("login.html");
    }
}
