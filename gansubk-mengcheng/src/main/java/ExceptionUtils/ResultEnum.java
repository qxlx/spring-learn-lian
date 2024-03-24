//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ExceptionUtils;

public enum ResultEnum {
    MAC_NOT_EXIST(1, "MAC值为空，请检查MAC值"),
    BYTESIGN_NOT_EXIST(2, "byteSign加签值为空"),
    MAJORKEY(3, "主密钥为空"),
    WORKKEY(4, "工作密钥"),
    WORKKEYSTR(5, "workKeyStr为空"),
    BUTTEXT(6, "加密后的密文buttext为空"),
    TEXTSTR(7, "解密后的明文内容为空"),
    STRDATA(8, "解密后的数据截取异常"),
    TEXTDATA(9, "截取的密文为空"),
    SIGNDATA(10, "截取的加签值为空"),
    MAJORKEYSTR(11, "工作密钥明文"),
    TEXT(12, "TEXT值为空"),
    USERID(13, "USERID为空"),
    SM2SKEY(14, "SM2SKEY为空"),
    SM2PKEY(14, "SM2PKEY为空"),
    IDENTIFIER_NOT_EXIST(15, "解密后报文标识分隔符不存在");

    private Integer code;
    private String message;

    private ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
