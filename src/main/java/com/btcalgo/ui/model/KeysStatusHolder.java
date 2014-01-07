package com.btcalgo.ui.model;

import com.btcalgo.service.api.templates.InfoTemplate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class KeysStatusHolder {
    private StringProperty keysStatus = new SimpleStringProperty();
    private BooleanProperty validateBtnDisabled = new SimpleBooleanProperty(false);


    public void updateStatus(InfoTemplate infoTemplate) {
        setValidateBtnDisabled(false);
        if (infoTemplate.hasAllRights()) {
            setKeysStatus("Key is valid");
        } else if (infoTemplate.getSuccess() != 1) {
            setKeysStatus("Invalid key or secret");
        } else if (!infoTemplate.hasInfoRights() && !infoTemplate.hasTradeRights()) {
            setKeysStatus("Key does not have 'ino' and 'trade' rights");
        } else if (!infoTemplate.hasInfoRights()) {
            setKeysStatus("Key does not have 'info' right");
        } else if (!infoTemplate.hasTradeRights()) {
            setKeysStatus("Key does not have 'trade' right");
        }
    }

    public void clearStatus() {
        setKeysStatus("");
    }

    public void setValidatingStatus() {
        setKeysStatus("Validating...");
    }

    public final String getKeyStatus() {
        return keysStatus.get();
    }

    public final void setKeysStatus(String value) {
        keysStatus.set(value);
    }

    public StringProperty keysStatusProperty() {
        return keysStatus;
    }

    public boolean getValidateBtnDisabled() {
        return validateBtnDisabled.get();
    }

    public BooleanProperty validateBtnDisabledProperty() {
        return validateBtnDisabled;
    }

    public void setValidateBtnDisabled(boolean validateBtnDisabled) {
        this.validateBtnDisabled.set(validateBtnDisabled);
    }
}
