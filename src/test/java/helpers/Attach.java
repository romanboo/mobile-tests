package helpers;

import io.qameta.allure.Attachment;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class Attach {

    @Attachment(value = "{attachName}", type = "image/png")
    public static byte[] screenshotAs(String attachName) {
        try {
            return ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            System.out.println("Failed to take screenshot: " + e.getMessage());
            return new byte[0];
        }
    }

    @Attachment(value = "Page source", type = "text/plain")
    public static byte[] pageSource() {
        try {
            return getWebDriver().getPageSource().getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Failed to get page source: " + e.getMessage());
            return "Failed to get page source".getBytes(StandardCharsets.UTF_8);
        }
    }

    @Attachment(value = "{attachName}", type = "text/plain")
    public static String attachAsText(String attachName, String message) {
        return message;
    }

    @Attachment(value = "Video", type = "text/html", fileExtension = ".html")
    public static String addVideo(String sessionId) {
        try {
            return "<html><body><video width='100%' height='100%' controls autoplay><source src='"
                    + Browserstack.videoUrl(sessionId)
                    + "' type='video/mp4'></video></body></html>";
        } catch (Exception e) {
            System.out.println("Failed to add video: " + e.getMessage());
            return "<html><body>Failed to get video URL</body></html>";
        }
    }

    @Attachment(value = "Element Tree (XML)", type = "text/xml")
    public static String getElementTree() {
        try {
            String pageSource = getWebDriver().getPageSource();
            System.out.println("Element tree XML length: " + pageSource.length());

            // Обрезаем слишком большие XML для удобства просмотра
            if (pageSource.length() > 10000) {
                pageSource = pageSource.substring(0, 10000) + "... [TRUNCATED]";
            }
            return pageSource;
        } catch (Exception e) {
            return "Failed to get element tree: " + e.getMessage();
        }
    }

    @Attachment(value = "Current Activity", type = "text/plain")
    public static String getCurrentActivity() {
        try {
            // Для Android можно получить текущую активность
            if (getWebDriver() instanceof RemoteWebDriver) {
                RemoteWebDriver driver = (RemoteWebDriver) getWebDriver();
                Object activity = driver.executeScript("mobile: getCurrentActivity");
                return "Current Activity: " + (activity != null ? activity.toString() : "null");
            }
            return "Not an Android driver or activity not available";
        } catch (Exception e) {
            return "Failed to get current activity: " + e.getMessage();
        }
    }

    @Attachment(value = "Logcat Output", type = "text/plain")
    public static String getLogcat() {
        try {
            if (getWebDriver() instanceof RemoteWebDriver) {
                RemoteWebDriver driver = (RemoteWebDriver) getWebDriver();
                Object logcat = driver.executeScript("mobile: getLogs", java.util.Map.of("type", "logcat"));
                return "Logcat: " + (logcat != null ? logcat.toString() : "null");
            }
            return "Not an Android driver or logcat not available";
        } catch (Exception e) {
            return "Failed to get logcat: " + e.getMessage();
        }
    }

    @Attachment(value = "Session Info", type = "text/plain")
    public static String getSessionInfo() {
        try {
            if (getWebDriver() instanceof RemoteWebDriver) {
                RemoteWebDriver driver = (RemoteWebDriver) getWebDriver();
                StringBuilder info = new StringBuilder();
                info.append("Session ID: ").append(driver.getSessionId()).append("\n");

                // Получаем capabilities
                Capabilities caps = driver.getCapabilities();
                info.append("Capabilities: ").append(caps).append("\n");

                info.append("Current URL: ").append(driver.getCurrentUrl()).append("\n");
                info.append("Window Handle: ").append(driver.getWindowHandle()).append("\n");
                return info.toString();
            }
            return "Not a RemoteWebDriver instance";
        } catch (Exception e) {
            return "Failed to get session info: " + e.getMessage();
        }
    }

    @Attachment(value = "All Screenshots", type = "image/png")
    public static byte[] takeScreenshotWithRetry(String attachName) {
        // Пытаемся сделать скриншот несколько раз
        for (int i = 0; i < 3; i++) {
            try {
                System.out.println("Attempt " + (i + 1) + " to take screenshot: " + attachName);
                byte[] screenshot = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
                if (screenshot.length > 0) {
                    System.out.println("Screenshot taken successfully, size: " + screenshot.length + " bytes");
                    return screenshot;
                }
            } catch (Exception e) {
                System.out.println("Screenshot attempt " + (i + 1) + " failed: " + e.getMessage());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        System.out.println("All screenshot attempts failed");
        return new byte[0];
    }

    @Attachment(value = "Device Info", type = "text/plain")
    public static String getDeviceInfo() {
        try {
            if (getWebDriver() instanceof RemoteWebDriver) {
                RemoteWebDriver driver = (RemoteWebDriver) getWebDriver();
                StringBuilder info = new StringBuilder();

                // Получаем device info через capabilities
                Capabilities caps = driver.getCapabilities();

                info.append("Platform Name: ").append(caps.getPlatformName()).append("\n");
                info.append("Browser Name: ").append(caps.getBrowserName()).append("\n");
                info.append("Browser Version: ").append(caps.getBrowserVersion()).append("\n");

                // Appium-specific capabilities
                info.append("Device Name: ").append(caps.getCapability("deviceName")).append("\n");
                info.append("Platform Version: ").append(caps.getCapability("platformVersion")).append("\n");
                info.append("App Package: ").append(caps.getCapability("appPackage")).append("\n");
                info.append("App Activity: ").append(caps.getCapability("appActivity")).append("\n");
                info.append("Automation Name: ").append(caps.getCapability("automationName")).append("\n");

                return info.toString();
            }
            return "Not a RemoteWebDriver instance";
        } catch (Exception e) {
            return "Failed to get device info: " + e.getMessage();
        }
    }

    @Attachment(value = "Screen Dimensions", type = "text/plain")
    public static String getScreenDimensions() {
        try {
            if (getWebDriver() instanceof RemoteWebDriver) {
                RemoteWebDriver driver = (RemoteWebDriver) getWebDriver();

                // Получаем размеры экрана через JavaScript
                Object screenSize = driver.executeScript("mobile: getDeviceSize");
                if (screenSize != null) {
                    return "Screen Size: " + screenSize.toString();
                }

                // Альтернативный способ
                Object windowSize = driver.executeScript("mobile: getWindowSize");
                if (windowSize != null) {
                    return "Window Size: " + windowSize.toString();
                }

                return "Screen dimensions not available";
            }
            return "Not a RemoteWebDriver instance";
        } catch (Exception e) {
            return "Failed to get screen dimensions: " + e.getMessage();
        }
    }
}