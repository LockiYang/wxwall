<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>活动消息列表</title>
<meta name="decorator" content="index" />
<%-- <link rel="stylesheet" type="text/css" href="${ctx}/statics/screen/css/emoji.css?v=1.0"> --%>
</head>
<body>
	<c:set var="checking_href" value="${ctx }/activity/${activityMid }/data_list?msg_type=checking"/>
	<c:set var="success_href" value="${ctx }/activity/${activityMid }/data_list?msg_type=success"/>
	<c:set var="faild_href" value="${ctx }/activity/${activityMid }/data_list?msg_type=faild"/>
	<c:if test="${msgStatus == 1 }">
		<c:set var="href" value="${checking_href }"/>
	</c:if>
	<c:if test="${msgStatus == 2 }">
		<c:set var="href" value="${success_href }"/>
	</c:if>
	<c:if test="${msgStatus == 3 }">
		<c:set var="href" value="${faild_href }"/>
	</c:if>
	<div class="page-header">首页&nbsp;>&nbsp;活动消息列表</div>
	<div class="publish-container">
		<div class="page-wrapper border-box-mode">
			<div class="publish-tabs">
					<a class="<c:if test="${msgStatus == 1 }">cur</c:if>" href="<c:if test="${msgStatus == 1 }">javascript:;</c:if><c:if test="${msgStatus != 1 }">${checking_href }</c:if>">未上墙消息</a>
					<a class="<c:if test="${msgStatus == 2 }">cur</c:if>" href="<c:if test="${msgStatus == 2 }">javascript:;</c:if><c:if test="${msgStatus != 2 }">${success_href }</c:if>">上墙消息</a>
					<a class="<c:if test="${msgStatus == 3 }">cur</c:if>" href="<c:if test="${msgStatus == 3 }">javascript:;</c:if><c:if test="${msgStatus != 3 }">${faild_href }</c:if>">失败消息</a>
				</div>
				
				<div class="publish-section" style="margin:10px 0;">
					<table class="checkin-data">
						<tbody>
						<c:forEach items="${activityMsgList}" var="activityMsg">
							<tr>
								<td width="64" class="tac"><img class="avatar" src="${activityMsg.avatar }"></td>
								<td width="250">
									<span style="display:block;margin: 10px 0;">${activityMsg.nick }</span>
									<span style="color:#bbb;">发送时间:&nbsp;<fmt:formatDate value="${activityMsg.updateTime }" type="time" timeStyle="full" pattern="yyyy-MM-dd HH:mm:ss" /></span>
								</td>
								<td width="400">
									<c:if test="${!empty activityMsg.messageText}">${activityMsg.messageText }</c:if>
									<c:if test="${!empty activityMsg.messagePhoto}"><img style="height: 64px;" src="${activityMsg.messagePhoto }"></c:if>
								</td>
								<td class="tar">
									<c:if test="${msgStatus == 1}">
										<a class="ui-btn" href="${href }&page=${pageNum}&type=upBang&msgId=${activityMsg.msgId}">上墙</a>
									</c:if>
									<c:if test="${msgStatus == 2}">
										<a class="ui-btn" href="${href }&page=${pageNum}&type=downBang&msgId=${activityMsg.msgId}">下墙</a>
									</c:if>
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