package com.jay.six.ui.adapter;

import android.content.Context;

import com.jay.six.R;
import com.jay.six.bean.ResultJoke.ResultBean.Joke;
import com.jay.six.ui.widget.recyclerview.ViewHolder;
import com.jay.six.ui.widget.recyclerview.base.SingleBaseAdapter;

import java.util.List;

/**
 * Created by jayli on 2017/5/8 0008.
 */

public class JokeAdapter extends SingleBaseAdapter<Joke> {

    public JokeAdapter(Context context, List<Joke> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected void convert(ViewHolder holder, Joke data) {
        holder.setText(R.id.tv_joke,data.getContent());
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_joke;
    }
}
