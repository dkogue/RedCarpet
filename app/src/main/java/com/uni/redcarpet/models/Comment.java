package com.uni.redcarpet.models;


public class Comment implements Comparable<Comment>{
    public String commentedEvent;
    public String commenterName;
    public String commenterImage;
    public String commentMessage;
    public long commentTimestamp;

    public Comment(){

    }

    public Comment(String commentedEvent, String commenterName, String commenterImage, String commentMessage, long commentTimestamp){
        this.commentedEvent = commentedEvent;
        this.commenterName = commenterName;
        this.commenterImage = commenterImage;
        this.commentMessage = commentMessage;
        this.commentTimestamp = commentTimestamp;

    }

    @Override
    public int compareTo(Comment that) {
        if (this.commentTimestamp > that.commentTimestamp) return 1;
        if (this.commentTimestamp < that.commentTimestamp) return -1;
        return 0;
    }
}
