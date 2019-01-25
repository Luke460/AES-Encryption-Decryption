package testEncription;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import com.google.common.hash.Hashing;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import LukePack.LP;
import encryption.AdvancedAesEncryptor;
import encryption.SpringEncryptor;
import encryption.EncryptionUtils;

import static encryption.AdvancedAesEncryptor.IV_BYTE_LENGTH;
import static encryption.AdvancedAesEncryptor.KEY_BYTE_LENGTH;
import static org.junit.Assert.assertEquals;

public class EncryptionTest {

	final static String TOKEN = "N4040NC04NDM4N947CM1847C984N7NM2V5656VC73465B4534N5436DC3M34M87R9349X4YRM9XR43M8MGXG348R743GRG43R438XG38T";
	
	@Test
	public void testAdvancedAesEncryptor() throws Exception {

		String payload = LP.readFile("payload.txt");
		//System.out.println("Original message:   " + payload);
		
		long t0 = System.currentTimeMillis();		
		String hexHashedToken = (Hashing.sha384().hashString(TOKEN, StandardCharsets.UTF_8)).toString();
		System.out.println(new String("SHA 384 Token:      " + hexHashedToken));
		
		//byte[] hashedToken = convertHexaToByteArray(hexHashedToken);
		byte[] hashedToken = Hex.decodeHex(hexHashedToken.toCharArray());
		byte[] sk = Arrays.copyOfRange(hashedToken,0,KEY_BYTE_LENGTH);	
		byte[] iv = Arrays.copyOfRange(hashedToken,KEY_BYTE_LENGTH,KEY_BYTE_LENGTH+IV_BYTE_LENGTH);

		//String sk = JWE.generateRandomB64SecretKey();
		System.out.println("Password:           " + EncryptionUtils.getStringFromByteArray(sk));
		System.out.println("I.V.                " + EncryptionUtils.getStringFromByteArray(iv));

		String encryptedString = AdvancedAesEncryptor.encrypt(payload,sk,iv);
		System.out.println("Encrypted msg b64:  " + encryptedString);
		String decryptedString = AdvancedAesEncryptor.decrypt(encryptedString,sk,iv);
		//System.out.println("Decrypted message:  " + decryptedString);
		assertEquals(payload,decryptedString);
		
		long tf = System.currentTimeMillis();
		
		long totalTime = tf - t0;
		EncryptionUtils.showStats("Advanced Encryption", payload, encryptedString, totalTime);
	}
	/*

	@Test
	public void testSpringEncryption() throws Exception {
		String payload = LP.readFile("payload.txt");
		//System.out.println("Original message:   " + payload);
		
		long t0 = System.currentTimeMillis();		
		System.out.println(new String("Token:              " + TOKEN));
		String encryptedString = EncryptionUtility.encrypt(payload,TOKEN);
		//System.out.println("Encrypted msg b64:  " + encryptedString);
		String decryptedString = EncryptionUtility.decrypt(encryptedString,TOKEN);
		//System.out.println("Decrypted message:  " + decryptedString);
		assertEquals(payload,decryptedString);
		
		long tf = System.currentTimeMillis();

		long totalTime = tf - t0;
		EncryptionUtils.showStats("Spring Encryption", payload, encryptedString, totalTime);
	}
*/
}
