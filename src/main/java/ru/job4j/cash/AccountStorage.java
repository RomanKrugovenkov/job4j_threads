package ru.job4j.cash;

import java.util.HashMap;
import java.util.Optional;

public class AccountStorage {
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        return accounts.putIfAbsent(account.id(), account) == null;
    }

    public synchronized boolean update(Account account) {
        if (getById(account.id()).isEmpty()) {
            throw new IllegalStateException(String.format("Not found account by id = %d", account.id()));
        }
        return accounts.put(account.id(), account) != null;
    }

    public synchronized void delete(int id) {
        if (getById(id).isEmpty()) {
            throw new IllegalStateException(String.format("Not found account by id = %d", id));
        }
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        if (getById(fromId).isEmpty()) {
            throw new IllegalStateException(String.format("Not found account by id = %d", fromId));
        }
        if (getById(toId).isEmpty()) {
            throw new IllegalStateException(String.format("Not found account by id = %d", toId));
        }
        boolean rsl = false;
        Account accFrom = getById(fromId).get();
        Account accTo = getById(toId).get();
        if (accFrom.amount() >= amount) {
            accounts.put(fromId, new Account(fromId, accFrom.amount() - amount));
            accounts.put(toId, new Account(toId, accTo.amount() + amount));
            rsl = true;
        }
        return rsl;
    }
}
