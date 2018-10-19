/*
 * © 2016-2018 FlowCrypt Limited. Limitations apply. Contact human@flowcrypt.com
 * Contributors: DenBond7
 */

package com.flowcrypt.email.database;

/**
 * This class describes the message states.
 *
 * @author Denis Bondarenko
 *         Date: 16.09.2018
 *         Time: 15:11
 *         E-mail: DenBond7@gmail.com
 */
public enum MessageState {
    NONE(-1),
    NEW(1),
    QUEUED(2),
    SENDING(3),
    CACHE_ERROR(4),
    SENT(5),
    SENT_WITHOUT_LOCAL_COPY(6),
    NEW_FORWARDED(7),
    ERROR_DURING_CREATION(8);

    private int value;

    MessageState(int value) {
        this.value = value;
    }

    public static MessageState generate(int code) {
        for (MessageState messageState : MessageState.values()) {
            if (messageState.getValue() == code) {
                return messageState;
            }
        }

        return null;
    }

    public int getValue() {
        return value;
    }
}
