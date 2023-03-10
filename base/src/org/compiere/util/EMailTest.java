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
package org.compiere.util;

/**
 *	The class <code>EMailTest</code> contains tests for the class 
 *	EMail
 * 	<p>
 *
 *  @author Jorg Janke
 *  @version  $Id: EMailTest.java,v 1.2 2006/07/30 00:54:36 jjanke Exp $
 *  @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
 *			<li> FR [ 402 ] Mail setup is hardcoded
 *			@see https://github.com/adempiere/adempiere/issues/402
 *			<li> FR [ 1308 ] Unable To send test mail
 *			@see https://github.com/adempiere/adempiere/issues/1308
 */
public class EMailTest
{
	
	//  TODO - mock these so the tests will function without a specific email service
//	/**
//	 * Construct new test instance
//	 *
//	 * @param name the test name
//	 */
//	public EMailTest(String name)
//	{
//	}
//
//	String host = 	"smtp.gmail.com";
//	String usr = 	"ysenih@erpconsultoresyasociados.com";
//	String pwd = 	"CorvetteZR2";
//	//
//	String from = 	"ysenih@erpconsultoresyasociados.com";
//	//
//	String to = 	"ysenih@erpconsultoresyasociados.com";
//	String to2 = 	"ysenih@erpconsultoresyasociados.com";
//	String to3 = 	"ysenih@erpconsultoresyasociados.com";
//
//	/**
//	 * Perform pre-test initialization
//	 * @throws Exception
//	 * @see TestCase#setUp()
//	 */
//	protected void setUp() throws Exception
//	{
//		super.setUp();
//		org.compiere.Adempiere.startup (true);
//	}	//	setup
//
//	/**
//	 *	Test sending to internal
//	 *
//	public void testInternal()
//	{
//		EMail emailTest = new EMail(host, from, from, "TestInternal", "Test Internal Message");
//		assertEquals(emailTest.send(), EMail.SENT_OK);
//	}	//	testInternal
//
//	/**
//	 *	Test sending to internal authenticated
//	 *
//	public void testInternalAuthenticate()
//	{
//		EMail emailTest = new EMail(host, from, from, "TestInternalAuthenticate", "Test Internal Authenticate Message");
//		emailTest.setEMailUser(usr, pwd);
//		assertEquals(emailTest.send(), EMail.SENT_OK);
//	}	//	testInternalAuthenticate
//
//	/**
//	 *	Test sending to external
//	 *
//	public void testExternal()
//	{
//		EMail emailTest = new EMail(host, from, to, "TestExternal", "Test External Message");
//		assertNotSame(emailTest.send(), EMail.SENT_OK);
//	}	//	testExternal
//
//	/**
//	 *	Test sending to external authenticated
//	 *
//	public void testExternalAuthenticate()
//	{
//		EMail emailTest = new EMail(host, from, to, "TestExternalAuthenticate", "Test External Authenticate Message");
//		emailTest.setEMailUser(usr, pwd);
//		assertEquals(emailTest.send(), EMail.SENT_OK);
//	}	//	testExternalAuthenticate
//
//	/**
//	 *	Test sending HTML
//	 *
//	public void testHTML()
//	{
//		EMail emailTest = new EMail(host, from, to);
//		emailTest.addCc(to2);
//		emailTest.setMessageHTML("TestHTML", "Test HTML Message");
//		emailTest.setEMailUser(usr, pwd);
//		assertEquals(emailTest.send(), EMail.SENT_OK);
//	}	//	testHTML
//
//	/**
//	 *	Test sending Attachment
//	 *
//	public void testAttachment()
//	{
//		EMail emailTest = new EMail(host, from, to, "TestAttachment", "Test Attachment Message");
//		emailTest.addTo(to2);
//		emailTest.addCc(to3);
//		emailTest.addAttachment(new File("C:\\Adempiere\\RUN_Adempiere.sh"));
//		emailTest.setEMailUser(usr, pwd);
//		assertEquals(emailTest.send(), EMail.SENT_OK);
//	}	//	testAttachmentHTML
//
//	/**
//	 *	Test sending Attachment HTML
//	 */
//	public void testAttachmentHTML() {
//		//	FR [ 402 ]
//		EMail emailTest = new EMail(MClient.get(Env.getCtx()));
//		if(emailTest.isSmtpAuthorization()) {
//			emailTest.createAuthenticator(usr, pwd);
//		}
//		emailTest.addTo(to2);
//		emailTest.addCc(to3);
//		emailTest.setMessageHTML("TestAttachmentHTML", "Test Attachment HTML Message");
//		emailTest.addAttachment(new File("C:\\Adempiere\\RUN_Adempiere.sh"));
//		assertEquals(emailTest.send(), EMail.SENT_OK);
//	}	//	testAttachmentHTML

}	//	EMailTest
