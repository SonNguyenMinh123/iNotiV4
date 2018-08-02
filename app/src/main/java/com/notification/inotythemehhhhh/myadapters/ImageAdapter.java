package com.notification.inotythemehhhhh.myadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.notification.inotythemehhhhh.R;
import com.notification.inotythemehhhhh.objects.FileInfor;

import java.util.ArrayList;

/**
 * Created by uythi on 02/11/2016.
 */
public class ImageAdapter extends BaseAdapter{
	Context context;
	LayoutInflater mInflater;
	ArrayList<FileInfor> arImages;

	public ImageAdapter(Context context, ArrayList<FileInfor> arImages) {
		this.context = context;
		this.arImages = arImages;
		mInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return arImages.size();
	}

	@Override
	public Object getItem(int position) {
		return arImages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHoder mViewHoder;
		if (view ==null) {
			mViewHoder = new ViewHoder();
			view = mInflater.inflate(R.layout.item_img,parent,false);
			mViewHoder.imageView = (ImageView) view.findViewById(R.id.item_img);
			view.setTag(mViewHoder);
		} else {
			mViewHoder = (ViewHoder) view.getTag();
		}
		Glide.with(context).load(arImages.get(position).getPath()).into(mViewHoder.imageView);
		return view;
	}

	class ViewHoder{
		ImageView imageView;
	}
}
