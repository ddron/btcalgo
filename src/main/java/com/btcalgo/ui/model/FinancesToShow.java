package com.btcalgo.ui.model;

import com.btcalgo.finances.Finances;
import com.btcalgo.finances.FundsEnum;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;

public class FinancesToShow {

    private Logger log = LoggerFactory.getLogger(getClass());

    private Finances finances;

    private ObservableList<FinancesInfo> dataToShow = FXCollections.observableArrayList(new ArrayList<FinancesInfo>());

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

    public ObservableList<FinancesInfo> getDataToShow() {
        return dataToShow;
    }
}
