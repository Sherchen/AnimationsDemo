package com.sherchen.wanjiedemo.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sherchen.wanjiedemo.R;

/**
 * <pre>
 *     author : Sherchen
 *     e-mail : ncuboy_045wsq@qq.com
 *     time   : 2017/5/26
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class DetailHelpFragment extends Fragment{
    public static final String TAG = "DetailHelpFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_help, container, false);
    }
}
