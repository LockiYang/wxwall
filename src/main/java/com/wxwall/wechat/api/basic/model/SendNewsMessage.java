package com.wxwall.wechat.api.basic.model;

import java.util.List;

/**  
*   
* 项目名称：wechatapi  
* 类名称：SendNewsMessage  
* 类描述：回复图文消息   
* 创建人：Myna Wang  
* 创建时间：2014-1-26 下午7:27:48  
* @version       
*/
public class SendNewsMessage extends SendBaseMessage{
	// 图文消息个数，限制为10条以内  
    private int ArticleCount;  
    // 多条图文消息信息，默认第一个item为大图  
    private List<SendArticle> Articles;  
  
    public int getArticleCount() {  
        return ArticleCount;  
    }  
  
    public void setArticleCount(int articleCount) {  
        ArticleCount = articleCount;  
    }  
  
    public List<SendArticle> getArticles() {  
        return Articles;  
    }  
  
    public void setArticles(List<SendArticle> articles) {  
        Articles = articles;  
    }  
}
