package javax.jmdns.impl.constants;

public enum DNSState {
   ANNOUNCED("announced", DNSState.StateClass.announced),
   ANNOUNCING_1("announcing 1", DNSState.StateClass.announcing),
   ANNOUNCING_2("announcing 2", DNSState.StateClass.announcing),
   CANCELED("canceled", DNSState.StateClass.canceled),
   CANCELING_1("canceling 1", DNSState.StateClass.canceling),
   CANCELING_2("canceling 2", DNSState.StateClass.canceling),
   CANCELING_3("canceling 3", DNSState.StateClass.canceling),
   CLOSED,
   CLOSING("closing", DNSState.StateClass.closing),
   PROBING_1("probing 1", DNSState.StateClass.probing),
   PROBING_2("probing 2", DNSState.StateClass.probing),
   PROBING_3("probing 3", DNSState.StateClass.probing);

   private final String _name;
   private final DNSState.StateClass _state;

   static {
      DNSState var0 = new DNSState("CLOSED", 11, "closed", DNSState.StateClass.closed);
      CLOSED = var0;
   }

   private DNSState(String var3, DNSState.StateClass var4) {
      this._name = var3;
      this._state = var4;
   }

   public final DNSState advance() {
      switch(null.$SwitchMap$javax$jmdns$impl$constants$DNSState[this.ordinal()]) {
      case 1:
         return PROBING_2;
      case 2:
         return PROBING_3;
      case 3:
         return ANNOUNCING_1;
      case 4:
         return ANNOUNCING_2;
      case 5:
         return ANNOUNCED;
      case 6:
         return ANNOUNCED;
      case 7:
         return CANCELING_2;
      case 8:
         return CANCELING_3;
      case 9:
         return CANCELED;
      case 10:
         return CANCELED;
      case 11:
         return CLOSED;
      case 12:
         return CLOSED;
      default:
         return this;
      }
   }

   public final boolean isAnnounced() {
      return this._state == DNSState.StateClass.announced;
   }

   public final boolean isAnnouncing() {
      return this._state == DNSState.StateClass.announcing;
   }

   public final boolean isCanceled() {
      return this._state == DNSState.StateClass.canceled;
   }

   public final boolean isCanceling() {
      return this._state == DNSState.StateClass.canceling;
   }

   public final boolean isClosed() {
      return this._state == DNSState.StateClass.closed;
   }

   public final boolean isClosing() {
      return this._state == DNSState.StateClass.closing;
   }

   public final boolean isProbing() {
      return this._state == DNSState.StateClass.probing;
   }

   public final DNSState revert() {
      switch(null.$SwitchMap$javax$jmdns$impl$constants$DNSState[this.ordinal()]) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
         return PROBING_1;
      case 7:
      case 8:
      case 9:
         return CANCELING_1;
      case 10:
         return CANCELED;
      case 11:
         return CLOSING;
      case 12:
         return CLOSED;
      default:
         return this;
      }
   }

   public final String toString() {
      return this._name;
   }

   private static enum StateClass {
      announced,
      announcing,
      canceled,
      canceling,
      closed,
      closing,
      probing;

      static {
         DNSState.StateClass var0 = new DNSState.StateClass("closed", 6);
         closed = var0;
      }
   }
}
