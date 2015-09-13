package com.haogrgr.test.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.haogrgr.test.exception.BizException;
import com.haogrgr.test.model.AjaxResult;

@ControllerAdvice("com.haogrgr.test.controller")
public class ControllerExceptionHandler implements ResponseBodyAdvice<Object> {

	private static Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	private static final String DEFAULT_ERROR_VIEW = "common/error";

	@ExceptionHandler(BizException.class)
	public ModelAndView handlerBizException(HandlerMethod methde, HttpServletRequest req, BizException e) {
		logger.error("", e);

		ModelAndView mav = new ModelAndView();
		mav.addObject("type", "业务异常");
		mav.addObject("message", e.getMsg());

		//ajaxResult
		mav.addObject("succ", AjaxResult.FAIL_BIZ);
		mav.addObject("info", e.getMsg());

		setView(req, mav);

		return mav;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) {
		logger.error("", e);

		ModelAndView mav = new ModelAndView();
		mav.addObject("type", "系统异常");
		mav.addObject("message", "服务器内部错误");

		//ajaxResult
		mav.addObject("succ", AjaxResult.FAIL_SYS);
		mav.addObject("info", "系统异常");

		setView(req, mav);

		return mav;
	}

	/**
	 * TODO:这里只是判断了ajax,没有判断是不是需要返回json,可以通过accept头来判断
	 * 判断是否为ajax请求
	 */
	private boolean isAjaxRequest(HttpServletRequest req) {
		return "XMLHttpRequest".equalsIgnoreCase(req.getHeader("X-Requested-With"));
	}

	/**
	 * 判断是否为ajax请求
	 */
	private void setView(HttpServletRequest req, ModelAndView mav) {
		if (isAjaxRequest(req)) {
			mav.setView(new MappingJackson2JsonView()); //{type:"业务异常", message:"e.getMsg()"}
		} else {
			mav.setViewName(DEFAULT_ERROR_VIEW);
		}
	}

	@Override
	public boolean supports(MethodParameter returnType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		//返回true表示拦截,返回false表示不拦截
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType,
			MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		//这里可以获取到ResponseBody注解方法的结果.可以在结果被转换为json或其他视图前处理,如日志
		System.out.println(body);
		return body;
	}

}
