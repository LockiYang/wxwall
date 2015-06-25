package com.wxwall.modules.user.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.wxwall.common.utils.Encodes;
import com.wxwall.modules.user.entity.User;
import com.wxwall.modules.user.utils.UserUtils;

/**
 * Shiro数据库Realm
 * @author Locki
 * @date 2015年1月9日
 *
 */
public class ShiroDbRealm extends AuthorizingRealm {

	protected UserService userService;

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
//		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		
		ShiroPrincipal principal = (ShiroPrincipal) getAvailablePrincipal(principals);
		User user = userService.get(principal.getLoginName());
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			//权限生成
//			info.addStringPermission("");
			info.addRole(user.getUserType());
			return info;
		} else {
			return null;
		}
	}

	/**
	 * 认证回调函数
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user = userService.get(token.getUsername());
		
		//认证
		if (user != null) {
			byte[] salt = Encodes.decodeHex(user.getSalt());
			
			ShiroPrincipal principal = new ShiroPrincipal(user);
			
			/*
			 * SimpleAuthenticationInfo(principal,salt,realmName)
			 * 保存身份(principals,用户名)和证书(credentials,密码)的实体
			 */
			
			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, user.getPassword(),
					ByteSource.Util.bytes(salt), getName());
			
//			UserUtils.putCache(UserUtils.CACHE_USER, user);
			return authenticationInfo;
		} else {
			return null;
		}
	}

	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	@PostConstruct
	private void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(
				UserService.HASH_ALGORITHM);
		matcher.setHashIterations(UserService.HASH_INTERATIONS);

		setCredentialsMatcher(matcher);
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroPrincipal implements Serializable {
		private static final long serialVersionUID = 1L;
		private Long id;
		private String loginName;
		private String userName;
		private Map<String, Object> cache;

		public ShiroPrincipal(User user) {
			this.id = user.getId();
			this.loginName = user.getLoginEmail();
			this.userName = user.getUserName();
		}

		public String getUserName() {
			return userName;
		}

		public Long getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}
		
		public Map<String, Object> getCache() {
			if (cache==null){
				cache = new HashMap<String, Object>();
			}
			return cache;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			if (StringUtils.isBlank(userName)) {
				return loginName;
			}
			return userName;
		}

		/**
		 * 重载equals,只计算loginMail;
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			ShiroPrincipal other = (ShiroPrincipal) obj;
			if (loginName == null) {
				if (other.loginName != null) {
					return false;
				}
			} else if (!loginName.equals(other.loginName)) {
				return false;
			}
			return true;
		}
	}
}
