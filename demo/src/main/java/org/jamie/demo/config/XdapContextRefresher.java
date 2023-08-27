package org.jamie.demo.config;

import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description TODO
 * @date 2023/4/17 18:32
 */
public class XdapContextRefresher extends ContextRefresher {
    public XdapContextRefresher(ConfigurableApplicationContext context, RefreshScope scope) {
        super(context, scope);
    }

    @Override
    public synchronized Set<String> refresh() {
        //Map<String, Object> before = extract(
        //        super.getContext().getEnvironment().getPropertySources());
        //super.addConfigFilesToEnvironment();
        //Set<String> keys = changes(before, extract(this.context.getEnvironment().getPropertySources())).keySet();
        //this.context.publishEvent(new EnvironmentChangeEvent(this.context, keys));
        //return keys;
        return new HashSet<>();
    }
}
