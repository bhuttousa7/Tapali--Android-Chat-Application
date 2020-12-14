package com.dajjas.tapali;

public class User {
  
  private Integer userId = null;
  private String username = null;
  private String displayName = null;
  private Boolean enabled = null;

  public Integer getUserId() {
    return userId;
  }
  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public Boolean getEnabled() {
    return enabled;
  }
  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    
    sb.append("  userId: ").append(userId).append("\n");
    sb.append("  username: ").append(username).append("\n");
    sb.append("  displayName: ").append(displayName).append("\n");
    sb.append("  enabled: ").append(enabled).append("\n");
    sb.append("}\n");
    return sb.toString();
  }

}
