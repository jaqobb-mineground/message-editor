package dev.jaqobb.message_editor.message;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import dev.jaqobb.message_editor.util.MessageUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public enum MessagePlace {
    
    GAME_CHAT("GC", "Game Chat", MinecraftVersion.BOUNTIFUL_UPDATE, Collections.singleton(PacketType.Play.Server.CHAT), (byte) 0, EnumWrappers.ChatType.CHAT) {
        @Override
        public String getMessage(PacketContainer packet) {
            return MessageUtils.retrieveMessage(packet, PacketType.Play.Server.CHAT);
        }
        
        @Override
        public void setMessage(PacketContainer packet, String message, boolean json) {
            MessageUtils.updateMessage(packet, PacketType.Play.Server.CHAT, message, json);
        }
    },
    SYSTEM_CHAT("SC", "System Chat", MinecraftVersion.BOUNTIFUL_UPDATE, new HashSet<>(Arrays.asList(PacketType.Play.Server.CHAT, PacketType.Play.Server.SYSTEM_CHAT)), (byte) 1, EnumWrappers.ChatType.SYSTEM) {
        @Override
        public String getMessage(PacketContainer packet) {
            return MessageUtils.retrieveMessage(packet, packet.getType());
        }
        
        @Override
        public void setMessage(PacketContainer packet, String message, boolean json) {
            MessageUtils.updateMessage(packet, packet.getType(), message, json);
        }
    },
    ACTION_BAR("AB", "Action Bar", MinecraftVersion.BOUNTIFUL_UPDATE, Collections.singleton(PacketType.Play.Server.CHAT), (byte) 2, EnumWrappers.ChatType.GAME_INFO) {
        @Override
        public String getMessage(PacketContainer packet) {
            // <1.19. 1.19+ uses system chat packet for action bar messages.
            if (!MinecraftVersion.WILD_UPDATE.atOrAbove()) {
                return MessageUtils.retrieveMessage(packet, PacketType.Play.Server.CHAT);
            }
            return MessageUtils.retrieveMessage(packet, PacketType.Play.Server.SYSTEM_CHAT);
        }
        
        @Override
        public void setMessage(PacketContainer packet, String message, boolean json) {
            // <1.19. 1.19+ uses system chat packet for action bar messages.
            if (!MinecraftVersion.WILD_UPDATE.atOrAbove()) {
                MessageUtils.updateMessage(packet, PacketType.Play.Server.CHAT, message, json);
                return;
            }
            MessageUtils.updateMessage(packet, PacketType.Play.Server.SYSTEM_CHAT, message, json);
        }
    },
    KICK("K", "Kick", MinecraftVersion.BOUNTIFUL_UPDATE, Collections.singleton(PacketType.Play.Server.KICK_DISCONNECT)) {
        @Override
        public String getMessage(PacketContainer packet) {
            return packet.getChatComponents().readSafely(0).getJson();
        }
        
        @Override
        public void setMessage(PacketContainer packet, String message, boolean json) {
            if (!json) {
                packet.getChatComponents().write(0, WrappedChatComponent.fromJson(MessageUtils.toJson(MessageUtils.toBaseComponents(message), true)));
                return;
            }
            packet.getChatComponents().write(0, WrappedChatComponent.fromJson(message));
        }
    },
    DISCONNECT("D", "Disconnect", MinecraftVersion.BOUNTIFUL_UPDATE, Collections.singleton(PacketType.Login.Server.DISCONNECT)) {
        @Override
        public String getMessage(PacketContainer packet) {
            return packet.getChatComponents().readSafely(0).getJson();
        }
        
        @Override
        public void setMessage(PacketContainer packet, String message, boolean json) {
            if (!json) {
                packet.getChatComponents().write(0, WrappedChatComponent.fromJson(MessageUtils.toJson(MessageUtils.toBaseComponents(message), true)));
                return;
            }
            packet.getChatComponents().write(0, WrappedChatComponent.fromJson(message));
        }
    },
    BOSS_BAR("BB", "Boss Bar", MinecraftVersion.COMBAT_UPDATE, Collections.singleton(PacketType.Play.Server.BOSS)) {
        @Override
        public String getMessage(PacketContainer packet) {
            if (!MinecraftVersion.CAVES_CLIFFS_1.atOrAbove()) {
                return packet.getChatComponents().readSafely(0).getJson();
            }
            return packet.getStructures().readSafely(1).getChatComponents().readSafely(0).getJson();
        }
        
        @Override
        public void setMessage(PacketContainer packet, String message, boolean json) {
            if (!MinecraftVersion.CAVES_CLIFFS_1.atOrAbove()) {
                if (!json) {
                    packet.getChatComponents().write(0, WrappedChatComponent.fromJson(MessageUtils.toJson(MessageUtils.toBaseComponents(message), true)));
                    return;
                }
                packet.getChatComponents().write(0, WrappedChatComponent.fromJson(message));
                return;
            }
            if (!json) {
                packet.getStructures().readSafely(1).getChatComponents().write(0, WrappedChatComponent.fromJson(MessageUtils.toJson(MessageUtils.toBaseComponents(message), true)));
                return;
            }
            packet.getStructures().readSafely(1).getChatComponents().write(0, WrappedChatComponent.fromJson(message));
        }
    },
    SCOREBOARD_TITLE("ST", "Scoreboard Title", MinecraftVersion.BOUNTIFUL_UPDATE, Collections.singleton(PacketType.Play.Server.SCOREBOARD_OBJECTIVE)) {
        @Override
        public String getMessage(PacketContainer packet) {
            if (packet.getStrings().size() == 2) {
                return packet.getStrings().readSafely(1);
            }
            return packet.getChatComponents().readSafely(0).getJson();
        }
        
        @Override
        public void setMessage(PacketContainer packet, String message, boolean json) {
            if (packet.getStrings().size() == 2) {
                packet.getStrings().write(1, message);
                return;
            }
            if (!json) {
                packet.getChatComponents().write(0, WrappedChatComponent.fromJson(MessageUtils.toJson(MessageUtils.toBaseComponents(message), true)));
                return;
            }
            packet.getChatComponents().write(0, WrappedChatComponent.fromJson(message));
        }
    },
    SCOREBOARD_ENTRY("SE", "Scoreboard Entry", MinecraftVersion.BOUNTIFUL_UPDATE, Collections.singleton(PacketType.Play.Server.SCOREBOARD_SCORE)) {
        @Override
        public String getMessage(PacketContainer packet) {
            return packet.getStrings().readSafely(0);
        }
        
        @Override
        public void setMessage(PacketContainer packet, String message, boolean json) {
            if (!json) {
                packet.getStrings().write(0, message);
                return;
            }
            packet.getStrings().write(0, BaseComponent.toLegacyText(ComponentSerializer.parse(message)));
        }
    },
    INVENTORY_TITLE("IT", "Inventory Title", MinecraftVersion.BOUNTIFUL_UPDATE, Collections.singleton(PacketType.Play.Server.OPEN_WINDOW)) {
        @Override
        public String getMessage(PacketContainer packet) {
            return packet.getChatComponents().readSafely(0).getJson();
        }
        
        @Override
        public void setMessage(PacketContainer packet, String message, boolean json) {
            if (!json) {
                packet.getChatComponents().write(0, WrappedChatComponent.fromJson(MessageUtils.toJson(MessageUtils.toBaseComponents(message), true)));
                return;
            }
            packet.getChatComponents().write(0, WrappedChatComponent.fromJson(message));
        }
    },
    INVENTORY_ITEM_NAME("ITN", "Inventory Item Name", MinecraftVersion.BOUNTIFUL_UPDATE, Collections.singleton(PacketType.Play.Server.WINDOW_ITEMS)) {
        @Override
        public String getMessage(PacketContainer packet) {
            // Items are an exception and do not use this.
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setMessage(PacketContainer packet, String message, boolean json) {
            // Items are an exception and do not use this.
            throw new UnsupportedOperationException();
        }
    },
    INVENTORY_ITEM_LORE("ITL", "Inventory Item Lore", MinecraftVersion.BOUNTIFUL_UPDATE, Collections.singleton(PacketType.Play.Server.WINDOW_ITEMS)) {
        // Items are an exception and do not use this.
        @Override
        public String getMessage(PacketContainer packet) {
            throw new UnsupportedOperationException();
        }
        
        // Items are an exception and do not use this.
        @Override
        public void setMessage(PacketContainer packet, String message, boolean json) {
            throw new UnsupportedOperationException();
        }
    },
    ENTITY_NAME("EN", "Entity Name", MinecraftVersion.BOUNTIFUL_UPDATE, Collections.singleton(PacketType.Play.Server.ENTITY_METADATA)) {
        @Override
        public String getMessage(PacketContainer packet) {
            if (!MinecraftVersion.FEATURE_PREVIEW_UPDATE.atOrAbove()) {
                List<WrappedWatchableObject> objects = packet.getWatchableCollectionModifier().readSafely(0);
                if (objects == null) {
                    return null;
                }
                for (WrappedWatchableObject object : objects) {
                    if (object.getIndex() != 2) {
                        continue;
                    }
                    Object value = object.getValue();
                    if (!(value instanceof Optional)) {
                        continue;
                    }
                    Optional<?> name = (Optional<?>) value;
                    if (!name.isPresent()) {
                        return null;
                    }
                    Object nameValue = name.get();
                    if (!(nameValue instanceof WrappedChatComponent)) {
                        return WrappedChatComponent.fromHandle(nameValue).getJson();
                    }
                    return ((WrappedChatComponent) nameValue).getJson();
                }
                return null;
            }
            List<WrappedDataValue> dataValues = packet.getDataValueCollectionModifier().readSafely(0);
            if (dataValues == null) {
                return null;
            }
            for (WrappedDataValue dataValue : dataValues) {
                if (dataValue.getIndex() != 2) {
                    continue;
                }
                Object value = dataValue.getValue();
                if (!(value instanceof Optional)) {
                    continue;
                }
                Optional<?> name = (Optional<?>) value;
                if (!name.isPresent()) {
                    return null;
                }
                Object nameValue = name.get();
                if (!(nameValue instanceof WrappedChatComponent)) {
                    return WrappedChatComponent.fromHandle(nameValue).getJson();
                }
                return ((WrappedChatComponent) nameValue).getJson();
            }
            return null;
        }
        
        @Override
        public void setMessage(PacketContainer packet, String message, boolean json) {
            if (!MinecraftVersion.FEATURE_PREVIEW_UPDATE.atOrAbove()) {
                List<WrappedWatchableObject> objects = packet.getWatchableCollectionModifier().readSafely(0);
                if (objects == null) {
                    return;
                }
                for (WrappedWatchableObject object : objects) {
                    if (object.getIndex() != 2) {
                        continue;
                    }
                    if (!json) {
                        object.setValue(Optional.of(WrappedChatComponent.fromJson(MessageUtils.toJson(MessageUtils.toBaseComponents(message), true)).getHandle()));
                        return;
                    }
                    object.setValue(Optional.of(WrappedChatComponent.fromJson(message).getHandle()));
                    return;
                }
                return;
            }
            List<WrappedDataValue> dataValues = packet.getDataValueCollectionModifier().readSafely(0);
            if (dataValues == null) {
                return;
            }
            for (WrappedDataValue dataValue : dataValues) {
                if (dataValue.getIndex() != 2) {
                    continue;
                }
                if (!json) {
                    dataValue.setValue(Optional.of(WrappedChatComponent.fromJson(MessageUtils.toJson(MessageUtils.toBaseComponents(message), true))));
                    return;
                }
                dataValue.setValue(Optional.of(WrappedChatComponent.fromJson(message)));
                return;
            }
        }
    };
    
    public static final MessagePlace[] VALUES = values();
    
    private final String id;
    private final String friendlyName;
    private final MinecraftVersion minimumRequiredMinecraftVersion;
    private final Set<PacketType> packetTypes;
    private final Byte chatType;
    private final EnumWrappers.ChatType chatTypeEnum;
    private final boolean supported;
    private boolean analyzing;
    
    MessagePlace(String id, String friendlyName, MinecraftVersion minimumRequiredMinecraftVersion, Set<PacketType> packetTypes) {
        this(id, friendlyName, minimumRequiredMinecraftVersion, packetTypes, null, null);
    }
    
    MessagePlace(String id, String friendlyName, MinecraftVersion minimumRequiredMinecraftVersion, Set<PacketType> packetTypes, Byte chatType, EnumWrappers.ChatType chatTypeEnum) {
        this.id = id;
        this.friendlyName = friendlyName;
        this.minimumRequiredMinecraftVersion = minimumRequiredMinecraftVersion;
        this.packetTypes = packetTypes;
        this.chatType = chatType;
        this.chatTypeEnum = chatTypeEnum;
        this.supported = this.minimumRequiredMinecraftVersion.atOrAbove();
        this.analyzing = false;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getFriendlyName() {
        return this.friendlyName;
    }
    
    public MinecraftVersion getMinimumRequiredMinecraftVersion() {
        return this.minimumRequiredMinecraftVersion;
    }
    
    public Set<PacketType> getPacketTypes() {
        return Collections.unmodifiableSet(this.packetTypes);
    }
    
    public Byte getChatType() {
        return this.chatType;
    }
    
    public EnumWrappers.ChatType getChatTypeEnum() {
        return this.chatTypeEnum;
    }
    
    public boolean isSupported() {
        return this.supported;
    }
    
    public boolean isAnalyzing() {
        return this.analyzing;
    }
    
    public void setAnalyzing(boolean analyzing) {
        this.analyzing = analyzing;
    }
    
    public abstract String getMessage(PacketContainer packet);
    
    public abstract void setMessage(PacketContainer packet, String message, boolean json);
    
    public static MessagePlace fromName(String name) {
        return Arrays.stream(VALUES)
            .filter(place -> place.name().equalsIgnoreCase(name) || place.friendlyName.equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    public static MessagePlace fromPacket(PacketContainer packet) {
        if (packet.getType() == PacketType.Play.Server.SYSTEM_CHAT) {
            // Hacky way to ensure action bar messages on 1.19+ return correct message place.
            return fromPacketType(PacketType.Play.Server.CHAT, (byte) (packet.getBooleans().readSafely(0) ? 2 : 1));
        }
        if (packet.getType() != PacketType.Play.Server.CHAT) {
            return fromPacketType(packet.getType());
        }
        if (packet.getBytes().size() == 1) {
            return fromPacketType(packet.getType(), packet.getBytes().readSafely(0));
        }
        return fromPacketType(packet.getType(), packet.getChatTypes().readSafely(0));
    }
    
    public static MessagePlace fromPacketType(PacketType packetType) {
        return Arrays.stream(VALUES)
            .filter(place -> place.packetTypes.contains(packetType))
            .findFirst()
            .orElse(null);
    }
    
    public static MessagePlace fromPacketType(PacketType packetType, byte chatType) {
        return Arrays.stream(VALUES)
            .filter(place -> place.packetTypes.contains(packetType))
            .filter(place -> place.chatType != null && place.chatType == chatType)
            .findFirst()
            .orElse(null);
    }
    
    public static MessagePlace fromPacketType(PacketType packetType, EnumWrappers.ChatType chatTypeEnum) {
        return Arrays.stream(VALUES)
            .filter(place -> place.packetTypes.contains(packetType))
            .filter(place -> place.chatTypeEnum != null && place.chatTypeEnum == chatTypeEnum)
            .findFirst()
            .orElse(null);
    }
}
