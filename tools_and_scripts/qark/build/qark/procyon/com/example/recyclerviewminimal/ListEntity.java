// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.example.recyclerviewminimal;

public class ListEntity
{
    private String id;
    private String title;
    
    public ListEntity() {
    }
    
    public ListEntity(final String title, final String id) {
        this.title = title;
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
}
