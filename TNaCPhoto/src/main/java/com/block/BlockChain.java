package ie.km.ripple.bc;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;

public class BlockChain {

    private static Block genesis;

    private LinkedList<Block> chain = new LinkedList<>();

    private HashMap<String, Block> transactionMap = new HashMap<>();

    private Block activeBlock;

    public BlockChain() throws NoSuchAlgorithmException {
        genesis = Block.createGenesisBlock();
        chain.add(genesis);
    }

    public Block createBlock() {
        return new Block(chain.peekLast().getRoot().getHash());
    }


    public String processTransaction(Transaction transaction) {
        if (activeBlock == null) {
            activeBlock = createBlock();
        }

        String transactionHash = activeBlock.addTransaction(transaction);

        if (activeBlock.isBlockFull()) {
            activeBlock.mineBlock();
            //
            // Link each transaction to the block in the clain
            activeBlock.getTransactionHashList()
                    .forEach(trans -> transactionMap.put(String.valueOf(trans), activeBlock));
            //
            // Block complete so add it to the chain
            chain.add(activeBlock);
        }

        return transactionHash;
    }

    public static void main(String args[])
            throws NoSuchAlgorithmException {
        BlockChain bc = new BlockChain();

        bc.processTransaction(new Transaction(Account.create(), Account.create(), 100l));
        bc.processTransaction(new Transaction(Account.create(), Account.create(), 200l));
        bc.processTransaction(new Transaction(Account.create(), Account.create(), 300l));
        bc.processTransaction(new Transaction(Account.create(), Account.create(), 400l));
        bc.processTransaction(new Transaction(Account.create(), Account.create(), 400l));
        bc.processTransaction(new Transaction(Account.create(), Account.create(), 500l));


    }

}
