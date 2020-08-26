package com.example.recipeactivity.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recipeactivity.R;
import com.example.recipeactivity.recipe.RecyclerViewAdapter;

import java.util.ArrayList;

class RestaurantViewAdapter extends RecyclerView.Adapter<RestaurantViewAdapter.RestaurantViewHolder> {

    private ArrayList<RestaurantItem> mList;
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
    private RestaurantViewAdapter.OnItemClickListener mListener = null;
    private RestaurantViewAdapter.OnItemLongClickListener mLongListener = null;
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
    public class RestaurantViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView RELAX_RSTRNT_NM; //식당이름
        protected TextView RELAX_ADD1;  //주소
        protected TextView RELAX_GUBUN_DETAIL;  // 음식 종류
        protected TextView RELAX_RSTRNT_TEL;    // 전화번호

        public RestaurantViewHolder(View view)
        {
            super(view);
            this.RELAX_RSTRNT_NM =(TextView)view.findViewById(R.id.relax_rstrnt_nm);
            this.RELAX_ADD1 =(TextView)view.findViewById(R.id.relax_add1);
            this.RELAX_GUBUN_DETAIL =(TextView)view.findViewById(R.id.relax_gubun_detail);
            this.RELAX_RSTRNT_TEL =(TextView)view.findViewById(R.id.relax_rstrnt_tel);

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

    // 생성자에서 List 객체를 전달 :: 액티비티에서 정말 Recipe 담겨있는 List 가 들어온다.
    public RestaurantViewAdapter(ArrayList<RestaurantItem> list) {this.mList = list; }


    // 아이템 뷰를 위한 뷰홀더 객체 생성해서 리턴한다.
    @Override
    public RestaurantViewAdapter.RestaurantViewHolder onCreateViewHolder( ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.restaurant_item,viewGroup,false);
       RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);
        this.context =viewGroup.getContext();
        return viewHolder;
    }


    // position 에 해당하는 데이터를 뷰홀더의 아이템 뷰에 표시한다.
    @Override
    public void onBindViewHolder(@NonNull RestaurantViewAdapter.RestaurantViewHolder viewholder, int position) {
        viewholder.RELAX_RSTRNT_NM.setText(mList.get(position).getRELAX_RSTRNT_NM());
        viewholder.RELAX_ADD1.setText(mList.get(position).getRELAX_ADD1());
        viewholder.RELAX_GUBUN_DETAIL.setText(mList.get(position).getRELAX_GUBUN_DETAIL());
        viewholder.RELAX_RSTRNT_TEL.setText(mList.get(position).getRELAX_RSTRNT_TEL());

    }

    @Override
    public int getItemCount() {
        // mList 비어 있으면 0리턴, 아니면 mList 사이즈 리턴
        return (null != mList ? mList.size():0);

    }
}
