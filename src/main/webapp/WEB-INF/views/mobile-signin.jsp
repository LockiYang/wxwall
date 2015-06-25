<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${data.activitySubject }</title>

<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1,user-scalable=no,minimal-ui" />
<link rel="stylesheet" type="text/css" href="${ctx }/statics/screen/css/mobile-signin.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/statics/screen/css/notyfy.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/statics/screen/css/jquery.notyfy.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/statics/screen/css/notyfy.theme.default.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/statics/screen/css/sign.css" />

</head>
<body style="margin: 0 auto;">

	<!--top start-->
	<div class="top_box">
		<div class="top_box_inner">
			<img src="${data.userAvatar }" class="img_user">
			<h3>${data.activitySubject }</h3>
		</div>
	</div>
	<!--top end-->
	<div class="content_box">
		<div class="content_box_inner">
			<form id="sign-in-form" method="post" action="${ctx}/activity-screen/mobile_signin">
				<input name="mid" value="${data.mid }" hidden="true">
				<input name="openId" value="${data.openId }" hidden="true">
			
				<input name="secret" type="text" class="sign_text mangin_top" id="Mobile" placeholder="房间暗号" />
				<button type="button" class="sign_btn" id="GoSignIn">确认微信签到</button>
			</form>
		</div>
	</div>

	<script type="text/javascript" src="${ctx}/statics/js/jquery-1.11.1.min.js"></script>
	<script type="text/javascript" src="${ctx }/statics/screen/js/jquery.notyfy.js"></script>
	<script type="text/javascript" src="${ctx }/statics/screen/js/jquery.formpost.js"></script>
	
	<script type="text/javascript">
		$(function() {
			$(document).on('click', '#GoSignIn', function() {
				if (!$('#Mobile').val()) {
					alertError("请输入房间暗号！");
				} else {
					if(!$('#GoSignIn').hasClass('disabled')) {
						$('#sign-in-form').submit();
					}
					$('#GoSignIn').html('请稍候...').addClass('disabled');
				}
			});
		});
	</script>

</body>
</html>
