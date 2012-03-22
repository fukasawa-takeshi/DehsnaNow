package jp.classmethod.routestatus.osaka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.classmethod.routestatus.osaka.adapter.TimelineAdapter;
import jp.classmethod.routestatus.osaka.data.LineInfo;
import jp.classmethod.routestatus.osaka.data.RouteLine;
import jp.classmethod.routestatus.osaka.logic.IntentLogic;
import jp.classmethod.routestatus.osaka.task.TweetSearchTask;
import jp.classmethod.routestatus.osaka.task.TweetSearchTask.OnTweetSearchResultListner;
import jp.classmethod.routestatus.osaka.task.TweetSearchTask.TweetSearchCondition;
import jp.classmethod.routestatus.osaka.util.ProgressUtil;
import twitter4j.Tweet;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * タイムライン表示Activityです。
 * 
 * @author fukasawa.takeshi
 * 
 */
public class RouteStatusActivity extends BaseActivity {

	private static final int REFRESH_INTERVAL_SEC = 10;
	private static final int MAX_NUM = 20;

	private TimelineAdapter mAdapter;
	private FrameLayout mBase;
	private Button mReflesh;
	private ListView mListView;

	private Handler mHandler = new Handler();
	private Runnable mRunnable;

	private long mSinceId;

	private List<Tweet> mNewTweets;
	private List<Tweet> mTweets;

	private HashMap<Object, Bitmap> mCache = new HashMap<Object, Bitmap>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mBase = (FrameLayout) inflater.inflate(R.layout.timeline, null);
		setContentView(mBase);

		mNewTweets = new ArrayList<Tweet>();

		mListView = (ListView) this.findViewById(R.id.timeline_lv);
		ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) mListView
				.getLayoutParams();
		mlp.setMargins(0, 0, 0, 0);

		mReflesh = (Button) findViewById(R.id.refresh_bt);
		mReflesh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				for (Tweet tweet : mNewTweets) {
					mTweets.add(0, tweet);
				}
				int size = mTweets.size();
				// MAX_NUM以上のTweetを削除
				if (size > MAX_NUM) {
					List<Tweet> removeList = new ArrayList<Tweet>();
					for (int i = MAX_NUM; i < size; i++) {
						removeList.add(mTweets.get(i));
					}
					for (Tweet tweet : removeList) {
						mTweets.remove(tweet);
					}
				}
				mNewTweets = new ArrayList<Tweet>();

				mAdapter = new TimelineAdapter(getApplicationContext(), R.layout.timeline_row,
						mTweets, mCache);
				mListView.setAdapter(mAdapter);
				mReflesh.setVisibility(View.INVISIBLE);
			}
		});

		Intent intent = getIntent();
		LineInfo lineInfo = (LineInfo) intent.getSerializableExtra(IntentLogic.LINE_INFO);
		routeLineList = (ArrayList<RouteLine>) intent
				.getSerializableExtra(IntentLogic.ROUTE_LINE_LIST);

		TextView lineName = (TextView) findViewById(R.id.line_name_tv);
		lineName.setText(lineInfo.companyName + " > " + lineInfo.lineName);

		final String searchWord = lineInfo.searchWord;
		mRunnable = new Runnable() {
			@Override
			public void run() {
				doGetTweeetSearch(searchWord);
				mHandler.postDelayed(this, 1000 * REFRESH_INTERVAL_SEC);
			}
		};
		mHandler.postDelayed(mRunnable, 0);
		// プログレスを表示
		ProgressUtil.show(getApplicationContext(), R.layout.progress, mBase);

	}

	/**
	 * ツイートの取得成功時イベントハンドラです。。
	 * 
	 * @param tweets
	 *            ツイートのリスト
	 */
	private void tweetSearchSuccess(List<Tweet> tweets) {
		if (mBase != null) {
			ProgressUtil.dissmiss(mBase);
		}
		if (tweets == null || tweets.size() == 0) {
			if (mAdapter == null) {
				Toast.makeText(getApplicationContext(), R.string.err_no_result, Toast.LENGTH_LONG)
						.show();
			}
			return;
		}

		// MaxIDをSinceIDとして取得
		mSinceId = tweets.get(0).getId();

		if (mAdapter == null) {
			// 初回
			mAdapter = new TimelineAdapter(this, R.layout.timeline_row, tweets, mCache);
			mListView.setAdapter(mAdapter);
			mTweets = tweets;
		} else {

			for (Tweet tweet : tweets) {
				mNewTweets.add(0, tweet);
			}
			int size = mNewTweets.size();
			String sSize = String.valueOf(size);
			if (size > MAX_NUM) {
				sSize = MAX_NUM + "+";
			}
			mReflesh.setText(sSize);
			mReflesh.setVisibility(View.VISIBLE);

			AlphaAnimation animation = new AlphaAnimation(0, 1);
			animation.setDuration(700);
			mReflesh.startAnimation(animation);

		}
	}

	/**
	 * ツイートを取得します。
	 * 
	 * @param searchWord
	 *            検索語
	 */
	private void doGetTweeetSearch(String searchWord) {
		TweetSearchCondition condition = new TweetSearchCondition();
		condition.searchWords = searchWord;
		condition.sinceId = mSinceId;
		condition.maxNum = MAX_NUM;

		TweetSearchTask task = new TweetSearchTask(new OnTweetSearchResultListner() {
			@Override
			public void onSuccess(List<Tweet> timetile) {
				tweetSearchSuccess(timetile);
			}

			@Override
			public void onFailed(Exception e) {
				Log.e("RouteStatusActivity", e.toString());
				// ExceptionUtil.showMessage(getApplicationContext(),
				// e.getMessage(), getClass()
				// .getName());
			}
		});
		task.execute(condition);

	}

	@Override
	protected void onDestroy() {
		if (mHandler != null && mRunnable != null) {
			mHandler.removeCallbacks(mRunnable);
		}
		super.onDestroy();
	}
}