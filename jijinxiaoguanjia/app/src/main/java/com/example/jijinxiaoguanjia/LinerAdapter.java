package com.example.jijinxiaoguanjia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LinerAdapter extends RecyclerView.Adapter<LinerAdapter.LinerViewHolder> {

    private Context mContext;

    public LinerAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public LinerAdapter.LinerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_liner_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinerAdapter.LinerViewHolder holder, final int position) {
        holder.textView.setText("i love you !!!");
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"click" + position,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    class LinerViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        public LinerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.rv_item);
        }
    }
}
