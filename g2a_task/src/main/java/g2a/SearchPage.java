package g2a;

import com.capgemini.mrchecker.selenium.core.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SearchPage extends BasePage {
    private By selectorResultRow = By.cssSelector("li[class$='productCard']");
    private By selectorProductNameHref = By.cssSelector("li[class$='productCard'] a[title]");
    private By selectorProductPrice = By.cssSelector("li[class$='productCard'] span[data-locator$='zth-price']");

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

    public List<WebElement> getResultsList() {
        return getDriver().findElementDynamics(selectorResultRow);
    }

    public String getProductNameTextFromResults(int index) {
        return getResultsList().get(index).getText();
    }

    public void verifySearchedProductName(String expectedProductName, int index) {
        assertEquals("Product name on index: " +  index + ", isn't: "  + expectedProductName, expectedProductName, getProductNameTextFromResults(index));
    }

    @Step("Save price from search results")
    public double getPriceFromSearchResults(int index) {
        String priceAsString = getDriver().findElementDynamics(selectorProductPrice).get(index).getText().split("PLN")[0];
        double price = Double.parseDouble(priceAsString);
        return price;
    }

    public void clickOnProductNameHref(int index){
        getDriver().findElementDynamics(selectorProductNameHref).get(index).click();
    }
}
