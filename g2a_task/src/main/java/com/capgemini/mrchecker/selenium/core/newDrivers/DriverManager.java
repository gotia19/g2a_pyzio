package com.capgemini.mrchecker.selenium.core.newDrivers;

import com.capgemini.mrchecker.selenium.core.base.properties.PropertiesSelenium;
import com.capgemini.mrchecker.selenium.core.base.runtime.RuntimeParametersSelenium;
import com.capgemini.mrchecker.selenium.core.enums.ResolutionEnum;
import com.capgemini.mrchecker.selenium.core.exceptions.BFSeleniumGridNotConnectedException;
import com.capgemini.mrchecker.selenium.core.utils.OperationsOnFiles;
import com.capgemini.mrchecker.selenium.core.utils.ResolutionUtils;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.WebDriverManagerException;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class DriverManager {
    private static final ThreadLocal<BrowserMobProxyServer> localProxy = new ThreadLocal<>();
    private static final ThreadLocal<INewWebDriver>         drivers    = new ThreadLocal<>();

    // Setup default variables
    private static final ResolutionEnum     DEFAULT_RESOLUTION     = ResolutionEnum.w1920;
    private static final int                IMPLICITYWAITTIMER     = 2;                    // in seconds
    private static final String             DOWNLOAD_DIR           = getDownloadDir();
    private static       boolean            driverDownloadedChrome = false;
    private static       boolean            driverDownloadedGecko  = false;
    private static       boolean            driverDownloadedIE     = false;
    private static       boolean            driverDownloadedEdge   = false;
    private static       boolean            driverDownloadedOpera  = false;
    private static       PropertiesSelenium propertiesSelenium;

    private static final int     PROXY_MIN_PORT = 35000;
    private static final int     PROXY_MAX_PORT = 35300;
    private static final String  PROXY_SLAVE    = System.getProperty("localProxy");
    private static final boolean PROXY_ENABLED  = PROXY_SLAVE != null;

    private static String getDownloadDir() {
        String dir = System.getProperty("java.io.tmpdir");
        BFLogger.logInfo("getDownloadDir() DIR: " + dir);
        File dirFile = new File(dir);
        BFLogger.logInfo("getDownloadDir() UsableSpace: " + (dirFile.getUsableSpace() / (1024 * 1024)));
        BFLogger.logInfo("getDownloadDir() FreeSpace: " + (dirFile.getFreeSpace() / (1024 * 1024)));
        BFLogger.logInfo("getDownloadDir() CanWrite: " + dirFile.canWrite());
        return dir;
    }

    @Inject
    public DriverManager(@Named("properties") PropertiesSelenium propertiesSelenium) {
        if (null == DriverManager.propertiesSelenium) {
            DriverManager.propertiesSelenium = propertiesSelenium;
        }

        this.start();
    }

    private static int nextFreePort(int from, int to) {
        int counter = 0;
        while (counter < 100) {
            int port = ThreadLocalRandom.current().nextInt(from, to);
            if (isLocalPortFree(port)) {
                return port;
            }
            counter++;
        }
        return -1;
    }

    private static boolean isLocalPortFree(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public static int startLocalProxy() {
        int  result      = -1;
        long timeBegin   = System.currentTimeMillis();
        long timeCounter = System.currentTimeMillis() - timeBegin;
        while (result < 0 && timeCounter < 30000) {
            timeCounter = System.currentTimeMillis() - timeBegin;
            try {
                result = startProxy();
            }
            catch (Throwable ignored) {
            }
        }
        return startProxy();
    }

    private static int startProxy() {
        BrowserMobProxyServer proxy = getProxy();
        if (proxy == null) {
            proxy = new BrowserMobProxyServer();
            proxy.setTrustAllServers(true);
            localProxy.set(proxy);
        }
        if (!proxy.isStarted() || proxy.isStopped()) {
            proxy.start(nextFreePort(PROXY_MIN_PORT, PROXY_MAX_PORT));
            BFLogger.logDebug("startLocalProxy() Local proxy started with port:" + proxy.getPort());
        }
        else {
            BFLogger.logDebug("startLocalProxy() Local proxy already started with port:" + proxy.getPort());
        }
        return proxy.getPort();
    }

    public static BrowserMobProxyServer getProxy() {
        return localProxy.get();
    }

    private static Proxy getSeleniumProxy() {
        BrowserMobProxyServer proxy = getProxy();
        BFLogger.logInfo("getSeleniumProxy() ::::::::::: hostIp=" + PROXY_SLAVE);
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        seleniumProxy.setHttpProxy(PROXY_SLAVE + ":" + proxy.getPort());
        seleniumProxy.setSslProxy(PROXY_SLAVE + ":" + proxy.getPort());
        return seleniumProxy;
    }

    public static void stopLocalProxy() {
        BrowserMobProxyServer proxy = getProxy();
        if (proxy == null) {
            BFLogger.logDebug("stopLocalProxy() There is no proxy");
            localProxy.set(null);
            return;
        }
        if (!proxy.isStarted() || proxy.isStopped()) {
            BFLogger.logDebug("stopLocalProxy() Proxy is already stopped");
            localProxy.set(null);
            return;
        }
        // STOP
        try {
            proxy.stop();
        }
        catch (Throwable ignored) {
        }
        long timeBegin   = System.currentTimeMillis();
        long timeCounter = System.currentTimeMillis() - timeBegin;
        while (!proxy.isStopped() && proxy.isStarted() && timeCounter < 30000) {
            try {
                timeCounter = System.currentTimeMillis() - timeBegin;
                proxy.stop();
            }
            catch (Throwable ignored) {
            }
        }
        // ABORT
        if (proxy.isStarted() && !proxy.isStopped()) {
            try {
                proxy.abort();
            }
            catch (Throwable ignored) {
            }
            timeBegin   = System.currentTimeMillis();
            timeCounter = System.currentTimeMillis() - timeBegin;
            while (!proxy.isStopped() && proxy.isStarted() && timeCounter < 30000) {
                try {
                    timeCounter = System.currentTimeMillis() - timeBegin;
                    proxy.abort();
                }
                catch (Throwable ignored) {
                }
            }
        }
        BFLogger.logDebug("stopLocalProxy() Proxy stopped: " + (proxy.isStopped() || !proxy.isStarted()));
        localProxy.set(null);
    }

    public void start() {
        DriverManager.getDriver();
    }

    public void stop() {
        try {
            closeDriver();
            BFLogger.logDebug("Closing Driver in stop()");
            BFLogger.logInfo(String.format("All clicks took %.2fs", 1.0 * NewRemoteWebElement.dropClickTimer() / 1000));
        }
        catch (Exception ignored) {
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            closeDriver();
            BFLogger.logDebug("Closing Driver in finalize()");
        }
        catch (Exception ignored) {
        }
    }

    public static INewWebDriver getDriver() {
        if (isDriverClosed()) {
            INewWebDriver driver = createDriver();
            drivers.set(driver);
            BFLogger.logDebug("driver:" + driver);
        }
        return drivers.get();
    }

    public static boolean isDriverClosed() {
        INewWebDriver driver = drivers.get();
        boolean       closed;
        try {
            closed = driver == null || driver.toString().contains("(null)") || driver.getWindowHandles().size() < 1;
        }
        catch (Exception e) {
            closed = true;
        }
        if (closed) {
            drivers.remove();
        }
        return closed;
    }

    public static void closeDriver() {
        stopLocalProxy();
        if (isDriverClosed()) {
            BFLogger.logDebug("closeDriver() was called but there was no driver for this thread.");
        }
        else {
            INewWebDriver driver = drivers.get();
            try {
                BFLogger.logDebug("Closing WebDriver for this thread. " + RuntimeParametersSelenium.BROWSER.getValue());
                driver.quit();
            }
            catch (WebDriverException e) {
                BFLogger.logDebug("Ooops! Something went wrong while closing the driver: ");
                e.printStackTrace();
            }
            finally {
                drivers.remove();
            }
        }
    }

    /**
     * Method sets desired 'driver' depends on chosen parameters
     */
    private static INewWebDriver createDriver() {
        // Could be moved to Mr.Checker properties
        if (PROXY_ENABLED) {
            startLocalProxy();
        }

        BFLogger.logDebug("Creating new " + RuntimeParametersSelenium.BROWSER + " WebDriver.");
        INewWebDriver driver;
        String        seleniumGridParameter = RuntimeParametersSelenium.SELENIUM_GRID.getValue();
        if (isEmpty(seleniumGridParameter)) {
            driver = setupBrowser();
        }
        else {
            driver = setupGrid();
        }
        driver.manage().timeouts().implicitlyWait(DriverManager.IMPLICITYWAITTIMER, TimeUnit.SECONDS);

        ResolutionUtils.setResolution(driver, DriverManager.DEFAULT_RESOLUTION);
        NewRemoteWebElement.setClickTimer();
        return driver;
    }

    private static boolean isEmpty(String seleniumGridParameter) {
        return seleniumGridParameter == null || seleniumGridParameter.trim().isEmpty();
    }

    /**
     * Method sets Selenium Grid
     */
    private static INewWebDriver setupGrid() {
        int counter = 0;
        while (counter < 3) {
            try {
                return Driver.SELENIUMGRID.getDriver();
            }
            catch (Throwable e) {
                BFLogger.logInfo("setupGrid() EXCEPTION: " + e);
                getDownloadDir();
                try {
                    Thread.sleep(10000);
                }
                catch (InterruptedException ignored) {
                }
            }
            counter++;
        }

        // Default Mr.Checker
        try {
            return Driver.SELENIUMGRID.getDriver();
        }
        catch (WebDriverException e) {
            throw new BFSeleniumGridNotConnectedException(e);
        }
    }

    /**
     * Method sets desired 'driver' depends on chosen parameters
     */
    private static INewWebDriver setupBrowser() {
        String browser = RuntimeParametersSelenium.BROWSER.getValue();
        switch (browser) {
            case "chrome":
                return Driver.CHROME.getDriver();
            case "opera":
            case "operablink":
                return Driver.OPERA.getDriver();
            case "edge":
                return Driver.EDGE.getDriver();
            case "firefox":
                return Driver.FIREFOX.getDriver();
            case "internet explorer":
                return Driver.IE.getDriver();
            case "chromeheadless":
                return Driver.CHROME_HEADLESS.getDriver();
            default:
                throw new RuntimeException("Unable to setup [" + browser + "] browser. Browser not recognized.");
        }
    }

    private enum Driver {
        CHROME {
            @Override
            public INewWebDriver getDriver() {
                String  browserPath                 = DriverManager.propertiesSelenium.getSeleniumChrome();
                boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
                synchronized (this) {
                    if (isDriverAutoUpdateActivated && !driverDownloadedChrome) {
                        if (!DriverManager.propertiesSelenium.getChromeDriverVersion().equals("")) {
                            System.setProperty("wdm.chromeDriverVersion",
                                               DriverManager.propertiesSelenium.getChromeDriverVersion());
                        }
                        downloadNewestOrGivenVersionOfWebDriver(ChromeDriver.class);
                        OperationsOnFiles.moveWithPruneEmptydirectories(
                                WebDriverManager.getInstance(ChromeDriver.class).getBinaryPath(), browserPath);
                    }
                    driverDownloadedChrome = true;
                }

                System.setProperty("webdriver.chrome.driver", browserPath);
                ChromeOptions options = getChromeOptions();
                return new NewChromeDriver(options);
            }
        },
        EDGE {
            @Override
            public INewWebDriver getDriver() {
                // Microsoft WebDriver for Microsoft Edge from version 18 is a Windows Feature on Demand.
                // To install run the following in an elevated command prompt:
                // DISM.exe /Online /Add-Capability /CapabilityName:Microsoft.WebDriver~~~~0.0.1.0
                // For builds prior to 18, download the appropriate driver for your installed version of Microsoft Edge
                // Info: https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/#downloads
                boolean featureOnDemand = DriverManager.propertiesSelenium.getEdgeDriverFeatureOnDemandFlag();
                if (!featureOnDemand) {
                    String  browserPath                 = DriverManager.propertiesSelenium.getSeleniumEdge();
                    boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
                    synchronized (this) {
                        if (isDriverAutoUpdateActivated && !driverDownloadedEdge) {
                            if (!DriverManager.propertiesSelenium.getEdgeDriverVersion().equals("")) {
                                System.setProperty("wdm.edgeVersion",
                                                   DriverManager.propertiesSelenium.getEdgeDriverVersion());
                            }
                            downloadNewestOrGivenVersionOfWebDriver(EdgeDriver.class);
                            OperationsOnFiles.moveWithPruneEmptydirectories(
                                    WebDriverManager.getInstance(EdgeDriver.class).getBinaryPath(), browserPath);
                        }
                        driverDownloadedEdge = true;
                    }
                    System.setProperty("webdriver.edge.driver", browserPath);
                }

                EdgeOptions options = getEdgeOptions();
                return new NewEdgeDriver(options);
            }
        },
        OPERA {
            @Override
            public INewWebDriver getDriver() {
                String  browserPath                 = DriverManager.propertiesSelenium.getSeleniumOpera();
                boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
                synchronized (this) {
                    if (isDriverAutoUpdateActivated && !driverDownloadedOpera) {
                        if (!DriverManager.propertiesSelenium.getOperaDriverVersion().equals("")) {
                            System.setProperty("wdm.operaDriverVersion",
                                               DriverManager.propertiesSelenium.getOperaDriverVersion());
                        }
                        downloadNewestOrGivenVersionOfWebDriver(OperaDriver.class);
                        OperationsOnFiles.moveWithPruneEmptydirectories(
                                WebDriverManager.getInstance(OperaDriver.class).getBinaryPath(), browserPath);
                    }
                    driverDownloadedOpera = true;
                }

                System.setProperty("webdriver.opera.driver", browserPath);
                OperaOptions options = getOperaOptions();
                return new NewOperaDriver(options);
            }
        },
        CHROME_HEADLESS {
            @Override
            public INewWebDriver getDriver() {
                String  browserPath                 = DriverManager.propertiesSelenium.getSeleniumChrome();
                boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
                synchronized (this) {
                    if (isDriverAutoUpdateActivated && !driverDownloadedChrome) {
                        if (!DriverManager.propertiesSelenium.getChromeDriverVersion().equals("")) {
                            System.setProperty("wdm.chromeDriverVersion",
                                               DriverManager.propertiesSelenium.getChromeDriverVersion());
                        }
                        downloadNewestOrGivenVersionOfWebDriver(ChromeDriver.class);
                        OperationsOnFiles.moveWithPruneEmptydirectories(
                                WebDriverManager.getInstance(ChromeDriver.class).getBinaryPath(), browserPath);
                    }
                    driverDownloadedChrome = true;
                }
                System.setProperty("webdriver.chrome.driver", browserPath);
                ChromeOptions options = getChromeOptions();
                options.addArguments("--headless");
                return new NewChromeDriver(options);
            }
        },
        FIREFOX {
            @Override
            public INewWebDriver getDriver() {
                String  browserPath                 = DriverManager.propertiesSelenium.getSeleniumFirefox();
                boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
                synchronized (this) {
                    if (isDriverAutoUpdateActivated && !driverDownloadedGecko) {
                        if (!DriverManager.propertiesSelenium.getGeckoDriverVersion().equals("")) {
                            System.setProperty("wdm.geckoDriverVersion",
                                               DriverManager.propertiesSelenium.getGeckoDriverVersion());
                        }
                        downloadNewestOrGivenVersionOfWebDriver(FirefoxDriver.class);
                        OperationsOnFiles.moveWithPruneEmptydirectories(
                                WebDriverManager.getInstance(FirefoxDriver.class).getBinaryPath(), browserPath);
                    }
                    driverDownloadedGecko = true;
                }
                System.setProperty("webdriver.gecko.driver", browserPath);
                System.setProperty("webdriver.firefox.logfile", "logs\\firefox_logs.txt");
                FirefoxOptions options = getFirefoxOptions();
                return new NewFirefoxDriver(options);
            }
        },
        IE {
            @Override
            public INewWebDriver getDriver() {
                String  browserPath                 = DriverManager.propertiesSelenium.getSeleniumIE();
                boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
                synchronized (this) {
                    if (isDriverAutoUpdateActivated && !driverDownloadedIE) {
                        if (!DriverManager.propertiesSelenium.getInternetExplorerDriverVersion().equals("")) {
                            System.setProperty("wdm.internetExplorerDriverVersion",
                                               DriverManager.propertiesSelenium.getInternetExplorerDriverVersion());
                        }
                        downloadNewestOrGivenVersionOfWebDriver(InternetExplorerDriver.class);
                        OperationsOnFiles.moveWithPruneEmptydirectories(
                                WebDriverManager.getInstance(InternetExplorerDriver.class).getBinaryPath(),
                                browserPath);
                    }
                    driverDownloadedIE = true;
                }
                System.setProperty("webdriver.ie.driver", browserPath);
                DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

                // Set users browser options
                RuntimeParametersSelenium.BROWSER_OPTIONS.getValues().forEach((key, value) -> {
                    BFLogger.logInfo("Add to IE desired capabilities: " + key + " = " + value.toString());
                    ieCapabilities.setCapability(key, value.toString());
                });

                // Due to some issues with IE11 this line must be commented
                // ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                // true);
                return new NewInternetExplorerDriver(ieCapabilities);
            }
        },
        SELENIUMGRID {
            @Override
            public INewWebDriver getDriver() {
                final String SELENIUM_GRID_URL = RuntimeParametersSelenium.SELENIUM_GRID.getValue();
                BFLogger.logDebug("Connecting to the selenium grid: " + SELENIUM_GRID_URL);
                DesiredCapabilities capabilities    = new DesiredCapabilities();
                String              operatingSystem = RuntimeParametersSelenium.OS.getValue();

                // TODO add others os's
                switch (operatingSystem) {
                    case "windows":
                        capabilities.setPlatform(Platform.WINDOWS);
                        break;
                    case "vista":
                        capabilities.setPlatform(Platform.VISTA);
                        break;
                    case "mac":
                        capabilities.setPlatform(Platform.MAC);
                        break;
                }

                capabilities.setVersion(RuntimeParametersSelenium.BROWSER_VERSION.getValue());
                capabilities.setBrowserName(RuntimeParametersSelenium.BROWSER.getValue());

                String browser = RuntimeParametersSelenium.BROWSER.getValue();
                switch (browser.toLowerCase()) {
                    case "chrome":
                        capabilities.setCapability(ChromeOptions.CAPABILITY, getChromeOptions());
                        break;
                    case "firefox":
                        capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, getFirefoxOptions());
                        break;
                    case "opera":
                    case "operablink":
                        capabilities.setCapability(OperaOptions.CAPABILITY, getOperaOptions());
                        break;
                    default:
                        throw new IllegalStateException("Unsupported browser: " + browser);
                }

                // Set users browser options
                RuntimeParametersSelenium.BROWSER_OPTIONS.getValues().forEach((key, value) -> {
                    BFLogger.logInfo("Browser option: " + key + " " + value.toString());
                    capabilities.setCapability(key, value.toString());
                });

                NewRemoteWebDriver newRemoteWebDriver = null;
                try {
                    newRemoteWebDriver = new NewRemoteWebDriver(new URL(SELENIUM_GRID_URL), capabilities);
                    newRemoteWebDriver.setFileDetector(new LocalFileDetector());
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                    System.out.println("Unable to find selenium grid URL: " + SELENIUM_GRID_URL);
                }
                return newRemoteWebDriver;
            }
        };

        private static <T extends RemoteWebDriver> void downloadNewestOrGivenVersionOfWebDriver(Class<T> webDriverType) {
            String proxy          = DriverManager.propertiesSelenium.getProxy();
            String webDriversPath = DriverManager.propertiesSelenium.getWebDrivers();
            try {
                System.setProperty("wdm.targetPath", webDriversPath);
                System.setProperty("wdm.useBetaVersions", "false");

                WebDriverManager.getInstance(webDriverType).proxy(proxy).setup();
                BFLogger.logDebug("Downloaded version of driver=" + WebDriverManager.getInstance(webDriverType)
                                                                                    .getDownloadedVersion());
            }
            catch (WebDriverManagerException e) {
                BFLogger.logInfo(
                        "Unable to download driver automatically. " + "Please try to set up the proxy in properties file. " + "If you want to download them manually, go to the " + "http://www.seleniumhq.org/projects/webdriver/ site.");
            }
        }

        abstract public INewWebDriver getDriver();
    }

    private static ChromeOptions getChromeOptions() {
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("download.default_directory", DOWNLOAD_DIR);
        //chromePrefs.put("--user-data-dir", DOWNLOAD_DIR);
        chromePrefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
        chromePrefs.put("profile.default_content_setting_values.notifications", 2);

        RuntimeParametersSelenium.BROWSER_OPTIONS.getValues().forEach((key, value) -> {
            BFLogger.logInfo("Add to Chrome prefs: " + key + " = " + value.toString());
            chromePrefs.put(key, value.toString());
        });

        ChromeOptions options = new ChromeOptions();
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--test-type");
        options.addArguments("window-size=1920x1080");
        options.addArguments("--ignore-certificate-errors");
        if (PROXY_ENABLED) {
            options.setProxy(getSeleniumProxy());
        }

        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless");
        }

        // Set users browser options
        RuntimeParametersSelenium.BROWSER_OPTIONS.getValues().forEach((key, value) -> {
            BFLogger.logInfo("Add to Chrome options: " + key + " = " + value.toString());
            String item = (value.toString().isEmpty()) ? key : key + "=" + value;
            options.addArguments(item);
            options.setCapability(key, value.toString());
        });
        return options;
    }

    private static FirefoxOptions getFirefoxOptions() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("webdriver_assume_untrusted_issuer", false);
        profile.setPreference("webdriver.firefox.marionette", true);
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", DOWNLOAD_DIR);
        profile.setPreference("browser.download.useDownloadDir", true);

        // Mariusz added those 4 lines
        profile.setPreference("dom.disable_beforeunload", true);
        profile.setPreference("browser.cache.disk.enable", false);
        profile.setPreference("browser.cache.memory.enable", false);
        profile.setPreference("browser.cache.offline.enable", false);

        profile.setPreference("intl.accept_languages", "en-us");
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                              "text/comma-separated-values, application/vnd.ms-excel, application/msword, application/csv, application/ris, text/csv, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.helperApps.alwaysAsk.force", false);

        //PDF elements stop loading on the pages if set to true
        //profile.setPreference("pdfjs.disabled", true);

        profile.setPreference("network.http.use-cache", true);
        profile.setAcceptUntrustedCertificates(true);
        profile.setAssumeUntrustedCertificateIssuer(false);

        RuntimeParametersSelenium.BROWSER_OPTIONS.getValues().forEach((key, value) -> {
            BFLogger.logInfo("Add to Firefox profile: " + key + " = " + value.toString());
            profile.setPreference(key, value.toString());
        });

        FirefoxOptions options = new FirefoxOptions().setProfile(profile);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setCapability("acceptInsecureCerts", true);
        options.setCapability("acceptSslCerts", true);
        options.setCapability("handleAlerts", true);
        options.setCapability("network.http.use-cache", false);
        options.setCapability("security.cert_pinning.enforcement_level", 0);
        options.setCapability("security.enterprise_roots.enabled", true);
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless");
        }

        if (PROXY_ENABLED) {
            options.setProxy(getSeleniumProxy());
        }

        // Set users browser options
        RuntimeParametersSelenium.BROWSER_OPTIONS.getValues().forEach((key, value) -> {
            BFLogger.logInfo("Add to Firefox options: " + key + " = " + value.toString());
            String item = (value.toString().isEmpty()) ? key : key + "=" + value;
            options.addArguments(item);
            options.setCapability(key, value.toString());
        });
        return options;
    }

    private static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();

        if (PROXY_ENABLED) {
            options.setProxy(getSeleniumProxy());
        }

        // Set users browser options
        RuntimeParametersSelenium.BROWSER_OPTIONS.getValues().forEach((key, value) -> {
            BFLogger.logInfo("Add to Edge options: " + key + " = " + value.toString());
            options.setCapability(key, value.toString());
        });

        return options;
    }

    private static OperaOptions getOperaOptions() {
        HashMap<String, Object> operaPrefs = new HashMap<>();
        operaPrefs.put("download.default_directory", DOWNLOAD_DIR);
        //operaPrefs.put("--user-data-dir", DOWNLOAD_DIR);
        operaPrefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
        operaPrefs.put("profile.default_content_setting_values.notifications", 2);

        RuntimeParametersSelenium.BROWSER_OPTIONS.getValues().forEach((key, value) -> {
            BFLogger.logInfo("Add to Opera prefs: " + key + " = " + value.toString());
            operaPrefs.put(key, value.toString());
        });

        OperaOptions options = new OperaOptions();
        options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
        options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, PageLoadStrategy.NORMAL);
        options.setExperimentalOption("prefs", operaPrefs);
        options.addArguments("--test-type");
        options.addArguments("window-size=1920x1080");
        if (PROXY_ENABLED) {
            options.setProxy(getSeleniumProxy());
        }

        //ToDo: Opera doesn't support headless mode?
        //https://github.com/operasoftware/operachromiumdriver/issues/62
        //        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
        //            options.addArguments("--headless");
        //        }

        // Set users browser options
        RuntimeParametersSelenium.BROWSER_OPTIONS.getValues().forEach((key, value) -> {
            BFLogger.logInfo("Add to Opera options: " + key + " = " + value.toString());
            String item = (value.toString().isEmpty()) ? key : key + "=" + value;
            options.addArguments(item);
            options.setCapability(key, value.toString());
        });
        return options;
    }
}