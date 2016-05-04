package com.srinivas.ele.pojo;

/**
 * Created by Mudavath Srinivas on 02-05-2016.
 */
public class CallLogItem {

    private String number;
    private long date;
    private String inCallDuration;
    private String outGoingDuration;
    private String noOfInCalls="0";
    private String noOfOutGoingCalls="0";
    private String noOfMissCalls="0";
    private String noOfBlockedCalls="0";

    public CallLogItem(String number, long date, String inCallDuration, String outGoingDuration, String noOfInCalls, String noOfOutGoingCalls, String noOfMissCalls, String noOfBlockedCalls) {
        this.number = number;
        this.date = date;
        this.inCallDuration = inCallDuration;
        this.outGoingDuration = outGoingDuration;
        this.noOfInCalls = noOfInCalls;
        this.noOfOutGoingCalls = noOfOutGoingCalls;
        this.noOfMissCalls = noOfMissCalls;
        this.noOfBlockedCalls = noOfBlockedCalls;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getInCallDuration() {
        return inCallDuration;
    }

    public void setInCallDuration(String inCallDuration) {
        this.inCallDuration = inCallDuration;
    }

    public String getOutGoingDuration() {
        return outGoingDuration;
    }

    public void setOutGoingDuration(String outGoingDuration) {
        this.outGoingDuration = outGoingDuration;
    }

    public String getNoOfInCalls() {
        return noOfInCalls;
    }

    public void setNoOfInCalls(String noOfInCalls) {
        this.noOfInCalls = noOfInCalls;
    }

    public String getNoOfOutGoingCalls() {
        return noOfOutGoingCalls;
    }

    public void setNoOfOutGoingCalls(String noOfOutGoingCalls) {
        this.noOfOutGoingCalls = noOfOutGoingCalls;
    }

    public String getNoOfMissCalls() {
        return noOfMissCalls;
    }

    public void setNoOfMissCalls(String noOfMissCalls) {
        this.noOfMissCalls = noOfMissCalls;
    }

    public String getNoOfBlockedCalls() {
        return noOfBlockedCalls;
    }

    public void setNoOfBlockedCalls(String noOfBlockedCalls) {
        this.noOfBlockedCalls = noOfBlockedCalls;
    }
}
