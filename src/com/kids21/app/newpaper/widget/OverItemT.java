
/** 
* @author pengluzhong
* E-mail: penghaozhong@163.com
* @version 创建时间：2 Aug 2012 14:17:22 
* 类说明 
*/ 

package com.kids21.app.newpaper.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.OverlayItem;

public class OverItemT extends ItemizedOverlay<OverlayItem> {
    private List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
    private Context mContext;
 
    
 
    public OverItemT(Drawable marker, Context context,int logitude,int longitude) {
        super(boundCenterBottom(marker));
 
        this.mContext = context;
 
        // 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
        GeoPoint p1 = new GeoPoint(logitude,longitude);
        GeoList.add(new OverlayItem(p1, "Location", "I'm here"));	
        populate();  //createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
    }
 
    @Override
    protected OverlayItem createItem(int i) {
        return GeoList.get(i);
    }
 
    @Override
    public int size() {
        return GeoList.size();
    }
 
    @Override
    // 处理当点击事件
    protected boolean onTap(int i) {
        Toast.makeText(this.mContext, GeoList.get(i).getSnippet(),
                Toast.LENGTH_SHORT).show();
        return true;
    }
}
