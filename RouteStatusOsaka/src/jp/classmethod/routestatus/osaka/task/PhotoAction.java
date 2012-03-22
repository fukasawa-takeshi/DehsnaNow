package jp.classmethod.routestatus.osaka.task;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import jp.classmethod.routestatus.osaka.task.AsyncActionQueue.Action;
import jp.classmethod.routestatus.osaka.util.ExceptionUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class PhotoAction implements Action {

	private String mUrl;
	private Context mAppContext;
	private OnPhotoResultListener mOnPhotoResult;

	public PhotoAction(Context context, String url, OnPhotoResultListener onPhotoResult) {
		mUrl = url;
		mOnPhotoResult = onPhotoResult;
		mAppContext = context;
	}

	@Override
	public void onPreExecute() {
	}

	@Override
	public Object onBackgroundAction() {
		PhotoResult result = new PhotoResult();
		result.tag = mUrl;

		URL imageUrl;
		try {
			imageUrl = new URL(mUrl);
			InputStream imageIs;
			imageIs = imageUrl.openStream();
			Bitmap photo = BitmapFactory.decodeStream(imageIs);
			if (photo != null) {
				result.photo = photo;
			}
		} catch (MalformedURLException e) {
			Log.e("PhotoAction", e.getLocalizedMessage());
			// ExceptionUtil.showMessage(mAppContext, e.getMessage(), getClass().getName());
		} catch (IOException e) {
			Log.e("PhotoAction", e.getLocalizedMessage());
			// ExceptionUtil.showMessage(mAppContext, e.getMessage(), getClass().getName());
		}
		return result;
	}

	@Override
	public void onCancel() {
	}

	@Override
	public void onPost(Object obj) {
		PhotoResult result = (PhotoResult) obj;
		mOnPhotoResult.onSuccess(result);
	}

	public class PhotoResult {
		public Bitmap photo;
		public Object tag;
	}

	public interface OnPhotoResultListener {
		void onSuccess(PhotoResult result);
	}

}
