package tests;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.appium.java_client.AppiumBy.*;
import static io.qameta.allure.Allure.step;

@DisplayName("Search tests (BrowserStack)")
public class SearchTests extends TestBase {

    @Test
    @Tag("android")
    @Tag("browserstack")
    @DisplayName("Search for Appium on BrowserStack")
    void searchForAppiumTest() {
        step("Skip onboarding", () -> {
            $(By.id("org.wikipedia.alpha:id/fragment_onboarding_skip_button"))
                    .shouldBe(visible, Duration.ofSeconds(5))
                    .click();
            Selenide.sleep(2000);
        });

        step("Open search", () -> {
            $(accessibilityId("Search Wikipedia")).click();
            Selenide.sleep(3000);
            Selenide.back();
            Selenide.sleep(1500);
        });

        step("Search for 'Appium'", () -> {
            $(By.id("org.wikipedia.alpha:id/search_src_text"))
                    .shouldBe(visible, Duration.ofSeconds(10))
                    .sendKeys("Appium");
            Selenide.sleep(2000);

            $$(By.id("org.wikipedia.alpha:id/page_list_item_title"))
                    .shouldHave(sizeGreaterThan(0));
        });
    }

    @Test
    @Tag("android")
    @Tag("browserstack")
    @DisplayName("Try to open article on BrowserStack")
    void openArticleTest() {
        step("Skip onboarding", () -> {
            $(By.id("org.wikipedia.alpha:id/fragment_onboarding_skip_button"))
                    .shouldBe(visible, Duration.ofSeconds(5))
                    .click();
            Selenide.sleep(2000);
        });

        step("Open search", () -> {
            $(accessibilityId("Search Wikipedia")).click();
            Selenide.sleep(3000);
            Selenide.back();
            Selenide.sleep(1500);
        });

        step("Search for 'Java'", () -> {
            $(By.id("org.wikipedia.alpha:id/search_src_text"))
                    .shouldBe(visible, Duration.ofSeconds(10))
                    .sendKeys("Java");
            Selenide.sleep(2000);

            int results = $$(By.id("org.wikipedia.alpha:id/page_list_item_title")).size();
            System.out.println("Found " + results + " search results");

            if (results > 0) {
                // Выводим текст первого результата
                String firstResult = $$(By.id("org.wikipedia.alpha:id/page_list_item_title"))
                        .first()
                        .getText();
                System.out.println("First result: " + firstResult);


                // Простой клик
                $$(By.id("org.wikipedia.alpha:id/page_list_item_title"))
                        .first()
                        .click();

                Selenide.sleep(5000);

                // Проверяем поле поиска
                boolean searchVisible = $(By.id("org.wikipedia.alpha:id/search_src_text")).isDisplayed();
                System.out.println("Search field visible: " + searchVisible);

                // Проверяем наличие других элементов
                int textViews = $$(By.className("android.widget.TextView")).size();
                System.out.println("TextViews found: " + textViews);

                if (!searchVisible) {
                    System.out.println("SUCCESS: Search field disappeared, article likely opened");
                } else {
                    System.out.println("FAIL: Search field still visible, article may not have opened");
                }
            }
        });
    }
}