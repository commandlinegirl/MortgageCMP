package com.codelemma.mortgagecmp.accounting;

import com.codelemma.mortgagecmp.accounting.Storage.StorageException;

public interface MortgageStorer<T extends Mortgage> 
    //extends MortgageFactory, MortgageSaver<T> {
    extends MortgageSaver<T> {
	public String getTag();
	public Mortgage load(int id) throws StorageException;
}
