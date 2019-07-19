package com.codebosses.flicks.pojo.user;

public class UserModel {

    private String userId, userName, userEmail, userPhone, userGender, userProfileImage, userProfileImageThumbnail, userAccountType;

    public UserModel(String userId, String userName, String userEmail, String userPhone, String userGender, String userProfileImage, String userProfileImageThumbnail, String userAccountType) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userGender = userGender;
        this.userProfileImage = userProfileImage;
        this.userProfileImageThumbnail = userProfileImageThumbnail;
        this.userAccountType = userAccountType;
    }

    public UserModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getUserProfileImageThumbnail() {
        return userProfileImageThumbnail;
    }

    public void setUserProfileImageThumbnail(String userProfileImageThumbnail) {
        this.userProfileImageThumbnail = userProfileImageThumbnail;
    }

    public String getUserAccountType() {
        return userAccountType;
    }

    public void setUserAccountType(String userAccountType) {
        this.userAccountType = userAccountType;
    }
}
