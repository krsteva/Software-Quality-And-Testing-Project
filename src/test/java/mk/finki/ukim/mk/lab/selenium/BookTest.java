package mk.finki.ukim.mk.lab.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Profile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookTest {
    private static WebDriver driver;
    private static String baseUrl = "http://localhost:8080";

    @BeforeAll
    public static void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }
    @AfterAll
    public static void teardown() {
        driver.quit();
    }

    @Test
    @Order(1)
    public void testListBooks() {
        driver.get(baseUrl + "/books");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("table")));

        List<WebElement> rows = driver.findElements(By.cssSelector("tbody tr"));
        Assertions.assertTrue(rows.size() > 0, "Books should be listed in the table");

        WebElement firstRow = rows.get(0);
        Assertions.assertTrue(firstRow.findElement(By.cssSelector("td")).isDisplayed(),
                "Book details should be visible");
    }

    @Test
    @Order(2)
    public void testAddBook() {
        driver.get(baseUrl + "/books/add-form");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        String testTitle = "Test Book " + System.currentTimeMillis();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("title")))
                .sendKeys(testTitle);
        driver.findElement(By.name("isbn")).sendKeys("1234567890");
        driver.findElement(By.name("genre")).sendKeys("Test Genre");
        driver.findElement(By.name("year")).sendKeys("2024");

        Select bookstoreSelect = new Select(driver.findElement(By.name("bookStore")));
        bookstoreSelect.selectByIndex(0);

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlToBe(baseUrl + "/books"));
        List<WebElement> titles = driver.findElements(By.cssSelector("td"));
        boolean bookFound = titles.stream()
                .anyMatch(element -> element.getText().equals(testTitle));
        Assertions.assertTrue(bookFound, "Added book should appear in the list");
    }

    @Test
    @Order(3)
    public void testEditBook() {
        driver.get(baseUrl + "/books");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement editLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("a[id='edit-item']")));
        editLink.click();

        String updatedTitle = "Updated Book " + System.currentTimeMillis();
        WebElement titleInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.name("title")));
        titleInput.clear();
        titleInput.sendKeys(updatedTitle);

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlToBe(baseUrl + "/books"));
        List<WebElement> titles = driver.findElements(By.cssSelector("td"));
        boolean bookFound = titles.stream()
                .anyMatch(element -> element.getText().equals(updatedTitle));
        Assertions.assertTrue(bookFound, "Updated book title should appear in the list");
    }

    @Test
    @Order(4)
    public void testAddReview() {
        driver.get(baseUrl + "/books");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement addReviewLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.linkText("Add Review")));
        addReviewLink.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("description")))
                .sendKeys("Test Review Description");

        Select scoreSelect = new Select(driver.findElement(By.name("score")));
        scoreSelect.selectByValue("5");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        WebElement dateInput = driver.findElement(By.name("time"));
        dateInput.sendKeys(now.format(formatter));


        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlToBe(baseUrl + "/books"));
        Assertions.assertEquals(baseUrl + "/books", driver.getCurrentUrl(),
                "Should redirect to books page after adding review");
    }

    @Test
    @Order(5)
    public void testListReviews() {
        driver.get(baseUrl + "/books");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement showReviewLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.linkText("Show Review")));
        showReviewLink.click();

        List<WebElement> reviews = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(".prose p")));
        Assertions.assertTrue(reviews.size() > 0, "Reviews should be listed");
    }

    @Test
    @Order(6)
    public void testFilterReviews() {
        driver.get(baseUrl + "/books");
        driver.findElement(By.linkText("Show Review")).click();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAgo = now.minusWeeks(1);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        driver.findElement(By.name("from")).sendKeys(weekAgo.format(formatter));
        driver.findElement(By.name("to")).sendKeys(now.format(formatter));

        WebElement filterButton = driver.findElement(By.id("filterReviews"));
        filterButton.click();

        // Verify we're on the same page after filtering
        Assertions.assertTrue(driver.getCurrentUrl().contains("/review/search"));
    }

    @Test
    @Order(7)
    public void testDeleteBook() {
        driver.get(baseUrl + "/books");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        List<WebElement> beforeDelete = driver.findElements(By.cssSelector("tbody tr"));
        int initialCount = beforeDelete.size();

        WebElement deleteLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.linkText("Delete")));
        String bookTitle = deleteLink.findElement(By.xpath("./ancestor::tr/td[1]"))
                .getText();
        deleteLink.click();

        wait.until(ExpectedConditions.urlToBe(baseUrl + "/books"));
        List<WebElement> afterDelete = driver.findElements(By.cssSelector("tbody tr"));

        Assertions.assertEquals(initialCount - 1, afterDelete.size(),
                "Book count should decrease by 1");

        boolean bookStillExists = afterDelete.stream()
                .anyMatch(row -> row.findElement(By.cssSelector("td")).getText()
                        .equals(bookTitle));
        Assertions.assertFalse(bookStillExists, "Deleted book should not appear in list");
    }
}