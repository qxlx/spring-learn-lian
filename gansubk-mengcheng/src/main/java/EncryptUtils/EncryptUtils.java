//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package EncryptUtils;

import ExceptionUtils.EncryptException;
import ExceptionUtils.ResultEnum;
import Md5Utils.MD5Utils;
import SM2Utils.SM2SignUtils;
import SM2Utils.SM2Utils;
import SM4Utils.SM4Utils;
import SM4Utils.Util;
import SM4Utils.Utils;

public class EncryptUtils {
    public EncryptUtils() {
    }

    public static String encryptText(String majorKey, String workKey, String text, String userId, String Sm2SKey) throws Exception {
        if (text != null && !"".equals(text)) {
            String mac = MD5Utils.MD5(text);
            if (mac != null && !"".equals(mac)) {
                if (Sm2SKey != null && !"".equals(Sm2SKey)) {
                    byte[] byteSign = SM2SignUtils.sign(userId.getBytes(), Utils.hexToByte(Sm2SKey), mac.getBytes());
                    if (byteSign.length == 0) {
                        throw new EncryptException(ResultEnum.BYTESIGN_NOT_EXIST);
                    } else {
                        String strSign = Util.byteToHex(byteSign);
                        System.out.println("strSign:" + strSign);
                        String textData = text + "||" + strSign;
                        if (majorKey != null && !"".equals(majorKey)) {
                            if (workKey != null && !"".equals(workKey)) {
                                String workKeyStr = decryptkey(majorKey, workKey);
                                SM4Utils sm4 = new SM4Utils();
                                sm4.secretKey = workKeyStr;
                                System.out.println("ECB模式");
                                String bufText = sm4.encryptData_ECB(textData);
                                if (bufText != null && !"".equals(bufText)) {
                                    System.out.println("加密后密文:" + bufText);
                                    return bufText;
                                } else {
                                    throw new EncryptException(ResultEnum.BUTTEXT);
                                }
                            } else {
                                throw new EncryptException(ResultEnum.WORKKEY);
                            }
                        } else {
                            throw new EncryptException(ResultEnum.MAJORKEY);
                        }
                    }
                } else {
                    throw new EncryptException(ResultEnum.SM2SKEY);
                }
            } else {
                throw new EncryptException(ResultEnum.MAC_NOT_EXIST);
            }
        } else {
            throw new EncryptException(ResultEnum.TEXT);
        }
    }

    public static String decryptText(String majorKey, String workKey, String text) throws Exception {
        if (majorKey != null && !"".equals(majorKey)) {
            if (workKey != null && !"".equals(workKey)) {
                String workKeyStr = decryptkey(majorKey, workKey);
                if (workKeyStr != null && !"".equals(workKeyStr)) {
                    SM4Utils sm4 = new SM4Utils();
                    sm4.secretKey = workKeyStr;
                    String textStr = sm4.decryptData_ECB(text);
                    if (textStr != null && !"".equals(textStr)) {
                        String textData = null;
                        String SignData = null;
                        if (textStr.indexOf("||") == -1) {
                            throw new EncryptException(ResultEnum.IDENTIFIER_NOT_EXIST);
                        } else {
                            String[] strData = textStr.split("\\|\\|");
                            textData = strData[0];
                            if (textData != null && !"".equals(textData)) {
                                if (strData.length > 1) {
                                    SignData = strData[1];
                                    if (SignData == null || "".equals(SignData)) {
                                        throw new EncryptException(ResultEnum.SIGNDATA);
                                    }
                                }

                                return textData;
                            } else {
                                throw new EncryptException(ResultEnum.TEXTDATA);
                            }
                        }
                    } else {
                        throw new EncryptException(ResultEnum.TEXTSTR);
                    }
                } else {
                    throw new EncryptException(ResultEnum.WORKKEYSTR);
                }
            } else {
                throw new EncryptException(ResultEnum.WORKKEY);
            }
        } else {
            throw new EncryptException(ResultEnum.MAJORKEY);
        }
    }

    public static String decryptJson(String majorKey, String workKey, String text) throws Exception {
        if (majorKey != null && !"".equals(majorKey)) {
            if (workKey != null && !"".equals(workKey)) {
                String workKeyStr = decryptkey(majorKey, workKey);
                SM4Utils sm4 = new SM4Utils();
                sm4.secretKey = workKeyStr;
                String textStr = sm4.decryptData_ECB(text);
                if (textStr != null && !"".equals(textStr)) {
                    System.out.println("sm4解密后明文：" + textStr);
                    String textData = null;
                    String SignData = null;
                    if (textStr.indexOf("||") == -1) {
                        System.out.println("密文解密出错,不存在标识符||");
                        throw new EncryptException(ResultEnum.IDENTIFIER_NOT_EXIST);
                    } else {
                        String[] strData = textStr.split("\\|\\|");
                        textData = strData[0];
                        if (textData != null && !"".equals(textData)) {
                            if (strData.length > 1) {
                                SignData = strData[1];
                                if (SignData == null || "".equals(SignData)) {
                                    throw new EncryptException(ResultEnum.SIGNDATA);
                                }

                                System.out.println("加签值：" + SignData);
                            }

                            return textData.replaceAll("\"", "\\\\\"");
                        } else {
                            throw new EncryptException(ResultEnum.TEXTDATA);
                        }
                    }
                } else {
                    throw new EncryptException(ResultEnum.TEXTSTR);
                }
            } else {
                throw new EncryptException(ResultEnum.WORKKEY);
            }
        } else {
            throw new EncryptException(ResultEnum.MAJORKEY);
        }
    }

    public static String decryptkey(String majorKey, String workKey) {
        String workKeyStr = null;

        try {
            String rootKey = "Fi6o8YChd6xxKa3Q";
            SM4Utils sm4 = new SM4Utils();
            sm4.secretKey = rootKey;
            String majorKeyStr = sm4.decryptData_ECB(majorKey);
            if (majorKeyStr == null || majorKeyStr.equals((Object)null)) {
                throw new EncryptException(ResultEnum.MAJORKEYSTR);
            }

            if (majorKeyStr == null || "".equals(majorKeyStr)) {
                throw new EncryptException(ResultEnum.MAJORKEYSTR);
            }

            SM4Utils sm4wKey = new SM4Utils();
            sm4wKey.secretKey = majorKeyStr;
            workKeyStr = sm4wKey.decryptData_ECB(workKey);
            if (workKeyStr == null || "".equals(workKeyStr)) {
                throw new EncryptException(ResultEnum.WORKKEYSTR);
            }

            System.out.println("工作秘钥解密完成");
        } catch (Exception var7) {
            System.out.println("工作秘钥解密失败");
        }

        return workKeyStr;
    }

    public static boolean verifySign(String majorKey, String workKey, String text, String userId, String Sm2PKey) throws Exception {
        boolean b1 = false;
        if (text != null && !"".equals(text)) {
            if (majorKey != null && !"".equals(majorKey)) {
                if (workKey != null && !"".equals(workKey)) {
                    String workKeyStr = decryptkey(majorKey, workKey);
                    if (workKeyStr != null && !"".equals(workKeyStr)) {
                        SM4Utils sm4 = new SM4Utils();
                        sm4.secretKey = workKeyStr;
                        String textStr = sm4.decryptData_ECB(text);
                        if (textStr != null && !"".equals(textStr)) {
                            System.out.println("sm4解密后明文：" + textStr);
                            String textData = null;
                            String SignData = null;
                            if (textStr.indexOf("||") == -1) {
                                System.out.println("密文解密出错,不存在标识符||");
                                throw new EncryptException(ResultEnum.IDENTIFIER_NOT_EXIST);
                            } else {
                                String[] strData = textStr.split("\\|\\|");
                                textData = strData[0];
                                if (textData != null && !"".equals(textData)) {
                                    if (strData.length > 1) {
                                        SignData = strData[1];
                                        if (SignData == null || "".equals(SignData)) {
                                            throw new EncryptException(ResultEnum.SIGNDATA);
                                        }

                                        System.out.println("加签值：" + SignData);
                                    }

                                    String mac = MD5Utils.MD5(textData);
                                    if (mac != null && !"".equals(mac)) {
                                        if (userId != null && !"".equals(userId)) {
                                            if (Sm2PKey != null && !"".equals(Sm2PKey)) {
                                                b1 = SM2SignUtils.verifySign(userId.getBytes(), Utils.hexToByte(Sm2PKey), mac.getBytes(), Utils.hexToByte(SignData));
                                                System.out.println("验签结果" + b1);
                                                return b1;
                                            } else {
                                                throw new EncryptException(ResultEnum.SM2PKEY);
                                            }
                                        } else {
                                            throw new EncryptException(ResultEnum.USERID);
                                        }
                                    } else {
                                        throw new EncryptException(ResultEnum.MAC_NOT_EXIST);
                                    }
                                } else {
                                    throw new EncryptException(ResultEnum.TEXTDATA);
                                }
                            }
                        } else {
                            throw new EncryptException(ResultEnum.TEXTSTR);
                        }
                    } else {
                        throw new EncryptException(ResultEnum.WORKKEYSTR);
                    }
                } else {
                    throw new EncryptException(ResultEnum.WORKKEY);
                }
            } else {
                throw new EncryptException(ResultEnum.MAJORKEY);
            }
        } else {
            throw new EncryptException(ResultEnum.TEXT);
        }
    }

    public static void main(String[] args) {
        SM2Utils.generateKeyPair();
        String majorKey = "lAt4T313+6PLDdLkzvZ6QfIgadtJczcgUTs8my/usps=";
        String workKey = "a9iyzC79IAOiPIODfBixnhmPSV7FlwVcOFYxV9KuTPw=";
        String userId = "gsbank";
        String Sm2SKey = "1D08CAA6144E3F6B75FB25AA7CEA7EBD266781E39B64899BB0A2E2B524C629DB";
        String Sm2PKey = "040297EF95ECD16956FD49F8F777DBBE22A9DA99FE2678C731C38D71BD90F297B20189B3C551F4B7725F0F7A02C2A27F18F8287DE16A6697FDED19035F85ADCC26";
        String text = "1";

        try {
            String textData = encryptText(majorKey, workKey, text, userId, Sm2SKey);
            boolean b2 = verifySign(majorKey, workKey, textData, userId, Sm2PKey);
            if (b2) {
                String textData2 = decryptText(majorKey, workKey, textData);
                System.out.println("返回明文：" + textData2);
            } else {
                System.out.println("验签失败，请重新验签");
            }
        } catch (Exception var10) {
            System.out.println("验签失败，请重试！" + var10.getMessage());
        }

    }
}
