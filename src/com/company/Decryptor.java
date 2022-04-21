package com.company;

import java.io.UnsupportedEncodingException;


class Vigenere {
    private char[] mletters= {'`', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']', '\\', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', ';', '\'', 'z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '/', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', '{', '}', '|', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', ':', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '<', '>', '?', ' '};
    private char[][] mvigenere = new char[mletters.length][mletters.length];
    private char[] mkey = "qwerty1@3$5^7&6%4#2!".toCharArray();
    private char[] mkeyTiled = null;
    private boolean mdebug = false;

    public Vigenere(boolean adebug) {
        if (adebug)
            mdebug = true;
        generateVigenere();
    }

    public Vigenere(String ps) {
        mkey = ps.toCharArray();
        generateVigenere();
    }

    public void setKey(char[] apass) {
        mkey = apass;
        if (mdebug)  System.out.println("Vigenere.setKet:  Vigenere key set successfully");
    }

    public char[] encryptMsg(char amsg[]) {
        if (mkey == null)
        {
            if (mdebug)
                System.out.println("Vigenere.encyptMsg: no key; msg return w/o encryption");
            return amsg;
        }

        //if (mdebug)
        //{
        //  print("Vigenere.encyptMsg: source msg (binary) = ");
        //  for ( int j = 0; j < amsg.length; j++)
        //  {
        //    print(binary(amsg[j]));
        //  }
        //  println();
        //}

        // extend the key to be as long as message
        mkeyTiled = new char[amsg.length];
        if (mdebug)  System.out.println("Vigenere.encyptMsg: extended key = ");
        for (int i = 0; i < amsg.length; i++)
        {
            int pos = i%mkey.length;
            mkeyTiled[i] = mkey[pos];
            if (mdebug)  System.out.println(mkey[pos]);
        }
        if (mdebug)
        {
            System.out.println();
            System.out.println("Vigenere.encyptMsg: extended key length = " + mkeyTiled.length);
        }

        // create encryped message
        if (mdebug)  System.out.println("Vigenere.encyptMsg: encrypted source msg = ");
        char encMsg[] = new char[amsg.length];
        for ( int j = 0; j < amsg.length; j++)
        {
            int row = findChar(mletters, mkeyTiled[j]);
            int col = findChar(mletters, amsg[j]);
            if (row == -1 || col == -1)
            {
                if (mdebug)  System.out.println("\nVigenere.encyptMsg: unrecognized character; msg return w/o encryption");
                return amsg;
            }
            encMsg[j] = mvigenere[row][col];
            if (mdebug)  System.out.println(encMsg[j]);
        }
        if (mdebug)  System.out.println();

        if (mdebug)
        {

            for ( int j = 0; j < amsg.length; j++)
            {
                //System.out.println(binary(encMsg[j]));
            }
            System.out.println();
        }

        return encMsg;
    }

    public char[] decryptMsg(char amsg[]) {
        if (mkey == null)
        {
            if (mdebug)
                System.out.println("Vigenere.decyptMsg: no key; msg return w/o decryption");
            return amsg;
        }


        // extend the key to be as long as message
        mkeyTiled = new char[amsg.length];
        if (mdebug)  System.out.println("Vigenere.encyptMsg: extended key = ");
        for (int i = 0; i < amsg.length; i++)
        {
            int pos = i%mkey.length;
            mkeyTiled[i] = mkey[pos];
            if (mdebug)  System.out.println(mkey[pos]);
        }
        if (mdebug)
        {
            System.out.println();
            System.out.println("Vigenere.encyptMsg: extended key length = " + mkeyTiled.length);
        }


        // create decryped message
        if (mdebug)
        {
            System.out.println("Vigenere.decyptMsg: source msg = ");
            for (int i = 0; i < amsg.length; i++)
            {
                System.out.println(amsg[i]);
            }
            System.out.println();
        }

        if (mdebug)  System.out.println("Vigenere.decyptMsg: decryped msg = ");
        char decMsg[] = new char[amsg.length];
        for ( int j = 0; j < amsg.length; j++)
        {
            int row = findChar(mletters, mkeyTiled[j]);
            int col = findChar(mvigenere[row], amsg[j]);
            if (row == -1 || col == -1)
            {
                if (mdebug)  System.out.println("\nVigenere.decyptMsg: unrecognized character; msg return w/o decryption");
                return amsg;
            }
            decMsg[j] = mletters[col];
            if (mdebug)  System.out.println(decMsg[j]);
        }
        if (mdebug)  System.out.println();

        if (mdebug)
        {

            for ( int j = 0; j < amsg.length; j++)
            {
                System.out.println(decMsg[j]);
            }
            System.out.println();
        }

        return decMsg;
    }

    private void generateVigenere() {
        if (mdebug)  System.out.println("Vigenère Table");

        for (int k = 0; k < mletters.length; k++)
        {
            for (int l = 0; l < mletters.length; l++)
            {
                int sum = k+l;
                if (sum >= mletters.length)
                    sum = sum -mletters.length;
                mvigenere[l][k] = mletters[sum];
                if (mdebug)  System.out.println(mvigenere[l][k]);
            }
            if (mdebug)  System.out.println();
        }
    }  // end of Method generateVigenere

    private int findChar(char[] aletters, char c) {
        for (int i = 0; i < aletters.length; i++)
        {
            if (aletters[i] == c)
                return i;
        }
        return -1;
    }
}

public class Decryptor {

    private Vigenere encryption;

    public Decryptor(){
        encryption = new Vigenere(false);
    }

    public Decryptor(String password){
        encryption = new Vigenere(password);
    }

    public String encrypt(String message){
        return new String(encryption.encryptMsg(message.toCharArray()));
    }

    public char[] decrypt(char[] message){
        return encryption.decryptMsg(message);
    }

}
