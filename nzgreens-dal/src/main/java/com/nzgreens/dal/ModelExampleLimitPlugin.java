package com.nzgreens.dal;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

public class ModelExampleLimitPlugin extends PluginAdapter {
	public boolean validate(List<String> arg0) {
		return true;
	}

	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
											  IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType limitType = new FullyQualifiedJavaType(
				"Limit");
		topLevelClass.addImportedType(limitType);

		Field field = new Field();
		field.setName("limit");
		field.setType(limitType);
		field.setVisibility(JavaVisibility.PRIVATE);
		topLevelClass.addField(field);

		Method setMethod = new Method();
		setMethod.setName("setLimit");
		setMethod.setVisibility(JavaVisibility.PUBLIC);
		setMethod.addParameter(new Parameter(limitType, "limit"));
		setMethod.addBodyLine("this.limit = limit;");
		topLevelClass.addMethod(setMethod);

		Method getMethod = new Method();
		getMethod.setName("getLimit");
		getMethod.setVisibility(JavaVisibility.PUBLIC);
		getMethod.setReturnType(limitType);
		getMethod.addBodyLine("return this.limit;");
		topLevelClass.addMethod(getMethod);

		return true;
	}

	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {
		addLimitSqlMapCode(element);
		return true;
	}

	public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {
		List elementList = element.getElements();
		XmlElement orderByElement = (XmlElement) elementList.get(elementList
				.size() - 1);
		orderByElement.getElements().set(0,
				new TextElement("order by ${orderByClause}"));
		addLimitSqlMapCode(element);
		return true;
	}

	private void addLimitSqlMapCode(XmlElement element) {
		XmlElement limit = new XmlElement("if");
		limit.addAttribute(new Attribute("test", "limit != null"));
		limit.addElement(new TextElement(
				"limit #{limit.start},#{limit.maxRows}"));
		element.addElement(limit);
	}

	public static void generate() {
		String config = ModelExampleLimitPlugin.class.getClassLoader().getResource(
				"generatorConfig.xml").getFile();
		String[] arg = {"-configfile", config, "-overwrite"};
		ShellRunner.main(arg);
	}

	public static void main(String[] args) {
		generate();
	}
}
