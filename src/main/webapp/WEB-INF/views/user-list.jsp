<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>活动用户列表</title>
<meta name="decorator" content="index" />
</head>
<body>
	<c:set var="sign_href" value="${ctx }/activity/${activityMid }/user_list?user_status=sign"/>
	<c:set var="cancel_sign_href" value="${ctx }/activity/${activityMid }/user_list?user_status=cancelSign"/>
	<c:if test="${userStatus == 1 }">
		<c:set var="href" value="${sign_href }"/>
	</c:if>
	<c:if test="${userStatus == 2 }">
		<c:set var="href" value="${cancel_sign_href }"/>
	</c:if>
	<div class="page-header">首页&nbsp;>&nbsp;活动用户列表</div>
	<div class="publish-container">
		<div class="page-wrapper border-box-mode">
			<div class="publish-tabs">
					<a class="<c:if test="${userStatus == 1 }">cur</c:if>" href="<c:if test="${userStatus == 1 }">javascript:;</c:if><c:if test="${userStatus != 1 }">${sign_href }</c:if>">已签到用户</a>
					<a class="<c:if test="${userStatus == 2 }">cur</c:if>" href="<c:if test="${userStatus == 2 }">javascript:;</c:if><c:if test="${userStatus != 2 }">${cancel_sign_href }</c:if>">签到取消用户</a>
				</div>
				
				<div class="publish-section" style="margin:10px 0;">
					<table class="checkin-data">
						<tbody>
						<c:forEach items="${userList}" var="user">
							<tr>
								<td width="64" class="tac"><img class="avatar" src="${user.avatar }"></td>
								<td width="250">
									<span style="display:block;margin: 10px 0;">${user.nick }</span>
									<span style="color:#bbb;">签到时间:&nbsp;<fmt:formatDate value="${user.updateTime }" type="time" timeStyle="full" pattern="yyyy-MM-dd HH:mm:ss" /></span>
								</td>
								<td width="400">
									<c:if test="${user.sex == '1'}">男</c:if>
									<c:if test="${user.sex == '2'}">女</c:if>
								</td>
								<td class="tar">
									${user.country }${user.province }${user.city }
								</td>
							</tr>
						</c:forEach>
					</tbody></table>
				</div>
				<c:if test="${totalNum > 1 }">
					<div class="page-navi fr">
						<c:if test="${pageNum != 1 }"><a class="btn" href="${href }&page=${pageNum - 1}"><i class="fa fa-chevron-left"></i></a></c:if>
						${pageNum }&nbsp;/&nbsp;${totalNum }
						<c:if test="${pageNum != totalNum && pageNum < totalNum }">
							<a class="btn" href="${href }&page=${pageNum + 1}"><i class="fa fa-chevron-right"></i></a>
						</c:if>
						<input class="tac turn-to-page" type="text">
						<a class="btn action" href="javascript:;">跳转</a>
					</div>
				</c:if>
				<div class="clearfix"></div>
		</div>
	</div>

</body>
</html>