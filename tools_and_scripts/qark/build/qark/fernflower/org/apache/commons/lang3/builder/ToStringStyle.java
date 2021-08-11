package org.apache.commons.lang3.builder;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;

public abstract class ToStringStyle implements Serializable {
   public static final ToStringStyle DEFAULT_STYLE = new ToStringStyle.DefaultToStringStyle();
   public static final ToStringStyle JSON_STYLE = new ToStringStyle.JsonToStringStyle();
   public static final ToStringStyle MULTI_LINE_STYLE = new ToStringStyle.MultiLineToStringStyle();
   public static final ToStringStyle NO_CLASS_NAME_STYLE = new ToStringStyle.NoClassNameToStringStyle();
   public static final ToStringStyle NO_FIELD_NAMES_STYLE = new ToStringStyle.NoFieldNameToStringStyle();
   private static final ThreadLocal REGISTRY = new ThreadLocal();
   public static final ToStringStyle SHORT_PREFIX_STYLE = new ToStringStyle.ShortPrefixToStringStyle();
   public static final ToStringStyle SIMPLE_STYLE = new ToStringStyle.SimpleToStringStyle();
   private static final long serialVersionUID = -2587890625525655916L;
   private boolean arrayContentDetail = true;
   private String arrayEnd = "}";
   private String arraySeparator = ",";
   private String arrayStart = "{";
   private String contentEnd = "]";
   private String contentStart = "[";
   private boolean defaultFullDetail = true;
   private String fieldNameValueSeparator = "=";
   private String fieldSeparator = ",";
   private boolean fieldSeparatorAtEnd = false;
   private boolean fieldSeparatorAtStart = false;
   private String nullText = "<null>";
   private String sizeEndText = ">";
   private String sizeStartText = "<size=";
   private String summaryObjectEndText = ">";
   private String summaryObjectStartText = "<";
   private boolean useClassName = true;
   private boolean useFieldNames = true;
   private boolean useIdentityHashCode = true;
   private boolean useShortClassName = false;

   protected ToStringStyle() {
   }

   static Map getRegistry() {
      return (Map)REGISTRY.get();
   }

   static boolean isRegistered(Object var0) {
      Map var1 = getRegistry();
      return var1 != null && var1.containsKey(var0);
   }

   static void register(Object var0) {
      if (var0 != null) {
         if (getRegistry() == null) {
            REGISTRY.set(new WeakHashMap());
         }

         getRegistry().put(var0, (Object)null);
      }

   }

   static void unregister(Object var0) {
      if (var0 != null) {
         Map var1 = getRegistry();
         if (var1 != null) {
            var1.remove(var0);
            if (var1.isEmpty()) {
               REGISTRY.remove();
            }
         }
      }

   }

   public void append(StringBuffer var1, String var2, byte var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, char var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, double var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, float var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, int var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, long var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, Object var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else {
         this.appendInternal(var1, var2, var3, this.isFullDetail(var4));
      }

      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, short var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, boolean var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, byte[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (this.isFullDetail(var4)) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, char[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (this.isFullDetail(var4)) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, double[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (this.isFullDetail(var4)) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, float[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (this.isFullDetail(var4)) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, int[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (this.isFullDetail(var4)) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, long[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (this.isFullDetail(var4)) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, Object[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (this.isFullDetail(var4)) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, short[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (this.isFullDetail(var4)) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   public void append(StringBuffer var1, String var2, boolean[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (this.isFullDetail(var4)) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   protected void appendClassName(StringBuffer var1, Object var2) {
      if (this.useClassName && var2 != null) {
         register(var2);
         if (this.useShortClassName) {
            var1.append(this.getShortClassName(var2.getClass()));
            return;
         }

         var1.append(var2.getClass().getName());
      }

   }

   protected void appendContentEnd(StringBuffer var1) {
      var1.append(this.contentEnd);
   }

   protected void appendContentStart(StringBuffer var1) {
      var1.append(this.contentStart);
   }

   protected void appendCyclicObject(StringBuffer var1, String var2, Object var3) {
      ObjectUtils.identityToString(var1, var3);
   }

   protected void appendDetail(StringBuffer var1, String var2, byte var3) {
      var1.append(var3);
   }

   protected void appendDetail(StringBuffer var1, String var2, char var3) {
      var1.append(var3);
   }

   protected void appendDetail(StringBuffer var1, String var2, double var3) {
      var1.append(var3);
   }

   protected void appendDetail(StringBuffer var1, String var2, float var3) {
      var1.append(var3);
   }

   protected void appendDetail(StringBuffer var1, String var2, int var3) {
      var1.append(var3);
   }

   protected void appendDetail(StringBuffer var1, String var2, long var3) {
      var1.append(var3);
   }

   protected void appendDetail(StringBuffer var1, String var2, Object var3) {
      var1.append(var3);
   }

   protected void appendDetail(StringBuffer var1, String var2, Collection var3) {
      var1.append(var3);
   }

   protected void appendDetail(StringBuffer var1, String var2, Map var3) {
      var1.append(var3);
   }

   protected void appendDetail(StringBuffer var1, String var2, short var3) {
      var1.append(var3);
   }

   protected void appendDetail(StringBuffer var1, String var2, boolean var3) {
      var1.append(var3);
   }

   protected void appendDetail(StringBuffer var1, String var2, byte[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   protected void appendDetail(StringBuffer var1, String var2, char[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   protected void appendDetail(StringBuffer var1, String var2, double[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   protected void appendDetail(StringBuffer var1, String var2, float[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   protected void appendDetail(StringBuffer var1, String var2, int[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   protected void appendDetail(StringBuffer var1, String var2, long[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   protected void appendDetail(StringBuffer var1, String var2, Object[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         Object var5 = var3[var4];
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         if (var5 == null) {
            this.appendNullText(var1, var2);
         } else {
            this.appendInternal(var1, var2, var5, this.arrayContentDetail);
         }
      }

      var1.append(this.arrayEnd);
   }

   protected void appendDetail(StringBuffer var1, String var2, short[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   protected void appendDetail(StringBuffer var1, String var2, boolean[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   public void appendEnd(StringBuffer var1, Object var2) {
      if (!this.fieldSeparatorAtEnd) {
         this.removeLastFieldSeparator(var1);
      }

      this.appendContentEnd(var1);
      unregister(var2);
   }

   protected void appendFieldEnd(StringBuffer var1, String var2) {
      this.appendFieldSeparator(var1);
   }

   protected void appendFieldSeparator(StringBuffer var1) {
      var1.append(this.fieldSeparator);
   }

   protected void appendFieldStart(StringBuffer var1, String var2) {
      if (this.useFieldNames && var2 != null) {
         var1.append(var2);
         var1.append(this.fieldNameValueSeparator);
      }

   }

   protected void appendIdentityHashCode(StringBuffer var1, Object var2) {
      if (this.isUseIdentityHashCode() && var2 != null) {
         register(var2);
         var1.append('@');
         var1.append(Integer.toHexString(System.identityHashCode(var2)));
      }

   }

   protected void appendInternal(StringBuffer var1, String var2, Object var3, boolean var4) {
      if (isRegistered(var3) && !(var3 instanceof Number) && !(var3 instanceof Boolean) && !(var3 instanceof Character)) {
         this.appendCyclicObject(var1, var2, var3);
      } else {
         register(var3);

         label8495: {
            Throwable var10000;
            label8494: {
               boolean var10001;
               label8501: {
                  try {
                     if (var3 instanceof Collection) {
                        break label8501;
                     }
                  } catch (Throwable var1264) {
                     var10000 = var1264;
                     var10001 = false;
                     break label8494;
                  }

                  label8487: {
                     try {
                        if (!(var3 instanceof Map)) {
                           break label8487;
                        }
                     } catch (Throwable var1263) {
                        var10000 = var1263;
                        var10001 = false;
                        break label8494;
                     }

                     if (var4) {
                        try {
                           this.appendDetail(var1, var2, (Map)var3);
                        } catch (Throwable var1232) {
                           var10000 = var1232;
                           var10001 = false;
                           break label8494;
                        }
                     } else {
                        try {
                           this.appendSummarySize(var1, var2, ((Map)var3).size());
                        } catch (Throwable var1233) {
                           var10000 = var1233;
                           var10001 = false;
                           break label8494;
                        }
                     }
                     break label8495;
                  }

                  label8481: {
                     try {
                        if (!(var3 instanceof long[])) {
                           break label8481;
                        }
                     } catch (Throwable var1262) {
                        var10000 = var1262;
                        var10001 = false;
                        break label8494;
                     }

                     if (var4) {
                        try {
                           this.appendDetail(var1, var2, (long[])var3);
                        } catch (Throwable var1234) {
                           var10000 = var1234;
                           var10001 = false;
                           break label8494;
                        }
                     } else {
                        try {
                           this.appendSummary(var1, var2, (long[])var3);
                        } catch (Throwable var1235) {
                           var10000 = var1235;
                           var10001 = false;
                           break label8494;
                        }
                     }
                     break label8495;
                  }

                  label8502: {
                     try {
                        if (var3 instanceof int[]) {
                           break label8502;
                        }
                     } catch (Throwable var1261) {
                        var10000 = var1261;
                        var10001 = false;
                        break label8494;
                     }

                     label8469: {
                        try {
                           if (!(var3 instanceof short[])) {
                              break label8469;
                           }
                        } catch (Throwable var1260) {
                           var10000 = var1260;
                           var10001 = false;
                           break label8494;
                        }

                        if (var4) {
                           try {
                              this.appendDetail(var1, var2, (short[])var3);
                           } catch (Throwable var1238) {
                              var10000 = var1238;
                              var10001 = false;
                              break label8494;
                           }
                        } else {
                           try {
                              this.appendSummary(var1, var2, (short[])var3);
                           } catch (Throwable var1239) {
                              var10000 = var1239;
                              var10001 = false;
                              break label8494;
                           }
                        }
                        break label8495;
                     }

                     label8463: {
                        try {
                           if (!(var3 instanceof byte[])) {
                              break label8463;
                           }
                        } catch (Throwable var1259) {
                           var10000 = var1259;
                           var10001 = false;
                           break label8494;
                        }

                        if (var4) {
                           try {
                              this.appendDetail(var1, var2, (byte[])var3);
                           } catch (Throwable var1240) {
                              var10000 = var1240;
                              var10001 = false;
                              break label8494;
                           }
                        } else {
                           try {
                              this.appendSummary(var1, var2, (byte[])var3);
                           } catch (Throwable var1241) {
                              var10000 = var1241;
                              var10001 = false;
                              break label8494;
                           }
                        }
                        break label8495;
                     }

                     label8503: {
                        try {
                           if (var3 instanceof char[]) {
                              break label8503;
                           }
                        } catch (Throwable var1258) {
                           var10000 = var1258;
                           var10001 = false;
                           break label8494;
                        }

                        label8504: {
                           try {
                              if (var3 instanceof double[]) {
                                 break label8504;
                              }
                           } catch (Throwable var1257) {
                              var10000 = var1257;
                              var10001 = false;
                              break label8494;
                           }

                           label8445: {
                              try {
                                 if (!(var3 instanceof float[])) {
                                    break label8445;
                                 }
                              } catch (Throwable var1256) {
                                 var10000 = var1256;
                                 var10001 = false;
                                 break label8494;
                              }

                              if (var4) {
                                 try {
                                    this.appendDetail(var1, var2, (float[])var3);
                                 } catch (Throwable var1246) {
                                    var10000 = var1246;
                                    var10001 = false;
                                    break label8494;
                                 }
                              } else {
                                 try {
                                    this.appendSummary(var1, var2, (float[])var3);
                                 } catch (Throwable var1247) {
                                    var10000 = var1247;
                                    var10001 = false;
                                    break label8494;
                                 }
                              }
                              break label8495;
                           }

                           label8439: {
                              try {
                                 if (!(var3 instanceof boolean[])) {
                                    break label8439;
                                 }
                              } catch (Throwable var1255) {
                                 var10000 = var1255;
                                 var10001 = false;
                                 break label8494;
                              }

                              if (var4) {
                                 try {
                                    this.appendDetail(var1, var2, (boolean[])var3);
                                 } catch (Throwable var1248) {
                                    var10000 = var1248;
                                    var10001 = false;
                                    break label8494;
                                 }
                              } else {
                                 try {
                                    this.appendSummary(var1, var2, (boolean[])var3);
                                 } catch (Throwable var1249) {
                                    var10000 = var1249;
                                    var10001 = false;
                                    break label8494;
                                 }
                              }
                              break label8495;
                           }

                           label8433: {
                              try {
                                 if (var3.getClass().isArray()) {
                                    break label8433;
                                 }
                              } catch (Throwable var1254) {
                                 var10000 = var1254;
                                 var10001 = false;
                                 break label8494;
                              }

                              if (var4) {
                                 try {
                                    this.appendDetail(var1, var2, var3);
                                 } catch (Throwable var1252) {
                                    var10000 = var1252;
                                    var10001 = false;
                                    break label8494;
                                 }
                              } else {
                                 try {
                                    this.appendSummary(var1, var2, var3);
                                 } catch (Throwable var1253) {
                                    var10000 = var1253;
                                    var10001 = false;
                                    break label8494;
                                 }
                              }
                              break label8495;
                           }

                           if (var4) {
                              try {
                                 this.appendDetail(var1, var2, (Object[])var3);
                              } catch (Throwable var1250) {
                                 var10000 = var1250;
                                 var10001 = false;
                                 break label8494;
                              }
                           } else {
                              try {
                                 this.appendSummary(var1, var2, (Object[])var3);
                              } catch (Throwable var1251) {
                                 var10000 = var1251;
                                 var10001 = false;
                                 break label8494;
                              }
                           }
                           break label8495;
                        }

                        if (var4) {
                           try {
                              this.appendDetail(var1, var2, (double[])var3);
                           } catch (Throwable var1244) {
                              var10000 = var1244;
                              var10001 = false;
                              break label8494;
                           }
                        } else {
                           try {
                              this.appendSummary(var1, var2, (double[])var3);
                           } catch (Throwable var1245) {
                              var10000 = var1245;
                              var10001 = false;
                              break label8494;
                           }
                        }
                        break label8495;
                     }

                     if (var4) {
                        try {
                           this.appendDetail(var1, var2, (char[])var3);
                        } catch (Throwable var1242) {
                           var10000 = var1242;
                           var10001 = false;
                           break label8494;
                        }
                     } else {
                        try {
                           this.appendSummary(var1, var2, (char[])var3);
                        } catch (Throwable var1243) {
                           var10000 = var1243;
                           var10001 = false;
                           break label8494;
                        }
                     }
                     break label8495;
                  }

                  if (var4) {
                     try {
                        this.appendDetail(var1, var2, (int[])var3);
                     } catch (Throwable var1236) {
                        var10000 = var1236;
                        var10001 = false;
                        break label8494;
                     }
                  } else {
                     try {
                        this.appendSummary(var1, var2, (int[])var3);
                     } catch (Throwable var1237) {
                        var10000 = var1237;
                        var10001 = false;
                        break label8494;
                     }
                  }
                  break label8495;
               }

               if (var4) {
                  label8370:
                  try {
                     this.appendDetail(var1, var2, (Collection)var3);
                  } catch (Throwable var1230) {
                     var10000 = var1230;
                     var10001 = false;
                     break label8370;
                  }
               } else {
                  label8372:
                  try {
                     this.appendSummarySize(var1, var2, ((Collection)var3).size());
                  } catch (Throwable var1231) {
                     var10000 = var1231;
                     var10001 = false;
                     break label8372;
                  }
               }
               break label8495;
            }

            Throwable var1265 = var10000;
            unregister(var3);
            throw var1265;
         }

         unregister(var3);
      }
   }

   protected void appendNullText(StringBuffer var1, String var2) {
      var1.append(this.nullText);
   }

   public void appendStart(StringBuffer var1, Object var2) {
      if (var2 != null) {
         this.appendClassName(var1, var2);
         this.appendIdentityHashCode(var1, var2);
         this.appendContentStart(var1);
         if (this.fieldSeparatorAtStart) {
            this.appendFieldSeparator(var1);
         }
      }

   }

   protected void appendSummary(StringBuffer var1, String var2, Object var3) {
      var1.append(this.summaryObjectStartText);
      var1.append(this.getShortClassName(var3.getClass()));
      var1.append(this.summaryObjectEndText);
   }

   protected void appendSummary(StringBuffer var1, String var2, byte[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   protected void appendSummary(StringBuffer var1, String var2, char[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   protected void appendSummary(StringBuffer var1, String var2, double[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   protected void appendSummary(StringBuffer var1, String var2, float[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   protected void appendSummary(StringBuffer var1, String var2, int[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   protected void appendSummary(StringBuffer var1, String var2, long[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   protected void appendSummary(StringBuffer var1, String var2, Object[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   protected void appendSummary(StringBuffer var1, String var2, short[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   protected void appendSummary(StringBuffer var1, String var2, boolean[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   protected void appendSummarySize(StringBuffer var1, String var2, int var3) {
      var1.append(this.sizeStartText);
      var1.append(var3);
      var1.append(this.sizeEndText);
   }

   public void appendSuper(StringBuffer var1, String var2) {
      this.appendToString(var1, var2);
   }

   public void appendToString(StringBuffer var1, String var2) {
      if (var2 != null) {
         int var3 = var2.indexOf(this.contentStart) + this.contentStart.length();
         int var4 = var2.lastIndexOf(this.contentEnd);
         if (var3 != var4 && var3 >= 0 && var4 >= 0) {
            if (this.fieldSeparatorAtStart) {
               this.removeLastFieldSeparator(var1);
            }

            var1.append(var2, var3, var4);
            this.appendFieldSeparator(var1);
         }
      }

   }

   protected String getArrayEnd() {
      return this.arrayEnd;
   }

   protected String getArraySeparator() {
      return this.arraySeparator;
   }

   protected String getArrayStart() {
      return this.arrayStart;
   }

   protected String getContentEnd() {
      return this.contentEnd;
   }

   protected String getContentStart() {
      return this.contentStart;
   }

   protected String getFieldNameValueSeparator() {
      return this.fieldNameValueSeparator;
   }

   protected String getFieldSeparator() {
      return this.fieldSeparator;
   }

   protected String getNullText() {
      return this.nullText;
   }

   protected String getShortClassName(Class var1) {
      return ClassUtils.getShortClassName(var1);
   }

   protected String getSizeEndText() {
      return this.sizeEndText;
   }

   protected String getSizeStartText() {
      return this.sizeStartText;
   }

   protected String getSummaryObjectEndText() {
      return this.summaryObjectEndText;
   }

   protected String getSummaryObjectStartText() {
      return this.summaryObjectStartText;
   }

   protected boolean isArrayContentDetail() {
      return this.arrayContentDetail;
   }

   protected boolean isDefaultFullDetail() {
      return this.defaultFullDetail;
   }

   protected boolean isFieldSeparatorAtEnd() {
      return this.fieldSeparatorAtEnd;
   }

   protected boolean isFieldSeparatorAtStart() {
      return this.fieldSeparatorAtStart;
   }

   protected boolean isFullDetail(Boolean var1) {
      return var1 == null ? this.defaultFullDetail : var1;
   }

   protected boolean isUseClassName() {
      return this.useClassName;
   }

   protected boolean isUseFieldNames() {
      return this.useFieldNames;
   }

   protected boolean isUseIdentityHashCode() {
      return this.useIdentityHashCode;
   }

   protected boolean isUseShortClassName() {
      return this.useShortClassName;
   }

   protected void reflectionAppendArrayDetail(StringBuffer var1, String var2, Object var3) {
      var1.append(this.arrayStart);
      int var5 = Array.getLength(var3);

      for(int var4 = 0; var4 < var5; ++var4) {
         Object var6 = Array.get(var3, var4);
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         if (var6 == null) {
            this.appendNullText(var1, var2);
         } else {
            this.appendInternal(var1, var2, var6, this.arrayContentDetail);
         }
      }

      var1.append(this.arrayEnd);
   }

   protected void removeLastFieldSeparator(StringBuffer var1) {
      int var5 = var1.length();
      int var6 = this.fieldSeparator.length();
      if (var5 > 0 && var6 > 0 && var5 >= var6) {
         boolean var4 = true;
         int var2 = 0;

         boolean var3;
         while(true) {
            var3 = var4;
            if (var2 >= var6) {
               break;
            }

            if (var1.charAt(var5 - 1 - var2) != this.fieldSeparator.charAt(var6 - 1 - var2)) {
               var3 = false;
               break;
            }

            ++var2;
         }

         if (var3) {
            var1.setLength(var5 - var6);
         }
      }

   }

   protected void setArrayContentDetail(boolean var1) {
      this.arrayContentDetail = var1;
   }

   protected void setArrayEnd(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.arrayEnd = var2;
   }

   protected void setArraySeparator(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.arraySeparator = var2;
   }

   protected void setArrayStart(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.arrayStart = var2;
   }

   protected void setContentEnd(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.contentEnd = var2;
   }

   protected void setContentStart(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.contentStart = var2;
   }

   protected void setDefaultFullDetail(boolean var1) {
      this.defaultFullDetail = var1;
   }

   protected void setFieldNameValueSeparator(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.fieldNameValueSeparator = var2;
   }

   protected void setFieldSeparator(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.fieldSeparator = var2;
   }

   protected void setFieldSeparatorAtEnd(boolean var1) {
      this.fieldSeparatorAtEnd = var1;
   }

   protected void setFieldSeparatorAtStart(boolean var1) {
      this.fieldSeparatorAtStart = var1;
   }

   protected void setNullText(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.nullText = var2;
   }

   protected void setSizeEndText(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.sizeEndText = var2;
   }

   protected void setSizeStartText(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.sizeStartText = var2;
   }

   protected void setSummaryObjectEndText(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.summaryObjectEndText = var2;
   }

   protected void setSummaryObjectStartText(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.summaryObjectStartText = var2;
   }

   protected void setUseClassName(boolean var1) {
      this.useClassName = var1;
   }

   protected void setUseFieldNames(boolean var1) {
      this.useFieldNames = var1;
   }

   protected void setUseIdentityHashCode(boolean var1) {
      this.useIdentityHashCode = var1;
   }

   protected void setUseShortClassName(boolean var1) {
      this.useShortClassName = var1;
   }

   private static final class DefaultToStringStyle extends ToStringStyle {
      private static final long serialVersionUID = 1L;

      DefaultToStringStyle() {
      }

      private Object readResolve() {
         return DEFAULT_STYLE;
      }
   }

   private static final class JsonToStringStyle extends ToStringStyle {
      private static final String FIELD_NAME_QUOTE = "\"";
      private static final long serialVersionUID = 1L;

      JsonToStringStyle() {
         this.setUseClassName(false);
         this.setUseIdentityHashCode(false);
         this.setContentStart("{");
         this.setContentEnd("}");
         this.setArrayStart("[");
         this.setArrayEnd("]");
         this.setFieldSeparator(",");
         this.setFieldNameValueSeparator(":");
         this.setNullText("null");
         this.setSummaryObjectStartText("\"<");
         this.setSummaryObjectEndText(">\"");
         this.setSizeStartText("\"<size=");
         this.setSizeEndText(">\"");
      }

      private void appendValueAsString(StringBuffer var1, String var2) {
         var1.append('"');
         var1.append(StringEscapeUtils.escapeJson(var2));
         var1.append('"');
      }

      private boolean isJsonArray(String var1) {
         return var1.startsWith(this.getArrayStart()) && var1.endsWith(this.getArrayEnd());
      }

      private boolean isJsonObject(String var1) {
         return var1.startsWith(this.getContentStart()) && var1.endsWith(this.getContentEnd());
      }

      private Object readResolve() {
         return JSON_STYLE;
      }

      public void append(StringBuffer var1, String var2, Object var3, Boolean var4) {
         if (var2 != null) {
            if (this.isFullDetail(var4)) {
               super.append(var1, var2, var3, var4);
            } else {
               throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
         } else {
            throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
         }
      }

      public void append(StringBuffer var1, String var2, byte[] var3, Boolean var4) {
         if (var2 != null) {
            if (this.isFullDetail(var4)) {
               super.append(var1, var2, var3, var4);
            } else {
               throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
         } else {
            throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
         }
      }

      public void append(StringBuffer var1, String var2, char[] var3, Boolean var4) {
         if (var2 != null) {
            if (this.isFullDetail(var4)) {
               super.append(var1, var2, var3, var4);
            } else {
               throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
         } else {
            throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
         }
      }

      public void append(StringBuffer var1, String var2, double[] var3, Boolean var4) {
         if (var2 != null) {
            if (this.isFullDetail(var4)) {
               super.append(var1, var2, var3, var4);
            } else {
               throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
         } else {
            throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
         }
      }

      public void append(StringBuffer var1, String var2, float[] var3, Boolean var4) {
         if (var2 != null) {
            if (this.isFullDetail(var4)) {
               super.append(var1, var2, var3, var4);
            } else {
               throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
         } else {
            throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
         }
      }

      public void append(StringBuffer var1, String var2, int[] var3, Boolean var4) {
         if (var2 != null) {
            if (this.isFullDetail(var4)) {
               super.append(var1, var2, var3, var4);
            } else {
               throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
         } else {
            throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
         }
      }

      public void append(StringBuffer var1, String var2, long[] var3, Boolean var4) {
         if (var2 != null) {
            if (this.isFullDetail(var4)) {
               super.append(var1, var2, var3, var4);
            } else {
               throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
         } else {
            throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
         }
      }

      public void append(StringBuffer var1, String var2, Object[] var3, Boolean var4) {
         if (var2 != null) {
            if (this.isFullDetail(var4)) {
               super.append(var1, var2, var3, var4);
            } else {
               throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
         } else {
            throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
         }
      }

      public void append(StringBuffer var1, String var2, short[] var3, Boolean var4) {
         if (var2 != null) {
            if (this.isFullDetail(var4)) {
               super.append(var1, var2, var3, var4);
            } else {
               throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
         } else {
            throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
         }
      }

      public void append(StringBuffer var1, String var2, boolean[] var3, Boolean var4) {
         if (var2 != null) {
            if (this.isFullDetail(var4)) {
               super.append(var1, var2, var3, var4);
            } else {
               throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
            }
         } else {
            throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
         }
      }

      protected void appendDetail(StringBuffer var1, String var2, char var3) {
         this.appendValueAsString(var1, String.valueOf(var3));
      }

      protected void appendDetail(StringBuffer var1, String var2, Object var3) {
         if (var3 == null) {
            this.appendNullText(var1, var2);
         } else if (!(var3 instanceof String) && !(var3 instanceof Character)) {
            if (!(var3 instanceof Number) && !(var3 instanceof Boolean)) {
               String var4 = var3.toString();
               if (!this.isJsonObject(var4) && !this.isJsonArray(var4)) {
                  this.appendDetail(var1, var2, var4);
               } else {
                  var1.append(var3);
               }
            } else {
               var1.append(var3);
            }
         } else {
            this.appendValueAsString(var1, var3.toString());
         }
      }

      protected void appendFieldStart(StringBuffer var1, String var2) {
         if (var2 != null) {
            StringBuilder var3 = new StringBuilder();
            var3.append("\"");
            var3.append(StringEscapeUtils.escapeJson(var2));
            var3.append("\"");
            super.appendFieldStart(var1, var3.toString());
         } else {
            throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
         }
      }
   }

   private static final class MultiLineToStringStyle extends ToStringStyle {
      private static final long serialVersionUID = 1L;

      MultiLineToStringStyle() {
         this.setContentStart("[");
         StringBuilder var1 = new StringBuilder();
         var1.append(System.lineSeparator());
         var1.append("  ");
         this.setFieldSeparator(var1.toString());
         this.setFieldSeparatorAtStart(true);
         var1 = new StringBuilder();
         var1.append(System.lineSeparator());
         var1.append("]");
         this.setContentEnd(var1.toString());
      }

      private Object readResolve() {
         return MULTI_LINE_STYLE;
      }
   }

   private static final class NoClassNameToStringStyle extends ToStringStyle {
      private static final long serialVersionUID = 1L;

      NoClassNameToStringStyle() {
         this.setUseClassName(false);
         this.setUseIdentityHashCode(false);
      }

      private Object readResolve() {
         return NO_CLASS_NAME_STYLE;
      }
   }

   private static final class NoFieldNameToStringStyle extends ToStringStyle {
      private static final long serialVersionUID = 1L;

      NoFieldNameToStringStyle() {
         this.setUseFieldNames(false);
      }

      private Object readResolve() {
         return NO_FIELD_NAMES_STYLE;
      }
   }

   private static final class ShortPrefixToStringStyle extends ToStringStyle {
      private static final long serialVersionUID = 1L;

      ShortPrefixToStringStyle() {
         this.setUseShortClassName(true);
         this.setUseIdentityHashCode(false);
      }

      private Object readResolve() {
         return SHORT_PREFIX_STYLE;
      }
   }

   private static final class SimpleToStringStyle extends ToStringStyle {
      private static final long serialVersionUID = 1L;

      SimpleToStringStyle() {
         this.setUseClassName(false);
         this.setUseIdentityHashCode(false);
         this.setUseFieldNames(false);
         this.setContentStart("");
         this.setContentEnd("");
      }

      private Object readResolve() {
         return SIMPLE_STYLE;
      }
   }
}
