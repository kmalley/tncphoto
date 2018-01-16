package ie.km.ripple.bc;

public class Transaction {
    private final Account source;

    private final Account destination;

    private final Long amount;

    private final long timestamp = System.currentTimeMillis();

    public Transaction(Account source, Account destination, Long amount) {
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        System.out.println("Creating " + toString());
    }

    public Long getAmount() {
        return amount;
    }

    public Account getDestination() {
        return destination;
    }

    public Account getSource() {
        return source;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "source='" + source.getAccountNumber() + '\'' +
                ", destination='" + destination.getAccountNumber() + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}
