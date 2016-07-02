package org.t_robop.nakasatomi.ntwi;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.FloatRange;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetViewFetchAdapter;

import java.util.List;

/**
 * Created by iris on 2016/06/26.
 */
public class TimelineActivity extends ListActivity {

    //swipe to refreshの画面
    private SwipeRefreshLayout mSwipeRefreshLayout;


    //表示ツイート数の設定
    final int TWEET_NUM = 100;
    TwitterApiClient twitterApiClient;

    //アダプターの設定
    final TweetViewFetchAdapter adapter = new TweetViewFetchAdapter<CompactTweetView>(TimelineActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


        //swipe to refreshの実装
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        // mSwipeRefreshLayout.setColorScheme(R.color.red, R.color.green, R.color.blue, R.color.yellow);

        //アダプターをセット
        setListAdapter(adapter);
        //データを読み込む
        twitterApiClient = TwitterCore.getInstance().getApiClient();
        //自分のタイムラインを表示するとき
        homeTimeline();

        //タグ検索したデータを表示するとき
        //search();

    }

    //自分のタイムラインを表示するメソッド
    void homeTimeline() {
        // statusAPI用のserviceクラス
        StatusesService statusesService = twitterApiClient.getStatusesService();
        //ログインユーザーのタイムラインを表示する
        statusesService.homeTimeline(TWEET_NUM, null, null, false, false, false, false,
                new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {
                        Log.d("aaa", String.valueOf(listResult));
                        adapter.setTweets(listResult.data);
                    }

                    @Override
                    public void failure(TwitterException e) {
                    }
                });
    }

    void search() {
        //検索で使う
        SearchService searchService = twitterApiClient.getSearchService();
        //指定したワードを使った検索
        /*
        データ形式
        String 検索したい文字列, GeoCode 緯度経度?,String 検索したい文字列1,String 検索したい文字列2,String 検索したい文字列3,
        Integer 取得するツイート数,String 検索したい文字列4,Long 検索したいLong?時間?,Long 検索したいLong1?時間?Boolean 謎,Callback 返り値
        */
        searchService.tweets("東海大学", null, null, null, null, TWEET_NUM, null, null, null, false, new Callback<Search>() {
            //成功した時
            @Override
            public void success(Result<Search> listResult) {
                Log.d("aa", String.valueOf(listResult.data.tweets));
                if (listResult.data.tweets.equals("[]")) {
                    toastMake("データがありません", 0, -200);
                    Log.d("aa", String.valueOf(listResult.data.tweets));
                }
                adapter.setTweets(listResult.data.tweets);

            }

            @Override
            public void failure(TwitterException e) {
            }
        });
    }


    //SwipeToRefresh
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            // 1秒待機
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);

            homeTimeline();
        }
    };


    //Toastを出力させるメソッド
    private void toastMake(String message, int x, int y) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER, x, y);
        toast.show();
    }
}
