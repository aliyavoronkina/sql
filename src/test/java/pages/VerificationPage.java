package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement codeField = $("[data-test-id=code] input");
    private final SelenideElement verifyButton = $("[data-test-id=action-verify]");

    public void verify(String code) {
        codeField.setValue(code);
        verifyButton.click();
    }

    public void shouldShowPersonalAccount() {
        $("h2").shouldHave(text("Личный кабинет"));
    }

    // ДОБАВЛЯЕМ ЭТОТ МЕТОД
    public void shouldShowCodeInput() {
        $("[data-test-id=code] input").shouldBe(visible, enabled);
    }
}