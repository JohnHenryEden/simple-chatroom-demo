package com.jhuang.chatservice.strategy;

public class StrategyFactory {
    /**
     * Create command strategy based on command
     * @param command
     * @return
     */
    public CommandStrategy createCommandStrategy(String command) {
        switch (command) {
            case "bye" -> {
                return new CloseStrategy();
            }
            case "toall" -> {
                return new BroadcastStrategy();
            }
            case "touser" -> {
                return new ToSingleUserStrategy();
            }
            case "setname" -> {
                return new SetNameStrategy();
            }
            default -> {
                return new RepeatMessageStrategy();
            }
        }

    }
}
