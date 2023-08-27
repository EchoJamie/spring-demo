package org.jamie.demo.lombok;

//import org.jamie.lombok.annotation.JamieGetter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description TODO
 * @date 2023/5/1 03:59
 */
//@JamieGetter
public class LombokTest {
    private String name;

    public void setName(String name) {
        this.name = name;
    }


    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("123");
        list.add(null);

        ParameterizedType aClass = (ParameterizedType) list.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = aClass.getActualTypeArguments();
        Class<?> actualTypeArgument = (Class<?>) actualTypeArguments[0];
        System.out.println(actualTypeArgument.toString());
    }
}
