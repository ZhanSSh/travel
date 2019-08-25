package com.itheima.travel.entity;

import java.util.List;

/**
 分页对象
 @param <T> 这一页封装的对象类型 */
public class PageBean<T> {

    //属性数据从数据库中查询
    private List<T> data; //这一页数据
    private int count; //总条数

    //值由客户端页面提供
    private int current; //当前页
    private int size; //每页大小

    //由其他的属性计算出来的
    private int total; //总页数
    private int first; //首页
    private int previous; //上页
    private int next; //下页

    //注：所有的计算方法都写在get方法中，调用get方法
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    //计算总页面
    public int getTotal() {
        //如果总条数能够整除页大小，正好是这么多页，如果不能整除，页数加1
        /*
        if (getCount() % getSize() == 0) {
            return getCount() / getSize();
        }
        else {
            return getCount() / getSize() + 1;
        }
        */
        return getCount() % getSize() == 0 ? getCount() / getSize() : getCount() / getSize() + 1;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    //第一页
    public int getFirst() {
        return 1;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    //得到上一页
    public int getPrevious() {
        //当前页减1
        if (getCurrent() > 1) {
            return getCurrent() - 1;
        }
        else {
            return 1;
        }
    }

    public void setPrevious(int previous) {
        this.previous = previous;
    }

    //得到下一页
    public int getNext() {
        //如果当前页小于最后一页
        if (getCurrent() < getTotal()) {
            return getCurrent() + 1;
        }
        else {
            return getTotal();  //返回最后一页
        }
    }

    public void setNext(int next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "data=" + getData() +
                ", count=" + getCount() +
                ", current=" + getCurrent() +
                ", size=" + getSize() +
                ", total=" + getTotal() +
                ", first=" + getFirst() +
                ", previous=" + getPrevious() +
                ", next=" + getNext() +
                '}';
    }
}
