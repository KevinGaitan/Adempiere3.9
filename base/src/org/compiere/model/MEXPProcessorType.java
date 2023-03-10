/**********************************************************************
 * This file is part of Adempiere ERP Bazaar                          * 
 * http://www.adempiere.org                                           * 
 *                                                                    * 
 * Copyright (C) Trifon Trifonov.                                     * 
 * Copyright (C) Contributors                                         * 
 *                                                                    * 
 * This program is free software; you can redistribute it and/or      * 
 * modify it under the terms of the GNU General Public License        * 
 * as published by the Free Software Foundation; either version 2     * 
 * of the License, or (at your option) any later version.             * 
 *                                                                    * 
 * This program is distributed in the hope that it will be useful,    * 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of     * 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the       * 
 * GNU General Public License for more details.                       * 
 *                                                                    * 
 * You should have received a copy of the GNU General Public License  * 
 * along with this program; if not, write to the Free Software        * 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,         * 
 * MA 02110-1301, USA.                                                * 
 *                                                                    * 
 * Contributors:                                                      * 
 *  - Trifon Trifonov (trifonnt@users.sourceforge.net)                *
 *                                                                    *
 * Sponsors:                                                          *
 *  - e-Evolution (http://www.e-evolution.com/)                       *
 *********************************************************************/

package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.adempiere.core.domains.models.X_EXP_Processor_Type;
import org.adempiere.process.rpl.IExportProcessorDefinition;
import org.compiere.util.CLogger;

/**
 * @author Trifon N. Trifonov
 */
public class MEXPProcessorType extends X_EXP_Processor_Type {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4987531346397814095L;
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MEXPProcessorType.class);
	
	
	public MEXPProcessorType(Properties ctx, int EXP_Processor_Type_ID, String trxName) {
		super(ctx, EXP_Processor_Type_ID, trxName);
	}
	
	public MEXPProcessorType(Properties ctx, ResultSet rs, String trxName) {
		super (ctx, rs, trxName);
	}
	
	/**
	 * Get processor instance from current type
	 * @return
	 * @throws Exception
	 */
	public IExportProcessorDefinition getProcessorInstance() throws Exception {
		try {
			Class<?> clazz = Class.forName(getJavaClass());
			return (IExportProcessorDefinition) clazz.newInstance();
		} catch (Exception e) {
			s_log.severe(e.toString());
			throw e;
		}
	}
}
