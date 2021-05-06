package com.zdj.sipdemo.ui.home;

import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.zdj.sipdemo.R;
import com.zdj.zdjsystemsiplibrary.SipHelp;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));

        textView.setOnClickListener(v -> {
            String serverDomain = "";  //sip服务器域名ip
            String username = "";  //sip账号
            String password = "";  //sip密码
            SipManager sipManager = SipHelp.getInstance().getSipManager(getActivity());
            SipProfile sipProfile = SipHelp.getInstance().getSipProfile(serverDomain, username, password);
            if (sipManager != null && sipProfile != null) {
                SipHelp.getInstance().initSip(sipManager, sipProfile, getActivity());
                SipHelp.getInstance().call("", serverDomain, sipManager, sipProfile);
            } else {
                Log.i("SipDemo", "SipManager为null或者SipProfile为null");
            }
        });
        return root;
    }
}