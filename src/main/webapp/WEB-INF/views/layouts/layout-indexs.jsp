<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator"%>

<!doctype html>
<html class="full-height">
<head>
<title><sitemesh:title /> - V动屏</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="shortcut icon" type="image/x-icon" href="${ctx}/statics/icons/favicon.ico">
<link rel="stylesheet" type="text/css" href="${ctx}/statics/css/reset.css?v=1.0">
<link rel="stylesheet" type="text/css" href="${ctx}/statics/css/common.css?v=1.0">
<link rel="stylesheet" type="text/css" href="${ctx}/statics/css/publish.css?v=1.0">
<link rel="stylesheet" type="text/css" href="${ctx}/statics/css/submit.css?v=1.0">
<link rel="stylesheet" type="text/css" href="${ctx}/statics/plugins/font-awesome-4.3.0/css/font-awesome.min.css?v=1.0">

<script type="text/javascript" src="${ctx}/statics/js/jquery-1.11.1.min.js?v=1.0"></script>
<script type="text/javascript" src="${ctx}/statics/plugins/jquery-validate/jquery.validate.min.js?v=1.0"></script>
<script type="text/javascript" src="${ctx}/statics/js/config-validate.js?v=1.0"></script>
<script type="text/javascript" src="${ctx}/statics/js/util.js?v=1.0"></script>
<sitemesh:head />
</head>

<body class="publish">
	<sitemesh:usePage id="thisPage"></sitemesh:usePage>
	<% pageContext.setAttribute("decorator", thisPage.getProperty("meta.decorator")); %>
	<% pageContext.setAttribute("navigation", thisPage.getProperty("meta.navigation")); %>
	
	<!-- HEADER -->
	<div class="publish-header full-width pos-fixed">
		<div class="publish-header-info pos-rel clearfix">
			<div class="logo fl">
				<a href="${ctx }/">
					<h1 style="line-height: 50px; font-size: 18px;">V动屏</h1>
				</a>
			</div>
			
			<ul class="nav clearfix fl">
				<li class="nav-item fl <c:if test="${navigation != 'apply' && navigation != 'list' && navigation != 'buy' }">cur</c:if>"><a hidefocus class="trs-all" href="${ctx }/">首页</a></li>
				<li class="nav-item fl <c:if test="${navigation == 'apply' }">cur</c:if>">
					<a hidefocus class="trs-all"href="${ctx }/activity/apply">
						申请活动
					</a>
				</li>
				<li class="nav-item fl <c:if test="${navigation == 'list' }">cur</c:if>"><a hidefocus class="trs-all" href="${ctx }/activity/list">我的活动</a></li>
				<li class="nav-item fl <c:if test="${navigation == 'buy' }">cur</c:if>"><a hidefocus class="trs-all" href="${ctx }/vip/list">套餐购买</a></li>
			</ul>
			<shiro:user>
				<div class="pos-abs user-drop-menu">
					<a hidefocus class="nick trs-all" href="javascript:;"> <shiro:principal />&nbsp;
						<span class="fa fa-caret-down"></span>
					</a>
					<div class="items trs-all pos-abs">
						<div class="item">
							<a hidefocus class="trs-all" href="${ctx}/user/wechat_info">公众号设置</a>
						</div>
						<div class="item">
							<a hidefocus class="trs-all" href="${ctx}/user/info">账号信息</a>
						</div>
						<div class="item">
							<a hidefocus class="trs-all" href="${ctx}/logout">退出</a>
						</div>
					</div>
				</div>
			</shiro:user>
		</div>
	</div>
	<!-- HEADER -->
	
	<sitemesh:body />

	<!-- FOOTER -->
	<div class="footer" id="footer">
		<ul class="links ft">
			<li class="links_item no_extra"><a
				href="http://www.tencent.com/zh-cn/index.shtml" target="_blank">关于腾讯</a></li>
			<li class="links_item"><a
				href="/cgi-bin/readtemplate?t=home/agreement_tmpl&amp;type=info&amp;lang=zh_CN&amp;token=711694543"
				target="_blank">服务协议</a></li>
			<li class="links_item"><a
				href="/cgi-bin/readtemplate?t=business/faq_operation_tmpl&amp;type=info&amp;lang=zh_CN&amp;token=711694543"
				target="_blank">运营规范</a></li>
			<li class="links_item"><a
				href="http://kf.qq.com/product/weixinmp.html" target="_blank">客服中心</a></li>
			<li class="links_item"><a href="mailto:weixinmp@qq.com"
				target="_blank">联系邮箱</a></li>
			<li class="links_item"><p class="copyright">Copyright ©
					2012-2015 Tencent. All Rights Reserved.</p></li>
		</ul>
	</div>
</body>
</html>