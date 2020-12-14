package com.dajjas.tapali;

public class Message  {

  private Integer messageId = null;
  private Integer chatId = null;
  private Integer userId = null;
  private String message = null;
  private String timestamp = null;

  public Integer getMessageId() {
    return messageId;
  }
  public void setMessageId(Integer messageId) {
    this.messageId = messageId;
  }

  public Integer getChatId() {
    return chatId;
  }
  public void setChatId(Integer chatId) {
    this.chatId = chatId;
  }

  public Integer getUserId() {
    return userId;
  }
  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }

  public String getTimestamp() {
    return timestamp;
  }
  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Message {\n");
    
    sb.append("  messageId: ").append(messageId).append("\n");
    sb.append("  chatId: ").append(chatId).append("\n");
    sb.append("  userId: ").append(userId).append("\n");
    sb.append("  message: ").append(message).append("\n");
    sb.append("  timestamp: ").append(timestamp).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
