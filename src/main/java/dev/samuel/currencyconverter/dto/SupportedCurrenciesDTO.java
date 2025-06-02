package dev.samuel.currencyconverter.dto;

import java.util.List;

public record SupportedCurrenciesDTO(List<List<String>> supported_codes) {
}
