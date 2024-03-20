package com.example.todo_back.data.constant;

public enum ColorList {
    RED("red"), BLUE("blue"), GREEN("green"), YELLOW("yellow"), BLACK("black"),
    CYAN("cyan"), BEIGE("beige"), CHARTREUSE("chartreuse"), PALEGREEN("palegreen");

    private final String stringValue;

    // 생성자를 사용하여 문자열 값을 초기화합니다.
    ColorList(String stringValue) {
        this.stringValue = stringValue;
    }

    // 각 상수에 대한 문자열 값을 반환하는 메서드를 오버라이드합니다.
    @Override
    public String toString() {
        return stringValue;
    }
}
