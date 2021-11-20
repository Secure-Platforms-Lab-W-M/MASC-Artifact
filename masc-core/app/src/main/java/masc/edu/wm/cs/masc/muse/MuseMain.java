package masc.edu.wm.cs.masc.muse;

public class MuseMain {

    public static void main(String[] args) throws Exception{
        if (args.length == 0){
            System.out.println("No properties file supplied");
        }
        else if (args.length > 1){
            System.out.println("Too many arguments were provided");
        }
        else if (!args[0].endsWith(".properties")){
            System.out.println("Properties file must end with the .properties extension");
        }
        else if (edu.wm.cs.muse.dataleak.support.Arguments.extractArguments(args[0]) < 0){
            System.out.println("Unable to extract arguments from properties file");
        }
        else{
            System.out.println("Let's run this thing");
            new edu.wm.cs.muse.Muse().runMuse(args);
        }
    }
}
