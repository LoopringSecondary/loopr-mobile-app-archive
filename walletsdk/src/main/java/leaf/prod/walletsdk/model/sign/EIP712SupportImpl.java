package leaf.prod.walletsdk.model.sign;

import java.util.Map;

import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-04-01 4:29 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class EIP712SupportImpl implements IEIP712Support {

    private static final String EIP191HeaderHex = "1901";

    @Override
    public EIP712TypedData jsonToTypedData(String jsonString) {
        return null;
    }

    @Override
    public String getEIP712Message(IEIP712Support.EIP712TypedData typedData) {
        return null;
    }

    private String hashStruct(String primaryType, Map<String, Object> data, Types typeDefs) {
        String encodedString = encodeData(primaryType, data, typeDefs);
        return Hash.sha3(encodedString);
    }

    private String encodeData(String dataType, Map<String, Object> data, Types typeDefs) {
        String dataTypeHash = Numeric.toHexString(hashType(dataType, typeDefs));
    }

    private byte[] hashType(String dataType, Types allTypes) {
        String encodedTypeStr = encodeType(dataType, allTypes);
        return Hash.sha3(encodedTypeStr.getBytes());
    }

    private String encodeType(String dataType, Types allTypes) {
        return null;
    }

    private findTypeDependencies
}
