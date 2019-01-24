package encryption;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

public class SpringEncryptor {
	
	public static int SALT_LENGTH = 32;

	public static String encrypt(String payload, String password) {
		
		String salt = "";
		for(int i=0; i<SALT_LENGTH/16;i++) {
			// add 16 random bytes to the salt
			salt += KeyGenerators.string().generateKey();
		}
		// queryableText -> iv = "00000000000000000000000000000000"
		TextEncryptor textEncryptor = Encryptors.queryableText(password, salt);
		
		return salt.concat(textEncryptor.encrypt(payload));
		
	}

	public static String decrypt(String encryptedPayload, String password) {
		
		final String salt = encryptedPayload.substring(0, SALT_LENGTH);
		final String encryptedPart = encryptedPayload.substring(SALT_LENGTH);
		
		final TextEncryptor textEncryptor = Encryptors.queryableText(password, salt);
		
		return textEncryptor.decrypt(encryptedPart);
		
	}

}
