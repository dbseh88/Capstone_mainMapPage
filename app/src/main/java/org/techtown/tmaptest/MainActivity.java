package org.techtown.tmaptest;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    String API_Key = "l7xx04eb869c4b064be4904d5fd75d609819";

    // T Map View
    TMapView tMapView = null;

    // T Map GPS
    TMapGpsManager tMapGPS = null;

    TMapPoint tMapPointStart = null;
    TMapPoint tMapPointEnd = null;
    double Distance = 0;

    TMapPOIItem POIItem = new TMapPOIItem();

    int testNum = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // T Map View
        tMapView = new TMapView(this);

        // API Key
        tMapView.setSKTMapApiKey(API_Key);

        //TMapMarkerItem markerItem1 = new TMapMarkerItem();

        //TMapPoint tMapPoint1 = new TMapPoint(37.3004, 127.0358); // 경기대학교 수원캠퍼스

        // 마커 아이콘
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.iconfinder_icons_pin);



        // Initial Setting
        tMapView.setZoomLevel(17);
        tMapView.setIconVisibility(true);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        // T Map View Using Linear Layout
        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.tmap);
        linearLayoutTmap.addView(tMapView);

        // Request For GPS permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // GPS using T Map
        tMapGPS = new TMapGpsManager(this);

        // Initial Setting
        tMapGPS.setMinTime(1000);
        tMapGPS.setMinDistance(10);
        //tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER);
        tMapGPS.setProvider(tMapGPS.GPS_PROVIDER);

        tMapGPS.OpenGps();

        //tMapPointStart = new TMapPoint(37.566441, 126.985002); // SKT타워(출발지)
        //tMapPointEnd = new TMapPoint(37.551135, 126.988205); // N서울타워(목적지)
        //tMapPointStart = new TMapPoint(37.2961, 127.0287); // 8강 육영관(출발지)
        //tMapPointEnd = new TMapPoint(37.3013, 127.03745); // 종합강의동(목적지)
        /*tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, tMapPointStart, tMapPointEnd, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                tMapView.addTMapPath(polyLine);
            }
        });*/



        //searchPOI(arrPOI);

        //getPOIPoint(arrPOI);

        //길 안내 버튼
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TMapPoint Now = new TMapPoint(tMapGPS.getLocation().getLatitude(), tMapGPS.getLocation().getLongitude());
                //getPOIPoint(arrPOI);
                drawPolyLine(Now, tMapPointEnd);
            }
        });

        //버튼별 경로탐색
        drawLineButton();


        // 클릭 이벤트 설정
        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                //Toast.makeText(getApplicationContext(), tMapPoint.getLatitude() + ", " + tMapPoint.getLongitude(), Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                //Toast.makeText(MapEvent.this, "onPressUp~!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // 롱 클릭 이벤트 설정
        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint) {
                //Toast.makeText(getApplicationContext(), tMapPoint.getLatitude() + ", " + tMapPoint.getLongitude(), Toast.LENGTH_SHORT).show();

                /*try {
                    TMapData tmapdata = new TMapData();
                    TMapMarkerItem markerItem1 = new TMapMarkerItem();

                    TMapPoint tMapPoint1 = new TMapPoint(tMapPoint.getLatitude(), tMapPoint.getLongitude());

                    if(tMapPointStart != null && tMapPointEnd != null) {
                        tMapPointStart = null;
                        tMapPointEnd = null;
                    }

                    if(tMapPointStart == null)
                        tMapPointStart = tMapPoint1;
                    else if (tMapPointStart != null)
                        tMapPointEnd = tMapPoint1;



                    System.out.println("test test test");
                    System.out.println("start:  " + tMapPointStart);
                    System.out.println("end:    " + tMapPointEnd);

                    drawPolyLine(tMapPointStart, tMapPointEnd);


                    markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                    markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                    markerItem1.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
                    markerItem1.setName(""); // 마커의 타이틀 지정
                    tMapView.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가
                    tMapView.setCenterPoint(tMapPoint.getLongitude(), tMapPoint.getLatitude(), true);
                    Toast.makeText(getApplicationContext(), tMapPoint.getLatitude() + ", " + tMapPoint.getLongitude(), Toast.LENGTH_LONG).show();


                }

                catch(Exception e) {
                    e.printStackTrace();
                }

                /*TMapMarkerItem markerItem1 = new TMapMarkerItem();

                TMapPoint tMapPoint1 = new TMapPoint(tMapPoint.getLatitude(), tMapPoint.getLongitude()); // 경기대학교 수원캠퍼스

                markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem1.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
                markerItem1.setName(""); // 마커의 타이틀 지정
                tMapView.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가
                Log.d("marker1","마커1 지정");
                tMapView.setCenterPoint(tMapPoint.getLongitude(), tMapPoint.getLatitude(), true);
                */
            }
        });

        // 지도 스크롤 종료
        tMapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
            @Override
            public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {

            }
        });
    }



    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
    }

    private void drawPolyLine(TMapPoint startPoint, TMapPoint endPoint){

        new Thread(){
            @Override
            public void run(){
                try {
                    TMapPolyLine tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, endPoint);
                    tMapPolyLine.setLineColor(Color.BLUE);
                    tMapPolyLine.setLineWidth(2);
                    tMapView.addTMapPolyLine("Line1", tMapPolyLine);
                    Distance = tMapPolyLine.getDistance();
                }catch(Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void setMultiMarkers(ArrayList<TMapPoint> arrTPoint, ArrayList<String> arrTitle,
                                 ArrayList<String> arrAddress)
    {
        for( int i = 0; i < arrTPoint.size(); i++ )
        {
            // 마커 아이콘
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.iconfinder_endpoint);

            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            tMapMarkerItem.setIcon(bitmap);

            tMapMarkerItem.setTMapPoint(arrTPoint.get(i));

            tMapView.addMarkerItem("markerItem" + i, tMapMarkerItem);

            //setBalloonView(tMapMarkerItem, arrTitle.get(i), arrAddress.get(i));
        }
    }

    private void setBalloonView(TMapMarkerItem marker, String title, String address)
    {
        marker.setCanShowCallout(true);

        if( marker.getCanShowCallout() )
        {
            marker.setCalloutTitle(title);
            marker.setCalloutSubTitle(address);

            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.iconfinder_arrow_down);
            marker.setCalloutRightButtonImage(bitmap);
        }
    }


    private void searchPOI(ArrayList<String> arrPOI) {
        final TMapData tMapData = new TMapData();
        final ArrayList<TMapPoint> arrTMapPoint = new ArrayList<>();
        final ArrayList<String> arrTitle = new ArrayList<>();
        final ArrayList<String> arrAddr = new ArrayList<>();

        for (int i = 0; i < arrPOI.size(); i++){
            tMapData.findTitlePOI(arrPOI.get(i), new TMapData.FindTitlePOIListenerCallback() {
                @Override
                public void onFindTitlePOI(ArrayList<TMapPOIItem> arrayList) {
                    for(int j = 0; j < arrayList.size(); j++){
                        TMapPOIItem tMapPOIItem = arrayList.get(j);
                        arrTMapPoint.add(tMapPOIItem.getPOIPoint());
                        arrTitle.add(tMapPOIItem.getPOIName());
                        arrAddr.add(tMapPOIItem.upperAddrName + " " +
                                tMapPOIItem.middleAddrName + " " + tMapPOIItem.lowerAddrName);
                        System.out.println(arrAddr);
                    }
                    setMultiMarkers(arrTMapPoint, arrTitle, arrAddr);
                }
            });
        }
    }


    private void getPOIPoint(ArrayList<String> arrPOI, int bldNum){
        final TMapData tMapData = new TMapData();
        final ArrayList<TMapPoint> arrTMapPoint = new ArrayList<>();
        final ArrayList<String> arrTitle = new ArrayList<>();
        final ArrayList<String> arrAddr = new ArrayList<>();
        TMapPoint Now = new TMapPoint(tMapGPS.getLocation().getLatitude(), tMapGPS.getLocation().getLongitude());

        //for (int i = 0; i < arrPOI.size(); i++) {
            tMapData.findTitlePOI(arrPOI.get(bldNum), new TMapData.FindTitlePOIListenerCallback() {
                @Override
                public void onFindTitlePOI(ArrayList<TMapPOIItem> arrayList) {
                    TMapPOIItem tMapPOIItem = arrayList.get(0);
                    arrTMapPoint.add(tMapPOIItem.getPOIPoint());
                    arrTitle.add(tMapPOIItem.getPOIName());
                    arrAddr.add(tMapPOIItem.upperAddrName + " " +
                            tMapPOIItem.middleAddrName + " " + tMapPOIItem.lowerAddrName);
                    System.out.println(arrAddr);
                    tMapPointEnd = tMapPOIItem.getPOIPoint();
                    setMultiMarkers(arrTMapPoint, arrTitle, arrAddr);
                    drawPolyLine(Now, tMapPointEnd);
                }
            });
        //}

    }

    //버튼으로 길 찾기 함수 - 테스트용
    private void drawLineButton(){
        //ArrayList<TMapPoint> arrTMP = new ArrayList<>();  //TMapPoint 형식의 배열을 이용해서 처리할 수 있을까?
        ArrayList<String> arrPOI = new ArrayList<>();
        arrPOI.add("경기대학교 수원캠퍼스 진리관");
        arrPOI.add("경기대학교 수원캠퍼스 성신관");
        arrPOI.add("경기대학교 수원캠퍼스 애경관");
        arrPOI.add("경기대학교 수원캠퍼스 예지관");
        arrPOI.add("경기대학교 수원캠퍼스 덕문관");
        arrPOI.add("경기대학교 수원캠퍼스 광교관");
        arrPOI.add("경기대학교 수원캠퍼스 집현관");
        arrPOI.add("경기대학교 수원캠퍼스 육영관");
        arrPOI.add("경기대학교 수원캠퍼스 종합강의동");

        //searchPOI(arrPOI);
        //TMapPoint btn0Point = new TMapPoint(37.30093, 127.03395);
        //TMapPoint btn1Point = new TMapPoint(37.29992, 127.03390);
        //TMapPoint btn2Point = new TMapPoint(37.29959, 127.03432);

        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TMapPoint Now = new TMapPoint(tMapGPS.getLocation().getLatitude(), tMapGPS.getLocation().getLongitude());
                switch (v.getId())
                {
                    case R.id.btn1:
                        getPOIPoint(arrPOI, 0);
                        break;
                    case R.id.btn2:
                        getPOIPoint(arrPOI, 1);
                        break;
                    case R.id.btn3:
                        getPOIPoint(arrPOI, 2);
                        break;
                    case R.id.btn4:
                        getPOIPoint(arrPOI, 3);
                        break;

                }
            }
        };

        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        btn3.setOnClickListener(listener);
        btn4.setOnClickListener(listener);
    }

}