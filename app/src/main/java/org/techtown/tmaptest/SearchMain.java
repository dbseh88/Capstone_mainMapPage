package org.techtown.tmaptest;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchMain extends AppCompatActivity {
    public static final String ROOT_DIR = "/data/data/org.techtown.tmaptest/databases/";


    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;


    String TblName = "Location";
    String list_sql = null;


    ConstraintLayout mainbuttonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_listview);


        Button btnyes = findViewById(R.id.btnyes);
        btnyes.setVisibility(View.INVISIBLE);

        Button btnAll = findViewById(R.id.btnAll);
        Button btnLec = findViewById(R.id.btnLec);
        Button btnEtc = findViewById(R.id.btnEtc);


        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                list_sql = "Select * FROM " + TblName;
                ShowMushDBInfo(list_sql);
                adapter.notifyDataSetChanged();

            }
        });
        btnLec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                list_sql = "Select * FROM " + TblName + " WHERE _id <= 10;";
                ShowMushDBInfo(list_sql);
                adapter.notifyDataSetChanged();
            }
        });
        btnEtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                list_sql = "Select * FROM " + TblName + " WHERE _id > 10;";
                ShowMushDBInfo(list_sql);
                adapter.notifyDataSetChanged();
            }
        });
        // btnyes.setVisibility(View.VISIBLE);


        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.listView);

        // 리스트를 생성한다.
        list = new ArrayList<String>();

        // 검색에 사용할 데이터을 미리 저장한다.

        list_sql = "Select * FROM " + TblName;
        ShowMushDBInfo(list_sql);

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list, this);

        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();

                search(text);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // position이 클릭된 위치입니다.
                // 컬렉션에서 적절하게 꺼내서 사용하시면 됩니다.
                Toast.makeText(getApplicationContext(), list.get(position), Toast.LENGTH_LONG).show();

                String selectLoc;
                selectLoc = list.get(position);

                double latitude = getLati(selectLoc);
                double longitude = getLongi(selectLoc);



                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("SearchMain_Lati", latitude);
                intent.putExtra("SearchMain_Longi", longitude);


                btnyes.setVisibility(View.VISIBLE);
                btnyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });



                // 추가된 부분
                String name = list.get(position);

            }
        });

    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0; i < arraylist.size(); i++) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }


    public static void setDB(Context ctx) {
        File folder = new File(ROOT_DIR);
        if(folder.exists()) {
        } else {
            folder.mkdirs();
        }
        AssetManager assetManager = ctx.getResources().getAssets();
        // db파일 이름 적어주기
        File outfile = new File(ROOT_DIR+"DBtest.db");
        InputStream is = null;
        FileOutputStream fo = null;
        long filesize = 0;
        try {
            is = assetManager.open("DBtest.db", AssetManager.ACCESS_BUFFER);
            filesize = is.available();
            if (outfile.length() <= 0) {
                byte[] tempdata = new byte[(int) filesize];
                is.read(tempdata);
                is.close();
                outfile.createNewFile();
                fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
            } else {}
        } catch (IOException e) {

        }
    }

    // Cursor를 통해 .db파일의 내용을 보여주는 함수
    public SQLiteDatabase db;
    public Cursor cursor;
    DBHelper mHelper;
    //String TblName = "Location";

    //private void ShowMushDBInfo(String name){



    private double getLati(String selectLoc) {
        double dbLati = 0;
        String sql = "SELECT latitude FROM "+TblName+" WHERE " +" locName= "+"'"+selectLoc+"';";
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            dbLati = cursor.getDouble(0);
        }
        cursor.close();
        return dbLati;
    }


    private double getLongi(String selectLoc) {
        double dbLongi = 0;
        String sql = "SELECT longitude FROM "+TblName+" WHERE " +" locName= "+"'"+selectLoc+"';";
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            dbLongi = cursor.getDouble(0);
        }
        cursor.close();
        return dbLongi;
    }



    private void ShowMushDBInfo(String list_sql){
        setDB(this);

        mHelper = new DBHelper(this);
        db = mHelper.getWritableDatabase();

        String dbLocName= null;

        //String sql = "Select * FROM " + TblName;


        cursor = db.rawQuery(list_sql, null);

        while (cursor.moveToNext()) {
            dbLocName = cursor.getString(1);
            list.add(dbLocName);
        }

        cursor.close();
    }


}