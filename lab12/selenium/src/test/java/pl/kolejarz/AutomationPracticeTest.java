package pl.kolejarz;

import java.lang.*;
import java.util.regex.Pattern;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.Validate;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class AutomationPracticeTest {

  
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  String alp="ABCDEFGHIJKLMNOPRSTWZYS";
  private String generateRandomString(String alp)
  {
    Random rand=new Random();
    StringBuilder res=new StringBuilder();
    for (int i = 0; i < 17; i++) {
       int randIndex=rand.nextInt(alp.length()); 
       res.append(alp.charAt(randIndex));            
    }
    return res.toString();
  }
  @Before
  public void setUp() throws Exception {
  System.setProperty("webdriver.gecko.driver", "/home/kolejarz/Desktop/GEcko/geckodriver");
  driver = new FirefoxDriver();
	baseUrl = "http://automationpractice.com/";
	driver.manage().window().fullscreen();
  driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  driver.manage().deleteAllCookies();
  }

  @Test
  public void FailedRegisterTest() throws Exception {
    driver.get(baseUrl + "/index.php");
    driver.findElement(By.linkText("Sign in")).click();
    driver.findElement(By.id("email_create")).click();
    driver.findElement(By.id("email_create")).clear();
    driver.findElement(By.id("email_create")).sendKeys("marek.dee@interia.pl");
    driver.findElement(By.id("SubmitCreate")).click();
    driver.findElement(By.cssSelector("label.top")).click();
    driver.findElement(By.id("id_gender1")).click();
    driver.findElement(By.id("customer_firstname")).clear();
    driver.findElement(By.id("customer_firstname")).sendKeys("marek");
    driver.findElement(By.id("customer_lastname")).clear();
    driver.findElement(By.id("customer_lastname")).sendKeys("addd");
    driver.findElement(By.id("passwd")).clear();
    driver.findElement(By.id("passwd")).sendKeys("weqweq");
    driver.findElement(By.id("city")).clear();
    driver.findElement(By.id("city")).sendKeys("sdddd");
    driver.findElement(By.id("postcode")).clear();
    driver.findElement(By.id("postcode")).sendKeys("DFFDDF");
    driver.findElement(By.id("phone_mobile")).clear();
    driver.findElement(By.id("phone_mobile")).sendKeys("7651621433");
   // assertNull(driver.findElement(By.id("address1")).getAttribute("value"));
   // assertFalse(driver.findElement(By.cssSelector("#alert alert-danger")).isSelected());
    driver.findElement(By.id("submitAccount")).click();
    assertTrue(driver.findElement(By.cssSelector(".alert")).isDisplayed());

  }

  @Test
  public void ValidationTest() throws Exception {
    driver.get(baseUrl + "/index.php");
    driver.findElement(By.linkText("Sign in")).click();
    driver.findElement(By.id("email_create")).click();
    driver.findElement(By.id("email_create")).clear();
    driver.findElement(By.id("email_create")).sendKeys("loza@interia.pl");
    driver.findElement(By.id("SubmitCreate")).click();
    driver.findElement(By.id("customer_firstname")).clear();
    driver.findElement(By.id("customer_firstname")).click();
   
    driver.findElement(By.id("customer_lastname")).clear();
    driver.findElement(By.id("customer_lastname")).click();
    driver.findElement(By.id("passwd")).clear();
    driver.findElement(By.id("passwd")).sendKeys("");

    driver.findElement(By.id("address1")).clear();
    driver.findElement(By.id("address1")).click();
    assertTrue(driver.findElement(By.cssSelector("div.required:nth-child(3)")).isDisplayed());
    assertTrue(driver.findElement(By.cssSelector("div.required:nth-child(4)")).isDisplayed());
    assertTrue(driver.findElement(By.cssSelector("div.required:nth-child(6)")).isDisplayed());
    
    driver.findElement(By.id("address1")).sendKeys("Kolejarz 3/3");
    assertNotNull(driver.findElement(By.id("address1")).getAttribute("value"));
    assertEquals("Kolejarz 3/3", driver.findElement(By.id("address1")).getAttribute("value"));
    driver.findElement(By.id("city")).clear();
    driver.findElement(By.id("city")).sendKeys("New York");
    assertNotNull(driver.findElement(By.id("city")).getAttribute("value"));
    assertEquals("New York", driver.findElement(By.id("city")).getAttribute("value"));
    new Select(driver.findElement(By.id("id_state"))).selectByVisibleText("Arkansas");
    driver.findElement(By.cssSelector("#id_state > option[value=\"4\"]")).click();
    assertEquals("4", driver.findElement(By.id("id_state")).getAttribute("value"));
    driver.findElement(By.id("postcode")).clear();
    driver.findElement(By.id("postcode")).sendKeys("12345");
    assertEquals(5, driver.findElement(By.id("postcode")).getAttribute("value").length());
    assertEquals("12345", driver.findElement(By.id("postcode")).getAttribute("value"));
    driver.findElement(By.id("phone_mobile")).clear();
    driver.findElement(By.id("phone_mobile")).sendKeys("654765675");
    assertNotNull(driver.findElement(By.id("phone_mobile")).getAttribute("value"));
    assertEquals("654765675", driver.findElement(By.id("phone_mobile")).getAttribute("value"));
    assertNotNull(driver.findElement(By.id("alias")).getAttribute("value"));
    assertEquals("My address", driver.findElement(By.id("alias")).getAttribute("value"));
  }

  
  @Test
  public void SuccessfulRegisterTest() throws Exception {
    driver.get(baseUrl + "/index.php");
    driver.findElement(By.linkText("Sign in")).click();
    driver.findElement(By.id("columns")).click();
    driver.findElement(By.id("email_create")).clear();
    driver.findElement(By.id("email_create")).sendKeys(generateRandomString(alp)+"@interia.pl");
    driver.findElement(By.id("SubmitCreate")).click();
    driver.findElement(By.id("id_gender1")).click();
    driver.findElement(By.id("customer_firstname")).clear();
    driver.findElement(By.id("customer_firstname")).sendKeys("Paweł");
    driver.findElement(By.id("customer_lastname")).clear();
    driver.findElement(By.id("customer_lastname")).sendKeys("Dering");
    driver.findElement(By.id("passwd")).clear();
    driver.findElement(By.id("passwd")).sendKeys("dobreprogramy");
    // new Select(driver.findElement(By.id("days"))).selectByVisibleText("regexp:19\\s+");
    // driver.findElement(By.cssSelector("option[value=\"19\"]")).click();
    // new Select(driver.findElement(By.id("months"))).selectByVisibleText("regexp:October\\s");
    // driver.findElement(By.cssSelector("#months > option[value=\"10\"]")).click();
    // new Select(driver.findElement(By.id("years"))).selectByVisibleText("regexp:2006\\s+");
    // driver.findElement(By.cssSelector("option[value=\"2006\"]")).click();
    driver.findElement(By.id("optin")).click();
    driver.findElement(By.id("newsletter")).click();
    driver.findElement(By.id("address1")).clear();
    driver.findElement(By.id("address1")).sendKeys("Kolejarz 3/3");
    driver.findElement(By.id("city")).clear();
    driver.findElement(By.id("city")).sendKeys("New York");
    new Select(driver.findElement(By.id("id_state"))).selectByVisibleText("Arkansas");
    driver.findElement(By.cssSelector("#id_state > option[value=\"4\"]")).click();
    driver.findElement(By.id("postcode")).clear();
    driver.findElement(By.id("postcode")).sendKeys("12345");
    driver.findElement(By.id("phone_mobile")).clear();
    driver.findElement(By.id("phone_mobile")).sendKeys("654765675");
    driver.findElement(By.id("submitAccount")).click();
    assertEquals("Sign out",driver.findElement(By.linkText("Sign out")).getText());
    driver.findElement(By.linkText("Sign out")).click();  
    Thread.sleep(3000);
  }

  @After
  public void tearDown() throws Exception {
	driver.manage().deleteAllCookies();
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}