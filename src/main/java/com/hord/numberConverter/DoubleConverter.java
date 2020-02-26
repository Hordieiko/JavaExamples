package com.hord.numberConverter;

public class DoubleConverter {

    public static void main(String[] args) {
        Double shortFormParamValue = 5000.50;
        long fractionalPart = new Double(Math.round(shortFormParamValue % 1 * 100)).longValue();
        long integralPart = shortFormParamValue.longValue();
        String result = convert(integralPart, false) + " and " + convert(fractionalPart, true);
        System.out.println(result.toUpperCase());
    }

    private static String convert(Long amount, boolean fractional) {
        return NumbersToWordsConverter.convert(amount).trim() + (fractional ? " Cents" : " Dollars");
    }
}
