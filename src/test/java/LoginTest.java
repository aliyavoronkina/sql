import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Condition;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.jupiter.api.*;

import java.sql.DriverManager;

import static com.codeborne.selenide.Selenide.*;

public class LoginTest {

    @BeforeAll
    static void setupAll() {
        Configuration.browser = "chrome";
        Configuration.timeout = 15000;

        // Очищаем данные перед запуском тестов (решение проблемы №2)
        clearDatabase();
    }

    @BeforeEach
    void setup() {
        // Дополнительная очистка перед каждым тестом
        clearAuthCodes();
    }

    private static void clearDatabase() {
        QueryRunner runner = new QueryRunner();
        try (var conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "appuser", "pass")) {

            runner.update(conn, "DELETE FROM card_transactions");
            runner.update(conn, "DELETE FROM auth_codes");
            runner.update(conn, "DELETE FROM cards");
            // НЕ очищаем users - они нужны из demo-data.sql
        } catch (Exception e) {
            System.out.println("Error clearing database: " + e.getMessage());
        }
    }

    private void clearAuthCodes() {
        QueryRunner runner = new QueryRunner();
        try (var conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "appuser", "pass")) {

            runner.update(conn, "DELETE FROM auth_codes");
        } catch (Exception e) {
            System.out.println("Error clearing auth codes: " + e.getMessage());
        }
    }

    private String getAuthCode() {
        QueryRunner runner = new QueryRunner();
        try (var conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "appuser", "pass")) {

            String code = runner.query(conn,
                    "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1",
                    new ScalarHandler<String>()
            );
            System.out.println("Retrieved auth code: " + code);
            return code;
        } catch (Exception e) {
            System.out.println("Error getting auth code: " + e.getMessage());
            return "000000"; // Заглушка для отладки
        }
    }

    @Test
    void shouldLoginWithValidCredentials() {
        open("http://localhost:9999");

        $("[data-test-id=login] input").setValue("vasya");
        $("[data-test-id=password] input").setValue("qwerty123");
        $("[data-test-id=action-login]").click();

        // Ждем появления поля для кода
        $("[data-test-id=code] input").shouldBe(Condition.visible, Condition.enabled);

        String verificationCode = getAuthCode();
        $("[data-test-id=code] input").setValue(verificationCode);
        $("[data-test-id=action-verify]").click();

        $("h2").shouldHave(Condition.text("Личный кабинет"));
    }

    @Test
    void shouldBlockAfterThreeInvalidPasswords() {
        open("http://localhost:9999");

        // Три неправильных пароля
        for (int i = 0; i < 3; i++) {
            $("[data-test-id=login] input").setValue("vasya");
            $("[data-test-id=password] input").setValue("wrong" + i);
            $("[data-test-id=action-login]").click();
            $("[data-test-id=error-notification]").shouldBe(Condition.visible);

            if (i < 2) {
                refresh();
                sleep(1000);
            }
        }

        // Четвертая попытка с правильным паролем - должна быть ошибка
        $("[data-test-id=login] input").setValue("vasya");
        $("[data-test-id=password] input").setValue("qwerty123");
        $("[data-test-id=action-login]").click();

        // Проверяем что все еще видим ошибку (не попали в личный кабинет)
        $("[data-test-id=error-notification]").shouldBe(Condition.visible);
    }
}