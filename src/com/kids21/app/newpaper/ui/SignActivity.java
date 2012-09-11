/** 
 * @author pengluzhong
 * E-mail: penghaozhong@163.com
 * @version 创建时间：7 Aug 2012 15:56:09 
 * 类说明 
 */

package com.kids21.app.newpaper.ui;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.kids21.app.newpaper.AppContext;
import com.kids21.app.newpaper.http.HttpClient;
import com.kids21.app.newpaper.widget.OverItemT;
import com.kids21.app.newspaper.R;

public class SignActivity extends com.baidu.mapapi.MapActivity {

	private LocationClient mLocationClient = null;
	private BMapManager mBMapMan = null;
	private MKLocationManager mLocationManager = null;
	private MapView mMapView = null;
	private Message msg = null;
	private GeoPoint point = null;
	private MapController mMapController = null;
	private LocationClientOption option = null;
	public static String BAITU_KEY = "5FB2ADB0EBE03CB6F039BC4B709955DE3D542953";
	// 定位成都中心
	private static final int latitudeE6 = (int) (30.660036 * 1000000);
	private static final int longitudeE6 = (int) (104.06526 * 1000000);

	// 按钮
	private Button bnSubmit = null;
	private Button bnBack = null;
	private EditText currentlyAdd=null;
	
	// 表单选项
	
	private  int lotitude ;
	private int  longitude;
	private String currentlyName=null;
	private String address=null;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		TextView textview = (TextView) findViewById(R.id.title_text);
		textview.setText(R.string.sign_title);
		textview.setVisibility(View.VISIBLE);


		// 初始化地图
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init(BAITU_KEY, null);
		super.initMapActivity(mBMapMan);
		mMapView = (MapView) findViewById(R.id.bmapsView);

		mMapController = mMapView.getController();
		point = new GeoPoint(latitudeE6, longitudeE6);
		mMapController.setCenter(point); // 设置地图中心点
		mMapController.setZoom(20);

		// 启用线程查找当前位置
		new Thread() {
			@Override
			public void run() {
				msg = handler.obtainMessage();
				msg.arg1 = 1;
				handler.sendMessage(msg);

			};
		}.start();

		// 返回事件
		bnBack = (Button) findViewById(R.id.title_left);
		bnBack.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// 提交事件
		bnSubmit = (Button) findViewById(R.id.title_right);
		bnSubmit.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				 currentlyName=((EditText)findViewById(R.id.curryName)).getText().toString();
				if(currentlyName ==null || currentlyName.equals("")){
					Toast.makeText(SignActivity.this, R.string.nearby_label_wn_name, Toast.LENGTH_SHORT).show();
					return ;
				}
				 address=currentlyAdd.getText().toString();
				if(address ==null || address.equals("")){
					Toast.makeText(SignActivity.this, R.string.nearby_label_wn_address, Toast.LENGTH_SHORT).show();
					return ;
				}
				// 提交表单数据  http://192.168.1.23:8888/phpcms/index.php?m=location&c=loandge 
				
					new Thread() {
						@Override
						public void run() {
							
								double  la=Math.round(lotitude)/1000000.0;
								try {
									AppContext.http.httpRequest(HttpClient.baseURL +"index.php?m=location&c=loandge", AppContext.http.createParams(new BasicNameValuePair("name", currentlyName),
											   new BasicNameValuePair("address", address), new BasicNameValuePair("longitude",  longitude / 1E6+""), new BasicNameValuePair("latitude", lotitude / 1E6 +"")), false, HttpPost.METHOD_NAME);
								} catch (com.kids21.app.newpaper.http.HttpException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							
					
							
						};
					}.start();
					Toast.makeText(getApplicationContext(), R.string.sign_sucess, Toast.LENGTH_SHORT).show();
					finish();
				
				
			}
		});

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				if (msg.arg1 == 1) {
					mLocationClient = new LocationClient(SignActivity.this);
					option = new LocationClientOption();
					option.setOpenGps(true); // 打开gps
					option.setCoorType("bd09ll"); // 设置坐标类型为bd09ll
					option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
					option.setProdName("YANGGRIL"); // 设置产品线名称
					//option.setScanSpan(5000); // 定时定位，每隔5秒钟定位一次。
					mLocationClient.setLocOption(option);
					mLocationClient
							.registerLocationListener(new BDLocationListener() {
								@Override
								public void onReceiveLocation(
										BDLocation location) {
									if (location == null) {
										Toast.makeText(SignActivity.this,
												"还没获取到您的位置！请稍候在试",
												Toast.LENGTH_LONG).show();
										return;
									}
									mMapController = mMapView.getController(); // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
									 lotitude = (int) (location
											.getLatitude() * 1E6);
									 longitude = (int) (location
											.getLongitude() * 1E6);
									point = new GeoPoint(lotitude, longitude);
									mMapController.setCenter(point); // 设置地图中心点
									Drawable marker = getResources()
											.getDrawable(
													R.drawable.default_cat_icon); // 得到需要标在地图上的资源
									mMapView.getOverlays().add(
											new OverItemT(marker,
													SignActivity.this,
													lotitude, longitude)); // 添加ItemizedOverlay实例到mMapView
									
									// 设置当前地址
									currentlyAdd=(EditText)findViewById(R.id.currently_add);
								
									// 四川省成都市青羊区东城根上街78号  截取到区位置
									if(location.getAddrStr()!=null){
										currentlyAdd.setText(location.getAddrStr().substring(6));
									}
								
								}

								public void onReceivePoi(BDLocation location) {
									// return ;
								}
							});

					mLocationClient.start();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		};
	};

	@Override
	protected void onDestroy() {
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
			mLocationClient = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (mBMapMan != null) {
			mBMapMan.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mBMapMan != null) {
			mBMapMan.start();
		}
		super.onResume();
	}
}
