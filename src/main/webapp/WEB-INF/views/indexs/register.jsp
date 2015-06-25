<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<c:set var="stc" value="${ctx}/statics/indexs"/>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>注册</title>
<link rel="shortcut icon" type="image/x-icon" href="${ctx}/statics/icons/favicon.ico" />
<link rel="stylesheet" href="${stc }/css/css-common.css" />
<link rel="stylesheet" href="${stc }/css/css-index.css" />
<script type="text/javascript" src="${stc }/plugins/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${stc }/plugins/jquery.SuperSlide.2.1.1.js"></script>
<script type="text/javascript" src="${stc }/plugins/jquery.placeholder.js"></script>
<script type="text/javascript" src="${ctx}/statics/plugins/jquery-validate/jquery.validate.min.js?v=1.0"></script>
<script type="text/javascript" src="${ctx}/statics/js/config-validate.js?v=1.0"></script>
<script type="text/javascript" src="${stc }/js/js-common.js"></script>
<style type="text/css">
body {
	width: 100%;
	background: #010619 url(../statics/indexs/images/bg_login.jpg) center 0 no-repeat;
}
</style>
</head>
<body>
	<div id="header" class="header">
		<div class="width" style="width: 100%;margin: 0 auto;">
			<div class="fl" style="padding-left: 15%;">
				<a href="${ctx }/"> <img src="${stc }/images/index-logo.png" />
				</a>
			</div>
			<div class="fr" style="padding-right: 25%;">
				<ul>
					<li class="select"><a href="${ctx }/">首页</a></li>
<!-- 					<li><a href="#">功能模块</a></li> -->
<!-- 					<li><a href="#">随变</a></li> -->
					<li><a href="${ctx }/register">注册</a></li>
					<li><a href="${ctx }/login">登录</a></li>
					<li><a href="${ctx }/statics/upbang.html?activityMid=3c2a06f414c39f58e4e54942bfff8ccd" class="demo" target="_blank" rel="nofollow">演示</a></li>
					<p class="cl"></p>
				</ul>
			</div>
		</div>
	</div>

	<div>
		<div class="login-width cl"  style="height:700px;">
			<div class="with-login-img"></div>
			<div class="regist-right">
				<c:if test="${not empty errorMsg }">
					<div class="tips-info warning">${errorMsg }</div>
				</c:if>
				<form id="register-form"  class="form-horizontal" method="post" autocomplete="off" action="${ctx}/register">
					<div class="txt-bar">
						<label> <input autofocus name="loginEmail" value="" type="text" placeholder="邮箱">
						</label> 
<!-- 						<span class="tips js_email">邮箱</span> -->
					</div>
					<div class="txt-bar">
						<label> <input id="plainPassword" name="plainPassword" type="password" placeholder="密码">
						</label>
					</div>
					<div class="txt-bar">
						<label> <input name="rePassword" type="password" placeholder="密码确认">
						</label>
					</div>
					<div class="txt-bar">
						<label> <input name="invitationCode" type="text" placeholder="邀请码">
						</label>
					</div>
					<p class="user-notice">
						<label> <i class="icon-select icon-selected"></i> <span>我已阅读并同意</span>
							<a href="javascript:;">《V动屏用户注册协议》</a>
						</label>
					</p>
					<div class="btnbox3">
						<button type="submit" class="btn-regist2">马上注册</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="${stc }/js/js-register.js"></script>
	<script type="text/javascript">
		// 表单验证
		$("#register-form").validate({
			rules : {
				loginEmail : {
					required : true,
					email : true,
					remote : "${ctx}/register/check_login_email"
				},
				plainPassword : {
					required : true,
					minlength : 6,
					maxlength : 16
				},
				rePassword: {
					required: true,
					minlength: 6,
                    maxlength: 16,
					equalTo: "#plainPassword"
				},
				invitationCode : {
					minlength : 32,
					maxlength : 32,
					required : true
				}
			},
			messages : {
				loginEmail : {
					required : "请输入登录邮箱!",
					email : "请输入有效的邮箱地址!",
					remote : "该邮箱已被注册!"

				},
				plainPassword : {
					required : "请输入密码!",
					minlength : "请输入6-16位密码，字母区分大小写!",
					maxlength : "请输入6-16位密码，字母区分大小写!"
				},
				rePassword: {
					minlength: "请输入6-16位密码，字母区分大小写!",
                    maxlength: "请输入6-16位密码，字母区分大小写!",
					equalTo: '两次输入的密码不一致!'
				},
				invitationCode : {
					minlength : "邀请码长度不正确!",
					maxlength : "邀请码长度不正确!",
					required : '请填写邀请码!'
				}
			}
		});
	</script>

</body>

</html>