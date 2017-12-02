package com.github.domktorymysli.grenton.cipher.encoder;

import com.github.domktorymysli.grenton.cipher.model.CipherKey;
import com.github.domktorymysli.grenton.cipher.model.MessageDecoded;
import com.github.domktorymysli.grenton.cipher.model.MessageEncoded;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import java.security.*;

public class Decoder {

    private CipherKey cipherKey;

    public Decoder(CipherKey cipherKey) {
        this.cipherKey = cipherKey;
    }

    public MessageDecoded decode(MessageEncoded msg) throws NoSuchPaddingException, ShortBufferException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException {
        return this.decode(msg.getMsg(), msg.getLength());
    }

    public MessageDecoded decode(byte[] message, int msgLength) throws NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, ShortBufferException, NoSuchProviderException, InvalidKeyException {
        {
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, cipherKey.getSecretKey(), cipherKey.getIvSpec());

            byte[] output = new byte[cipher.getOutputSize(msgLength)];
            int length = cipher.update(message, 0, msgLength, output, 0);
            length += cipher.doFinal(output, length);
            byte[] result = new byte[length];
            System.arraycopy(output, 0, result, 0, length);

            return new MessageDecoded(result);
        }
    }
}
