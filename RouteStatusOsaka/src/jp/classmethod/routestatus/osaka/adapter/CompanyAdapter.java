package jp.classmethod.routestatus.osaka.adapter;

import java.util.ArrayList;

import jp.classmethod.routestatus.osaka.data.RouteLine;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 会社一覧Adapterクラスです。
 * 
 * @author fukasawa.takeshi
 * 
 */
public class CompanyAdapter extends ArrayAdapter<RouteLine> {
	private LayoutInflater mInflater;
	private int mResourceId;

	public CompanyAdapter(Context context, int resourceId, ArrayList<RouteLine> routeLines) {
		super(context, resourceId, routeLines);
		mResourceId = resourceId;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	static class ViewHolder {
		TextView company;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(mResourceId, null);
			holder = new ViewHolder();
			holder.company = (TextView) convertView;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		RouteLine routeLine = getItem(position);

		TextView textView = (TextView) convertView;
		textView.setText(routeLine.company);

		return convertView;
	}
}