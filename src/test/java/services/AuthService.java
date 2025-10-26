package services;

import io.qameta.allure.Step;
import models.auth.AuthRequest;
import models.auth.LoginResponse;
import config.ReqresConfigProvider;
import models.ErrorResponse;

import static org.junit.jupiter.api.Assertions.*;
import static api.AuthApiClient.*;

public class AuthService {

    @Step("Успешная авторизация")
    public static LoginResponse successLogin() {
        AuthRequest request = new AuthRequest(
                ReqresConfigProvider.config.email(),
                ReqresConfigProvider.config.password()
        );
        LoginResponse response = login(request);
        verifySuccessfulLogin(response);
        return response;
    }

    @Step("Авторизация с email: {email}")
    public static LoginResponse loginWithCredentials(String email, String password) {
        AuthRequest request = new AuthRequest(email, password);
        LoginResponse response = login(request);
        verifySuccessfulLogin(response);
        return response;
    }

    @Step("Попытка авторизации с неверными данными")
    public static ErrorResponse loginWithInvalidCredentials(String email, String password) {
        AuthRequest request = new AuthRequest(email, password);
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
        assertAll(
                () -> assertNotNull(response.getToken(), "Токен не должен быть null"),
                () -> assertEquals(expectedToken, response.getToken(), "Токен должен соответствовать"),
                () -> assertFalse(response.getToken().isEmpty(), "Токен не должен быть пустой строкой"),
                () -> assertTrue(response.getToken().trim().length() > 0, "Токен не должен состоять только из пробелов"),
                () -> assertTrue(response.getToken().length() >= 8, "Токен должен содержать минимум 8 символов"),
                () -> assertTrue(response.getToken().matches("^[a-zA-Z0-9]+$"), "Токен должен содержать только буквы и цифры"),
                () -> assertTrue(hasBothLettersAndNumbers(response.getToken()), "Токен должен содержать и буквы, и цифры")
        );
    }

    private static boolean hasBothLettersAndNumbers(String token) {
        boolean hasLetters = token.matches(".*[a-zA-Z].*");
        boolean hasNumbers = token.matches(".*\\d.*");
        return hasLetters && hasNumbers;
    }
}