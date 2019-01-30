package encryption;

import java.nio.file.Files;
import java.nio.file.Paths;

public class EncryptionUtils {

	public static void showStats(String methodName, String payload, String encryptedString, long executionTime) throws Exception  {

		double originalLength = payload.length();
		double encryptedLength = encryptedString.length();
		System.out.println(" - - - - - - - - - - - - - - - - - - ");
		System.out.println("Encryption Type: " + methodName);
		System.out.println("Encryption Time: " + executionTime + "ms");
		System.out.println("Original size: " + originalLength + " byte");
		System.out.println("Encrypted size: " + encryptedLength + " byte");
		System.out.println("Overhead: " + (encryptedLength-originalLength) + " byte");
		System.out.println("Overhead percentage: " + (int)(((encryptedLength/originalLength)-1)*100) + "%");
		String lastEncryption;
		try {
			lastEncryption = new String(Files.readAllBytes(Paths.get(methodName + "-lastEncryption.txt")));
		} catch (Exception e) {
			lastEncryption = encryptedString;
			Files.write(Paths.get(methodName + "-lastEncryption.txt"), lastEncryption.getBytes("UTF-8"));
		}
		System.out.println(advancedStringComparator(encryptedString,lastEncryption));
		Files.write(Paths.get(methodName + "-lastEncryption.txt"), lastEncryption.getBytes("UTF-8"));
		System.out.println(" - - - - - - - - - - - - - - - - - - ");
	}

	public static String getStringFromByteArray(byte[] encodedKey) {
		String s = "";
		for(byte b:encodedKey) {
			s+= "[" + Byte.valueOf(b) + "] ";
		}
		return s;
	}

	public static String advancedStringComparator(String s1, String s2) {
		int l = s1.length();
		int diffCount = 0;
		int sequenceCount = 0;
		int recordSeq = -1;
		if(s1 == null || s2 == null) {
			diffCount = l;
		} else {
			if(s2.length()<l) l = s2.length();
			for(int i = 0; i<l; i++) {
				if(s1.charAt(i)!=s2.charAt(i)) {
					diffCount++;
					if(sequenceCount>recordSeq) {
						recordSeq = sequenceCount;
					}
					sequenceCount = 0;
				} else {
					sequenceCount++;
				}
				if(sequenceCount>recordSeq) {
					recordSeq = sequenceCount;
				}
			}
			if(sequenceCount>recordSeq) {
				recordSeq = sequenceCount;
			}
		}
		return "Diff percentage: " + (int)((((float)diffCount)/l)*100) + "%    Max Equal Sequence length: " + recordSeq;
	}

}




