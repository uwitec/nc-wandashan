package nc.vo.so.so002;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so001.SOToolVO;
import nc.vo.so.so001.SoVoConst;
import nc.vo.so.so016.SoVoTools;

/**
 * zhf 修改   物流生成的销售出库单 会 自动签字生成  销售发票  ，取销售组织等字段的逻辑需要调整
 * wds  2011-06-22
 */

/**
 * 此处插入类型说明。 创建日期：(2003-11-25 14:32:05)
 * 
 * @author：杨涛
 */
public class OutToInvoiceChangeVO implements nc.vo.pf.change.IchangeVO {
    /*
     * 根据产品组自己的需求，把源VO中信息通过运算，
     */
    public nc.vo.pub.AggregatedValueObject retChangeBusiVO(
            nc.vo.pub.AggregatedValueObject preVo,
            nc.vo.pub.AggregatedValueObject nowVo)
            throws BusinessException {

        if (preVo == null || nowVo == null)
            return null;

        nc.vo.pub.AggregatedValueObject[] preVos = (nc.vo.pub.AggregatedValueObject[]) Array
                .newInstance(preVo.getClass(), 1);
        preVos[0] = preVo;
        nc.vo.pub.AggregatedValueObject[] nowVos = (nc.vo.pub.AggregatedValueObject[]) Array
                .newInstance(nowVo.getClass(), 1);
        nowVos[0] = nowVo;
        nc.vo.pub.AggregatedValueObject[] retvos = retChangeBusiVOs(preVos,
                nowVos);
        if (retvos != null && retvos.length > 0)
            return retvos[0];
        return null;
    }

    /*
     * 根据产品组自己的需求，把源VO中信息通过运算进行转换 数组转换
     */
    public nc.vo.pub.AggregatedValueObject[] retChangeBusiVOs(
            nc.vo.pub.AggregatedValueObject[] preVo,
            nc.vo.pub.AggregatedValueObject[] nowVo)
            throws BusinessException {

        if (preVo == null || preVo.length <= 0 || nowVo == null
                || nowVo.length <= 0)
            return null;

        if (SoVoTools.isUI()){
        	ArrayList alPre = new ArrayList();
        	ArrayList alNow = new ArrayList();
        	for(int i = 0; i < preVo.length ; i++){
        		CircularlyAccessibleValueObject head = preVo[i].getParentVO();
        		CircularlyAccessibleValueObject[] bodys = preVo[i].getChildrenVO();
        		if (bodys != null && bodys.length > 0)
        			for(int j = 0; j < bodys.length ;j++){
        				nc.vo.pub.AggregatedValueObject tmp = new nc.vo.ic.pub.bill.GeneralBillVO();
        				tmp.setParentVO((CircularlyAccessibleValueObject)preVo[i].getParentVO().clone());
        				CircularlyAccessibleValueObject[] tVos = (CircularlyAccessibleValueObject[]) Array
        				.newInstance(bodys[j].getClass(), 1);
        				tVos[0] = bodys[j];
        				tmp.setChildrenVO(tVos);
        				alPre.add(tmp);
        			}
        	}
        	preVo = (AggregatedValueObject[]) alPre.toArray(new AggregatedValueObject[alPre.size()]);
        	for(int i = 0; i < nowVo.length ; i++){
        		CircularlyAccessibleValueObject head = nowVo[i].getParentVO();
        		CircularlyAccessibleValueObject[] bodys = nowVo[i].getChildrenVO();
        		if (bodys != null && bodys.length > 0)
        			for(int j = 0; j < bodys.length ;j++){
        				nc.vo.pub.AggregatedValueObject tmp = new SaleinvoiceVO();
        				tmp.setParentVO((CircularlyAccessibleValueObject)nowVo[i].getParentVO().clone());
        				CircularlyAccessibleValueObject[] tVos = (CircularlyAccessibleValueObject[]) Array
        				.newInstance(bodys[j].getClass(), 1);
        				tVos[0] = bodys[j];
        				tmp.setChildrenVO(tVos);
        				alNow.add(tmp);
        			}
        	}
        	nowVo = (AggregatedValueObject[]) alNow.toArray(new AggregatedValueObject[alPre.size()]);
        }
        
        ArrayList alPreType3U = new ArrayList();
        ArrayList alNowType3U = new ArrayList();
        ArrayList alPreTypeOther = new ArrayList();
        ArrayList alNowTypeOther = new ArrayList();

        for (int i = 0; i < preVo.length; i++) {
            String cfirsttype = (String) preVo[i].getChildrenVO()[0]
                    .getAttributeValue("cfirsttype");
            if ("3U".equals(cfirsttype)) {
                alPreType3U.add(preVo[i]);
                alNowType3U.add(nowVo[i]);
            } else {
                alPreTypeOther.add(preVo[i]);
                alNowTypeOther.add(nowVo[i]);
            }
        }
        AggregatedValueObject[] preVo3U = null;
        AggregatedValueObject[] nowVo3U = null;
        if (alPreType3U.size() > 0) {
            preVo3U = (AggregatedValueObject[]) alPreType3U
                    .toArray(new AggregatedValueObject[alPreType3U.size()]);
            nowVo3U = (AggregatedValueObject[]) alNowType3U
                    .toArray(new AggregatedValueObject[alNowType3U.size()]);
        }
        AggregatedValueObject[] preVoOther = null;
        AggregatedValueObject[] nowVoOther = null;
        if (alPreTypeOther.size() > 0) {
            preVoOther = (AggregatedValueObject[]) alPreTypeOther
                    .toArray(new AggregatedValueObject[alPreTypeOther.size()]);
            nowVoOther = (AggregatedValueObject[]) alNowTypeOther
                    .toArray(new AggregatedValueObject[alNowTypeOther.size()]);
        }

        AggregatedValueObject[] retvosOther = retSourceTypeVOs(preVoOther,
                nowVoOther);
        AggregatedValueObject[] retvos3U = retSourceTypeVOs(preVo3U, nowVo3U);
        ArrayList alRet = new ArrayList();
        if (retvosOther != null) {
            for (int i = 0; i < retvosOther.length; i++) {
                alRet.add(retvosOther[i]);
            }
        }
        if (retvos3U != null) {
            for (int i = 0; i < retvos3U.length; i++) {
                alRet.add(retvos3U[i]);
            }
        }
        SaleinvoiceVO[] retvos = null;
        if (alRet.size() > 0) {
            retvos = (SaleinvoiceVO[]) alRet.toArray(new SaleinvoiceVO[alRet
                    .size()]);
        }
        return retvos;
    }

    public nc.vo.pub.AggregatedValueObject[] retSourceTypeVOs(
            nc.vo.pub.AggregatedValueObject[] preVo,
            nc.vo.pub.AggregatedValueObject[] nowVo)
    throws BusinessException {

    	if (preVo == null || preVo.length <= 0 || nowVo == null
    			|| nowVo.length <= 0)
    		return null;

    	String cfirsttype = (String) preVo[0].getChildrenVO()[0]
    	                                                      .getAttributeValue("cfirsttype");

    	// zhf add
    	boolean isfromwl = cfirsttype.equalsIgnoreCase("WDS8");


    	ArrayList hvolist = new ArrayList();
    	SOToolVO htoolvo = null;
    	ArrayList bvolist = new ArrayList();
    	SOToolVO btoolvo = null;

    	nc.vo.pub.AggregatedValueObject[] retvos = nowVo;
    	SaleinvoiceVO saleinvoice = null;
    	SaleVO saleHeader = null;
    	SaleinvoiceBVO[] saleItems = null;

    	ArrayList nowbvolist = new ArrayList();

    	CircularlyAccessibleValueObject[] prebvos = null;

    	for (int i = 0, loop = nowVo.length; i < loop; i++) {

    		prebvos = preVo[i].getChildrenVO();

    		if (prebvos == null || prebvos.length <= 0)
    			continue;

    		saleItems = (SaleinvoiceBVO[]) nowVo[i].getChildrenVO();

    		if (saleItems == null || saleItems.length <= 0)
    			continue;

    		htoolvo = new SOToolVO();
    		htoolvo.setAttributeValue("cfirstbillhid", isfromwl?prebvos[0]
    		                                                            .getAttributeValue("csourcebillhid")
    		                                                            :prebvos[0]
    		                                                                     .getAttributeValue("cfirstbillhid"));
    		htoolvo.setAttributeValue("csalecorpid", null);
    		htoolvo.setAttributeValue("creceiptcorpid", null);

    		htoolvo.setAttributeValue("cdeptid", null);

    		htoolvo.setAttributeValue("cemployeeid", null);

    		hvolist.add(htoolvo);

    		for (int k = 0, loopk = prebvos.length; k < loopk; k++) {

    			btoolvo = new SOToolVO();
    			btoolvo.setAttributeValue("cfirstbillhid", isfromwl?prebvos[0]
    			                                                            .getAttributeValue("csourcebillhid")
    			                                                            :prebvos[k]
    			                                                                     .getAttributeValue("cfirstbillhid"));
    			btoolvo.setAttributeValue("cinvbasdocid", null);
    			btoolvo.setAttributeValue("cfirstbillbid", isfromwl?prebvos[0]
    			                                                            .getAttributeValue("csourcebillbid")
    			                                                            :prebvos[k]
    			                                                                     .getAttributeValue("cfirstbillbid"));
    			btoolvo.setAttributeValue("ccurrencytypeid", prebvos[k]
    			                                                     .getAttributeValue("cquotecurrency"));
    			btoolvo.setAttributeValue("cinventoryid", prebvos[k]
    			                                                  .getAttributeValue("cinventoryid"));
    			btoolvo.setAttributeValue("cgeneralbid", prebvos[k].getAttributeValue("cgeneralbid"));

    			bvolist.add(btoolvo);

    			nowbvolist.add(saleItems[k]);
    		}

    	}

    	SOToolVO[] htoolvos = (SOToolVO[]) hvolist.toArray(new SOToolVO[hvolist
    	                                                                .size()]);

    	SOToolVO[] btoolvos = (SOToolVO[]) bvolist.toArray(new SOToolVO[bvolist
    	                                                                .size()]);

    	String[] fs = null;
    	if ("3U".equals(cfirsttype)) {
    		//退货申请单
    		fs = new String[] {
    				"csalecorpid->getColValue(so_apply,csalecorpid,pk_apply, cfirstbillhid )",
    				"creceiptcorpid->getColValue(so_apply,creceiptcorpid,pk_apply, cfirstbillhid)",
    				"cdeptid->getColValue(so_apply,cdeptid,pk_apply,cfirstbillhid)",
    				"cfreecustid->getColValue(so_apply,cfreecustid,pk_apply,cfirstbillhid)",
    				"bfreecustflag->getColValue(so_apply,bfreecustflag,pk_apply,cfirstbillhid)",
    		"cemployeeid->getColValue( so_apply ,cemployeeid ,pk_apply , cfirstbillhid )" };
    		if(SoVoTools.isUI()) SoVoTools.execFormulas(fs, htoolvos);
    		else SoVoTools.execFormulasAtBs(fs, htoolvos);

    		fs = new String[] {
    				"cinvbasdocid->getColValue( bd_invmandoc,pk_invbasdoc ,pk_invmandoc , cinventoryid)"
    		};
    		if(SoVoTools.isUI()) SoVoTools.execFormulas(fs, btoolvos);
    		else SoVoTools.execFormulasAtBs(fs, btoolvos);

    		fs = new String[] {
    				"ndiscountrate->getColValue(so_apply_b,ndiscountrate,pk_apply_b,cfirstbillbid)",
    				"nitemdiscountrate->getColValue(so_apply_b,nitemdiscountrate,pk_apply_b,cfirstbillbid)",
    				"nexchangeotobrate->getColValue(so_apply_b,nexchangeotobrate,pk_apply_b,cfirstbillbid)",
    				"nexchangeotoarate->getColValue(so_apply_b,nexchangeotoarate,pk_apply_b,cfirstbillbid)",
    				"ntaxrate->getColValue(so_apply_b,ntaxrate,pk_apply_b,cfirstbillbid)",

    				"noriginalcurprice->getColValue(so_apply_b,noriginalcurprice,pk_apply_b,cfirstbillbid)",
    				"noriginalcurtaxprice->getColValue(so_apply_b,noriginalcurtaxprice,pk_apply_b,cfirstbillbid)",
    				"noriginalcurnetprice->getColValue(so_apply_b,noriginalcurnetprice,pk_apply_b,cfirstbillbid)",
    				"noriginalcurtaxnetprice->getColValue(so_apply_b,noriginalcurtaxnetprice,pk_apply_b,cfirstbillbid)",
    				"nprice->getColValue(so_apply_b,nprice,pk_apply_b,cfirstbillbid)",

    				"ntaxprice->getColValue(so_apply_b,ntaxprice,pk_apply_b,cfirstbillbid)",
    				"nnetprice->getColValue(so_apply_b,nnetprice,pk_apply_b,cfirstbillbid)",
    				"ntaxnetprice->getColValue(so_apply_b,ntaxnetprice,pk_apply_b,cfirstbillbid)",
    				"cprolineid->getColValue(so_apply_b,pk_productline,pk_apply_b,cfirstbillbid)",

    				//为精度处理 2007-11-06 xhq
    				"nnumber->getColValue(so_apply_b,nnumber,pk_apply_b,cfirstbillbid)",
    				"noriginalcurmny->getColValue(so_apply_b,noriginalcurmny,pk_apply_b,cfirstbillbid)",
    				"noriginalcursummny->getColValue(so_apply_b,noriginalcursummny,pk_apply_b,cfirstbillbid)",
    				"nmny->getColValue(so_apply_b,nmny,pk_apply_b,cfirstbillbid)",
    				"nsummny->getColValue(so_apply_b,nsummny,pk_apply_b,cfirstbillbid)",
    				"nassistcurmny->getColValue(so_apply_b,nassistcurmny,pk_apply_b,cfirstbillbid)",
    				"nassistcursummny->getColValue(so_apply_b,nassistcursummny,pk_apply_b,cfirstbillbid)"
    		};
    		if(SoVoTools.isUI()) SoVoTools.execFormulas(fs, btoolvos);
    		else SoVoTools.execFormulasAtBs(fs, btoolvos);

    	} else {
    		//销售订单
    		fs = new String[] {
    				"csalecorpid->getColValue(so_sale,csalecorpid,csaleid, cfirstbillhid )",
    				"creceiptcorpid->getColValue(so_sale,creceiptcorpid,csaleid, cfirstbillhid )",
    				"cdeptid->getColValue(so_sale,cdeptid,csaleid,cfirstbillhid)",
    				"bfreecustflag->getColValue(so_sale,bfreecustflag,csaleid,cfirstbillhid)",
    				"cfreecustid->getColValue(so_sale,cfreecustid,csaleid,cfirstbillhid)",
    		"cemployeeid->getColValue( so_sale ,cemployeeid ,csaleid , cfirstbillhid )" };
    		if(SoVoTools.isUI()) SoVoTools.execFormulas(fs, htoolvos);
    		else SoVoTools.execFormulasAtBs(fs, htoolvos);

    		fs = new String[] {
    				"cinvbasdocid->getColValue( bd_invmandoc,pk_invbasdoc ,pk_invmandoc , cinventoryid)"
    		};
    		if(SoVoTools.isUI()) SoVoTools.execFormulas(fs, btoolvos);
    		else SoVoTools.execFormulasAtBs(fs, btoolvos);

    		fs = new String[] {
    				"ndiscountrate->getColValue(so_saleorder_b,ndiscountrate,corder_bid,cfirstbillbid)",
    				"nitemdiscountrate->getColValue(so_saleorder_b,nitemdiscountrate,corder_bid,cfirstbillbid)",
    				"nexchangeotobrate->getColValue(so_saleorder_b,nexchangeotobrate,corder_bid,cfirstbillbid)",
    				"nexchangeotoarate->getColValue(so_saleorder_b,nexchangeotoarate,corder_bid,cfirstbillbid)",
    				"ntaxrate->getColValue(so_saleorder_b,ntaxrate,corder_bid,cfirstbillbid)",

    				"noriginalcurprice->getColValue(so_saleorder_b,noriginalcurprice,corder_bid,cfirstbillbid)",
    				"noriginalcurtaxprice->getColValue(so_saleorder_b,noriginalcurtaxprice,corder_bid,cfirstbillbid)",
    				"noriginalcurnetprice->getColValue(so_saleorder_b,noriginalcurnetprice,corder_bid,cfirstbillbid)",
    				"noriginalcurtaxnetprice->getColValue(so_saleorder_b,noriginalcurtaxnetprice,corder_bid,cfirstbillbid)",
    				"nprice->getColValue(so_saleorder_b,nprice,corder_bid,cfirstbillbid)",

    				"ntaxprice->getColValue(so_saleorder_b,ntaxprice,corder_bid,cfirstbillbid)",
    				"nnetprice->getColValue(so_saleorder_b,nnetprice,corder_bid,cfirstbillbid)",
    				"ntaxnetprice->getColValue(so_saleorder_b,ntaxnetprice,corder_bid,cfirstbillbid)",
    				"cprolineid->getColValue(so_saleorder_b,cprolineid,corder_bid,cfirstbillbid)",
    				"ct_manageid->getColValue(so_saleorder_b,ct_manageid,corder_bid,cfirstbillbid)",

    				"nquoteoriginalcurprice->getColValue(so_saleorder_b,norgqtprc,corder_bid,cfirstbillbid)",
    				"nquoteoriginalcurtaxprice->getColValue(so_saleorder_b,norgqttaxprc,corder_bid,cfirstbillbid)",
    				"nquoteoriginalcurnetprice->getColValue(so_saleorder_b,norgqtnetprc,corder_bid,cfirstbillbid)",
    				"nquoteoriginalcurtaxnetprice->getColValue(so_saleorder_b,norgqttaxnetprc,corder_bid,cfirstbillbid)",

    				"nquoteprice->getColValue(so_saleorder_b,nqtprc,corder_bid,cfirstbillbid)",
    				"nquotetaxprice->getColValue(so_saleorder_b,nqttaxprc,corder_bid,cfirstbillbid)",
    				"nquotenetprice->getColValue(so_saleorder_b,nqtnetprc,corder_bid,cfirstbillbid)",
    				"nquotetaxnetprice->getColValue(so_saleorder_b,nqttaxnetprc,corder_bid,cfirstbillbid)",

    				//为精度处理 2007-11-06 xhq
    				"noutnum->getColValue(ic_general_b,noutnum,cgeneralbid,cgeneralbid)",
    				"naccumwastnum->getColValue(ic_general_b,naccumwastnum,cgeneralbid,cgeneralbid)",
    				"nsignnum->getColValue(ic_general_bb3,nsignnum,cgeneralbid,cgeneralbid)",
    				"nrushnum->getColValue(ic_general_bb3,nrushnum,cgeneralbid,cgeneralbid)", 

    				"nnumber->getColValue(so_saleorder_b,nnumber,corder_bid,cfirstbillbid)",
    				"noriginalcurmny->getColValue(so_saleorder_b,noriginalcurmny,corder_bid,cfirstbillbid)",
    				"noriginalcursummny->getColValue(so_saleorder_b,noriginalcursummny,corder_bid,cfirstbillbid)",
    				"nmny->getColValue(so_saleorder_b,nmny,corder_bid,cfirstbillbid)",
    				"nsummny->getColValue(so_saleorder_b,nsummny,corder_bid,cfirstbillbid)"
    		};

    		if(SoVoTools.isUI()) SoVoTools.execFormulas(fs, btoolvos); 
    		else SoVoTools.execFormulasAtBs(fs, btoolvos); 
    	}

    	for (int i = 0, loop = nowVo.length; i < loop; i++) {

    		saleinvoice = (SaleinvoiceVO) nowVo[i];
    		saleHeader = (SaleVO) saleinvoice.getParentVO();

    		saleHeader.setCsalecorpid((String) htoolvos[i]
    		                                            .getAttributeValue("csalecorpid"));
    		saleHeader.setCreceiptcorpid((String) htoolvos[i]
    		                                               .getAttributeValue("creceiptcorpid"));
    		if (saleHeader.getFinvoicetype() == null)
    			saleHeader.setFinvoicetype(SoVoConst.i0);
    		saleHeader.setCdeptid((String) htoolvos[i]
    		                                        .getAttributeValue("cdeptid"));
    		saleHeader.setCreceipttype("32");
    		saleHeader.setCemployeeid((String) htoolvos[i]
    		                                            .getAttributeValue("cemployeeid"));
    		saleHeader.setCfreecustid((String) htoolvos[i].getAttributeValue("cfreecustid"));
    		String stmp = (String)htoolvos[i].getAttributeValue("bfreecustflag");
    		saleHeader.setBfreecustflag(new UFBoolean(stmp==null?"N":stmp));
    		saleHeader.setFstatus(SoVoConst.i1);

    	}

    	SaleinvoiceBVO saleItem = null;
    	Object oTemp = null;
    	boolean bCalculated = false;
    	UFDouble dTemp = null, dOutNum = null, dAccumWastNum = null, dSignNum = null, dRushNum = null;

    	for (int i = 0, loop = nowbvolist.size(); i < loop; i++) {
    		saleItem = (SaleinvoiceBVO) nowbvolist.get(i);
    		saleItem.setCinvbasdocid((String) btoolvos[i]
    		                                           .getAttributeValue("cinvbasdocid"));
    		saleItem.setCcurrencytypeid((String) btoolvos[i]
    		                                              .getAttributeValue("ccurrencytypeid"));
    		//ndiscountrate
    		if (btoolvos[i].getAttributeValue("ndiscountrate") != null)
    			saleItem.setNdiscountrate(new UFDouble(btoolvos[i]
    			                                                .getAttributeValue("ndiscountrate").toString()));
    		//nitemdiscountrate
    		if (btoolvos[i].getAttributeValue("nitemdiscountrate") != null)
    			saleItem.setNitemdiscountrate(new UFDouble(btoolvos[i]
    			                                                    .getAttributeValue("nitemdiscountrate").toString()));
    		//nexchangeotobrate
    		if (btoolvos[i].getAttributeValue("nexchangeotobrate") != null)
    			saleItem.setNexchangeotobrate(new UFDouble(btoolvos[i]
    			                                                    .getAttributeValue("nexchangeotobrate").toString()));
    		//nexchangeotoarate
    		if (btoolvos[i].getAttributeValue("nexchangeotoarate") != null)
    			saleItem.setNexchangeotoarate(new UFDouble(btoolvos[i]
    			                                                    .getAttributeValue("nexchangeotoarate").toString()));
    		//ntaxrate
    		if (btoolvos[i].getAttributeValue("ntaxrate") != null)
    			saleItem.setNtaxrate(new UFDouble(btoolvos[i]
    			                                           .getAttributeValue("ntaxrate").toString()));
    		//noriginalcurprice
    		//nquoteoriginalcurprice
    		if (btoolvos[i].getAttributeValue("noriginalcurprice") != null) {
    			saleItem.setNoriginalcurprice(new UFDouble(btoolvos[i]
    			                                                    .getAttributeValue("noriginalcurprice").toString()));

    			saleItem.setNquoteoriginalcurprice(new UFDouble(btoolvos[i]
    			                                                         .getAttributeValue("noriginalcurprice").toString()));
    		}
    		//noriginalcurtaxprice
    		//Nquoteoriginalcurtaxprice
    		if (btoolvos[i].getAttributeValue("noriginalcurtaxprice") != null) {
    			saleItem.setNoriginalcurtaxprice(new UFDouble(btoolvos[i]
    			                                                       .getAttributeValue("noriginalcurtaxprice").toString()));
    			saleItem.setNquoteoriginalcurtaxprice(new UFDouble(btoolvos[i]
    			                                                            .getAttributeValue("noriginalcurtaxprice").toString()));
    		}
    		//noriginalcurnetprice
    		//nquoteoriginalcurnetprice
    		if (btoolvos[i].getAttributeValue("noriginalcurnetprice") != null) {
    			saleItem.setNoriginalcurnetprice(new UFDouble(btoolvos[i]
    			                                                       .getAttributeValue("noriginalcurnetprice").toString()));

    			saleItem.setNquoteoriginalcurnetprice(new UFDouble(btoolvos[i]
    			                                                            .getAttributeValue("noriginalcurnetprice").toString()));
    		}
    		//noriginalcurtaxnetprice
    		//Nquoteoriginalcurtaxnetprice
    		if (btoolvos[i].getAttributeValue("noriginalcurtaxnetprice") != null) {
    			saleItem.setNoriginalcurtaxnetprice(new UFDouble(btoolvos[i]
    			                                                          .getAttributeValue("noriginalcurtaxnetprice")
    			                                                          .toString()));
    			saleItem.setNquoteoriginalcurtaxnetprice(new UFDouble(
    					btoolvos[i]
    					         .getAttributeValue("noriginalcurtaxnetprice")
    					         .toString()));
    		}
    		//nprice
    		if (btoolvos[i].getAttributeValue("nprice") != null) {
    			saleItem.setNprice(new UFDouble(btoolvos[i].getAttributeValue(
    			"nprice").toString()));
    			saleItem.setNquoteprice(new UFDouble(btoolvos[i]
    			                                              .getAttributeValue("nprice").toString()));
    			saleItem.setNsubquoteprice(new UFDouble(btoolvos[i]
    			                                                 .getAttributeValue("nprice").toString()));
    		}
    		//ntaxprice
    		if (btoolvos[i].getAttributeValue("ntaxprice") != null) {
    			saleItem.setNtaxprice(new UFDouble(btoolvos[i]
    			                                            .getAttributeValue("ntaxprice").toString()));
    			saleItem.setNquotetaxprice(new UFDouble(btoolvos[i]
    			                                                 .getAttributeValue("ntaxprice").toString()));
    			saleItem.setNsubquotetaxprice(new UFDouble(btoolvos[i]
    			                                                    .getAttributeValue("ntaxprice").toString()));
    		}
    		//nnetprice
    		if (btoolvos[i].getAttributeValue("nnetprice") != null) {
    			saleItem.setNnetprice(new UFDouble(btoolvos[i]
    			                                            .getAttributeValue("nnetprice").toString()));
    			saleItem.setNquotenetprice(new UFDouble(btoolvos[i]
    			                                                 .getAttributeValue("nnetprice").toString()));
    			saleItem.setNsubquotenetprice(new UFDouble(btoolvos[i]
    			                                                    .getAttributeValue("nnetprice").toString()));
    		}
    		//ntaxnetprice
    		if (btoolvos[i].getAttributeValue("ntaxnetprice") != null) {
    			saleItem.setNtaxnetprice(new UFDouble(btoolvos[i]
    			                                               .getAttributeValue("ntaxnetprice").toString()));
    			saleItem.setNquotenetprice(new UFDouble(btoolvos[i]
    			                                                 .getAttributeValue("ntaxnetprice").toString()));
    			saleItem.setNsubtaxnetprice(new UFDouble(btoolvos[i]
    			                                                  .getAttributeValue("ntaxnetprice").toString()));
    		}
    		//setCprolineid
    		saleItem.setCprolineid((String) btoolvos[i]
    		                                         .getAttributeValue("cprolineid"));
    		//ct_manageid
    		if (!"3U".equals(cfirsttype)) {
    			saleItem.setCt_manageid((String) btoolvos[i]
    			                                          .getAttributeValue("ct_manageid"));

    			//设置发票默认数量
    			oTemp = btoolvos[i].getAttributeValue("noutnum");
    			if(oTemp != null) dOutNum = new UFDouble(oTemp.toString());
    			else dOutNum = new UFDouble(0);
    			oTemp = btoolvos[i].getAttributeValue("naccumwastnum");
    			if(oTemp != null) dAccumWastNum = new UFDouble(oTemp.toString());
    			else dAccumWastNum = new UFDouble(0);
    			oTemp = btoolvos[i].getAttributeValue("nsignnum");
    			if(oTemp != null) dSignNum = new UFDouble(oTemp.toString());
    			else dSignNum = new UFDouble(0);
    			oTemp = btoolvos[i].getAttributeValue("nrushnum");
    			if(oTemp != null) dRushNum = new UFDouble(oTemp.toString());
    			else dRushNum = new UFDouble(0);

    			dTemp = dOutNum.sub(dAccumWastNum).sub(dSignNum).sub(dRushNum);
    			saleItem.setNnumber(dTemp);
    			if(saleItem.getNqtscalefactor() != null && saleItem.getNqtscalefactor().doubleValue() != 0) saleItem.setNquotenumber(saleItem.getNnumber().div(saleItem.getNqtscalefactor()));
    			if(saleItem.getScalefactor() != null && saleItem.getScalefactor().doubleValue() != 0) saleItem.setNpacknumber(saleItem.getNnumber().div(saleItem.getScalefactor()));
    		}

    		//如果发票数量和订单数量相等，则金额和价税合计直接到发票中
    		bCalculated = false;
    		dTemp = new UFDouble(0);
    		oTemp = btoolvos[i].getAttributeValue("nnumber");
    		if(oTemp != null){
    			dTemp = new UFDouble(oTemp.toString());
    			if ("3U".equals(cfirsttype)) dTemp = dTemp.multiply(-1);
    		}
    		if(oTemp != null && saleItem.getNnumber() != null && saleItem.getNnumber().doubleValue()== dTemp.doubleValue()){
    			oTemp = btoolvos[i].getAttributeValue("noriginalcurmny"); 
    			if(oTemp != null){
    				saleItem.setNoriginalcurmny(new UFDouble(oTemp.toString()));
    				if ("3U".equals(cfirsttype)) saleItem.setNoriginalcurmny(saleItem.getNoriginalcurmny().multiply(-1));
    			}

    			oTemp = btoolvos[i].getAttributeValue("noriginalcursummny"); 
    			if(oTemp != null){
    				saleItem.setNoriginalcursummny(new UFDouble(oTemp.toString()));
    				if ("3U".equals(cfirsttype)) saleItem.setNoriginalcursummny(saleItem.getNoriginalcursummny().multiply(-1));
    			}

    			oTemp = btoolvos[i].getAttributeValue("nmny"); 
    			if(oTemp != null) saleItem.setNmny(new UFDouble(oTemp.toString()));
    			if ("3U".equals(cfirsttype)) saleItem.setNmny(saleItem.getNmny().multiply(-1));

    			oTemp = btoolvos[i].getAttributeValue("nsummny"); 
    			if(oTemp != null){
    				saleItem.setNsummny(new UFDouble(oTemp.toString()));
    				if ("3U".equals(cfirsttype)) saleItem.setNsummny(saleItem.getNsummny().multiply(-1));
    			}

    			oTemp = btoolvos[i].getAttributeValue("nassistcurmny"); 
    			if(oTemp != null){
    				saleItem.setNassistcurmny(new UFDouble(oTemp.toString()));
    				if ("3U".equals(cfirsttype)) saleItem.setNassistcurmny(saleItem.getNassistcurmny().multiply(-1));
    			}

    			oTemp = btoolvos[i].getAttributeValue("nassistcursummny"); 
    			if(oTemp != null){
    				saleItem.setNassistcursummny(new UFDouble(oTemp.toString()));
    				if ("3U".equals(cfirsttype)) saleItem.setNassistcursummny(saleItem.getNassistcursummny().multiply(-1));
    			}

    			bCalculated = true;
    		}

    		//根据数量计算金额，价税合计
    		//原币
    		if(!bCalculated) saleItem.setNoriginalcurmny(SoVoTools.mult(saleItem.getNoriginalcurnetprice(), saleItem.getNnumber()));            
    		saleItem.setNoriginalcurtaxmny(SoVoTools.mult(saleItem.getNoriginalcurmny(), SoVoTools.div(saleItem.getNtaxrate(), new UFDouble(100.0))));
    		if(!bCalculated) saleItem.setNoriginalcursummny(SoVoTools.add(saleItem.getNoriginalcurmny(), saleItem.getNoriginalcurtaxmny()));
    		saleItem.setNoriginalcurdiscountmny(SoVoTools.mult(saleItem.getNoriginalcurtaxprice(), saleItem.getNnumber()));
    		saleItem.setNoriginalcurdiscountmny(SoVoTools.sub(saleItem.getNoriginalcurdiscountmny(), saleItem.getNoriginalcursummny()));

    		//辅币
    		if(!bCalculated) saleItem.setNassistcurmny(SoVoTools.mult(saleItem.getNassistcurnetprice(), saleItem.getNnumber()));
    		saleItem.setNassistcurtaxmny(SoVoTools.mult(saleItem.getNassistcurmny(), SoVoTools.div(saleItem.getNtaxrate(),new UFDouble(100.0))));
    		if(!bCalculated) saleItem.setNassistcursummny(SoVoTools.add(saleItem.getNassistcurmny(), saleItem.getNassistcurtaxmny()));
    		saleItem.setNassistcurdiscountmny(SoVoTools.mult(saleItem.getNassistcurtaxprice(), saleItem.getNnumber()));
    		saleItem.setNassistcurdiscountmny(SoVoTools.sub(saleItem.getNassistcurdiscountmny(), saleItem.getNassistcursummny()));

    		//本币
    		if(!bCalculated) saleItem.setNmny(SoVoTools.mult(saleItem.getNnetprice(), saleItem.getNnumber()));
    		saleItem.setNtaxmny(SoVoTools.mult(saleItem.getNmny(), SoVoTools.div(saleItem.getNtaxrate(), new UFDouble(100.0))));
    		if(!bCalculated) saleItem.setNsummny(SoVoTools.add(saleItem.getNmny(), saleItem.getNtaxmny()));
    		saleItem.setNdiscountmny(SoVoTools.mult(saleItem.getNtaxprice(),saleItem.getNnumber()));
    		saleItem.setNdiscountmny(SoVoTools.sub(saleItem.getNdiscountmny(),saleItem.getNsummny()));

    		//冲减前金额原币
    		saleItem.setNsubsummny(saleItem.getNoriginalcursummny());
    		//冲减前本币金额
    		saleItem.setNsubcursummny(saleItem.getNsummny());

    		saleItem.setFrowstatus(SoVoConst.i1);

    	}

    	return retvos;
    }

}