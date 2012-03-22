package jp.classmethod.routestatus.osaka.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日付処理に関するユーティリティクラス.
 */
public final class DateUtil {

	private DateUtil() {
		// nothing
	}

	/** 日付形式 */
	private static final String DATE_FORMAT = "HH:mm   yyyy/MM/dd";

	public static String getStringDate(Date date) {
		if(date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(date);
	}
}
