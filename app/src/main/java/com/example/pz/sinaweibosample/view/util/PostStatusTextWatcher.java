package com.example.pz.sinaweibosample.view.util;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;

/**
 * Created by pz on 2016/9/23.
 */

public class PostStatusTextWatcher implements TextWatcher {

//    SpannableStringBuilder spannableStringBuilder;

//    public PostStatusTextWatcher(SpannableStringBuilder spannableStringBuilder) {
////        this.spannableStringBuilder = spannableStringBuilder;
//    }


    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        spannableStringBuilder.clear();
//        spannableStringBuilder.append(s);
    }
}
