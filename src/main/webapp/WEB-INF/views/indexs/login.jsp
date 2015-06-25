<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<c:set var="stc" value="${ctx}/statics/indexs"/>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录</title>
<link rel="shortcut icon" type="image/x-icon" href="${ctx}/statics/icons/favicon.ico">
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
		<div class="login-width cl" style="height:700px;">
			<div class="with-login-img"></div>
			<div class="login-right">
				<form id="login-form"  class="form-horizontal" method="post" autocomplete="off" action="${ctx }/login">
					<div class="txt-bar">
						<label> <input name="username" type="text"
							data-js="js_user" placeholder="邮箱"> 
<!-- 							<span class="tips js_user">用户名/邮箱/大屏幕账号</span> -->
						</label>
					</div>
					<div class="txt-bar">
						<label> <input name="password" type="password"
							data-js="js_pwd" placeholder="密码">
<!-- 							<span class="tips js_pwd">密码</span> -->
						</label>
					</div>
					<div class="shop-under cl" style="margin: 0 0 15px 0;">
<!-- 						<a href="javascript:;" class="forg-pass fr">忘记密码</a>  -->
						<a href="javascript:;"
							class="select-me"> <i class="icon-select icon-selected"></i> <span>记住登录状态</span>
						</a>
					</div>
					<c:if test="${not empty errorMsg }">
						<div class="error">${errorMsg }</div>
					</c:if>
					<div class="btnbox">
						<button type="submit" class="btn-login" id="loginSubmitBtn">登 录</button>
						<button class="btn-regist">注册帐号</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="${stc }/js/js-login.js"></script>
	<script type="text/javascript">
		// 注册按钮
		$(".btn-regist").click(function(e) {
			e.preventDefault();
			window.location.href = "${ctx}/register";
		});
		
		// 表单验证
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
				username: {
                    required: "请输入登录邮箱!",
                    email: "请输入有效的邮箱地址!",
                    remote: "该邮箱已被注册!"
                    
                },
                password: {
					required: "请输入密码!",
					minlength: "请输入6-16位密码，字母区分大小写!",
                    maxlength: "请输入6-16位密码，字母区分大小写!"
				}
			}
		});
	</script>
</body>

</html>