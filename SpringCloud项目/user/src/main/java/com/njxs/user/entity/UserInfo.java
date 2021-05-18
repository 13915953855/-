package com.njxs.user.entity;

import java.io.Serializable;

/**
 * (UserInfo)实体类
 *
 * @author makejava
 * @since 2021-05-18 14:26:00
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -12550773175387644L;
    
    private Integer id;
    
    private String account;
    
    private Integer age;
    
    private String email;
    
    private Integer level;
    
    private String levelName;
    
    private String name;
    
    private String passwd;
    
    private String staffNumber;
    
    private String status;
    
    private String telephone;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

}