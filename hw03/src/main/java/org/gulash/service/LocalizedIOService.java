package org.gulash.service;

public interface LocalizedIOService extends LocalizedMessagesService, IOService {
    void printLineLocalized(String code);

    void printFormattedLineLocalized(String code, Object ...args);

    String readStringWithPromptLocalized(String promptCode);

}
