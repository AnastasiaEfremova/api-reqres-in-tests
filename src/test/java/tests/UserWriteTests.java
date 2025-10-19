package tests;

import io.qameta.allure.*;
import models.createUser.CreateUserResponse;
import models.updateUser.UpdateUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.TestDataGenerator;

import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;
import static services.UserService.*;

@DisplayName("API тесты управления пользователями")
@Epic("Пользователи")
@Feature("Операции создания, обновления и удаления")
@Owner("efremovaa")
@Tag("users")
@Tag("regression")
public class UserWriteTests {

    @Test
    @DisplayName("Создание пользователя со случайными данными")
    @Story("Создание пользователей")
    @Severity(SeverityLevel.CRITICAL)
    void createUserWithRealisticDataTest() {
        String name = TestDataGenerator.getRealisticName();
        String job = TestDataGenerator.getRealisticJob();

        CreateUserResponse response = step(
                String.format("Создание пользователя: %s - %s", name, job),
                () -> createNewUser(name, job)
        );

        step("Валидация созданного пользователя", () -> assertAll(
                () -> assertEquals(name, response.getName(), "Имя должно соответствовать отправленному"),
                () -> assertEquals(job, response.getJob(), "Должность должна соответствовать отправленной"),
                () -> assertNotNull(response.getId(), "ID должен быть сгенерирован"),
                () -> assertFalse(response.getId().isEmpty(), "ID не должен быть пустой строкой"),
                () -> assertNotNull(response.getCreatedAt(), "Дата создания должна присутствовать"),
                () -> assertTrue(response.getCreatedAt().contains("202"), "Дата должна содержать текущий год")
        ));
    }

    @ParameterizedTest
    @MethodSource("userDataProvider")
    @DisplayName("Создание пользователей с различными должностями")
    @Story("Создание пользователей")
    @Severity(SeverityLevel.NORMAL)
    void createUsersWithVariousJobsTest(String name, String job) {
        CreateUserResponse response = step(
                String.format("Создание %s на позиции %s", name, job),
                () -> createNewUser(name, job)
        );

        step("Валидация ответа", () -> assertAll("Проверка обязательных полей",
                () -> assertEquals(name, response.getName()),
                () -> assertEquals(job, response.getJob()),
                () -> assertNotNull(response.getId()),
                () -> assertNotNull(response.getCreatedAt())
        ));
    }

    private static Stream<Object[]> userDataProvider() {
        return Stream.of(
                new Object[]{TestDataGenerator.getRealisticName(), "Senior Software Engineer"},
                new Object[]{TestDataGenerator.getRealisticName(), "Product Manager"},
                new Object[]{TestDataGenerator.getRealisticName(), "QA Automation Engineer"},
                new Object[]{TestDataGenerator.getRealisticName(), "DevOps Specialist"},
                new Object[]{TestDataGenerator.getRealisticName(), "Data Scientist"}
        );
    }

    @Test
    @DisplayName("Полное обновление данных пользователя")
    @Story("Обновление пользователей")
    @Severity(SeverityLevel.CRITICAL)
    void completeUserUpdateTest() {
        String newName = TestDataGenerator.getRealisticName();
        String newJob = TestDataGenerator.getRealisticJob();

        UpdateUserResponse response = step(
                String.format("Полное обновление пользователя на: %s - %s", newName, newJob),
                () -> updateUserById(2, newName, newJob)
        );

        step("Валидация обновленных данных", () -> assertAll("Проверка ответа обновления",
                () -> assertEquals(newName, response.getName(), "Обновленное имя должно соответствовать"),
                () -> assertEquals(newJob, response.getJob(), "Обновленная должность должна соответствовать"),
                () -> assertNotNull(response.getUpdatedAt(), "Дата обновления должна присутствовать"),
                () -> assertTrue(response.getUpdatedAt().contains("202"), "Дата должна быть актуальной")
        ));
    }

    @Test
    @DisplayName("Частичное обновление имени пользователя")
    @Story("Обновление пользователей")
    @Severity(SeverityLevel.NORMAL)
    void partialUserNameUpdateTest() {
        String newName = TestDataGenerator.getRealisticName();

        UpdateUserResponse response = step(
                "Частичное обновление имени на: " + newName,
                () -> patchUserById(2, newName, null)
        );

        step("Валидация частичного обновления", () -> assertAll(
                () -> assertEquals(newName, response.getName(), "Имя должно быть обновлено"),
                () -> assertNotNull(response.getUpdatedAt(), "Дата обновления должна присутствовать")
        ));
    }

    @Test
    @DisplayName("Удаление пользователя")
    @Story("Удаление пользователей")
    @Severity(SeverityLevel.CRITICAL)
    void deleteUserTest() {
        step("Удаление пользователя с ID: 2", () ->
                deleteUserById(2)
        );

        step("Проверка что пользователь все еще доступен (mock-сервис)", () -> verifyUserExists(2));
    }

    @Test
    @DisplayName("Тест CRUD операций")
    @Story("CRUD")
    @Severity(SeverityLevel.CRITICAL)
    void comprehensiveCrudTest() {
        // 1. Create
        String name = TestDataGenerator.getRealisticName();
        String job = TestDataGenerator.getRealisticJob();
        CreateUserResponse createdUser = step("Создание пользователя", () ->
                createNewUser(name, job)
        );

        // 2. Verify creation
        step("Проверка создания", () -> {
            assertEquals(name, createdUser.getName());
            assertEquals(job, createdUser.getJob());
        });

        // 3. Update
        String updatedName = TestDataGenerator.getRealisticName();
        String updatedJob = TestDataGenerator.getRealisticJob();
        UpdateUserResponse updatedUser = step("Обновление пользователя", () ->
                updateUserById(2, updatedName, updatedJob)
        );

        // 4. Verify update
        step("Проверка обновления", () -> {
            assertEquals(updatedName, updatedUser.getName());
            assertEquals(updatedJob, updatedUser.getJob());
        });

        // 5. Delete
        step("Удаление пользователя", () ->
                deleteUserById(2)
        );
    }
}