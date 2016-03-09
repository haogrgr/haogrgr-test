package com.haogrgr.test.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haogrgr.test.model.TestModel;

@Controller
public class JSR303Controller {

	@ResponseBody
	@RequestMapping("/303/group/select")
	public Object testGroup1(@Validated(TestModel.Select.class) TestModel model) {
		System.out.println(model);
		return model;
	}

	@ResponseBody
	@RequestMapping("/303/group/edit")
	public Object testGroup2(@Validated(TestModel.Edit.class) TestModel model) {
		System.out.println(model);
		return model;
	}

	@ResponseBody
	@RequestMapping("/303/group/")
	public Object testGroup3(@Validated TestModel model) {
		System.out.println(model);
		return model;
	}

}
