<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>自动上墙</title>
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
						<dd class="menu-item">
							<a href="${ctx }/func/${weChatActivity.activityMid }/logo">自定义LOGO</a>
						</dd>
						<dd class="menu-item">
							<a href="${ctx }/func/${weChatActivity.activityMid }/background">自定义背景</a>
						</dd>
						<dd class="menu-item selected">
							<a href="javascript:;">自动上墙</a>
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
					<i class="icon trs-all fa fa-newspaper-o"></i>
					<div class="fl">
						<p class="title">自动上墙</p>
						<span class="desc">
							<c:if test="${weChatActivity.fAutoUpWall == true}">
								已开通
							</c:if>
							<c:if test="${weChatActivity.fAutoUpWall == false}">
								未开通
							</c:if>
						</span>
					</div>
					<div class="fr" style="padding-top: 12px;">
						<span style="float:left;line-height:25px;margin:0 10px 0 0;">开关</span>
						<label class="iosCheck">
							<input type="checkbox" id="checkbox" 
							<c:if test="${weChatActivity.fAutoUpWall == true}">checked</c:if>><i></i>
						</label>
					</div>
					
				</div>
				<div class="info border-box-mode col-main-wrapper">
					<div class="func-intro">
						<span class="title"><i class="fa fa-file-text-o icon"></i>功能介绍</span>
						<ul class="list">
							<li>自动上墙:当用户关注微信公众号后，可以通过活动链接，输入房间号或者直接扫描活动二维码(认证服务号)参加活动，当参与活动后，用户发送的消息将会实时自动显示在活动页面的消息墙上.</li>
							<li>自动上墙功能默认打开,当关闭此功能，需要手动到数据列表页面手动挑选每条页面上墙.</li>
							<li>自动上墙功能打开后,后台将会智能过滤敏感词汇，以"*"号代替.</li>
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
					fAutoUpWall : false
				};
				if (true == $(this).prop("checked")) {
					data.fAutoUpWall = true;
				} else {
					data.fAutoUpWall = false;
				}
				$.ajax({
					url: '${ctx }/func/swith',
					data: data,
					type: 'POST',
					dataType: 'json',
					success: function (response) {
						if (response.success) {
							if (data.fAutoUpWall == true) {
								$(".func-title-wrapper .desc").html("已开通")
								alert("功能已打开")
							} else {
								$(".func-title-wrapper .desc").html("未开通")
								alert("功能已关闭")
							}
						} else {
							alert(response.message)
							if (data.fAutoUpWall == true) {
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
		});
	</script>
</body>
</html>