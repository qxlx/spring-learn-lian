//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SM4Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SM4Utils {
    public String secretKey = "";
    private String iv = "";
    private boolean hexString = false;

    public SM4Utils() {
    }

    public String encryptData_ECB(String plainText) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = 1;
            byte[] keyBytes;
            if (this.hexString) {
                keyBytes = Util.hexStringToBytes(this.secretKey);
            } else {
                keyBytes = this.secretKey.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_ecb(ctx, plainText.getBytes("utf-8"));
            String cipherText = (new BASE64Encoder()).encode(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }

            return cipherText;
        } catch (Exception var9) {
            var9.printStackTrace();
            return null;
        }
    }

    public String decryptData_ECB(String cipherText) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = 0;
            byte[] keyBytes;
            if (this.hexString) {
                keyBytes = Util.hexStringToBytes(this.secretKey);
            } else {
                keyBytes = this.secretKey.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_ecb(ctx, (new BASE64Decoder()).decodeBuffer(cipherText));
            return new String(decrypted, "utf-8");
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public String encryptData_CBC(String plainText) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = 1;
            byte[] keyBytes;
            byte[] ivBytes;
            if (this.hexString) {
                keyBytes = Util.hexStringToBytes(this.secretKey);
                ivBytes = Util.hexStringToBytes(this.iv);
            } else {
                keyBytes = this.secretKey.getBytes();
                ivBytes = this.iv.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, plainText.getBytes("utf-8"));
            String cipherText = (new BASE64Encoder()).encode(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }

            return cipherText;
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        }
    }

    public String decryptData_CBC(String cipherText) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = 0;
            byte[] keyBytes;
            byte[] ivBytes;
            if (this.hexString) {
                keyBytes = Util.hexStringToBytes(this.secretKey);
                ivBytes = Util.hexStringToBytes(this.iv);
            } else {
                keyBytes = this.secretKey.getBytes();
                ivBytes = this.iv.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, (new BASE64Decoder()).decodeBuffer(cipherText));
            return new String(decrypted, "utf-8");
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }
}
