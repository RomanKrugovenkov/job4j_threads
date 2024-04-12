package ru.job4j.cash;

import java.util.HashMap;
import java.util.Optional;

public class AccountStorage {
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        return accounts.putIfAbsent(account.id(), account) == null;
    }

    public synchronized boolean update(Account account) {
        return accounts.put(account.id(), account) != null;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        var accFrom = getById(fromId);
        var accTo = getById(toId);
        boolean rsl = false;
        if (accFrom.isEmpty()) {
            throw new IllegalStateException(String.format("Not found account by id = %d", fromId));
        }
        if (accTo.isEmpty()) {
            throw new IllegalStateException(String.format("Not found account by id = %d", toId));
        }
        if (accFrom.get().amount() >= amount) {
            update(new Account(fromId, accFrom.get().amount() - amount));
            update(new Account(toId, accTo.get().amount() + amount));
            rsl = true;
        }
        return rsl;
    }
}
