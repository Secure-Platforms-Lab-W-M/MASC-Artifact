package org.jsoup.nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class FormElement extends Element {
   private final Elements elements = new Elements();

   public FormElement(Tag var1, String var2, Attributes var3) {
      super(var1, var2, var3);
   }

   public FormElement addElement(Element var1) {
      this.elements.add(var1);
      return this;
   }

   public Elements elements() {
      return this.elements;
   }

   public List formData() {
      ArrayList var3 = new ArrayList();
      Iterator var4 = this.elements.iterator();

      while(true) {
         while(true) {
            Element var2;
            String var5;
            do {
               do {
                  do {
                     if (!var4.hasNext()) {
                        return var3;
                     }

                     var2 = (Element)var4.next();
                  } while(!var2.tag().isFormSubmittable());
               } while(var2.hasAttr("disabled"));

               var5 = var2.attr("name");
            } while(var5.length() == 0);

            String var6 = var2.attr("type");
            if ("select".equals(var2.tagName())) {
               Elements var8 = var2.select("option[selected]");
               boolean var1 = false;

               for(Iterator var9 = var8.iterator(); var9.hasNext(); var1 = true) {
                  var3.add(HttpConnection.KeyVal.create(var5, ((Element)var9.next()).val()));
               }

               if (!var1) {
                  var2 = var2.select("option").first();
                  if (var2 != null) {
                     var3.add(HttpConnection.KeyVal.create(var5, var2.val()));
                  }
               }
            } else if (!"checkbox".equalsIgnoreCase(var6) && !"radio".equalsIgnoreCase(var6)) {
               var3.add(HttpConnection.KeyVal.create(var5, var2.val()));
            } else if (var2.hasAttr("checked")) {
               String var7;
               if (var2.val().length() > 0) {
                  var7 = var2.val();
               } else {
                  var7 = "on";
               }

               var3.add(HttpConnection.KeyVal.create(var5, var7));
            }
         }
      }
   }

   public Connection submit() {
      String var1;
      if (this.hasAttr("action")) {
         var1 = this.absUrl("action");
      } else {
         var1 = this.baseUri();
      }

      Validate.notEmpty(var1, "Could not determine a form action URL for submit. Ensure you set a base URI when parsing.");
      Connection.Method var2;
      if (this.attr("method").toUpperCase().equals("POST")) {
         var2 = Connection.Method.POST;
      } else {
         var2 = Connection.Method.GET;
      }

      return Jsoup.connect(var1).data((Collection)this.formData()).method(var2);
   }
}
