package com.algo.btce;

import com.algo.btce.reactor.NameableConsumer;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Reactor;
import reactor.event.Event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleCommandProcessor  implements NameableConsumer<Event<Void>> {

    private static final Logger log = LoggerFactory.getLogger(ConsoleCommandProcessor.class);

    private static enum CommandEnum {
        STOP("stop"),
        NONE("");

        private String commandText;

        private CommandEnum(String commandText) {
            this.commandText = commandText;
        }

        private static CommandEnum valueByCommandText(String text) {
            for (CommandEnum command : CommandEnum.values()) {
                if (command.commandText.equalsIgnoreCase(text)) {
                    return command;
                }
            }
            return NONE;
        }
    }

    private Reactor reactor;
    private BufferedReader br;

    public ConsoleCommandProcessor(Reactor reactor) {
        this.reactor = reactor;
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String getId() {
        return "consoleProcessor";
    }

    @Override
    public void accept(Event<Void> voidEvent) {
        try {
            String line = br.readLine();
            CommandEnum command = CommandEnum.valueByCommandText(line);

            if (!Strings.isNullOrEmpty(line)) {
                if (command == CommandEnum.NONE) {
                   log.info("Unknown command");
                } else {
                    reactor.notify(command.commandText);
                }
            }
            reactor.notify(getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
