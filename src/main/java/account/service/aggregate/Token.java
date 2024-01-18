package account.service.aggregate;

import java.io.Serializable;

public class Token implements Serializable {
    private static final long serialVersionUID = 7873796962475638382L;
    String rfid;
    public Token() {
    }
    public Token(String rfid) {
        this.rfid = rfid;
    }
    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }
}
