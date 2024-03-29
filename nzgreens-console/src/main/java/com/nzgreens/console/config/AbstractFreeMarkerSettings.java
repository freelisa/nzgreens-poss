package com.nzgreens.console.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FreeMarker设置抽象类。
 */
public abstract class AbstractFreeMarkerSettings implements Comparable<AbstractFreeMarkerSettings> {
  /** 加载序号 */
  private Integer order = 0;
  /** 枚举包列表 */
  private List<String> enumPackages = new ArrayList<String>();
  /** 静态类列表 */
  private List<Class<?>> staticClasses = new ArrayList<Class<?>>();
  /** 模版路径列表 */
  private List<String> templatePaths = new ArrayList<String>();
  /** 自动包含文件列表 */
  private List<String> autoIncludes = new ArrayList<String>();
  /** 自动导入宏列表 */
  private Map<String, String> autoImports = new HashMap<String, String>();
  /** 全局组件变量列表 */
  private Map<String, String> globalBeans = new HashMap<String, String>();

  /**
   * 添加枚举包名。
   * 
   * @param enumPackage 枚举包名
   */
  public void addEnumPackage(String enumPackage) {
    enumPackages.add(enumPackage);
  }

  /**
   * 添加静态类名。
   * 
   * @param staticClass 静态类名
   */
  public void addStaticClass(Class<?> staticClass) {
    staticClasses.add(staticClass);
  }

  /**
   * 添加模版路径。
   * 
   * @param templatePath 模版路径
   */
  public void addTemplatePath(String templatePath) {
    templatePaths.add(templatePath);
  }

  /**
   * 添加自动包含文件。
   * 
   * @param autoInclude 自动包含文件
   */
  public void addAutoInclude(String autoInclude) {
    autoIncludes.add(autoInclude);
  }

  /**
   * 添加自动导入宏。
   * 
   * @param alias 宏别名
   * @param templateName 模版名
   */
  public void addAutoImport(String alias, String templateName) {
    autoImports.put(alias, templateName);
  }

  /**
   * 添加全局组件变量。
   * 
   * @param varName 变量名称
   * @param beanName 组件名称
   */
  public void addGlobalBean(String varName, String beanName) {
    globalBeans.put(varName, beanName);
  }

  @Override
  public int compareTo(AbstractFreeMarkerSettings other) {
    return other.getOrder() - order;
  }

  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }

  public List<String> getEnumPackages() {
    return enumPackages;
  }

  public List<Class<?>> getStaticClasses() {
    return staticClasses;
  }

  public List<String> getTemplatePaths() {
    return templatePaths;
  }

  public List<String> getAutoIncludes() {
    return autoIncludes;
  }

  public Map<String, String> getAutoImports() {
    return autoImports;
  }

  public Map<String, String> getGlobalBeans() {
    return globalBeans;
  }
}
