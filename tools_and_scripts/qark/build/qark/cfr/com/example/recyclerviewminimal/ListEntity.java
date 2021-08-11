/*
 * Decompiled with CFR 0_124.
 */
package com.example.recyclerviewminimal;

public class ListEntity {
    private String id;
    private String title;

    public ListEntity() {
    }

    public ListEntity(String string2, String string3) {
        this.title = string2;
        this.id = string3;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setId(String string2) {
        this.id = string2;
    }

    public void setTitle(String string2) {
        this.title = string2;
    }
}

