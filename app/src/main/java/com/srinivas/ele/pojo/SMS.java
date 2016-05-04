
package com.srinivas.ele.pojo;

public class SMS
{

    public static final int RECEIVED_SMS = 1;
    public static final int SEND_SMS = 2;
    private long date;
    private long id;
    private NumberC number;
    private int type;

    public SMS(long date, long id, NumberC number, int type) {
        this.date = date;
        this.id = id;
        this.number = number;
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public NumberC getNumber() {
        return number;
    }

    public void setNumber(NumberC number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
