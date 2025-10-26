package services;

import io.qameta.allure.Step;
import models.users.CreateUserResponse;
import models.users.UserRequest;
import models.users.UserResponse;
import models.users.UpdateUserResponse;

import static org.junit.jupiter.api.Assertions.*;
import static api.UsersApiClient.*;

public class UserService {

    @Step("Получение пользователя по ID: {userId}")
    public static UserResponse getUserById(int userId) {
        UserResponse response = getUser(userId);
        assertAll("Проверка структуры ответа пользователя",
                () -> assertNotNull(response.getData(), "User data should not be null"),
                () -> assertEquals(userId, response.getData().getId(), "User ID should match")
        );
        return response;
    }

    @Step("Создание пользователя с именем: {name} и должностью: {job}")
    public static CreateUserResponse createNewUser(String name, String job) {
        UserRequest request = new UserRequest(name, job);
        CreateUserResponse response = createUser(request);

        assertAll("Проверка созданного пользователя",
                () -> assertEquals(name, response.getName(), "Name should match"),
                () -> assertEquals(job, response.getJob(), "Job should match"),
                () -> assertNotNull(response.getId(), "ID should be generated"),
                () -> assertNotNull(response.getCreatedAt(), "Creation date should be present")
        );
        return response;
    }

    @Step("Полное обновление пользователя ID: {userId}")
    public static UpdateUserResponse updateUserById(int userId, String name, String job) {
        UserRequest request = new UserRequest(name, job);
        UpdateUserResponse response = updateUser(request, userId);

        assertAll("Проверка обновленного пользователя",
                () -> assertEquals(name, response.getName(), "Updated name should match"),
                () -> assertEquals(job, response.getJob(), "Updated job should match"),
                () -> assertNotNull(response.getUpdatedAt(), "Update date should be present")
        );
        return response;
    }

    @Step("Частичное обновление пользователя ID: {userId}")
    public static UpdateUserResponse patchUserById(int userId, String name, String job) {
        UserRequest request = new UserRequest(name, job);
        UpdateUserResponse response = patchUser(request, userId);

        assertAll("Проверка частично обновленного пользователя",
                () -> assertNotNull(response.getUpdatedAt(), "Update date should be present")
        );
        if (name != null) {
            assertEquals(name, response.getName(), "Patched name should match");
        }
        if (job != null) {
            assertEquals(job, response.getJob(), "Patched job should match");
        }
        return response;
    }

    @Step("Удаление пользователя ID: {userId}")
    public static void deleteUserById(int userId) {
        deleteUser(userId);
    }

    @Step("Проверка что пользователь существует")
    public static void verifyUserExists(int userId) {
        UserResponse response = getUserById(userId);
        assertAll("Проверка существования пользователя",
                () -> assertNotNull(response.getData(), "User data should exist"),
                () -> assertEquals(userId, response.getData().getId(), "User ID should match")
        );
    }
}