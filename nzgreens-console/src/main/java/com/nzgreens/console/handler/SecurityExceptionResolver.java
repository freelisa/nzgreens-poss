package com.nzgreens.console.handler;

import com.nzgreens.common.model.ResultModel;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fei
 * @version V1.0
 */
public class SecurityExceptionResolver extends CommonExceptionResolver {
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
											  HttpServletResponse response, Object handler, Exception exception) {
		//如果抛出没有登录异常 跳转到登录
		if (handler instanceof HandlerMethod) {
			String mv = null;
			int statusCode = 0;
			//判断是否为权限相关的异常
			boolean flg = false;
			if(exception instanceof UnauthenticatedException ) { //未登录
				flg = true;
				mv = "redirect:/no-login";
				statusCode = HttpStatus.UNAUTHORIZED.value();
			}else if(exception instanceof UnauthorizedException) {  //请求无权限
				flg = true;
				mv = "redirect:/to-index";
				statusCode = HttpStatus.FORBIDDEN.value();
			}
			if(true == flg) {
				if (!this.isAjaxRequest(request)) {
					return new ModelAndView(mv);
				} else {
					ResultModel<String> resultModel = new ResultModel<>();
					resultModel.setSuccess(false);
					resultModel.setErrorCode(statusCode + "");
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json; charset=utf-8");
					response.setStatus(HttpStatus.OK.value());
					this.processAjaxReturnJson(response, resultModel);
				}
			}
		}
		return super.doResolveException(request, response, handler, exception);
	}

	@Override
	protected void logException(Exception ex, HttpServletRequest request) {
		if (ex instanceof UnauthenticatedException) {
			return;
		}
		super.logException(ex, request);
	}

	@Override
	protected void processCustomExceptions(HttpServletRequest request, ModelAndView mav, Exception exception) {
		super.processCustomExceptions(request,mav, exception);
		if (exception instanceof UnauthenticatedException) {
		}
		if (exception instanceof UnauthorizedException) {
		}
	}
}
