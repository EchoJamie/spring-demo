package org.jamie.spring.bean.constant;

/**
 * @author jamie
 * @version 1.0.0
 * @description 域常量
 * @date 2022/12/29 23:56
 */
public enum ScopeConstant {

    SINGLETON("singleton"),
    PROTOTYPE("prototype")
    ;

    private String scopeStr;

    ScopeConstant(String scopeStr) {
        this.scopeStr = scopeStr;
    }

    public String getScopeStr() {
        return this.scopeStr;
    }
}
