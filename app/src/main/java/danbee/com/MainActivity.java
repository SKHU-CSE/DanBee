package danbee.com;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.security.MessageDigest;

import danbee.com.DbHelper.AutoLoginDbHelper;
import danbee.com.service.ShowNotificationService;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {
    Marker marker;
    NaverMap mnaverMap;
    LatLng mCurPos;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    BoomMenuButton boombt;
    boolean startPos = true;

    CardView batteryCard;

    //    private SensorManager mSensorManager;
//    private Sensor mAccelerometer;
//    private Sensor mMagnetometer;
//    private float[] mLastAccelerometer = new float[3];
//    private float[] mLastMagnetometer = new float[3];
//    private boolean mLastAccelerometerSet = false;
//    private boolean mLastMagnetometerSet = false;
//    private float[] mR = new float[9];
//    private float[] mOrientation = new float[3];
//    private float mCurrentDegree = 0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        batteryCard = findViewById(R.id.main_battery_cardview);
        //kakao 해시키가져옴
        getAppKeyHash();
        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());

        //자동로그인확인
        autoLogin();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //만든툴바 액션바로 설정

        boomMenuSet();
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

//        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //현위치 다시찾기
        findViewById(R.id.main_gps_imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurPos != null){
                    mnaverMap.setCameraPosition(new CameraPosition(mCurPos, 17));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //로그인상태와 로그아웃상태 변경
        loginButtonChange();

        //센서 등록
//        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        mSensorManager.registerListener( this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);


        if(UserInfo.info.getKickid() != -1){
            batteryCard.setVisibility(View.VISIBLE);
            /*
            킥보드 배터리 양체크 통신, 반납하기버튼
             */
        }else{
            batteryCard.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //센서반납
//        mSensorManager.unregisterListener( this, mAccelerometer);
//        mSensorManager.unregisterListener( this, mMagnetometer);
    }

    //방위각 체크하기위한 센서
    @Override
    public void onSensorChanged(SensorEvent event) {
//        if(event.sensor == mAccelerometer){
//            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
//            mLastAccelerometerSet = true;
//        }else if(event.sensor == mMagnetometer){
//            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
//            mLastMagnetometerSet = true;
//        }
//        if(mLastAccelerometerSet && mLastMagnetometerSet){
//            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
//            float azimuthinDegress = (int) (Math.toDegrees(SensorManager.getOrientation(mR, mOrientation)[0])+360)%360;
//            mCurrentDegree = azimuthinDegress;
//            if (mnaverMap != null){
//                mnaverMap.getLocationOverlay().setBearing(mCurrentDegree);
//            }
//        }
    }

    //센서 인터페이스 오버라이드해줘야함
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }




    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        mnaverMap = naverMap;
        naverMap.setLocationSource(locationSource); //현위치
        naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);

        // 위치(위도,경도) 객체
        LatLng maker_location = new LatLng(37.487936, 126.825071);

        //위치변경시 콜백
        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {
                if (naverMap == null || location == null) {
                    return;
                }
                mCurPos = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition(mCurPos, 17);

                //현위치 최초설정
                if (startPos == true) {
                    naverMap.setCameraPosition(cameraPosition);
                    startPos = !startPos;
                }
            }
        });



        marker = new Marker(); //마커 생성
        marker.setIcon(OverlayImage.fromResource(R.drawable.danbeemarker)); //아이콘 변경
        marker.setWidth(150);
        marker.setHeight(150);
        marker.setPosition(maker_location); //마커 위치설정
        marker.setMap(naverMap); //마커 표시 (보여짐)


        //마커누를때 뜨는정보창
        final InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return "정보 창 내용";
            }
        });


        marker.setOnClickListener(new Overlay.OnClickListener() {   //지도에서 마커 클릭했을때 이벤트
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                infoWindow.open(marker); //정보창 내용과 마커 연결
                return false;
            }
        });


//
//        // 카메라 위치와 줌 조절(숫자가 클수록 확대)
//        CameraPosition cameraPosition = new CameraPosition(location, 17);
//        naverMap.setCameraPosition(cameraPosition);

        // 줌 범위 제한
        naverMap.setMinZoom(5.0);   //최소
        naverMap.setMaxZoom(18.0);  //최대

        // 카메라 영역 제한
        LatLng northWest = new LatLng(31.43, 122.37);
        LatLng southEast = new LatLng(44.35, 132);
        naverMap.setExtent(new LatLngBounds(northWest, southEast));


    }

    //버튼메뉴생성
    void boomMenuSet(){

        boombt = findViewById(R.id.bmb);

        for (int i = 0; i < boombt.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .normalImageRes(buttonImage(i))
                    .normalText(buttonText(i))
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            btClickIntent(index);
                        }
                    })
                    .normalTextColor(Color.BLACK)
                    .normalColorRes(btColorSet(i))
                    .imagePadding(new Rect(15, 10, 15, 40));

            boombt.addBuilder(builder);
        }
    }

    //버튼이미지생성
    int buttonImage(int pos){
        int res = -1;
        switch (pos){
            case 0:
                res=R.drawable.qrcode;
                break;
            case 1:
                if(UserInfo.info.isLoginState()){
                    res = R.drawable.lock;
                }else {
                    res = R.drawable.unlock;
                }
                break;
            case 2:
                res=R.drawable.profile;
                break;
            case 3:
                res=R.drawable.danbeelogo;
                break;
            case 4:
                res=R.drawable.alarm;
                break;
            case 5:
                res=R.drawable.history;
                break;
            case 6:
                res=R.drawable.guidebook;
                break;
        }
        return res;
    }

    //버튼텍스트 생성
    String buttonText(int pos){
        String title="";
        switch (pos){
            case 0:
                return title="QR코드";
            case 1:
                if( UserInfo.info.isLoginState() ) {
                    return title = "로그아웃";
                }else{
                    return title = "로그인";
                }
            case 2:
                return title="내정보";
            case 3:
                return title="";
            case 4:
                return title="공지사항/Q&A";
            case 5:
                return title="이용내역";
            case 6:
                return title="사용방법";
            default:
                return title;
        }
    }

    void btClickIntent(int index){
        Intent intent = null;
        switch (index) {
            case 0:
                if (UserInfo.info.getUserid() == null || UserInfo.info.getUserid().equals("")) { // 로그인 안되있을 때
                    loginCheckMessage();
                    return;
                }
                //qr코드 스캐너
                new IntentIntegrator(this)
                        .setPrompt("QR코드를 찍어주세요")
                        .setOrientationLocked(true)
                        .initiateScan();
                break;
            case 1:
                if (UserInfo.info.isLoginState()) { //로그아웃클릭
                    UserInfo.info.setLoginState(false);
                    UserInfo.info.setUserid("");
                    UserInfo.info.setPhone("");
                    UserInfo.info.setName("");
                    UserInfo.info.setGender(-1);
                    UserInfo.info.setBirth("");


                    //내부디비수정
                    AutoLoginDbHelper.openDatabase(this, "auto");
                    AutoLoginDbHelper.deleteLog();
                    AutoLoginDbHelper.createAutoTable();
                    AutoLoginDbHelper.insertData(0, "", "", "", 10, "");

                    AlertDialog.Builder adbuilder = new AlertDialog.Builder(this);
                    adbuilder.setTitle("로그아웃 되었습니다.")
                            .setPositiveButton("확인", null)
                            .setCancelable(false)
                            .show();
                    stopService();
                    loginButtonChange();
                    //소셜로그인 로그아웃
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {

                        }
                    });
                } else { //로그인클릭
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case 2:
                if (UserInfo.info.getUserid() == null || UserInfo.info.getUserid().equals("")) { // 로그인 안되있을 때
                    loginCheckMessage();
                    return;
                }
                intent = new Intent(this, MypageActivity.class);
                startActivity(intent);
                break;
            case 3:

                break;
            case 4:
                intent = new Intent(this, NoticeQuestionActivity.class);
                startActivity(intent);
                break;
            case 5:
                if (UserInfo.info.getUserid() == null || UserInfo.info.getUserid().equals("")) { // 로그인 안되있을 때
                    loginCheckMessage();
                    return;
                }
                intent = new Intent(this, UserHistoryActivity.class);
                startActivity(intent);
                break;
            case 6:
                intent = new Intent(this, GuideActivity.class);
                startActivity(intent);
                break;
            default:
                return;
        }
    }

    //버튼색 지정
    int btColorSet(int index){
        switch (index){
            case 0:
            case 6:
                return R.color.danbeeBomButton1;
            case 1:
            case 5:
                return R.color.danbeeBomButton2;
            case 2:
            case 4:
                return R.color.danbeeBomButton3;
            default:
                return R.color.colorPrimaryDark;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //qr코드 결과값
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                qrCodeRequest(result.getContents());
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //qr코드 통신
    void qrCodeRequest(String suburl){
        // http://3.17.25.223/api/kick/borrow/{kickid}/{userid}
        // http://3.17.25.223/api/kick/lend/{userid}
        String userid = UserInfo.info.getUserid();
        String kickid = suburl.substring(35);
        UserInfo.info.setKickid(Integer.parseInt(kickid));
        Log.d("test", "kickid: "+kickid+", "+suburl);
        String url = suburl+"/"+userid;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() { //응답 받음
                    @Override
                    public void onResponse(String response) {
                        Log.d("test", "qrcode: "+response);
                        startService(); //빌리기시작하면 배터리용량알려줌
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", "qrcode: "+error);
                    }
                }
        );
        //자동캐싱잇는경우 이전결과 그대로보여짐
        request.setShouldCache(false);  //새로요청해서 결과보여줌
        AppHelper.requestQueue.add(request);
    }

    //로그인 로그아웃 버튼상태변경
    void loginButtonChange(){

        BoomButton loginBoombt = boombt.getBoomButton(1);
        if (loginBoombt == null)
            return;

        if(UserInfo.info.isLoginState()) {
            loginBoombt.getTextView().setText("로그아웃");
            loginBoombt.getImageView().setImageResource(R.drawable.lock);
        }else{
            loginBoombt.getTextView().setText("로그인");
            loginBoombt.getImageView().setImageResource(R.drawable.unlock);
        }
    }

    //자동로그인
    void autoLogin(){
        AutoLoginDbHelper.openDatabase(this, "auto");
        AutoLoginDbHelper.createAutoTable();
        AutoLoginDbHelper.selectData();
    }

    //Service실행 - qr코드로 빌릴시
    public void startService(){
        int battery = 95;
        Intent serviceIntent = new Intent(this, ShowNotificationService.class);
        serviceIntent.putExtra("battery", battery);
        ContextCompat.startForegroundService(this, serviceIntent);

    }

    //Service중지 - 로그아웃시
    public void stopService(){
        Intent serviceIntent = new Intent(this, ShowNotificationService.class);
        stopService(serviceIntent);
    }

    //Kakao 해시키가져오기
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

    //로그인 안했을시 나타나는 다이얼로그
    public void loginCheckMessage() {
        AlertDialog.Builder adbuilder = new AlertDialog.Builder(MainActivity.this);
        adbuilder.setTitle("알림")
                .setMessage("로그인을 하시오.")
                .setCancelable(false)
                .setIcon(R.drawable.danbeelogoj)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this , LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }

    /*
    // 회원탈퇴 처리
    UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
        @Override
        public void onFailure(ErrorResult errorResult) {
            int result = errorResult.getErrorCode();

            if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "오류가 발생했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
            Toast.makeText(getApplicationContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNotSignedUp() {
            Toast.makeText(getApplicationContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(Long result) { }
    });

     */


}