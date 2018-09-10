package com.tomcat360.lyqb.model.eventbusData;

/**
 * Created by niedengqiang on 2018/8/21.
 */

public class NameChangeData {
    private String name;

    public String getName() {
        return name;
    }

    public NameChangeData(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NameChangeData{" +
                "name='" + name + '\'' +
                '}';
    }
}
