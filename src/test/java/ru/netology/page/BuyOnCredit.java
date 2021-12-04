package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class BuyOnCredit {

    private SelenideElement heading = $$(".heading").findBy(text("Кредит по данным карты"));
    private SelenideElement cardNumberCredit = $$(".input__control").get(0);
    private SelenideElement monthCredit = $$(".input__control").get(1);
    private SelenideElement yearCredit = $$(".input__control").get(2);
    private SelenideElement ownerCredit = $$(".input__control").get(3);
    private SelenideElement cvcCredit = $$(".input__control").get(4);
    private SelenideElement buttonCredit = $$("button").findBy(text("Продолжить"));

}
