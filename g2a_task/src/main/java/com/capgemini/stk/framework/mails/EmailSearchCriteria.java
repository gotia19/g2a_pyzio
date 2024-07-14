package com.capgemini.stk.framework.mails;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.RecipientTerm;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author mdzienia
 */
public class EmailSearchCriteria {
    private SearchTerm    bodyContainsTerm;
    private SearchTerm    subjectContainsTerm;
    private RecipientTerm internetAddressTOTerm;
    private SearchTerm    receivedDateTerm;
    private String        criteria = "";

    public RecipientTerm getRecipientTerm() {
        return internetAddressTOTerm;
    }

    public SearchTerm getReceivedDateTerm() {
        return receivedDateTerm;
    }

    public String getCriteriaText() {
        return criteria.trim();
    }

    public SearchTerm build() {
        SearchTerm searchTerm = bodyContainsTerm;

        if (subjectContainsTerm != null) {
            if (searchTerm == null) {
                searchTerm = subjectContainsTerm;
            }
            else {
                searchTerm = new AndTerm(searchTerm, subjectContainsTerm);
            }
        }

        if (internetAddressTOTerm != null) {
            if (searchTerm == null) {
                searchTerm = internetAddressTOTerm;
            }
            else {
                searchTerm = new AndTerm(searchTerm, internetAddressTOTerm);
            }
        }

        if (receivedDateTerm != null) {
            if (searchTerm == null) {
                searchTerm = receivedDateTerm;
            }
            else {
                searchTerm = new AndTerm(searchTerm, receivedDateTerm);
            }
        }

        return searchTerm;
    }

    public EmailSearchCriteria searchForAddressTO(InternetAddress addressTO) {
        internetAddressTOTerm = new RecipientTerm(Message.RecipientType.TO, addressTO);
        criteria += MessageFormat.format("\nRecipient: {0}", addressTO.getAddress());
        return this;
    }

    public EmailSearchCriteria searchForSubjectContains(String emailSubjectPart) {
        SearchTerm newTerm = new SearchTerm() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean match(Message message) {
                try {
                    if (message.getSubject().contains(emailSubjectPart)) {
                        return true;
                    }
                }
                catch (MessagingException ignored) {
                }
                return false;
            }
        };

        if (subjectContainsTerm == null) {
            subjectContainsTerm = newTerm;
        }
        else {
            subjectContainsTerm = new AndTerm(newTerm, subjectContainsTerm);
        }

        criteria += MessageFormat.format("\nSubject contains: {0}", emailSubjectPart);
        return this;
    }

    public EmailSearchCriteria searchForBodyContains(String emailBodyPart) {
        SearchTerm newTerm = new SearchTerm() {
            private static final long serialVersionUID = 2L;

            @Override
            public boolean match(Message message) {
                return bodyContainsMatch(message, emailBodyPart);
            }
        };

        if (bodyContainsTerm == null) {
            bodyContainsTerm = newTerm;
        }
        else {
            bodyContainsTerm = new AndTerm(newTerm, bodyContainsTerm);
        }
        criteria += MessageFormat.format("\nBody contains: {0}", emailBodyPart);
        return this;
    }

    private boolean bodyContainsMatch(Part p, String emailBodyPart) {
        try {
            if (p.isMimeType("text/*")) {
                String s = (String) p.getContent();
                return s.contains(emailBodyPart);
            }
            else if (p.isMimeType("multipart/*")) {
                Multipart mp    = (Multipart) p.getContent();
                int       count = mp.getCount();
                for (int i = 0; i < count; i++) {
                    if (bodyContainsMatch(mp.getBodyPart(i), emailBodyPart)) {
                        return true;
                    }
                }
            }
            else if (p.isMimeType("message/rfc822")) {
                return bodyContainsMatch((Part) p.getContent(), emailBodyPart);
            }
        }
        catch (MessagingException | IOException | RuntimeException ignored) {
        }
        return false;
    }

    public EmailSearchCriteria searchForReceivedBeforeMinutes(int minutesBack) {
        criteria += MessageFormat.format("\nReceived before minutes: {0}", minutesBack);
        return searchForReceivedDate(minutesBack, ComparisonTerm.LT);
    }

    public EmailSearchCriteria searchForReceivedInLastMinutes(int minutesBack) {
        criteria += MessageFormat.format("\nReceived in last minutes: {0}", minutesBack);
        return searchForReceivedDate(minutesBack, ComparisonTerm.GE);
    }

    private EmailSearchCriteria searchForReceivedDate(int minutesBack, int term) {
        SearchTerm newTerm = new SearchTerm() {
            private static final long serialVersionUID = 3L;

            @Override
            public boolean match(Message message) {
                try {
                    Date     d   = message.getReceivedDate();
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MINUTE, -minutesBack);
                    Date date = cal.getTime();
                    switch (term) {
                        case ComparisonTerm.LE:
                            return d.before(date) || d.equals(date);
                        case ComparisonTerm.LT:
                            return d.before(date);
                        case ComparisonTerm.EQ:
                            return d.equals(date);
                        case ComparisonTerm.NE:
                            return !d.equals(date);
                        case ComparisonTerm.GT:
                            return d.after(date);
                        case ComparisonTerm.GE:
                            return d.after(date) || d.equals(date);
                        default:
                            return false;
                    }
                }
                catch (MessagingException e) {
                    return false;
                }
            }
        };

        if (receivedDateTerm == null) {
            receivedDateTerm = newTerm;
        }
        else {
            receivedDateTerm = new AndTerm(newTerm, receivedDateTerm);
        }
        return this;
    }
}