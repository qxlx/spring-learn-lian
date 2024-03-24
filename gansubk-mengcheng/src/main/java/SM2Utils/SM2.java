//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SM2Utils;

import SM4Utils.Util;
import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECFieldElement.Fp;

public class SM2 {
    public static final String[] ecc_param = new String[]{"8542D69E4C044F18E8B92435BF6FF7DE457283915C45517D722EDB8B08F1DFC3", "787968B4FA32C3FD2417842E73BBFEFF2F3C848B6831D7E0EC65228B3937E498", "63E4C6D3B23B0C849CF84241484BFE48F61D59A5B16BA06E6E12D1DA27C5249A", "8542D69E4C044F18E8B92435BF6FF7DD297720630485628D5AE74EE7C32E79B7", "421DEBD61B62EAB6746434EBC3CC315E32220B3BADD50BDC4C4E6C147FEDD43D", "0680512BCBB42C07D47349D2153B70C4E5D7FDFCBFA36EA1A85841B9E46E09A2"};
    public final BigInteger ecc_p;
    public final BigInteger ecc_a;
    public final BigInteger ecc_b;
    public final BigInteger ecc_n;
    public final BigInteger ecc_gx;
    public final BigInteger ecc_gy;
    public final ECCurve ecc_curve;
    public final ECPoint ecc_point_g;
    public final ECDomainParameters ecc_bc_spec;
    public final ECKeyPairGenerator ecc_key_pair_generator;
    public final ECFieldElement ecc_gx_fieldelement;
    public final ECFieldElement ecc_gy_fieldelement;

    public static SM2 Instance() {
        return new SM2();
    }

    public SM2() {
        this.ecc_p = new BigInteger(ecc_param[0], 16);
        this.ecc_a = new BigInteger(ecc_param[1], 16);
        this.ecc_b = new BigInteger(ecc_param[2], 16);
        this.ecc_n = new BigInteger(ecc_param[3], 16);
        this.ecc_gx = new BigInteger(ecc_param[4], 16);
        this.ecc_gy = new BigInteger(ecc_param[5], 16);
        this.ecc_gx_fieldelement = new Fp(this.ecc_p, this.ecc_gx);
        this.ecc_gy_fieldelement = new Fp(this.ecc_p, this.ecc_gy);
        this.ecc_curve = new org.bouncycastle.math.ec.ECCurve.Fp(this.ecc_p, this.ecc_a, this.ecc_b);
        this.ecc_point_g = new org.bouncycastle.math.ec.ECPoint.Fp(this.ecc_curve, this.ecc_gx_fieldelement, this.ecc_gy_fieldelement);
        this.ecc_bc_spec = new ECDomainParameters(this.ecc_curve, this.ecc_point_g, this.ecc_n);
        ECKeyGenerationParameters ecc_ecgenparam = new ECKeyGenerationParameters(this.ecc_bc_spec, new SecureRandom());
        this.ecc_key_pair_generator = new ECKeyPairGenerator();
        this.ecc_key_pair_generator.init(ecc_ecgenparam);
    }

    public byte[] sm2GetZ(byte[] userId, ECPoint userKey) {
        SM3Digest sm3 = new SM3Digest();
        int len = userId.length * 8;
        sm3.update((byte)(len >> 8 & 255));
        sm3.update((byte)(len & 255));
        sm3.update(userId, 0, userId.length);
        byte[] p = Util.byteConvert32Bytes(this.ecc_a);
        sm3.update(p, 0, p.length);
        p = Util.byteConvert32Bytes(this.ecc_b);
        sm3.update(p, 0, p.length);
        p = Util.byteConvert32Bytes(this.ecc_gx);
        sm3.update(p, 0, p.length);
        p = Util.byteConvert32Bytes(this.ecc_gy);
        sm3.update(p, 0, p.length);
        p = Util.byteConvert32Bytes(userKey.getX().toBigInteger());
        sm3.update(p, 0, p.length);
        p = Util.byteConvert32Bytes(userKey.getY().toBigInteger());
        sm3.update(p, 0, p.length);
        byte[] md = new byte[sm3.getDigestSize()];
        sm3.doFinal(md, 0);
        return md;
    }

    public void sm2Sign(byte[] md, BigInteger userD, ECPoint userKey, SM2Result sm2Result) {
        BigInteger e = new BigInteger(1, md);
        BigInteger k = null;
        ECPoint kp = null;
        BigInteger r = null;
        BigInteger s = null;

        while(true) {
            do {
                String kS = "6CB28D99385C175C94F94E934817663FC176D925DD72B727260DBAAE1FB2F96F";
                k = new BigInteger(kS, 16);
                kp = this.ecc_point_g.multiply(k);
                r = e.add(kp.getX().toBigInteger());
                r = r.mod(this.ecc_n);
            } while(r.equals(BigInteger.ZERO));

            if (!r.add(k).equals(this.ecc_n)) {
                BigInteger da_1 = userD.add(BigInteger.ONE);
                da_1 = da_1.modInverse(this.ecc_n);
                s = r.multiply(userD);
                s = k.subtract(s).mod(this.ecc_n);
                s = da_1.multiply(s).mod(this.ecc_n);
                if (!s.equals(BigInteger.ZERO)) {
                    sm2Result.r = r;
                    sm2Result.s = s;
                    return;
                }
            }
        }
    }

    public void sm2Verify(byte[] md, ECPoint userKey, BigInteger r, BigInteger s, SM2Result sm2Result) {
        sm2Result.R = null;
        BigInteger e = new BigInteger(1, md);
        BigInteger t = r.add(s).mod(this.ecc_n);
        if (!t.equals(BigInteger.ZERO)) {
            ECPoint x1y1 = this.ecc_point_g.multiply(sm2Result.s);
            x1y1 = x1y1.add(userKey.multiply(t));
            sm2Result.R = e.add(x1y1.getX().toBigInteger()).mod(this.ecc_n);
        }
    }

    public static void main(String[] args) {
    }
}
