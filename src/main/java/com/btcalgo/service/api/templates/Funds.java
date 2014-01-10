package com.btcalgo.service.api.templates;

public class Funds {
    private double btc;
    private double ltc;
    private double nmc;
    private double nvc;
    private double trc;
    private double ppc;
    private double ftc;
    private double xpm;
    private double usd;
    private double rur;
    private double eur;

    public double getBtc() {
        return btc;
    }

    public void setBtc(double btc) {
        this.btc = btc;
    }

    public double getLtc() {
        return ltc;
    }

    public void setLtc(double ltc) {
        this.ltc = ltc;
    }

    public double getNmc() {
        return nmc;
    }

    public void setNmc(double nmc) {
        this.nmc = nmc;
    }

    public double getNvc() {
        return nvc;
    }

    public void setNvc(double nvc) {
        this.nvc = nvc;
    }

    public double getTrc() {
        return trc;
    }

    public void setTrc(double trc) {
        this.trc = trc;
    }

    public double getPpc() {
        return ppc;
    }

    public void setPpc(double ppc) {
        this.ppc = ppc;
    }

    public double getFtc() {
        return ftc;
    }

    public void setFtc(double ftc) {
        this.ftc = ftc;
    }

    public double getXpm() {
        return xpm;
    }

    public void setXpm(double xpm) {
        this.xpm = xpm;
    }

    public double getUsd() {
        return usd;
    }

    public void setUsd(double usd) {
        this.usd = usd;
    }

    public double getRur() {
        return rur;
    }

    public void setRur(double rur) {
        this.rur = rur;
    }

    public double getEur() {
        return eur;
    }

    public void setEur(double eur) {
        this.eur = eur;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Funds{");
        sb.append("btc=").append(btc);
        sb.append(", ltc=").append(ltc);
        sb.append(", nmc=").append(nmc);
        sb.append(", nvc=").append(nvc);
        sb.append(", trc=").append(trc);
        sb.append(", ppc=").append(ppc);
        sb.append(", ftc=").append(ftc);
        sb.append(", xpm=").append(xpm);
        sb.append(", usd=").append(usd);
        sb.append(", rur=").append(rur);
        sb.append(", eur=").append(eur);
        sb.append('}');
        return sb.toString();
    }
}
