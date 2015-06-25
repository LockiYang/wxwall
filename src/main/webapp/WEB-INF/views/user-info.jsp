<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
	<head>
		<title>个人信息设置</title>
		<meta name="decorator" content="index"/>
	</head>
	<body>
		<div class="page-header">首页&nbsp;>&nbsp;个人信息设置</div>
		<div class="publish-container">
			<div class="page-wrapper border-box-mode">
				<div class="publish-section">
					<div class="account-section">
						<c:if test="${not empty errorMsg }">
							<div class="tips-info error">${errorMsg }</div>
						</c:if>
						<c:if test="${not empty infoMsg }">
							<div class="tips-info success">${infoMsg }</div>
						</c:if>
						<table class="full-width info-table" style="margin-bottom:1em;">
							<tr>
								<th colspan="3">账号详情</th>
							</tr>
<!-- 							<tr> -->
<!-- 								<td class="tar" width="150">头像</td> -->
<%-- 								<td class="no-e-border"><img class="avatar" src="${ctx}/statics/images/getheadimg.jpg"></td> --%>
<!-- 								<td class="no-w-border" width="150"><a href="javascript:;">修改</a></td> -->
<!-- 							</tr> -->
							<tr>
								<td class="tar">主体信息</td>
								<td class="no-e-border">${user.userName }</td>
								<td class="no-w-border">
									<a class="open-target" data-show="userName-dialog" href="javascript:;">修改</a>
								</td>
							</tr>
							<tr>
								<td class="tar">联系人</td>
								<td class="no-e-border">${user.linkman }</td>
								<td class="no-w-border">
									<a class="open-target" data-show="linkman-dialog" href="javascript:;">修改</a>
								</td>
							</tr>
							<tr>
								<td class="tar">手机号</td>
								<td class="no-e-border">${user.mobilePhone }</td>
								<td class="no-w-border">
									<a class="open-target" data-show="mobilePhone-dialog" href="javascript:;">修改</a>
								</td>
							</tr>
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
								<th colspan="3">注册信息</th>
							</tr>
							<tr>
								<td class="tar" width="150">注册邮箱</td>
								<td class="no-e-border">${user.loginEmail }</td>
								<td class="no-w-border" width="150"></td>
							</tr>
							<tr>
								<td class="tar">注册时间</td>
								<td class="no-e-border">
									<fmt:formatDate value="${user.createDate }" type="time" timeStyle="full" pattern="yyyy-MM-dd HH:mm:ss" />
								</td>
								<td class="no-w-border"></td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
		
		<!-- this is dialog mask -->
		<div class="mask trs-all"></div>
		<!-- /this is dialog mask -->
		
		<div class="dialog trs-all" id="userName-dialog" style="width:700px">
			<div class="head">
				<h1 class="title">设置主体信息</h1>
				<a class="x close-target trs-all fa fa-close" href="javascript:;"></a>
			</div>
			<div class="body">
				<form id="userName-form" method="post" action="${ctx }/user/info">
					<input name="id" hidden="true" type="text" value="${user.id }">
					<div class="info border-box-mode tac">
						<input type="text" class="ui-input trs-all" name="userName" autofocus>
						<div class="desc tal" style="margin:0 auto;padding:0.5em 0">设置主体信息，如公司名称、法人名称</div>
					</div>
				
				<div class="foot">
					<button type="submit" class="ui-btn trs-all">确定</button>
					<button type="button" class="ui-btn ui-btn-cancel trs-all close-target">取消</button>
				</div>
				</form>
			</div>
		</div>
		
		<div class="dialog trs-all" id="linkman-dialog" style="width:700px">
			<div class="head">
				<h1 class="title">设置联系人</h1>
				<a class="x close-target trs-all fa fa-close" href="javascript:;"></a>
			</div>
			<div class="body">
				<input name="id" hidden="true" type="text" value="${user.id }">
				<form id="linkman-form" method="post" action="${ctx }/user/info">
					<div class="info border-box-mode tac">
						<input type="text" class="ui-input trs-all" name="linkman" autofocus>
					</div>
				
				<div class="foot">
					<button type="submit" class="ui-btn trs-all">确定</button>
					<button type="button" class="ui-btn ui-btn-cancel trs-all close-target">取消</button>
				</div>
				</form>
			</div>
		</div>
		
		<div class="dialog trs-all" id="mobilePhone-dialog" style="width:700px">
			<div class="head">
				<h1 class="title">设置手机号</h1>
				<a class="x close-target trs-all fa fa-close" href="javascript:;"></a>
			</div>
			<div class="body">
				<input name="id" hidden="true" type="text" value="${user.id }">
				<form id="mobilePhone-form" method="post" action="${ctx }/user/info">
					<div class="info border-box-mode tac">
						<input type="text" class="ui-input trs-all" name="mobilePhone" autofocus>
					</div>
				
				<div class="foot">
					<button type="submit" class="ui-btn trs-all">确定</button>
					<button type="button" class="ui-btn ui-btn-cancel trs-all close-target">取消</button>
				</div>
				</form>
			</div>
		</div>
		
		<script type="text/javascript" src="${ctx}/statics/js/dialog.js?v=1.0"></script>
		<script type="text/javascript">
			$(function() {
				$("#linkman-form").validate({
					rules : {
						linkman : {
							required : true
						}
					},
					messages : {
						linkman : {
							required : '联系人为必填项'
						}
					}
				});
				$("#userName-form").validate({
					rules : {
						userName : {
							required : true
						}
					},
					messages : {
						userName : {
							required : '主体信息为必填项'
						}
					}
				});
				$("#mobilePhone-form").validate({
					rules : {
						mobilePhone : {
							required : true,
							mobile : true
						}
					},
					messages : {
						mobilePhone : {
							required : '手机号为必填项',
							mobile : '手机号码格式错误'
						}
					}
				});
			})
		</script>
	</body>
</html>