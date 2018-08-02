package com.notification.inotythemehhhhh.controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.notification.inotythemehhhhh.objects.FileInfor;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by fragment_test on 28/10/2016.
 */
public class LoadListFile extends AsyncTask<String, String, String> {
	private Context mContext;
	private OnLoadListFile onLoadListFile;
	private ArrayList<FileInfor> arrImage = null;

	public LoadListFile(Context mContext, OnLoadListFile onLoadListFile) {
		this.mContext = mContext;
		this.onLoadListFile = onLoadListFile;
	}


	@Override
	protected String doInBackground(String... params) {
		initArrImage();
		return null;
	}


	public void initArrImage() {

		while (arrImage == null) {
			arrImage = new ArrayList<>();
			arrImage = getListFiles(new File(Environment.getExternalStorageDirectory().getPath()));
		}
	}

	private ArrayList<FileInfor> getListFiles(File parentDir) {
		ArrayList<FileInfor> arrayList = new ArrayList<>();
		File[] files = parentDir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {

				arrayList.addAll(getListFiles(file));
			} else {

				int vt = file.getPath().lastIndexOf(".");

				if (vt > 0) {
					if (file.getName().endsWith(".png")
							|| file.getName().endsWith(".jpg")
							|| file.getName().endsWith(".jpeg")
							|| file.getName().endsWith(".tiff")
							|| file.getName().endsWith(".bmp")
							) {
						FileInfor fileInfor = new FileInfor(file.getName(), file.length(), file.getPath());
						arrayList.add(fileInfor);
					}
				}
			}
		}
		return arrayList;
	}

	@Override
	protected void onPostExecute(String s) {
		super.onPostExecute(s);

		if (onLoadListFile != null) {
			onLoadListFile.onFinish(arrImage);
		}
	}

	public interface OnLoadListFile {
		void onFinish(ArrayList<FileInfor> arrImage);
	}
}
