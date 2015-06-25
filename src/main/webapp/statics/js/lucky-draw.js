
(function ($,document) {
	var drawable = true,
		lucky = {},
		invalidId = null,
		levelId = null,
		luckyDrawList = [],
		interval = 10,
		drawTimer,
		activityMid = util.getQueryString('activityMid') || -1;

	var luckyDraw = {
		init: function  () {
			this.cacheElements();
			this.setMessage();
			this.bindEvents();
			this.loadWinnersList();
			this.loadLuckDrawList();
		},
		cacheElements: function () {
			this.$luckyDraw = $('#lucky-draw');
			this.$level = this.$luckyDraw.find('.level');
			this.$prizePic = this.$luckyDraw.find('.prize-pic');
			this.$prizeName = this.$luckyDraw.find('.prize-name');
			this.$prizeNum = this.$luckyDraw.find('.prize-num');
			this.$tabCard = this.$luckyDraw.find('.tab-card');
			this.$tabContent =  this.$luckyDraw.find('.tab-content');
			this.$nick = $('#draw-nick');
			this.$drawTotal = $('#draw-total');
			this.$avatar = $('#draw-avatar');
			this.$startDrawBtn = $('#start-draw-btn');
			this.$luckyMask = $('.lucky-mask');
			this.$continueDraw = $('.continue-draw');
			this.$winPrizeUserNum = $('#winPrizeUserNum');
		},
		setMessage: function () {
            this.noData = '等不及了快点开始';
        },
		bindEvents: function () {
			this.$tabCard.on('click', '.card-item', luckyDraw.tabCardClick);
			this.$startDrawBtn.on('click',luckyDraw.toggleDraw);
			this.$continueDraw.on('click',luckyDraw.hideLucky);
			$(document).on('click','.draw-invalid-action', luckyDraw.invalidClick);
		},
		showLucky: function  (luckyinfo) {
			luckyDraw.$luckyMask.find('img').attr('src',luckyinfo.avatar);
			luckyDraw.$luckyMask.find('.nick').html(luckyinfo.nick);
			luckyDraw.$luckyMask.find('.lucky-level').html($('.tab-card').find('.cur').html());
			luckyDraw.$luckyMask.show();
		},
		hideLucky: function  () {
			luckyDraw.$luckyMask.hide();
		},
		draw: function () { // 抽奖
			var ramdom = luckyDrawList.shuffle().random(),
				id = ramdom.id,
				nick = ramdom.nick,
				avatar = ramdom.avatar;

			lucky = ramdom;
			luckyDraw.$nick.html(nick);
			luckyDraw.$avatar.attr('src',avatar);
		},
		startDraw: function () { //开始抽奖
			if(!luckyDrawList || !luckyDrawList.length) {
				luckyDraw.loadLuckDrawList();
				alert('奖池已空！无人可抽取！');
				return;
			}
			drawTimer = setInterval(luckyDraw.draw,interval);
			drawable = false;
			luckyDraw.$startDrawBtn.html('停');
		},
		lucky: function () {
			clearInterval(drawTimer);
			drawable = true;
			luckyDraw.$startDrawBtn.html('开始');
			luckyDraw.showLucky(lucky);
			luckyDraw.sendLucky();
		},
		toggleDraw: function () {
			drawable ? luckyDraw.startDraw() : luckyDraw.lucky();
		},
		tabCardClick: function (e) { // TAB点击切换事件
			var $me = $(this),
				$cardItems = luckyDraw.$tabCard.find('.card-item'),
				$contentItems = luckyDraw.$tabContent.find('.content-item'),
				winnersInfo = $me.data('winners-info');

			$cardItems.removeClass('cur');
			$contentItems.removeClass('cur');
			$contentItems.eq($me.index()).addClass('cur');
			$me.addClass('cur');
			levelId = winnersInfo.levelId;

			luckyDraw.$level.html(winnersInfo.level + "("+ winnersInfo.prizeNum +"名)");
			luckyDraw.$prizeName.html(winnersInfo.prize);
			luckyDraw.$prizePic.attr("src", winnersInfo.prizePic);
			luckyDraw.$winPrizeUserNum.html(winnersInfo.winPrizeUserNum);
		},
		sendLucky: function () {
			// 发送中奖者信息至服务器端，然后处理抽奖名单和中奖名单返回至浏览器端
			$.ajax({
				url:'../activity-screen/send-lucky',
				type: 'POST',
				data: {
					userId: lucky.id,
					prizeId: levelId,
					activityMid: activityMid
				},
				success: function (response) {
					if (response.success == false) {
						alert(response.message);
					}
					luckyDraw.loadWinnersList();
					luckyDraw.loadLuckDrawList();
				}
			});
		},
		invalidClick: function (e) {
			invalidId = $(this).data('invalid-id');
			luckyDraw.invalidLucky();
		},
		invalidLucky: function () {
			// 作废
			if (confirm('1、抽奖名单随机无序打乱\n2、中奖名额为随机抽取\n3、该操作无法撤消，如非必要请勿进行此操作')) {
				$.ajax({
					url:'../activity-screen/invalid-lucky',
					type: 'POST',
					data: {
						userId: invalidId,
						prizeId: levelId,
						invalidType: 'user'
					},
					success: function () {
						luckyDraw.loadWinnersList();
						luckyDraw.loadLuckDrawList();
					}
				});
			}
		},
		loadLuckDrawList: function () {
			$.ajax({
                url: '../activity-screen/lucky-draw-list?activityMid=' + activityMid,
                dataType: 'json',
                success: function (response) {
                	luckyDrawList = response.items;
                	luckyDraw.$drawTotal.html(luckyDrawList.length);
                }
            });
		},
		loadWinnersList: function () { //加载中奖用户
			$.ajax({
                url: '../activity-screen/winners-list?activityMid=' + activityMid,
                dataType: 'json',
                success: function (response) {

                    var items = response.items,
                        wrap
                        = '<ul class="draw-list content-item">'
                        + '</ul>',
						
                        markup
                        = '<li class="clearfix">'
						+ '<span class="serial-num tac fl">'
						+ '<em>${index}</em>'
						+ '</span>'
						+ '<div class="wrap-pair-per fl">'
						+ '<div class="draw-sel fl">'
						+ '<img width="50" height="50"'
						+ 'src="${avatar}">'
						+ '<a class="draw-name">${nick}</a>'
						+ '</div>'
						+ '</div>'
						+ '<div class="pair-del fl">'
						+ '<a href="javascript:;" data-invalid-id="${id}" class="icon-del invalid-action draw-invalid-action">×</a>'
						+ '</div>'
						+ '</li>'

                        $tabCard = luckyDraw.$tabCard,
                		$tabContent = luckyDraw.$tabContent;

                    if (response.success) {
                        $.template('html', markup);
                        for (var i = 0; i < items.length; i++) {
                        	var item = items[i],
                        		$trs = $.tmpl('html', item.items);
                        		
                    		if($tabCard.find('.card-item').size() < items.length){
                    			$tabCard.append('<div class="card-item trs-all fl">'+item.level+'</div>');
                			}
                    		$tabContent.append(wrap);
                    		$tabContent.find('.content-item').eq(i).html($trs);
                    		var winPrizeUserNum = item.items.length;
                    		$tabCard.find('.card-item').eq(i).data('winners-info', {
                    			level: item.level,
								prize: item.prize,
								prizePic: item.prizePic,
								prizeNum: item.prizeNum,
								levelId: item.levelId,
								winPrizeUserNum: winPrizeUserNum
                    		});
                        }
                        if (!luckyDraw.$tabCard.find('.card-item.cur').size()) {
                        	luckyDraw.$tabCard.find('.card-item:first').trigger('click');
                        }
                    } else {
                    	alert(response.message);
                    	return;
                    }
                }
            })
		}
	};

	luckyDraw.init();

	window.luckyDrawLoaded = true;

})(jQuery,document);