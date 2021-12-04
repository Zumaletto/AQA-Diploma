package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class CardInfo {
        private String cardNumber;
        private String month;
        private String year;
        private String owner;
        private String cvc;
    }

    public static String getApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getInvalidCardNumber() {
        Faker faker = new Faker();
        return Long.toString(faker.number().randomNumber(7, true));
    }

    public static String getValidMonth() {
        String[] month = {"01", "02", "03", "04",
                "05", "06", "07", "08",
                "09", "10", "11", "12"};
        int rnd = new Random().nextInt(month.length);
        return month[rnd];
    }

    public static String getInvalidMonth1() {
        String[] month = {"1", "2", "3",
                "7", "8", "6"};
        int rnd = new Random().nextInt(month.length);
        return month[rnd];
    }

    public static String getInvalidMonth2() {
        String[] month = {"22", "32", "14",
                "13", "20", "15"};
        int rnd = new Random().nextInt(month.length);
        return month[rnd];
    }

    public static String getValidYear() {
        return LocalDate.now().plusYears(2).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getInvalidLastYear() {
        return LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getInvalidFutureYear() {
        return LocalDate.now().plusYears(10).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getInvalidYear() {
        String[] year = {"1", "2", "3", "7"};
        int rnd = new Random().nextInt(year.length);
        return year[rnd];
    }

    public static String getValidOwner() {
        Faker faker = new Faker(new Locale("en"));
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String getInvalidOwnerCyrillic() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String getInvalidOwnerMathSymbols() {
        String[] symbols = {"12-(*&^&3", "/18*@$%", "-4986$%*", "()89)&%%~~"};
        int rnd = new Random().nextInt(symbols.length);
        return symbols[rnd];
    }

    public static String getInvalidOwnerRegister() {
        String[] register = {"aNtonY kiEdis", "пЕтрОв АнТон", "WolLy HollY", "СиДоРов ВИктОР"};
        int rnd = new Random().nextInt(register.length);
        return register[rnd];
    }

    public static String getInvalidOwnerOverLength() {
        return "лывоаылдвлаопвлдаполвадопоплдфаоплофрадлфоаырвплдфырполРАдфаырлыораждывтаимлоистлдЫоващрыфлдваодлЫОВАОРЫЛОПРЛыорвадлфВАЫВАЫП";
    }

    public static String getInvalidOwnerUnderLength() {
        return "P";
    }

    public static String getValidCvc() {
        Faker faker = new Faker();
        String cvc = faker.numerify("###");
        return cvc;
    }

    public static String getInvalidCvc() {
        Faker faker = new Faker();
        String cvc = faker.numerify("#");
        return cvc;
    }


}
