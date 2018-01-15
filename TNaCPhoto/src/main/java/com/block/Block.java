package ie.km.ripple.bc;

import ie.km.ripple.bc.MerkleTree.Node;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sun.misc.BASE64Encoder;

public class Block {

    private final byte[] previousHash;

    private Map<byte[], Transaction> transactionMap = new HashMap<byte[], Transaction>();

    private List<Transaction> transactionList = new ArrayList<>();

    private int nonce;

    private MessageDigest md = MessageDigest.getInstance("SHA-256");

    private MerkleTree angela;

    private Node root;

    private BASE64Encoder encoder = new BASE64Encoder();

    public Block(byte[] previousHash) throws NoSuchAlgorithmException {
        this.previousHash = previousHash;
    }

    public String addTransaction(Transaction t) {
        byte[] transHash = md.digest(t.toString().getBytes());

        String hashAsString =  encoder.encode(transHash);
        transactionMap.put(transHash, t);
        transactionList.add(t);
        System.out.println("Hash="+ hashAsString);
        return hashAsString;
    }

    public void create() throws NoSuchAlgorithmException {
        angela = new MerkleTree(transactionMap.keySet());
        root = angela.build();
        System.out.println("Root hash=" + encoder.encode(root.getHash()));
    }


    protected static Block createGenesisBlock() throws NoSuchAlgorithmException {
        return new Block("/mttCtevLaG1GsBa0azSACs9S5uoYw7xVy9QxfJwbiU=".getBytes());
    }

    public Node getRoot() {
        return root;
    }
}
