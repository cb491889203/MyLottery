package com.coconut.mylottery.bean;

/**
 * 记录用户的用户名和密码的javabean.
 * Created by Administrator on 2016/6/23 0023.
 */
public class User {
    private String username;
    private String password;
    private String nickname;
    private String mail;
    private String phone;
    private String recommentuser;

    public User() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRecommentuser() {
        return recommentuser;
    }

    public void setRecommentuser(String recommentuser) {
        this.recommentuser = recommentuser;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
