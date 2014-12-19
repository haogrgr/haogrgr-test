package com.haogrgr.test.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.haogrgr.test.exception.BizException;

@ControllerAdvice("com.haogrgr.test.controller")
public class ControllerExceptionHandler {

	private static Logger logger = Logger.getLogger(ControllerExceptionHandler.class);

	private static final String DEFAULT_ERROR_VIEW = "common/error";

	@ExceptionHandler(BizException.class)
	public ModelAndView handlerBizException(HttpServletRequest req, BizException e) {
		logger.error("", e);

		ModelAndView mav = new ModelAndView();
		mav.addObject("type", "业务异常");
		mav.addObject("message", e.getMsg());
		mav.setViewName(DEFAULT_ERROR_VIEW);
		//mav.setView(new MappingJackson2JsonView()); //{type:"业务异常", message:"e.getMsg()"}
		return mav;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) {
		logger.error("", e);

		ModelAndView mav = new ModelAndView();
		mav.addObject("type", "系统异常");
		mav.addObject("message", "服务器内部错误");
		mav.setViewName(DEFAULT_ERROR_VIEW);
		return mav;
	}

}
