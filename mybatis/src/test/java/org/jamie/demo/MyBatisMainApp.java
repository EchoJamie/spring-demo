package org.jamie.demo;

import org.jamie.demo.entity.User;
import org.jamie.demo.mapper.UserMapper;
import org.jamie.mybatis.proxy.MapperProxyFactory;

import java.util.List;

public class MyBatisMainApp {
    public static void main(String[] args) {
        UserMapper userMapper = MapperProxyFactory.getMapper(UserMapper.class);
        List<User> users = userMapper.selectByNameAndAge("Jamie", 20);
        System.out.println("main: " + users);
        if (users != null) {
            users.forEach(System.out::println);
        }
        System.out.println("======================================");
        User user = userMapper.selectById("100001");
        System.out.println(user);
        System.out.println("======================================");
        List<User> userList = userMapper.selectAll();
        System.out.println("selectAll: " + userList);
        if (userList != null) {
            userList.forEach(System.out::println);
        }
    }
}