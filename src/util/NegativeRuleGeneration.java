package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import struct.Relation;
import struct.Rule;
import struct.Triple;

public class NegativeRuleGeneration {
	public Rule PositiveRule;
	public int iNumberOfRelations;
	
	public NegativeRuleGeneration(Rule inPositiveRule,
			int inNumberOfRelations) {
		PositiveRule = inPositiveRule;
		iNumberOfRelations = inNumberOfRelations;
	}
	
	public HashSet<Rule> generateNegRule(int m_NumNeg) throws Exception {
		Relation ruleHead = PositiveRule.relations().get(0);
		int headRel = ruleHead.rid();		
		HashSet<Rule> NegativeTripleSet = new HashSet<Rule>();
		
		while (NegativeTripleSet.size() < m_NumNeg) {
			int iNegRelation = headRel;
			Rule NegativeRule = new Rule();
			Relation NegRuleHead = null;
			while (iNegRelation == headRel 
					|| iNegRelation == PositiveRule.relations().get(1).rid() 
					|| (PositiveRule.len()==3 && iNegRelation == PositiveRule.relations().get(2).rid())) {
				iNegRelation = (int)(Math.random() * iNumberOfRelations);
				NegRuleHead = new Relation(iNegRelation, 1);
			}
			NegativeRule.add(NegRuleHead);
			for (int i=1; i<PositiveRule.len(); i++) {
				NegativeRule.add(PositiveRule.relations().get(i));
			}
			NegativeRule.conf = PositiveRule.conf;
			NegativeRule.len = PositiveRule.len;
			NegativeTripleSet.add(NegativeRule);
		}
		return NegativeTripleSet;
		
	}
	
}
