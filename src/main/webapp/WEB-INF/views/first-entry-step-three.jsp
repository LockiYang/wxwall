<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
	<head>
		<title>账号注册</title>
		<meta name="decorator" content="index"/>
	</head>
	<body>
		<div class="page-header">首页&nbsp;>&nbsp;账号注册</div>
		<div class="publish-container">
			<div class="page-wrapper border-box-mode">
				<div class="page-wrapper-header">
					<span class="icon fa fa-connectdevelop fa-3"></span>
					<span class="text">资料完善向导<span>
				</div>
				<div class="publish-section">
					<div class="progress-stick x3 clearfix">
						<div class="fl step-tag step-1 completed"><div class="inner">1、完善资料</div></div>
						<div class="fl step-tag step-2 completed"><div class="inner">2、绑定微信</div></div>
						<div class="fl step-tag step-3 now"><div class="inner">3、完成</div></div>
					</div>
					<div class="step-content">
						<div><span class="fa fa-star"></span>恭喜你,设置完成了!现在可以进行以下操作了!</div>
						<c:if test="${!empty errorMsg }"><div class="tips-info warning"><span class="fa fa-star"></span>${errorMsg }</div></c:if>
						<div><span class="icon fa fa-star"></span>复制下列token和url到公众平台绑定</div>
						<div>
							<table>
								<tr><td>URL</td><td>${weChatApp.mid }</td></tr>
								<tr><td>TOKEN</td><td>${weChatApp.token }</td></tr>
							</table>
						</div>
						<div class="tac">
							<a hidefocus class="ui-btn trs-all" href="https://mp.weixin.qq.com/" target="_blank">到微信公众平台</a>
							<a hidefocus class="ui-btn trs-all" href="${ctx }/activity/apply">免费申请活动</a>
							<a hidefocus class="ui-btn trs-all" href="${ctx}/user/wechat_info">查看公众号信息</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
