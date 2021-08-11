package net.sf.fmj.filtergraph;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.media.Codec;
import javax.media.Format;
import javax.media.Multiplexer;
import javax.media.PlugIn;
import javax.media.PlugInManager;
import javax.media.Renderer;
import javax.media.ResourceUnavailableException;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import net.sf.fmj.media.BasicPlugIn;
import net.sf.fmj.media.BasicTrackControl;
import net.sf.fmj.media.Log;
import org.atalk.android.util.java.awt.Dimension;

public class SimpleGraphBuilder {
   public static GraphInspector inspector;
   protected int STAGES = 5;
   protected int indent = 0;
   protected Hashtable plugIns = new Hashtable(40);
   protected Vector targetPluginNames = null;
   protected GraphNode[] targetPlugins = null;
   protected int targetType = -1;

   public static PlugIn createPlugIn(String var0, int var1) {
      Object var4;
      try {
         var4 = BasicPlugIn.getClassForName(var0).newInstance();
      } catch (Exception var2) {
         return null;
      } catch (Error var3) {
         return null;
      }

      return verifyClass(var4, var1) ? (PlugIn)var4 : null;
   }

   public static Codec findCodec(Format var0, Format var1, Format[] var2, Format[] var3) {
      Vector var12 = PlugInManager.getPlugInList(var0, var1, 2);
      if (var12 == null) {
         return null;
      } else {
         for(int var4 = 0; var4 < var12.size(); ++var4) {
            Codec var11 = (Codec)createPlugIn((String)var12.elementAt(var4), 2);
            if (var11 != null) {
               Format var9 = matches((Format)var0, (Format[])var11.getSupportedInputFormats(), (PlugIn)null, var11);
               Format var8 = var9;
               if (var9 != null) {
                  if (var2 != null && var2.length > 0) {
                     var2[0] = var9;
                  }

                  Format[] var13 = var11.getSupportedOutputFormats(var9);
                  if (var13 != null && var13.length != 0) {
                     boolean var7 = false;
                     int var5 = 0;

                     boolean var6;
                     while(true) {
                        var9 = var8;
                        var6 = var7;
                        if (var5 >= var13.length) {
                           break;
                        }

                        label70: {
                           if (var1 != null) {
                              var9 = var8;
                              if (!var1.matches(var13[var5])) {
                                 break label70;
                              }

                              Format var10 = var1.intersects(var13[var5]);
                              var9 = var10;
                              var8 = var10;
                              if (var10 == null) {
                                 break label70;
                              }
                           } else {
                              var8 = var13[var5];
                           }

                           var9 = var8;
                           if (var11.setOutputFormat(var8) != null) {
                              var6 = true;
                              var9 = var8;
                              break;
                           }
                        }

                        ++var5;
                        var8 = var9;
                     }

                     if (var6) {
                        try {
                           var11.open();
                        } catch (ResourceUnavailableException var14) {
                        }

                        if (var3 != null && var3.length > 0) {
                           var3[0] = var9;
                        }

                        return var11;
                     }
                  }
               }
            }
         }

         return null;
      }
   }

   public static Renderer findRenderer(Format var0) {
      Vector var3 = PlugInManager.getPlugInList(var0, (Format)null, 4);
      if (var3 == null) {
         return null;
      } else {
         for(int var1 = 0; var1 < var3.size(); ++var1) {
            Renderer var2 = (Renderer)createPlugIn((String)var3.elementAt(var1), 4);
            if (var2 != null && matches((Format)var0, (Format[])var2.getSupportedInputFormats(), (PlugIn)null, var2) != null) {
               try {
                  var2.open();
                  return var2;
               } catch (ResourceUnavailableException var4) {
                  return var2;
               }
            }
         }

         return null;
      }
   }

   public static Vector findRenderingChain(Format var0, Vector var1) {
      GraphNode var2 = (new SimpleGraphBuilder()).buildGraph(var0);
      GraphNode var3 = var2;
      if (var2 == null) {
         return null;
      } else {
         Vector var4;
         for(var4 = new Vector(10); var3 != null && var3.plugin != null; var3 = var3.prev) {
            var4.addElement(var3.plugin);
            if (var1 != null) {
               var1.addElement(var3.input);
            }
         }

         return var4;
      }
   }

   public static GraphNode getPlugInNode(String var0, int var1, Map var2) {
      if (var2 != null) {
         GraphNode var3 = (GraphNode)var2.get(var0);
         if (var3 != null) {
            if (var3.failed) {
               return null;
            }

            if (verifyClass(var3.plugin, var1)) {
               return var3;
            }

            return null;
         }
      }

      PlugIn var5 = createPlugIn(var0, var1);
      GraphNode var4 = new GraphNode(var0, var5, (Format)null, (GraphNode)null, 0);
      if (var2 != null) {
         var2.put(var0, var4);
      }

      if (var5 == null) {
         var4.failed = true;
         return null;
      } else {
         return var4;
      }
   }

   public static Format matches(Format var0, Format[] var1, PlugIn var2, PlugIn var3) {
      if (var0 == null) {
         return null;
      } else if (var1 == null) {
         return null;
      } else {
         for(int var4 = 0; var4 < var1.length; ++var4) {
            if (var1[var4] != null && var1[var4].getClass().isAssignableFrom(var0.getClass()) && var0.matches(var1[var4])) {
               Format var6 = var0.intersects(var1[var4]);
               if (var6 != null) {
                  Format var5 = var6;
                  if (var3 != null) {
                     var6 = verifyInput(var3, var6);
                     var5 = var6;
                     if (var6 == null) {
                        continue;
                     }
                  }

                  var6 = var5;
                  if (var2 != null) {
                     Format var7 = verifyOutput(var2, var5);
                     var6 = var7;
                     if (var7 == null) {
                        continue;
                     }
                  }

                  if (var3 == null || var6 == var5 || verifyInput(var3, var6) != null) {
                     return var6;
                  }
               }
            }
         }

         return null;
      }
   }

   public static Format matches(Format[] var0, Format var1, PlugIn var2, PlugIn var3) {
      return matches(var0, new Format[]{var1}, var2, var3);
   }

   public static Format matches(Format[] var0, Format[] var1, PlugIn var2, PlugIn var3) {
      if (var0 == null) {
         return null;
      } else {
         for(int var4 = 0; var4 < var0.length; ++var4) {
            Format var5 = matches(var0[var4], var1, var2, var3);
            if (var5 != null) {
               return var5;
            }
         }

         return null;
      }
   }

   public static void setGraphInspector(GraphInspector var0) {
      inspector = var0;
   }

   public static boolean verifyClass(Object var0, int var1) {
      Class var2;
      if (var1 != 2) {
         if (var1 != 4) {
            if (var1 != 5) {
               var2 = PlugIn.class;
            } else {
               var2 = Multiplexer.class;
            }
         } else {
            var2 = Renderer.class;
         }
      } else {
         var2 = Codec.class;
      }

      return var2.isInstance(var0);
   }

   public static Format verifyInput(PlugIn var0, Format var1) {
      if (var0 instanceof Codec) {
         return ((Codec)var0).setInputFormat(var1);
      } else {
         return var0 instanceof Renderer ? ((Renderer)var0).setInputFormat(var1) : null;
      }
   }

   public static Format verifyOutput(PlugIn var0, Format var1) {
      return var0 instanceof Codec ? ((Codec)var0).setOutputFormat(var1) : null;
   }

   protected GraphNode buildGraph(Vector var1) {
      GraphNode var2;
      do {
         var2 = this.doBuildGraph(var1);
      } while(var2 == null && !var1.isEmpty());

      return var2;
   }

   GraphNode buildGraph(Format var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("Input: ");
      var2.append(var1);
      Log.comment(var2.toString());
      Vector var5 = new Vector();
      GraphNode var3 = new GraphNode((String)null, (PlugIn)null, var1, (GraphNode)null, 0);
      this.indent = 1;
      Log.setIndent(1);
      if (!this.setDefaultTargets(var1)) {
         return null;
      } else {
         var5.addElement(var3);

         while(true) {
            var3 = this.buildGraph(var5);
            if (var3 == null) {
               this.indent = 0;
               Log.setIndent(0);
               return var3;
            }

            GraphNode var4 = this.verifyGraph(var3);
            if (var4 == null) {
               this.indent = 0;
               Log.setIndent(0);
               return var3;
            }

            this.removeFailure(var5, var4, var1);
         }
      }
   }

   public boolean buildGraph(BasicTrackControl var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("Input: ");
      var2.append(var1.getOriginalFormat());
      Log.comment(var2.toString());
      Vector var4 = new Vector();
      GraphNode var3 = new GraphNode((String)null, (PlugIn)null, var1.getOriginalFormat(), (GraphNode)null, 0);
      this.indent = 1;
      Log.setIndent(1);
      if (!this.setDefaultTargets(var1.getOriginalFormat())) {
         return false;
      } else {
         var4.addElement(var3);

         while(true) {
            var3 = this.buildGraph(var4);
            if (var3 == null) {
               this.indent = 0;
               Log.setIndent(0);
               return false;
            }

            var3 = this.buildTrackFromGraph(var1, var3);
            if (var3 == null) {
               this.indent = 0;
               Log.setIndent(0);
               return true;
            }

            this.removeFailure(var4, var3, var1.getOriginalFormat());
         }
      }
   }

   protected GraphNode buildTrackFromGraph(BasicTrackControl var1, GraphNode var2) {
      return null;
   }

   GraphNode doBuildGraph(Vector var1) {
      if (var1.isEmpty()) {
         return null;
      } else {
         GraphNode var9 = (GraphNode)var1.firstElement();
         var1.removeElementAt(0);
         if (var9.input != null || var9.plugin != null && var9.plugin instanceof Codec) {
            int var4 = this.indent;
            Log.setIndent(var9.level + 1);
            if (var9.plugin != null && verifyInput(var9.plugin, var9.input) == null) {
               return null;
            } else {
               GraphNode var6 = this.findTarget(var9);
               if (var6 != null) {
                  this.indent = var4;
                  Log.setIndent(var4);
                  return var6;
               } else if (var9.level >= this.STAGES) {
                  this.indent = var4;
                  Log.setIndent(var4);
                  return null;
               } else {
                  label152: {
                     boolean var5 = false;
                     Format[] var8;
                     Format var14;
                     if (var9.plugin != null) {
                        Format[] var13;
                        if (var9.output != null) {
                           var13 = new Format[]{var9.output};
                        } else {
                           Format[] var7 = var9.getSupportedOutputs(var9.input);
                           if (var7 == null) {
                              break label152;
                           }

                           var13 = var7;
                           if (var7.length == 0) {
                              break label152;
                           }
                        }

                        var14 = var9.input;
                        var8 = var13;
                     } else {
                        var8 = new Format[]{var9.input};
                        var14 = null;
                     }

                     int var2 = 0;

                     for(Format var15 = var14; var2 < var8.length; ++var2) {
                        if (var9.custom || var15 == null || !var15.equals(var8[var2])) {
                           if (var9.plugin != null) {
                              GraphInspector var16;
                              if (verifyOutput(var9.plugin, var8[var2]) == null) {
                                 var16 = inspector;
                                 if (var16 != null && var16.detailMode()) {
                                    inspector.verifyOutputFailed(var9.plugin, var8[var2]);
                                 }
                                 continue;
                              }

                              var16 = inspector;
                              if (var16 != null && !var16.verify((Codec)var9.plugin, var9.input, var8[var2])) {
                                 continue;
                              }
                           }

                           Vector var17 = PlugInManager.getPlugInList(var8[var2], (Format)null, 2);
                           if (var17 != null && var17.size() != 0) {
                              for(int var3 = 0; var3 < var17.size(); ++var3) {
                                 GraphNode var10 = getPlugInNode((String)var17.elementAt(var3), 2, this.plugIns);
                                 if (var10 != null && !var10.checkAttempted(var8[var2])) {
                                    Format[] var11 = var10.getSupportedInputs();
                                    Format var18 = matches((Format)var8[var2], (Format[])var11, (PlugIn)null, var10.plugin);
                                    if (var18 == null) {
                                       GraphInspector var19 = inspector;
                                       if (var19 != null && var19.detailMode()) {
                                          inspector.verifyInputFailed(var10.plugin, var8[var2]);
                                       }
                                    } else {
                                       GraphInspector var12 = inspector;
                                       if (var12 == null || !var12.detailMode() || inspector.verify((Codec)var10.plugin, var18, (Format)null)) {
                                          var1.addElement(new GraphNode(var10, var18, var9, var9.level + 1));
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }

                     this.indent = var4;
                     Log.setIndent(var4);
                     return null;
                  }

                  this.indent = var4;
                  Log.setIndent(var4);
                  return null;
               }
            }
         } else {
            Log.error("Internal error: doBuildGraph");
            return null;
         }
      }
   }

   protected GraphNode findTarget(GraphNode var1) {
      Format[] var2;
      if (var1.plugin == null) {
         var2 = new Format[]{var1.input};
      } else if (var1.output != null) {
         var2 = new Format[]{var1.output};
      } else {
         Format[] var3 = var1.getSupportedOutputs(var1.input);
         if (var3 == null) {
            return null;
         }

         var2 = var3;
         if (var3.length == 0) {
            return null;
         }
      }

      if (this.targetPlugins != null) {
         var1 = this.verifyTargetPlugins(var1, var2);
         if (var1 != null) {
            return var1;
         }
      }

      return null;
   }

   void removeFailure(Vector var1, GraphNode var2, Format var3) {
      if (var2.plugin != null) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Failed to open plugin ");
         var4.append(var2.plugin);
         var4.append(". Will re-build the graph allover again");
         Log.comment(var4.toString());
         var1.removeAllElements();
         GraphNode var7 = new GraphNode((String)null, (PlugIn)null, var3, (GraphNode)null, 0);
         this.indent = 1;
         Log.setIndent(1);
         var1.addElement(var7);
         var2.failed = true;
         this.plugIns.put(var2.plugin.getClass().getName(), var2);
         Enumeration var5 = this.plugIns.keys();

         while(var5.hasMoreElements()) {
            String var6 = (String)var5.nextElement();
            if (!((GraphNode)this.plugIns.get(var6)).failed) {
               this.plugIns.remove(var6);
            }
         }

      }
   }

   public void reset() {
      Enumeration var1 = this.plugIns.elements();

      while(var1.hasMoreElements()) {
         ((GraphNode)var1.nextElement()).resetAttempted();
      }

   }

   protected boolean setDefaultTargetRenderer(Format var1) {
      if (var1 instanceof AudioFormat) {
         this.targetPluginNames = PlugInManager.getPlugInList(new AudioFormat((String)null, -1.0D, -1, -1, -1, -1, -1, -1.0D, (Class)null), (Format)null, 4);
      } else if (var1 instanceof VideoFormat) {
         this.targetPluginNames = PlugInManager.getPlugInList(new VideoFormat((String)null, (Dimension)null, -1, (Class)null, -1.0F), (Format)null, 4);
      } else {
         this.targetPluginNames = PlugInManager.getPlugInList((Format)null, (Format)null, 4);
      }

      Vector var2 = this.targetPluginNames;
      if (var2 != null && var2.size() != 0) {
         this.targetPlugins = new GraphNode[this.targetPluginNames.size()];
         this.targetType = 4;
         return true;
      } else {
         return false;
      }
   }

   protected boolean setDefaultTargets(Format var1) {
      return this.setDefaultTargetRenderer(var1);
   }

   protected GraphNode verifyGraph(GraphNode var1) {
      Format var3 = null;
      Vector var4 = new Vector(5);
      if (var1.plugin == null) {
         return null;
      } else {
         int var2 = this.indent++;
         Log.setIndent(var2);

         while(true) {
            if (var1 != null && var1.plugin != null) {
               StringBuilder var7;
               label85: {
                  if (var4.contains(var1.plugin)) {
                     if (var1.cname == null) {
                        break label85;
                     }

                     PlugIn var5 = createPlugIn(var1.cname, -1);
                     if (var5 == null) {
                        break label85;
                     }

                     var1.plugin = var5;
                  } else {
                     var4.addElement(var1.plugin);
                  }

                  if ((var1.type == -1 || var1.type == 4) && var1.plugin instanceof Renderer) {
                     ((Renderer)var1.plugin).setInputFormat(var1.input);
                  } else if ((var1.type == -1 || var1.type == 2) && var1.plugin instanceof Codec) {
                     ((Codec)var1.plugin).setInputFormat(var1.input);
                     if (var3 != null) {
                        ((Codec)var1.plugin).setOutputFormat(var3);
                     } else if (var1.output != null) {
                        ((Codec)var1.plugin).setOutputFormat(var1.output);
                     }
                  }

                  if (var1.type != -1 && var1.type != 4 || !(var1.plugin instanceof Renderer)) {
                     try {
                        var1.plugin.open();
                     } catch (Exception var6) {
                        var7 = new StringBuilder();
                        var7.append("Failed to open: ");
                        var7.append(var1.plugin);
                        Log.warning(var7.toString());
                        var1.failed = true;
                        return var1;
                     }
                  }

                  var3 = var1.input;
                  var1 = var1.prev;
                  continue;
               }

               var7 = new StringBuilder();
               var7.append("Failed to instantiate ");
               var7.append(var1.cname);
               Log.write(var7.toString());
               return var1;
            }

            var2 = this.indent--;
            Log.setIndent(var2);
            return null;
         }
      }
   }

   protected GraphNode verifyTargetPlugins(GraphNode var1, Format[] var2) {
      int var3 = 0;

      GraphNode var6;
      Format var8;
      while(true) {
         GraphNode[] var4 = this.targetPlugins;
         if (var3 >= var4.length) {
            return null;
         }

         label63: {
            GraphNode var5 = var4[var3];
            var6 = var5;
            if (var5 == null) {
               String var7 = (String)this.targetPluginNames.elementAt(var3);
               if (var7 == null || matches((Format[])var2, (Format[])PlugInManager.getSupportedInputFormats(var7, this.targetType), (PlugIn)null, (PlugIn)null) == null) {
                  break label63;
               }

               var5 = getPlugInNode(var7, this.targetType, this.plugIns);
               var6 = var5;
               if (var5 == null) {
                  this.targetPluginNames.setElementAt((Object)null, var3);
                  break label63;
               }

               this.targetPlugins[var3] = var5;
            }

            var8 = matches(var2, var6.getSupportedInputs(), var1.plugin, var6.plugin);
            if (var8 != null) {
               if (inspector == null) {
                  break;
               }

               if (var1.plugin == null || inspector.verify((Codec)var1.plugin, var1.input, var8)) {
                  if ((var6.type == -1 || var6.type == 2) && var6.plugin instanceof Codec) {
                     if (inspector.verify((Codec)var6.plugin, var8, (Format)null)) {
                        break;
                     }
                  } else if (var6.type != -1 && var6.type != 4 || !(var6.plugin instanceof Renderer) || inspector.verify((Renderer)var6.plugin, var8)) {
                     break;
                  }
               }
            }
         }

         ++var3;
      }

      return new GraphNode(var6, var8, var1, var1.level + 1);
   }
}
