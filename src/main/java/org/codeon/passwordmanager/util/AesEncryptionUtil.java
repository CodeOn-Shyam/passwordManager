package org.codeon.passwordmanager.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
public class AesEncryptionUtil{
    private static final String SECRET_KEY = "1234567890123456";
    private static  final String ALGORITHM = "AES";

    private static SecretKeySpec getKeySpec(){
        byte[] keyBytes = SECRET_KEY.getBytes();
        return new SecretKeySpec(keyBytes,ALGORITHM);
    }
    public static String encrypt(String plainText){
        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE,getKeySpec());
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }catch(Exception e){
            throw new RuntimeException("Error while encrypting",e);
        }
    }
    public static String decrypt(String ciphertext){
        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,getKeySpec());
            byte[] decodeBytes = Base64.getDecoder().decode(ciphertext);
            byte[] decrytedBytes = cipher.doFinal(decodeBytes);
            return new String(decrytedBytes);
        }catch(Exception e){
            throw new RuntimeException("Error while decrypting",e);
        }
    }
}