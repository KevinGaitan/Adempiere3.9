/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 Adempiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *
 * Copyright (C) 2005 Robert KLEIN.  robeklein@hotmail.com  
 * 
 *****************************************************************************/

package org.adempiere.pipo;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.adempiere.core.domains.models.X_AD_Package_Imp_Proc;
import org.compiere.Adempiere;
import org.compiere.db.CConnection;
import org.compiere.util.CLogMgt;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Trx;
import org.spin.util.XMLUtils;

/**
 * IntPackIn Tool.
 * 
 * @author: Robert KLEIN. robeklein@hotmail.com
 */
public class PackIn extends PackInAbstract {

	/** Logger */
	private CLogger log = CLogger.getCLogger("PackIn");
	public static boolean updateMode = true;
	public static String database = "Oracle";
	public static String packageDirectory = null;
	public static boolean isRequiresSync = true;
	/**
	 * Uses PackInHandler to update AD.
	 * 
	 * @param fileName
	 *            xml file to read
	 * @return status message
	 */
	public String importXML(String fileName, Properties ctx, String trxName) throws Exception {
		log.info("importXML:" + fileName);
		File in = new File(fileName);
		if (!in.exists()) {
			String msg = "File does not exist: " + fileName;
			log.info("importXML:" + msg);
			return msg;
		}
		try {
			log.info("starting");
			System.setProperty("javax.xml.parsers.SAXParserFactory",
					"org.apache.xerces.jaxp.SAXParserFactoryImpl");
			PackInHandler handler = new PackInHandler();
			handler.set_TrxName(trxName);
			handler.setCtx(ctx);
			handler.setProcess(this);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			//	Set default features
			XMLUtils.setDefaultFeatures(factory);
			SAXParser parser = factory.newSAXParser();
			String msg = "Start Parser";
			log.info(msg);
			parser.parse(in, handler);
			msg = "End Parser";
			log.info(msg);
			return "OK.";
		} catch (Exception e) {
			log.log(Level.SEVERE, "importXML:", e);
			throw e;
		}
	}

	/**
	 * Doit
	 * 
	 * @return ""
	 * 
	 */
	protected String doIt() throws Exception {

		X_AD_Package_Imp_Proc packageToImport = new X_AD_Package_Imp_Proc(getCtx(), getRecord_ID(), get_TrxName());

		// clear cache of previous runs
		IDFinder.clearIDCache();

		// Create Target directory if required
		String packageDirectory = packageToImport.getAD_Package_Dir();
		if (packageDirectory == null || packageDirectory.trim().length() == 0) {
			packageDirectory = Adempiere.getAdempiereHome();
		}
		File targetDir = new File( packageDirectory + File.separator
				+ "packages");

		if (!targetDir.exists()) {
			boolean success = (new File(packageDirectory
					+ File.separator + "packages")).mkdirs();
			if (!success) {
				log.info("Target directory creation failed");
			}
		}

		// Unzip package
		File zipFilepath = new File(packageToImport.getAD_Package_Source());
		log.info("zipFilepath->" + zipFilepath);
		String PackageName = CreateZipFile.getParentDir(zipFilepath);
		CreateZipFile.unpackFile(zipFilepath, targetDir);

		String dict_file = packageDirectory + File.separator
				+ "packages" + File.separator + PackageName + File.separator
				+ "dict" + File.separator + "PackOut.xml";
		log.info("dict file->" + dict_file);
		PackIn packIn = new PackIn();
		//	Set from record
		PackIn.updateMode = packageToImport.isAD_Override_Dict();
		PackIn.isRequiresSync = packageToImport.isRequiresSync();
		
		PackIn.packageDirectory = packageDirectory + File.separator
				+ "packages" + File.separator + PackageName + File.separator;
		if (DB.isOracle())
			PackIn.database = "Oracle";
		else if (DB.isPostgreSQL())
			PackIn.database = "PostgreSQL";

		// call XML Handler
		String msg = packIn.importXML(dict_file, getCtx(), get_TrxName());
		return msg;
	} // doIt

	/***************************************************************************
	 * 
	 * @param args
	 *            XMLfile host port db username password
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out
					.println("Please give the file name to read as first parameter.");
			System.exit(1);
		}

		String file = args[0];
		org.compiere.Adempiere.startup(true);

		// globalqss - added argument 8 to generate system sequences
		if (args.length > 8 && args[8].equals(Ini.P_ADEMPIERESYS)) {
			System.out.println("**** WARNING: Working with system sequences "
					+ Ini.P_ADEMPIERESYS + " ****");
			Ini.setProperty(Ini.P_ADEMPIERESYS, true);
		}

		PackIn packIn = new PackIn();
		// org.compiere.Compiere.startupEnvironment(true);
		// Force connection if there are enough parameters. Else we work with
		// Compiere.properties
		if (args.length >= 6) {
			// CConnection cc = CConnection.get("PostgreSQL", args[1],
			// Integer.valueOf(args[2]).intValue(), args[5], args[3], args[4]);
			CConnection cc = CConnection.get();
			// System.out.println("DB Connect String1:"+cc.getDbName());
			PackIn.database = cc.getType();
			DB.setDBTarget(cc);
		}

		// Level.OFF, Level.SEVERE, Level.WARNING, Level.INFO,
		// Level.CONFIG, Level.FINE, Level.FINER, Level.FINEST, Level.ALL

		Level logLevel = Level.FINER;

		switch (Integer.parseInt(args[6])) {
		case 1:
			logLevel = Level.OFF;
			break;
		case 2:
			logLevel = Level.SEVERE;
			break;
		case 3:
			logLevel = Level.WARNING;
			break;
		case 4:
			logLevel = Level.INFO;
			break;
		case 5:
			logLevel = Level.CONFIG;
			break;
		case 6:
			logLevel = Level.FINE;
			break;
		case 7:
			logLevel = Level.FINER;
			break;
		case 8:
			logLevel = Level.FINEST;
			break;
		case 9:
			logLevel = Level.ALL;
			break;
		}
		CLogMgt.setLevel(logLevel);
		CLogMgt.setLoggerLevel(logLevel, null);

		if (args.length >= 8)
			PackIn.updateMode = args[7] == "true";
		
		String trxName = Trx.createTrxName("PackIn");
		try {
			packIn.importXML(file, Env.getCtx(), trxName);
			Trx trx = Trx.get(trxName, false);
			if (trx != null)
				trx.commit(true);
		} catch (Exception e) {
			System.out.println("Import Failed: " + e.getLocalizedMessage());
			Trx trx = Trx.get(trxName, false);
			if (trx != null)
				trx.rollback();
		}

		System.exit(0);
	} // main
} // PackIn
