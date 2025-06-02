package dev.samuel.currencyconverter.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.samuel.currencyconverter.config.ConfigLoader;
import dev.samuel.currencyconverter.dto.PairConversionDTO;
import dev.samuel.currencyconverter.dto.SupportedCurrenciesDTO;
import dev.samuel.currencyconverter.exception.NotValidCurrencyCodeException;

public class CurrencyApiClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String baseUrl = "https://v6.exchangerate-api.com/v6/" + ConfigLoader.getApiKey() + "/";
    private List<String> supportedCurrencies;

    private String sendRequest(String endpoint) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(this.baseUrl + endpoint)).build();
            HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Request failed", e);
        }
    }

    private List<String> getSupportedCurrencies() {
        if (this.supportedCurrencies == null) {
            SupportedCurrenciesDTO supportedCurrencies = gson.fromJson(sendRequest("codes"), SupportedCurrenciesDTO.class);
            this.supportedCurrencies = supportedCurrencies.supported_codes().stream()
                    .map(List::getFirst)
                    .toList();
        }

        return this.supportedCurrencies;
    }

    public void isValidCurrency(String currency) throws NotValidCurrencyCodeException {
        if (currency == null || currency.length() != 3) {
            throw new NotValidCurrencyCodeException(currency + " is not a valid 3 digit currency code");
        }

        if (!getSupportedCurrencies().contains(currency)) {
            throw new NotValidCurrencyCodeException(currency + " was not found in the supported list.");
        }
    }

    public String convertCurrency(String source, String target, double amount) {
        PairConversionDTO pairConversion = gson.fromJson(sendRequest("pair/" + source + "/" + target + "/" + amount), PairConversionDTO.class);
        return String.format("%.2f", pairConversion.conversion_result());
    }
}
