//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SM2Utils;

import SM4Utils.Util;
import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

public class Cipher {
    private int ct = 1;
    private ECPoint p2;
    private SM3Digest sm3keybase;
    private SM3Digest sm3c3;
    private byte[] key = new byte[32];
    private byte keyOff = 0;

    public Cipher() {
    }

    private void Reset() {
        this.sm3keybase = new SM3Digest();
        this.sm3c3 = new SM3Digest();
        byte[] p = Util.byteConvert32Bytes(this.p2.getX().toBigInteger());
        this.sm3keybase.update(p, 0, p.length);
        this.sm3c3.update(p, 0, p.length);
        p = Util.byteConvert32Bytes(this.p2.getY().toBigInteger());
        this.sm3keybase.update(p, 0, p.length);
        this.ct = 1;
        this.NextKey();
    }

    private void NextKey() {
        SM3Digest sm3keycur = new SM3Digest(this.sm3keybase);
        sm3keycur.update((byte)(this.ct >> 24 & 255));
        sm3keycur.update((byte)(this.ct >> 16 & 255));
        sm3keycur.update((byte)(this.ct >> 8 & 255));
        sm3keycur.update((byte)(this.ct & 255));
        sm3keycur.doFinal(this.key, 0);
        this.keyOff = 0;
        ++this.ct;
    }

    public ECPoint Init_enc(SM2 sm2, ECPoint userKey) {
        AsymmetricCipherKeyPair key = sm2.ecc_key_pair_generator.generateKeyPair();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters)key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters)key.getPublic();
        BigInteger k = ecpriv.getD();
        ECPoint c1 = ecpub.getQ();
        this.p2 = userKey.multiply(k);
        this.Reset();
        return c1;
    }

    public void Encrypt(byte[] data) {
        this.sm3c3.update(data, 0, data.length);

        for(int i = 0; i < data.length; ++i) {
            if (this.keyOff == this.key.length) {
                this.NextKey();
            }

            byte var10002 = data[i];
            byte[] var10003 = this.key;
            byte var10006 = this.keyOff;
            this.keyOff = (byte)(var10006 + 1);
            data[i] = (byte)(var10002 ^ var10003[var10006]);
        }

    }

    public void Init_dec(BigInteger userD, ECPoint c1) {
        this.p2 = c1.multiply(userD);
        this.Reset();
    }

    public void Decrypt(byte[] data) {
        for(int i = 0; i < data.length; ++i) {
            if (this.keyOff == this.key.length) {
                this.NextKey();
            }

            byte var10002 = data[i];
            byte[] var10003 = this.key;
            byte var10006 = this.keyOff;
            this.keyOff = (byte)(var10006 + 1);
            data[i] = (byte)(var10002 ^ var10003[var10006]);
        }

        this.sm3c3.update(data, 0, data.length);
    }

    public void Dofinal(byte[] c3) {
        byte[] p = Util.byteConvert32Bytes(this.p2.getY().toBigInteger());
        this.sm3c3.update(p, 0, p.length);
        this.sm3c3.doFinal(c3, 0);
        this.Reset();
    }
}
