package jp.classmethod.routestatus.osaka.logic;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * タイムラインを取得するロジッククラスです。
 * 
 * @author fukasawa.takeshi
 * 
 */
public class TimelineLogic {

	private Twitter twitter = new TwitterFactory().getInstance();

	public List<Tweet> search(String searchWord, long sinceId, int rpp) throws TwitterException {
		Query query = new Query(searchWord);
		query.setResultType(Query.RECENT);
		query.setPage(1);
		query.setRpp(rpp);
		query.setSinceId(sinceId);
		QueryResult result = null;
		result = twitter.search(query);

		return result.getTweets();
	}
}
