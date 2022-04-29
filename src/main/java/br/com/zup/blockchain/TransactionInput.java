package br.com.zup.blockchain;

public class TransactionInput {

	public String transactionOutputId; // Referência a TransactionOutputs -> transactionId
	public TransactionOutput UTXO; // Contém a saída da transação não gasta
	
	public TransactionInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}

}
