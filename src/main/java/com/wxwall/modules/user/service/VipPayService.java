package com.wxwall.modules.user.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wxwall.common.service.ServiceException;
import com.wxwall.common.utils.DateUtils;
import com.wxwall.modules.user.dao.UserDao;
import com.wxwall.modules.user.dao.VipPackageDao;
import com.wxwall.modules.user.entity.User;
import com.wxwall.modules.user.entity.VipPackage;
import com.wxwall.modules.user.utils.UserUtils;

/**
 * 付费购买的服务层
 *
 * @author Locki<lockiyang@qq.com>
 * @since 2015年2月28日
 *
 */

@Service
@Transactional
public class VipPayService {
	
	@Autowired private VipPackageDao vipPackageDao;
	@Autowired private UserDao userDao;
	
	public List<VipPackage> getAllVipPackage() {
		
		return (List<VipPackage>) vipPackageDao.findAll();
		
	}
	
	/**
	 * 购买套餐
	 * @param id
	 */
	public void buy(long id) {
		VipPackage one = vipPackageDao.findOne(id);
		User user = UserUtils.getUser();
		if (one != null) {
			if (one.getValidMouth() == 0) {
				user.setVipTimes(user.getVipTimes() +1);
			} else if (one.getValidMouth() > 0) {
				user.setUserType(User.VIP_USER);
				user.setVipStartDate(DateUtils.now());
				Date dateEnd = DateUtils.getDateEnd(DateUtils.addDays(DateUtils.now(), one.getValidMouth()*30));
				user.setVipEndDate(dateEnd);
			}
		} else {
			throw new ServiceException("系统不存在此套餐");
		}
		userDao.save(user);
		
	}

}
