package ie.km.ripple.bc;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Random;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.BinaryCodec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class HashCash {

    private final String version;

    private final int numZeroBits;

    private final String date;

    private final String resource;

    private final String randomString;

    private final int NUM_RAND_CHARACTERS = 20;

    private final String HASH_PREFIX;

    private int nonce = 0;

    private BASE64Encoder encoder = new BASE64Encoder();

    private MessageDigest md = MessageDigest.getInstance("SHA-1");

    public HashCash(String version, int numZeroBits, String date, String resource) throws NoSuchAlgorithmException {
        this.version = version;
        this.numZeroBits = numZeroBits;
        this.date = date;
        this.resource = resource;
        this.randomString = generateEncodedRandomString();
        this.HASH_PREFIX = String.join("", Collections.nCopies(numZeroBits, "0"));
    }

    public String mine() throws EncoderException, IOException {

        String header = version + ":" + numZeroBits + ":" + date + ":" + resource + ":"
                + randomString + ":" + encoder.encode(String.valueOf(nonce).getBytes());

        System.out.println("Hashing the header " + header);

        String result = "";
        String resultInBinary = "";
        byte[] hash = null;

        long start = System.currentTimeMillis();
        while (!resultInBinary.startsWith(HASH_PREFIX)) {
            nonce++;
            header = version + ":" + numZeroBits + ":" + date + ":" + resource + "::"
                    + randomString + ":" + encoder.encode(String.valueOf(nonce).getBytes());
            hash = md.digest(header.getBytes());
            resultInBinary = BinaryCodec.toAsciiString(hash);

            if (nonce % 100000 == 0)
                System.out.println("Hashing iteration= " + nonce  + " + current val=" + resultInBinary );
        }

        long stop = System.currentTimeMillis();
//
//        String a = encoder.encode(hash);
//        System.out.println(a);
        System.out.println("Result in " + ((stop - start) / 1000.0) + " seconds and " + nonce + " iterations. Result=" + resultInBinary);
//        BASE64Decoder decoder = new BASE64Decoder();
//        byte[] b = decoder.decodeBuffer(a);

        return header;
    }

    private String generateEncodedRandomString() {
        byte[] b = new byte[NUM_RAND_CHARACTERS];
        new Random().nextBytes(b);
        return encoder.encode(b);
    }

}
