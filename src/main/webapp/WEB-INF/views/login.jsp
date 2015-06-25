<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
	<head>
		<title>登录</title>
		<meta name="decorator" content="simple"/>
	</head>
<body>
	<div class="login">

		<form id="login-form" method="post" action="${ctx}/login">
			<table class="full-width no-border">
				<tr>
					<td class="tar" width="60">用户名</td>
					<td><input type="text" class="ui-input trs-all"
						name="username" value="${username }" autofocus></td>
				</tr>
				<tr>
					<td class="tar">密 码</td>
					<td><input type="password" class="ui-input trs-all" name="password" id="password"></td>
				</tr>
				<c:if test="${not empty errorMsg }">
					<tr>
						<td class="tar"></td>
						<td style="color:red;">${errorMsg }</td>
					</tr>
				</c:if>
				<tr>
					<td class="tar"></td>
					<td><button type="submit" class="ui-btn trs-all">登 录</button></td>
				</tr>
			</table>
		</form>
	</div>
	
	<script type="text/javascript">
		$(function() {
			$("#login-form").validate({
				rules : {
					username : {
						required : true
					},
					password : {
						required : true,
						minlength: 6,
                        maxlength: 16
					}
				},
				messages : {
					password : {
						minlength: "请输入6-16位密码，字母区分大小写!",
                        maxlength: "请输入6-16位密码，字母区分大小写!"
					}
				}
			});
		});
	</script>
</body>