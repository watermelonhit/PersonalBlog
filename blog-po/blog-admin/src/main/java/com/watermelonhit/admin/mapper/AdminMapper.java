package com.watermelonhit.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.watermelonhit.admin.dao.Admin;
import com.watermelonhit.admin.dao.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/28
 */
public interface AdminMapper extends BaseMapper<Admin> {
    @Select("SELECT * FROM ms_permission WHERE id IN (SELECT permission_id FROM ms_admin_permission WHERE admin_id=#{id});")
    List<Permission> getPermissionsByAdminId(Long id);
}
