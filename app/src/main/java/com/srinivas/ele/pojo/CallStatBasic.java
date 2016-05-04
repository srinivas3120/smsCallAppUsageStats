package com.srinivas.ele.pojo;

/**
 * Created by Mudavath Srinivas on 03-05-2016.
 */
public class CallStatBasic {

    private String inCallDuration;
    private String outGoingDuration;
    private String noOfInCalls="0";
    private String noOfOutGoingCalls="0";
    private String noOfMissCalls="0";
    private String noOfBlockedCalls="0";

    public CallStatBasic(String inCallDuration, String outGoingDuration, String noOfInCalls, String noOfOutGoingCalls) {
        this.inCallDuration = inCallDuration;
        this.outGoingDuration = outGoingDuration;
        this.noOfInCalls = noOfInCalls;
        this.noOfOutGoingCalls = noOfOutGoingCalls;
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
}
