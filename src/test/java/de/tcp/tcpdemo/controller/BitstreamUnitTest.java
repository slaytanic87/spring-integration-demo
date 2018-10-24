package de.tcp.tcpdemo.controller;

import de.tcp.tcpdemo.controller.deserializer.BitstreamReader;
import de.tcp.tcpdemo.controller.serializer.BitstreamWriter;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Lam, Le (msg systems ag) 2018
 */
public class BitstreamUnitTest {

    private BitstreamWriter writer;

    @Before
    public void setup() {
        writer = new BitstreamWriter();
    }

    @Test
    public void shouldWriteBigEndianValue() {
        // given
        long toBeSerialize = 736L;

        // when
        writer.writeV(toBeSerialize);
        byte[] binary = writer.buildByteArray();
        long value = new BitstreamReader(binary).readV();

        // then
        Assertions.assertThat(value).isEqualTo(toBeSerialize);
    }

    @Test
    public void shouldWriteValue() {
        // given
        long toBeSerialize = 1213123L;
        int byteLength = 8;
        writer.writeLv(toBeSerialize, byteLength);

        // when
        byte[] binary = writer.buildByteArray();
        long value = new BitstreamReader(binary).readV(byteLength);

        // then
        Assertions.assertThat(value).isEqualTo(toBeSerialize);
    }

    @Test
    public void shouldCalcCorrectLength() {
        // given
        long value1 = 2000;
        long value2 = 0;

        // when
        int length1 = writer.calcLength(value1);
        int length2 = writer.calcLength(value2);

        // then
        Assertions.assertThat(length1).isEqualTo(2);
        Assertions.assertThat(length2).isEqualTo(1);
    }
}