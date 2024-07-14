package com.capgemini.stk.framework.mails;

import com.capgemini.stk.framework.enums.TIMESLOTS;
import com.capgemini.stk.framework.environment.GetEnvironmentParam;
import org.apache.commons.lang.StringUtils;

import javax.mail.MessagingException;
import javax.mail.Store;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public enum EmailAccounts {
    User01(GetEnvironmentParam.EMAIL_USER01, GetEnvironmentParam.EMAIL_PASS);

    EmailAccounts(GetEnvironmentParam mail, GetEnvironmentParam password) {
        this.address  = mail.getValue();
        this.password = password.getValue();
    }

    private final String address, password;
    private final        List<LocalDateTime>   timestamp        = new ArrayList<>();
    private final        List<EmailOperations> activeOperations = new ArrayList<>();
    private static final int                   connectionsLimit = 10;

    public void setUnused() {
        synchronized (timestamp) {
            if (timestamp.size() > 0) {
                LocalDateTime oldest = timestamp.get(0);
                for (LocalDateTime dt : timestamp) {
                    if (Duration.between(oldest, dt).toMillis() < 0) {
                        oldest = dt;
                    }
                }
                timestamp.remove(oldest);
            }
        }
    }

    private void removeUnused() {
        synchronized (timestamp) {
            timestamp.removeIf(
                    dt -> Duration.between(dt, LocalDateTime.now()).toMillis() > TIMESLOTS.WAIT_FOR_EMAIL_MESSAGE_MS);
        }
    }

    public void removeEmailOperation(EmailOperations operation) {
        synchronized (activeOperations) {
            activeOperations.remove(operation);
        }
    }

    public void connectToStore(EmailOperations operation, Store store, String host, String emailAddress, String emailPassword) throws MessagingException {
        synchronized (activeOperations) {
            if (activeOperations.size() >= connectionsLimit) {
                activeOperations.removeIf(op -> !op.isStoreConnected());
            }
            if (activeOperations.size() < connectionsLimit) {
                store.connect(host, emailAddress, emailPassword);
                activeOperations.add(operation);
                return;
            }
            throw new MessagingException("Too many connections right now: " + activeOperations.size());
        }
    }

    //    public static EmailAccounts randomForNewUser() {
    //        List<EmailAccounts> userAccounts = getUserAccounts();
    //        //Random order
    //        Collections.shuffle(userAccounts);
    //        for (EmailAccounts randomAccount : userAccounts) {
    //            synchronized (randomAccount.timestamp) {
    //                randomAccount.removeUnused();
    //                if (randomAccount.timestamp.size() < connectionsLimit) {
    //                    randomAccount.timestamp.add(LocalDateTime.now());
    //                    return randomAccount;
    //                }
    //            }
    //        }
    //        fail("All users accounts are used at this time");
    //        return null;
    //    }

    //    public static List<EmailAccounts> getUserAccounts() {
    //        List<EmailAccounts> userAccounts = new ArrayList<>();
    //        userAccounts.add(TAUser01);
    //        userAccounts.add(TAUser02);
    //        userAccounts.add(TAUser03);
    //        userAccounts.add(TAUser04);
    //        return userAccounts;
    //    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return this.getAddress().split("@")[0];
    }

    public String getHost() {
        return this.getAddress().split("@")[1];
    }

    public String prepareMailForUser(String userAddition) {
        return MessageFormat.format("{0}+{1}@{2}", getName(), userAddition, getHost());
    }

    public static EmailAccounts fromMail(String mail) {
        String accountMail = mail.split("\\+")[0];
        for (EmailAccounts account : values()) {
            String acc = account.getAddress().split("\\@")[0];
            if (StringUtils.equalsIgnoreCase(accountMail, acc)) {
                return account;
            }
        }
        fail("Can't find account for mail: " + mail);
        return null;
    }
}