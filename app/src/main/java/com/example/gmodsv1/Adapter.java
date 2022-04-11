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
    public Adapter(List<ModelClass> userlist,RecyclerViewClickListener listener)
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

        String upvoteCount=userlist.get(position).getUpvoteCount();
        String replyCount=userlist.get(position).getReplyCount();

        String timeDelta=userlist.get(position).getTimeDelta();

        holder.setData(resource,name,msg,comment,upvoteCount,replyCount, timeDelta);

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

        private TextView upvoteCountText;
        private TextView upvoteDisplayText;

        private TextView replyCountText;
        private TextView replyDisplayText;

        private TextView timeDeltaText;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.pfpIcon);
            textView=itemView.findViewById(R.id.usernameText);
            textView2=itemView.findViewById(R.id.documentId);
            textView3=itemView.findViewById(R.id.contentText);

            upvoteCountText=itemView.findViewById(R.id.upvoteCountText);
            upvoteDisplayText=itemView.findViewById(R.id.upvoteDisplayText);

            replyCountText=itemView.findViewById(R.id.replyCountText);
            replyDisplayText=itemView.findViewById(R.id.replyDisplayText);

            timeDeltaText=itemView.findViewById(R.id.timeDeltaThreadListText);

            itemView.setOnClickListener(this);


        }

        public void setData(int resource, String name, String msg, String comment, String upvoteCount, String replyCount, String timeDelta) {
            imageView.setImageResource(resource);
            textView.setText(name);
            textView3.setText(msg);
            textView2.setText(comment);

            upvoteCountText.setText(upvoteCount);
            if(upvoteCount.equals("1")) {
                upvoteDisplayText.setText("Upvote");
            }
            else {
                upvoteDisplayText.setText("Upvotes");
            }

            replyCountText.setText(replyCount);
            if(replyCount.equals("1")) {
                replyDisplayText.setText("Reply");
            }
            else {
                replyDisplayText.setText("Replies");
            }
            timeDeltaText.setText(timeDelta);
        }
        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());


        }




    }
}
