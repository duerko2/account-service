package account.service;

import java.util.HashMap;
import java.util.Map;

public class AccountRepo {
    Map<String, Account> accountIdToAccountMap = new HashMap<>();
    Map<String, String> cprToAccountIdMap = new HashMap<>();


    public void storeAccount(Account account) {
        accountIdToAccountMap.put(account.getAccountId(), account);
        cprToAccountIdMap.put(account.getCpr(), account.getAccountId());
    }
    public Account getAccount(String accountId) {
        return accountIdToAccountMap.get(accountId);
    }
    public void deleteAccount(String accountId) {
        Account a = accountIdToAccountMap.get(accountId);
        if(a==null){
            return;
        }
        cprToAccountIdMap.remove(a.getCpr());
        accountIdToAccountMap.remove(accountId);
    }

    public boolean accountExists(String cpr) {
        return cprToAccountIdMap.containsKey(cpr);
    }
}
