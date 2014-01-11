package com.btcalgo.execution;

public enum OrderStatus {
    WAITING(Action.CANCEL),
    SENDING(Action.CANCEL),
    SENT(Action.REMOVE),
    CANCELLED(Action.REMOVE),
    ERROR(Action.REMOVE);

    private OrderStatus(Action validAction) {
        this.validAction = validAction;
    }

    private Action validAction;

    public Action getValidAction() {
        return validAction;
    }
}
