package br.com.zup.blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {

    public String transactionId; // Contém um hash de transação*
    public PublicKey sender; // Endereço do remetente/chave pública.
    public PublicKey reciepient; // Endereço do destinatário/chave pública.
    public float value; // Contém o valor que desejamos enviar ao destinatário.
    public byte[] signature; // Isso é para evitar que outra pessoa gaste fundos em nossa carteira.

    public ArrayList<TransactionInput> inputs = new ArrayList<>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    private static int sequence = 0; // Uma contagem aproximada de quantas transações foram geradas

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }

    public boolean processTransaction() {

        if (verifySignature() == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        // Reúne entradas de transações (certificando-se de que não foram gastas):
        for (TransactionInput i : inputs) {
            i.UTXO = ZopChain.UTXOs.get(i.transactionOutputId);
        }

        // Verifica se a transação é válida:
        if (getInputsValue() < ZopChain.minimumTransaction) {
            System.out.println("Transaction Inputs too small: " + getInputsValue());
            System.out.println("Please enter the amount greater than " + ZopChain.minimumTransaction);
            return false;
        }

        // Gere saídas de transação:
        float leftOver = getInputsValue() - value; // obtenha o valor das entradas e, em seguida, a alteração que sobrou:
        transactionId = calulateHash();
        outputs.add(new TransactionOutput(this.reciepient, value, transactionId)); // enviar valor ao destinatário
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); // envie o que sobrou de 'mudança' de volta ao remetente

        // Adicionar saídas à lista não gastos
        for (TransactionOutput o : outputs) {
            ZopChain.UTXOs.put(o.id, o);
        }

        // Remova as entradas de transação das listas UTXO conforme gastas:
        for (TransactionInput i : inputs) {
            if (i.UTXO == null) continue; // se a transação não puder ser encontrada, pule-a
            ZopChain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    public float getInputsValue() {
        float total = 0;
        for (TransactionInput i : inputs) {
            if (i.UTXO == null) continue; // se a transação não puder ser encontrada, ignore-a.
            total += i.UTXO.value;
        }
        return total;
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public float getOutputsValue() {
        float total = 0;
        for (TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }

    private String calulateHash() {
        sequence++; // aumente a sequência para evitar 2 transações idênticas com o mesmo hash
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(reciepient) +
                        Float.toString(value) + sequence);
    }
}
