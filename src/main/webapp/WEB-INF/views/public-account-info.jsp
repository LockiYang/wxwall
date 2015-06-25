<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
	<head>
		<title>公众号信息设置</title>
		<meta name="decorator" content="index"/>
	</head>
	<body>
		<div class="page-header">首页&nbsp;>&nbsp;公众号信息设置</div>
		<div class="publish-container">
			<div class="page-wrapper border-box-mode">
				<div class="publish-section">
					<div class="account-section">
						<c:if test="${not empty errorMsg }">
							<div class="tips-info error">${errorMsg }</div>
						</c:if>
						<table class="full-width info-table">
							<tr>
								<th colspan="3">公众号信息</th>
							</tr>
							<tr>
								<td class="tar" width="150">二维码</td>
								<td class="no-e-border">
								<img class="qr-code" src='<c:if test="${not empty weChatApp.weChatImage}">${ctx}/${weChatApp.weChatImage }</c:if>'>
								</td>
								<td class="no-w-border">
									<form id='upload-img-form' action='${ctx}/user/wechat_info_ticket' method='post' enctype='multipart/form-data'>
										<div class="upload-btn" style="display: block;">
											<div class="upload-btn-word">上传图片</div>
											<input type="file" class="upload-btn-input" name="fileName" style="cursor: pointer;">
										</div>
									</form>
								</td>
							</tr>
							<tr>
								<td class="tar">公众号原始ID</td>
								<td class="no-e-border">${weChatApp.weChatOriginId }</td>
								<td class="no-w-border">
									<a class="open-target" data-show="weChatOriginId-dialog" href="javascript:;">修改</a>
								</td>
							</tr>
							<tr>
								<td class="tar">公众号名称</td>
								<td class="no-e-border">${weChatApp.weChatName }</td>
								<td class="no-w-border">
									<a class="open-target" data-show="weChatName-dialog" href="javascript:;">修改</a>
								</td>
							</tr>
							<tr>
								<td class="tar">AppID(应用ID)</td>
								<td class="no-e-border">${weChatApp.appId }</td>
								<td class="no-w-border">
									<a class="open-target" data-show="idSecret-dialog" href="javascript:;">修改</a>
								</td>
							</tr>
							<tr>
								<td class="tar">AppSecret(应用密钥)</td>
								<td class="no-e-border">${weChatApp.appSecret }</td>
								<td class="no-w-border">
									<a class="open-target" data-show="idSecret-dialog" href="javascript:;">修改</a>
								</td>
							</tr>
							<tr>
								<td class="tar">ServerUrl(服务器地址)</td>
								<td class="no-e-border">${weChatApp.mid }</td>
								<td class="no-w-border"></td>
							</tr>
							<tr>
								<td class="tar">Token(令牌)</td>
								<td class="no-e-border">${weChatApp.token }</td>
								<td class="no-w-border"></td>
							</tr>
							<tr>
								<td class="tar">公众号类型</td>
								<td class="no-e-border">
									<c:if test="${weChatApp.accountType == '1' }">
										未认证公众号，请转到<a style="color:#01CF97" href="https://mp.weixin.qq.com/" target="_blank">公众平台</a>进行认证!
									</c:if>
									<c:if test="${weChatApp.accountType == '2' }">
										订阅号
									</c:if>
									<c:if test="${weChatApp.accountType == '3' }">
										服务号
									</c:if>
								</td>
								<td class="no-w-border"></td>
							</tr>
							<tr>
								<td class="tar">状态</td>
								<td class="no-e-border">
									<c:if test="${weChatApp.status == 'SUCCESS' }">
										<span style="color:#01CF97">绑定成功</span>
									</c:if>
									<c:if test="${weChatApp.status == 'FAILD' }">
										<span style="color:red">绑定失败</span>
									</c:if>
									&nbsp;&nbsp;&nbsp;<a style="padding:3px 8px;background-color:#01CF97;color:#fff;" href="${ctx }/user/wechat_test">测试</a>
								</td>
								<td class="no-w-border"></td>
							</tr>
							<tr>
								<td class="tar">设置</td>
								<td class="no-e-border">
									<a style="padding:3px 8px;background-color:#01CF97;color:#fff;" href="${ctx }/user/wechat_advance_info">高级设置</a>
								</td>
								<td class="no-w-border"></td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>

		<!-- this is dialog mask -->
		<div class="mask trs-all"></div>
		<!-- /this is dialog mask -->

		<!-- this is a dialog  -->
		<div class="dialog trs-all" id="weChatOriginId-dialog" style="width:700px">
			<div class="head">
				<h1 class="title">设置公众号原始ID</h1>
				<a class="x close-target trs-all fa fa-close" href="javascript:;"></a>
			</div>
			<div class="body">
				<form id="weChatOriginId-form" method="post" action="${ctx }/user/wechat_info">
					<div class="info border-box-mode tac">
						<input type="text" class="ui-input trs-all" name="weChatOriginId" autofocus>
						<div class="desc tal" style="margin:0 auto;padding:0.5em 0">前往公众号设置中查找</div>
					</div>
				
				<div class="foot">
					<button type="submit" class="ui-btn trs-all">确定</button>
					<button type="button" class="ui-btn ui-btn-cancel trs-all close-target">取消</button>
				</div>
				</form>
			</div>
		</div>
		
		<div class="dialog trs-all" id="weChatName-dialog" style="width:700px">
			<div class="head">
				<h1 class="title">设置公众号名称</h1>
				<a class="x close-target trs-all fa fa-close" href="javascript:;"></a>
			</div>
			<div class="body">
				<form id="weChatName-form" method="post" action="${ctx }/user/wechat_info">
					<div class="info border-box-mode tac">
						<input type="text" class="ui-input trs-all" name="weChatName" autofocus>
						<div class="desc tal" style="margin:0 auto;padding:0.5em 0">前往公众号设置中查找</div>
					</div>
				
					<div class="foot">
						<button type="submit" class="ui-btn trs-all">确定</button>
						<button type="button" class="ui-btn ui-btn-cancel trs-all close-target">取消</button>
					</div>
				</form>
			</div>
		</div>
		<!-- /this is a dialog  -->
		
		<div class="dialog trs-all" id="idSecret-dialog" style="width:700px">
			<div class="head">
				<h1 class="title">设置应用ID和应用密钥</h1>
				<a class="x close-target trs-all fa fa-close" href="javascript:;"></a>
			</div>
			<div class="body">
				<form id="idSecret-form" method="post" action="${ctx }/user/wechat_info">
					<div class="info border-box-mode tac">
						<input type="text" class="ui-input trs-all" placeholder="AppID(应用ID)" name="appId" autofocus>
						<div class="desc tal" style="margin:0 auto;padding:0.5em 0">前往公众号设置中查找</div>
						<br/>
						<input type="text" class="ui-input trs-all" placeholder="AppSecret(应用密钥)" name="appSecret" autofocus>
						<div class="desc tal" style="margin:0 auto;padding:0.5em 0">前往公众号设置中查找</div>
					</div>
				
					<div class="foot">
						<button type="submit" class="ui-btn trs-all">确定</button>
						<button type="button" class="ui-btn ui-btn-cancel trs-all close-target">取消</button>
					</div>
				</form>
			</div>
		</div>

		<script type="text/javascript" src="${ctx}/statics/js/dialog.js"></script>
		<script type="text/javascript" src="${ctx}/statics/js/jquery.form.js"></script>
		<script type="text/javascript">
			$(function() {
				$("#idSecret-form").validate({
					rules : {
						appId : {
							required : true
						},
						appSecret : {
							required : true
						}
					},
					messages : {
						appId : {
							required : 'appId为必填项'
						},
						appSecret : {
							required : 'appSecret为必填项'
						}
					}
				});
				
				$("#weChatName-form").validate({
					rules : {
						weChatName : {
							required : true
						}
					},
					messages : {
						weChatName : {
							required : '公众号名称为必填项'
						}
					}
				});
				
				$("#weChatOriginId-form").validate({
					rules : {
						weChatOriginId : {
							required : true
						}
					},
					messages : {
						weChatOriginId : {
							required : '公众号原始ID为必填项'
						}
					}
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