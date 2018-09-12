package com.github.vaerys.enums;

public enum LogType {
    MSG_DELETE      (0xC04040),
    MSG_EDIT        (0x4040C0),
    ROLE_UPDATE     (0x20C0C0),
    CHANNEL_UPDATE  (0x008080),
    CHANNEL_CREATE  (0x20C020),
    CHANNEL_DELETE  (0xC00000),
    USER_JOIN       (0xA0A000),
    USER_LEAVE      (0xA00000);

    public int color;

    LogType(int color){
        this.color = color;
    }
}
