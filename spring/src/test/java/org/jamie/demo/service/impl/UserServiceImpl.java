package org.jamie.demo.service.impl;

import org.jamie.demo.service.OrderService;
import org.jamie.demo.service.UserService;
import org.jamie.spring.annotation.Component;
import org.jamie.spring.bean.BeanNameAware;
import org.jamie.spring.bean.InitializingBean;
import org.jamie.spring.bean.annotation.Autowired;
import org.jamie.spring.bean.annotation.Scope;

import static org.jamie.spring.bean.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;
import static org.jamie.spring.bean.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

/**
 * @author jamie
 * @version 1.0.0
 * @description User业务类 实现
 * @date 2022/12/29 22:54
 */
@Component("userService")
// @Scope(SCOPE_PROTOTYPE)
@Scope
public class UserServiceImpl implements BeanNameAware, InitializingBean, UserService {

    @Autowired
    private OrderService orderService;
    private String xmlProperty;

    private String beanName;

    private String initStr;

    @Override
    public void test() {
        System.out.println("this: " + this);
        System.out.println("beanName: " + beanName);
        System.out.println("orderService: " + orderService);
        System.out.println("initStr: " + initStr);
        System.out.println("xmlProperty: " + xmlProperty);
    }

    public void setInitStr(String initStr) {
        this.initStr = initStr;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化....");
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setXmlProperty(String xmlProperty) {
        this.xmlProperty = xmlProperty;
    }
}
