package com.capgemini.stk.framework.basetest;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.newDrivers.DriverManager;
import com.capgemini.mrchecker.selenium.core.newDrivers.INewWebDriver;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import io.qameta.allure.Attachment;
import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class BasePageGUI extends BasePage {
    // protected static final String CONFIDENTIAL_EN = "All information displayed is confidential";
    // protected static final String CONFIDENTIAL_DE = "Alle angezeigten Informationen sind vertraulich.";

    //    private static final ThreadLocal<INewWebDriver> drv              = new ThreadLocal<>();
    //    private static final ThreadLocal<Boolean>       changeResolution = new ThreadLocal<>();
    //    private              List<String>               urls             = new ArrayList<>();
    //
    //    public static INewWebDriver getDriver() {
    //        changeResolution.set(hasDriverQuit());
    //        INewWebDriver driver = BasePage.getDriver();
    //        drv.remove();
    //        drv.set(driver);
    //        if (changeResolution.get() != null && changeResolution.get()) {
    //            driver.manage().window().maximize();
    //            Dimension      size       = driver.manage().window().getSize();
    //            ResolutionEnum resolution = ResolutionEnum.w1920;
    //            if (size.getHeight() < resolution.getHeight() || size.getWidth() < resolution.getWidth()) {
    //                ResolutionUtils.setResolution(driver, resolution);
    //            }
    //        }
    //        return driver;
    //    }
    //
    public static boolean hasDriverQuit() {
        return DriverManager.isDriverClosed();
    }

    //
    //    protected abstract boolean isPageCorrect();
    //
    //    public abstract boolean isPageOpened();
    //
    //    public final void isPageCorrectAssert() {
    //        isPageCorrectAssert(true);
    //    }
    //
    //    public final void isPageCorrectAssertStepless() {
    //        isPageCorrectAssert(false);
    //    }
    //
    //    private void isPageCorrectAssert(boolean logSuccessStep) {
    //        long timeBegin = System.currentTimeMillis();
    //        PerformAction.waitForLoadingToEndAssert();
    //        boolean       correct   = isPageCorrect();
    //        String        time      = DateTime.getTimeFromMilliseconds(System.currentTimeMillis() - timeBegin);
    //        String        className = PerformAction.splitCamelCase(this.getClass().getSimpleName());
    //        StringBuilder url       = new StringBuilder();
    //        if (this.urls.size() < 1) {
    //            url = new StringBuilder("Only the presence of an element on the page was checked");
    //        }
    //        else if (this.urls.size() < 2) {
    //            url = new StringBuilder("URL: " + this.urls.get(0));
    //        }
    //        else {
    //            url.append("One of URLs:\n");
    //            for (String alternatives : this.urls) {
    //                url.append("\t").append(alternatives).append("\n");
    //            }
    //            url = new StringBuilder(url.toString().trim());
    //        }
    //        String msgPattern = "{0} was {1}opened\n{2}\nTime: {3}";
    //        if (!correct) {
    //            makeSourcePageOnFailure();
    //            ErrorPage.check();
    //            ErrorPage.checkBodyKnownErrors();
    //            fail(MessageFormat.format(msgPattern, className, "not ", url.toString(), time));
    //        }
    //        if (logSuccessStep) {
    //            StepLogger.step(MessageFormat.format(msgPattern, className, "", url.toString(), time));
    //        }
    //    }
    //
    //    private boolean isURLMatchList(String currentURL) {
    //        for (String url : this.urls) {
    //            Pattern pattern = Pattern.compile(".*" + url + ".*");
    //            if (pattern.matcher(currentURL).matches()) {
    //                return true;
    //            }
    //        }
    //        return this.urls.isEmpty();
    //    }
    //
    //    protected final boolean isPageCorrect(By[] selectors, Object[] urlArray, boolean checkTabs) {
    //        boolean isOpened    = isPageOpened(selectors, urlArray, checkTabs);
    //        long    timeBegin   = System.currentTimeMillis();
    //        long    timeCounter = System.currentTimeMillis() - timeBegin;
    //        while (!isOpened && timeCounter < TIMESLOTS.WAIT_FOR_LAGGY_PAGE_MS) {
    //            isOpened    = isPageOpened(selectors, urlArray, checkTabs);
    //            timeCounter = System.currentTimeMillis() - timeBegin;
    //        }
    //        if (!isOpened) {
    //            if (!oneOfSelectorsVisible(selectors)) {
    //                StepLogger.error("Page object not visible. Selectors: " + Arrays.toString(selectors));
    //            }
    //            String  currentURL = PerformAction.getCurrentURL();
    //            boolean urlMatch   = isURLMatchList(currentURL);
    //            if (!urlMatch) {
    //                StepLogger.error(MessageFormat.format("Current URL ({0}) not match to list ({1})", currentURL, urls));
    //            }
    //        }
    //        return isOpened;
    //    }
    //
    //    protected final boolean isPageCorrect(By[] selectors, Object url) {
    //        return isPageCorrect(selectors, new Object[]{url}, false);
    //    }
    //
    //    protected final boolean isPageCorrect(By selector, Object url) {
    //        return isPageCorrect(new By[]{selector}, new Object[]{url}, false);
    //    }
    //
    //    protected final boolean isPageCorrect(By selector, Object url, boolean checkTabs) {
    //        return isPageCorrect(new By[]{selector}, new Object[]{url}, checkTabs);
    //    }
    //
    //    protected final boolean isPageCorrect(By selector) {
    //        return this.isPageCorrect(new By[]{selector}, new Object[0], false);
    //    }
    //
    //    protected final boolean isPageCorrect(By selector, boolean checkTabs) {
    //        return this.isPageCorrect(new By[]{selector}, new Object[0], checkTabs);
    //    }
    //
    //    protected final boolean isPageCorrect(By selector, Object[] urlArray) {
    //        return this.isPageCorrect(new By[]{selector}, urlArray, false);
    //    }
    //
    //    protected final boolean isPageOpened(By[] selectors, Object[] urlArray, boolean checkTabs) {
    //        this.urls = Arrays.stream(urlArray).map(Object::toString).collect(Collectors.toList());
    //        if (hasDriverQuit()) {
    //            StepLogger.error("Driver is closed");
    //            return false;
    //        }
    //        boolean selectorObjectVisible = oneOfSelectorsVisible(selectors);
    //        boolean urlMatch              = isURLMatchList(PerformAction.getCurrentURL());
    //        boolean isOpened              = selectorObjectVisible && urlMatch;
    //        if (!isOpened) {
    //            ErrorPage.check();
    //            if (checkTabs) {
    //                int tabsCount = PerformAction.getBrowserTabs().size();
    //                if (tabsCount > 1) {
    //                    for (int i = 0; i < tabsCount; i++) {
    //                        PerformAction.switchToBrowserTab(i);
    //                        isOpened = isPageOpened(selectors, urlArray, false);
    //                        if (isOpened) {
    //                            break;
    //                        }
    //                    }
    //                }
    //            }
    //        }
    //        return isOpened;
    //    }
    //
    //    protected final boolean isPageOpened(By selector, Object url) {
    //        return isPageOpened(new By[]{selector}, new Object[]{url}, false);
    //    }
    //
    //    protected final boolean isPageOpened(By selector, Object url, boolean checkTabs) {
    //        return isPageOpened(new By[]{selector}, new Object[]{url}, checkTabs);
    //    }
    //
    //    protected final boolean isPageOpened(By[] selectors, Object url) {
    //        return isPageOpened(selectors, new Object[]{url}, false);
    //    }
    //
    //    protected final boolean isPageOpened(By selector) {
    //        return this.isPageOpened(new By[]{selector}, new Object[0], false);
    //    }
    //
    //    protected final boolean isPageOpened(By selector, boolean checkTabs) {
    //        return this.isPageOpened(new By[]{selector}, new Object[0], checkTabs);
    //    }
    //
    //    protected final boolean isPageOpened(By selector, Object[] urlArray) {
    //        return this.isPageOpened(new By[]{selector}, urlArray, false);
    //    }
    //
    //    private boolean oneOfSelectorsVisible(By[] selectors) {
    //        for (By selector : selectors) {
    //            if (PerformAction.isVisible(selector)) {
    //                return true;
    //            }
    //        }
    //        return false;
    //    }
    //
    //    @Override
    //    public final boolean isLoaded() {
    //        return false;
    //    }
    //
    //    @Override
    //    public final void load() {
    //    }
    //
    //    @Override
    //    public final String pageTitle() {
    //        return null;
    //    }
    //
    //    @Override
    //    public void onTestClassFinish() {
    //        BFLogger.logDebug("[AFTER CLASS - BASE PAGE GUI] onTestClassFinish");
    //        DriverManager.closeDriver();
    //    }
    //
    static void makeScreenShot(String attachmentName) {
        BFLogger.logInfo("*makeScreenShot*");
        if (System.getProperty("screenshots", "true").equalsIgnoreCase("false")) {
            BFLogger.logInfo("screenshot disabled");
        }
        else if (!hasDriverQuit()) {
            makeScreenShotAlways(attachmentName);
        }
        else {
            StepLogger.info("Unable to take screenshot - No driver");
        }
    }

    //
    //    static void makeScreenShot(String attachmentName, WebElement element) {
    //        BFLogger.logInfo("*makeScreenShot*Element*");
    //        if (System.getProperty("screenshots", "true").equalsIgnoreCase("false")) {
    //            BFLogger.logInfo("screenshot disabled");
    //        }
    //        else if (!hasDriverQuit()) {
    //            makeScreenShotAlways(attachmentName, element);
    //        }
    //        else {
    //            StepLogger.info("Unable to take screenshot - No driver");
    //        }
    //    }
    //
    @Attachment(value = "{attachmentName}", type = "image/png")
    private static byte[] makeScreenShotAlways(String attachmentName) {
        BFLogger.logDebug("BasePageGUI.makeScreenShotAlways attachmentName=" + attachmentName);
        byte[] screenshot = new byte[0];
        try {
            screenshot = Files.readAllBytes(Paths.get("src/resources/img/noImage.png"));
        }
        catch (IOException ignored) {
        }
        if (!hasDriverQuit()) {
            try {
                INewWebDriver driver = getDriver();
                return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            }
            catch (Exception e) {
                return screenshot;
            }
        }
        else {
            BFLogger.logDebug("Unable to take screenshot - No driver");
        }
        return screenshot;
    }

    @Attachment(value = "{attachmentName}", type = "image/png")
    private static byte[] makeScreenShotAlways(String attachmentName, WebElement element) {
        BFLogger.logDebug("BasePageGUI.makeScreenShotAlways Element attachmentName=" + attachmentName);
        byte[] screenshot = new byte[0];
        try {
            screenshot = Files.readAllBytes(Paths.get("src/resources/img/noImage.png"));
        }
        catch (IOException ignored) {
        }
        if (!hasDriverQuit()) {
            try {
                INewWebDriver driver        = getDriver();
                BufferedImage fullImg       = ImageIO.read(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE));
                int           fullImgWidth  = fullImg.getWidth();
                int           fullImgHeight = fullImg.getHeight();
                Point         elementPoint  = element.getLocation();
                int           elementStartX = elementPoint.getX();
                int           elementStartY = elementPoint.getY();
                Dimension     elementSize   = element.getSize();
                int           elementWidth  = elementSize.getWidth();
                int           elementHeight = elementSize.getHeight();
                if (elementWidth >= (fullImgWidth - elementStartX)) {
                    elementWidth = fullImgWidth - elementStartX - 1;
                }
                if (elementHeight >= (fullImgHeight - elementStartY)) {
                    elementHeight = fullImgHeight - elementStartY - 1;
                }
                RenderedImage elementScreenshot = fullImg
                        .getSubimage(elementStartX, elementStartY, elementWidth, elementHeight);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write(elementScreenshot, "png", stream);
                screenshot = stream.toByteArray();
                stream.close();
            }
            catch (Throwable e) {
                return screenshot;
            }
        }
        else {
            BFLogger.logDebug("Unable to take element screenshot - No driver");
        }
        return screenshot;
    }

    @Override
    public byte[] makeScreenshotOnFailure() {
        BFLogger.logDebug("BasePageGUI.makeScreenshotOnFailure");
        return makeScreenShotAlways("Screenshot on failure");
    }
}