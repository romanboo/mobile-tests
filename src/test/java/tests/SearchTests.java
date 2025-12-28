package tests;

import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static io.appium.java_client.AppiumBy.*;
import static io.qameta.allure.Allure.step;

public class SearchTests extends TestBase {

    @Test
    void successfulSearchTest() {
        step("Type search", () -> {
            $(accessibilityId("Search Wikipedia")).click();
            $(id("org.wikipedia.alpha:id/search_src_text")).sendKeys("Appium");
        });
        step("Verify content found", () ->
                $$(id("org.wikipedia.alpha:id/page_list_item_title"))
                        .shouldHave(sizeGreaterThan(0)));
    }

    @Test
    void openArticleTest() {
        step("Search for article", () -> {
            $(accessibilityId("Search Wikipedia")).click();
            $(id("org.wikipedia.alpha:id/search_src_text")).sendKeys("Java");
        });

        step("Click on first article", () -> {
            $$(id("org.wikipedia.alpha:id/page_list_item_title"))
                    .first()
                    .click();
        });

        step("Verify article is opened", () -> {
            // Проверяем, что открылась страница статьи
            $(id("org.wikipedia.alpha:id/view_page_title_text"))
                    .shouldBe(visible);

            // Или проверяем наличие контента
            $(id("org.wikipedia.alpha:id/view_page_subtitle_text"))
                    .shouldBe(visible);

            // Можно добавить проверку навигации
            $(accessibilityId("Navigate up")).shouldBe(visible);
        });
    }
}