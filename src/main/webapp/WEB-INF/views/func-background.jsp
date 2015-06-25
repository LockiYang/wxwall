<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>自定义背景</title>
<meta name="decorator" content="index" />
<meta name="navigation" content="list" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/statics/css/upload.css" />
</head>
<body>
	<div class="page-header">首页&nbsp;>&nbsp;活动功能设置</div>
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
						<dd class="menu-item selected">
							<a href="javascript:;">自定义背景</a>
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
							<a href="${ctx }/func/${weChatActivity.activityMid }/signin">签到</a>
						</dd>
						<dd class="menu-item">
							<a href="${ctx }/func/${weChatActivity.activityMid }/ad">广告设置</a>
						</dd>
						<dd class="menu-item">
							<a href="${ctx }/func/${weChatActivity.activityMid }/album">相册设置</a>
						</dd>
						<dd class="menu-item selected">
							<a href="${ctx }/func/${weChatActivity.activityMid }/photoPile">精彩瞬间</a>
						</dd>
<!-- 						<dd class="menu-item"> -->
<%-- 							<a href="${ctx }/func/${weChatActivity.activityMid }/sch">日程推送</a> --%>
<!-- 						</dd> -->
					</dl>
				</div>
			</div>

			<div class="col-main">
				<div class="func-title-wrapper">
					<i class="icon trs-all fa fa-picture-o"></i>
					<div class="fl">
						<p class="title">自定义背景</p>
						<span class="desc">
							<c:if test="${weChatActivity.fBackground == true}">
								已开通
							</c:if>
							<c:if test="${weChatActivity.fBackground == false}">
								未开通
							</c:if>
						</span>
					</div>
					<div class="fr" style="padding-top: 12px;">
						<span style="float:left;line-height:25px;margin:0 10px 0 0;">开关</span>
						<label class="iosCheck">
							<input type="checkbox" id="checkbox" 
							<c:if test="${weChatActivity.fBackground == true}">checked</c:if>><i></i>
						</label>
					</div>
				</div>
				<div class="info border-box-mode col-main-wrapper">
					<table class="full-width info-table">
						<tr>
							<td>
								<form id='upload-img-form' action='${ctx}/func/background?activityMid=${weChatActivity.activityMid}' method='post' enctype='multipart/form-data'>
									<div class="bubble-tips-inner" style="float: left">大小:
										不超过2M,&nbsp;&nbsp;像素:建议(1024px x 768px),&nbsp;&nbsp;格式: bmp, png, jpeg, jpg, gif</div>
									<div class="upload-btn" style="display: block;">
										<div class="upload-btn-word">上传图片</div>
										<input type="file" class="upload-btn-input" name="fileName" style="cursor: pointer;">
									</div>
								</form>
							</td>
						</tr>
	
						<tr>
							<td style="padding: 3em 1em;">
								<c:forEach items="${listBackGround}" var="backgroudImg">
									<div class="image-wrapper">
										<img src="${ctx }/${backgroudImg.relativePath}"> <span><a href="${ctx }/func/${weChatActivity.activityMid}/background?delId=${backgroudImg.id}"><i
											class="fa fa-times"></i></a></span>
									</div>
								</c:forEach>
							</td>
						</tr>
					</table>
					<div class="func-intro">
						<span class="title"><i class="fa fa-file-text-o icon"></i>功能介绍</span>
						<ul class="list">
							<li>每个活动还可以上传一个自定义背景，上传完成后在大屏幕控制栏进行背景选择</li>
							<li>每个活动只允许上传一张背景图，重复上传将覆盖掉之前的</li>
							<li>默认提供7款精美的背景供选择</li>
						</ul>
					</div>
				</div>
				
				
			</div>
		</div>
	</div>

	<script type="text/javascript" src="${ctx}/statics/js/jquery.form.js"></script>
	<script type="text/javascript">
		$(function() {
			$(document).on('click','#checkbox',function (e) {
				var me = $(this);
				var data = {
					activityMid : "${weChatActivity.activityMid }",
					fBackground : false
				};
				if (true == $(this).prop("checked")) {
					data.fBackground = true;
				} else {
					data.fBackground = false;
				}
				$.ajax({
					url: '${ctx }/func/swith',
					data: data,
					type: 'POST',
					dataType: 'json',
					success: function (response) {
						if (response.success) {
							if (data.fBackground == true) {
								$(".func-title-wrapper .desc").html("已开通")
								alert("功能已打开")
							} else {
								$(".func-title-wrapper .desc").html("未开通")
								alert("功能已关闭")
							}
						} else {
							alert(response.message)
							if (data.fBackground == true) {
								$(".func-title-wrapper .desc").html("未开通");
								me.attr("checked", false);
							} else {
								$(".func-title-wrapper .desc").html("已开通");
								me.attr("checked", true);
							}
						};
					}
				});
			});
			$(".upload-btn-input").change(function() { //选择文件 
				var file = $(".upload-btn-input"),
				btn = $(".upload-btn-word");
				$("#upload-img-form").ajaxSubmit({
					dataType : 'json', //数据格式为json 
					beforeSend : function() { //开始上传 
						btn.html("上传中..."); //上传按钮显示上传中 
						file.prop('disabled',true)
					},
					uploadProgress : function(event, position,total,percentComplete) {
					},
					success : function(result) {
						btn.html("上传图片");
						file.prop('disabled',false)
						if (result.success == true) {
							// 刷新页面 
							window.location.reload();
						} else {
							alert(result.message);
						}
						
					},
					error : function(xhr) { //上传失败 
						btn.html("上传失败");
						files.html(xhr.responseText); //返回失败信息 
					}
				});
			});
		});
	</script>
</body>
</html>