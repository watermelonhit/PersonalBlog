package com.watermelonhit.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.watermelonhit.admin.dao.Admin;
import com.watermelonhit.admin.dao.Permission;
import com.watermelonhit.admin.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/28
 */
@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    public Admin getAdminByName(String name){
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername,name);
        queryWrapper.last("limit 1");
        Admin admin = adminMapper.selectOne(queryWrapper);
        return admin;
    }

    public List<Permission> getPermissionsByAdminId(Long id) {
        return adminMapper.getPermissionsByAdminId(id);
    }
}
