package net.sf.robocode.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class compareTest {
    private AlphanumericComparator alphanumericComparator = new AlphanumericComparator();

    /**
     * Generates a set of random alphabetical sequences.
     *
     * @param size   The number of sequences to generate.
     * @param length The length of each sequence.
     * @return A list of randomly generated sequences.
     */
    public static List<String> generateSequences(int size, int length) {
        List<String> sequences = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            StringBuilder sequence = new StringBuilder();
            for (int j = 0; j < length; j++) {
                char randomChar = (char) (random.nextInt(26) + 'a');
                sequence.append(randomChar);
            }
            sequences.add(sequence.toString());
        }

        return sequences;
    }

    /**
     * SelfDesigned comparator for random test
     * @param str1
     * @param str2
     * @return
     */
    public int compare(String str1, String str2) {
        if (str1 == null) {
            return (str2 == null) ? 0 : 1;
        }
        if (str2 == null) {
            return -1;
        }
        if (str1.equals(str2)) {
            return 0;
        }

        String[] tokens1 = str1.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        String[] tokens2 = str2.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

        int minLength = Math.min(tokens1.length, tokens2.length);
        for (int i = 0; i < minLength; i++) {
            String token1 = tokens1[i];
            String token2 = tokens2[i];

            if (isNumeric(token1) && isNumeric(token2)) {
                int num1 = Integer.parseInt(token1);
                int num2 = Integer.parseInt(token2);
                if (num1 != num2) {
                    return Integer.compare(num1, num2);
                }
            } else {
                int result = token1.compareToIgnoreCase(token2);
                if (result != 0) {
                    return result;
                }
            }
        }
        return Integer.compare(tokens1.length, tokens2.length);
    }

    /**
     * Help method for checking is numeric
     * @param str
     * @return
     */
    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }


    /**
     * static test for comparator
     */
    @Test
    public void comparatorStaticTest() {
        String A = "abc";
        String B = "bcd";
        String C = "cde";
        String D = "acd";

        int result_A_B = alphanumericComparator.compare(A, B);
        int result_B_C = alphanumericComparator.compare(B, C);
        int result_C_A = alphanumericComparator.compare(C, A);
        int result_A_D = alphanumericComparator.compare(A, D);

        Assert.assertEquals(-1, result_A_B);
        Assert.assertEquals(-1, result_B_C);
        Assert.assertEquals(2, result_C_A);
        Assert.assertEquals(-1, result_A_D);
    }

    @Test
    public void comparatorRandomTest() {
        Random random = new Random();
        for (int loop = 0; loop < 10000; loop++) {
            List<String> randomCharList = generateSequences(2, 1 + random.nextInt(10));
            int result = alphanumericComparator.compare(randomCharList.get(0), randomCharList.get(1));
            int expect = compare(randomCharList.get(0), randomCharList.get(1));
            System.out.println("Test passed " + loop);
            Assert.assertEquals(expect, result);
        }
    }
}
