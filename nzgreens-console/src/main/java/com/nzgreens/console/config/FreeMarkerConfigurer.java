package com.nzgreens.console.config;

import com.nzgreens.common.enums.IEnum;
import com.nzgreens.common.utils.ClassUtils;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * 自定义FreeMarker配置管理。
 */
public class FreeMarkerConfigurer
        extends org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
        implements ApplicationContextAware {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private ServletContext servletContext;
    private ApplicationContext context;
    private List<AbstractFreeMarkerSettings> settings = new ArrayList<AbstractFreeMarkerSettings>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
        Map<String, AbstractFreeMarkerSettings> freemarkerSettingsMap =
                context.getBeansOfType(AbstractFreeMarkerSettings.class);
        settings.addAll(freemarkerSettingsMap.values());
        Collections.sort(settings);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        super.setServletContext(servletContext);
        this.servletContext = servletContext;
    }

    @Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet();
        initStatics();
        initSharedVariables();
        initAutoIncludes();
        initAutoImports();
        initEnums();
    }

    @Override
    protected void postProcessTemplateLoaders(List<TemplateLoader> templateLoaders) {
        super.postProcessTemplateLoaders(templateLoaders);
        for (String templatePath : getTemplatePaths()) {
            templateLoaders.add(getTemplateLoaderForPath(templatePath));
            log.debug("加载模版路径[{}]。", templatePath);
        }
    }

    @Override
    protected TemplateLoader getTemplateLoaderForPath(String templateLoaderPath) {
        if (isPreferFileSystemAccess()) {
            try {
                Resource path = getResourceLoader().getResource(templateLoaderPath);
                File file = path.getFile();
                return new FileTemplateLoader(file);
            } catch (IOException ex) {
                return new SpringTemplateLoader(getResourceLoader(), templateLoaderPath);
            }
        } else {
            return new SpringTemplateLoader(getResourceLoader(), templateLoaderPath);
        }
    }

    /**
     * 初始化静态变量。
     *
     * @throws TemplateModelException 初始化静态变量失败时抛出异常。
     */
    protected void initStatics() throws TemplateModelException {
        TemplateHashModel statics =
                ((BeansWrapper) getConfiguration().getObjectWrapper()).getStaticModels();
        getConfiguration().setSharedVariable("statics", statics);
        for (Class<?> staticClass : getStaticClasses()) {
            getConfiguration().setSharedVariable(staticClass.getSimpleName(),
                    statics.get(staticClass.getName()));
            log.debug("初始化静态变量[{}:{}]。", staticClass.getSimpleName(), staticClass.getName());
        }
    }

    /**
     * 初始化公共变量。
     *
     * @throws TemplateModelException 初始化公共变量失败时抛出异常。
     */
    protected void initSharedVariables() throws TemplateModelException {
        for (Entry<String, String> globalBean : getGlobalBeans().entrySet()) {
            getConfiguration().setSharedVariable(globalBean.getKey(),
                    context.getBean(globalBean.getValue()));
        }
    }

    /**
     * 初始化自动包含文件。
     */
    protected void initAutoIncludes() {
        for (String autoInclude : getAutoIncludes()) {
            getConfiguration().addAutoInclude(autoInclude);
        }
    }

    /**
     * 初始化自动导入文件。
     */
    protected void initAutoImports() {
        for (Entry<String, String> autoImport : getAutoImports().entrySet()) {
            getConfiguration().addAutoImport(autoImport.getKey(), autoImport.getValue());
        }
    }

    /**
     * 初始化枚举变量。
     *
     * @throws TemplateModelException 初始化枚举变量失败时抛出异常
     */
    protected void initEnums() throws TemplateModelException {
        TemplateHashModel enums =
                ((BeansWrapper) getConfiguration().getObjectWrapper()).getEnumModels();
        getConfiguration().setSharedVariable("enums", enums);
        for (Class<?> enumClass : ClassUtils.findClassesByParentClass(IEnum.class, getEnumPackages())) {
            getConfiguration().setSharedVariable(enumClass.getSimpleName(),
                    enums.get(enumClass.getName()));
            log.debug("初始化枚举变量[{}:{}]。", enumClass.getSimpleName(), enumClass.getName());
        }
    }

    /**
     * 获取枚举包列表。
     *
     * @return 返回枚举包列表。
     */
    private String[] getEnumPackages() {
        List<String> enumPackages = new ArrayList<String>();
        for (AbstractFreeMarkerSettings freeMarkerSettings : settings) {
            for (String enumPackage : freeMarkerSettings.getEnumPackages()) {
                if (enumPackages.contains(enumPackage)) {
                    throw new RuntimeException("加载枚举类包[" + enumPackage + "]时发生冲突。");
                }
                enumPackages.add(enumPackage);
            }
        }
        return enumPackages.toArray(new String[]{});
    }

    /**
     * 获取静态类列表。
     *
     * @return 返回静态类列表。
     */
    private List<Class<?>> getStaticClasses() {
        List<Class<?>> staticClasses = new ArrayList<Class<?>>();
        List<String> staticClassNames = new ArrayList<String>();
        for (AbstractFreeMarkerSettings freeMarkerSettings : settings) {
            for (Class<?> staticClass : freeMarkerSettings.getStaticClasses()) {
                if (staticClassNames.contains(staticClass.getSimpleName())) {
                    throw new RuntimeException("加载静态类[" + staticClass + "]时发生冲突。");
                }
                staticClasses.add(staticClass);
                staticClassNames.add(staticClass.getSimpleName());
            }
        }
        return staticClasses;
    }

    /**
     * 获取模版路径列表。
     *
     * @return 返回模版路径列表。
     */
    private List<String> getTemplatePaths() {
        List<String> templatePaths = new ArrayList<String>();
        for (AbstractFreeMarkerSettings freeMarkerSettings : settings) {
            for (String templatePath : freeMarkerSettings.getTemplatePaths()) {
                if (templatePaths.contains(templatePath)) {
                    throw new RuntimeException("加载模版路径[" + templatePath + "]时发生冲突。");
                }
                templatePaths.add(templatePath);
            }
        }
        return templatePaths;
    }

    /**
     * 获取自动包含文件列表。
     *
     * @return 返回自动包含文件列表。
     */
    private List<String> getAutoIncludes() {
        List<String> autoIncludes = new ArrayList<String>();
        for (AbstractFreeMarkerSettings freeMarkerSettings : settings) {
            for (String autoInclude : freeMarkerSettings.getAutoIncludes()) {
                if (autoIncludes.contains(autoInclude)) {
                    throw new RuntimeException("加载自动包含文件[" + autoInclude + "]时发生冲突。");
                }
                autoIncludes.add(autoInclude);
            }
        }
        return autoIncludes;
    }

    /**
     * 获取自动导入宏列表。
     *
     * @return 返回自动导入宏列表。
     */
    private Map<String, String> getAutoImports() {
        Map<String, String> autoImports = new HashMap<String, String>();
        for (AbstractFreeMarkerSettings freeMarkerSettings : settings) {
            for (Entry<String, String> autoImport : freeMarkerSettings.getAutoImports().entrySet()) {
                if (autoImports.containsKey(autoImport.getKey())) {
                    throw new RuntimeException(
                            "加载自动导入文件[" + autoImport.getKey() + ":" + autoImport.getValue() + "]时发生冲突。");
                }
                autoImports.put(autoImport.getKey(), autoImport.getValue());
            }
        }
        return autoImports;
    }

    /**
     * 获取全局组件变量列表。
     *
     * @return 返回全局组件变量列表。
     */
    private Map<String, String> getGlobalBeans() {
        Map<String, String> globalBeans = new HashMap<String, String>();
        for (AbstractFreeMarkerSettings freeMarkerSettings : settings) {
            for (Entry<String, String> globalBean : freeMarkerSettings.getGlobalBeans().entrySet()) {
                if (globalBeans.containsKey(globalBean.getKey())) {
                    throw new RuntimeException(
                            "加载全局组件变量[" + globalBean.getKey() + ":" + globalBean.getValue() + "]时发生冲突。");
                }
                globalBeans.put(globalBean.getKey(), globalBean.getValue());
            }
        }
        return globalBeans;
    }
}
