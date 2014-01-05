package com.algo.btce.service.api.templates;

public class Info {
    private FundsTemplate funds;
    private RightsTemplate rights;
    private int transaction_count;
    private int open_orders;
    private long server_time;

    public FundsTemplate getFunds() {
        return funds;
    }

    public void setFunds(FundsTemplate funds) {
        this.funds = funds;
    }

    public RightsTemplate getRights() {
        return rights;
    }

    public void setRights(RightsTemplate rights) {
        this.rights = rights;
    }

    public int getTransaction_count() {
        return transaction_count;
    }

    public void setTransaction_count(int transaction_count) {
        this.transaction_count = transaction_count;
    }

    public int getOpen_orders() {
        return open_orders;
    }

    public void setOpen_orders(int open_orders) {
        this.open_orders = open_orders;
    }

    public long getServer_time() {
        return server_time;
    }

    public void setServer_time(long server_time) {
        this.server_time = server_time;
    }
}
