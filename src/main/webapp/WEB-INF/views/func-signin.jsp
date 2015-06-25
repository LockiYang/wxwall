<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>签到</title>
<meta name="decorator" content="index" />
<meta name="navigation" content="list" />
<link rel="stylesheet" type="text/css" href="${ctx}/statics/css/upload.css"/>
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
						<dd class="menu-item selected">
							<a href="javascript:;">签到</a>
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
					<i class="icon trs-all fa fa-edit"></i>
					<div class="fl">
						<p class="title">签到</p>
						<span class="desc">
							<c:if test="${weChatActivity.fSignIn == true}">
								已开通
							</c:if>
							<c:if test="${weChatActivity.fSignIn == false}">
								未开通
							</c:if>
						</span>
					</div>
					<div class="fr" style="padding-top: 12px;">
						<span style="float:left;line-height:25px;margin:0 10px 0 0;">开关</span>
						<label class="iosCheck">
							<input type="checkbox" id="checkbox" 
							<c:if test="${weChatActivity.fSignIn == true}">checked</c:if>><i></i>
						</label>
					</div>
					
				</div>
				<div class="info border-box-mode col-main-wrapper">
					<div class="block-title pos-rel">
							<strong>用户签到方式</strong>
					</div>
					<div class="ad-info" style="margin: 10px 0px;">
						<div class="block-header pos-rel tar">
							<span style="padding-right:1em;">编辑</span>
							<span class="status-icon pos-abs fa fa-caret-up"></span>
							<span class="status-icon pos-abs fa fa-caret-down"></span>
						</div>
						<div class="fullview">
							<form id="sign-form" method="post" action="${ctx }/func/signin">
							<input type="hidden" name="activityMid" value="${weChatActivity.activityMid }">
							<table class="full-width info-table">
								<tr>
									<td class="tips" colspan="2">房间号签到设置</td>
								</tr>
								<tr>
									<td class="no-e-border" width="80">房间号内容</td>
									<td class="no-w-border tal vot">
										<textarea class="ui-input trs-all vot"
										name="signKeyWord">${weChatActivity.signKeyWord }</textarea>
										<div class="desc tal" style="margin:0 auto;padding:0.5em 0">
										房间号签到内容不能超过250字，建议比较精简，比如："1314",默认微信客户端打开签到页面输入房间号"1314"即可签到
										</div>
									</td>
								</tr>
								<c:if test="${weChatActivity.weChatApp.accountType == '3' }">
								<tr>
									<td class="tips"  colspan="2">扫码签到</td>
								</tr>
								<tr >
									<td class="no-e-border no-n-border" width="80">二维码图片</td>
									<td class="no-w-border no-n-border tal vot">
										<div>
											<img alt="点击上传,奖品图片显示在此区域" src="${ctx }/${weChatActivity.ticket }" id="upload-img-show" width="120px" height="120px" style="margin-left: -4px">
										</div>
										<div class="desc tal" style="margin:0 auto;padding:0.5em 0">
											扫码签到图片由系统自动生成，用户无需修改
										</div>
									</td>
								</tr>
								</c:if>
								<tr>
									<td class="tips tac" colspan="2"><button type="submit" class="ui-btn trs-all">保存</button></td>
								</tr>
								</table>
							</form>
						</div>
						
						<div class="preview">
							<table class="full-width info-table">
								<tr>
									<td class="tips" colspan="2">房间号签到设置</td>
								</tr>
								<tr>
									<td class="no-e-border" width="80">房间号内容</td>
									<td class="tal vot">
										${weChatActivity.signKeyWord }
									</td>
								</tr>
								<c:if test="${weChatActivity.weChatApp.accountType == '3' }">
								<tr>
									<td class="tips"  colspan="2">扫码签到设置</td>
								</tr>
								<tr >
									<td class="no-e-border no-n-border" width="80">图片</td>
									<td class="no-n-border tal vot">
										<img alt="点击上传,奖品图片显示在此区域" src="${ctx }/${weChatActivity.ticket }" width="120px" height="120px" style="margin-left: -4px">
									</td>
								</tr>
								</c:if>
							</table>
						</div>
					</div>
					<div class="func-intro">
						<span class="title"><i class="fa fa-file-text-o icon"></i>功能介绍</span>
						<ul class="list">
							<li>活动现场，已经关注过公号的参与者，可直接通过扫描二维码(认证服务号)或者房间号(认证订阅号)完成签到。未关注过公号的参与者，通过扫描现场二维码可关注微信公号，关注后即可完成签到</li>
							<li>认证服务号:扫描二维码签到</li>
							<li>认证订阅号:只能房间号签到</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="${ctx}/statics/js/jquery.form.js"></script>
	<script type="text/javascript">
		$(function() {
			var validator = $("#sign-form").validate({
		        rules: {
		        	signKeyWord: {
	                    required: true
	                }
				},
				messages: {
					signKeyWord: {
						required: "房间号签到内容不能为空!"
					}
				}
		    });
			
			$(document).on('click','#checkbox',function (e) {
				var me = $(this);
				var data = {
					activityMid : "${weChatActivity.activityMid }",
					fSignIn : false
				};
				if (true == $(this).prop("checked")) {
					data.fSignIn = true;
				} else {
					data.fSignIn = false;
				}
				$.ajax({
					url: '${ctx }/func/swith',
					data: data,
					type: 'POST',
					dataType: 'json',
					success: function (response) {
						if (response.success) {
							if (data.fSignIn == true) {
								$(".func-title-wrapper .desc").html("已开通")
								alert("功能已打开")
							} else {
								$(".func-title-wrapper .desc").html("未开通")
								alert("功能已关闭")
							}
						} else {
							alert(response.message);
							if (data.fSignIn == true) {
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
			
			$(document).on('click','.block-header',function(){
				$(this).closest('.ad-info').toggleClass('edit');
			})
		});
	</script>
</body>
</html>