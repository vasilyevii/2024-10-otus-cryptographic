package ru.otus.cryptography.hw1;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Decoder {
    private final Map<Integer, Key> keys = new HashMap<>();

    public void findKeys(Text ciphertext1, Text ciphertext2) {
        var xored = ciphertext1.xor(ciphertext2);
        for (int l = 0; l < xored.getLetters().size(); l++) {
            var xl = xored.getLetters().get(l);
            if (xl.isEngLetter()) {
                var xlInverted = xl.invertCase();
                var cl1 = ciphertext1.getLetter(l);
                var cl2 = ciphertext2.getLetter(l);
                addKey(l, cl1.xor(xlInverted));
                addKey(l, cl2.xor(xlInverted));
            }
        }
    }

    public void sortKeys() {
        keys.forEach((pos, key) -> key.sortKeys());
    }

    public Text decrypt(Text ciphertext) {
        var keyLetters = new ArrayList<Text.Letter>(ciphertext.size());
        for (int i = 0; i < ciphertext.size(); i++) {
            var letter = keys.containsKey(i) ? keys.get(i).getLetter() : null;
            keyLetters.add(letter);
        }
        var key = new Text(keyLetters);
        return ciphertext.xor(key);
    }

    private void addKey(int position, Text.Letter letter) {
        if (!keys.containsKey(position)) {
            keys.put(position, new Key());
        }
        keys.get(position).addLetter(letter);
    }

    private static class Key {
        private final Map<Text.Letter, KeyLetter> keyLettersMap = new HashMap<>();
        private final Set<KeyLetter> keyLettersTreeSet = new TreeSet<>();

        private void addLetter(Text.Letter letter) {
            if (keyLettersMap.containsKey(letter)) {
                keyLettersMap.get(letter).increment();
            } else {
                var keyLetter = new KeyLetter(letter, 1);
                keyLettersMap.put(letter, keyLetter);
            }
        }

        private void sortKeys() {
            keyLettersMap.forEach((letter, keyLetter) -> keyLettersTreeSet.add(keyLetter));
        }

        private Text.Letter getLetter() {
            return keyLettersTreeSet.stream().findFirst().map(KeyLetter::getLetter).orElse(null);
        }

        @Data
        private static class KeyLetter implements Comparable<KeyLetter> {
            private final Text.Letter letter;
            private int count;

            private KeyLetter(Text.Letter letter, int count) {
                this.letter = letter;
                this.count = count;
            }

            @Override
            public int compareTo(KeyLetter o) {
                var i = (o.count) - (count);
                if (i == 0) {
                    return (int) letter.getAscii() > (int) o.letter.getAscii() ? 1 : -1;
                }
                return i;
            }

            private void increment() {
                count++;
            }
        }
    }
}
