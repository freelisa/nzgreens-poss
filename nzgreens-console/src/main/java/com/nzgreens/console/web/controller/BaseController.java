package com.nzgreens.console.web.controller;

import com.nzgreens.common.exception.CommonException;
import com.nzgreens.console.service.IAdminUserService;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * @author fei
 * @version V1.0
 */
@Controller
public class BaseController {
	@Resource
	protected MessageSource messageSource;
	@Resource
	private IAdminUserService adminUserService;

	/**
	 * 抛出异常
	 *
	 * @param code
	 * @param args
	 * @throws CommonException
	 */
	protected void thrown(String code, String... args) throws CommonException {
		throw new CommonException(code, messageSource.getMessage(code, args, Locale.SIMPLIFIED_CHINESE));
	}

	/**
	 * 抛出异常
	 *
	 * @param code
	 * @param args
	 * @throws CommonException
	 */
	protected void thrownConsole(String code, String... args) throws CommonException {
		throw new CommonException(code, messageSource.getMessage(code, args, Locale.SIMPLIFIED_CHINESE));
	}

}
