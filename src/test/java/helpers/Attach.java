package helpers;

import io.qameta.allure.Attachment;

import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class Attach {
    @Attachment(value = "{attachName}", type = "image/png")
    public static byte[] screenshotAs(String attachName) {
        // Временно отключаем
        return new byte[0];
    }

    @Attachment(value = "Page source", type = "text/plain")
    public static byte[] pageSource() {
        // Временно отключаем
        return "".getBytes(StandardCharsets.UTF_8);
    }

    @Attachment(value = "{attachName}", type = "text/plain")
    public static String attachAsText(String attachName, String message) {
        return message;
    }

    @Attachment(value = "Video", type = "text/html", fileExtension = ".html")
    public static String addVideo(String sessionId) {
        // Временно отключаем видео
        return "<html><body>Video disabled for now</body></html>";

        // Для будущего использования (когда понадобится видео):
        // try {
        //     String videoUrl = Browserstack.videoUrl(sessionId);
        //     return "<html><body><video width='100%' height='100%' controls autoplay><source src='"
        //             + videoUrl
        //             + "' type='video/mp4'></video></body></html>";
        // } catch (Exception e) {
        //     return "<html><body>Error getting video: " + e.getMessage() + "</body></html>";
        // }
    }
}