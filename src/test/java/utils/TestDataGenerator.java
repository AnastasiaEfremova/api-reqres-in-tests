package utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

import java.util.Locale;

public class TestDataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));

    public static String getRandomEmail() {
        return faker.internet().emailAddress();
    }

    public static String getRandomPassword() {
        return faker.internet().password(8, 16, true, true, true);
    }

    public static String getRealisticName() {
        return faker.name().fullName();
    }

    public static String getRealisticJob() {
        return faker.job().position();
    }

    public static String getReqresLikeEmail() {
        return faker.name().firstName().toLowerCase() + "." +
                faker.name().lastName().toLowerCase() + "@reqres.in";
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserData {
        public String email;
        public String password;
    }

    public static UserData generateUserData() {
        return new UserData(getRandomEmail(), getRandomPassword());
    }
}