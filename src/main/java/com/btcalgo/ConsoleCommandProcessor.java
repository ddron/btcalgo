package com.btcalgo;

import com.btcalgo.reactor.NameableConsumer;
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

            if (!Strings.isNullOrEmpty(line)) {
                CommandEnum command = CommandEnum.valueByCommandText(line);
                if (command == CommandEnum.NONE) {
                   log.info("Unknown command");
                } else {
                    reactor.notify(command.getCommandText());
                }
            }
            reactor.notify(getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
