package com.kids21.app.newpaper.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kids21.app.newpaper.AppContext;
import com.kids21.app.newpaper.common.FileHelper;
import com.kids21.app.newpaper.common.ImageManager;
import com.kids21.app.newpaper.http.HttpClient;
import com.kids21.app.newpaper.http.HttpException;
import com.kids21.app.newpaper.http.Response;
import com.kids21.app.newpaper.http.ResponseException;
import com.kids21.app.newpaper.task.GenericTask;
import com.kids21.app.newpaper.task.TaskAdapter;
import com.kids21.app.newpaper.task.TaskListener;
import com.kids21.app.newpaper.task.TaskParams;
import com.kids21.app.newpaper.task.TaskResult;
import com.kids21.app.newpaper.widget.ItemView;
import com.kids21.app.newspaper.R;

public class UserActivity extends Activity {
	private Button bnBack = null;
	private ItemView avatar = null;
	private BroadcastReceiver setTakePhotoreceiver;
	IntentFilter takePhotoFilter;

	public static String AVATAR_PIC = "avatar_pic";
	private static final String TAG = "UserActivity";

	// 拍照

	// Picture
	private boolean withPic = false;
	private File mFile;
	private ImageView mPreview;
	private ImageView imageDelete;
	private static final int MAX_BITMAP_SIZE = 400;
	private File mImageFile;
	private Uri mImageUri;
	static final private int USER_TAKE_PHOTO = 0x0001;
	static final private int USER_AUBLUM = 0x0002;
	private static final int REQUEST_IMAGE_CAPTURE = 2;
	private static final int REQUEST_PHOTO_LIBRARY = 3;
	
	private ProgressDialog dialog;

	// Task
	private GenericTask mSendTask;

	private TaskListener mSendTaskListener = new TaskAdapter() {
		@Override
		public void onPreExecute(GenericTask task) {
			onSendBegin();
		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			
			if (result == TaskResult.OK) {
				onSendResult(R.string.page_status_update_success);
			} else if (result == TaskResult.IO_ERROR) {
				onSendResult(R.string.page_status_unable_to_update);
			}
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "SendTask";
		}
	};
	
	
	private void onSendBegin() {
	
		dialog = ProgressDialog.show(UserActivity.this, "",
				getString(R.string.page_status_updating), true);
		if (dialog != null) {
			dialog.setCancelable(false);
		}
		
	}

	/**
	 * 发送后的结果
	 * 成功：R.string.page_status_update_success
	 * 失败：R.string.page_status_unable_to_update
	 * @param id
	 */
	private void onSendResult(int id) {
		if (dialog != null) {
			dialog.setMessage(getString(id));
			dialog.dismiss();
		}
		
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);

		// 标题
		TextView textview = (TextView) findViewById(R.id.title_text);
		textview.setText(R.string.user_label_title);
		textview.setVisibility(View.VISIBLE);

		// 返回事件
		bnBack = (Button) findViewById(R.id.title_left);
		bnBack.setOnClickListener(new ImageButton.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

		// 修改头像
		avatar = (ItemView) findViewById(R.id.user_avatar);
		registerForContextMenu(avatar);
		avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 调用concentMenu
				openContextMenu(v);
			}
		});
		
		mPreview=(ImageView)findViewById(R.id.image);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case USER_TAKE_PHOTO:
			openImageCaptureMenu();
			return true;
		case USER_AUBLUM:
			openPhotoLibraryMenu();
			return true;

		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderIcon(android.R.drawable.ic_menu_more);
		menu.setHeaderTitle(getString(R.string.user_avatar_title));

		menu.add(0, USER_TAKE_PHOTO, 0, R.string.user_avatar_take);
		menu.add(0, USER_AUBLUM, 0, R.string.user_avatar_album);
	}

	// 上传图片广播
	public void initTakePhotoBroadCast() {
		setTakePhotoreceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// 图片更新
				Bitmap bitmap = (Bitmap) intent.getParcelableExtra(AVATAR_PIC);
				// 将图片显示在ImageView里
				if (bitmap != null) {
					((ImageView) findViewById(R.id.image))
							.setImageBitmap(bitmap);

				} else {
					// 图片错误
					((TextView) findViewById(R.id.desc))
							.setText(R.string.page_status_error);

				}

			}
		};
		takePhotoFilter = new IntentFilter("intentToTakePhoto");
		registerReceiver(setTakePhotoreceiver, takePhotoFilter);
	}

	private String _getPhotoFilename(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddKms");
		return dateFormat.format(date) + ".jpg";
	}

	// sub menu
	protected void openImageCaptureMenu() {
		try {
			// TODO: API < 1.6, images size too small
			String filename = _getPhotoFilename(new Date());
			Log.d(TAG, "Photo filename=" + filename);
			mImageFile = new File(FileHelper.getBasePath(), filename);
			mImageUri = Uri.fromFile(mImageFile);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
			startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	protected void openPhotoLibraryMenu() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, REQUEST_PHOTO_LIBRARY);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Intent intent = UserActivity.createImageIntent(this, mImageUri);

			// 照相完后不重新起一个WriteActivity
			getPic(intent, mImageUri);
			/*
			 * intent.setClass(this, WriteActivity.class);
			 * 
			 * startActivity(intent);
			 * 
			 * // 打开发送图片界面后将自身关闭 finish();
			 */
		} else if (requestCode == REQUEST_PHOTO_LIBRARY
				&& resultCode == RESULT_OK) {
			mImageUri = data.getData();

			Intent intent = UserActivity.createImageIntent(this, mImageUri);

			// 选图片后不重新起一个WriteActivity
			getPic(intent, mImageUri);
			/*
			 * intent.setClass(this, WriteActivity.class);
			 * 
			 * startActivity(intent);
			 * 
			 * // 打开发送图片界面后将自身关闭 finish();
			 */
		}
	}

	public static Intent createImageIntent(Activity activity, Uri uri) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		try {
			UserActivity writeActivity = (UserActivity) activity;
		} catch (ClassCastException e) {
			// do nothing
		}
		return intent;

	}

	private void getPic(Intent intent, Uri uri) {

		withPic = true;
		mFile = null;

		mImageUri = uri;
		if (uri.getScheme().equals("content")) {
			mFile = new File(getRealPathFromURI(mImageUri));
		} else {
			mFile = new File(mImageUri.getPath());
		}
		doSend();
		
		// TODO:想将图片放在EditText左边
		mPreview.setImageBitmap(createThumbnailBitmap(mImageUri,
				MAX_BITMAP_SIZE));

		if (mFile == null) {

		}

	}

	private String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaColumns.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/**
	 * 制作微缩图
	 * 
	 * @param uri
	 * @param size
	 * @return
	 */
	private Bitmap createThumbnailBitmap(Uri uri, int size) {
		InputStream input = null;

		try {
			input = getContentResolver().openInputStream(uri);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(input, null, options);
			input.close();

			// Compute the scale.
			int scale = 1;
			while ((options.outWidth / scale > size)
					|| (options.outHeight / scale > size)) {
				scale *= 2;
			}

			options.inJustDecodeBounds = false;
			options.inSampleSize = scale;

			input = getContentResolver().openInputStream(uri);

			return BitmapFactory.decodeStream(input, null, options);
		} catch (IOException e) {
			Log.w(TAG, e);

			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					Log.w(TAG, e);
				}
			}
		}
	}
	
	private void doSend() {
		Log.d(TAG, "dosend  " + withPic);
		

		if (mSendTask != null
				&& mSendTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
		//	String status = mTweetEdit.getText().toString();

			
				int mode=SendTask.TYPE_NORMAL;
				if (withPic) {
					mode = SendTask.TYPE_PHOTO;
				} 

				mSendTask = new SendTask();
				mSendTask.setListener(mSendTaskListener);

				TaskParams params = new TaskParams();
				params.put("mode", mode);
				mSendTask.execute(params);
			
		}
	}
	
	/*
	 * 上传图片方法
	 * post 方式参数参考
	 * http.httpRequest(
	   baseURL + "index.php?m=upfile&c=up",
	   createParams(new BasicNameValuePair("status", status),
	   new BasicNameValuePair("source", source)), mFile, true,
	   HttpPost.METHOD_NAME);
	 */
	private class SendTask extends GenericTask {

		public static final int TYPE_NORMAL = 0;
		public static final int TYPE_REPLY = 1;
		public static final int TYPE_REPOST = 2;
		public static final int TYPE_PHOTO = 3;

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			TaskParams param = params[0];
			try {
				int mode = param.getInt("mode");

				// Send status in different way
				switch (mode) {

				case TYPE_REPLY:
					break;

				case TYPE_REPOST:
					break;

				case TYPE_PHOTO:
					if (null != mFile) {
						// Compress image
						try {
							mFile = getImageManager().compressImage(mFile, 100);
							// ImageManager.DEFAULT_COMPRESS_QUALITY);
						} catch (IOException ioe) {
							Log.e(TAG, "Cann't compress images.");
						}
						
						
						
								Response ress = AppContext.http.httpRequest(
								HttpClient.baseURL + "index.php?m=upfile&c=up",null, mFile, true,
								HttpPost.METHOD_NAME);
								
								// 解析json 显示结果
								if(showResult(ress)==1){
									return TaskResult.OK;
								}
								
					} else {
						Log.e(TAG,
								"Cann't send status in PICTURE mode, photo is null");
					}
					break;

				case TYPE_NORMAL:
				default:
					break;
				}
			} catch (HttpException e) {
				Log.e(TAG, e.getMessage(), e);

				if (e.getStatusCode() == HttpClient.NOT_AUTHORIZED) {
					return TaskResult.AUTH_ERROR;
				}
				return TaskResult.IO_ERROR;
			}

			return TaskResult.FAILED;
		}

		private ImageManager getImageManager() {
			return AppContext.mImageManager;
		}
	}
	
	/**
	 * 生成POST Parameters助手
	 * 
	 * @param nameValuePair
	 *            参数(一个或多个)
	 * @return post parameters
	 */
	public ArrayList<BasicNameValuePair> createParams(
			BasicNameValuePair... nameValuePair) {
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		for (BasicNameValuePair param : nameValuePair) {
			params.add(param);
		}
		return params;
	}
	
	/**
	 * 返回结果
	 * @param res
	 * @return 1：成功  0：失败
	 */
	public int showResult(Response res){
		int re=0;
		if(res!=null){
			try {
				JSONObject json=res.asJSONObject();
				int resu=json.getInt("message");
				if(resu==1){
					// sucuss
					re=1;
				}else{
					//fail
				}
			} catch (ResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return re;
	}
	
	
}
