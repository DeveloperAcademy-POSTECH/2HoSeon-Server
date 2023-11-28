package com.twohoseon.app.dto.apple;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : AppleEventType
 * @date : 11/18/23 4:57 AM
 * @modifyed : $
 **/
public enum AppleEventType {
    CONSENT_REVOKED("consent-revoked"), ACCOUNT_DELETE("account-delete");

    private final String value;

    AppleEventType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static AppleEventType forValue(String value) {
        for (AppleEventType type : AppleEventType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
