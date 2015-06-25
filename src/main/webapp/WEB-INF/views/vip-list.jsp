<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>套餐购买</title>
<meta name="decorator" content="index" />
<meta name="navigation" content="buy" />
</head>
<body>
	<div class="page-header">首页&nbsp;>&nbsp;套餐查看</div>
	<div class="publish-container">
		<div class="page-wrapper border-box-mode">
			<div class="publish-section">
				<div class="account-section">
					<table class="full-width info-table" style="margin-bottom: 1em;">
						<c:if test="${user.userType == 'free' }">
								<tr>
									<td class="tar">账号类型</td>
									<td class="no-e-border">
										免费会员
									</td>
									<td class="no-w-border">
<!-- 										<a class="open-target" href="javascript:;">成为VIP</a> -->
									</td>
								</tr>
								<tr>
									<td class="tar">剩余活动次数</td>
									<td class="no-e-border">
									${user.vipTimes }&nbsp;次
									<c:if test="${user.vipTimes == 0 }">
										<span style="color:#FF4DC0;margin-left:10px;">(可申请活动试用，仅能使用上墙功能，并且每次活动限制上墙20条消息。)</span>
									</c:if>
									<c:if test="${user.vipTimes > 0 }">
										<span style="color:#FF4DC0;margin-left:10px;">(可申请活动，全部功能开放。)</span>
									</c:if>
									</td>
									<td class="no-w-border"></td>
								</tr>
							</c:if>
							<c:if test="${user.userType == 'vip' }">
								<tr>
									<td class="tar">账号类型</td>
									<td class="no-e-border">
										VIP会员
									</td>
									<td class="no-w-border">
										
									</td>
								</tr>
								<tr>
									<td class="tar">VIP到期时间</td>
									<td class="no-e-border"><fmt:formatDate
										value="${user.vipEndDate }" type="time" timeStyle="full"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td class="no-w-border">
										<a class="open-target" href="${ctx }/vip/list">续期</a>
									</td>
								</tr>
								<tr>
									<td class="tar">剩余活动次数</td>
									<td class="no-e-border">无限制
										<span style="color:#FF4DC0;margin-left:10px;">(可申请活动，全部功能开放。)</span>
									</td>
									<td class="no-w-border"></td>
								</tr>
							</c:if>
					</table>

					<table class="full-width info-table">
						<tr>
							<th colspan="4">套餐购买</th>
						</tr>
						<tr>
							<td class="no-e-border" width="150">套餐名称</td>
							<td class="no-e-border" width="250">说明</td>
							<td class="no-e-border" width="100">价格</td>
<!-- 							<td width="80">购买</td> -->
						</tr>
						<c:forEach items="${packages}" var="p">
							<tr>
								<td class="no-e-border">${p.name }</td>
								<td class="no-e-border">${p.description }</td>
								<td class="no-e-border">${p.price }&nbsp;元</td>
<%-- 								<td><a class="ui-btn" href="${ctx }/vip/buy/${p.id }">购买</a></td> --%>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="${ctx}/statics/js/dialog.js?v=1.0"></script>
</body>
</html>