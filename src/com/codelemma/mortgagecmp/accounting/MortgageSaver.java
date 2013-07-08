package com.codelemma.mortgagecmp.accounting;

import com.codelemma.mortgagecmp.accounting.Storage.StorageException;

public interface MortgageSaver<T extends Mortgage> {
	public void save(T mortgage) throws StorageException;
	public void remove(T mortgage) throws StorageException;
}