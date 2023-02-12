package org.jamie.demo;

import org.jamie.demo.entity.User;
import org.jamie.demo.mapper.UserMapper;
import org.jamie.mybatis.configuration.Configuration;
import org.jamie.mybatis.configuration.MyBatisConfiguration;
import org.jamie.mybatis.io.Resources;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.util.List;
import java.util.Properties;

public class MyBatisMainApp {

    private Properties properties = new Properties();
    public static final String resourcePath = "application.properties";

    @Before
    public void init() {

        try (Reader reader = Resources.getResourceAsReader(resourcePath)) {
            properties.load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void main() {
        // 方式一
        Configuration configuration = new MyBatisConfiguration(resourcePath);
        // 方式二
        //Reader reader = Resources.getResourceAsReader(resourcePath);
        //Configuration configuration = new MyBatisConfiguration(reader);
        // 方式三
        //Configuration configuration = new MyBatisConfiguration(properties);

        UserMapper userMapper = configuration.getMapperProxyFactory(UserMapper.class).getMapper();

        // 查询 及数据输出
        {
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
}