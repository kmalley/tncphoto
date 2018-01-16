package ie.km.ripple.bc;

import ie.km.ripple.bc.MerkleTree.Node;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Block {

    public final static int MAX_NUM_TRANSACTIONS = 6;

    private final String previousHash;

    private Map<byte[], Transaction> transactionMap = new HashMap<>();

    private MerkleTree angela;

    private Node root;

    private String proofOfWork;

    private int transactionCounter = 0;

    public Block(byte[] previousHash) {
        this.previousHash = String.valueOf(previousHash);
    }

    public boolean isBlockFull() {
        return (transactionCounter == MAX_NUM_TRANSACTIONS);
    }

    public String addTransaction(Transaction t) {
        if (!isBlockFull()) {
            transactionCounter++;
            byte[] transHash = Hash.digest(t.toString().getBytes());

            String hashAsString = Hash.encode(transHash);
            transactionMap.put(transHash, t);
            System.out.println("Hash=" + hashAsString);
            return hashAsString;
        } else throw new RuntimeException("Block full");
    }

    public Node mineBlock() {
        angela = new MerkleTree(transactionMap.keySet());
        root = angela.build();
        System.out.println("Root hash=" + Hash.encode(root.getHash()));
        proofOfWork = generateProofOfWork();
        return root;
    }

    private String generateProofOfWork() {
        HashCash hc = new HashCash("1", 20, "170115", String.valueOf(previousHash + root.getHash()));
        return hc.mint();
    }

    public boolean isMined() {
        return proofOfWork == null ? false : true;
    }

    public Set<byte[]> getTransactionHashList() {
        return transactionMap.keySet();
    }
    public Node getRoot() {
        return root;
    }

    public MerkleTree getMerkleTree() {
        return angela;
    }

    public int getNumberOfTransactions() {
        return transactionMap.size();
    }

    protected static Block createGenesisBlock() throws NoSuchAlgorithmException {
        Block b = new Block("/mttCtevLaG1GsBa0azSACs9S5uoYw7xVy9QxfJwbiU=".getBytes());
        b.addTransaction(new Transaction(Account.create(), Account.create(), 100l));
        b.addTransaction(new Transaction(Account.create(), Account.create(), 200l));
        b.mineBlock();
        return b;
    }

}
