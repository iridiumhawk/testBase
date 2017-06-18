package com.cherkasov;

import org.junit.Test;

/**
 * Created by Lu on 18.06.2017.
 */
public class CheckBOM {
    @Test
    public void check() {
        byte[] tmp = {(byte) 0xEF,(byte) 0xBB,(byte) 0xBF};

        String test = new String (tmp)+"lalala";

        System.out.println(test);
        byte[] bom = test.getBytes();
        byte[] dest = new byte[bom.length-3];
        if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB) && (bom[2] == (byte) 0xBF)) {
            String encoding = "UTF-8";
            System.arraycopy(bom, 3, dest, 0, bom.length-3);
            test = new String(dest);
        }

        System.out.println(test);
        //check for first 4 bytes
//        tmp = tmp.replace("\uFEFF", "");

    }
}
