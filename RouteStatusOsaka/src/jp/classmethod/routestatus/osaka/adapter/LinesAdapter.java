package jp.classmethod.routestatus.osaka.adapter;

import java.util.ArrayList;

import jp.classmethod.routestatus.osaka.R;
import jp.classmethod.routestatus.osaka.data.LineInfo;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 路線/BookmarkのAdapterクラスです。
 * 
 * @author fukasawa.takeshi
 * 
 */
public class LinesAdapter extends ArrayAdapter<LineInfo> {
	private LayoutInflater mInflater;
	private int mResourceId;

	public LinesAdapter(Context context, int resourceId, ArrayList<LineInfo> lineInfo) {
		super(context, resourceId, lineInfo);
		mResourceId = resourceId;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	static class ViewHolder {
		LinearLayout layout;
		TextView lineName;
		TextView favorite;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(mResourceId, null);
			holder = new ViewHolder();
			holder.layout = (LinearLayout) convertView;

			holder.lineName = (TextView) convertView.findViewById(R.id.line_name_tv);
			holder.favorite = (TextView) convertView.findViewById(R.id.favorite);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final LineInfo lineInfo = getItem(position);
		final String lineName = lineInfo.lineName;
		final String id = lineInfo.id;
		holder.lineName.setText(lineName);

		if (lineInfo.isFavorite) {
			holder.favorite.setText("★");
		} else {
			holder.favorite.setText("☆");
		}

		holder.favorite.setOnTouchListener(new OnTouchListener() {
			SharedPreferences pref = getContext().getSharedPreferences("favorite",
					Activity.MODE_PRIVATE);

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					TextView textView = (TextView) v;
					String text = (String) textView.getText();
					Editor editor = pref.edit();
					if (text.equals("☆")) {
						textView.setText("★");
						editor.putBoolean(id, true);
						lineInfo.isFavorite = true;
					} else {
						textView.setText("☆");
						editor.remove(id);
						lineInfo.isFavorite = false;
					}
					editor.commit();
					return false;
				}
				return true;
			}
		});

		return convertView;
	}
}