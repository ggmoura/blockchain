package br.com.zup.blockchain;

import java.security.PublicKey;

public class TransactionOutput {
    public String id;
    public PublicKey reciepient; //O novo proprietário dessas moedas.
    public float value; //A quantidade de moedas que possuem
    public String parentTransactionId; //O id da transação em que esta saída foi criada

    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepient) + Float.toString(value) + parentTransactionId);
    }

    //Verifique se a moeda pertence a você
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == reciepient);
    }

}
