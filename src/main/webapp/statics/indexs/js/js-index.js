jQuery(".fullSlide").slide({
	titCell : ".hd ul",
	mainCell : ".bd ul",
	effect : "fold",
	autoPlay : true,
	autoPage : true,
	trigger : "click",
	mouseOverStop : false,/* 鼠标移到容器层继续播放 */
	/* 控制进度条 */
	startFun : function() {
		var timer = jQuery(".fullSlide .timer");
		timer.stop(true, true).animate({
			"width" : "0%"
		}, 0).animate({
			"width" : "100%"
		}, 2500);
	}
});

// skip 快速导航浮动
var clientHeight = 499;
function adsorption() {
	var scrollTop = 0;
	window.onscroll = function() {
		scrollTop = document.body.scrollTop
				|| document.documentElement.scrollTop;
		if (scrollTop > clientHeight) {
			$("#skip").addClass("fixed");
			$("#skip-bug").removeClass("no");
		} else {
			$("#skip").removeClass("fixed");
			$("#skip-bug").addClass("no");
		}
	}
}

$(document).ready(function() {
	adsorption();
});