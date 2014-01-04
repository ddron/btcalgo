package com.algo.btce.templates;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InfoTemplate extends LoginTemplate {
    @JsonProperty(value = "return")
    private Info info;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InfoTemplate{");
        sb.append("super=").append(super.toString());
        sb.append("info=").append(info);
        sb.append('}');
        return sb.toString();
    }
}
