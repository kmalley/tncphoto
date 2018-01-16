package ie.km.ripple.bc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Random;
import org.apache.commons.codec.binary.BinaryCodec;
import sun.misc.BASE64Encoder;

/**
 * Simple implementation of the HashCash algorithm.
 *
 * The cost of generating the header with a 20 zero bit hash prefix is between 1 and 15 seconds
 *
 * Sample header:
 *
 * X-Hashcash: 1:20:1801160600:P5px+rhe4k9Vn4J08fXW1hnt0MX/QN5ddqRU4w+ykjA=::zmcjVcMDnCt98G5TfczLDTdfaT4:MA==
 *
 * Version  : HashCash format version currently at 1
 * Bits     : Number of zero bits in the hashed code
 * Date     : Time of message in the format YYMMDD[hhmm[ss]]
 * Resource : Date string being transmitted
 * Extension: Ignored in version 1
 * Random   : String of random character base-64 encoded
 * Counter  : Base-64 encoded counter
 */
public class HashCash {

    private final String version;

    private final int numZeroBits;

    private final String date;

    private final String resource;

    private final String randomString;

    private final String HASH_PREFIX;

    private final int NUM_RAND_CHARACTERS = 20;

    public HashCash(String version, int numZeroBits, String date, String resource) {
        this.version = version;
        this.numZeroBits = numZeroBits;
        this.date = date;
        this.resource = resource;
        this.randomString = generateEncodedRandomString();
        this.HASH_PREFIX = String.join("", Collections.nCopies(numZeroBits, "0"));
    }

    public String mint()  {
        int counter = 0;

        System.out.println("Hashing the header " + generateHeader(0));

        String binaryResult = "";

        long start = System.currentTimeMillis();

        while (!binaryResult.startsWith(HASH_PREFIX)) {
            counter++;
            byte[] hash = Hash.digest(generateHeader(counter).getBytes());
            binaryResult = BinaryCodec.toAsciiString(hash);

            if (counter % 100000 == 0) {
                System.out.println("Hashing iteration= " + counter + " + current val=" + binaryResult);
            }
        }

        long stop = System.currentTimeMillis();

        System.out.println("Result in " + ((stop - start) / 1000.0) + " seconds and " + counter + " iterations. Result=" + binaryResult);

        return generateHeader(counter);
    }

    private String generateHeader(int counter) {
        return new StringBuffer()
                .append(version)
                .append(':')
                .append(numZeroBits)
                .append(':')
                .append(date)
                .append(':')
                .append(resource)
                .append("::")
                .append(randomString)
                .append(Hash.encode(String.valueOf(counter).getBytes())).toString();
    }

    private String generateEncodedRandomString() {
        byte[] b = new byte[NUM_RAND_CHARACTERS];
        new Random().nextBytes(b);
        return Hash.encode(b);
    }

}
