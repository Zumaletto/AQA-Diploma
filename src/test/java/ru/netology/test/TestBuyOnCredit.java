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
        //"Переключение между вкладками кнопками Купить и Купить в кредит"
    void shouldCheckTransitionToCard() {
        MainPage mainPage = new MainPage();
        mainPage.getPageByCard();
        BuyByCard buyByCard = new BuyByCard();
        buyByCard.transitionToCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.transitionToCard();
    }

    @Test
//"Успешная покупка тура картой со статусом APPROVED"
    void shouldBuyOnCreditApproved() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyOnCredit.getSuccessMessage();
        assertEquals(approvedCard.getStatus(), creditData().getStatus());
        assertEquals(creditData().getBank_id(), orderData().getPayment_id());
    }

    @Test
//"Покупка тура картой со статусом DECLINED"
    void shouldBuyOnCreditDecline() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getDeclinedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyOnCredit.getFailMessage();
        assertEquals(declinedCard.getStatus(), creditData().getStatus());
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Покупка тура с невалидным номером карты"
    void shouldBuyInvalidCardNumber() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getInvalidCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
        //"Отправка формы с пустым полем Номер карты"
    void shouldBuyEmptyCardNumber() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterWithOutNumber(
                DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Отправка формы с невалидным месяцем (однозначное числовое значение)"
    void shouldBuyInvalidMonth1() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getInvalidMonth1(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Отправка формы с невалидным месяцем (неверно указан срок действия карты)
    void shouldBuyInvalidMonth2() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getInvalidMonth2(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyOnCredit.invalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Отправка формы с пустым полем Месяц"
    void shouldBuyEmptyMonth() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterWithOutMonth(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Ввод нулевого значения в поле Месяц"
    void shouldBuyNullMonth() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getNullMonth(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Отправка формы с невалидным годом (однозначное числовое значение)"
    void shouldBuyInvalidYear() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getInvalidYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Отправка формы с невалидным годом (неверно указан срок действия карты)"
    void shouldBuyInvalidLastYear() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getInvalidLastYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyOnCredit.expiredError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Отправка формы с невалидным годом (неверно указан срок действия карты)"
    void shouldBuyFutureYear() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getInvalidFutureYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyOnCredit.invalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Отправка формы с пустым полем Год"
    void shouldBuyEmptyYear() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterWithOutYear(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyOnCredit.invalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Ввод нулевого значения в поле Год"
    void shouldBuyNullYear() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getNullYear(),
                DataHelper.getValidOwner(), DataHelper.getValidCvc());
        buyOnCredit.expiredError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();;
    }

    @Test
//"Отправка формы с невалидным данными владельца (значение набрано кириллицей)"
    void shouldBuyInvalidOwner1() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getInvalidOwnerCyrillic(), DataHelper.getValidCvc());
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Отправка формы с введеными в поле Владелец цифровых значений и математических символов"
    void shouldBuyInvalidOwner2() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getInvalidOwnerMathSymbols(), DataHelper.getValidCvc());
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Отправка формы с введеными в поле Владелец буквенных значений в нижнем и верхнем регистре"
    void shouldBuyByInvalidOwner3() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getInvalidOwnerRegister(), DataHelper.getValidCvc());
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Отправка формы с введеным в поле Владелец одного буквенного значения (минимальная длина поля)"
    void shouldBuyInvalidOwner4() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getInvalidOwnerUnderLength(), DataHelper.getValidCvc());
        buyOnCredit.emptyError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Отправка формы с введеными в поле Владелец 270 буквенных значений (максимальная длина поля)"
    void shouldBuyInvalidOwner5() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getInvalidOwnerOverLength(), DataHelper.getValidCvc());
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Отправка формы с пустым полем Владелец"
    void shouldBuyEmptyOwner() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterWithOutOwner(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(),
                DataHelper.getValidYear(), DataHelper.getValidCvc());
        buyOnCredit.emptyError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Отправка формы с невалидным CVC/CCV (однозначное числовое значение)"
    void shouldBuyInvalidCvc() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterCardData(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(),
                DataHelper.getValidOwner(), DataHelper.getInvalidCvc());
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }

    @Test
//"Отправка пустой формы CVC"
    void shouldBuyEmptyCvc() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.getPageOnCredit();
        BuyOnCredit buyOnCredit = new BuyOnCredit();
        buyOnCredit.enterWithOutCvc(
                DataHelper.getApprovedCardNumber(), DataHelper.getValidMonth(),
                DataHelper.getValidYear(), DataHelper.getValidOwner());
        buyOnCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
        checkEmptyCreditEntity();
    }
}
