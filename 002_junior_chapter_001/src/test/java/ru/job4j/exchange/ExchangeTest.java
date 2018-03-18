package ru.job4j.exchange;

import org.junit.Test;

public class ExchangeTest {

    @Test
    public void printExchange() {
        Exchange exchange = new Exchange();
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "Газпром", 5, 2));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "Газпром", 5, 7));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "Газпром", 5, 7));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.BID, "Газпром", 3, 20));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "Газпром", 10, 1));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "Газпром", 10, 10));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "МежГазТранс", 10, 10));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "МежГазТранс", 10, 10));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "МежГазТранс", 10, 10));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "МежГазТранс", 10, 10));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "МежГазТранс", 10, 10));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "МежГазТранс", 10, 10));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "МежГазТранс", 10, 10));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.BID, "МежГазТранс", 10, 10));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.BID, "МежГазТранс", 10, 10));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.BID, "МежГазТранс", 10, 10));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.BID, "МежГазТранс", 10, 10));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.BID, "МежГазТранс", 34, 10));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.BID, "СельхозЯвь", 10, 10));
        exchange.processNew(new Task("123", ActionEnum.ADD, OperationEnum.BID, "УгорьПром", 10, 10));
        System.out.println(exchange);
    }

}