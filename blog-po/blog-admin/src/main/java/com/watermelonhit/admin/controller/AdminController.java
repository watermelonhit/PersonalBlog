package com.watermelonhit.admin.controller;

import com.watermelonhit.admin.dao.Permission;
import com.watermelonhit.admin.service.PermissionService;
import com.watermelonhit.admin.vo.Result;
import com.watermelonhit.admin.vo.params.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/28
 */
@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private PermissionService permissionService;

    /**
     * 首页权限列表显示
     * @param pageParam
     * @return
     */
    @PostMapping("permission/permissionList")
    public Result listPermission(@RequestBody PageParam pageParam){
     return permissionService.listPermission(pageParam);
    }

    /**
     * 完善：为关联表添加关系
     * @param permission
     * @return
     */
    @PostMapping("permission/add")
    public Result add(@RequestBody Permission permission){
        return permissionService.add(permission);
    }

    @PostMapping("permission/update")
    public Result update(@RequestBody Permission permission){
        return permissionService.update(permission);
    }

    @GetMapping("permission/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        return permissionService.delete(id);
    }
}
