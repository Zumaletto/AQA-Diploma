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
//"Переключение между вкладками кнопками Купить и Купить в кредит"
    void shouldCheckTransitionToCard() {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.transitionToCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.transitionToCredit();
    }

    @Test
//"Успешная покупка тура картой со статусом APPROVED"
    void shouldBuyByCardApproved() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyByCard.getSuccessMessage();
        assertEquals(approvedCard.getStatus(), payData().getStatus());
        assertEquals(payData().getTransaction_id(), orderData().getPayment_id());
    }

    @Test
//"Покупка тура картой со статусом DECLINED"
    void shouldBuyByCardDecline() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getDeclinedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyByCard.getFailMessage();
        assertEquals(declinedCard.getStatus(), payData().getStatus());
        checkEmptyOrderEntity();
    }

    @Test
//"Покупка тура с невалидным номером карты"
    void shouldBuyByInvalidCardNumber() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getInvalidCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
        //"Отправка формы с пустым полем Номер карты"
    void shouldBuyByEmptyCardNumber() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterWithOutNumber(
                DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с невалидным месяцем (однозначное числовое значение)"
    void shouldBuyByInvalidMonth1() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getInvalidMonth1(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с невалидным месяцем (неверно указан срок действия карты)
    void shouldBuyByInvalidMonth2() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getInvalidMonth2(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyByCard.invalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с пустым полем Месяц"
    void shouldBuyByEmptyMonth() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterWithOutMonth(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Ввод нулевого значения в поле Месяц"
    void shouldBuyByNullMonth() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getNullMonth(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с невалидным годом (однозначное числовое значение)"
    void shouldBuyByInvalidYear() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getInvalidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с невалидным годом (неверно указан срок действия карты)"
    void shouldBuyByInvalidLastYear() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getInvalidLastYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyByCard.expiredError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с невалидным годом (неверно указан срок действия карты)"
    void shouldBuyByFutureYear() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getInvalidFutureYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyByCard.invalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с пустым полем Год"
    void shouldBuyByEmptyYear() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterWithOutYear(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyByCard.invalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Ввод нулевого значения в поле Год"
    void shouldBuyByNullYear() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getNullYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyByCard.expiredError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с невалидным данными владельца (значение набрано кириллицей)"
    void shouldBuyByInvalidOwner1() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getInvalidOwnerCyrillic(), DataHelper.getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с введеными в поле Владелец цифровых значений и математических символов"
    void shouldBuyByInvalidOwner2() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getInvalidOwnerMathSymbols(), DataHelper.getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с введеными в поле Владелец буквенных значений в нижнем и верхнем регистре"
    void shouldBuyByInvalidOwner3() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getInvalidOwnerRegister(), DataHelper.getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с введеным в поле Владелец одного буквенного значения (минимальная длина поля)"
    void shouldBuyByInvalidOwner4() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getInvalidOwnerUnderLength(), DataHelper.getValidCvc());
        buyByCard.emptyError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с введеными в поле Владелец 270 буквенных значений (максимальная длина поля)"
    void shouldBuyByInvalidOwner5() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getInvalidOwnerOverLength(), DataHelper.getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с пустым полем Владелец"
    void shouldBuyByEmptyOwner() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterWithOutOwner(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(),
                DataHelper.getValidYear(),DataHelper.getValidCvc());
        buyByCard.emptyError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с невалидным CVC/CCV (однозначное числовое значение)"
    void shouldBuyByInvalidCvc() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getInvalidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка пустой формы CVC"
    void shouldBuyByEmptyCvc() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.enterWithOutCvc(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(),
                DataHelper.getValidYear(), DataHelper.getValidOwner());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }
}
