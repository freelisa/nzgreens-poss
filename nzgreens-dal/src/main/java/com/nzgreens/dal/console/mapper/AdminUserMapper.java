package com.nzgreens.dal.console.mapper;

import com.nzgreens.common.form.console.AdminUserSearchForm;
import com.nzgreens.common.model.console.AdminUserSearchModel;
import com.nzgreens.dal.console.example.AdminUser;
import com.nzgreens.dal.console.example.AdminUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminUserMapper {
    int countByExample(AdminUserExample example);

    int deleteByExample(AdminUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    List<AdminUser> selectByExample(AdminUserExample example);

    AdminUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AdminUser record, @Param("example") AdminUserExample example);

    int updateByExample(@Param("record") AdminUser record, @Param("example") AdminUserExample example);

    int updateByPrimaryKeySelective(AdminUser record);

    int updateByPrimaryKey(AdminUser record);

    /**
     * 查询用户列表
     * @param adminUserSearchForm
     * @return
     */
    List<AdminUserSearchModel> selectList(AdminUserSearchForm adminUserSearchForm);
}