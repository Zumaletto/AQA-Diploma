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
public class TestBuyByCard {

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
        mainPage.getPageOnCredit();
        var buyOnCredit = new BuyOnCredit();
        buyOnCredit.transitionToCard();
        $$(".heading").findBy(text("Оплата по карте")).shouldBe(visible);
        var buyByCard = new BuyByCard();
        buyByCard.transitionToCredit();
        $$(".heading").findBy(text("Кредит по данным карты")).shouldBe(visible);
    }

    @Test
    @Order(2)
    @DisplayName("")//"Успешная покупка тура картой со статусом APPROVED"
    void shouldBuyByCardApproved() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.validPaymentGate();
        buyByCard.getSuccessMessage();
        assertEquals(approvedCard.getStatus(), payData().getStatus());
        assertEquals(payData().getTransaction_id(), orderData().getPayment_id());
    }

    @Test
    @Order(3)
    @DisplayName("")//"Покупка тура картой со статусом DECLINED"
    void shouldBuyByCardDecline() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.invalidPaymentGate();
        buyByCard.getFailMessage();
        assertEquals(declinedCard.getStatus(), payData().getStatus());
        checkEmptyOrderEntity();
    }

    @Test
    @Order(4)
    @DisplayName("")//"Покупка тура с невалидным номером карты"
    void shouldBuyByInvalidCardNumber() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.invalidNumberCard();
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(5)
    @DisplayName("")//"Отправка формы с пустым полем Номер карты"
    void shouldBuyByEmptyCardNumber() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.emptyNumberCard();
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(6)
    @DisplayName("")//"Отправка формы с невалидным месяцем (однозначное числовое значение)"
    void shouldBuyByInvalidMonth1() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.invalidMonth1();
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(7)
    @DisplayName("") //"Отправка формы с невалидным месяцем (неверно указан срок действия карты)
    void shouldBuyByInvalidMonth2() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.invalidMonth2();
        buyByCard.invalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(8)
    @DisplayName("")//"Отправка формы с пустым полем Месяц"
    void shouldBuyByEmptyMonth() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.emptyMonth();
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(9)
    @DisplayName("")//"Отправка формы с невалидным годом (однозначное числовое значение)"
    void shouldBuyByInvalidYear() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.invalidYear();
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(10)
    @DisplayName("")//"Отправка формы с невалидным годом (неверно указан срок действия карты)"
    void shouldBuyByInvalidLastYear() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.invalidLastYear();
        buyByCard.expiredError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(11)
    @DisplayName("")//"Отправка формы с невалидным годом (неверно указан срок действия карты)"
    void shouldBuyByFutureYear() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.invalidFutureYear();
        buyByCard.invalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(12)
    @DisplayName("")//"Отправка формы с пустым полем Год"
    void shouldBuyByEmptyYear() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.invalidFutureYear();
        buyByCard.invalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(13)
    @DisplayName("")//"Отправка формы с невалидным данными владельца (значение набрано кириллицей)"
    void shouldBuyByInvalidOwner1() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.invalidOwnerCyrillic();
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(14)
    @DisplayName("")//"Отправка формы с введеными в поле Владелец цифровых значений и математических символов"
    void shouldBuyByInvalidOwner2() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.invalidOwnerMathSymbols();
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(15)
    @DisplayName("")//"Отправка формы с введеными в поле Владелец буквенных значений в нижнем и верхнем регистре"
    void shouldBuyByInvalidOwner3() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.invalidOwnerOwnerRegister();
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(16)
    @DisplayName("")//"Отправка формы с введеным в поле Владелец одного буквенного значения (минимальная длина поля)"
    void shouldBuyByInvalidOwner4() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.invalidOwnerUnderLength();
        buyByCard.emptyError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(17)
    @DisplayName("")//"Отправка формы с введеными в поле Владелец 270 буквенных значений (максимальная длина поля)"
    void shouldBuyByInvalidOwner5() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.invalidOwnerOverLength();
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(18)
    @DisplayName("")//"Отправка формы с пустым полем Владелец"
    void shouldBuyByEmptyOwner() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.emptyOwner();
        buyByCard.emptyOwner();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(19)
    @DisplayName("")//"Отправка формы с невалидным CVC/CCV (однозначное числовое значение"
    void shouldBuyByInvalidCvc() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.invalidCVC();
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    @Order(20)
    @DisplayName("")//"Отправка формы с невалидным CVC/CCV (однозначное числовое значение"
    void shouldBuyByEmptyCvc() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.emptyCVC();
        buyByCard.emptyError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }
}
