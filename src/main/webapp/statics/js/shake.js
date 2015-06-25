

(function ($,document) {

	var isInit = true,
		shakeList = [],
		invalidId = null,
		interval = 1000,
		fShakeStart = false,
		drawTimer,
		countDownSecs = 3,
		activityMid = util.getQueryString('activityMid') || -1;

	var shake = {
		init: function  () {
			this.cacheElements();
			this.bindEvents();
			this.loadShakeList();
			this.setMessage();
		},
		cacheElements: function () {
			this.$shake = $('#shake');
			this.$countDownBtn = $('#countdown-btn');
			this.$resetBtn = $('#shake-reset-btn');
			this.$shakeBox = $('#shake-box');
			this.$luckyShakeList = $('#lucky-shake-list');
			this.$shakeRunway = $('.shake-runway');
			this.$shakePrize = $('.shake-prize');
//			this.$startShakeBtn = $('#start-shake-btn');
		},
		setMessage: function () {
        },
		bindEvents: function () {
//			this.$startShakeBtn.on('click',shake.toggleDraw);
			this.$countDownBtn.on('click', shake.startShake);
			this.$resetBtn.on('click', shake.restartShake);
		},
		countDown : function() {
			if (countDownSecs > 0) {
				setTimeout(shake.countDown, 1000);
				$("#countdown-btn p").text(countDownSecs);
				$("#countdown-btn p").animate({'font-size':'10px'}, 200);
				$("#countdown-btn p").animate({'font-size':'200px'}, 200);
				countDownSecs--;
			} else {
				$("#countdown-btn p").animate({'font-size':'70px'}, 200);
				$("#countdown-btn p").text("Go!");
				shake.initShakeInfo();
				setTimeout(shake.startDraw, 1000);
			}
		},
		startShake : function () {
			if (countDownSecs < 3) {
				return;
			}
			
			if (isInit == true) {
				return;
			}
			shake.countDown();
		},
		restartShake : function() {
			shake.resetCountDown();
		},
		resetCountDown : function() {
			shake.$shakeBox.hide();
			$("#countdown-btn").show();
		},
		draw: function () {
			if (fShakeStart == false) {
				shake.stopDraw();
				return;
			}
			shake.loadShakeList();
		},
		startDraw: function () {
			$("#countdown-btn").slideUp("slow");
			shake.$shakeBox.show();
			shake.$shakeRunway.show();
			shake.$shakePrize.hide();
			shake.$resetBtn.hide();
			fShakeStart = true;
			drawTimer = setInterval(shake.draw,interval);
		},
		stopDraw: function () {
			clearInterval(drawTimer);
			fShakeStart = false;
			shake.$shakeRunway.hide();
			shake.$shakePrize.slideDown("slow");
			$("#countdown-btn p").text('倒计时3秒');
			countDownSecs=3;
			this.$resetBtn.show();
		},
		loadShakeList: function () {
			$.ajax({
                url: '../activity-screen/shake-list?activityMid=' + activityMid,
                dataType: 'json',
                success: function (response) {
                    if (response.success) {
                    	$('#first-prize').hide();
                    	$('#second-prize').hide();
                    	$('#third-prize').hide();
                    	
                    	fShakeStart = response.data.fShakeStart;
                        $('#shake-user-num').html('共' + response.total + '人参赛')
                        if (fShakeStart == true && isInit == true) {
                        	$('#runner1').find('img').removeAttr('src').animate({"margin-left":0}, 200);
                        	$('#runner1').find('span').html('');
        					$('#runner2').find('img').removeAttr('src').animate({"margin-left":0}, 200);
        					$('#runner2').find('span').html('');
        					$('#runner3').find('img').removeAttr('src').animate({"margin-left":0}, 200);
        					$('#runner3').find('span').html('');
        					$('#runner4').find('img').removeAttr('src').animate({"margin-left":0}, 200);
        					$('#runner4').find('span').html('');
        					$('#runner5').find('img').removeAttr('src').animate({"margin-left":0}, 200);
        					$('#runner5').find('span').html('');
        					$('#runner6').find('img').removeAttr('src').animate({"margin-left":0}, 200);
        					$('#runner6').find('span').html('');
        					isInit = false;
                        	shake.startDraw();
                        	return;
                        }
                        isInit = false;
                        var endShakeNum = response.data.endShakeNum;
	                	 $.each(response.items, function(i, item){      
	                		 if (item.index == 1) {
	                 			var shakeNum = item.shakeNum;
	                 			$('#runner1').find('img').attr('src', item.avatar);
	                 			$('#runner1').find('span').html(item.nick);
	                 			$('#runner1').animate({"margin-left":shakeNum * 785 / endShakeNum}, 200);
	                 			$('#first-prize').find('.avatar').attr('src', item.avatar);
	                 			$('#first-prize').find('.nick-name').html(item.nick);
	                 			$('#first-prize').find('.shake-num').html(item.shakeNum + " 次");
	                 			$('#first-prize').show();
	                 		} else if (item.index == 2) {
	                 			var shakeNum = item.shakeNum;
	                 			$('#runner2').find('img').attr('src', item.avatar);
	                 			$('#runner2').find('span').html(item.nick);
	                 			$('#runner2').animate({"margin-left":shakeNum * 785 / endShakeNum}, 200);
	                 			$('#second-prize').find('.avatar').attr('src', item.avatar);
	                 			$('#second-prize').find('.nick-name').html(item.nick);
	                 			$('#second-prize').find('.shake-num').html(item.shakeNum + " 次");
	                 			$('#second-prize').show();
	                 		} else if (item.index == 3) {
	                 			var shakeNum = item.shakeNum;
	                 			$('#runner3').find('img').attr('src', item.avatar);
	                 			$('#runner3').find('span').html(item.nick);
	                 			$('#runner3').animate({"margin-left":shakeNum * 785 / endShakeNum}, 200);
	                 			$('#third-prize').find('.avatar').attr('src', item.avatar);
	                 			$('#third-prize').find('.nick-name').html(item.nick);
	                 			$('#third-prize').find('.shake-num').html(item.shakeNum + " 次");
	                 			$('#third-prize').show();
	                 		} else if (item.index == 4) {
	                 			var shakeNum = item.shakeNum;
	                 			$('#runner4').find('img').attr('src', item.avatar);
	                 			$('#runner4').find('span').html(item.nick);
	                 			$('#runner4').animate({"margin-left":shakeNum * 785 / endShakeNum}, 200);
	                 		} else if (item.index == 5) {
	                 			var shakeNum = item.shakeNum;
	                 			$('#runner5').find('img').attr('src', item.avatar);
	                 			$('#runner5').find('span').html(item.nick);
	                 			$('#runner5').animate({"margin-left":shakeNum * 785 / endShakeNum}, 200);
	                 		} else if (item.index == 6) {
	                 			var shakeNum = item.shakeNum;
	                 			$('#runner6').find('img').attr('src', item.avatar);
	                 			$('#runner6').find('span').html(item.nick);
	                 			$('#runner6').animate({"margin-left":shakeNum * 785 / endShakeNum}, 200);
	                 		}
	                	  });
                    }
                }
            })
		},
		initShakeInfo: function () { //摇一摇开始
			$.ajax({
				url:'../activity-screen/init-shake-info',
				type: 'POST',
				data: {
					activityMid: activityMid
				},
				success: function (response) {
					if (response.success == false) {
						alert(response.message);
					}
					$('#runner1').find('img').removeAttr('src').animate({"margin-left":785}, 200);
                	$('#runner1').find('span').html('');
					$('#runner2').find('img').removeAttr('src').animate({"margin-left":0}, 200);
					$('#runner2').find('span').html('');
					$('#runner3').find('img').removeAttr('src').animate({"margin-left":0}, 200);
					$('#runner3').find('span').html('');
					$('#runner4').find('img').removeAttr('src').animate({"margin-left":0}, 200);
					$('#runner4').find('span').html('');
					$('#runner5').find('img').removeAttr('src').animate({"margin-left":0}, 200);
					$('#runner5').find('span').html('');
					$('#runner6').find('img').removeAttr('src').animate({"margin-left":0}, 200);
					$('#runner6').find('span').html('');
					//shake.loadShakeList();
				}
			});
		}
	};

	shake.init();

	window.shakeLoaded = true;

})(jQuery,document);