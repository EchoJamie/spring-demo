package org.jamie.mybatis.proxy;

import org.jamie.mybatis.annotation.Delete;
import org.jamie.mybatis.annotation.Insert;
import org.jamie.mybatis.annotation.Select;
import org.jamie.mybatis.annotation.Update;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 方法类型 枚举类
 * @date 2023/02/12 14:42
 */
public enum MethodType {

    /**
     * 查询
     */
    SELECT(Select.class),
    /**
     * 插入
     */
    INSERT(Insert.class),
    /**
     * 更新
     */
    UPDATE(Update.class),
    /**
     * 删除
     */
    DELETE(Delete.class),

    /**
     * 未知
     */
    UNKNOWN(null)
    ;

    private Class annotation;

    public void setAnnotation(Class annotation) {
        this.annotation = annotation;
    }

    public Class getAnnotation() {
        return annotation;
    }
    MethodType(Class annotation) {
        this.annotation = annotation;
    }
}
