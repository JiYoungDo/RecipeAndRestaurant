package com.example.recipeactivity.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.recipeactivity.MainActivity;
import com.example.recipeactivity.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListActivity extends AppCompatActivity {

    private ArrayList<RecipeItem> mArrayList;
    private RecyclerViewAdapter mAdapter;
    ImageButton back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().hide();

        back_btn= findViewById(R.id.list_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();
        // mArrayList 에 json 형태들의 Recipe 아이템들을 담아야 한다.

        ListActivity.MyAsyncTask mProcessTask = new ListActivity.MyAsyncTask();
        mProcessTask.execute();


        mAdapter = new RecyclerViewAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

      mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
          String recipe_nm_ko, summary, cooking_time, calorie, pc_nm, det_url, img;
          @Override
          public void onItemClick(View v, int pos) {
              Intent intent = new Intent(ListActivity.this, SearchResultActivity.class);

              RecipeItem recipeitem = mArrayList.get(pos);
              recipe_nm_ko = recipeitem.getRECIPE_NM_KO();
              summary = recipeitem.getSUMRY();
              cooking_time = recipeitem.getCOOKING_TIME();
              calorie =recipeitem.getCALORIE();
              pc_nm = recipeitem.getPC_NM();
              det_url = recipeitem.getDET_URL();
              img = recipeitem.getIMG_URL();

              intent.putExtra("recipe_nm_ko",recipe_nm_ko);
              intent.putExtra("summary",summary);
              intent.putExtra("cooking_time",cooking_time);
              intent.putExtra("calorie",calorie);
              intent.putExtra("pc_nm",pc_nm);
              intent.putExtra("det_url",det_url);
              intent.putExtra("img",img);

              startActivity(intent);
              finish();

          }
      });

      mAdapter.setOnItemLongClickListener(new RecyclerViewAdapter.OnItemLongClickListener() {
          @Override
          public void onItemLongClick(View v, int pos) {

          }
      });

    }


    public class MyAsyncTask extends AsyncTask<String,Void,RecipeItem[]>
    {
        ProgressDialog progressDialog = new ProgressDialog(ListActivity.this);
        OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로딩 중...");
            progressDialog.show();
        }

        @Override
        protected RecipeItem[] doInBackground(String... params) {

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://211.237.50.150:7080/openapi/cfcf7b6d1705a4a060a6ce134dbec9f80d183ae1737eec8f4eded265f478222b/json/Grid_20150827000000000226_1/1/537").newBuilder();
            //urlBuilder.addQueryParameter("RECIPE_NM_KO", "오곡밥");
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();

                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();

                JsonElement rootObject = parser.parse(response.body().charStream())
                        .getAsJsonObject().get("Grid_20150827000000000226_1").getAsJsonObject().get("row");

                RecipeItem[] posts = gson.fromJson(rootObject, RecipeItem[].class);

                for(int i = 0; i<537; i++)
                {
                    mArrayList.add(posts[i]);
                }

                return posts;

            } catch (Exception e) {
                e.printStackTrace();
                RecipeItem[] a = new RecipeItem[]{};
                return a ;
            }
        }


        @Override
        protected void onPostExecute(RecipeItem[] result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            // 요청 결과를 여기서 처리한다. 화면에 출력하기 등.
            //  Log.d("Result:", result.toString());

        }
    }
}
