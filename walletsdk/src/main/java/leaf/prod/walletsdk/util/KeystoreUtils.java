package leaf.prod.walletsdk.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;
import com.fasterxml.jackson.databind.ObjectMapper;

//import leaf.prod.walletsdk.EventAdvisor;
import leaf.prod.walletsdk.exception.IllegalCredentialException;
import leaf.prod.walletsdk.exception.InvalidKeystoreException;
import leaf.prod.walletsdk.exception.InvalidPrivateKeyException;
import leaf.prod.walletsdk.exception.KeystoreCreateException;
import leaf.prod.walletsdk.model.RandomWallet;

/**
 * Functions work with Keystore.
 *
 * @author slice30k
 */
public class KeystoreUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @SuppressLint("SimpleDateFormat")
    public static String generateKeystoreFilename(String address) {
        address = Numeric.cleanHexPrefix(address);
        SimpleDateFormat dateFormat = new SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'");
        return dateFormat.format(new Date()) + address + ".json";
    }

    public static String generatePrivateKey() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        String privateKey;
        do {
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            BigInteger privateKeyInDec = ecKeyPair.getPrivateKey();
            privateKey = privateKeyInDec.toString(16);
        } while (!WalletUtils.isValidPrivateKey(privateKey));
        return privateKey;
    }

    public static String createFromKeystoreJson(String keystoreJson, String password, File dest) throws InvalidKeystoreException, KeystoreCreateException, IllegalCredentialException {
        Credentials credentials = unlock(password, keystoreJson);
        String filename = generateKeystoreFilename(credentials.getAddress());
        File keystoreFile = new File(dest, filename);
        writeToFile(keystoreJson, keystoreFile);
        //todo order
//        EventAdvisor.notifyCreation(credentials.getAddress());
        return filename;
    }

    public static RandomWallet createFromPrivateKey() throws InvalidPrivateKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        String privateKey = generatePrivateKey();
        String address = createFromPrivateKey(privateKey);
        return RandomWallet.builder().address(address).privateKey(privateKey).build();
    }

    private static String createFromPrivateKey(String privateKey) throws InvalidPrivateKeyException {
        if (!WalletUtils.isValidPrivateKey(privateKey)) {
            throw new InvalidPrivateKeyException();
        }
        Credentials credentials = Credentials.create(privateKey);
        return credentials.getAddress();
    }

    public static String createFromPrivateKey(String privateKey, String password, File dest) throws InvalidPrivateKeyException, KeystoreCreateException {
        if (!WalletUtils.isValidPrivateKey(privateKey)) {
            throw new InvalidPrivateKeyException();
        }
        Credentials credentials = Credentials.create(privateKey);
        String filename;
        try {
            filename = WalletUtils.generateWalletFile(password, credentials.getEcKeyPair(), dest, false);
        } catch (Exception e) {
            throw new KeystoreCreateException(e);
        }
        //todo order
//        EventAdvisor.notifyCreation(credentials.getAddress());
        return filename;
    }

    public static Credentials unlock(String password, String keystoreJson) throws InvalidKeystoreException, IllegalCredentialException {
        WalletFile walletFile = loadFromJsonString(keystoreJson);
        try {
            ECKeyPair ecKeyPair = Wallet.decrypt(password, walletFile);
            return Credentials.create(ecKeyPair);
        } catch (CipherException e) {
            throw new IllegalCredentialException(e);
        }
    }

    public static Credentials unlock(String password, File keystoreFile) throws InvalidKeystoreException, IllegalCredentialException {
        WalletFile walletFile = loadFromFile(keystoreFile);
        try {
            ECKeyPair ecKeyPair = Wallet.decrypt(password, walletFile);
            return Credentials.create(ecKeyPair);
        } catch (CipherException e) {
            throw new IllegalCredentialException(e);
        }
    }

    private static WalletFile loadFromFile(File keystoreFile) throws InvalidKeystoreException {
        WalletFile walletFile;
        try {
            walletFile = objectMapper.readValue(keystoreFile, WalletFile.class);
        } catch (IOException e) {
            throw new InvalidKeystoreException(e);
        }
        return walletFile;
    }

    private static WalletFile loadFromJsonString(String json) throws InvalidKeystoreException {
        WalletFile walletFile;
        try {
            walletFile = objectMapper.readValue(json, WalletFile.class);
        } catch (IOException e) {
            throw new InvalidKeystoreException(e);
        }
        return walletFile;
    }

    private static void writeToFile(String json, File keystoreFile) throws KeystoreCreateException {
        try {
            FileWriter fileWriter = new FileWriter(keystoreFile);
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            throw new KeystoreCreateException(e);
        }
    }

    private static void writeToFile(WalletFile walletFile, File keystoreFile) throws KeystoreCreateException {
        try {
            objectMapper.writeValue(keystoreFile, walletFile);
        } catch (IOException e) {
            throw new KeystoreCreateException(e);
        }
    }
}
