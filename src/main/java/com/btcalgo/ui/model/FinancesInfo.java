package com.btcalgo.ui.model;

import static com.btcalgo.util.DoubleFormatter.fmt;

public final class FinancesInfo implements Comparable<FinancesInfo> {
    private final String fundEnumName;
    private final String financesOnOrders;
    private final String financesTotal;

    public FinancesInfo(String fundEnumName, Double financesOnOrders, Double financesTotal) {
        this.fundEnumName = fundEnumName;
        this.financesOnOrders = fmt(financesOnOrders);
        this.financesTotal = fmt(financesTotal);
    }

    public String getFundEnumName() {
        return fundEnumName;
    }

    public String getFinancesOnOrders() {
        return financesOnOrders;
    }

    public String getFinancesTotal() {
        return financesTotal;
    }

    @Override
    public int compareTo(FinancesInfo o) {
        return fundEnumName.compareTo(o.fundEnumName);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FinancesInfo{");
        sb.append("fundEnumName='").append(fundEnumName).append('\'');
        sb.append(", financesOnOrders='").append(financesOnOrders).append('\'');
        sb.append(", financesTotal='").append(financesTotal).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
