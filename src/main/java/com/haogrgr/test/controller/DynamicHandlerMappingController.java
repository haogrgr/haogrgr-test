package com.haogrgr.test.controller;

import java.lang.reflect.Method;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.haogrgr.test.util.AppContextUtil;

/**
 * spring4.2公开了运行期添加HandlerMapping的方法
 * 
 * @author desheng.tu
 * @date 2015年8月3日 下午3:13:02
 *
 */
@Controller
public class DynamicHandlerMappingController {

	@ResponseBody
	@RequestMapping(value = "/mapping/add")
	public String addHandlerMappingDiy() throws Exception {
		RequestMappingHandlerMapping mapping = AppContextUtil.getBean(RequestMappingHandlerMapping.class);
		MappingJackson2JsonView jsonView = AppContextUtil.getBean(MappingJackson2JsonView.class);

		RequestMappingInfo info = RequestMappingInfo.paths("/mapping/add_1").methods(RequestMethod.GET).build();

		//这样就会报, Object不能被转换为HttpServletRequest, 因为Lambda生成的接口实例类方法签名是(Object)Object的, 然后里面在强制类型转换的.
		//所以Spring反射根据参数类型来注入参数时, 获取到的是Object类型
		Function<HttpServletRequest, ModelAndView> handler = req -> {
			ModelAndView mav = new ModelAndView();
			mav.addObject(req.getRequestURL());
			mav.setView(jsonView);
			return mav;
		};

		//匿名内部类就好
		handler = new Function<HttpServletRequest, ModelAndView>() {
			public ModelAndView apply(HttpServletRequest req) {
				ModelAndView mav = new ModelAndView();
				mav.addObject(req.getRequestURL());
				mav.setView(jsonView);
				return mav;
			}
		};

		Method method = handler.getClass().getMethod("apply", Object.class);

		mapping.registerMapping(info, handler, method);

		return "succ";
	}

}
