package javax.media;

import java.util.EventListener;
import javax.media.format.FormatChangeEvent;

public class ControllerAdapter implements ControllerListener, EventListener {
   public void audioDeviceUnavailable(AudioDeviceUnavailableEvent var1) {
   }

   public void cachingControl(CachingControlEvent var1) {
   }

   public void configureComplete(ConfigureCompleteEvent var1) {
   }

   public void connectionError(ConnectionErrorEvent var1) {
   }

   public void controllerClosed(ControllerClosedEvent var1) {
   }

   public void controllerError(ControllerErrorEvent var1) {
   }

   public void controllerUpdate(ControllerEvent var1) {
      if (var1 instanceof AudioDeviceUnavailableEvent) {
         this.audioDeviceUnavailable((AudioDeviceUnavailableEvent)var1);
      } else if (var1 instanceof CachingControlEvent) {
         this.cachingControl((CachingControlEvent)var1);
      } else {
         if (var1 instanceof ControllerClosedEvent) {
            this.controllerClosed((ControllerClosedEvent)var1);
            if (var1 instanceof ControllerErrorEvent) {
               this.controllerError((ControllerErrorEvent)var1);
               if (var1 instanceof ConnectionErrorEvent) {
                  this.connectionError((ConnectionErrorEvent)var1);
               }

               if (var1 instanceof InternalErrorEvent) {
                  this.internalError((InternalErrorEvent)var1);
               }

               if (var1 instanceof ResourceUnavailableEvent) {
                  this.resourceUnavailable((ResourceUnavailableEvent)var1);
                  return;
               }
            } else if (var1 instanceof DataLostErrorEvent) {
               this.dataLostError((DataLostErrorEvent)var1);
               return;
            }
         } else {
            if (var1 instanceof DurationUpdateEvent) {
               this.durationUpdate((DurationUpdateEvent)var1);
               return;
            }

            if (var1 instanceof FormatChangeEvent) {
               this.formatChange((FormatChangeEvent)var1);
               if (var1 instanceof SizeChangeEvent) {
                  this.sizeChange((SizeChangeEvent)var1);
                  return;
               }
            } else {
               if (var1 instanceof MediaTimeSetEvent) {
                  this.mediaTimeSet((MediaTimeSetEvent)var1);
                  return;
               }

               if (var1 instanceof RateChangeEvent) {
                  this.rateChange((RateChangeEvent)var1);
                  return;
               }

               if (var1 instanceof StopTimeChangeEvent) {
                  this.stopTimeChange((StopTimeChangeEvent)var1);
                  return;
               }

               if (var1 instanceof TransitionEvent) {
                  this.transition((TransitionEvent)var1);
                  if (var1 instanceof ConfigureCompleteEvent) {
                     this.configureComplete((ConfigureCompleteEvent)var1);
                     return;
                  }

                  if (var1 instanceof PrefetchCompleteEvent) {
                     this.prefetchComplete((PrefetchCompleteEvent)var1);
                     return;
                  }

                  if (var1 instanceof RealizeCompleteEvent) {
                     this.realizeComplete((RealizeCompleteEvent)var1);
                     return;
                  }

                  if (var1 instanceof StartEvent) {
                     this.start((StartEvent)var1);
                     return;
                  }

                  if (var1 instanceof StopEvent) {
                     this.stop((StopEvent)var1);
                     if (var1 instanceof DataStarvedEvent) {
                        this.dataStarved((DataStarvedEvent)var1);
                        return;
                     }

                     if (var1 instanceof DeallocateEvent) {
                        this.deallocate((DeallocateEvent)var1);
                        return;
                     }

                     if (var1 instanceof EndOfMediaEvent) {
                        this.endOfMedia((EndOfMediaEvent)var1);
                        return;
                     }

                     if (var1 instanceof RestartingEvent) {
                        this.restarting((RestartingEvent)var1);
                        return;
                     }

                     if (var1 instanceof StopAtTimeEvent) {
                        this.stopAtTime((StopAtTimeEvent)var1);
                        return;
                     }

                     if (var1 instanceof StopByRequestEvent) {
                        this.stopByRequest((StopByRequestEvent)var1);
                     }
                  }
               }
            }
         }

      }
   }

   public void dataLostError(DataLostErrorEvent var1) {
   }

   public void dataStarved(DataStarvedEvent var1) {
   }

   public void deallocate(DeallocateEvent var1) {
   }

   public void durationUpdate(DurationUpdateEvent var1) {
   }

   public void endOfMedia(EndOfMediaEvent var1) {
   }

   public void formatChange(FormatChangeEvent var1) {
   }

   public void internalError(InternalErrorEvent var1) {
   }

   public void mediaTimeSet(MediaTimeSetEvent var1) {
   }

   public void prefetchComplete(PrefetchCompleteEvent var1) {
   }

   public void rateChange(RateChangeEvent var1) {
   }

   public void realizeComplete(RealizeCompleteEvent var1) {
   }

   public void replaceURL(Object var1) {
      throw new UnsupportedOperationException();
   }

   public void resourceUnavailable(ResourceUnavailableEvent var1) {
   }

   public void restarting(RestartingEvent var1) {
   }

   public void showDocument(Object var1) {
      throw new UnsupportedOperationException();
   }

   public void sizeChange(SizeChangeEvent var1) {
   }

   public void start(StartEvent var1) {
   }

   public void stop(StopEvent var1) {
   }

   public void stopAtTime(StopAtTimeEvent var1) {
   }

   public void stopByRequest(StopByRequestEvent var1) {
   }

   public void stopTimeChange(StopTimeChangeEvent var1) {
   }

   public void transition(TransitionEvent var1) {
   }
}
