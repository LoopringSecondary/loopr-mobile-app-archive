package leaf.prod.walletsdk.model.sign;

import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;
import org.web3j.utils.Numeric;

import lombok.Data;
import lombok.val;

/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-04-01 3:32 PM
 * Cooperation: loopring.org 路印协议基金会
 */

@Data
public class Bitstream {

    private String data;

    private static final Integer ADDRESS_LENGTH = 20;

    private static final BigInteger UINT256MAX = new BigInteger(StringUtils.repeat("f", 64), 16);

    public String getData() {
        return StringUtils.isEmpty(data) ? "0x0" : "0x" + data;
    }

    public byte[] getBytes() {
        return Numeric.hexStringToByteArray(data);
    }

    public Integer getLength() {
        return data.length() / 2;
    }

    public Integer addAddress(String address, Boolean forceAppend) {
        return addAddress(address, ADDRESS_LENGTH, forceAppend);
    }

    private Integer addAddress(String address, Integer numBytes, Boolean forceAppend) {
        String value = StringUtils.isEmpty(address) ? "0" : address;
        return insert(Numeric.toHexStringNoPrefixZeroPadded(Numeric.toBigInt(value), numBytes * 2), forceAppend);
    }

    public Integer addUint16(BigInteger num, Boolean forceAppend) {
        return addBigInt(num, 2, forceAppend);
    }

    public Integer addInt16(BigInteger num, Boolean forceAppend) {
        if (num.signum() != -1) {
            return addBigInt(num, 2, forceAppend);
        } else {
            BigInteger negative = num.add(UINT256MAX).add(BigInteger.ONE);
            String int16Str = negative.toString(16).substring(60, 64);
            return addHex(int16Str, forceAppend);
        }
    }

    public Integer addUint32(BigInteger num, Boolean forceAppend) {
        return addBigInt(num, 4, forceAppend);
    }

    public Integer addUint(BigInteger num, Boolean forceAppend) {
        return addBigInt(num, 32, forceAppend);
    }

    public Integer addNumber(BigInteger num, Integer numBytes, Boolean forceAppend) {
        return addBigInt(num, numBytes, forceAppend);
    }

    public Integer addBoolean(Boolean b, Boolean forceAppend) {
        BigInteger i = b ? BigInteger.ONE : BigInteger.ZERO;
        return addBigInt(i, 1, forceAppend);
    }

    public Integer addBytes32(String x, Boolean forceAppend) {
        String strWithoutPrefix = Numeric.cleanHexPrefix(x);
        if (strWithoutPrefix.length() > 64) {
            throw new IllegalArgumentException("invalid bytes32 str: too long" + x);
        }
        val strPadded = strWithoutPrefix + StringUtils.repeat("0", 64 - strWithoutPrefix.length());
        return insert(strPadded, forceAppend);
    }

    public Integer addHex(String x, Boolean forceAppend) {
        return insert(Numeric.cleanHexPrefix(x), forceAppend);
    }

    public Integer addRawBytes(byte[] bytes, Boolean forceAppend) {
        return insert(Numeric.cleanHexPrefix(Numeric.toHexString(bytes)), forceAppend);
    }

    public Integer addBigInt(BigInteger num, Integer numBytes, Boolean forceAppend) {
        return insert(Numeric.toHexStringNoPrefixZeroPadded(num, numBytes * 2), forceAppend);
    }

    private Integer insert(String x, Boolean forceAppend) {
        int offset = getLength();
        if (!forceAppend) {
            int start = 0;
            while (start != -1) {
                start = data.indexOf(x, start);
                if (start != -1) {
                    if ((start % 2) == 0) {
                        offset = start / 2;
                        return offset;
                    }
                }
            }
        }
        data += x;
        return offset;
    }
}
