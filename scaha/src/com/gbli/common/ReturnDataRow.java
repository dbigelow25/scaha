/**
 * 
 */
package com.gbli.common;

import java.util.Vector;


/**
 * @author dbigelow
 *
 */
public class ReturnDataRow extends Vector {
	
	String rowkey = null;

	
	public ReturnDataRow (int _i) {
		super();
		rowkey = Integer.toString(_i);
	}

	/**
	 * @return the rowkey
	 */
	public String getRowkey() {
		return rowkey;
	}

	/**
	 * @param rowkey the rowkey to set
	 */
	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}
	
	
}
