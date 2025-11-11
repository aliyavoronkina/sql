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

        verificationPage.shouldShowCodeInput();

        String verificationCode = DatabaseHelper.getVerificationCode();
        verificationPage.verify(verificationCode);

        verificationPage.shouldShowPersonalAccount();
    }

    @Test
    void shouldShowErrorAfterInvalidPassword() {
        LoginPage loginPage = new LoginPage();

        loginPage.login(DataHelper.getValidLogin(), DataHelper.getInvalidPassword());

        loginPage.shouldShowErrorNotification();
        loginPage.shouldHaveErrorText("Ошибка! Неверно указан логин или пароль");
    }
}