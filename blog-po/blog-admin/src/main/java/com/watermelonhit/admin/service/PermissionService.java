package com.watermelonhit.admin.service;

import com.watermelonhit.admin.dao.Permission;
import com.watermelonhit.admin.vo.Result;
import com.watermelonhit.admin.vo.params.PageParam;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/28
 */
public interface PermissionService {
    Result listPermission(PageParam pageParam);

    Result add(Permission permission);

    Result update(Permission permission);

    Result delete(Long id);
}
