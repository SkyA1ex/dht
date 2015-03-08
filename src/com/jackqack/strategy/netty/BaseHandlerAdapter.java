package com.jackqack.strategy.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

/**
 * Created by jackqack on 3/8/15.
 */
public class BaseHandlerAdapter extends ChannelHandlerAdapter{

    // Метод вызывается при создании канала
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final ByteBuf buf = ctx.alloc().buffer(6);
        buf.writeBytes("hello\n".getBytes());
        ctx.writeAndFlush(buf);

        NettyServer.allChannels.add(ctx.channel());
        // Сообщить всем о подключенном пользователе и
        // вывести количество подключенных пользователей
        for (Channel ch: NettyServer.allChannels){
            final ByteBuf buf2 = ch.alloc().buffer(50);
            StringBuilder sb = new StringBuilder();
            if (ctx.channel()!=ch)
                sb.append("new user connected\n");
            sb.append(NettyServer.allChannels.size());
            sb.append(" users online\n");
            buf2.writeBytes(sb.toString().getBytes());
            ch.writeAndFlush(buf2);
        }
    }

    // Метод вызывается при получении данных
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ChannelFuture cf;
        ByteBuf b = (ByteBuf) msg;
        int close = b.getInt(0);
        // Отключить канал, если пришло "quit" ( c int быстрее и проще! )
        if (close==1903520116) {
            cf = ctx.close();
            cf.addListener(ChannelFutureListener.CLOSE);
            return;
        }
        // Отправить сообщение всем пользователям, кроме самого отправителя
        for (Channel ch: NettyServer.allChannels){
            if (ch == ctx.channel())
                continue;

            ch.writeAndFlush(msg);
        }
    }

    // Метод вызывается при отключении канала
    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise)
            throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("user has left the chat\n");
        sb.append(NettyServer.allChannels.size());
        sb.append(" users are online\n");

        for (Channel ch: NettyServer.allChannels){
            final ByteBuf buf = ctx.alloc().buffer(30);
            buf.writeBytes(sb.toString().getBytes());
            ch.write(buf);
            ch.flush();
        }

        super.close(ctx, promise);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
