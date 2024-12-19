package org.navitrace.protocol;

import org.junit.jupiter.api.Test;
import org.navitrace.ProtocolTest;

public class SnapperProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        var decoder = inject(new SnapperProtocolDecoder(null));

        verifyNull(decoder, binary(
                "4b0341a6b0c608000040000005000000000000007d5e14010068656c6c6f"));

        verifyAttributes(decoder, binary(
                "4b044daff87aff5b8aad0000d4000000000000000b001337000500210008000d0000ffffffff7f2300190000000000000001000000000224ff000003404000000400000034008c007b2273223a22303034313438222c226332223a2230303030303030303030303030303030222c226132223a2230303030303030303030303830304530222c226f223a2230303030222c2274223a2230303030222c227a223a223030222c2277223a223030222c2272223a222d222c226d223a223030303030303030222c2262223a223030303030303030227d32000a007b2266223a223234227d330007007b2262223a5d7d"));

        verifyPosition(decoder, binary(
                "4b044daff87aff5b8aad00007b010000000013ea0c006837000500210008003e000058c48fa94823001900000080000200018080000002deff0f0003404000000400000034008c007b2273223a22303034303438222c226332223a2230303030303030303030303030303030222c226132223a2230303030303030303030303030303030222c226f223a2230303030222c2274223a2230303030222c227a223a223030222c2277223a223030222c2272223a222d222c226d223a223030303030303030222c2262223a223030303030303030227d320079007b2266223a224445222c2274223a22303932383336222c2264223a22313530343234222c226c61223a22353334312e34333732222c226c6f223a2230303935342e30373036222c2261223a22382e34222c2273223a22302e3030222c2263223a2232362e3036222c227376223a223135222c2270223a22227d33003f007b2263223a22323632222c226e223a223033222c2262223a5b7b226c223a2232423334222c2263223a223030303041313231222c2273223a223132227d5d7d"));

        verifyPosition(decoder, binary(
                "4b044daff87aff5b8aad0000430100000000bb870c005b37000500210008003e0100ffffffff7f2300190000007f000000018080000002deff080003404000000400000034008c007b2273223a22303034303438222c226332223a2230303030303030303030303030303030222c226132223a2230303030303030303030303030303030222c226f223a2230303030222c2274223a2230303030222c227a223a223030222c2277223a223030222c2272223a222d222c226d223a223030303030303030222c2262223a223030303030303030227d320079007b2266223a224445222c2274223a22303832303335222c2264223a22313730343234222c226c61223a22353334312e34323635222c226c6f223a2230303935342e30373935222c2261223a22362e32222c2273223a22322e3237222c2263223a2233302e3135222c227376223a223038222c2270223a22227d330007007b2262223a5d7d"));

        verifyNull(decoder, binary(
                "5003"));

    }

}
