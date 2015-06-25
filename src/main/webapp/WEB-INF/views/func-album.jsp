<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>自定义相册</title>
<meta name="decorator" content="index" />
<meta name="navigation" content="list" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/statics/css/upload.css" />
<style type="text/css">
.page-wrapper .ui-input {
	width: 40%;
}
.info-table td.no-w-border.tal,
.info-table th.no-w-border.tal {
	text-align:left;
}
.page-wrapper textarea.ui-input {
	width: 40%;
	height: 8em;
}
.info-table td.no-n-border {
	border-top: 0 none;
}
.info-table td.no-s-border {
	border-bottom: 0 none;
}

.info-table td.tips {
	background: rgba(238, 238, 238, 1);
}

.block-title {
	cursor:pointer;
	padding: 1em;
	background:#01CF97;
	border:1px solid #ddd;
	color: white;
}

.block-header {
	cursor:pointer;
	padding: 1em;
	background:#eee;
	border:1px solid #ddd;
}

.ad-info .fullview,
.ad-info .fa-caret-up {
	display:none;
}
.ad-info .preview,
.ad-info .fa-caret-down {
	display:block;
}
.ad-info.edit .fullview,
.ad-info.edit .fa-caret-up {
	display:block;
}
.ad-info.edit .preview,
.ad-info.edit .fa-caret-down {
	display:none;
}

.block-header .status-icon {
	top:50%;
	right:1em;
	margin-top:-6px;
}

</style>
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
						<dd class="menu-item selected">
							<a href="javascript:;">相册设置</a>
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
						<p class="title">自定义相册</p>
						<span class="desc">
							<c:if test="${weChatActivity.fAlbum == true}">
								已开通
							</c:if>
							<c:if test="${weChatActivity.fAlbum == false}">
								未开通
							</c:if>
						</span>
					</div>
					<div class="fr" style="padding-top: 12px;">
						<span style="float:left;line-height:25px;margin:0 10px 0 0;">开关</span>
						<label class="iosCheck">
							<input type="checkbox" id="checkbox" 
							<c:if test="${weChatActivity.fAlbum == true}">checked</c:if>><i></i>
						</label>
					</div>
				</div>
				<div class="info border-box-mode col-main-wrapper">
					<div class="block-title pos-rel">
						<strong>相册描述信息</strong>
					</div>
					
					<div class="ad-info" style="margin: 10px 0px;">
						<div class="block-header pos-rel tar">
							<span style="padding-right:1em;">编辑</span>
							<span class="status-icon pos-abs fa fa-caret-up"></span>
							<span class="status-icon pos-abs fa fa-caret-down"></span>
						</div>
						<div class="fullview">
							<form id="album-info-form" method="post" action="${ctx }/func/albumInfo">
							<input type="hidden" name="activityMid" value="${weChatActivity.activityMid }">
							<table class="full-width info-table">
								<tr>
									<td class="no-e-border" width="80">相册主题</td>
									<td class="no-w-border tal vot">
										<textarea class="ui-input trs-all vot"
										name="albumSubject">${weChatActivity.albumSubject }</textarea>
										<div class="desc tal" style="margin:0 auto;padding:0.5em 0">
										相册主题内容不能超过15字，比如："小明与小红的结婚照"
										</div>
									</td>
								</tr>
								<tr>
									<td class="tips tac" colspan="2"><button type="submit" class="ui-btn trs-all">保存</button></td>
								</tr>
								</table>
							</form>
						</div>
						<div class="preview">
							<table class="full-width info-table">
								<tr>
									<td class="no-e-border" width="80">相册主题</td>
									<td class="tal vot">
										${weChatActivity.albumSubject }
									</td>
								</tr>
							</table>
						</div>
					</div>
					<div class="block-title pos-rel">
						<strong>相册图片素材</strong>
					</div>
					<table class="full-width info-table">
						<tr>
							<td>
								<form id='upload-img-form' action='${ctx}/func/album?activityMid=${weChatActivity.activityMid}' method='post' enctype='multipart/form-data'>
									<div class="bubble-tips-inner" style="float: left">大小:
										不超过1M,&nbsp;&nbsp;格式: bmp, png, jpeg, jpg, gif</div>
									<div class="upload-btn" style="display: block;">
										<div class="upload-btn-word">上传图片</div>
										<input type="file" class="upload-btn-input" name="fileName" style="cursor: pointer;">
									</div>
								</form>
							</td>
						</tr>
	
						<tr>
							<td style="padding: 3em 1em;">
								<c:forEach items="${listAlbum}" var="albumImg">
									<div class="image-wrapper">
										<img src="${ctx }/${albumImg.relativePath}"> <span><a href="${ctx }/func/${weChatActivity.activityMid}/album?delId=${albumImg.id}"><i
											class="fa fa-times"></i></a></span>
									</div>
								</c:forEach>
							</td>
						</tr>
					</table>
					<div class="func-intro">
						<span class="title"><i class="fa fa-file-text-o icon"></i>功能介绍</span>
						<ul class="list">
							<li>自定义相册针对用户发送消息上墙后，将会自带相册链接地址，微信点击即可打开相册</li>
							<li>每个活动还可以上传20张自定义相册，上传完成后在手机微信终端可以查看新人相册</li>
						</ul>
					</div>
				</div>
				
				
			</div>
		</div>
	</div>

	<script type="text/javascript" src="${ctx}/statics/js/jquery.form.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#album-info-form").validate({
		        rules: {
		        	albumSubject: {
	                    required: true
	                }
				},
				messages: {
					albumSubject: {
						required: "请输入相册主题!"
					}
				}
		    });
			$(document).on('click','#checkbox',function (e) {
				var me = $(this);
				var data = {
					activityMid : "${weChatActivity.activityMid }",
					fAlbum : false
				};
				if (true == $(this).prop("checked")) {
					data.fAlbum = true;
				} else {
					data.fAlbum = false;
				}
				$.ajax({
					url: '${ctx }/func/swith',
					data: data,
					type: 'POST',
					dataType: 'json',
					success: function (response) {
						if (response.success) {
							if (data.fAlbum == true) {
								$(".func-title-wrapper .desc").html("已开通")
								alert("功能已打开")
							} else {
								$(".func-title-wrapper .desc").html("未开通")
								alert("功能已关闭")
							}
						} else {
							alert(response.message)
							if (data.fAlbum == true) {
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
			$(document).on('click','.block-header',function(){
				$(this).closest('.ad-info').toggleClass('edit');
			});
		});
	</script>
</body>
</html>