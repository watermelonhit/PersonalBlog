package com.watermelonhit.admin.util;

import com.watermelonhit.admin.dao.Admin;
import org.springframework.stereotype.Component;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/28
 */
public class AdminThreadLocal {
    private static final ThreadLocal<Admin> threadLocal=new ThreadLocal<>();
    private AdminThreadLocal(){}
    public static Admin get(){
        return threadLocal.get();
    }
    public static  void remove(){
        threadLocal.remove();
    }
    public static void  set(Admin admin){
        threadLocal.set(admin);
    }
}
