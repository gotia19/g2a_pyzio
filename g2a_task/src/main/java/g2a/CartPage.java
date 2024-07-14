package g2a;

import com.capgemini.mrchecker.selenium.core.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class CartPage extends BasePage {
    private By selectorProductPriceSpan = By.cssSelector("[class*='indexes__RightColumn2'] span[data-locator='zth-price']");

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

    @Step("Get product price in cart")
    public double getProductPriceInCart(int index) {
        String priceAsString = getDriver().findElementDynamics(selectorProductPriceSpan).get(index).getText().split("PLN")[0];
        double price = Double.parseDouble(priceAsString);
        return price;
    }
}
