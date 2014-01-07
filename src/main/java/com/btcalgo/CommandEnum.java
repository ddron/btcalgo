package com.btcalgo;

public enum CommandEnum {
    STOP("stop"),
    INFO("info"),
    NONE("");

    private String commandText;

    private CommandEnum(String commandText) {
        this.commandText = commandText;
    }

    public static CommandEnum valueByCommandText(String text) {
        for (CommandEnum command : CommandEnum.values()) {
            if (command.commandText.equalsIgnoreCase(text)) {
                return command;
            }
        }
        return NONE;
    }

    public String getCommandText() {
        return commandText;
    }
}
