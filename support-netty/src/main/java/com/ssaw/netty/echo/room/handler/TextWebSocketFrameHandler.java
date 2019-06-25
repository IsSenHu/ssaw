package com.ssaw.netty.echo.room.handler;

import com.ssaw.netty.echo.room.cons.Prefix;
import com.ssaw.netty.echo.room.vo.UserVO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HuSen
 * @date 2019/6/11 17:57
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	private final ChannelGroup group;

	private static final String HANDSHAKE_COMPLETE = "HANDSHAKE_COMPLETE";

	private static final Map<String, UserVO> USER_VO_MAP = new ConcurrentHashMap<>();

	private static final Map<String, ChannelId> USERNAME_CHANNEL_MAP = new ConcurrentHashMap<>();

	private static final Map<ChannelId, String> CHANNEL_USERNAME_MAP = new ConcurrentHashMap<>();

	private static final Map<ChannelId, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

	public TextWebSocketFrameHandler(ChannelGroup group) {
		this.group = group;
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof WebSocketServerProtocolHandler.ServerHandshakeStateEvent && ((WebSocketServerProtocolHandler.ServerHandshakeStateEvent) evt).name().equals(HANDSHAKE_COMPLETE)) {
			ctx.pipeline().remove(HttpRequestHandler.class);
			Channel channel = ctx.channel();
			group.writeAndFlush(new TextWebSocketFrame("Client " + channel + "joined"));
			group.add(channel);
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
		String text = msg.text();
		boolean haveJoined = CHANNEL_MAP.containsKey(ctx.channel().id());
		if (StringUtils.isNotBlank(text)) {
			TextWebSocketFrame frame;
			if (text.startsWith(Prefix.USERNAME)) {
				String username = StringUtils.substringAfter(text, Prefix.SPLIT);
				if (StringUtils.isNotBlank(username)) {
					if (!USER_VO_MAP.containsKey(username)) {
						UserVO userVO = new UserVO();
						userVO.setFriends(new HashSet<>());
						userVO.setUsername(username);
						USER_VO_MAP.put(username, userVO);
						USERNAME_CHANNEL_MAP.put(username, ctx.channel().id());
						CHANNEL_USERNAME_MAP.put(ctx.channel().id(), username);
						CHANNEL_MAP.put(ctx.channel().id(), ctx.channel());
						frame = new TextWebSocketFrame("成功加入聊天室");
					} else {
						frame = new TextWebSocketFrame("用户名已存在");
					}
				} else {
					frame = new TextWebSocketFrame("用户名不能为空");
				}
				ctx.writeAndFlush(frame);
			} else if (text.startsWith(Prefix.ADD_FRIEND) && haveJoined) {
				String friend = StringUtils.substringAfter(text, Prefix.SPLIT);
				if (StringUtils.isNotBlank(friend) && USER_VO_MAP.containsKey(friend)) {
					String my = CHANNEL_USERNAME_MAP.get(ctx.channel().id());
					USER_VO_MAP.get(my).getFriends().add(friend);
					USER_VO_MAP.get(friend).getFriends().add(my);
					frame = new TextWebSocketFrame("添加好友成功");
				} else {
					frame = new TextWebSocketFrame("好友用户名不正确");
				}
				ctx.writeAndFlush(frame);
			} else if (text.startsWith(Prefix.SEND_FRIEND) && haveJoined) {
				String[] split = StringUtils.substringAfter(text, Prefix.SPLIT).split(Prefix.SPLIT);
				if (split.length > 1) {
					if (USERNAME_CHANNEL_MAP.containsKey(split[0])) {
						ChannelId channelId = USERNAME_CHANNEL_MAP.get(split[0]);
						Channel channel = CHANNEL_MAP.get(channelId);
						frame = new TextWebSocketFrame(CHANNEL_USERNAME_MAP.get(ctx.channel().id()).concat(Prefix.SPLIT).concat(split[1]));
						channel.writeAndFlush(frame);
					} else {
						frame = new TextWebSocketFrame("该好友不存在");
						ctx.writeAndFlush(frame);
					}
				} else {
					frame = new TextWebSocketFrame("参数不正确");
					ctx.writeAndFlush(frame);
				}
			} else {
				frame = new TextWebSocketFrame("未知的操作");
				ctx.writeAndFlush(frame);
			}
		}
		// 空消息，不做任何处理 会自动的释放
	}
}