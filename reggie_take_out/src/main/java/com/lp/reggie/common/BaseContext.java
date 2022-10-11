package com.lp.reggie.common;
/*
* 基于ThreadLocal 封装的工具类,用户保存和封装当前用户的id
* */
public class BaseContext {
    private static final ThreadLocal<Long> threadLocal= new ThreadLocal<>();

    /*
    * 返回值
    * @return id
    * */
    public static long getCurRentId(){
        return threadLocal.get();
    }

    /*
    * 设置值
    * @param id
    * */
    public  static void setCurRentId(long id){
        threadLocal.set(id);
    }
}
