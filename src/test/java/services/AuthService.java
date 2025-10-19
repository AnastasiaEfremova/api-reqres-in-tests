package services;

import io.qameta.allure.Step;
import models.login.LoginRequest;
import models.login.LoginResponse;
import config.ReqresConfigProvider;

import static org.junit.jupiter.api.Assertions.*;
import static api.AuthApiClient.login;

public class AuthService {

    @Step("Авторизация с пользователем по умолчанию")
    public static LoginResponse loginWithDefaultUser() {
        LoginRequest request = new LoginRequest(
                ReqresConfigProvider.config.defaultEmail(),
                ReqresConfigProvider.config.defaultPassword()
        );
        LoginResponse response = login(request);
        verifySuccessfulLogin(response);
        return response;
    }

    @Step("Авторизация с email: {email}")
    public static LoginResponse loginWithCredentials(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        LoginResponse response = login(request);
        verifySuccessfulLogin(response);
        return response;
    }

    @Step("Проверка успешной авторизации")
    public static void verifySuccessfulLogin(LoginResponse response) {
        assertAll("Проверка ответа авторизации",
                () -> assertNotNull(response.getToken(), "Токен не должен быть null"),
                () -> assertFalse(response.getToken().isEmpty(), "Токен не должен быть пустым")
        );
    }

    @Step("Проверка авторизации с конкретным токеном")
    public static void verifyLoginWithToken(LoginResponse response, String expectedToken) {
        assertAll("Проверка ответа авторизации с токеном",
                () -> assertNotNull(response.getToken(), "Токен не должен быть null"),
                () -> assertEquals(expectedToken, response.getToken(), "Токен должен соответствовать")
        );
    }

    @Step("Получение токена для API запросов")
    public static String getAuthToken() {
        return loginWithDefaultUser().getToken();
    }
}