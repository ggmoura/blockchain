https://medium.com/programmers-blockchain/creating-your-first-blockchain-with-java-part-2-transactions-2cdac335e0ce

# nonce
Um nonce é uma abreviação de "número usado apenas uma vez", que, no contexto da mineração de criptomoedas,
é um número adicionado a um bloco com hash - ou criptografado - em um blockchain que, quando refeito,
atende às restrições de nível de dificuldade. O nonce é o número que os mineradores de blockchain estão resolvendo.
Quando a solução é encontrada, os mineradores de blockchain recebem criptomoedas em troca.

# merkleroot
Uma merkleroot é criada juntando pares de TXID s, o que lhe dá uma impressão digital curta, mas única, 
para todas as transações em um bloco. Essa raiz merkle é então usada como um campo em um cabeçalho de bloco, 
o que significa que cada cabeçalho de bloco terá uma representação curta de cada transação dentro do bloco.