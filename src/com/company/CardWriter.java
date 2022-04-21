package com.company;
import processing.core.PImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static processing.core.PApplet.*;

public class CardWriter {

    Steg steg;

    public CardWriter(){
        steg = new Steg();
    }

    public enum payloadOption{
        openDoor,lockDoor,modifyWhiteList,
        fakeAttendance
    }

    public void pushPayload(User user, payloadOption option){
        steg.setImg(new PImage(user.getImage()));
        if(option == payloadOption.openDoor){
            steg.setMsg("".toCharArray());
        }else if(option == payloadOption.lockDoor){
            steg.setMsg("".toCharArray());
        }else if(option == payloadOption.modifyWhiteList){
            steg.setMsg("".toCharArray());
        }else if(option == payloadOption.fakeAttendance){
            steg.setMsg("".toCharArray());
        }
    }

    public void changeText(String text, File outputfile) throws IOException {

        char buffer[] = new char[text.length()];

        for(int i = 0; i < text.length(); i++){
            buffer[i] = text.charAt(i);
        }

        steg.setMsg(buffer);
        steg.hide();

        BufferedImage image = (BufferedImage) steg.getImg().getImage();

        ImageIO.write(image, "png", outputfile);
    }

    public char[] getMessageFromImage(BufferedImage image){
        steg.setImg(new PImage(image));
        return steg.unHide();
    }

}

class Steg {
    private PImage mimg = null;
    private char mmsg[] = null;
    private boolean mdebug = false;
    private long mimgCap;  // image information capacity (in bytes)
    private int mmask = (1 << 16) | (1 << 8) | (1 << 0);


    public Steg() {
    }

    public void setImg(PImage aimg) {
        mimg = aimg;
        mimg.loadPixels();
        mimgCap = floor((3 * mimg.pixels.length - 32) / 8);

        if (mdebug) {
            println("Steg.setImg: image size (pixels) = " + mimg.pixels.length);
            println("Steg.setImg: image size (bytes) = " + 3 * mimg.pixels.length);
            println("Steg.setImg: target capacity (chars) = " + mimgCap);
        }
    }

    public PImage getImg() {
        return mimg;
    }


    public void setMsg(char amsg[]) {
        mmsg = amsg;
    }


    public PImage hide() {
        if (mimg == null || mmsg == null) {
            if (mdebug)
                println("Steg.hide: source message or target image missing");
            return new PImage();
        }

        PImage img = mimg;
        double DR = (double) mmsg.length / (double) mimgCap;//(double(mmsg.length)/double(mimgCap))*100;
        long PS = floor((float) (1 / DR));
        if (mdebug) {
            println("Steg.hide: info density factor = " + DR);
            println("Steg.hide: data Pixel Spacing = " + PS);
        }

        img.loadPixels();
        /* store the message size in the first 32 bytes of the image */
        char msgSize[] = binary(mmsg.length).toCharArray();
        int bitItr = 0;
        int pixItr = 0;


        while (bitItr < msgSize.length) {
            img.pixels[pixItr] &= ~mmask;  // clear the least significant bits in the color channels
            if (mdebug) println("pixel " + pixItr + " post mask = " + binary(img.pixels[pixItr]));

            // step through each color channel
            for (int i = 0; i < 3; i++) {
                if (bitItr + i == msgSize.length)
                    break;

                boolean ee = msgSize[bitItr + i] == '1';

                int temp;

                if(ee){
                    temp = 0 | 1;
                }else{
                    temp = 0 | 0;
                }
                //
                // get the bit in a usable format
                img.pixels[pixItr] |= (temp << (8 * i));
                if (mdebug) println("pixel " + pixItr + " edit mask = " + binary(temp << 8 * i));
                if (mdebug) println("pixel " + pixItr + " post edit = " + binary(img.pixels[pixItr]));
            }
            pixItr++;
            bitItr += 3;
            if (mdebug) println();
        }
        if (mdebug) println("Finished writing message size to image\n");

        /* encode message into the rest of the image*/
        if (mdebug) println("Started writing message body to image");
        char msgBits[] = new char[mmsg.length * 8];

        // expand the characters to individual bits
        for (int i = 0; i < mmsg.length; i++) {
            for (int j = 0; j < 8; j++) {
                boolean erer;
                if((mmsg[i] & (1 << (7 - j))) >> (7 - j) != 0){
                    erer = true;
                }else{
                    erer = false;
                }
                msgBits[i * 8 + j] = erer ? '1' : '0';
                print(msgBits[i * 8 + j]);
            }
            print("\t");
        }
        println("\n");

        bitItr = 0;
        //pixItr = 11;
        while (bitItr < msgBits.length) {
            img.pixels[pixItr] &= ~mmask;  // clear the least significant bits in the color channels
            if (mdebug) println("pixel " + pixItr + " post mask = " + binary(img.pixels[pixItr]));

            // step through each color channel
            for (int i = 0; i < 3; i++) {
                if (bitItr + i == msgBits.length - 1)
                    break;

                boolean ede = msgSize[bitItr + i] == '1';
                int temp;

                if(ede){
                    temp = 0 | 1;
                }else{
                    temp = 0 | 0;
                }

                // get the bit in a usable format
                img.pixels[pixItr] |= (temp << (8 * i));
                // get the bit in a usable format
                img.pixels[pixItr] |= (temp << (8 * i));
                if (mdebug) println("pixel " + pixItr + " edit mask = " + binary(temp << 8 * i));
                if (mdebug) println("pixel " + pixItr + " post edit = " + binary(img.pixels[pixItr]));
            }
            pixItr += PS;
            bitItr += 3;
            if (mdebug) println();
        }
        if (mdebug) println("Finished writing message body to image\n");


        img.updatePixels();

        return img;
    }


    public char[] unHide() {
        if (mdebug) println("Steg.unHide: entered method");
        if (mimg == null) {
            if (mdebug)
                println("Steg.unHide: source image missing");
            return new char[0];
        }

        PImage img = mimg;

        img.loadPixels();
        /* store the message size in the first 32 bytes of the image */
        char msgSize[] = new char[32];
        int bitItr = 0;
        int pixItr = 0;

        if (mdebug) println("fetching message size: ");

        while (bitItr < msgSize.length) {
            // step through each color channel
            for (int i = 0; i < 3; i++) {
                if (bitItr + i == msgSize.length)
                    break;
                if (mdebug) println("pixel " + pixItr);
                if (mdebug) println("\tmask = " + binary((1 << 8 * i)));
                if (mdebug) println("\tpixel = " + binary(img.pixels[pixItr]));
                if (mdebug) println("\tdata bit = " + binary(((1 << 8 * i) & img.pixels[pixItr]) >> 8 * i));


                msgSize[bitItr + i] = (binary(((1 << 8 * i) & img.pixels[pixItr]) >> 8 * i).toCharArray()[31]);
                if (mdebug) println("\textracted size bit = " + msgSize[bitItr + i]);
            }
            pixItr++;
            bitItr += 3;
            if (mdebug) println();
        }
        if (mdebug) {
            print("message size = ");//binary(int(msgSize.toString())));
            for (int i = 0; i < msgSize.length; i++) {
                print(msgSize[i]);
            }
            println();
        }

        // get decimal value of size
        int msg_length = 0;
        for (int i = 0; i < 32; i++) {

            int erer;

            if(msgSize[31 - i] == '1'){
                erer = 1;
            }else{
                erer = 0;
            }

            msg_length += erer * pow(2, i);
        }

        double DR = (double) msg_length / (double) mimgCap;//(double(mmsg.length)/double(mimgCap))*100;
        long PS = floor((float) (1 / DR));

        // get the message body
        char msgBits[] = new char[msg_length * 8];
        bitItr = 0;
        // pixItr = 11;

        if (mdebug) println("fetching message body: ");

        while (bitItr < msgBits.length) {
            // step through each color channel
            for (int i = 0; i < 3; i++) {
                if (bitItr + i == msgBits.length)
                    break;

                msgBits[bitItr + i] = (binary(((1 << 8 * i) & img.pixels[pixItr]) >> 8 * i).toCharArray()[31]);
                if (mdebug) println("\textracted data bit = " + msgBits[bitItr + i]);
            }
            pixItr += PS;
            bitItr += 3;
            if (mdebug) println();
        }

        int msgInt[] = new int[msg_length];
        char msg[] = new char[msg_length];
        for (int i = 0; i < msg_length; i++) {
            for (int j = 0; j < 8; j++) {

                int erer;

                if(msgBits[i * 8 + (7 - j)] == '1'){
                    erer = 1;
                }else{
                    erer = 0;
                }

                msgInt[i] += erer * pow(2, j);
            }
            msg[i] = (char)msgInt[i];
        }

        return msg;
    }
}
