package org.gulash.service;

public interface LocalizedMessagesService {
    String getMessage(String code, Object ...args);
}
