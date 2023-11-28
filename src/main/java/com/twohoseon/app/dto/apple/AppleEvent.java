package com.twohoseon.app.dto.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : AppleEvent
 * @date : 11/18/23 4:54 AM
 * @modifyed : $
 **/
@Getter
public class AppleEvent {
    AppleEventType type;
    String sub;
    @JsonProperty("event_time")
    long eventTime;
}
