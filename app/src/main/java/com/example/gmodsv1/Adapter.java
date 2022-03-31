package com.example.gmodsv1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private RecyclerViewClickListener listener;
    private List<ModelClass> userlist;
    public Adapter(List<ModelClass>userlist,RecyclerViewClickListener listener)
    {
        this.userlist=userlist;
        this.listener=listener;
    }
    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.thread_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        int resource =userlist.get(position).getImageview1();
        String name=userlist.get(position).getTextview1();
        String msg=userlist.get(position).getTextview3();
        String comment=userlist.get(position).getTextview2();

        holder.setData(resource,name,msg,comment);

    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }
    public interface RecyclerViewClickListener{
        void onClick(View v,int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageView;
        private TextView textView;
        private TextView textView2;
        private TextView textView3;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageview1);
            textView=itemView.findViewById(R.id.textview);
            textView2=itemView.findViewById(R.id.textview2);
            textView3=itemView.findViewById(R.id.textview3);
            itemView.setOnClickListener(this);


        }

        public void setData(int resource, String name, String msg, String comment) {
            imageView.setImageResource(resource);
            textView.setText(name);
            textView3.setText(msg);
            textView2.setText(comment);
        }
        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());


        }




    }
}
