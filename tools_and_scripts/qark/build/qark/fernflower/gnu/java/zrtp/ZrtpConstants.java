package gnu.java.zrtp;

import gnu.java.bigintcrypto.BigIntegerCrypto;
import gnu.java.zrtp.utils.ZrtpFortuna;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.engines.TwofishEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.cryptozrtp.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.cryptozrtp.BasicAgreement;
import org.bouncycastle.cryptozrtp.agreement.DHBasicAgreement;
import org.bouncycastle.cryptozrtp.agreement.Djb25519DHBasicAgreement;
import org.bouncycastle.cryptozrtp.agreement.ECDHBasicAgreement;
import org.bouncycastle.cryptozrtp.generators.DHBasicKeyPairGenerator;
import org.bouncycastle.cryptozrtp.generators.Djb25519KeyPairGenerator;
import org.bouncycastle.cryptozrtp.generators.ECKeyPairGenerator;
import org.bouncycastle.cryptozrtp.params.DHKeyGenerationParameters;
import org.bouncycastle.cryptozrtp.params.DHParameters;
import org.bouncycastle.cryptozrtp.params.Djb25519KeyGenerationParameters;
import org.bouncycastle.cryptozrtp.params.ECDomainParameters;
import org.bouncycastle.cryptozrtp.params.ECKeyGenerationParameters;
import org.bouncycastle.mathzrtp.ec.ECCurve;

public class ZrtpConstants {
   public static final String AES_128 = "AES-CM-128";
   public static final String AES_256 = "AES-CM-256";
   public static final byte[] CommitMsg = new byte[]{67, 111, 109, 109, 105, 116, 32, 32};
   public static final byte[] Conf2AckMsg = new byte[]{67, 111, 110, 102, 50, 65, 67, 75};
   public static final byte[] Confirm1Msg = new byte[]{67, 111, 110, 102, 105, 114, 109, 49};
   public static final byte[] Confirm2Msg = new byte[]{67, 111, 110, 102, 105, 114, 109, 50};
   public static final byte[] DHPart1Msg = new byte[]{68, 72, 80, 97, 114, 116, 49, 32};
   public static final byte[] DHPart2Msg = new byte[]{68, 72, 80, 97, 114, 116, 50, 32};
   public static final byte[] ErrorAckMsg = new byte[]{69, 114, 114, 111, 114, 65, 67, 75};
   public static final byte[] ErrorMsg = new byte[]{69, 114, 114, 111, 114, 32, 32, 32};
   public static final byte[] HelloAckMsg = new byte[]{72, 101, 108, 108, 111, 65, 67, 75};
   public static final byte[] HelloMsg = new byte[]{72, 101, 108, 108, 111, 32, 32, 32};
   public static final byte[] KDFString = new byte[]{90, 82, 84, 80, 45, 72, 77, 65, 67, 45, 75, 68, 70};
   public static final int MAX_DIGEST_LENGTH = 64;
   public static final BigIntegerCrypto P2048 = new BigIntegerCrypto("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3DC2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB9ED529077096966D670C354E4ABC9804F1746C08CA18217C32905E462E36CE3BE39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9DE2BCBF6955817183995497CEA956AE515D2261898FA051015728E5A8AACAA68FFFFFFFFFFFFFFFF", 16);
   public static final BigIntegerCrypto P2048MinusOne;
   public static final BigIntegerCrypto P3072 = new BigIntegerCrypto("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3DC2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB9ED529077096966D670C354E4ABC9804F1746C08CA18217C32905E462E36CE3BE39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9DE2BCBF6955817183995497CEA956AE515D2261898FA051015728E5A8AAAC42DAD33170D04507A33A85521ABDF1CBA64ECFB850458DBEF0A8AEA71575D060C7DB3970F85A6E1E4C7ABF5AE8CDB0933D71E8C94E04A25619DCEE3D2261AD2EE6BF12FFA06D98A0864D87602733EC86A64521F2B18177B200CBBE117577A615D6C770988C0BAD946E208E24FA074E5AB3143DB5BFCE0FD108E4B82D120A93AD2CAFFFFFFFFFFFFFFFF", 16);
   public static final BigIntegerCrypto P3072MinusOne;
   public static final byte[] PingAckMsg = new byte[]{80, 105, 110, 103, 65, 67, 75, 32};
   public static final byte[] PingMsg = new byte[]{80, 105, 110, 103, 32, 32, 32, 32};
   public static final byte[] RelayAckMsg = new byte[]{82, 101, 108, 97, 121, 65, 67, 75};
   public static final byte[] SASRelayMsg = new byte[]{83, 65, 83, 114, 101, 108, 97, 121};
   public static final int SHA256_DIGEST_LENGTH = 32;
   public static final String TWO_128 = "TWO-CM-128";
   public static final String TWO_256 = "TWO-CM-256";
   public static final byte[] aes1 = new byte[]{65, 69, 83, 49};
   public static final byte[] aes3 = new byte[]{65, 69, 83, 51};
   public static final byte[] b256 = new byte[]{66, 50, 53, 54};
   public static final byte[] b32 = new byte[]{66, 51, 50, 32};
   public static final byte[] b32e = new byte[]{66, 51, 50, 69};
   public static final String clientId = "GNU ZRTP4J 4.1.0";
   public static final byte[] dh2k = new byte[]{68, 72, 50, 107};
   public static final byte[] dh3k = new byte[]{68, 72, 51, 107};
   public static final byte[] e255 = new byte[]{69, 50, 53, 53};
   public static final byte[] ec25 = new byte[]{69, 67, 50, 53};
   public static final byte[] ec38 = new byte[]{69, 67, 51, 56};
   public static final byte[] hs32 = new byte[]{72, 83, 51, 50};
   public static final byte[] hs80 = new byte[]{72, 83, 56, 48};
   public static final byte[] iniHmacKey = new byte[]{73, 110, 105, 116, 105, 97, 116, 111, 114, 32, 72, 77, 65, 67, 32, 107, 101, 121, 0};
   public static final byte[] iniMasterKey = new byte[]{73, 110, 105, 116, 105, 97, 116, 111, 114, 32, 83, 82, 84, 80, 32, 109, 97, 115, 116, 101, 114, 32, 107, 101, 121, 0};
   public static final byte[] iniMasterSalt = new byte[]{73, 110, 105, 116, 105, 97, 116, 111, 114, 32, 83, 82, 84, 80, 32, 109, 97, 115, 116, 101, 114, 32, 115, 97, 108, 116, 0};
   public static final byte[] iniZrtpKey = new byte[]{73, 110, 105, 116, 105, 97, 116, 111, 114, 32, 90, 82, 84, 80, 32, 107, 101, 121, 0};
   public static final byte[] initiator = new byte[]{73, 110, 105, 116, 105, 97, 116, 111, 114};
   public static final byte[] mult = new byte[]{77, 117, 108, 116};
   public static final byte[] respHmacKey = new byte[]{82, 101, 115, 112, 111, 110, 100, 101, 114, 32, 72, 77, 65, 67, 32, 107, 101, 121, 0};
   public static final byte[] respMasterKey = new byte[]{82, 101, 115, 112, 111, 110, 100, 101, 114, 32, 83, 82, 84, 80, 32, 109, 97, 115, 116, 101, 114, 32, 107, 101, 121, 0};
   public static final byte[] respMasterSalt = new byte[]{82, 101, 115, 112, 111, 110, 100, 101, 114, 32, 83, 82, 84, 80, 32, 109, 97, 115, 116, 101, 114, 32, 115, 97, 108, 116, 0};
   public static final byte[] respZrtpKey = new byte[]{82, 101, 115, 112, 111, 110, 100, 101, 114, 32, 90, 82, 84, 80, 32, 107, 101, 121, 0};
   public static final byte[] responder = new byte[]{82, 101, 115, 112, 111, 110, 100, 101, 114};
   public static final byte[] retainedSec = new byte[]{114, 101, 116, 97, 105, 110, 101, 100, 32, 115, 101, 99, 114, 101, 116, 0};
   public static final byte[] s256 = new byte[]{83, 50, 53, 54};
   public static final byte[] s384 = new byte[]{83, 51, 56, 52};
   public static final String[] sas256WordsEven;
   public static final String[] sas256WordsOdd;
   public static final byte[] sasString = new byte[]{83, 65, 83, 0};
   public static final byte[] sk32 = new byte[]{83, 75, 51, 50};
   public static final byte[] sk64 = new byte[]{83, 75, 54, 52};
   public static final DHParameters specDh2k;
   public static final DHParameters specDh3k;
   public static final BigIntegerCrypto two = BigIntegerCrypto.valueOf(2L);
   public static final byte[] two1 = new byte[]{50, 70, 83, 49};
   public static final byte[] two3 = new byte[]{50, 70, 83, 51};
   public static final X9ECParameters x9Ec25 = SECNamedCurves.getByName("secp256r1");
   public static final X9ECParameters x9Ec38 = SECNamedCurves.getByName("secp384r1");
   public static final byte[] zrtpMsk = new byte[]{90, 82, 84, 80, 32, 77, 83, 75, 0};
   public static final byte[] zrtpSessionKey = new byte[]{90, 82, 84, 80, 32, 83, 101, 115, 115, 105, 111, 110, 32, 75, 101, 121, 0};
   public static final byte[] zrtpTrustedMitm = new byte[]{84, 114, 117, 115, 116, 101, 100, 32, 77, 105, 84, 77, 32, 107, 101, 121, 0};
   public static final byte[] zrtpVersion_11 = new byte[]{49, 46, 49, 48};
   public static final byte[] zrtpVersion_12 = new byte[]{49, 46, 50, 48};

   static {
      P2048MinusOne = P2048.subtract(BigIntegerCrypto.ONE);
      P3072MinusOne = P3072.subtract(BigIntegerCrypto.ONE);
      specDh2k = new DHParameters(P2048, two, (BigIntegerCrypto)null, 256);
      specDh3k = new DHParameters(P3072, two, (BigIntegerCrypto)null, 512);
      sas256WordsOdd = new String[]{"adroitness", "adviser", "aftermath", "aggregate", "alkali", "almighty", "amulet", "amusement", "antenna", "applicant", "Apollo", "armistice", "article", "asteroid", "Atlantic", "atmosphere", "autopsy", "Babylon", "backwater", "barbecue", "belowground", "bifocals", "bodyguard", "bookseller", "borderline", "bottomless", "Bradbury", "bravado", "Brazilian", "breakaway", "Burlington", "businessman", "butterfat", "Camelot", "candidate", "cannonball", "Capricorn", "caravan", "caretaker", "celebrate", "cellulose", "certify", "chambermaid", "Cherokee", "Chicago", "clergyman", "coherence", "combustion", "commando", "company", "component", "concurrent", "confidence", "conformist", "congregate", "consensus", "consulting", "corporate", "corrosion", "councilman", "crossover", "crucifix", "cumbersome", "customer", "Dakota", "decadence", "December", "decimal", "designing", "detector", "detergent", "determine", "dictator", "dinosaur", "direction", "disable", "disbelief", "disruptive", "distortion", "document", "embezzle", "enchanting", "enrollment", "enterprise", "equation", "equipment", "escapade", "Eskimo", "everyday", "examine", "existence", "exodus", "fascinate", "filament", "finicky", "forever", "fortitude", "frequency", "gadgetry", "Galveston", "getaway", "glossary", "gossamer", "graduate", "gravity", "guitarist", "hamburger", "Hamilton", "handiwork", "hazardous", "headwaters", "hemisphere", "hesitate", "hideaway", "holiness", "hurricane", "hydraulic", "impartial", "impetus", "inception", "indigo", "inertia", "infancy", "inferno", "informant", "insincere", "insurgent", "integrate", "intention", "inventive", "Istanbul", "Jamaica", "Jupiter", "leprosy", "letterhead", "liberty", "maritime", "matchmaker", "maverick", "Medusa", "megaton", "microscope", "microwave", "midsummer", "millionaire", "miracle", "misnomer", "molasses", "molecule", "Montana", "monument", "mosquito", "narrative", "nebula", "newsletter", "Norwegian", "October", "Ohio", "onlooker", "opulent", "Orlando", "outfielder", "Pacific", "pandemic", "Pandora", "paperweight", "paragon", "paragraph", "paramount", "passenger", "pedigree", "Pegasus", "penetrate", "perceptive", "performance", "pharmacy", "phonetic", "photograph", "pioneer", "pocketful", "politeness", "positive", "potato", "processor", "provincial", "proximate", "puberty", "publisher", "pyramid", "quantity", "racketeer", "rebellion", "recipe", "recover", "repellent", "replica", "reproduce", "resistor", "responsive", "retraction", "retrieval", "retrospect", "revenue", "revival", "revolver", "sandalwood", "sardonic", "Saturday", "savagery", "scavenger", "sensation", "sociable", "souvenir", "specialist", "speculate", "stethoscope", "stupendous", "supportive", "surrender", "suspicious", "sympathy", "tambourine", "telephone", "therapist", "tobacco", "tolerance", "tomorrow", "torpedo", "tradition", "travesty", "trombonist", "truncated", "typewriter", "ultimate", "undaunted", "underfoot", "unicorn", "unify", "universe", "unravel", "upcoming", "vacancy", "vagabond", "vertigo", "Virginia", "visitor", "vocalist", "voyager", "warranty", "Waterloo", "whimsical", "Wichita", "Wilmington", "Wyoming", "yesteryear", "Yucatan"};
      sas256WordsEven = new String[]{"aardvark", "absurd", "accrue", "acme", "adrift", "adult", "afflict", "ahead", "aimless", "Algol", "allow", "alone", "ammo", "ancient", "apple", "artist", "assume", "Athens", "atlas", "Aztec", "baboon", "backfield", "backward", "banjo", "beaming", "bedlamp", "beehive", "beeswax", "befriend", "Belfast", "berserk", "billiard", "bison", "blackjack", "blockade", "blowtorch", "bluebird", "bombast", "bookshelf", "brackish", "breadline", "breakup", "brickyard", "briefcase", "Burbank", "button", "buzzard", "cement", "chairlift", "chatter", "checkup", "chisel", "choking", "chopper", "Christmas", "clamshell", "classic", "classroom", "cleanup", "clockwork", "cobra", "commence", "concert", "cowbell", "crackdown", "cranky", "crowfoot", "crucial", "crumpled", "crusade", "cubic", "dashboard", "deadbolt", "deckhand", "dogsled", "dragnet", "drainage", "dreadful", "drifter", "dropper", "drumbeat", "drunken", "Dupont", "dwelling", "eating", "edict", "egghead", "eightball", "endorse", "endow", "enlist", "erase", "escape", "exceed", "eyeglass", "eyetooth", "facial", "fallout", "flagpole", "flatfoot", "flytrap", "fracture", "framework", "freedom", "frighten", "gazelle", "Geiger", "glitter", "glucose", "goggles", "goldfish", "gremlin", "guidance", "hamlet", "highchair", "hockey", "indoors", "indulge", "inverse", "involve", "island", "jawbone", "keyboard", "kickoff", "kiwi", "klaxon", "locale", "lockup", "merit", "minnow", "miser", "Mohawk", "mural", "music", "necklace", "Neptune", "newborn", "nightbird", "Oakland", "obtuse", "offload", "optic", "orca", "payday", "peachy", "pheasant", "physique", "playhouse", "Pluto", "preclude", "prefer", "preshrunk", "printer", "prowler", "pupil", "puppy", "python", "quadrant", "quiver", "quota", "ragtime", "ratchet", "rebirth", "reform", "regain", "reindeer", "rematch", "repay", "retouch", "revenge", "reward", "rhythm", "ribcage", "ringbolt", "robust", "rocker", "ruffled", "sailboat", "sawdust", "scallion", "scenic", "scorecard", "Scotland", "seabird", "select", "sentence", "shadow", "shamrock", "showgirl", "skullcap", "skydive", "slingshot", "slowdown", "snapline", "snapshot", "snowcap", "snowslide", "solo", "southward", "soybean", "spaniel", "spearhead", "spellbind", "spheroid", "spigot", "spindle", "spyglass", "stagehand", "stagnate", "stairway", "standard", "stapler", "steamship", "sterling", "stockman", "stopwatch", "stormy", "sugar", "surmount", "suspense", "sweatband", "swelter", "tactics", "talon", "tapeworm", "tempest", "tiger", "tissue", "tonic", "topmost", "tracker", "transit", "trauma", "treadmill", "Trojan", "trouble", "tumor", "tunnel", "tycoon", "uncut", "unearth", "unwind", "uproot", "upset", "upshot", "vapor", "village", "virus", "Vulcan", "waffle", "wallet", "watchword", "wayside", "willow", "woodlark", "Zulu"};
   }

   public static enum SupportedAuthAlgos {
      // $FF: renamed from: HS gnu.java.zrtp.ZrtpConstants$SupportedAuthAlgos
      field_200,
      // $FF: renamed from: SK gnu.java.zrtp.ZrtpConstants$SupportedAuthAlgos
      field_201;

      static {
         ZrtpConstants.SupportedAuthAlgos var0 = new ZrtpConstants.SupportedAuthAlgos("SK", 1);
         field_201 = var0;
      }
   }

   public static enum SupportedAuthLengths {
      HS32(ZrtpConstants.hs32, ZrtpConstants.SupportedAuthAlgos.field_200, 32),
      HS80,
      SK32(ZrtpConstants.sk32, ZrtpConstants.SupportedAuthAlgos.field_201, 32),
      SK64(ZrtpConstants.sk64, ZrtpConstants.SupportedAuthAlgos.field_201, 64);

      public final ZrtpConstants.SupportedAuthAlgos algo;
      public final int length;
      public final byte[] name;

      static {
         ZrtpConstants.SupportedAuthLengths var0 = new ZrtpConstants.SupportedAuthLengths("HS80", 3, ZrtpConstants.hs80, ZrtpConstants.SupportedAuthAlgos.field_200, 80);
         HS80 = var0;
      }

      private SupportedAuthLengths(byte[] var3, ZrtpConstants.SupportedAuthAlgos var4, int var5) {
         this.name = var3;
         this.algo = var4;
         this.length = var5;
      }
   }

   public static enum SupportedHashes {
      S256(ZrtpConstants.s256),
      S384;

      public byte[] name;

      static {
         ZrtpConstants.SupportedHashes var0 = new ZrtpConstants.SupportedHashes("S384", 1, ZrtpConstants.s384);
         S384 = var0;
      }

      private SupportedHashes(byte[] var3) {
         this.name = var3;
      }
   }

   public static enum SupportedPubKeys {
      DH2K(ZrtpConstants.dh2k, 256, new DHKeyGenerationParameters(ZrtpFortuna.getInstance(), ZrtpConstants.specDh2k)),
      DH3K(ZrtpConstants.dh3k, 384, new DHKeyGenerationParameters(ZrtpFortuna.getInstance(), ZrtpConstants.specDh3k)),
      E255(ZrtpConstants.e255, 32, new Djb25519KeyGenerationParameters(ZrtpFortuna.getInstance())),
      EC25(ZrtpConstants.ec25, 64, new ECKeyGenerationParameters(new ECDomainParameters(ZrtpConstants.x9Ec25.getCurve(), ZrtpConstants.x9Ec25.getG(), ZrtpConstants.x9Ec25.getN(), ZrtpConstants.x9Ec25.getH(), ZrtpConstants.x9Ec25.getSeed()), ZrtpFortuna.getInstance())),
      EC38(ZrtpConstants.ec38, 96, new ECKeyGenerationParameters(new ECDomainParameters(ZrtpConstants.x9Ec38.getCurve(), ZrtpConstants.x9Ec38.getG(), ZrtpConstants.x9Ec38.getN(), ZrtpConstants.x9Ec38.getH(), ZrtpConstants.x9Ec38.getSeed()), ZrtpFortuna.getInstance())),
      MULT;

      public final ECCurve curve;
      public final BasicAgreement dhContext;
      public final AsymmetricCipherKeyPairGenerator keyPairGen;
      public byte[] name;
      public final int pubKeySize;
      public final DHParameters specDh;

      static {
         ZrtpConstants.SupportedPubKeys var0 = new ZrtpConstants.SupportedPubKeys("MULT", 5, ZrtpConstants.mult);
         MULT = var0;
      }

      private SupportedPubKeys(byte[] var3) {
         this.name = var3;
         this.pubKeySize = 0;
         this.keyPairGen = null;
         this.specDh = null;
         this.dhContext = null;
         this.curve = null;
      }

      private SupportedPubKeys(byte[] var3, int var4, DHKeyGenerationParameters var5) {
         this.name = var3;
         this.pubKeySize = var4;
         if (var5 != null) {
            DHBasicKeyPairGenerator var6 = new DHBasicKeyPairGenerator();
            this.keyPairGen = var6;
            var6.init(var5);
            this.specDh = var5.getParameters();
            this.dhContext = new DHBasicAgreement();
         } else {
            this.keyPairGen = null;
            this.specDh = null;
            this.dhContext = null;
         }

         this.curve = null;
      }

      private SupportedPubKeys(byte[] var3, int var4, Djb25519KeyGenerationParameters var5) {
         this.name = var3;
         this.pubKeySize = var4;
         Djb25519KeyPairGenerator var6 = new Djb25519KeyPairGenerator();
         this.keyPairGen = var6;
         var6.init(var5);
         this.dhContext = new Djb25519DHBasicAgreement();
         this.curve = null;
         this.specDh = null;
      }

      private SupportedPubKeys(byte[] var3, int var4, ECKeyGenerationParameters var5) {
         this.name = var3;
         this.pubKeySize = var4;
         if (var5 != null) {
            ECKeyPairGenerator var6 = new ECKeyPairGenerator();
            this.keyPairGen = var6;
            var6.init(var5);
            this.curve = var5.getDomainParameters().getCurve();
            this.dhContext = new ECDHBasicAgreement();
         } else {
            this.curve = null;
            this.keyPairGen = null;
            this.dhContext = null;
         }

         this.specDh = null;
      }
   }

   public static enum SupportedSASTypes {
      B256(ZrtpConstants.b256),
      B32(ZrtpConstants.b32),
      B32E;

      public byte[] name;

      static {
         ZrtpConstants.SupportedSASTypes var0 = new ZrtpConstants.SupportedSASTypes("B32E", 2, ZrtpConstants.b32e);
         B32E = var0;
      }

      private SupportedSASTypes(byte[] var3) {
         this.name = var3;
      }
   }

   public static enum SupportedSymAlgos {
      AES,
      TwoFish;

      static {
         ZrtpConstants.SupportedSymAlgos var0 = new ZrtpConstants.SupportedSymAlgos("TwoFish", 1);
         TwoFish = var0;
      }
   }

   public static enum SupportedSymCiphers {
      AES1(ZrtpConstants.aes1, 16, "AES-CM-128", new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 128)), ZrtpConstants.SupportedSymAlgos.AES),
      AES3(ZrtpConstants.aes3, 32, "AES-CM-256", new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 128)), ZrtpConstants.SupportedSymAlgos.AES),
      TWO1,
      TWO3(ZrtpConstants.two3, 32, "TWO-CM-256", new BufferedBlockCipher(new CFBBlockCipher(new TwofishEngine(), 128)), ZrtpConstants.SupportedSymAlgos.TwoFish);

      public final ZrtpConstants.SupportedSymAlgos algo;
      public final BufferedBlockCipher cipher;
      public final int keyLength;
      public final byte[] name;
      public final String readable;

      static {
         ZrtpConstants.SupportedSymCiphers var0 = new ZrtpConstants.SupportedSymCiphers("TWO1", 3, ZrtpConstants.two1, 16, "TWO-CM-128", new BufferedBlockCipher(new CFBBlockCipher(new TwofishEngine(), 128)), ZrtpConstants.SupportedSymAlgos.TwoFish);
         TWO1 = var0;
      }

      private SupportedSymCiphers(byte[] var3, int var4, String var5, BufferedBlockCipher var6, ZrtpConstants.SupportedSymAlgos var7) {
         this.name = var3;
         this.keyLength = var4;
         this.readable = var5;
         this.cipher = var6;
         this.algo = var7;
      }
   }
}
