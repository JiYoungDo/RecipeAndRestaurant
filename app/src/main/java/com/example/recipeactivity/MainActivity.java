package com.example.recipeactivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipeactivity.recipe.ListActivity;
import com.example.recipeactivity.recipe.RecipeItem;
import com.example.recipeactivity.restaurant.NearRestaurantActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    ImageButton list_btn;
    ImageButton search_btn;


    // SearchResultActivity 로 보낼 값들 : 7개고 모두 스트링
    String recipe_nm_ko, summary, cooking_time, calorie, pc_nm, det_url, img;
    String strNickname;
    String strId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();



        mContext = getApplicationContext();
        // 해시키 가져오기 _ 카카오에서 필요해서 알아보기 위해 썼다.
        // getHashKey(mContext);

        MyAsyncTask mProcessTask = new MyAsyncTask();
        mProcessTask.execute();


        TextView tvNickname = findViewById(R.id.main_tv_user_id);
        Intent intent = getIntent();
        strNickname = intent.getStringExtra("name");

        tvNickname.setText(strNickname);

        Button btnLogout = findViewById(R.id.main_btn_logout);
        btnLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"정상적으로 로그아웃 되었습니다.",Toast.LENGTH_SHORT).show();
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(MainActivity.this, KakaoLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

            }
        });

        list_btn = findViewById(R.id.main_list_btn);
        search_btn = findViewById(R.id.main_search_btn);

        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.clickblink);
                list_btn.startAnimation(startAnimation);

                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("name",strNickname);
                startActivity(intent);
                finish();
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.clickblink);
                search_btn.startAnimation(startAnimation);

                Intent intent = new Intent(MainActivity.this, NearRestaurantActivity.class);
                intent.putExtra("name",strNickname);
                startActivity(intent);
                finish();
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    // AsyncTask 생성 - 모든 네트워크 로직을 여기서 작성 한다.
    public class MyAsyncTask extends AsyncTask<String, Void, RecipeItem[]> {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
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
            urlBuilder.addQueryParameter("RECIPE_NM_KO", "오곡밥");    // 여기 value 에 사용자 입력 edit 이 들어가야 하는데
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();

                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();

                JsonElement rootObject = parser.parse(response.body().charStream())
                        .getAsJsonObject().get("Grid_20150827000000000226_1").getAsJsonObject().get("row");

                RecipeItem[] posts = gson.fromJson(rootObject, RecipeItem[].class);
                return posts;

            } catch (Exception e) {
                e.printStackTrace();
                RecipeItem[] a = new RecipeItem[]{};
                return a;
            }
        }


        @Override
        protected void onPostExecute(RecipeItem[] result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            // 요청 결과를 여기서 처리한다. 화면에 출력하기 등.
            //  Log.d("Result:", result.toString());
            if (result.length > 0) {
                for (RecipeItem post : result) {
                    recipe_nm_ko = String.valueOf(post.getRECIPE_NM_KO() + "");
                    summary = String.valueOf(post.getSUMRY() + "");
                    cooking_time = String.valueOf(post.getCOOKING_TIME() + "");
                    calorie = String.valueOf(post.getCALORIE() + "");
                    pc_nm = String.valueOf(post.getPC_NM() + "");
                    det_url = String.valueOf(post.getDET_URL() + "");
                    img = String.valueOf(post.getIMG_URL() + "");
                }
            }
        }
    }


    @Nullable
    public static String getHashKey(Context context) {

        final String TAG = "KeyHash";

        String keyHash = null;

        try {
            PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = new String(Base64.encode(md.digest(), 0));
                Log.d(TAG, keyHash);
            }

        } catch (Exception e) {
            Log.e("name not found", e.toString());
        }

        if (keyHash != null) {
            return keyHash;

        } else {
            return null;
        }

    }

}



