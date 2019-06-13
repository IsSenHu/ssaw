import com.ssaw.netty.echo.demo.AbsIntegerEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author HuSen
 * @date 2019/6/11 10:56
 */
public class AbsIntegerEncodeTest {

    @Test
    public void test() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buf.writeInt(i * -1);
        }
        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
        assertTrue(channel.writeOutbound(buf));
        assertTrue(channel.finish());
        for (int i = 1; i < 10; i++) {
            assertSame(i, channel.readOutbound());
        }
        assertNull(channel.readOutbound());
    }
}