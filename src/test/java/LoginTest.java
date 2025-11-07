import helpers.DataHelper;
import helpers.DatabaseHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.VerificationPage;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class LoginTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @AfterEach
    void cleanup() {
        DatabaseHelper.clearAuthCodes();
    }

    @Test
    void shouldLoginWithValidCredentials() {
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = new VerificationPage();

        loginPage.login(DataHelper.getValidLogin(), DataHelper.getValidPassword());

        $("[data-test-id=code] input").shouldBe(visible, enabled);

        String verificationCode = DatabaseHelper.getVerificationCode();
        verificationPage.verify(verificationCode);

        $("h2").shouldHave(text("Личный кабинет"));
    }

    @Test
    void shouldBlockAfterThreeInvalidPasswords() {
        LoginPage loginPage = new LoginPage();

        // Используем DataHelper для неправильных паролей
        for (int i = 0; i < 3; i++) {
            loginPage.login(DataHelper.getValidLogin(), DataHelper.getInvalidPassword());
            $("[data-test-id=error-notification]").shouldBe(visible);

            if (i < 2) {
                refresh();
            }
        }

        loginPage.login(DataHelper.getValidLogin(), DataHelper.getValidPassword());
        $("[data-test-id=error-notification]").shouldBe(visible);
    }
}