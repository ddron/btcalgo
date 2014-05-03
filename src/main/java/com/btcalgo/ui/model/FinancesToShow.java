package com.btcalgo.ui.model;

import com.btcalgo.finances.Finances;
import com.btcalgo.finances.FundsConverter;
import com.btcalgo.finances.FundsEnum;
import com.btcalgo.reactor.NameableConsumer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.event.Event;

import java.util.ArrayList;
import java.util.Map;

public class FinancesToShow implements NameableConsumer<Event<Void>>, ChangeListener<String> {

    private Logger log = LoggerFactory.getLogger(getClass());

    private Finances finances;
    private FundsConverter converter;

    private ObservableList<FinancesInfo> dataToShow = FXCollections.observableArrayList(new ArrayList<FinancesInfo>());
    private StringProperty totalFinances = new SimpleStringProperty("0.00");
    private FundsEnum fundOfTotalAmount = FundsEnum.USD;

    public void updateFinancesToShow() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                dataToShow.clear();
                Map<FundsEnum, Double> availableFunds = finances.getAvailableFunds();
                Map<FundsEnum, Double> onOrdersFunds = finances.getOnOrdersFunds();

                for (FundsEnum fundsEnum : FundsEnum.values()) {
                    double onOrdersAmount = onOrdersFunds.get(fundsEnum) == null ? 0 : onOrdersFunds.get(fundsEnum);
                    double totalAmount = onOrdersAmount + (availableFunds.get(fundsEnum) == null ? 0 : availableFunds.get(fundsEnum));
                    dataToShow.add(new FinancesInfo(fundsEnum.name(), onOrdersAmount, totalAmount));
                }

                FXCollections.sort(dataToShow);
                log.debug("Finances to show: {}", dataToShow);
            }
        });
    }

    public void setFinances(Finances finances) {
        this.finances = finances;
    }

    public void setConverter(FundsConverter converter) {
        this.converter = converter;
    }

    public ObservableList<FinancesInfo> getDataToShow() {
        return dataToShow;
    }

    @Override
    public String getId() {
        return "finances.to.show";
    }

    @Override
    public void accept(Event<Void> voidEvent) {
        double totalAmount = 0;

        Map<FundsEnum, Double> availableFunds = finances.getAvailableFunds();
        Map<FundsEnum, Double> onOrdersFunds = finances.getOnOrdersFunds();

        for (FundsEnum fundsEnum : FundsEnum.values()) {
            double onOrdersAmount = onOrdersFunds.get(fundsEnum) == null ? 0 : onOrdersFunds.get(fundsEnum);
            double amount = onOrdersAmount + (availableFunds.get(fundsEnum) == null ? 0 : availableFunds.get(fundsEnum));
            totalAmount += converter.convertWithFee(fundOfTotalAmount, fundsEnum, amount);
        }

        final double finalTotalAmount = totalAmount;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                totalFinances.set(String.format("%.2f", finalTotalAmount));
            }
        });
    }

    @Override
    public void changed(ObservableValue<? extends String> observableValue, String oldFund, String newFund) {
        FundsEnum fund = FundsEnum.valueOf(newFund);
        if (fund != fundOfTotalAmount) {
            fundOfTotalAmount = fund;
            accept(null);
        }
    }

    public String getTotalFinances() {
        return totalFinances.get();
    }

    public StringProperty totalFinancesProperty() {
        return totalFinances;
    }

    public void setTotalFinances(String totalFinances) {
        this.totalFinances.set(totalFinances);
    }
}
