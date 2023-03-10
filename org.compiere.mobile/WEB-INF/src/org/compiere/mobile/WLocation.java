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
package org.compiere.mobile;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.adempiere.legacy.apache.ecs.AlignType;
import org.adempiere.legacy.apache.ecs.xhtml.a;
import org.adempiere.legacy.apache.ecs.xhtml.b;
import org.adempiere.legacy.apache.ecs.xhtml.button;
import org.adempiere.legacy.apache.ecs.xhtml.div;
import org.adempiere.legacy.apache.ecs.xhtml.fieldset;
import org.adempiere.legacy.apache.ecs.xhtml.form;
import org.adempiere.legacy.apache.ecs.xhtml.h1;
import org.adempiere.legacy.apache.ecs.xhtml.input;
import org.adempiere.legacy.apache.ecs.xhtml.label;
import org.adempiere.legacy.apache.ecs.xhtml.option;
import org.adempiere.legacy.apache.ecs.xhtml.p;
import org.adempiere.legacy.apache.ecs.xhtml.script;
import org.adempiere.legacy.apache.ecs.xhtml.select;
import org.adempiere.legacy.apache.ecs.xhtml.table;
import org.adempiere.legacy.apache.ecs.xhtml.td;
import org.adempiere.legacy.apache.ecs.xhtml.tr;
import org.compiere.model.GridField;
import org.compiere.model.MCountry;
import org.compiere.model.MLocation;
import org.compiere.model.MRegion;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 *  WLocation Servlet.
 *  <p>
 *  The servlet is invoked by a parent window via
 *  <code>
 *  WLocation?FormName=formName%ColumnName=columnName
 *  </code>
 *  and assumes that in the opening window/form there are two fields
 *  <code>
 *  opener.document.formName.columnName - The (hidden) field for the ID
 *  opener.document.formName.columnName_D - The display field for the value
 *  </code>
 *  When selecting an entry, the window is closed and the value of the two fields set.
 *
 *  @author Jorg Janke
 *  @version  $Id: WLocation.java,v 1.1 2009/04/15 11:27:15 vinhpt Exp $
 *  
 *  @contrib adaxa-pb
 */
public class WLocation extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 285780594700749274L;
	
	/**	Logger			*/
	protected static CLogger	log = CLogger.getCLogger(WLocation.class);

	/**
	 * Initialize global variables
	 *
	 * @param config config
	 * @throws ServletException
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		if (!MobileEnv.initWeb(config))
			throw new ServletException("WLocation.init");
	}   //  init

	/** Parameter   */
	private static final String P_TARGET = "TARGET";
	private static final String P_C_LOCATION_ID = "C_LOCATION_ID";
	private static final String P_ADDRESS1 = "ADDRESS1";
	private static final String P_ADDRESS2 = "ADDRESS2";
	private static final String P_ADDRESS3= "ADDRESS3";
	private static final String P_ADDRESS4= "ADDRESS4";	
	private static final String P_CITY = "CITY";
	private static final String P_POSTAL = "POSTAL";
	private static final String P_C_COUNTRY_ID = "C_COUNTRY_ID";
	private static final String P_C_REGION_ID = "C_REGION_ID";

	/**
	 * Process the HTTP Get request - initial Start
	 * Needs to have parameters FormName and ColumnName
	 *
	 * @param request request
	 * @param response response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		log.fine("");
		HttpSession sess = request.getSession();
		WWindowStatus ws = WWindowStatus.get(request);
		if (ws == null)
		{
			MobileUtil.createTimeoutPage(request, response, this, null);
			return;
		}
		//  Get Mandatory Parameters
		String columnName = MobileUtil.getParameter (request, "ColumnName");
		log.info ("ColumnName=" + columnName + " - " + ws.toString());
		//
		GridField mField = ws.curTab.getField(columnName);
		log.config("ColumnName=" + columnName
			+ ", MField=" + mField);
		if (mField == null || columnName == null
			|| columnName.equals(""))
		{
			MobileUtil.createErrorPage(request, response, this,
				Msg.getMsg(ws.ctx, "ParameterMissing"));
			return;
		}
		MLocation location = null;
		Object value = mField.getValue();
		if (value != null && value instanceof Integer)
			location = new MLocation(ws.ctx, ((Integer)value).intValue(),null);
		else
			location = new MLocation(ws.ctx, 0,null);

		//String targetBase = "parent.WWindow." + WWindow.FORM_NAME + "." + columnName;
		String targetBase = "opener.WWindow." + WWindow.FORM_NAME + "." + columnName;
                String action = request.getRequestURI();
		//  Create Document
		MobileDoc doc = MobileDoc.createPopup (mField.getHeader());
		boolean hasDependents = ws.curTab.hasDependants(columnName);
		boolean hasCallout = mField.getCallout().length() > 0;

		//  Reset
		button reset = new button();
		reset.addElement("Reset");                      //  translate
		String script = targetBase + "D.value='';" + targetBase + "F.value='';closePopup();";
		if (hasDependents || hasCallout)
			script += "startUpdate(" + targetBase + "F);";
		reset.setOnClick(script);
		//

		div div = new div();
		div.setClass("toolbar");
		h1 header = new h1();
		header.setID("pageTitle");
		div.addElement(header);
		a anchor = new a();
		anchor.setID("backButton");
		anchor.setClass("button");
		div.addElement(anchor);
		
		doc.getBody().addElement(fillForm(ws, action, location, targetBase, hasDependents || hasCallout))
			.addElement(reset).addElement(div);
		//
	//	log.trace(log.l6_Database, doc.toString());
		MobileUtil.createResponse (request, response, this, null, doc, true);
	}   //  doGet


	/**
	 *  Process the HTTP Post request (update Address)
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.fine("");
		HttpSession sess = request.getSession();
		WWindowStatus ws = WWindowStatus.get(request);
		if (ws == null)
		{
			MobileUtil.createTimeoutPage(request, response, this, null);
			return;
		}
		int C_Location_ID = MobileUtil.getParameterAsInt(request, P_C_LOCATION_ID);
		String targetBase = "opener.WWindow." + WWindow.FORM_NAME + ".C_Location_ID";

		//  Create Location
		MLocation location = new MLocation(ws.ctx, C_Location_ID,null);
		log.fine("doPost updating C_Location_ID=" + C_Location_ID + " - " + targetBase);

		location.setAddress1 (MobileUtil.getParameter(request, P_ADDRESS1));
		location.setAddress2 (MobileUtil.getParameter(request, P_ADDRESS2));
		location.setAddress3(MobileUtil.getParameter(request, P_ADDRESS3));
		location.setAddress4(MobileUtil.getParameter(request, P_ADDRESS4));
		location.setCity (MobileUtil.getParameter(request, P_CITY));
		location.setPostal (MobileUtil.getParameter(request, P_POSTAL));
		location.setC_Country_ID (MobileUtil.getParameterAsInt(request, P_C_COUNTRY_ID));
                location.setC_Region_ID (MobileUtil.getParameterAsInt(request, P_C_REGION_ID));
                System.out.println("location =========== "+location);
		//  Document
		MobileDoc doc = MobileDoc.createPopup ("WLocation");

		//  Save Location
		location.saveEx();
		C_Location_ID = location.getC_Location_ID();

		if (C_Location_ID == 0)
			doc.getBody().addElement(new p(new b("ERROR - Location=0")));
		doc.getBody().addElement(new p().addElement(location.toString()));

		//  Update Target
		script script = new script(new StringBuffer()
			.append(targetBase).append("D.value='").append(C_Location_ID).append("';")
			.append(targetBase).append("F.value='").append(location.toString())
			.append("';closePopup();").toString());
		doc.getBody().addElement(script);
		log.fine("script=" + script.toString());
		//
                form myForm = null;
		myForm = new form ();
                table table = new table();
		table.setID("WLocation");
                button button = new button();
                button.addElement("ok");
                StringBuffer script2 = new StringBuffer();
                String targetBase2 = "opener.document.WForm.C_Location_ID";
                script2.append(targetBase2).append("D.value='").append(C_Location_ID).append("';")
                        //script.append(targetBase).append("D.value='").append("';")
                        .append(targetBase2).append("F.value='").append(location.toString())
                        //.append(targetBase).append("F.value='")
                         .append("';submit();window.close();");
                button.setOnClick(script2.toString());
                table.addElement(button);
                myForm.addElement(table);
                doc.getBody().addElement(myForm);

		MobileUtil.createResponse(request, response, this, null, doc, true);
	}   //  doPost

	/**
	 *  Fill Address Form
	 *	@param ws WindowStatus
	 *	@param location location
	 *	@param targetBase target field string - add field Type
	 *	@param addStart add startUpdate
	 *	@return  Table with selection
	 */
	private form fillForm (WWindowStatus ws, String action, MLocation location,
		String targetBase, boolean addStart)
	{
		form myForm = null;
		myForm = new form (action).setName("WLocation");
		myForm.setID("Location");
		myForm.setTitle("Location");
		myForm.addAttribute("selected", "true");
		myForm.setClass("panel");
		myForm.setMethod("post");
		myForm.addElement(new input(input.TYPE_HIDDEN, P_TARGET, targetBase));
		myForm.addElement(new input(input.TYPE_HIDDEN, P_C_LOCATION_ID, location.getC_Location_ID()));
		//
		fieldset fs = new fieldset();
		fs.setID("WLocation");
		//  --  Line 1
		div line = new div();
		line.setClass("row");
		line.addElement(new label().addElement(Msg.getMsg(ws.ctx, "Address")+ " 1"));
		input myInput = null;
		myInput = new input (input.TYPE_TEXT, P_ADDRESS1, location.getAddress1());
		line.addElement(myInput);
		fs.addElement(line);
		//  --  Line 2
		line = new div();
		line.setClass("row");
		line.addElement(new label().addElement(Msg.getMsg(ws.ctx, "Address")+ " 2"));
		myInput = new input (input.TYPE_TEXT, P_ADDRESS2, location.getAddress2());
		line.addElement(myInput);
		fs.addElement(line);
		
		//-- add by Dan 
		line = new div();
		line.setClass("row");
		line.addElement(new label().addElement(Msg.getMsg(ws.ctx, "Address")+ " 3"));
		myInput = new input (input.TYPE_TEXT, P_ADDRESS3, location.getAddress3());
		line.addElement(myInput);
		fs.addElement(line);
		
//		-- add by Dan 
		line = new div();
		line.setClass("row");
		line.addElement(new label().addElement(Msg.getMsg(ws.ctx, "Address")+ " 4"));
		myInput = new input (input.TYPE_TEXT, P_ADDRESS4, location.getAddress4());
		line.addElement(myInput);
		fs.addElement(line);
		
		//  --  Line 3
		line = new div();
		line.setClass("row");
		line.addElement(new label().addElement(Msg.getMsg(ws.ctx, "City")));      //  1
		myInput = new input (input.TYPE_TEXT, P_CITY, location.getCity());
		line.addElement(myInput);                            //  2
		//

		line = new div();
		line.setClass("row");
		if (location.getCountry().isHasRegion())
		{
			line.addElement(new label().addElement(Msg.getMsg(ws.ctx, "Region")));    //  3
			line.addElement(getRegion(location, ws));         //  4
		}
		//
		

		line = new div();
		line.setClass("row");
		line.addElement(new label().addElement(Msg.getMsg(ws.ctx, "Postal")));    //  5
		myInput = new input (input.TYPE_TEXT, P_POSTAL, location.getPostal());
		myInput.setMaxlength(10).setSize(6);
		line.addElement(myInput);                            //  6
		//
	//	input = new input (input.TYPE_TEXT, "PostalAdd", mLocation.PostalAdd );
	//	line.addElement(new td(input).setAlign(AlignType.LEFT));
		fs.addElement(line);
		//  --  Line 4
		line = new div();
		line.setClass("row");
		line.addElement(new label().addElement(Msg.getMsg(ws.ctx, "Country")));
		line.addElement(this.getCountry(location, ws));
		fs.addElement(line);


		//  --  Line 5
		line = new div();
		line.setClass("row");
		
		//  Submit
		StringBuffer script = new StringBuffer();
		script.append(targetBase).append("D.value='").append("temp").append("';")
		//script.append(targetBase).append("D.value='").append("';")
		.append(targetBase).append("F.value='").append("temp")
		//.append(targetBase).append("F.value='")
		.append("';submit();closePopup();");


		a button = new a("#", "Submit");
		button.addAttribute("type", "submit");
		button.setClass("whiteButton");
		button.setOnClick(script.toString());
		line.addElement(button);
		
		fs.addElement(line);



		/*button button = new button();
		button.addElement("&gt;");
		StringBuffer script = new StringBuffer();
		script.append(targetBase).append("D.value='").append("temp").append("';")
			//script.append(targetBase).append("D.value='").append("';")
                        .append(targetBase).append("F.value='").append("temp")
			//.append(targetBase).append("F.value='")
                         .append("';closePopup();");
		if (addStart)
			script.append("startUpdate(").append(targetBase).append("F);");
		button.setOnClick(script.toString());
                table.addElement(button);*/
		myForm.addElement(fs);
		//
		return myForm;
	}   //  fillform


	/**
	 *  Get Country Pick-List
	 *
	 *  @param location location model
	 *  @param ws window status
	 *  @return Select Field with Countries
	 */
	private select getCountry (MLocation location, WWindowStatus ws)
	{
		MCountry[] countries =  MCountry.getCountries (location.getCtx());
		int comp = location.getC_Country_ID();
		if (comp == 0)
			comp = Env.getContextAsInt(ws.ctx, "C_Country_ID");
		option[] options = new option[countries.length];
		for (int i = 0; i < countries.length; i++)
		{
			options[i] = new option (String.valueOf(countries[i].getC_Country_ID()));
			options[i].addElement(countries[i].getName());
			if (comp == countries[i].getC_Country_ID())
				options[i].setSelected(true);
		}

		select select = new select (P_C_COUNTRY_ID, options);
		return select;
	}   //  getRegion

	/**
	 *  Get Region Pick-List
	 *
	 *  @param location localion model
	 *  @param ws window status
	 *  @return Select field with Region
	 */
	private select getRegion (MLocation location, WWindowStatus ws)
	{
		MRegion[] regions =  MRegion.getRegions (location.getCtx(), location.getC_Country_ID());
		int comp = location.getC_Region_ID();
		if (comp == 0)
			comp = Env.getContextAsInt(ws.ctx, "C_Region_ID");
		option[] options = new option[regions.length];
		for (int i = 0; i < regions.length; i++)
		{
			options[i] = new option (String.valueOf(regions[i].getC_Region_ID()));
			options[i].addElement(regions[i].getName());
			if (comp == regions[i].getC_Region_ID())
				options[i].setSelected(true);
		}

		select select = new select (P_C_REGION_ID, options);
		return select;
	}   //  getRegion

}   //  WLocation

