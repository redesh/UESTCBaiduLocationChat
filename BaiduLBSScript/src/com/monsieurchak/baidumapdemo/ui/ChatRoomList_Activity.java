package com.monsieurchak.baidumapdemo.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Parser;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.monsieurchak.baidumapdemo.R;
import com.monsieurchak.baidumapdemo.Util.BMapUtil;
import com.monsieurchak.baidumapdemo.Util.Util;
import com.monsieurchak.baidumapdemo.bean.CONSTANT;
import com.monsieurchak.baidumapdemo.bean.RoomInfo;
import com.monsieurchak.baidumapdemo.bean.Task;
import com.monsieurchak.baidumapdemo.logic.MainService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChatRoomList_Activity extends Activity implements OnClickListener, UIActivity{

	Button joinButton,createButton;
	EditText chatRoomEditText;

	BMapManager mBMapMan = null;  
	/**
	 *  MapView 是地图主控件
	 */
	private MapView mMapView = null;
	/**
	 *  用MapController完成地图控制 
	 */
	private MapController mMapController = null;
	private MyOverlay mOverlay = null;
	private PopupOverlay   pop  = null;
	private ArrayList<OverlayItem>  mItems = null; 
	private TextView  popupText = null;
	private View viewCache = null;
	private View popupInfo = null;
	private View popupLeft = null;
	private View popupRight = null;
	private Button button = null;
	private MapView.LayoutParams layoutParam = null;

	//老隆镇
	double mLon = 115.2503479;
	double mLat = 24.09343555;

	Button requestLocButton = null;
	boolean isRequest = false;//是否手动触发请求定位
	boolean isFirstLoc = true;//是否首次定位
	boolean isLocationClientStop = false;
	locationOverlay myLocationOverlay = null;

	//LocationClient mLocClient;
	LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * 使用地图sdk前需先初始化BMapManager.
		 * BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
		 * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
		 */
		mBMapMan=new BMapManager(getApplication());  
		mBMapMan.init(CONSTANT.BAIDU_MAP_KEP, new MKGeneralListener() {

			@Override
			public void onGetPermissionState(int iError) {
				if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
					//授权Key错误：
					Toast.makeText(ChatRoomList_Activity.this, 
							"请在 CONSTANT.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onGetNetworkState(int iError) {
				if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
					Toast.makeText(ChatRoomList_Activity.this, "您的网络出错啦！",
							Toast.LENGTH_LONG).show();
				}
				else if (iError == MKEvent.ERROR_NETWORK_DATA) {
					Toast.makeText(ChatRoomList_Activity.this, "输入正确的检索条件！",
							Toast.LENGTH_LONG).show();
				}
				// ...
			}
		});

		/**
		 * 由于MapView在setContentView()中初始化,所以它需要在BMapManager初始化之后
		 */
		setContentView(R.layout.activity_chat_rooms_list);

		//地图右上角的点位按钮
		requestLocButton = (Button)findViewById(R.id.requestLocationButton);
		requestLocButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (openGPSSettings()) {

					//手动定位请求
					getLocation();
				}
			}


		});

		joinButton = (Button)findViewById(R.id.test_baidu_map_yeah);
		joinButton.setOnClickListener(this);
		createButton = (Button)findViewById(R.id.test_baidu_map_create);
		createButton.setOnClickListener(this);
		chatRoomEditText = (EditText)findViewById(R.id.test_baidu_map_tip);

		mMapView = (MapView)findViewById(R.id.bmapsView);

		//定位图层初始化
		myLocationOverlay = new locationOverlay(mMapView);
		//设置定位数据
		myLocationOverlay.setData(locData);
		//添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		//修改定位数据后刷新图层生效
		mMapView.refresh();

		/**
		 * 获取地图控制器
		 */
		mMapController = mMapView.getController();
		/**
		 *  设置地图是否响应点击事件  .
		 */
		mMapController.enableClick(true);
		/**
		 * 设置地图缩放级别
		 */
		mMapController.setZoom(14);
		/**
		 * 显示内置缩放控件
		 */
		mMapView.setBuiltInZoomControls(true);

		//		initOverlay();

		/**
		 * 设定地图中心点
		 */
		GeoPoint p = new GeoPoint((int)(mLat * 1E6), (int)(mLon* 1E6));
		mMapController.setCenter(p);
	}

	//	/**
	//	 * 手动触发一次定位请求
	//	 */
	//	public void requestLocClick(){
	//		isRequest = true;
	//		mLocClient.requestLocation();
	//		Toast.makeText(ChatRoomList_Activity.this, "正在定位…", Toast.LENGTH_SHORT).show();
	//	}

	public void displayOverlay(RoomInfo roomInfo){

		//提取聊天室信息
		//聊天室精确地址，11位经度+11位纬度 = 22位
		final String positionString = roomInfo.getROOM_LOCATION();
		long roomLatitude = Util.getFirst(positionString);
		long roomLongitude = Util.getLast(positionString);
		//聊天室名称
		final String roomName = roomInfo.getNAME();

		/**
		 * 创建自定义overlay
		 */
		mOverlay = new MyOverlay(getResources().getDrawable(R.drawable.nav_turn_via_1),mMapView);	
		/**
		 * 准备overlay 数据
		 */
		GeoPoint p1 = new GeoPoint ((int)(roomLatitude/1E2),(int)(roomLongitude/1E2));
		OverlayItem item1 = new OverlayItem(p1,roomName,"");

		/**
		 * 将item 添加到overlay中
		 * 注意： 同一个itme只能add一次
		 */
		mOverlay.addItem(item1);

		//保存所有item，以便overlay在reset后重新添加
		mItems = new ArrayList<OverlayItem>();
		mItems.addAll(mOverlay.getAllItem());

		//将overlay 添加至MapView中
		mMapView.getOverlays().add(mOverlay);

		//刷新地图
		mMapView.refresh();

		//向地图添加自定义View,
		viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
		popupInfo = (View) viewCache.findViewById(R.id.popinfo);
		popupLeft = (View) viewCache.findViewById(R.id.popleft);
		popupRight = (View) viewCache.findViewById(R.id.popright);
		popupText =(TextView) viewCache.findViewById(R.id.textcache);

//		button = new Button(this);
//		button.setBackgroundResource(R.drawable.popup);

		/**
		 * 创建一个popupoverlay
		 */
		PopupClickListener popListener = new PopupClickListener(){

			//index的次序从左向右排列
			@Override
			public void onClickedPopup(int index) {
				if ( index == 0){
					Toast.makeText(ChatRoomList_Activity.this, "第一个按钮被按下", Toast.LENGTH_SHORT).show();
				}
				else if(index == 2){
					Toast.makeText(ChatRoomList_Activity.this, "第三个按钮被按下", Toast.LENGTH_SHORT).show();
				}
				else if (index == 1) {
					HashMap<String, Object> roomMap = new HashMap<String, Object>();
					roomMap.put(CONSTANT.ROOM_NAME, roomName);
					roomMap.put(CONSTANT.ROOM_LOCATION, positionString);
					
					//向MainService发送请求
					MainService.addTask(new Task(Task.JOIN_ROOM, roomMap));
					MainService.addActivity(ChatRoomList_Activity.this);
				}
			}
		};
		pop = new PopupOverlay(mMapView,popListener);
	}

	public class popUpClickListener implements PopupClickListener{

		String roomName;

		public popUpClickListener(String roomName) {
			this.roomName = roomName;
		}

		@Override
		public void onClickedPopup(int arg0) {

		}

	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || isLocationClientStop)
				return ;
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();

			System.out.println("location.getLatitude()"+location.getLatitude()+"location.getLongitude()"+location.getLongitude());

			//如果不显示定位精度圈，将accuracy赋值为0即可
			locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();

			//更新定位数据
			myLocationOverlay.setData(locData);

			//更新图层数据执行刷新后生效
			mMapView.refresh();

			//是手动触发请求或首次定位时，移动到定位点
			if (isRequest || isFirstLoc){

				//移动地图到定位点
				mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
				isRequest = false;
			}

			//首次定位完成
			isFirstLoc = false;
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ;
			}
		}
	}

	//继承MyLocationOverlay重写dispatchTap实现点击处理
	public class locationOverlay extends MyLocationOverlay{

		public locationOverlay(MapView mapView) {
			super(mapView);
		}
		@Override
		protected boolean dispatchTap() {
			//处理点击事件,弹出泡泡
			popupText.setBackgroundResource(R.drawable.popup);
			popupText.setText("我的位置");
			pop.showPopup(BMapUtil.getBitmapFromView(popupText),
					new GeoPoint((int)(locData.latitude*1e6), (int)(locData.longitude*1e6)),
					8);
			return true;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.test_baidu_map_yeah:	//加入指定聊天室
			HashMap<String, Object> local = new HashMap<String, Object>();
			local.put(CONSTANT.SEARCH_ROOM, getLocation());
			MainService.addTask(new Task(Task.ROOM_SEARCH, local));
			MainService.addActivity(ChatRoomList_Activity.this);
			break;

		case R.id.test_baidu_map_create:	//创建聊天室
			LocationData currentLocation = getLocation();

			//构建具体位置，用于标识一个具体的聊天室
			long specifiedLatitude = (long) (currentLocation.latitude * 1E8);
			long specifiedLongtitude = (long)(currentLocation.longitude * 1E8);
			String specfiedLatitudeString = Util.frontCompWithZore(specifiedLatitude, 11);
			String specfiedLongtitudeString = Util.frontCompWithZore(specifiedLongtitude, 11);

			//精确地址格式：经度(11位整数)+纬度(11位整数)=22位字符串
			String specifiedLocation = specfiedLatitudeString + specfiedLongtitudeString;

			//构建大概位置，用于搜索附近的聊天室
			int latitude = (int)(currentLocation.latitude * CONSTANT.ACCURACY);
			int longtitude = (int)(currentLocation.longitude * CONSTANT.ACCURACY);
			String latitudeString = Util.frontCompWithZore(latitude, 5);
			String longtitudeString = Util.frontCompWithZore(longtitude, 5);
			String roomLBS = latitudeString + longtitudeString;

			String roomName = chatRoomEditText.getText().toString();
			HashMap<String, Object> roomMap = new HashMap<String, Object>();
			roomMap.put(CONSTANT.ROOM_ID_LOCAL, roomLBS);			//聊天室大致位置，用以搜寻附近的聊天室
			roomMap.put(CONSTANT.ROOM_NAME, roomName);				//聊天室名称
			roomMap.put(CONSTANT.ROOM_LOCATION, specifiedLocation);	//聊天室具体位置

			//向MainService发送请求
			MainService.addTask(new Task(Task.CREATE_ROOM, roomMap));
			MainService.addActivity(ChatRoomList_Activity.this);
			break;
		default:
			break;
		}
	}

	@Override  
	protected void onDestroy(){  
		mMapView.destroy();  
		if(mBMapMan!=null){  
			mBMapMan.destroy();  
			mBMapMan=null;  
		}  
		super.onDestroy();  
	}  
	@Override  
	protected void onPause(){  
		mMapView.onPause();  
		if(mBMapMan!=null){  
			mBMapMan.stop();  
		}  
		super.onPause();  
	}  
	@Override  
	protected void onResume(){  
		mMapView.onResume();  
		if(mBMapMan!=null){  
			mBMapMan.start();  
		}  
		super.onResume();  
	}  

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	public class MyOverlay extends ItemizedOverlay{

		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		@Override
		public boolean onTap(int index){
			OverlayItem item = getItem(index);
			popupText.setText(getItem(index).getTitle());
			Bitmap[] bitMaps={
					BMapUtil.getBitmapFromView(popupLeft), 		
					BMapUtil.getBitmapFromView(popupInfo), 		
					BMapUtil.getBitmapFromView(popupRight) 		
			};
			pop.showPopup(bitMaps,item.getPoint(),32);
			return true;
		}

		@Override
		public boolean onTap(GeoPoint pt , MapView mMapView){
			if (pop != null){
				pop.hidePop();
				mMapView.removeView(button);
			}
			return false;
		}

	}

	@Override
	public void init() {

	}

	private boolean openGPSSettings() {
		LocationManager alm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT)
			.show();
			return true;
		}

		Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivityForResult(intent,0); //此为设置完成后返回到获取界面
		return false;
	}

	public LocationData getLocation(){
		LocationManager locationManager;
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) this.getSystemService(serviceName);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);

		String provider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(provider);
		LocationData locationData = new LocationData();
		locationData.latitude = location.getLatitude();
		locationData.longitude = location.getLongitude();
		locationData.direction = 0.0f;
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);
		myLocationOverlay.setData(locationData);

		mMapController.setZoom(13);
		mMapView.getOverlays().add(myLocationOverlay);
		mMapView.refresh();
		mMapView.getController().animateTo(new GeoPoint(
				(int)(locationData.latitude*1E6), 
				(int)(locationData.longitude*1E6)));

		return locationData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refresh(Object... params) {
		String type = (String) params[0];

		if (type.equals(CONSTANT.CREATE_ROOM)) {
			if (params[1].equals(CONSTANT.SUCCEED)) {
				Intent intent = new Intent(this,ChatRoom_Activity.class);
				intent.putExtra(CONSTANT.ROOM_NAME, (String) params[2]);
				startActivity(intent);
			}
			else if (params[1].equals(CONSTANT.FAILED)) {
				Toast.makeText(this, "创建聊天室失败", Toast.LENGTH_SHORT).show();
			}
		}
		else if (type.equals(CONSTANT.SEARCH_ROOM)) {
			if (params[1].equals(CONSTANT.SUCCEED)) {
				ArrayList<Object> roomsFound = (ArrayList<Object>)params[2];
				for (Object object : roomsFound) {
					RoomInfo roomInfo = (RoomInfo)object;
					
					//显示附近的聊天室
					displayOverlay(roomInfo);
				}
			}
			else if (params[1].equals(CONSTANT.FAILED)) {
				Toast.makeText(this, "搜索聊天室失败", Toast.LENGTH_SHORT).show();
			}
		}
		else if (type.equals(CONSTANT.JOIN_ROOM)) {
			if (params[1].equals(CONSTANT.SUCCEED)) {
				Intent intent = new Intent(this,ChatRoom_Activity.class);
				intent.putExtra("roomID", (String) params[2]);
				startActivity(intent);
			}
			else if (params[1].equals(CONSTANT.FAILED)) {
				Toast.makeText(this, "加入聊天室失败", Toast.LENGTH_SHORT).show();
			}
		}

	}

}
