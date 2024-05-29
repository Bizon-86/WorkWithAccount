package ru.inno.local.account;

import java.util.Map;

public interface Command {
    void execute();
    Account undo();
}
