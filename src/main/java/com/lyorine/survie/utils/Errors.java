package com.lyorine.survie.utils;

public enum Errors {

    ALREADYCONTAINS("AL-CONTAIN"),
    NOTCONTAINS("NO-CONTAIN"),
    ;

    private String name;

    Errors(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
