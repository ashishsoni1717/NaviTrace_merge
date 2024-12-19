package org.navitrace.protocol;

import org.junit.jupiter.api.Test;
import org.navitrace.ProtocolTest;

public class GalileoProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        var decoder = inject(new GalileoProtocolDecoder(null));

        verifyPositions(decoder, binary(
                "011801018202130338363833343530333230343234323604640010a406207caa9f5b300c830a7901ca0ec802330000000034b802350540003e41703f422b1043234504004600e09000000000a000a100a200a300a400a500a600a700a800a900aa00ab00ac00ad00ae00af00b00000b10000b20000b30000b40000b50000b60000b70000b80000b90000c000000000c100000000c200000000c300000000c400c500c600c700c800c900ca00cb00cc00cd00ce00cf00d000d100d200d4d3140000d60000d70000d80000d90000da0000db00000000dc00000000dd00000000de00000000df00000000f000000000f100000000f200000000f300000000f400000000f500000000f600000000f700000000f800000000f9000000008960"));

        verifyPositions(decoder, binary(
                "01bf83043200101ee4209832bc62300549589302511aaa013300002e00342e02350440003b41b15d42d50e4326450e0046040050000051000052fc5c5300006100008b009000000000d400000000e201000000e376000000e4efce0100e53b590200e600000000e773000000e800000000e9a002d007ea140000d6021b00f8430220ac760000000000000000043200101de4201232bc62300549589302511aaa013300002e00342e02350440012b41b55d42d40e4326450e0046040050000051000052145d5300006100008b009000000000d400000000e201000000e376000000e4efce0100e53b590200e600000000e773000000e800000000e9a002d007ea140000d6021b00f8430220ac760000000000000000043200101ce4208e2ebc62300549589302511aaa013300002e00342e02350440013b41a95d42cd0e4325450f0046040050000051000052235d5300006100008b009000000000d400000000e201000000e376000000e4efce0100e53b590200e600000000e773000000e802000000e9a002d007ea140000d6021b00f8430220ac760000000000000000043200101be4208b2ebc62300549589302511aaa013300002e00342e02350440013b41a45d42cd0e432545090046040050000051000052115d5300006100008b009000000000d400000000e201000000e375000000e48ac90100e53a590200e673000000e773000000e806000000e9a002d007ea140000d6021b00f8430220ac760000000000000000043200101ae420642ebc62300549589302511aaa013300002e00342e02350440013b419f5d42cd0e4324450b00460600500000519313521c5d5300006100008b009000000000d400000000e201000000e300000000e406000000e5c5580200e673000000e700000000e801000000e9a002d007ea140000d6021b00f8430220ac7600000000000000000432001019e420632ebc62300549589302511aaa013300002e00342e02350440013b41725d42cd0e4324450b0046060050000051ab1352035d5300006100008b009000000000d400000000e201000000e300000000e406000000e5c5580200e673000000e700000000e8d6021b00e9a002d007ea140000d6021b00f8430220ac7600000000000000000432001018e4205c2ebc62300549589302511aaa013300002e00342e02350440013b41955d42cd0e4324450a00460400500000510000520b5d5300006100008b009000000000d400000000e201000000e30d000000e4a4350000e5c5580200e600000000e700000000e8d6021b00e9a002d007ea140000d6021b00f8430220ac76000000000000000099f3"));

        verifyPositions(decoder, binary(
                "017583018202120338363833343530333230363635373304520010384520c850975b300cc03a910107cbf9023365000607341300350640012a41236a4215104329450400460020500000510000520000530000540000550000c000000000c100000000c44bc500c6ffc700c800c900ca00cb00d4993b0500d64100d70000d8be02d90000da0000db00000000dc00000000dd00000000de00000000df00000000f000000000f100000000f200000000f300000000018202120338363833343530333230363635373304520010394520c950975b300cab3a91010ecbf902336000be06341300350640012a41266a4216104329450400460020500000510000520000530000540000550000c000000000c100000000c44bc500c6ffc700c800c900ca00cb00d49b3b0500d64100d70000d8bc02d90000da0000db00000000dc00000000dd00000000de00000000df00000000f000000000f100000000f200000000f3000000000182021203383638333435303332303636353733045200103a4520ca50975b300c953a910113cbf9023358008f06341300350640012a41206a4215104329450400460020500000510000520000530000540000550000c000000000c100000000c44bc500c6ffc700c800c900ca00cb00d49e3b0500d64100d70000d8ba02d90000da0000db00000000dc00000000dd00000000de00000000df00000000f000000000f100000000f200000000f3000000000182021203383638333435303332303636353733045200103b45204251975b300c6d3a91011dcbf9023300008a06341300350640013a41726a4216104329450400460020500000510000520000530000540000550000c000000000c100000000c44bc500c6ffc700c800c900ca00cb00d4a33b0500d64800d70000d80003d90000da0000db00000000dc00000000dd00000000de00000000df00000000f000000000f100000000f200000000f3000000000182021203383638333435303332303636353733045200103c4520bb51975b300c6d3a91011dcbf9023300008a06341300350640013a41816a4216104329450400460020500000510000520000530000540000550000c000000000c100000000c44bc500c6ffc700c800c900ca00cb00d4a33b0500d64800d70000d80003d90000da0000db00000000dc00000000dd00000000de00000000df00000000f000000000f100000000f200000000f300000000e007"));

        verifyNull(decoder, binary(
                "07e10300ffd8ffe000114a464946000101010000000000000affe1011445786966000049492a000800000005000e010200140000004a0000000f0102000a0000005e000000100102000a0000006800000032010200130000007200000025880400010000008600000000000000494d45492033353336313230383730353035393247616c696c656f536b7971717a6d202020202020323031383a30383a30392030363a35393a303100060001000200020000004e0000000200050003000000d40000000300020002000000450000000400050003000000ec000000050001000100000000000000060005000100000004010000000000008e7f930240420f0000000000010000000000000001000000a11aaa0140420f00000000000100000000000000010000003300000001000000ffdb0084000d09090b09080d0b0a0b0e0d0d0f131f1413111113261b1d171f2d28302f2d282c2b3238483d323544362b2c3f553f444a4d505150303c585f584e5e484f504d010d0e0e131013251414254d342c344d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4dffc000110801e0028003012100021101031101ffdd0004000affc401a20000010501010101010100000000000000000102030405060708090a0b100002010303020403050504040000017d01020300041105122131410613516107227114328191a1082342b1c11552d1f02433627282090a161718191a25262728292a3435363738393a434445464748494a535455565758595a636465666768696a737475767778797a838485868788898a92939495969798999aa2a3a4a5a6a7a8a9aab2b3b4b5b6b7b8b9bac2c3c4c5c6c7c8c9cad2d3d4d5d6d7d8d9dae1e2e3e4e5e6e7e8e9eaf1f2f3f4f5f6f7f8f9fa0100030101010101010101010000000000000102030405060708090a0b1100020102040403040705040400010277000102031104052131061241510761711322328108144291a1b1c109233352f0156272d10a162434e125f11718191a262728292a35363738393a434445464748494a535455565758595a636465666768696a737475767778797a82838485868788898a92939495969798999aa2a3a4a5a6a7a8a9aab2b3b4b5b6b7b8b9bac2c3c4c5c6c7c8c9cad2d3d4d5d6d7d8d9dae2e3e4e5e6e7e8e9eaf2f3f4f5f6f7f8f9faffda000c03010002110311003f00f41237526768c119a0811803c8e29a7eb40075151ba9e2801436060d0df779ef408685c50092db6801e63c9c83834c772a3140c818163522924fa5310367b9a8f3ce2806382734e2bc7340c68031c52ab1e86811ffd0edcf0335170fd7b5333023151c83d4ba"));

        verifyPositions(decoder, false, binary(
                "01560003383636303530303338343337353836044701e000000000e13c494e414c4c3a696e303d31313230362c696e313d302c696e323d302c696e333d302c696e343d302c696e353d302c4163633d3536363932343732353bfdef"));

        verifyPositions(decoder, false, binary(
                "012a0003383633353931303233353137333732046600e000000000e1104f555428332e2e3029203d2031313130bb29"));

        verifyPositions(decoder, binary(
                "0144030338363832303430303132363939333404320010ee0f20f5a86c57300570172f03bc7dfd023363002604343e00351c40092a414a6842af0e432445000046030050246b51666a524c055300000338363832303430303132363939333404320010ed0f20f4a86c57300570172f03b47dfd023363000d05343e00351140090a41c56742a60e432445000046030050b56a514f6a521b045300000338363832303430303132363939333404320010ec0f20e6a86c57300b34172f03287efd023300000000344900350d40290a41562742030b43234500004603205023455190445295005300000338363832303430303132363939333404320010eb0f20e4a86c57300b34172f03287efd023300000000344900350d40290b41000042bd0b432345000046032050dc31518c315200005300000338363832303430303132363939333404320010ea0f20c7a86c57300b34172f03287efd023300000000344900350d40a90b41000042050d43234500004600205000005100005200005300000338363832303430303132363939333404320010e90f204fa86c57300b34172f03287efd023300000000344900350d40a90b41000042ff0c43244500004600205000005100005200005300000338363832303430303132363939333404320010e80f20d7a76c57300b34172f03287efd023300000000344900350d40a90b41000042fd0c43244500004600205000005100005200005300000338363832303430303132363939333404320010e70f205fa76c57300b34172f03287efd023300000000344900350d40a90b41000042fd0c43254500004600205000005100005200005300000338363832303430303132363939333404320010e60f20e7a66c57300b34172f03287efd023300000000344900350d40a90b41000042fd0c43264500004600205000005100005200005300000338363832303430303132363939333404320010e50f206fa66c57300468172f03907cfd023300007a0a343600352b40a90b41000042030d43274500004600205000005100005200005300000338363832303430303132363939333404320010e40f2051a66c5730048c172f03ac7cfd02335300980a341600352b40a12b41000042040d43274500004600e0500000510000520000530000abde"));

        verifyNull(decoder, binary(
                "011380033836383230343030313534393038370432008590"));
        
        verifyPositions(decoder, binary(
                "01cf030446ba10630320a7054c533008f86c8e0310062c043347049e02344000350940013241506b428f10432244aeea572045f9004604a0500000510000529a6b5300000446ba10712420ce1c4b533009b4f06703043df4033381037b0a343800350a40093241db6b428f10432544c05ef81f45f9004604a050000051000052886b5300000446ba10702420c11c4b53300a54f16703c450f403336e034e0a343900350840093241dd6b428f1043254491eaf71f45f9004604a050000051000052c26b5300000446ba106f2420b31c4b53300cecf267033865f403336a03300a343800350740093241e66b429010432544b446582045f9004604a050000051000052f76b5300000446ba106e2420a61c4b53300c9cf467038878f403337b03370a343800350740093241b56b428f10432544ba46f81f45f9004604a050000051000052c66b5300000446ba106d2420991c4b53300bc8f56703508cf403338d036e0a343700350840093241d66b428f10432544b4ea572045f9004604a050000051000052846b5300000446ba106c24208c1c4b533008c8f5670370a0f403338703920a343a00350e40093241c76b428f10432544c0fef71f45f9004604a0500000510000528d6b5300000446ba106b24207f1c4b533009a4f5670338b4f403337603920a343c00350a40093241d06b428f104325449146a81f45f9004604a0500000510000528a6b5300000446ba106a2420721c4b53300b9cf56703ecc7f403337103810a343a00350840093241ca6b428f10432544d12e582045f9004604a050000051000052996b5300000446ba10692420651c4b53300a64f6670358dbf403337a03490a343900350840093241e56b429010432544aed2f71f45f9004604a050000051000052b26b5300000446ba10682420581c4b5330094cf86703e0eef4033381030c0a343a00350940093241f96b428f10432544cb2e182145f9004604a050000051000052926b5300000446ba106724204b1c4b533009f8fa67032802f503337b03fc09343b00350a40093241d86b428f10432544c0ea772145f9004604a0500000510000529e6b5300000446ba106624203e1c4b533009a0fd67036815f503338403fd09343c00350a40093241a86b428f10432544ae2e582045f9004604a050000051000052a86b5300000446ba10652420311c4b53300944006803b028f503338003ff09343d00350940093241dc6b428e10432544a8fea71f45f9004604a050000051000052e26b5300000446ba10642420241c4b533008f0026803083cf503338b03f909343c00350d40093241d36b428f10432544c0eaa71f45f9004604a050000051000052ab6b530000ff3f"));
        
        verifyPositions(decoder, binary(
                "011e8304320010270220dbd2f051300a90cf740328ac59033300000000347600351240012a41e92e42500f431f440006c814450f00460020500000510000520000530000540000550000560000570000580000600000610000620000a000a100a200a300a400a500a600a700a800a900aa00ab00ac00ad00ae00af00b00000b10000b20000b30000b40000b50000b60000b70000b80000b90000c000000000c100000000c200000000c300000000c400c500c600c700c800c900ca00cb00cc00cd00ce00cf00d000d100d200d471020000d60000d70000d80000d90000da0000db00000000dc00000000dd00000000de00000000df00000000f000000000f100000000f200000000f30000000004320010260220bdd2f051300590cf740328ac59033300000000347600351440090a41f02e427b0f431f44ff0db814450f00460000500000510000520000530000540000550000560000570000580000600000610000620000a000a100a200a300a400a500a600a700a800a900aa00ab00ac00ad00ae00af00b00000b10000b20000b30000b40000b50000b60000b70000b80000b90000c000000000c100000000c200000000c300000000c400c500c600c700c800c900ca00cb00cc00cd00ce00cf00d000d100d200d471020000d60000d70000d80000d90000da0000db00000000dc00000000dd00000000de00000000df00000000f000000000f100000000f200000000f300000000043200102502208ed2f051300ed8d0740304ac5903330000000034a500350a40012a41ec2e422d0f431f440016b814450f00460020500000510000520000530000540000550000560000570000580000600000610000620000a000a100a200a300a400a500a600a700a800a900aa00ab00ac00ad00ae00af00b00000b10000b20000b30000b40000b50000b60000b70000b80000b90000c000000000c100000000c200000000c300000000c400c500c600c700c800c900ca00cb00cc00cd00ce00cf00d000d100d200d44d020000d60000d70000d80000d90000da0000db00000000dc00000000dd00000000de00000000df00000000f000000000f100000000f200000000f300000000622e"));

        verifyPositions(decoder, binary(
                "01d48304320010020520a5829f58300f50dc8a024c0965013300000000344102350740003a41e14b426610431b4459fa672a4500004601a050364c510000520000530000c000000000c100000000c200000000c300000000d80000dd00000000e293000000043200100105202d829f58300f50dc8a024c0965013300000000344102350740003a41d04b426110431b445702882a4500004601a050374c510000520000530000c000000000c100000000c200000000c300000000d80000dd00000000e29400000004320010000520b5819f58300f50dc8a024c0965013300000000344102350740003a419e4b426a10431c4456fab72a4500004601a050434c510000520000530000c000000000c100000000c200000000c300000000d80000dd00000000e29500000004320010ff04203d819f58300f50dc8a024c0965013300000000344102350740003a41874b426310431c4454fe572a4500004601a050334c510000520000530000c000000000c100000000c200000000c300000000d80000dd00000000e29600000004320010fe0420c5809f58300f50dc8a024c0965013300000000344102350840003a41a24b426710431c4457fea72a4500004601a050214c510000520000530000c000000000c100000000c200000000c300000000d80000dd00000000e29700000004320010fd04204d809f58300f50dc8a024c0965013300000000344102350840003a41a34b426310431c4455f6772a4500004601a0502e4c510000520000530000c000000000c100000000c200000000c300000000d80000dd00000000e29900000004320010fc0420d57f9f58300f50dc8a024c0965013300000000344102350840003a41bd4b426510431d4458fe672a4500004601a0501f4c510000520000530000c000000000c100000000c200000000c300000000d80000dd00000000e29700000004320010fb04205d7f9f58300f50dc8a024c0965013300000000344102350840003a41b54b426310431d4456fa772a4500004601a0502d4c510000520000530000c000000000c100000000c200000000c300000000d80000dd00000000e29500000004320010fa0420e57e9f58300f50dc8a024c0965013300000000344102350840003a41b24b426210431e4454fa872a4500004601a050fe4b510000520000530000c000000000c100000000c200000000c300000000d80000dd00000000e29000000004320010f904206d7e9f58300f50dc8a024c0965013300000000344102350a40003a41af4b426710431f4458fea72a4500004601a0500a4c510000520000530000c000000000c100000000c200000000c300000000d80000dd00000000e28900000067c5"));

    }

    @Test
    public void testDecodeIridium() throws Exception {

        var decoder = inject(new GalileoProtocolDecoder(null));

        decoder.setCompressed(false);

        verifyPosition(decoder, binary(
                "01004e01001c0747ea59333030323334303639363034353930000012000063c85e6903000b0321a8f846aba50000000202001e205f5ec863300c4643fdfdbbe6c8fb330000000034e7013505d400000000"));

        /*decoder.setCompressed(true);

        verifyPosition(decoder, binary(
                "01003a01001c07553e40333030323334303639363034353930020021000063d1921c03000b0321ac2c4545b20000009f02000ac200000000db00000000"));*/

    }

}
