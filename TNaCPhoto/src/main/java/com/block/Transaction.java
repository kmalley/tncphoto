package ie.km.ripple.bc;

public class Transaction {
    private final String id;

    private final String source;

    private final String destination;

    private final Long amount;

    private final long timestamp = System.currentTimeMillis();

    public Transaction(String id, String source, String destination, Long amount) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.amount = amount;
    }

    public Long getAmount() {
        return amount;
    }

    public String getDestination() {
        return destination;
    }

    public String getSource() {
        return source;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }

    public String getId() {
        return id;
    }
}
