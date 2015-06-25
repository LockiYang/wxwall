// come from jQuery Easing v1.3 - http://gsgd.co.uk/sandbox/jquery/easing/
$.extend($.easing, {
	easeOutBack : function(x, t, b, c, d, s) {
		if (s == undefined)
			s = 1.70158;
		return c * ((t = t / d - 1) * t * ((s + 1) * t + s) + 1) + b;
	}
});

$.ajaxSetup({
	cache : false,
	error : function(response) {
		console.log(response)
	}
});

(function($, document) {

	var msgWallTimer,funcSwitchs, speed = 500, timeout = 3000, switchSpeed = 500, // 重播等待时间
	switchTimeout = 1000 * 8, easing = 'easeOutBack', // 缓动
	play = false, timestamp = null, fullScreen = false, playEnabled = true, // 该标志用于防止鼠标狂点
	themePreviewShown = false, docElm = document.documentElement, activityMid = util
			.getQueryString('activityMid')
			|| -1, fullscreenchangeTypes = 'fullscreenchange msfullscreenchange mozfullscreenchange webkitfullscreenchange';

	var preventDefault = function(e) {
		e.preventDefault();
	};

	var stopPropagation = function(e) {
		e.stopPropagation();
	};
	var upbang = {
		init : function() {
			this.cacheElements();
			this.loadFuncSwitchs();
			this.loadHeadInfo();
			this.hideQrCode();
			this.play();
			this.bindEvents();
			this.loadThemePreviewList();
		},
		cacheElements : function() {
			this.$upbangMsgList = $('#upbang-msg-list');
			this.$upbangHeader = $('#upbang-header');
			this.$upbangFooter = $('#upbang-footer');
			this.$upbangBox = $('#upbang-box');
			this.$themePreview = $('#theme-preview');
			this.$themePreviewList = $('#theme-preview-list');
			this.$qrCode = $('#qr-code');
			this.$bigQrCode = $('#big-qr-code');
			this.$upbangMsgTotal = $('#upbang-msg-total');
			this.$logoPic = $('#logo-pic');
			this.$screenControlBtns = $('#screen-control-btns');
			this.$playOrPause = this.$upbangFooter.find('.play-or-pause');
			this.$fullOrNomal = this.$upbangFooter.find('.full-or-nomal');
			this.$upbangBoxItems = this.$upbangBox.find('.upbang-box-item');
		},
		bindEvents : function() {
			this.$upbangFooter.on('click', '.btn-old', upbang.first);
			this.$upbangFooter.on('click', '.btn-prev', upbang.prev);
			this.$upbangFooter.on('click', '.btn-next', upbang.next);
			this.$upbangFooter.on('click', '.btn-new', upbang.last);
			this.$upbangFooter.on('click', '.play-or-pause', upbang.togglePlay);
			this.$upbangFooter.on('click', '.btn-full', upbang.toggleFullScreen);
			this.$upbangFooter.on('click', 'a', upbang.switchBoxItem);
			this.$upbangFooter.on('click', '.btn-style', upbang.toggleThemePreview);
			this.$upbangFooter.on('click', '.btn-style', stopPropagation);
			$(document).on('click', '.theme-preview-item', upbang.themeClick);
			$(document).on('click', '.theme-preview', stopPropagation);
			$(document).on('click', upbang.hideThemePreview);
			$(document).on('click', '.refresh-theme-preview', upbang.loadThemePreviewList);
			$(document).on('click', '.close-theme-preview', upbang.hideThemePreview);
			$(document).on('click', '#qr-code', upbang.showQrCode);
			$(document).on('click', '#qr-code-mask', upbang.hideQrCode);
			$(document).on('click', '#big-qr-code', stopPropagation);
			// $(document).on('keyup', upbang.keyF11ToChange);
			// $(document).on(fullscreenchangeTypes, upbang.fullscreenchange);
		},
		setHeadInfo : function(info) {
			if (info.fLogo == false) {
				upbang.$logoPic.attr('src', 'images/logo.png');
			} else {
				upbang.$logoPic.attr('src', info.logo);
			}

			upbang.$bigQrCode.find('img').attr("src", info.ticket);
			upbang.$qrCode.attr("src", info.ticket);
			upbang.$upbangMsgTotal.html(info.totalUpbangMsgNum);

			if (info.appType == 3) {
				var qrCodeMsg = "<h1>互动步骤</h1>"
					+"<h2>1、扫码签到：<br>&#12288;&#12288;扫描活动二维码进行签到</h2>"
					+"<h2>2、发送内容：<br>&#12288;&#12288;发送文字、表情、图片、图文组合即有机会上墙参与互动</h2>";
				upbang.$bigQrCode.find('.rule-msgs').html(qrCodeMsg);
			} else {
				var qrCodeMsg = "<h1>互动步骤</h1>"
					+"<h2>1、关注公众号：<br>&#12288;&#12288;扫描二维码或搜索"+ info.appName +"关注公众号</h2>"
					+"<h2>2、活动签到：<br>&#12288;&#12288;从活动列表选择活动，输入"+ info.secret +"进行签到</h2>"
					+"<h2>3、发送内容：<br>&#12288;&#12288;发送文字、表情、图片、图文组合即有机会上墙参与互动</h2>";
				upbang.$bigQrCode.find('.rule-msgs').html(qrCodeMsg);
			}
			
			
			var tpl = '<li class="msg-item{{if text.length <= 12}} subject{{/if}}">${text}</li>';
			$.template('html', tpl);
			$.tmpl('html', info.switchMsgItems).appendTo('#msg-switch');
			upbang.msgSwitch('#msg-switch');
		},
		showQrCode : function() {
			$('#qr-code-mask').show();
		},
		hideQrCode : function() {
			$('#qr-code-mask').hide();
		},
		loadFuncSwitchs : function() {
			$.ajax({
				url : '../activity-screen/get_func_switch?mid=' + activityMid,
				dataType : 'json',
				success : function(response) {
					if (!response) {
						upbang.ifActivityNotExist();
					}
					if (response.success) {
						var switchs = response.data;
						if (switchs.fexist) {
							funcSwitchs = switchs;
						} else {
							upbang.ifActivityNotExist();
						}
					} else {
						upbang.ifActivityNotExist();
					}
				}
			})
		},
		ifActivityNotExist : function() {
			alert('此活动不存在，请从活动后台打开链接！');
			window.location.href="http://www.vdongping.com";
		},
		loadHeadInfo : function() {
			$.ajax({
				url : '../activity-screen/head-info?activityMid=' + activityMid,
				dataType : 'json',
				success : function(response) {
					if (!response) {
						return
					}
					;
					upbang.setHeadInfo(response.data);
				}
			})
		},
		msgSwitch : function(container) { // 头部消息切换
			var msgItems = $(container).find('.msg-item');
			var msgSize = msgItems.size();
			var next = 0;
			var showNextMsg = function() {
				msgItems.css({
					'opacity' : 0
				});
				msgItems.eq(next).animate({
					opacity : 1
				}, switchSpeed);
				next++;
				if (next == msgSize) {
					next = 0
				}
				;
				setTimeout(showNextMsg, switchTimeout);
			}
			showNextMsg();
		},
		loadUpbangMsg : function() {
			var url = '../activity-screen/upbang-msg?activityMid=' + activityMid;
			if (timestamp != null && timestamp != '') {
				url = url + '&updateTime=' + timestamp;
			}

			$.ajax({
				url : url,
				dataType : 'json',
				success : function(response) {
					if (!response.data) {
						return
					};
					timestamp = response.data.updateTime;
					upbang.$upbangMsgTotal.html(response.total);
					upbang.putUpbangMsg(response.data, upbang.$upbangMsgList);
				}
			})
		},
		putUpbangMsg : function(data, container, putAfter) {
			var tpl = '';
			data = data || {};
			tpl = '<div class="upbang-msg cur clearfix">'
					+ '<div class="upbang-avatar tooltipped tooltipped-s fl" aria-label="'
					+ data.nick
					+ '">'
					+ '<img src="'
					+ data.avatar
					+ '">'
					+ '</div>'
					+ '<div class="upbang-msg-content">'
					+ '<span class="pos-rel">'
					+ (data.messagePhoto ? '<img src="'
							+ data.messagePhoto + '">' : data.messageText) + '</span>' + '</div>'
					+ '</div>';
			container.find('.upbang-msg').removeClass('cur');
			var childLen = container.find('.upbang-msg').length;
			if (childLen == 50) {//默认只保留50条信息
				container.find('.upbang-msg:last-child').remove();
			}
			if (putAfter) {
				$(tpl).appendTo(container);
			} else {
				$(tpl).prependTo(container);
				container.css(
						{
							top : -container.find('.upbang-msg').eq(0)
									.outerHeight()
									+ 'px'
						}).animate({
					top : 0
				}, speed, easing, function(data, container, putAfter) {

				});
			}
		},
		next : function() {
			var $upbangMsgList = upbang.$upbangMsgList, $cur = $upbangMsgList
					.find('.upbang-msg.cur'), scrollHeight = $cur.outerHeight()
					+ parseInt($cur.css('margin-top'))
					+ parseInt($cur.css('margin-bottom')), containerTop = parseInt($upbangMsgList
					.css('top'));

			if ($cur.index() && playEnabled) {
				upbang.pause();
				playEnabled = false;
				$cur.removeClass('cur').prev().addClass('cur');
				$upbangMsgList.animate({
					top : containerTop + scrollHeight
				}, speed, easing, function() {
					playEnabled = true;
					upbang.play();
				});
			}
		},
		prev : function() {
			var $upbangMsgList = upbang.$upbangMsgList, $cur = $upbangMsgList
					.find('.upbang-msg.cur'), scrollHeight = $cur.outerHeight()
					+ parseInt($cur.css('margin-top'))
					+ parseInt($cur.css('margin-bottom')), containerTop = parseInt($upbangMsgList
					.css('top'));

			if ($cur.next().size() && playEnabled) {
				upbang.pause();
				playEnabled = false;
				$cur.removeClass('cur').next().addClass('cur');
				$upbangMsgList.animate({
					top : containerTop - scrollHeight
				}, speed, easing, function() {
					playEnabled = true;
					upbang.play();
				});
			}
		},
		first : function() {
			upbang.pause();
			var $upbangMsgList = upbang.$upbangMsgList, $cur = $upbangMsgList
					.find('.upbang-msg.cur'), $last = $upbangMsgList
					.find('.upbang-msg:last'), containerHeight = $upbangMsgList
					.outerHeight();

			if ($cur.next().size()) {
				$upbangMsgList.animate({
					top : -containerHeight + $last.outerHeight()
							+ parseInt($last.css('margin-top'))
							+ parseInt($last.css('margin-bottom'))
				}, speed, easing);
				$cur.removeClass('cur');
				$last.addClass('cur');
			}

			upbang.play();
		},
		last : function() {
			upbang.pause();
			var $upbangMsgList = upbang.$upbangMsgList, $cur = $upbangMsgList
					.find('.upbang-msg.cur'), $first = $upbangMsgList
					.find('.upbang-msg:first');

			if ($cur.index()) {
				$upbangMsgList.animate({
					top : 0
				}, speed, easing);
				$cur.removeClass('cur');
				$first.addClass('cur');
			}

			upbang.play();
		},
		changeToPlayState : function() {
			upbang.$playOrPause.addClass('btn-begin');
			upbang.$playOrPause.removeClass('btn-pause');
			play = true;
		},
		changeToPauseState : function() {
			upbang.$playOrPause.addClass('btn-pause');
			upbang.$playOrPause.removeClass('btn-begin');
			play = false;
		},
		play : function() {
			msgWallTimer = setInterval(function() {
				upbang.loadUpbangMsg();
			}, timeout);
			upbang.changeToPlayState();
		},
		replay : function() { // 重新播放
			upbang.loadUpbangMsg();
			upbang.play();
		},
		pause : function() {
			clearInterval(msgWallTimer);
			upbang.changeToPauseState();
		},
		togglePlay : function() { // 播放OR暂停事件
			play ? upbang.pause() : upbang.replay();
		},
		fullscreenchange : function(e) {
			if (fullScreen) {
				upbang.changeToNomalScreenState();
			} else {
				upbang.changeToFullScreenState();
			}
		},
		keyF11ToChange : function(e) {
			if (e.keyCode == 122) {
				upbang.fullscreenchange(e)
			}
		},
		fullScreenForIELowVersion : function() {
			var WsShell = new ActiveXObject('WScript.Shell');
			WsShell.SendKeys('{F11}');
		},
		changeToFullScreenState : function() {
			upbang.$fullOrNomal.addClass('fa-compress');
			fullScreen = true;
		},
		changeToNomalScreenState : function() {
			upbang.$fullOrNomal.removeClass('fa-compress');
			fullScreen = false;
		},
		fullScreen : function() {
			// thanks robnyman http://robnyman.github.io/fullscreen/
			// Fullscreen API https://fullscreen.spec.whatwg.org/
			if (docElm.requestFullscreen) {
				docElm.requestFullscreen();
			} else if (docElm.msRequestFullscreen) {
				docElm.msRequestFullscreen();
			} else if (docElm.mozRequestFullScreen) {
				docElm.mozRequestFullScreen();
			} else if (docElm.webkitRequestFullScreen) {
				docElm.webkitRequestFullScreen();
			} else {
				upbang.fullScreenForIELowVersion();
			}
			upbang.changeToFullScreenState();
		},
		exitFullscreen : function() {
			if (document.exitFullscreen) {
				document.exitFullscreen();
			} else if (document.msExitFullscreen) {
				document.msExitFullscreen();
			} else if (document.mozCancelFullScreen) {
				document.mozCancelFullScreen();
			} else if (document.webkitCancelFullScreen) {
				document.webkitCancelFullScreen();
			} else {
				upbang.fullScreenForIELowVersion();
			}
			upbang.changeToNomalScreenState();
		},
		toggleFullScreen : function() {
			fullScreen ? upbang.exitFullscreen() : upbang.fullScreen();
		},
		getById : function(id) {
			return $('#' + id);
		},
		switchBoxItem : function() { //菜单栏点击切换事件
			
			var $me = $(this), path = 'js/', action = $me.data('action'), loadedMsg = ' loaded success', scriptUrl = {
				luckyDraw : path + 'lucky-draw.js',
				pairing : path + 'pairing.js',
				shake : path + 'shake.js',
				rote : path + 'rote.js',
				signin: path + 'signin.js',
				photopile: path + 'photopile.js'
			};

			if (action) {
				
				if (action === 'upbang-msg-list') {
					//window.signInTimer = false;
					if (play != true) {
						upbang.$screenControlBtns.show();
						upbang.replay();
					}
				}

				if (action === 'lucky-draw') {
					if (funcSwitchs.fdraw) {
						upbang.pause();
						upbang.$screenControlBtns.hide();
						//window.signInTimer = false;
						if (!window.luckyDrawLoaded) {
							$.getScript(scriptUrl.luckyDraw, function(script) {
								console.log(scriptUrl.luckyDraw + loadedMsg);
							});
						}
					} else {
						alert("请从活动管理后台开启抽奖功能！");
						return;
					}
					
				}
				if (action === 'pairing') {
					if (funcSwitchs.fpair) {
						upbang.pause();
						upbang.$screenControlBtns.hide();
						//window.signInTimer = false;
						if (!window.pairingLoaded) {
							$.getScript(scriptUrl.pairing, function(script) {
								console.log(scriptUrl.pairing + loadedMsg);
							});
						}
					} else {
						alert("请从活动管理后台开启对对碰功能！");
						return;
					}
					
				}
				if (action === 'shake') {
					if (funcSwitchs.fshake) {
						upbang.pause();
						upbang.$screenControlBtns.hide();
						//window.signInTimer = false;
						if (!window.shakeLoaded) {
							$.getScript(scriptUrl.shake, function(script) {
								console.log(scriptUrl.shake + loadedMsg);
							});
						}
					} else {
						alert("请从活动管理后台开启摇一摇功能！");
						return;
					}
					
				}
				
				if (action === 'photopile') {
					upbang.pause();
					upbang.$screenControlBtns.hide();
					//window.signInTimer = false;
					if (!window.photopileLoaded) {
						$.getScript(scriptUrl.photopile, function(script) {
							console.log(scriptUrl.photopile + loadedMsg);
						});
					} else {
						window.photopile.startPhotoPile();
					}
				} else {
					window.photopile.stopPhotoPile();
				}
				
				if (action === 'rote') {
					upbang.pause();
					upbang.$screenControlBtns.hide();
					window.signInTimer = false;
					if (!window.roteLoaded) {
						$.getScript(scriptUrl.rote, function(script) {
							console.log(scriptUrl.rote + loadedMsg);
						});
					}
				}
				
				if (action === 'signin') {
					if (funcSwitchs.fsignin) {
						upbang.pause();
						upbang.$screenControlBtns.hide();
						window.signInTimer = true;
						if (!window.signInLoaded) {
							$.getScript(scriptUrl.signin, function(script) {
								console.log(scriptUrl.signin + loadedMsg);
							});
						}
					} else {
						alert("请从活动管理后台开启签到功能！");
						return;
					}
					$('.screen-box').css({overflow:'visible'});
					
				}else{
					$('.screen-box').css({overflow:'hidden'});
					window.signInTimer = false;
				}
				
				upbang.$upbangBoxItems.removeClass('visible');
				upbang.getById(action).addClass('visible');
				
			}
		},
		loadThemePreviewList : function() { //主题列表
			$.ajax({
						url : '../activity-screen/theme-preview-list?activityMid='
								+ activityMid,
						dataType : 'json',
						success : function(response) {
							items = response.items || {};
							var tpl = '<a class="theme-preview-item" href="javascript:;" id="${id}" data-src="${src}" aria-label="${name}">'
									+ '<img class="trs-all" src="${src}">'
									+ '</a>';
							$.template('html', tpl);
							var $list = upbang.$themePreviewList, themeOption = store
									.get('themeOption');
							$list.html($.tmpl('html', items));
							if (themeOption) {
								upbang
										.setTheme(themeOption.id,
												themeOption.src);
							}
						}
					})
		},
		setTheme : function(id, src) {
			var $list = upbang.$themePreviewList;
			$list.find('.theme-preview-item').removeClass('cur');
			$list.find('#' + id).addClass('cur');
			$(document.body).css({
				backgroundImage : 'url(' + src + ')'
			});
		},
		themeClick : function() {
			var $me = $(this), id = $me.attr('id'), src = $me.data('src');
			console.log(id, src)
			store.set('themeOption', {
				id : id,
				src : src
			});
			upbang.setTheme(id, src);
		},
		showThemePreview : function() {
			upbang.$themePreview.addClass('visible');
			themePreviewShown = true;
		},
		hideThemePreview : function() {
			upbang.$themePreview.removeClass('visible');
			themePreviewShown = false;
		},
		toggleThemePreview : function() {
			themePreviewShown ? upbang.hideThemePreview() : upbang
					.showThemePreview();
		}
	};
	
	$('.hide-icon').click(function(){
		$(this).closest('#upbang-footer').hide();
		$('.show-icon').show();
	});
	$('.show-icon').click(function(){
		$('#upbang-footer').show();
		$(this).hide();
	});

	upbang.init();

})(jQuery, document);