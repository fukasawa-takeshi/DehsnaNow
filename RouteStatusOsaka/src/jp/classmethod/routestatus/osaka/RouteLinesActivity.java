package jp.classmethod.routestatus.osaka;

import java.util.ArrayList;
import java.util.HashMap;

import jp.classmethod.routestatus.osaka.adapter.LinesAdapter;
import jp.classmethod.routestatus.osaka.data.LineInfo;
import jp.classmethod.routestatus.osaka.data.RouteLine;
import jp.classmethod.routestatus.osaka.logic.IntentLogic;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 路線/Bookmark用Activityです。
 * 
 * @author fukasawa.takeshi
 * 
 */
public class RouteLinesActivity extends BaseActivity {

	private ArrayList<LineInfo> lineInfoList;
	private int companyIndex;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lines);

		// Intent引数の取得
		Intent intent = getIntent();
		routeLineList = (ArrayList<RouteLine>) intent
				.getSerializableExtra(IntentLogic.ROUTE_LINE_LIST);
		isFavorite = intent.getBooleanExtra(IntentLogic.IS_FAVORITE, false);
		companyIndex = intent.getIntExtra(IntentLogic.COMPANY_INDEX, 0);

		// ヘッダ
		TextView lineName = (TextView) findViewById(R.id.line_name_tv);

		if (!isFavorite) {
			// 路線選択
			RouteLine routeLine = routeLineList.get(companyIndex);
			SharedPreferences pref = getSharedPreferences("favorite", MODE_PRIVATE);
			HashMap<String, ?> prefsMap = (HashMap<String, ?>) pref.getAll();
			lineInfoList = routeLine.lineInfo;
			companyLinesLogic.setFavoriteToLineInfoList(lineInfoList, prefsMap);
			lineName.setText(routeLine.company);
		} else {
			// お気に入り表示
			lineInfoList = (ArrayList<LineInfo>) intent
					.getSerializableExtra(IntentLogic.FAVORITE_LINE_LIST);
			lineName.setText(getResources().getString(R.string.favorite_lines));
		}

		// リストの設定
		ListView lines = (ListView) findViewById(R.id.lines_lv);
		LinesAdapter adapter = new LinesAdapter(getApplicationContext(), R.layout.lines_row,
				lineInfoList);
		lines.setAdapter(adapter);
		// タップ時
		lines.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int pos,
					long id) {
				lineClick(lineInfoList, pos);
			}
		});
		// 長押し時
		final Activity that = this;
		lines.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView,
					int pos, long id) {
				String[] menus = that.getResources().getStringArray(R.array.route_context_nemu);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(that,
						R.layout.route_context_row, menus);

				final LineInfo lineInfo = lineInfoList.get(pos);
				String name = lineInfo.companyName + " " + lineInfo.lineName;
				AlertDialog.Builder builder = new AlertDialog.Builder(that);
				builder.setTitle(name).setAdapter(adapter, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Uri uri = Uri.parse(lineInfo.url);
						Intent i = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(i);
					}
				}).show();
				return false;
			}
		});
	}

	/**
	 * 路線クリック時ハンドラです。
	 * 
	 * @param lineInfoList
	 *            路線情報
	 * @param pos
	 *            Listのポジション
	 */
	private void lineClick(ArrayList<LineInfo> lineInfoList, int pos) {
		LineInfo lineInfo = lineInfoList.get(pos);
		intentLogic.showStatus(lineInfo, routeLineList, RouteLinesActivity.this);
	}

}