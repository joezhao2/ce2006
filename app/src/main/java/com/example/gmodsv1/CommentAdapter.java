package com.example.gmodsv1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private RecyclerViewClickListener listener;
    private List<CommentModelClass> commentlist;
    public CommentAdapter(List<CommentModelClass>userlist, RecyclerViewClickListener listener)
    {
        this.commentlist =userlist;
        this.listener=listener;
    }
    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        int imageId = commentlist.get(position).getPfpIcon();
        String username= commentlist.get(position).getUsernameText();
        String id= commentlist.get(position).getDocumentId();
        String content= commentlist.get(position).getContentText();

        Integer upvoteCount= commentlist.get(position).getUpvoteCount();
        Boolean upvoted= commentlist.get(position).getUpvoted();
        String timeStr= commentlist.get(position).getTimeStr();
        holder.setData(imageId, username, id, content, upvoteCount, upvoted, timeStr);

    }

    @Override
    public int getItemCount() {
        return commentlist.size();
    }

    public void clear() {
        commentlist.clear();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v,int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView pfpIcon;
        private TextView usernameText;
        private TextView documentId;
        private TextView contentText;

        private ImageView upvoteOffIcon;
        private ImageView upvoteOnIcon;
        private TextView upvoteCountText;

        private TextView timeDeltaText;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pfpIcon=itemView.findViewById(R.id.pfpIcon);
            usernameText=itemView.findViewById(R.id.usernameText);
            documentId=itemView.findViewById(R.id.documentId);
            contentText=itemView.findViewById(R.id.contentText);

            upvoteOffIcon=itemView.findViewById((R.id.upvoteOffIcon));
            upvoteOnIcon=itemView.findViewById((R.id.upvoteOnIcon));
            upvoteCountText=itemView.findViewById((R.id.upvoteCountText));
            timeDeltaText=itemView.findViewById((R.id.timeDeltaText));
            itemView.setOnClickListener(this);


        }

        public void setData(int imageId, String username, String id, String content, Integer upvoteCount, Boolean upvoted, String timeStr) {
            pfpIcon.setImageResource(imageId);
            usernameText.setText(username);
            contentText.setText(content);
            documentId.setText(id);

            if(upvoted) {
                upvoteOffIcon.setVisibility(View.GONE);
                upvoteOnIcon.setVisibility(View.VISIBLE);
            }
            else {
                upvoteOffIcon.setVisibility(View.VISIBLE);
                upvoteOnIcon.setVisibility(View.GONE);
            }
            upvoteCountText.setText(upvoteCount.toString());
            timeDeltaText.setText(timeStr);
        }
        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());
        }




    }
}
