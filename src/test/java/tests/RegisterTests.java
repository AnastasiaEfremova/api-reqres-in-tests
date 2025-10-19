package tests;

import io.qameta.allure.*;
import models.register.RegisterResponse;
import models.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import services.RegisterService;
import utils.TestDataGenerator;

import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;
import static services.RegisterService.*;

@DisplayName("API тесты регистрации пользователей")
@Epic("Регистрация")
@Feature("Создание новых учетных записей")
@Owner("efremovaa")
@Tag("registration")
@Tag("regression")
public class RegisterTests {

    @Test
    @DisplayName("Успешная регистрация пользователя")
    @Story("Позитивные сценарии регистрации")
    @Severity(SeverityLevel.CRITICAL)
    void successRegisterTest() {
        RegisterResponse response = step("Регистрация валидного пользователя", RegisterService::registerSuccess
        );

        step("Валидация структуры ответа успешной регистрации", () ->
                verifySuccessfulRegistrationStructure(response)
        );
    }

    @Test
    @DisplayName("Ошибка регистрации")
    @Story("Негативные сценарии регистрации")
    @Severity(SeverityLevel.NORMAL)
    void registerWithGeneratedUserTest() {

        TestDataGenerator.UserData userData = TestDataGenerator.generateUserData();

        ErrorResponse response = step(
                String.format("Регистрация сгенерированного пользователя: %s", userData.email),
                () -> registerUnsuccessful(userData.email, userData.password)
        );

        step("Проверка сообщения об ошибке регистрации", () ->
                verifyUndefinedUserError(response)
        );
    }

    @Test
    @DisplayName("Регистрация без обязательного пароля")
    @Story("Валидация обязательных полей")
    @Severity(SeverityLevel.CRITICAL)
    void registerWithoutPasswordTest() {
        ErrorResponse response = step("Попытка регистрации без пароля", () ->
                registerWithoutPassword("eve.holt@reqres.in")
        );

        step("Валидация ошибки отсутствия пароля", () -> assertEquals("Missing password", response.getError()));
    }

    @Test
    @DisplayName("Регистрация без обязательного email")
    @Story("Валидация обязательных полей")
    @Severity(SeverityLevel.CRITICAL)
    void registerWithoutEmailTest() {
        ErrorResponse response = step("Попытка регистрации без email", () ->
                registerWithoutEmail("ValidPass123!")
        );

        step("Валидация ошибки отсутствия email", () -> assertEquals("Missing email or username", response.getError()));
    }

    @ParameterizedTest
    @MethodSource("validButUndefinedUserDataProvider")
    @DisplayName("Регистрация с валидными, но неопределенными пользователями")
    @Story("Негативные сценарии регистрации")
    @Severity(SeverityLevel.NORMAL)
    void registerWithValidButUndefinedUsersTest(String email, String password, String testCase) {
        ErrorResponse response = step(
                String.format("Тест кейс: %s - Регистрация с email: %s", testCase, email),
                () -> registerUnsuccessful(email, password)
        );

        step("Проверка стандартной ошибки для неопределенных пользователей", () ->
                verifyUndefinedUserError(response)
        );
    }

    private static Stream<Object[]> validButUndefinedUserDataProvider() {
        return Stream.of(
                new Object[]{TestDataGenerator.getRandomEmail(), "StrongPass123!", "Случайный email"},
                new Object[]{TestDataGenerator.getReqresLikeEmail(), "Pass123!", "Email в домене reqres.in"},
                new Object[]{"test.user@example.com", "Password123", "Тестовый email"},
                new Object[]{TestDataGenerator.getRandomEmail().toUpperCase(), "Pass123!", "Email в верхнем регистре"}
        );
    }

    @Test
    @DisplayName("Ошибка регистрации случайных пользователей")
    @Story("Негативные сценарии регистрации")
    @Severity(SeverityLevel.MINOR)
    void bulkRegistrationAttemptTest() {
        step("Попытка регистрации 3 случайных пользователей", () -> {
            for (int i = 0; i < 3; i++) {
                TestDataGenerator.UserData user = TestDataGenerator.generateUserData();
                ErrorResponse response = registerUnsuccessful(user.email, user.password);
                verifyUndefinedUserError(response);
            }
        });
    }

    @Test
    @DisplayName("Регистрация с некорректным форматом email")
    @Story("Валидация формата данных")
    @Severity(SeverityLevel.MINOR)
    void registerWithInvalidEmailFormatTest() {
        ErrorResponse response = step("Попытка регистрации с некорректным email", () ->
                registerUnsuccessful("invalid-email", "password123")
        );

        step("Проверка ошибки", () ->
                verifyUndefinedUserError(response)
        );
    }
}