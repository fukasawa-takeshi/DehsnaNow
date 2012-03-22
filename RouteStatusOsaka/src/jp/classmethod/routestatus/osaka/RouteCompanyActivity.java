package jp.classmethod.routestatus.osaka;

import java.util.ArrayList;

import jp.classmethod.routestatus.osaka.adapter.CompanyAdapter;
import jp.classmethod.routestatus.osaka.data.RouteLine;
import jp.classmethod.routestatus.osaka.logic.IntentLogic;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 鉄道会社選択Activityです。
 * 
 * @author fukasawa.takeshi
 * 
 */
public class RouteCompanyActivity extends BaseActivity {

	/** Called when the activity is first created. */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.companys);

		// Intent引数の取得
		Intent intent = getIntent();
		routeLineList = (ArrayList<RouteLine>) intent
				.getSerializableExtra(IntentLogic.ROUTE_LINE_LIST);

		// 会社リストの表示設定
		ListView lines = (ListView) findViewById(R.id.companys_lv);
		CompanyAdapter adapter = new CompanyAdapter(getApplicationContext(), R.layout.company_row,
				routeLineList);
		lines.setAdapter(adapter);
		lines.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
				companyClick(routeLineList, pos);
			}
		});

		// ヘッダの表示設定
		TextView lineName = (TextView) findViewById(R.id.line_name_tv);
		lineName.setText(getResources().getString(R.string.company_select));

	}

	/**
	 * 鉄道会社クリック時。
	 * 
	 * @param routeLineList
	 *            選択された会社データ
	 * @param pos
	 *            選択されたポジション
	 */
	private void companyClick(ArrayList<RouteLine> routeLineList, int pos) {
		intentLogic.showLines(pos, routeLineList, RouteCompanyActivity.this);
	}
}