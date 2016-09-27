package com.example.pz.sinaweibosample.view.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.view.adapter.EmojiRecyclerAdapter;
import com.example.pz.sinaweibosample.view.iview.IEmojiView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by pz on 2016/9/26.
 */

public class EmojiFragment extends Fragment {

    @BindView(R.id.recycler_emoji)
    RecyclerView emojiRecyclerView;

    Unbinder unbinder;
    View view;
    EmojiRecyclerAdapter adapter;
    List<String> datas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_emoji, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public static EmojiFragment instanceOf(List<String> data) {
        EmojiFragment emojiFragment = new EmojiFragment();
        emojiFragment.datas = data;
        return emojiFragment;
    }

    void initView() {
        adapter = new EmojiRecyclerAdapter(datas);
        adapter.setOnEmojiClickListener(new EmojiRecyclerAdapter.OnEmojiClickListener() {
            @Override
            public void onEmojiClick(View view) {
                String emojiName = (String) view.getTag();
//                Drawable drawable = MyApplication.getContext().getResources().getDrawable((int)((ImageView)view).getTag());
                //处理emoji输入，只有实现了IEmojiView的Activity可以用
                ((IEmojiView)getActivity()).handleEmojiInput(emojiName);
            }
        });
        emojiRecyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManagernew = new GridLayoutManager(MyApplication.getContext(), Constant.EMOJI_COUNT_COLUMN);
        gridLayoutManagernew.setSmoothScrollbarEnabled(true);

        emojiRecyclerView.setLayoutManager(gridLayoutManagernew);
    }

    @Override
    public void onDestroyView() {
        if(unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }
}
