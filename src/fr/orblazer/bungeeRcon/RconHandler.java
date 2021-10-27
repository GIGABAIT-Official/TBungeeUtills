package fr.orblazer.bungeeRcon;

import bungee.main.Main;
import bungee.main.SaveLogs;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.md_5.bungee.api.ChatColor;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;

public class RconHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final byte FAILURE = -1;
    private static final byte TYPE_RESPONSE = 0;
    private static final byte TYPE_COMMAND = 2;
    private static final byte TYPE_LOGIN = 3;
    
    private static String hostString;

    private final String password;

    private boolean loggedIn = false;

    private final RconServer rconServer;

    private final RconCommandSender commandSender;

    private final List<String> allowedIP;

    public RconHandler(RconServer rconServer) {
        this.rconServer = rconServer;
        this.password = Main.generalConf.getString("Rcon.password");
        this.allowedIP = Main.generalConf.getStringList("Rcon.allowed_ip");
        this.commandSender = new RconCommandSender();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) {
        if (buf.readableBytes() < 8) {
            return;
        }

        int requestId = buf.readIntLE();
        int type = buf.readIntLE();

        byte[] payloadData = new byte[buf.readableBytes() - 2];
        buf.readBytes(payloadData);
        String payload = new String(payloadData, StandardCharsets.UTF_8);

        buf.readBytes(2);

        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        hostString = socketAddress.getHostString();
        
        if(Main.generalConf.getBoolean("Rcon.ip_allowed")) 
        {
            if(!allowedIP.contains(hostString)) {
                loggedIn = false;
                sendResponse(ctx, FAILURE, TYPE_COMMAND, "");
                rconServer.getServer().getLogger().log(Level.INFO, Main.lang.getFild("rcon_deny_ip", null) + " [{0}]", socketAddress);
                return;
            }
        	
        }


        if (type == TYPE_LOGIN) {
            handleLogin(ctx, payload, requestId);
        } else if (type == TYPE_COMMAND) {
            handleCommand(ctx, payload, requestId);
        } else {
            sendLargeResponse(ctx, requestId, "Unknown request " + Integer.toHexString(type));
        }
    }

    private void handleLogin(ChannelHandlerContext ctx, String payload, int requestId) {
        if (password.equals("") || password.equals(payload)) {
            loggedIn = true;

            sendResponse(ctx, requestId, TYPE_COMMAND, "");
            rconServer.getServer().getLogger().log(Level.INFO, Main.lang.getFild("rcon_connection_f", null) + " [{0}]", ctx.channel().remoteAddress());
        } else {
            loggedIn = false;
            sendResponse(ctx, FAILURE, TYPE_COMMAND, "");
        }
    }

    private void handleCommand(ChannelHandlerContext ctx, String payload, int requestId) {
        if (!loggedIn) {
            sendResponse(ctx, FAILURE, TYPE_COMMAND, "");
            return;
        }

        if (rconServer.getServer().getPluginManager().dispatchCommand(commandSender, payload)) {
            String message = commandSender.flush();

            if (!Main.generalConf.getBoolean("Rcon.colored")) {
                message = ChatColor.stripColor(message);
            }

            sendLargeResponse(ctx, requestId, message);
            new SaveLogs("rcon_log", " Rcon connect to: " + hostString + " || Command: " + payload + System.lineSeparator() +"Response: "+ System.lineSeparator() + message);
        } else {
            String message = Main.lang.getFild("rcon_no_command", null);

            if (!Main.generalConf.getBoolean("Rcon.colored")) {
                message = ChatColor.stripColor(message);
            }
            new SaveLogs("rcon_log", " Rcon connect to: " + hostString + " || Command: " + payload + System.lineSeparator() +"Response: "+ System.lineSeparator() + message);
            sendLargeResponse(ctx, requestId, String.format(Main.lang.getFild("rcon_error_executing", null) + " %s (%s)", payload, message));
        }
    }

    private void sendResponse(ChannelHandlerContext ctx, int requestId, int type, String payload) {
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeIntLE(requestId);
        buf.writeIntLE(type);
        buf.writeBytes(payload.getBytes(StandardCharsets.UTF_8));
        buf.writeByte(0);
        buf.writeByte(0);
        ctx.write(buf);
    }

    private void sendLargeResponse(ChannelHandlerContext ctx, int requestId, String payload) {
        if (payload.length() == 0) {
            sendResponse(ctx, requestId, TYPE_RESPONSE, "");
            return;
        }

        int start = 0;
        while (start < payload.length()) {
            int length = payload.length() - start;
            int truncated = Math.min(length, 2048);

            sendResponse(ctx, requestId, TYPE_RESPONSE, payload.substring(start, truncated));
            start += truncated;
        }
    }
}