package com.notification.notyos10.customviews.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.notification.notyos10.R;
import com.notification.notyos10.constants.Const;
import com.notification.notyos10.activitys.WeatherActivity;
import com.notification.notyos10.services.NotifyService;
import com.notification.notyos10.threadsapp.LoadLocationTask;
import com.notification.notyos10.utils.MyToast;
import com.notification.notyos10.utils.NetworkUtil;
import com.notification.notyos10.utils.ScreenUtil;
import com.notification.notyos10.utils.SharedPreferencesUtil;
import com.notification.notyos10.weather.services.AppLocationService;
import com.sevenheaven.iosswitch.ShSwitchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChooseLocationDialog extends Dialog {
    public static final String TAG = "ChooseLocationDialog";
    private static Location[] mLatLong;
    private List<String> mAddressList;
    private RelativeLayout rllDialogChooseLocationCurrent;
    private ShSwitchView swcDialogChooseLocationCurrrent;
    private ListView ltvDialogChooseLocation;
    private EditText edtDialogChooseLocationCity;
    private static final String LOCURL = "http://autocomplete.wunderground.com/aq?query=";
    private Context mContext;
    private static String mCity;
    private static boolean mAutoLocate;
    private AppLocationService mAppLocationService;

    public ChooseLocationDialog(Context context) {
        super(context);
        mContext = context;
        initialize();
    }

    private void initialize() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_choose_location);
        reSizeDialog();
        rllDialogChooseLocationCurrent = (RelativeLayout) findViewById(R.id.rll_dialog_choose_location__current);
        swcDialogChooseLocationCurrrent = (ShSwitchView) findViewById(R.id.swc_dialog_choose_location__currrent);
        ltvDialogChooseLocation = (ListView) findViewById(R.id.ltv_dialog_choose_location);
        edtDialogChooseLocationCity = (EditText) findViewById(R.id.edt_dialog_choose_location_city);
        edtDialogChooseLocationCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mAddressList.clear();
                    getCity(edtDialogChooseLocationCity.getText().toString());
                    return true;
                }
                return false;
            }
        });
        mAddressList = new ArrayList<String>();
        ltvDialogChooseLocation.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCity = mAddressList.get(position);
                SharedPreferencesUtil.savePreferences(mContext, Const.CITYLOCATION, mCity);
                SharedPreferencesUtil.saveLocation(mContext, mLatLong[position].getLatitude(), mLatLong[position].getLongitude());
                MyToast.showToast(mContext, "Successful!!!");
                ((WeatherActivity) mContext).chooseCityLocation();
                ChooseLocationDialog.this.dismiss();
            }
        });
        swcDialogChooseLocationCurrrent.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                SharedPreferencesUtil.setCurrentLocation(mContext, isOn);
                edtDialogChooseLocationCity.setEnabled(!isOn);
                ltvDialogChooseLocation.setEnabled(!isOn);
                if (isOn) {
                    getCurrentLocation();
//                    ((WeatherActivity) mContext).chooseCityLocation();
                    ltvDialogChooseLocation.setFocusableInTouchMode(false);
                    NotifyService.getInstance().updateWeather();
                } else {
                    edtDialogChooseLocationCity.setFocusable(true);
                }
            }
        });
        swcDialogChooseLocationCurrrent.setOn(SharedPreferencesUtil.isCurrentLocation(mContext));
        ltvDialogChooseLocation.setEmptyView(findViewById(R.id.txv_empty));
    }

    private void getCurrentLocation() {
        if (NetworkUtil.networkIsAvailable(mContext)) {
            //gps or network
            mAppLocationService = new AppLocationService(mContext);
            Location gpsLocation = mAppLocationService.getLocation(LocationManager.GPS_PROVIDER);
            Location nwLocation = mAppLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
            if (gpsLocation != null) {
                SharedPreferencesUtil.saveLocation(mContext, gpsLocation.getLatitude(), gpsLocation.getLongitude());
                MyToast.showToast(mContext, "GPS Location");
                getAddress();
            } else if (nwLocation != null) {
                SharedPreferencesUtil.saveLocation(mContext, nwLocation.getLatitude(), nwLocation.getLongitude());
                MyToast.showToast(mContext, "Network Location");
                getAddress();
            } else {
                MyToast.showToast(mContext, "You must enable Location Service in your device!");
            }
            mAppLocationService.stopLocation();
        } else {
            MyToast.showToast(mContext, "Can't connect to your network");
        }
    }


    private void getCity(final String city) {
        if (NetworkUtil.networkIsAvailable(mContext)) {
            new LoadLocationTask(mContext, new LoadLocationTask.CommunicatorLoadLocation() {
                @Override
                public void onPostExecute(List<String> addressList, Location[] latLong) {
                    mAddressList = addressList;
                    mLatLong = latLong;
                    myArrayAdapter listAdapter = new myArrayAdapter(mContext, R.layout.location_textview, mAddressList);
                    ltvDialogChooseLocation.setAdapter(listAdapter);
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,LOCURL + city);
        } else {
//            alertUserAboutError("Can't retrieve location at this time.");
            MyToast.showToast(mContext, "Can't connect network");
        }
    }

    private void getAddress() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            double[] location = SharedPreferencesUtil.getLocation(mContext);
            addresses = geocoder.getFromLocation(location[0], location[1], 1);
            mCity = addresses.get(0).getAddressLine(1);
            SharedPreferencesUtil.savePreferences(mContext, Const.CITYLOCATION, mCity);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }


    private class myArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<>();

        public myArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }
    }


    public void reSizeDialog() {

        int width = ScreenUtil.getCameraSize(mContext)[0];
        int height = ScreenUtil.getCameraSize(mContext)[1];

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = width;
        lp.height = height;

        getWindow().setAttributes(lp);

    }

}
