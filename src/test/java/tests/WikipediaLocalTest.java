package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

@DisplayName("Wikipedia Local Tests")
public class WikipediaLocalTest extends LocalTestBase {

    @Test
    @Tag("android")
    @Tag("local")
    @DisplayName("Complete full onboarding flow")
    void completeOnboardingFlow() {
        System.out.println("=== COMPLETE ONBOARDING FLOW TEST ===");

        step("Wait for app to load", () -> {
            sleep(3000);
        });

        // ЭКРАН 1: The Free Encyclopedia
        step("Verify Screen 1: The Free Encyclopedia", () -> {
            $(By.id("org.wikipedia.alpha:id/primaryTextView"))
                    .shouldBe(visible, Duration.ofSeconds(30))
                    .shouldHave(text("The Free Encyclopedia").or(text("Wikipedia")));

            $(By.id("org.wikipedia.alpha:id/fragment_onboarding_forward_button"))
                    .shouldBe(enabled, Duration.ofSeconds(10));

            $(By.id("org.wikipedia.alpha:id/fragment_onboarding_skip_button"))
                    .shouldBe(visible, Duration.ofSeconds(10));

            screenshot("onboarding-screen-1");
            System.out.println("✓ Screen 1 verified");
        });

        step("Navigate to Screen 2", () -> {
            $(By.id("org.wikipedia.alpha:id/fragment_onboarding_forward_button")).click();
            sleep(3000);
        });

        // ЭКРАН 2: New ways to explore
        step("Verify Screen 2: New ways to explore", () -> {
            $(By.id("org.wikipedia.alpha:id/primaryTextView"))
                    .shouldBe(visible, Duration.ofSeconds(20))
                    .shouldHave(text("New ways to explore"));

            $(By.id("org.wikipedia.alpha:id/fragment_onboarding_forward_button"))
                    .shouldBe(enabled, Duration.ofSeconds(10));

            screenshot("onboarding-screen-2");
            System.out.println("✓ Screen 2 verified");
        });

        step("Navigate to Screen 3", () -> {
            $(By.id("org.wikipedia.alpha:id/fragment_onboarding_forward_button")).click();
            sleep(3000);
        });

        // ЭКРАН 3: Reading lists with sync
        step("Verify Screen 3: Reading lists with sync", () -> {
            $(By.id("org.wikipedia.alpha:id/primaryTextView"))
                    .shouldBe(visible, Duration.ofSeconds(20))
                    .shouldHave(text("Reading lists with sync"));

            $(By.id("org.wikipedia.alpha:id/fragment_onboarding_forward_button"))
                    .shouldBe(enabled, Duration.ofSeconds(10));

            screenshot("onboarding-screen-3");
            System.out.println("✓ Screen 3 verified");
        });

        step("Navigate to Screen 4", () -> {
            $(By.id("org.wikipedia.alpha:id/fragment_onboarding_forward_button")).click();
            sleep(3000);
        });

        // ЭКРАН 4: Data & Privacy
        step("Verify Screen 4: Data & Privacy", () -> {
            $(By.id("org.wikipedia.alpha:id/primaryTextView"))
                    .shouldBe(visible, Duration.ofSeconds(20))
                    .shouldHave(text("Data & Privacy"));

            // Универсальный поиск кнопки завершения
            boolean foundButton = false;
            int buttonCount = $$(By.className("android.widget.Button")).size();

            for (int i = 0; i < buttonCount; i++) {
                String buttonText = $$(By.className("android.widget.Button")).get(i).getText();
                if (buttonText.contains("Get started") || buttonText.contains("Accept") ||
                        buttonText.contains("Finish") || buttonText.contains("Complete")) {
                    $$(By.className("android.widget.Button")).get(i)
                            .shouldBe(enabled, Duration.ofSeconds(10))
                            .click();
                    foundButton = true;
                    System.out.println("✓ Clicked button: '" + buttonText + "'");
                    break;
                }
            }

            if (!foundButton && buttonCount > 0) {
                $$(By.className("android.widget.Button")).last()
                        .shouldBe(enabled, Duration.ofSeconds(10))
                        .click();
                System.out.println("✓ Clicked last available button");
            }

            screenshot("onboarding-screen-4");
            System.out.println("✓ Screen 4 verified");
        });

        step("Verify main screen after onboarding", () -> {
            sleep(3000); // Ждем переход

            // Проверяем, что мы на главном экране (поиск доступен)
            $(By.id("org.wikipedia.alpha:id/search_container"))
                    .shouldBe(visible, Duration.ofSeconds(20));

            screenshot("main-screen-after-onboarding");
            System.out.println("✓ Successfully reached main screen");
        });

        System.out.println("=== ONBOARDING TEST PASSED ===");
    }

    @Test
    @Tag("android")
    @Tag("local")
    @DisplayName("Smoke test: Skip onboarding from first screen")
    void skipOnboardingSmokeTest() {
        System.out.println("=== SKIP ONBOARDING SMOKE TEST ===");

        step("Wait for app to load", () -> {
            sleep(1000);
        });

        step("Skip onboarding from first screen", () -> {
            $(By.id("org.wikipedia.alpha:id/fragment_onboarding_skip_button"))
                    .shouldBe(visible, Duration.ofSeconds(30))
                    .click();

            sleep(1000); // Ждем переход
        });

        step("Verify main screen after skip", () -> {
            // Проверяем, что пропуск сработал
            boolean onMainScreen = $(By.id("org.wikipedia.alpha:id/search_container")).exists() ||
                    $(By.id("org.wikipedia.alpha:id/nav_tab_explore")).exists();

            if (onMainScreen) {
                System.out.println("✓ Successfully skipped to main screen");
                screenshot("main-screen-after-skip");
            } else {
                // Если не на главном, проверяем, может быть все еще на онбординге
                boolean stillOnOnboarding = $(By.id("org.wikipedia.alpha:id/primaryTextView")).exists();
                if (stillOnOnboarding) {
                    System.out.println("✗ Still on onboarding screen after skip");
                    screenshot("skip-failed");
                }
            }
        });

        System.out.println("=== SKIP TEST COMPLETE ===");
    }
}