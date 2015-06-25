
// come from jQuery Easing v1.3 - http://gsgd.co.uk/sandbox/jquery/easing/
$.extend($.easing,{
	easeOutBack: function (x, t, b, c, d, s) {
		if (s == undefined) s = 1.70158;
		return c*((t=t/d-1)*t*((s+1)*t + s) + 1) + b;
	}
});

$.ajaxSetup({
	cache: false,
	error: function (response) {
		console.log(response)
	}
});

(function ($,document) {

	var requestTimer,
		speed = 500,
		timeout = 4000,
		switchSpeed = 500,
		replayWaiting = 1000, // 重播等待时间
		switchTimeout = 1000*8,
		easing = 'easeOutBack', // 缓动
		play = false,
		timestamp = null,
		fullScreen = false,
		playEnabled = true, // 该标志用于防止鼠标狂点
		themePreviewShown = false,
		docElm = document.documentElement,
		activityMid = util.getQueryString('activityMid') || -1,
		fullscreenchangeTypes = 'fullscreenchange msfullscreenchange mozfullscreenchange webkitfullscreenchange';

	var preventDefault = function (e) {
		e.preventDefault();
	};

	var stopPropagation = function (e) {
		e.stopPropagation();
	};

	var upbang = {
		init: function () {
			this.cacheElements();
			this.loadHeadInfo();
			this.loadUpbangMsgList();
			this.loadThemePreviewList();
			this.bindEvents();
		},
		cacheElements: function () {
			this.$upbangMsgList = $('#upbang-msg-list');
			this.$upbangHeader = $('#upbang-header');
			this.$upbangFooter = $('#upbang-footer');
			this.$upbangBox = $('#upbang-box');
			this.$themePreview = $('#theme-preview');
			this.$themePreviewList = $('#theme-preview-list');
			this.$playOrPause = this.$upbangFooter.find('.play-or-pause');
			this.$fullOrNomal = this.$upbangFooter.find('.full-or-nomal');
			this.$upbangBoxItems = this.$upbangBox.find('.upbang-box-item');
		},
		bindEvents: function () {
			this.$upbangFooter.on('click', '.first', upbang.first);
			this.$upbangFooter.on('click', '.prev', upbang.prev);
			this.$upbangFooter.on('click', '.next', upbang.next);
			this.$upbangFooter.on('click', '.last', upbang.last);
			this.$upbangFooter.on('click', '.play-or-pause', upbang.togglePlay);
			this.$upbangFooter.on('click', '.full-or-nomal', upbang.toggleFullScreen);
			this.$upbangFooter.on('click', 'a', upbang.switchBoxItem);
			this.$upbangFooter.on('click', '.show-or-hide', upbang.toggleThemePreview);
			this.$upbangFooter.on('click', '.show-or-hide', stopPropagation);
			$(document).on('click', '.theme-preview-item', upbang.themeClick);
			$(document).on('click', '.theme-preview', stopPropagation);
			$(document).on('click', upbang.hideThemePreview);
			$(document).on('click', '.refresh-theme-preview', upbang.loadThemePreviewList);
			$(document).on('click', '.close-theme-preview', upbang.hideThemePreview);
			// $(document).on('keyup', upbang.keyF11ToChange);
			// $(document).on(fullscreenchangeTypes, upbang.fullscreenchange);
		},
		setHeadInfo: function (info) {
			$('#logo-pic').attr('src',info.logoSrc);
			$('#qr-code').attr('src',info.qrCodeSrc);
			$('#upbang-msg-total').html(info.upbangMsgTotal);

			var tpl = '<li class="msg-item">${text}</li>';
			$.template('html',tpl);
			$.tmpl('html',info.switchMsgItems).appendTo('#msg-switch');
			upbang.msgSwitch('#msg-switch');
			
		},
		loadHeadInfo: function () {
			$.ajax({
				url: 'json/head-info.json?activityMid=' + activityMid,
				dataType: 'json',
				success: function (response) {
					if (!response) { return };
					upbang.setHeadInfo(response.data);
				}
			})
		},
		msgSwitch: function (container) {
			var msgItems = $(container).find('.msg-item');
			var msgSize = msgItems.size();
			var next = 0;
			var showNextMsg = function () {
				msgItems.css({'opacity':0});
				msgItems.eq(next).animate({
					opacity: 1
				}, switchSpeed);
				next++;
				if (next == msgSize) { next = 0 };
				setTimeout(showNextMsg,switchTimeout);
			}
			showNextMsg();
		},
		loadUpbangMsg: function () {
			$.ajax({
				url: 'json/upbang-msg.json?activityMid=' + activityMid + '&timestamp=' + timestamp,
				dataType: 'json',
				success: function (response) {
					if (!response) { return };
					upbang.putUpbangMsg(response.data,upbang.$upbangMsgList);
				}
			})
		},
		loadUpbangMsgList: function () {
			$.ajax({
				url: 'json/upbang-msg-list.json?activityMid=' + activityMid,
				dataType: 'json',
				success: function (response) {
					if (!response.items) { return };
					upbang.putUpbangMsgList(response.items,upbang.$upbangMsgList);
					timestamp = response.items[0].createTime;
					upbang.loadUpbangMsg();
					upbang.play();
				}
			})
		},
		putUpbangMsg: function (data,container,putAfter) {
			var tpl = '';
			data = data || {};
			tpl
			= '<div class="upbang-msg cur clearfix">'
			+ '<div class="upbang-avatar tooltipped tooltipped-s fl" aria-label="' + data.nick + '">'
			+ '<img src="' + data.avatar + '">'
			+ '</div>'
			+ '<div class="upbang-msg-content">'
			+ '<span class="pos-rel">' + (data.messageText ? data.messageText : '<img src="' + data.messagePhoto + '">') + '</span>'
			+ '</div>'
			+ '</div>';
			container.find('.upbang-msg').removeClass('cur');
			if (putAfter) {
				$(tpl).appendTo(container);
			} else {
				$(tpl).prependTo(container);
				container.css({
					top: -container.find('.upbang-msg').eq(0).outerHeight() + 'px'
				}).animate({
					top: 0 
				},speed,easing, function (data,container,putAfter) {
					
				});
			}
		},
		putUpbangMsgList: function (data,container) {
			var tpl = '', $upbangMsgList;
			data = data || {};
			tpl
			= '<div class="upbang-msg clearfix">'
			+ '<div class="upbang-avatar tooltipped tooltipped-s fl" aria-label="${nick}">'
			+ '<img src="${avatar}">'
			+ '</div>'
			+ '<div class="upbang-msg-content">'
			+ '<span class="msg-inner-wrap pos-rel" data-msg-text="${messageText}">{{if messagePhoto}}<img src="${messagePhoto}">{{/if}}</span>'
			+ '</div>'
			+ '</div>';
			$.template('html',tpl);
			var $msgInnerWraps =  $.tmpl('html',data).appendTo(container).find('.msg-inner-wrap');
			for (var i = 0; i < $msgInnerWraps.size(); i++) {
				var $msgInnerWrap = $($msgInnerWraps[i]), innerhtml = $msgInnerWrap.data('msg-text');
				if (innerhtml) {
					$msgInnerWrap.html(innerhtml);
				}
			}
		},
		next: function () {
			var $upbangMsgList = upbang.$upbangMsgList,
				$cur = $upbangMsgList.find('.upbang-msg.cur'),
				scrollHeight = $cur.outerHeight() + parseInt($cur.css('margin-top')) + parseInt($cur.css('margin-bottom')),
				containerTop = parseInt($upbangMsgList.css('top'));

			if ($cur.index() && playEnabled) {
				upbang.pause();
				playEnabled = false;
				$cur.removeClass('cur').prev().addClass('cur');
				$upbangMsgList.animate({
					top: containerTop + scrollHeight
				}, speed, easing, function () {
					playEnabled = true;
					upbang.play();
				});
			}
		},
		prev: function () {
			var $upbangMsgList = upbang.$upbangMsgList,
				$cur = $upbangMsgList.find('.upbang-msg.cur'),
				scrollHeight = $cur.outerHeight() + parseInt($cur.css('margin-top')) + parseInt($cur.css('margin-bottom')),
				containerTop = parseInt($upbangMsgList.css('top'));

			if ($cur.next().size() && playEnabled) {
				upbang.pause();
				playEnabled = false;
				$cur.removeClass('cur').next().addClass('cur');
				$upbangMsgList.animate({
					top: containerTop - scrollHeight
				}, speed, easing, function () {
					playEnabled = true;
					upbang.play();
				});
			}			
		},
		first: function () {
			upbang.pause();
			var $upbangMsgList = upbang.$upbangMsgList,
				$cur = $upbangMsgList.find('.upbang-msg.cur'),
				$last = $upbangMsgList.find('.upbang-msg:last'),
				containerHeight = $upbangMsgList.outerHeight();

			if ($cur.next().size()) {
				$upbangMsgList.animate({
					top: - containerHeight + $last.outerHeight() + parseInt($last.css('margin-top')) + parseInt($last.css('margin-bottom'))
				},speed,easing);
				$cur.removeClass('cur');
				$last.addClass('cur');
			}

			upbang.play();
		},
		last: function () {
			upbang.pause();
			var $upbangMsgList = upbang.$upbangMsgList,
				$cur = $upbangMsgList.find('.upbang-msg.cur'),
				$first = $upbangMsgList.find('.upbang-msg:first');

			if ($cur.index()) {
				$upbangMsgList.animate({
					top:0
				},speed,easing);
				$cur.removeClass('cur');
				$first.addClass('cur');
			}

			upbang.play();
		},
		changeToPlayState: function () {
			upbang.$playOrPause.addClass('fa-pause');
			play = true;
		},
		changeToPauseState: function () {
			upbang.$playOrPause.removeClass('fa-pause');
			play = false;
		},
		play: function () {
			requestTimer = setInterval(function () {
				upbang.loadUpbangMsg();
			},timeout);
			upbang.changeToPlayState();
			// upbang.loadUpbangMsg();
		},
		replay: function () {
			upbang.play();
			setTimeout(upbang.loadUpbangMsg,replayWaiting);
		},
		pause: function () {
			clearInterval(requestTimer);
			upbang.changeToPauseState();
		},
		togglePlay: function () {
			play ? upbang.pause() : upbang.replay();
		},
		fullscreenchange: function (e) {
			if (fullScreen) {
				upbang.changeToNomalScreenState();
			} else {
				upbang.changeToFullScreenState();
			}
		},
		keyF11ToChange: function  (e) {
			if (e.keyCode == 122) {
				upbang.fullscreenchange(e)
			}
		},
		fullScreenForIELowVersion: function () {
			var WsShell = new ActiveXObject('WScript.Shell');
     		WsShell.SendKeys('{F11}');
		},
		changeToFullScreenState: function () {
			upbang.$fullOrNomal.addClass('fa-compress');
			fullScreen = true;
		},
		changeToNomalScreenState: function () {
			upbang.$fullOrNomal.removeClass('fa-compress');
			fullScreen = false;
		},
		fullScreen: function () {
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
		exitFullscreen: function () {
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
		toggleFullScreen: function () {
			fullScreen ? upbang.exitFullscreen() : upbang.fullScreen();
		},
		getById: function (id) {
			return $('#'+id);
		},
		switchBoxItem: function () {
			var $me = $(this),
				path = 'js/',
				action = $me.data('action'),
				loadedMsg = ' loaded success',
				scriptUrl = {
					luckyDraw: path + 'lucky-draw.js',
					pairing: path + 'pairing.js'
				};

			if (action) {
				upbang.$upbangBoxItems.removeClass('visible');
				upbang.getById(action).addClass('visible');

				if (action === 'lucky-draw') {
					if (!window.luckyDrawLoaded) {
						$.getScript(scriptUrl.luckyDraw,function (script) {
							console.log(scriptUrl.luckyDraw + loadedMsg);
						});
					}
				}
				if (action === 'pairing') {
					if (!window.pairingLoaded) {
						$.getScript(scriptUrl.pairing,function (script) {
							console.log(scriptUrl.pairing + loadedMsg);
						});
					}
				}
			}
		},
		loadThemePreviewList: function () {
			$.ajax({
				url: 'json/theme-preview-list.json?activityMid=' + activityMid,
				dataType: 'json',
				success: function (response) {
					items = response.items || {};
					var tpl
					= '<a class="theme-preview-item" href="javascript:;" id="${id}" data-src="${src}" aria-label="${name}">'
					+ '<img class="trs-all" src="${src}">'
					+ '</a>';
					$.template('html',tpl);
					var $list = upbang.$themePreviewList, themeOption = store.get('themeOption');
					$list.html($.tmpl('html',items));
					if (themeOption) {
						upbang.setTheme(themeOption.id,themeOption.src);
					}
				}
			})
		},
		setTheme: function (id,src) {
			var $list = upbang.$themePreviewList;
			$list.find('.theme-preview-item').removeClass('cur');
			$list.find('#'+id).addClass('cur');
			$(document.body).css({backgroundImage:'url('+src+')'});
		},
		themeClick: function () {
			var $me = $(this),
			id = $me.attr('id'),
			src = $me.data('src');
			console.log(id,src)
			store.set('themeOption',{
				id: id,
				src: src
			});
			upbang.setTheme(id,src);
		},
		showThemePreview: function () {
			upbang.$themePreview.addClass('visible');
			themePreviewShown = true;
		},
		hideThemePreview: function () {
			upbang.$themePreview.removeClass('visible');
			themePreviewShown = false;
		},
		toggleThemePreview: function () {
			themePreviewShown ? upbang.hideThemePreview() : upbang.showThemePreview();
		}
	};

	upbang.init();
	$('.hide-icon').click(function(){
		$(this).closest('.upbang-footer').hide();
		$('.show-icon').show();
	});
	$('.show-icon').click(function(){
		$('.upbang-footer').show();
		$(this).hide();
	});

})(jQuery,document);