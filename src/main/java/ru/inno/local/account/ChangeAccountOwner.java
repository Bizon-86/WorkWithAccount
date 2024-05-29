package ru.inno.local.account;

public class ChangeAccountOwner implements Command {
    private final String newOwner, prevOwner;
    private final Account account;

    public ChangeAccountOwner(Account account, String newOwner) {
        this.account = account;
        this.newOwner = newOwner;
        this.prevOwner = account.getOwner();
    }

    @Override
    public void execute() {
        account.setOwner(newOwner);
    }

    @Override
    public Account undo() {
        return new Account(prevOwner);
    }
}
