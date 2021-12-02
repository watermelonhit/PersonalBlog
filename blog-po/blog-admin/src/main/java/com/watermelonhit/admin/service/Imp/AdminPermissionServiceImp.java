package com.watermelonhit.admin.service.Imp;

import com.watermelonhit.admin.dao.AdminPermission;
import com.watermelonhit.admin.mapper.AdminPermissionMapper;
import com.watermelonhit.admin.service.AdminPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/28
 */
@Service
public class AdminPermissionServiceImp implements AdminPermissionService {
    @Autowired
    private AdminPermissionMapper adminPermissionMapper;
    @Override
    public void insert(Long adminId, Long permissionId) {
        AdminPermission adminPermission = new AdminPermission();
        adminPermission.setPermissionId(permissionId);
        adminPermission.setAdminId(adminId);
        adminPermissionMapper.insert(adminPermission);
    }
}
