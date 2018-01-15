package ie.km.ripple.bc;

import java.io.IOException;
import java.math.BigInteger;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.EncoderException;
import sun.misc.BASE64Encoder;

public class BlockChain {

   private static Block genesis;

   private static BASE64Encoder encoder = new BASE64Encoder();

   public static void main(String args[])
           throws NoSuchAlgorithmException, DigestException, IOException, EncoderException {
      Block block = new Block("mttCtevLaG1GsBa0azSACs9S5uoYw7xVy9QxfJwbiU=".getBytes());

      block.addTransaction(new Transaction("A","1", "2", 100l));
      block.addTransaction(new Transaction("B", "3", "4", 200l));
      block.addTransaction(new Transaction("C", "5", "6", 300l));
      block.addTransaction(new Transaction("D", "7", "8", 400l));
      block.addTransaction(new Transaction("E","9", "10", 400l));
      block.addTransaction(new Transaction("F", "11", "12", 400l));

      block.create();
       //      block.createBlock();

      String encodedRootHash = encoder.encode(block.getRoot().getHash());


      HashCash hc = new HashCash("1", 20, "170115", encodedRootHash);
      hc.mine();


   }

   public BlockChain() throws NoSuchAlgorithmException {
       genesis = Block.createGenesisBlock();
   }



   public void addBlock() {

   }

}
