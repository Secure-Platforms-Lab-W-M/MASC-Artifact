package org.apache.commons.lang3.event;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.Validate;

public class EventListenerSupport implements Serializable {
   private static final long serialVersionUID = 3593265990380473632L;
   private List listeners;
   private transient Object[] prototypeArray;
   private transient Object proxy;

   private EventListenerSupport() {
      this.listeners = new CopyOnWriteArrayList();
   }

   public EventListenerSupport(Class var1) {
      this(var1, Thread.currentThread().getContextClassLoader());
   }

   public EventListenerSupport(Class var1, ClassLoader var2) {
      this();
      Validate.notNull(var1, "Listener interface cannot be null.");
      Validate.notNull(var2, "ClassLoader cannot be null.");
      Validate.isTrue(var1.isInterface(), "Class {0} is not an interface", var1.getName());
      this.initializeTransientFields(var1, var2);
   }

   public static EventListenerSupport create(Class var0) {
      return new EventListenerSupport(var0);
   }

   private void createProxy(Class var1, ClassLoader var2) {
      InvocationHandler var3 = this.createInvocationHandler();
      this.proxy = var1.cast(Proxy.newProxyInstance(var2, new Class[]{var1}, var3));
   }

   private void initializeTransientFields(Class var1, ClassLoader var2) {
      this.prototypeArray = (Object[])Array.newInstance(var1, 0);
      this.createProxy(var1, var2);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      Object[] var2 = (Object[])var1.readObject();
      this.listeners = new CopyOnWriteArrayList(var2);
      this.initializeTransientFields(var2.getClass().getComponentType(), Thread.currentThread().getContextClassLoader());
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      ArrayList var3 = new ArrayList();
      ObjectOutputStream var2 = new ObjectOutputStream(new ByteArrayOutputStream());
      Iterator var4 = this.listeners.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();

         try {
            var2.writeObject(var5);
            var3.add(var5);
         } catch (IOException var6) {
            var2 = new ObjectOutputStream(new ByteArrayOutputStream());
         }
      }

      var1.writeObject(var3.toArray(this.prototypeArray));
   }

   public void addListener(Object var1) {
      this.addListener(var1, true);
   }

   public void addListener(Object var1, boolean var2) {
      Validate.notNull(var1, "Listener object cannot be null.");
      if (var2) {
         this.listeners.add(var1);
      } else {
         if (!this.listeners.contains(var1)) {
            this.listeners.add(var1);
         }

      }
   }

   protected InvocationHandler createInvocationHandler() {
      return new EventListenerSupport.ProxyInvocationHandler();
   }

   public Object fire() {
      return this.proxy;
   }

   int getListenerCount() {
      return this.listeners.size();
   }

   public Object[] getListeners() {
      return this.listeners.toArray(this.prototypeArray);
   }

   public void removeListener(Object var1) {
      Validate.notNull(var1, "Listener object cannot be null.");
      this.listeners.remove(var1);
   }

   protected class ProxyInvocationHandler implements InvocationHandler {
      public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
         Iterator var4 = EventListenerSupport.this.listeners.iterator();

         while(var4.hasNext()) {
            var2.invoke(var4.next(), var3);
         }

         return null;
      }
   }
}
