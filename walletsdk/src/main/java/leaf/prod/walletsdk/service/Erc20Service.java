package leaf.prod.walletsdk.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;
import leaf.prod.walletsdk.SDK;

public class Erc20Service {

    private Web3j web3j;

    public Erc20Service() {
        String ethBase = SDK.ethBase();
        HttpService httpService = new HttpService(ethBase);
        web3j = Web3jFactory.build(httpService);
    }

    public BigInteger getTotalSupply(String contractAddress) throws IOException {
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
                // todo edit.
                "0x5c479c8a0B9Da9949dfA0793B055195FcCDE6a93",
                contractAddress,
                encode
        );
        EthCall send = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).send();
        String value = send.getValue();
        System.out.println(value);
        return Numeric.toBigInt(Numeric.cleanHexPrefix(value));
    }
}
