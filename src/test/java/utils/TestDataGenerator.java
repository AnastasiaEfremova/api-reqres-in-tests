package utils;

import net.datafaker.Faker;

import java.util.Locale;

public class TestDataGenerator {
    private static final Faker faker = new Faker(new Locale("ru"));

    // Генерация пользовательских данных
    public static String getRandomFirstName() {
        return faker.name().firstName();
    }

    public static String getRandomLastName() {
        return faker.name().lastName();
    }

    public static String getRandomJobTitle() {
        return faker.job().title();
    }

    public static String getRandomEmail() {
        return faker.internet().emailAddress();
    }

    public static String getRandomPassword() {
        return faker.internet().password(8, 16, true, true, true);
    }

    public static String getRandomCompany() {
        return faker.company().name();
    }

    public static String getRandomCity() {
        return faker.address().city();
    }

    public static String getRandomPhone() {
        return faker.phoneNumber().phoneNumber();
    }

    // Специфичные данные для API тестов
    public static String getRealisticName() {
        return faker.name().fullName();
    }

    public static String getRealisticJob() {
        return faker.job().position();
    }

    public static String getInvalidEmail() {
        // Используем safeEmail или username из имени
        return faker.name().firstName().toLowerCase() + "@invalid";
    }

    public static String getWeakPassword() {
        return "123";
    }

    public static String getEmptyEmail() {
        return "";
    }

    public static String getEmailWithoutAt() {
        return "invalid-email";
    }

    public static String getEmailWithoutDomain() {
        return "test@";
    }

    // Генерация тестовых данных для разных сценариев
    public static String getVeryLongName() {
        return faker.lorem().characters(100);
    }

    public static String getSpecialCharactersName() {
        return "User!@#$%^&*()";
    }

    public static String getNumericName() {
        return "12345";
    }

    // Генерация полного набора данных пользователя
    public static UserData generateUserData() {
        return new UserData(
                getRandomFirstName(),
                getRandomLastName(),
                getRandomEmail(),
                getRandomPassword(),
                getRandomJobTitle(),
                getRandomCompany()
        );
    }

    // Генерация данных для негативных тестов
    public static UserData generateInvalidUserData() {
        return new UserData(
                "", // пустое имя
                "", // пустая фамилия
                getInvalidEmail(),
                getWeakPassword(),
                "", // пустая должность
                ""  // пустая компания
        );
    }

    public static class UserData {
        public final String firstName;
        public final String lastName;
        public final String email;
        public final String password;
        public final String job;
        public final String company;

        public UserData(String firstName, String lastName, String email,
                        String password, String job, String company) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.job = job;
            this.company = company;
        }

        @Override
        public String toString() {
            return String.format(
                    "UserData{firstName='%s', lastName='%s', email='%s', job='%s'}",
                    firstName, lastName, email, job
            );
        }
    }

    // Дополнительные утилитные методы
    public static String generateRandomString(int length) {
        return faker.lorem().characters(length);
    }

    public static int generateRandomId() {
        return faker.number().numberBetween(1000, 9999);
    }

    public static String generateRandomAvatarUrl() {
        return "https://example.com/avatars/" + faker.internet().uuid() + ".jpg";
    }

    public static String getReqresLikePassword() {
        return faker.lorem().word();
    }

    // Генерация данных специфичных для Reqres API
    public static String getReqresLikeEmail() {
        return faker.name().firstName().toLowerCase() + "." +
                faker.name().lastName().toLowerCase() + "@reqres.in";
    }

    // Добавляем метод для генерации специальных тестовых случаев
    public static String getEmptyString() {
        return "";
    }

    public static String getVeryLongEmail() {
        return faker.lorem().characters(50) + "@example.com";
    }

    public static String getEmailWithSpecialChars() {
        return "test!" + faker.number().digits(3) + "@example.com";
    }
}