package com.wxwall.modules.wechat.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 线上DEMO
 *
 * @author Locki<lockiyang@qq.com>
 * @since 2015年3月18日
 *
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

	@RequestMapping(method = RequestMethod.GET)
	public String demo() {
		return "redirect:/";
	}
}
