package com.watermelonhit.admin.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.watermelonhit.admin.dao.Admin;
import com.watermelonhit.admin.dao.Permission;
import com.watermelonhit.admin.mapper.PermissionMapper;
import com.watermelonhit.admin.service.AdminPermissionService;
import com.watermelonhit.admin.service.PermissionService;
import com.watermelonhit.admin.util.AdminThreadLocal;
import com.watermelonhit.admin.vo.PageResult;
import com.watermelonhit.admin.vo.Result;
import com.watermelonhit.admin.vo.params.PageParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/28
 */
@Service
public class PermissionServiceImp implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private AdminPermissionService adminPermissionService;
    @Override
    public Result listPermission(PageParam pageParam) {
        Admin admin = AdminThreadLocal.get();
        Long adminId = admin.getId();
        Page<Permission> permissionPage = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        IPage<Permission>permissionIPage=permissionMapper.listPermission(permissionPage,pageParam.getQueryString(),adminId);
/*        String queryString = pageParam.getQueryString();
        LambdaQueryWrapper<Permission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(queryString)){
            lambdaQueryWrapper.eq(Permission::getName,queryString);
       }
       Page<Permission> PermissionList = permissionMapper.selectPage(permissionIPage);
*/
        List<Permission> records = permissionIPage.getRecords();
        PageResult<Permission> permissionPageResult = new PageResult<>();
       permissionPageResult.setList(records);
       permissionPageResult.setTotal(permissionIPage.getTotal());
        //防止内存泄露
        AdminThreadLocal.remove();
        return Result.success(permissionPageResult);
    }

    @Override
    public Result add(Permission permission) {
        Admin admin = AdminThreadLocal.get();
        Long adminId = admin.getId();
        permissionMapper.insert(permission);
        Long permissionId = permission.getId();
        adminPermissionService.insert(adminId,permissionId);
        AdminThreadLocal.remove();
        return Result.success(null);
    }

    @Override
    public Result update(Permission permission) {
        permissionMapper.updateById(permission);
        return Result.success(null);
    }

    @Override
    public Result delete(Long id) {
        Admin admin = AdminThreadLocal.get();
        Long adminId = admin.getId();
        permissionMapper.deleteById(id);
        return Result.success(null);
    }
}
