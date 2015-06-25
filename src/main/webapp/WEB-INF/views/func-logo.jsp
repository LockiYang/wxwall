<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>自定义LOGO</title>
<meta name="decorator" content="index" />
<meta name="navigation" content="list" />
<link rel="stylesheet" type="text/css" href="${ctx}/statics/css/upload.css"/>
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
						<dd class="menu-item selected">
							<a href="javascript:;">自定义LOGO</a>
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
					<i class="icon trs-all fa fa-smile-o"></i>
					<div class="fl">
						<p class="title">自定义LOGO</p>
						<span class="desc">
							<c:if test="${weChatActivity.fLogo == true}">
								已开通
							</c:if>
							<c:if test="${weChatActivity.fLogo == false}">
								未开通
							</c:if>
						</span>
					</div>
					<div class="fr" style="padding-top: 12px;">
						<span style="float:left;line-height:25px;margin:0 10px 0 0;">开关</span>
						<label class="iosCheck">
							<input type="checkbox" id="checkbox" 
							<c:if test="${weChatActivity.fLogo == true}">checked</c:if>><i></i>
						</label>
					</div>
					
				</div>
				<div class="info border-box-mode col-main-wrapper">
					<table class="full-width info-table">
						<tr>
							<td>
								<form id='upload-img-form' action='${ctx}/func/logo?activityMid=${weChatActivity.activityMid}' method='post' enctype='multipart/form-data'>
									<div class="bubble-tips-inner" style="float: left">大小:
										不超过2M,&nbsp;&nbsp;像素:建议(180px x 72px),&nbsp;&nbsp;格式: bmp, png, jpeg, jpg, gif</div>
									<div class="upload-btn" style="display: block;">
										<div class="upload-btn-word">上传图片</div>
										<input type="file" class="upload-btn-input" name="fileName" style="cursor: pointer;">
									</div>
								</form>
							</td>
						</tr>
	
						<tr>
							<td style="padding: 3em 1em;">
								<c:if test="${!empty weChatActivity.logo}">
										<center><img src="${ctx }/${weChatActivity.logo}" style="height: 240px;width: 240px;"></center>
								</c:if>
							</td>
						</tr>
					</table>
					
					<div class="func-intro">
						<span class="title"><i class="fa fa-file-text-o icon"></i>功能介绍</span>
						<ul class="list">
							<li>活动主办方可以自定义每个活动的Logo，Logo将显示在大屏幕的左上方位置</li>
							<li>每个活动只允许上传一个Logo，重复上传将覆盖掉之前的</li>
							<li>默认使用V动屏Logo</li>
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
					fLogo : false
				};
				if (true == $(this).prop("checked")) {
					data.fLogo = true;
				} else {
					data.fLogo = false;
				}
				$.ajax({
					url: '${ctx }/func/swith',
					data: data,
					type: 'POST',
					dataType: 'json',
					success: function (response) {
						if (response.success) {
							if (data.fLogo == true) {
								$(".func-title-wrapper .desc").html("已开通")
								alert("功能已打开")
							} else {
								$(".func-title-wrapper .desc").html("未开通")
								alert("功能已关闭")
							}
						} else {
							alert(response.message);
							if (data.fLogo == true) {
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
						
						if (result.success == true) {
							// 刷新页面 
							window.location.reload();
						} else {
							btn.html("上传图片");
							file.prop('disabled',false)
							alert(result.message);
						}
						
					},
					error : function(xhr) { //上传失败 
						btn.html("上传失败");
						files.html(xhr.responseText); //返回失败信息 
					}
				});
			});
	    	//$("#upload-logo-fileupload").wrap("<form id='upload-logo-form' action='${ctx}/common/uploadImgToTmp' method='post' enctype='multipart/form-data'></form>"); 
	    	
// 	    	$("#upload-logo-fileupload").change(function(){ //选择文件 
// 	    		var bar = $('#upload-logo-bar')
// 				percent = $('#upload-logo-percent'),
// 				progress = $("#upload-logo-progress"),
// 				files = $("#upload-logo-files"),
// 				btn = $("#upload-logo-btn span");
// 				//清除前期数据
// 				files.html("");
// 		        $("#upload-logo-form").ajaxSubmit({ 
// 		            dataType:  'json', //数据格式为json 
// 		            beforeSend: function() { //开始上传 
// 		                progress.show(); //显示进度条 
// 		                var percentVal = '0%'; //开始进度为0% 
// 		                bar.width(percentVal); //进度条的宽度 
// 		                percent.html(percentVal); //显示进度为0% 
// 		                btn.html("上传中..."); //上传按钮显示上传中 
// 		            }, 
// 		            uploadProgress: function(event, position, total, percentComplete) { 
// 		                var percentVal = percentComplete + '%'; //获得进度 
// 		                bar.width(percentVal); //上传进度条宽度变宽 
// 		                percent.html(percentVal); //显示上传进度百分比 
// 		            }, 
// 		            success: function(result) { //成功 
// 		            	if (result.success == true) {
// 		            		//获得后台返回的json数据，显示文件名，大小，以及删除按钮 
// 			                files.html("<b>"+result.data.name+"("+result.data.size+"k)</b><span class='add-file-delimg' rel='" + result.data.pic + "'>删除</span>"); 
// 			                //显示上传后的图片 
// 			                var img = result.data.pic; 
// 			                $('#upload-logo-show').attr("src", img);
// 			                btn.html("上传"); //上传按钮还原
// 			                $('#uploadLogoInput').val(result.data.tmpPath);
// 		            	} else {
// 		            		 //btn.html("上传失败"); 
// 			                bar.width('0'); 
// 			                files.html(result.message); //返回失败信息 
// 		            	}
// 		            }, 
// 		            error:function(xhr){ //上传失败 
// 		                btn.html("上传失败"); 
// 		                bar.width('0'); 
// 		                files.html(xhr.responseText); //返回失败信息 
// 		            } 
// 		        }); 
// 		    }); 
		});
	</script>
</body>
</html>