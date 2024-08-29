package github.andredimaz.plugin.core.utils.basics;

import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableBiMap.Builder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.Vector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class JSONMessage {
    private static final BiMap<ChatColor, String> stylesToNames;
    private final List<JSONMessage.MessagePart> parts = new ArrayList();
    private int centeringStartIndex = -1;

    private JSONMessage(String text) {
        this.parts.add(new JSONMessage.MessagePart(text));
    }

    public static JSONMessage create(String text) {
        return new JSONMessage(text);
    }

    public static JSONMessage create() {
        return create("");
    }

    public static void actionbar(String message, Player... players) {
        JSONMessage.ReflectionHelper.sendPacket(JSONMessage.ReflectionHelper.createActionbarPacket(ChatColor.translateAlternateColorCodes('&', message)), players);
    }

    public JSONMessage.MessagePart last() {
        if (this.parts.size() <= 0) {
            throw new ArrayIndexOutOfBoundsException("No MessageParts exist!");
        } else {
            return (JSONMessage.MessagePart)this.parts.get(this.parts.size() - 1);
        }
    }

    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty("text", "");
        JsonArray array = new JsonArray();
        this.parts.stream().map(JSONMessage.MessagePart::toJSON).forEach(array::add);
        obj.add("extra", array);
        return obj;
    }

    public String toString() {
        return this.toJSON().toString();
    }

    public String toLegacy() {
        StringBuilder output = new StringBuilder();
        this.parts.stream().map(JSONMessage.MessagePart::toLegacy).forEach(output::append);
        return output.toString();
    }

    public void send(Player... players) {
        JSONMessage.ReflectionHelper.sendPacket(JSONMessage.ReflectionHelper.createTextPacket(this.toString()), players);
    }

    public void title(int fadeIn, int stay, int fadeOut, Player... players) {
        JSONMessage.ReflectionHelper.sendPacket(JSONMessage.ReflectionHelper.createTitleTimesPacket(fadeIn, stay, fadeOut), players);
        JSONMessage.ReflectionHelper.sendPacket(JSONMessage.ReflectionHelper.createTitlePacket(this.toString()), players);
    }

    public void subtitle(Player... players) {
        JSONMessage.ReflectionHelper.sendPacket(JSONMessage.ReflectionHelper.createSubtitlePacket(this.toString()), players);
    }

    public void actionbar(Player... players) {
        actionbar(this.toLegacy(), players);
    }

    public JSONMessage color(ChatColor color) {
        if (!color.isColor()) {
            throw new IllegalArgumentException(color.name() + " is not a color.");
        } else {
            this.last().setColor(color);
            return this;
        }
    }

    public JSONMessage color(String color) {
        return this.color(color, ChatColor.WHITE);
    }

    public JSONMessage color(String color, ChatColor def) {
        if (color.startsWith("#") && JSONMessage.ReflectionHelper.MAJOR_VER < 16) {
            return this.color(def);
        } else {
            this.last().setColor(color);
            return this;
        }
    }

    public JSONMessage font(String font) {
        if (JSONMessage.ReflectionHelper.MAJOR_VER < 16) {
            return this;
        } else {
            this.last().setFont(font);
            return this;
        }
    }

    public JSONMessage style(ChatColor style) {
        this.last().addStyle(style);
        return this;
    }

    public JSONMessage runCommand(String command) {
        this.last().setOnClick(JSONMessage.ClickEvent.runCommand(command));
        return this;
    }

    public JSONMessage suggestCommand(String command) {
        this.last().setOnClick(JSONMessage.ClickEvent.suggestCommand(command));
        return this;
    }

    public JSONMessage openURL(String url) {
        this.last().setOnClick(JSONMessage.ClickEvent.openURL(url));
        return this;
    }

    public JSONMessage copyText(String text) {
        this.last().setOnClick(JSONMessage.ClickEvent.copyText(text));
        return this;
    }

    public JSONMessage changePage(int page) {
        this.last().setOnClick(JSONMessage.ClickEvent.changePage(page));
        return this;
    }

    public JSONMessage tooltip(String text) {
        this.last().setOnHover(JSONMessage.HoverEvent.showText(text));
        return this;
    }

    public JSONMessage tooltip(JSONMessage message) {
        this.last().setOnHover(JSONMessage.HoverEvent.showText(message));
        return this;
    }

    public JSONMessage achievement(String id) {
        this.last().setOnHover(JSONMessage.HoverEvent.showAchievement(id));
        return this;
    }

    public JSONMessage then(String text) {
        return this.then(new JSONMessage.MessagePart(text));
    }

    public JSONMessage then(JSONMessage.MessagePart nextPart) {
        this.parts.add(nextPart);
        return this;
    }

    public JSONMessage bar(int length) {
        return this.then(Strings.repeat("-", length)).color(ChatColor.DARK_GRAY).style(ChatColor.STRIKETHROUGH);
    }

    public JSONMessage bar() {
        return this.bar(53);
    }

    public JSONMessage newline() {
        return this.then("\n");
    }

    public JSONMessage beginCenter() {
        this.centeringStartIndex = this.parts.size();
        return this;
    }

    public JSONMessage endCenter() {
        int current = this.centeringStartIndex;

        while(current < this.parts.size()) {
            Vector<JSONMessage.MessagePart> currentLine = new Vector();
            int totalLineLength = 0;

            while(true) {
                JSONMessage.MessagePart part = current < this.parts.size() ? (JSONMessage.MessagePart)this.parts.get(current) : null;
                String raw = part == null ? null : ChatColor.stripColor(part.toLegacy());
                int rawLength = raw == null ? 0 : raw.length();
                if (current >= this.parts.size() || totalLineLength + rawLength >= 53) {
                    int padding = Math.max(0, (53 - totalLineLength) / 2);
                    ((JSONMessage.MessagePart)currentLine.firstElement()).setText(Strings.repeat(" ", padding) + ((JSONMessage.MessagePart)currentLine.firstElement()).getText());
                    ((JSONMessage.MessagePart)currentLine.lastElement()).setText(((JSONMessage.MessagePart)currentLine.lastElement()).getText() + "\n");
                    currentLine.clear();
                    break;
                }

                totalLineLength += rawLength;
                currentLine.add(part);
                ++current;
            }
        }

        JSONMessage.MessagePart last = (JSONMessage.MessagePart)this.parts.get(this.parts.size() - 1);
        last.setText(last.getText().substring(0, last.getText().length() - 1));
        this.centeringStartIndex = -1;
        return this;
    }

    static {
        Builder<ChatColor, String> builder = ImmutableBiMap.builder();
        ChatColor[] var1 = ChatColor.values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ChatColor style = var1[var3];
            if (style.isFormat()) {
                String styleName;
                switch(style) {
                    case MAGIC:
                        styleName = "obfuscated";
                        break;
                    case UNDERLINE:
                        styleName = "underlined";
                        break;
                    default:
                        styleName = style.name().toLowerCase();
                }

                builder.put(style, styleName);
            }
        }

        stylesToNames = builder.build();
    }

    public static class MessagePart {
        private final List<ChatColor> styles = new ArrayList();
        private JSONMessage.MessageEvent onClick;
        private JSONMessage.MessageEvent onHover;
        private String color;
        private ChatColor legacyColor;
        private String font;
        private String text;

        public MessagePart(String text) {
            this.text = text == null ? "null" : text;
        }

        public JsonObject toJSON() {
            Objects.requireNonNull(this.text);
            JsonObject obj = new JsonObject();
            obj.addProperty("text", this.text);
            if (this.color != null && !this.color.isEmpty()) {
                obj.addProperty("color", this.color.toLowerCase());
            }

            Iterator var2 = this.styles.iterator();

            while(var2.hasNext()) {
                ChatColor style = (ChatColor)var2.next();
                obj.addProperty((String)JSONMessage.stylesToNames.get(style), true);
            }

            if (this.onClick != null) {
                obj.add("clickEvent", this.onClick.toJSON());
            }

            if (this.onHover != null) {
                obj.add("hoverEvent", this.onHover.toJSON());
            }

            if (this.font != null) {
                obj.addProperty("font", this.font);
            }

            return obj;
        }

        public String toLegacy() {
            StringBuilder output = new StringBuilder();
            ChatColor legacyColor = this.getColor();
            if (legacyColor != null) {
                output.append(legacyColor);
            }

            this.styles.stream().map(ChatColor::toString).forEach(output::append);
            return output.append(this.text).toString();
        }

        public JSONMessage.MessageEvent getOnClick() {
            return this.onClick;
        }

        public void setOnClick(JSONMessage.MessageEvent onClick) {
            this.onClick = onClick;
        }

        public JSONMessage.MessageEvent getOnHover() {
            return this.onHover;
        }

        public void setOnHover(JSONMessage.MessageEvent onHover) {
            this.onHover = onHover;
        }

        public String getColorValue() {
            return this.color;
        }

        /** @deprecated */
        @Deprecated
        public ChatColor getColor() {
            if (this.legacyColor != null) {
                return this.legacyColor;
            } else if (this.color.startsWith("#") && JSONMessage.ReflectionHelper.MAJOR_VER < 16) {
                throw new IllegalStateException("Custom Hex colors can only be used in Minecraft 1.16 or newer!");
            } else {
                try {
                    return ChatColor.valueOf(this.color.toUpperCase());
                } catch (Exception var2) {
                    return null;
                }
            }
        }

        /** @deprecated */
        @Deprecated
        public void setColor(ChatColor color) {
            this.setColor(color == null ? null : color.name().toLowerCase());
            this.setLegacyColor(color);
        }

        /** @deprecated */
        @Deprecated
        public void setLegacyColor(ChatColor color) {
            this.legacyColor = color;
        }

        public void setColor(String color) {
            if (color != null && color.isEmpty()) {
                throw new IllegalArgumentException("Color cannot be null!");
            } else {
                this.color = color;
            }
        }

        public List<ChatColor> getStyles() {
            return this.styles;
        }

        public void addStyle(ChatColor style) {
            if (style == null) {
                throw new IllegalArgumentException("Style cannot be null!");
            } else if (!style.isFormat()) {
                throw new IllegalArgumentException(style.name() + " is not a style!");
            } else {
                this.styles.add(style);
            }
        }

        public String getFont() {
            return this.font;
        }

        public void setFont(String font) {
            this.font = font;
        }

        public String getText() {
            return this.text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    private static class ReflectionHelper {
        private static final String version;
        private static Constructor<?> chatComponentText;
        private static Class<?> packetPlayOutChat;
        private static Field packetPlayOutChatComponent;
        private static Field packetPlayOutChatMessageType;
        private static Field packetPlayOutChatUuid;
        private static Object enumChatMessageTypeMessage;
        private static Object enumChatMessageTypeActionbar;
        private static Constructor<?> titlePacketConstructor;
        private static Constructor<?> titleTimesPacketConstructor;
        private static Object enumActionTitle;
        private static Object enumActionSubtitle;
        private static Field connection;
        private static MethodHandle GET_HANDLE;
        private static MethodHandle SEND_PACKET;
        private static MethodHandle STRING_TO_CHAT;
        private static boolean SETUP;
        private static int MAJOR_VER = -1;

        private ReflectionHelper() {
        }

        static void sendPacket(Object packet, Player... players) {
            assertIsSetup();
            if (packet != null) {
                Player[] var2 = players;
                int var3 = players.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    Player player = var2[var4];

                    try {
                        SEND_PACKET.bindTo(connection.get(GET_HANDLE.bindTo(player).invoke())).invoke(packet);
                    } catch (Throwable var7) {
                        System.err.println("Failed to send packet");
                        var7.printStackTrace();
                    }
                }

            }
        }

        static Object createActionbarPacket(String message) {
            assertIsSetup();
            Object packet = createTextPacket(message);
            setType(packet, (byte)2);
            return packet;
        }

        static Object createTextPacket(String message) {
            assertIsSetup();

            try {
                Object packet = packetPlayOutChat.newInstance();
                setFieldValue(packetPlayOutChatComponent, packet, fromJson(message));
                setFieldValue(packetPlayOutChatUuid, packet, UUID.randomUUID());
                setType(packet, (byte)1);
                return packet;
            } catch (Exception var2) {
                var2.printStackTrace();
                return null;
            }
        }

        static Object createTitlePacket(String message) {
            assertIsSetup();

            try {
                return titlePacketConstructor.newInstance(enumActionTitle, fromJson(message));
            } catch (Exception var2) {
                var2.printStackTrace();
                return null;
            }
        }

        static Object createTitleTimesPacket(int fadeIn, int stay, int fadeOut) {
            assertIsSetup();

            try {
                return titleTimesPacketConstructor.newInstance(fadeIn, stay, fadeOut);
            } catch (Exception var4) {
                var4.printStackTrace();
                return null;
            }
        }

        static Object createSubtitlePacket(String message) {
            assertIsSetup();

            try {
                return titlePacketConstructor.newInstance(enumActionSubtitle, fromJson(message));
            } catch (Exception var2) {
                var2.printStackTrace();
                return null;
            }
        }

        private static void setType(Object chatPacket, byte type) {
            assertIsSetup();
            if (MAJOR_VER < 12) {
                setFieldValue(packetPlayOutChatMessageType, chatPacket, type);
            } else {
                switch(type) {
                    case 1:
                        setFieldValue(packetPlayOutChatMessageType, chatPacket, enumChatMessageTypeMessage);
                        break;
                    case 2:
                        setFieldValue(packetPlayOutChatMessageType, chatPacket, enumChatMessageTypeActionbar);
                        break;
                    default:
                        throw new IllegalArgumentException("type must be 1 or 2");
                }

            }
        }

        static Object componentText(String message) {
            assertIsSetup();

            try {
                return chatComponentText.newInstance(message);
            } catch (Exception var2) {
                var2.printStackTrace();
                return null;
            }
        }

        static Object fromJson(String json) {
            assertIsSetup();
            if (!json.trim().startsWith("{")) {
                return componentText(json);
            } else {
                try {
                    return STRING_TO_CHAT.invoke(json);
                } catch (Throwable var2) {
                    var2.printStackTrace();
                    return null;
                }
            }
        }

        private static void assertIsSetup() {
            if (!SETUP) {
                throw new IllegalStateException("JSONMessage.ReflectionHelper is not set up yet!");
            }
        }

        private static Class<?> getClass(String path) throws ClassNotFoundException {
            return Class.forName(path.replace("{nms}", "net.minecraft.server." + version).replace("{obc}", "org.bukkit.craftbukkit." + version));
        }

        private static void setFieldValue(Field field, Object instance, Object value) {
            if (field != null) {
                try {
                    field.set(instance, value);
                } catch (IllegalAccessException var4) {
                    var4.printStackTrace();
                }

            }
        }

        private static Field getField(Class<?> classObject, String fieldName) {
            try {
                Field field = classObject.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException var3) {
                var3.printStackTrace();
                return null;
            }
        }

        private static int getVersion() {
            return MAJOR_VER;
        }

        static {
            String[] split = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
            version = split[split.length - 1];

            try {
                MAJOR_VER = Integer.parseInt(version.split("_")[1]);
                Class<?> craftPlayer = getClass("{obc}.entity.CraftPlayer");
                Method getHandle = craftPlayer.getMethod("getHandle");
                connection = getHandle.getReturnType().getField("playerConnection");
                Method sendPacket = connection.getType().getMethod("sendPacket", getClass("{nms}.Packet"));
                chatComponentText = getClass("{nms}.ChatComponentText").getConstructor(String.class);
                Class<?> iChatBaseComponent = getClass("{nms}.IChatBaseComponent");
                Method stringToChat;
                if (MAJOR_VER < 8) {
                    stringToChat = getClass("{nms}.ChatSerializer").getMethod("a", String.class);
                } else {
                    stringToChat = getClass("{nms}.IChatBaseComponent$ChatSerializer").getMethod("a", String.class);
                }

                GET_HANDLE = MethodHandles.lookup().unreflect(getHandle);
                SEND_PACKET = MethodHandles.lookup().unreflect(sendPacket);
                STRING_TO_CHAT = MethodHandles.lookup().unreflect(stringToChat);
                packetPlayOutChat = getClass("{nms}.PacketPlayOutChat");
                packetPlayOutChatComponent = getField(packetPlayOutChat, "a");
                packetPlayOutChatMessageType = getField(packetPlayOutChat, "b");
                packetPlayOutChatUuid = MAJOR_VER >= 16 ? getField(packetPlayOutChat, "c") : null;
                Class<?> packetPlayOutTitle = getClass("{nms}.PacketPlayOutTitle");
                Class<?> titleAction = getClass("{nms}.PacketPlayOutTitle$EnumTitleAction");
                titlePacketConstructor = packetPlayOutTitle.getConstructor(titleAction, iChatBaseComponent);
                titleTimesPacketConstructor = packetPlayOutTitle.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
                enumActionTitle = titleAction.getField("TITLE").get((Object)null);
                enumActionSubtitle = titleAction.getField("SUBTITLE").get((Object)null);
                if (MAJOR_VER >= 12) {
                    Method getChatMessageType = getClass("{nms}.ChatMessageType").getMethod("a", Byte.TYPE);
                    enumChatMessageTypeMessage = getChatMessageType.invoke((Object)null, 1);
                    enumChatMessageTypeActionbar = getChatMessageType.invoke((Object)null, 2);
                }

                SETUP = true;
            } catch (Exception var9) {
                var9.printStackTrace();
                SETUP = false;
            }

        }
    }

    public static class HoverEvent {
        public HoverEvent() {
        }

        public static JSONMessage.MessageEvent showText(String text) {
            return new JSONMessage.MessageEvent("show_text", text);
        }

        public static JSONMessage.MessageEvent showText(JSONMessage message) {
            JsonArray arr = new JsonArray();
            arr.add(new JsonPrimitive(""));
            arr.add(message.toJSON());
            return new JSONMessage.MessageEvent("show_text", arr);
        }

        public static JSONMessage.MessageEvent showAchievement(String id) {
            return new JSONMessage.MessageEvent("show_achievement", id);
        }
    }

    public static class ClickEvent {
        public ClickEvent() {
        }

        public static JSONMessage.MessageEvent runCommand(String command) {
            return new JSONMessage.MessageEvent("run_command", command);
        }

        public static JSONMessage.MessageEvent suggestCommand(String command) {
            return new JSONMessage.MessageEvent("suggest_command", command);
        }

        public static JSONMessage.MessageEvent openURL(String url) {
            return new JSONMessage.MessageEvent("open_url", url);
        }

        public static JSONMessage.MessageEvent changePage(int page) {
            return new JSONMessage.MessageEvent("change_page", page);
        }

        public static JSONMessage.MessageEvent copyText(String text) {
            return JSONMessage.ReflectionHelper.MAJOR_VER < 15 ? suggestCommand(text) : new JSONMessage.MessageEvent("copy_to_clipboard", text);
        }
    }

    public static class MessageEvent {
        private String action;
        private Object value;

        public MessageEvent(String action, Object value) {
            this.action = action;
            this.value = value;
        }

        public JsonObject toJSON() {
            JsonObject obj = new JsonObject();
            obj.addProperty("action", this.action);
            String valueType = JSONMessage.ReflectionHelper.MAJOR_VER >= 16 && this.action.startsWith("show_") ? "contents" : "value";
            if (this.value instanceof JsonElement) {
                obj.add(valueType, (JsonElement)this.value);
            } else {
                obj.addProperty(valueType, this.value.toString());
            }

            return obj;
        }

        public String getAction() {
            return this.action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Object getValue() {
            return this.value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
