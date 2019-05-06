package cn.smartGym.pojo;

import java.util.Date;

public class Remark {
    private Long id;

    private String wxId;

    private String remarkMessage;

    private Date created;

    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId == null ? null : wxId.trim();
    }

    public String getRemarkMessage() {
        return remarkMessage;
    }

    public void setRemarkMessage(String remarkMessage) {
        this.remarkMessage = remarkMessage == null ? null : remarkMessage.trim();
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