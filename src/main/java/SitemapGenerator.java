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

    static WebDriver driver = new ChromeDriver();

    static ArrayList<SiteInfos> mapItems = new ArrayList<SiteInfos>();

    static ArrayList<SiteInfos> sortedMapItems = new ArrayList<SiteInfos>();

    static ArrayList<String> mapItemsStr = new ArrayList<String>();

    static ArrayList<String> mapItemsUrlStr = new ArrayList<String>();

    public static void main(String[] args) {
        init();

        int siteIterator=0;

        Iterator<Row> Rows = readFromXlsxRows();
        while (Rows.hasNext()) {

            Row row = Rows.next();

            Iterator<Cell> cellIterator = row.cellIterator();

            String actualSite = cellIterator.next().toString();

            siteIterator++;

            int depth = (int)cellIterator.next().getNumericCellValue();
            int depthCounter = 1;

            driver.get(actualSite);

            mapItems.add(new SiteInfos(actualSite, "Homepage:", actualSite, 0 ));

            siteMapGen(depth, depthCounter, actualSite, actualSite);

            Collections.sort(mapItems);

            sortingMapItems();

            printResult();

            writeToFile(siteIterator);

            clearLists();
        }
    }

    /**
     * Settings for the chromedriver.
     */
    public static void init() {
        System.setProperty("webdriver.chrome.driver", "C://chromedriver.exe");
    }

    /**
     * Returns with the readable file. The .xlsx file should be on this location,
     * and with this name: C://test//sites.xlsx
     * You have to write the site url in the first, and the depth limit in the second
     * column.
     * @return file
     */
    public static FileInputStream fileOpen() {
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File("C://test//sites.xlsx"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Return with the rows of .xlsx document. First open the file,
     * then opens the sheet what we need to inspect, then it load the rows.
     * When
     * @return Row iterator
     */
    public static Iterator<Row> readFromXlsxRows() {
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(fileOpen());
        } catch (IOException e) {
            e.printStackTrace();
        }

        XSSFSheet sheet = workbook.getSheetAt(0);

        System.out.println("Number of sites in this file: " + sheet.getPhysicalNumberOfRows()); //for test
        return sheet.iterator();

    }

    /**
     * This is a recursive method, that provide the items for the ArrayLists.
     * First, it finds the elements by tagname, then collects the right element
     * informations (the urls, and Texts, and how deep is this element)
     * in the actual iteration. Then it check that we have not crossed the depth
     * limit. If it not true, they call this method.
     * @param depth This is the depth limit.
     * @param depthCounter this gave information about how deep are the actual iteration.
     * @param actualSite the current site, that runs on web browser.
     * @param homeUrl the homepage's url.
     */
    public static void siteMapGen(int depth, int depthCounter, String actualSite, String homeUrl) {

        List<WebElement> actualWebElements = driver.findElements(By.tagName("a"));

        if (actualWebElements.size()==0) {
            return;
        }

        ArrayList<SiteInfos> actMapItems = new ArrayList<SiteInfos>();
        ArrayList<String> actMapItemsStr = new ArrayList<String>();
        ArrayList<String> actMapItemsUrlStr = new ArrayList<String>();

        for (WebElement element : actualWebElements) {
            if (element.getAttribute("href") != null && element.getText() != null && !element.getText().equals("") &&
                    !element.getAttribute("href").equals("#") && !mapItemsStr.contains(element.getText()) &&
                    !mapItemsUrlStr.contains(element.getAttribute("href")) && element.isDisplayed() &&
                    element.getAttribute("href").startsWith(homeUrl) &&
                    !element.getAttribute("href").equals(driver.getCurrentUrl())) {

                System.out.println(element.getText());  //for test.

                actMapItemsStr.add(element.getText());

                actMapItemsUrlStr.add(element.getAttribute("href"));

                actMapItems.add(new SiteInfos(element.getAttribute("href"), element.getText(), actualSite, depthCounter));
            }
        }

        if (actMapItemsStr.size()==0) {
            return;
        }

        mapItemsStr.addAll(actMapItemsStr);
        mapItemsUrlStr.addAll(actMapItemsUrlStr);
        mapItems.addAll(actMapItems);

        if (depth >= depthCounter+1) {
            for (SiteInfos element : actMapItems) {
                try {
                    driver.findElement(By.linkText(element.getText())).click();
                } catch (Exception e) {
                    System.out.println(element.getText() + " is not clickable yet. Continue to the next link...");
                    continue;
                }

                siteMapGen(depth, depthCounter + 1, driver.getCurrentUrl(), homeUrl);

                driver.navigate().back();

            }
        }
    }

    /**
     * Sorting the items by depth and the connection of the urls.
     */
    public static void sortingMapItems() {

        for (SiteInfos item : mapItems) {

            sortedMapItems.add(item);

            for (SiteInfos innerItem : mapItems) {

                if (item.getUrl().equals(innerItem.getPreviousUrl()) && !item.equals(innerItem) && !sortedMapItems.contains(innerItem)) {
                    sortedMapItems.add(innerItem);
                }
            }
        }
    }

    /**
     * Writes the result to the console. (for testing)
     */
    public static void printResult() {
        System.out.println("############ R E S U L T ##############");

        for (SiteInfos item : sortedMapItems) {
            for (int i = 0; i < item.getDepth() ; i++) {
                System.out.print("\t");
            }
            System.out.print(item);
        }
        System.out.println("#######################################");
    }

    /**
     * Write the items of mapItems (SiteInfos ArrayList) into an .rtf file.
     * Every sites will get an individual file, which only contains the actual site informations.
     * The name of file will be: result-site-(and the indexnumber of the site).rtf
     *
     * @param siteIterator it is the indexnumber of sites.
     */
    public static void writeToFile(int siteIterator) {
        FileWriter writer = null;
        try {
            writer = new FileWriter("C://test//result-site-" + siteIterator + ".rtf");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(SiteInfos item: mapItems) {
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

    /**
     * It clears the String and the SiteInfos ArrayList.
     */
    public static void clearLists() {
        mapItems.clear();
        mapItemsStr.clear();
        mapItemsUrlStr.clear();
        sortedMapItems.clear();
    }

}
