package com.capgemini.stk.framework.basetest;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.allure.model.Link;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import net.lightbody.bmp.core.har.Har;
import org.jsoup.Jsoup;
import org.junit.Assume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.nio.file.StandardOpenOption.APPEND;

@SuppressWarnings("deprecation")
public final class StepLogger {
    private StepLogger() {
    }

    @Step("[INFO] {info}")
    public static void info(String info) {
        BFLogger.logInfo("[INFO] " + info);
    }

    public static void error(String error) {
        String message = "[ERROR] " + error;
        String uuid    = UUID.randomUUID().toString();
        try {
            AllureLifecycle allureLifeCycle = Allure.getLifecycle();
            allureLifeCycle.startStep(uuid, new StepResult().withStatus(Status.FAILED).withName(message));
            BFLogger.logError(message);
            makeScreenShot();
            allureLifeCycle.stopStep(uuid);
        }
        catch (NoSuchElementException e) {
            info(message);
        }
    }

    public static void issue(String grp) {
        String pattern = "https://devstack.vwgroup.com/jira/browse/";
        try {
            Allure.addLinks(new Link().withType("issue").withName(grp).withUrl(pattern + grp));
        }
        catch (NullPointerException e) {
            // Catch when no allure report
            info(pattern + grp);
        }
    }

    public static void tmsLink(String grp) {
        String pattern = "https://devstack.vwgroup.com/jira/browse/";
        try {
            Allure.addLinks(new Link().withType(io.qameta.allure.util.ResultsUtils.TMS_LINK_TYPE).withName(grp)
                                      .withUrl(pattern + grp));
        }
        catch (NullPointerException e) {
            // Catch when no allure report
            info(pattern + grp);
        }
    }

    @Step("{step}")
    public static void step(String step) {
        BFLogger.logInfo(step);
    }

    @Step("[SKIP] {step}")
    static void stepSkip(String step) {
        Assume.assumeTrue(step, false);
    }

    @Step("{nameWrapper}")
    public static void addListOfItemsToRaport(List<String> itemListByName, String nameWrapper) {
        for (String x : itemListByName) {
            step(x);
        }
    }

    @Step("--Screenshot--")
    public static void makeScreenShot() {
        BasePageGUI.makeScreenShot("Screenshot");
    }

    //    @Step("-- {elementName} Screenshot--")
    //    public static void makeScreenShot(WebElement element, String elementName) {
    //        BasePageGUI.makeScreenShot(elementName + " Screenshot", element);
    //    }
    //
    //    @Step("-- {elementName} Screenshot--")
    //    public static void makeScreenShot(By selector, String elementName) {
    //        List<WebElement> list = PerformAction.findElementDynamics(selector);
    //        if (list.size() > 0) {
    //            BasePageGUI.makeScreenShot(elementName + " Screenshot", list.get(0));
    //        }
    //        else {
    //            info("Unable to take screenshot - Element not found");
    //        }
    //    }

    /**
     * Taking a body content from mail and attach it to allure-result
     *
     * @author Jacek Z
     */
    public static void saveEmailToLog(String attachName, String message) {
        String msg = message.replaceAll("<br/>", "&#13;");
        msg = msg.replaceAll("<br>", "&#13;");
        msg = Jsoup.parse(msg).wholeText();
        saveTextAttachmentToLog(attachName, msg);
    }

    public static void saveMailCalendarToLog(String attachName, String message) {
        saveTextAttachmentToLog("Mail calendar attachment", message);
    }

    /**
     * Taking a text and attach it to allure-result
     *
     * @return String
     * @author Mariusz K
     * @author Jacek Z
     */
    @Attachment(value = "{attachName}", type = "text/plain")
    public static String saveTextAttachmentToLog(String attachName, String message) {
        BFLogger.logInfo(attachName.toUpperCase() + ":\n" + message);
        return message;
    }

    @Attachment(value = "{attachName}", type = "text/plain")
    public static String saveTextAttachmentToLogNoConsole(String attachName, String message) {
        return message;
    }

    @Attachment(value = "{name}", type = "text/csv")
    private static byte[] attachCSVFile(File file, String name) throws IOException {
        return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
    }

    @Attachment(value = "{name}", type = "text/plain")
    private static byte[] attachTXTFile(File file, String name) throws IOException {
        return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
    }

    @Attachment("Zipped [{name}]")
    private static byte[] attachZippedFile(File fileToAttach, String name) throws IOException {
        String           TEMP_PATH     = System.getProperty("java.io.tmpdir");
        String           ZIP_FILE_NAME = "attachement.zip";
        File             ZIP_FILE      = new File(TEMP_PATH, ZIP_FILE_NAME);
        byte[]           buffer        = new byte[1024];
        FileOutputStream fos           = new FileOutputStream(ZIP_FILE);
        ZipOutputStream  zos           = new ZipOutputStream(fos);
        FileInputStream  fis           = new FileInputStream(fileToAttach);
        zos.putNextEntry(new ZipEntry(fileToAttach.getName()));
        int length;
        while ((length = fis.read(buffer)) > 0) {
            zos.write(buffer, 0, length);
        }
        zos.closeEntry();
        fis.close();
        zos.close();
        return Files.readAllBytes(Paths.get(ZIP_FILE.getAbsolutePath()));
    }

    @Attachment(value = "{name}", type = "application/pdf")
    private static byte[] attachPDFFile(File file, String name) throws IOException {
        return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
    }

    @Attachment(value = "{name}", type = "image/png")
    private static byte[] attachPNGFile(File file, String name) throws IOException {
        return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
    }

    @Attachment(value = "{name}", type = "application/zip")
    private static byte[] attachZIPFile(File file, String name) throws IOException {
        return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
    }

    public static void attachFile(File file, String name) throws IOException {
        String fileName  = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (file.length() == 0) {
            Files.write(Paths.get(file.toURI()), " ".getBytes(), APPEND);
        }
        switch (extension.toLowerCase()) {
            case "pdf": {
                attachPDFFile(file, name);
                break;
            }
            case "xlsx": {
                attachZippedFile(file, name);
                break;
            }
            case "csv": {
                attachCSVFile(file, name);
                break;
            }
            case "png": {
                attachPNGFile(file, name);
                break;
            }
            case "zip": {
                attachZIPFile(file, name);
                break;
            }
            case "txt": {
                attachTXTFile(file, name);
                break;
            }
            default:
                error("Couldn't attach file with extension: " + extension);
        }
    }

    public static void attachHarFile(Har har) {
        String       harText = "";
        StringWriter writer  = new StringWriter();
        try {
            har.writeTo(writer);
            harText = writer.toString();
        }
        catch (IOException e) {
            StepLogger.error(e.getMessage());
        }
        Gson        gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser  jp   = new JsonParser();
        JsonElement je   = jp.parse(harText);
        harText = gson.toJson(je);
        saveTextAttachmentToLogNoConsole("HAR File", harText);
    }
}
