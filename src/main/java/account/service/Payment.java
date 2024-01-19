package account.service;
/**
 * @Author: Alex Batten
 * Mob programming, all members
 */
public class Payment {
    int amount;
    String merchantId;
    String customerId;
    String merchantBankId;
    String customerBankId;
    String paymentId;

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

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Payment)) {
            return false;
        }
        var c = (Payment) obj;
        return c.getPaymentId().equals(this.getPaymentId());
    }

}
