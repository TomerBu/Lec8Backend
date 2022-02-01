package edu.tomerbu.lec4tdd.user;

public class Views {
    //basic (excludes password)
    //extended (includes password)
    public interface Basic{}
    //all the basic + password/sensitive data
    public interface Sensitive extends Basic{}
}
