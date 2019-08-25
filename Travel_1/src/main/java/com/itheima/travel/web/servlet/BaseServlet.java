package com.itheima.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 所有Servlet的父类
 */
public class BaseServlet extends HttpServlet {

    protected ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //每次请求都多带一个参数：action，提示调用哪个方法
        String action = request.getParameter("action");
        /*
            switch (action) {
            case "register":
                register(request, response);
                break;
            case "login":
                login(request, response);
                break;
            case "getLoginUserData":
                getLoginUserData(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
        }*/

        //得到当前对象 this
        //得到类对象是一个Servlet的子类
        Class<? extends Servlet> clazz = this.getClass();

        try {
            //得到方法对象
            Method method = clazz.getDeclaredMethod(action, HttpServletRequest.class,HttpServletResponse.class);
            //暴力反射，调用方法，传递真实的参数
            method.setAccessible(true);
            System.out.println(this + " 调用了方法：" + method.getName());
            method.invoke(this, request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
