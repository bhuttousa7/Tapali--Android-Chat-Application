package com.dajjas.tapali;

import java.util.List;

public class Chat  {

    private Integer id = null;
    private String chatTitle = null;
    private List<Integer> users = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChatTitle() {
        return chatTitle;
    }
    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }

    public List<Integer> getUsers() {
        return users;
    }
    public void setUsers(List<Integer> users) {
        this.users = users;
    }

    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class Chat {\n");

        sb.append("  id: ").append(id).append("\n");
        sb.append("  chatTitle: ").append(chatTitle).append("\n");
        sb.append("  users: ").append(users).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
