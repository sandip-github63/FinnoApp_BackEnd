package com.finnoapp.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "enable")
    private boolean enable = true;

    @Column(name = "about")
    private String about;

    @Column(name = "password")
    private String password;

    @Column(name = "profile")
    private String profile;

    @Column(name = "otp")
    private String otp;

    @Column(name = "otpExpirationTime")
    private LocalDateTime otpExpirationTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    Set<UserRole> uRole = new HashSet<>();

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public User(Long userId, String firstName, String lastName, String email, String userName, String phone,
                boolean enable, String about, String password, String profile, Set<UserRole> uRole) {
        super();
        System.out.println("inside user parametrized constructor...");
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.phone = phone;
        this.enable = enable;
        this.about = about;
        this.password = password;
        this.profile = profile;
        this.uRole = uRole;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Set<UserRole> getuRole() {
        return uRole;
    }

    public void setuRole(Set<UserRole> uRole) {
        this.uRole = uRole;
    }

    public User() {
        super();
        System.out.println("inside user default construction....");
    }

    public User(String firstName, String lastName, String email, String userName, String phone, boolean enable,
                String about, String password, String profile) {
        super();
        System.out.println("inside user parametrized constructor...");
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.phone = phone;
        this.enable = enable;
        this.about = about;
        this.password = password;
        this.profile = profile;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpExpirationTime() {
        return otpExpirationTime;
    }

    public void setOtpExpirationTime(LocalDateTime otpExpirationTime) {
        this.otpExpirationTime = otpExpirationTime;
    }

}
