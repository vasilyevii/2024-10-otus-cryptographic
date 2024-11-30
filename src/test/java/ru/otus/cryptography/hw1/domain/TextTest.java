package ru.otus.cryptography.hw1.domain;

import org.junit.jupiter.api.Test;
import ru.otus.cryptography.hw1.Text;

import static org.junit.jupiter.api.Assertions.*;

class TextTest {

    @Test
    void test() {
        var expectedAscii = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()_+ ";
        var expectedHex = "4142434445464748494a4b4c4d4e4f505152535455565758595a6162636465666768696a6b6c6d6e6f707172737475767778797a3132333435363738393021402324255e262a28295f2b20";

        // from hex
        var text = Text.fromAscii(expectedAscii);
        assertEquals(expectedHex, text.printHex());
        assertEquals(expectedAscii, text.printAscii());

        // from ascii
        assertEquals(Text.fromAscii(expectedAscii), Text.fromHex(expectedHex));

        // xor
        var key = Text.fromHex("315c4eeaa8b5f8aaf9174145bf43e1784b8fa00dc71d885a804e5ee9fa40b16349c146fb778cdf2d3aff021dfff5b403b510d0d0455468aeb98622b137dae857553ccd8883a7bc37520e06e515d22c954eba5025b8cc57ee59418ce7dc6bc41556bdb36bbca3e8774301fbcaa3b83b220809560987815f65286764703de0f3d524400a19b159610b11ef3e");
        var expectedCiphertextHex = "701e0dffaeffedfff3ffbfffe2ffb05d0a09fff20dffae281affddfff359ff924bffdf02ffd9143fff8bff9924ffd4052effa92fff911cffe0ffb24355ff8f736fff8cff81ffc175ffc268ffa9ffaa74665bff9aff8cffb015ff890effeaffc9177618ffe8ffd6ffa5ff8dff941e0d2526";
        assertEquals(expectedCiphertextHex, text.xor(key).printHex());
    }

}