package com.capgemini.stk.framework.actions;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.MessageFormat;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assume.assumeTrue;

public final class PerformAction extends BasePage {
    private PerformAction() {
    }

    @Override
    public String pageTitle() {
        return null;
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public void load() {

    }

    //
    //    public static INewWebDriver getDriver() {
    //        // Create stub page here to handle driver closing
    //        if (BasePageGUI.hasDriverQuit() || stubPage.get() == null) {
    //            stubPage.remove();
    //            stubPage.set(new PopupMessagePage());
    //        }
    //        PopupMessagePage page = stubPage.get();
    //        return page.getDriver();
    //    }
    //
    //    public static Actions getAction() {
    //        return new Actions(getDriver());
    //    }
    //
    //    public static String getCurrentURL() {
    //        return getDriver().getCurrentUrl();
    //    }
    //
    //    public static String getCurrentTitle() {
    //        return getDriver().getTitle();
    //    }
    //
    //    public static WebElement findElementByContainingText(String text) {
    //        JavascriptExecutor js  = null;
    //        INewWebDriver      drv = getDriver();
    //        if (drv instanceof JavascriptExecutor) {
    //            js = (JavascriptExecutor) drv;
    //        }
    //        else {
    //            fail("Cannot execute JavaScript");
    //        }
    //        String     jQuery          = "return $(\":contains('" + text + "'):last\").get(0);";
    //        WebElement elementToSearch = (WebElement) js.executeScript(jQuery);
    //        assertNotNull("Element with text \"" + text + "\" does not exist", elementToSearch);
    //        return elementToSearch;
    //    }
    //
    //    static void executeJquery(String text) {
    //        JavascriptExecutor js  = null;
    //        INewWebDriver      drv = getDriver();
    //        if (drv instanceof JavascriptExecutor) {
    //            js = (JavascriptExecutor) drv;
    //        }
    //        else {
    //            fail("Cannot execute JavaScript");
    //        }
    //        js.executeScript(text);
    //    }
    //
    //    public static WebElement findElementByParentAndContainingText(String parent, String text) {
    //        JavascriptExecutor js  = null;
    //        INewWebDriver      drv = getDriver();
    //        if (drv instanceof JavascriptExecutor) {
    //            js = (JavascriptExecutor) drv;
    //        }
    //        else {
    //            fail("Cannot execute JavaScript");
    //        }
    //        String     jQuery          = "return $(\"" + parent + ":contains('" + text + "'):last\").get(0);";
    //        WebElement elementToSearch = (WebElement) js.executeScript(jQuery);
    //        assertNotNull("Element with parent \"" + parent + "\" and text \"" + text + "\" does not exist",
    //                      elementToSearch);
    //        return elementToSearch;
    //    }
    //
    //    @Step("Fill text field \"{fieldName}\" with \"{inputValue}\"")
    //    public static String fillTextField(By selector, String inputValue, String fieldName) {
    //        BFLogger.logInfo(MessageFormat.format("Fill text field \"{0}\" with \"{1}\"", fieldName, inputValue));
    //        return fillTextFieldStepless(selector, inputValue);
    //    }
    //
    //    @Step("Fill text field \"{fieldName}\" with \"{inputValue}\"")
    //    public static String fillTextField(WebElement element, String inputValue, String fieldName) {
    //        BFLogger.logInfo(MessageFormat.format("Fill text field \"{0}\" with \"{1}\"", fieldName, inputValue));
    //        return fillTextFieldStepless(element, inputValue);
    //    }
    //
    //    @Step("Fill text field \"{fieldName}\" with password")
    //    public static String fillTextFieldPassword(By selector, String password, String fieldName) {
    //        BFLogger.logInfo(MessageFormat.format("Fill text field \"{0}\" with password", fieldName));
    //        return fillTextFieldStepless(selector, password);
    //    }
    //
    //    @Step("Fill text field \"{fieldName}\" with \"{inputValue}\"")
    //    public static String fillTextFieldInstant(By selector, String inputValue, String fieldName) {
    //        BFLogger.logInfo(MessageFormat.format("Fill text field \"{0}\" with \"{1}\"", fieldName, inputValue));
    //        return fillTextFieldSteplessInstant(selector, inputValue);
    //    }
    //
    //    public static String fillTextFieldStepless(By selector, String inputValue) {
    //        // Fill the inputFiled quickly
    //        try {
    //            String inputtedValue = fillTextFieldSteplessInstant(selector, inputValue);
    //            if (inputtedValue.equals(inputValue)) {
    //                return inputtedValue;
    //            }
    //            BFLogger.logInfo("Slow typing needed");
    //        }
    //        catch (Throwable e) {
    //            ErrorPage.check();
    //            clearTextFieldStepless(selector);
    //            // if this was not successful, then the slow typing below will be used
    //        }
    //        // Fill the inputFiled slowly (quickly was not successful)
    //        return fillTextFieldSteplessByTyping(selector, inputValue);
    //    }
    //
    //    public static String fillTextFieldStepless(WebElement element, String inputValue) {
    //        // Fill the inputFiled quickly
    //        try {
    //            String inputtedValue = fillTextFieldSteplessInstant(element, inputValue);
    //            if (inputtedValue.equals(inputValue)) {
    //                return inputtedValue;
    //            }
    //            BFLogger.logInfo("Slow typing needed");
    //        }
    //        catch (Throwable e) {
    //            ErrorPage.check();
    //            clearTextFieldStepless(element);
    //            // if this was not successful, then the slow typing below will be used
    //        }
    //        // Fill the inputFiled slowly (quickly was not successful)
    //        return fillTextFieldSteplessByTyping(element, inputValue);
    //    }
    //
    //    public static String fillTextFieldSteplessByTyping(By selector, String inputValue) {
    //        int attempts = 0;
    //        while (attempts < 5) {
    //            try {
    //                WebElement element = getDriver().findElementDynamic(selector);
    //                waitUntilClickable(element);
    //                for (int i = 0; i < inputValue.length(); i++) {
    //                    char c = inputValue.charAt(i);
    //                    element.sendKeys(Character.toString(c));
    //                    waitMilliseconds(TIMESLOTS.WAIT_TYPING_DELAY_MS);
    //                }
    //                // try trigger events
    //                tryTriggerEventsOnInput(element);
    //                return getTextFieldValue(selector);
    //            }
    //            catch (Throwable e) {
    //                ErrorPage.check();
    //                clearTextFieldStepless(selector);
    //                waitMilliseconds(TIMESLOTS.WAIT_INSTANT_MOMENT_MS);
    //            }
    //            attempts++;
    //        }
    //
    //        fail("Can't fill text field. Selector: " + selector);
    //        return null;
    //    }
    //
    //    public static String fillTextFieldSteplessByTyping(WebElement element, String inputValue) {
    //        int attempts = 0;
    //        while (attempts < 5) {
    //            try {
    //                waitUntilClickable(element);
    //                for (int i = 0; i < inputValue.length(); i++) {
    //                    char c = inputValue.charAt(i);
    //                    element.sendKeys(Character.toString(c));
    //                    waitMilliseconds(TIMESLOTS.WAIT_TYPING_DELAY_MS);
    //                }
    //                // try trigger events
    //                tryTriggerEventsOnInput(element);
    //                return getTextFieldValue(element);
    //            }
    //            catch (Throwable e) {
    //                ErrorPage.check();
    //                clearTextFieldStepless(element);
    //                waitMilliseconds(TIMESLOTS.WAIT_INSTANT_MOMENT_MS);
    //            }
    //            attempts++;
    //        }
    //
    //        fail("Can't fill text field. Element: " + element);
    //        return null;
    //    }
    //
    //    private static void tryTriggerEventsOnInput(WebElement element) {
    //        // try trigger events
    //        try {
    //            WebDriver          driver = getDriver();
    //            JavascriptExecutor jse    = (JavascriptExecutor) driver;
    //            jse.executeScript("arguments[0].focus();", element);
    //            getAction().moveToElement(element).keyDown(Keys.LEFT_SHIFT).keyUp(Keys.LEFT_SHIFT).build().perform();
    //        }
    //        catch (Throwable ignored) {
    //        }
    //    }
    //
    //    public static String fillTextFieldSteplessInstant(By selector, String inputValue) {
    //        WebElement element = getDriver().findElementDynamic(selector);
    //        element.sendKeys(inputValue);
    //        if (inputValue.equals(Keys.ENTER.toString()) || inputValue.equals(Keys.TAB.toString())) {
    //            return inputValue;
    //        }
    //        // try trigger events
    //        tryTriggerEventsOnInput(element);
    //        return getTextFieldValue(selector);
    //    }
    //
    //    public static String fillTextFieldSteplessInstant(WebElement element, String inputValue) {
    //        element.sendKeys(inputValue);
    //        if (inputValue.equals(Keys.ENTER.toString()) || inputValue.equals(Keys.TAB.toString())) {
    //            return inputValue;
    //        }
    //
    //        // try trigger events
    //        tryTriggerEventsOnInput(element);
    //        return getTextFieldValue(element);
    //    }
    //
    //    public static String getTextFieldValue(By selector) {
    //        int attempts = 0;
    //        while (attempts < 2) {
    //            try {
    //                return getElementAttribute(selector, "value");
    //            }
    //            catch (Throwable ignored) {
    //                ErrorPage.check();
    //            }
    //            attempts++;
    //        }
    //        fail("Can't get text from field. Selector: " + selector);
    //        return null;
    //    }
    //
    //    public static String getTextFieldValue(WebElement element) {
    //        int attempts = 0;
    //        while (attempts < 2) {
    //            try {
    //                return getElementAttribute(element, "value");
    //            }
    //            catch (Throwable ignored) {
    //                ErrorPage.check();
    //            }
    //            attempts++;
    //        }
    //        fail("Can't get text from field. Element: " + element);
    //        return null;
    //    }
    //
    //    @Step("Clear text field \"{fieldName}\"")
    //    public static void clearTextField(By selector, String fieldName) {
    //        assertTrue(MessageFormat.format("Text field \"{0}\" is not visible", fieldName),
    //                   PerformAction.isVisible(selector));
    //        assertTrue(MessageFormat.format("Text field \"{0}\" is disabled", fieldName),
    //                   PerformAction.isEnabled(selector));
    //        BFLogger.logInfo(MessageFormat.format("Clear text field \"{0}\"", fieldName));
    //        clearTextFieldStepless(selector);
    //    }
    //
    //    @Step("Clear text field \"{fieldName}\"")
    //    public static void clearTextField(WebElement element, String fieldName) {
    //        assertTrue(MessageFormat.format("Text field \"{0}\" is not visible", fieldName),
    //                   PerformAction.isVisible(element));
    //        assertTrue(MessageFormat.format("Text field \"{0}\" is disabled", fieldName), PerformAction.isEnabled(element));
    //        BFLogger.logInfo(MessageFormat.format("Clear text field \"{0}\"", fieldName));
    //        clearTextFieldStepless(element);
    //    }
    //
    //    public static void clearTextFieldStepless(By selector) {
    //        int attempts = 0;
    //        while (attempts < 2) {
    //            try {
    //                WebElement element = getDriver().findElementDynamic(selector);
    //                waitUntilClickable(element);
    //                element.clear();
    //                // try trigger events
    //                tryTriggerEventsOnInput(element);
    //                return;
    //            }
    //            catch (Throwable ignored) {
    //                ErrorPage.check();
    //            }
    //            attempts++;
    //        }
    //    }
    //
    //    public static void clearTextFieldStepless(WebElement element) {
    //        int attempts = 0;
    //        while (attempts < 2) {
    //            try {
    //                waitUntilClickable(element);
    //                element.clear();
    //                // try trigger events
    //                tryTriggerEventsOnInput(element);
    //                return;
    //            }
    //            catch (Throwable ignored) {
    //                ErrorPage.check();
    //            }
    //            attempts++;
    //        }
    //    }
    //
    //    public static void waitUntilElementDisappear(By selector, String name) {
    //        long timeBegin   = System.currentTimeMillis();
    //        long timeCounter = System.currentTimeMillis() - timeBegin;
    //        while (timeCounter < TIMESLOTS.WAIT_VERY_LONG_MOMENT_MS) {
    //            if (!isVisible(selector)) {
    //                return;
    //            }
    //            timeCounter = System.currentTimeMillis() - timeBegin;
    //        }
    //        assertFalse(name + " is still visible. Selector: " + selector, isVisible(selector));
    //    }
    //
    //    public static void waitUntilElementDisappear(By selector, int milliseconds, String name) {
    //        long timeBegin   = System.currentTimeMillis();
    //        long timeCounter = System.currentTimeMillis() - timeBegin;
    //        while (timeCounter < milliseconds) {
    //            if (!isVisible(selector)) {
    //                return;
    //            }
    //            timeCounter = System.currentTimeMillis() - timeBegin;
    //        }
    //        assertFalse(name + " is still visible after " + TimeUnit.MILLISECONDS
    //                .toSeconds(timeCounter) + " seconds. Selector: " + selector, isVisible(selector));
    //    }
    //
    //    public static void waitUntilElementDisappearNoAssert(By selector, int milliseconds) {
    //        long timeBegin   = System.currentTimeMillis();
    //        long timeCounter = System.currentTimeMillis() - timeBegin;
    //        while (timeCounter < milliseconds) {
    //            if (!isVisible(selector)) {
    //                return;
    //            }
    //            timeCounter = System.currentTimeMillis() - timeBegin;
    //        }
    //    }
    //
    //    @Step("Click button \"{buttonName}\"")
    //    public static void clickButton(By selector, String buttonName) {
    //        waitForElementVisible(selector, buttonName + " button");
    //        assertTrue(MessageFormat.format("Button \"{0}\" is not visible", buttonName),
    //                   PerformAction.isVisible(selector));
    //        assertTrue(MessageFormat.format("Button \"{0}\" is disabled", buttonName), PerformAction.isEnabled(selector));
    //        BFLogger.logInfo(MessageFormat.format("Click button \"{0}\"", buttonName));
    //        clickStepless(selector);
    //    }
    //
    //    @Step("Click element \"{name}\"")
    //    public static void clickElement(By selector, String name) {
    //        waitForElementVisible(selector, name);
    //        assertTrue(MessageFormat.format("Element \"{0}\" is not visible", name), PerformAction.isVisible(selector));
    //        assertTrue(MessageFormat.format("Element \"{0}\" is disabled", name), PerformAction.isEnabled(selector));
    //        BFLogger.logInfo(MessageFormat.format("Click element \"{0}\"", name));
    //        clickStepless(selector);
    //    }
    //
    //    @Step("Click element \"{name}\"")
    //    public static void clickElement(WebElement element, String name) {
    //        assertTrue(MessageFormat.format("Element \"{0}\" is not visible", name), PerformAction.isVisible(element));
    //        assertTrue(MessageFormat.format("Element \"{0}\" is disabled", name), PerformAction.isEnabled(element));
    //        BFLogger.logInfo(MessageFormat.format("Click element \"{0}\"", name));
    //        clickStepless(element);
    //    }
    //
    //    public static void clickStepless(By selector) {
    //        int attempts = 0;
    //        while (attempts < 4) {
    //            try {
    //                waitUntilClickable(selector);
    //                getDriver().findElementDynamic(selector).click();
    //                return;
    //            }
    //            catch (Throwable ignored) {
    //                ErrorPage.check();
    //            }
    //            attempts++;
    //            waitMilliseconds(TIMESLOTS.WAIT_INSTANT_MOMENT_MS);
    //        }
    //        fail("Can't click element. Selector: " + selector);
    //    }
    //
    //    public static void clickStepless(WebElement element) {
    //        int attempts = 0;
    //        while (attempts < 4) {
    //            try {
    //                waitUntilClickable(element);
    //                element.click();
    //                return;
    //            }
    //            catch (Throwable ignored) {
    //                ErrorPage.check();
    //            }
    //            attempts++;
    //            waitMilliseconds(TIMESLOTS.WAIT_INSTANT_MOMENT_MS);
    //        }
    //        fail("Can't click element. Element: " + element);
    //    }
    //
    //    public static void clickStepless(WebElement element, Keys key) {
    //        int attempts = 0;
    //        while (attempts < 4) {
    //            try {
    //                waitUntilClickable(element);
    //                getAction().keyDown(key).click(element).keyUp(key).build().perform();
    //                return;
    //            }
    //            catch (Throwable ignored) {
    //                ErrorPage.check();
    //            }
    //            attempts++;
    //            waitMilliseconds(TIMESLOTS.WAIT_INSTANT_MOMENT_MS);
    //        }
    //        fail("Can't click element. Element: " + element);
    //    }
    //
    //    public static void setAttribute(WebElement element, String attName, String attValue) {
    //        JavascriptExecutor je = (JavascriptExecutor) getDriver();
    //        je.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, attName, attValue);
    //    }
    //
    //    @Step("Double click element \"{name}\"")
    //    public static void doubleClickElement(By selector, String name) {
    //        BFLogger.logInfo(MessageFormat.format("DoubleClick element \"{0}\"", name));
    //        doubleClickStepless(selector);
    //    }
    //
    //    @Step("Double click element \"{name}\"")
    //    public static void doubleClickElement(WebElement element, String name) {
    //        BFLogger.logInfo(MessageFormat.format("DoubleClick element \"{0}\"", name));
    //        doubleClickStepless(element);
    //    }
    //
    //    private static void doubleClickStepless(By selector) {
    //        int attempts = 0;
    //        while (attempts < 2) {
    //            try {
    //                WebElement element = getDriver().findElementDynamic(selector);
    //                doubleClickStepless(element);
    //                return;
    //            }
    //            catch (Throwable ignored) {
    //                ErrorPage.check();
    //            }
    //            attempts++;
    //        }
    //        fail("Can't double-click element. Selector: " + selector);
    //    }
    //
    //    private static void doubleClickStepless(WebElement element) {
    //        int attempts = 0;
    //        while (attempts < 4) {
    //            try {
    //                waitUntilClickable(element);
    //                getAction().doubleClick(element).perform();
    //                return;
    //            }
    //            catch (Throwable ignored) {
    //                ErrorPage.check();
    //            }
    //            attempts++;
    //        }
    //        fail("Can't double-click element");
    //    }
    //
    //    private static String trySelectValueFromDropBoxNoAssert(By selectorLabel, By selectorPanel, By selectorValues, String newValue, String name) {
    //        String error = "";
    //        try {
    //            boolean panelVisible = waitForElementVisibleNoAssertion(selectorPanel, TIMESLOTS.WAIT_SHORT_MOMENT_MS);
    //            if (!panelVisible) {
    //                clickButton(selectorLabel, name);
    //                waitForElementVisible(selectorPanel, name + " list panel");
    //                waitUntilOpacityIsOver(selectorPanel, 30);
    //            }
    //            if (waitForElementVisibleNoAssertion(selectorPanel, TIMESLOTS.WAIT_SHORT_MOMENT_MS)) {
    //                // Fill input if exists
    //                String panelInputsSelectorText = selectorPanel.toString();
    //                panelInputsSelectorText = panelInputsSelectorText.substring(panelInputsSelectorText.indexOf(":") + 1)
    //                                                                 .trim() + " input";
    //                By panelInputsSelector = By.cssSelector(panelInputsSelectorText);
    //                if (waitForElementVisibleNoAssertion(panelInputsSelector, TIMESLOTS.WAIT_SHORT_MOMENT_MS)) {
    //                    clearTextFieldStepless(panelInputsSelector);
    //                    fillTextField(panelInputsSelector, newValue, name + " : Text input");
    //                }
    //
    //                boolean valueExists = false;
    //                long    timeBegin   = System.currentTimeMillis();
    //                long    timeCounter = 0;
    //                while (!valueExists && timeCounter < TIMESLOTS.WAIT_MEDIUM_MOMENT_MS) {
    //                    List<String> list = getTextOfElements(selectorValues);
    //                    for (String value : list) {
    //                        if (StringUtils.equalsIgnoreCase(value, newValue)) {
    //                            valueExists = true;
    //                            break;
    //                        }
    //                    }
    //                    timeCounter = System.currentTimeMillis() - timeBegin;
    //                }
    //                if (valueExists) {
    //                    selectValueFromList(selectorValues, selectorLabel, newValue, name);
    //                    waitUntilElementDisappear(selectorPanel, name + " list panel");
    //                    waitForLoadingToEndAssert();
    //                    BFLogger.logInfo(name + " selected value: " + newValue);
    //                    return "";
    //                }
    //                else {
    //                    return "There is no dropdown item: " + newValue;
    //                }
    //            }
    //        }
    //        catch (Throwable e) {
    //            error = e.getMessage();
    //            ErrorPage.check();
    //        }
    //        return error;
    //    }
    //
    //    public static void selectValueFromDropBox(By selectorLabel, By selectorPanel, By selectorValues, String newValue, String name) {
    //        int counter = 0;
    //        String error = trySelectValueFromDropBoxNoAssert(selectorLabel, selectorPanel, selectorValues, newValue, name);
    //        while (counter < 3 && !error.isEmpty()) {
    //            error = trySelectValueFromDropBoxNoAssert(selectorLabel, selectorPanel, selectorValues, newValue, name);
    //            waitMilliseconds(TIMESLOTS.WAIT_MOMENT_MS);
    //            counter++;
    //        }
    //        String message = MessageFormat.format("Can''t select {0} in {1}\n{2}", newValue, name, error);
    //        assertTrue(message, error.isEmpty());
    //    }
    //
    //    @Step("Select \"{inputValue}\" from \"{listName}\"")
    //    public static void selectValueFromList(By selectorValues, By selectorLabel, String inputValue, String listName) {
    //        selectValueFromListStepless(selectorValues, selectorLabel, inputValue, listName);
    //    }
    //
    //    private static void selectValueFromListStepless(By selectorValues, By selectorLabel, String inputValue, String listName) {
    //        // Wait for list update
    //        long timeBegin   = System.currentTimeMillis();
    //        long timeCounter = 0;
    //        while (timeCounter < TIMESLOTS.WAIT_MEDIUM_MOMENT_MS) {
    //            try {
    //                List<WebElement> listOfElements = findElementDynamics(selectorValues);
    //                for (WebElement x : listOfElements) {
    //                    String value          = x.getText();
    //                    String dataLabelValue = getElementAttribute(x, "data-label");
    //                    if ((value != null && StringUtils
    //                            .equalsIgnoreCase(value, inputValue)) || (dataLabelValue != null && StringUtils
    //                            .equalsIgnoreCase(dataLabelValue, inputValue))) {
    //                        waitUntilClickable(x);
    //                        clickElement(x, inputValue);
    //                        return;
    //                    }
    //                }
    //            }
    //            catch (Throwable ignored) {
    //                ErrorPage.check();
    //            }
    //            timeCounter = System.currentTimeMillis() - timeBegin;
    //        }
    //        String text = getElementText(selectorLabel);
    //        StepLogger.info(listName + " Text: " + text);
    //        BFLogger.logInfo(selectorLabel.toString());
    //        BFLogger.logInfo(listName + " Text: " + text);
    //        StepLogger.info(listName + " Items: " + getTextOfElements(selectorValues));
    //        fail("Item \"" + inputValue + "\" was not found in \"" + listName + "\"");
    //    }
    //
    //    @Step("Select item from dropbox by value \"{value}\"")
    //    public static void selectItemFromDropBoxByValue(By selector, String value) {
    //        String val  = value.replace(" ", "");
    //        Select item = new Select(getDriver().findElementDynamic(selector));
    //        item.selectByValue(val);
    //    }
    //
    //    public static void waitUntilClickable(WebElement element) {
    //        boolean clickable   = false;
    //        long    timeBegin   = System.currentTimeMillis();
    //        long    timeCounter = 0;
    //        while (!clickable && timeCounter < TIMESLOTS.WAIT_LONG_MOMENT_MS) {
    //            clickable   = isVisible(element) && isEnabled(element);
    //            timeCounter = System.currentTimeMillis() - timeBegin;
    //        }
    //    }
    //
    //    public static void waitUntilClickable(By selector) {
    //        int attempts = 0;
    //        while (attempts < 2) {
    //            try {
    //                getDriver().waitUntilElementIsClickable(selector);
    //                break;
    //            }
    //            catch (Throwable ignored) {
    //            }
    //            attempts++;
    //        }
    //    }
    //
    //    public static void moveToElement(WebElement element) {
    //        int counter = 0;
    //        while (counter < 10) {
    //            try {
    //                JavascriptExecutor je = (JavascriptExecutor) getDriver();
    //                je.executeScript("arguments[0].scrollIntoView(true);", element);
    //                getAction().moveToElement(element).perform();
    //                return;
    //            }
    //            catch (Throwable e) {
    //                ErrorPage.check();
    //                counter++;
    //            }
    //        }
    //    }
    //
    //    public static void moveToElement(By selector) {
    //        int counter = 0;
    //        while (counter < 10) {
    //            try {
    //                WebElement element = getDriver().findElementDynamic(selector);
    //                moveToElement(element);
    //                return;
    //            }
    //            catch (Throwable e) {
    //                ErrorPage.check();
    //                counter++;
    //            }
    //        }
    //    }
    //
    //    public static void scrollToTop() {
    //        JavascriptExecutor je = (JavascriptExecutor) getDriver();
    //        je.executeScript("window.scrollTo(0, 0);");
    //    }
    //
        public static boolean isVisible(By selector) {
            try {
                WebElement webElement = getDriver().findElementDynamic(selector, 0);
                return webElement.isDisplayed();
            }
            catch (Throwable ignored) {
            }
            return false;
        }
    //
    //    public static boolean isVisible(WebElement element) {
    //        try {
    //            return element.isDisplayed();
    //        }
    //        catch (Throwable ignored) {
    //        }
    //        return false;
    //    }
    //
    //    public static boolean isEnabled(By selector) {
    //        boolean isEditable;
    //        try {
    //            WebElement webElement = getDriver().findElementDynamic(selector);
    //            isEditable = isEnabled(webElement);
    //        }
    //        catch (Throwable a) {
    //            isEditable = false;
    //        }
    //        return isEditable;
    //    }
    //
    //    public static boolean isEnabled(WebElement element) {
    //        boolean isEditable;
    //        try {
    //            boolean enabled              = element.isEnabled();
    //            String  classAttribute       = getElementClassAttribute(element);
    //            String  ariaAttribute        = getElementAttribute(element, "aria-disabled");
    //            boolean classUiStateDisabled = classAttribute != null && classAttribute.contains("ui-state-disabled");
    //            boolean classAriaDisabled    = ariaAttribute != null && ariaAttribute.contains("true");
    //            isEditable = enabled && !classUiStateDisabled && !classAriaDisabled;
    //        }
    //        catch (Throwable a) {
    //            isEditable = false;
    //        }
    //        return isEditable;
    //    }
    //
    //    @Step("\"{itemName}\" is filling only by choosing value from list")
    //    public static boolean checkIfInputCanBeFillOnlyByChooseValueFromList(By selector, String itemName) {
    //        return getElementAttribute(selector, "aria-autocomplete").contains("list");
    //    }
    //
    //    @Step("\"{itemName}\" is free text area")
    //    public static boolean checkIfInputIsFreeTextArea(By selector, String itemName) {
    //        return getElementAttribute(selector, "role").contains("textbox");
    //    }
    //
    //    @Step("Format of time in \"{itemName}\" is \"HH:mm\"")
    //    public static void checkTimeFormatInField(By selector, String itemName) {
    //        String timeFromField = getTextFieldValue(selector);
    //        StepLogger.info("Time in: " + itemName + " is: " + timeFromField);
    //        String hour    = timeFromField.substring(0, 2);
    //        String minutes = timeFromField.substring(3, 5);
    //        assertEquals("Format of time in: " + itemName + " is other than HH:mm", timeFromField, hour + ":" + minutes);
    //        StepLogger.info("Format of time is: HH:mm");
    //    }
    //
    //    @Step("Format of date in \"{itemName}\" is \"YYYY-MM-DD\"")
    //    public static void checkDateFormatInField(By selector, String itemName) {
    //        String dateFromField = getTextFieldValue(selector);
    //        StepLogger.info("Date in: " + itemName + " is: " + dateFromField);
    //        String year  = dateFromField.substring(0, 4);
    //        String month = dateFromField.substring(5, 7);
    //        String day   = dateFromField.substring(8, 10);
    //        assertEquals("Format of date in: " + itemName + " is other than YYYY-MM-DD", dateFromField,
    //                     year + "-" + month + "-" + day);
    //        StepLogger.info("Format of date is: YYYY-MM-DD");
    //    }
    //
    //    public static void checkIfObjectIsVisibleAndEnabledWithAssert(By selector, String itemName) {
    //        waitUntilClickable(selector);
    //        assertTrue("\"" + itemName + "\" object does not exist", isVisible(selector));
    //        assertTrue("\"" + itemName + "\" object is not enabled", isEnabled(selector));
    //        moveToElement(selector);
    //        StepLogger.info("\"" + itemName + "\" object does exist and is enabled");
    //    }
    //
    //    public static void checkIfObjectIsNotVisibleAndDisabledWithAssert(By selector, String itemName) {
    //        assertFalse("\"" + itemName + "\" object does exist", isVisible(selector));
    //        assertFalse("\"" + itemName + "\" object is enabled", isEnabled(selector));
    //        StepLogger.info("\"" + itemName + "\" object does not exist");
    //    }
    //
    //    public static void checkIfObjectIsNotVisibleWithAssert(By selector, String itemName) {
    //        assertFalse("\"" + itemName + "\" object does exist", isVisible(selector));
    //        StepLogger.info("\"" + itemName + "\" object does not exist");
    //    }
    //
    //    public static void checkIfObjectIsVisibleAndDisabledWithAssert(By selector, String itemName) {
    //        assertTrue("\"" + itemName + "\" object does not exist", isVisible(selector));
    //        assertFalse("\"" + itemName + "\" object is enabled", isEnabled(selector));
    //        moveToElement(selector);
    //        StepLogger.info("\"" + itemName + "\" object does exist and is disabled");
    //    }
    //
    //    /**
    //     * @author mdzienia Takes all entries which corresponds to selector and return innerText of one random selected
    //     * @author mkowalski Method nextInt generate number from 0 to boundry value (exclusive). nextInt(10) can generate
    //     * range 0-9
    //     */
    //    public static String getListRandomValue(By selector, String listName) {
    //        return getListRandomValue(selector, 0, listName);
    //    }
    //
    //    public static String getListRandomValueWithoutFirst(By selector, String listName) {
    //        return getListRandomValue(selector, 1, listName);
    //    }
    //
    //    private static String getListRandomValue(By selector, int lowerBoundary, String listName) {
    //        WebElement element = getRandomWebElement(selector, lowerBoundary, listName);
    //        return element == null ? null : getElementAttribute(element, "innerText");
    //    }
    //
    //    public static int getRandomInt(int lowerBoundary, int upperBoundary) {
    //        ThreadLocalRandom random = ThreadLocalRandom.current();
    //        return random.nextInt((upperBoundary - lowerBoundary) + 1) + lowerBoundary;
    //    }
    //
    //    /**
    //     * @author patmaj Takes all entries which corresponds to selector and return innerText of one containing given
    //     * string
    //     */
    //    public static String getListValueByPartialText(By selector, String itemName) {
    //        List<WebElement> listOfElements = findElementDynamics(selector);
    //        for (WebElement element : listOfElements) {
    //            if (element.getText().contains(itemName)) {
    //                return element.getText();
    //            }
    //        }
    //        return null;
    //    }
    //
    //    /**
    //     * @author patmaj Selects an item from dropbox by a random index
    //     */
    //    @Step("Set checkbox \"{name}\" to state \"{state}\"")
    //    public static void setRadioButtonState(By selector, boolean state, String name) {
    //        if (!getDriver().findElementDynamic(selector).isSelected() == state) {
    //            getDriver().findElementDynamic(selector).click();
    //        }
    //    }
    //
    //    private static boolean getCheckboxState(By selector, String name) {
    //        List<WebElement> elements = PerformAction.findElementDynamics(selector);
    //        assertFalse("Can't find element " + name, elements.isEmpty());
    //        assertEquals("Too many elements " + name, 1, elements.size());
    //        WebElement checkbox  = elements.get(0);
    //        boolean    isEnabled = getElementClassAttribute(checkbox).contains("ui-state-active");
    //        if (!isEnabled) {
    //            try {
    //                WebElement subElement = checkbox.findElement(By.cssSelector(".ui-state-active"));
    //                isEnabled = PerformAction.isVisible(subElement);
    //            }
    //            catch (Throwable ignored) {
    //            }
    //        }
    //        return isEnabled;
    //    }
    //
    //    @Step("Set checkbox \"{name}\" to state \"{state}\"")
    //    public static boolean setCheckboxState(By selector, boolean state, String name) {
    //        boolean isEnabled = getCheckboxState(selector, name);
    //        if (state && isEnabled) {
    //            StepLogger.info("Checkbox is already set to true");
    //            return false;
    //        }
    //        if (!state && !isEnabled) {
    //            StepLogger.info("Checkbox is already set to false");
    //            return false;
    //        }
    //        int limit   = 5;
    //        int counter = 0;
    //        while (state != isEnabled && counter < limit) {
    //            counter++;
    //            clickElement(selector, name);
    //            waitForLoadingToEndAssert();
    //            StepLogger.makeScreenShot();
    //            isEnabled = getCheckboxState(selector, name);
    //        }
    //        assertEquals("Checkbox not set to expected state", state, isEnabled);
    //        StepLogger.info("Checkbox set to state: " + state);
    //        return true;
    //    }
    //
    //    public static void clickAndHoldOnItem(By selector) {
    //        clickStepless(selector);
    //        WebElement element = getDriver().findElementDynamic(selector);
    //        getAction().moveToElement(element).clickAndHold().perform();
    //    }
    //
    //    public static void releaseClickOnItem(By selector) {
    //        clickStepless(selector);
    //        WebElement element = getDriver().findElementDynamic(selector);
    //        getAction().moveToElement(element).release().perform();
    //    }
    //
    //    public static String getElementText(By selector) {
    //        int counter = 0;
    //        while (counter < 5) {
    //            try {
    //                return getDriver().findElementDynamic(selector).getText();
    //            }
    //            catch (Throwable ignored) {
    //            }
    //            waitMilliseconds(TIMESLOTS.WAIT_SHORT_MOMENT_MS);
    //            counter++;
    //        }
    //        fail("Can't get text from element: " + selector);
    //        return null;
    //    }
    //
    //    public static String getElementClassAttribute(By selector) {
    //        return getElementAttribute(selector, "class");
    //    }
    //
    //    public static String getElementClassAttribute(WebElement element) {
    //        return getElementAttribute(element, "class");
    //    }
    //
    //    public static String getElementAttribute(By selector, String attribute) {
    //        int counter = 0;
    //        while (counter < 5) {
    //            try {
    //                return getElementAttribute(getDriver().findElementDynamic(selector), attribute);
    //            }
    //            catch (Throwable ignored) {
    //            }
    //            waitMilliseconds(TIMESLOTS.WAIT_SHORT_MOMENT_MS);
    //            counter++;
    //        }
    //        fail("Can't get attribute [" + attribute + "] from selector: " + selector);
    //        return null;
    //    }
    //
    //    public static String getElementAttribute(WebElement element, String attribute) {
    //        int counter = 0;
    //        while (counter < 5) {
    //            try {
    //                return element.getAttribute(attribute);
    //            }
    //            catch (Throwable ignored) {
    //            }
    //            waitMilliseconds(TIMESLOTS.WAIT_SHORT_MOMENT_MS);
    //            counter++;
    //        }
    //        fail("Can't get attribute [" + attribute + "] from element");
    //        return null;
    //    }
    //
    //    public static String getElementCSSValue(By selector, String propertyName) {
    //        int counter = 0;
    //        while (counter < 5) {
    //            try {
    //                return getDriver().findElementDynamic(selector).getCssValue(propertyName);
    //            }
    //            catch (Throwable ignored) {
    //            }
    //            waitMilliseconds(TIMESLOTS.WAIT_SHORT_MOMENT_MS);
    //            counter++;
    //        }
    //        fail("Can't get CSS value [" + propertyName + "] from element: " + selector);
    //        return null;
    //    }
    //
    //    public static void waitForLoadingToEndAssert() {
    //        waitForLoadingToEndAssert(selectorLoadingPopup);
    //    }
    //
    //    private static void waitForLoadingToEndAssert(By selector) {
    //        long    timeBegin = System.currentTimeMillis();
    //        long    timeCounter;
    //        boolean visible;
    //        do {
    //            waitForPageLoaded();
    //            visible     = PerformAction.isVisible(selector);
    //            timeCounter = System.currentTimeMillis() - timeBegin;
    //        }
    //        while (timeCounter < TIMESLOTS.WAIT_FOR_LOADING_BUBBLE_MS && visible);
    //        String time = DateTime.getTimeFromMilliseconds(timeCounter);
    //        assertFalse(MessageFormat.format("Loading is still visible after more than {0}", time), visible);
    //        BFLogger.logInfo(MessageFormat.format("Loading Time: {0}", time));
    //    }
    //
    //    private static void waitUntilOpacityIsOver(By selector, int secondsMax) {
    //        String opacity = "0";
    //        for (int seconds = 0; seconds < secondsMax; seconds++) {
    //            opacity = getDriver().findElementDynamic(selector).getCssValue("opacity");
    //            if (opacity.equals("1")) {
    //                break;
    //            }
    //            waitMilliseconds(TIMESLOTS.WAIT_SHORT_MOMENT_MS);
    //        }
    //        assertEquals("Element: " + selector + " did not disappear after " + secondsMax + "s", "1", opacity);
    //    }
    //
        public static boolean waitForElementVisibleNoAssertion(By selector) {
            return waitForElementVisibleNoAssertion(selector, 10000);
        }
    //
    //    public static boolean waitForElementEnabledNoAssertion(By selector) {
    //        return waitForElementEnabledNoAssertion(selector, TIMESLOTS.WAIT_LONG_MOMENT_MS);
    //    }
    //
        public static void waitForElementVisible(By selector, String elementName) {
            assertTrue(elementName + " element is not visible", waitForElementVisibleNoAssertion(selector));
        }
    //
    //    public static void waitForElementVisible(By selector, int milliseconds, String elementName) {
    //        assertTrue(elementName + " element is not visible", waitForElementVisibleNoAssertion(selector, milliseconds));
    //    }
    //
    //    public static void waitForElementEnabled(By selector, String elementName) {
    //        assertTrue(elementName + " element is not enabled", waitForElementEnabledNoAssertion(selector));
    //    }
    //
    //    public static void waitForElementEnabled(By selector, int milliseconds, String elementName) {
    //        assertTrue(elementName + " element is not enabled", waitForElementEnabledNoAssertion(selector, milliseconds));
    //    }
    //
        public static boolean waitForElementVisibleNoAssertion(By selector, int milliseconds) {
            long    timeBegin   = System.currentTimeMillis();
            long    timeCounter = 0;
            boolean visible     = false;
            while (timeCounter < milliseconds && !visible) {
                visible     = isVisible(selector);
                timeCounter = System.currentTimeMillis() - timeBegin;
            }
            return visible;
        }
    //
    //    public static boolean waitForElementEnabledNoAssertion(By selector, int milliseconds) {
    //        long    timeBegin   = System.currentTimeMillis();
    //        long    timeCounter = 0;
    //        boolean enabled     = false;
    //        while (timeCounter < milliseconds && !enabled) {
    //            enabled     = isEnabled(selector);
    //            timeCounter = System.currentTimeMillis() - timeBegin;
    //        }
    //        return enabled;
    //    }
    //
    //    public static boolean checkIfObjectsAttributeContainsStringNoAssert(By selector, String attribute, String attributeStatus) {
    //        WebElement element        = getDriver().findElementDynamic(selector);
    //        String     attributeValue = getElementAttribute(element, attribute);
    //        BFLogger.logInfo("ATTRIBUTE VAL: " + attributeValue);
    //        return attributeValue != null && attributeValue.contains(attributeStatus);
    //    }
    //
    //    public static WebElement getRowById(By tableRowsSelector, String tableName, int id) {
    //        getDriver().waitForElementVisible(tableRowsSelector);
    //        try {
    //            List<WebElement> tableList = findElementDynamics(tableRowsSelector);
    //            return tableList.get(id);
    //        }
    //        catch (Throwable ex) {
    //            BFLogger.logInfo("That row doesn't exist in " + tableName);
    //            fail("Row " + id + " doesn't exist in table " + tableName);
    //        }
    //        return null;
    //    }
    //
    //    public static String getObjectsCssValue(By selector, String cssValue) {
    //        return getDriver().findElementDynamic(selector).getCssValue(cssValue);
    //    }
    //
    //    public static void waitForPageLoaded() {
    //        waitMilliseconds(TIMESLOTS.WAIT_MOMENT_MS);
    //        try {
    //            getDriver().waitForPageLoaded();
    //        }
    //        //Catch connection timeouts
    //        catch (BFElementNotFoundException ignored) {}
    //    }
    //
    //    public static void refreshPage() {
    //        getDriver().navigate().refresh();
    //        waitForLoadingToEndAssert();
    //    }
    //
    //    public static void setFilter(By filterSelector, String inputValue, String fieldName) {
    //        waitUntilClickable(filterSelector);
    //        assertTrue(fieldName + " filter is not visible", isVisible(filterSelector));
    //        assertTrue(fieldName + " filter is not enabled", isEnabled(filterSelector));
    //        List<WebElement> elements = findElementDynamics(filterSelector);
    //        setFilter(elements.get(0), inputValue, fieldName);
    //    }
    //
    //    public static void setFilter(WebElement filterElement, String inputValue, String fieldName) {
    //        waitUntilClickable(filterElement);
    //        assertTrue(fieldName + " filter is not visible", isVisible(filterElement));
    //        assertTrue(fieldName + " filter is not enabled", isEnabled(filterElement));
    //        clearTextFieldStepless(filterElement);
    //        waitForLoadingToEndAssert();
    //        fillTextField(filterElement, inputValue, fieldName);
    //        fillTextFieldStepless(filterElement, Keys.RIGHT.toString());
    //        waitForLoadingToEndAssert();
    //    }
    //
    public static void waitMilliseconds(Integer milliseconds) {
        BFLogger.logDebug("[waitMilliseconds] " + milliseconds);
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException e) {
            assumeTrue(MessageFormat.format("WAIT: {0} thread interrupted", Thread.currentThread().getName()), false);
        }
    }

    //
    //    public static String openNewBrowserTab() {
    //        INewWebDriver driver  = getDriver();
    //        List<String>  oldTabs = getBrowserTabs();
    //        ((JavascriptExecutor) driver).executeScript("window.open('about:blank');");
    //        List<String> newTabs = getBrowserTabs();
    //        for (String tab : newTabs) {
    //            if (!oldTabs.contains(tab)) {
    //                driver.switchTo().window(tab);
    //                return tab;
    //            }
    //        }
    //        fail("New browser tab was not opened");
    //        return null;
    //    }
    //
    //    static void clickSortingButton(By selectorColumnSortButton, String columnName) {
    //        clickButton(selectorColumnSortButton, columnName + " Sort");
    //        waitForLoadingToEndAssert();
    //        StepLogger.makeScreenShot();
    //    }
    //
    //    static List<String> getStringListForColumn(By selectorColumnValues) {
    //        return getTextOfElements(selectorColumnValues);
    //    }
    //
    //    public static List<String> getTextOfElements(By selectorElements) {
    //        return getTextOfElements(selectorElements, false);
    //    }
    //
    //    public static List<String> getTextOfElements(By selectorElements, boolean trimText) {
    //        List<WebElement> elements = findElementDynamics(selectorElements);
    //        return getTextOfElements(elements, trimText);
    //    }
    //
    //    public static List<String> getTextOfElements(List<WebElement> webElements) {
    //        return getTextOfElements(webElements, false);
    //    }
    //
    //    public static List<String> getTextOfElements(List<WebElement> webElements, boolean trimText) {
    //        boolean repeat  = false;
    //        int     limit   = 10;
    //        int     counter = 0;
    //
    //        while (counter < limit) {
    //            List<String> textOfElements = new ArrayList<>();
    //            for (WebElement element : webElements) {
    //                try {
    //                    String elementText = element.getText();
    //                    if (trimText) {
    //                        textOfElements.add(elementText.trim());
    //                    }
    //                    else {
    //                        textOfElements.add(elementText);
    //                    }
    //                }
    //                catch (Exception ignored) {
    //                    repeat = true;
    //                }
    //            }
    //            if (!repeat) {
    //                return textOfElements;
    //            }
    //            counter++;
    //        }
    //        if (webElements.size() > 0) {
    //            fail("Can't get text of elements");
    //        }
    //        return new ArrayList<>();
    //    }
    //
    //    static void checkIfSortingIsPossible(By selectorColumnValues) {
    //        List<String> stringValueListBeforeSorting = getStringListForColumn(selectorColumnValues);
    //        assumeTrue("List of elements is too short for sorting", stringValueListBeforeSorting.size() >= 2);
    //    }
    //
    //    static boolean checkIfColumnHasTheSameValues(By selectorColumnValues) {
    //        List<String> stringValueList = getStringListForColumn(selectorColumnValues);
    //        return Collections.frequency(stringValueList, stringValueList.get(0)) == stringValueList.size();
    //    }
    //
    //    public static String getStringFromFirstRow() {
    //        String value = getElementText(selectorFirstTableRow);
    //        BFLogger.logDebug(value);
    //        return value;
    //    }
    //
    //    public static void selectAllAndDeleteFromTextField(By selectorTextField) {
    //        WebElement element = getDriver().findElementDynamic(selectorTextField);
    //        moveToElement(element);
    //        clickStepless(element);
    //        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
    //        element.sendKeys(Keys.BACK_SPACE);
    //        tryTriggerEventsOnInput(element);
    //    }
    //
    //    public static List<WebElement> findElementDynamics(By selector, int timeOutSeconds) {
    //        List<WebElement> result = new ArrayList<>();
    //        try {
    //            result = getDriver().findElementDynamics(selector, timeOutSeconds);
    //        }
    //        catch (Throwable ignored) {
    //        }
    //        return result;
    //    }
    //
    //    public static List<WebElement> findElementDynamics(By selector) {
    //        List<WebElement> result = new ArrayList<>();
    //        try {
    //            result = getDriver().findElementDynamics(selector);
    //        }
    //        catch (Throwable ignored) {
    //        }
    //        return result;
    //    }

    //
    //    public static WebElement getElementByTextFromList(By selector, String searchedText) {
    //        List<WebElement> list = findElementDynamics(selector);
    //        for (WebElement element : list) {
    //            if (element.getText().contains(searchedText)) {
    //                return element;
    //            }
    //        }
    //        fail("Didn't find element with text \"" + searchedText + "\"");
    //        return null;
    //    }
    //
    //    @Step("Show 100 rows per table page")
    //    public static void show100RowsPerPage() {
    //        if (isVisible(selectorShow100Rows)) {
    //            clickButton(selectorShow100Rows, "Show 100 rows per page");
    //            waitForLoadingToEndAssert();
    //        }
    //    }
    //
    //    @Step("Show {numberOfRowsToSelect} rows per table page")
    //    public static void showXRowsPerPage(int numberOfRowsToSelect) {
    //        if (isVisible(By.cssSelector("select[id] option[value='" + numberOfRowsToSelect + "']"))) {
    //            clickButton(By.cssSelector("select[id] option[value='" + numberOfRowsToSelect + "']"),
    //                        "Show " + numberOfRowsToSelect + " rows per page");
    //            waitForLoadingToEndAssert();
    //        }
    //    }
    //
    //    public static String fillFileInputDialogSteplessInstant(By selector, String filePath) {
    //        WebElement element = getDriver().findElementDynamic(selector);
    //        if (System.getProperty("seleniumGrid") != null) {
    //            BFLogger.logDebug("[REMOTE] GRID FILE UPLOAD START");
    //            LocalFileDetector detector = new LocalFileDetector();
    //            ((RemoteWebDriver) getDriver()).setFileDetector(detector);
    //            File uploadedFile = new File(filePath);
    //            element.sendKeys(uploadedFile.getAbsolutePath());
    //            BFLogger.logDebug("[REMOTE] GRID FILE UPLOAD END");
    //        }
    //        else {
    //            BFLogger.logDebug("[LOCAL] NO GRID RUN");
    //            BFLogger.logDebug("[LOCAL] FILE UPLOAD START");
    //            fillTextFieldSteplessInstant(selector, filePath);
    //            BFLogger.logDebug("[LOCAL] FILE UPLOAD END");
    //        }
    //        return getTextFieldValue(selector);
    //    }
    //
    //    public static WebElement getRandomWebElement(By selector, int lowerBoundry, String name) {
    //        List<WebElement> listOfElements = findElementDynamics(selector);
    //        return getRandomElement(listOfElements, lowerBoundry, name);
    //    }
    //
    //    public static <T> T getRandomElement(List<T> list, String name) {
    //        return getRandomElement(list, 0, list.size() - 1, name);
    //    }
    //
    //    public static <T> T getRandomElement(List<T> list, int lowerBoundry, String name) {
    //        return getRandomElement(list, lowerBoundry, list.size() - 1, name);
    //    }
    //
    //    public static <T> T getRandomElement(List<T> list, int lowerBoundry, int upperBoundry, String name) {
    //        int size = list.size();
    //        if (size < (1 + lowerBoundry)) {
    //            fail("Can't select random " + name + " - there are no such elements");
    //        }
    //        if (size - 1 > upperBoundry) {
    //            fail("Can't select random " + name + " - there are no such elements");
    //        }
    //        int randNummer = getRandomInt(lowerBoundry, upperBoundry);
    //        return list.get(randNummer);
    //    }
    //
    //    public static WebElement getRandomWebElement(By selector, String name) {
    //        return getRandomWebElement(selector, 0, name);
    //    }
    //
    //    public static String getLabelValueFromWebElement(WebElement webElement) {
    //        return getElementAttribute(webElement, "data-label");
    //    }
    //
    //    public static boolean isClickable(By selector) {
    //        return isVisible(selector) && isEnabled(selector);
    //    }
    //
    //    public static int getCountOfCoockies() {
    //
    //        Set<Cookie> cookies = getDriver().manage().getCookies();
    //
    //        StepLogger.info("Found " + cookies.size());
    //
    //        for (Cookie cookie : cookies) {
    //            StepLogger.step("[COOKIE] " + cookie.toString());
    //        }
    //        return cookies.size();
    //    }
    //
    //    @Step("Type value in DropDown \"{valueToType}\"")
    //    public static void selectDropDownByText(By selectorDropBox, By selectorDropBoxInput, By selectorDropBoxResults, String valueToType) {
    //        assertEquals("There are more than one dropdown with this selector: " + selectorDropBox, 1,
    //                     findElementDynamics(selectorDropBox).size());
    //        PerformAction.clickElement(selectorDropBox, "Dropdown");
    //        waitForElementVisible(selectorDropBoxInput, "Dropdown Input");
    //        List<WebElement> inputs = findElementDynamics(selectorDropBoxInput);
    //        assertEquals("There are more than one dropdown inputs with this selector: " + selectorDropBoxInput, 1,
    //                     inputs.size());
    //        WebElement input = inputs.get(0);
    //        PerformAction.fillTextField(input, valueToType, "Dropdown Input");
    //        List<WebElement> results = findElementDynamics(selectorDropBoxResults);
    //        assertFalse("There is no result on dropdown", results.isEmpty());
    //        if (results.size() == 1) {
    //            PerformAction.fillTextField(input, Keys.ENTER.toString(), "Dropdown Input");
    //        }
    //        else {
    //            for (WebElement result : results) {
    //                if (result.getText().equals(valueToType)) {
    //                    clickElement(result, valueToType);
    //                    break;
    //                }
    //            }
    //        }
    //        waitUntilElementDisappear(selectorDropBoxInput, "Dropdown Input");
    //    }
    //
    //    @Step("Select position {index} in DropDown")
    //    public static void selectDropDownByIndex(By selectorDropBox, By selectorDropBoxOptions, int index) {
    //
    //        getDriver().findElementDynamic(selectorDropBox).click();
    //
    //        waitForElementVisible(selectorDropBoxOptions, "Options");
    //
    //        getDriver().findElementDynamics(selectorDropBoxOptions).get(index).click();
    //
    //        waitMilliseconds(1000);
    //    }
    //
    //    public static void checkIfLabelHaveText(By element, String labelName, String expectedText) {
    //        checkIfElementHaveText(element, "label " + labelName, expectedText);
    //    }
    //
    //    public static void checkIfElementHaveText(By element, String elementName, String expectedText) {
    //        assertEquals("Incorrect text on " + elementName, expectedText, getElementText(element));
    //        StepLogger.info(elementName + "\" has expected text: " + expectedText);
    //    }
    //
    //    @Step("Fill text field \"{fieldName}\" with \"{inputValue}\"")
    //    public static String fillTextFieldWithWaitings(By selector, String inputValue, String fieldName) {
    //        int attempts = 0;
    //        while (attempts < 5) {
    //            try {
    //                WebElement element = getDriver().findElementDynamic(selector);
    //                waitUntilClickable(element);
    //                for (int i = 0; i < inputValue.length(); i++) {
    //                    char c = inputValue.charAt(i);
    //                    element.sendKeys(Character.toString(c));
    //                    waitForLoadingToEndAssert();
    //                    waitMilliseconds(TIMESLOTS.WAIT_TYPING_DELAY_MS);
    //                }
    //                // try trigger events
    //                tryTriggerEventsOnInput(element);
    //                return getTextFieldValue(selector);
    //            }
    //            catch (Throwable e) {
    //                ErrorPage.check();
    //                clearTextFieldStepless(selector);
    //                waitMilliseconds(TIMESLOTS.WAIT_INSTANT_MOMENT_MS);
    //            }
    //            attempts++;
    //        }
    //
    //        fail("Can't fill text field. Selector: " + selector);
    //        return null;
    //    }
    //
    //    public static void isVisibleAssert(By selector, String itemName) {
    //        assertTrue("\"" + itemName + "\" is not visible", isVisible(selector));
    //        StepLogger.info("\"" + itemName + "\" is visible");
    //    }
    //
    //    public static void isVisibleAssert(WebElement element, String itemName) {
    //        assertTrue("\"" + itemName + "\" is not visible", isVisible(element));
    //        StepLogger.info("\"" + itemName + "\" is visible");
    //    }
    //
    //    @Step("Check Tooltip text: {expectedText}")
    //    public static void checkIfTooltipParentHasText(By element, String tooltipName, String expectedText) {
    //        WebElement tooltip = getDriver().findElementDynamic(element);
    //        WebElement parent  = tooltip.findElement(By.xpath(".."));
    //        assertEquals("Incorrect tooltip text", expectedText, getElementAttribute(parent, "innerText"));
    //        StepLogger.info("Tooltip \"" + tooltipName + "\" has expected text: " + expectedText);
    //    }
    //
    //    public static WebElement getParentFromWebElement(By childElement) {
    //        WebElement element = getDriver().findElementDynamic(childElement);
    //        return element.findElement(By.xpath(".."));
    //    }
    //
    //    public static List<String> getStringListFromWebelementList(List<WebElement> webelementTable) {
    //        List<String> strings = new ArrayList<>();
    //        for (WebElement item : webelementTable) {
    //            strings.add(item.getText());
    //        }
    //        return strings;
    //    }
    //
    //    @Step("Verify if header: {headerName} is visible and correct")
    //    public static void verifyIfHeaderIsVisibleAndCorrect(By selector, String headerName, String expectedHeaderText) {
    //        isVisibleAssert(selector, headerName);
    //        assertEquals("Text in header isn't correct", getElementText(selector), expectedHeaderText);
    //        StepLogger.info("Header " + headerName + " has expected text: " + expectedHeaderText);
    //    }
    //
    //    public static String getPseudoElementContent(WebElement htmlElement, String pseudoElement) {
    //        String             script = "return window.getComputedStyle(arguments[0],'" + pseudoElement + "').getPropertyValue('content')";
    //        JavascriptExecutor js     = (JavascriptExecutor) getDriver();
    //        return (String) js.executeScript(script, htmlElement);
    //    }
    //
    //    public static String getPseudoElementContent(By selector, String pseudoElement) {
    //        List<WebElement> htmlElements = findElementDynamics(selector);
    //        if (htmlElements.size() < 1) {
    //            fail("Element not found. Selector " + selector);
    //        }
    //        return getPseudoElementContent(htmlElements.get(0), pseudoElement);
    //    }
    //

    //    public static String switchToBrowserTab(int tabIndex, List<String> tabs) {
    //        if (tabs.size() > 1) {
    //            StepLogger.info("Number of tabs in browser: " + tabs.size());
    //        }
    //        assertTrue("There is no tab with index " + tabIndex + " in browser. There are " + tabs.size() + " tabs",
    //                   tabs.size() > tabIndex);
    //        String windowHandle = tabs.get(tabIndex);
    //        return switchToBrowserTab(windowHandle, tabs);
    //    }
    //
    //    public static String switchToBrowserTab(int tabIndex) {
    //        List<String> tabs = getBrowserTabs();
    //        return switchToBrowserTab(tabIndex, tabs);
    //    }
    //
    //    public static String switchToBrowserTab(String windowHandle) {
    //        List<String> tabs = getBrowserTabs();
    //        return switchToBrowserTab(windowHandle, tabs);
    //    }
    //
    //    public static String switchToBrowserTab(String windowHandle, List<String> tabs) {
    //        assertTrue("There is no tab with handle '" + windowHandle + "' in browser. There are: " + tabs,
    //                   tabs.contains(windowHandle));
    //        getDriver().switchTo().window(windowHandle);
    //        return windowHandle;
    //    }
    //
    //    public static List<String> getBrowserTabs() {
    //        return new ArrayList<>(getDriver().getWindowHandles());
    //    }
    //
    //    public static String splitCamelCase(String s) {
    //        return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
    //                                          "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
    //    }
    //
    //    public static void verifyLink(By selectorLink, String linkName, PageSubURLsEnum linkHref) {
    //        verifyLink(selectorLink, linkName, linkHref.getValue());
    //    }
    //
    //    @Step("Verify {linkName} link")
    //    public static void verifyLink(By selectorLink, String linkName, String linkHref) {
    //        List<WebElement> links = findElementDynamics(selectorLink);
    //        assertTrue(linkName + " link not found. Selector: " + selectorLink, links.size() > 0);
    //        assertEquals(linkName + " link found more than once. Selector: " + selectorLink, 1, links.size());
    //        WebElement link = links.get(0);
    //        String     href = getElementAttribute(link, "href");
    //        assertNotNull(linkName + " link href attribute is null", href);
    //        assertTrue(linkName + " link href attribute [" + href + "] doesn't contain checked part [" + linkHref + "]",
    //                   href.contains(linkHref));
    //        StepLogger.info(linkName + " link href attribute is correct [" + href + "]");
    //    }
    //
    //    @Step("Check if all requested elements are visible")
    //    public static void checkIfElementsAreVisibleOnPageAssert(List<String> elementsOnWebsiteToCheck, List<String> providedElementsToCheck) {
    //
    //        boolean       allVisible         = true;
    //        int           notVisibleCounter  = 0;
    //        StringBuilder notVisibleListText = new StringBuilder();
    //
    //        for (String elementToCheck : providedElementsToCheck) {
    //            boolean visible = elementsOnWebsiteToCheck.contains(elementToCheck);
    //            if (visible) {
    //                StepLogger.step(MessageFormat.format("Element \"{0}\" is visible", elementToCheck));
    //            }
    //            else {
    //                String message = MessageFormat.format("Element \"{0}\" is not visible", elementToCheck);
    //                notVisibleListText.append(MessageFormat.format("\n{0}", message));
    //                StepLogger.error(message);
    //                allVisible = false;
    //                notVisibleCounter++;
    //            }
    //        }
    //
    //        assertTrue(MessageFormat.format("{0} elements of {1} are not visible: {2}", notVisibleCounter,
    //                                        providedElementsToCheck.size(), notVisibleListText.toString()), allVisible);
    //        StepLogger.step(MessageFormat.format("All {0} elements are visible", providedElementsToCheck.size()));
    //        StepLogger.makeScreenShot();
    //    }
    //
    //    @Step("Check if all requested elements are not visible")
    //    public static void checkIfElementsAreNotVisibleOnPageAssert(List<String> elementsOnWebsiteToCheck, List<String> providedElementsToCheck) {
    //
    //        boolean       allNotVisible   = true;
    //        int           visibleCounter  = 0;
    //        StringBuilder visibleListText = new StringBuilder();
    //
    //        for (String elementToCheck : providedElementsToCheck) {
    //            boolean visible = elementsOnWebsiteToCheck.contains(elementToCheck);
    //            if (!visible) {
    //                StepLogger.step(MessageFormat.format("Element \"{0}\" is not visible", elementToCheck));
    //            }
    //            else {
    //                String message = MessageFormat.format("Element \"{0}\" is visible", elementToCheck);
    //                visibleListText.append(MessageFormat.format("\n{0}", message));
    //                StepLogger.error(message);
    //                allNotVisible = false;
    //                visibleCounter++;
    //            }
    //        }
    //
    //        assertTrue(MessageFormat.format("{0} elements of {1} are visible: {2}", visibleCounter,
    //                                        providedElementsToCheck.size(), visibleListText.toString()), allNotVisible);
    //        StepLogger.step(MessageFormat.format("All {0} elements are not visible", providedElementsToCheck.size()));
    //        StepLogger.makeScreenShot();
    //    }
    //
    //    public static String removeDiacriticalMarks(String string) {
    //        return Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    //    }
    //
    //    public static void checkNoDuplicatesInList(List<String> list, String listName) {
    //        List<String> duplicates = list.stream().filter(e -> Collections.frequency(list, e) > 1).distinct()
    //                                      .collect(Collectors.toList());
    //        if (duplicates.isEmpty()) {
    //            return;
    //        }
    //        fail(MessageFormat.format("There are duplicated values on {0} list: {1}", listName, duplicates));
    //    }
    //
    //    public static List<String> removeDuplicatesFromList(List<String> list) {
    //        List<String> result = new ArrayList<>(list);
    //        List<String> duplicates = list.stream().filter(e -> Collections.frequency(list, e) > 1).distinct()
    //                                      .collect(Collectors.toList());
    //        for (String duplicate : duplicates) {
    //            while (result.contains(duplicate)) {
    //                result.remove(duplicate);
    //            }
    //        }
    //        return result;
    //    }
    //
    //    public static void checkIfNothingIsLoading() {
    //        assertFalse("Popup loading is visible", PerformAction
    //                .waitForElementVisibleNoAssertion(PerformAction.selectorLoadingPopup,
    //                                                  TIMESLOTS.WAIT_INSTANT_MOMENT_MS));
    //    }
    //
    //    public static void switchToFrame(By selectorIFrame) {
    //        assumeTrue("Frame is not visible", isVisible(selectorIFrame));
    //        List<WebElement> frames = findElementDynamics(selectorIFrame);
    //        assertEquals("Incorrect amount of frames with this selector: " + selectorIFrame, 1, frames.size());
    //        getDriver().switchTo().frame(frames.get(0));
    //    }
    //
    //    public static void switchToDefaultContent() {
    //        getDriver().switchTo().defaultContent();
    //    }
    //
    //    public static boolean checkIfFrameHasContent(By selectorIFrame) {
    //        String currentBrowser = System.getProperty("browser");
    //        PerformAction.switchToFrame(selectorIFrame);
    //        String bodyText = PerformAction.getElementText(By.cssSelector("body"));
    //        By pdfSelector = currentBrowser.equals("firefox") ? By.cssSelector("div[id='viewer'][class='pdfViewer']") : By
    //                .cssSelector("embed[type='application/pdf']");
    //        PerformAction.waitForElementVisibleNoAssertion(pdfSelector, TIMESLOTS.WAIT_VERY_LONG_MOMENT_MS);
    //        boolean pdfVisible = PerformAction.isVisible(pdfSelector);
    //        if (pdfVisible) {
    //            PerformAction.switchToDefaultContent();
    //            return true;
    //        }
    //        if (bodyText.isEmpty()) {
    //            return false;
    //        }
    //        PerformAction.switchToDefaultContent();
    //        return true;
    //    }
    //
    //    public static void checkIfDescriptionFrameHasContent(By selectorIFrame) {
    //        assertTrue("Description is empty", checkIfFrameHasContent(selectorIFrame));
    //    }
    //
    //    public static void acceptAllTermsPrivacyPages() {
    //        LoginPage loginPage = new LoginPage();
    //        loginPage.acceptAllTermsPrivacyPages();
    //    }
    //
    //    public static boolean changeCheckboxStateAndCheckElementsState(By checkboxSelector, boolean state, String checkboxName, By activatedSelector, By deactivatedSelector, String elementName) {
    //        boolean stateChanged = PerformAction.setCheckboxState(checkboxSelector, state, checkboxName);
    //        if (stateChanged) {
    //            PerformAction.waitForLoadingToEndAssert();
    //            //Checkbox selected - all moved automatic to activated
    //            //Checkbox unselected - all moved automatic to deactivated
    //            //Changes here may impact test cases logic!
    //            if (!state) {
    //                //All elements moved to deactivated section with enabled state (check visibility and enabled)
    //                PerformAction.waitForElementVisible(deactivatedSelector, TIMESLOTS.WAIT_FOR_LAGGY_PAGE_MS,
    //                                                    "Deactivated " + elementName);
    //                PerformAction.waitForElementEnabled(deactivatedSelector, TIMESLOTS.WAIT_FOR_LAGGY_PAGE_MS,
    //                                                    "Deactivated " + elementName);
    //                PerformAction.waitUntilElementDisappear(activatedSelector, TIMESLOTS.WAIT_FOR_LAGGY_PAGE_MS,
    //                                                        "Activated " + elementName);
    //            }
    //            else {
    //                //All elements moved to activated section but with disabled state (check only visibility)
    //                PerformAction.waitForElementVisible(activatedSelector, TIMESLOTS.WAIT_FOR_LAGGY_PAGE_MS,
    //                                                    "Activated " + elementName);
    //                PerformAction.waitUntilElementDisappear(deactivatedSelector, TIMESLOTS.WAIT_FOR_LAGGY_PAGE_MS,
    //                                                        "Deactivated " + elementName);
    //            }
    //        }
    //        return stateChanged;
    //    }
}