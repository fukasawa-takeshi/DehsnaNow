package jp.classmethod.routestatus.osaka.util;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

/**
 * 例外処理用Utilクラスです。
 * @author fukasawa.takeshi
 */
public final class ExceptionUtil {

    private static Toast toast;

    /**
     * コンストラクタです。
     */
    private ExceptionUtil() {
    }

    /**
     * エラーメッセージを表示します。
     * @param context Context
     * @param resId リソースID
     * @param tag タグ
     * @param ex Exception
     */
    public static void showMessage(Context context, int resId, String tag, Exception ex) {
        if (ex != null) {
            if (tag == null) {
                tag = "";
            }
            Log.e(tag, ex.getMessage());
        }
        if (resId == 0) {
            showMessage(context, "エラーが発生しました。", context.getClass().getName());
        }
        String s = context.getString(resId);
        showMessage(context, s, tag);
    }

    /**
     * エラーメッセージを表示します。
     * @param context Context
     * @param msg メッセージ
     * @param tag タグ
     */
    public static void showMessage(final Context context, final String msg, String tag) {
        String m = msg;
        if (msg == null || StringUtil.isEmpty(msg)) {
            m = "エラー";
        }
        CharSequence val = Html.fromHtml(m);
        if (toast == null) {
            toast = Toast.makeText(context, val, Toast.LENGTH_LONG);
        } else {
            toast.setText(val);
        }
        toast.show();
        Log.e(tag, context.getClass().getName());
    }

}
