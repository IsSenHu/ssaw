import com.ssaw.netty.echo.demo.FixedLengthFrameDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author HuSen
 * @date 2019/6/11 10:40
 */
public class FixedLengthFrameDecoderTest {

    @Test
    public void test() {
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buffer.writeByte(i);
        }
        ByteBuf input = buffer.duplicate();
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        assertTrue(embeddedChannel.writeInbound(input.retain()));
        assertTrue(embeddedChannel.finish());

        ByteBuf read = embeddedChannel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        read = embeddedChannel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        read = embeddedChannel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        assertNull(embeddedChannel.readInbound());
        buffer.release();
    }
}