package com.common.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StartMessage.class, name = "start"),
        @JsonSubTypes.Type(value = ResultMessage.class, name = "result"),
        @JsonSubTypes.Type(value = LoggingMessage.class, name = "log"),
        @JsonSubTypes.Type(value = InfoMessage.class, name = "i"),
        @JsonSubTypes.Type(value = EchoMessage.class, name = "e")
})
public interface Message {
}
