package com.lyqb.walletsdk;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import com.lyqb.walletsdk.exception.TransactionException;
import com.lyqb.walletsdk.model.TransactionObject;
import com.lyqb.walletsdk.service.EthereumService;

public class TransactionTest {

    @Test
    public void test() {
        SDK.initSDK();
        EthereumService ethereumService = new EthereumService();
        TransactionObject transactionObject = new TransactionObject(
                ((byte) 1),
                "",
                "",
                BigInteger.ONE,
                BigInteger.ZERO,
                BigInteger.ZERO,
                new BigInteger("100"),
                "hello world"
        );
        BigInteger bigInteger = ethereumService.estimateGasLimit(transactionObject);
        System.out.println(bigInteger);
    }

    @Test
    public void send() throws TransactionException {
        SDK.initSDK();
        String from = "0x5c479c8a0B9Da9949dfA0793B055195FcCDE6a93";
        String to = "0x78cc40ee3b9eCC6febAF8F18127f10392eb96c1f";
        String pk = "ae04c2046f31112fc29e7acc46a353209d14c2c6ab2e11f3943d0a2ea20b1196";
        //        LoopringService loopringService = new LoopringService();
        //        String nonceStr = loopringService.getNonce(from).toBlocking().single();
        //        BigInteger nonce = Numeric.toBigInt(nonceStr);
        //
        //        String gasPriceStr = loopringService.getEstimateGasPrice().toBlocking().single();
        //        BigInteger gasPrice = Numeric.toBigInt(gasPriceStr);
        //
        //        TransactionObject transaction = TransactionHelper.createTransaction(
        //                ((byte) 1),
        //                from,
        //                to,
        //                nonce,
        //                gasPrice,
        //                new BigInteger("25200"),
        //                BigInteger.ZERO,
        //                ""
        //                );
        ////        String signedTransaction = SignUtils.signTransaction(transaction, pk);
        ////        EthereumService ethereumService = new EthereumService();
        ////        String txHash = ethereumService.sendRawTransaction(signedTransaction);
        //
        //        String txHash = TransactionHelper.sendTransaction(transaction, pk);
        //        System.out.println(txHash);
        String ethBase = SDK.ethBase();
        HttpService httpService = new HttpService(ethBase);
        Web3j web3j = Web3jFactory.build(httpService);
        try {
            TransactionReceipt transactionReceipt = Transfer.sendFunds(web3j, Credentials.create(pk), to, BigDecimal.ZERO, Convert.Unit.ETHER)
                    .send();
            String transactionHash = transactionReceipt.getTransactionHash();
            System.out.println(transactionHash);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.web3j.protocol.exceptions.TransactionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void qwerty() throws TransactionException, IOException {
        SDK.initSDK();
        String ethBase = SDK.ethBase();
        HttpService httpService = new HttpService(ethBase);
        Web3j web3j = Web3jFactory.build(httpService);
        String contractAddress = "0xd26114cd6EE289AccF82350c8d8487fedB8A0C07";
        List<Type> input = new ArrayList<>();
        List<TypeReference<?>> output = new ArrayList<>();
        Function function = new Function(
                "totalSupply",
                input,
                output
        );
        String encode = FunctionEncoder.encode(function);
        System.out.println(encode);
        Transaction ethCallTransaction = Transaction.createEthCallTransaction(
                "0x5c479c8a0B9Da9949dfA0793B055195FcCDE6a93",
                "0xB8c77482e45F1F44dE1745F52C74426C631bDD52",
                encode
        );
        EthCall send = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).send();
        String value = send.getValue();
        System.out.println(value);
        BigInteger bigInteger = Numeric.toBigInt(Numeric.cleanHexPrefix(value));
        System.out.println(bigInteger.toString());
    }
}
