package preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class ChangeDataString2ID {
	public HashMap<String, Integer> m_EntityIDMap = null;
	public HashMap<String, Integer> m_RelationIDMap = null;
	
	public ChangeDataString2ID() {
	}
	
	public void LoadIDMaps(String fnEntID, String fnRelID) throws Exception {
		m_EntityIDMap = new HashMap<String, Integer>();
		m_RelationIDMap = new HashMap<String, Integer>();
		BufferedReader ent = new BufferedReader(new InputStreamReader(
				new FileInputStream(fnEntID), "UTF-8"));
		BufferedReader rel = new BufferedReader(new InputStreamReader(
				new FileInputStream(fnRelID), "UTF-8"));
		
		String line = "";
		while ((line = ent.readLine()) != null) {
			String[] tokens = line.split("\t");
			Integer iID = Integer.parseInt(tokens[0]);
			m_EntityIDMap.put(tokens[1], iID);
		}
		while ((line = rel.readLine()) != null) {
			String[] tokens = line.split("\t");
			Integer iID = Integer.parseInt(tokens[0]);
			m_RelationIDMap.put(tokens[1], iID);
		}
		ent.close();
		rel.close();
	}
	
	public void ChangeFormat(String fnInput, String fnOutput) throws Exception {
		BufferedReader sr = new BufferedReader(new InputStreamReader(
				new FileInputStream(fnInput), "UTF-8"));
		BufferedWriter sw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fnOutput), "UTF-8"));
		
		String line = "";
		while ((line = sr.readLine()) != null) {
			String[] tokens = line.split("\t");
			int iHead = m_EntityIDMap.get(tokens[0]);
			int iTail = m_EntityIDMap.get(tokens[2]);
			int iRelation = m_RelationIDMap.get(tokens[1]);
			sw.write(iHead + "\t" + iRelation + "\t" + iTail + "\n");
		}
		sr.close();
		sw.close();
	}
	
	public static void main(String[] args) throws Exception {
		String fnEntID = "datasets\\FB15K\\EntityIDMap.tsv";
		String fnRelID = "datasets\\FB15K\\RelationIDMap.tsv";
		String fnInput = "datasets\\FB15K\\_train.txt";
		String fnOutput = "datasets\\FB15K\\train1.txt";
		ChangeDataString2ID converter = new ChangeDataString2ID();
		converter.LoadIDMaps(fnEntID, fnRelID);
		converter.ChangeFormat(fnInput, fnOutput);
	}
}
