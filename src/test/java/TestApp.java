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
import org.openqa.selenium.support.ui.Select;

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
    private final String PHONE = "//input[contains(@data-bind,'Phone')]";

    private String lastName;
    private String firstName;
    private String middleName;
    private String Region;
    private String Phone;
    private String Email;
    private String Comment;


    public TestApp(String lName, String fName, String mName, String region, String phone, String email, String comment) {
        this.lastName = lName;
        this.firstName = fName;
        this.middleName = mName;
        this.Region = region;
        this.Phone = phone;
        this.Email = email;
        this.Comment = comment;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {"Иванов", "Иван", "Иванович", "Москва", "1234567890", "ivanov@ya.ru", "Любимый банк"},
                {"Семенов", "Семен", "Семенович", "Санкт-Петербург", "1234567890", "semenov@gmail.ru", "Так себе"},
                {"Петров", "Петр", "Петрович", "Еврейская АО", "1234567890", "petrov@.mail", "Есть и лучше"}};
        return Arrays.asList(data);
    }

    @BeforeClass
    public static void beforeTest() throws Exception {
        String object = System.getProperty("driver");
        switch (object) {
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
        driver.findElement(By.xpath("//select[contains(@name,'Region')]")).click();
        new Select(driver.findElement(By.name("Region"))).selectByVisibleText(Region);
        fillField(By.xpath(PHONE), Phone);
        fillField(By.name("Email"), Email);
        fillField(By.name("Comment"), Comment);
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);

        if (driver.findElement(By.xpath("//input[contains(@data-bind, 'checked')]")).isSelected()) {
            //driver.findElement(By.className("checkbox")).clear();
            driver.findElement(By.className("checkbox")).click();
            driver.manage().timeouts().implicitlyWait(2000, TimeUnit.SECONDS);
        }
        driver.findElement(By.className("checkbox")).click();
        boolean isChecked;
        isChecked = driver.findElement(By.className("checkbox")).isSelected();

        driver.findElement(By.id("button-m")).click();


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
