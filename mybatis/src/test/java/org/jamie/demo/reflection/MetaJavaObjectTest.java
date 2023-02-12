package org.jamie.demo.reflection;

import org.jamie.demo.entity.User;
import org.jamie.mybatis.reflection.MetaJavaObject;
import org.junit.Test;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description Java 元对象 测试类
 * @date 2023/02/12 03:05
 */
public class MetaJavaObjectTest {

    @Test
    public void ConstructorTest() {
        User user = new User();
        MetaJavaObject<User> userMetaJavaObject = new MetaJavaObject<>(user);
        System.out.println(userMetaJavaObject.containSetterMethod("id"));
    }

    @Test
    public void shouldContainSetterMethod() {
        MetaJavaObject<User> userMetaJavaObject = new MetaJavaObject<>(User.class);
        System.out.println(userMetaJavaObject.containSetterMethod("id"));
        System.out.println(userMetaJavaObject.containSetterMethod("name"));
        System.out.println(userMetaJavaObject.containSetterMethod("age"));
    }

}
