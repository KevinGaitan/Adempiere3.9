/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.adempiere.core.domains.models.X_AD_Ref_Table;
import org.compiere.util.CCache;

/**
 * 
 * @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
 *		<a href="https://github.com/adempiere/adempiere/issues/1281">
 * 		@see FR [ 1281 ] Error alter table when create Table reference</a>
 *
 */
public class MRefTable extends X_AD_Ref_Table {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9123965213307214868L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Reference_ID id warning if you are referring to reference list or table type should be used AD_Reference_Value_ID
	 *	@param trxName trx
	 */
	public MRefTable (Properties ctx, int AD_Reference_ID, String trxName)
	{
		super (ctx, AD_Reference_ID, trxName);
		if (AD_Reference_ID == 0)
		{
		//	setAD_Table_ID (0);
		//	setAD_Display (0);
		//	setAD_Key (0);
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setIsValueDisplayed (false);
		}
	}	//	MRefTable

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MRefTable (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MRefTable
	
	/** Value Cache						*/
	private static CCache<Integer,MRefTable> cache = new CCache<Integer,MRefTable>(Table_Name, 20);
	
	
	/**
	 * Get reference table
	 * @param ctx
	 * @param referenceId
	 * @return
	 */
	public static MRefTable getById(Properties ctx, int referenceId) {
		MRefTable retValue = cache.get(referenceId);
		if (retValue != null)
			return retValue;
		//	Get from disk
		retValue = new Query(ctx, Table_Name, "AD_Reference_ID=?", null)
					.setParameters(referenceId)
					.first();
		//	Validate
		if(retValue != null
				&& retValue.getAD_Reference_ID() > 0) {
			cache.put(referenceId, retValue);
		}
		//	Default Return
		return retValue;
	}	//	get

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MRefTable [getAD_Disp()=" + getAD_Disp() + ", getAD_Key()=" + getAD_Key() + ", getAD_Reference_ID()="
				+ getAD_Reference_ID() + ", getAD_Table()=" + getAD_Table() + "]";
	}
	
}	//	MRefTable
