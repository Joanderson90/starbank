package com.uefs.starbank.mutex;


import java.util.ArrayList;
import java.util.List;

public class AccountMutex {
    private static final List<Integer> accountCriticalSectionList = new ArrayList<>();


    public static void addAccessToCriticalSection(Integer accountNumber) {
        accountCriticalSectionList.add(accountNumber);
    }

    public static void removeAccessToCriticalSection(Integer accountNumber) {
        accountCriticalSectionList.remove(accountNumber);
    }


    public static boolean hasAccessToCriticalSection(Integer accountNumber) {
        return accountCriticalSectionList.contains(accountNumber);
    }
}
