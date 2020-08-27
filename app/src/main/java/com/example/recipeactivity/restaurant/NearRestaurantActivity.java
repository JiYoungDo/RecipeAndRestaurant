package com.example.recipeactivity.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.recipeactivity.MainActivity;

import com.example.recipeactivity.R;
import com.example.recipeactivity.recipe.ListActivity;
import com.example.recipeactivity.recipe.RecipeItem;
import com.example.recipeactivity.recipe.RecyclerViewAdapter;
import com.example.recipeactivity.recipe.SearchResultActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NearRestaurantActivity extends AppCompatActivity {

    ImageButton back_btn, search_btn;
    Spinner spinner_do, spinner_si;
    ArrayList<RestaurantItem> mArrayList = new ArrayList<>(); // 모든 아이템들을 담을 어레이 리스트
    ArrayList<RestaurantItem> mArrayList2 = new ArrayList<>(); // 모든 아이템들을 담을 어레이 리스트

    String selected_do, selected_si;
    RecyclerView mRecyclerview;
    boolean IsOnSearch = false;
    String strNickname;

    RestaurantViewAdapter mAdapter;
    ArrayList<RestaurantItem> mAdaptArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_restaurant);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        strNickname = intent.getStringExtra("name");

        back_btn = findViewById(R.id.near_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NearRestaurantActivity.this, MainActivity.class);
                intent.putExtra("name",strNickname);
                startActivity(intent);
                finish();
            }
        });
        search_btn = findViewById(R.id.near_search_btn);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsOnSearch = true;
                mAdaptArrayList.clear();
                Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.clickblink);
                search_btn.startAnimation(startAnimation);
                // 스피너의 값에 따라 array 값을 바꿔 줘야 한다.

                // mAdaptArrayList 를 조정
                for(int i = 0; i<mArrayList.size(); i++) {
                    if(mArrayList.get(i).getRELAX_SI_NM().equals(selected_do) && mArrayList.get(i).getRELAX_SIDO_NM().equals(selected_si))
                    {
                        mAdaptArrayList.add(mArrayList.get(i));
                    }
//                    Log.d("a",mArrayList.get(i).getRELAX_SI_NM());
//                    Log.d("b",selected_do);
//                    Log.d("d",String.valueOf(mArrayList2.get(i).getRELAX_SI_NM().equals(selected_do)));
                }
                mRecyclerview = findViewById(R.id.recyclerview_near_restaurant_list);
                LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerview.setLayoutManager(mLinearLayoutManager);

                mAdapter = new RestaurantViewAdapter(mAdaptArrayList);
                mRecyclerview.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerview.getContext(), mLinearLayoutManager.getOrientation());
                mRecyclerview.addItemDecoration(dividerItemDecoration);
            }
        });

        // 리스트에 추가 하기 (mArrayList,list_do, list_si)  &  mArrayList 에 json 형태들의 Restaurant 아이템들을 담아야 한다.
        NearRestaurantActivity.MyAsyncTask_1_1000 mProcessTask = new NearRestaurantActivity.MyAsyncTask_1_1000();
        mProcessTask.execute();
        NearRestaurantActivity.MyAsyncTask_1001_2000 mProcessTask2 = new NearRestaurantActivity.MyAsyncTask_1001_2000();
        mProcessTask2.execute();
        NearRestaurantActivity.MyAsyncTask_2001_3000 mProcessTask3 = new NearRestaurantActivity.MyAsyncTask_2001_3000();
        mProcessTask3.execute();
        NearRestaurantActivity.MyAsyncTask_3001_4000 mProcessTask4 = new NearRestaurantActivity.MyAsyncTask_3001_4000();
        mProcessTask4.execute();
        NearRestaurantActivity.MyAsyncTask_4001_5000 mProcessTask5 = new NearRestaurantActivity.MyAsyncTask_4001_5000();
        mProcessTask5.execute();
        NearRestaurantActivity.MyAsyncTask_5001_6000 mProcessTask6 = new NearRestaurantActivity.MyAsyncTask_5001_6000();
        mProcessTask6.execute();
        NearRestaurantActivity.MyAsyncTask_6001_6601 mProcessTask7 = new NearRestaurantActivity.MyAsyncTask_6001_6601();
        mProcessTask7.execute();

        if(IsOnSearch == false)
        {
            mRecyclerview = findViewById(R.id.recyclerview_near_restaurant_list);
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerview.setLayoutManager(mLinearLayoutManager);

            mAdapter = new RestaurantViewAdapter(mArrayList);
            mRecyclerview.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerview.getContext(), mLinearLayoutManager.getOrientation());
            mRecyclerview.addItemDecoration(dividerItemDecoration);
        }
        spinner_do = findViewById(R.id.near_restaurant_do);
        spinner_si = findViewById(R.id.near_restaurant_si);

        spinner_do.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // 스피너 선택시
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_do = String.valueOf(parent.getItemAtPosition(position));
                Log.d("selected_do", selected_do);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinner_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_si = String.valueOf(parent.getItemAtPosition(position));
                Log.d("selected_si", selected_si);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        mAdapter.setOnItemClickListener(new RestaurantViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Toast.makeText(getApplicationContext(), "클릭이 불가합니다.",Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.setOnItemLongClickListener(new RestaurantViewAdapter.OnItemLongClickListener() {
            String RELAX_RSTRNT_TEL;
            @Override
            public void onItemLongClick(View v, int pos) {
                RestaurantItem restaurantItem = mAdaptArrayList.get(pos);
                RELAX_RSTRNT_TEL = restaurantItem.getRELAX_RSTRNT_TEL();

                AlertDialog.Builder builder = new AlertDialog.Builder(NearRestaurantActivity.this);
                builder.setTitle("전화 걸기");
                builder.setMessage("해당 번호로 전화를 거시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(RELAX_RSTRNT_TEL));
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                startActivity(intent);
                                finish();
                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

    }

     public class MyAsyncTask_1_1000 extends AsyncTask<String,Void, RestaurantItem[]>
     {
         ProgressDialog progressDialog = new ProgressDialog(NearRestaurantActivity.this);
         OkHttpClient client = new OkHttpClient();

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             progressDialog.setMessage("로딩 중...");
             progressDialog.show();
         }

         @Override
         protected RestaurantItem[] doInBackground(String... params) {

             HttpUrl.Builder urlBuilder = HttpUrl.parse("http://211.237.50.150:7080/openapi/cfcf7b6d1705a4a060a6ce134dbec9f80d183ae1737eec8f4eded265f478222b/json/Grid_20200713000000000605_1/1/1000").newBuilder();
             String url = urlBuilder.build().toString();

             Request request = new Request.Builder().url(url).build();
             try {
                 Response response = client.newCall(request).execute();

                 Gson gson = new GsonBuilder().create();
                 JsonParser parser = new JsonParser();

                 JsonElement rootObject = parser.parse(response.body().charStream())
                         .getAsJsonObject().get("Grid_20200713000000000605_1").getAsJsonObject().get("row");

                 RestaurantItem[] posts = gson.fromJson(rootObject, RestaurantItem[].class);

                 for(int i = 0; i<1000; i++)
                 {
                         mArrayList.add(posts[i]);
                 }

                 return posts;

             } catch (Exception e) {
                 e.printStackTrace();
                 RestaurantItem[] a = new RestaurantItem[]{};
                 return a ;
             }
         }


         @Override
         protected void onPostExecute(RestaurantItem[] result) {
             super.onPostExecute(result);

             progressDialog.dismiss();
             // 요청 결과를 여기서 처리한다. 화면에 출력하기 등.
             //  Log.d("Result:", result.toString());

         }
     }

     public class MyAsyncTask_1001_2000 extends AsyncTask<String,Void, RestaurantItem[]>
     {
         ProgressDialog progressDialog = new ProgressDialog(NearRestaurantActivity.this);
         OkHttpClient client = new OkHttpClient();

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             progressDialog.setMessage("로딩 중...");
             progressDialog.show();
         }

         @Override
         protected RestaurantItem[] doInBackground(String... params) {

             HttpUrl.Builder urlBuilder = HttpUrl.parse("http://211.237.50.150:7080/openapi/cfcf7b6d1705a4a060a6ce134dbec9f80d183ae1737eec8f4eded265f478222b/json/Grid_20200713000000000605_1/1001/2000").newBuilder();
             String url = urlBuilder.build().toString();

             Request request = new Request.Builder().url(url).build();
             try {
                 Response response = client.newCall(request).execute();

                 Gson gson = new GsonBuilder().create();
                 JsonParser parser = new JsonParser();

                 JsonElement rootObject = parser.parse(response.body().charStream())
                         .getAsJsonObject().get("Grid_20200713000000000605_1").getAsJsonObject().get("row");

                 RestaurantItem[] posts = gson.fromJson(rootObject, RestaurantItem[].class);

                 for(int i = 0; i<1000; i++)
                 {
                     mArrayList.add(posts[i]);
                 }
                 return posts;

             } catch (Exception e) {
                 e.printStackTrace();
                 RestaurantItem[] a = new RestaurantItem[]{};
                 return a ;
             }
         }


         @Override
         protected void onPostExecute(RestaurantItem[] result) {
             super.onPostExecute(result);

             progressDialog.dismiss();
             // 요청 결과를 여기서 처리한다. 화면에 출력하기 등.
             //  Log.d("Result:", result.toString());

         }
     }

     public class MyAsyncTask_2001_3000 extends AsyncTask<String,Void, RestaurantItem[]>
     {
         ProgressDialog progressDialog = new ProgressDialog(NearRestaurantActivity.this);
         OkHttpClient client = new OkHttpClient();

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             progressDialog.setMessage("로딩 중...");
             progressDialog.show();
         }

         @Override
         protected RestaurantItem[] doInBackground(String... params) {

             HttpUrl.Builder urlBuilder = HttpUrl.parse("http://211.237.50.150:7080/openapi/cfcf7b6d1705a4a060a6ce134dbec9f80d183ae1737eec8f4eded265f478222b/json/Grid_20200713000000000605_1/2001/3000").newBuilder();
             String url = urlBuilder.build().toString();

             Request request = new Request.Builder().url(url).build();
             try {
                 Response response = client.newCall(request).execute();

                 Gson gson = new GsonBuilder().create();
                 JsonParser parser = new JsonParser();

                 JsonElement rootObject = parser.parse(response.body().charStream())
                         .getAsJsonObject().get("Grid_20200713000000000605_1").getAsJsonObject().get("row");

                 RestaurantItem[] posts = gson.fromJson(rootObject, RestaurantItem[].class);

                 for(int i = 0; i<1000; i++)
                 {
                     mArrayList.add(posts[i]);
                 }
                 return posts;

             } catch (Exception e) {
                 e.printStackTrace();
                 RestaurantItem[] a = new RestaurantItem[]{};
                 return a ;
             }
         }


         @Override
         protected void onPostExecute(RestaurantItem[] result) {
             super.onPostExecute(result);

             progressDialog.dismiss();
             // 요청 결과를 여기서 처리한다. 화면에 출력하기 등.
             //  Log.d("Result:", result.toString());

         }
     }

     public class MyAsyncTask_3001_4000 extends AsyncTask<String,Void, RestaurantItem[]>
     {
         ProgressDialog progressDialog = new ProgressDialog(NearRestaurantActivity.this);
         OkHttpClient client = new OkHttpClient();

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             progressDialog.setMessage("로딩 중...");
             progressDialog.show();
         }

         @Override
         protected RestaurantItem[] doInBackground(String... params) {

             HttpUrl.Builder urlBuilder = HttpUrl.parse("http://211.237.50.150:7080/openapi/cfcf7b6d1705a4a060a6ce134dbec9f80d183ae1737eec8f4eded265f478222b/json/Grid_20200713000000000605_1/3001/4000").newBuilder();
             String url = urlBuilder.build().toString();

             Request request = new Request.Builder().url(url).build();
             try {
                 Response response = client.newCall(request).execute();

                 Gson gson = new GsonBuilder().create();
                 JsonParser parser = new JsonParser();

                 JsonElement rootObject = parser.parse(response.body().charStream())
                         .getAsJsonObject().get("Grid_20200713000000000605_1").getAsJsonObject().get("row");

                 RestaurantItem[] posts = gson.fromJson(rootObject, RestaurantItem[].class);

                 for(int i = 0; i<1000; i++)
                 {
                     mArrayList.add(posts[i]);
                 }

                 return posts;

             } catch (Exception e) {
                 e.printStackTrace();
                 RestaurantItem[] a = new RestaurantItem[]{};
                 return a ;
             }
         }


         @Override
         protected void onPostExecute(RestaurantItem[] result) {
             super.onPostExecute(result);

             progressDialog.dismiss();
             // 요청 결과를 여기서 처리한다. 화면에 출력하기 등.
             //  Log.d("Result:", result.toString());

         }
     }

     public class MyAsyncTask_4001_5000 extends AsyncTask<String,Void, RestaurantItem[]>
     {
         ProgressDialog progressDialog = new ProgressDialog(NearRestaurantActivity.this);
         OkHttpClient client = new OkHttpClient();

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             progressDialog.setMessage("로딩 중...");
             progressDialog.show();
         }

         @Override
         protected RestaurantItem[] doInBackground(String... params) {

             HttpUrl.Builder urlBuilder = HttpUrl.parse("http://211.237.50.150:7080/openapi/cfcf7b6d1705a4a060a6ce134dbec9f80d183ae1737eec8f4eded265f478222b/json/Grid_20200713000000000605_1/4001/5000").newBuilder();
             String url = urlBuilder.build().toString();

             Request request = new Request.Builder().url(url).build();
             try {
                 Response response = client.newCall(request).execute();

                 Gson gson = new GsonBuilder().create();
                 JsonParser parser = new JsonParser();

                 JsonElement rootObject = parser.parse(response.body().charStream())
                         .getAsJsonObject().get("Grid_20200713000000000605_1").getAsJsonObject().get("row");

                 RestaurantItem[] posts = gson.fromJson(rootObject, RestaurantItem[].class);

                 for(int i = 0; i<1000; i++)
                 {
                     mArrayList.add(posts[i]);
                 }

                 return posts;

             } catch (Exception e) {
                 e.printStackTrace();
                 RestaurantItem[] a = new RestaurantItem[]{};
                 return a ;
             }
         }


         @Override
         protected void onPostExecute(RestaurantItem[] result) {
             super.onPostExecute(result);

             progressDialog.dismiss();
             // 요청 결과를 여기서 처리한다. 화면에 출력하기 등.
             //  Log.d("Result:", result.toString());

         }
     }

     public class MyAsyncTask_5001_6000 extends AsyncTask<String,Void, RestaurantItem[]>
     {
         ProgressDialog progressDialog = new ProgressDialog(NearRestaurantActivity.this);
         OkHttpClient client = new OkHttpClient();

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             progressDialog.setMessage("로딩 중...");
             progressDialog.show();
         }

         @Override
         protected RestaurantItem[] doInBackground(String... params) {

             HttpUrl.Builder urlBuilder = HttpUrl.parse("http://211.237.50.150:7080/openapi/cfcf7b6d1705a4a060a6ce134dbec9f80d183ae1737eec8f4eded265f478222b/json/Grid_20200713000000000605_1/5001/6000").newBuilder();
             String url = urlBuilder.build().toString();

             Request request = new Request.Builder().url(url).build();
             try {
                 Response response = client.newCall(request).execute();

                 Gson gson = new GsonBuilder().create();
                 JsonParser parser = new JsonParser();

                 JsonElement rootObject = parser.parse(response.body().charStream())
                         .getAsJsonObject().get("Grid_20200713000000000605_1").getAsJsonObject().get("row");

                 RestaurantItem[] posts = gson.fromJson(rootObject, RestaurantItem[].class);

                 for(int i = 0; i<1000; i++)
                 {
                     mArrayList.add(posts[i]);
                 }

                 return posts;

             } catch (Exception e) {
                 e.printStackTrace();
                 RestaurantItem[] a = new RestaurantItem[]{};
                 return a ;
             }
         }


         @Override
         protected void onPostExecute(RestaurantItem[] result) {
             super.onPostExecute(result);

             progressDialog.dismiss();
             // 요청 결과를 여기서 처리한다. 화면에 출력하기 등.
             //  Log.d("Result:", result.toString());

         }
     }

     public class MyAsyncTask_6001_6601 extends AsyncTask<String,Void, RestaurantItem[]>
     {
         ProgressDialog progressDialog = new ProgressDialog(NearRestaurantActivity.this);
         OkHttpClient client = new OkHttpClient();

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             progressDialog.setMessage("로딩 중...");
             progressDialog.show();
         }

         @Override
         protected RestaurantItem[] doInBackground(String... params) {

             HttpUrl.Builder urlBuilder = HttpUrl.parse("http://211.237.50.150:7080/openapi/cfcf7b6d1705a4a060a6ce134dbec9f80d183ae1737eec8f4eded265f478222b/json/Grid_20200713000000000605_1/6001/6601").newBuilder();
             String url = urlBuilder.build().toString();

             Request request = new Request.Builder().url(url).build();
             try {
                 Response response = client.newCall(request).execute();

                 Gson gson = new GsonBuilder().create();
                 JsonParser parser = new JsonParser();

                 JsonElement rootObject = parser.parse(response.body().charStream())
                         .getAsJsonObject().get("Grid_20200713000000000605_1").getAsJsonObject().get("row");

                 RestaurantItem[] posts = gson.fromJson(rootObject, RestaurantItem[].class);

                 for(int i = 0; i<1000; i++)
                 {
                     mArrayList.add(posts[i]);
                 }

                 return posts;

             } catch (Exception e) {
                 e.printStackTrace();
                 RestaurantItem[] a = new RestaurantItem[]{};
                 return a ;
             }
         }


         @Override
         protected void onPostExecute(RestaurantItem[] result) {
             super.onPostExecute(result);

             progressDialog.dismiss();
             // 요청 결과를 여기서 처리한다. 화면에 출력하기 등.
             //  Log.d("Result:", result.toString());

         }
     }
}
