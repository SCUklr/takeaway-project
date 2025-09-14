package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    @Select("select * from user where openid = #{openid}")
    User getByOpenId(String openid);

    /**
     * 根据主键查询用户
     */
    @Select("select * from user where id = #{id}")
    User getById(Long id);

    /**
     * 插入数据
     * @param user
     */
    void insert(User user);

    /**
     * 根据条件动态统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

}
