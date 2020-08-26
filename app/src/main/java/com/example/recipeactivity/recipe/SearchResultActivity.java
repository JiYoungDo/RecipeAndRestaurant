package com.example.recipeactivity.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.recipeactivity.R;

public class SearchResultActivity extends AppCompatActivity {

    TextView RECIPE_NM_KO;
    TextView SUMRY;
    TextView COOKING_TIME;
    TextView CALORIE;
    TextView PC_NM;
    TextView DET_URL;
    ImageView IMG;
    String recipe_nm_ko, summary, cooking_time, calorie, pc_nm, det_url, img;

    ImageButton back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        getSupportActionBar().hide();

        back_btn= findViewById(R.id.search_result_back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchResultActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        RECIPE_NM_KO = findViewById(R.id.search_result_recipe_name);
        SUMRY = findViewById(R.id.search_result_recipe_summary);
        COOKING_TIME = findViewById(R.id.search_result_recipe_time);
        CALORIE = findViewById(R.id.search_result_recipe_calories);
        PC_NM = findViewById(R.id.search_result_recipe_price);
        DET_URL = findViewById(R.id.search_result_recipe_detail_url);
        IMG = findViewById(R.id.search_result_recipe_img_url);

        Intent intent = getIntent();
        recipe_nm_ko = intent.getStringExtra("recipe_nm_ko");
        summary = intent.getStringExtra("summary");
        cooking_time = intent.getStringExtra("cooking_time");
        calorie = intent.getStringExtra("calorie");
        pc_nm = intent.getStringExtra("pc_nm");
        det_url = intent.getStringExtra("det_url");
        img = intent.getStringExtra("img"); //img url string 값으로

        RECIPE_NM_KO.setText(recipe_nm_ko);
        SUMRY.setText(summary);
        COOKING_TIME.setText(cooking_time);
        CALORIE.setText(calorie);
        PC_NM.setText(pc_nm);
        DET_URL.setText(det_url);

        Glide.with(this).load(img).into(IMG);

        DET_URL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(det_url));
                startActivity(intent);
            }
        });


    }
}
