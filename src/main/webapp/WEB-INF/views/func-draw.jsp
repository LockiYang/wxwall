<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>抽奖</title>
<meta name="decorator" content="index" />
<meta name="navigation" content="list" />
<link rel="stylesheet" type="text/css" href="${ctx}/statics/css/upload.css" />

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
						<dd class="menu-item selected">
							<a href="javascript:;">抽奖</a>
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
					<i class="icon trs-all fa fa-gift"></i>
					<div class="fl">
						<p class="title">抽奖</p>
						<span class="desc">
							<c:if test="${weChatActivity.fDraw == true}">
								已开通
							</c:if>
							<c:if test="${weChatActivity.fDraw == false}">
								未开通
							</c:if>
						</span>
					</div>
					<div class="fr" style="padding-top: 12px;">
						<span style="float:left;line-height:25px;margin:0 10px 0 0;">开关</span>
						<label class="iosCheck">
							<input type="checkbox" id="checkbox" 
							<c:if test="${weChatActivity.fDraw == true}">checked</c:if>><i></i>
						</label>
					</div>
				</div>
				
				<div id="prize_info" class="info border-box-mode col-main-wrapper">
					<div id="addPrize">
						<table class="full-width info-table">
							<tr>
								<th class="no-e-border" width="120">奖项名</td>
								<th class="no-e-border" width="120">奖品名</td>
								<th class="no-e-border" width="130">奖品预览图片</td>
								<th class="no-e-border" width="100">中奖人数</td>
								<th width="100">操作</td>
							</tr>
							<c:forEach items="${weChatActivity.activityPrizes }" var="prize">
								<tr>
									<td class="no-e-border" width="120">${prize.prizeName }</td>
									<td class="no-e-border" width="120">${prize.description }</td>
									<td class="no-e-border" width="130">
										<img alt="点击上传,奖品图片显示在此区域" src='<c:if test="${!empty prize.img }">${ctx }/${prize.img }</c:if>'
										width="120px" height="120px" style="margin-left: -4px">
									</td>
									<td class="no-e-border" width="100">${prize.winNum }</td>
									<td width="100">
										<div style="margin-bottom: 1em;">
											<a class="ui-btn trs-all open-target" style="margin-bottom:5px"
												data-show="add-prize-dialog" id="edit-prize" href="javascript:;"
												data-data='{"prizeId": "${prize.id}", "prizeName": "${prize.prizeName}", "description": "${prize.description}", "winNum": "${prize.winNum}"}'
												data-img='${prize.img}'>编辑</a>
											<a class="ui-btn trs-all" href="${ctx }/func/delete_draw?activityMid=${weChatActivity.activityMid }&prizeId=${prize.id}">删除</a>
										</div>
									</td>
								</tr>
							</c:forEach>
						</table>
						
						<div style="margin: 10px 0px;">
							<a class="ui-btn trs-all open-target"
								data-show="add-prize-dialog" href="javascript:;">奖项增加</a>
						</div>
					</div>
					
					<div class="func-intro">
						<span class="title"><i class="fa fa-file-text-o icon"></i>功能介绍</span>
						<ul class="list">
							<li>所有已签过的参与者均可参与现场抽奖活动；通过抽奖环节可以有效的调动现场参与者的热情.</li>
							<li>抽奖功能打开必须先添加奖项,否则无法开启抽奖活动.</li>
							<li>奖项设置可以不设置图片,可以添加多个奖项，前端抽取的活动可以手动清除</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- this is dialog mask -->
	<div class="mask trs-all"></div>
	<!-- /this is dialog mask -->

	<div class="dialog trs-all" id="add-prize-dialog" style="width: 700px">
		<div class="head">
			<h1 class="title">添加奖项</h1>
			<a class="x close-target trs-all fa fa-close" href="javascript:;"></a>
		</div>
		<div class="body">
			<form id="prize-form" method="post" action="${ctx }/func/draw">
				<input type="hidden" name="activityMid" value="${weChatActivity.activityMid }">
				<input type="hidden" name="prizeId" id="prizeId" value="">
				<table class="full-width no-border">
					<tr>
						<td class="tar" width="20%">奖项名</td>
						<td><input type="text" class="ui-input trs-all" autofocus
							placeholder="奖项名" name="prizeName" id="prizeName"></td>
					</tr>
					<tr>
						<td class="tar">奖品名</td>
						<td><input type="text" class="ui-input trs-all"
							placeholder="奖品名" name="description" id="description"></td>
					</tr>
					<tr>
						<td class="tar">奖品预览图片</td>
						<td>
							<input type="hidden" id="uploadPrizeImgInput" name="img"  value="">
							<div>
								<img alt="点击上传,奖品图片显示在此区域" src="" id="upload-prize-img-show" width="120px" height="120px" style="margin-left: -4px">
							</div>
							<div class="bubble-tips-inner">大小:
								不超过2M,&nbsp;&nbsp;像素:建议(178px x 178px),&nbsp;&nbsp;格式: bmp, png, jpeg, jpg, gif
							</div>
							<div>
								<div class="add-file-btn" id="upload-prize-img-btn"> 
								     <span>上传</span> 
								     <input id="upload-prize-img-fileupload" type="file" name="mypic"> 
								</div>
								<div class="add-file-progress" id="upload-prize-img-progress"> 
						    		<span class="add-file-bar" id="upload-prize-img-bar"></span>
						    		<span class="add-file-percent" id="upload-prize-img-percent">50%</span > 
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td class="tar">中奖人数</td>
						<td><input type="text" class="ui-input trs-all"
							placeholder="中奖人数" name="winNum" id="winNum"></td>
					</tr>
				</table>
				<div class="foot">
					<button type="submit" class="ui-btn trs-all">确定</button>
					<button type="button"
						class="ui-btn ui-btn-cancel trs-all close-target">取消</button>
				</div>
			</form>
		</div>
	</div>

	<script type="text/javascript" src="${ctx}/statics/js/jquery.form.js"></script>
	<script type="text/javascript" src="${ctx}/statics/js/dialog.js?v=1.0"></script>
	<script type="text/javascript">
	$(function () {
		$(document).on('click','#checkbox',function (e) {
			var me = $(this);
			var data = {
				activityMid : "${weChatActivity.activityMid }",
				fDraw : false
			};
			if (true == $(this).prop("checked")) {
				data.fDraw = true;
			} else {
				data.fDraw = false;
			}
			$.ajax({
				url: '${ctx }/func/swith',
				data: data,
				type: 'POST',
				dataType: 'json',
				success: function (response) {
					if (response.success) {
						if (data.fDraw == true) {
							$(".func-title-wrapper .desc").html("已开通");
							alert("功能已打开");
						} else {
							$(".func-title-wrapper .desc").html("未开通");
							alert("功能已关闭");
						}
					} else {
						alert(response.message);
						if (data.fDraw == true) {
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
		var validator = $("#prize-form").validate({
	        rules: {
	        	prizeName: {
                    required: true
                },
                description: {
                    required: true
                },
                img: {
                    required: true
                },
	        	winNum: {
                    required: true,
                    number: true
                }
			},
			messages: {
				prizeName: {
					required: "请输入奖项名称!"
				},
				description: {
					required: "请输入奖品名称!"
				},
				img: {
					required: "请上传预览图片!"
				},
                winNum: {
					required: "请输入中奖人数!",
					number: "请输入数字!"
				}
			}
	    });
		
		$("#upload-prize-img-fileupload").change(function(){ //选择文件 
    		var bar = $('#upload-prize-img-bar')
			percent = $('#upload-prize-img-percent'),
			progress = $("#upload-prize-img-progress"),
			btn = $("#upload-prize-img-btn span");
			//清除前期数据
			$("#prize-form").attr("enctype", "multipart/form-data")
	        $("#prize-form").ajaxSubmit({ 
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
		                $('#upload-prize-img-show').attr("src", img);
		                btn.html("上传"); //上传按钮还原
		                $('#uploadPrizeImgInput').val(result.data.tmpPath);
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
			$("#prize-form").attr("enctype", "application/x-www-form-urlencoded");
	    }); 
		$(document).on('click','.close-target',function(e){
			validator.resetForm();
			$('#upload-prize-img-show').attr("src", "");
			$("#upload-prize-img-progress").hide();
		});
		$(document).on('click','#edit-prize',function(e){
			var me = $(this),
				prizeId = me.data('data').prizeId,
				prizeName = me.data('data').prizeName,
				description = me.data('data').description,
				img = me.data('img'),
				winNum = me.data('data').winNum;
			 $('#prizeId').val(prizeId);
			 $('#prizeName').val(prizeName);
			 $('#description').val(description);
			 $('#winNum').val(winNum);
			 $('#upload-prize-img-show').attr("src", "${ctx}/" + img);
		});
	});
	</script>
</body>
</html>