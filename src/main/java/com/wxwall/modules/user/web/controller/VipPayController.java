package com.wxwall.modules.user.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wxwall.modules.user.entity.VipPackage;
import com.wxwall.modules.user.service.VipPayService;
import com.wxwall.modules.user.utils.UserUtils;

@Controller
@RequestMapping(value = "/vip")
public class VipPayController {

	@Autowired private VipPayService vipPayService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String  listAll(Model model) {
		List<VipPackage> allVipPackage = vipPayService.getAllVipPackage();
		model.addAttribute("packages", allVipPackage);
		model.addAttribute("user", UserUtils.getUser());
		return "vip-list";
	}
	
	@RequestMapping(value = "/buy/{id}", method = RequestMethod.GET)
	public String buy(@PathVariable("id") long id, Model model, RedirectAttributes ra) {
		
		try {
			vipPayService.buy(id);
		} catch (Exception e) {
			
		}
		ra.addFlashAttribute("infoMsg", "套餐购买成功.");
		
		return "redirect:/user/info";
	}
}
