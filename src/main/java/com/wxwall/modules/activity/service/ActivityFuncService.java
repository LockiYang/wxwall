package com.wxwall.modules.activity.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wxwall.common.entity.Result;
import com.wxwall.common.scheduler.ActivityShake;
import com.wxwall.common.scheduler.SchedulerManager;
import com.wxwall.common.scheduler.SendPrizeMsg;
import com.wxwall.common.service.BaseService;
import com.wxwall.common.utils.CommonUtils;
import com.wxwall.common.utils.ShakeManager;
import com.wxwall.modules.activity.dao.ActivityPrizeDao;
import com.wxwall.modules.activity.dao.WechatUserFatePairDao;
import com.wxwall.modules.activity.dao.WechatUserPrizeListDao;
import com.wxwall.modules.activity.entity.ActivityPrize;
import com.wxwall.modules.activity.entity.WechatUserFatePair;
import com.wxwall.modules.activity.entity.WechatUserPrizeList;
import com.wxwall.modules.user.dao.UserDao;
import com.wxwall.modules.wechat.dao.ActivityAutoReplyDao;
import com.wxwall.modules.wechat.dao.ActivityImageDao;
import com.wxwall.modules.wechat.dao.ActivityPhotoDao;
import com.wxwall.modules.wechat.dao.AutoReplyDao;
import com.wxwall.modules.wechat.dao.RActivityWeChatUserDao;
import com.wxwall.modules.wechat.dao.WeActivitySchedulerDao;
import com.wxwall.modules.wechat.dao.WeActivityTipsDao;
import com.wxwall.modules.wechat.dao.WeChatActivityDao;
import com.wxwall.modules.wechat.dao.WeChatImageTextDao;
import com.wxwall.modules.wechat.dao.WeChatTextDao;
import com.wxwall.modules.wechat.dao.WeChatUserDao;
import com.wxwall.modules.wechat.dao.WeChatUserMsgDao;
import com.wxwall.modules.wechat.entity.ActivityAutoReply;
import com.wxwall.modules.wechat.entity.ActivityImage;
import com.wxwall.modules.wechat.entity.ActivityPhoto;
import com.wxwall.modules.wechat.entity.AutoReply;
import com.wxwall.modules.wechat.entity.RActivityWeChatUser;
import com.wxwall.modules.wechat.entity.ShakeCacheUserInfo;
import com.wxwall.modules.wechat.entity.WeActivityScheduler;
import com.wxwall.modules.wechat.entity.WeActivityTips;
import com.wxwall.modules.wechat.entity.WeChatActivity;
import com.wxwall.modules.wechat.entity.WeChatApp;
import com.wxwall.modules.wechat.entity.WeChatUser;

/**
 * 活动各个功能服务层
 * 
 * @author Locki<lockiyang@qq.com>
 * @since 2015年4月9日
 * 
 */
@Service
@Transactional
public class ActivityFuncService extends BaseService {

	@Autowired
	private WeChatActivityDao weChatActivityDao;

	@Autowired
	private WeChatUserMsgDao weChatUserMsgDao;

	@Autowired
	private WeChatUserDao weChatUserDao;

	@Autowired
	private RActivityWeChatUserDao rActivityWeChatUserDao;

	@Autowired
	private ActivityPrizeDao activityPrizeDao;

	@Autowired
	private WechatUserPrizeListDao wechatUserPrizeListDao;

	@Autowired
	private ActivityImageDao activityImageDao;
	@Autowired
	private ActivityPhotoDao activityPhotoDao;

	@Autowired
	private WeChatTextDao weChatTextDao;

	@Autowired
	private WeChatImageTextDao weChatImageTextDao;

	@Autowired
	private AutoReplyDao autoReplyDao;

	@Autowired
	private ActivityAutoReplyDao activityAutoReplyDao;

	@Autowired
	private WeActivitySchedulerDao weActivitySchedulerDao;

	@Autowired
	private WeActivityTipsDao weActivityTipsDao;
	@Autowired
	private WechatUserFatePairDao wechatUserFatePairDao;
	@Autowired
	private UserDao userDao;

	/**
	 * 按顺序返回签到用户
	 * 
	 * @param mid
	 * @param serial
	 * @return
	 */
	public WeChatUser getSignInUser(String mid, int serial) {
		WeChatActivity weChatActivity = weChatActivityDao
				.findByActivityMid(mid);
		if (weChatActivity == null) {
			return null;
		}
		WeChatUser weChatUser = null;
		RActivityWeChatUser raw = rActivityWeChatUserDao
				.findSingleByActivityId(weChatActivity.getId(), serial);
		if (raw != null && raw.getWeChatUser() != null) {
			weChatUser = raw.getWeChatUser();
		}
		return weChatUser;
	}
	/**
	 * 获得最新的精彩瞬间图片
	 * @param mid
	 * @param serial
	 * @return
	 */
	public ActivityPhoto getSignPhotoPile(String mid, int serial) {
		WeChatActivity weChatActivity = weChatActivityDao
				.findByActivityMid(mid);
		if (weChatActivity == null) {
			return null;
		}
		ActivityPhoto activityPhoto = activityPhotoDao.findByWeChatActivityIDAndSerialD(weChatActivity.getId(), serial);
		return activityPhoto;
	}
	/**
	 * 获取精彩瞬间列表
	 * @param mid
	 * @param limit
	 * @return
	 */
	public Result listPhotoPile(String mid, int limit, String serverUrl) {
		Result result = new Result();
		List<ActivityPhoto> activityPhotos = activityPhotoDao.findByWeChatActivityMID(mid);
		List<Object> listActivityPhotos = new ArrayList<Object>();
		if (activityPhotos != null && activityPhotos.size() != 0) {
			int count = 0;
			for (int i=activityPhotos.size()-1; i>=0; i--) {
				ActivityPhoto activityPhoto = activityPhotos.get(i);
				Map<String, String> obj = new HashMap<String, String>();
				if (activityPhoto.getType().equalsIgnoreCase(ActivityPhoto.SYSTEM_UPLOAD)) {
					obj.put("imgSrc", serverUrl + File.separator + activityPhoto.getRelativePath());
				} else {
					obj.put("imgSrc", activityPhoto.getUrl());
				}
				listActivityPhotos.add(obj);
				count++;
				if (count >= limit) {
					break;
				}
			}
		}
		result.setSuccess(true);
		result.setItems(listActivityPhotos);
		return result;
	}

	/**
	 * 返回活动奖项
	 * 
	 * @param activityId
	 *            活动ID
	 * @param prizeId
	 *            奖项ID
	 * @return
	 */
	public ActivityPrize getActivityPrize(String activityMid, long prizeId) {
		return activityPrizeDao.findByWechatActivityMIDAndPrizeId(activityMid,
				prizeId);
	}

	public Result deleteWeActivityScheduler(WeChatActivity weChatActivity,
			long schedulerId) {
		Result result = new Result();
		WeActivityScheduler weActivityScheduler = weActivitySchedulerDao
				.findByWechatActivityMIDAndSchedulerId(
						weChatActivity.getActivityMid(), schedulerId);
		if (weActivityScheduler == null) {
			result.setMessage("日程!");
			result.setSuccess(false);
			return result;
		}
		weActivitySchedulerDao.delete(schedulerId);
		result.setSuccess(true);
		return result;
	}

	/**
	 * 删除活动奖项
	 * 
	 * @param activityId
	 * @param prizeId
	 * @return
	 */
	public Result deleteActivityPrize(WeChatActivity weChatActivity,
			long prizeId, String rootPath) {
		Result result = new Result();
		try {
			ActivityPrize activityPrize = activityPrizeDao
					.findByWechatActivityMIDAndPrizeId(
							weChatActivity.getActivityMid(), prizeId);
			if (activityPrize == null) {
				result.setMessage("未发现次奖项!");
				result.setSuccess(false);
				return result;
			}
			/**
			 * 防止奖项设置未设置情况下开启抽奖
			 */
			if (weChatActivity.getActivityPrizes().size() == 1
					&& weChatActivity.isfDraw() == true) {
				result.setMessage("请先关闭抽奖设置按钮!");
				result.setSuccess(false);
				return result;
			}
			activityPrizeDao.delete(prizeId);
			String preViewImg = rootPath + File.separator
					+ activityPrize.getImg();
			if (StringUtils.isNoneBlank(activityPrize.getImg())) {
				File orignLogoFile = new File(preViewImg);
				if (orignLogoFile.exists()) {
					FileUtils.forceDelete(orignLogoFile);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(), e);
			result.setMessage("系统错误!");
			result.setSuccess(false);
			return result;
		}
		result.setSuccess(true);
		return result;
	}

	/**
	 * 活动功能开关
	 * 
	 * @param activityId
	 * @param fDraw
	 * @param fLogo
	 * @param fBackground
	 * @param fAutoUpWall
	 * @param fAutoFiter
	 * @param fVote
	 * @param fShake
	 * @param fPair
	 * @param fSms
	 * @param fSignIn
	 * @param fScheduler
	 * @return
	 */
	public Result funcSwith(String activityMid, String fDraw, String fLogo,
			String fBackground, String fAutoUpWall, String fAutoFiter,
			String fVote, String fShake, String fPair, String fSms,
			String fSignIn, String fScheduler, String fAlbum, String fActivityPhoto) {
		Result result = new Result();
		WeChatActivity weChatActivity = weChatActivityDao
				.findByActivityMid(activityMid);
		// 检查系统是否存在此活动
		if (weChatActivity == null) {
			result.setMessage("系统未发现此活动！");
			result.setSuccess(false);
			return result;
		}
		// 是否抽奖
		if (StringUtils.equalsIgnoreCase("true", fDraw)) {

			if (weChatActivity.getActivityPrizes() == null
					|| weChatActivity.getActivityPrizes().size() == 0) {
				result.setSuccess(false);
				result.setMessage("请先进行奖项设置！");
				return result;
			}
			weChatActivity.setfDraw(true);
		} else if (StringUtils.equalsIgnoreCase("false", fDraw)) {
			weChatActivity.setfDraw(false);
		}
		// 是否自定义logo
		if (StringUtils.equalsIgnoreCase("true", fLogo)) {
			if (StringUtils.isBlank(weChatActivity.getLogo())) {
				result.setSuccess(false);
				result.setMessage("请先上传logo图片！");
				return result;
			}
			weChatActivity.setfLogo(true);
		} else if (StringUtils.equalsIgnoreCase("false", fLogo)) {
			weChatActivity.setfLogo(false);
		}
		// 允许自定义背景
		if (StringUtils.equalsIgnoreCase("true", fBackground)) {
			if (weChatActivity.getBackgroudImgs() == null
					|| weChatActivity.getBackgroudImgs().size() == 0) {
				result.setSuccess(false);
				result.setMessage("请先上传背景图片！");
				return result;
			}
			weChatActivity.setfBackground(true);
		} else if (StringUtils.equalsIgnoreCase("false", fBackground)) {
			weChatActivity.setfBackground(false);
		}
		// 自动上墙
		if (StringUtils.equalsIgnoreCase("true", fAutoUpWall)) {
			weChatActivity.setfAutoUpWall(true);
		} else if (StringUtils.equalsIgnoreCase("false", fAutoUpWall)) {
			weChatActivity.setfAutoUpWall(false);
		}
		// 自动上墙过滤
		if (StringUtils.equalsIgnoreCase("true", fAutoFiter)) {
			weChatActivity.setfAutoFiter(true);
		} else if (StringUtils.equalsIgnoreCase("false", fAutoFiter)) {
			weChatActivity.setfAutoFiter(false);
		}
		// 投票
		if (StringUtils.equalsIgnoreCase("true", fVote)) {
			weChatActivity.setfVote(true);
		} else if (StringUtils.equalsIgnoreCase("false", fVote)) {
			weChatActivity.setfVote(false);
		}
		// 摇一摇
		if (StringUtils.equalsIgnoreCase("true", fShake)) {
			weChatActivity.setfShake(true);
		} else if (StringUtils.equalsIgnoreCase("false", fShake)) {
			weChatActivity.setfShake(false);
		}
		// 对对碰
		if (StringUtils.equalsIgnoreCase("true", fPair)) {
			weChatActivity.setfPair(true);
		} else if (StringUtils.equalsIgnoreCase("false", fPair)) {
			weChatActivity.setfPair(false);
		}
		// 短信
		if (StringUtils.equalsIgnoreCase("true", fSms)) {
			weChatActivity.setfSms(true);
		} else if (StringUtils.equalsIgnoreCase("false", fSms)) {
			weChatActivity.setfSms(false);
		}
		// 签到
		if (StringUtils.equalsIgnoreCase("true", fSignIn)) {
			weChatActivity.setfSignIn(true);
		} else if (StringUtils.equalsIgnoreCase("false", fSignIn)) {
			weChatActivity.setfSignIn(false);
		}
		// 日程安排
		if (StringUtils.equalsIgnoreCase("true", fScheduler)) {
			weChatActivity.setfScheduler(true);
		} else if (StringUtils.equalsIgnoreCase("false", fScheduler)) {
			weChatActivity.setfScheduler(false);
		}
		
		if (StringUtils.equalsIgnoreCase("true", fActivityPhoto)) {
			weChatActivity.setfActivityPhoto(true);
		} else if (StringUtils.equalsIgnoreCase("false", fActivityPhoto)) {
			weChatActivity.setfActivityPhoto(false);
		}
		

		// 自定义相册
		if (StringUtils.equalsIgnoreCase("true", fAlbum)) {
			if (StringUtils.isBlank(weChatActivity.getAlbumSubject())) {
				result.setSuccess(false);
				result.setMessage("请先设置相册主题！");
				return result;
			}
			List<ActivityImage> listAlbum = activityImageDao
					.findByWeChatActivityMID(weChatActivity.getActivityMid(),
							ActivityImage.IMAGE_TYPE.ALBUM.getName());
			if (listAlbum == null || listAlbum.size() <= 0) {
				result.setSuccess(false);
				result.setMessage("请先上传相片！");
				return result;
			}
			weChatActivity.setfAlbum(true);
			;
		} else if (StringUtils.equalsIgnoreCase("false", fAlbum)) {
			weChatActivity.setfAlbum(false);
		}

		weChatActivityDao.save(weChatActivity);
		result.setSuccess(true);
		return result;
	}

	/**
	 * 返回活动的奖项设置
	 * 
	 * @param activityId
	 * @return
	 */
	public List<ActivityPrize> getActivityPrizes(String activityMid) {
		return activityPrizeDao.findByActivityMID(activityMid);
	}

	/**
	 * 新增或者更新奖项
	 * 
	 * @param weChatActivity
	 * @param strPrizeId
	 * @param prizeId
	 * @param prizeName
	 * @param description
	 * @param img
	 * @param winNum
	 * @param rootPath
	 * @return
	 */
	public Result addOrUpdateActivityPrize(WeChatActivity weChatActivity,
			String prizeId, String prizeName, String description, String img,
			int winNum, String rootPath) {
		Result result = new Result();
		try {
			if (StringUtils.isBlank(prizeId)) {// 新增
				String preViewImg = null;
				if (StringUtils.isNoneBlank(img)) {
					// 拷贝上传文件
					File tmpFile = new File(img);
					// 检查临时文件是否存在
					if (!tmpFile.exists()) {
						LOG.error("系统未发现已上传的图片！路径：" + img);
						result.setMessage("系统未发现已上传的图片！");
						result.setSuccess(false);
						return result;
					}

					String preViewImgName = tmpFile.getName();
					String relativePath = CommonUtils
							.generateNewDir(CommonUtils.STORE_PICTURE_DIR);
					String strUploadLogoPath = rootPath + File.separator
							+ relativePath;
					File destFile = new File(strUploadLogoPath, preViewImgName);
					FileUtils.copyFile(tmpFile, destFile, true);
					preViewImg = relativePath + File.separator + preViewImgName;
				}

				ActivityPrize activityPrize = new ActivityPrize(prizeName,
						description, preViewImg, winNum, weChatActivity);
				activityPrizeDao.save(activityPrize);
			} else {// 更新
				ActivityPrize activityPrize = activityPrizeDao
						.findByWechatActivityMIDAndPrizeId(
								weChatActivity.getActivityMid(),
								Long.parseLong(prizeId));
				if (activityPrize == null) {
					LOG.error("系统未发现此奖项！奖项ID：" + prizeId);
					result.setMessage("系统未发现此奖项！");
					result.setSuccess(false);
					return result;
				}
				String orignPreViewImg = null;
				activityPrize.setPrizeName(prizeName);
				activityPrize.setDescription(description);
				if (StringUtils.isNoneBlank(img)) {
					File tmpFile = new File(img);
					// 检查临时文件是否存在
					if (!tmpFile.exists()) {
						LOG.error("系统未发现已上传的图片！路径：" + img);
						result.setMessage("系统未发现已上传的图片！");
						result.setSuccess(false);
						return result;
					}

					String preViewImgName = tmpFile.getName();
					String relativePath = CommonUtils
							.generateNewDir(CommonUtils.STORE_PICTURE_DIR);
					String strUploadLogoPath = rootPath + File.separator
							+ relativePath;
					File destFile = new File(strUploadLogoPath, preViewImgName);
					FileUtils.copyFile(tmpFile, destFile, true);
					orignPreViewImg = rootPath + File.separator
							+ activityPrize.getImg();
					activityPrize.setImg(relativePath + File.separator
							+ preViewImgName);
				}
				activityPrize.setWinNum(winNum);
				activityPrizeDao.save(activityPrize);
				if (StringUtils.isNotBlank(orignPreViewImg)) {
					File orignLogoFile = new File(orignPreViewImg);
					if (orignLogoFile.exists()) {
						FileUtils.forceDelete(orignLogoFile);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(), e);
			result.setMessage("系统内部错误！");
			result.setSuccess(false);
		}
		result.setSuccess(true);
		return result;
	}

	public Result addOrUpdateWeActivityScheduler(WeChatActivity weChatActivity,
			String schedulerId, Date noticeTime, String noticeContent) {
		Result result = new Result();
		if (StringUtils.isBlank(schedulerId)) {// 新增
			WeActivityScheduler weActivityScheduler = new WeActivityScheduler(
					noticeTime, noticeContent,
					WeActivityScheduler.STATUS.CHECKING.getIndex(), null,
					weChatActivity);
			weActivitySchedulerDao.save(weActivityScheduler);
		} else {// 更新
			WeActivityScheduler weActivityScheduler = weActivitySchedulerDao
					.findByWechatActivityMIDAndSchedulerId(
							weChatActivity.getActivityMid(),
							Long.parseLong(schedulerId));
			if (weActivityScheduler == null) {
				LOG.error("系统未发现此日程！日程ID：" + schedulerId);
				result.setMessage("系统未发现此日程！");
				result.setSuccess(false);
				return result;
			}
			weActivityScheduler.setNoticeTime(noticeTime);
			weActivityScheduler.setNoticeContent(noticeContent);
			weActivitySchedulerDao.save(weActivityScheduler);
		}
		result.setSuccess(true);
		return result;
	}

	/**
	 * 返回摇一摇排名列表
	 * 
	 * @param activityID
	 * @param limit
	 * @return
	 */
	public Result getShakeList(String activityMid, int limit) {
		// TODO
		Result result = new Result();
		WeChatActivity weChatActivity = weChatActivityDao
				.findByActivityMid(activityMid);
		if (weChatActivity == null) {
			LOG.warn("活动ID[" + activityMid + "]在数据库中未存在!");
			result.setSuccess(false);
			result.setMessage("请求参数错误");
			return result;
		}
		// PageRequest pageRequest = new PageRequest(0, limit, Direction.DESC,
		// new String[] { "shakeNum" });
		//
		//
		// Page<RActivityWeChatUser> rActivityWeChatUsersPage =
		// rActivityWeChatUserDao.findShakeUsersByActivityMID(activityMid,
		// pageRequest);
		// List<RActivityWeChatUser> rActivityWeChatUsers =
		// rActivityWeChatUsersPage.getContent();

		List<ShakeCacheUserInfo> listShakeUsers = new ArrayList<ShakeCacheUserInfo>();
		Map<String,ShakeCacheUserInfo> sortShakeUser = ShakeManager.getInstance()
				.getShakeList(activityMid);
		boolean fShakeStart = ShakeManager.getInstance().isShakeStart(
				activityMid);
		int totalUser = ShakeManager.getInstance().getTotalUser(activityMid);
		int count = 0;
		for (String uuid : sortShakeUser.keySet()) {
			count++;
			if (count > limit) {
				break;
			}
			ShakeCacheUserInfo shakeUserInfo = sortShakeUser.get(uuid);
			if (shakeUserInfo == null) {
				continue;
			}
			shakeUserInfo.setIndex(count);
			if (!shakeUserInfo.isLoadUserInfo()) {
				RActivityWeChatUser rActivityWeChatUser = rActivityWeChatUserDao.findByUuid(uuid);
				if (rActivityWeChatUser == null) {
					continue;
				}
				
				if (rActivityWeChatUser.getStatus() == RActivityWeChatUser.USER_STATUS.CANCEL_SIGN
						.getIndex()) {
					continue;
				}
				WeChatUser weChatUser = rActivityWeChatUser.getWeChatUser();
				shakeUserInfo.setId(rActivityWeChatUser.getId());
				shakeUserInfo.setAvatar(weChatUser.getHeadImgUrl());
				shakeUserInfo.setNick(weChatUser.getNickName());
				shakeUserInfo.setLoadUserInfo(true);
			}
			listShakeUsers.add(shakeUserInfo);
		}

		if (listShakeUsers.size() == 0) {
			result.setTotal(0);
			Map<String, Object> data = new HashMap<String, Object>();
			// data.put("shakeTime", weChatActivity.getShakeTime());
			data.put("fShakeStart", fShakeStart);
			result.setData(data);
			result.setSuccess(true);
			return result;
		} else {
			Map<String, Object> data = new HashMap<String, Object>();
			// data.put("shakeTime",
			// rActivityWeChatUserDao.getTotalShakeUsersByActivityMID(activityMid));
			data.put("fShakeStart", fShakeStart);
			data.put("endShakeNum", weChatActivity.getEndShakeNum());
			result.setTotal(totalUser);
			result.setData(data);
			result.setSuccess(true);
			result.setItems(listShakeUsers);
			return result;
		}
	}

	/**
	 * 更新摇一摇数据
	 * 
	 * @param activityID
	 * @param uuid
	 * @param shakeNum
	 * @return
	 */
	public Result updateActivityShake(String activityMid, String uuid,
			int shakeNum) {
		// TODO
		Result result = new Result();
//		WeChatActivity weChatActivity = weChatActivityDao
//				.findByActivityMid(activityMid);
//		if (weChatActivity == null) {
//			LOG.warn("活动ID[" + activityMid + "]在数据库中未存在!");
//			result.setSuccess(false);
//			result.setMessage("请求参数错误");
//			return result;
//		}

		boolean fShakeStart = ShakeManager.getInstance().isShakeStart(
				activityMid);
		if (!fShakeStart) {
			LOG.warn("活动[" + activityMid + "]摇一摇游戏还没有开始!");
			result.setSuccess(false);
			result.setMessage("摇一摇游戏还没有开始,请关注大屏幕!");
			return result;
		}

//		RActivityWeChatUser rActivityWeChatUser = rActivityWeChatUserDao
//				.findByUuid(uuid);
//		if (rActivityWeChatUser == null) {
//			LOG.warn("用户uuID[" + uuid + "]在数据库中未存在!");
//			result.setSuccess(false);
//			result.setMessage("你还未参加活动，请先签到!");
//			return result;
//		}
//
//		if (rActivityWeChatUser.getStatus() == RActivityWeChatUser.USER_STATUS.CANCEL_SIGN
//				.getIndex()) {
//			LOG.warn("用户uuID[" + uuid + "]已退出活动!");
//			result.setSuccess(false);
//			result.setMessage("你还未参加活动，请先签到!");
//			return result;
//		}

		shakeNum = ShakeManager.getInstance().getUserShakeNum(activityMid, uuid)
				+ shakeNum;
		int endShakeNum = ShakeManager.getInstance().getEndShakeNum(activityMid);
		ShakeManager.getInstance().updateUserShakeNum(activityMid,uuid, shakeNum);

		if (shakeNum >= endShakeNum && endShakeNum > 0) {
			try {
				SchedulerManager.getInstance().killJob(
						ActivityShake.CONTEXT_PREFIX + activityMid);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}

		// rActivityWeChatUser.setShakeNum(shakeNum);
		// rActivityWeChatUserDao.save(rActivityWeChatUser);
		result.setSuccess(true);
		return result;
	}

	public Result deleteSetupActivityAd(String activityMid, String setupType,
			String tipId) {
		Result result = new Result();
		WeChatActivity weChatActivity = weChatActivityDao
				.findByActivityMid(activityMid);
		if (weChatActivity == null) {
			// 错误处理
			LOG.error("delete_ad页面请求参数错误！在系统中未发现活动ID：" + activityMid);
			return null;
		}

		if (StringUtils.equalsIgnoreCase("tip", setupType)) {// 删除页面提示信息
			// 如果未存在tipId, 就新增
			if (StringUtils.isBlank(tipId)) {
				result.setSuccess(true);
				return result;
			}
			// 修改提示信息
			WeActivityTips weActivityTips = weActivityTipsDao.findOne(Long
					.parseLong(tipId));
			if (weActivityTips == null) {
				// 错误处理
				result.setSuccess(false);
				result.setMessage("提示信息未发现！");
				return result;
			}
			weActivityTipsDao.delete(Long.parseLong(tipId));
			result.setSuccess(true);
			return result;
		} else {
			result.setSuccess(true);
			return result;
		}
	}

	/**
	 * 设置相册主题
	 * 
	 * @param activityMid
	 * @param albumSubject
	 * @return
	 */
	public Result SetupActivityAlbumSubject(String activityMid,
			String albumSubject) {
		Result result = new Result();
		try {
			WeChatActivity weChatActivity = weChatActivityDao
					.findByActivityMid(activityMid);
			if (weChatActivity == null) {
				// 错误处理
				LOG.error("ad-setup页面请求参数错误！在系统中未发现活动MID：" + activityMid);
				return null;
			}

			if (StringUtils.isBlank(albumSubject)) {
				// 错误处理
				result.setSuccess(false);
				result.setMessage("相册主题不能为空！");
				return result;
			} else {
				weChatActivity.setAlbumSubject(albumSubject);
			}
			weChatActivityDao.save(weChatActivity);
			result.setSuccess(true);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误！");
			return result;
		}
	}
	/**
	 * 设置精彩瞬间
	 * @param activityMid
	 * @param adminUpload
	 * @param signUserUpload
	 * @param systemUpload
	 * @return
	 */
	public Result SetupActivityPhotoPile(String activityMid, String adminUpload, String signUserUpload, String systemUpload) {
		Result result = new Result();
		try {
			WeChatActivity weChatActivity = weChatActivityDao
					.findByActivityMid(activityMid);
			if (weChatActivity == null) {
				// 错误处理
				LOG.error("ad-setup页面请求参数错误！在系统中未发现活动MID：" + activityMid);
				return null;
			}
			
			if (adminUpload.equals("1")) {
				weChatActivity.setfActivityPhotoForAdmin(true);
			} else {
				weChatActivity.setfActivityPhotoForAdmin(false);
			}
			
			if (signUserUpload.equals("1")) {
				weChatActivity.setfActivityPhotoForSignUser(true);
			} else {
				weChatActivity.setfActivityPhotoForSignUser(false);
			}
			
			if (systemUpload.equals("1")) {
				weChatActivity.setfActivityPhotoForSystem(true);
			} else {
				weChatActivity.setfActivityPhotoForSystem(false);
			}
			
			weChatActivityDao.save(weChatActivity);
			result.setSuccess(true);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误！");
			return result;
		}
	}

	public Result SetupActivityShakeInfo(String activityMid, int endShakeNum,
			int endShakeTime) {
		Result result = new Result();
		try {
			WeChatActivity weChatActivity = weChatActivityDao
					.findByActivityMid(activityMid);
			if (weChatActivity == null) {
				// 错误处理
				LOG.error("ad-setup页面请求参数错误！在系统中未发现活动MID：" + activityMid);
				return null;
			}
			weChatActivity.setEndShakeNum(endShakeNum);
			weChatActivity.setEndShakeTime(endShakeTime);
			weChatActivityDao.save(weChatActivity);
			result.setSuccess(true);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误！");
			return result;
		}
	}

	/**
	 * 设置广告页面
	 * 
	 * @param activityId
	 * @param setupType
	 * @param signReplyType
	 * @param signContent
	 * @param signTitle
	 * @param signUrl
	 * @param signImg
	 * @param upbangContent
	 * @param tipId
	 * @param tipContent
	 * @param rootPath
	 * @return
	 */
	public Result SetupActivityAd(String activityMid, String setupType,
			String signReplyType, String signContent, String signTitle,
			String signUrl, String signImg, String upbangContent, String tipId,
			String tipContent, String albumSubject, String rootPath) {
		Result result = new Result();
		try {
			WeChatActivity weChatActivity = weChatActivityDao
					.findByActivityMid(activityMid);
			if (weChatActivity == null) {
				// 错误处理
				LOG.error("ad-setup页面请求参数错误！在系统中未发现活动MID：" + activityMid);
				return null;
			}

			ActivityAutoReply activityAutoReply = weChatActivity
					.getActivityAutoReply();
			if (StringUtils.equalsIgnoreCase("signReply", setupType)) {// 处理签到欢迎回复
				if (StringUtils.isBlank(signReplyType)) {
					// 错误处理
					result.setSuccess(false);
					result.setMessage("回复类型不能为空！");
					return result;
				} else {
					if (StringUtils.equals("text", signReplyType)) {
						activityAutoReply.getSiginAutoReply().setReplyType(
								AutoReply.REPLY_TYPE.TEXT.getIndex());
					} else {
						activityAutoReply.getSiginAutoReply().setReplyType(
								AutoReply.REPLY_TYPE.IMG_TEXT.getIndex());
					}
				}

				if (StringUtils.isBlank(signContent)) {
					// 错误处理
					result.setSuccess(false);
					result.setMessage("文本消息内容不能为空！");
					return result;
				} else {
					activityAutoReply.getSiginAutoReply().getWeChatText()
							.setContent(signContent);
				}

				if (StringUtils.isBlank(signTitle)) {
					// 错误处理
					result.setSuccess(false);
					result.setMessage("图文标题不能为空！");
					return result;
				} else {
					activityAutoReply.getSiginAutoReply().getWeChatImageText()
							.setTitle(signTitle);
				}

				if (StringUtils.isNotBlank(signUrl)) {
					activityAutoReply.getSiginAutoReply().getWeChatImageText()
							.setImgUrl(null);
				} else {
					activityAutoReply.getSiginAutoReply().getWeChatImageText()
							.setImgUrl(signUrl);
				}

				String orignPreViewImg = null;
				if (StringUtils.isNoneBlank(signImg)) {
					File tmpFile = new File(signImg);
					// 检查临时文件是否存在
					if (!tmpFile.exists()) {
						LOG.error("系统未发现已上传的图片！路径：" + signImg);
						result.setMessage("系统未发现已上传的图片！");
						result.setSuccess(false);
						return result;
					}

					String preViewImgName = tmpFile.getName();
					String relativePath = CommonUtils
							.generateNewDir(CommonUtils.STORE_PICTURE_DIR);
					String strUploadLogoPath = rootPath + File.separator
							+ relativePath;
					File destFile = new File(strUploadLogoPath, preViewImgName);
					FileUtils.copyFile(tmpFile, destFile, true);
					orignPreViewImg = rootPath
							+ File.separator
							+ activityAutoReply.getSiginAutoReply()
									.getWeChatImageText().getImg();
					activityAutoReply
							.getSiginAutoReply()
							.getWeChatImageText()
							.setImg(relativePath + File.separator
									+ preViewImgName);
				}
				activityAutoReplyDao.save(activityAutoReply);
				if (StringUtils.isNotBlank(orignPreViewImg)) {
					File orignLogoFile = new File(orignPreViewImg);
					if (orignLogoFile.exists()) {
						FileUtils.forceDelete(orignLogoFile);
					}
				}
				result.setSuccess(true);
				return result;
			} else if (StringUtils.equalsIgnoreCase("upbangReply", setupType)) {// 处理上墙自动回复
				if (StringUtils.isBlank(upbangContent)) {
					// 错误处理
					result.setSuccess(false);
					result.setMessage("上墙回复消息内容不能为空！");
					return result;
				} else {
					activityAutoReply.getUpbangAutoReply().getWeChatText()
							.setContent(upbangContent);
				}
				activityAutoReplyDao.save(activityAutoReply);
				result.setSuccess(true);
				return result;
			} else if (StringUtils.equalsIgnoreCase("tip", setupType)) {// 新增/修改页面提示信息
				if (StringUtils.isBlank(tipContent)) {
					// 错误处理
					result.setSuccess(false);
					result.setMessage("提示信息内容不能为空！");
					return result;
				}

				// 如果未存在tipId, 就新增
				if (StringUtils.isBlank(tipId)) {
					WeActivityTips weActivityTips = new WeActivityTips(
							tipContent, weChatActivity);
					weActivityTipsDao.save(weActivityTips);
					result.setSuccess(true);
					return result;
				}
				// 修改提示信息
				WeActivityTips weActivityTips = weActivityTipsDao.findOne(Long
						.parseLong(tipId));
				if (weActivityTips == null) {
					// 错误处理
					result.setSuccess(false);
					result.setMessage("提示信息未发现！");
					return result;
				}
				weActivityTips.setTipContent(tipContent);
				weActivityTipsDao.save(weActivityTips);
				result.setSuccess(true);
				return result;
			} else {
				result.setSuccess(true);
				return result;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误！");
			return result;
		}
	}

	/**
	 * 设置签到方式内容
	 * 
	 * @param activityMid
	 * @param signKeyWord
	 * @param signType
	 * @return
	 */
	public Result SetupSignIn(String activityMid, String signKeyWord) {
		Result result = new Result();
		try {
			WeChatActivity weChatActivity = weChatActivityDao
					.findByActivityMid(activityMid);
			if (weChatActivity == null) {
				// 错误处理
				LOG.error("ad-setup页面请求参数错误！在系统中未发现活动MID：" + activityMid);
				return null;
			}

			weChatActivity.setSignKeyWord(signKeyWord);
			weChatActivityDao.save(weChatActivity);
			result.setSuccess(true);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("系统内部错误！");
			return result;
		}
	}

	/**
	 * 
	 * <p>
	 * 描述:根据抽奖活动ID返回可抽奖用户
	 * </p>
	 * 
	 * @param activityPrizeID
	 *            抽奖活动设置ID
	 * @return
	 * @throws
	 * @see
	 * @since %I%
	 */
	public Result listAllUserForPrize(String activityMid) {
		// 获取所有用户
		Result result = new Result();
		WeChatActivity weChatActivity = weChatActivityDao
				.findByActivityMid(activityMid);
		// 检查系统是否存在此活动
		if (weChatActivity == null) {
			LOG.error("系统未发现此活动！");
			result.setSuccess(false);
			result.setMessage("系统未发现此活动!");
			return result;
		}
		List<RActivityWeChatUser> listAllWeUserAct = rActivityWeChatUserDao
				.findSignUsersByWeChatActivityMIDAndStatus(activityMid);
		// 获取已经中奖用户
		List<WechatUserPrizeList> weUserPrizeLists = wechatUserPrizeListDao
				.findByActivityMID(activityMid);
		List<String> openIDs = new ArrayList<String>();
		for (WechatUserPrizeList wechatUserPrizeList : weUserPrizeLists) {
			openIDs.add(wechatUserPrizeList.getrActivityWeChatUser()
					.getWeChatUser().getOpenID());
		}
		result.setSuccess(true);
		List<Object> items = new ArrayList<Object>();
		// 返回真实可抽奖用户
		for (RActivityWeChatUser rActivityWeChatUser : listAllWeUserAct) {
			WeChatUser weChatUserActivity = rActivityWeChatUser.getWeChatUser();
			if (openIDs.contains(weChatUserActivity.getOpenID())) {
				continue;
			}
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", rActivityWeChatUser.getId());
			item.put("nick", weChatUserActivity.getNickName());
			item.put("avatar", weChatUserActivity.getHeadImgUrl());
			items.add(item);
		}
		result.setTotal(items.size());
		result.setItems(items);
		return result;
	}

	public Result listAllUserForPairing(String activityMid) {
		// 获取所有用户
		Result result = new Result();
		WeChatActivity weChatActivity = weChatActivityDao
				.findByActivityMid(activityMid);
		// 检查系统是否存在此活动
		if (weChatActivity == null) {
			LOG.error("系统未发现此活动！");
			result.setSuccess(false);
			result.setMessage("系统未发现此活动!");
			return result;
		}
		List<RActivityWeChatUser> listAllBoyWeUserAct = rActivityWeChatUserDao
				.findByWeChatActivityMIDAndSex(activityMid,
						WeChatUser.USER_SEX.BOY.getIndex());

		List<RActivityWeChatUser> listAllGirlWeUserAct = rActivityWeChatUserDao
				.findByWeChatActivityMIDAndSex(activityMid,
						WeChatUser.USER_SEX.GIRL.getIndex());
		// 获取已经配对成功用户
		List<WechatUserFatePair> wechatUserFatePairLists = weChatActivity
				.getWechatUserFatePairs();
		List<String> openIDs = new ArrayList<String>();
		for (WechatUserFatePair wechatUserFatePair : wechatUserFatePairLists) {
			openIDs.add(wechatUserFatePair.getManRActivityWeChatUser()
					.getWeChatUser().getOpenID());
			openIDs.add(wechatUserFatePair.getWomanRActivityWeChatUser()
					.getWeChatUser().getOpenID());
		}

		result.setSuccess(true);
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		List<Object> boys = new ArrayList<Object>();
		List<Object> girls = new ArrayList<Object>();
		// 返回真实可抽奖用户
		for (RActivityWeChatUser rActivityWeChatUser : listAllBoyWeUserAct) {
			WeChatUser boyUser = rActivityWeChatUser.getWeChatUser();
			if (openIDs.contains(boyUser.getOpenID())) {
				continue;
			}
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", rActivityWeChatUser.getId());
			item.put("nick", boyUser.getNickName());
			item.put("avatar", boyUser.getHeadImgUrl());
			boys.add(item);
		}

		for (RActivityWeChatUser rActivityWeChatUser : listAllGirlWeUserAct) {
			WeChatUser girlUser = rActivityWeChatUser.getWeChatUser();
			if (openIDs.contains(girlUser.getOpenID())) {
				continue;
			}
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", rActivityWeChatUser.getId());
			item.put("nick", girlUser.getNickName());
			item.put("avatar", girlUser.getHeadImgUrl());
			girls.add(item);
		}

		data.put("boys", boys);
		data.put("girls", girls);
		result.setData(data);
		return result;
	}

	/**
	 * 
	 * <p>
	 * 描述: 返回抽奖抽中用户
	 * </p>
	 * 
	 * @param activityID
	 *            活动ID
	 * @param activityPrizeID
	 *            抽奖活动设置ID
	 * @param userID
	 *            抽中用户ID
	 * @return
	 * @throws
	 * @see
	 * @since %I%
	 */
	public Result addUserForPrize(long activityPrizeID,
			long rActivityWeChatUserID) {
		Result result = new Result();
		// 获取奖项设置相关对象
		ActivityPrize activityPrize = activityPrizeDao.findOne(activityPrizeID);

		if (activityPrize == null) {
			LOG.error("系统未发现此奖项！");
			result.setSuccess(false);
			result.setMessage("系统未发现此奖项!");
			return result;
		}

		WeChatActivity weChatActivity = activityPrize.getWeChatActivity();
		if (weChatActivity.beforeProgress()) {
			LOG.error("活动还未开始，暂时不能进行抽奖活动！");
			result.setSuccess(false);
			result.setMessage("活动还未开始，暂时不能进行抽奖活动！");
			return result;
		}
		// 添加获奖人
		RActivityWeChatUser rActivityWeChatUser = rActivityWeChatUserDao
				.findOne(rActivityWeChatUserID);
		if (rActivityWeChatUser == null) {
			LOG.error("系统未发现此用户！");
			result.setSuccess(false);
			result.setMessage("用户不存在!");
			return result;
		}
		WechatUserPrizeList wechatUserPrizeList = wechatUserPrizeListDao
				.findByRActivityUserIDAndActivityPrizeID(activityPrize.getId(),
						rActivityWeChatUserID);
		if (wechatUserPrizeList == null) {
			wechatUserPrizeList = new WechatUserPrizeList(activityPrize,
					rActivityWeChatUser);
			wechatUserPrizeListDao.save(wechatUserPrizeList);
			try {
				//认证服务号才能发送
				if (weChatActivity.getWeChatApp().getAccountType() == WeChatApp.ACCOUNT_TYPE.SERVICER.getIndex()) {
					SendPrizeMsg sendPrizeMsg = new SendPrizeMsg(
							rActivityWeChatUser.getWeChatActivity().getWeChatApp()
									.getAccessToken(), rActivityWeChatUser
									.getWeChatUser().getOpenID(), "恭喜你中奖!\n"
									+ activityPrize.getPrizeName() + "\n"
									+ activityPrize.getDescription(),
							rActivityWeChatUser.getId());
					SchedulerManager.getInstance().submit(sendPrizeMsg);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}

		result.setSuccess(true);
		return result;
	}

	/**
	 * 添加对对碰用户
	 * 
	 * @param activityID
	 * @param rActivityWeChatBoyUserID
	 * @param rActivityWeChatGirlUserID
	 * @return
	 */
	public Result addUserForPairing(String activityMid,
			long rActivityWeChatBoyUserID, long rActivityWeChatGirlUserID) {
		Result result = new Result();
		WeChatActivity weChatActivity = weChatActivityDao
				.findByActivityMid(activityMid);
		// 检查系统是否存在此活动
		if (weChatActivity == null) {
			LOG.error("系统未发现此活动！");
			result.setSuccess(false);
			result.setMessage("系统未发现此活动!");
			return result;
		}
		// 检查活动是否正在进行中
		if (weChatActivity.beforeProgress()) {
			LOG.error("活动还未开始，暂时不能进行缘分对对碰活动！");
			result.setSuccess(false);
			result.setMessage("活动还未开始，暂时不能进行缘分对对碰活动！");
			return result;
		}
		// 添加碰碰对男生
		RActivityWeChatUser rActivityWeChatBoyUser = rActivityWeChatUserDao
				.findOne(rActivityWeChatBoyUserID);
		if (rActivityWeChatBoyUser == null) {
			LOG.error("系统未发现此用户！");
			result.setSuccess(false);
			result.setMessage("用户不存在!");
			return result;
		}

		// 添加碰碰对男生
		RActivityWeChatUser rActivityWeChatGirlUser = rActivityWeChatUserDao
				.findOne(rActivityWeChatGirlUserID);
		if (rActivityWeChatGirlUser == null) {
			LOG.error("系统未发现此用户！");
			result.setSuccess(false);
			result.setMessage("用户不存在!");
			return result;
		}

		WechatUserFatePair wechatUserFatePair = new WechatUserFatePair(
				weChatActivity, rActivityWeChatBoyUser, rActivityWeChatGirlUser);
		wechatUserFatePairDao.save(wechatUserFatePair);
		result.setSuccess(true);
		return result;
	}

	/**
	 * 初始化shakenum数
	 * 
	 * @param activityID
	 * @return
	 */
	public Result initUserInfoForShake(String activityMid) {
		Result result = new Result();
		WeChatActivity weChatActivity = weChatActivityDao
				.findByActivityMid(activityMid);
		// 检查系统是否存在此活动
		if (weChatActivity == null) {
			LOG.error("系统未发现此活动！");
			result.setSuccess(false);
			result.setMessage("系统未发现此活动!");
			return result;
		}
		// 检查活动是否正在进行中
		if (weChatActivity.beforeProgress()) {
			LOG.error("活动还未开始，暂时不能进行摇一摇活动！");
			result.setSuccess(false);
			result.setMessage("活动还未开始，暂时不能进行摇一摇活动！");
			return result;
		}

		// if (weChatActivity.isfShakeStart()) {
		// LOG.error("摇一摇活动已经开始，请勿重复提交！");
		// result.setSuccess(false);
		// result.setMessage("摇一摇活动已经开始，请勿重复提交！");
		// return result;
		// }
		// //////////邪恶的分割线/////////////
		// rActivityWeChatUserDao.updateShakeNumByActivityMID(activityMid);
		// weChatActivity.setfShakeStart(true);
		// weChatActivity.setShakeTime(weChatActivity.getEndShakeTime() * 60);
		// weChatActivityDao.save(weChatActivity);
		// //////////邪恶的分割线/////////////
		ShakeManager.getInstance().startShake(weChatActivity.getActivityMid(), weChatActivity.getEndShakeNum());

		try {
			ActivityShake activityShake = new ActivityShake(weChatActivity);
			SchedulerManager.getInstance().submit(activityShake);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		result.setSuccess(true);
		return result;
	}

	/**
	 * 返回活动中奖用户列表
	 * 
	 * @param activityID
	 * @return
	 */
	public Result getWinnersListForPrize(String activityMid, String serverRoot) {
		Result result = new Result();
		WeChatActivity weChatActivity = weChatActivityDao
				.findByActivityMid(activityMid);
		// 检查系统是否存在此活动
		if (weChatActivity == null) {
			LOG.error("系统未发现此活动！");
			result.setSuccess(false);
			result.setMessage("系统未发现此活动!");
			return result;
		}

		if (weChatActivity.isfDraw() == false) {
			result.setSuccess(false);
			result.setMessage("抽奖功能未开启，请先打开抽奖设置按钮!");
			return result;
		}

		List<Object> prizeItems = new ArrayList<Object>();
		// 获取奖项类型
		List<ActivityPrize> activityPrizes = weChatActivity.getActivityPrizes();
		for (ActivityPrize activityPrize : activityPrizes) {
			Map<String, Object> prizeItem = new LinkedHashMap<String, Object>();
			prizeItem.put("levelId", activityPrize.getId());
			prizeItem.put("level", activityPrize.getPrizeName());
			prizeItem.put("prize", activityPrize.getDescription());
			prizeItem.put("prizePic", serverRoot + activityPrize.getImg());
			prizeItem.put("prizeNum", activityPrize.getWinNum());
			List<Object> userItems = new ArrayList<Object>();

			// 获取该奖项下已经中奖用户
			List<WechatUserPrizeList> weUserPrizeLists = activityPrize
					.getWechatUserPrizeLists();
			int count = 0;
			for (WechatUserPrizeList wechatUserPrizeList : weUserPrizeLists) {
				count++;
				RActivityWeChatUser rActivityWeChatUser = wechatUserPrizeList
						.getrActivityWeChatUser();
				WeChatUser weChatUser = rActivityWeChatUser.getWeChatUser();
				Map<String, Object> userItem = new LinkedHashMap<String, Object>();
				userItem.put("id", rActivityWeChatUser.getId());
				userItem.put("index", count);
				userItem.put("nick", weChatUser.getNickName());
				userItem.put("avatar", weChatUser.getHeadImgUrl());
				userItems.add(userItem);
			}
			Collections.reverse(userItems);
			prizeItem.put("items", userItems);
			prizeItems.add(prizeItem);
		}

		result.setSuccess(true);
		result.setItems(prizeItems);

		return result;
	}

	/**
	 * 返回配对成功列表
	 * 
	 * @param activityID
	 * @return
	 */
	public Result getLuckyPairingList(String activityMid) {
		Result result = new Result();
		WeChatActivity weChatActivity = weChatActivityDao
				.findByActivityMid(activityMid);
		// 检查系统是否存在此活动
		if (weChatActivity == null) {
			LOG.error("系统未发现此活动！");
			result.setSuccess(false);
			result.setMessage("系统未发现此活动!");
			return result;
		}

		List<Object> pairingItems = new ArrayList<Object>();
		// 获取奖项类型
		List<WechatUserFatePair> wechatUserFatePairs = weChatActivity
				.getWechatUserFatePairs();
		int count = 0;
		for (WechatUserFatePair wechatUserFatePair : wechatUserFatePairs) {
			count++;
			Map<String, Object> pairingItem = new LinkedHashMap<String, Object>();
			WeChatUser boyUser = wechatUserFatePair.getManRActivityWeChatUser()
					.getWeChatUser();
			WeChatUser girlUser = wechatUserFatePair
					.getWomanRActivityWeChatUser().getWeChatUser();
			pairingItem.put("index", count);
			pairingItem.put("id", wechatUserFatePair.getId());
			pairingItem.put("nickBoy", boyUser.getNickName());
			pairingItem.put("avatarBoy", boyUser.getHeadImgUrl());
			pairingItem.put("nickGirl", girlUser.getNickName());
			pairingItem.put("avatarGirl", girlUser.getHeadImgUrl());
			pairingItems.add(pairingItem);
		}
		Collections.reverse(pairingItems);
		result.setSuccess(true);
		result.setItems(pairingItems);
		return result;
	}

	/**
	 * 删除已中奖活动用户
	 * 
	 * @param activityPrizeID
	 *            奖项ID
	 * @param userID
	 * @return
	 */
	public Result clearUserForPrize(long activityPrizeID,
			long rActivityWeChatUserId) {
		Result result = new Result();
		// 这里将不会重新选中此用户
		if (activityPrizeID != -1 && rActivityWeChatUserId != -1) {
			wechatUserPrizeListDao.deleteByRActivityUserIDAndActivityPrizeID(
					activityPrizeID, rActivityWeChatUserId);
			result.setSuccess(true);
			return result;
		}
		result.setSuccess(true);
		return result;
	}

	/**
	 * 删除活动配对用户
	 * 
	 * @param userPairingID
	 * @return
	 */
	public Result clearUserForPiring(long userPairingID) {
		Result result = new Result();
		// 这里将不会重新选中此用户
		if (userPairingID != -1) {
			wechatUserFatePairDao.deleteByUserPairID(userPairingID);
			result.setSuccess(true);
			return result;
		}
		result.setSuccess(true);
		return result;
	}

	/**
	 * 删除奖项下面的中奖用户
	 * 
	 * @param activityPrizeID
	 * @return
	 */
	public Result clearUserForPrizeByActivityPrizeID(long activityPrizeID) {
		Result result = new Result();
		// 删除某个奖项下所有用户
		if (activityPrizeID != -1) {
			wechatUserPrizeListDao.deleteByActivityPrizeID(activityPrizeID);
			result.setSuccess(true);
			return result;
		}
		result.setSuccess(true);
		return result;
	}

	/**
	 * 删除活动下面的所有配对用户
	 * 
	 * @param activityPrizeID
	 * @return
	 */
	public Result clearUserForPairingByActivityID(String activityMid) {
		Result result = new Result();
		// 删除某个奖项下所有用户
		if (StringUtils.isNotBlank(activityMid)) {
			wechatUserFatePairDao.deleteByActivityMID(activityMid);
			result.setSuccess(true);
			return result;
		}
		result.setSuccess(true);
		return result;
	}

	/**
	 * 删除活动下面的中奖用户
	 * 
	 * @param activityID
	 * @return
	 */
	public Result clearUserForPrizeByActivityID(String activityMid) {
		Result result = new Result();
		// 根据活动ID，删除中奖用户
		if (StringUtils.isNotBlank(activityMid)) {
			wechatUserPrizeListDao.deleteByActivityMID(activityMid);
			result.setSuccess(true);
			return result;
		}
		result.setSuccess(true);
		return result;
	}
}
