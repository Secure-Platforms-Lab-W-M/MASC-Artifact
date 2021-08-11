public class Supplier {
    String val;

    Supplier() {
        val = "";
       for(int i = 0; i<9; i++){
           val += (char)(65+i);
       }

    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return val.toString();
    }

    public byte[] toBytes() {
        return val.getBytes();
    }
}