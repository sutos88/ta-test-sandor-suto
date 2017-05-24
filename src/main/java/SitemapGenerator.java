import java.io.*;
import java.util.*;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


import java.util.List;

public class SitemapGenerator {

    static WebDriver driver = new ChromeDriver();;

    public static List<String> mapItems = new ArrayList<String>();

    public static List<String> mapItemsUrl = new ArrayList<String>();

    public SitemapGenerator(WebDriver driver) {
        this.driver = driver;
    }


    public static void init() {
        System.setProperty("webdriver.chrome.driver", "C://chromedriver.exe");
    }

    public static FileInputStream fileOpen() {
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File("C://test//sites.xlsx"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static Iterator<Row> readFromXlsxRows() {
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(fileOpen());
        } catch (IOException e) {
            e.printStackTrace();
        }

        XSSFSheet sheet = workbook.getSheetAt(0);

        return sheet.iterator();
    }

    public static void main(String[] args) {
        init();

        int siteIterator=0;

        while (readFromXlsxRows().hasNext()) {

            Row row = readFromXlsxRows().next();

            Iterator<Cell> cellIterator = row.cellIterator();

            String actualSite = cellIterator.next().toString();

//        String actualSite="http://users.atw.hu/de-mi/";
//        String actualSite="http://nagev.hu";
//        String actualSite="http://gamestar.hu";
//        String actualSite="http://ni.com";

            siteIterator++;

            int depth = (int)cellIterator.next().getNumericCellValue();
            int depthCounter = 1;

            driver.get(actualSite);

            System.out.println(depth);  //for test.

            mapItems.add("HOME");
            mapItems.add("\n");

            new SitemapGenerator(driver).siteMapGen(depth, depthCounter, actualSite);

//        siteMapGen(depth, depthCounter++, actualSite, actualSite);

            System.out.println("############ R E S U L T ##############");
            for (String item : mapItems) {
                System.out.print(item);
            }

            writeToFile(siteIterator);

            mapItems.clear();
        }
    }

    public static void writeToFile(int siteIterator) {
        FileWriter writer = null;
        try {
            writer = new FileWriter("C://test//result-site-" + siteIterator + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(String item: mapItems) {
            try {
                writer.write(item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void siteMapGen(int depth, int depthCounter, String homeUrl) {

        int actDepthCounter = depthCounter;
        System.out.println("depthCounter: " + depthCounter); //for test
        if (depthCounter >= depth) {
            System.out.println("###  counter >= depth  ###");   //for test
            driver.navigate().back();
            return;
        }

        List<WebElement> actualWebElements = driver.findElements(By.tagName("a"));

        for (WebElement element : actualWebElements) {
            if (!mapItems.contains(element.getText()) && element.isDisplayed() /*&&
                    element.getAttribute("href").startsWith(homeUrl)*/) {
                for (int i = 0; i < actDepthCounter; i++) {
                    mapItems.add("\t");
                }
                mapItems.add(element.getText());
                mapItems.add("\n");
                System.out.println(element.getText());  //for test
                element.click();
                new SitemapGenerator(driver).siteMapGen(depth, actDepthCounter+1, homeUrl);
            }
        }
        driver.navigate().back();
    }

//    public static void siteMapGen(int depth, int depthCounter, String actualSite, String homeUrl) {
//        if (depthCounter >= depth) {
//            System.out.println("### counter>=depth###");
//            //depthCounter--;
//            return;
//        }
//        List<WebElement> actualWebElements = driver.findElements(By.tagName("a"));
//
//        if (actualWebElements.size()==0) {
//            //depthCounter--;
//            return;
//        }
//        List<String> actMapItems = new ArrayList<String>();
//        List<String> actMapItemUrls = new ArrayList<String>();
//        List<PageUrlText> actMapItemsUrl = new ArrayList<PageUrlText>();
//
//        for (WebElement element : actualWebElements) {
//            if (element.getAttribute("href") != null && element.getText() != null && !element.getText().equals("") &&
//                    !element.getAttribute("href").equals("#") && !mapItems.contains(element.getText()) &&
//                    !mapItemsUrl.contains(element.getAttribute("href")) && element.isDisplayed() &&
//                    element.getAttribute("href").startsWith(homeUrl) &&
//                    !element.getAttribute("href").equals(driver.getCurrentUrl())) {
//
//                System.out.println(element.getText());  //for test.
//
//                for (int j = 0; j < depthCounter; j++) {
//                    actMapItems.add("\t");
//                }
//
//                actMapItems.add(element.getText().toString() + "\n");
//                actMapItemUrls.add(element.getAttribute("href"));
//                actMapItemsUrl.add(new PageUrlText(element.getAttribute("href"), element.getText()));
//            }
//        }
//
//        mapItems.addAll(actMapItems);
//        mapItemsUrl.addAll(actMapItemUrls);
//        if (actMapItemsUrl.size()==0) {
//            depthCounter--;
//            return;
//        }
//        for (PageUrlText element : actMapItemsUrl) {
//
//            driver.findElement(By.linkText(element.getText())).click();
//
//            try {
//                Thread.sleep(1000);
//                System.out.println("Waiting to load page...");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            new SitemapGenerator(driver).siteMapGen(depth, depthCounter+1, driver.getCurrentUrl(), homeUrl);
//
//            System.out.println("depthCounter: " + depthCounter); //for test.
//
//            driver.navigate().back();
//
//            try {
//                Thread.sleep(1000);
//                System.out.println("Waiting to load previous page...");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        depthCounter--;
//    }

}
