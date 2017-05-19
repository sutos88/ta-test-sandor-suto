import java.util.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


import java.util.List;

public class sitemapGenerator {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C://chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("http://gamestar.hu");

        List<WebElement> links = driver.findElements(By.tagName("a"));

        for (WebElement link : links){
            System.out.println("***");
            System.out.println(link.getText());
        }
    }
}
