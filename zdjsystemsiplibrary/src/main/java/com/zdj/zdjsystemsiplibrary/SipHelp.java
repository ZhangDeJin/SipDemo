package com.zdj.zdjsystemsiplibrary;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.util.Log;

import java.text.ParseException;

/**
 * <pre>
 *     author : dejinzhang
 *     time : 2021/04/29
 *     desc : SIP辅助类
 * </pre>
 */
public class SipHelp {
    private static SipHelp instance;
    private SipHelp(){}
    public static SipHelp getInstance() {
        /**
         * 加上同步，更加严谨。
         * 如果在其他地方都需要调用，在线程中都需要调用，则就需要同步。
         */
        synchronized (SipHelp.class) {
            if (instance == null) {
                instance = new SipHelp();
            }
        }
        return instance;
    }

    /**
     * 获取SipManager实例
     * 注：SipManager提供了SIP任务的相关API，例如初始化SIP连接，提供对相关SIP服务的访问等。
     * @param context
     * @return
     */
    public SipManager getSipManager(Context context) {
        SipManager sipManager = SipManager.newInstance(context);
        if (sipManager == null) {
            Log.i("SipDemo", "sipManager为null");
        }
        return sipManager;
    }

    /**
     * 获取SipProfile
     * 注：SipProfile定义了一个SIP配置文件，包括SIP账户、域和服务器信息等。
     * @param serverDomain
     * @param username
     * @param password
     * @return
     */
    public SipProfile getSipProfile(String serverDomain, String username, String password) {
        SipProfile sipProfile = null;
        try {
            SipProfile.Builder builder = new SipProfile.Builder(username, serverDomain);
            builder.setPassword(password);
            sipProfile = builder.build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sipProfile;
    }

    /**
     * 初始化sip
     * @param sipManager
     * @param sipProfile
     * @param context
     */
    public void initSip(SipManager sipManager, SipProfile sipProfile, Context context) {
        /**
         * 添加一个本地的过滤器，用于接受电话
         * 构造一个PendingIntent现象，这样当sip Service收到一个通话请求时，
         * SipService会调用PendingIntent的send方法发送相应广播消息给调用者，也就是当前的SipProfile对象。
         */
        Intent intent = new Intent();
        intent.setAction("android.SipDemo.INCOMING_CALL");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, Intent.FILL_IN_DATA);
        try {
            //注册一个账户到sip服务器
            sipManager.open(sipProfile, pendingIntent, null);
        } catch (SipException e) {
            e.printStackTrace();
        }
    }

    public void call(String address, String serverDomain, SipManager sipManager, SipProfile sipProfile) {
        SipAudioCall.Listener listener = new SipAudioCall.Listener() {
            @Override
            public void onReadyToCall(SipAudioCall call) {
                Log.i("SipDemo", "开始打电话");
                super.onReadyToCall(call);
            }

            @Override
            public void onRinging(SipAudioCall call, SipProfile caller) {
                Log.i("SipDemo", "呼叫中...");
                super.onRinging(call, caller);
            }

            @Override
            public void onCallEstablished(SipAudioCall call) {
                Log.i("SipDemo", "通话已建立，正在通话中");
                call.startAudio();
                call.setSpeakerMode(true);
                call.toggleMute();
                super.onCallEstablished(call);
            }

            @Override
            public void onCallEnded(SipAudioCall call) {
                Log.i("SipDemo", "通话结束");
                super.onCallEnded(call);
            }
        };

        try {
            SipAudioCall call = sipManager.makeAudioCall(sipProfile.getUriString(), address + "@" + serverDomain, listener, 30);
            call.toggleMute();
        } catch (SipException e) {
            e.printStackTrace();
        }
    }
}
