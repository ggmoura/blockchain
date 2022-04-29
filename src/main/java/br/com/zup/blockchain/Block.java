package br.com.zup.blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {

    public String hash;
    public String previousHash;
    public String merkleRoot; // merkleRoot é o hash de todos os hashes de todas as transações que fazem parte de um bloco em uma rede blockchain.
    public List<Transaction> transactions = new ArrayList<>(); // nossos dados serão uma simples mensagem.
    public long timeStamp; // como número de milissegundos desde 1/1/1970.
    public int nonce; // abreviação de "número usado apenas uma vez" número que os mineradores de blockchain estão resolvendo. Quando a solução é encontrada, os mineradores de blockchain recebem criptomoedas em troca.

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    // Calcular novo hash com base no conteúdo dos blocos
    public String calculateHash() {
        String calculatedhash = StringUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        merkleRoot
        );
        return calculatedhash;
    }

    // Aumenta o valor de nonce até que o alvo de hash seja alcançado.
    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDificultyString(difficulty); // Crie uma string com dificuldade * "0"
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    // Adicionar transações a este bloco
    public boolean addTransaction(Transaction transaction) {
        // processe a transação e verifique se é válida, a menos que o bloco seja o bloco de gênese, ignore.
        if (transaction == null) return false;
        if ((!"0".equals(previousHash))) {
            if ((transaction.processTransaction() != true)) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }

}
