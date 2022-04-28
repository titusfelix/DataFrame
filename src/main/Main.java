package main;

import java.util.Arrays;

public class Main {
    private static void print(DataFrame dataFrame) {
        System.out.println("Coloane:");
        System.out.println(Arrays.toString(dataFrame.getColumns()));
        System.out.println("Index:");
        System.out.println(Arrays.toString(dataFrame.getIndex()));
        System.out.println("Coloane numerice/nenumerice:");
        System.out.println(Arrays.toString(dataFrame.getNumeric()));
//            double[][] data = dataFrame.getData(new String[]{"I1","I2a","N","R1a"});
        System.out.println("Date:");
        double[][] data = dataFrame.getData();
        for (double[] v : data) {
            System.out.println(Arrays.toString(v));
        }
        System.out.println("Coloane numerice:" + Arrays.toString(dataFrame.getNumericColumns()));
    }

    public static void main(String[] args) {
        try {
            DataFrame dataFrame1 = new DataFrame("ADN_Tari.csv", ",", true, 0);
            System.out.println("----->Citire cu antet si index din coloana 0:\n");
            print(dataFrame1);

            DataFrame dataFrame2 = new DataFrame("ADN_Tari.csv", ",", true, -1);
            System.out.println("\n----->Citire cu antet si fara index:\n");
            print(dataFrame2);

            DataFrame dataFrame3 = new DataFrame("ADN_Tari_.csv", ",", false, 1);
            System.out.println("\n----->Citire fara antet si cu index din coloana 1:\n");
            print(dataFrame3);

            DataFrame dataFrame4 = new DataFrame("ADN_Tari_.csv", ",", false, -1);
            System.out.println("\n----->Citire fara antet si fara index:\n");
            print(dataFrame4);

        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
