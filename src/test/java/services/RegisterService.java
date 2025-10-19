package services;

import io.qameta.allure.Step;
import models.register.RegisterRequest;
import models.register.RegisterResponse;
import models.ErrorResponse;

import static org.junit.jupiter.api.Assertions.*;
import static api.RegisterApiClient.*;

public class RegisterService {

    @Step("Успешная регистрация")
    public static RegisterResponse registerSuccess() {

        RegisterRequest request = new RegisterRequest("eve.holt@reqres.in", "pistol");
        RegisterResponse response = register(request);

        assertAll("Проверка успешной регистрации",
                () -> assertNotNull(response.getId(), "ID должен быть сгенерирован"),
                () -> assertNotNull(response.getToken(), "Token должен быть возвращен"),
                () -> assertEquals("4", response.getId(), "ID должен быть 4"),
                () -> assertEquals("QpwL5tke4Pnpja7X4", response.getToken(), "Token должен соответствовать")
        );
        return response;
    }

    @Step("Попытка регистрации с неопределенным пользователем")
    public static ErrorResponse registerUnsuccessful(String email, String password) {
        RegisterRequest request = new RegisterRequest(email, password);
        ErrorResponse response = registerError(request);

        assertAll("Проверка ошибки регистрации",
                () -> assertNotNull(response.getError(), "Сообщение об ошибке должно присутствовать"),
                () -> assertFalse(response.getError().isEmpty(), "Сообщение об ошибке не должно быть пустым")
        );
        return response;
    }

    @Step("Регистрация без пароля")
    public static ErrorResponse registerWithoutPassword(String email) {
        RegisterRequest request = new RegisterRequest(email, "");
        ErrorResponse response = registerError(request);

        assertAll("Проверка ошибки отсутствия пароля",
                () -> assertNotNull(response.getError(), "Сообщение об ошибке должно присутствовать"),
                () -> assertEquals("Missing password", response.getError(),
                        "Сообщение об ошибке должно указывать на отсутствие пароля")
        );
        return response;
    }

    @Step("Регистрация без email")
    public static ErrorResponse registerWithoutEmail(String password) {
        RegisterRequest request = new RegisterRequest("", password);
        ErrorResponse response = registerError(request);

        assertAll("Проверка ошибки отсутствия email",
                () -> assertNotNull(response.getError(), "Сообщение об ошибке должно присутствовать"),
                () -> assertEquals("Missing email or username", response.getError(),
                        "Сообщение об ошибке должно указывать на отсутствие email")
        );
        return response;
    }

    @Step("Проверка структуры успешного ответа регистрации")
    public static void verifySuccessfulRegistrationStructure(RegisterResponse response) {
        assertAll("Проверка структуры ответа",
                () -> assertNotNull(response.getId(), "ID не должен быть null"),
                () -> assertNotNull(response.getToken(), "Token не должен быть null"),
                () -> assertFalse(response.getId().isEmpty(), "ID не должен быть пустым"),
                () -> assertFalse(response.getToken().isEmpty(), "Token не должен быть пустым")
        );
    }

    @Step("Проверка что ошибка соответствует ожидаемой")
    public static void verifyErrorContains(ErrorResponse response, String expectedError) {
        assertTrue(response.getError().contains(expectedError),
                "Ошибка должна содержать: " + expectedError + ", но получили: " + response.getError());
    }

    @Step("Проверка стандартной ошибки для неопределенных пользователей")
    public static void verifyUndefinedUserError(ErrorResponse response) {
        assertEquals("Note: Only defined users succeed registration", response.getError(),
                "Для неопределенных пользователей должна быть стандартная ошибка");
    }
}