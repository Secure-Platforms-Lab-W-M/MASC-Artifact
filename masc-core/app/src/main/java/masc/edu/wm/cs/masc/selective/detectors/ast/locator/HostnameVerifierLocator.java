package edu.wm.cs.mplus.detectors.ast.locator;

import edu.wm.cs.mplus.model.location.MutationLocation;

import java.util.ArrayList;
import java.util.List;

public class HostnameVerifierLocator implements Locator{
    @Override
    public List<MutationLocation> findExactLocations(List<MutationLocation> locations) {
        List<MutationLocation> exactMutationLocations = new ArrayList<MutationLocation>();

        for(MutationLocation loc : locations){

            try{
                MutationLocation newLoc = findExactLocation(loc);

//                if(newLoc != null){
                    exactMutationLocations.add(loc);
//                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return exactMutationLocations;
    }

    private MutationLocation findExactLocation(MutationLocation loc) {
        //Fix start column
        loc.setStartColumn(loc.getStartColumn()+1);

        return loc;
    }

}
