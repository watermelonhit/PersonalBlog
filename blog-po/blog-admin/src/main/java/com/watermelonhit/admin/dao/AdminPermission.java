package com.watermelonhit.admin.dao;

import lombok.Data;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/28
 */
@Data
public class AdminPermission {
    private Long id;
    private Long adminId;
    private Long permissionId;
}
