package UserModel;

import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {
    public static String email = RandomStringUtils.randomAlphabetic(8);
    public static String password = RandomStringUtils.randomAlphabetic(8);
    public static String name = RandomStringUtils.randomAlphabetic(8);

    public static User getUser() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@ya.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);
        return new User(email, password, name);
    }

    public static User getFirstExistingUser() {
        return new User("existing_User", password, name);
    }
    public static User getSecondExistingUser() {
        return new User("existing_User", password+2, name +2);
    }

    public static User getUserWithoutEmail() {
        return new User("", password, name);
    }

    public static User getUserWithoutPassword() {
        return new User(email, "", name);
    }

    public static User getUserWithoutName() {
        return new User(email, password, "");
    }
}
