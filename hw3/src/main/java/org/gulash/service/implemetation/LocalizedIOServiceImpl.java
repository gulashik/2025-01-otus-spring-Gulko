package org.gulash.service.implemetation;

import lombok.RequiredArgsConstructor;
import org.gulash.service.LocalizedIOService;
import org.gulash.service.gateway.IOServiceGateway;
import org.gulash.service.gateway.LocalizedMessagesServiceGateway;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LocalizedIOServiceImpl implements LocalizedIOService {

    private final LocalizedMessagesServiceGateway localizedMessagesService;

    private final IOServiceGateway ioService;

    public void printLine(String s) {
        ioService.printLine(s);
    }

    public void printFormattedLine(String s, Object... args) {
        ioService.printFormattedLine(s, args);
    }

    public String readString() {
        return ioService.readString();
    }

    public String readStringWithPrompt(String prompt) {
        return ioService.readStringWithPrompt(prompt);
    }

    public int readIntForRange(int min, int max, String errorMessage) {
        return ioService.readIntForRange(min, max, errorMessage);
    }

    public int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage) {
        return ioService.readIntForRangeWithPrompt(min, max, prompt, errorMessage);
    }

    public void printLineLocalized(String code) {
        ioService.printLine(localizedMessagesService.getMessage(code));
    }

    public void printFormattedLineLocalized(String code, Object... args) {
        ioService.printLine(localizedMessagesService.getMessage(code, args));
    }

    public String readStringWithPromptLocalized(String promptCode) {
        return ioService.readStringWithPrompt(localizedMessagesService.getMessage(promptCode));
    }

    public int readIntForRangeLocalized(int min, int max, String errorMessageCode) {
        return ioService.readIntForRange(min, max, localizedMessagesService.getMessage(errorMessageCode));
    }

    public int readIntForRangeWithPromptLocalized(int min, int max, String promptCode, String errorMessageCode) {
        return ioService.readIntForRangeWithPrompt(min, max,
                localizedMessagesService.getMessage(promptCode),
                localizedMessagesService.getMessage(errorMessageCode)
                );
    }

    public String getMessage(String code, Object... args) {
        return localizedMessagesService.getMessage(code, args);
    }
}
