package com.nzgreens.console.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Lazy(value = false)
public class SpringUtils implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        SpringUtils.context = context;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    public static <T> T getBean(Class<T> beanType) {
        return context.getBean(beanType);
    }

    /**
     * 根据通配符资源路径获取资源列表。
     *
     * @param wildcardResourcePaths 通配符资源路径
     * @return 返回资源列表。
     */
    public static List<Resource> getResourcesByWildcard(
            String... wildcardResourcePaths) {
        List<Resource> resources = new ArrayList<Resource>();
        try {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            for (String basename : wildcardResourcePaths) {
                for (Resource resource : resourcePatternResolver
                        .getResources(basename)) {
                    resources.add(resource);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("获取资源列表时反生异常。", e);
        }
        return resources;
    }
}
