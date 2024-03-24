//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ExceptionUtils;

public class EncryptException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Integer code;

    public EncryptException(ResultEnum resultenums) {
        super(resultenums.getMessage());
        this.code = resultenums.getCode();
    }

    public EncryptException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
