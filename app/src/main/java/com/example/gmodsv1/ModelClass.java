package com.example.gmodsv1;

import android.net.Uri;

public class ModelClass {
    private int imageview1;
    private String textview1;
    private String textview2;
    private String textview3;

    private String upvoteCount;
    private String replyCount;

    private String timeDelta;
    private boolean hasPic;
    private Uri uri;


    ModelClass(int imageview1,String textview1,String textview2,String textview3,String upvoteCount,String replyCount,String timeDelta){
        this.imageview1=imageview1;
        this.textview1=textview1;
        this.textview2=textview2;
        this.textview3=textview3;
        this.upvoteCount=upvoteCount;
        this.replyCount=replyCount;
        this.timeDelta=timeDelta;

        this.hasPic=false;
    }

    ModelClass(Uri uri, String textview1, String textview2, String textview3, String upvoteCount, String replyCount, String timeDelta){
        this.uri=uri;
        this.textview1=textview1;
        this.textview2=textview2;
        this.textview3=textview3;
        this.upvoteCount=upvoteCount;
        this.replyCount=replyCount;
        this.timeDelta=timeDelta;

        this.hasPic=true;
    }

    public int getImageview1() {
        return imageview1;
    }

    public String getTextview1() {
        return textview1;
    }

    public String getTextview2() {
        return textview2;
    }

    public String getTextview3() {
        return textview3;
    }

    public String getUpvoteCount() { return upvoteCount; }

    public String getReplyCount() { return replyCount; }

    public String getTimeDelta() { return timeDelta; }

    public Uri getUri() { return uri; }

    public boolean getHasPic() { return hasPic; }
}
