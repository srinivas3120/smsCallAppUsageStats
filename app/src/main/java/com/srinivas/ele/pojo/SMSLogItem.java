package com.srinivas.ele.pojo;

/**
 * Created by Mudavath Srinivas on 03-05-2016.
 */
public class SMSLogItem {

    private String number;
    private long date;
    private String noOfInSMSs="0";
    private String noOfOutSMSs="0";

    public SMSLogItem(String number, long date, String noOfInSMSs, String noOfOutSMSs) {
        this.number = number;
        this.date = date;
        this.noOfInSMSs = noOfInSMSs;
        this.noOfOutSMSs = noOfOutSMSs;
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
