package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginField = $("[data-test-id=login] input");
    private final SelenideElement passwordField = $("[data-test-id=password] input");
    private final SelenideElement loginButton = $("[data-test-id=action-login]");
    private final SelenideElement errorText = $("[data-test-id=error-notification] .notification__content");

    public void login(String login, String password) {
        loginField.setValue(login);
        passwordField.setValue(password);
        loginButton.click();
    }

    public void shouldShowErrorNotification() {
        $("[data-test-id=error-notification]").shouldBe(visible);
    }

    public void shouldHaveErrorText(String expectedText) {
        errorText.shouldHave(text(expectedText));
    }

    public String getErrorText() {
        SelenideElement error = $("[data-test-id=error-notification] .notification__content");
        if (error.exists() && error.isDisplayed()) {
            return error.getText().trim();
        }

        error = $(".notification__content");
        if (error.exists() && error.isDisplayed()) {
            return error.getText().trim();
        }

        error = $("[data-test-id=error-notification]");
        if (error.exists() && error.isDisplayed()) {
            return error.getText().trim();
        }

        return "";
    }

    public boolean isErrorVisible() {
        return $("[data-test-id=error-notification]").isDisplayed();
    }
}