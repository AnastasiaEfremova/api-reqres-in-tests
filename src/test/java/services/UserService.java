package services;

import io.qameta.allure.Step;
import models.createUser.CreateUserRequest;
import models.createUser.CreateUserResponse;
import models.getUsers.UserResponse;
import models.getUsers.UsersResponse;
import models.updateUser.UpdateUserRequest;
import models.updateUser.UpdateUserResponse;

import static org.junit.jupiter.api.Assertions.*;
import static api.UsersApiClient.*;

public class UserService {

    @Step("Получение списка пользователей на странице {page}")
    public static UsersResponse getUsersList(int page) {
        UsersResponse response = getUsers(page);
        assertAll("Проверка структуры ответа списка пользователей",
                () -> assertNotNull(response.getPage(), "Page should not be null"),
                () -> assertFalse(response.getData().isEmpty(), "Users list should not be empty")
        );
        return response;
    }

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
        CreateUserRequest request = new CreateUserRequest(name, job);
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
        UpdateUserRequest request = new UpdateUserRequest(name, job);
        UpdateUserResponse response = updateUser(request, userId);

        assertAll("Проверка обновленного пользователя",
                () -> assertEquals(name, response.getName(), "Updated name should match"),
                () -> assertEquals(job, response.getJob(), "Updated job should match"),
                () -> assertNotNull(response.getUpdatedAt(), "Update date should be present")
        );
        return response;
    }

    @Step("Частичное обновление пользователя ID: {userId}")
    public static UpdateUserResponse patchUserById(int userId, String name) {
        UpdateUserRequest request = new UpdateUserRequest(name, null);
        UpdateUserResponse response = patchUser(request, userId);

        assertAll("Проверка частично обновленного пользователя",
                () -> assertEquals(name, response.getName(), "Patched name should match"),
                () -> assertNotNull(response.getUpdatedAt(), "Update date should be present")
        );
        return response;
    }

    @Step("Удаление пользователя ID: {userId}")
    public static void deleteUserById(int userId) {
        deleteUser(userId);
        // Для DELETE проверяем только статус код 204 через ResponseSpec
    }
}