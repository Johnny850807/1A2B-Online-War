package com.ood.clean.waterball.a1a2bsdk.core.modules.game.model;


/**
 * The object saving the result from the guess used for all 1A2B games.
 */
public class Guess1A2BResult {
    private int a;
    private int b;

    public Guess1A2BResult(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}
