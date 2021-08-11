package org.apache.http.impl.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieIdentityComparator;

public class BasicCookieStore implements CookieStore, Serializable {
   private static final long serialVersionUID = -7581093305228232025L;
   private final TreeSet cookies = new TreeSet(new CookieIdentityComparator());
   private transient ReadWriteLock lock = new ReentrantReadWriteLock();

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.lock = new ReentrantReadWriteLock();
   }

   public void addCookie(Cookie var1) {
      if (var1 != null) {
         this.lock.writeLock().lock();

         try {
            this.cookies.remove(var1);
            if (!var1.isExpired(new Date())) {
               this.cookies.add(var1);
            }
         } finally {
            this.lock.writeLock().unlock();
         }

      }
   }

   public void addCookies(Cookie[] var1) {
      if (var1 != null) {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            this.addCookie(var1[var2]);
         }
      }

   }

   public void clear() {
      this.lock.writeLock().lock();

      try {
         this.cookies.clear();
      } finally {
         this.lock.writeLock().unlock();
      }

   }

   public boolean clearExpired(Date var1) {
      if (var1 == null) {
         return false;
      } else {
         this.lock.writeLock().lock();
         boolean var2 = false;

         label103: {
            Throwable var10000;
            label102: {
               boolean var10001;
               Iterator var3;
               try {
                  var3 = this.cookies.iterator();
               } catch (Throwable var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label102;
               }

               while(true) {
                  try {
                     do {
                        if (!var3.hasNext()) {
                           break label103;
                        }
                     } while(!((Cookie)var3.next()).isExpired(var1));

                     var3.remove();
                  } catch (Throwable var8) {
                     var10000 = var8;
                     var10001 = false;
                     break;
                  }

                  var2 = true;
               }
            }

            Throwable var10 = var10000;
            this.lock.writeLock().unlock();
            throw var10;
         }

         this.lock.writeLock().unlock();
         return var2;
      }
   }

   public List getCookies() {
      this.lock.readLock().lock();

      ArrayList var1;
      try {
         var1 = new ArrayList(this.cookies);
      } finally {
         this.lock.readLock().unlock();
      }

      return var1;
   }

   public String toString() {
      this.lock.readLock().lock();

      String var1;
      try {
         var1 = this.cookies.toString();
      } finally {
         this.lock.readLock().unlock();
      }

      return var1;
   }
}
