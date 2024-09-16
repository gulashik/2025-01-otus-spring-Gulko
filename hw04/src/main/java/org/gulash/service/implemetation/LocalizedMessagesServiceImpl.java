package org.gulash.service.implemetation;

import lombok.RequiredArgsConstructor;
import org.gulash.config.TestFileProperties;
import org.gulash.service.gateway.LocalizedMessagesServiceGateway;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LocalizedMessagesServiceImpl implements LocalizedMessagesServiceGateway {

    private final TestFileProperties testFileProperties;

    private final MessageSource messageSource;

    @Override
    public String getMessage(String code, Object... args) {
        return  messageSource.getMessage(code, args, testFileProperties.getLocale());
    }
}
