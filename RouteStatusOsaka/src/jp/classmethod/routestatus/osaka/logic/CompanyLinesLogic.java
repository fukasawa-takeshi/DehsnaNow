package jp.classmethod.routestatus.osaka.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import jp.classmethod.routestatus.osaka.R;
import jp.classmethod.routestatus.osaka.data.LineInfo;
import jp.classmethod.routestatus.osaka.data.RouteLine;
import android.content.Context;
import android.content.SharedPreferences;

public class CompanyLinesLogic {

	/**
	 * SharedPreferenceからお気に入りリストを設定します。
	 * 
	 * @param routeLines
	 *            路線会社リスト
	 * @param prefsMap
	 *            SharedPreferenceのMap
	 * @return
	 */
	public ArrayList<LineInfo> getFavoriteData(ArrayList<RouteLine> routeLines,
			HashMap<String, ?> prefsMap) {
		// お気に入りのデータを取得。
		Set<String> set = prefsMap.keySet();
		ArrayList<LineInfo> favoriteLineInfoList = new ArrayList<LineInfo>();
		for (RouteLine routeLine : routeLines) {
			ArrayList<LineInfo> lineInfoList = routeLine.lineInfo;
			for (LineInfo lineInfo : lineInfoList) {
				String id = lineInfo.id;
				Iterator<String> iterator = set.iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					if (id.equals(key)) {
						lineInfo.isFavorite = true;
						favoriteLineInfoList.add(lineInfo);
						break;
					}
				}
			}
		}
		return favoriteLineInfoList;
	}

	/**
	 * 路線情報リストにお気に入り情報を設定します。
	 * 
	 * @param lineInfoList
	 * @param prefsMap
	 */
	public void setFavoriteToLineInfoList(ArrayList<LineInfo> lineInfoList,
			HashMap<String, ?> prefsMap) {

		Set<String> set = prefsMap.keySet();
		for (LineInfo lineInfo : lineInfoList) {
			lineInfo.isFavorite = false;
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				if (key.equals(lineInfo.id)) {
					lineInfo.isFavorite = true;
					continue;
				}
			}
		}
	}

	/**
	 * 路線名と検索語リストを設定します。
	 */
	public ArrayList<RouteLine> getRouteLinesData(Context context) {

		SharedPreferences pref = context.getSharedPreferences("favorite", Context.MODE_PRIVATE);

		Iterator<String> ite = pref.getAll().keySet().iterator();

		String[] lineNameSearch = context.getResources().getStringArray(R.array.line_name_search);
		String[] sp;

		ArrayList<RouteLine> routeLines = new ArrayList<RouteLine>();
		RouteLine routeLine = null;
		String prevCom = "";
		for (String val : lineNameSearch) {
			sp = val.split("\\|");
			if (!prevCom.equals(sp[1])) {
				routeLine = new RouteLine();
				routeLine.company = sp[1];
				routeLine.lineInfo = new ArrayList<LineInfo>();
				routeLines.add(routeLine);
			}
			prevCom = sp[1];
			LineInfo lineInfo = new LineInfo();
			lineInfo.id = sp[0];
			lineInfo.companyName = sp[1];
			lineInfo.lineName = sp[2];
			lineInfo.searchWord = sp[3];
			lineInfo.url = sp[4];
			// お気に入り
			while (ite.hasNext()) {
				String key = ite.next();
				if (sp[0].equals(key)) {
					lineInfo.isFavorite = true;
					break;
				}
			}

			routeLine.lineInfo.add(lineInfo);
		}
		return routeLines;
	}
}
