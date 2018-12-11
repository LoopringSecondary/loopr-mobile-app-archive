/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-10 1:28 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk;

import org.junit.Before;
import org.junit.Test;
import org.web3j.ens.EnsResolver;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.Web3jService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ENSTest {

    private Web3j web3j;

    private Web3jService web3jService;

    private EnsResolver ensResolver;

    @Before
    public void setUp() {
        web3jService = mock(Web3jService.class);
        web3j = Web3jFactory.build(web3jService);
        ensResolver = new EnsResolver(web3j);
    }

//    @Test
//    public void testLookupAddress() throws Exception {
//        NetVersion netVersion = new NetVersion();
//        netVersion.setResult(Byte.toString(ChainId.MAINNET));
//
//        String resolverAddress =
//                "0x0000000000000000000000004c641fb9bad9b60ef180c31f56051ce826d21a9a";
//        String contractAddress =
//                "0x00000000000000000000000019e03255f667bdfd50a32722df860b1eeaf4d635";
//
//        EthCall resolverAddressResponse = new EthCall();
//        resolverAddressResponse.setResult(resolverAddress);
//
//        EthCall contractAddressResponse = new EthCall();
//        contractAddressResponse.setResult(contractAddress);
//
//        when(web3jService.send(any(Request.class), eq(NetVersion.class)))
//                .thenReturn(netVersion);
//        when(web3jService.send(any(Request.class), eq(EthCall.class)))
//                .thenReturn(resolverAddressResponse);
//        when(web3jService.send(any(Request.class), eq(EthCall.class)))
//                .thenReturn(contractAddressResponse);
//
//        assertThat(ensResolver.loo("web3j.eth"),
//                is("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
//    }

    @Test
    public void testEns() {
        SDK.initSDK();
        Web3j web3j = SDK.getWeb3j();
        EnsResolver ensResolver = new EnsResolver(web3j);
        String resolve = ensResolver.reverseResolve("0xa3ae668b6239fa3eb1dc26daabb03f244d0259f0");
        assertEquals(ensResolver.resolve("wangdong.eth"), "0xe7b95e3aefeb28d8a32a46e8c5278721dad39550");


    }
}
