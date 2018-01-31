package com.uni.redcarpet.models;


public class CheckIn implements Comparable<CheckIn>{
    public String checkedInEvent;
    public String checkUserNumber;
    public String checkUserName;
    public String checkUserImage;
    public long checkInTimestamp;

    public CheckIn(){

    }

    public CheckIn(String checkedInEvent, String checkUserNumber, String checkUserName, String checkUserImage, long checkInTimestamp){
        this.checkedInEvent = checkedInEvent;
        this.checkUserNumber = checkUserNumber;
        this.checkUserName = checkUserName;
        this.checkUserImage = checkUserImage;
        this.checkInTimestamp = checkInTimestamp;

    }

    @Override
    public int compareTo(CheckIn that) {
        if (this.checkInTimestamp > that.checkInTimestamp) return 1;
        if (this.checkInTimestamp < that.checkInTimestamp) return -1;
        return 0;
    }
}
