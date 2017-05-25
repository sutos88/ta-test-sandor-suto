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

    public static List<PageUrlText> mapItems = new ArrayList<PageUrlText>();

    public static List<String> mapItemsStr = new ArrayList<String>();

    public static List<String> mapItemsUrlStr = new ArrayList<String>();

    public SitemapGenerator(WebDriver driver) {
        this.driver = driver;
    }

    public static void main(String[] args) {
        init();

        int siteIterator=0;
        Iterator<Row> Rows = readFromXlsxRows();
        while (Rows.hasNext()) {

            Row row = Rows.next();

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

            mapItems.add(new PageUrlText(actualSite, "Homepage:", actualSite, 0 ));

//            new SitemapGenerator(driver).siteMapGen(depth, depthCounter, actualSite);

//        new SitemapGenerator(driver).siteMapGen(depth, depthCounter, actualSite, actualSite);

            siteMapGen(depth, depthCounter, actualSite, actualSite);

            System.out.println("############ R E S U L T ##############");
            for (PageUrlText item : mapItems) {
                System.out.print(item);
            }
            Arrays.sort(mapItems);
            writeToFile(siteIterator);

            mapItems.clear();
        }
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

        System.out.println("rows: " + sheet.getPhysicalNumberOfRows()); //for test
        return sheet.iterator();

    }

    /**
     * Write the items of mapItems (String ArrayList) into an .rtf file.
     * Every sites will get an individual file, which only contains the actual site informations.
     * The name of file will be result-site-(and the indexnumber of the site).rtf
     *
     * @param siteIterator it is the indexnumber of sites
     */
    public static void writeToFile(int siteIterator) {
        FileWriter writer = null;
        try {
            writer = new FileWriter("C://test//result-site-" + siteIterator + ".rtf");
        } catch (IOException e) {
            e.printStackTrace();
        }


        for(PageUrlText item: mapItems) {
            try {
                writer.write(item.toString());
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

    public static void siteMapGen(int depth, int depthCounter, String actualSite, String homeUrl) {

        List<WebElement> actualWebElements = driver.findElements(By.tagName("a"));

        if (actualWebElements.size()==0) {
            return;
        }
        List<PageUrlText> actMapItems = new ArrayList<PageUrlText>();
        List<String> actMapItemsStr = new ArrayList<String>();
        List<String> actMapItemsUrlStr = new ArrayList<String>();

        for (WebElement element : actualWebElements) {
            if (element.getAttribute("href") != null && element.getText() != null && !element.getText().equals("") &&
                    !element.getAttribute("href").equals("#") && !mapItemsStr.contains(element.getText()) &&
                    !mapItemsUrlStr.contains(element.getAttribute("href")) && element.isDisplayed() &&
                    element.getAttribute("href").startsWith(homeUrl) &&
                    !element.getAttribute("href").equals(driver.getCurrentUrl())) {

                System.out.println(element.getText());  //for test.

                actMapItemsStr.add(element.getText());

                actMapItemsUrlStr.add(element.getAttribute("href"));
                actMapItems.add(new PageUrlText(element.getAttribute("href"), element.getText(), actualSite, depthCounter));
            }
        }

        if (actMapItemsStr.size()==0) {
            return;
        }

        mapItemsStr.addAll(actMapItemsStr);


        if (depth >= depthCounter+1) {
            for (PageUrlText element : actMapItems) {
                try {
                    driver.findElement(By.linkText(element.getText())).click();
                } catch (Exception e) {
                    System.out.println(element.getText() + " is not clickable yet. Continue to the next link...");
                    continue;
                }
//                try {
//                    Thread.sleep(1000);
//                    System.out.println("Waiting to load page...");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                new SitemapGenerator(driver).siteMapGen(depth, depthCounter + 1, driver.getCurrentUrl(), homeUrl);

                System.out.println("depthCounter: " + depthCounter); //for test.

                driver.navigate().back();

//                try {
//                    Thread.sleep(1000);
//                    System.out.println("Waiting to load previous page...");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    //    public static void siteMapGen(int depth, int depthCounter, String homeUrl) {
//
//        int actDepthCounter = depthCounter;
//        System.out.println("depthCounter: " + depthCounter); //for test
//
//        List<WebElement> actualWebElements = driver.findElements(By.tagName("a"));
//
//        for (WebElement element : actualWebElements) {
//            if (!mapItems.contains(element.getText()) && element.isDisplayed() &&
//                    element.getAttribute("href").startsWith(homeUrl)) {
//                for (int i = 0; i < actDepthCounter; i++) {
//                    mapItems.add("\t");
//                }
//                mapItems.add(element.getText());
//                mapItems.add("\n");
//                System.out.println(element.getText());  //for test
//                try {
//                    element.click();
//                } catch (Exception e) {
//                    continue;
//                }
//                new SitemapGenerator(driver).siteMapGen(depth, actDepthCounter+1, homeUrl);
//            }
//        }
//        driver.navigate().back();
//    }

}
