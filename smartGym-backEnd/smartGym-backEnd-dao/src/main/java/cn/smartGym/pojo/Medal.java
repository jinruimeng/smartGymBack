package cn.smartGym.pojo;

import java.util.Date;

public class Medal {
    private Long id;

    private String game;

    private Integer college;

    private Integer first;

    private Integer second;

    private Integer third;

    private Integer fourth;

    private Integer fifth;

    private Integer sixth;

    private Integer seventh;

    private Integer eighth;

    private Integer score;

    private Integer status;

    private Date created;

    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game == null ? null : game.trim();
    }

    public Integer getCollege() {
        return college;
    }

    public void setCollege(Integer college) {
        this.college = college;
    }

    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public Integer getThird() {
        return third;
    }

    public void setThird(Integer third) {
        this.third = third;
    }

    public Integer getFourth() {
        return fourth;
    }

    public void setFourth(Integer fourth) {
        this.fourth = fourth;
    }

    public Integer getFifth() {
        return fifth;
    }

    public void setFifth(Integer fifth) {
        this.fifth = fifth;
    }

    public Integer getSixth() {
        return sixth;
    }

    public void setSixth(Integer sixth) {
        this.sixth = sixth;
    }

    public Integer getSeventh() {
        return seventh;
    }

    public void setSeventh(Integer seventh) {
        this.seventh = seventh;
    }

    public Integer getEighth() {
        return eighth;
    }

    public void setEighth(Integer eighth) {
        this.eighth = eighth;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}