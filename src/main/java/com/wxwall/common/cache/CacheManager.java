package com.wxwall.common.cache;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CacheManager {
	private static LoadingCache<String, OAuthCacheUserInfo> oAuthUserCache = CacheBuilder.newBuilder()
	        .maximumSize(10000)
	        .expireAfterAccess(10, TimeUnit.MINUTES)
	        .build(new CacheLoader<String, OAuthCacheUserInfo>(){
	            @Override
	            public OAuthCacheUserInfo load(String key) throws Exception {        
	                return null;
	            }
	            
	        });
	
	public static LoadingCache<String, OAuthCacheUserInfo> getOAuthUserCache() {
		return oAuthUserCache;
	}
	
	public static void main(String[] args) {
		oAuthUserCache.put("xxx", new OAuthCacheUserInfo(""));
		System.out.print(oAuthUserCache.getIfPresent("xxx"));;
		System.out.print(oAuthUserCache.getIfPresent("xxxx"));;
	}
}
