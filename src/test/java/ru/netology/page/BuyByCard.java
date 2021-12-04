package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class BuyByCard {
    private SelenideElement heading = $$(".heading").findBy(text("Оплата по карте"));
    private SelenideElement cardNumberCard = $$(".input__control").get(0);
    private SelenideElement monthCard = $$(".input__control").get(1);
    private SelenideElement yearCard = $$(".input__control").get(2);
    private SelenideElement ownerCard = $$(".input__control").get(3);
    private SelenideElement cvcCard = $$(".input__control").get(4);
    private SelenideElement buttonCard = $$("button").findBy(text("Продолжить"));

    public BuyByCard(){
        heading.shouldBe(visible);
    }

    public void validPaymentGate(){
        cardNumberCard.setValue(DataHelper.getApprovedCardNumber());
        monthCard.setValue(DataHelper.getValidMonth());
        yearCard.setValue(DataHelper.getValidYear());
        ownerCard.setValue(DataHelper.getValidOwner());
        cvcCard.setValue(DataHelper.getValidCvc());
        buttonCard.click();



    }








}
