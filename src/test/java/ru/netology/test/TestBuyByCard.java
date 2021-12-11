package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.DbHelper;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.DbHelper.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestBuyByCard {

    DataHelper.CardNumber approvedCard = DataHelper.approvedCardInfo();
    DataHelper.CardNumber declinedCard = DataHelper.declinedCardInfo();

    @BeforeEach
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
        var mainPage = new MainPage();
        var buyOnCredit = mainPage.getPageOnCredit();
        var buyByCard = buyOnCredit.transitionToCard();
        buyByCard.transitionToCredit();
    }

    @Test
//"Успешная покупка тура картой со статусом APPROVED"
    void shouldBuyByApprovedCard() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getValidMonth(), getValidYear(),
                getValidOwner(), getValidCvc());
        buyByCard.getSuccessMessage();
        assertEquals(approvedCard.getStatus(), payData().getStatus());
        assertEquals(payData().getTransaction_id(), orderData().getPayment_id());
    }

    @Test
//"Покупка тура картой со статусом DECLINED"
    void shouldBuyByDeclineCard() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getDeclinedCardNumber(), getValidMonth(), getValidYear(),
                getValidOwner(), getValidCvc());
        buyByCard.getFailMessage();
        assertEquals(declinedCard.getStatus(), payData().getStatus());
        checkEmptyOrderEntity();
    }

    @Test
//"Покупка тура с невалидным номером карты"
    void shouldSendFormWithInvalidCardNumber() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getInvalidCardNumber(), getValidMonth(), getValidYear(),
                getValidOwner(), getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
        //"Отправка формы с пустым полем Номер карты"
    void shouldSendFormWithoutCardNumber() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getEmptyCardNumber(), getValidMonth(), getValidYear(),
                getValidOwner(), getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с невалидным месяцем (однозначное числовое значение)"
    void shouldSendFormWithInvalidMonth1() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getInvalidMonth1(), getValidYear(),
                getValidOwner(), getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с невалидным месяцем (неверно указан срок действия карты)
    void shouldSendFormWithInvalidMonth2() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getInvalidMonth2(), getValidYear(),
                getValidOwner(), getValidCvc());
        buyByCard.invalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с пустым полем Месяц"
    void shouldSendFormWithoutMonth() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getEmptyMonth(), getValidYear(),
                getValidOwner(), getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Ввод нулевого значения в поле Месяц"
    void shouldSendFormWithNullMonth() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getNullMonth(), getValidYear(),
                getValidOwner(), getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с невалидным годом (однозначное числовое значение)"
    void shouldSendFormWithInvalidCardTerm1() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getValidMonth(), getInvalidYear(),
                getValidOwner(), getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с невалидным годом (неверно указан срок действия карты)"
    void shouldSendFormWithInvalidCardTerm2() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getValidMonth(), getInvalidLastYear(),
                getValidOwner(), getValidCvc());
        buyByCard.expiredError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с невалидным годом (неверно указан срок действия карты)"
    void shouldSendFormWithInvalidCardTerm3() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getValidMonth(), getInvalidFutureYear(),
                getValidOwner(), getValidCvc());
        buyByCard.invalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с пустым полем Год"
    void shouldSendFormWithoutYear() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getValidMonth(), getEmptyYear(),
                getValidOwner(), getValidCvc());
        buyByCard.invalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Ввод нулевого значения в поле Год"
    void shouldSendFormWithNullYear() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getValidMonth(), getNullYear(),
                getValidOwner(), getValidCvc());
        buyByCard.expiredError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с невалидным данными владельца (значение набрано кириллицей)"
    void shouldSendFormWithInvalidOwner1() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getValidMonth(), getValidYear(),
                getInvalidOwnerCyrillic(), getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с введеными в поле Владелец цифровых значений и математических символов"
    void shouldSendFormWithInvalidOwner2() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getValidMonth(), getValidYear(),
                getInvalidOwnerMathSymbols(), getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с введеными в поле Владелец буквенных значений в нижнем и верхнем регистре"
    void shouldSendFormWithInvalidOwner3() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getValidMonth(), getValidYear(),
                getInvalidOwnerRegister(), getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с введеным в поле Владелец одного буквенного значения (минимальная длина поля)"
    void shouldSendFormWithInvalidOwner4() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getValidMonth(), getValidYear(),
                getInvalidOwnerUnderLength(), getValidCvc());
        buyByCard.emptyError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с введеными в поле Владелец 270 буквенных значений (максимальная длина поля)"
    void shouldSendFormWithInvalidOwner5() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getValidMonth(), getValidYear(),
                getInvalidOwnerOverLength(), getValidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с пустым полем Владелец"
    void shouldSendFormWithoutOwner() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getValidMonth(), getValidYear(),
                getEmptyOwner(), getValidCvc());
        buyByCard.emptyError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с невалидным CVC/CCV (однозначное числовое значение)"
    void shouldSendFormWithInvalidCvc() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getValidMonth(), getValidYear(),
                getValidOwner(), getInvalidCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка формы с невалидным CVC/CCV (проверка на "000")"
    void shouldSendFormWithNullCvc() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getValidMonth(), getValidYear(),
                getValidOwner(), getNullCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
//"Отправка пустой формы CVC"
    void shouldSendFormWithoutEmptyCvc() {
        var mainPage = new MainPage();
        var buyByCard = mainPage.getPageByCard();
        buyByCard.enterCardData(
                getApprovedCardNumber(), getValidMonth(), getValidYear(),
                getValidOwner(), getEmptyCvc());
        buyByCard.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }
}
