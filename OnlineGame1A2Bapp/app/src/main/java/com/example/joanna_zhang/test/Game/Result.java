package com.example.joanna_zhang.test.Game;

/**
 * Created by Lin on 2017/9/17.
 */

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
