package com.nhnacademy.springbootmvc.domain;

public enum Category {
    COMPLAINT("불만 접수"),
    SUGGESTION("제안"),
    REFUND_EXCHANGE("환불/교환"),
    COMPLIMENT("칭찬해요"),
    OTHER("기타 문의");

    private final String toString;

    Category(String string) {
        this.toString = string;
    }
    public String toString() {
        return this.toString;
    }
}
