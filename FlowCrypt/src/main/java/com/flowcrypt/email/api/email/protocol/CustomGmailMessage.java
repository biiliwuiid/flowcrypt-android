/*
 * © 2016-2018 FlowCrypt Limited. Limitations apply. Contact human@flowcrypt.com
 * Contributors: DenBond7
 */

package com.flowcrypt.email.api.email.protocol;

import com.sun.mail.gimap.GmailMessage;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.protocol.BODY;
import com.sun.mail.imap.protocol.BODYSTRUCTURE;
import com.sun.mail.imap.protocol.ENVELOPE;
import com.sun.mail.imap.protocol.INTERNALDATE;
import com.sun.mail.imap.protocol.Item;
import com.sun.mail.imap.protocol.MODSEQ;
import com.sun.mail.imap.protocol.RFC822DATA;
import com.sun.mail.imap.protocol.RFC822SIZE;
import com.sun.mail.imap.protocol.UID;
import com.sun.mail.util.ASCIIUtility;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.mail.Session;

/**
 * A custom implementation of the {@link GmailMessage}
 *
 * @author Denis Bondarenko
 * Date: 29.05.2018
 * Time: 13:28
 * E-mail: DenBond7@gmail.com
 */
public class CustomGmailMessage extends GmailMessage implements FlowCryptIMAPMessage {

    protected CustomGmailMessage(IMAPFolder folder, int msgnum) {
        super(folder, msgnum);
    }

    protected CustomGmailMessage(Session session) {
        super(session);
    }

    @Override
    public String getBodyAsString() {
        try {
            if (content != null) {
                return IOUtils.toString(content, StandardCharsets.UTF_8.displayName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Apply the data in the FETCH item to this message.
     * <p>
     * ASSERT: Must hold the messageCacheLock.
     *
     * @param item       the fetch item
     * @param hdrs       the headers we're asking for
     * @param allHeaders load all headers?
     * @return did we handle this fetch item?
     * @throws MessagingException for failures
     * @since JavaMail 1.4.6
     */
    @Override
    public boolean handleFetchItemWithCustomBody(Item item, String[] hdrs, boolean allHeaders) throws
            MessagingException {
        // Check for the standard items
        if (item instanceof Flags
                || item instanceof ENVELOPE
                || item instanceof INTERNALDATE
                || item instanceof RFC822SIZE
                || item instanceof MODSEQ
                || item instanceof BODYSTRUCTURE
                || item instanceof UID
                || item instanceof RFC822DATA) {
            return handleFetchItem(item, hdrs, allHeaders);
        }

        // Check for header items
        else if (item instanceof BODY) {
            ByteArrayInputStream bodyStream;
            bodyStream = ((BODY) item).getByteArrayInputStream();

            if (!((BODY) item).isHeader()) {
                content = ASCIIUtility.toString(bodyStream).getBytes();
            }
        } else
            return false;    // not handled
        return true;        // something above handled it
    }

    @Override
    protected void handleExtensionFetchItems(Map<String, Object> extensionItems) {
        super.handleExtensionFetchItems(extensionItems);
    }
}