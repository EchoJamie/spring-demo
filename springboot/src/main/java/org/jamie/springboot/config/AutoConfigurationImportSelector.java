package org.jamie.springboot.config;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author jamie
 * @version 1.0.0
 * @description 自动配置 导入选择器
 * @date 2023/01/01 01:51
 */
public class AutoConfigurationImportSelector implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        // SPI 技术 自动加载 META-INF/**/spring.factory
        return new String[0];
    }
}
