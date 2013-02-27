package org.asterisk.utility;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESControl {
    private Cipher Cip;
    private SecretKeySpec KeySpecAES;   
    private final static String UTF8 = "UTF-8";
    private final static String MD5 = "MD5";
    private final static String AES = "AES";
    private byte[] key;
    private MessageDigest sha;   

    public AESControl(String strKey) {
        super();
        try {
            key = strKey.getBytes(UTF8);
            sha = MessageDigest.getInstance(MD5);
            key = sha.digest(key);
            // Generate the secret key specs.
            KeySpecAES = new SecretKeySpec(key, AES);
        } catch (Exception e) {
               
        }					
    }

    public String encryptData(String input){
        try{
            Cip = Cipher.getInstance(AES);
            Cip.init(Cipher.ENCRYPT_MODE, KeySpecAES); // ma hoa su dung keySpec
            byte[] utf8 = input.getBytes("UTF8");
            byte[] enc = Cip.doFinal(utf8);
            return Base64.encode(enc).toString();
        }catch(Exception e){
            return null;
        }
    }
    public String decryptData(String input){
        try{
            Cip = Cipher.getInstance(AES);
            Cip.init(Cipher.DECRYPT_MODE, KeySpecAES); // ma hoa su dung keySpec
            byte[] dec = Base64.decode(input);
            // Decrypt
            byte[] utf8 = Cip.doFinal(dec);
            // Decode using utf-8
            return new String(utf8, "UTF8");
        }catch(Exception e){
            return null;
        }
    }
}