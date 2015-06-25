

(function ($,document) {

	var pairingable = true,
		luckyBoy = {},
		luckyGirl = {},
		invalidId = null,
		boys = [],
		girls = [],
		interval = 10,
		drawTimer = null,
		activityMid = util.getQueryString('activityMid') || -1;

	var pairing = {
		init: function  () {
			this.cacheElements();
			this.bindEvents();
			this.loadPairingList();
			this.loadLuckyPairingList();
			this.setMessage();
		},
		cacheElements: function () {
			this.$pairing = $('#pairing');
			this.$luckyPairingList = $('#lucky-pairing-list');
			this.$nickBoy = $('#nick-boy'); // 男生人数
			this.$nickGirl = $('#nick-girl');
			this.$bubble = this.$pairing.find(".bubble");
			this.$avatarBoy = $('#avatar-boy'); // 男生头像
			this.$avatarGirl = $('#avatar-girl');
			this.$startPairingBtn = $('#start-pairing-btn');
			this.$winPairNum = $('#winPairNum');
			//this.$pairingMask = $('#pairing-mask');
//			this.$continuePairing = $('.continue-pairing');
		},
		setMessage: function () {
            this.noData = '等不及了快点开始';
        },
		bindEvents: function () {
			this.$startPairingBtn.on('click',pairing.toggleDraw);
//			this.$continuePairing.on('click',pairing.hideLucky);
			$(document).on('click','.pair-invalid-action', pairing.invalidClick);
		},
//		showLucky: function (luckyBoyInfo, luckyGirlInfo) {
////			pairing.$pairingMask.find('.avatar-boy').attr('src',luckyBoyInfo.avatarBoy);
////			pairing.$pairingMask.find('.nick-boy').html(luckyBoyInfo.nickBoy);
////			pairing.$pairingMask.find('.avatar-girl').attr('src',luckyGirlInfo.avatarGirl);
////			pairing.$pairingMask.find('.nick-girl').html(luckyGirlInfo.nickGirl);
////			pairing.$pairingMask.show();
//		},
//		hideLucky: function  () {
//			pairing.$pairingMask.hide();
//		},
		draw: function () {
			var ramdomBoy = boys.shuffle().random(),
				idBoy = ramdomBoy.id,
				nickBoy = ramdomBoy.nick,
				avatarBoy = ramdomBoy.avatar;

			var ramdomGirl = girls.shuffle().random(),
				idGirl = ramdomGirl.id,
				nickGirl = ramdomGirl.nick,
				avatarGirl = ramdomGirl.avatar;

			luckyBoy = ramdomBoy;
			luckyGirl = ramdomGirl;
			pairing.$nickBoy.html(nickBoy);
			pairing.$nickGirl.html(nickGirl);
			pairing.$avatarBoy.attr('src',avatarBoy);
			pairing.$avatarGirl.attr('src',avatarGirl);
		},
		startPairing: function () {
			if(!boys || !boys.length) {
				pairing.loadLuckyPairingList();
				pairing.loadPairingList();
				alert('配对池已空！无男生可配对！');
				return;
			}
			if(!girls || !girls.length) {
				pairing.loadLuckyPairingList();
				pairing.loadPairingList();
				alert('配对池已空！无女生可配对！');
				return;
			}
			this.$bubble.hide();
			drawTimer = setInterval(pairing.draw,interval);
			pairingable = false;
			pairing.$startPairingBtn.html('停');
		},
		stopPairing: function () {
			clearInterval(drawTimer);
			pairingable = true;
			pairing.$startPairingBtn.html('开始对对碰');
//			pairing.showLucky(luckyBoy, luckyGirl);
			pairing.sendLucky();
		},
		toggleDraw: function () {
			pairingable ? pairing.startPairing() : pairing.stopPairing();
		},
		sendLucky: function () {
			// 发送中奖者信息至服务器端，然后处理抽奖名单和中奖名单返回至浏览器端
			this.$bubble.show();
			$.ajax({
				url:'../activity-screen/send-pairing',
				type: 'POST',
				data: {
					luckyBoyId: luckyBoy.id,
					luckyGirlId: luckyGirl.id,
					activityMid: activityMid
				},
				success: function (response) {
					if (response.success == false) {
						alert(response.message);
					}
					pairing.loadLuckyPairingList();
					pairing.loadPairingList();
				}
			});
		},
		invalidClick: function (e) {
			invalidId = $(this).data('invalid-id');
			pairing.invalidLucky();
		},
		invalidLucky: function () {// 作废
			if (confirm('1、配对名单随机无序打乱\n2、配对名额为随机抽取\n3、该操作无法撤消，如非必要请勿进行此操作')) {
				$.ajax({
					url:'../activity-screen/invalid-pairing',
					type: 'POST',
					data: {
						pairingId: invalidId,
						activityMid: activityMid,
						invalidType: 'pairing'
					},
					success: function () {
						pairing.loadLuckyPairingList();
						pairing.loadPairingList();
					}
				});
			}
		},
		loadPairingList: function () {
			$.ajax({
                url: '../activity-screen/pairing-list?activityMid=' + activityMid,
                dataType: 'json',
                success: function (response) {
                	boys = response.data.boys;
                	girls = response.data.girls;
            		pairing.$nickBoy.html("♂共" + boys.length + "位");
        			pairing.$nickGirl.html("♀共" + girls.length + "位");
//            			pairing.$avatarBoy.attr('src',"images/pair-default.png");
//            			pairing.$avatarGirl.attr('src',"images/pair-default.png");
                }
            });
		},
		loadLuckyPairingList: function () {
			$.ajax({
                url: '../activity-screen/lucky-pairing-list?activityMid=' + activityMid,
                dataType: 'json',
                success: function (response) {

//                    var emptyMsg = '<div class="empty-msg">'+pairing.noData+'</div>',
//                        errorMsg = '<div class="error-msg">'+response.message+'</div>'
//                    var markup
//                        = '<tr>'
//                        + '<td class="index">${index}</td>'
//                        + '<td class="tac"><img class="avatar" src="${avatarBoy}"><br>${nickBoy}</td>'
//                        + '<td class="tac"><span class="icon-heart"></span></td>'
//                        + '<td class="tac"><img class="avatar" src="${avatarGirl}"><br>${nickGirl}</td>'
//                        + '<td class="tac"><a class="invalid-action pair-invalid-action" data-invalid-id="${id}" href="javascript:;">作废</a></td>'
//                        + '</tr>';
                    
                    var newmark
                    	= '<li class="clearfix">'
                    	+ '<span class="serial-num tac fl">'
						+ '<em>${index}</em>'
						+ '</span>'
						+ '<div class="wrap-pair-per fl">'
						+ '<div class="pair-sel fl">'
						+ '<img width="50" height="50"'
						+ 'src="${avatarBoy}">'
						+ '<a class="pair-name">${nickBoy}</a>'
						+ '</div>'
						+ '<span class="pair-heart fl"></span>'
						+ '<div class="pair-sel fl">'
						+ '<img width="50" height="50"'
						+ 'src="${avatarGirl}">'
						+ '<a class="pair-name">${nickGirl}</a>'
						+ '</div>'
						+ '</div>'
						+ '<div class="pair-del fl">'
						+ '<a href="javascript:;" data-invalid-id="${id}" class="icon-del pair-invalid-action">×</a>'
						+ '</div>'
						+ '</li>';

                    if (response.success) {
                        $.template('html', newmark);
                        pairing.$luckyPairingList.html($.tmpl('html', response.items));
                        pairing.$winPairNum.html(response.items.length);
                    }
                }
            })
		}
	};

	pairing.init();

	window.pairingLoaded = true;

})(jQuery,document);