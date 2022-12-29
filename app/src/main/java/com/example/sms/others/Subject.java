package com.example.sms.others;

public class Subject {
    private boolean maths;
    private boolean science;

    public Subject() {
    }

    public Subject(boolean maths, boolean science) {
        this.maths = maths;
        this.science = science;
    }

    public boolean isMaths() {
        return maths;
    }

    public void setMaths(boolean maths) {
        this.maths = maths;
    }

    public boolean isScience() {
        return science;
    }

    public void setScience(boolean science) {
        this.science = science;
    }
}
