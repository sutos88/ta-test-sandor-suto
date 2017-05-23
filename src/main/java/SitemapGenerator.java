import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


import java.util.List;

public class SitemapGenerator {

    public static List<String> mapItems = new ArrayList<String>();

    public static List<String> mapItemsUrl = new ArrayList<String>();

    //    static WebDriver driver = new RemoteWebDriver("http://localhost:9515", DesiredCapabilities.chrome());

    static WebDriver driver = new ChromeDriver();

    public static void init() {
        System.setProperty("webdriver.chrome.driver", "C://chromedriver.exe");
    }

    public static void main(String[] args) throws InterruptedException {
        init();
//        String actualSite="http://alaszkakft.hu/";
//        String actualSite="http://users.atw.hu/de-mi/";
        String actualSite="http://nagev.hu";
//        String actualSite="http://cinemacity.hu";
        driver.get(actualSite);

        int depth = 2;
        int depthCounter = 0;

        mapItems.add("HOME\n");

        siteMapGen(depth, depthCounter++, actualSite, actualSite);

        System.out.println("############ R E S U L T ##############");
        for (String item : mapItems) {
            System.out.print(item);
        }

    }


    public static void siteMapGen(int depth, int depthCounter, String actualSite, String homeUrl) {
        if (depthCounter >= depth) {
            System.out.println("### counter>=depth###");
            depthCounter--;
            return;
        }
        List<WebElement> actualWebElements = driver.findElements(By.tagName("a"));

        if (actualWebElements.size()==0) {
            depthCounter--;
            return;
        }
        List<String> actMapItems = new ArrayList<String>();
        List<String> actMapItemUrls = new ArrayList<String>();
        List<PageUrlText> actMapItemsUrl = new ArrayList<PageUrlText>();

        for (WebElement element : actualWebElements) {
            if (element.getAttribute("href") != null && element.getText() != null && !element.getText().equals("") &&
                    !element.getAttribute("href").equals("#") && !mapItems.contains(element.getText()) &&
                    !mapItemsUrl.contains(element.getAttribute("href")) && element.isDisplayed() &&
                    element.getAttribute("href").startsWith(homeUrl) /*&&
                    !element.getAttribute("href").equals(driver.getCurrentUrl())*/) {

                System.out.println(element.getText());  //for test.

                for (int j = 0; j < depthCounter; j++) {
                    actMapItems.add("\t");
                }

                actMapItems.add(element.getText().toString() + "\n");
                actMapItemUrls.add(element.getAttribute("href"));
                actMapItemsUrl.add(new PageUrlText(element.getAttribute("href"), element.getText()));
            }
        }

        mapItems.addAll(actMapItems);
        mapItemsUrl.addAll(actMapItemUrls);
        if (actMapItemsUrl.size()==0) {
            depthCounter--;
            return;
        }
        for (PageUrlText element : actMapItemsUrl) {

            driver.findElement(By.linkText(element.getText())).click();

            try {
                Thread.sleep(1000);
                System.out.println("Waiting to load page...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            siteMapGen(depth, depthCounter+1, driver.getCurrentUrl(), homeUrl);

            System.out.println("depthCounter:" + depthCounter + "\n"); //for test.

            driver.navigate().back();

            try {
                Thread.sleep(1000);
                System.out.println("Waiting to load previous page...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        depthCounter--;
    }
}
