package com.srinivas.ele.pojo;

/**
 * Created by Mudavath Srinivas on 03-05-2016.
 */
public class SmsStatBasic {

    private String noOfInSMSs="0";
    private String noOfOutSMSs="0";

    public SmsStatBasic(String noOfInSMSs, String noOfOutSMSs) {
        this.noOfInSMSs = noOfInSMSs;
        this.noOfOutSMSs = noOfOutSMSs;
    }

    public String getNoOfInSMSs() {
        return noOfInSMSs;
    }

    public void setNoOfInSMSs(String noOfInSMSs) {
        this.noOfInSMSs = noOfInSMSs;
    }

    public String getNoOfOutSMSs() {
        return noOfOutSMSs;
    }

    public void setNoOfOutSMSs(String noOfOutSMSs) {
        this.noOfOutSMSs = noOfOutSMSs;
    }
}
