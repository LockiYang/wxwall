$.ajaxSetup({
	cache: false,
	error: function (response) {
		console.log(response)
	}
});

(function ($,document) {

    var activityMid = util.getQueryString('activityMid') || -1;

    var actions = {
        showSection1: function (argument) {
            console.log('showSection1');
        },
        showSection2: function (argument) {
            console.log('showSection2');
        },
        showSection3: function (argument) {
            console.log('showSection3');
        },
        showSection4: function (argument) {
            console.log('showSection4');
        },
        showSection5: function (argument) {
            console.log('showSection5');
        }
    }

    var publish = {
        init: function  () {
            this.cacheElements();
            this.bindEvents();
            this.setMessage();
            this.mapRoutes();
            this.getActivityList();
            this.getWeChat();
        },
        cacheElements: function () {
            this.$publishNav = $('#publish-nav');
            this.$publishSection = $('#publish-section');
            this.$activityList = $('#activity-list');
            this.$weChatInfo = $('#weChatInfo');
            this.$navItems = this.$publishNav.find('.nav-item');
            this.$sectionItems = this.$publishSection.find('.section-item');
        },
        setMessage: function () {
            this.noData = '亲还没有活动哦，赶紧去发布一个';
        },
        bindEvents: function () {
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
                '/section1': actions.showSection1,
                '/section2': actions.showSection2,
                '/section3': actions.showSection3,
                '/section4': actions.showSection4,
                '/section5': actions.showSection5
            };
            var router = Router(routes);

            router.configure({
                on: publish.switchRoute
            });

            router.init();
        },
        getActivityList: function () {
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
        },
        getWeChat: function () {
            $.ajax({
                url: '../weixin/getWechat',
                dataType: 'json',
                success: function (response) {
                    var items = response.items,
                        emptyMsg = '<div class="empty-msg">'+publish.noData+'</div>',
                        errorMsg = '<div class="error-msg">'+response.message+'</div>',
                        $tbody = publish.$weChatInfo.find('tbody');

                    if (response.success) {
                    	 markup
                         = '<tr><td>'+ '微信号' + '</td>' + '<td>'+ response.data.weChatName + '</td></tr>'
                         + '<tr><td>'+ 'AppID(应用ID)' + '</td>' + '<td>'+ response.data.weChatId + '</td></tr>'
                         + '<tr><td>'+ 'AppSecret(应用密钥)' + '</td>' + '<td>'+ response.data.appSecret + '</td></tr>'
                         + '<tr><td>'+ 'URL' + '</td>' + '<td>'+ response.data.mid + '</td></tr>'
                         + '<tr><td>'+ 'Token' + '</td>' + '<td>'+ response.data.token + '</td></tr>';
                    	 $tbody.html(markup);
                    } else {
                        $tbody.html(errorMsg);
                    }
                }
            })
        },
    };

    publish.init();
	var optionsForWechat = {
		//target:'#output1',   // 从服务传过来的数据显示在这个div内部，也就是ajax局部刷新
		//data:{param1:"我自己的第一个额外的参数"},
		//dataType:'json',
		type:'POST',
		url:'../weixin/setWechat', //重新提交的url，即url可以在form中配置，也可以在这里配置。
		beforeSubmit:  checkSetWeChatParameter,  // ajax提交之前的处理
		success:       showResult// 处理之后的处理
	};
	$('#formForSetWeChat').submit(function() {
		$(this).ajaxSubmit(optionsForWechat);
		return false; //非常重要，如果是false，则表明是不跳转，在本页上处理，也就是ajax，如果是非false，则传统的form跳转。
	});
	
	var optionsForActivity = {
		//target:'#output1',   // 从服务传过来的数据显示在这个div内部，也就是ajax局部刷新
		//data:{param1:"我自己的第一个额外的参数"},
		//dataType:'json',
		type:'POST',
		url:'../activity/setActivity', //重新提交的url，即url可以在form中配置，也可以在这里配置。
		beforeSubmit:  checkSetActivityParameter,  // ajax提交之前的处理
		success:       showResult// 处理之后的处理
	};
	$('#formForActivity').submit(function() {
		$(this).ajaxSubmit(optionsForActivity);
		return false; //非常重要，如果是false，则表明是不跳转，在本页上处理，也就是ajax，如果是非false，则传统的form跳转。
	});
})(jQuery,document);

function showResult(responseText, statusText, xhr, $form)  {
    //alert(xhr.responseText+"=="+$form.attr("method")+'status: ' + statusText + '\n\nresponseText: \n' + responseText);
	//xhr：说明你可以用ajax来自己再次发出请求
	//$form：是那个form对象，是一个jquery对象
	//statusText：状态，成功则为success
	//responseText，服务器返回的是字符串（当然包括html，不包括json）
	if (responseText.success == true) {
		alert('设置成功!');
	} else {
		alert('设置失败,' + responseText.msg);
	}
    
}
/** 
 * 检查微信公众号设置参数 
 * @returns {Boolean} 
 */  
function checkSetWeChatParameter(formData, jqForm, options) {
    //formData是数组，就是各个input的键值map数组
    //通过这个方法来进行处理出来拼凑出来字符串。
    //formData：拼凑出来的form字符串，比如name=hera&password，其实就是各个表单中的input的键值对，如果加上method=XXXX，那也就是相当于ajax内的data。
    //var queryString = $.param(formData);
    for (var i=0; i < formData.length; i++) {
    	if (formData[i].name == 'weChatName' && formData[i].value.replace(/(^\s*)|(\s*$)/g, "").length ==0) {
    		alert('微信号不能设置为空!');
    		return false;
    	}
    	
    	if (formData[i].name == 'weChatID' && formData[i].value.replace(/(^\s*)|(\s*$)/g, "").length ==0) {
    		alert('微信AppID(应用ID)不能设置为空!');
    		return false;
    	}
    	
    	if (formData[i].name == 'weChatSecret' && formData[i].value.replace(/(^\s*)|(\s*$)/g, "").length ==0) {
    		alert('微信AppSecret(应用密钥)不能设置为空!');
    		return false;
    	}
    }
    //jqForm，jquery form对象
    //var formElement = jqForm[0];
    //alert($(formElement).attr("method"));
    //alert($(jqForm[0].name).attr("maxlength"));
       
    //非常重要，返回true则说明在提交ajax之前你验证成功，则提交ajax form，如果验证不成功，则返回非true，不提交
    return true;
}

function checkSetActivityParameter(formData, jqForm, options) {
    /*for (var i=0; i < formData.length; i++) {
    	if (formData[i].name == 'weChatName' && formData[i].value.replace(/(^\s*)|(\s*$)/g, "").length ==0) {
    		alert('微信号不能设置为空!');
    		return false;
    	}
    	
    	if (formData[i].name == 'weChatID' && formData[i].value.replace(/(^\s*)|(\s*$)/g, "").length ==0) {
    		alert('微信AppID(应用ID)不能设置为空!');
    		return false;
    	}
    	
    	if (formData[i].name == 'weChatSecret' && formData[i].value.replace(/(^\s*)|(\s*$)/g, "").length ==0) {
    		alert('微信AppSecret(应用密钥)不能设置为空!');
    		return false;
    	}
    }*/
    return true;
}

/** 
 * 异步上传图片 
 * @returns {Boolean} 
 */  
function ajaxFileUpload() {  
    $("#loading").ajaxStart(function() {  
        $(this).show();  
    })//开始上传文件时显示一个图片    
    .ajaxComplete(function() {  
        $(this).hide();  
    });//文件上传完成将图片隐藏起来    
  
    var file = $("#img").val();  
    if(!/\.(gif|jpg|jpeg|png|JPG|PNG)$/.test(file))  
    {  
        alert("不支持的图片格式.图片类型必须是.jpeg,jpg,png,gif格式.");  
        return false;
    }  
     
    /*$.ajaxFileUpload({  
        url : '../common/uploadToTmp',//用于文件上传的服务器端请求地址    
        secureuri : false,//一般设置为false    
        fileElementId : 'img',//文件上传空间的id属性  <input type="file" id="file" name="file" />    
        dataType : 'json',//返回值类型 一般设置为json    
        success : function(data, status) //服务器成功响应处理函数    
        {
            // 图片在服务器上的相对地址，加随机数防止不刷新
        	alert(data);
            path = "../" + data.obj + "?" + Math.random();  
            $("#preImg").attr("src", path);
        },  
        error : function(data, status, e)//服务器响应失败处理函数    
        {  
        	alert(e);
        }  
    });  */
    return true;  
}  