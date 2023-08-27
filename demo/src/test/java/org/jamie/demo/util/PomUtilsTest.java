package org.jamie.demo.util;

import org.junit.Test;

/**
 * @author jamie
 * @version 1.0.0
 * @description TODO
 * @date 2023/7/12 16:21
 */
public class PomUtilsTest {

    @Test
    public void getPomPropertiesTest() {
        System.out.println(PomUtils.getPomProperties("spring-cloud.version"));
    }

    @Test
    public void getPomBaseAttrTest() {
        PomUtils.getPomBaseAttr().forEach((k, v) -> {
            System.out.println(k + "=" + v);
        });
    }
}
