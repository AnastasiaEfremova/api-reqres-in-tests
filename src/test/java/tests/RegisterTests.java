package tests;

import io.qameta.allure.*;
import models.register.RegisterResponse;
import models.register.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.TestDataGenerator;

import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;
import static services.RegisterService.*;

@DisplayName("API тесты регистрации пользователей")
@Epic("Регистрация")
@Feature("Создание новых учетных записей")
@Owner("API Automation Team")
@Tag("registration")
@Tag("regression")
public class RegisterTests {

    @Test
    @DisplayName("Успешная регистрация предопределенного пользователя")
    @Story("Позитивные сценарии регистрации")
    @Severity(SeverityLevel.CRITICAL)
    void successRegisterTest() {
        RegisterResponse response = step("Регистрация валидного предопределенного пользователя", () ->
                registerSuccess()
        );

        step("Валидация структуры ответа успешной регистрации", () ->
                verifySuccessfulRegistrationStructure(response)
        );
    }

    @Test
    @DisplayName("Попытка регистрации сгенерированного пользователя")
    @Story("Негативные сценарии регистрации")
    @Severity(SeverityLevel.NORMAL)
    void registerWithGeneratedUserTest() {
        TestDataGenerator.UserData userData = TestDataGenerator.generateUserData();

        ErrorResponse response = step(
                String.format("Регистрация сгенерированного пользователя: %s", userData.email),
                () -> registerUnsuccessful(userData.email, userData.password)
        );

        step("Проверка стандартного сообщения об ошибке для неопределенных пользователей", () ->
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

        step("Валидация ошибки отсутствия пароля", () -> {
            assertEquals("Missing password", response.getError());
        });
    }

    @Test
    @DisplayName("Регистрация без обязательного email")
    @Story("Валидация обязательных полей")
    @Severity(SeverityLevel.CRITICAL)
    void registerWithoutEmailTest() {
        ErrorResponse response = step("Попытка регистрации без email", () ->
                registerWithoutEmail("ValidPass123!")
        );

        step("Валидация ошибки отсутствия email", () -> {
            assertEquals("Missing email or username", response.getError());
        });
    }

    @ParameterizedTest
    @MethodSource("validButUndefinedUserDataProvider")
    @DisplayName("Параметризованная проверка регистрации с валидными но неопределенными пользователями")
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

    @ParameterizedTest
    @MethodSource("invalidFormatDataProvider")
    @DisplayName("Параметризованная проверка регистрации с невалидным форматом данных")
    @Story("Валидация формата данных")
    @Severity(SeverityLevel.NORMAL)
    void registerWithInvalidFormatDataTest(String email, String password, String expectedError) {
        ErrorResponse response = step(
                String.format("Регистрация с email: %s", email),
                () -> registerUnsuccessful(email, password)
        );

        step("Проверка что ошибка содержит ожидаемый текст", () ->
                verifyErrorContains(response, expectedError)
        );
    }

    private static Stream<Object[]> invalidFormatDataProvider() {
        return Stream.of(
                new Object[]{"", "ValidPass123!", "Missing email"},
                new Object[]{"eve.holt@reqres.in", "", "Missing password"},
                new Object[]{"", "", "Missing email"}
        );
    }

    @Test
    @DisplayName("Массовая проверка регистрации случайных пользователей")
    @Story("Нагрузочное тестирование")
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