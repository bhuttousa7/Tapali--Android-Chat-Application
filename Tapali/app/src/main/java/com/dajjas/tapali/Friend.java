package com.dajjas.tapali;

public class Friend  {
  
  private String username = null;
  private String displayName = null;
  private Integer id = null;

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

  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Friend {\n");
    
    sb.append("  username: ").append(username).append("\n");
    sb.append("  displayName: ").append(displayName).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
