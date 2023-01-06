package org.jamie.demo.mapper;

import org.jamie.demo.entity.User;
import org.jamie.mybatis.annotation.Param;
import org.jamie.mybatis.annotation.Select;

import java.util.List;

/**
 * @author jamie
 * @version 1.0.0
 * @description User SQL 映射
 * @date 2023/01/02 05:31
 */
public interface UserMapper {

    @Select("select * from user")
    public List<User> selectAll();

    @Select("select * from user where name = #{name} and age = #{age}")
    public List<User> selectByNameAndAge(@Param("name") String name, @Param("age") Integer age);

    @Select("select * from user where id = #{id}")
    public User selectById(@Param("id") String id);
}
