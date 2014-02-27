/**
 * 
 */
package com.gbli.common;

import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Simple3Des {


 
    private String key = "123456789012345678901234";
    private String initializationVector = "iloveyou";
    public Simple3Des()
    {

    }

    public String encryptText(String plainText) throws Exception{
    //---- Use specified 3DES key and IV from other source --------------
    byte[] plaintext = plainText.getBytes();
    byte[] tdesKeyData = key.getBytes();

    // byte[] myIV = initializationVector.getBytes();

    Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
    IvParameterSpec ivspec = new IvParameterSpec(initializationVector.getBytes());

    c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
    byte[] cipherText = c3des.doFinal(plaintext);

    return new sun.misc.BASE64Encoder().encode(cipherText);
    }
    
    public String decryptText(String cipherText)throws Exception{
    byte[] encData = new sun.misc.BASE64Decoder().decodeBuffer(cipherText);
    Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    byte[] tdesKeyData = key.getBytes();
    SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
    IvParameterSpec ivspec = new IvParameterSpec(initializationVector.getBytes());
    decipher.init(Cipher.DECRYPT_MODE, myKey, ivspec);
    byte[] plainText = decipher.doFinal(encData);
    return new String(plainText);
    }
    }

    
//      private KeySpec keySpec;
//      private SecretKey key;
//      private IvParameterSpec iv;
//      
//      public Simple3Des() {
//        try {
//          final MessageDigest md = MessageDigest.getInstance("md5");
//          final byte[] digestOfPassword = md.digest(Base64.decodeBase64(keyBytes));
//          final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
//          for (int j = 0, k = 16; j < 8;) {
//            keyBytes[k++] = keyBytes[j++];
//          }
//          
//          keySpec = new DESedeKeySpec(keyBytes);
//          
//          key = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);
//          
//          iv = new IvParameterSpec(vctBytes);
//        } catch(Exception e) {
//          e.printStackTrace();
//        }
//      }
//      
//      public String encrypt(String value) {
//        try {
//          Cipher ecipher = Cipher.getInstance("DESede/CBC/PKCS5Padding","SunJCE");
//          ecipher.init(Cipher.ENCRYPT_MODE, key, iv);
//          
//          if(value==null)
//            return null;
//          
//          // Encode the string into bytes using utf-8
//          byte[] utf8 = value.getBytes("UTF8");
//          
//          // Encrypt
//          byte[] enc = ecipher.doFinal(utf8);
//          
//          // Encode bytes to base64 to get a string
//          return new String(Base64.encodeBase64(enc),"UTF-8");
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//        return null;
//      }
//      
//      public String decrypt(String value) {
//        try {
//          Cipher dcipher = Cipher.getInstance("DESede/CBC/PKCS5Padding","SunJCE");
//          dcipher.init(Cipher.DECRYPT_MODE, key, iv);
//          
//          if(value==null)
//            return null;
//          
//          // Decode base64 to get bytes
//          byte[] dec = Base64.decodeBase64(value.getBytes());
//          
//          // Decrypt
//          byte[] utf8 = dcipher.doFinal(dec);
//          
//          // Decode using utf-8
//          return new String(utf8, "UTF8");
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//        return null;
//      }
//    } 
//    private static String algorithm = "DESede";
//    private static String transformation = "DESede/CBC/PKCS5Padding";
//
//    static {
//        int keySize = 168;
//        int ivSize = 8;
//        try {
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
//            keyGenerator.init(keySize);
//            DESedeKeySpec keySpec = new DESedeKeySpec(keyBytes);
//
//            sharedkey = SecretKeyFactory.getInstance(algorithm).generateSecret(keySpec);
//            sharedvector = new byte [ivSize];
//            byte [] data = sharedkey.getEncoded();
//
//            int half = ivSize / 2;
//            System.arraycopy(data, data.length-half, sharedvector, 0, half);
//            System.arraycopy(sharedvector, 0, sharedvector, half, half);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//		} catch (InvalidKeyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidKeySpecException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//
//    public static void main(String [] args) throws Exception {
//        System.out.println(Decrypt(Encrypt("Hello World")));
//
//    }
//
//    public static String Encrypt(String val) throws GeneralSecurityException, UnsupportedEncodingException {
//        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, sharedkey, new IvParameterSpec(sharedvector));
//
//        return new String(Base64.encodeBase64(cipher.doFinal(val.getBytes("utf-8"))));
//    }
//
//    public static String Decrypt(String val) throws GeneralSecurityException, IOException {
//        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
//        cipher.init(Cipher.DECRYPT_MODE, sharedkey, new IvParameterSpec(sharedvector));
//
//        return new String(cipher.doFinal(Base64.decodeBase64(val.getBytes("utf-8"))));
//    }
//    
    
