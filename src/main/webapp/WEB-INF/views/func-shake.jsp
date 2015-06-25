<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>摇一摇</title>
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
						<dd class="menu-item selected">
							<a href="javascript:;">摇一摇</a>
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
<%-- 							<a href="${ctx }/func/${weChatActivity.activityMid }/ad">日程推送</a> --%>
<!-- 						</dd> -->
					</dl>
				</div>
			</div>

			<div class="col-main">
				<div class="func-title-wrapper">
					<i class="icon trs-all fa fa-chain-broken"></i>
					<div class="fl">
						<p class="title">摇一摇</p>
						<span class="desc">
							<c:if test="${weChatActivity.fShake == true}">
								已开通
							</c:if>
							<c:if test="${weChatActivity.fShake == false}">
								未开通
							</c:if>
						</span>
					</div>
					<div class="fr" style="padding-top: 12px;">
						<span style="float:left;line-height:25px;margin:0 10px 0 0;">开关</span>
						<label class="iosCheck">
							<input type="checkbox" id="checkbox" 
							<c:if test="${weChatActivity.fShake == true}">checked</c:if>><i></i>
						</label>
					</div>
					
				</div>
				<div class="info border-box-mode col-main-wrapper">
					<div class="block-title pos-rel">
						<strong>摇一摇详细设置</strong>
					</div>
					
					<div class="ad-info" style="margin: 10px 0px;">
						<div class="block-header pos-rel tar">
							<span style="padding-right:1em;">编辑</span>
							<span class="status-icon pos-abs fa fa-caret-up"></span>
							<span class="status-icon pos-abs fa fa-caret-down"></span>
						</div>
						<div class="fullview">
							<form id="shake-info-form" method="post" action="${ctx }/func/shake">
							<input type="hidden" name="activityMid" value="${weChatActivity.activityMid }">
							<table class="full-width info-table">
								<tr>
									<td class="no-e-border" width="80">截止次数</td>
									<td class="no-w-border tal vot">
										<input class="ui-input trs-all vot"
										name="endShakeNum" value="${weChatActivity.endShakeNum }">
										<div class="desc tal" style="margin:0 auto;padding:0.5em 0">
										摇一摇活动开始后，当第一个参与者摇的次数达到截止次数时，系统自动停止摇一摇游戏
										</div>
									</td>
								</tr>
								<tr>
									<td class="no-e-border" width="80">截止时间(分钟)</td>
									<td class="no-w-border tal vot">
										<input class="ui-input trs-all vot"
										name="endShakeTime" value="${weChatActivity.endShakeTime }">
										<div class="desc tal" style="margin:0 auto;padding:0.5em 0">
										摇一摇活动开始后，当活动经过截止时间(分钟)后，系统自动停止摇一摇游戏
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
									<td class="no-e-border" width="80">截止次数</td>
									<td class="tal vot">
										${weChatActivity.endShakeNum }
									</td>
								</tr>
								<tr>
									<td class="no-e-border" width="80">截止时间(分钟)</td>
									<td class="tal vot">
										${weChatActivity.endShakeTime }
									</td>
								</tr>
							</table>
						</div>
					</div>
					<div class="func-intro">
						<span class="title"><i class="fa fa-file-text-o icon"></i>功能介绍</span>
						<ul class="list">
							<li>做为活动组织策划者的您是否想象过现场所有人员都站起来拿着手机拼命摇的画面？这样的现场气氛需要用语言来形容吗？真的Hold不住！</li>
							<li>后台功能开关未开启时，前端活动页面无法使用.</li>
							<li>1、截止次数:摇一摇活动开始后，当第一个参与者摇的次数达到截止次数时，系统自动停止摇一摇游戏.</li>
							<li>2、截止时间(分钟):摇一摇活动开始后，当活动经过截止时间(分钟)后，系统自动停止摇一摇游戏.</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="${ctx}/statics/js/jquery.form.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#shake-info-form").validate({
		        rules: {
		        	endShakeNum: {
		        		number: true,
	                    required: true,
	                    min: 1
	                },
	                endShakeTime: {
	                	number: true,
	                    required: true,
	                    min: 1
	                }
				},
				messages: {
					endShakeNum: {
						number: "请输入合法的数字",
	                    required: "必须输入截止次数",
	                    min: "请输入一个最小为1的值"
					},
					endShakeTime: {
						number: "请输入合法的数字",
	                    required: "必须输入截止时间(分钟)",
	                    min: "请输入一个最小为1的值"
	                }
				}
		    });
			$(document).on('click','#checkbox',function (e) {
				var me = $(this);
				var data = {
					activityMid : "${weChatActivity.activityMid }",
					fShake : false
				};
				if (true == $(this).prop("checked")) {
					data.fShake = true;
				} else {
					data.fShake = false;
				}
				$.ajax({
					url: '${ctx }/func/swith',
					data: data,
					type: 'POST',
					dataType: 'json',
					success: function (response) {
						if (response.success) {
							if (data.fShake == true) {
								$(".func-title-wrapper .desc").html("已开通")
								alert("功能已打开")
							} else {
								$(".func-title-wrapper .desc").html("未开通")
								alert("功能已关闭")
							}
						} else {
							alert(response.message);
							if (data.fShake == true) {
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
			});
		});
	</script>
</body>
</html>