package com.notification.notyos10.objects;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class MyBackground extends RealmObject{
    private String content;
    private String name;
    @Ignore
    private int sessionId;

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void setName(String name){
        this.name = name;
    }
}
