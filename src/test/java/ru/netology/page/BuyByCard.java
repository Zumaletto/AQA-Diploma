package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class BuyByCard {
    private SelenideElement heading = $$(".heading").findBy(text("Оплата по карте"));
    private SelenideElement cardNumberCard = $(".input__control[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthCard = $(".input__control[placeholder='08']");
    private SelenideElement yearCard = $(".input__control[placeholder='22']");
    private SelenideElement ownerCard = $("div:nth-child(3) > span > span:nth-child(1) > span > span > span.input__box > input");
    private SelenideElement cvcCard = $(".input [placeholder='999']");

    private SelenideElement buttonBuyCard = $$("button").findBy(text("Купить"));
    private SelenideElement buttonBuyByCredit = $$("button").findBy(text("Купить в кредит"));
    private SelenideElement buttonCard = $$("button").findBy(text("Продолжить"));

    private ElementsCollection operationMessage = $$(".notification__content");
    private SelenideElement errorMessage = $(".input__sub");

    public BuyByCard() {
        heading.shouldBe(visible);
    }

    public BuyOnCredit transitionToCredit() {
        buttonBuyByCredit.click();
        $$(".heading").findBy(text("Кредит по данным карты")).shouldBe(visible);
        return new BuyOnCredit();
    }

    public void enterCardData(String cardNumber, String month, String year, String owner, String cvc) {
        cardNumberCard.setValue(cardNumber);
        monthCard.setValue(month);
        yearCard.setValue(year);
        ownerCard.setValue(owner);
        cvcCard.setValue(cvc);
        buttonCard.click();
    }

    public void enterWithOutNumber(String month, String year, String owner, String cvc) {
        monthCard.setValue(month);
        yearCard.setValue(year);
        ownerCard.setValue(owner);
        cvcCard.setValue(cvc);
        buttonCard.click();
    }

    public void enterWithOutMonth(String cardNumber, String year, String owner, String cvc) {
        cardNumberCard.setValue(cardNumber);
        yearCard.setValue(year);
        ownerCard.setValue(owner);
        cvcCard.setValue(cvc);
        buttonCard.click();
    }

    public void enterWithOutYear(String cardNumber, String month, String owner, String cvc) {
        cardNumberCard.setValue(cardNumber);
        monthCard.setValue(month);
        ownerCard.setValue(owner);
        cvcCard.setValue(cvc);
        buttonCard.click();
    }

    public void enterWithOutOwner(String cardNumber, String month, String year, String cvc) {
        cardNumberCard.setValue(cardNumber);
        monthCard.setValue(month);
        yearCard.setValue(year);
        cvcCard.setValue(cvc);
        buttonCard.click();
    }

    public void enterWithOutCvc(String cardNumber, String month, String year, String owner) {
        cardNumberCard.setValue(cardNumber);
        monthCard.setValue(month);
        yearCard.setValue(year);
        ownerCard.setValue(owner);
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
