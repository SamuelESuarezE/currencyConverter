package dev.samuel.currencyconverter.cli;

import dev.samuel.currencyconverter.api.CurrencyApiClient;
import dev.samuel.currencyconverter.exception.NotValidCurrencyCodeException;

import java.util.Scanner;

public class CurrencyConverterCLI {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("""
        
        Welcome to the Currency Converter CLI
        ______________________________________
        """);

        CurrencyApiClient currencyApiClient = new CurrencyApiClient();

        String sourceCurrency;
        String targetCurrency;
        double amount;
        String continueOp = "";

        do {
            System.out.println("Enter the source currency code (e.g., USD): ");
            sourceCurrency = sc.nextLine().toUpperCase().trim();
            System.out.println("Enter the target currency code (e.g., GBP): ");
            targetCurrency = sc.nextLine().toUpperCase().trim();

            try {
                currencyApiClient.isValidCurrency(sourceCurrency);
                currencyApiClient.isValidCurrency(targetCurrency);
            } catch (NotValidCurrencyCodeException e) {
                System.out.println();
                System.out.println(e.getMessage());
                System.out.println("Please try again.");
                System.out.println();
                continue;
            }

            System.out.println("Enter the amount to convert (" + sourceCurrency + " to " + targetCurrency + "): ");
            amount = sc.nextDouble();
            sc.nextLine();

            System.out.println(amount + " " + sourceCurrency + " is equal to " + currencyApiClient.convertCurrency(sourceCurrency, targetCurrency, amount) + " " + targetCurrency);

            System.out.println();
            System.out.println("Press any key to continue or enter 0 to exit.");
            continueOp = sc.nextLine();
        } while (!continueOp.equals("0"));

        System.out.println();
        System.out.println("See you later!");
    }
}
