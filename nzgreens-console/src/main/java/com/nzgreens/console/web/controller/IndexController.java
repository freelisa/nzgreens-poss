package com.nzgreens.console.web.controller;

import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.IAdminUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author fei
 * @version V1.0
 */
@Controller
@RequestMapping
@Auth
public class IndexController extends BaseController {
	@Resource
	private IAdminUserService adminUserService;

	/**
	 * 查看主页。
	 *
	 * @param model 数据模型
	 */
	@RequestMapping("index")
	public void index(Model model) throws Exception{
		model.addAttribute("currentUser", adminUserService.getCurrentUser());
	}

	@RequestMapping("to-index")
	public String noLogin() {
		return "to_index";
	}

	@RequestMapping("welcome")
	public String toWelcome(Model model) {
		return "welcome";
	}

}
