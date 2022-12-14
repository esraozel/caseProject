package com.caseProject.step;

import com.caseProject.base.BaseTest;
import com.caseProject.model.ElementInfo;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseSteps extends BaseTest {

    public static int DEFAULT_MAX_ITERATION_COUNT = 10;
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 1000;

    public BaseSteps() {
        initMap(getFileList());
    }

    WebElement findElement(String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    List<WebElement> findElements(String key) {
        return driver.findElements(getElementInfoToBy(findElementInfoByKey(key)));
    }

    public By getElementInfoToBy(ElementInfo elementInfo) {
        By by = null;
        if (elementInfo.getType().equals("css")) {
            by = By.cssSelector(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("name"))) {
            by = By.name(elementInfo.getValue());
        } else if (elementInfo.getType().equals("id")) {
            by = By.id(elementInfo.getValue());
        } else if (elementInfo.getType().equals("xpath")) {
            by = By.xpath(elementInfo.getValue());
        } else if (elementInfo.getType().equals("linkText")) {
            by = By.linkText(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("partialLinkText"))) {
            by = By.partialLinkText(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("src"))) {
            by = By.partialLinkText(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("tagname"))) {
            by = By.tagName(elementInfo.getValue());
        }
        return by;
    }

    private void clickElement(WebElement element) {
        element.click();
    }

    public WebElement findElementWithKey(String key) {
        return findElement(key);
    }

    public void javascriptclicker(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    @Step("<int> saniye bekle")
    public void waitBySeconds(int seconds) {
        try {
            logger.info(seconds + " saniye bekleniyor.");
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("<long> milisaniye bekle")
    public void waitByMilliSeconds(long milliseconds) {
        try {
            logger.info(milliseconds + " milisaniye bekleniyor.");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("Elementine tıkla <key>")
    public void clickElement(String key) {
        if (!key.isEmpty()) {
            clickElement(findElement(key));
            logger.info(key + " elementine tıklandı.");
        }
    }

    @Step("Element var mı kontrol et <key>")
    public WebElement getElementWithKeyIfExists(String key) {
        WebElement webElement;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            try {
                webElement = findElementWithKey(key);
                logger.info(key + " elementi bulundu.");
                return webElement;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        assertFalse(Boolean.parseBoolean("Element: '" + key + "' doesn't exist."));
        return null;
    }

    @Step("<text> textini <key> elemente yaz")
    public void sendKeys(String text, String key) {
        if (!key.equals("")) {
            findElement(key).sendKeys(text);
            logger.info(key + " elementine " + text + " texti yazıldı.");
        }
    }

    @Step({"<key> elementi <text> değerini içeriyor mu kontrol et"})
    public void checkElementContainsText(String key, String text) {
        System.out.print(findElement(key).getText());
        Boolean containsText = findElement(key).getText().contains(text);
        System.out.print(containsText);
        logger.info("Alınan text değeri" + containsText);
        assertTrue(containsText, "Expected text is not contained");
        logger.info(key + " elementi " + text + " değerini içeriyor.");
    }

    @Step({"<key> listesinden ilk elemente tıkla"})
    public void firstPick(String key) {
        List<WebElement> elements = findElements(key);
        javascriptclicker(elements.get(0));
        logger.info("İlk elemente tıklandı.");
    }

    public void randomPick(String key) {
        try {
            List<WebElement> elements = findElements(key);
            Random random = new Random();
            int index = random.nextInt(elements.size());
            if (elements.size() > 0) {
                elements.get(index).click();
            }
        } catch (Exception e) {
            System.out.println("Tıklama işlemi hatalı.");
        }
    }

    public void checkList(String key, String text) {
        List<WebElement> elements = findElements(key);
        for(int i = 0; i< elements.size() ; i++)
        {
            elements.get(i).getText().contains(text);
        }
    }

    @Step("<key> elementine javascript ile tıkla")
    public void clickToElementWithJavaScript(String key) {
        WebElement element = findElement(key);
        javascriptclicker(element);
        logger.info(key + " elementine javascript ile tıklandı");
    }

    @Step("<key> menu listesinden rastgele seç")
    public void chooseRandomElementFromList(String key) {
        randomPick(key);
    }

    @Step("<key> menu listesi <text> değerini içeriyor mu kontrol et")
    public void checkListText(String key, String text) {
        checkList(key, text);
    }

    @Step("<key> menu listesinden sonuncuyu seç")
    public void chooselastpick(String key) {
        lastPick(key);
    }

    public void lastPick(String key) {
        List<WebElement> elements = findElements(key);
        javascriptclicker(elements.get(elements.size()-1));
    }

    @Step("Element varsa javascript ile tıkla <key>")
    public void clickOnDefinedElementWithJavascript(String key) {
        try
        {
            if (findElements(key).size() > 0) {
                WebElement element = findElement(key);
                javascriptclicker(element);
            }else
            {
                System.out.println("Element bulunamadı.");
            }
        }catch (Exception e){
            System.out.println("Tıklama işlemi hatalı.");
        }
    }
}
