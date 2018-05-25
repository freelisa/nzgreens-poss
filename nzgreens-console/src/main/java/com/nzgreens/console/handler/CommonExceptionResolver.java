package com.nzgreens.console.handler;

import com.alibaba.fastjson.JSONObject;
import com.nzgreens.common.exception.CommonException;
import com.nzgreens.common.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 公共异常处理
 *
 * @author fei
 * @version V1.0
 */
public class CommonExceptionResolver extends GenericExceptionResolver {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonExceptionResolver.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
											  HttpServletResponse response, Object handler, Exception exception) {
		ModelAndView mav = super.doResolveException(request, response, handler,
				exception);
		//上传ajax 无法识别，加入请求判断
		if (!isAjaxRequest(request) && !request.getRequestURI().contains("file-upload")) {
			response.setStatus(HttpStatus.OK.value());
			mav.setViewName("error");
			processCustomExceptions(request, mav, exception);
		} else {
			processAjaxRequestException(response, request, exception);
		}
		return mav;
	}

	/**
	 * 判断客户端请求是否是Ajax请求。
	 *
	 * @param request 请求对象
	 * @return 如果客户端请求是Ajax请求返回true，否则返回false。
	 */
	protected Boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equalsIgnoreCase(request
				.getHeader("X-Requested-With"));
	}

	/**
	 * 处理自定义异常。
	 *
	 * @param mav       数据模型
	 * @param exception 异常
	 */
	protected void processCustomExceptions(HttpServletRequest request, ModelAndView mav, Exception exception) {
		LOGGER.error("{} error params:{}", request.getRequestURI(), request.getParameterMap(), exception);
		if (exception instanceof CommonException) {
			mav.addObject("errorInfo", exception.getMessage());
		}
	}

	/**
	 * 处理ajax请求
	 *
	 * @param response
	 * @param request
	 * @param exception
	 */
	protected void processAjaxRequestException(HttpServletResponse response, HttpServletRequest request, Exception exception) {
		LOGGER.error("{} error params:{}", request.getRequestURI(), request.getParameterMap(), exception);
		ResultModel<String> resultModel = new ResultModel<>();
		resultModel.setSuccess(false);
		resultModel.setErrorCode("500");
		resultModel.setErrorInfo("系统异常");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		response.setStatus(HttpStatus.OK.value());
		if (exception instanceof CommonException) {
			CommonException commonException = (CommonException) exception;
			resultModel.setErrorCode(commonException.getErrorCode());
			resultModel.setErrorInfo(commonException.getMessage());
		}
		processAjaxReturnJson(response, resultModel);
	}

	/**
	 * 处理ajax请求
	 *
	 * @param response
	 */
	protected void processAjaxReturnJson(HttpServletResponse response, ResultModel<String> resultModel) {
		PrintWriter printWriter = null;
		try {
			printWriter = response.getWriter();
			printWriter.append(JSONObject.toJSONString(resultModel));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
	}
}
