package com.notification.inotythemehhhhh.myadapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.notification.inotythemehhhhh.R;
import com.notification.inotythemehhhhh.objects.NotifyEntity;
import com.notification.inotythemehhhhh.myservices.NotificationMonitorService;
import com.notification.inotythemehhhhh.mycenterutils.DateUtil;
import com.notification.inotythemehhhhh.customviews.widgets.TextViewOSBold;
import com.notification.inotythemehhhhh.customviews.widgets.TextViewOSLight;
import com.notification.inotythemehhhhh.customviews.widgets.TextViewOSNormal;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class AdapterNotyPartialItem extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<NotifyEntity> mBaseNotyModelArrayList;
    private int[] mSectionIndices;
    private String[] mSectionLetters;

    public AdapterNotyPartialItem(Context context, ArrayList<NotifyEntity> baseNotyModelArrayList) {
        this.mContext = context;
        mBaseNotyModelArrayList = baseNotyModelArrayList;
        mInflater = LayoutInflater.from(context);
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }

    @Override
    public int getCount() {
        return mBaseNotyModelArrayList.size();
    }

    @Override
    public NotifyEntity getItem(int position) {
        return mBaseNotyModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int currentPos = position;
        final ChildViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.item_ltv_child_noty, parent, false);
            vh = ChildViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ChildViewHolder) convertView.getTag();
        }

        NotifyEntity item = getItem(position);
        vh.txvItemLtvChildNotyAppName.setText(item.getTitle());
        vh.txvItemLtvChildNotyContent.setText(item.getContent());
        String strPosTime = DateUtil.toTimeAgo(item.getPostTime());
        vh.txvItemLtvChildNotyTimeAgo.setText(strPosTime);
//        if (item.isAction()) {
//            vh.imvItemLtvChildNotyClear.setVisibility(View.VISIBLE);
        vh.imvItemLtvChildNotyClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NotificationMonitorService.ACTION_NLS_CONTROL);
                i.putExtra("command", "cancel_position");
                i.putExtra("id", mBaseNotyModelArrayList.get(currentPos).getId());
                i.putExtra("tag", mBaseNotyModelArrayList.get(currentPos).getTag());
                i.putExtra("packagename", mBaseNotyModelArrayList.get(currentPos).getPackageName());
                i.putExtra("pos", mBaseNotyModelArrayList.get(currentPos).getPosition());
                if (mBaseNotyModelArrayList.get(currentPos).getKey() != null) {
                    i.putExtra("key", mBaseNotyModelArrayList.get(currentPos).getKey());
                }
                mContext.sendBroadcast(i);
                mBaseNotyModelArrayList.remove(currentPos);
                notifyDataSetChanged();
            }
        });
//        } else {
//            vh.imvItemLtvChildNotyClear.setVisibility(View.INVISIBLE);
//        }

        // TODOBind your data to the customviews here

        return vh.rootView;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (sectionIndex >= mSectionIndices.length) {
            sectionIndex = mSectionIndices.length - 1;
        } else if (sectionIndex < 0) {
            sectionIndex = 0;
        }
        return mSectionIndices[sectionIndex];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        final HeaderViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.item_ltv_header_noty, parent, false);
            vh = HeaderViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (HeaderViewHolder) convertView.getTag();
        }
        NotifyEntity item = getItem(position);
        vh.txvItemLtvHeaderNoty.setText(item.getAppName());
//        vh.imvItemLtvHeaderNoty.setImageBitmap(item.getBigIcon());
        vh.imvItemLtvHeaderNoty.setImageDrawable(item.getDrawableIcon());

        // TODOBind your data to the customviews here

        return vh.rootView;
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        if (mBaseNotyModelArrayList.size() > 0) {
            String lastFirstChar = mBaseNotyModelArrayList.get(0).getAppName();
            sectionIndices.add(0);
            for (int i = 1; i < mBaseNotyModelArrayList.size(); i++) {
                if (!mBaseNotyModelArrayList.get(i).getAppName().equals(lastFirstChar)) {
                    lastFirstChar = mBaseNotyModelArrayList.get(i).getAppName();
                    sectionIndices.add(i);
                }
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private String[] getSectionLetters() {
        String[] letters = new String[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = mBaseNotyModelArrayList.get(i).getAppName();
        }
        return letters;
    }

    @Override
    public long getHeaderId(int position) {
        return mBaseNotyModelArrayList.get(position).getHeaderId();
    }

    private static class HeaderViewHolder {
        public final RelativeLayout rootView;
        public final ImageView imvItemLtvHeaderNoty;
        public final TextView txvItemLtvHeaderNoty;

        private HeaderViewHolder(RelativeLayout rootView, ImageView imvItemLtvHeaderNoty, TextView txvItemLtvHeaderNoty) {
            this.rootView = rootView;
            this.imvItemLtvHeaderNoty = imvItemLtvHeaderNoty;
            this.txvItemLtvHeaderNoty = txvItemLtvHeaderNoty;
        }

        public static HeaderViewHolder create(RelativeLayout rootView) {
            ImageView imvItemLtvHeaderNoty = (ImageView) rootView.findViewById(R.id.imv_item_ltv_header_noty);
            TextView txvItemLtvHeaderNoty = (TextView) rootView.findViewById(R.id.txv_item_ltv_header_noty);
            return new HeaderViewHolder(rootView, imvItemLtvHeaderNoty, txvItemLtvHeaderNoty);
        }
    }

    private static class ChildViewHolder {
        public final RelativeLayout rootView;
        public final TextViewOSBold txvItemLtvChildNotyAppName;
        public final TextViewOSNormal txvItemLtvChildNotyContent;
        public final TextViewOSLight txvItemLtvChildNotyTimeAgo;
        public final ImageView imvItemLtvChildNotyClear;

        private ChildViewHolder(RelativeLayout rootView, TextViewOSBold txvItemLtvChildNotyAppName, TextViewOSNormal txvItemLtvChildNotyContent, TextViewOSLight txvItemLtvChildNotyTimeAgo, ImageView imvItemLtvChildNotyClear) {
            this.rootView = rootView;
            this.txvItemLtvChildNotyAppName = txvItemLtvChildNotyAppName;
            this.txvItemLtvChildNotyContent = txvItemLtvChildNotyContent;
            this.txvItemLtvChildNotyTimeAgo = txvItemLtvChildNotyTimeAgo;
            this.imvItemLtvChildNotyClear = imvItemLtvChildNotyClear;
        }

        public static ChildViewHolder create(RelativeLayout rootView) {
            TextViewOSBold txvItemLtvChildNotyAppName = (TextViewOSBold) rootView.findViewById(R.id.txv_item_ltv_child_noty__app_name);
            TextViewOSNormal txvItemLtvChildNotyContent = (TextViewOSNormal) rootView.findViewById(R.id.txv_item_ltv_child_noty__content);
            TextViewOSLight txvItemLtvChildNotyTimeAgo = (TextViewOSLight) rootView.findViewById(R.id.txv_item_ltv_child_noty__time_ago);
            ImageView imvItemLtvChildNotyClear = (ImageView) rootView.findViewById(R.id.imv_item_ltv_child_noty__clear);
            return new ChildViewHolder(rootView, txvItemLtvChildNotyAppName, txvItemLtvChildNotyContent, txvItemLtvChildNotyTimeAgo, imvItemLtvChildNotyClear);
        }
    }
}
