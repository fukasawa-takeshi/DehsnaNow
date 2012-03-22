package jp.classmethod.routestatus.osaka;

import java.util.ArrayList;
import java.util.HashMap;

import jp.classmethod.routestatus.osaka.data.LineInfo;
import jp.classmethod.routestatus.osaka.data.RouteLine;
import jp.classmethod.routestatus.osaka.logic.CompanyLinesLogic;
import jp.classmethod.routestatus.osaka.logic.IntentLogic;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
	private static final int MENU_ID_FAVORITE = (Menu.FIRST + 1);
	private static final int MENU_ID_COMPANY = (Menu.FIRST + 2);

	protected boolean isFavorite = false;
	protected ArrayList<RouteLine> routeLineList;

	protected CompanyLinesLogic companyLinesLogic = new CompanyLinesLogic();
	protected IntentLogic intentLogic = new IntentLogic();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// メニューアイテムを追加します
		Resources res = getResources();
		menu.add(Menu.NONE, MENU_ID_COMPANY, Menu.NONE, res.getString(R.string.menu_lines));
		menu.add(Menu.NONE, MENU_ID_FAVORITE, Menu.NONE, res.getString(R.string.menu_favorite));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		if (this instanceof RouteCompanyActivity) {
			menu.findItem(MENU_ID_COMPANY).setEnabled(false);
		} else if (this instanceof RouteLinesActivity) {
			if (isFavorite) {
				menu.findItem(MENU_ID_FAVORITE).setEnabled(false);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	// オプションメニューアイテムが選択された時に呼び出されます
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch (item.getItemId()) {
		case MENU_ID_FAVORITE:
			ret = true;
			if (isFavorite) {
				return ret;
			}
			Context app = getApplicationContext();
			// お気に入り
			SharedPreferences pref = getSharedPreferences("favorite", MODE_PRIVATE);
			HashMap<String, ?> prefsMap = (HashMap<String, ?>) pref.getAll();
			if (prefsMap == null || prefsMap.size() == 0) {
				// お気に入りに一つも入っていない。
				Toast.makeText(app, R.string.err_no_favorite, Toast.LENGTH_SHORT).show();
				return ret;
			}
			ArrayList<LineInfo> lineInfoList = companyLinesLogic.getFavoriteData(routeLineList,
					prefsMap);
			intentLogic.showFavorite(lineInfoList, routeLineList, this);
			finish();

			break;
		case MENU_ID_COMPANY:
			ret = true;
			// TODO routeLineListのお気に入りを更新
			intentLogic.showCompany(routeLineList, this);
			finish();
			break;
		default:
			ret = super.onOptionsItemSelected(item);
			break;
		}
		return ret;
	}

}
