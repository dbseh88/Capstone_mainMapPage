package org.techtown.tmaptest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {
    private static final int REQUEST_CODE = 101;
    String API_Key = "l7xx04eb869c4b064be4904d5fd75d609819";

    // T Map View
    TMapView tMapView = null;

    // T Map GPS
    TMapGpsManager tMapGPS = null;
    TMapPoint tMapPointEnd = null;
    TMapPoint searchPoint = null;


    String Distance = null;
    String Time = null;

    ArrayList<String> arrPOI = new ArrayList<>();

    ConstraintLayout mainbuttonLayout;
    ConstraintLayout navibuttonLayout;
    ConstraintLayout lecbuttonLayout;
    ConstraintLayout etcbuttonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // T Map View
        tMapView = new TMapView(this);

        // API Key
        tMapView.setSKTMapApiKey(API_Key);

        mainbuttonLayout = findViewById(R.id.MainButtonLayout);
        navibuttonLayout = findViewById(R.id.NaviButtonLayout);
        lecbuttonLayout = findViewById(R.id.LecButtonLayout);
        etcbuttonLayout = findViewById(R.id.EtcButtonLayout);

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
        /*Button button = findViewById(R.id.btn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TMapPoint Now = new TMapPoint(tMapGPS.getLocation().getLatitude(), tMapGPS.getLocation().getLongitude());
                //getPOIPoint(arrPOI);
                drawPolyLine(Now, tMapPointEnd);
            }
        });*/

        //버튼별 경로탐색
        drawLineButton();

        //검색창 Intent
        Button searchbtn = findViewById(R.id.search);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchMain.class);
                startActivityForResult(intent, 101);
            }
        });


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
    protected void onActivityResult(int requetCode, int resultCode, Intent data) {
        super.onActivityResult(101, resultCode, data);

        if(requetCode == REQUEST_CODE) {
            if(resultCode != Activity.RESULT_OK) {
                return;
            }
            double search_lati = data.getExtras().getDouble("SearchMain_Lati");
            double search_longi = data.getExtras().getDouble("SearchMain_Longi");

            TMapPoint Now = new TMapPoint(tMapGPS.getLocation().getLatitude(), tMapGPS.getLocation().getLongitude());
            searchPoint = new TMapPoint(search_lati, search_longi);
            drawPolyLine(Now, searchPoint);
        }
    }

    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
    }

    // 뒤로가기 키 이벤트 처리
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(navibuttonLayout.getVisibility() == View.VISIBLE){
                navibuttonLayout.setVisibility(View.INVISIBLE);
                mainbuttonLayout.setVisibility(View.VISIBLE);
                return true;
            }
            if(lecbuttonLayout.getVisibility() == View.VISIBLE){
                lecbuttonLayout.setVisibility(View.INVISIBLE);
                navibuttonLayout.setVisibility(View.VISIBLE);
                return true;
            }
            if(etcbuttonLayout.getVisibility() == View.VISIBLE){
                etcbuttonLayout.setVisibility(View.INVISIBLE);
                navibuttonLayout.setVisibility(View.VISIBLE);
                return true;
            }
        }
        return false;
    }
    public void onButtonNaviClicked(View v){
        mainbuttonLayout.setVisibility(View.INVISIBLE);
        navibuttonLayout.setVisibility(View.VISIBLE);
    }
    public void onButtonBackClicked(View v){
        navibuttonLayout.setVisibility(View.INVISIBLE);
        mainbuttonLayout.setVisibility(View.VISIBLE);
    }

    public void onButtonLecClicked(View v){
        navibuttonLayout.setVisibility(View.INVISIBLE);
        lecbuttonLayout.setVisibility(View.VISIBLE);
    }

    public void onButtonEtcClicked(View v){
        navibuttonLayout.setVisibility(View.INVISIBLE);
        etcbuttonLayout.setVisibility(View.VISIBLE);
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

                    new TMapData().findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, endPoint, new TMapData.FindPathDataAllListenerCallback(){
                        @Override
                        public void onFindPathDataAll(Document document) {
                            Element root = document.getDocumentElement();
                            NodeList nodeListPlacemark = root.getElementsByTagName("Document");
                            for( int i=0; i< nodeListPlacemark.getLength();i++){

                                NodeList Dis = root.getElementsByTagName("tmap:totalDistance");
                                Distance = Dis.item(0).getChildNodes().item(0).getNodeValue();
                                NodeList time = root.getElementsByTagName("tmap:totalTime");
                                Time = time.item(0).getChildNodes().item(0).getNodeValue();
                                int min = Integer.parseInt(Time)/60;
                                int sec = Integer.parseInt(Time)%60;
                                TextView text = (TextView)findViewById(R.id.textTest);
                                text.setText("거리 : " + Distance + "m 시간 : " + min + "분 " + sec + "초");
                                Log.d("debug", "거리 : " + Distance + "m\n시간 : " + min + "분 " + sec + "초");
                            }
                        }//end onFindPathDataAll
                    }); //end findPathDataWithType

                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void setMultiMarkers(ArrayList<TMapPoint> arrTPoint)
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
                    //setMultiMarkers(arrTMapPoint, arrTitle, arrAddr);
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
                //setMultiMarkers(arrTMapPoint);
                drawPolyLine(Now, tMapPointEnd);
            }
        });
        //}

    }

    private void getPoint(ArrayList<TMapPoint> arrPoint, int bldNum){
        final ArrayList<TMapPoint> arrTMapPoint = new ArrayList<>();
        TMapPoint Now = new TMapPoint(tMapGPS.getLocation().getLatitude(), tMapGPS.getLocation().getLongitude());
        arrTMapPoint.add(arrPoint.get(bldNum));
        setMultiMarkers(arrTMapPoint);
        drawPolyLine(Now, arrPoint.get(bldNum));
    }


    //버튼으로 길 찾기 함수 - 테스트용
    private void drawLineButton(){
        //ArrayList<TMapPoint> arrTMP = new ArrayList<>();  //TMapPoint 형식의 배열을 이용해서 처리할 수 있을까?
        //ArrayList<String> arrPOI = new ArrayList<>();
        arrPOI.add("경기대학교 수원캠퍼스 진리관");
        arrPOI.add("경기대학교 수원캠퍼스 성신관");
        arrPOI.add("경기대학교 수원캠퍼스 애경관");
        arrPOI.add("경기대학교 수원캠퍼스 예지관");
        arrPOI.add("경기대학교 수원캠퍼스 덕문관");
        arrPOI.add("경기대학교 수원캠퍼스 광교관");
        arrPOI.add("경기대학교 수원캠퍼스 집현관");
        arrPOI.add("경기대학교 수원캠퍼스 육영관");
        arrPOI.add("경기대학교 수원캠퍼스 호연관");
        arrPOI.add("경기대학교 수원캠퍼스 종합강의동");

        //searchPOI(arrPOI);
        //TMapPoint btn0Point = new TMapPoint(37.30093, 127.03395);
        //TMapPoint btn1Point = new TMapPoint(37.29992, 127.03390);
        //TMapPoint btn2Point = new TMapPoint(37.29959, 127.03432);

        ArrayList<TMapPoint> arrPoint = new ArrayList<>();
        arrPoint.add(0, new TMapPoint(37.2985, 127.0396));  //기숙사정류장
        arrPoint.add(1, new TMapPoint(37.3017, 127.0340));  //매표소정류장
        arrPoint.add(2, new TMapPoint(37.3012, 127.0344));  //경기탑 정류장
        arrPoint.add(3, new TMapPoint(37.2988, 127.0371));  //테니스장 정류장
        arrPoint.add(4, new TMapPoint(37.3035, 127.0340));  //9강 정류장
        arrPoint.add(5, new TMapPoint(37.3007, 127.0444));  //광교역 정류장
        arrPoint.add(6, new TMapPoint(37.3005, 127.0371));  //e스퀘어
        arrPoint.add(7, new TMapPoint(37.3011, 127.0361));  //감성코어


        Button lec1 = findViewById(R.id.lec_1);
        Button lec2 = findViewById(R.id.lec_2);
        Button lec3 = findViewById(R.id.lec_3);
        Button lec4 = findViewById(R.id.lec_4);
        Button lec5 = findViewById(R.id.lec_5);
        Button lec6 = findViewById(R.id.lec_6);
        Button lec7 = findViewById(R.id.lec_7);
        Button lec8 = findViewById(R.id.lec_8);
        Button lec9 = findViewById(R.id.lec_9);
        Button lec10 = findViewById(R.id.lec_10);
        Button etc1 = findViewById(R.id.etc1);
        Button etc2 = findViewById(R.id.etc2);
        Button etc3 = findViewById(R.id.etc3);
        Button etc4 = findViewById(R.id.etc4);
        Button etc5 = findViewById(R.id.etc5);
        Button etc6 = findViewById(R.id.etc6);
        Button etc7 = findViewById(R.id.etc7);
        Button etc8 = findViewById(R.id.etc8);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TMapPoint Now = new TMapPoint(tMapGPS.getLocation().getLatitude(), tMapGPS.getLocation().getLongitude());
                switch (v.getId())
                {
                    case R.id.lec_1:
                        getPOIPoint(arrPOI, 0);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        lecbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.lec_2:
                        getPOIPoint(arrPOI, 1);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        lecbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.lec_3:
                        getPOIPoint(arrPOI, 2);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        lecbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.lec_4:
                        getPOIPoint(arrPOI, 3);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        lecbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.lec_5:
                        getPOIPoint(arrPOI, 4);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        lecbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.lec_6:
                        getPOIPoint(arrPOI, 5);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        lecbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.lec_7:
                        getPOIPoint(arrPOI, 6);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        lecbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.lec_8:
                        getPOIPoint(arrPOI, 7);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        lecbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.lec_9:
                        getPOIPoint(arrPOI, 8);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        lecbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.lec_10:
                        getPOIPoint(arrPOI, 9);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        lecbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.etc1:
                        //getPOIPoint(arrPOI, 3);
                        getPoint(arrPoint, 0);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        etcbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.etc2:
                        //getPOIPoint(arrPOI, 3);
                        getPoint(arrPoint, 1);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        etcbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.etc3:
                        //getPOIPoint(arrPOI, 3);
                        getPoint(arrPoint, 2);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        etcbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.etc4:
                        //getPOIPoint(arrPOI, 3);
                        getPoint(arrPoint, 3);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        etcbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.etc5:
                        //getPOIPoint(arrPOI, 3);
                        getPoint(arrPoint, 4);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        etcbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.etc6:
                        //getPOIPoint(arrPOI, 3);
                        getPoint(arrPoint, 5);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        etcbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.etc7:
                        //getPOIPoint(arrPOI, 3);
                        getPoint(arrPoint, 6);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        etcbuttonLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.etc8:
                        //getPOIPoint(arrPOI, 3);
                        getPoint(arrPoint, 7);
                        mainbuttonLayout.setVisibility(View.VISIBLE);
                        etcbuttonLayout.setVisibility(View.INVISIBLE);
                        break;

                }
            }
        };

        lec1.setOnClickListener(listener);
        lec2.setOnClickListener(listener);
        lec3.setOnClickListener(listener);
        lec4.setOnClickListener(listener);
        lec5.setOnClickListener(listener);
        lec6.setOnClickListener(listener);
        lec7.setOnClickListener(listener);
        lec8.setOnClickListener(listener);
        lec9.setOnClickListener(listener);
        lec10.setOnClickListener(listener);
        etc1.setOnClickListener(listener);
        etc2.setOnClickListener(listener);
        etc3.setOnClickListener(listener);
        etc4.setOnClickListener(listener);
        etc5.setOnClickListener(listener);
        etc6.setOnClickListener(listener);
        etc7.setOnClickListener(listener);
        etc8.setOnClickListener(listener);
    }

}