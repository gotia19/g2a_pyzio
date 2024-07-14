package g2a.gherkin;

import com.capgemini.stk.framework.basetest.StepLogger;
import g2a.CartPage;
import g2a.HomePage;
import g2a.ProductPage;
import g2a.SearchPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;

public class G2AStepDefinitions {
    HomePage homePage = new HomePage();
    SearchPage searchPage = new SearchPage();
    ProductPage productPage = new ProductPage();
    CartPage cartPage = new CartPage();
    double selectedProductPriceOnProductPage;
    String priceTypeDefault = "default";
    double productPriceInCart;
    int indexOfProductInSearch = 0;
    int indexOfProductInCart = 0;

    @Given("I am on the homepage")
    public void iAmOnTheHomepage() {
        homePage.load();
    }

    @When("I search for the product {string}")
    public void iSearchForTheProduct(String productName) {
        homePage.searchTheProduct(productName); //Fails in this place. Search list can't be loaded on UI during run test
        searchPage.verifySearchedProductName(productName, indexOfProductInSearch);
    }

    @Then("I save the price of the product")
    public void iSaveThePriceOfTheProduct() {
        searchPage.clickOnProductNameHref(indexOfProductInSearch);
        selectedProductPriceOnProductPage = productPage.selectPrice(priceTypeDefault);
        StepLogger.info("Selected price on product page is: " + selectedProductPriceOnProductPage);
    }

    @When("I add the product to the cart")
    public void iAddTheProductToTheCart() {
        productPage.clickAddToCartButton();
        homePage.clickCartButton();
        productPriceInCart = cartPage.getProductPriceInCart(indexOfProductInCart);
        StepLogger.info("Price in a cart is: " + productPriceInCart);
    }

    @Then("I verify the product price in the cart")
    public void iVerifyTheProductPriceInTheCart() {
        assertEquals("Selected price on product page is not equals to price in a cart", selectedProductPriceOnProductPage, productPriceInCart);
        StepLogger.info("Selected price on product page and price in a cart are equals: " + selectedProductPriceOnProductPage);
    }
}
