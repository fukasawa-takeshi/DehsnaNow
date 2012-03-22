package jp.classmethod.routestatus.osaka.task;

import java.util.List;

import jp.classmethod.routestatus.osaka.logic.TimelineLogic;
import jp.classmethod.routestatus.osaka.task.TweetSearchTask.TweetSearchCondition;
import jp.classmethod.routestatus.osaka.task.TweetSearchTask.TweetSearchResult;
import twitter4j.Tweet;
import twitter4j.TwitterException;
import android.os.AsyncTask;

public class TweetSearchTask extends AsyncTask<TweetSearchCondition, Void, TweetSearchResult> {

	private TimelineLogic timelineLogic = new TimelineLogic();

	private OnTweetSearchResultListner mOnTimelineResult;

	public TweetSearchTask(OnTweetSearchResultListner timelineResult) {
		mOnTimelineResult = timelineResult;
	}

	@Override
	protected TweetSearchResult doInBackground(TweetSearchCondition... params) {
		TweetSearchCondition condition = params[0];
		TweetSearchResult result = new TweetSearchResult();
		try {
			result.timeline = timelineLogic.search(condition.searchWords, condition.sinceId,
					condition.maxNum);
		} catch (TwitterException e) {
			result.exception = e;
		}
		return result;
	}

	@Override
	protected void onPostExecute(TweetSearchResult result) {
		if (result.exception == null) {
			mOnTimelineResult.onSuccess(result.timeline);
		} else {
			mOnTimelineResult.onFailed(result.exception);
		}
	}

	public interface OnTweetSearchResultListner {
		void onSuccess(List<Tweet> timeline);

		void onFailed(Exception e);
	}

	public static class TweetSearchCondition {
		public String searchWords;
		public long sinceId;
		public int maxNum;
	}

	public static class TweetSearchResult {
		public List<Tweet> timeline;
		public Exception exception;
	}

}
