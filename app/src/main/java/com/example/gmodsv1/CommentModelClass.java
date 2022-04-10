package com.example.gmodsv1;

public class CommentModelClass {
    private int pfpIcon;
    private String usernameText;
    private String contentText;
    private String documentId;
    private Integer upvoteCount;
    private boolean upvoted;

    CommentModelClass(int pfpIcon, String usernameText, String contentText, String documentId, int upvoteCount, boolean upvoted){
        this.pfpIcon=pfpIcon;
        this.usernameText=usernameText;
        this.contentText=contentText;
        this.documentId=documentId;
        this.upvoteCount=upvoteCount;
        this.upvoted=upvoted;
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
}
