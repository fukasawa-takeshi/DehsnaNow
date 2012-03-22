package jp.classmethod.routestatus.osaka;

import java.util.ArrayList;
import java.util.HashMap;

import jp.classmethod.routestatus.osaka.data.LineInfo;
import jp.classmethod.routestatus.osaka.data.RouteLine;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * 最初に起動されるActivityです。 <br>
 * BookMarkの有無で表示する画面を変えます。
 * 
 * @author fukasawa.takeshi
 * 
 */
public class InitActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 路線名と検索語リストを設定。
		final ArrayList<RouteLine> routeLines = companyLinesLogic
				.getRouteLinesData(getApplicationContext());

		SharedPreferences pref = getSharedPreferences("favorite", MODE_PRIVATE);
		HashMap<String, ?> prefsMap = (HashMap<String, ?>) pref.getAll();
		if (prefsMap == null || prefsMap.size() == 0) {
			// お気に入りに一つも入っていない場合は会社選択。
			intentLogic.showCompany(routeLines, this);
			finish();
			return;
		}

		// お気に入りのデータを取得。
		ArrayList<LineInfo> favoriteLineInfoList = companyLinesLogic.getFavoriteData(routeLines,
				prefsMap);
		intentLogic.showFavorite(favoriteLineInfoList, routeLines, this);
		finish();
	}

}
