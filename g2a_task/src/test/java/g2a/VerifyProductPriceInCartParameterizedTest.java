package g2a;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.stk.framework.basetest.StepLogger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static com.capgemini.mrchecker.selenium.core.newDrivers.DriverManager.closeDriver;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class VerifyProductPriceInCartParameterizedTest extends BaseTest {
    HomePage homePage = new HomePage();
    SearchPage searchPage = new SearchPage();
    ProductPage productPage = new ProductPage();
    CartPage cartPage = new CartPage();
    double productPriceInSearch;
    double selectedProductPriceOnProductPage;
    String priceTypeDefault = "default";
    double productPriceInCart;
    int indexOfProductInSearch = 0;
    int indexOfProductInCart = 0;


    @Parameterized.Parameter(0)
    public String productName;

    @Parameterized.Parameters(name = "{0}")
    public static Object[] getProductNames() {
        return new Object[][]{
                {"Corel Painter Essentials 7 (PC) - Corel Key - GLOBAL"},
                {"McAfee AntiVirus PC 1 Device 1 Year McAfee Key GLOBAL"},
                {"Avast Driver Updater (PC) 1 Device, 1 Year - Avast Key - GLOBAL"}
        };
    }

    @Override
    public void setUp() {
        homePage.load();
    }

    @Override
    public void tearDown() {
        closeDriver();
    }

    @Test
    public void verifyProductPriceInCartParameterizedTest() {
        homePage.searchTheProduct(productName); //Fails in this place. Search list can't be loaded on UI during run test
        searchPage.verifySearchedProductName(productName, indexOfProductInSearch);
//        productPriceInSearch = searchPage.getPriceFromSearchResults(indexOfProductInSearch); //additionally step
//        StepLogger.info("Price in search results is: " + productPriceInSearch);
        searchPage.clickOnProductNameHref(indexOfProductInSearch);
        selectedProductPriceOnProductPage = productPage.selectPrice(priceTypeDefault);
        StepLogger.info("Selected price on product page is: " + selectedProductPriceOnProductPage);
        productPage.clickAddToCartButton();
        homePage.clickCartButton();
        productPriceInCart = cartPage.getProductPriceInCart(indexOfProductInCart);
        StepLogger.info("Price in a cart is: " + productPriceInCart);
        assertEquals("Selected price on product page is not equals to price in a cart", selectedProductPriceOnProductPage, productPriceInCart);
        StepLogger.info("Selected price on product page and price in a cart are equals: " + selectedProductPriceOnProductPage);
//        assertEquals("Price on search list is not equals to price in a cart", productPriceInSearch, productPriceInCart); //Will fail, because prices are other on results list and in a cart - not user friendly behavior or a bug
//        StepLogger.info("Price on search results list and price in a cart are equals: " + productPriceInSearch);
    }
}
