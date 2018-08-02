package com.notification.notyos10.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.notification.notyos10.R;
import com.notification.notyos10.constants.Const;
import com.notification.notyos10.customviews.slide.NotifyLayout;
import com.notification.notyos10.objects.NotifyEntity;
import com.notification.notyos10.utils.DefaultAppUtils;

import java.util.ArrayList;


public class AdapterNotyPartialItem extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<NotifyEntity> mBaseNotyModelArrayList;

    private NotifyEntity notyMissedCallInfor;
    private NotifyEntity notyMissedSmsInfor;

    public AdapterNotyPartialItem(Context context, ArrayList<NotifyEntity> baseNotyModelArrayList) {
        this.mContext = context;
        mBaseNotyModelArrayList = baseNotyModelArrayList;
        Log.e("test_arr_noty", "size nhận được trước khi chỉnh sửa : " + baseNotyModelArrayList.size());
        try {
            for (int i = 0; i < mBaseNotyModelArrayList.size();i++){
                if (mBaseNotyModelArrayList.get(i).getPackageName().equals(DefaultAppUtils.getPackageDefaultSmsApp(mContext))){
                    notyMissedSmsInfor = mBaseNotyModelArrayList.get(i);
                    break;
                }
            }
            for (int i = 0; i < mBaseNotyModelArrayList.size();i++){
                if (mBaseNotyModelArrayList.get(i).getPackageName().equals(DefaultAppUtils.isDefaultPhoneApp(mContext))){
                    notyMissedCallInfor = mBaseNotyModelArrayList.get(i);
                    break;
                }
            }

            if (Const.arrNotyMissedCall.size() != 0) {
                for (int i = 0; i < Const.arrNotyMissedCall.size(); i++) {
                    Const.arrNotyMissedCall.get(i).setPackageName("Miss_Call_Pkm");
                    Const.arrNotyMissedCall.get(i).setMissCalled(true);
                    Const.arrNotyMissedCall.get(i).setPostionMissedCall(i);
                    if (notyMissedCallInfor != null){
                        Const.arrNotyMissedCall.get(i).setDrawableIcon(notyMissedCallInfor.getDrawableIcon());
                        break;
                    }
                }
                mBaseNotyModelArrayList.addAll(Const.arrNotyMissedCall);
            }

            if (Const.arrNotySms.size() != 0) {
                for (int i = 0; i < Const.arrNotySms.size(); i++) {
                    Const.arrNotySms.get(i).setPackageName("SMS");
                    Const.arrNotySms.get(i).setPositionSms(i);
                    Const.arrNotySms.get(i).setSms(true);
                    if (notyMissedSmsInfor != null){
                        Const.arrNotySms.get(i).setDrawableIcon(notyMissedSmsInfor.getDrawableIcon());
                    }

                }
                mBaseNotyModelArrayList.addAll(Const.arrNotySms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (mBaseNotyModelArrayList.get(position).getPackageName().equals(DefaultAppUtils.isDefaultPhoneApp(mContext))
                    && mBaseNotyModelArrayList.get(position).isMissCalled())
                return NotifyLayout.MENU_CALL;
            else if (mBaseNotyModelArrayList.get(position).getPackageName().equals(DefaultAppUtils.getPackageDefaultSmsApp(mContext))
                    && mBaseNotyModelArrayList.get(position).isSms())
                return NotifyLayout.MENU_SMS;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            if (mBaseNotyModelArrayList.get(position).getPackageName().equals("Miss_Call_Pkm"))
                return NotifyLayout.MENU_CALL;
            if (mBaseNotyModelArrayList.get(position).getPackageName().equals("SMS"))
                return NotifyLayout.MENU_SMS;
        } catch (Exception e) {
            e.printStackTrace();
            return NotifyLayout.MENU_BASIC;
        }
        return NotifyLayout.MENU_BASIC;
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
        ItemNotyViewHolder itemNotyViewHolder;
        if (convertView == null) {
            itemNotyViewHolder = new ItemNotyViewHolder();
            convertView = mInflater.inflate(R.layout.item_ltv_noty, parent, false);
            itemNotyViewHolder.imvAppIcon = (ImageView) convertView.findViewById(R.id.imv_item_ltv_noty_icon_app);
            itemNotyViewHolder.txvAppName = (TextView) convertView.findViewById(R.id.txv_item_ltv_noty_name_app);
            itemNotyViewHolder.txvTitleNoty = (TextView) convertView.findViewById(R.id.txv_item_ltv_noty_title);
            itemNotyViewHolder.txvContentNoty = (TextView) convertView.findViewById(R.id.txv_item_ltv_noty_content_noty);

            convertView.setTag(itemNotyViewHolder);
        } else {
            itemNotyViewHolder = (ItemNotyViewHolder) convertView.getTag();
        }
        NotifyEntity item = getItem(position);
        itemNotyViewHolder.txvAppName.setText(item.getAppName());
        itemNotyViewHolder.imvAppIcon.setImageDrawable(item.getDrawableIcon());
        itemNotyViewHolder.txvTitleNoty.setText(item.getTitle());
        itemNotyViewHolder.txvContentNoty.setText(item.getContent());
        return convertView;
    }

    public class ItemNotyViewHolder {
        ImageView imvAppIcon;
        TextView txvAppName;
        TextView txvTitleNoty;
        TextView txvContentNoty;
    }

}
