package com.olavgg.pc;

import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;

import java.io.IOException;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }

    @EventListener
    void onStartup(ServerStartupEvent event) {
        try {
            new PasswordReader().read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}