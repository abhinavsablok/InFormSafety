package com.example.informsafety;

import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecrypt {
    private static final String ALGORITHM = "Blowfish";
    private static final String MODE = "Blowfish/CBC/PKCS5Padding";
    private static final String IV = "abcedfgh";
    private static final String Key = "SECERETKEY"; //replace this with your custom Key

//    public static String encrypt(String value) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(Key.getBytes(),ALGORITHM);
//        Cipher cipher = Cipher.getInstance(MODE);
//        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,new IvParameterSpec(IV.getBytes()));
//        byte[] values= cipher.doFinal(value.getBytes());
//        return Base64.encodeToString(values,Base64.DEFAULT);
//
//    }
//    public static String decrypt(String value) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//
//        byte[] values = Base64.decode(value,Base64.DEFAULT);
//        SecretKeySpec secretKeySpec=new SecretKeySpec(Key.getBytes(),ALGORITHM);
//        Cipher cipher = Cipher.getInstance(MODE);
//        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,new IvParameterSpec(IV.getBytes()));
//        return new String(cipher.doFinal(values));
//
//    }

    public static String encrypt(String value) { //throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String output = "";
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(Key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(MODE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(IV.getBytes()));
            byte[] values = cipher.doFinal(value.getBytes());
            output = Base64.encodeToString(values, Base64.DEFAULT);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String decrypt(String value) { // throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String output = "";
        try {
            byte[] values = Base64.decode(value,Base64.DEFAULT);
            SecretKeySpec secretKeySpec=new SecretKeySpec(Key.getBytes(),ALGORITHM);
            Cipher cipher = Cipher.getInstance(MODE);
            cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,new IvParameterSpec(IV.getBytes()));
            return new String(cipher.doFinal(values));
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return output;
    }

}