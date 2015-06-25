<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>公众号信息高级设置</title>
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
	<div class="page-header">首页&nbsp;>&nbsp;公众号信息高级设置</div>
	<div class="publish-container">
		<div class="page-wrapper border-box-mode" style="padding: 0;">
			<div class="col-side">
				<div class="menu-box">
					<dl class="menu no-extra">
						<dd class="menu-item selected">
							<a href="javascript:;">自动回复设置</a>
						</dd>
					</dl>
				</div>
			</div>

			<div class="col-main">
				<div class="info border-box-mode col-main-wrapper">
					<c:if test="${not empty errorMsg }">
						<div class="tips-info warning">${errorMsg }</div>
					</c:if>
					<div class="block-title pos-rel">
							<strong>被添加自动回复</strong>
					</div>
					<div class="ad-info" style="margin: 10px 0px;">
						<div class="block-header pos-rel tar">
							<span style="padding-right:1em;">编辑</span>
							<span class="status-icon pos-abs fa fa-caret-up"></span>
							<span class="status-icon pos-abs fa fa-caret-down"></span>
						</div>
						<div class="fullview">
							<form id="subscribe-form" class="info-form" method="post" action="${ctx }/user/wechat_advance_info">
							<input type="hidden" name="setupType" value="subscribe">
							<table class="full-width info-table">
								<tr>
									<td class="no-e-border" width="80">消息回复类型</td>
									<td class="no-w-border tal">
										<select class="ui-input trs-all" name="autoType">
											<option value="text" 
												<c:if test="${weChatApp.weChatAppAutoReply.subscribeAutoReply.replyType == '1' }">selected</c:if>>文本消息</option>
											<option value="image" 
												<c:if test="${weChatApp.weChatAppAutoReply.subscribeAutoReply.replyType == '2' }">selected</c:if>>图文消息</option>
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
										name="content">${weChatApp.weChatAppAutoReply.subscribeAutoReply.weChatText.content }</textarea>
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
										<input type="text" class="ui-input trs-all" name="title" value="${weChatApp.weChatAppAutoReply.subscribeAutoReply.weChatImageText.title }">
										<div class="desc tal" style="margin:0 auto;padding:0.5em 0">
										文本消息内容内容不能超过60字，比如："欢迎您：参加我们的活动！"
										</div>
									</td>
								</tr>
								<tr >
									<td class="no-e-border no-n-border no-s-border" width="80">链接</td>
									<td class="no-w-border no-n-border no-s-border tal vot">
										<input type="text" class="ui-input trs-all" name="imgUrl" value="${weChatApp.weChatAppAutoReply.subscribeAutoReply.weChatImageText.imgUrl }">
										<div class="desc tal" style="margin:0 auto;padding:0.5em 0">
										选填内容，图文消息链接地址，比如："http://www.baidu.com"
										</div>
									</td>
								</tr>
								<tr >
									<td class="no-e-border no-n-border" width="80">图片</td>
									<td class="no-w-border no-n-border tal vot">
										<input type="hidden" name="img" class="imgInput" value="">
										<div>
											<img alt="点击上传,奖品图片显示在此区域" src='<c:if test="${!empty weChatApp.weChatAppAutoReply.subscribeAutoReply.weChatImageText.img }">${ctx }/${weChatApp.weChatAppAutoReply.subscribeAutoReply.weChatImageText.img }</c:if>' 
											class="upload-img-show" width="120px" height="120px" style="margin-left: -4px">
										</div>
										<div>
											<div class="add-file-btn"> 
											     <span>上传</span> 
											     <input class="upload-img-fileupload" type="file" name="mypic"> 
											</div>
											<div class="add-file-progress"> 
									    		<span class="add-file-bar"></span>
									    		<span class="add-file-percent">0%</span > 
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
										<c:if test="${weChatApp.weChatAppAutoReply.subscribeAutoReply.replyType == '1' }">
											文本消息
										</c:if>
										<c:if test="${weChatApp.weChatAppAutoReply.subscribeAutoReply.replyType == '2' }">
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
										${weChatApp.weChatAppAutoReply.subscribeAutoReply.weChatText.content }
									</td>
								</tr>
								<tr>
									<td class="tips"  colspan="2">图文消息设置</td>
								</tr>
								<tr >
									<td class="no-e-border no-s-border" width="80">图文标题</td>
									<td class="no-s-border tal vot">
										${weChatApp.weChatAppAutoReply.subscribeAutoReply.weChatImageText.title }
									</td>
								</tr>
								<tr >
									<td class="no-e-border no-n-border no-s-border" width="80">链接</td>
									<td class="no-n-border no-s-border tal vot">
										${weChatApp.weChatAppAutoReply.subscribeAutoReply.weChatImageText.imgUrl }
									</td>
								</tr>
								<tr >
									<td class="no-e-border no-n-border" width="80">图片</td>
									<td class="no-n-border tal vot">
										<img alt="点击上传,奖品图片显示在此区域" src='<c:if test="${!empty weChatApp.weChatAppAutoReply.subscribeAutoReply.weChatImageText.img }">${ctx }/${weChatApp.weChatAppAutoReply.subscribeAutoReply.weChatImageText.img }</c:if>' width="120px" height="120px" style="margin-left: -4px">
									</td>
								</tr>
							</table>
						</div>
					</div>
					
					<!-- 自动回复设置 -->
					
					<div class="block-title pos-rel">
							<strong>自动回复</strong>
					</div>
					<div class="ad-info" style="margin: 10px 0px;">
						<div class="block-header pos-rel tar">
							<span style="padding-right:1em;">编辑</span>
							<span class="status-icon pos-abs fa fa-caret-up"></span>
							<span class="status-icon pos-abs fa fa-caret-down"></span>
						</div>
						<div class="fullview">
							<form class="info-form" method="post" action="${ctx }/user/wechat_advance_info">
							<input type="hidden" name="setupType" value="autoReply">
							<table class="full-width info-table">
								<tr>
									<td class="no-e-border" width="80">消息回复类型</td>
									<td class="no-w-border tal">
										<select class="ui-input trs-all" name="autoType">
											<option value="text" 
												<c:if test="${weChatApp.weChatAppAutoReply.autoReply.replyType == '1' }">selected</c:if>>文本消息</option>
											<option value="image" 
												<c:if test="${weChatApp.weChatAppAutoReply.autoReply.replyType == '2' }">selected</c:if>>图文消息</option>
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
										name="content">${weChatApp.weChatAppAutoReply.autoReply.weChatText.content }</textarea>
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
										<input type="text" class="ui-input trs-all" name="title" value="${weChatApp.weChatAppAutoReply.autoReply.weChatImageText.title }">
										<div class="desc tal" style="margin:0 auto;padding:0.5em 0">
										文本消息内容内容不能超过60字，比如："欢迎您：参加我们的活动！"
										</div>
									</td>
								</tr>
								<tr >
									<td class="no-e-border no-n-border no-s-border" width="80">链接</td>
									<td class="no-w-border no-n-border no-s-border tal vot">
										<input type="text" class="ui-input trs-all" name="imgUrl" value="${weChatApp.weChatAppAutoReply.autoReply.weChatImageText.imgUrl }">
										<div class="desc tal" style="margin:0 auto;padding:0.5em 0">
										选填内容，图文消息链接地址，比如："http://www.baidu.com"
										</div>
									</td>
								</tr>
								<tr >
									<td class="no-e-border no-n-border" width="80">图片</td>
									<td class="no-w-border no-n-border tal vot">
										<input type="hidden" name="img" class="imgInput" value="">
										<div>
											<img alt="点击上传,奖品图片显示在此区域" src='<c:if test="${!empty weChatApp.weChatAppAutoReply.autoReply.weChatImageText.img }">${ctx }/${weChatApp.weChatAppAutoReply.autoReply.weChatImageText.img }</c:if>' class="upload-img-show" width="120px" height="120px" style="margin-left: -4px">
										</div>
										<div>
											<div class="add-file-btn"> 
											     <span>上传</span> 
											     <input class="upload-img-fileupload" type="file" name="mypic"> 
											</div>
											<div class="add-file-progress"> 
									    		<span class="add-file-bar"></span>
									    		<span class="add-file-percent">0%</span > 
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
										<c:if test="${weChatApp.weChatAppAutoReply.autoReply.replyType == '1' }">
											文本消息
										</c:if>
										<c:if test="${weChatApp.weChatAppAutoReply.autoReply.replyType == '2' }">
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
										${weChatApp.weChatAppAutoReply.autoReply.weChatText.content }
									</td>
								</tr>
								<tr>
									<td class="tips"  colspan="2">图文消息设置</td>
								</tr>
								<tr >
									<td class="no-e-border no-s-border" width="80">图文标题</td>
									<td class="no-s-border tal vot">
										${weChatApp.weChatAppAutoReply.autoReply.weChatImageText.title }
									</td>
								</tr>
								<tr >
									<td class="no-e-border no-n-border no-s-border" width="80">链接</td>
									<td class="no-n-border no-s-border tal vot">
										${weChatApp.weChatAppAutoReply.autoReply.weChatImageText.imgUrl }
									</td>
								</tr>
								<tr >
									<td class="no-e-border no-n-border" width="80">图片</td>
									<td class="no-n-border tal vot">
										<img alt="点击上传,奖品图片显示在此区域" src="${ctx }/${weChatApp.weChatAppAutoReply.autoReply.weChatImageText.img }" width="120px" height="120px" style="margin-left: -4px">
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="${ctx}/statics/js/jquery.form.js"></script>
	<script type="text/javascript" src="${ctx}/statics/js/dialog.js?v=1.0"></script>
	<script type="text/javascript">
	$(function () {
		$(".info-form").validate({
	        rules: {
			},
			messages: {
			}
	    });
		
		$(".upload-img-fileupload").change(function(){ //选择文件 
			var $me = $(this), $form = $me.closest('.info-form');
    		var bar = $form.find('.add-file-bar');
			percent = $form.find('.add-file-percent'),
			progress = $form.find(".add-file-progress"),
			btn = $form.find(".add-file-btn span");
			console.log( $me.closest('.info-form').find('.upload-img-show'))
			//清除前期数据
			$form.attr("enctype", "multipart/form-data");
			$form.ajaxSubmit({ 
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
		                $form.find('.upload-img-show').attr("src", img);
		                btn.html("上传"); //上传按钮还原
		                $form.find('.imgInput').val(result.data.tmpPath);
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
			$(this).closest('.info-form').attr("enctype", "application/x-www-form-urlencoded");
	    }); 
		$(document).on('click','.block-header',function(){
			$(this).closest('.ad-info').toggleClass('edit');
		});
	});
	</script>
</body>
</html>