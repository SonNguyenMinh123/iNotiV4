package com.notification.notyos10.downloader;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ImageDownloader {

  private OnImageLoaderListener imageLoaderListener;
  private Set<String> urlsInProgress = new HashSet<>();
  private final String TAG = this.getClass().getSimpleName();

  public ImageDownloader(@NonNull OnImageLoaderListener listener) {
    this.imageLoaderListener = listener;
  }

  /**
   * Interface definition for callbacks to be invoked
   * when the image download status changes.
   */
  public interface OnImageLoaderListener {
    void onError(ImageError error);
    void onProgressChange(int percent);
    void onComplete(Bitmap result);
  }
  public void download(@NonNull final String imageUrl, final boolean displayProgress) {
    if (urlsInProgress.contains(imageUrl)) {
      return;
    }

    new AsyncTask<Void, Integer, Bitmap>() {

      private ImageError error;

      @Override
      protected void onPreExecute() {
        urlsInProgress.add(imageUrl);
        Log.d(TAG, "starting download");
      }

      @Override
      protected void onCancelled() {
        urlsInProgress.remove(imageUrl);
        imageLoaderListener.onError(error);
      }

      @Override
      protected void onProgressUpdate(Integer... values) {
        imageLoaderListener.onProgressChange(values[0]);
      }

      @Override
      protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        ByteArrayOutputStream out = null;
        try {
          connection = (HttpURLConnection) new URL(imageUrl).openConnection();
          if (displayProgress) {
            connection.connect();
            final int length = connection.getContentLength();
            if (length <= 0) {
              error = new ImageError("Invalid content length. The URL is probably not pointing to a file")
                  .setErrorCode(ImageError.ERROR_INVALID_FILE);
              this.cancel(true);
            }
            is = new BufferedInputStream(connection.getInputStream(), 8192);
            out = new ByteArrayOutputStream();
            byte bytes[] = new byte[8192];
            int count;
            long read = 0;
            while ((count = is.read(bytes)) != -1) {
              read += count;
              out.write(bytes, 0, count);
              publishProgress((int) ((read * 100) / length));
            }
            bitmap = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size());
          } else {
            is = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
          }
        } catch (Throwable e) {
          if (!this.isCancelled()) {
            error = new ImageError(e).setErrorCode(ImageError.ERROR_GENERAL_EXCEPTION);
            this.cancel(true);
          }
        } finally {
          try {
            if (connection != null)
              connection.disconnect();
            if (out != null) {
              out.flush();
              out.close();
            }
            if (is != null)
              is.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        return bitmap;
      }

      @Override
      protected void onPostExecute(Bitmap result) {
        if (result == null) {
          Log.e(TAG, "factory returned a null result");
          imageLoaderListener.onError(new ImageError("downloaded file could not be decoded as bitmap")
              .setErrorCode(ImageError.ERROR_DECODE_FAILED));
        } else {
          imageLoaderListener.onComplete(result);
        }
        urlsInProgress.remove(imageUrl);
        System.gc();
      }
    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }

  public interface OnBitmapSaveListener {
    void onBitmapSaved();
    void onBitmapSaveError(ImageError error);
  }

  public static void writeToDisk(Context context, @NonNull String imageUrl, @NonNull String downloadSubfolder) {
    Uri imageUri = Uri.parse(imageUrl);
    String fileName = imageUri.getLastPathSegment();
    String downloadSubpath = downloadSubfolder + fileName;
    Log.e("test_file_save","filename : " + fileName);
    Log.e("test_file_save","uri : " + imageUrl);
    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    DownloadManager.Request request = new DownloadManager.Request(imageUri);
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
    request.setDescription(imageUrl);
    request.allowScanningByMediaScanner();
    request.setDestinationUri(getDownloadDestination(downloadSubpath));
    downloadManager.enqueue(request);

  }



  private static File mFile;
  @NonNull
  public static Uri getDownloadDestination(String downloadSubpath) {
    File picturesFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File destinationFile = new File(picturesFolder, downloadSubpath);
    destinationFile.mkdirs();
    mFile = destinationFile;
    return Uri.fromFile(destinationFile);
  }

  public static Uri getUriImage(){
    return Uri.fromFile(mFile);
  }

  public static File getmFile(){
    return mFile;
  }


  public static Bitmap readFromDisk(@NonNull File imageFile) {
    if (!imageFile.exists() || imageFile.isDirectory()) return null;
    return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
  }


  public interface OnImageReadListener {
    void onImageRead(Bitmap bitmap);
    void onReadFailed();
  }

  /**
   * Reads the given file as Bitmap in the background. The appropriate callback
   * of the provided <i>OnImageReadListener</i> will be triggered upon completion.
   * @param imageFile the file to read
   * @param listener the listener to notify the caller when the
   *                 image read operation finishes
   * @since 1.1
   */
  public static void readFromDiskAsync(@NonNull File imageFile, @NonNull final OnImageReadListener listener) {
    new AsyncTask<String, Void, Bitmap>() {
      @Override
      protected Bitmap doInBackground(String... params) {
        return BitmapFactory.decodeFile(params[0]);
      }

      @Override
      protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null)
          listener.onImageRead(bitmap);
        else
          listener.onReadFailed();
      }
    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageFile.getAbsolutePath());
  }


  public static final class ImageError extends Throwable {

    private int errorCode;

    public static final int ERROR_GENERAL_EXCEPTION = -1;

    public static final int ERROR_INVALID_FILE = 0;

    public static final int ERROR_DECODE_FAILED = 1;

    public static final int ERROR_FILE_EXISTS = 2;

    public static final int ERROR_PERMISSION_DENIED = 3;

    public static final int ERROR_IS_DIRECTORY = 4;


    public ImageError(@NonNull String message) {
      super(message);
    }

    public ImageError(@NonNull Throwable error) {
      super(error.getMessage(), error.getCause());
      this.setStackTrace(error.getStackTrace());
    }

    /**
     * @param code the code for the occurred error
     * @return the same ImageError object
     */
    public ImageError setErrorCode(int code) {
      this.errorCode = code;
      return this;
    }

    /**
     * @return the error code that was previously set
     * by {@link #setErrorCode(int)}
     */
    public int getErrorCode() {
      return errorCode;
    }
  }
}
