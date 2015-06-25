
(function ($,document) {
	var trsDura = 300;
	var dialog = {
		init: function  () {
			this.cacheElements();
			this.bindEvents();
		},
		cacheElements: function () {
			this.$mask = $('.mask');
		},
		bindEvents: function () {
			$(document).on('click', '.open-target', this.show);
			$(document).on('click', '.close-target', this.hide);
		},
		getById: function (id) {
			return $('#'+id);
		},
		show: function () {
			var $me = $(this),
				$dialog = dialog.getById($me.data('show')),
				setPos = $dialog.data('set-pos');

			dialog.$mask.addClass('show');
			$dialog.addClass('show');

			setTimeout(function () {
				dialog.$mask.addClass('real-show');
				$dialog.addClass('real-show');
				if (!setPos) {
					$dialog.css({
						'top': '50%',
						'left': '50%',
						'margin-top': -$dialog.outerHeight()/2,
						'margin-left': -$dialog.outerWidth()/2
					});
					$dialog.data('set-pos',true);
				}
			},0);

//			setTimeout(function () {
//				$dialog.find('input[type=text],select,textarea').filter(':enabled:visible:first').focus().select();
//			},trsDura)
		},
		hide: function () {
			var $me = $(this),
				$dialog = $me.closest('.dialog');

			dialog.$mask.removeClass('real-show');
			$dialog.removeClass('real-show');
			setTimeout(function () {
				dialog.$mask.removeClass('show');
				$dialog.removeClass('show');
			},trsDura);
		}
	};

	dialog.init();

})(jQuery,document);