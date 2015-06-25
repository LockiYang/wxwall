<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>微信菜单设置</title>
<meta name="decorator" content="index" />
<meta name="navigation" content="list" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/statics/css/menu.css" />

</head>
<body>
	<div class="publish-container">
		<div class="page-wrapper border-box-mode" style="padding: 0;">
			<div class="col-main">
				<div class="func-title-wrapper">
					<i class="icon trs-all fa fa-gift"></i>
					<div class="fl">
						<p class="title">自定义背景</p>
						<span class="desc"> 已开通 </span>
					</div>
					<div class="fr" style="padding-top: 12px;">
						<span style="float: left; line-height: 25px; margin: 0 10px 0 0;">开关</span>
						<label class="iosCheck"> <input type="checkbox"
							id="checkbox" checked><i></i>
						</label>
					</div>
				</div>

				<div class="menu_setting_area_wrp" id="menu_container" style="">
					<div class="menu_setting_area edit">
						<p class="menu_setting_tips">可创建最多3个一级菜单，每个一级菜单下可创建最多5个二级菜单。</p>
						<div class="inner_container_box side_l cell_layout">
							<div class="inner_container_box_bd">
								<div class="inner_side">
									<div class="bd">
										<div class="menu_manage">
											<div class="sub_title_bar light">
												<h4>菜单管理</h4>
												<div class="opr_wrp">
													<a href="javascript:void(0);" id="addBt"
														class="opr_meta icon16_common add_gray">添加</a> <a
														href="javascript:void(0);" id="orderBt"
														class="opr_meta icon16_common sort_gray">排序</a> <a
														href="javascript:void(0);" id="finishBt"
														class="opr_meta btn btn_primary btn_sorting"
														style="display: none">完成</a> <a href="javascript:void(0);"
														id="cancelBt" class="opr_meta btn btn_default btn_sorting"
														style="display: none">取消</a> &nbsp;
												</div>
											</div>
											<div
												class="inner_menu_box gray with_switch ui-sortable ui-sortable-disabled"
												id="menuList">
												<dl
													class="inner_menu jsMenu ui-sortable ui-sortable-disabled">
													<dt class="inner_menu_item jslevel1" id="menu_0">
														<a href="javascript:void(0);" class="inner_menu_link"><strong>测试1</strong></a>
														<span class="menu_opr"> <a
															href="javascript:void(0);"
															class="icon14_common add_gray jsAddBt">添加</a> <a
															href="javascript:void(0);"
															class="icon14_common  edit_gray jsEditBt">编辑</a> <a
															href="javascript:void(0);"
															class="icon14_common del_gray jsDelBt">删除</a> <a
															href="javascript:void(0);"
															class="icon14_common sort_gray jsOrderBt"
															style="display: none">排序</a>
														</span>
													</dt>

													<dd class="inner_menu_item jslevel2 selected"
														id="subMenu_menu_0_0">
														<i class="icon_dot">●</i> <a href="javascript:void(0);"
															class="inner_menu_link"> <strong>测试1-1</strong>
														</a> <span class="menu_opr"> <a
															href="javascript:void(0);"
															class="icon14_common edit_gray jsSubEditBt">编辑</a> <a
															href="javascript:void(0);"
															class="icon14_common del_gray jsSubDelBt">删除</a> <a
															href="javascript:void(0);"
															class="icon14_common sort_gray jsOrderBt"
															style="display: none">排序</a>
														</span>
													</dd>

												</dl>
											</div>
										</div>
									</div>
								</div>
								<div class="inner_main">
									<div class="bd">
										<div class="action_setting">
											<div class="sub_title_bar light">
												<h4>设置动作</h4>
											</div>
											<div class="action_content default jsMain" id="none"
												style="display: none;">
												<p class="action_tips">已有子菜单，无法设置动作</p>
											</div>
											<div class="action_content init jsMain"
												style="display: block;" id="index">
												<p class="action_tips">请选择订阅者点击菜单后，公众号做出的相应动作</p>
												<a href="javascript:void(0);" id="sendMsg"><i
													class="icon_menu_action send"></i><strong>发送信息</strong></a> <a
													href="javascript:void(0);" id="goPage"><i
													class="icon_menu_action url"></i><strong>跳转到网页</strong></a>
											</div>
											<div class="action_content url jsMain" id="url"
												style="display: none;">
												<form action="" id="urlForm" onsubmit="return false;">
													<p class="action_tips" id="urlTips">订阅者点击该子菜单会跳到以下链接</p>
													<div class="frm_control_group">
														<label for="" class="frm_label">页面地址</label>
														<div class="frm_controls">
															<span class="frm_input_box disabled"> <input
																type="text" class="frm_input" id="urlText"
																name="urlText" disabled="disabled">
															</span>
															<p class="frm_tips" id="js_urlTitle"
																style="display: none;">
																来自图文消息"<span></span>"
															</p>
															<p class="frm_msg fail" style="display: none;"
																id="urlFail">
																<span for="urlText" class="frm_msg_content"
																	style="display: inline;">请输入正确的URL</span>
															</p>
														</div>
													</div>
													<div class="frm_control_group btn_appmsg_wrap">
														<div class="frm_controls">
															<a href="javascript:;" id="js_appmsgPop">从素材库图文消息中选择</a>
															<p class="frm_msg fail" style="display: none;"
																id="urlUnSelect">
																<span for="urlText" class="frm_msg_content"
																	style="display: inline;">请选择一篇文章</span>
															</p>
														</div>
													</div>
												</form>
												<div class="tool_bar">
													<a class="submit btn btn_primary" type="submit"
														id="urlSave">保存</a> <a href="javascript:void(0);"
														class="btn btn_default" id="urlBack">返回</a>
												</div>
											</div>
											<div class="action_content sended jsMain" id="view"
												style="display: none;">
												<p class="action_tips">订阅者点击该子菜单会收到以下消息</p>
												<div class="msg_wrp" id="viewDiv"></div>
												<div class="btn_wrp">
													<a href="javascript:void(0);"
														class="btn btn_default btn_editing" id="changeBt"
														style="display: none;">修改</a>
												</div>
											</div>
											<div class="action_content send jsMain" id="edit"
												style="display: none;">
												<p class="action_tips">订阅者点击该子菜单会收到以下消息</p>
												<div class="msg_sender small" id="editDiv">
													<div class="msg_tab">
														<ul class="tab_navs">

															<li class="tab_nav tab_img width4 selected" data-type="2"
																data-tab=".js_imgArea" data-tooltip="图片"><a
																href="javascript:void(0);">&nbsp;<i
																	class="icon_msg_sender"></i></a></li>

															<li class="tab_nav tab_audio width4" data-type="3"
																data-tab=".js_audioArea" data-tooltip="语音"><a
																href="javascript:void(0);">&nbsp;<i
																	class="icon_msg_sender"></i></a></li>

															<li class="tab_nav tab_video width4" data-type="15"
																data-tab=".js_videoArea" data-tooltip="视频"><a
																href="javascript:void(0);">&nbsp;<i
																	class="icon_msg_sender"></i></a></li>

															<li class="tab_nav tab_appmsg width4 no_extra"
																data-type="10" data-tab=".js_appmsgArea"
																data-tooltip="图文消息"><a href="javascript:void(0);">&nbsp;<i
																	class="icon_msg_sender"></i></a></li>

														</ul>
														<div class="tab_panel">

															<div class="tab_content">
																<div class="js_imgArea inner "></div>
															</div>

															<div class="tab_content" style="display: none;">
																<div class="js_audioArea inner "></div>
															</div>

															<div class="tab_content" style="display: none;">
																<div class="js_videoArea inner "></div>
															</div>

															<div class="tab_content" style="display: none;">
																<div class="js_appmsgArea inner "></div>
															</div>

														</div>
													</div>
												</div>
												<div class="tool_bar">
													<a href="javascript:void(0);" class="btn btn_primary"
														id="editSave">保存</a> <a href="javascript:void(0);"
														class="btn btn_default" id="editBack">返回</a>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="inner_container_box_ft" id="menuStatus" style="">
								<p class="menu_msg mini_tips warn" style="" id="menustatus_0">菜单未发布</p>
								<p class="menu_msg mini_tips warn" id="menustatus_3"
									style="display: none">待发布(还有13小时)</p>
								<p class="menu_msg mini_tips success" style="display: none"
									id="menustatus_1">菜单正在使用中</p>
								<p class="menu_tips mini_tips">编辑中的菜单需要进行发布才能更新到用户手机上</p>
							</div>
						</div>
					</div>
					<div class="tool_bar tc">
						<a href="javascript:void(0);" class="btn btn_primary" id="pubBt">发布</a>
						<a href="javascript:void(0);" class="btn btn_default" id="viewBt">预览</a>
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
						<td><input type="hidden" id="uploadPrizeImgInput" name="img"
							value="">
							<div>
								<img alt="点击上传,奖品图片显示在此区域" src="" id="upload-prize-img-show"
									width="120px" height="120px" style="margin-left: -4px">
							</div>
							<div>
								<div class="add-file-btn" id="upload-prize-img-btn">
									<span>上传</span> <input id="upload-prize-img-fileupload"
										type="file" name="mypic">
								</div>
								<div class="add-file-progress" id="upload-prize-img-progress">
									<span class="add-file-bar" id="upload-prize-img-bar"></span> <span
										class="add-file-percent" id="upload-prize-img-percent">50%</span>
								</div>
							</div></td>
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
		
	</script>
</body>
</html>