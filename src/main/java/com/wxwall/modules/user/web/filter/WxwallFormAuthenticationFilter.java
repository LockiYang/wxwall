package com.wxwall.modules.user.web.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.wxwall.common.utils.PathUtils;
import com.wxwall.modules.user.entity.User;
import com.wxwall.modules.user.utils.UserUtils;
import com.wxwall.modules.wechat.entity.WeChatApp;

/**
 * 基于表单认证的拦截器
 * 
 * @author locki
 * @date 2015年2月26日
 *
 */
public class WxwallFormAuthenticationFilter extends FormAuthenticationFilter {

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,
			Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {
		issueSuccessRedirect(request, response);

		// we handled the success redirect directly, prevent the chain from
		// continuing:
		return false;
	}
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) {
		if (super.isAccessAllowed(request, response, mappedValue)) {
			boolean pathsMatch = pathsMatch("/user/profile/**", request);
			if (!pathsMatch) {
				String webRootUrl = PathUtils.getWebRootUrl((HttpServletRequest) request);
				User user = UserUtils.getUser();
				if (user.needComplete()) {
					try {
						
						WebUtils.issueRedirect(request, response, webRootUrl + "/user/profile/1");
						return true;
					} catch (IOException e) {
						//TODO
						e.printStackTrace();
					}
				} else {
					WeChatApp weChatApp = user.getWeChatApp();
					if (weChatApp == null || weChatApp.needComplete()) {
						try {
							WebUtils.issueRedirect(request, response, webRootUrl + "/user/profile/2");
							return true;
						} catch (IOException e) {
							//TODO
							e.printStackTrace();
						}
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

}
