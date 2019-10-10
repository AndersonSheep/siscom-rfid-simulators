package control;
import java.util.ArrayList;
import java.util.List;

import estimators.EomLee;
import estimators.Estimator;
import estimators.LowerBound;
import general.Metrics;

public class Controller {
	
	private List<Estimator> est; 
	
	private int inicialNumberTags;
	private int incrementTagsBy;
	private int maxNumberTags;
	private int repetitionsEachNumberTags;
	private int inicialFrameSize;
	
	private Metrics metrics;
	
	public Controller (int iniNumTags, int incTagsBy,
		int maxNumTags, int numRepet, int iniFrameSize, int choosenEstimators) {
		
		this.inicialNumberTags = iniNumTags;
		this.incrementTagsBy = incTagsBy;
		this.maxNumberTags = maxNumTags;
		this.repetitionsEachNumberTags = numRepet;
		this.inicialFrameSize = iniFrameSize;
		
		this.metrics = new Metrics();
		
		this.est = new ArrayList<Estimator>();
		if(choosenEstimators==1) {
			this.est.add(new LowerBound(this.inicialNumberTags,this.inicialFrameSize));
		}else if(choosenEstimators==2) {
			this.est.add(new EomLee(this.inicialNumberTags,this.inicialFrameSize));
		}else {
			this.est.add(new LowerBound(this.inicialNumberTags,this.inicialFrameSize));
			this.est.add(new LowerBound(this.inicialNumberTags,this.inicialFrameSize));
		}
		
		
	}
	
	public Estimator resetEst(Estimator est, int iniNumTags,int iniFrameSize) {
		
		if(est instanceof LowerBound) {
			return new LowerBound(iniNumTags,iniFrameSize);
		}else if(est instanceof EomLee) {
			return new EomLee(iniNumTags,iniFrameSize);
		}
		return est;
		
	}
	
	private void runEstimator(Estimator est) {
		
		int numberTags = this.inicialNumberTags;
		
		while(numberTags<=this.maxNumberTags) {
			
			est = resetEst(est, numberTags, this.inicialFrameSize); 
			
			for(int i=0;i<this.repetitionsEachNumberTags;i++) {
				est.simulate();
				
				this.metrics=this.metrics.sumMetrics(est.getMetrics());
				est = resetEst(est, numberTags, this.inicialFrameSize);
			}
			
			this.metrics = this.metrics.divByNumberRepetitions(this.repetitionsEachNumberTags);
			
			numberTags+=this.incrementTagsBy;
		}
		
	}
	
	public void runEstimators() {
			
		for(int i=0;i<this.est.size();i++) {
			runEstimator(this.est.get(i));
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}