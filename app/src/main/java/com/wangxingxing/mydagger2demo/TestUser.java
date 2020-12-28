package com.wangxingxing.mydagger2demo;

/**
 * author : 王星星
 * date : 2020/12/28 15:41
 * email : 1099420259@qq.com
 * description :
 */
public class TestUser {

    String userName;

    public TestUser() {
    }

    public TestUser(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "TestUser{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
