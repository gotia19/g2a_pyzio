package com.capgemini.stk.framework.mails;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.stk.framework.actions.PerformAction;
import com.capgemini.stk.framework.basetest.StepLogger;
import com.capgemini.stk.framework.enums.Messages;
import com.capgemini.stk.framework.enums.TIMESLOTS;
import com.capgemini.stk.framework.helpers.DateTime;
import io.qameta.allure.Step;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

public final class EmailOperations implements AutoCloseable {
    private final int    CONNECTION_LIMIT = TIMESLOTS.WAIT_FOR_EMAIL_CONNECTION_MS;
    private       Store  store;
    private       Folder inbox, trash;
    private       Properties props;
    private       Session    session;
    private final String     emailAddress, emailPassword;
    private final EmailAccounts emailAccount;

    public EmailOperations(EmailAccounts account) {
        this.emailAccount  = account;
        this.emailAddress  = account.getAddress();
        this.emailPassword = account.getPassword();
    }

    @Override
    public void close() {
        try {
            disconnect();
        }
        catch (MessagingException e) {
            BFLogger.logError(Messages.EMAIL_CONNECTION_CLOSING_ISSUE + e.getMessage());
        }
        finally {
            //emailAccount.setUnused();
            emailAccount.removeEmailOperation(this);
        }
    }

    private void disconnect() throws MessagingException {
        long timeBegin   = System.currentTimeMillis();
        long timeCounter = System.currentTimeMillis() - timeBegin;

        while (isStoreConnected() && timeCounter < CONNECTION_LIMIT) {
            timeCounter = System.currentTimeMillis() - timeBegin;
            BFLogger.logInfo("MAIL: Close connection");
            store.close();
            PerformAction.waitMilliseconds(TIMESLOTS.WAIT_SHORT_MOMENT_MS);
        }
        store   = null;
        inbox   = null;
        trash   = null;
        props   = null;
        session = null;
        emailAccount.removeEmailOperation(this);
    }

    private void connect() throws MessagingException {
        if (!isConnected()) {
            //Store
            BFLogger.logInfo(MessageFormat.format("MAIL: Start connection ({0})", emailAddress));
            props = new Properties();
            props.put("mail.store.protocol", "imaps");
            props.put("mail.imap.partialfetch", "false");
            props.put("mail.imap.fetchsize", "1048576");
            props.put("mail.imaps.partialfetch", "false");
            props.put("mail.imaps.fetchsize", "1048576");
            session = Session.getDefaultInstance(props);
            store   = session.getStore("imaps");
            emailAccount.connectToStore(this, store, "imap.gmail.com", emailAddress, emailPassword);
            BFLogger.logInfo(MessageFormat.format("MAIL: Store connected ({0})", emailAddress));

            //Inbox
            BFLogger.logInfo(MessageFormat.format("MAIL: Open Inbox folder ({0})", emailAddress));
            inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);
            BFLogger.logInfo(MessageFormat.format("MAIL: Opened Inbox folder ({0})", emailAddress));

            //Trash
            //BFLogger.logInfo(MessageFormat.format("MAIL: Open Trash folder ({0})", emailAddress));
            // Folder[] f = store.getDefaultFolder()
            // .list("*");
            // for (Folder fd : f) {
            // System.out.println(">> " + fd.getName());
            // }
            //            trash = store.getFolder("[Gmail]/Trash");
            //            trash.open(Folder.READ_WRITE);
            //            BFLogger.logInfo(MessageFormat.format("MAIL: Opened Trash folder ({0})", emailAddress));
        }
        if (!isConnected()) {
            throw new MessagingException("Not connected");
        }
    }

    boolean isStoreConnected() {
        return store != null && store.isConnected();
    }

    private boolean isInboxOpened() {
        return inbox != null && inbox.isOpen();
    }

    private boolean isTrashOpened() {
        return trash != null && trash.isOpen();
    }

    private boolean isConnected() {
        return isStoreConnected() && isInboxOpened(); //&& isTrashOpened();
    }

    private Message[] search(EmailSearchCriteria searchFor) throws MessagingException {
        SearchTerm recipientTerm = searchFor.getRecipientTerm();
        SearchTerm term          = searchFor.build();
        connect();
        BFLogger.logInfo(MessageFormat.format("MAIL: Search ({0})", emailAddress));
        if (null != recipientTerm) {
            //User
            Message[] messages = inbox.search(recipientTerm);
            //Time
            if (null != searchFor.getReceivedDateTerm() && messages.length > 0) {
                messages = inbox.search(searchFor.getReceivedDateTerm(), messages);
            }
            //Other terms
            if (messages.length > 0) {
                FetchProfile fetchProfile = new FetchProfile();
                fetchProfile.add(FetchProfile.Item.ENVELOPE);
                fetchProfile.add(FetchProfile.Item.CONTENT_INFO);
                fetchProfile.add("X-mailer");
                inbox.fetch(messages, fetchProfile);
                return inbox.search(term, messages);
            }
            return messages;
        }
        return inbox.search(term);
    }

    @Step("Wait for the email to arrive and delete this email")
    public void refreshUntilMailArrivesAndDelete(EmailSearchCriteria searchFor) {
        refreshUntilMailArrives(searchFor);
        deleteAllFilteredEmails(searchFor);
    }

    public Message[] refreshUntilMailArrives(EmailSearchCriteria searchFor) {
        return refreshUntilMailArrives(searchFor, TIMESLOTS.WAIT_FOR_EMAIL_MESSAGE_MS, true);
    }

    public Message[] refreshUntilMailArrives(EmailSearchCriteria searchFor, long timeLimitMilliseconds) {
        return refreshUntilMailArrives(searchFor, timeLimitMilliseconds, true);
    }

    @Step("Wait for the email to arrive")
    public Message[] refreshUntilMailArrives(EmailSearchCriteria searchFor, long timeLimitMilliseconds, boolean failIfNotFound) {
        SearchTerm receivedDateTerm = searchFor.getReceivedDateTerm();
        //By default search only messages from last few minutes
        if (null == receivedDateTerm) {
            searchFor = searchFor.searchForReceivedInLastMinutes(TIMESLOTS.WAIT_FOR_EMAIL_MESSAGE_MINUTES);
        }
        StepLogger.saveTextAttachmentToLog("Search criteria", searchFor.getCriteriaText());
        final int nextTrialTime    = TIMESLOTS.WAIT_MEDIUM_MOMENT_MS;
        long      timeBegin        = System.currentTimeMillis();
        long      timeCounter      = System.currentTimeMillis() - timeBegin;
        String    exceptionMessage = "";
        Message[] messages         = new Message[0];
        while (timeCounter < timeLimitMilliseconds) {
            if (Thread.currentThread().isInterrupted()) {
                assumeTrue(MessageFormat.format("MAIL: {0} thread interrupted", Thread.currentThread().getName()),
                           false);
            }
            try {
                messages = search(searchFor);
                String time = DateTime.getTimeFromMilliseconds(System.currentTimeMillis() - timeBegin);
                if (messages.length > 0) {
                    BFLogger.logInfo("MAIL: FOUND IN " + time);
                    for (int i = 0; i < messages.length; i++) {
                        StepLogger.saveEmailToLog(MessageFormat.format("[{0}] Mail body", i + 1),
                                                  getContentFromMessage(messages[i]));
                    }
                    return messages;
                }
            }
            catch (MessagingException e) {
                if (e.getMessage().length() > 0) {
                    exceptionMessage = e.getMessage();
                    BFLogger.logError(exceptionMessage);
                }
            }
            timeCounter = System.currentTimeMillis() - timeBegin;
            String time = DateTime.getTimeFromMilliseconds(System.currentTimeMillis() - timeBegin);
            BFLogger.logInfo("MAIL: " + time + " have passed");
            PerformAction.waitMilliseconds(nextTrialTime);
        }

        String time        = DateTime.getTimeFromMilliseconds(System.currentTimeMillis() - timeBegin);
        String failMessage = "MAIL: NOT FOUND AFTER " + time;
        BFLogger.logInfo(failMessage);
        if (failIfNotFound) {
            fail(MessageFormat.format("{0}\n{1}\n{2}", failMessage, emailAddress, exceptionMessage));
        }
        else {
            StepLogger.step(failMessage);
        }
        return messages;
    }

    @Step("Delete all filtered emails")
    public void deleteAllFilteredEmails(EmailSearchCriteria searchFor) {
        StepLogger.saveTextAttachmentToLog("Search criteria", searchFor.getCriteriaText());
        deleteAllFilteredEmailsSteeples(searchFor);
    }

    public void deleteAllFilteredEmailsSteeples(EmailSearchCriteria searchFor) {
        try {
            Message[] messages = search(searchFor);
            if (messages != null && messages.length > 0) {
                BFLogger.logInfo("MAIL: " + messages.length + " emails to remove");
                deleteMails(messages);
                BFLogger.logInfo("MAIL: Emails have been removed");
            }
            else {
                BFLogger.logInfo("MAIL: No old emails with filter have been found");
            }
        }
        catch (Throwable e) {
            BFLogger.logError(Messages.EMAIL_DELETING_MESSAGE_ISSUE + e.getMessage());
        }
    }

    private void permanentlyRemoveDeletedMessages() {
        try {
            connect();
            Flags     flags    = new Flags(Flags.Flag.DELETED);
            Message[] messages = trash.getMessages();
            trash.setFlags(messages, flags, true);
            trash.expunge();
            inbox.expunge();
            BFLogger.logInfo("MAIL: Expunged (permanently removed) messages marked DELETED");
        }
        catch (Throwable e) {
            BFLogger.logError(Messages.EMAIL_DELETING_MESSAGE_ISSUE + e.getMessage());
        }
    }

    private void setFlagForMessage(Message message, Flag flag) {
        try {
            message.setFlag(flag, true);
        }
        catch (MessagingException e) {
            BFLogger.logError(Messages.EMAIL_CONNECTION_CHANGING_FLAG_ISSUE + e.getMessage());
        }
    }

    public void deleteMail(Message message) {
        deleteMails(new Message[]{message});
    }

    public void deleteMails(Message[] messages) {
        try {
            inbox.copyMessages(messages, trash);
            permanentlyRemoveDeletedMessages();
        }
        catch (Throwable e) {
            BFLogger.logError(Messages.EMAIL_DELETING_MESSAGE_ISSUE + e.getMessage());
        }
    }

    public String getContentFromMessage(Message message) {
        try {
            int    counter    = 0;
            Object content    = message.getContent();
            String msgContent = "";
            if (content instanceof Multipart) {
                msgContent = getContentFromMessage((Multipart) content);
            }
            if (msgContent.isEmpty() && content instanceof String) {
                msgContent = content.toString();
            }
            while (msgContent.isEmpty() && counter < 5) {
                counter++;
                content = message.getContent();
                if (content instanceof Multipart) {
                    msgContent = getContentFromMessage((Multipart) content);
                }
                if (msgContent.isEmpty() && content instanceof String) {
                    msgContent = content.toString();
                }
            }
            return msgContent;
        }
        catch (Throwable e) {
            fail(Messages.EMAIL_READING_MESSAGE_ISSUE + e.getMessage());
        }
        return null;
    }

    public String getContentFromMessage(Multipart multipart) {
        StringBuilder contentText = new StringBuilder();
        try {
            for (int index = 0; index < multipart.getCount(); index++) {
                BodyPart bodyPart = multipart.getBodyPart(index);
                if (bodyPart.getContentType().toLowerCase().contains("multipart")) {
                    if (contentText.length() > 0) {
                        contentText.append("\n");
                    }
                    contentText.append(getContentFromMessage((Multipart) bodyPart.getContent()));
                }
                else if (bodyPart.getContentType().toLowerCase().contains("text")) {
                    if (contentText.length() > 0) {
                        contentText.append("\n");
                    }
                    contentText.append(bodyPart.getContent());
                }
            }
            return contentText.toString();
        }
        catch (Throwable e) {
            fail(Messages.EMAIL_READING_MESSAGE_ISSUE + e.getMessage());
        }
        return null;
    }

    @Step("Get all links from email")
    public List<String> getAllLinksFromContent(String message) {
        return getAllLinksFromContentSteeples(message);
    }

    public List<String> getAllLinksFromContentSteeples(String message) {
        List<String> listOfLinks = new ArrayList<>();
        Document     doc         = Jsoup.parse(message);
        BFLogger.logInfo(doc.toString());
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String href         = link.attr("href");
            int    indexOfQuote = href.indexOf("\"");
            if (indexOfQuote < 0) {
                indexOfQuote = 0;
            }
            String linkText = href.substring(indexOfQuote).replace("\"", "");
            StepLogger.info("Link: " + linkText);

            listOfLinks.add(linkText);
            link.remove();
        }
        String   newMessage = doc.text();
        String[] parts      = newMessage.split("\\s+");
        for (String item : parts) {
            try {
                URL url = new URL(item);
                listOfLinks.add(url.toString());
            }
            catch (MalformedURLException ignored) {
            }
        }
        return listOfLinks;
    }

    public List<String> getAllLinksFromContent(Message message) {
        return getAllLinksFromContentSteeples(message);
    }

    public List<String> getAllLinksFromContentSteeples(Message message) {
        setFlagForMessage(message, Flag.SEEN);
        String body = getContentFromMessage(message);
        StepLogger.info(body);
        return getAllLinksFromContent(body);
    }

    private String getFirstLinkFromMessage(Message message) {
        List<String> links = getAllLinksFromContentSteeples(message);
        String       link  = null;
        if (links.size() > 0) {
            link = links.get(0);
            StepLogger.info("Link: " + link);
        }
        return link;
    }

    @Step("Get registration link from email")
    public String getLinkFromRegistrationMessage(Message message) {
        return getFirstLinkFromMessage(message);
    }

    @Step("Get calendar attachments from mail")
    public List<String> getCalendarAttachments(Message message) {
        try {
            Object content = message.getContent();
            if (content instanceof String) {
                return null;
            }
            if (content instanceof Multipart) {
                Multipart    multipart = (Multipart) content;
                List<String> result    = new ArrayList<>();
                for (int i = 0; i < multipart.getCount(); i++) {
                    result.addAll(getCalendarAttachments(multipart.getBodyPart(i)));
                }
                return result;
            }
        }
        catch (MessagingException | IOException e) {
            fail(Messages.EMAIL_READING_MESSAGE_ISSUE + e.getMessage());
        }
        return null;
    }

    private List<String> getCalendarAttachments(BodyPart part) throws IOException, MessagingException {
        List<String> result      = new ArrayList<>();
        Object       content     = part.getContent();
        String       contentType = part.getContentType().toUpperCase();
        if (StringUtils.containsIgnoreCase(contentType,
                                           "TEXT/CALENDAR") && (content instanceof InputStream || content instanceof String)) {
            String coding = contentType.substring(contentType.indexOf("CHARSET="));
            coding = coding.substring("CHARSET=".length(), coding.indexOf(";"));
            result.add(IOUtils.toString(part.getInputStream(), Charset.forName(coding)));
        }
        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                result.addAll(getCalendarAttachments(bodyPart));
            }
        }
        return result;
    }

    @Step("Get attachments from mail")
    public List<InputStream> getAttachments(Message message) {
        try {
            Object content = message.getContent();
            if (content instanceof String) {
                return null;
            }

            if (content instanceof Multipart) {
                Multipart         multipart = (Multipart) content;
                List<InputStream> result    = new ArrayList<>();

                for (int i = 0; i < multipart.getCount(); i++) {
                    result.addAll(getAttachments(multipart.getBodyPart(i)));
                }
                return result;
            }
        }
        catch (MessagingException | IOException e) {
            fail(Messages.EMAIL_READING_MESSAGE_ISSUE + e.getMessage());
        }
        return null;
    }

    private List<InputStream> getAttachments(BodyPart part) throws IOException, MessagingException {
        List<InputStream> result  = new ArrayList<>();
        Object            content = part.getContent();
        if (content instanceof InputStream || content instanceof String) {
            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) || StringUtils.isNotBlank(part.getFileName())) {
                result.add(part.getInputStream());
                return result;
            }
            return new ArrayList<>();
        }

        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                result.addAll(getAttachments(bodyPart));
            }
        }
        return result;
    }
}