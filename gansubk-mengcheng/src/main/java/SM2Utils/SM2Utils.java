//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SM2Utils;

import SM4Utils.Util;
import java.io.IOException;
import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

public class SM2Utils {
    public SM2Utils() {
    }

    public static void generateKeyPair() {
        SM2 sm2 = SM2.Instance();
        AsymmetricCipherKeyPair key = sm2.ecc_key_pair_generator.generateKeyPair();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters)key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters)key.getPublic();
        BigInteger privateKey = ecpriv.getD();
        ECPoint publicKey = ecpub.getQ();
        System.out.println("SM2-PublicKey：" + Util.byteToHex(publicKey.getEncoded()));
        System.out.println("SM2-PrivateKey：" + Util.byteToHex(privateKey.toByteArray()));
    }

    public static String encrypt(byte[] publicKey, byte[] data) throws IOException {
        if (publicKey != null && publicKey.length != 0) {
            if (data != null && data.length != 0) {
                byte[] source = new byte[data.length];
                System.arraycopy(data, 0, source, 0, data.length);
                Cipher cipher = new Cipher();
                SM2 sm2 = SM2.Instance();
                ECPoint userKey = sm2.ecc_curve.decodePoint(publicKey);
                ECPoint c1 = cipher.Init_enc(sm2, userKey);
                cipher.Encrypt(source);
                byte[] c3 = new byte[32];
                cipher.Dofinal(c3);
                return Util.byteToHex(c1.getEncoded()) + Util.byteToHex(source) + Util.byteToHex(c3);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static byte[] decrypt(byte[] privateKey, byte[] encryptedData) throws IOException {
        if (privateKey != null && privateKey.length != 0) {
            if (encryptedData != null && encryptedData.length != 0) {
                String data = Util.byteToHex(encryptedData);
                byte[] c1Bytes = Util.hexToByte(data.substring(0, 130));
                int c2Len = encryptedData.length - 97;
                byte[] c2 = Util.hexToByte(data.substring(130, 130 + 2 * c2Len));
                byte[] c3 = Util.hexToByte(data.substring(130 + 2 * c2Len, 194 + 2 * c2Len));
                SM2 sm2 = SM2.Instance();
                BigInteger userD = new BigInteger(1, privateKey);
                ECPoint c1 = sm2.ecc_curve.decodePoint(c1Bytes);
                Cipher cipher = new Cipher();
                cipher.Init_dec(userD, c1);
                cipher.Decrypt(c2);
                cipher.Dofinal(c3);
                return c2;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        generateKeyPair();
    }
}
