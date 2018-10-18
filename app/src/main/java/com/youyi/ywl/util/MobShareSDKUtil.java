package com.youyi.ywl.util;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.youyi.ywl.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2018/10/15.
 * 分享操作工具类
 */

public class MobShareSDKUtil {

    public static void showShare(final Context context, String platType, HashMap shareMap) {
        Platform.ShareParams sp = new Platform.ShareParams();
        if (shareMap != null) {
            if (Wechat.NAME.equals(platType) || WechatMoments.NAME.equals(platType)) {
                //微信或者微信朋友圈分享
                sp.setTitle(shareMap.get("title") + "");
                sp.setUrl(shareMap.get("share_url") + "");
                sp.setText(shareMap.get("desc") + "");
                String imgPath = shareMap.get("img") + "";
                if (imgPath != null && imgPath.length() > 0) {
                    sp.setImageUrl(imgPath);
                } else {
                    sp.setImageData(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_square_fox));
                }
            } else if (QQ.NAME.equals(platType)) {
                //QQ分享
                sp.setTitle(shareMap.get("title") + "");
                sp.setTitleUrl(shareMap.get("share_url") + "");
                sp.setText(shareMap.get("desc") + "");
                String imgPath = shareMap.get("img") + "";
                if (imgPath != null && imgPath.length() > 0) {
                    sp.setImageUrl(imgPath);
                } else {
                    sp.setImageData(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_square_fox));
                }
            } else if (QZone.NAME.equals(platType)) {
                //QQ空间分享
                sp.setTitle(shareMap.get("title") + "");
                sp.setTitleUrl(shareMap.get("share_url") + "");
                sp.setText(shareMap.get("desc") + "");
                String imgPath = shareMap.get("img") + "";
                if (imgPath != null && imgPath.length() > 0) {
                    sp.setImageUrl(imgPath);
                } else {
                    sp.setImageData(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_square_fox));
                }
            } else if (SinaWeibo.NAME.equals(platType)) {
                //新浪微博分享
                sp.setTitle(shareMap.get("title") + "");
                String imgPath = shareMap.get("img") + "";
                if (imgPath != null && imgPath.length() > 0) {
                    sp.setImageUrl(imgPath);
                } else {
                    sp.setImageData(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_square_fox));
                }
            }


            sp.setShareType(Platform.SHARE_WEBPAGE);
            Platform weChatPlat = ShareSDK.getPlatform(platType);
            weChatPlat.setPlatformActionListener((PlatformActionListener) context
//                    new PlatformActionListener() {
//                @Override
//                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                    ToastUtil.show(context, "分享成功:  " + i, 0);
//                }
//
//                @Override
//                public void onError(Platform platform, int i, Throwable throwable) {
//                    ToastUtil.show(context, "分享错误:  " + i, 0);
//                    Log.i("throwable", "     throwable.toString:    " + throwable.toString());
//                    Log.i("throwable", "     throwable.getMessage:    " + throwable.getMessage());
//                    Log.i("throwable", "     throwable.getLocalizedMessage:    " + throwable.getLocalizedMessage());
//                }
//
//                @Override
//                public void onCancel(Platform platform, int i) {
//                    ToastUtil.show(context, "分享取消:  " + i, 0);
//                }
//            }
            );

            weChatPlat.share(sp);
        }
    }
}
