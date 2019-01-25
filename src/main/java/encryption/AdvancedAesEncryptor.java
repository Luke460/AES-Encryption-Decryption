package encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class AdvancedAesEncryptor{

	public static final int KEY_BYTE_LENGTH = 32;
	public static final int IV_BYTE_LENGTH = 16;
	public static final int PAYLOAD_START_POINT = 32;
	public static final int SEGMENT_SIZE = 16;

	public static String encrypt(String payload, byte[] key, byte[] initVector) {
		try {

			IvParameterSpec iv = new IvParameterSpec(initVector);
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			
			//   Structure:
			// [ 16 random bytes | 16 bytes representing the payload length | payload | final padding in order to fulfill segment size ]            		
			byte[] padding = generateRandomSequence(16);
			byte[] payloadInBytes = payload.getBytes("UTF-8");
			byte[] lengthInfo = getStringByInteger(payloadInBytes.length).getBytes();
			byte[] finalPadding = generateRandomSequence(SEGMENT_SIZE-(payloadInBytes.length%SEGMENT_SIZE));
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			outputStream.write(padding);
			outputStream.write(lengthInfo);
			outputStream.write(payloadInBytes);
			outputStream.write(finalPadding);
			byte fullPayload[] = outputStream.toByteArray( );
					
			Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(fullPayload);
			return Base64.getEncoder().encodeToString(encrypted);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static String decrypt(String payload, byte[] key, byte[] initVector) {
		try {

			IvParameterSpec iv = new IvParameterSpec(initVector);
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

			Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] fullDecryptedPayloadInByte = cipher.doFinal(Base64.getDecoder().decode(payload));
			byte[] payloadLengthInByte = Arrays.copyOfRange(fullDecryptedPayloadInByte, 16, 32);
			int payloadLength = Integer.parseInt(new String(payloadLengthInByte));
			return new String(Arrays.copyOfRange(fullDecryptedPayloadInByte, PAYLOAD_START_POINT, PAYLOAD_START_POINT + payloadLength));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	private static byte[] generateRandomSequence(int length) {
		Random random = new Random();
		byte[] buffer = new byte[length];
		random.nextBytes(buffer);
		return buffer;
	}
	
	private static String getStringByInteger(int value) {
		int maxLength = 16;
		String rawValue = String.valueOf(value);
		int length = rawValue.length();
		String zeroPadding = "";
		for(int i=0;i<(maxLength-length); i++) {
			zeroPadding += '0';
		}
		return zeroPadding + rawValue;
	}
}