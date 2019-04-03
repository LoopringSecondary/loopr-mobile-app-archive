/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-04-01 4:29 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.sign;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

public class EIP712SupportImpl implements IEIP712Support {

    private static final String EIP191HeaderHex = "1901";

    @Override
    public EIP712TypedData jsonToTypedData(String jsonString) {
        Gson gson = new Gson();
        Map<String, Type> typesMap = new HashMap<>();
        JsonObject json = gson.fromJson(jsonString, JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            JsonArray array = entry.getValue().getAsJsonArray();
            List<TypeItem> typeItems = new ArrayList<>();
            for (JsonElement element : array) {
                String name = element.getAsJsonObject().get("name").getAsString();
                String type = element.getAsJsonObject().get("type").getAsString();
                TypeItem typeItem = TypeItem.builder().name(name).itemType(type).build();
                typeItems.add(typeItem);
            }
            Type type = Type.builder().name(entry.getKey()).typeItems(typeItems).build();
            typesMap.put(entry.getKey(), type);
        }
        Types types = Types.builder().types(typesMap).build();
        String primaryType = json.get("primaryType").getAsString();
        String domain = json.get("domain").getAsString();
        String message = json.get("message").getAsString();
        Map<String, Object> domainMap = gson.fromJson(domain, LinkedTreeMap.class);
        Map<String, Object> messageMap = gson.fromJson(message, LinkedTreeMap.class);
        return new EIP712TypedData(types, primaryType, domainMap, messageMap);
    }

    @Override
    public String getEIP712Message(IEIP712Support.EIP712TypedData typedData) {
        String domainHash = hashStruct("EIP712Domain", typedData.getDomain(), typedData.getTypes());
        String messageHash = hashStruct(typedData.getPrimaryType(), typedData.getMessage(), typedData.getTypes());
        String source = EIP191HeaderHex + domainHash + messageHash;
        return Hash.sha3(source);
    }

    private String hashStruct(String primaryType, Map<String, Object> data, Types typeDefs) {
        String encodedString = encodeData(primaryType, data, typeDefs);
        return Numeric.cleanHexPrefix(Hash.sha3(encodedString));
    }

    private String encodeData(String dataType, Map<String, Object> data, Types typeDefs) {
        String dataTypeHash = Numeric.toHexString(hashType(dataType, typeDefs));
        List<String> encodedValues = Collections.singletonList(dataTypeHash);
        List<TypeItem> typeItems = typeDefs.getTypes().get(dataType).getTypeItems();
        for (TypeItem typeItem : typeItems) {
            String stringValue = "0x0";
            Object value = data.get(typeItem.getName());
            if (value != null) {
                if (value instanceof String) {
                    String valueStr = (String) value;
                    stringValue = valueStr.length() == 0 ? "0x0" : valueStr;
                }
                String itemType = typeItem.getItemType();
                if (itemType.equals("string")) {
                    String valueHash = Numeric.toHexString(Hash.sha3(stringValue.getBytes()));
                    encodedValues.add(valueHash);
                } else if (itemType.equals("bytes")) {
                    encodedValues.add(Hash.sha3(stringValue));
                } else if (itemType.startsWith("bytes") && itemType.length() > 5) {
                    BigInteger valueBigInt = Numeric.toBigInt(stringValue);
                    byte[] valueBytes32 = Numeric.toBytesPadded(valueBigInt, 32);
                    Bytes32 bytesData = new Bytes32(valueBytes32);
                    String encodedValue = TypeEncoder.encode(bytesData);
                    encodedValues.add(encodedValue);
                } else if (itemType.startsWith("uint")) {
                    BigInteger bigIntValue = Numeric.toBigInt(stringValue);
                    Uint uintData = new Uint(bigIntValue);
                    String encodedValue = TypeEncoder.encode(uintData);
                    encodedValues.add(encodedValue);
                } else if (itemType.startsWith("int")) {
                    BigInteger bigIntValue = Numeric.toBigInt(stringValue);
                    Int intData = new Int(bigIntValue);
                    String encodedValue = TypeEncoder.encode(intData);
                    encodedValues.add(encodedValue);
                } else if (itemType.equals("address")) {
                    Address address = new Address(stringValue);
                    encodedValues.add(TypeEncoder.encode(address));
                } else if (itemType.equals("bool")) {
                    Boolean boolValue = (Boolean) value;
                    Bool boolData = new Bool(boolValue);
                    encodedValues.add(TypeEncoder.encode(boolData));
                } else if (typeDefs.getTypes().containsKey(itemType)) {
                    Gson gson = new Gson();
                    Map map = gson.fromJson(gson.toJson(value), LinkedTreeMap.class);
                    String typeValueEncoded = encodeData(itemType, map, typeDefs);
                    encodedValues.add(typeValueEncoded);
                } else {
                    throw new IllegalArgumentException("unsupport solidity data type: " + itemType);
                }
            } else {
                throw new IllegalStateException("can not get value for " + typeItem.getName() + " in type " + typeItem.getItemType());
            }
        }

        Collections.reverse(encodedValues);
        StringBuilder result = new StringBuilder();
        for (String encodedValue : encodedValues) {
            result.append(Numeric.cleanHexPrefix(encodedValue));
        }
        return result.toString();
    }

    private byte[] hashType(String dataType, Types allTypes) {
        String encodedTypeStr = encodeType(dataType, allTypes);
        return Hash.sha3(encodedTypeStr.getBytes());
    }

    private String encodeType(String dataType, Types allTypes) {
        List<Type> types = new ArrayList<>();
        List<Type> dependencies = findTypeDependencies(dataType, allTypes, types);
        Collections.sort(dependencies, (t1, t2) -> t1.getName().compareTo(t2.getName()));
        StringBuilder result = new StringBuilder();
        for (Type dependence : dependencies) {
            result.append("(");
            for (TypeItem typeItem : dependence.getTypeItems()) {
                result.append(typeItem.getItemType()).append(" ").append(typeItem.getName()).append(",");
            }
            result.deleteCharAt(result.length()).append(")");
        }
        return result.toString();
    }

    private List<Type> findTypeDependencies(String targetType, Types allTypes, List<Type> results) {
        Type type = allTypes.getTypes().get(targetType);
        if (type != null) {
            List<Type> typeDependencies = null;
            for (TypeItem item : type.getTypeItems()) {
                results.add(type);
                typeDependencies = findTypeDependencies(item.getItemType(), allTypes, results);
                if (typeDependencies != null && !typeDependencies.contains(type)) {
                    typeDependencies.add(type);
                }
            }
            return typeDependencies;
        } else {
            return results;
        }
    }
}
