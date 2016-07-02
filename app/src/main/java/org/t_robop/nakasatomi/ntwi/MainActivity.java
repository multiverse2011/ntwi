package org.t_robop.nakasatomi.ntwi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "bqKbRyAzgnxDQqBBWETMFjhhc";
    private static final String TWITTER_SECRET = "wR2wywCURF4XlPYcF1MgWq8rk387XtUz0rKuyAb8M7JVEV4kgg";


    private TwitterLoginButton loginButton;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);



        //ログインボタンの実装
        //Twitterボタンの関連付け
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        //押したあと
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;

                String msg = "@" + session.getUserName() + " logged in (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                saveSettings(1);

            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    //Tweet画面にIntentする

    public void TweetBtn(View view) {
        Intent intent = new Intent();
        intent.setClass(this, TweetActivity.class);
        startActivity(intent);
    }

    //タイムライン表示画面にIntentする
    public void TimelineBtn(View view) {
        Intent intent = new Intent();
        intent.setClass(this, TimelineActivity.class);
        startActivity(intent);
    }

    //設定値を保存しておくプリファレンス
    public void saveSettings(int login){
        // プリファレンスの準備 //
        SharedPreferences pref =
                getSharedPreferences( "settings", Context.MODE_PRIVATE );

        // プリファレンスに書き込むためのEditorオブジェクト取得 //
        SharedPreferences.Editor editor = pref.edit();

        // "login_status" というキーで名前を登録
        editor.putInt( "login_status", login );

        // 書き込みの確定（実際にファイルに書き込む）
        editor.commit();
    }
}
