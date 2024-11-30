package ru.otus.cryptography.hw1;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Text {

    private final List<Letter> letters = new ArrayList<>();

    public Text(List<Letter> letters) {
        this.letters.addAll(letters);
    }

    public static Text fromHex(String hex) {
        var text = new Text(new ArrayList<>());
        for (var i = 0; i < hex.length(); i = i + 2) {
            text.letters.add(Letter.fromHex(hex.substring(i, i + 2)));
        }
        return text;
    }

    public static Text fromAscii(String ascii) {
        var text = new Text(new ArrayList<>());
        for (var i = 0; i < ascii.length(); i++) {
            text.letters.add(Letter.fromAscii(ascii.charAt(i)));
        }
        return text;
    }

    public Text xor(Text key) {
        var result = new ArrayList<Letter>();
        for (int i = 0; i < Math.min(letters.size(), key.letters.size()); i++) {
            result.add(letters.get(i).xor(key.letters.get(i)));
        }
        return new Text(result);
    }

    public String printHex() {
        return letters.stream().map(Letter::getHex).collect(Collectors.joining());
    }

    public String printAscii() {
        return letters.stream().map(Letter::getStr).collect(Collectors.joining());
    }

    public Letter getLetter(int i) {
        return letters.get(i);
    }

    public int size() {
        return letters.size();
    }

    @Getter
    @EqualsAndHashCode
    public static class Letter {
        private final String hex;
        private final char ascii;
        private final String str;

        public Letter(String hex, char ascii) {
            this.hex = hex;
            this.ascii = ascii;
            this.str = String.valueOf(ascii);
        }

        public static Letter fromHex(String hex) {
            return new Letter(hex, hexToAscii(hex));
        }

        public static Letter fromAscii(char ascii) {
            return new Letter(asciiToHex(ascii), ascii);
        }

        private static char hexToAscii(String hex) {
            // "7a" to "z"
            return (char)Integer.parseInt(hex, 16);
        }

        private static String asciiToHex(char ascii) {
            // "z" to "7a"
            return String.format("%02x", (int) ascii);
        }

        public Letter xor(Letter key) {
            if (key == null) {
                return fromAscii(ascii);
            }
            return fromAscii((char) ((byte) ascii ^ (byte) key.ascii));
        }

        public boolean isUpperCase() {
            return String.valueOf(ascii).matches("[A-Z]");
        }

        public boolean isEngLetter() {
            return str.matches("[a-zA-Z]");
        }

        public Letter toUpperCase() {
            return Text.Letter.fromAscii(str.toUpperCase().charAt(0));
        }

        public Letter toLowerCase() {
            return Text.Letter.fromAscii(str.toLowerCase().charAt(0));
        }

        public Letter invertCase() {
            return isUpperCase() ? toLowerCase() : toUpperCase();
        }

        @Override
        public String toString() {
            return str;
        }
    }
}
