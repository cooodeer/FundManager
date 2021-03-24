package com.example.jijinxiaoguanjia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.UrlQuerySanitizer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextView mJJName, mJJRate;
    String result = "";
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        sendRequest();
    }
    private void init() {
        mJJName = findViewById(R.id.tv_jj_name);
        mJJRate = findViewById(R.id.tv_jj_rate);
        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new MyDecorition());
        mRecyclerView.setAdapter(new LinerAdapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.setting:
                Toast toastS = Toast.makeText(this, "设置AFASNFANV;AIRV;INA;OSIERNGV;OAINER;VGINAER;OINV", Toast.LENGTH_SHORT);
                toastS.setGravity(Gravity.LEFT,0, 0);
                toastS.show();
                break;
            case R.id.help:
                Toast toastH = Toast.makeText(this, "帮助", Toast.LENGTH_SHORT);
                toastH.setGravity(Gravity.LEFT,0, 0);
                toastH.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickButton(View view) {
        sendRequest();
    }

    private void sendRequest() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;

                try {
//                    URL url = new URL("http://fund.eastmoney.com/data/");
//                    URL url = new URL("http://fundf10.eastmoney.com/jjjz_001643.html");
                    URL url = new URL("http://fundgz.1234567.com.cn/js/005827.js?rt=1589463125600");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString());

                    Log.d("TAG", "run: xdxd: " +response.toString());
                    String list = match2(response.toString());

                    Log.d("TAG", "run: xdxd：list: " + list);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mJJRate.setText(result);
            }
        });
    }

    private void resolver(String html) {
        int deltaRitePosition = html.indexOf("fix_zzl  bold ui-color-green");
        String deltaRite = html.substring(deltaRitePosition + 30, deltaRitePosition + 36);
        Log.d("TAG", "resolver: xdxd: deltapos: " + deltaRitePosition + " delta: " + deltaRite);
    }

    public String match(String source) {
        List<String> resultSection = new ArrayList<String>();

//        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?\\s.*?>";
//        String reg ="</span><span class=...";
        String reg ="<b class=\"(grn|red) lar bold\">.{1,45}";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find() && m.group().length()>=2) {
            String r = m.group();
            resultSection.add(r);
        }
        int riteStar = resultSection.toString().indexOf("(") + 2 ;
        int riteEnd = resultSection.toString().indexOf(')') - 1;

//        for (int i = riteStar; i < riteEnd; i++) {
//            result = result + resultSection.get(i);
//        }

        result = resultSection.toString().substring(riteStar,riteEnd);

        Log.d("TAG", "match: xdxd: " + resultSection + " riteStar: " + riteStar + " riteEnd: " + riteEnd);

        return result;
    }

    public String match2(String source) {
        List<String> resultSection = new ArrayList<String>();

//        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?\\s.*?>";
//        String reg ="</span><span class=...";
        String reg ="span class=\"fix_zzl.{1,40}";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find() && m.group().length()>=2) {
            String r = m.group();
            resultSection.add(r);
        }
        int riteStar = resultSection.toString().indexOf(">") + 1 ;
        int riteEnd = resultSection.toString().indexOf('<') ;

//        for (int i = riteStar; i < riteEnd; i++) {
//            result = result + resultSection.get(i);
//        }

//        result = resultSection.toString().substring(riteStar,riteEnd);

        Log.d("TAG", "match: xdxd: " + resultSection + " riteStar: " + riteStar + " riteEnd: " + riteEnd);

        return result;
    }

    class MyDecorition extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0,0,0,getResources().getDimensionPixelSize(R.dimen.dividerHeight));
        }
    }
}

