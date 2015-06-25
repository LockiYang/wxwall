<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<c:set var="stc" value="${ctx}/statics/indexs"/>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="baidu-site-verification" content="prFGDCtOL4" />
<title>V动屏-微信上墙，微信现场活动-专为互动而生</title>
<meta name="keywords" content="V动屏,微信墙,微信现场大屏幕,微活动,微抽奖,缘分对对碰,微信摇一摇游戏,签到墙,微婚庆,微信上墙,微信互动">
<meta name="description" content="V动屏网站平台，简称微信墙，是通过微信公众账号实现贯穿整个活动的互动平台。 无论是文字、表情、图文组合、语音、图片、地理位置均可实时自动上墙； 对对碰、摇一摇、抽奖、投票等微信互动.">
<link rel="icon" href="${ctx}/statics/icons/favicon.ico" type="image/x-icon" /> 
<link rel="shortcut icon" type="image/x-icon" href="${ctx}/statics/icons/favicon.ico">
<link rel="stylesheet" href="${stc }/css/css-common.css" />
<link rel="stylesheet" href="${stc }/css/css-index.css" />
<script type="text/javascript" src="${stc }/plugins/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${stc }/plugins/jquery.SuperSlide.2.1.1.js"></script>
<script type="text/javascript" src="${stc }/js/js-common.js"></script>

</head>
<body>

	<div id="header" class="header">
		<div class="width">
			<div class="fl">
				<a href="${ctx }/"> <img src="${stc }/images/index-logo.png" />
				</a>
			</div>
			<div class="fr">
				<ul>
					<li class="select"><a href="${ctx }/">首页</a></li>
<!-- 					<li><a href="#">功能模块</a></li> -->
<!-- 					<li><a href="#">随变</a></li> -->
					<shiro:guest>
						<li><a href="${ctx }/register">注册</a></li>
						<li><a href="${ctx }/login">登录</a></li>
					</shiro:guest>
					<shiro:user>
						<li><a href="${ctx }/activity/list">管理系统</a></li>
					</shiro:user>
					
					<li><a href="${ctx }/statics/upbang.html?activityMid=3c2a06f414c39f58e4e54942bfff8ccd" class="demo" target="_blank" rel="nofollow">演示</a></li>
					<p class="cl"></p>
				</ul>
			</div>
		</div>
	</div>

	<div class="fullSlide">
		<div class="bd">
			<ul>
				<li
					style="width:100%;background: url(${stc }/images/bg1.jpg) #194982 center 0 no-repeat; background-size: 100% auto">
					<a href="javascript:;"></a>
				</li>
				<li
					style="width:100%;background: url(${stc }/images/bg2.jpg) #15181e center 0 no-repeat; background-size: 100% auto">
					<a href="javascript:;"></a>
				</li>
				<li
					style="width:100%;background: url(${stc }/images/bg3.jpg) #0b0b0b center 0 no-repeat; background-size: 100% auto">
					<a href="javascript:;"></a>
				</li>
			</ul>
		</div>

		<div class="hd">
			<ul></ul>
		</div>
		<div class="timer"></div>
	</div>

	<div id="skip" class="skip">
		<div class="width">
			<ul>
				<li><a href="#purpose" class="li-purpose">目的及好处</a></li>
				<li><a href="#application" class="li-app">如何使用</a></li>
				<li><a href="#cases" class="li-module">媒体报道</a></li>
				<li><a href="#theme" class="li-module">酷炫主题</a></li>
				<li><a href="#safe" class="li-safe">数据安全</a></li>
				<li><a href="#try" class="li-try">免费体验</a></li>
				<li><a href="${ctx }/statics/upbang.html?activityMid=3c2a06f414c39f58e4e54942bfff8ccd" class="li-demo" target="_blank">演示体验</a></li>
				<p class="cl"></p>
			</ul>
		</div>
	</div>
	<div id="skip-bug" class="skip-bug no">&nbsp;</div>
	<div class="width top-line">&nbsp;</div>

	<div id="purpose" class="purpose">
		<!-- 使用微信墙互动的目的 -->
		<div class="width">
			<dl class="block-title">
				<dt>
					<img src="${stc }/images/index-purpose.png" />
				</dt>
				<dd>为什么要使用V动屏</dd>
				<p class="cl"></p>
			</dl>
			<ul class="block-content">
				<li>
					<div class="tit">吸引现场人员提升现场气氛</div>
					<div class="pic">
						<img src="${stc }/images/purpose1.jpg" />
					</div>
					<div class="desc">
						通过微信来搭建现场互动墙，简单的操作及高效、方便的互动形式吸引现场用户参与到活动
						<h2>微信互动</h2>
						中来，在提升现场气氛的同时加深参与者对活动的印象，从而提高活动传播与影响力。
					</div>
				</li>
				<li>
					<div class="tit">汇集最真实有效的用户</div>
					<div class="pic">
						<img src="${stc }/images/purpose2.jpg" />
					</div>
					<div class="desc">使用您自己的微信公众账号，现场所有互动参与者都将转变为您的关注者即粉丝，在互动的同时增加了公众平台的用户数，并且这些参与者都将是真实、有效的潜在客户！</div>
				</li>
				<li>
					<div class="tit">现场大屏幕双屏同步更灵活</div>
					<div class="pic">
						<img src="${stc }/images/purpose3.jpg" />
					</div>
					<div class="desc">
						通过微信发送文字、表情、图文组合、语音、图片、地理位置等均可自动实时展示到现场微信大屏幕上！认证及非认证微信订阅号服务号均可。
					</div>
				</li>
				<li>
					<div class="tit">
						根本停不下来的
						<h2>现场互动</h2>
					</div>
					<div class="pic">
						<img src="${stc }/images/purpose4.jpg" />
					</div>
					<div class="desc">
						上墙、抽奖、对对碰、摇一摇、大转盘等等
						<h2>微信互动</h2>
						方式，微信互动游戏你想怎么玩都可以，彻底惊呆现场小伙伴，多样的互动方式让现场互动根本停不下来！
					</div>
				</li>
				<p class="cl"></p>
			</ul>
		</div>
	</div>
	
	<div class="width top-line">&nbsp;</div>

	<div id="cases" class="cases">
		<div class="width">
			<dl class="block-title">
				<dt>
					<img src="${stc }/images/index-application.png" />
				</dt>
				<dd>媒体报道</dd>
				<p class="cl"></p>
			</dl>
			<div style="text-align: center; margin: 0 0 20px 0;">
				<embed height="300" width="600" type="application/x-shockwave-flash"
					align="middle"
					src="http://static.video.qq.com/TPout.swf?vid=t0129cs2anp&amp;auto=0"
					allowscriptaccess="always" quality="high" allowfullscreen="true">
			</div>
		</div>
	</div>
	
	<div class="width top-line">&nbsp;</div>

	<div id="application" class="application">
		<!-- 如何应用走你大屏幕实现现场互动 -->
		<div class="width">
			<dl class="block-title">
				<dt>
					<img src="${stc }/images/index-application.png" />
				</dt>
				<dd>如何使用V动屏进行现场互动</dd>
				<p class="cl"></p>
			</dl>
			<ul class="content">
				<li class="pcs-1"><img src="${stc }/images/app1.png" /> <font>1、微信公众账号</font>微信订阅号、服务号均可使用</li>
				<li class="pcs-2"><img src="${stc }/images/app2.png" /><font>2、搭建显示设备</font>活动现场只须准备一台连网电脑，将电脑与现场显示设备（投影或LED屏）连接即可</font></li>
				<li class="pcs-3"><img src="${stc }/images/vdongpingticket.jpg" /><font>3、现场互动</font>参与者只需关注活动主办方微信公号即可参与整个微信互动</li>
				<p class="cl"></p>
			</ul>
		</div>
	</div>

	<div class="width top-line">&nbsp;</div>

	<div id="theme" class="module">
		<!-- 随变 -->
		<div class="width">
			<dl class="block-title">
				<dt>
					<img src="${stc }/images/index-theme.png" />
				</dt>
				<dd>更多酷炫主题，自定义主题，想换就换</dd>
				<p class="cl"></p>
			</dl>
			<ul class="block-content">
				<li>
					<div class="tit">
						<a href="javascript:;" title="草绿主题" target="_blank">羊年快乐主题</a>
					</div>
					<div class="pic">
						<a href="javascript:;" target="_blank"> <img
							src="${stc }/images/cl.jpg" /></a>
					</div>
					<div style="height: 30px;">&nbsp;</div>
				</li>
				<li>
					<div class="tit">
						<a href="javascript:;" title="默认大红主题" target="_blank">2014圣诞节主题</a>
					</div>
					<div class="pic">
						<a href="javascript:;" target="_blank"> <img
							src="${stc }/images/mrdh.jpg" /></a>
					</div>
					<div style="height: 30px;">&nbsp;</div>
				</li>
				<li>
					<div class="tit">
						<a href="javascript:;" title="砰然心动主题" target="_blank">牛郎织女节主题</a>
					</div>
					<div class="pic">
						<a href="javascript:;" target="_blank"> <img
							src="${stc }/images/prxd.jpg" /></a>
					</div>
					<div style="height: 30px;">&nbsp;</div>
				</li>
				<li>
					<div class="tit">
						<a href="javascript:;" title="天蓝主题" target="_blank">未来在前方主题</a>
					</div>
					<div class="pic">
						<a href="javascript:;" target="_blank"> <img
							src="${stc }/images/tl.jpg" /></a>
					</div>
					<div style="height: 30px;">&nbsp;</div>
				</li>
				<li>
					<div class="tit">
						<a href="javascript:;" title="夕夕相映主题" target="_blank">超炫红主题</a>
					</div>
					<div class="pic">
						<a href="javascript:;" target="_blank"> <img
							src="${stc }/images/xxxy.jpg" /></a>
					</div>
					<div style="height: 30px;">&nbsp;</div>
				</li>
				<li>
					<div class="tit">
						<a href="javascript:;" title="喜庆大红主题" target="_blank">2014巴西世界杯主题</a>
					</div>
					<div class="pic">
						<a href="javascript:;" target="_blank"> <img
							src="${stc }/images/xqdh.jpg" /></a>
					</div>
					<div style="height: 30px;">&nbsp;</div>
				</li>
				<p class="cl"></p>
			</ul>
		</div>
	</div>

	<div class="width top-line">&nbsp;</div>

	<div id="safe" class="safe">
		<!-- 安全 -->
		<div class="width">
			<dl class="block-title">
				<dt>
					<img src="${stc }/images/index-safe.png" />
				</dt>
				<dd>全方位数据安全保障</dd>
				<p class="cl"></p>
			</dl>
			<ul class="content">
				<li><p>
						<img src="${stc }/images/safe1.png" />硬件防火墙抗DDoS攻击
					</p>阿里云千兆级硬防机房提供网络监控及保护，抵御任何恶意网络攻击；保障千人级大型活动现场互动不受影响。</li>
				<li class="arrow">&nbsp;</li>
				<li><p>
						<img src="${stc }/images/safe2.png" />异地数据实时备份
					</p>活动数据将实时多地云存储备份，为活动结束后进行活动报表、关键内容分析提供保障。</li>
				<li class="arrow">&nbsp;</li>
				<li><p>
						<img src="${stc }/images/safe3.png" />云服务器瞬间切换
					</p>每一次的活动都会有两台不同机房的服务器同时运行，遇到服务器故障可智能瞬间切换服务器，保证现场活动不受影响。</li>
				<li class="arrow">&nbsp;</li>
				<li><p>
						<img src="${stc }/images/safe4.png" />活动数据屏蔽
					</p>除活动主办者可登陆活动管理中心查看微信互动数据外，通过其它任何途径均无法查看活动数据，保证活动信息的保密性、安全性。</li>
				<p class="cl"></p>
			</ul>
			<div class="down"></div>
		</div>
	</div>

	<div class="width top-line">&nbsp;</div>

	<div id="try" class="try">
		<!-- 试用 -->
		<div class="width">
			<dl class="block-title">
				<dt>
					<img src="${stc }/images/index-try.png" />
				</dt>
				<dd>免费体验流程</dd>
				<p class="cl"></p>
			</dl>
			<ul class="content">
				<li><p>
						<img src="${stc }/images/vdongpingticket.jpg" />1.关注公号
					</p>微信扫描上方二维码或查找公众号"V动屏"、"vdongping"搜索并关注"V动屏"来参与<strong>V动屏</strong>所有功能的试用；<img
					src="${stc }/images/try-signal-1.jpg" class="img" /></li>
				<li class="arrow">&nbsp;</li>
				<li><p>
						<img src="${stc }/images/try3.jpg" />2.微信上墙与互动
					</p>在微信"服务号"中找到"V动屏"，发送文字、图片、表情、图文组合均可完成<strong>微信上墙</strong>，上墙成功后可参所有互动；<img
					src="${stc }/images/try-signal-3.jpg" class="img" /></li>
				<li class="arrow">&nbsp;</li>
				<li><p class="enter">
						<a href="#" target="_blank" rel="nofollow"><span>3.进入大屏幕查看</span></a>
					</p>建议打开大屏幕界面后按F11进行全屏浏览；界面底部有隐藏按钮，点击可打开控制栏，点击各功能模块名称可切换至各模块浏览操作。</li>
				<p class="cl"></p>
			</ul>
			<div class="down"></div>
		</div>
	</div>

	<script type="text/javascript" src="${stc }/js/js-index.js" ></script>

	<div id="footer" class="footer">
		<div class="width">
			<dl class="row1 fl">
				<dt>微信现场互动方案</dt>
				<dd>
					<ul>
						<li><a href="javascript:;"><img src="${stc }/images/ihow_item_hl.png"
								width="40" />婚礼婚庆</a></li>
						<li><a href="javascript:;"><img src="${stc }/images/ihow_item_xq.png"
								width="40" />相亲联谊</a></li>
						<li><a href="javascript:;"><img src="${stc }/images/ihow_item_nh.png"
								width="40" />企业年会</a></li>
						<li><a href="javascript:;"><img src="${stc }/images/ihow_item_gg.png"
								width="40" />公关活动</a></li>
						<li><a href="javascript:;"><img src="${stc }/images/ihow_item_zh.png"
								width="40" />展览展示</a></li>
						<li><a href="javascript:;"><img src="${stc }/images/ihow_item_hy.png"
								width="40" />会议论坛</a></li>
						
					</ul>
				</dd>
			</dl>
			<dl class="row2 fl">
				<dt>功能模块</dt>
				<dd>
					<ul>
						<li><a href="javascript:;">签到墙-行业首创</a></li>
						<li><a href="javascript:;">微信墙</a></li>
						<li><a href="javascript:;">抽奖</a></li>
						<li><a href="javascript:;">摇一摇</a></li>
						<li><a href="javascript:;">对对碰</a></li>
						<li><a href="javascript:;">自定义场景</a></li>
						<li><a href="javascript:;">日程推送-行业首创</a></li>
					</ul>
				</dd>
			</dl>
			<dl class="row3 fl">
				<dt>联系方式</dt>
				<dd>
					深圳市宝安区石岩街道宝石东293号璧辉大厦603<br />公司：深圳汇奇思科技有限公司<br />手机：18676719508(甘先生)<br />邮箱：ganjunzx@outlook.com<br />合作：ganjunzx@outlook.com<br />
					<br /> <img src="${stc }/images/vdongpingticket.jpg" /><br />（官方微信公众账号）
				</dd>
			</dl>
<!-- 			<dl class="row4 fl"> -->
<!-- 				<dt>&nbsp;</dt> -->
<!-- 				<dd> -->
<!-- 					<div class="map"> -->
<%-- 						<img src="${stc }/images/baidumap.png" /> --%>
<!-- 					</div> -->
<!-- 					<ul> -->
<!-- 						<li></li> -->
<!-- 					</ul> -->
<!-- 				</dd> -->
<!-- 			</dl> -->
			<p class="cl"></p>
<!-- 			<div class="copyright">版权所有 Copyright©2013-2015 Vdongping.com All -->
<!-- 				Rights Reserved</div> -->
		</div>
	</div>
</body>

</html>