package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.BuyByCard;
import ru.netology.page.MainPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DbHelper.orderData;
import static ru.netology.data.DbHelper.payData;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestBuyByCard {

    DataHelper.CardNumber approvedCard = DataHelper.approvedCardInfo();
    DataHelper.CardNumber declinedCard = DataHelper.declinedCardInfo();

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
    @DisplayName("Успешная покупка тура картой со статусом APPROVED")
    void shouldBuyByCardApproved() throws SQLException {
        var mainPage = new MainPage();
        mainPage.getPageByCard();
        var buyByCard = new BuyByCard();
        buyByCard.validPaymentGate();
        buyByCard.getSuccessMessage();
        assertEquals(approvedCard.getStatus(), payData().getStatus());
        assertEquals(payData().getTransaction_id(), orderData().getPayment_id());
    }
}
