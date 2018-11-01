package leaf.prod.walletsdk;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Int;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import leaf.prod.walletsdk.api.Erc20Contract;

public class Erc20TransactionManager {

    private static final BigInteger gasPrice = Contract.GAS_PRICE; //22000000000

    //    private static final BigInteger gasLimit = Contract.GAS_LIMIT; //4300000
    private static final BigInteger gasLimit = BigInteger.valueOf(100000); //weth转账最低limit

    private Web3j web3j = SDK.getWeb3j();

    private Erc20Contract erc20Contract;

    public Erc20TransactionManager(String contractAddress, TransactionManager transactionManager) {
        this.erc20Contract = Erc20Contract.load(contractAddress, SDK.getWeb3j(), transactionManager, gasPrice, gasLimit);
    }

    public Erc20Contract getErc20Contract() {
        return erc20Contract;
    }

    public String name() throws Exception {
        return erc20Contract.name().send();
    }

    public String symbol() throws Exception {
        return erc20Contract.symbol().send();
    }

    public BigInteger decimals() throws Exception {
        return erc20Contract.decimals().send();
    }

    public BigInteger totalSupply() throws Exception {
        return erc20Contract.totalSupply().send();
    }

    public BigInteger balanceOf(String owner) throws Exception {
        return erc20Contract.balanceOf(owner).send();
    }

    public String transfer(BigInteger gasPrice, String to, BigInteger value) throws Exception {
        erc20Contract.setGasPrice(gasPrice);
        TransactionReceipt transactionReceipt = erc20Contract.transfer(to, value).send();
        return transactionReceipt.getTransactionHash();
    }

    public RawTransaction getRawTransaction(Credentials credentials, String to, String contractAddress, BigInteger gasPrice, BigInteger gasLimit, BigInteger value) throws IOException {
        Function function = new Function("transfer",
                Arrays.asList(new Address(to), new Int(value)),
                Collections.emptyList());
        String data = FunctionEncoder.encode(function);
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.PENDING).send();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        return RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, data);
    }
}
