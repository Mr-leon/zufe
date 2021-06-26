package org.smartjq.plugin.quartz.model;

/**
 * @author Leon
 * @create 2021-04-02 10:11
 */
public class RetrunModel {
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    private Integer status;

    private String message;

    private String list;


}
