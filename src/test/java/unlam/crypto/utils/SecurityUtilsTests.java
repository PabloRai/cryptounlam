package unlam.crypto.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SecurityUtilsTests {

	protected static PublicKey publicKey;
	protected static PrivateKey privateKey;
	protected static String data;
	protected static String dataModified;

	@BeforeAll
	static void initAll() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		final KeyPair keyPair = SecurityUtils.generateKeyPair();
		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();
		data = "test text";
		dataModified = data + "!";
	}

	@Test
	void testValid() {
		byte[] signature = SecurityUtils.applyEllipticCurveDigitalSignatureAlgorithm(SecurityUtilsTests.privateKey,
				data);
		assertTrue(SecurityUtils.verifyEllipticCurveDigitalSignature(SecurityUtilsTests.publicKey, data, signature));
	}

	@Test
	void testDataModified() {
		byte[] signature = SecurityUtils.applyEllipticCurveDigitalSignatureAlgorithm(SecurityUtilsTests.privateKey,
				dataModified);
		assertFalse(SecurityUtils.verifyEllipticCurveDigitalSignature(SecurityUtilsTests.publicKey, data, signature));
	}

	@Test
	void testSignatureModified() {
		byte[] signature = SecurityUtils.applyEllipticCurveDigitalSignatureAlgorithm(SecurityUtilsTests.privateKey,
				data);
		signature[0]++;
		assertThrows(RuntimeException.class, () -> {
			SecurityUtils.verifyEllipticCurveDigitalSignature(SecurityUtilsTests.publicKey, data, signature);
		});
	}

}
