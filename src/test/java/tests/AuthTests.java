package tests;

import io.qameta.allure.*;
import models.login.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import services.AuthService;
import utils.TestDataGenerator;

import static io.qameta.allure.Allure.step;
import static services.AuthService.*;

@DisplayName("API тесты авторизации")
@Epic("Безопасность")
@Feature("Аутентификация пользователей")
@Owner("API Automation Team")
@Tag("auth")
@Tag("regression")
public class AuthTests {

    @Test
    @DisplayName("Успешная авторизация с пользователем по умолчанию")
    @Story("Позитивные сценарии авторизации")
    @Severity(SeverityLevel.CRITICAL)
    void successLoginWithDefaultUserTest() {
        LoginResponse response = step("Авторизация с предустановленными учетными данными",
                AuthService::loginWithDefaultUser
        );

        step("Валидация токена авторизации", () ->
                verifyLoginWithToken(response, "QpwL5tke4Pnpja7X4")
        );
    }

    @ParameterizedTest
    @CsvSource({
            "eve.holt@reqres.in, cityslicka",
            "emma.wong@reqres.in, cityslicka"
    })
    @DisplayName("Параметризованная авторизация различных пользователей")
    @Story("Позитивные сценарии авторизации")
    @Severity(SeverityLevel.CRITICAL)
    void loginWithDifferentUsersTest(String email, String password) {
        LoginResponse response = step("Аутентификация пользователя: " + email, () ->
                loginWithCredentials(email, password)
        );

        step("Проверка успешного ответа авторизации", () ->
                verifySuccessfulLogin(response)
        );
    }

    @Test
    @DisplayName("Попытка авторизации с неверными учетными данными")
    @Story("Негативные сценарии авторизации")
    @Severity(SeverityLevel.NORMAL)
    void loginWithInvalidCredentialsTest() {
        String randomEmail = TestDataGenerator.getRandomEmail();
        String randomPassword = TestDataGenerator.getRandomPassword();

        step("Попытка входа с сгенерированными неверными данными", () -> {
            // Здесь нужно добавить метод для обработки неуспешной авторизации
            // loginWithInvalidCredentials(randomEmail, randomPassword);
        });
    }

    @Test
    @DisplayName("Авторизация с некорректным форматом email")
    @Story("Валидация входных данных")
    @Severity(SeverityLevel.NORMAL)
    void loginWithInvalidEmailFormatTest() {
        String invalidEmail = TestDataGenerator.getInvalidEmail();

        step("Попытка авторизации с email: " + invalidEmail, () -> {
            // Обработка ошибки валидации
        });
    }
}