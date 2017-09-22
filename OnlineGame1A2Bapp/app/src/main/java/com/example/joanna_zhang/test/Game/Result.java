package com.example.joanna_zhang.test.Game;


public class Result {

    private int a = 0, b = 0;

    public int getA() {
        return a;
    }

    public int getB(){
        return b;
    }

    @Override
    public String toString() {
        return this.getA() + "A" + this.getB() + "B";
    }
}
