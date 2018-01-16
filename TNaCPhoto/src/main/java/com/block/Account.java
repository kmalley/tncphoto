package ie.km.ripple.bc;

public class Account {
    private final String accountNumber;

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public static Account create() {
        return new Account(Hash.encode(String.valueOf(System.currentTimeMillis()).getBytes()));
    }

}
