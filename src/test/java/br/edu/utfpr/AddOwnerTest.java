package br.edu.utfpr;

import br.edu.utfpr.po.EditOwnerPage;
import br.edu.utfpr.po.FindOwnerPage;
import br.edu.utfpr.po.HomePage;
import br.edu.utfpr.po.OwnerInformationPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 *
 * @author andreendo
 */
public class AddOwnerTest {
    
    private WebDriver driver;
    
    @BeforeClass
    public static void beforeClass() {
        WebDriverManager.chromedriver().setup();
    }
    
    @Before
    public void before() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("headless");
        chromeOptions.addArguments("window-size=1200x600");
        chromeOptions.addArguments("start-maximized");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);     
    }
    
    @After
    public void after() {
        driver.close();
    }    
    
    @Test
    public void testSuccessfulOwnerInsertion() {
        HomePage homePage = new HomePage(driver);
        
        FindOwnerPage findOwnerPage = homePage.getMenu().goToFindOwners();
        EditOwnerPage editOwnerPage = findOwnerPage.goToAddOwner();
        assertEquals("Owner", editOwnerPage.getTitle());
        
        OwnerInformationPage ownerInfoPage = editOwnerPage.setFirstName("Joao")
                .setLastName("Da Silva")
                .setAddress("Rua das Acacias, 451")
                .setCity("Cornelio Procopio")
                .setTelephone("993335544")
                .addValidData();
        
        assertEquals("Owner Information", ownerInfoPage.getTitle());
        assertEquals("Joao Da Silva", ownerInfoPage.getName());
        assertEquals("Rua das Acacias, 451", ownerInfoPage.getAddress());
        assertEquals("Cornelio Procopio", ownerInfoPage.getCity());
        assertEquals("993335544", ownerInfoPage.getTelephone());
    }    
    
    @Test
    public void testErrorNoLastNameOwnerInsertion() {
        HomePage homePage = new HomePage(driver);
        FindOwnerPage findOwnerPage = homePage.getMenu().goToFindOwners();
        EditOwnerPage editOwnerPage = findOwnerPage.goToAddOwner();
        editOwnerPage.setFirstName("Jose")
                .setAddress("Rua das Acacias, 451")
                .setCity("Cornelio Procopio")
                .setTelephone("993335544")
                .addInvalidData();
        
        assertEquals(1, editOwnerPage.getNumberOfErrors());
        assertTrue(editOwnerPage.getErrorMessage(0).endsWith("pode estar vazio"));
    }
    
    @Test
    public void testErrorNoData() {
        HomePage homePage = new HomePage(driver);
        FindOwnerPage findOwnerPage = homePage.getMenu().goToFindOwners();
        EditOwnerPage editOwnerPage = findOwnerPage.goToAddOwner();
        editOwnerPage.addInvalidData();
        
        assertEquals(5, editOwnerPage.getNumberOfErrors());
    }    
}
