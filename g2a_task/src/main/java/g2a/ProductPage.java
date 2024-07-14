package g2a;

import com.capgemini.mrchecker.selenium.core.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ProductPage extends BasePage {
    private By selectorDefaultPriceSpan = By.cssSelector("[data-locator='ppa-payment'] span[class^='Radiostyles__Description'] span[data-locator='zth-price']");
    private By selectorSubscribedPriceSpan = By.cssSelector("[data-locator='ppa-payment-plus'] span[class^='Radiostyles__Description'] span[data-locator='zth-price']");
    private By selectorAddToCartButton = By.cssSelector("button[data-locator='ppa-payment__btn']");

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

    @Step("Click add to cart button")
    public void clickAddToCartButton() {
        getDriver().findElementDynamic(selectorAddToCartButton).click();
    }

    @Step("Save product price")
    public double selectPrice(String priceType) {
        double price;
        switch (priceType) {
            case "default":
                WebElement defaultPrice = getDriver().findElementDynamic(selectorDefaultPriceSpan);
                defaultPrice.click();
                price = Double.parseDouble(defaultPrice.getText().split("PLN")[0]);
                return price;
            case "subscribed":
                WebElement subscribedPrice = getDriver().findElementDynamic(selectorSubscribedPriceSpan);
                subscribedPrice.click();
                price = Double.parseDouble(subscribedPrice.getText().split("PLN")[0]);
                return price;
            default:
                return Double.parseDouble(getDriver().findElementDynamic(selectorDefaultPriceSpan).getText().split("PLN")[0]);
        }

    }
}
