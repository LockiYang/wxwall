<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
	<head>
		<title>注册</title>
		<meta name="decorator" content="simple"/>
	</head>
	<body class="publish">
		<div class="register">
			<form id="register-form" method="post" action="${ctx}/register">
				<table class="full-width no-border">
					<tr>
						<td class="tar" width="60">邮&#12288;箱</td>
						<td><input type="text" class="ui-input trs-all" name="loginEmail" value="${user.loginEmail }" autofocus></td>
					</tr>
					<tr>
						<td class="tar">密&#12288;码</td>
						<td><input type="password" class="ui-input trs-all" name="plainPassword" id="plainPassword"></td>
					</tr>
					<tr>
						<td class="tar">确认密码</td>
						<td><input type="password" class="ui-input trs-all" name="repassword"></td>
					</tr>
					<tr>
						<td class="tar">邀请码</td>
						<td><input type="text" class="ui-input trs-all" name="invitationCode"></td>
					</tr>
					<c:if test="${not empty errorMsg }">
						<tr>
							<td class="tar"></td>
							<td style="color:red;">${errorMsg }</td>
						</tr>
					</c:if>
					<tr>
						<td class="tar"></td>
						<td><button type="submit" class="ui-btn trs-all">注 册</button></td>
					</tr>
				</table>
			</form>
		</div>

		<script type="text/javascript">
			$(function () {
				$("#register-form").validate({
			        rules: {
			        	loginEmail: {
	                        required: true,
	                        email: true,
	                        remote: "${ctx}/register/check_login_email"
	                    },
						plainPassword: {
							required: true,
							minlength: 6,
	                        maxlength: 16
						},
						repassword: {
							required: true,
							minlength: 6,
	                        maxlength: 16,
							equalTo: "#plainPassword"
						},
						invitationCode: {
							required: true
						}
					},
					messages: {
						loginEmail: {
	                        required: "请输入登录邮箱!",
	                        email: "请输入有效的邮箱地址!",
	                        remote: "该邮箱已被注册!"
	                        
	                    },
						plainPassword: {
							required: "请输入密码!",
							minlength: "请输入6-16位密码，字母区分大小写!",
	                        maxlength: "请输入6-16位密码，字母区分大小写!"
						},
						repassword: {
							minlength: "请输入6-16位密码，字母区分大小写!",
	                        maxlength: "请输入6-16位密码，字母区分大小写!",
							equalTo: '两次输入的密码不一致!'
						},
						invitationCode: {
							required: '请填写邀请码!'
						}
					}
			    });
			});
		</script>
	</body>
</html>