package com.ypc.forthVerticle.entity;

public class User {
    private Integer id;

    private String userName;

    private String address;

    private Integer age;

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public String getUserName() {
      return userName;
    }

    public void setUserName(String userName) {
      this.userName = userName;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public Integer getAge() {
      return age;
    }

    public void setAge(Integer age) {
      this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
          "id=" + id +
          ", userName='" + userName + '\'' +
          ", address='" + address + '\'' +
          ", age=" + age +
          '}';
    }
}
