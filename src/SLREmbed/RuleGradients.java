package SLREmbed;
import struct.Matrix;
import struct.Relation;
import struct.Rule;

import java.util.ArrayList;

public class RuleGradients {
    public Rule dRule;
    public Matrix Real_MatrixR;
    public Matrix Imag_MatrixR;
    public Matrix Real_MatrixRGradient;
    public Matrix Imag_MatrixRGradient;
    public double dMu;

    public RuleGradients(
            Rule inRule,
            Matrix inReal_MatrixR,
            Matrix inImag_MatrixR,
            Matrix inReal_MatrixRGradient,
            Matrix inImag_MatrixRGradient,
            double inMu){
        dRule = inRule;
        Real_MatrixR = inReal_MatrixR;
        Imag_MatrixR = inImag_MatrixR;
        Real_MatrixRGradient = inReal_MatrixRGradient;
        Imag_MatrixRGradient = inImag_MatrixRGradient;
        dMu = inMu;
    }

    public void calculateGradients(double dCon) throws Exception{            	
        double confidence = dRule.confidence();
        if (dRule.len() == 2) {
            Relation ruleHead = dRule.relations().get(0);
            Relation ruleBody = dRule.relations().get(1);
            int numOfFactors = Real_MatrixR.columns();
			
            for(int p = 0; p < numOfFactors; p++){
                double real_body = Real_MatrixR.get(ruleBody.rid(), p);
                double imag_body = Imag_MatrixR.get(ruleBody.rid(), p) * ruleBody.direction();
                double real_head = Real_MatrixR.get(ruleHead.rid(), p);
                double imag_head = Imag_MatrixR.get(ruleHead.rid(), p);
                if(real_body - real_head > 0.0){
                    //Calculate gradients of head
                    Real_MatrixRGradient.add(ruleHead.rid(), p, -1 * dMu * confidence);
                    //Calculate gradients of body
                    Real_MatrixRGradient.add(ruleBody.rid(), p, 1 * dMu * confidence);
                }
                //Calculate gradinets of head
                Imag_MatrixRGradient.add(ruleHead.rid(), p, - 2 * (imag_body - imag_head) * dMu * confidence);
                //Calculate gradients of body
                Imag_MatrixRGradient.add(ruleBody.rid(), p, 2 * (imag_body - imag_head) * ruleBody.direction() * dMu * confidence);
            }
        }
        else {
            Relation ruleHead = dRule.relations().get(0);
            Relation ruleBody1 = dRule.relations().get(1);
            Relation ruleBody2 = dRule.relations().get(2);
            int numOfFactors = Real_MatrixR.columns();
            for(int p = 0; p < numOfFactors; p++){
                double real_body1 = Real_MatrixR.get(ruleBody1.rid(), p);
                double imag_body1 = Imag_MatrixR.get(ruleBody1.rid(), p) * ruleBody1.direction();
                double real_body2 = Real_MatrixR.get(ruleBody2.rid(), p);
                double imag_body2 = Imag_MatrixR.get(ruleBody2.rid(), p) * ruleBody2.direction();
                double real_head = Real_MatrixR.get(ruleHead.rid(), p);
                double imag_head = Imag_MatrixR.get(ruleHead.rid(), p);
                double iBody = 0.0;
                iBody = real_body1 * real_body2 - imag_body1 * imag_body2;
                if( iBody - dCon * real_head > 0.0){                  
                    //Calculate gradients of head
                    Real_MatrixRGradient.add(ruleHead.rid(), p, -1 * dCon * dMu * confidence);
                    //Calculate gradients of body1 w.r.t real part
                    Real_MatrixRGradient.add(ruleBody1.rid(), p, 1 * real_body2 * dMu * confidence);
                    //Calculate gradients of body2 w.r.t real part
                    Real_MatrixRGradient.add(ruleBody2.rid(), p, 1 * real_body1 * dMu * confidence);
                    //Calculate gradients of body1 w.r.t imag part
                    Imag_MatrixRGradient.add(ruleBody1.rid(), p, -1 * imag_body2 * ruleBody1.direction() *  dMu * confidence);
                    //Calculate gradients of body2 w.r.t imag part
                    Imag_MatrixRGradient.add(ruleBody2.rid(), p, -1 * imag_body1 * ruleBody2.direction() * dMu * confidence);
                }
                iBody = real_body1 * imag_body2 + imag_body1 * real_body2;
                //Calculate gradinets of head
                Imag_MatrixRGradient.add(ruleHead.rid(), p, - 2 * (iBody - dCon * imag_head) * dCon * dMu * confidence);
                //Calculate gradients of body1 w.r.t real part
                Real_MatrixRGradient.add(ruleBody1.rid(), p, 2 * (iBody - dCon * imag_head) * imag_body2 * dMu * confidence);
                //Calculate gradients of body2 w.r.t real part
                Real_MatrixRGradient.add(ruleBody2.rid(), p, 2 * (iBody - dCon * imag_head) * imag_body1 * dMu * confidence);
                //Calculate gradients of body1 w.r.t imag part
                Imag_MatrixRGradient.add(ruleBody1.rid(), p, 2 * (iBody - dCon * imag_head) * real_body2 * ruleBody1.direction() *  dMu * confidence);
                //Calculate gradients of body2 w.r.t imag part
                Imag_MatrixRGradient.add(ruleBody2.rid(), p, 2 * (iBody - dCon * imag_head) * real_body1 * ruleBody2.direction() * dMu * confidence);
                              
            }
        }

    }
}
