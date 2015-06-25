<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>广告设置</title>
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
						<dd class="menu-item selected">
							<a href="javascript:;">广告设置</a>
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
					<i class="icon trs-all fa fa-volume-up"></i>
					<div class="fl">
						<p class="title">广告设置</p>
						<span class="desc">
							已开通
						</span>
					</div>
				</div>
				<div class="clearfix"></div>
				<div class="info border-box-mode col-main-wrapper">
					<div class="block-title pos-rel">
							<strong>签到后推送的广告消息</strong>
					</div>
					<div class="ad-info" style="margin: 10px 0px;">
						<div class="block-header pos-rel tar">
							<span style="padding-right:1em;">编辑</span>
							<span class="status-icon pos-abs fa fa-caret-up"></span>
							<span class="status-icon pos-abs fa fa-caret-down"></span>
						</div>
						<div class="fullview">
							<form id="sign-ad-form" method="post" action="${ctx }/func/ad">
							<input type="hidden" name="activityMid" value="${weChatActivity.activityMid }">
							<input type="hidden" name="setupType" value="signReply">
							<table class="full-width info-table">
								<tr>
									<td class="no-e-border" width="80">消息回复类型</td>
									<td class="no-w-border tal">
										<select class="ui-input trs-all" name="signReplyType">
											<option value="text" 
												<c:if test="${weChatActivity.activityAutoReply.siginAutoReply.replyType == '1' }">selected</c:if>>文本消息</option>
											<option value="image" 
												<c:if test="${weChatActivity.activityAutoReply.siginAutoReply.replyType == '2' }">selected</c:if>>图文消息</option>
										</select>
									</td>
								</tr>
								<tr>
									<td class="tips" colspan="2">文本消息设置</td>
								</tr>
								<tr>
									<td class="no-e-border" width="80">文本消息内容</td>
									<td class="no-w-border tal vot">
										<textarea class="ui-input trs-all vot"
										name="signContent">${weChatActivity.activityAutoReply.siginAutoReply.weChatText.content }</textarea>
										<div class="desc tal" style="margin:0 auto;padding:0.5em 0">
										文本消息内容内容不能超过250字，比如："欢迎您：参加我们的活动！"
										</div>
									</td>
								</tr>
								<tr>
									<td class="tips"  colspan="2">图文消息设置</td>
								</tr>
								<tr >
									<td class="no-e-border no-s-border" width="80">图文标题</td>
									<td class="no-w-border  no-s-border tal vot">
										<input type="text" class="ui-input trs-all" name="signTitle" value="${weChatActivity.activityAutoReply.siginAutoReply.weChatImageText.title }">
										<div class="desc tal" style="margin:0 auto;padding:0.5em 0">
										文本消息内容内容不能超过60字，比如："欢迎您：参加我们的活动！"
										</div>
									</td>
								</tr>
								<tr >
									<td class="no-e-border no-n-border no-s-border" width="80">链接</td>
									<td class="no-w-border no-n-border no-s-border tal vot">
										<input type="text" class="ui-input trs-all" name="signUrl" value="${weChatActivity.activityAutoReply.siginAutoReply.weChatImageText.imgUrl }">
										<div class="desc tal" style="margin:0 auto;padding:0.5em 0">
										选填内容，图文消息链接地址，比如："http://www.baidu.com"
										</div>
									</td>
								</tr>
								<tr >
									<td class="no-e-border no-n-border" width="80">图片</td>
									<td class="no-w-border no-n-border tal vot">
										<input type="hidden" name="signImg" id="imgInput" value="">
										<div>
											<img alt="点击上传,奖品图片显示在此区域" src='<c:if test="${!empty weChatActivity.activityAutoReply.siginAutoReply.weChatImageText.img }">${ctx }/${weChatActivity.activityAutoReply.siginAutoReply.weChatImageText.img }</c:if>' id="upload-img-show" width="120px" height="120px" style="margin-left: -4px">
										</div>
										<div>
											<div class="add-file-btn" id="upload-img-btn"> 
											     <span>上传</span> 
											     <input id="upload-img-fileupload" type="file" name="mypic"> 
											</div>
											<div class="add-file-progress" id="upload-img-progress"> 
									    		<span class="add-file-bar" id="upload-img-bar"></span>
									    		<span class="add-file-percent" id="upload-img-percent">0%</span > 
											</div>
										</div>
										<div class="desc tal" style="margin:0 auto;padding:0.5em 0">
										推荐图片尺寸为360*200 (不填写将为系统默认)
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
									<td class="no-e-border" width="80">消息回复类型</td>
									<td class="tal">
										<c:if test="${weChatActivity.activityAutoReply.siginAutoReply.replyType == '1' }">
											文本消息
										</c:if>
										<c:if test="${weChatActivity.activityAutoReply.siginAutoReply.replyType == '2' }">
											图文消息
										</c:if>
									</td>
								</tr>
								<tr>
									<td class="tips" colspan="2">文本消息设置</td>
								</tr>
								<tr>
									<td class="no-e-border" width="80">文本消息内容</td>
									<td class="tal vot">
										${weChatActivity.activityAutoReply.siginAutoReply.weChatText.content }
									</td>
								</tr>
								<tr>
									<td class="tips"  colspan="2">图文消息设置</td>
								</tr>
								<tr >
									<td class="no-e-border no-s-border" width="80">图文标题</td>
									<td class="no-s-border tal vot">
										${weChatActivity.activityAutoReply.siginAutoReply.weChatImageText.title }
									</td>
								</tr>
								<tr >
									<td class="no-e-border no-n-border no-s-border" width="80">链接</td>
									<td class="no-n-border no-s-border tal vot">
										${weChatActivity.activityAutoReply.siginAutoReply.weChatImageText.imgUrl }
									</td>
								</tr>
								<tr >
									<td class="no-e-border no-n-border" width="80">图片</td>
									<td class="no-n-border tal vot">
										<img alt="点击上传,奖品图片显示在此区域" src='<c:if test="${!empty weChatActivity.activityAutoReply.siginAutoReply.weChatImageText.img }">${ctx }/${weChatActivity.activityAutoReply.siginAutoReply.weChatImageText.img }</c:if>'
										 width="120px" height="120px" style="margin-left: -4px">
									</td>
								</tr>
							</table>
						</div>
					</div>
					
					<div class="block-title pos-rel">
						<strong>上墙成功回复的广告消息</strong>
					</div>
					
					<div class="ad-info" style="margin: 10px 0px;">
						<div class="block-header pos-rel tar">
							<span style="padding-right:1em;">编辑</span>
							<span class="status-icon pos-abs fa fa-caret-up"></span>
							<span class="status-icon pos-abs fa fa-caret-down"></span>
						</div>
						<div class="fullview">
							<form id="upbang-ad-form" method="post" action="${ctx }/func/ad">
							<input type="hidden" name="activityMid" value="${weChatActivity.activityMid }">
							<input type="hidden" name="setupType" value="upbangReply">
							<table class="full-width info-table">
								<tr>
									<td class="no-e-border" width="80">上墙成功回复内容</td>
									<td class="no-w-border tal vot">
										<textarea class="ui-input trs-all vot"
										name="upbangContent">${weChatActivity.activityAutoReply.upbangAutoReply.weChatText.content }</textarea>
										<div class="desc tal" style="margin:0 auto;padding:0.5em 0">
										上墙成功回复内容不能超过250字，比如："微乐尚提醒你：上墙成功！"
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
									<td class="no-e-border" width="80">上墙成功回复</td>
									<td class="tal vot">
										${weChatActivity.activityAutoReply.upbangAutoReply.weChatText.content }
									</td>
								</tr>
							</table>
						</div>
					</div>
					
					<div class="block-title pos-rel">
						<strong>活动页面顶部提示信息</strong>
					</div>
					
					<div style="margin-top:1em" id="addTipsButton">
						<button type="submit" class="ui-btn trs-all" onClick="addTipsDiv()">添加新提示</button>
					</div>
					<c:forEach items="${weChatActivity.tips}" var="tip" varStatus="status">
					
					<div class="ad-info" style="margin: 10px 0px;">
						<div class="block-header pos-rel tar">
							<!-- <strong>活动页面顶部提示信息</strong> -->
							<span style="padding-right:1em;">编辑</span>
							<span class="status-icon pos-abs fa fa-caret-up"></span>
							<span class="status-icon pos-abs fa fa-caret-down"></span>
						</div>
						<div class="fullview">
							<form method="post" action="${ctx }/func/ad">
							<input type="hidden" name="activityMid" value="${weChatActivity.activityMid }">
							<input type="hidden" name="setupType" value="tip">
							<input type="hidden" name="tipId" value="${tip.id }">
							<table class="full-width info-table">
								<tr>
									<td class="no-e-border" width="80">提示信息-${status.index + 1 }</td>
									<td class="no-w-border tal vot">
										<textarea class="ui-input trs-all vot"
										name="tipContent">${tip.tipContent }</textarea>
										<div class="desc tal" style="margin:0 auto;padding:0.5em 0">
										提示信息内容不能超过15字，比如："宅男和腐女结婚大典！"
										</div>
									</td>
								</tr>
								<tr>
									<td class="tips tac" colspan="2">
										<button type="submit" class="ui-btn trs-all" style="margin-right: 1em;">保存</button>
										<a href="${ctx }/func/del_ad?activityMid=${weChatActivity.activityMid }&setupType=tip&tipId=${tip.id }" class="ui-btn trs-all">删除</a>
									</td>
								</tr>
								</table>
							</form>
						</div>
						<div class="preview">
							<table class="full-width info-table">
								<tr>
									<td class="no-e-border" width="80">提示信息-${status.index + 1 }</td>
									<td class="tal vot">
										${tip.tipContent }
									</td>
								</tr>
							</table>
						</div>
					</div>
					</c:forEach>
				</div>
				<div class="info border-box-mode col-main-wrapper" style="padding:0px">
					<div class="func-intro">
						<span class="title"><i class="fa fa-file-text-o icon"></i>功能介绍</span>
						<ul class="list">
							<li>1.签到后成功返回给用户的消息（认证服务号）</li>
							<li>2.消息上墙成功返回给用户的消息</li>
							<li>3.活动页面顶部提示信息，默认为活动主题</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="${ctx}/statics/js/jquery.form.js"></script>
	<script type="text/javascript" src="${ctx}/statics/js/dialog.js?v=1.0"></script>
	<script type="text/javascript">
	$(function () {
		var validator = $("#sign-ad-form").validate({
	        rules: {
	        	upbangReply: {
                    required: true
                }
			},
			messages: {
				upbangReply: {
					required: "上墙成功回复内容!"
				}
			}
	    });
		
		var validator = $("#upbang-ad-form").validate({
	        rules: {
	        	signReplyType: {
                    required: true
                },
                signContent: {
                    required: true
                },
                signTitle: {
                    required: true
                },
                signUrl: {
                    required: true
                },
                signImg: {
                    required: true
                }
			},
			messages: {
				signReplyType: {
					required: "请选择回复类型!"
				},
				signContent: {
					required: "请输入文本消息内容!"
				},
				signTitle: {
					required: "请输入图文标题!"
				},
				signUrl: {
					required: "请输入url链接地址!"
				},
				signImg: {
					required: "请上传图片!"
				}
			}
	    });
		
		$("#upload-img-fileupload").change(function(){ //选择文件 
    		var bar = $('#upload-img-bar')
			percent = $('#upload-img-percent'),
			progress = $("#upload-img-progress"),
			btn = $("#upload-img-btn span");
			//清除前期数据
			$("#sign-ad-form").attr("enctype", "multipart/form-data")
	        $("#sign-ad-form").ajaxSubmit({ 
	            dataType:  'json', //数据格式为json
	            url: '${ctx}/common/uploadImgToTmp',
	            beforeSend: function() { //开始上传 
	                progress.show(); //显示进度条 
	                var percentVal = '0%'; //开始进度为0% 
	                bar.width(percentVal); //进度条的宽度 
	                percent.html(percentVal); //显示进度为0% 
	                btn.html("上传中..."); //上传按钮显示上传中 
	            }, 
	            uploadProgress: function(event, position, total, percentComplete) { 
	                var percentVal = percentComplete + '%'; //获得进度 
	                bar.width(percentVal); //上传进度条宽度变宽 
	                percent.html(percentVal); //显示上传进度百分比 
	            }, 
	            success: function(result) { //成功 
	            	if (result.success == true) {
		                //显示上传后的图片 
		                var img = result.data.pic; 
		                $('#upload-img-show').attr("src", img);
		                btn.html("上传"); //上传按钮还原
		                $('#imgInput').val(result.data.tmpPath);
	            	} else {
	            		 //btn.html("上传失败"); 
		                bar.width('0'); 
		                files.html(result.message); //返回失败信息 
	            	}
	            }, 
	            error:function(xhr){ //上传失败 
	                btn.html("上传失败"); 
	                bar.width('0'); 
	                files.html(xhr.responseText); //返回失败信息 
	            } 
	        }); 
			$("#sign-ad-form").attr("enctype", "application/x-www-form-urlencoded");
	    }); 
		$(document).on('click','.block-header',function(){
			$(this).closest('.ad-info').toggleClass('edit');
		})
	});
	
	function addTipsDiv() {
		var addTipsDivHtml = '<div class="ad-info edit" style="margin: 10px 0px;">'
							+ '<div class="block-header pos-rel tar">'
								+ '<span style="padding-right:1em;">编辑</span>'
								+ '<span class="status-icon pos-abs fa fa-caret-up"></span>'
								+ '<span class="status-icon pos-abs fa fa-caret-down"></span>'
							+ '</div>'
							+ '<div class="fullview">'
							+ '<form method="post" action="${ctx }/func/ad">'
							+ '<input type="hidden" name="activityMid" value="${weChatActivity.activityMid }">'
							+ '<input type="hidden" name="setupType" value="tip">'
							+ '<table class="full-width info-table">'
								+ '<tr>'
									+ '<td class="no-e-border" width="80">新增提示信息</td>'
									+ '<td class="no-w-border tal vot">'
									+ '<textarea class="ui-input trs-all vot" name="tipContent"></textarea>'
									+ '<div class="desc tal" style="margin:0 auto;padding:0.5em 0">'
									+ '提示信息内容内容不能超过15字，比如："宅男和腐女结婚大典！"'
									+ '</div>'
									+ '</td>'
								+ '</tr>'
								+ '<tr>'
									+ '<td class="tips tac" colspan="2">'
										+ '<button type="submit" class="ui-btn trs-all" style="margin-right: 1em;">保存</button>'
										+ '<a href="${ctx }/func/del_ad?activityMid=${weChatActivity.activityMid }&setupType=tip" class="ui-btn trs-all">删除</a>'
									+ '</td>'
								+ '</tr>'
							+ '</table>'
							+ '</form>'
							+ '</div>'
							+ '<div class="preview">'
							+ '<table class="full-width info-table">'
								+ '<tr>'
									+ '<td class="no-e-border" width="80">新增提示信息</td>'
									+ '<td class="tal vot">'
									+ '</td>'
								+ '</tr>'
							+ '</table>'
							+ '</div>'
							+ '</div>';
		$('#addTipsButton').after(addTipsDivHtml);
	
	}
	</script>
</body>
</html>