<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>申请活动</title>
<meta name="decorator" content="index" />
<meta name="navigation" content="apply" />
<link rel="stylesheet" type="text/css" href="${ctx}/statics/css/jquery.datetimepicker.css"/>
</head>
<body>
	<div class="page-header">首页&nbsp;>&nbsp;申请活动</div>
	
	<div class="publish-container">
		<div class="page-wrapper border-box-mode">
			<div class="publish-section">
				<c:if test="${not empty errorMsg }">
					<div class="tips-info warning">${errorMsg }</div>
				</c:if>
				<form id="publish-form" method="post"
					action="${ctx }/activity/apply">
					<div class="info form">
						<div class="frm-control-group">
							<label class="frm-label">活动主题</label>
							<div class="frm-controls">
								<span class="frm-input-box">
									<input id="subject" name="subject" type="text" class="frm-input js-cardname">
								</span>
								<p class="frm-tips">举办活动的主题，如李雷和韩梅梅的婚礼。将在大屏幕上显示，请谨慎填写。</p>
							</div>
						</div>
						<div class="frm-control-group">
							<label class="frm-label">主办方</label>
							<div class="frm-controls">
								<span class="frm-input-box">
									<input id="organisers" name="organisers"  type="text" class="frm-input">
								</span>
								<p class="frm-tips">活动的主办方，如李雷和韩梅梅夫妇。</p>
							</div>
						</div>
						<div class="frm-control-group">
							<label class="frm-label">开始时间</label>
							<div class="frm-controls">
								<span class="frm-input-box">
									<input readonly id="startDate" name="startDate"  type="text" class="frm-input">
								</span>
								<p class="frm-tips">活动的开始时间，如不填写默认申请即开始活动，活动持续时间默认为72小时。</p>
							</div>
						</div>
						<div class="frm-control-group">
							<label class="frm-label">活动简介</label>
							<div class="frm-controls">
								<span class="frm-textarea-box">
									<textarea id="description" name="description" class="frm-textarea"></textarea>
								</span>
								<p class="frm-tips">活动的简单介绍，方便记忆。</p>
							</div>
						</div>
						<div class="tool-bar border with-form">
							<button type="submit" class="ui-btn trs-all">确认</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="${ctx}/statics/js/jquery.datetimepicker.js"></script>
	<script type="text/javascript">
		$(function() {
			$('#startDate,#endDate').datetimepicker({
				lang:'zh',
				minDate: '0',
				minTime: '0',
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
			
			$("#publish-form").validate({
				errorPlacement: function (error, element) {
                    element.parent().after(error);
                },
                errorElement: "p",
                errorClass: "error",
				rules : {
					subject : 'required',
					organisers : 'required',
					category : 'required',
					startDate : 'required',
					endDate : 'required'
				}
			});
		})
	</script>
</body>
</html>