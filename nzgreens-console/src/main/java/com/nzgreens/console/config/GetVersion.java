package com.nzgreens.console.config;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.List;

/**
 * 静态资源版本号
 */
public class GetVersion implements TemplateMethodModelEx {

	@Value("${static.dir:}")
	private String staticDir;

	@Override
	public Object exec(List arguments) throws TemplateModelException {
		SimpleScalar fileName = (SimpleScalar) arguments.get(0);
		long v = getVersion(fileName.getAsString());
		if(fileName.getAsString().contains("?")) {
			return fileName + "&v=" + v;
		}
		return fileName + "?v=" + v;
	}

	private long getVersion(String fileName) {
		try {
			File file = new File(staticDir + "/" + fileName);
			return  file.lastModified();
		} catch (Exception e) {
			return 1;
		}
	}
}
