package com.lolzorrior.supernaturalmod.capabilities;

public interface ISupernaturalClass {
    void setSupernaturalClass(String classes);
    String getSupernaturalClass();

    void consume(int points);
    void fill(int points);
    void set(int points);
    int getPower();
}
