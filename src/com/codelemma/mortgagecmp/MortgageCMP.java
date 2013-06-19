package com.codelemma.mortgagecmp;

import com.codelemma.mortgagecmp.accounting.Account;
import com.codelemma.mortgagecmp.accounting.Mortgage;

import android.app.Application;

public class MortgageCMP extends Application {

    private int simStartMonth = -1; // not set yet
    private int simStartYear = -1;  // not set yet
    private int simulationLength = 360;// 50*12;
    private Account account;  
    private static MortgageCMP appInstance;
	private boolean needToRecalculate = true;
	private Mortgage currentMortgage;
    
    @Override
    public void onCreate() {        
        super.onCreate();
        appInstance = this;
    }
    
    public static MortgageCMP getInstance() {
         return appInstance;
    }
	
	public void setSimulationStartYear(int simStartYear){
	    this.simStartYear = simStartYear;
	}		

	public void setSimulationStartMonth(int simStartMonth){
	    this.simStartMonth = simStartMonth;
	}	
	
	public int getSimulationStartYear(){
	    return simStartYear;
	}		
	
	public int getSimulationStartMonth(){
	    return simStartMonth;
	}	

	public int getSimulationLength() {
		return simulationLength;
	}
	
	public void setAccount() {
		account = new Account();
	}

	public Account getAccount(){
	    return account;
	}

    public Mortgage getCurrentMortgage() {
    	return currentMortgage;
    }
    
    public void setCurrentMortgage(Mortgage mortgage) {
    	currentMortgage = mortgage;
    }
}