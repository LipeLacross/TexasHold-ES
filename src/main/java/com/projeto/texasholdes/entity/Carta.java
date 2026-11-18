package com.projeto.texasholdes.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Carta implements Comparable<Carta> {
    private final String valor;
    private final String naipe;

    public int getValorNumerico() {
        return switch (valor.trim().toUpperCase()) {
            case "2" -> 2;
            case "3" -> 3;
            case "4" -> 4;
            case "5" -> 5;
            case "6" -> 6;
            case "7" -> 7;
            case "8" -> 8;
            case "9" -> 9;
            case "10" -> 10;
            case "VALETE", "J" -> 11;
            case "DAMA", "Q" -> 12;
            case "REI", "K" -> 13;
            case "ÁS", "AS", "A" -> 14;
            default -> 0;
        };
    }

    public static Carta parse(String cartaStr) {
        if (cartaStr == null || cartaStr.isBlank()) {
            return new Carta("2", "Copas");
        }
        String[] partes = cartaStr.split(" de ");
        if (partes.length == 2) {
            return new Carta(partes[0].trim(), partes[1].trim());
        }
        return new Carta(cartaStr.trim(), "Copas");
    }

    @Override
    public int compareTo(Carta outra) {
        return Integer.compare(this.getValorNumerico(), outra.getValorNumerico());
    }

    @Override
    public String toString() {
        return valor + " de " + naipe;
    }
}
