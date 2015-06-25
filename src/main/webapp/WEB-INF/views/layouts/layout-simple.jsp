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
<script type="text/javascript" src="${ctx}/statics/js/util.js?v=1.0"></script>
<script type="text/javascript" src="${ctx}/statics/js/config-validate.js?v=1.0"></script>
<script type="text/javascript" src="${ctx}/statics/js/dialog.js?v=1.0"></script>
</head>

<body class="publish">
	<div class="publish-header full-width pos-fixed">
		<div class="publish-header-info pos-rel">
			<div class="publish-logo">
				<a href="${ctx }">
					<h1>抱走王尼玛</h1>
					<p>活动发布管理系统</p>
				</a>
			</div>
			<a hidefocus class="guide-tips pos-abs" href="${ctx }/register">还没有帐号?</a>
		</div>
	</div>

	<sitemesh:body />
</body>
</html>