package com.example.sms.model;

public class Results1 {
    private String username;
    private Float part1;
    private Float part2;
    private Float total;
    private String grades;

    public Results1() {
    }

    public Results1(String username, Float part1, Float part2, Float total, String grades) {
        this.username = username;
        this.part1 = part1;
        this.part2 = part2;
        this.total = total;
        this.grades = grades;
    }

    public String getGrades() {
        return grades;
    }

    public void setGrades(String grades) {
        this.grades = grades;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Float getPart1() {
        return part1;
    }

    public void setPart1(Float part1) {
        this.part1 = part1;
    }

    public Float getPart2() {
        return part2;
    }

    public void setPart2(Float part2) {
        this.part2 = part2;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }
}
