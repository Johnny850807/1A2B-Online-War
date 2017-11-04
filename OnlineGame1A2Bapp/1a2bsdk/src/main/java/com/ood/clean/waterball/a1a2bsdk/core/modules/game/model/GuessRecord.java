package com.ood.clean.waterball.a1a2bsdk.core.modules.game.model;


public class GuessRecord {
    private Guess1A2BResult result;
    private String guess;

    public GuessRecord(Guess1A2BResult result, String guess) {
        this.result = result;
        this.guess = guess;
    }

    public Guess1A2BResult getResult() {
        return result;
    }

    public void setResult(Guess1A2BResult result) {
        this.result = result;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }
}
