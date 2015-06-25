/*
需要引用jquery.notyfy/的js和css
*/
function alertOK(msg) {
    notyfy({
        text: msg,
        type: 'success',
        dismissQueue: true,
        layout: 'top', timeout: 1000,
        buttons: false
    });
}
function alertError(msg) {
    notyfy({
        text: msg,
        type: 'error',
        dismissQueue: true,
        layout: 'top', timeout: 3000,
        buttons: false
    });
}
function AjaxPost(url, frm, btn, callback) {
    var btnText = $(btn).html();
    $(btn).html('请稍候...').addClass('disabled');
    $('.alert_warning_my').removeClass('alert_warning_my');
    $.post(url, $('#' + frm).serializeArray(), function (json, status, req) {
        var success = json.result;
        var action = json.action;
        var msg = json.msg;
        $('.loading').hide();
        switch (action) {
            case "eval":
                eval(msg);
                break;
            case "alert":
                if (success) alertOK(msg);
                else alertError(msg);
                break;
            case "send_goto":
                alertOK(msg.msg);
                setTimeout(function () { location.href = msg.url; }, 2000);
                return;
            case "reload":
                alertOK(msg);
                setTimeout(function () { location.reload(); }, 2000);
                return;
            case "goto":
                location.href = msg;
                return;
            case "errors":
                var s = '';
                for (var o in msg) {
                    $('#e_' + o).addClass('alert_warning_my');
                    s += '<em>' + msg[o] + '</em><br />';
                }
                alertError(s);
                break;
        }

        //回调函数
        if (callback) {
            callback(json);
        }
    }, 'json').always(function () {
        $(btn).html(btnText).removeClass('disabled');
    });
}
