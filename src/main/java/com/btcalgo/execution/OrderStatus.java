package com.btcalgo.execution;

public enum OrderStatus {
    WAITING(Action.CANCEL) {
        @Override
        public boolean isTerminal() {
            return false;
        }
    },
    SENDING(Action.CANCEL) {
        @Override
        public boolean isTerminal() {
            return false;
        }
    },
    SENT(Action.REMOVE) {
        @Override
        public boolean isTerminal() {
            return true;
        }
    },
    CANCELLED(Action.REMOVE) {
        @Override
        public boolean isTerminal() {
            return true;
        }
    },
    ERROR(Action.REMOVE) {
        @Override
        public boolean isTerminal() {
            return true;
        }
    };

    private OrderStatus(Action validAction) {
        this.validAction = validAction;
    }

    private Action validAction;

    public Action getValidAction() {
        return validAction;
    }

    public abstract boolean isTerminal();
}
