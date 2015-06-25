package com.wxwall.common.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.wxwall.modules.wechat.entity.ShakeCacheUserInfo;
/**
 * 摇一摇活动管理，基于内存
 * @author ganjun
 *
 */
public class ShakeManager {
	protected Logger LOG = Logger.getLogger(getClass());
	
	private static ShakeManager instance;
	private Map<String, Map<String, Object>> shakeInfoMap;
	private Map<String, Map<String, Object>> cacheShakeInfoMap;
	/**
	 * 单例模式开启
	 */
	private ShakeManager() {
		this.shakeInfoMap = new HashMap<String, Map<String, Object>>();
		this.cacheShakeInfoMap = new HashMap<String, Map<String, Object>>();
	}
	public static ShakeManager getInstance() {
		if (instance == null) {
			synchronized (ShakeManager.class) {
				if (instance == null) {
					instance = new ShakeManager();
				}
			}
		}
		return instance;
	}
	/**
	 * 摇一摇游戏是否开始
	 * @param activityMid
	 * @return
	 */
	public boolean isShakeStart(String activityMid) {
		Map<String, Object> shakeInfo = shakeInfoMap.get(activityMid);
		if (shakeInfo != null) {
			//Map<String, ShakeCacheUserInfo> shakeUserInfo = (Map<String, ShakeCacheUserInfo>)shakeInfo.get("userInfo");
			return true;
		} else {
			return false;
		}
	}
	
	public int getEndShakeNum(String activityMid) {
		Map<String, Object> shakeInfo = shakeInfoMap.get(activityMid);
		if (shakeInfo != null) {
//			int endShakeNum = Integer.parseInt(String.valueOf(shakeInfo.get("endShakeNum")));
			int endShakeNum = (Integer) shakeInfo.get("endShakeNum");
			return endShakeNum;
		} else {
			return -1;
		}
	}
	/**
	 * 开始摇一摇游戏
	 * @param activityMid
	 */
	public void startShake(String activityMid, int endShakeNum) {
		Map<String, Object> shakeInfo = shakeInfoMap.get(activityMid);
		if (shakeInfo == null) {
			shakeInfo = new HashMap<String, Object>();
			Map<String, ShakeCacheUserInfo> userInfo = new HashMap<String, ShakeCacheUserInfo>();
			shakeInfo.put("userInfo", userInfo);
			shakeInfo.put("endShakeNum", endShakeNum);
			shakeInfoMap.put(activityMid, shakeInfo);
		}
	}
	/**
	 * 更新用户摇一摇次数
	 * @param activityMid
	 * @param userID
	 * @param shakeNum
	 */
	public void updateUserShakeNum(String activityMid, String uuid, int shakeNum) {
		Map<String, Object> shakeInfo = shakeInfoMap.get(activityMid);
		if (shakeInfo != null) {
			Map<String, ShakeCacheUserInfo> userInfo = (Map<String, ShakeCacheUserInfo>)shakeInfo.get("userInfo");
			if (userInfo.containsKey(uuid)) {
				ShakeCacheUserInfo shakeCacheUserInfo = userInfo.get(uuid);
				shakeCacheUserInfo.setShakeNum(shakeNum);
			} else {
				ShakeCacheUserInfo shakeCacheUserInfo = new ShakeCacheUserInfo();
				shakeCacheUserInfo.setShakeNum(shakeNum);
				userInfo.put(uuid, shakeCacheUserInfo);
			}
		}
	}
	/**
	 * 获得用户摇一摇次数
	 * @param activityMid
	 * @param userID
	 * @return
	 */
	public int getUserShakeNum(String activityMid, String uuid) {
		Map<String, Object> shakeInfo = shakeInfoMap.get(activityMid);
		if (shakeInfo != null) {
			Map<String, ShakeCacheUserInfo> userInfo = (Map<String, ShakeCacheUserInfo>)shakeInfo.get("userInfo");
			ShakeCacheUserInfo ShakeCacheUserInfo = userInfo.get(uuid);
			if (ShakeCacheUserInfo != null) {
				return ShakeCacheUserInfo.getShakeNum();
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
	/**
	 * 停止摇一摇游戏，把数据转入缓存中
	 * @param activityMid
	 */
	public void stopShake(String activityMid) {
		Map<String, Object> shakeInfo = shakeInfoMap.get(activityMid);
		if (shakeInfo != null) {
			Map<String, ShakeCacheUserInfo> userInfo = (Map<String, ShakeCacheUserInfo>)shakeInfo.get("userInfo");
			//缓存
			Map<String, Object> cacheShakeInfo = cacheShakeInfoMap.get(activityMid);
			if (cacheShakeInfo != null) {
				cacheShakeInfoMap.remove(activityMid);
			}
			cacheShakeInfo = new ConcurrentHashMap<String, Object>();
			Map<String, ShakeCacheUserInfo> cacheUserInfo = sortMapByValue(userInfo);
			if (cacheUserInfo == null) {
				cacheUserInfo = new LinkedHashMap<String, ShakeCacheUserInfo>();
			}
			cacheShakeInfo.put("userInfo", cacheUserInfo);
			cacheShakeInfo.put("totalUser", userInfo.size());
			cacheShakeInfo.put("updateTime", Calendar.getInstance(TimeZone.getDefault()).getTime());
			cacheShakeInfoMap.put(activityMid, cacheShakeInfo);
			
			userInfo.clear();
			shakeInfoMap.remove(activityMid);
		}
	}
	/**
	 * 返回摇一摇列表
	 * @param activityMid
	 * @return
	 */
	public Map<String, ShakeCacheUserInfo> getShakeList(String activityMid) {
		Map<String, Object> shakeInfo = shakeInfoMap.get(activityMid);
		if (shakeInfo != null) {
			Map<String, ShakeCacheUserInfo> userInfo = (Map<String, ShakeCacheUserInfo>)shakeInfo.get("userInfo");
			Map<String, ShakeCacheUserInfo> sortUserInfo = sortMapByValue(userInfo);
			if (sortUserInfo == null) {
				sortUserInfo = new LinkedHashMap<String, ShakeCacheUserInfo>();
			}
			return sortUserInfo;
		} else {
			Map<String, Object> cacheShakeInfo = cacheShakeInfoMap.get(activityMid);
			if (cacheShakeInfo != null) {
				return (Map<String, ShakeCacheUserInfo>)cacheShakeInfo.get("userInfo");
			} else {
				return new LinkedHashMap<String, ShakeCacheUserInfo>();
			}
		}
	}
	/**
	 * 返回摇一摇总数
	 * @param activityMid
	 * @return
	 */
	public int getTotalUser(String activityMid) {
		Map<String, Object> shakeInfo = shakeInfoMap.get(activityMid);
		if (shakeInfo != null) {
			Map<String, ShakeCacheUserInfo> userInfo = (Map<String, ShakeCacheUserInfo>)shakeInfo.get("userInfo");
			return userInfo.size();
		} else {
			Map<String, Object> cacheShakeInfo = cacheShakeInfoMap.get(activityMid);
			if (cacheShakeInfo != null) {
				return Integer.parseInt(String.valueOf(cacheShakeInfo.get("totalUser")));
			} else {
				return 0;
			}
		}
	}
	/**
	 * map基于值排序
	 * @param map
	 * @return
	 */
	public static Map<String, ShakeCacheUserInfo> sortMapByValue(Map<String, ShakeCacheUserInfo> map) {  
        if (map == null || map.isEmpty()) {  
            return null;  
        }  
        Map<String, ShakeCacheUserInfo> sortedMap = new LinkedHashMap<String, ShakeCacheUserInfo>();
        List<Map.Entry<String, ShakeCacheUserInfo>> entryList = new ArrayList<Map.Entry<String, ShakeCacheUserInfo>>(map.entrySet());  
        Collections.sort(entryList, new MapValueComparator());  
        Iterator<Map.Entry<String, ShakeCacheUserInfo>> iter = entryList.iterator();  
        Map.Entry<String, ShakeCacheUserInfo> tmpEntry = null;
        int count = 0;
        while (iter.hasNext()) {  
        	count++;
        	if (count > 15) {
        		break;
        	}
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
        }  
        return sortedMap;  
    }
	
	/**
	 * 定时清理缓存，默认一小时
	 */
	public void cleanCache() {
		LOG.info("shake cache num before starting clean: " + cacheShakeInfoMap.size());
		for (String key : cacheShakeInfoMap.keySet()) {
			Map<String, Object> cacheShakeInfo = cacheShakeInfoMap.get(key);
			Date updateTime = (Date)cacheShakeInfo.get("updateTime");
			Date currentTime = Calendar.getInstance(TimeZone.getDefault()).getTime();
			if ((currentTime.getTime() - updateTime.getTime()) > 60 * 60 * 1000) {
				cacheShakeInfoMap.remove(key);
			}
		}
		LOG.info("shake cache num after starting clean: " + cacheShakeInfoMap.size());
	}
}

//比较器类  
class MapValueComparator implements Comparator<Map.Entry<String, ShakeCacheUserInfo>> {  
  public int compare(Entry<String, ShakeCacheUserInfo> me1, Entry<String, ShakeCacheUserInfo> me2) {
	  if (me1.getValue().getShakeNum() > me2.getValue().getShakeNum()) {
		  return -1;
	  } else if (me1.getValue().getShakeNum() == me2.getValue().getShakeNum()) {
		  return 0;
	  } else {
		  return 1;
	  }
      //return me1.getValue().compareTo(me2.getValue());  
  }  
}  
