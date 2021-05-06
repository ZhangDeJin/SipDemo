package com.zdj.zdjsystemsiplibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipProfile;

/**
 * <pre>
 *     author : dejinzhang
 *     time : 2021/05/06
 *     desc : 广播接收器，接受电话
 * </pre>
 */
public class IncomingCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SipAudioCall incomingCall = null;
        SipAudioCall.Listener listener = new SipAudioCall.Listener(){
            @Override
            public void onRinging(SipAudioCall call, SipProfile caller) {
                try {
                    call.answerCall(30);
                } catch (SipException e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            incomingCall = SipHelp.getInstance().getSipManager(context).takeAudioCall(intent, listener);
            incomingCall.answerCall(30);
            incomingCall.startAudio();
            incomingCall.setSpeakerMode(true);
            if (incomingCall.isMuted()) {
                incomingCall.toggleMute();
            }
        } catch (SipException e) {
            e.printStackTrace();
            if (incomingCall != null) {
                incomingCall.close();
            }
        }
    }
}
