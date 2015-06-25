$.ajaxSetup({
	cache: false,
	error: function (response) {
		console.log(response)
	}
});

$.validator.setDefaults({
    ignore: ':disabled, .ignore',
    submitHandler: function() {
        console.log("submitted!");
    }
});

(function ($,document) {

    var activityMid = util.getQueryString('activityMid') || -1;

    var actions = {
        
    }

    var publish = {
        init: function  () {
            this.cacheElements();
            this.bindEvents();
            this.setMessage();
            this.mapRoutes();
            this.loadUserInfo();
            this.loadActivityList();
            this.userInfo = {};
        },
        cacheElements: function () {
            this.$userName = $('#username');
            this.$publishNav = $('#publish-nav');
            this.$publishSection = $('#publish-section');
            this.$activityList = $('#activity-list');
            this.$weChatInfo = $('#weChatInfo');
            this.$navItems = this.$publishNav.find('.nav-item');
            this.$sectionItems = this.$publishSection.find('.section-item');

            this.$accountSection = $('#account-section');
            this.$dropControl = this.$accountSection.find('.drop-control');
            this.$fieldSection = this.$accountSection.find('.field-section');
            this.$displayform = this.$fieldSection.find('.init-form');
            this.$displayValues = this.$fieldSection.find('.ui-input-value');
        },
        setMessage: function () {
            this.noData = '亲还没有活动哦，赶紧去发布一个';
        },
        bindEvents: function () {
            this.$accountSection.on('mousedown', '.drop-control', $.proxy(this.dropdown,this));
            this.$accountSection.on('mouseup','.drop-control', $.proxy(this.cancelDropdown,this));
        },
        dropdown: function (e) {
            // e.preventDefault();
            this.$dropControl.addClass('md');
        },
        cancelDropdown: function (e) {
            // e.preventDefault();
            this.$dropControl.removeClass('md');
            setTimeout(function () {
                publish.$fieldSection.addClass('drop-down');
            },350);
        },
        dropup: function () {
            this.$fieldSection.removeClass('drop-down');
        },
        mapDisplayValues: function (data) {
            for (var i in data) {
                this.$displayValues.filter('[data-value='+i+']').html(data[i]);
            }
        },
        settingPublicAccount: function () {
            window.location.hash = '#/public-account-setting';
        },
        switchRoute: function () {
            var route = window.location.hash.slice(2),
                $navItems = publish.$navItems,
                $sectionItems = publish.$sectionItems,
                $navItem = $navItems.filter('[data-action=' + route + ']'),
                $sectionItem = $sectionItems.filter('[data-route=' + route + ']');

            if ($sectionItem.size()) {
                $sectionItems.removeClass('cur');
                $sectionItem.addClass('cur');
                $navItems.removeClass('cur');
                $navItem.addClass('cur');
            }
        },
        mapRoutes: function () {
            var routes = {
                '/activities': function () {},
                '/publish': function () {},
                '/public-account-setting': function () {},
            };
            var router = Router(routes);

            router.configure({
                on: publish.switchRoute
            });

            router.init();
        },
        loadUserInfo: function () {
            $.ajax({
                url: '../json/userinfo.json',
                dataType: 'json',
                success: function (response) {
                    if (response.success) {
                        var userInfo = publish.userInfo = response.userInfo;
                        publish.$userName.html(userInfo.userName);
                        if (!userInfo.publicAccountSetting) {
                            publish.settingPublicAccount();
                        }
                    } else {
                        publish.$userName.html('未知用户');
                    }
                }
            })
        },
        loadActivityList: function () {
            $.ajax({
                url: '../json/activity-list.json?activityMid=' + activityMid,
                dataType: 'json',
                success: function (response) {
                    var items = response.items,
                        emptyMsg = '<div class="empty-msg">'+publish.noData+'</div>',
                        errorMsg = '<div class="error-msg">'+response.message+'</div>',
                        markup
                        = '<tr>'
                        + '<td class="index">${index}</td>'
                        + '<td><a target="blank" href="../upbang.html?activityMid=${id}">${name}</a></td>'
                        + '<td>${status}</td>'
                        + '</tr>';

                    var $tmpl= '',
                        $tbody = publish.$activityList.find('tbody');

                    if (response.success) {
                        if (items && items.length && (typeof items !== 'string')) {
                            $.template('html', markup);
                            $tmpl = $.tmpl('html', items);
                            $tbody.html($tmpl);
                        } else {
                            $tbody.html(emptyMsg);
                        }
                    } else {
                        $tbody.html(errorMsg);
                    }
                }
            })
        }
    };

    publish.init();

    $("#init-form").validate({
        rules: {
            weChatName: "required",
            weChatID: "required",
            weChatSecret: "required"
        },
        submitHandler: function (form) {
            $(form).ajaxSubmit({
                target: '#result-msg',
                dataType: 'json', 
                beforeSubmit: function (a,b,c) {
                    // console.log(a,b,c)
                },
                success: function (response) {
                    publish.dropup();
                    publish.mapDisplayValues(response.data)
                }
            });
        }
    });

})(jQuery,document);


