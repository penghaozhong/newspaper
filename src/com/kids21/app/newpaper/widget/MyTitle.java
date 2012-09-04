
/** 
* @author pengluzhong
* E-mail: penghaozhong@163.com
* @version 创建时间：8 Aug 2012 15:45:52 
* 类说明 
*/ 

package com.kids21.app.newpaper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.kids21.app.newspaper.R;

public class MyTitle extends LinearLayout {

	private  	 ImageButton ib=null;
	private  	 ImageButton submit=null;
	
	
	public MyTitle(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public MyTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
	
		LinearLayout title=	(LinearLayout)LayoutInflater.from(context).inflate(R.layout.activity_title, this, true);
		 TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.myView);//TypedArray是一个数组容器 
	    // 返回按钮
		   ib=(ImageButton)title.findViewById( R.id.title_img_left);
		 Drawable back=a.getDrawable(R.styleable.myView_left_icon);
		 ib.setBackgroundDrawable(back);
		 ib.setVisibility(VISIBLE);
		 
			// 提交按钮
			 submit = (ImageButton) findViewById(R.id.title_img_right);
			submit.setBackgroundResource(R.drawable.common_icon_confirm);
			submit.setVisibility(View.VISIBLE);
		 
		 
	}

	
	

}
