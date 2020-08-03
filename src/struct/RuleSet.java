package struct;

import util.StringSplitter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import struct.Rule;

public class RuleSet {
	private int iNumberOfRules;
    private ArrayList<Rule> lstRules;
    
	public int len() {
		return iNumberOfRules;
	}
    
    public void load(String fnInput) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(fnInput));
        lstRules = new ArrayList<Rule>();
        String line = "";
        while((line = reader.readLine()) != null){
            String[] tokens = line.split("\t");
            Rule rule = new Rule();
            String ruleString = tokens[0];
            rule.conf = Double.parseDouble(tokens[1]);
            if (tokens.length == 3)
            	rule.type = Integer.parseInt(tokens[2]);
            String[] ruleStrings = ruleString.split(",");
            rule.add(new Relation(Integer.parseInt(ruleStrings[ruleStrings.length-1]),1));//The head relation
            for(int i = 0; i < ruleStrings.length - 1; i++){
                int rel = Integer.parseInt(ruleStrings[i]);
                int dir = 1;
                if(rel < 0){
                    rel = -1 * rel;
                    dir = -1;
                }
                rule.add(new Relation(rel, dir));
            }
            rule.len = rule.relations().size();
            lstRules.add(rule);
        }
        
        iNumberOfRules = lstRules.size();
        reader.close();
    }
    
    public ArrayList<Rule> rules(){
        return lstRules;
    }
    
	public Rule get(int iID) throws Exception {
		if (iID < 0 || iID >= iNumberOfRules) {
			throw new Exception("getRule error in RuleSet: ID out of range");
		}
		return lstRules.get(iID);
	}
    
	public void randomShuffle() {
		TreeMap<Double, Rule> tmpMap = new TreeMap<Double, Rule>();
		for (int iID = 0; iID < iNumberOfRules; iID++) {
			ArrayList<Relation> lstRelations = lstRules.get(iID).relations();
			Rule rule = new Rule();
			for(Relation rel:lstRelations) {
				rule.add(new Relation(rel.rid(), rel.direction()));
			}
			rule.conf = lstRules.get(iID).confidence();
			rule.len = lstRules.get(iID).len();
			tmpMap.put(Math.random(), rule);
		}
		
		lstRules = new ArrayList<Rule>();
		Iterator<Double> iterValues = tmpMap.keySet().iterator();
		while (iterValues.hasNext()) {		
			double dRand = iterValues.next();
			Rule rule= tmpMap.get(dRand);
			Rule nRule = new Rule();
			ArrayList<Relation> lstRelations = rule.relations();
			for(Relation rel:lstRelations) {
				nRule.add(new Relation(rel.rid(), rel.direction()));
			}
			nRule.conf = rule.confidence();
			nRule.len = rule.len();
			lstRules.add(nRule);
		}
		iNumberOfRules = lstRules.size();
		tmpMap.clear();
	}
}
