package model;

import java.util.List;

/**
 * User Model.
 * A data structure for storing a user.
 * @author Peizhi Shao
 */
public class User {
    private String userId;
    private String userName;
    private String userType;
    private List<Project> userProjects;
    private String userDir;

    public User(String userId, String userName, String userType, List<Project> userProjects) {
        this.userId = userId;
        this.userName = userName;
        this.userType = userType;
        this.userProjects = userProjects;
        this.userDir = userName;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<Project> getUserProjects() {
        return userProjects;
    }

    public void setUserProjects(List<Project> userProjects) {
        this.userProjects = userProjects;
    }

    public String getUserDir() {
        return userDir;
    }

    public void setUserDir(String userDir) {
        this.userDir = userDir;
    }

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", userName=" + userName + ", userType=" + userType + ", userProjects=" + userProjects + ", userDir=" + userDir + '}';
    }
   
}
