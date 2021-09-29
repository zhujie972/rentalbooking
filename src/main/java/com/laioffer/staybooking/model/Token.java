package com.laioffer.staybooking.model;
// created @ 09/25 lecture 28
// 这个class 为了生成对象给前端转换成 json 数据格式

public class Token {
    private final String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
