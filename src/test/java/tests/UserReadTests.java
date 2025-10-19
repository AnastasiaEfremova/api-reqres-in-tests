package tests;

import io.qameta.allure.*;
import models.getUsers.UserResponse;
import models.getUsers.UsersResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static api.UsersApiClient.getUser;
import static api.UsersApiClient.getUsers;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;
import static services.UserService.*;

@DisplayName("API тесты чтения данных пользователей")
@Epic("Пользователи")
@Feature("Получение информации о пользователях")
@Owner("API Automation Team")
@Tag("users")
@Tag("read")
@Tag("regression")
public class UserReadTests {

    @Test
    @DisplayName("Получение пагинированного списка пользователей")
    @Story("Пагинация данных")
    @Severity(SeverityLevel.CRITICAL)
    void getUsersWithPaginationTest() {
        UsersResponse response = step("Запрос второй страницы списка пользователей", () ->
                getUsers(2)
        );

        step("Валидация структуры пагинации", () -> {
            assertAll("Проверка метаданных пагинации",
                    () -> assertEquals(2, response.getPage(), "Номер страницы должен быть 2"),
                    () -> assertEquals(6, response.getPerPage(), "Количество элементов на странице должно быть 6"),
                    () -> assertEquals(12, response.getTotal(), "Общее количество пользователей должно быть 12"),
                    () -> assertEquals(2, response.getTotalPages(), "Общее количество страниц должно быть 2"),
                    () -> assertFalse(response.getData().isEmpty(), "Список пользователей не должен быть пустым"),
                    () -> assertEquals(6, response.getData().size(), "Количество пользователей на странице должно быть 6")
            );
        });
    }

    @ParameterizedTest
    @CsvSource({
            "1, George, Bluth",
            "2, Janet, Weaver",
            "3, Emma, Wong",
            "4, Eve, Holt",
            "5, Charles, Morris",
            "6, Tracey, Ramos"
    })
    @DisplayName("Параметризованное получение пользователей по идентификатору")
    @Story("Получение детальной информации")
    @Severity(SeverityLevel.NORMAL)
    void getUserByIdParameterizedTest(int userId, String expectedFirstName, String expectedLastName) {
        UserResponse response = step(String.format("Запрос пользователя с ID: %d", userId), () ->
                getUser(userId)
        );

        step(String.format("Валидация данных пользователя ID: %d", userId), () -> {
            assertAll("Комплексная проверка данных пользователя",
                    () -> assertEquals(userId, response.getData().getId(), "Идентификатор пользователя должен совпадать"),
                    () -> assertEquals(expectedFirstName, response.getData().getFirstName(), "Имя должно соответствовать"),
                    () -> assertEquals(expectedLastName, response.getData().getLastName(), "Фамилия должна соответствовать"),
                    () -> assertNotNull(response.getData().getEmail(), "Email должен присутствовать"),
                    () -> assertTrue(response.getData().getEmail().contains("@"), "Email должен содержать @"),
                    () -> assertNotNull(response.getData().getAvatar(), "Аватар должен присутствовать"),
                    () -> assertTrue(response.getData().getAvatar().startsWith("https://"), "Аватар должен быть валидной ссылкой"),
                    () -> assertNotNull(response.getSupport(), "Информация о поддержке должна присутствовать"),
                    () -> assertNotNull(response.getSupport().getUrl(), "URL поддержки должен присутствовать"),
                    () -> assertNotNull(response.getSupport().getText(), "Текст поддержки должен присутствовать")
            );
        });
    }

    @Test
    @DisplayName("Получение первой страницы пользователей")
    @Story("Пагинация данных")
    @Severity(SeverityLevel.NORMAL)
    void getFirstPageUsersTest() {
        UsersResponse response = step("Запрос первой страницы пользователей", () ->
                getUsers(1)
        );

        step("Валидация данных первой страницы", () -> {
            assertAll("Проверка первой страницы",
                    () -> assertEquals(1, response.getPage()),
                    () -> assertFalse(response.getData().isEmpty()),
                    () -> assertTrue(response.getData().size() <= 6)
            );
        });
    }
}