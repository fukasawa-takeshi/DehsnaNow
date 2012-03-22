package jp.classmethod.routestatus.osaka.adapter;

import java.util.HashMap;
import java.util.List;

import jp.classmethod.routestatus.osaka.R;
import jp.classmethod.routestatus.osaka.task.AsyncActionQueue;
import jp.classmethod.routestatus.osaka.task.PhotoAction;
import jp.classmethod.routestatus.osaka.task.PhotoAction.OnPhotoResultListener;
import jp.classmethod.routestatus.osaka.task.PhotoAction.PhotoResult;
import jp.classmethod.routestatus.osaka.util.DateUtil;
import twitter4j.Tweet;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * タイムライン表示Adapterクラスです。
 * 
 * @author fukasawa.takeshi
 * 
 */
public class TimelineAdapter extends ArrayAdapter<Tweet> {
	private LayoutInflater mInflater;
	private Context mAppContext;
	private HashMap<Object, Bitmap> mCache;
	private AsyncActionQueue queue = new AsyncActionQueue();
	private ViewGroup mParent;

	/**
	 * コンストラクタです。
	 * 
	 * @param context
	 *            AppContext;
	 * @param resourceId
	 *            リソースID
	 * @param timelineList
	 *            タイムラインのリスト
	 * @param cache
	 *            アイコンのキャッシュ
	 */
	public TimelineAdapter(Context context, int resourceId, List<Tweet> timelineList,
			HashMap<Object, Bitmap> cache) {
		super(context, resourceId, timelineList);
		mAppContext = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mCache = cache;
	}

	/**
	 * ViewHolderクラスです。
	 * 
	 * @author fukasawa.takeshi
	 * 
	 */
	static class ViewHolder {
		TextView body;
		TextView date;
		TextView name;
		ImageView image;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (mParent == null) {
			mParent = parent;
		}

		// 無駄なビューを生成しないために使用
		ViewHolder holder;
		if (convertView == null) {
			// inflater.xmlから1行分のレイアウトを生成
			convertView = mInflater.inflate(R.layout.timeline_row, null);

			holder = new ViewHolder();
			holder.body = (TextView) convertView.findViewById(R.id.body);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.image = (ImageView) convertView.findViewById(R.id.twitterImage);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position < getCount()) {
			Tweet model = (Tweet) getItem(position);

			holder.date.setText(DateUtil.getStringDate(model.getCreatedAt()));
			holder.name.setText(model.getFromUser());
			holder.body.setText(model.getText());
			holder.image.setImageBitmap(null);

			String url = model.getProfileImageUrl();
			holder.image.setTag(url);
			if (mCache.containsKey(url)) {
				holder.image.setImageBitmap(mCache.get(url));
			} else {
				// 画像を取得
				PhotoAction action = new PhotoAction(mAppContext, url, new OnPhotoResultListener() {
					@Override
					public void onSuccess(PhotoResult result) {
						photoSuccess(result);
					}
				});
				queue.pushBack(action);
				queue.startActions();
			}

		} else {
			Log.d("WORN", "position > list.size()." + position + "," + getCount());
		}

		return convertView;
	}

	private void photoSuccess(PhotoResult result) {
		Bitmap photo = result.photo;
		if (result.photo == null) {
			return;
		}
		mCache.put(result.tag, photo);
		ImageView image = (ImageView) mParent.findViewWithTag(result.tag);
		if (image == null) {
			return;
		}
		image.setImageBitmap(photo);
	}
}