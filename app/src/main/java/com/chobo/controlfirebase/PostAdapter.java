package com.chobo.controlfirebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{

    private ArrayList<Post> arrayList;
    private ArrayList<Post> arrayListSearched; // 검색 결과를 보관할 리스트
/*
    public void setFilter(ArrayList<Post> filteredList) {
        arrayListSearched.clear(); // arrayListSearched 초기화
        arrayListSearched.addAll(filteredList); // 필터링된 리스트를 arrayListSearched에 추가
        notifyDataSetChanged(); // RecyclerView 갱신
    }
*/
    public PostAdapter(ArrayList<Post> arrayList) {
        this.arrayList = arrayList;
        this.arrayListSearched = new ArrayList<>(arrayList); // 검색 결과 리스트 초기화
    }
    //실제 list가 연결될 다음 처음으로 ViewHolder를 만들어 낸다.
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent, false);
        PostViewHolder holder = new PostViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.tv_uid.setText(arrayList.get(position).getUID());
        holder.tv_text.setText(arrayList.get(position).getText());
        holder.tv_time.setText(arrayList.get(position).getTime()); //현재 position에 있는 것을 가져와서

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tv_uid;
        TextView tv_text;
        TextView tv_time;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_uid = itemView.findViewById(R.id.tv_uid);
            this.tv_text = itemView.findViewById(R.id.tv_text);
            this.tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}