package account.service;

public class Payment {

    String merchantId;
    int amount;
    Token token;
    String customerId;
    String merchantBankId;
    String customerBankId;

    public Payment() {
    }

    public int getAmount() {
        return amount;
    }


    public String getMerchantId() {
        return merchantId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setAccountId(String accountId) {
        this.customerId = accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMerchantBankId() {
        return merchantBankId;
    }

    public void setMerchantBankId(String merchantBankId) {
        this.merchantBankId = merchantBankId;
    }

    public String getCustomerBankId() {
        return customerBankId;
    }

    public void setCustomerBankId(String customerBankId) {
        this.customerBankId = customerBankId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Payment)) {
            return false;
        }
        var c = (Payment) obj;
        return merchantId != null && merchantId.equals(c.getMerchantId()) &&
                amount == c.getAmount() &&
                token != null && token.equals(c.getToken()) &&
                customerId != null && customerId.equals(c.getCustomerId()) &&
                merchantBankId != null && merchantBankId.equals(c.getMerchantBankId()) &&
                customerBankId != null && customerBankId.equals(c.getCustomerBankId()) ||
                merchantId == null && c.getMerchantId() == null &&
                        amount == c.getAmount() &&
                        token == null && c.getToken() == null &&
                        customerId == null && c.getCustomerId() == null &&
                        merchantBankId == null && c.getMerchantBankId() == null &&
                        customerBankId == null && c.getCustomerBankId() == null;
    }
}
