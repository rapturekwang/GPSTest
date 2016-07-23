package com.diygame.gpstest;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView tv1 = null;
    TextView tv2 = null;

    //위치정보 객체
    LocationManager lm = null;
    //위치정보 장치 이름
    String provider = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView)findViewById(R.id.text);
        tv2 = (TextView)findViewById(R.id.text2);

        /**위치정보 객체를 생성한다.*/
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        /** 현재 사용가능한 위치 정보 장치 검색*/
        //위치정보 하드웨어 목록
        Criteria c = new Criteria();
        //최적의 하드웨어 이름을 리턴받는다.
        provider = lm.getBestProvider(c, true);

        // 최적의 값이 없거나, 해당 장치가 사용가능한 상태가 아니라면,
        //모든 장치 리스트에서 사용가능한 항목 얻기
        if(provider == null || !lm.isProviderEnabled(provider)){
            // 모든 장치 목록
            List<String> list = lm.getAllProviders();

            for(int i = 0; i < list.size(); i++){
                //장치 이름 하나 얻기
                String temp = list.get(i);

                //사용 가능 여부 검사
                if(lm.isProviderEnabled(temp)){
                    provider = temp;
                    break;
                }
            }
        }// (end if)위치정보 검색 끝

        /**마지막으로  조회했던 위치 얻기*/
        Location location = lm.getLastKnownLocation(provider);

        if(location == null){
            Toast.makeText(this, "사용가능한 위치 정보 제공자가 없습니다.", Toast.LENGTH_SHORT).show();
        }else{
            //최종 위치에서 부터 이어서 GPS 시작...
            onLocationChanged(location);

        }
    }
    /** 이 화면이 불릴 때, 일시정지 해제 처리*/
    @Override
    public void onResume(){
        //Activity LifrCycle 관련 메서드는 무조건 상위 메서드 호출 필요
        super.onResume();

        //위치정보 객체에 이벤트 연결
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, this);
        lm.requestLocationUpdates(provider, 500, 0, this);
    }
    /** 다른 화면으로 넘어갈 때, 일시정지 처리*/
    @Override
    public void onPause(){
        //Activity LifrCycle 관련 메서드는 무조건 상위 메서드 호출 필요
        super.onPause();

        //위치정보 객체에 이벤트 해제
        lm.removeUpdates(this);
    }

    /** 위치가 변했을 경우 호출된다.*/
    @Override
    public void onLocationChanged(Location location) {
        // 위도, 경도
        double lat = location.getLatitude();
        double lng = location.getLongitude();

 /* // String이외의 데이터 형을 String으로 변환하는 메서드
  tv1.setText(String.valueOf(lat));
  // String이외의 데이터 형을 String으로 변화하는 꼼수~!!
  tv2.setText(lng +""); */

        // String이외의 데이터 형을 String으로 변환하는 메서드
        tv1.setText(String.valueOf(lat) + " / " + String.valueOf(lng));
        // String이외의 데이터 형을 String으로 변화하는 꼼수~!!
        tv2.setText(getAddress(lat, lng));

    }
    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    /** 위도와 경도 기반으로 주소를 리턴하는 메서드*/
    public String getAddress(double lat, double lng){
        String address = null;

        //위치정보를 활용하기 위한 구글 API 객체
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        //주소 목록을 담기 위한 HashMap
        List<Address> list = null;

        try{
            list = geocoder.getFromLocation(lat, lng, 1);
        } catch(Exception e){
            e.printStackTrace();
        }

        if(list == null){
            Log.e("getAddress", "주소 데이터 얻기 실패");
            return null;
        }

        if(list.size() > 0){
            Address addr = list.get(0);
            address = addr.getAddressLine(0);
//            address = addr.getCountryName() + " "
//                    + addr.getPostalCode() + " "
//                    + addr.getLocality() + " "
//                    + addr.getThoroughfare() + " "
//                    + addr.getFeatureName();
        }

        return address;



    }



}