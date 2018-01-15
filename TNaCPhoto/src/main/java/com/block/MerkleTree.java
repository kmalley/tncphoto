package ie.km.ripple.bc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class MerkleTree {

    private final Logger logger = Logger.getLogger(MerkleTree.class.getName());

    private Set<byte[]> hashList;

    private List<Transaction> transactionList;

    private Node root;

    private LinkedList<Node> nodes = new LinkedList<>();

    private MessageDigest md = MessageDigest.getInstance("SHA-256");

    public MerkleTree(Set<byte[]> hashList) throws NoSuchAlgorithmException {
        logger.info("Creating new MerkleTree with " + hashList.size() + " nodes");

        this.hashList = hashList;
    }

    public MerkleTree(List<Transaction> transactionList) throws NoSuchAlgorithmException {
        logger.info("Creating new MerkleTree with " + transactionList.size() + " transactions");

        this.transactionList = transactionList;
    }

    public void initTransactions() {
        transactionList.forEach(entry -> nodes.add(new Node(entry.getId(), entry.toString().getBytes(), 1)));
        buildTheTree();

        System.out.println("Root node=" + root.name + ". With a depth of " + root.depth);
    }


    public Node build() {
        hashList.forEach(entry -> nodes.add(new Node("", entry, 0)));
        buildTheTree();
        System.out.println("Root node=" + root.hash);
        return root;
    }

    public Node buildWithId() {
        transactionList.forEach(entry -> nodes.add(new Node(entry.getId(), entry.toString().getBytes(), 0)));
        buildTheTree();
        System.out.println("Root node=" + root.name);
        return root;
    }

    private void buildTheTree() {
        while (!nodes.isEmpty()) {
            logger.info("Remaining nodes=" + nodes.size());

            Node left = nodes.remove();

            //
            // To ensure the tree is balanced both the left and right nodes should be at the same depth
            // To balance the tree a new node is added to the head of the queue with the same details as the
            // unbalanced leaf node
            if (left.depth != nodes.peek().depth) {
                logger.info("Tree is not balanced! l.name=" + left.name + " l.depth=" + left.depth + " r.name="
                        + nodes.peek().name + " r.depth=" + nodes.peek().depth);
                Node n = new Node(left.name, left.hash, left.depth);

                nodes.addFirst(n);
            }

            Node right = nodes.remove();

            byte[] combined = combineByteArrays(left.hash, right.hash);

            Node parent = new Node(left.name + right.name,left, right, md.digest(combined), left.depth+1);
            logger.info("Creating parent name=" + parent.name);

            if (nodes.isEmpty()) {
                root = parent; // root node
                return;
            }

            nodes.add(parent);
        }

    }

    private byte[] combineByteArrays(byte[] aArray, byte[] bArray) {
        byte[] combined = new byte[aArray.length + bArray.length];
        System.arraycopy(aArray, 0, combined, 0, aArray.length);
        System.arraycopy(bArray, 0, combined, aArray.length, bArray.length);
        return combined;
    }

    class Node {
        private final String name;
        private final Node left;
        private final Node right;
        private final byte[] hash;
        private final int depth;
        private boolean isLeaf;

        private Node(String name, Node left, Node right, byte[] hash, int depth, boolean isLeaf) {
            this.left = left;
            this.right = right;
            this.hash = hash;
            this.depth = depth;
            this.name = name;
            this.isLeaf = isLeaf;
        }

        public Node(String name, Node left, Node right, byte[] hash, int depth) {
            this(name, left, right, hash, depth, false);
        }

        public Node(String name, byte[] hash, int depth) {
            this(name, null, null, hash, depth, true);
        }

        public byte[] getHash() {
            return hash;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public boolean isLeaf() {
            return isLeaf;
        }
    }
}
