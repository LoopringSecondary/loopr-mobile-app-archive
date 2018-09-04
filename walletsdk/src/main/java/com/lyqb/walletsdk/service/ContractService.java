package com.lyqb.walletsdk.service;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ContractService extends Contract {

    protected ContractService(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractBinary, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ContractService(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractBinary, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    // try 1.
    public void totalSupply() throws Exception {

        List<Type> input = new ArrayList<>();
        List<TypeReference<?>> output = new ArrayList<>();
        Function function = new Function(
                "totalSupply",
                input,
                output
        );
        BigInteger bigInteger = executeRemoteCallSingleValueReturn(function, BigInteger.class).send();
        System.out.println(bigInteger.toString());
    }

    public static void main(String[] args) {

    }
}
