package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.DataHelper.getInvalidCvc;

public class BuyOnCredit {

    private SelenideElement heading = $$(".heading").findBy(text("Кредит по данным карты"));
    private SelenideElement cardNumberCard = $$(".input__control").get(0);
    private SelenideElement monthCard = $$(".input__control").get(1);
    private SelenideElement yearCard = $$(".input__control").get(2);
    private SelenideElement ownerCard = $$(".input__control").get(3);
    private SelenideElement cvcCard = $$(".input__control").get(4);

    private SelenideElement buttonBuyCard = $$("button").findBy(text("Купить"));
    private SelenideElement buttonBuyByCredit = $$("button").findBy(text("Купить в кредит"));
    private SelenideElement buttonCard = $$("button").findBy(text("Продолжить"));

    private ElementsCollection operationMessage = $$(".notification__content");
    private SelenideElement errorMessage = $(".input__sub");

    public BuyOnCredit() {
        heading.shouldBe(visible);
    }

    public BuyOnCredit transitionToCredit() {
        buttonBuyByCredit.click();
        return new BuyOnCredit();
    }

    public BuyByCard transitionToCard() {
        buttonBuyCard.click();
        return new BuyByCard();
    }

    public void validPaymentGate(DataHelper.CardInfo cardInfo) {
        cardNumberCard.setValue(getApprovedCardNumber());
        monthCard.setValue(getValidMonth());
        yearCard.setValue(getValidYear());
        ownerCard.setValue(getValidOwner());
        cvcCard.setValue(getValidCvc());
        buttonCard.click();
    }

    public void invalidPaymentGate(CardInfo cardInfo) {
        cardNumberCard.setValue(getDeclinedCardNumber());
        monthCard.setValue(getValidMonth());
        yearCard.setValue(getValidYear());
        ownerCard.setValue(getValidOwner());
        cvcCard.setValue(getValidCvc());
        buttonCard.click();
    }

    public void invalidNumberCard(CardInfo cardInfo) {
        cardNumberCard.setValue(getInvalidCardNumber());
        monthCard.setValue(getValidMonth());
        yearCard.setValue(getValidYear());
        ownerCard.setValue(getValidOwner());
        cvcCard.setValue(getValidCvc());
        buttonCard.click();
    }

    public void invalidMonth1(CardInfo cardInfo) {
        cardNumberCard.setValue(getApprovedCardNumber());
        monthCard.setValue(getInvalidMonth1());
        yearCard.setValue(getValidYear());
        ownerCard.setValue(getValidOwner());
        cvcCard.setValue(getValidCvc());
        buttonCard.click();
    }

    public void invalidMonth2(CardInfo cardInfo) {
        cardNumberCard.setValue(getApprovedCardNumber());
        monthCard.setValue(getInvalidMonth2());
        yearCard.setValue(getValidYear());
        ownerCard.setValue(getValidOwner());
        cvcCard.setValue(getValidCvc());
        buttonCard.click();
    }

    public void invalidYear(CardInfo cardInfo) {
        cardNumberCard.setValue(getApprovedCardNumber());
        monthCard.setValue(getValidMonth());
        yearCard.setValue(getInvalidYear());
        ownerCard.setValue(getValidOwner());
        cvcCard.setValue(getValidCvc());
        buttonCard.click();
    }

    public void invalidLastYear(CardInfo cardInfo) {
        cardNumberCard.setValue(getApprovedCardNumber());
        monthCard.setValue(getValidMonth());
        yearCard.setValue(getInvalidLastYear());
        ownerCard.setValue(getValidOwner());
        cvcCard.setValue(getValidCvc());
        buttonCard.click();
    }

    public void invalidFutureYear(CardInfo cardInfo) {
        cardNumberCard.setValue(getApprovedCardNumber());
        monthCard.setValue(getValidMonth());
        yearCard.setValue(getInvalidFutureYear());
        ownerCard.setValue(getValidOwner());
        cvcCard.setValue(getValidCvc());
        buttonCard.click();
    }

    public void invalidOwnerCyrillic(CardInfo cardInfo) {
        cardNumberCard.setValue(getApprovedCardNumber());
        monthCard.setValue(getValidMonth());
        yearCard.setValue(getValidYear());
        ownerCard.setValue(getInvalidOwnerCyrillic());
        cvcCard.setValue(getValidCvc());
        buttonCard.click();
    }

    public void invalidOwnerMathSymbols(CardInfo cardInfo) {
        cardNumberCard.setValue(getApprovedCardNumber());
        monthCard.setValue(getValidMonth());
        yearCard.setValue(getValidYear());
        ownerCard.setValue(getInvalidOwnerMathSymbols());
        cvcCard.setValue(getValidCvc());
        buttonCard.click();
    }

    public void invalidOwnerOwnerRegister(CardInfo cardInfo) {
        cardNumberCard.setValue(getApprovedCardNumber());
        monthCard.setValue(getValidMonth());
        yearCard.setValue(getValidYear());
        ownerCard.setValue(getInvalidOwnerRegister());
        cvcCard.setValue(getValidCvc());
        buttonCard.click();
    }

    public void invalidOwnerOverLength(CardInfo cardInfo) {
        cardNumberCard.setValue(getApprovedCardNumber());
        monthCard.setValue(getValidMonth());
        yearCard.setValue(getValidYear());
        ownerCard.setValue(getInvalidOwnerOverLength());
        cvcCard.setValue(getValidCvc());
        buttonCard.click();
    }

    public void invalidOwnerUnderLength(CardInfo cardInfo) {
        cardNumberCard.setValue(getApprovedCardNumber());
        monthCard.setValue(getValidMonth());
        yearCard.setValue(getValidYear());
        ownerCard.setValue(getInvalidOwnerUnderLength());
        cvcCard.setValue(getValidCvc());
        buttonCard.click();
    }

    public void invalidCVC(CardInfo cardInfo) {
        cardNumberCard.setValue(getApprovedCardNumber());
        monthCard.setValue(getValidMonth());
        yearCard.setValue(getValidYear());
        ownerCard.setValue(getValidOwner());
        cvcCard.setValue(getInvalidCvc());
        buttonCard.click();
    }

    public void getSuccessMessage() {
        operationMessage.get(0).shouldBe(visible, Duration.ofSeconds(15));
        operationMessage.get(0).shouldHave(exactText("Операция одобрена Банком."));
    }

    public void getFailMessage() {
        operationMessage.get(1).shouldBe(visible, Duration.ofSeconds(15));
        operationMessage.get(1).shouldHave(exactText("Ошибка! Банк отказал в проведении операции."));
    }

    public void formatError() {
        errorMessage.shouldHave(text("Неверный формат"));
    }

    public void expiredError() {
        errorMessage.shouldHave(text("Истёк срок действия карты"));
    }

    public void invalidError() {
        errorMessage.shouldHave(text("Неверно указан срок действия карты"));
    }

    public void emptyError() {
        errorMessage.shouldHave(text("Поле обязательно для заполнения"));
    }

}
