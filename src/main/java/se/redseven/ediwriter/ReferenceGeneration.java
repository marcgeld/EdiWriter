package se.redseven.ediwriter;

import java.util.Random;

/**
 * Skapar en Referens.
 */
public final class ReferenceGeneration {

    public static final char[] CA_ALFA = generateCharArray(new Range('a', 'z'), new Range('A', 'Z'));
    public static final char[] CA_NUMERIC = generateCharArray(new Range('0', '9'));
    public static final char[] CA_ALFANUMERIC_MIXEDCASE =
        generateCharArray(new Range('0', '9'), new Range('a', 'z'), new Range('A', 'Z'));
    private static final Random RANDOM = new Random();

    /**
     * Range - klass som anger till och från värde (inklusive).
     * @author ICC
     */
    public static class Range {

        private final char start;
        private final char stop;

        /**
         * Konstruktor.
         * @param start från-värde
         * @param stop till-värde
         */
        public Range(char start, char stop) {

            this.start = start;
            this.stop = stop;
        }
    }

    /**
     * Type of Reference.
     */
    public enum TypeOfReference {
        ALFA, ALFANUMERIC_MIXEDCASE, ALFANUMERIC_UPPERCASE, NUMERIC
    }

    /**
     * Private constructor as only static methods are used!
     */
    private ReferenceGeneration() {
    }

    /**
     * Genererar en char array ifrån en eller flera char range.
     * @param fromAsciiChar från char i ascii (utf-8) listan
     * @param toAsciiChar till char i ascii (utf-8) listan
     * @return array med alla teckan frn -> till (inklusive)
     */
    private static char[] generateCharArray(Range... ranges) {

        String tmp = "";

        for (Range range : ranges) {

            for (char ch = range.start; ch <= range.stop; ++ch) {

                tmp += ch;
            }
        }

        return tmp.toCharArray();
    }

    /**
     * Returnerar en alfa numerisk sträng med hyfsat unika tecken.
     * @param lenght antal tecken som skall genereras
     * @return Genererad referens.
     */
    public static String getReference(int lenght) {

        return getReference(TypeOfReference.ALFANUMERIC_MIXEDCASE, lenght);
    }

    /**
     * Returnerar en sträng med hyfsat unika tecken.
     * @param lenght antal tecken som skall genereras
     * @return Genererad referens.
     */
    public static String getReferenceUppercase(int lenght) {

        return getReference(TypeOfReference.ALFANUMERIC_UPPERCASE, lenght);
    }

    /**
     * Returnerar en sträng med hyfsat unika tecken.
     * @param type Vilken typ som skall genereras.
     * @param lenght antal tecken som skall genereras
     * @return Genererad referens.
     */
    public static String getReference(TypeOfReference type, int lenght) {

        String genRef = "";
        char[] teckenLista = null;

        switch (type) {

        case ALFA:
            teckenLista = CA_ALFA;
            break;

        case NUMERIC:
            teckenLista = CA_NUMERIC;
            break;

        default:
            teckenLista = CA_ALFANUMERIC_MIXEDCASE;
            break;

        }

        while (lenght-- > 0) {
            genRef += teckenLista[RANDOM.nextInt(teckenLista.length)];
        }

        return (TypeOfReference.ALFANUMERIC_MIXEDCASE == type) ? genRef.toUpperCase() : genRef;

    }
}
