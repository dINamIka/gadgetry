package com.gadgetry.domain.exception;

public class DeviceInUseException extends RuntimeException {

    public DeviceInUseException(String message) {
        super(message);
    }
}
