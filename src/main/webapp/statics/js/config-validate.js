/*!
 * 表单验证参数配置文件config-validate.js
 * @author mo-om
 * @dependence [jQuery, jQuery.validate]
 * @app.formRules {Object} all form rules config;
 * @app.validateOpts {Object} default validate options of the form
 * @app.getFormValidateOpts {Function} return validate options which form you pass
 */

// $.validator.messages localized
$.extend($.validator.messages, {
	url: "请输入合法的网址",
	date: "请输入合法的日期",
	email: "请输入正确格式的电子邮件",
	number: "请输入合法的数字",
	digits: "只能输入整数",
	remote: "请修正该字段",
	accept: "请输入拥有合法后缀名的字符串",
	dateISO: "请输入合法的日期 (ISO).",
	equalTo: "请再次输入相同的值",
	required: '不能为空',
	creditcard: "请输入合法的信用卡号",
	max: $.validator.format("请输入一个最大为{0}的值"),
	min: $.validator.format("请输入一个最小为{0}的值"),
	range: $.validator.format("请输入一个介于{0}和{1}之间的值"),
	maxlength: $.validator.format("请输入一个长度最多是{0}的字符串"),
	minlength: $.validator.format("请输入一个长度最少是{0}的字符串"),
	rangelength: $.validator.format("请输入一个长度介于{0}和{1}之间的字符串")
});

// 手机号码验证 
$.validator.addMethod("mobile", function(value, element) { 
  var length = value.length; 
  var mobile = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
  return this.optional(element) || (length == 11 && mobile.test(value)); 
}, "请填写正确的手机号码");
