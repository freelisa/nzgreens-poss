package com.nzgreens.console.config;


import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

/**
 * FreeMarker配置组件。
 */
@Component("com.bkcloud.console.config.FreeMarkerSettings")
public class FreeMarkerSettings extends AbstractFreeMarkerSettings {
    /**
     * 构造方法。
     */
    public FreeMarkerSettings() {
		addAutoInclude("/macros/security.ftl");
		addAutoImport("sec", "/macros/sec.ftl");
		addAutoImport("s", "/macros/mvc.ftl");
        addStaticClass(SecurityUtils.class);
        addStaticClass(StringUtils.class);
        addEnumPackage("com.nzgreens.common.enums");
    }
}
