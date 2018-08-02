package com.notification.inotythemehhhhh.objects;

/**
 * Created by fragment_test on 26/10/2016.
 */
public class FileInfor {
	private String name;
	private long size;
	private String path;

	public FileInfor(String name, long size, String path) {
		this.name = name;
		this.size = size;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
