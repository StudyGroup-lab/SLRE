package preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class ChangeRuleString2ID {
	public HashMap<String, Integer> m_EntityIDMap = null;
	public HashMap<String, Integer> m_RelationIDMap = null;
	
	public ChangeRuleString2ID() {
	}
	
	public void LoadIDMaps(String fnRelID) throws Exception {
		m_RelationIDMap = new HashMap<String, Integer>();
		BufferedReader rel = new BufferedReader(new InputStreamReader(
				new FileInputStream(fnRelID), "UTF-8"));
		
		String line = "";

		while ((line = rel.readLine()) != null) {
			String[] tokens = line.split("\t");
			Integer iID = Integer.parseInt(tokens[0]);
			m_RelationIDMap.put(tokens[1], iID);
		}
		rel.close();
	}
	
	public void ChangeFormat(String fnInput, String fnOutput) throws Exception {
		BufferedReader sr = new BufferedReader(new InputStreamReader(
				new FileInputStream(fnInput), "UTF-8"));
		BufferedWriter sw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fnOutput), "UTF-8"));
		
		String line = "";
		while ((line = sr.readLine()) != null) {
			String rule = "";
			String[] tokens = line.split("\t");
			String confidence = tokens[1];
//			String type = tokens[2];
			String[] rels = tokens[0].split(",");
			for(String s:rels){
				if(s.startsWith("-")) {
					int iRelation = m_RelationIDMap.get(s.substring(1));
					rule += "-" + iRelation + ",";
				}
				else {
					int iRelation = m_RelationIDMap.get(s);
					rule += iRelation + ",";
				}				
			}
			if(tokens.length == 2)
				sw.write(rule.substring(0, rule.length() - 1)+ "\t" + confidence + "\n");
			else
				sw.write(rule.substring(0, rule.length() - 1)+ "\t" + confidence + "\t" + tokens[2] + "\n");
		}
		sr.close();
		sw.close();
	}
	
	public static void main(String[] args) throws Exception {
		String fnRelID = "datasets\\FB15K\\RelationIDMap.tsv";
		String fnInput = "datasets\\FB15K\\RulePaths.txt";
		String fnOutput = "datasets\\FB15K\\RulePathsID1.txt";
		ChangeRuleString2ID converter = new ChangeRuleString2ID();
		converter.LoadIDMaps(fnRelID);
		converter.ChangeFormat(fnInput, fnOutput);
	}
}
