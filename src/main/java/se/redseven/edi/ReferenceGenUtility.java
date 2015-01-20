package se.redseven.edi;

import java.util.Random;

public class ReferenceGenUtility {

    private static ReferenceGenUtility referenceIdGenerator = null;
    private Random rand = null;
    private static final char[] chars;

    static {

        StringBuilder tmp = new StringBuilder();

        for (char ch = '0'; ch <= '9'; ++ch) {

            tmp.append(ch);
        }

        for (char ch = 'a'; ch <= 'z'; ++ch) {

            tmp.append(ch);
        }

        for (char ch = 'A'; ch <= 'Z'; ++ch) {

            tmp.append(ch);
        }

        chars = tmp.toString().toCharArray();
    }

    private ReferenceGenUtility() {

        rand = new Random();
    }

    /**
     * Generate a reasonable unique alphanumeric ID
     * @param lenght of the ID
     * @return String with ID
     */
    public static String generateRefId(int lenght) {

        if (null == referenceIdGenerator) {

            referenceIdGenerator = new ReferenceGenUtility();
        }

        StringBuffer buffer = new StringBuffer();

        for (int idx = 0; idx < lenght; ++idx) {

            buffer.append(chars[referenceIdGenerator.rand.nextInt(chars.length)]);
        }

        return buffer.toString();
    }
}
