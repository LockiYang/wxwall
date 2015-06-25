<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>提示信息</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1,user-scalable=no,minimal-ui" />
<link rel="stylesheet" type="text/css" href="${ctx }/statics/screen/css/mobile-signin.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/statics/screen/css/notyfy.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/statics/screen/css/jquery.notyfy.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/statics/screen/css/notyfy.theme.default.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/statics/screen/css/sign.css" />
<script type="text/javascript" src="${ctx}/statics/js/jquery-1.11.1.min.js"></script>

</head>
<body style="margin: 0 auto;">

	<!--top end-->
	<div class="content_box">
		<div class="content_box_inner">
			<div class="Error-box">
				<img src="${ctx }/statics/screen/images/getheadimg.jpg">
				<h3>
					${data.message }
				</h3>

				<c:if test="${data.success == true }">
					<a href="javascript:;" class="sign_btn" onclick="close_wechat()">我知道了</a>
				</c:if>
				<c:if test="${data.success == false }">
					<a href="javascript:;" onclick="history.go(-1);" class="sign_btn">返回上一步</a>
				</c:if>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="${ctx }/statics/screen/js/jquery.formpost.js"></script>
	<script type="text/javascript" src="${ctx }/statics/screen/js/jquery.notyfy.js"></script>
	<script type="text/javascript" src="${ctx }/statics/screen/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript">
		function close_wechat(){
			WeixinJSBridge.call("closeWindow");
		}
	</script>
	
</body>
</html>
