/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2020 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net or http://www.adempiere.net/license.html         *
 *****************************************************************************/

package org.compiere.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.adempiere.test.CommonGWSetup;
import org.junit.jupiter.api.Test;

class IT_MCostElement extends CommonGWSetup {

    @Test
    void checkThatCostElementsExist() {

        List<MCostElement> list = MCostElement.getCostElement(ctx, trxName);
        assertTrue(list.size() > 0, "Expected cost elements to exist");

    }

}
