package com.notification.inotythemehhhhh.widgets;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.notification.inotythemehhhhh.R;
import com.notification.inotythemehhhhh.controllers.LoadListFile;
import com.notification.inotythemehhhhh.myadapters.ImageAdapter;
import com.notification.inotythemehhhhh.mycenterutils.SharedPreferencesUtil;
import com.notification.inotythemehhhhh.myservices.NotifyService;
import com.notification.inotythemehhhhh.objects.FileInfor;

import java.util.ArrayList;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

/**
 * Created by uythi on 02/11/2016.
 */
public class BackGroundActivity extends Activity implements View.OnTouchListener {
	private TextView textDefault, textBlack, textBlue, textPurple, textOrange, textRed;
	private View headerView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_background_notyfi);

		final GridViewWithHeaderAndFooter gridview = (GridViewWithHeaderAndFooter) findViewById(R.id.gridview);

		LayoutInflater layoutInflater = LayoutInflater.from(this);
		headerView = layoutInflater.inflate(R.layout.header, null);
//		View footerView = layoutInflater.inflate(R.layout.test_footer_view, null);
		gridview.addHeaderView(headerView);
		initView();

		LoadListFile loadListFile = new LoadListFile(this, new LoadListFile.OnLoadListFile() {
			@Override
			public void onFinish(final ArrayList<FileInfor> arrImage) {

				ImageAdapter imageAdapter = new ImageAdapter(BackGroundActivity.this, arrImage);

				gridview.setAdapter(imageAdapter);
				gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
						AlertDialog.Builder builder1 = new AlertDialog.Builder(BackGroundActivity.this);
						builder1.setMessage("Set background Notify");
						builder1.setCancelable(true);

						builder1.setPositiveButton(
								"Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										SharedPreferencesUtil.setBackgroundNotify(BackGroundActivity.this, arrImage.get(position).getPath());
										NotifyService notifyService = NotifyService.getInstance();
										if (notifyService!=null) {
											notifyService.updateBackGroundNotify(SharedPreferencesUtil.getBackgroundNotify(BackGroundActivity.this), 3);
										}
										dialog.cancel();
										onBackPressed();
									}
								});

						builder1.setNegativeButton(
								"No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});

						AlertDialog alert11 = builder1.create();
						alert11.show();
					}
				});
				Log.e("TAG", "" + arrImage.size());
			}
		});
		loadListFile.execute();
	}

	private void initView() {
		textDefault = (TextView) headerView.findViewById(R.id.bgr_default_header);
		textBlack = (TextView) headerView.findViewById(R.id.bgr_black_header);
		textBlue = (TextView) headerView.findViewById(R.id.bgr_blue_header);
		textPurple = (TextView) headerView.findViewById(R.id.bgr_purple_header);
		textOrange = (TextView) headerView.findViewById(R.id.bgr_orange_header);
		textRed = (TextView) headerView.findViewById(R.id.bgr_red_header);
		textDefault.setOnTouchListener(this);
		textBlue.setOnTouchListener(this);
		textPurple.setOnTouchListener(this);
		textOrange.setOnTouchListener(this);
		textRed.setOnTouchListener(this);
		textBlack.setOnTouchListener(this);
	}


	@Override
	public boolean onTouch(final View v, MotionEvent event) {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(BackGroundActivity.this);
		builder1.setMessage("Set background Notify");
		builder1.setCancelable(true);

		builder1.setPositiveButton(
				"Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						NotifyService notifyService = NotifyService.getInstance();
						if (notifyService!=null) {
							switch (v.getId()) {
								case R.id.bgr_default_header:
									notifyService.updateBackGroundNotify("", 1);
									break;
								case R.id.bgr_black_header:
									notifyService.updateBackGroundNotify("black", 2);
									break;
								case R.id.bgr_blue_header:
									notifyService.updateBackGroundNotify("blue", 2);
									break;
								case R.id.bgr_purple_header:
									notifyService.updateBackGroundNotify("purple", 2);
									break;
								case R.id.bgr_orange_header:
									notifyService.updateBackGroundNotify("orange", 2);
									break;
								case R.id.bgr_red_header:
									notifyService.updateBackGroundNotify("red", 2);
									break;
							}
						}
						dialog.cancel();
						onBackPressed();
					}
				});

		builder1.setNegativeButton(
				"No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.show();

		return false;
	}
}
