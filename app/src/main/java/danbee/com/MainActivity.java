package danbee.com;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    Marker marker;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;


    boolean startPos = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
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
        naverMap.setLocationSource(locationSource); //현위치
        naverMap.setLocationTrackingMode(LocationTrackingMode.Face);



        // 위치(위도,경도) 객체
        LatLng location = new LatLng(37.487936, 126.825071);

        //위치변경시 콜백
        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {
                if (naverMap == null || location == null) {
                    return;
                }

                CameraPosition cameraPosition = new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), 17);
                LocationOverlay locationOverlay = naverMap.getLocationOverlay();
                locationOverlay.setBearing(location.getBearing());


                //현위치 최초설정
                if (startPos) {
                    naverMap.setCameraPosition(cameraPosition);
                    startPos = !startPos;
                }
            }
        });


        UiSettings uiSettings=naverMap.getUiSettings(); // UI관련 설정 담당
        uiSettings.setLocationButtonEnabled(true); //현위치 활성화

        marker = new Marker(); //마커 생성
        marker.setPosition(location); //마커 위치설정
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
}
