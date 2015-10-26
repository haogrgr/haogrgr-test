package com.haogrgr.test.mvc;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 使用方法,
 * 1.注册PrefixMapMethodArgumentResolver到<mvc:argument-resolvers>中
 * 2.Controller参数类型为MapWapper且打上本注解 eg:(@PrefixMapParam("prop") MapWapper prop)
 * 3.前台表单字段名使用 PrefixMapParam.value() + PrefixMapParam.split() + 参数名  eg: (prop.param1.param2) ==> MapWapper{param1=MapWapper{param2:value}}
 * 
 * Spring使用这个方法来设置@PathVariable
 * org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping.handleMatch(RequestMappingInfo, String, HttpServletRequest)
 * 
 * TODO: MapWapper{param1=MapWapper{param2:value}} 改为   MapWapper{param1=HashMap{param2:value}}, 并提供MapWapper.toHashMap()
 */
public class PrefixMapMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		PrefixMapParam ann = parameter.getParameterAnnotation(PrefixMapParam.class);
		return ann != null && StringUtils.hasText(ann.value()) && MapWapper.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
			throws Exception {
		
		PrefixMapParam ann = parameter.getParameterAnnotation(PrefixMapParam.class);
		String prefix = ann.value() + ann.split();
		
		MapWapper resultMap = new MapWapper();
		Map<String, String[]> parameterMap = webRequest.getParameterMap();
		for (Entry<String, String[]> entity : parameterMap.entrySet()) {
			if(entity.getKey().startsWith(prefix)){
				String key = getKey(entity.getKey(), prefix);
				if(key.indexOf(ann.split()) == -1){
					resultMap.put(key, entity.getValue().length == 1 ? entity.getValue()[0] : entity.getValue());
					continue;
				}
				
				String tempkey = key;
				MapWapper tempmap = resultMap;
				while(tempkey.indexOf(ann.split()) != -1){
					String newkey = tempkey.substring(0, tempkey.indexOf(ann.split()));
					MapWapper param = (MapWapper) tempmap.get(newkey);
					if(param == null){
						param = new MapWapper();
						tempmap.put(newkey, param);
					}
					
					if(tempkey.indexOf(ann.split()) != -1){//还有.
						tempkey = getKey(tempkey, newkey + ".");
						tempmap = param;
					}
				}
				tempmap.put(tempkey, entity.getValue().length == 1 ? entity.getValue()[0] : entity.getValue());
			}
		}
		
		return resultMap;
	}
	
	private String getKey(String src, String prefix){
		return src.substring(prefix.length(), src.length());
	}
	
}
