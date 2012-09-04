/** 
 * @author pengluzhong
 * E-mail: penghaozhong@163.com
 * @version 创建时间：13 Aug 2012 13:52:47 
 * 类说明 
 */

package com.kids21.app.newpaper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kids21.app.newspaper.R;

public class ItemView extends LinearLayout {

	private ImageView a;
	private ImageView b;
	private ImageView c;
	private TextView d;
	private TextView e;
	private TextView f;
	private TextView g;
	private View i;
	private View j;
	private View k;
	private View l;
	private View.OnClickListener m;

	public ItemView(Context context) {
		super(context);
		a(context);
	}

	public ItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		a(context);
		TypedArray localTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.myView);
		String title = localTypedArray.getString(R.styleable.myView_item_title);
		Drawable icon = localTypedArray.getDrawable(R.styleable.myView_item_icon);
		
		if(icon!=null){
			// 设置icon
			setIamge(icon,R.id.icon);
		}
		
		Drawable image = localTypedArray.getDrawable(R.styleable.myView_item_image);
		
		if(image!=null){
			setIamge(image,R.id.image);
		}
		
		//int location = localTypedArray.getInt(R.styleable.myView_position, 1);
		// a(location);
		if (title != null) {
			TextView txtitle = (TextView) findViewById(R.id.title);
			txtitle.setText(title);
			txtitle.setVisibility(VISIBLE);
		}
		
		localTypedArray.recycle();

	}

	private void a(Context paramContext) {
		((LayoutInflater) paramContext.getSystemService("layout_inflater"))
				.inflate(R.layout.widget_item_view, this);
		this.k = findViewById(R.id.layout_itemView);
		this.k.setFocusable(true);
		this.k.setFocusableInTouchMode(true);

	}

	public void a(int paramInt) {

		int i1 = toosa(getContext(), 10);
		int i2 = toosa(getContext(), 15);
		int i3 = toosa(getContext(), 13);
		switch (paramInt) {
		default:
		case 0:
			this.k.setBackgroundResource(2130837538);

			this.k.setPadding(i1, i2, i1, i2);
			break;
		case 1:

			this.k.setBackgroundResource(R.drawable.common_bg_item_t);

			this.k.setPadding(i1, i1, i1, i3);
			break;
		case 2:
			this.k.setBackgroundResource(R.drawable.common_bg_item_m);
			this.k.setPadding(i1, i1, i1, i1);

			break;
		case 3:

			this.k.setBackgroundResource(R.drawable.common_bg_item_b);
			this.k.setPadding(i1, i1, i1, i1);
			break;
		}

	}

	public void setIamge(Drawable paramDrawable,int id) {
		if (paramDrawable == null)
			return;
		if (this.a == null) {
			this.a = ((ImageView) findViewById(id));
			this.a.setVisibility(0);
		}
		this.a.setImageDrawable(paramDrawable);
	}
	
	
	 public  int toosa(Context paramContext, int paramInt)
	  {
	    return (int)(0.5F + paramContext.getResources().getDisplayMetrics().density * paramInt);
	  }

}
