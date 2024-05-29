import org.junit.Test;
import ru.inno.local.account.*;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AccountTest {
    @Test
    public void createEmptyAccount() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account x = new Account("");
        });
    }

    @Test
    public void createNullAccount() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account x = new Account(null);
        });
    }

    @Test
    public void addNegativeBal() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account acc = new Account("Maxim");
            acc.addCurrencyBalance(Currency.RUB, -100);
        });
    }

    @Test
    public void addEmptyCurrency() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account acc = new Account("Maxim");
            acc.addCurrencyBalance(null, 100);
        });
    }

    @Test
    public void setBadOwner() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account x = new Account("Max");
            x.setOwner("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Account x = new Account("Pete");
            x.setOwner(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Account acc = new Account("Vasya");
            acc.executeCommand(new ChangeAccountOwner(acc, ""));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Account acc = new Account("Vasya");
            acc.executeCommand(new ChangeAccountOwner(acc, null));
        });
    }

    @Test
    public void execCommandChangeOwner(){
        Account acc = new Account("Vasya");
        String oldName = acc.getOwner();
        acc.executeCommand(new ChangeAccountOwner(acc,"Petr"));
        assertNotEquals(oldName,acc.getOwner());
    }

    @Test
    public void execCommandChangeCurrency(){
        Account acc = new Account("Vasya");
        acc.addCurrencyBalance(Currency.EUR,100L);
        var oldCurBal = new EnumMap<Currency,Long>(Currency.class);
        oldCurBal.putAll(acc.getCurBal());
        acc.executeCommand(new ChangeCurrency(Currency.RUB, 900L,acc));
        assertNotEquals(oldCurBal,acc.getCurBal());
    }

    @Test
    public void execUndoChangeOwnerCommand(){
        Account acc = new Account("Vasya");
        String oldName = acc.getOwner();
        acc.executeCommand(new ChangeAccountOwner(acc,"Petr"));
        acc.undoLastCommand();
        assertEquals(oldName,acc.getOwner());
    }

    @Test
    public void execUndoChangeCurrencyCommand(){
        Account acc = new Account("Vasya");
        acc.addCurrencyBalance(Currency.EUR,100L);
        var oldCurBal = new EnumMap<Currency,Long>(Currency.class);
        oldCurBal.putAll(acc.getCurBal());
        acc.executeCommand(new ChangeCurrency(Currency.RUB, 900L,acc));
        acc.undoLastCommand();
        assertEquals(oldCurBal,acc.getCurBal());
    }

    @Test
    public void execSaveRestoreOwnerCommand(){
        Account acc = new Account("Vasya");
        AccountState accountState = acc.save();
        acc.setOwner("Vova");
        acc = Account.restore(accountState);
        assertEquals(acc.getOwner(),accountState.getOwner());
    }

    @Test
    public void execSaveRestoreCurrencyCommand(){
        Account acc = new Account("Vasya");
        AccountState accountState = acc.save();
        acc.addCurrencyBalance(Currency.EUR,600);
        acc = Account.restore(accountState);
        assertEquals(accountState.getCurBal(),acc.getCurBal());
    }

    @Test
    public void createCurrency(){
        Currency currency = Currency.EUR;
        assertEquals(currency.getCurName(),"Евро");
    }
}
