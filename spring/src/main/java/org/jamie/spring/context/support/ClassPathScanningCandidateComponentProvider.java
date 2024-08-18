package org.jamie.spring.context.support;

import org.jamie.spring.annotation.Component;
import org.jamie.spring.bean.BeanDefinition;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 06:04
 */
public class ClassPathScanningCandidateComponentProvider {

    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        HashSet<BeanDefinition> candidates = new LinkedHashSet<>();
        String basePackagePath = basePackage.replace(".", "/");
        URL resource = getClassLoader().getResource(basePackagePath);
        File basePackageFile = new File(resource.getFile());
        if (basePackageFile.isDirectory()) {
            candidates.addAll(scanFromDirectory(basePackageFile));
        }
        return candidates;
    }

    private Set<BeanDefinition> scanFromDirectory(File dir) {
        HashSet<BeanDefinition> candidates = new LinkedHashSet<>();
        for (File file : dir.listFiles()) {
            String fileName = file.getAbsolutePath();
            // 判断是java class文件
            if (fileName.endsWith(".class")) {
                String className = fileName.substring(fileName.indexOf("org"), fileName.indexOf(".class")).replace("/", ".");
                try {
                    Class<?> clazz = getClassLoader().loadClass(className);
                    if (clazz.isAnnotationPresent(Component.class)) {
                        candidates.add(new BeanDefinition(clazz));
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (file.isDirectory()) {
                candidates.addAll(scanFromDirectory(file));
            }
        }
        return candidates;
    }

    private ClassLoader getClassLoader() {
        return ClassPathScanningCandidateComponentProvider.class.getClassLoader();
    }
}
