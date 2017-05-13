package com.jay.six.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.jay.six.R;
import com.jay.six.bean.Picture;
import com.jay.six.ui.widget.recyclerview.ViewHolder;
import com.jay.six.ui.widget.recyclerview.base.SingleBaseAdapter;
import com.jay.six.utils.picture.ImageLoader;
import com.jay.six.utils.picture.LruCacheUtils;

import java.util.List;

/**
 * Created by jayli on 2017/5/9 0009.
 */

public class PictureAdapter extends SingleBaseAdapter<Picture> {

    public PictureAdapter(Context context, List<Picture> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected void convert(ViewHolder holder, Picture data) {
        holder.setText(R.id.tv_title,data.getTitle());
        String imgUrl = data.getImageUrl();
        //判断缓存中是否已经缓存过该图片，有则直接拿Bitmap，没有则直接调用Glide加载并缓存Bitmap
        Bitmap bitmap = LruCacheUtils.getInstance().getBitmapFromMemCache(imgUrl);
        if (bitmap != null) {
            holder.setImageBitmap(R.id.img_pic,bitmap);
        } else {
            holder.setImageUrl(R.id.img_pic, new ViewHolder.ImageLoder(imgUrl) {
                @Override
                public void loadImage(ImageView imageView, String path) {
                    //封装的图片缓存加载类
                    ImageLoader.getInstance().displayImageTarget(imageView,path);
                }
            });
        }
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_pic;
    }
}
