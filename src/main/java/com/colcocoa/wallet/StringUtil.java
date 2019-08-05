package com.colcocoa.wallet;

import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;

import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECCurve;

public class StringUtil {
	//Applies Sha256 to a string and returns the result. 
		public static String applySha256(String input){		
			try {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");	        
				//Applies sha256 to our input, 
				byte[] hash = digest.digest(input.getBytes("UTF-8"));	        
				StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
				for (int i = 0; i < hash.length; i++) {
					String hex = Integer.toHexString(0xff & hash[i]);
					if(hex.length() == 1) hexString.append('0');
					hexString.append(hex);
				}
				return hexString.toString();
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
			Signature dsa;
			byte[] output = new byte[0];
			try {
				dsa = Signature.getInstance("ECDSA", "BC");
				dsa.initSign(privateKey);
				byte[] strByte = input.getBytes();
				dsa.update(strByte);
				byte[] realSig = dsa.sign();
				output = realSig;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return output;
		}
		
		//Verifies a String signature 
		public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
			try {
				Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
				ecdsaVerify.initVerify(publicKey);
				ecdsaVerify.update(data.getBytes());
				return ecdsaVerify.verify(signature);
			}catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		public static PublicKey getPublicKey(String encodedKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException{
			byte[] publicKeyBytes = Base64.getDecoder().decode(encodedKey); 
			ECNamedCurveParameterSpec params = ECNamedCurveTable.getParameterSpec("prime192v1");
		    KeyFactory fact = KeyFactory.getInstance("ECDSA", "BC");
		    ECCurve curve = params.getCurve();
		    java.security.spec.EllipticCurve ellipticCurve = EC5Util.convertCurve(curve, params.getSeed());
		    java.security.spec.ECPoint point = ECPointUtil.decodePoint(ellipticCurve, publicKeyBytes);
		    java.security.spec.ECParameterSpec params2 =EC5Util.convertSpec(ellipticCurve, params);
		    java.security.spec.ECPublicKeySpec keySpec = new java.security.spec.ECPublicKeySpec(point,params2);
		    return (ECPublicKey) fact.generatePublic(keySpec);
		    
		}

		public static String getStringFromKey(Key key) {
			return Base64.getEncoder().encodeToString(key.getEncoded());
		}
		
		public static String getMerkleRoot(ArrayList<Transaction> transactions) {
			int count = transactions.size();
			ArrayList<String> previousTreeLayer = new ArrayList<String>();
			for(Transaction transaction : transactions) {
				previousTreeLayer.add(transaction.getTransactionId());
			}
			ArrayList<String> treeLayer = previousTreeLayer;
			while(count > 1) {
				treeLayer = new ArrayList<String>();
				for(int i=1; i < previousTreeLayer.size(); i++) {
					treeLayer.add(applySha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
				}
				count = treeLayer.size();
				previousTreeLayer = treeLayer;
			}
			String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
			return merkleRoot;
		}
		public static String getDificultyString(int difficulty) {
			int dificult = difficulty * 0;
			return String.valueOf(dificult);
			}
}
