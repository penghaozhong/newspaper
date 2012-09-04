package com.kids21.app.newpaper.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.kids21.app.newpaper.widget.MyImageView;
import com.kids21.app.newspaper.R;

public class TestRolateAnimActivity extends Activity {
    /** Called when the activity is first created. */
	MyImageView joke;
	MyImageView nearBy;
	MyImageView user;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 签到
        joke=(MyImageView) findViewById(R.id.imageview_sign);
        joke.setOnClickIntent(new MyImageView.OnViewClick() {
			@Override
			public void onClick() {
				Intent intent=new Intent(TestRolateAnimActivity.this, SignActivity.class);
				startActivity(intent);
				
			}
		});
        
        // 签到
        nearBy=(MyImageView) findViewById(R.id.c_nearBy);
        nearBy.setOnClickIntent(new MyImageView.OnViewClick() {
			@Override
			public void onClick() {
				Intent intent=new Intent(TestRolateAnimActivity.this, NearByActivity.class);
				startActivity(intent);
				
			}
		});
     // 个人资料
        user=(MyImageView) findViewById(R.id.c_user);
        user.setOnClickIntent(new MyImageView.OnViewClick() {
			@Override
			public void onClick() {
				Intent intent=new Intent(TestRolateAnimActivity.this, UserActivity.class);
				startActivity(intent);
				
			}
		});
        
    }
}