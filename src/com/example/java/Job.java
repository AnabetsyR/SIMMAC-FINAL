package com.example.java;

public class Job {

    private int start;
    private int end;
    public boolean isFinished;

    public Job(int x, int y){
        this.start = x;
        this.end = y;
    }
    
    @Override
    public String toString(){
        return "Start: " + this.start + ", " + "End: " + end;
    }
}
