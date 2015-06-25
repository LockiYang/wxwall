<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!doctype html>
<html>
<head>
<title>我的活动</title>
<meta name="decorator" content="index" />
<meta name="navigation" content="list" />
<link rel="stylesheet" type="text/css" href="${ctx}/statics/css/upload.css"/>
</head>

<body class="publish">

	<c:set var="inprogress_href" value="${ctx }/activity/list?type=inprogress"/>
	<c:set var="done_href" value="${ctx }/activity/list?type=done"/>
	<c:set var="prep_href" value="${ctx }/activity/list?type=prep"/>
	<c:if test="${type == 'inprogress' }">
		<c:set var="href" value="${inprogress_href }"/>
	</c:if>
	<c:if test="${type == 'done' }">
		<c:set var="href" value="${done_href }"/>
	</c:if>
	<c:if test="${type == 'prep' }">
		<c:set var="href" value="${prep_href }"/>
	</c:if>
	
	<div class="page-header">
		首页&nbsp;>&nbsp;我的活动
	</div>
	<div class="publish-container">
		<div class="page-wrapper border-box-mode">
			<div class="publish-tabs" style="margin-bottom:10px;">
				<a class="<c:if test="${type == 'inprogress' }">cur</c:if>" href="<c:if test="${type == 'inprogress' }">javascript:;</c:if><c:if test="${type != 'inprogress' }">${inprogress_href }</c:if>">进行中</a>
					<a class="<c:if test="${type == 'prep' }">cur</c:if>" href="<c:if test="${type == 'prep' }">javascript:;</c:if><c:if test="${type != 'prep' }">${prep_href }</c:if>">筹备中</a>
					<a class="<c:if test="${type == 'done' }">cur</c:if>" href="<c:if test="${type == 'done' }">javascript:;</c:if><c:if test="${type != 'done' }">${done_href }</c:if>">已结束</a>
			</div>
			<div class="activities-section">
				<c:forEach items="${activityList}" var="activity">
					<!-- this is a activity-block-->
					<div class="activity-block clearfix">
						<div class="menu fr">
							<a hidefocus class="menu-item tac fl" href="${ctx }/func/${activity.activityMid }/logo" 
							data-data='{"activityMid": "${activity.activityMid }", "logo": "${activity.logo }"}' href="javascript:;">
								<div class="icon <c:if test="${activity.fLogo == false }"> icon-disable </c:if>trs-all fa fa-smile-o"></div>
								<div class="text">自定义logo</div>
							</a> <a hidefocus class="menu-item tac fl" href="${ctx }/func/${activity.activityMid }/background">
								<div class="icon <c:if test="${activity.fBackground == false }"> icon-disable </c:if>trs-all fa fa-picture-o"></div>
								<div class="text">自定义背景</div>
							</a> <a hidefocus class="menu-item tac fl" href="${ctx }/func/${activity.activityMid }/autoupwall">
								<div class="icon <c:if test="${activity.fAutoUpWall == false }"> icon-disable </c:if>trs-all fa fa-newspaper-o"></div>
								<div class="text">自动上墙</div>
							</a> <a hidefocus class="menu-item tac fl" href="${ctx }/func/${activity.activityMid }/draw">
								<div class="icon <c:if test="${activity.fDraw == false }"> icon-disable </c:if>trs-all fa  fa-gift"></div>
								<div class="text">抽奖</div>
							</a> <a hidefocus class="menu-item tac fl" href="${ctx }/func/${activity.activityMid }/pair">
								<div class="icon <c:if test="${activity.fPair == false }"> icon-disable </c:if>trs-all fa fa-heartbeat"></div>
								<div class="text">对对碰</div>
							</a> <a hidefocus class="menu-item tac fl" href="${ctx }/func/${activity.activityMid }/shake">
								<div class="icon <c:if test="${activity.fShake == false }"> icon-disable </c:if>trs-all fa fa-chain-broken"></div>
								<div class="text">摇一摇</div>
							</a> <a hidefocus class="menu-item tac fl" href="${ctx }/func/${activity.activityMid }/signin">
								<div class="icon <c:if test="${activity.fSignIn == false }"> icon-disable </c:if>trs-all fa fa-edit"></div>
								<div class="text">签到</div>
							</a> <a hidefocus class="menu-item tac fl" href="${ctx }/func/${activity.activityMid }/ad">
								<div class="icon trs-all fa fa-volume-up"></div>
								<div class="text">广告设置</div>
							</a>
							<a hidefocus class="menu-item tac fl" href="${ctx }/func/${activity.activityMid }/album">
								<div class="icon <c:if test="${activity.fAlbum == false }"> icon-disable </c:if>trs-all fa fa-file-image-o"></div>
								<div class="text">自定义相册</div>
							</a>
							<a hidefocus class="menu-item tac fl" href="${ctx }/func/${activity.activityMid }/photoPile">
								<div class="icon <c:if test="${activity.fActivityPhoto == false }"> icon-disable </c:if>trs-all fa fa-file-image-o"></div>
								<div class="text">精彩瞬间</div>
							</a>
<%-- 							 <a hidefocus class="menu-item tac fl" href="${ctx }/func/${activity.activityMid }/sch"> --%>
<%-- 								<div class="icon <c:if test="${activity.fScheduler == false }"> icon-disable </c:if>trs-all fa fa-bell"></div> --%>
<!-- 								<div class="text">日程推送</div> -->
<!-- 							</a> -->
						</div>
						<div class="info pos-rel">
							<h1 class="title">${activity.subject}</h1>
							<div class="info form-inner">
								<div class="frm-control-group">
									<label class="frm-label">活动时间</label>
									<div class="frm-controls">
										<label><fmt:formatDate
										value="${activity.startDate }" type="time" timeStyle="full"
										pattern="yyyy-MM-dd HH:mm" />&nbsp;至 &nbsp;<fmt:formatDate
										value="${activity.endDate }" type="time" timeStyle="full"
										pattern="yyyy-MM-dd HH:mm" />
										</label>
									</div>
								</div>
								<div class="frm-control-group">
									<label class="frm-label">主办方</label>
									<div class="frm-controls">
										<label>
											${activity.organisers}
										</label>
									</div>
								</div>
								<div class="frm-control-group">
									<label class="frm-label">活动状态</label>
									<div class="frm-controls">
										<label>
											<c:if test="${activity.status == 'INPROGRESS' }">
												<span class="ui-lable">
													进行中
												</span>
											</c:if>
											<c:if test="${activity.status == 'DONE' }">
												<span class="ui-lable gray">
													已结束
												</span>
											</c:if>
											<c:if test="${activity.status == 'PREP' }">
												<span class="ui-lable red">
													筹备中
												</span>
											</c:if>
										</label>
									</div>
								</div>
							</div>
							<div class="buttons tal" style="margin-top:20px;">
								<c:if test="${type == 'inprogress' || type == 'prep' }">
									<a hidefocus class="ui-btn" href="${ctx}/statics/upbang.html?activityMid=${activity.activityMid}" target="_blank">查看活动</a>
								</c:if>
								<a hidefocus class="link-btn" href="${ctx }/activity/${activity.activityMid}/data_list"><i class="fa fa-pie-chart"></i>查看消息数据</a>
								<a hidefocus class="link-btn" href="${ctx }/activity/${activity.activityMid}/user_list"><i class="fa fa-pie-chart"></i>查看用户数据</a>
								<c:if test="${ type == 'prep' }">
									<a id="edit-activity" hidefocus class="link-btn open-target" data-show="activity-dialog" href="javascript:;"
										data-data='{"subject": "${activity.subject }",
													"activityMid": "${activity.activityMid }",
													"organisers": "${activity.organisers }",
													"startDate": "<fmt:formatDate value="${activity.startDate }" type="time" timeStyle="full" pattern="yyyy-MM-dd HH:mm" />",
													"endDate": "<fmt:formatDate value="${activity.endDate }" type="time" timeStyle="full" pattern="yyyy-MM-dd HH:mm" />",
													"description": "${activity.description }"}' ><i class="fa fa-pencil-square-o"></i>修改</a>
								</c:if>
								<c:if test="${type == 'inprogress' || type == 'prep' }">
									<a hidefocus class="link-btn" href="${ctx }/activity/${activity.activityMid}/end"><i class="fa fa-check-square-o"></i>结束</a>
								</c:if>
<!-- 								<a hidefocus class="link-btn" href="javascript:;" style="color: #459ae9;"><i class="fa fa-question-circle"></i>常见问题</a> -->
							</div>
							<c:if test="${!empty activity.ticket}"><img class="qr-code" src="${ctx}/${activity.ticket}"></c:if>
						</div>
					</div>
					
					
					<!-- /this is a activity-block-->
				</c:forEach>
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
	</div>
	<!-- this is dialog mask -->
	<div class="mask trs-all"></div>
	<!-- /this is dialog mask -->

	<div class="dialog trs-all" id="activity-dialog" style="width:700px">
		<div class="head">
			<h1 class="title">设置公众号</h1>
			<a class="x close-target trs-all fa fa-close" href="javascript:;"></a>
		</div>
		<div class="body">
			<form id="activity-form" method="post" action="${ctx }/activity/update">
				<div class="info form-inner">
					<input id="activityMid" name="activityMid" type="text" hidden="true" >
					<div class="frm-control-group">
						<label class="frm-label">活动主题</label>
						<div class="frm-controls">
							<span class="frm-input-box">
								<input id="subject" name="subject" type="text" class="frm-input js-cardname">
							</span>
							<p class="frm-msg fail">
								<i>●</i><span class="frm-msg-content">功能介绍长度为4-120个字</span>
							</p>
						</div>
					</div>
					<div class="frm-control-group">
						<label class="frm-label">主办方</label>
						<div class="frm-controls">
							<span class="frm-input-box">
								<input id="organisers" name="organisers"  type="text" class="frm-input">
							</span>
						</div>
					</div>
					<div class="frm-control-group">
						<label class="frm-label">活动时间</label>
						<div class="frm-controls">
							<label id="activityDate"></label>
						</div>
					</div>
					<div class="frm-control-group">
						<label class="frm-label">活动简介</label>
						<div class="frm-controls">
							<span class="frm-textarea-box">
								<textarea id="description" name="description" class="frm-textarea"></textarea>
							</span>
						</div>
					</div>
				</div>
				<div class="foot">
					<button type="submit" class="ui-btn trs-all">确定</button>
					<button type="button" class="ui-btn ui-btn-cancel trs-all close-target">取消</button>
				</div>
			</form>
		</div>
	</div>
	
	<!-- /this is a dialog  -->
	<script type="text/javascript" src="${ctx}/statics/js/dialog.js?v=1.0"></script>
	<script type="text/javascript" src="${ctx}/statics/js/jquery.form.js?v=1.0"></script>
	<script type="text/javascript">
		$(function() {
			$(document).on('click','#edit-activity',function(e){
				var me = $(this),
					data = me.data('data');
				 $('#subject').val(data.subject);
				 $('#activityMid').val(data.activityMid);
				 $('#organisers').val(data.organisers);
				 $('#description').val(data.description);
				 $('#activityDate').html(data.startDate + "&nbsp;至&nbsp;" + data.endDate);
			});
			
			$("#activity-form").validate({
				errorPlacement: function (error, element) {
                    element.parent().after(error);
                },
                errorElement: "p",
                errorClass: "error",
				rules : {
					subject : 'required',
					organisers : 'required'
				}
			});
			
		});
	</script>
</body>
</html>