package org.techtowm.retrofit2.Data;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

/*
DTO Class
: 계층 간 데이터 교환을 위한 객체(Java Beans)
DB 에서 데이터를 얻어 Service 나 Controller 등으로 보낼떄 사용
로직을 갖지 않는 순수한 객체.
 */

public class UserDTO {
    @SerializedName("userID")
    private String ID;

    @SerializedName("userPassword")
    private String Password;

    @SerializedName("userName")
    private String Name;

    @SerializedName("userAge")
    private Integer Age;

    @SerializedName("userIdx")
    private Integer Idx;

    @SerializedName("distance")
    private Integer Distance;

    @SerializedName("speed")
    private Double Speed;

    @SerializedName("time")
    private Integer Time;

    @SerializedName("date")
    private Date date;

    @SerializedName("mode")
    private Integer mode;

    @SerializedName("no")
    private Integer recordNo;

    @SerializedName("userToken")
    private Integer token;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getAge() {
        return Age;
    }

    public void setAge(Integer age) {
        Age = age;
    }

    public Integer getIdx() {
        return Idx;
    }

    public void setIdx(Integer idx) {
        Idx = idx;
    }

    public Integer getDistance() {
        return Distance;
    }

    public void setDistance(Integer distance) {
        Distance = distance;
    }

    public Double getSpeed() {
        return Speed;
    }

    public void setSpeed(Double speed) {
        Speed = speed;
    }

    public Integer getTime() {
        return Time;
    }

    public void setTime(Integer time) {
        Time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(Integer recordNo) {
        this.recordNo = recordNo;
    }

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

}