package com.wxwall.modules.user.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wxwall.common.service.BaseService;
import com.wxwall.common.service.ServiceException;
import com.wxwall.common.utils.DateUtils;
import com.wxwall.common.utils.Digests;
import com.wxwall.common.utils.Encodes;
import com.wxwall.modules.user.dao.UserDao;
import com.wxwall.modules.user.dao.UserInvitationCodeDao;
import com.wxwall.modules.user.entity.User;
import com.wxwall.modules.user.entity.UserInvitationCode;
import com.wxwall.modules.user.utils.UserUtils;
import com.wxwall.modules.wechat.dao.WeChatAppDao;

/**
 * 账户操作的服务类
 * 
 * @author Locki
 * @date 2014年8月7日
 * 
 */
@Service
@Transactional
public class UserService extends BaseService {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	private static final int DEFAULT_VIP_TIMES = 1; // 默认试用次数

	@Autowired private UserDao userDao;
	@Autowired private UserInvitationCodeDao userInvitationCodeDao;
	@Autowired private WeChatAppDao weChatAppDao;

	/**
	 * 查询方法
	 * 
	 * @return
	 */
	public List<User> getAll() {
		return (List<User>) userDao.findAll();
	}

	public User get(Long id) {
		return userDao.findOne(id);
	}

	public User getByUserName(String userName) {
		return userDao.findByUserName(userName);
	}

	public User saveUser(User user) {
		return userDao.save(user);
	}

	public User get(String loginEmail) {
		return userDao.findByLoginEmail(loginEmail);
	}

	/**
	 * 注册用户
	 * 
	 * @param user
	 * @param unUsedCode
	 */
	public void register(User user, String code) {
		
		UserInvitationCode byCode = userInvitationCodeDao.findByCode(code);
		if (byCode != null) {
			if (byCode.isValid()) {
				this.initUser(user);
				this.entryptPassword(user);
				byCode.setStatus(UserInvitationCode.Status.USED.toString());
				byCode.setUser(user);
				user.setTextPassword(user.getPlainPassword());
				userDao.save(user);
				userInvitationCodeDao.save(byCode);
			} else {
				throw new ServiceException("验证码不正确或已过期");
			}
		} else {
			throw new ServiceException("验证码不正确或已过期");
		}
	}

	/**
	 * 更新当前用户
	 * 
	 * @param user
	 */
	public User update(String userName, String linkman, String mobilePhone,
			String plainPassword) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(plainPassword)) {
			user.setPlainPassword(plainPassword);
			user.setTextPassword(plainPassword);
			entryptPassword(user);
		}
		if (StringUtils.isNotBlank(userName)) {
			user.setUserName(userName);
		}
		if (StringUtils.isNotBlank(mobilePhone)) {
			user.setMobilePhone(mobilePhone);
		}
		if (StringUtils.isNotBlank(linkman)) {
			user.setLinkman(linkman);
		}
		
		return userDao.save(user);
	}
	
	/**
	 * 取出Shiro中的当前用户LoginName.
	 */
//	private String getCurrentUserName() {
//		ShiroPrincipal user = (ShiroPrincipal) SecurityUtils.getSubject().getPrincipal();
//		return user.userName;
//	}

	/**
	 * 密码加密
	 */
	private void entryptPassword(User user) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(),
				salt, HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));
	}

	/**
	 * 注册时初始化用户
	 */
	private void initUser(User user) {
		user.setCreateDate(DateUtils.now());
		user.setUserType(User.FREE_USER);
		user.setVipTimes(DEFAULT_VIP_TIMES);
	}

}
