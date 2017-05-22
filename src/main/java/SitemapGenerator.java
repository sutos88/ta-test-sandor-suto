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
        String actualSite="http://users.atw.hu/de-mi/";

        driver.get(actualSite);

        int depth = 5;
        int depthCounter = 0;

        List<WebElement> links = driver.findElements(By.tagName("a"));

        siteMapGen(depth, links, depthCounter, actualSite);

//        String pageSource = driver.getPageSource();
//        System.out.println(pageSource);

//        String actualSite="http://users.atw.hu/de-mi/";
//        String actualSite="https://gamestar.hu/";
//        String actualSite="https://it.inf.unideb.hu/honlap/prog1/";
//        String actualSite="http://www.nagev.hu/";

//        String actualSite="https://mobilarena.hu/";
//        int border=2;
//        int depth=border;
//
//        driver.get(actualSite);
//
//        siteMapGen(depth, actualSite, border);

//            2.version
//        List<WebElement> links = driver.findElements(By.tagName("a"));
//
//        for (WebElement link : links) {
//            if (link.getAttribute("href").startsWith(actualSite)) {
//                siteMapGen(depth, link.getText(), border, link.getAttribute("href"));
//                break;
//            }
//        }
//        2. version end

        for (String item : mapItems) {
            System.out.print(item);
        }

//        for (int i = 0; i < 3; i++) {
//            driver.get(actualSite);
//
//            List<WebElement> links = driver.findElements(By.tagName("a"));
//            for (WebElement link : links) {
//
//                if (link.getAttribute("href") != null) {
//                    if (link.getAttribute("href").toString().startsWith("https://444.hu")) {
//                        System.out.println("***");
//                        System.out.print(link.getAttribute("href"));
//                        System.out.println("---");
//                    }
//                }
//            }
//        }

    }
//    public static void siteMapGen (int depth, String actualSite, int border) {
//        WebDriver driver = new ChromeDriver();
//        driver.get(actualSite);
//
//        List<WebElement> links = driver.findElements(By.tagName("a"));
//
//        for (WebElement link : links) {
//            if (link.getAttribute("href") != null) {
//                String linkUrl = link.getAttribute("href").toString();
//                if (linkUrl.startsWith(actualSite) && !mapitems.contains(linkUrl)) {
//                    for (int i = 0; i < border; i++) {
//                        mapitems.add("\t");
//                    }
//                    mapitems.add(linkUrl);
//                    border++;
//                    depth--;
//                    actualSite = linkUrl;
//                    siteMapGen(depth, actualSite, border);
//                }
//            }
//        }
//
//    }

//    public static void siteMapGen (int depth, String actualSite, int border) throws InterruptedException {
//        if (depth == 0) {
//            driver.navigate().back();
//            return;
//        }
//        border-=depth;
//        String linkUrl = "";
//        List<WebElement> links = driver.findElements(By.tagName("a"));
//        int db = 0;
//        for (WebElement link : links) {
//            if (link.getAttribute("href") != null) {
//                System.out.println(link.getText());
//                if (link.getAttribute("href").toString().startsWith(actualSite) && link.isDisplayed() && link.getAttribute("href").toString().equals(actualSite) && !mapitems.contains(link.getText())) {
//                    for (int i = 0; i < border; i++) {
//                        mapitems.add("\t");
//                    }
//                    mapitems.add(link.getText());
//                    mapitems.add("\n");
//                    db++;
//                }
//            }
//        }
//        if (db==0) {
//            return;
//        }
//        for (String item : mapitems) {
//            System.out.print(item);
//        }
//        for (WebElement link : links) {
//            if (link.getAttribute("href") != null) {
//                linkUrl = link.getAttribute("href");
//                if (linkUrl.startsWith(actualSite) &&  link.isDisplayed() && !linkUrl.equals(actualSite) && !mapitems.contains(link.getText())) {
//                    System.out.println(actualSite);
//                    System.out.println(linkUrl);
//                    driver.findElement(By.linkText(link.getText())).click();
//                    System.out.println("****");
//                    siteMapGen(depth--, linkUrl, border++);
//                    driver.navigate().back();
//                }
//            }
//        }
//        driver.navigate().back();
//    }

//    public static void siteMapGen (int depth, String actualSiteText, int border, String actualLink) {
//        if (depth == 0) {
//            driver.navigate().back();
//            return;
//        }
//
//        border-=depth;
//
////        driver.findElement(By.linkText(actualSiteText)).click();
//
//        List<WebElement> previousLinks = driver.findElements(By.tagName("a"));
//        for (WebElement link : previousLinks) {
//            if (link.getText().equalsIgnoreCase(actualSiteText)) {
//                System.out.println(link.getText());
//                link.click();
//                System.out.println("Klikk a metódusban");
//                break;
//            }
//        }
//
//
//        List<WebElement> links = driver.findElements(By.tagName("a"));
//        List<String> actualList = new ArrayList<String>();
//
//        for (WebElement link : links) {
//            if (link.getAttribute("href") != null && link.getAttribute("href").startsWith(actualLink) && link.getText()!=actualSiteText && !mapitems.contains(link.getText())) {
//                actualList.add(link.getText());
//            }
//        }
//
//        if (actualList.size()==0) {
//            driver.navigate().back();
//            return;
//        }
//
//        for (String acItem : actualList) {
//            for (int i = 0; i < border; i++) {
//                System.out.println(acItem);
//                mapitems.add("\t");
//                mapitems.add(acItem + "\n");
//                siteMapGen(depth--, acItem, border++, driver.getCurrentUrl());
//                driver.navigate().back();
//            }
//        }
//    }

    public static void siteMapGen(int depth, List<WebElement> actualWebElements, int depthCounter, String actualSite) {
        if (depthCounter == depth) {
            return;
        }

        for (WebElement element : actualWebElements) {
            if (element.getAttribute("href") != null && !element.getAttribute("href").equals("") && !element.getAttribute("href").equals("#")
                    && !mapItems.contains(element.getText()) && !mapItemsUrl.contains(element.getAttribute("href")) /*&& element.getAttribute("href").startsWith(actualSite)*/) {

                System.out.println(element.getText());  //for test.

                mapItemsUrl.add(element.getAttribute("href"));

                for (int j = 0; j < depthCounter; j++) {
                    mapItems.add("\t");
                }

                mapItems.add(element.getText().toString() + "\n");

                driver.findElement(By.linkText(element.getText())).click();

                try {
                    Thread.sleep(1000);
                    System.out.println("Waiting to load page...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                List<WebElement> nextLinks = driver.findElements(By.tagName("a"));

                siteMapGen(depth, nextLinks, depthCounter++, driver.getCurrentUrl());

                mapItems.add("depthCounter:" + depthCounter + "\n"); //for test.

                driver.navigate().back();

                try {
                    Thread.sleep(1000);
                    System.out.println("Waiting to load previous page...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
