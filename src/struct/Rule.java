package struct;

import java.util.ArrayList;

public class Rule {
    private ArrayList<Relation> lstRelations;
    public double conf;
    public int len;
    public int type = 0;
    public Rule(){
        lstRelations = new ArrayList<Relation>();
    }
    public double confidence(){
        return conf;
    }
    public ArrayList<Relation> relations(){
        return lstRelations;
    }
    public void add(Relation r){
        lstRelations.add(r);
    }
    public int len() {
    	return len;
    }
}
