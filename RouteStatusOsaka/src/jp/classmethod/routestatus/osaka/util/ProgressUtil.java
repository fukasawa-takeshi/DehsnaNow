package jp.classmethod.routestatus.osaka.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

/**
 * プログレス関連のUtilクラスです。
 * 
 * @author fukasawa.takeshi
 */
public final class ProgressUtil {

	/**
	 * コンストラクタです。
	 */
	private ProgressUtil() {
	}

	/** プログレスコンポーネントのインスタンスです。 */
	private static ViewGroup progress;

	/** LayoutInflater。 */
	private static LayoutInflater inflater;

	/** 表示中か。 */
	public static boolean isShowing = false;

	/**
	 * プログレスバーを表示します。
	 * 
	 * @param context
	 *            Context
	 * @param vg
	 *            表示対象のViewGroup
	 */
	public static void show(Context context, int layoutId, ViewGroup vg) {

		vg.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		if (progress == null) {
			if (inflater == null) {
				inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}
			progress = (ViewGroup) inflater.inflate(layoutId, null);
			progress.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// Touchイベントの伝播を止める。
					return true;
				}
			});
		}

		ViewGroup parent = (ViewGroup) progress.getParent();
		if (parent != null) {
			parent.removeView(progress);
		}
		vg.addView(progress);
		isShowing = true;
	}

	/**
	 * プログレスバーの表示を取り消します。
	 * 
	 * @param v
	 *            対象のViewGroup
	 */
	public static void dissmiss(ViewGroup v) {

		v.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});

		ViewGroup parent = (ViewGroup) progress.getParent();
		if (parent != null) {
			parent.removeView(progress);
		}
		isShowing = false;
	}

}
