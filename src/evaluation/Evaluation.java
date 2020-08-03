package evaluation;

import struct.Matrix;
import struct.TripleDict;
import struct.TripleSet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Evaluation {
	public TripleSet lstTestTriples;
	public HashMap<String, Boolean> lstAllTriples;
	public Matrix Real_MatrixE;
	public Matrix Real_MatrixR;
	public Matrix Imag_MatrixE;
	public Matrix Imag_MatrixR;
	public double dMRR;
	public double dMeanRank;
	public double dMedian;
	public double dHits1;
	public double dHits3;
	public double dHits5;
	public double dHits10;
	
	public Evaluation(
			int m_NumEntity, int m_NumRelation, int m_NumFactor,
			String fnTestTriples,
			String fnAllTriples,
			String fnRealMatrixE,
			String fnRealMatrixR,
			String fnImagMatrixE,
			String fnImagMatrixR
			) throws Exception {
		Real_MatrixE = new Matrix(m_NumEntity, m_NumFactor);
		Real_MatrixR = new Matrix(m_NumRelation, m_NumFactor);
		Imag_MatrixE = new Matrix(m_NumEntity, m_NumFactor);
		Imag_MatrixR = new Matrix(m_NumRelation, m_NumFactor);
		Real_MatrixE.load(fnRealMatrixE);
		Real_MatrixR.load(fnRealMatrixR);
		Imag_MatrixE.load(fnImagMatrixE);
		Imag_MatrixR.load(fnImagMatrixR);
		lstTestTriples = new TripleSet(m_NumEntity, m_NumRelation);
		lstTestTriples.load(fnTestTriples, -1);
		
		TripleDict lstDicAllTriples = new TripleDict();	
		lstDicAllTriples.load(fnAllTriples);
		lstAllTriples = lstDicAllTriples.tripleDict();	
		
	}
	
	public void calculateMetrics(String fnOutput) throws Exception {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fnOutput), "UTF-8"));
		
		int iNumberOfEntities = Real_MatrixE.rows();
		int iNumberOfFactors = Real_MatrixE.columns();
		List<Double> iList = new ArrayList<Double>();
				
		int iCnt = 0;
		double avgMRR = 0.0;
		double avgMeanRank = 0.0;
		int avgHits1=0, avgHits3=0, avgHits5=0, avgHits10 = 0;
		for (int iID = 0; iID < lstTestTriples.triples(); iID++) {
			int iRelation = lstTestTriples.get(iID).relation();
			int iHead = lstTestTriples.get(iID).head();
			int iTail = lstTestTriples.get(iID).tail();
			double dTargetValue = 0.0;
			for (int p = 0; p < iNumberOfFactors; p++) {
				dTargetValue += Real_MatrixE.get(iHead, p) * Real_MatrixR.get(iRelation, p) * Real_MatrixE.get(iTail, p)
						      - Imag_MatrixE.get(iHead, p) * Imag_MatrixR.get(iRelation, p) * Real_MatrixE.get(iTail, p)
						      + Real_MatrixE.get(iHead, p) * Imag_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iTail, p)
						      + Imag_MatrixE.get(iHead, p) * Real_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iTail, p);
			}
			
			int iLeftRank = 1;
			int iLeftIdentical = 0;
			for (int iLeftID = 0; iLeftID < iNumberOfEntities; iLeftID++) {
				double dValue = 0.0;
				String negTiple = iLeftID + "\t" + iRelation + "\t" +iTail;
				if(!lstAllTriples.containsKey(negTiple)){
					for (int p = 0; p < iNumberOfFactors; p++) {
						dValue += Real_MatrixE.get(iLeftID, p) * Real_MatrixR.get(iRelation, p) * Real_MatrixE.get(iTail, p)
							    - Imag_MatrixE.get(iLeftID, p) * Imag_MatrixR.get(iRelation, p) * Real_MatrixE.get(iTail, p)
							    + Real_MatrixE.get(iLeftID, p) * Imag_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iTail, p)
							    + Imag_MatrixE.get(iLeftID, p) * Real_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iTail, p);
					}
					if (dValue > dTargetValue) {
						iLeftRank++;
					}
					if (dValue == dTargetValue) {
						iLeftIdentical++;
					}
				}
			}
			double dLeftRank = (double)(2.0 * iLeftRank + iLeftIdentical) / 2.0;
			int iLeftHits1=0, iLeftHits3=0, iLeftHits5=0, iLeftHits10 = 0;
			if (dLeftRank <= 1.0) {
				iLeftHits1 = 1;
			}
			if (dLeftRank <= 3.0) {
				iLeftHits3 = 1;
			}
			if (dLeftRank <= 5.0) {
				iLeftHits5 = 1;
			}
			if (dLeftRank <= 10.0) {
				iLeftHits10 = 1;
			}
			avgMRR += 1.0/(double)dLeftRank;
			avgMeanRank += dLeftRank;
			avgHits1 += iLeftHits1;
			avgHits3 += iLeftHits3;
			avgHits5 += iLeftHits5;
			avgHits10 += iLeftHits10;
			iList.add(dLeftRank);
			iCnt++;
			writer.write("Left" + iHead + " " + iRelation + " " +iTail 
					+ "\t" + 1.0/(double)dLeftRank + "\t"  + iLeftHits1 + "\t"  + iLeftHits3 + "\t"  + iLeftHits10 + "\n" );
			
			int iRightRank = 1;
			int iRightIdentical = 0;
			for (int iRightID = 0; iRightID < iNumberOfEntities; iRightID++) {
				double dValue = 0.0;
				String negTiple = iHead + "\t" + iRelation + "\t" + iRightID;
				if(!lstAllTriples.containsKey(negTiple)){
					for (int p = 0; p < iNumberOfFactors; p++) {
						dValue += Real_MatrixE.get(iHead, p) * Real_MatrixR.get(iRelation, p) * Real_MatrixE.get(iRightID, p)
							    - Imag_MatrixE.get(iHead, p) * Imag_MatrixR.get(iRelation, p) * Real_MatrixE.get(iRightID, p)
							    + Real_MatrixE.get(iHead, p) * Imag_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iRightID, p)
							    + Imag_MatrixE.get(iHead, p) * Real_MatrixR.get(iRelation, p) * Imag_MatrixE.get(iRightID, p);
					}
					if (dValue > dTargetValue) {
						iRightRank++;
					}
					if (dValue == dTargetValue) {
						iRightIdentical++;
					}
				}
			}
			double dRightRank = (double)(2.0 * iRightRank + iRightIdentical) / 2.0;
			int iRightHits1=0, iRightHits3=0, iRightHits5=0, iRightHits10 = 0;
			if (dRightRank <= 1.0) {
				iRightHits1 = 1;
			}
			if (dRightRank <= 3.0) {
				iRightHits3 = 1;
			}
			if (dRightRank <= 5.0) {
				iRightHits5 = 1;
			}
			if (dRightRank <= 10.0) {
				iRightHits10 = 1;
			}
			avgMRR += 1.0/(double)dRightRank;
			avgMeanRank += dRightRank;
			avgHits1 += iRightHits1;
			avgHits3 += iRightHits3;
			avgHits5 += iRightHits5;
			avgHits10 += iRightHits10;
			iList.add(dRightRank);
			iCnt++;
			writer.write("Righ" + iHead + " " + iRelation + " " +iTail 
					+ "\t" + 1.0/(double)dRightRank + "\t"  + iRightHits1 + "\t"  + iRightHits3 + "\t"  + iRightHits10 + "\n" );
			writer.flush();
		}
		
		writer.close();
		dMRR = avgMRR / (double)iCnt;
		dMeanRank = avgMeanRank / (double)iCnt;
		dHits1 = (double)avgHits1 / (double)iCnt;
		dHits3 = (double)avgHits3 / (double)iCnt;
		dHits5 = (double)avgHits5 / (double)iCnt;
		dHits10 = (double)avgHits10 / (double)iCnt;
		

		System.out.println("################################################\n");
		System.out.println("MRR:\t" + dMRR + "\n");
		System.out.println("MeanRank:\t" + dMeanRank + "\n");
		System.out.println("Median:\t" + dMedian + "\n");
		System.out.println("Hits@1:\t" + dHits1 + "\n");
		System.out.println("Hits@3:\t" + dHits3 + "\n");
		System.out.println("Hits@5:\t" + dHits5 + "\n");
		System.out.println("Hits@10:\t" + dHits10 + "\n");
		Collections.sort(iList);
		int indx=iList.size()/2;
		if (iList.size()%2==0) {
			dMedian = (iList.get(indx-1)+iList.get(indx))/2.0;
		}
		else {
			dMedian = iList.get(indx);
		}
	}
	
	public static void main(String[] args) throws Exception {
		int NumEntity = Integer.parseInt(args[0]);
		int NumRelation = Integer.parseInt(args[1]);
		int NumFactor = Integer.parseInt(args[2]); 
		String fnTestTriple = args[3];
		String fnAllTriple = args[4];
		String fnRealMatrixE = args[5];
		String fnRealMatrixR = args[6];
		String fnImagMatrixE = args[7];
		String fnImagMatrixR = args[8];

		String fnOutput = args[9];
		
		Evaluation evaluation = new Evaluation(
				NumEntity, NumRelation, NumFactor, 
				fnTestTriple, fnAllTriple, fnRealMatrixE, fnRealMatrixR, fnImagMatrixE, fnImagMatrixR);
		evaluation.calculateMetrics(fnOutput);
	}
}
