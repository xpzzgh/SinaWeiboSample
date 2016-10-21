package com.example.pz.sinaweibosample.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.view.iview.IErrorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by pz on 2016/10/20.
 */

public class ErrorFragment extends Fragment implements IErrorView{

    public static final String ERROR_FLAG = "error_flag";

    @BindView(R.id.text_error_fragment)
    TextView errorText;

    private String errorString;
    private Unbinder unbinder;

    public static ErrorFragment instanceOf(String errorString) {
        ErrorFragment errorFragment = new ErrorFragment();
        if(errorString == null || errorString.isEmpty()) {
            throw new RuntimeException("错误页面的数据为空！");
        }
        errorFragment.setErrorInfo(errorString);
        return errorFragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_error, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillErrorInfo();
    }

    private void setErrorInfo(String errorString) {
        this.errorString = errorString;
    }

    @Override
    public void fillErrorInfo() {
        errorText.setText(errorString);
    }

    @Override
    public void onDestroyView() {
        if(unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }
}
