/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
 * Copyright (C) 2003-2012 e-Evolution,SC. All Rights Reserved.               *
 * Contributor(s): victor.perez@e-evolution.com, www.e-evolution.com          *
 *****************************************************************************/
package org.eevolution.manufacturing.model;

import java.math.BigDecimal;

import org.adempiere.core.domains.models.I_PP_Order;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartner;
import org.compiere.model.MProduct;
import org.eevolution.manufacturing.model.MPPOrder;
import org.eevolution.manufacturing.model.MPPProductBOM;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test Case Manufacturing Order Make-To-Kit Using Standard BOM and
 * Manufacturing Workflow
 * 
 * @author Victor Perez, www.e-evolution.com
 */
@Tag("Model")
@Tag("MPPOrder")
@Tag("eevolution")
@Tag("Manufacturing")
class IT_MakeToOrderNotStandardBOMandWF extends IT_AbstractMakeToOrder {

    @Test
    void test01() {

        Qty = new BigDecimal(10);
        // Define Product
        product = MProduct.get(getCtx(), M_Product_ID);
        // Define Business Partner
        BPartner = new MBPartner(getCtx(), C_BPartner_ID, trxName);

        // force not BOM Standard
        bom = new MPPProductBOM(getCtx(), PP_Product_BOM_ID, trxName);
        if (bom != null) {
            bom.setValue(product.getValue() + "-Alternate");
            bom.setBOMType(MPPProductBOM.BOMTYPE_Make_To_Order);
            bom.setBOMUse(MPPProductBOM.BOMUSE_Manufacturing);
            bom.saveEx();
        }

        createOrder();
        MPPOrder expected = createPPOrder();

        I_PP_Order actual = MPPOrder.forC_OrderLine_ID(getCtx(), oline.get_ID(),
                oline.getM_Product_ID(), trxName);

        if (actual == null) {
            throw new AdempiereException(
                    "@NotFound@ @PP_Order_ID@ not was generate");
        }
        this.assertPPOrderAreEqual(expected, actual,
                "Confirming Manufacturing Order");

    }

}
