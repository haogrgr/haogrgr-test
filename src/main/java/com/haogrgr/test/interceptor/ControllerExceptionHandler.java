package com.haogrgr.test.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.haogrgr.test.exception.BizException;
import com.haogrgr.test.model.AjaxResult;

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

}
