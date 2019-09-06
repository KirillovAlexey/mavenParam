import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;

@RunWith(value = Parameterized.class)
public class TestApp {
    private static WebDriver driver;
    private static String url;

    private static final String SAFE_SOUL = "//li[contains(@class, 'dropdown adv-analytics')]//a[contains(text(),'Меню')]";
    private static final String SEND = "//a[contains(text(), 'Отправить заявку')]";

    private String lastName;
    private String firstName;
    private String middleName;

    public TestApp(String lName, String fName, String mName) {
        this.lastName = lName;
        this.firstName = fName;
        this.middleName = mName;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{{"Иванов", "Иван", "Иванович"}, {"Семенов", "Семен", "Семенович"}, {"Петров", "Петр", "Петрович"},};
        return Arrays.asList(data);
    }

    @BeforeClass
    public static void beforeTest() throws Exception {
        String s = "ie";
        switch (s) {
            case "chrome": {
                System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
                driver = new ChromeDriver();
                break;
            }
            case "ff": {
                System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
                driver = new FirefoxDriver();
                break;
            }
            case "ie": {
                System.setProperty("webdriver.ie.driver", "src/main/resources/IEDriverServer.exe");
                driver = new InternetExplorerDriver();
                break;
            }
            default: {
                System.out.println("Браузер по умолчанию");
                System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
                driver = new FirefoxDriver();
                break;
            }
        }
        url = "https://www.rgs.ru/";
        driver.manage().window().maximize();
        driver.get(url);
        //(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(driver.findElement
        //(By.xpath(SAFE_SOUL)))).click();
        driver.findElement(By.xpath(SAFE_SOUL)).click();

        driver.findElement(By.linkText("ДМС")).click();
        driver.findElement(By.xpath(SEND)).click();
    }

    @Test
    public void TestRGS() throws Exception {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        fillField(By.name("LastName"), lastName);
        fillField(By.name("FirstName"), firstName);
        fillField(By.name("MiddleName"), middleName);

        assertEquals(lastName, driver.findElement(By.name("LastName")).getAttribute("value"));
        assertEquals(middleName, driver.findElement(By.name("MiddleName")).getAttribute("value"));
        assertEquals(firstName, driver.findElement(By.name("FirstName")).getAttribute("value"));
    }

    @AfterClass
    public static void afterTest() throws Exception {
        driver.quit();
    }

    private void fillField(By locator, String value) {
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(value);
    }
}
