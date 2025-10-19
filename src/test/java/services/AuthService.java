package services;

import io.qameta.allure.Step;
import models.login.LoginRequest;
import models.login.LoginResponse;
import config.ReqresConfigProvider;
import models.ErrorResponse;

import static org.junit.jupiter.api.Assertions.*;
import static api.AuthApiClient.*;

public class AuthService {

    @Step("Успешная авторизация")
    public static LoginResponse successLogin() {
        LoginRequest request = new LoginRequest(
                ReqresConfigProvider.config.email(),
                ReqresConfigProvider.config.password()
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

    @Step("Попытка авторизации с неверными данными")
    public static ErrorResponse loginWithInvalidCredentials(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        ErrorResponse response = loginError(request);

        assertAll("Проверка ошибки авторизации",
                () -> assertNotNull(response.getError(), "Сообщение об ошибке должно присутствовать"),
                () -> assertFalse(response.getError().isEmpty(), "Сообщение об ошибке не должно быть пустым")
        );
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
}