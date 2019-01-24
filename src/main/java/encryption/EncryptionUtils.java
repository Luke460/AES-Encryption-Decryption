package encryption;

import LukePack.LP;

public class EncryptionUtils {
	
	public static void showStats(String methodName, String payload, String encryptedString, long executionTime) {

		double originalLength = payload.length();
		double encryptedLength = encryptedString.length();
		System.out.println(" - - - - - - - - - - - - - - - - - - ");
		System.out.println("Encryption Type: " + methodName);
		System.out.println("Encryption Time: " + executionTime + "ms");
		System.out.println("Original size: " + originalLength + " byte");
		System.out.println("Encrypted size: " + encryptedLength + " byte");
		System.out.println("Overhead: " + (encryptedLength-originalLength) + " byte");
		System.out.println("Overhead percentage: " + (int)(((encryptedLength/originalLength)-1)*100) + "%");

		String lastEncryption = LP.readFile("lastEncryption.txt");
		System.out.println(advancedStringComparator(encryptedString,lastEncryption));
		LP.writeNewFile("lastEncryption.txt", encryptedString);
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
		return "Diff percentage: " + (int)((((float)diffCount)/l)*100) + "%    Max Equal Sequence length: " + recordSeq;
	}
	
}




