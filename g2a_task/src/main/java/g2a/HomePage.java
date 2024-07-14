package g2a;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.stk.framework.basetest.StepLogger;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import javax.swing.*;

public class HomePage extends BasePage {
    private final String url = "https://www.g2a.com";
    private final By selectorSearchInput = By.cssSelector("input[type='search']");
    private final By selectorSearchButton = By.cssSelector("svg[class='search_icon']");
    private final By selectorSearchedResultRow = By.cssSelector("li[data-url^='/pl/search']");
    private final By selectorCartButton = By.cssSelector("a[href$='/cart']");

    @Override
    @Step("Open home page " + url)
    public void load() {
        getDriver().get(url);
        getDriver().waitForElementVisible(selectorSearchButton);
    }

    @Override
    public String pageTitle() {
        return null;
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Step("Search the product: {text}")
    public void searchTheProduct(String text) {
        fillSearchInput(text);
        waitForSearchedResultsListIsVisible();
        clickSearchButton();
    }

    @Step("Search the product - enter value")
    public String searchTheProductEnterValue() {
        String productName = setTheProductName();
        waitForSearchedResultsListIsVisible();
        clickSearchButton();
        return productName;
    }

    public String setTheProductName() {
        String input = JOptionPane.showInputDialog("Enter product name: ");
        StepLogger.info("Entered product name: " + input + "\"");
        getDriver().findElementDynamic(selectorSearchInput).sendKeys(input);
        return input;
    }

    public void fillSearchInput(String text) {
        getDriver().findElementDynamic(selectorSearchInput).sendKeys(text);
    }

    public void clickSearchButton() {
        getDriver().findElementDynamic(selectorSearchButton).click();
    }

    public void waitForSearchedResultsListIsVisible() {
        getDriver().waitForElementVisible(selectorSearchedResultRow);
    }

    public void clickCartButton(){
        getDriver().findElementDynamic(selectorCartButton).click();
    }
}
