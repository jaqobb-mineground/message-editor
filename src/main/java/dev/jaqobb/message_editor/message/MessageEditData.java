package dev.jaqobb.message_editor.message;

import dev.jaqobb.message_editor.util.MessageUtils;

public final class MessageEditData {
    
    private final String id;
    private String fileName;
    private final String originalOldMessage;
    private final boolean originalOldMessageJson;
    private String oldMessage;
    private String oldMessagePattern;
    private boolean oldMessageJson;
    private String oldMessagePatternKey;
    private final MessagePlace oldMessagePlace;
    private final String originalNewMessage;
    private final boolean originalNewMessageJson;
    private String newMessage;
    private boolean newMessageJson;
    private String newMessageCache;
    private String newMessageKey;
    private MessagePlace newMessagePlace;
    private Mode currentMode;
    
    public MessageEditData(MessageData data) {
        this(data.getId(), data.getId(), data.getMessage(), data.isJson(), data.getMessagePlace(), data.getMessage(), data.isJson(), data.getMessagePlace());
    }
    
    public MessageEditData(String id, String fileName, String oldMessage, boolean oldMessageJson, MessagePlace oldMessagePlace, String newMessage, boolean newMessageJson, MessagePlace newMessagePlace) {
        this.id = id;
        this.fileName = fileName;
        this.originalOldMessage = oldMessage;
        this.originalOldMessageJson = oldMessageJson;
        this.oldMessage = oldMessage;
        this.oldMessagePattern = oldMessage.replaceAll(MessageUtils.SPECIAL_REGEX_CHARACTERS, "\\\\$0");
        this.oldMessageJson = oldMessageJson;
        this.oldMessagePatternKey = "";
        this.oldMessagePlace = oldMessagePlace;
        this.originalNewMessage = newMessage;
        this.originalNewMessageJson = newMessageJson;
        this.newMessage = newMessage;
        this.newMessageJson = newMessageJson;
        this.newMessageCache = "";
        this.newMessageKey = "";
        this.newMessagePlace = newMessagePlace;
        this.currentMode = Mode.NONE;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getOriginalOldMessage() {
        return this.originalOldMessage;
    }
    
    public boolean isOriginalOldMessageJson() {
        return this.originalOldMessageJson;
    }
    
    public String getOldMessage() {
        return this.oldMessage;
    }
    
    public void setOldMessage(String oldMessage) {
        this.oldMessage = oldMessage;
    }
    
    public String getOldMessagePattern() {
        return this.oldMessagePattern;
    }
    
    public void setOldMessagePattern(String oldMessagePattern) {
        this.oldMessagePattern = oldMessagePattern;
    }
    
    public boolean isOldMessageJson() {
        return this.oldMessageJson;
    }
    
    public void setOldMessageJson(boolean oldMessageJson) {
        this.oldMessageJson = oldMessageJson;
    }
    
    public String getOldMessagePatternKey() {
        return this.oldMessagePatternKey;
    }
    
    public void setOldMessagePatternKey(String oldMessagePatternKey) {
        this.oldMessagePatternKey = oldMessagePatternKey;
    }
    
    public MessagePlace getOldMessagePlace() {
        return this.oldMessagePlace;
    }
    
    public String getOriginalNewMessage() {
        return this.originalNewMessage;
    }
    
    public boolean isOriginalNewMessageJson() {
        return this.originalNewMessageJson;
    }
    
    public String getNewMessage() {
        return this.newMessage;
    }
    
    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }
    
    public boolean isNewMessageJson() {
        return this.newMessageJson;
    }
    
    public void setNewMessageJson(boolean newMessageJson) {
        this.newMessageJson = newMessageJson;
    }
    
    public String getNewMessageCache() {
        return this.newMessageCache;
    }
    
    public void setNewMessageCache(String newMessageCache) {
        this.newMessageCache = newMessageCache;
    }
    
    public String getNewMessageKey() {
        return this.newMessageKey;
    }
    
    public void setNewMessageKey(String newMessageKey) {
        this.newMessageKey = newMessageKey;
    }
    
    public MessagePlace getNewMessagePlace() {
        return this.newMessagePlace;
    }
    
    public void setNewMessagePlace(MessagePlace newMessagePlace) {
        this.newMessagePlace = newMessagePlace;
    }
    
    public Mode getCurrentMode() {
        return this.currentMode;
    }
    
    public void setCurrentMode(Mode currentMode) {
        this.currentMode = currentMode;
    }
    
    public enum Mode {
        
        NONE(true),
        EDITING_FILE_NAME(false),
        EDITING_OLD_MESSAGE_PATTERN_KEY(false),
        EDITING_OLD_MESSAGE_PATTERN_VALUE(false),
        EDITING_NEW_MESSAGE(false),
        EDITING_NEW_MESSAGE_KEY(false),
        EDITING_NEW_MESSAGE_VALUE(false),
        EDITING_NEW_MESSAGE_PLACE(false);
        
        private final boolean invalidateCache;
        
        Mode(boolean invalidateCache) {
            this.invalidateCache = invalidateCache;
        }
        
        public boolean shouldInvalidateCache() {
            return this.invalidateCache;
        }
    }
}
