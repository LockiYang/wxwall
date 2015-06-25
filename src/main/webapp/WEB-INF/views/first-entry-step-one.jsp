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
					<div class="fl step-tag step-1 now">
						<div class="inner">1、完善资料</div>
					</div>
					<div class="fl step-tag step-2">
						<div class="inner">2、绑定微信</div>
					</div>
					<div class="fl step-tag step-3">
						<div class="inner">3、完成</div>
					</div>
				</div>
				<div class="step-content">
					<form id="step-form" method="post" action="${ctx }/user/profile/1">
						<input name="id" hidden="true" type="text" value="${user.id }">
						<table class="full-width no-border">
							<tr>
								<td class="tar" width="33%">邮箱</td>
								<td><input type="text" class="ui-input trs-all"
									readonly="readonly" autofocus
									value="<shiro:principal property="loginName"/>"></td>
							</tr>
							<tr>
								<td class="tar">主体信息</td>
								<td><input type="text" class="ui-input trs-all"
									name="userName" value="${user.userName }"></td>
							</tr>
							<tr>
								<td class="tar">联系人</td>
								<td><input type="text" class="ui-input trs-all"
									name="linkman" value="${user.linkman }"></td>
							</tr>
							<tr>
								<td class="tar">手机号</td>
								<td><input type="text" class="ui-input trs-all"
									name="mobilePhone" value="${user.mobilePhone }"></td>
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
					loginEmail : {
						required : true,
						email : true
					},
					userName : 'required',
					linkman : 'required',
					mobilePhone : {
						required : true,
						mobile : true
					}
				},
				messages : {
					mobilePhone : {
						mobile : '手机号码格式错误'
					}
				}
			});
		})
	</script>
</body>
</html>