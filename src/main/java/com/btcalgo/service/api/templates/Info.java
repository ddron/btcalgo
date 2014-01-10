package com.btcalgo.service.api.templates;

public class Info {
    private Funds funds;
    private Rights rights;
    private int transaction_count;
    private int open_orders;
    private long server_time;

    public Funds getFunds() {
        return funds;
    }

    public void setFunds(Funds funds) {
        this.funds = funds;
    }

    public Rights getRights() {
        return rights;
    }

    public void setRights(Rights rights) {
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

    public boolean hasInfoRights() {
        return rights.getInfo() == 1;
    }

    public boolean hasTradeRights() {
        return rights.getTrade() == 1;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Info{");
        sb.append("funds=").append(funds);
        sb.append(", rights=").append(rights);
        sb.append(", transaction_count=").append(transaction_count);
        sb.append(", open_orders=").append(open_orders);
        sb.append(", server_time=").append(server_time);
        sb.append('}');
        return sb.toString();
    }
}
