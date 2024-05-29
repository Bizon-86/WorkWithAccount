package ru.inno.local.account;

import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Account acc = new Account("Vasya");
        System.out.println("Создан счет с владельцем " + acc.getOwner());
        acc.executeCommand(new ChangeAccountOwner(acc,"Petr"));
        System.out.println("Сменили владельца счета " + acc.getOwner());
        acc.executeCommand(new ChangeAccountOwner(acc,"Ivan"));
        System.out.println("Сменили владельца счета " + acc.getOwner());
        acc.executeCommand(new ChangeCurrency(Currency.USD,100L,acc));
        System.out.println("Добавили валюту " + Currency.USD + " баланс " + acc.getCurBal().get(Currency.USD));
        acc.executeCommand(new ChangeCurrency(Currency.RUB,200L,acc));
        System.out.println("Добавили валюту " + Currency.RUB + " баланс " + acc.getCurBal().get(Currency.RUB));
        acc.executeCommand(new ChangeCurrency(Currency.EUR,500L,acc));
        System.out.println("Добавили валюту " + Currency.EUR + " баланс " + acc.getCurBal().get(Currency.EUR));
        System.out.println("Исходные значения: Владелец счета " + acc.getOwner() + " Валюты = " + acc.getCurBal());
        for (int i = 1; i<=10; i++){
            if (acc.canUndo()) {
                acc.undoLastCommand();
            }else break;
            System.out.println("После " + i + " отмены: Владелец счета " + acc.getOwner() + " Валюты = " + acc.getCurBal());
        }
        System.out.println("Save");
        AccountState accountState = acc.save();
        acc.setOwner("Max");
        System.out.println("Изменили состояние счета владелец " + acc.getOwner() + " валюты " + acc.getCurBal());
        acc = Account.restore(accountState);
        System.out.println("Восстановили счет владелец " + acc.getOwner() + " валюты " + acc.getCurBal());
        acc.executeCommand(new ChangeCurrency(Currency.EUR,800L,acc));
        System.out.println("Изменили состояние счета владелец " + acc.getOwner() + " валюты " + acc.getCurBal());
        acc = Account.restore(accountState);
        System.out.println("Восстановили счет владелец " + acc.getOwner() + " валюты " + acc.getCurBal());
    }
}
