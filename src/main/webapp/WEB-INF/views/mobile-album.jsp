<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>${WeChatActivity.albumSubject }</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0,user-scalable=0,minimal-ui" />
<meta name="apple-mobile-web-app-capable" content="yes">
<link rel="stylesheet"
	href="${ctx}/statics/plugins/PhotoSwipe-master/styles.css">
<link rel="stylesheet"
	href="${ctx}/statics/plugins/PhotoSwipe-master/photoswipe.css">
	<script type="text/javascript" src="${ctx}/statics/js/jquery-1.11.1.min.js"></script>
	<script type="text/javascript"
		src="${ctx}/statics/plugins/PhotoSwipe-master/klass.min.js"></script>
	<script type="text/javascript"
		src="${ctx}/statics/plugins/PhotoSwipe-master/code.photoswipe-3.0.5.js"></script>
		<script type="text/javascript"
		src="${ctx}/statics/plugins/PhotoSwipe-master/jquery.transit.js"></script>
		<script type="text/javascript"
		src="${ctx}/statics/plugins/PhotoSwipe-master/hammer.js"></script>
		<script type="text/javascript"
		src="${ctx}/statics/plugins/PhotoSwipe-master/jquery.hammer.js"></script>
	<script type="text/javascript">
		(function(window, PhotoSwipe) {
			document.addEventListener('DOMContentLoaded', function() {
				var options = {
						preventHide:true,
						getImageSource: function(obj) {
							return obj.url;
						},
						getImageCaption: function(obj) {
							return obj.caption;
						}
				}, 
				
				instance = PhotoSwipe.attach(
						[
							<c:forEach items="${listAlbum}" var="albumImg" varStatus="status">
								<c:if test="${status.last}">
									{ url : '${ctx }/${albumImg.relativePath}', caption:'${WeChatActivity.albumSubject }'}
								</c:if>
								<c:if test="${!status.last}">
									{ url : '${ctx }/${albumImg.relativePath}', caption:'${WeChatActivity.albumSubject }'},
								</c:if>
							</c:forEach>
				        ], 
				        options);
				instance.show(0);
			}, false);
		}(window, window.Code.PhotoSwipe));
	</script>
</head>
<body>
</body>
</html>