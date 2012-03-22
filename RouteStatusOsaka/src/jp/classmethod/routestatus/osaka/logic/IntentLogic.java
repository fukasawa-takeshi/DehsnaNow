package jp.classmethod.routestatus.osaka.logic;

import java.util.ArrayList;

import jp.classmethod.routestatus.osaka.RouteCompanyActivity;
import jp.classmethod.routestatus.osaka.RouteLinesActivity;
import jp.classmethod.routestatus.osaka.RouteStatusActivity;
import jp.classmethod.routestatus.osaka.data.LineInfo;
import jp.classmethod.routestatus.osaka.data.RouteLine;
import android.app.Activity;
import android.content.Intent;

public class IntentLogic {

	public static final String LINE_INFO = "lineInfo";
	public static final String LINE_INFO_LIST = "lineInfoList";
	public static final String ROUTE_LINE = "routeLine";
	public static final String ROUTE_LINE_LIST = "routeLineList";
	public static final String FAVORITE_LINE_LIST = "favoriteLineList";
	public static final String IS_FAVORITE = "isFavorite";
	public static final String COMPANY_INDEX = "companyIndex";

	/**
	 * タイムラインを表示します。
	 * 
	 * @param lineInfo
	 *            路線情報
	 * @param fromActivity
	 *            遷移元Activity
	 */
	public void showStatus(LineInfo lineInfo, ArrayList<RouteLine> routeLines, Activity fromActivity) {
		Intent intent = new Intent();
		intent.putExtra(LINE_INFO, lineInfo);
		intent.putExtra(ROUTE_LINE_LIST, routeLines);
		intent.setClass(fromActivity, RouteStatusActivity.class);
		fromActivity.startActivity(intent);

	}

	/**
	 * お気に入り一覧を表示します。
	 * 
	 * @param favoriteRouteLines
	 *            お気に入り一覧
	 */
	public void showFavorite(ArrayList<LineInfo> favoriteLineInfoList,
			ArrayList<RouteLine> routeLines, Activity fromActivity) {
		Intent intent = new Intent();
		intent.putExtra(FAVORITE_LINE_LIST, favoriteLineInfoList);
		intent.putExtra(ROUTE_LINE_LIST, routeLines);
		intent.putExtra(IS_FAVORITE, true);
		intent.setClass(fromActivity, RouteLinesActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		fromActivity.startActivity(intent);
	}

	/**
	 * 路線一覧を表示します。
	 * 
	 * @param pos
	 * @param routeLineList
	 * @param fromActivity
	 */
	public void showLines(int pos, ArrayList<RouteLine> routeLineList, Activity fromActivity) {
		Intent intent = new Intent();
		intent.putExtra(COMPANY_INDEX, pos);
		intent.putExtra(ROUTE_LINE_LIST, routeLineList);
		intent.putExtra(IS_FAVORITE, false);
		intent.setClass(fromActivity, RouteLinesActivity.class);
		fromActivity.startActivity(intent);
	}

	/**
	 * 鉄道会社一覧を表示します。
	 * 
	 * @param routeLines
	 *            鉄道会社データ
	 */
	public void showCompany(ArrayList<RouteLine> routeLines, Activity fromActivity) {
		Intent intent = new Intent();
		intent.putExtra(ROUTE_LINE_LIST, routeLines);
		intent.putExtra(IS_FAVORITE, false);
		intent.setClass(fromActivity, RouteCompanyActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		fromActivity.startActivity(intent);
	}
}
