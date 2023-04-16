package org.techtowm.retrofit2.ui.login;

public class LoginModel {
    private String id;
    private Integer idx;
    private String name;

    public LoginModel() {

    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
