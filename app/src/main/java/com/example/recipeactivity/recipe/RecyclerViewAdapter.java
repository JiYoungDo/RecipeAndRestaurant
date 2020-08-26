package com.example.recipeactivity.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipeactivity.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>
{
    private ArrayList<RecipeItem> mList;
    Context context; // 나중에 this 로 context 에 접근할 수 없어서 추가했다.

    // 리스너 인터페이스 정의하기
    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos);
    }
    public interface OnItemLongClickListener
    {
        void onItemLongClick(View v, int pos);
    }

    // 리스너 객체를 전달하는 메서드와 전달된 객체를 저장할 변수 추가
    //전달된 객체 저장할 변수
    private OnItemClickListener mListener = null;
    private OnItemLongClickListener mLongListener = null;
    // 리스너 객체 전달 메서드
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener)
    {
        this.mLongListener = listener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView RECIPE_NM_KO;
        protected TextView SUMRY;
        protected TextView COOKING_TIME;
        protected TextView CALORIE;
        protected ImageView IMG;

        public RecyclerViewHolder(View view)
        {
            super(view);
            this.RECIPE_NM_KO =(TextView)view.findViewById(R.id.list_recipe_name);
            this.SUMRY =(TextView)view.findViewById(R.id.list_recipe_summary);
            this.COOKING_TIME =(TextView)view.findViewById(R.id.list_recipe_time);
            this.CALORIE =(TextView)view.findViewById(R.id.list_recipe_calories);
            this.IMG =(ImageView) view.findViewById(R.id.list_recipe_img_url);

            // 뷰홀더가 만들어지는 시점에 클릭 이벤트를 처리해야 한다.
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION)
                    {
                        mListener.onItemClick(v,pos);
                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION)
                    {
                        mLongListener.onItemLongClick(v,pos);
                    }
                    return true;
                }
            });
        }
    }

    // 생성자에서 List 객체를 전달 :: 액티비티에서 정말 Recipe 담겨있는 List가 들어온다.
    public RecyclerViewAdapter(ArrayList<RecipeItem> list) {this.mList = list; }


    // 아이템 뷰를 위한 뷰홀더 객체 생성해서 리턴한다.
    @Override
    public RecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder( ViewGroup viewGroup, int viewType) {
       View view = LayoutInflater.from(viewGroup.getContext())
               .inflate(R.layout.list_item,viewGroup,false);
       RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
       this.context =viewGroup.getContext();
       return viewHolder;
    }


    // position 에 해당하는 데이터를 뷰홀더의 아이템 뷰에 표시한다.
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.RecyclerViewHolder viewholder, int position) {
        viewholder.RECIPE_NM_KO.setText(mList.get(position).getRECIPE_NM_KO());
        viewholder.SUMRY.setText(mList.get(position).getSUMRY());
        viewholder.COOKING_TIME.setText(mList.get(position).getCOOKING_TIME());
        viewholder.CALORIE.setText(mList.get(position).getCALORIE());
        String imgurl = mList.get(position).getIMG_URL();
        Glide.with(this.context).load(imgurl).into(viewholder.IMG); // this.context 쓰려구 필드에 context 추가함.

    }

    @Override
    public int getItemCount() {
        // mList 비어 있으면 0리턴, 아니면 mList 사이즈 리턴
        return (null != mList ? mList.size():0);

    }
}


