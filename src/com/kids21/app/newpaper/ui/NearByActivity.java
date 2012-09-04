
/** 
* @author pengluzhong
* E-mail: penghaozhong@163.com
* @version 创建时间：10 Aug 2012 16:21:40 
* 类说明 
*/ 

package com.kids21.app.newpaper.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kids21.app.newspaper.R;

public class NearByActivity extends Activity {
	private Button bnBack = null;
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_nearby);
	        
	        // 标题
	    	TextView textview = (TextView) findViewById(R.id.title_text);
			textview.setText(R.string.nearby_title);
			textview.setVisibility(View.VISIBLE);
			
			// 返回事件
			bnBack = (Button) findViewById(R.id.title_left);
			bnBack.setOnClickListener(new ImageButton.OnClickListener() {

				
				public void onClick(View v) {
					finish();
				}
			});
	        		
	   }
}
