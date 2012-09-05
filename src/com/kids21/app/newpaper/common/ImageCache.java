package com.kids21.app.newpaper.common;

import android.graphics.Bitmap;

import com.kids21.app.newpaper.AppContext;
import com.kids21.app.newspaper.R;

public interface ImageCache {
	public static Bitmap mDefaultBitmap = ImageManager
			.drawableToBitmap(AppContext.mContext.getResources()
					.getDrawable(R.drawable.common_icon_empty_user));

	public Bitmap get(String url);

	public void put(String url, Bitmap bitmap);
}
