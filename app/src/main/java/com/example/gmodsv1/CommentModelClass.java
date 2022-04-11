package com.example.gmodsv1;

public class CommentModelClass {
    private int pfpIcon;
    private String usernameText;
    private String contentText;
    private String documentId;
    private Integer upvoteCount;
    private boolean upvoted;
    private String timeStr;

    CommentModelClass(int pfpIcon, String usernameText, String contentText, String documentId, int upvoteCount, boolean upvoted, String timeStr){
        this.pfpIcon=pfpIcon;
        this.usernameText=usernameText;
        this.contentText=contentText;
        this.documentId=documentId;
        this.upvoteCount=upvoteCount;
        this.upvoted=upvoted;
        this.timeStr=timeStr;
    }

    public int getPfpIcon() {
        return pfpIcon;
    }

    public String getUsernameText() {
        return usernameText;
    }

    public String getContentText() {
        return contentText;
    }

    public String getDocumentId() {
        return documentId;
    }

    public Integer getUpvoteCount() {
        return upvoteCount;
    }

    public Boolean getUpvoted() {
        return upvoted;
    }

    public String getTimeStr() { return timeStr; }
}
