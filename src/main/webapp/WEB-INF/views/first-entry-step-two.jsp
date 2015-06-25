<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>账号注册</title>
<meta name="decorator" content="index" />
</head>
<body>
	<div class="page-header">首页&nbsp;>&nbsp;账号注册</div>
	<div class="publish-container">
		<div class="page-wrapper border-box-mode">
			<div class="page-wrapper-header">
				<span class="icon fa fa-connectdevelop fa-3"></span> <span
					class="text">资料完善向导<span>
			</div>
			<div class="publish-section">
				<div class="progress-stick x3 clearfix">
					<div class="fl step-tag step-1 completed">
						<div class="inner">1、完善资料</div>
					</div>
					<div class="fl step-tag step-2 now">
						<div class="inner">2、绑定微信</div>
					</div>
					<div class="fl step-tag step-3">
						<div class="inner">3、完成</div>
					</div>
				</div>
				<div class="step-content">
					<form id="step-form" method="post" action="${ctx }/user/profile/2">
						<div>
							<span class="icon fa fa-star"></span>正确填写以下信息
						</div>
						<table class="full-width no-border">
							<tr>
								<td class="tar" width="33%">公众号原始ID</td>
								<td><input type="text" class="ui-input trs-all" autofocus
									name="weChatOriginId" value="${weChatApp.weChatOriginId }"
									placeholder="登录公众号平台，在 设置》公众号设置 中查看"></td>
							</tr>
							<tr>
								<td class="tar">公众号名称</td>
								<td><input type="text" class="ui-input trs-all"
									name="weChatName" value="${weChatApp.weChatName }"></td>
							</tr>
							<tr>
								<td class="tar">应用ID</td>
								<td><input type="text" class="ui-input trs-all"
									name="appId" value="${weChatApp.appId }"></td>
							</tr>
							<tr>
								<td class="tar">应用密匙</td>
								<td><input type="text" class="ui-input trs-all"
									name="appSecret" value="${weChatApp.appSecret }"></td>
							</tr>
							<c:if test="${not empty errorMsg }">
								<tr>
									<td class="tar"></td>
									<td style="color: red;">${errorMsg }</td>
								</tr>
							</c:if>
							<tr>
								<td class="tar"></td>
								<td><button type="submit" class="ui-btn trs-all"
										style="margin-left: 8em;">下一步</button></td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(function() {
			$("#step-form").validate({
				rules : {
					weChatOriginId : 'required',
					weChatName : 'required',
					weChatId : 'required',
					appSecret : 'required'
				}
			});
		})
	</script>
</body>
</html>
