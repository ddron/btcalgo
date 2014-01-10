package com.btcalgo.service.api.templates;

import com.google.gson.annotations.SerializedName;

public class InfoTemplate extends LoginTemplate {
    @SerializedName(value = "return")
    private Info info;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public boolean hasAllRights() {
        return isSuccess() && hasInfoRights() && hasTradeRights();
    }

    public boolean hasInfoRights() {
        return info.hasInfoRights();
    }

    public boolean hasTradeRights() {
        return info.hasTradeRights();
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
