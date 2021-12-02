package com.watermelonhit.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.watermelonhit.admin.dao.Permission;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/28
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    public IPage<Permission> listPermission(Page<Permission> permissionPage, String queryString, Long adminId);

}
