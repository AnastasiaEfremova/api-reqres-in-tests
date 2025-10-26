package tests;

import io.qameta.allure.*;
import models.auth.LoginResponse;
import models.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import services.AuthService;
import utils.TestDataGenerator;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static services.AuthService.*;

@DisplayName("API тесты авторизации")
@Epic("Безопасность")
@Feature("Аутентификация пользователей")
@Owner("efremovaa")
@Tag("auth")
@Tag("regression")
public class AuthTests {
    @Test
    @DisplayName("Успешная авторизация")
    @Story("Позитивные сценарии авторизации")
    @Severity(SeverityLevel.CRITICAL)
    void successLoginWithDefaultUserTest() {
        LoginResponse response = step("Авторизация с предустановленными учетными данными",
                AuthService::successLogin
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
    @DisplayName("Авторизация различных пользователей")
    @Story("Позитивные сценарии авторизации")
    @Severity(SeverityLevel.CRITICAL)
    void loginWithDifferentUsersTest(String email, String password) {
        LoginResponse response = step("Авторизация пользователя: " + email, () ->
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

        ErrorResponse response = step("Попытка входа с сгенерированными неверными данными", () ->
                AuthService.loginWithInvalidCredentials(randomEmail, randomPassword)
        );

        step("Проверка сообщения об ошибке", () ->
                assertEquals("user not found", response.getError().toLowerCase())
        );
    }

    @Test
    @DisplayName("Авторизация без пароля")
    @Story("Негативные сценарии авторизации")
    @Severity(SeverityLevel.NORMAL)
    void loginWithoutPasswordTest() {
        ErrorResponse response = step("Попытка авторизации без пароля", () ->
                AuthService.loginWithInvalidCredentials("test@example.com", "")
        );

        step("Проверка ошибки валидации", () ->
                assertEquals("Missing password", response.getError())
        );
    }
}