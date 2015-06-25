(function ($,document) {
	var signInTimer,activityMid = util.getQueryString('activityMid') || -1,
		serial = 1,timeout = 3000, test;
	//图片放大
	var thumbBorderWidth  = 2;
	var photoZIndex       = 100;        // z-index (show above all)
    var photoBorder       = 10;         // border width around fullsize image
    var photoBorderColor  = 'white';    // border color
    var loading    = 'images/loading.gif';  // path to img displayed while gallery/thumbnails loads
    var showInfo          = true;       // include photo description (alt tag) in photo container
    var fadeDuration      = 200;        // speed at which photo fades (ms)
    var pickupDuration    = 500;        // speed at which photo is picked up & put down (ms)
    var numLayers         = 5;          // number of layers in the pile (max zindex)
	
	var signin = {
		init: function  () {
			this.cacheElements();
			this.bindEvents();
			photo.init();
			this.startSignIn();
		},
		cacheElements: function () {
			this.$signIn = $('#signin');
			this.$signInBox = this.$signIn.find('.signin-box');
			this.$signInList = this.$signIn.find('.signin-list');
			this.$signUserNum = this.$signIn.find('#signUserNum');
		},
		bindEvents: function () {
		},
		startSignIn : function() {
			signInTimer = setInterval(function() {
				signin.loadSingleSignInUser();
			}, timeout);
		},
		endSignIn : function() {
			clearInterval(signInTimer);
		},
		loadSingleSignInUser : function() {
			if (!window.signInTimer) {
				return;
			}
			$.ajax({
                url: '../activity-screen/single_signin_user?mid=' + activityMid + '&serial=' + serial,
//				url: 'json/signUser.json',
                dataType: 'json',
                success: function (response) {
                	
                	var markup
                		= '<div class="user-avatar out">'
                		+ '<img class="avatar" src="${avatar}" width="56" height="56" alt="${nickName}">'
                		+ '</div>';
                	
                	if (response.success) {
                		$.template('html', markup);
                		var user = response.data;
                		if (user != null) {
                			if (serial > 46) {
                				var index = parseInt(Math.random()*46+1);
                				//signin.$signInList.find('li').css('z-index', '0');
                				var sign = signin.$signInList.find('.li' + index);
                				sign.html($.tmpl('html', user));
                    			photo.pickup(sign);
                    			setTimeout(function(){
                    				photo.putDown();
                    			}, 2000);
                			} else {
                				//signin.$signInList.find('li').css('z-index', '0');
                				var sign = signin.$signInList.find('.li' + serial);
                				sign.html($.tmpl('html', user));
                    			photo.pickup(sign);
                    			setTimeout(function(){
                    				photo.putDown();
                    			}, 2000);
//                    			signin.$signInList.find('.li' + serial).css('z-index', '9999');
//                    			var current = signin.$signInList.find('.li' + serial).find('.avatar');
//                    			var top = current.offset().top;
//                                var left = current.offset().left;
//                                console.log(top);
//                                console.log(left);
//                                console.log(($(window).scrollTop()) + ($(window).height() / 2) - (400 / 2));
//                                console.log(($(window).scrollLeft()) + ($(window).width() / 2) - (400 / 2));
//                                current.css('position');
//                    			current.animate({
//                    				'position': 'fixed',
//                                    'top'     : ($(window).scrollTop()) + ($(window).height() / 2) - (400 / 2),
//                                    'left'    : ($(window).scrollLeft()) + ($(window).width() / 2) - (400 / 2),
//                                    'width'   : 400 + 'px',
//                                    'height'  : 400 + 'px'
//                                }, 1000,function(){
//                    				setTimeout(function(){
//                    					current.stop().animate({
//                    						'position': 'fixed',
//                                            'top'     : top,
//                                            'left'    : left,
//                                            'width'   : 56 + 'px',
//                                            'height'  : 56 + 'px'
//                                        }, 1000);
//                    				},1000)
//                    			});
                			}
                			signin.$signUserNum.html(serial);
                    		serial = serial + 1;
                		}
                	} else {
                		
                	}
                }
            });
		}
		
//		checkinplus: function (items) {
//			var checkers = this.$checkinListTmpl.tmpl(items);
//			checkers.prependTo(this.$checkinList);
//			setTimeout(function () {
//				checkers.addClass('growup');
//			},150);
//			this.checkinBubbleUp(items.length)
//		},
//		updateCheckinTotal: function (total) {
//			this.$checkinTotal.html(total);
//		},
//		checkinBubbleUp: function (plus) {
//			setTimeout(function () {
//				checkin.$checkinBubble.html('+'+plus);
//				checkin.$checkinBubble.addClass('up');
//				setTimeout(function () {
//					checkin.$checkinBubble.removeClass('up');
//					clickEnabled = true;
//				},2000);
//			},1000);
//		},
//		updateBtnClick: function () {
//			if (!clickEnabled) {return};
//			clickEnabled = false;
//			checkinType = 'increament';
//			checkin.loadCheckinList();
//		},
//		loadCheckinList: function () {
//			$.ajax({
//				url: '../json/checkin-list.json?checkinType=' + checkinType + '&activityMid=' + activityMid,
//				dataType: 'json',
//				success: function (response) {
//					if (!response.items.length) { return };
//					checkin.checkinplus(response.items);
//					checkin.updateCheckinTotal(response.total);
//				}
//			})
//		}

	};
	
	var photo = {
	        // Photo container elements
	        container : $( '<div id="sign-active-image-container"/>' ), 
	        image     : $( '<img id="sign-active-image" />'),
	        info      : $( '<div id="sign-active-image-info"/>'),
	        active : 'sign-active-thumbnail',  // active (or clicked) sign class name

	        isPickedUp     : false,  // track if photo container is currently viewable
	        fullSizeWidth  : null,   // will hold width of active thumbnail's fullsize image
	        fullSizeHeight : null,   // will hold height of active thumbnail's fullsize image
	        windowPadding  : 40,     // minimum space between container and edge of window (px)
	        
	        // Adds photo container elements to DOM.
	        init : function() {

	            // append and style photo container
	            $('body').append( this.container );
	            this.container.css({
	                'display'    : 'none',
	                'position'   : 'absolute',
	                'padding'    : thumbBorderWidth,
	                'z-index'    : photoZIndex,
	                'background' : photoBorderColor,
	                'background-image'    : 'url(' + loading + ')',
	                'background-repeat'   : 'no-repeat',
	                'background-position' : '50%, 50%'
	            });

	            // append and style image
	            this.container.append( this.image );
	            this.image.css('display', 'block');

	            // append and style info div
	            if ( showInfo ) {
	                this.container.append( this.info );
	                this.info.append('<p></p>');
	                this.info.css('opacity', '0');
	                this.info.css('background', 'none repeat scroll 0 0 rgba(0, 0, 0, 0.5)');
	                this.info.css('bottom', '0');
	                this.info.css('font-size', '3em');
	                this.info.css('font-weight', '500');
	                this.info.css('left', '0');
	                this.info.css('padding', '8px 0');
	                this.info.css('line-height', '50px');
	                this.info.css('overflow', 'hidden');
	                this.info.css('position', 'absolute');
	                this.info.css('text-align', 'center');
	                this.info.css('width', '100%');
	                this.info.css('color', 'rgb(255, 255, 255)');
	            };
	            //photo.pickup($($('.photopile').find('li').get().random()))
//	            drawTimer = setInterval(function(){
//	                photo.pickup($($('.photopile').find('li').get().random()))
//	            },3000);
	        }, // init
	        setActive : function( sign ) { sign.addClass(this.active); },
	        clearActiveClass : function() { $('li.' + this.active).fadeTo(fadeDuration, 1).removeClass(this.active); },
	        getActiveImgSrc   : function(sign) { return $('li.' + this.active).find('img').attr('src'); },
	        getActive : function() { 
	            return ($('li.' + this.active)[0]) ? $('li.' + this.active).first() : false;
	        },
	        setZ        : function( sign, layer ) { sign.css( 'z-index', layer ); },
	        getActiveOffset   : function() { return $('li.' + this.active).offset(); },
	        getActiveHeight   : function() { return $('li.' + this.active).height(); },
	        getActiveWidth    : function() { return $('li.' + this.active).width(); },

	        // Simulates picking up a photo from the photopile.
	        pickup : function( sign ) {
	            var self = this;
	            if ( self.isPickedUp ) {
	                // photo already picked up. put it down and then pickup the clicked thumbnail
	                self.putDown( function() { self.pickup( sign ); });
	            } else {
	                self.isPickedUp = true;
	                self.clearActiveClass();
	                self.setActive( sign );
	                self.loadImage( self.getActiveImgSrc(sign), function() {
	                    self.image.fadeTo(fadeDuration, '1');
	                    self.enlarge();
	                    //$('body').bind('click', function() { self.putDown(); }); // bind putdown event to body
	                });
	            }
	        }, // pickup

	        // Simulates putting a photo down, or returning to the photo pile.
	        putDown : function( callback ) {
	            self = this;
	            $('body').unbind();
	            self.hideInfo();
	            //navigator.hideControls();
	            self.setZ( self.getActive(), numLayers );
	            self.container.stop().animate({
	                'top'     : self.getActiveOffset().top,
	                'left'    : self.getActiveOffset().left,
	                'width'   : self.getActiveWidth() + 'px',
	                'height'  : self.getActiveHeight() + 'px',
	                'padding' : thumbBorderWidth + 'px'
	            }, pickupDuration, function() {
	                self.isPickedUp = false;
	                self.clearActiveClass();
	                self.container.fadeOut( fadeDuration, function() {
	                    if (callback) callback();
	                });
	            });
	        },

	        // Handles the loading of an image when a thumbnail is clicked.
	        loadImage : function ( src, callback ) {
	            var self = this;
	            self.image.css('opacity', '0');         // Image is not visible until
	            self.startPosition();                   // the container is positioned,
	            var img = new Image;                    // the source is updated,
	            img.src = src;                          // and the image is loaded.
	            img.onload = function() {               // Restore visibility in callback
//	                self.fullSizeWidth = this.width;
//	                self.fullSizeHeight = this.height;
	            	self.fullSizeWidth = 400;
	                self.fullSizeHeight = 400;
	                self.setImageSource( src );
	                if (callback) callback();
	            };
	        },

	        // Positions the div container over the active thumb and brings it into view.
	        startPosition : function() {
	        	var self = this;
	            this.container.css({
	                'top'       : self.getActiveOffset().top,
	                'left'      : self.getActiveOffset().left,
	                //'transform' : 'rotate(' + thumb.getActiveRotation() + 'deg)',
	                'width'     : self.getActiveWidth() + 'px',
	                'height'    : self.getActiveHeight() + 'px',
	                'padding'   : thumbBorderWidth
	            }).fadeTo(fadeDuration, '1');
	            self.getActive().fadeTo(fadeDuration, '0');
	        },

	        // Enlarges the photo container based on window and image size (loadImage callback).
	        enlarge : function() {
	            var windowHeight = window.innerHeight ? window.innerHeight : $(window).height(); // mobile safari hack
	            var availableWidth = $(window).width() - (2 * this.windowPadding);
	            var availableHeight = windowHeight - (2 * this.windowPadding);
	            if ((availableWidth < this.fullSizeWidth) && ( availableHeight < this.fullSizeHeight )) {
	                // determine which dimension will allow image to fit completely within the window
	                if ((availableWidth * (this.fullSizeHeight / this.fullSizeWidth)) > availableHeight) {
	                    this.enlargeToWindowHeight( availableHeight );
	                } else {
	                    this.enlargeToWindowWidth( availableWidth );
	                }
	            } else if ( availableWidth < this.fullSizeWidth ) {
	                this.enlargeToWindowWidth( availableWidth );
	            } else if ( availableHeight < this.fullSizeHeight ) {
	                this.enlargeToWindowHeight( availableHeight );
	            } else {
	                this.enlargeToFullSize();
	            }
	        }, // enlarge

	        // Updates the info div text and makes visible within the photo container.
	        showInfo : function() {
	        	var self = this;
	            if ( showInfo ) {
	                this.info.children().text( self.getActive().find('img').attr('alt') );
	                console.log(self.getActive().find('img').attr('alt'));
	                this.info.css({
	                    'margin-top' : -(this.info.height()) + 'px'
	                }).fadeTo(fadeDuration, 1);
	            }
	        },

	        // Hides the info div.
	        hideInfo : function() {
	            if ( showInfo ) {
	                this.info.fadeTo(fadeDuration, 0);
	            };
	        },

	        // Fullsize image will fit in window. Display it and show nav controls.
	        enlargeToFullSize : function() {
	            self = this;
	            self.container.css('transform', 'rotate(0deg)').animate({
	                'top'     : ($(window).scrollTop()) + ($(window).height() / 2) - (self.fullSizeHeight / 2),
	                'left'    : ($(window).scrollLeft()) + ($(window).width() / 2) - (self.fullSizeWidth / 2),
	                'width'   : (self.fullSizeWidth - (2 * photoBorder)) + 'px',
	                'height'  : (self.fullSizeHeight - (2 * photoBorder)) + 'px',
	                'padding' : photoBorder + 'px',
	            }, function() {
	                self.showInfo();
	                //navigator.showControls();
	            });
	        },

	        // Fullsize image width exceeds window width. Display it and show nav controls.
	        enlargeToWindowWidth : function( availableWidth ) {
	            self = this;
	            var adjustedHeight = availableWidth * (self.fullSizeHeight / self.fullSizeWidth);
	            self.container.css('transform', 'rotate(0deg)').animate({
	                'top'     : $(window).scrollTop()  + ($(window).height() / 2) - (adjustedHeight / 2),
	                'left'    : $(window).scrollLeft() + ($(window).width() / 2)  - (availableWidth / 2),
	                'width'   : availableWidth + 'px',
	                'height'  : adjustedHeight + 'px',
	                'padding' : photoBorder + 'px'
	            }, function() {
	                self.showInfo();
	                //navigator.showControls();
	            });
	        },

	        // Fullsize image height exceeds window height. Display it and show nav controls.
	        enlargeToWindowHeight : function( availableHeight ) {
	            self = this;
	            var adjustedWidth = availableHeight * (self.fullSizeWidth / self.fullSizeHeight);
	            self.container.css('transform', 'rotate(0deg)').animate({
	                'top'     : $(window).scrollTop()  + ($(window).height() / 2) - (availableHeight / 2),
	                'left'    : $(window).scrollLeft() + ($(window).width() / 2)  - (adjustedWidth / 2),
	                'width'   : adjustedWidth + 'px',
	                'height'  : availableHeight + 'px',
	                'padding' : photoBorder + 'px'
	            }, function() {
	                self.showInfo();
	                //navigator.showControls();
	            });
	        },

	        // Sets the photo container's image source.
	        setImageSource : function( src ) { 
	            this.image.attr('src', src).css({
	                'width'      : '100%',
	                'height'     : '100%',
	                'margin-top' : '0' 
	            });
	        }
	    } // photo

	signin.init();
	window.signInLoaded = true;
})(jQuery,document);