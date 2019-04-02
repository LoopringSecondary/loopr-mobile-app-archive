package leaf.prod.walletsdk.model.sign;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-04-01 4:29 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public interface IEIP712Support {

    class TypeItem {
        private String name;
        private String itemType;
    }

    class Type {
        private String name;
        private List<TypeItem> typeItems;
    }

    class Types {
        private Map<String, Type> types;
    }

    class EIP712TypedData {
        private Types types;
        private String primaryType;
        private Map<String, Object> domain;
        private Map<String, Object> message;
    }

    EIP712TypedData jsonToTypedData(String jsonString);

    String getEIP712Message(EIP712TypedData typedData);
}
