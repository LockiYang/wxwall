<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>申请活动</title>
<meta name="decorator" content="index" />
<meta name="navigation" content="apply" />
<link rel="stylesheet" type="text/css" href="${ctx}/statics/css/upload.css"/>
</head>
<body>
	<div class="publish-container">
		<div class="page-wrapper border-box-mode">
			<div class="page-wrapper-header">
				<span class="icon fa fa-connectdevelop fa-3"></span> <span
					class="text">自定义活动背景页面<span>
			</div>
			<div class="publish-section">
			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(function() {
			$("#publish-form").validate({
				rules : {
					subject : 'required',
					organisers : 'required',
					category : 'required',
					startDate : 'required',
					endDate : 'required',
					description : 'required'
				}
			});
		})
	</script>
</body>
</html>