package Utilities;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;

import static StepDefinitions.Hooks.driver;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrowserUtilities {
    public static void scrollToWebElement(WebElement webElement) {

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
    }

    public static WebElement waitForVisibility(WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofMillis(50000));
        return wait.until(ExpectedConditions.visibilityOf(element));

    }

    // 1. way throw exception
    // 2. way try/catch
    // 3. way lombok annotation SneakyThrows

    public static void waitFor(int sec) {

        try {
            Thread.sleep(sec*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static void hoverWebElement(WebElement webElement) {
          Actions actions = new Actions(driver);
        actions.moveToElement(webElement).build().perform();

    }
    public static WebElement waitForClickablility(WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(element));

    }

    private static WebDriverWait wait;

    public static String getScreenshot() throws IOException {
        // naming the screenshot with the current date to avoid duplication
        String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        // TakesScreenshot is an interface of selenium that takes the screenshot
        TakesScreenshot ts = (TakesScreenshot) Driver.getDriver();
        File source = ts.getScreenshotAs(OutputType.FILE);
        // full path to the screenshot location
        String target = System.getProperty("user.dir") + "/test-output/Screenshots/" + date + ".png";
        File finalDestination = new File(target);
        // save the screenshot to the path given
        FileUtils.copyFile(source, finalDestination);
        return target;
    }

    //========Switching Window=====//
    public static Object switchToWindow(String targetTitle) {
        String origin = Driver.getDriver().getWindowHandle();
        for (String handle : Driver.getDriver().getWindowHandles()) {
            Driver.getDriver().switchTo().window(handle);
            if (Driver.getDriver().getTitle().equals(targetTitle)) {
                return null;
            }
        }
        Driver.getDriver().switchTo().window(origin);
        return null;
    }

    //========Hover Over=====//
    public static void hover(WebElement element) {
        Actions actions = new Actions(Driver.getDriver());
        actions.moveToElement(element).perform();
    }

    //==========Return a list of string given a list of Web Element====////
    public static List<String> getElementsText(List<WebElement> list) {
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : list) {
            if (!el.getText().isEmpty()) {
                elemTexts.add(el.getText());
            }
        }
        return elemTexts;
    }

    //========Returns the Text of the element given an element locator==//
    public static List<String> getElementsText(By locator) {
        List<WebElement> elems = Driver.getDriver().findElements(locator);
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : elems) {
            if (!el.getText().isEmpty()) {
                elemTexts.add(el.getText());
            }
        }
        return elemTexts;
    }

    //   HARD WAIT WITH THREAD.SLEEP
//   waitFor(5);  => waits for 5 second => Thread.sleep(5000)




    public static WebElement waitForVisibility(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }



    public static WebElement waitForClickablility(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void clickWithTimeOut(WebElement element, int timeout) {
        for (int i = 0; i < timeout; i++) {
            try {
                element.click();
                return;
            } catch (WebDriverException e) {
                waitFor(1);
            }
        }
    }

    public static void waitForPageToLoad(long timeout) {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        try {
            System.out.println("Waiting for page to load...");
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
            wait.until(expectation);
        } catch (Throwable error) {
            System.out.println(
                    "Timeout waiting for Page Load Request to complete after " + timeout + " seconds");
        }
    }

    //======Fluent Wait====//
    public static WebElement fluentWait(final WebElement webElement, int timeout) {
        //FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Driver.getDriver()).withTimeout(timeinsec, TimeUnit.SECONDS).pollingEvery(timeinsec, TimeUnit.SECONDS);
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Driver.getDriver())
                .withTimeout(Duration.ofSeconds(3))//Wait 3 second each time
                .pollingEvery(Duration.ofSeconds(1))////Check for the element every 1 second
                .ignoring(NoSuchMethodException.class);
        WebElement element = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return webElement;
            }
        });
        return element;
    }

    public static void doubleClick(WebElement element) {
        new Actions(Driver.getDriver()).doubleClick(element).build().perform();
    }

    public static void selectCheckBox(WebElement element, boolean check) {
        if (check) {
            if (!element.isSelected()) {
                element.click();
            }
        } else {
            if (element.isSelected()) {
                element.click();
            }
        }
    }

    public static WebElement selectRandomTextFromDropdown(Select select) {
        Random random = new Random();
        List<WebElement> weblist = select.getOptions();
        int optionIndex = 1 + random.nextInt(weblist.size() - 1);
        select.selectByIndex(optionIndex);
        return select.getFirstSelectedOption();
    }

    public static void verifyElementDisplayed(By by) {
        try {
            assertTrue("Element not visible: " + by, Driver.getDriver().findElement(by).isDisplayed());
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + by);
        }
    }

    public static void verifyElementNotDisplayed(By by) {
        try {
            assertFalse("Element should not be visible: " + by, Driver.getDriver().findElement(by).isDisplayed());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    public static void verifyElementNotDisplayed(WebElement element) {
        try {
            assertFalse("Element should not be visible: " + element, element.isDisplayed());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    public static void verifyElementDisplayed(WebElement element) {
        try {
            assertTrue("Element not visible: " + element, element.isDisplayed());
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + element);
        }
    }

    public static void verifyElementEnabled(WebElement element) {
        try {
            assertTrue("Element not enabled: " + element, element.isEnabled());
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + element);
        }
    }

    protected static WebElement waitClickableByOfElement(WebElement webElement) {
        WebElement element = null;

        try {
            element = wait.until(ExpectedConditions.elementToBeClickable(webElement));
        } catch (Exception e) {
        }
        return element;
    }

    protected WebElement waitVisibleByLocator(By locator) {
        WebElement element = null;

        try {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
        }
        return element;
    }

    public static void clickElement(WebElement element) {
        element.click();
    }

    public static void scrollToElement(WebElement element) {
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void switchToWindow(int windowNumber) {
        List<String> list = new ArrayList<>(Driver.getDriver().getWindowHandles());
        Driver.getDriver().switchTo().window(list.get(windowNumber));
    }

    public static void scrollAndClickWithJS(WebElement webElement) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", webElement);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", webElement);
    }




    public static void clearValue(WebElement element) {
        String value = element.getAttribute("value");
        for (int i = 0; i < value.length(); i++) {
            element.sendKeys(Keys.BACK_SPACE);
        }
    }


    public static void clearValue(WebElement element, String text) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value','" + text + "')", element);
    }

    public static void nullCheck(WebElement element, String text) {
        if (text != null) {
            element.sendKeys(text);
        } else System.err.println("Null is not allowed for this method");
    }

    public static void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,document.body.scrollHeight)");
        waitFor(2);
    }

    public static String getCellValue(int satir, int sutun) {
        // //tbody/tr[7]/td[3]
        String dinamikXPath = "//tbody/tr[" + satir + "]/td[" + sutun + "]";
        return driver.findElement(By.xpath(dinamikXPath)).getText();
    }

    public static void clickCell(int row, int column) {
        String dynamicXPath = "//tbody/tr[" + row + "]/td[" + column + "]";
        driver.findElement(By.xpath(dynamicXPath)).click();
    }

    public static String giveDateAfterGivenDays(int dayToSkip) {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, dayToSkip);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(cal.getTime());


    }
}
