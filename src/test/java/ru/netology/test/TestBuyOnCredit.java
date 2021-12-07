package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.DbHelper;
import ru.netology.page.BuyByCard;
import ru.netology.page.BuyOnCredit;
import ru.netology.page.MainPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DbHelper.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestBuyOnCredit {

    DataHelper.CardNumber approvedCard = DataHelper.approvedCardInfo();
    DataHelper.CardNumber declinedCard = DataHelper.declinedCardInfo();

    @AfterEach
    public void cleanTables() {
        DbHelper.cleanData();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setupClass() {
        open("http://localhost:8080");
    }

    @Test
    @Order(1)
    @DisplayName("")//"Переключение между вкладками кнопками Купить и Купить в кредит"
    void shouldCheckTransitionToCard() {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.transitionToCredit();
        $$(".heading").findBy(text("Кредит по данным карты")).shouldBe(visible);
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.transitionToCard();
        $$(".heading").findBy(text("Оплата по карте")).shouldBe(visible);
    }

    @Test
    @Order(2)
    @DisplayName("")//"Успешная покупка тура картой со статусом APPROVED"
    void shouldBuyOnCreditApproved() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.validPaymentGate();
        buyOnCredit.getSuccessMessage();
        assertEquals(approvedCard.getStatus(), creditData().getStatus());
        assertEquals(creditData().getBank_id(), orderData().getPayment_id());
    }

    @Test
    @Order(3)
    @DisplayName("")//"Покупка тура картой со статусом DECLINED"
    void shouldBuyOnCreditDecline() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.invalidPaymentGate();
        buyOnCredit.getFailMessage();
        assertEquals(declinedCard.getStatus(), creditData().getStatus());
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(4)
    @DisplayName("")//"Покупка тура с невалидным номером карты"
    void shouldBuyOnCreditInvalidCardNumber() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.invalidNumberCard();
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(5)
    @DisplayName("")//"Отправка формы с пустым полем Номер карты"
    void shouldBuyOnCreditEmptyCardNumber() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.emptyNumberCard();
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(6)
    @DisplayName("")//"Отправка формы с невалидным месяцем (однозначное числовое значение)"
    void shouldBuyOnCreditInvalidMonth1() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.invalidMonth1();
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(7)
    @DisplayName("") //"Отправка формы с невалидным месяцем (неверно указан срок действия карты)
    void shouldBuyOnCreditInvalidMonth2() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.invalidMonth2();
        buyOnCredit.invalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(8)
    @DisplayName("")//"Отправка формы с пустым полем Месяц"
    void shouldBuyOnCreditEmptyMonth() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.emptyMonth();
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(9)
    @DisplayName("")//"Отправка формы с невалидным годом (однозначное числовое значение)"
    void shouldBuyOnCreditInvalidYear() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.invalidYear();
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(10)
    @DisplayName("")//"Отправка формы с невалидным годом (неверно указан срок действия карты)"
    void shouldBuyOnCreditInvalidLastYear() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.invalidLastYear();
        buyOnCredit.expiredError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(11)
    @DisplayName("")//"Отправка формы с невалидным годом (неверно указан срок действия карты)"
    void shouldBuyOnCreditFutureYear() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.invalidFutureYear();
        buyOnCredit.invalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(12)
    @DisplayName("")//"Отправка формы с пустым полем Год"
    void shouldBuyOnCreditEmptyYear() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.invalidFutureYear();
        buyOnCredit.invalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(13)
    @DisplayName("")//"Отправка формы с невалидным данными владельца (значение набрано кириллицей)"
    void shouldBuyOnCreditInvalidOwner1() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.invalidOwnerCyrillic();
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(14)
    @DisplayName("")//"Отправка формы с введеными в поле Владелец цифровых значений и математических символов"
    void shouldBuyOnCreditInvalidOwner2() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.invalidOwnerMathSymbols();
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(15)
    @DisplayName("")//"Отправка формы с введеными в поле Владелец буквенных значений в нижнем и верхнем регистре"
    void shouldBuyOnCreditInvalidOwner3() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.invalidOwnerOwnerRegister();
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(16)
    @DisplayName("")//"Отправка формы с введеным в поле Владелец одного буквенного значения (минимальная длина поля)"
    void shouldBuyOnCreditInvalidOwner4() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.invalidOwnerUnderLength();
        buyOnCredit.emptyError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(17)
    @DisplayName("")//"Отправка формы с введеными в поле Владелец 270 буквенных значений (максимальная длина поля)"
    void shouldBuyOnCreditInvalidOwner5() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.invalidOwnerOverLength();
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(18)
    @DisplayName("")//"Отправка формы с пустым полем Владелец"
    void shouldBuyOnCreditEmptyOwner() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.emptyOwner();
        buyOnCredit.emptyOwner();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(19)
    @DisplayName("")//"Отправка формы с невалидным CVC/CCV (однозначное числовое значение"
    void shouldBuyOnCreditInvalidCvc() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.invalidCVC();
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
    @Order(20)
    @DisplayName("")//"Отправка формы с невалидным CVC/CCV (однозначное числовое значение"
    void shouldBuyOnCreditEmptyCvc() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.emptyCVC();
        buyOnCredit.emptyError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }
}
