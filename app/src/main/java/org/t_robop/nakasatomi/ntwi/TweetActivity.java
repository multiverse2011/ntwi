package org.t_robop.nakasatomi.ntwi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

public class TweetActivity extends AppCompatActivity {

    //投稿用のtextView
    EditText TwitterText;
    //Tweetを入れる用
    String TweetStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        //Tweetを入力するeditTextの関連付け
        TwitterText = (EditText)findViewById(R.id.editText);
    }

    //ツイートボタンをおした時の処理
    public void Tweet(View view) {
        TweetStr = String.valueOf(TwitterText.getText());
        StatusesService statusesService = TwitterCore.getInstance().getApiClient().getStatusesService();
        statusesService.update(TweetStr, null, null, null, null, null, null, null, new Callback<Tweet>() {

            //成功した場合
            @Override
            public void success(Result<Tweet> tweetResult) {
                Log.d("ツイート完了","");
                Toast.makeText(getApplicationContext(), "ツイートが完了しました", Toast.LENGTH_LONG).show();

                finish();
            }

            //失敗した場合
            @Override
            public void failure(TwitterException e) {
                Log.d("ツイート失敗","");
                Toast.makeText(getApplicationContext(), "できてない", Toast.LENGTH_LONG).show();
            }
        });
    }


}