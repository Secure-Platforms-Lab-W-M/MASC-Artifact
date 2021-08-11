package com.example.recyclerviewminimal;

public class ListEntity {
   // $FF: renamed from: id java.lang.String
   private String field_11;
   private String title;

   public ListEntity() {
   }

   public ListEntity(String var1, String var2) {
      this.title = var1;
      this.field_11 = var2;
   }

   public String getId() {
      return this.field_11;
   }

   public String getTitle() {
      return this.title;
   }

   public void setId(String var1) {
      this.field_11 = var1;
   }

   public void setTitle(String var1) {
      this.title = var1;
   }
}
