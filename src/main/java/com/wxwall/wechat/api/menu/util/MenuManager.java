package com.wxwall.wechat.api.menu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxwall.wechat.api.advanced.model.AccessToken;
import com.wxwall.wechat.api.menu.model.Button;
import com.wxwall.wechat.api.menu.model.CommonButton;
import com.wxwall.wechat.api.menu.model.ComplexButton;
import com.wxwall.wechat.api.menu.model.Menu;
import com.wxwall.wechat.api.util.CommonUtil;

/**  
*   
* 项目名称：wechatapi  
* 类名称：MenuManager  
* 类描述：菜单管理器类   
* 创建人：Myna Wang  
* 创建时间：2014-3-11 下午1:09:25  
* @version       
*/
public class MenuManager {  
    private static Logger log = LoggerFactory.getLogger(MenuManager.class);  
  
    public static void main(String[] args) {  
        // 第三方用户唯一凭证  
        String appId = "appid";  
        // 第三方用户唯一凭证密钥  
        String appSecret = "appsecret";    
        // 调用接口获取access_token  
        AccessToken at = CommonUtil.getAccessToken(appId, appSecret);  

        if (null != at) {  
            // 调用接口创建菜单  
            boolean result = MenuUtil.createMenu(getMenu(), at.getAccesstoken());   
    		/*int result = WeixinUtil.createMenu(getMenu(), "0aFyg9AhkcWs__0NAAOWYR0O2pkV5yThzFFwIe_gh9ANHNV5-RwB1lo4iOyASzPlEuh4hpotT418drJ8LHQGyilOBpMVJQ9JPinr8cN5XUFfVQNkGzz-GChXTeoqbULnVHy-F1XVNVMyjUji8mbEhQ");*/
            // 判断菜单创建结果  
            if (result)  
                log.info("菜单创建成功！ok");  
            else  
                log.info("菜单创建失败，错误码：" + result);  
        }  
    }  
  
    /** 
     * 组装菜单数据 
     *  
     * @return 
     */  
    private static Menu getMenu() {  
 
        CommonButton btn21 = new CommonButton();  
        btn21.setName("在线贷款");  
        btn21.setType("click");  
        btn21.setKey("21");  

        CommonButton btn22 = new CommonButton();  
        btn22.setName("申请条件");  
        btn22.setType("click");  
        btn22.setKey("22");  
  
        CommonButton btn23 = new CommonButton();  
        btn23.setName("进度查询");  
        btn23.setType("click");  
        btn23.setKey("23"); 

        ComplexButton mainBtn1 = new ComplexButton();  
        mainBtn1.setName("操作说明");
        mainBtn1.setType("click");  
        mainBtn1.setKey("1+"); 

        ComplexButton mainBtn2 = new ComplexButton();  
        mainBtn2.setName("我要贷款");  
        mainBtn2.setSub_button(new CommonButton[] { btn21, btn22, btn23});  
  
        ComplexButton mainBtn3 = new ComplexButton();  
        mainBtn3.setName("微公益");
        mainBtn3.setType("click");  
        mainBtn3.setKey("3+"); 
        /** 
         * 每个一级菜单都有二级菜单项 
         * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？
         * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：
         * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 }); 
         */  
        Menu menu = new Menu();  
        menu.setButton(new Button[] { mainBtn1, mainBtn2,mainBtn3});    
        return menu;  
    }  
}  
