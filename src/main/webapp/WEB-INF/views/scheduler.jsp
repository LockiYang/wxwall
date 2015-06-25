<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>日程安排</title>
<meta name="decorator" content="index" />
<meta name="navigation" content="list" />
<link rel="stylesheet" type="text/css" href="${ctx}/statics/css/upload.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/statics/css/jquery.datetimepicker.css"/>
<style type="text/css">
.page-wrapper .ui-input {
	width: 13em;
}
</style>
</head>
<body>
	<div class="publish-container">
		<div class="page-wrapper border-box-mode" style="padding: 0;">
			<div class="col-side">
				<div class="menu-box">
					<dl class="menu no-extra">
						<!-- <dt class="menu-title"> -->
						<!-- 	<i class="fa fa-medium icon-menu"></i>管理 -->
						<!-- </dt> -->
						<dd class="menu-item">
							<a href="${ctx }/func/${weChatActivity.activityMid }/logo">自定义LOGO</a>
						</dd>
						<dd class="menu-item">
							<a href="${ctx }/func/${weChatActivity.activityMid }/background">自定义背景</a>
						</dd>
						<dd class="menu-item">
							<a href="${ctx }/func/${weChatActivity.activityMid }/autoupwall">自动上墙</a>
						</dd>
						<dd class="menu-item">
							<a href="${ctx }/func/${weChatActivity.activityMid }/draw">抽奖</a>
						</dd>
						<dd class="menu-item">
							<a href="${ctx }/func/${weChatActivity.activityMid }/pair">对对碰</a>
						</dd>
						<dd class="menu-item">
							<a href="${ctx }/func/${weChatActivity.activityMid }/shake">摇一摇</a>
						</dd>
						<dd class="menu-item">
							<a href="javascript:;">签到</a>
						</dd>
						<dd class="menu-item">
							<a href="${ctx }/func/${weChatActivity.activityMid }/ad">广告设置</a>
						</dd>
						<dd class="menu-item selected">
							<a href="${ctx }/func/${weChatActivity.activityMid }/sch">日程推送</a>
						</dd>
					</dl>
				</div>
			</div>

			<div class="col-main">
				<div>
					<h2>日程安排</h2>
					<span style="float: left; line-height: 25px; margin: 0 10px 0 0;">开关</span>
					<label class="iosCheck"> <input type="checkbox"
						id="checkbox"
						<c:if test="${weChatActivity.fScheduler == true}">checked</c:if>><i></i>
					</label>
				</div>
				<div class="clearfix"></div>
				<div id="prize_info">
					<div id="addPrize">
						<div style="margin: 10px 0px;">
							<a class="ui-btn trs-all open-target"
								data-show="add-scheduler-dialog" href="javascript:;">新增日程</a>
							<!-- <button type="button" class="ui-btn trs-all" onClick="addElement()">奖项增加</button> -->
						</div>
						<table class="full-width info-table">
							<tr>
								<th colspan="5">日程详细信息</th>
							</tr>
							<tr>
								<th class="no-e-border" width="120">推送时间</td>
								<th class="no-e-border" width="120">推送内容</td>
								<th class="no-e-border" width="130">状态</td>
								<th width="100">操作</td>
							</tr>
							<c:forEach items="${weChatActivity.weActivitySchedulers }" var="weActivityScheduler">
								<tr>
									<td class="no-e-border" width="120">
									<fmt:formatDate
										value="${weActivityScheduler.noticeTime }" type="time" timeStyle="full"
										pattern="yyyy-MM-dd HH:mm" />
									</td>
									<td class="no-e-border" width="120">${weActivityScheduler.noticeContent }</td>
									<td class="no-e-border" width="100">
										<c:if test="${weActivityScheduler.status == 1 }">未推送</c:if>
										<c:if test="${weActivityScheduler.status == 2 }">已推送</c:if>
										<c:if test="${weActivityScheduler.status == 3 }">推送失败</c:if>
									</td>
									<td width="100">
										<div style="margin-bottom: 1em;">
											<a class="ui-btn trs-all open-target" style="margin-bottom:5px"
												data-data='{"schedulerId": "${weActivityScheduler.id }",
												"noticeTime": "<fmt:formatDate
										value="${weActivityScheduler.noticeTime }" type="time" timeStyle="full" pattern="yyyy-MM-dd HH:mm" />",
										"noticeContent": "${weActivityScheduler.noticeContent }"}'
												data-show="add-scheduler-dialog" id="edit-scheduler" href="javascript:;"
												>编辑</a>
											<a class="ui-btn trs-all"
												href="${ctx }/activity/delete_scheduler?activityMid=${weChatActivity.activityMid }&schedulerId=${weActivityScheduler.id }">删除</a>
										</div>
									</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- this is dialog mask -->
	<div class="mask trs-all"></div>
	<!-- /this is dialog mask -->

	<div class="dialog trs-all" id="add-scheduler-dialog" style="width: 700px">
		<div class="head">
			<h1 class="title">新增日程</h1>
			<a class="x close-target trs-all fa fa-close" href="javascript:;"></a>
		</div>
		<div class="body">
			<form id="scheduler-form" method="post" action="${ctx }/activity/scheduler">
				<input type="hidden" name="activityMid" value="${weChatActivity.activityMid }">
				<input type="hidden" name="schedulerId" id="schedulerId" value="">
				<table class="full-width no-border">
					<tr>
						<td class="tar" width="20%">推送时间</td>
						<td>
							<input type="text" class="ui-input trs-all datetimepicker"
								placeholder="" name="noticeTime" id="noticeTime" readOnly>
						</td>
					</tr>
					<tr>
						<td class="tar">推送内容</td>
						<td>
							<input type="text" class="ui-input trs-all"
								placeholder="推送内容" name="noticeContent" id="noticeContent">
						</td>
					</tr>
				</table>
				<div class="foot">
					<button type="submit" class="ui-btn trs-all">确定</button>
					<button type="button"
						class="ui-btn ui-btn-cancel trs-all close-target">取消</button>
				</div>
			</form>
		</div>
	</div>

	<script type="text/javascript" src="${ctx}/statics/js/jquery.form.js"></script>
	<script type="text/javascript" src="${ctx}/statics/js/jquery.datetimepicker.js"></script>
	<script type="text/javascript" src="${ctx}/statics/js/dialog.js?v=1.0"></script>
	<script type="text/javascript">
	$(function () {
		$('.datetimepicker').datetimepicker({
			lang:'zh',
			minDate: '0',
			format:'Y-m-d H:i',
			onSelectDate:function(ct,$i){
				var data = new Date();
				if (data.getFullYear() == ct.getFullYear() &&
						data.getMonth() == ct.getMonth() &&
						data.getDate() == ct.getDate() &&
						data.getHours() >= ct.getHours()) {
					$i.val(data.getFullYear() + '-'
							+ (data.getMonth() + 1) + '-'
							+ data.getDate() + ' '
							+ (data.getHours() + 1) + ':00')
					console.log(data.getHours());
					this.setOptions({
						minTime: '0'
					});
				} else {
					if(data.getHours() >= ct.getHours()) {
						$i.val(ct.getFullYear() + '-'
								+ (ct.getMonth() + 1) + '-'
								+ ct.getDate() + ' '
								+ (ct.getHours() + 1) + ':00')
					}
					
					this.setOptions({
						minTime: false,
					});
				}
			},
			onShow:function(current_time,$input){
				this.setOptions({
					minTime: '0'
				});
			},
			onSelectTime:function(current_time,$input){
				$input.datetimepicker('hide');
			}
		});
		$(document).on('click','#checkbox',function (e) {
			var data = {
				activityMid : "${weChatActivity.activityMid }",
				fScheduler : false
			};
			if (true == $(this).prop("checked")) {
				data.fScheduler = true;
				
			} else {
				data.fScheduler = false;
			}
			$.ajax({
				url: '${ctx }/activity/turn_off_function',
				data: data,
				type: 'POST',
				dataType: 'json',
				success: function (response) {
					if (!response.success) { alert(response.message)};
				}
			});
		});
		var validator = $("#scheduler-form").validate({
	        rules: {
	        	noticeTime: {
                    required: true,
                    date : true
                },
                noticeContent: {
                    required: true
                }
			},
			messages: {
				noticeTime: {
					required: "请输入推送时间!",
					date : "请输入合法的日期"
				},
				noticeContent: {
					required: "请输入推送内容!"
				}
			}
	    });
		
		$(document).on('click','.close-target',function(e){
			validator.resetForm();
		});
		
		$(document).on('click','#edit-scheduler',function(e){
			var me = $(this),
				schedulerId = me.data('data').schedulerId,
				noticeTime = me.data('data').noticeTime,
				noticeContent = me.data('data').noticeContent;
			 $('#schedulerId').val(schedulerId);
			 $('#noticeTime').val(noticeTime);
			 $('#noticeContent').val(noticeContent);
		});
		
		$(document).on('click','.open-target',function(e){
			$('.datetimepicker').datetimepicker('hide');
		});
	});
	</script>
</body>
</html>