package nc.bs.pp.ask;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.NamingException;

import nc.bs.framework.common.NCLocator;
import nc.bs.po.OrderImpl;
import nc.bs.po.OrderPubDMO;
import nc.bs.pr.pray.PraybillImpl;
import nc.bs.pu.pub.GetSysBillCode;
import nc.bs.pu.pub.PubDMO;
import nc.bs.pu.pub.PubImpl;
import nc.bs.pub.SystemException;
import nc.bs.scm.pub.SqlMethod;
import nc.itf.pp.ask.IAsk;
import nc.itf.pu.pub.fw.LockTool;
import nc.itf.scm.cenpur.service.ChgDataUtil;
import nc.itf.scm.pub.bill.IScm;
import nc.itf.uap.rbac.IPowerManageQuery;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.ui.bd.b21.CurrParamQuery;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pp.ask.AskbillHeaderVO;
import nc.vo.pp.ask.AskbillItemBVO;
import nc.vo.pp.ask.AskbillItemMergeVO;
import nc.vo.pp.ask.AskbillItemVO;
import nc.vo.pp.ask.AskbillItemVendorVO;
import nc.vo.pp.ask.AskbillMergeVO;
import nc.vo.pp.ask.AskbillVO;
import nc.vo.pp.ask.EffectPriceParaVO;
import nc.vo.pp.ask.EffectPriceVO;
import nc.vo.pp.ask.IAskBillStatus;
import nc.vo.pp.ask.IBillType;
import nc.vo.pp.ask.IOperation;
import nc.vo.pp.ask.IPosition;
import nc.vo.pp.ask.PriceExchangeException;
import nc.vo.pp.ask.PriceauditBVO;
import nc.vo.pp.ask.PriceauditBb1VO;
import nc.vo.pp.ask.PriceauditHeaderVO;
import nc.vo.pp.ask.PriceauditMergeVO;
import nc.vo.pp.ask.PriceauditVO;
import nc.vo.pp.ask.VendorInvPriceVO;
import nc.vo.pp.ask.VendorVO;
import nc.vo.pp.price.QuoteConHeaderVO;
import nc.vo.pp.price.QuoteConItemVO;
import nc.vo.pp.price.QuoteConVO;
import nc.vo.pp.price.StatParaVO;
import nc.vo.pp.price.StockExecHeaderVO;
import nc.vo.pp.price.StockExecItemVO;
import nc.vo.pp.price.StockExecVO;
import nc.vo.pp.price.StockVarHeaderVO;
import nc.vo.pp.price.StockVarItemVO;
import nc.vo.pp.price.StockVarVO;
import nc.vo.pu.util.PuUtils;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ProductCode;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.util.StringUtil;
import nc.vo.scm.cenpur.service.ChgDocPkVO;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.ctpo.ParaCtToPoRewriteVO;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.ClassNameConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.rewrite.ParaRewriteVO;
import nc.vo.uap.bd.rbac.ResourceConst;
import nc.vo.uap.rbac.IOrgType;
import nc.vo.uap.rbac.power.UserPowerQueryVO;

public class AskbillImpl implements IAsk {
	/**
	 * AskbillImpl ������ע�⡣
	 */
	public AskbillImpl() {
		super();
	}
	/**
	 * �������������ݿ������һ��VO����(ѯ���۵�)
	 * 
	 * �������ڣ�(2001-8-4)
	 * 
	 * @param  Vector  0---AskbillHeaderVO
	 *                  1---AskbillItemVO[]
	 *                  2---AskbillItemBVO[]
	 *                  3---AskbillItemVendorVO[]
	 * @return Vector  0---AskbillHeaderVO
	 *                  1---AskbillItemVO[]
	 *                  2---AskbillItemBVO[]
	 *                  3---AskbillItemVendorVO[]     
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public Vector doSaveForAskBill(Vector resultV) throws BusinessException {
		AskbillHeaderVO headerVO = (AskbillHeaderVO) resultV.get(0);
		AskbillItemVO[] itemVO = (AskbillItemVO[]) resultV.get(1);
		AskbillItemBVO[] itemBVO = (AskbillItemBVO[]) resultV.get(2);
		AskbillVO askbill = new AskbillVO();
		askbill.setParentVO(headerVO);
		askbill.setChildrenVO(itemVO);
		String sHeadKey = headerVO.getCaskbillid();
		if (headerVO == null || itemVO == null) {
			return null;
		}
        //����Զ�����
	    IScm srv = (IScm) NCLocator.getInstance().lookup(IScm.class.getName());    
	    srv.checkDefDataType(askbill);
	    
		UFBoolean isLockSuccess = null;
		PubDMO pubDmo = null;
		
		try {
			pubDmo = new PubDMO();
			isLockSuccess = pubDmo.lockPkForVo(askbill);
			if (isLockSuccess != null && isLockSuccess.booleanValue() ) { 
				checkVoNoChanged(headerVO, itemVO, itemBVO,
						ClassNameConst._29_AGGR_CLASS_NAME_);
			}
			ParaRewriteVO paraVo = new ParaRewriteVO();
			PraybillImpl prayBO = new PraybillImpl();
			Vector rowIdsTemp = new Vector();
			Vector headIdsTemp = new Vector();
			Vector numTemp = new Vector();
			String[] rowIds = null;
			 //���б���
		    AskbillDMO dmo = new AskbillDMO();
		    if(sHeadKey == null || sHeadKey.length() == 0){//����
			  resultV = dmo.insertForAskBill(resultV);
                //��д�ۼ�����ѯ������
			    AskbillItemVO[] itemVOs = (AskbillItemVO[]) resultV.get(1);
				rowIds = new String[itemVOs.length];
				for (int j = 0; j < itemVOs.length; j++) {
					if (itemVOs[j].getCupsourcebilltype() != null
							&& itemVOs[j].getCupsourcebilltype().equals(IBillType.PRAYBILL)) {
						rowIdsTemp.add(itemVOs[j].getCupsourcebillrowid());
						headIdsTemp.add(itemVOs[j].getCupsourcebillid());
						numTemp.add(itemVOs[j].getNasknum());
					}
				}
				if (rowIdsTemp.size() > 0 && numTemp.size() > 0
						&& rowIdsTemp.size() == numTemp.size()) {
					updateNaccAskNum(paraVo, prayBO, rowIdsTemp, IOperation.ADD);
				}
		    }else{//�޸�
              //V501�޸�ʱ�޸ĵ��ݺ�
		      Vector oldV = findByPrimaryKeyForAskBill(sHeadKey);
		      AskbillHeaderVO headerVOOld = (AskbillHeaderVO) oldV.get(0);
			  AskbillItemVO[] itemVOOld = (AskbillItemVO[]) oldV.get(1);
			  AskbillVO askbillOld = new AskbillVO();
			  askbillOld.setParentVO(headerVOOld);
			  askbillOld.setChildrenVO(itemVOOld);
			  new GetSysBillCode().setBillNoWhenModify(askbill, askbillOld, "vaskbillcode");
			  ((AskbillHeaderVO) resultV.get(0)).setVaskbillcode(((AskbillHeaderVO)askbill.getParentVO()).getVaskbillcode());
		      resultV = dmo.updateForAskBill(resultV);
		      Vector vFroRwForNew = (Vector)resultV.get(4);
			  Vector vFroRwForDel = (Vector)resultV.get(5);
              //��д�빺�����ɼ۸������������ֶ�
				// ���������
				if (vFroRwForNew.size() > 0 ) {
					rowIds = new String[vFroRwForNew.size()];
					vFroRwForNew.copyInto(rowIds);
					paraVo.setCBodyIdArray(rowIds);
					updateNaccAskNum(paraVo, prayBO, vFroRwForNew,IOperation.ADD);
				}
				// ɾ��
				if (vFroRwForDel.size() > 0 ) {
					rowIds = new String[vFroRwForNew.size()];
					vFroRwForNew.copyInto(rowIds);
					paraVo.setCBodyIdArray(rowIds);
					updateNaccAskNum(paraVo, prayBO, vFroRwForDel,IOperation.DELETE);
				}
				//V501�޸�ʱ�޸ĵ��ݺ�--���˵��ݺ�
				new nc.bs.pu.pub.GetSysBillCode().returnBillNoWhenModify(askbill, askbillOld, "vaskbillcode");
		    }
		} catch (Exception e) {
			PubDMO.throwBusinessException(
					"nc.bs.pp.askbillImpl.doSaveForAskBill", e);
		} finally {
			// ��ҵ����
			if (isLockSuccess != null && isLockSuccess.booleanValue()) {
				try {
					pubDmo.freePkForVo(askbill);
				} catch (Exception e) {
					nc.bs.pu.pub.PubDMO.throwBusinessException(e);
				}
			}
		}
		
		return resultV;
	}
	/**
	 * ��д�ۼ�����ѯ������
	 * @param paraVo
	 * @param prayBO
	 * @param rowIdsTemp
	 * @param headIdsTemp
	 * @param numTemp
	 * @throws BusinessException
	 */
	private void updateNaccAskNum(ParaRewriteVO paraVo, PraybillImpl prayBO, Vector rowIdsTemp, int iOperation) throws BusinessException {
		String[] rowIds = null;
		rowIds = new String[rowIdsTemp.size()];
		rowIdsTemp.copyInto(rowIds);
		paraVo.setCBodyIdArray(rowIds);
		prayBO.updateNaccAskNum(paraVo, new Integer(iOperation).toString());
	}
	/**
	 * �������������ݿ������һ��VO����(�۸�������)
	 * 
	 * �������ڣ�(2001-8-4)
	 * 
	 * @param  Vector  0---PriceauditHeaderVO
	 *                  1---PriceauditBVO[]
	 *                  2---PriceauditBb1VO[]
	 * @return Vector  0---PriceauditHeaderVO
	 *                  1---PriceauditBVO[]
	 *                  2---PriceauditBb1VO[] 
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public Vector doSaveForPriceAuditBill(Vector resultV) throws BusinessException {
		PriceauditHeaderVO headerVO = (PriceauditHeaderVO) resultV.get(0);
		PriceauditBVO[] itemVO = (PriceauditBVO[]) resultV.get(1);
		PriceauditBb1VO[] itemBVO = (PriceauditBb1VO[]) resultV.get(2);
		PriceauditVO priceauditbill = new PriceauditVO();
		priceauditbill.setParentVO(headerVO);
		priceauditbill.setChildrenVO(itemVO);
		String sHeadKey = headerVO.getPrimaryKey();
		if (headerVO == null || itemVO == null) {
			return null;
		}
        //����Զ�����
	    IScm srv = (IScm) NCLocator.getInstance().lookup(IScm.class.getName());    
	    srv.checkDefDataType(priceauditbill);
	    
		UFBoolean isLockSuccess = null;
		PubDMO pubDmo = null;
		try {
			pubDmo = new PubDMO();
			isLockSuccess = pubDmo.lockPkForVo(priceauditbill);
			if (isLockSuccess != null && isLockSuccess.booleanValue() ) { 
				checkVoNoChanged(headerVO, itemVO, itemBVO,
						ClassNameConst._28_AGGR_CLASS_NAME_);
			}
            //���б���
			AskbillDMO dmo = new AskbillDMO();
			PraybillImpl prayBO = new PraybillImpl();
			ParaRewriteVO paraVo = new ParaRewriteVO();
			Vector rowIdsTemp = new Vector();
			Vector headIdsTemp = new Vector();
			Vector numTemp = new Vector();
//			String[] rowIds = null;
			Vector upPk = new Vector();
			Hashtable itemTempH = new Hashtable();
			if(sHeadKey == null  || sHeadKey.length() == 0){
			    resultV = dmo.insertForPriceAuditBill(resultV);
			    PriceauditBVO[] itemVOs = (PriceauditBVO[]) resultV.get(1);
			    Vector rowIdsFromAskBill = (Vector) resultV.get(3);
                //��д���ɼ۸�����������
//				rowIds = new String[itemVOs.length];
				for (int j = 0; j < itemVOs.length; j++) {
					if (itemVOs[j].getCupsourcebilltype() != null
							&& itemVOs[j].getCupsourcebilltype().equals(IBillType.PRAYBILL)) {// �빺ת�۸�������
						rowIdsTemp.add(itemVOs[j].getCupsourcebillrowid());
						headIdsTemp.add(itemVOs[j].getCupsourcebillid());
						numTemp.add(itemVOs[j].getNnum());
					} else if (itemVOs[j].getCupsourcebilltype() != null
							&& itemVOs[j].getCupsourcebilltype().equals(IBillType.ASKBILL)) {// ѯ����ת�۸�������
						upPk.add(itemVOs[j].getCupsourcebillrowid());
						if (itemVOs[j].getNnum() != null
								&& itemVOs[j].getNnum().toString().trim().length() > 0) {
							itemTempH.put(itemVOs[j].getCupsourcebillrowid(),
							    PuPubVO.getUFDouble_NullAsZero(itemVOs[j].getNnum()));
						} else {
							itemTempH.put(itemVOs[j].getCupsourcebillrowid(),
									new UFDouble(0.00));
						}
					}
				}
				// ѯ����ת�۸�����������Դ���빺������Ҳ��Ҫ��д
				if (upPk.size() > 0) {
					AskbillItemVO itemT = null;
                    //��ѯѯ���۵���Դ���빺������
					Hashtable result = querySourceInfoForRwPray(dmo, upPk);
					if (result.size() > 0) {
						for (int i = 0; i < upPk.size(); i++) {
							itemT = (AskbillItemVO) result.get(upPk.get(i));
							rowIdsTemp.add(itemT.getCupsourcebillrowid());
							headIdsTemp.add(itemT.getCupsourcebillid());
							numTemp.add(itemTempH.get(upPk.get(i)));
						}
					}
				}
				// ��д�빺�����ɼ۸������������ֶ�
				if (rowIdsTemp.size() > 0 && numTemp.size() > 0
						&& rowIdsTemp.size() == numTemp.size()) {
					updateNaccPriceAuditNum(prayBO, paraVo, rowIdsTemp,IOperation.ADD);
				}
				// ��дѯ���۵���ɱ�־
				ReWriteAskBillStatus(rowIdsFromAskBill, 0);
			}else{
				 //V501�޸�ʱ�޸ĵ��ݺ�
			      Vector oldV = findByPrimaryKeyForPriceAuditBill(sHeadKey);
			      PriceauditHeaderVO headerVOOld = (PriceauditHeaderVO) oldV.get(0);
				  PriceauditBVO[] itemVOOld = (PriceauditBVO[]) oldV.get(1);
				  PriceauditVO priceauditbillOld = new PriceauditVO();
				  priceauditbillOld.setParentVO(headerVOOld);
				  priceauditbillOld.setChildrenVO(itemVOOld);
				  new GetSysBillCode().setBillNoWhenModify(priceauditbill, priceauditbillOld, "vpriceauditcode");
				  ((PriceauditHeaderVO) resultV.get(0)).setVpriceauditcode(((PriceauditHeaderVO)priceauditbill.getParentVO()).getVpriceauditcode());
				resultV = dmo.updateForPriceAuditBill(resultV);	
				PriceauditBVO[] itemVOsForRw = (PriceauditBVO[])resultV.get(3);
                //��д���ɼ۸�����������
				Vector rowIdsTempForInsOrUpd = new Vector();
				Vector headIdsTempForInsOrUpd = new Vector();
				Vector numTempForInsOrUpd = new Vector();
				Vector rowIdsTempForDel = new Vector();
				Vector headIdsTempForDel = new Vector();
				Vector numTempForDel = new Vector();
				Hashtable itemTempHForInsOrUpd = new Hashtable();
				Hashtable itemTempHForDel = new Hashtable();
				if (itemVOsForRw != null && itemVOsForRw.length > 0) {
					for (int j = 0; j < itemVOsForRw.length; j++) {
						if (itemVOsForRw[j].getCupsourcebilltype() != null
								&& itemVOsForRw[j].getCupsourcebilltype().equals(IBillType.PRAYBILL)) {// �빺ת�۸�������
							if (itemVOsForRw[j].getStatus() == VOStatus.NEW
									|| itemVOsForRw[j].getStatus() == VOStatus.UPDATED) {
								rowIdsTempForInsOrUpd.add(itemVOsForRw[j]
										.getCupsourcebillrowid());
								headIdsTempForInsOrUpd.add(itemVOsForRw[j]
										.getCupsourcebillid());
								numTempForInsOrUpd.add(itemVOsForRw[j].getNnum());
							} else if (itemVOsForRw[j].getStatus() == VOStatus.DELETED) {
								rowIdsTempForDel.add(itemVOsForRw[j]
										.getCupsourcebillrowid());
								headIdsTempForDel.add(itemVOsForRw[j]
										.getCupsourcebillid());
								numTempForDel.add(itemVOsForRw[j].getNnum());
							}
						} else if (itemVOsForRw[j].getCupsourcebilltype() != null
								&& itemVOsForRw[j].getCupsourcebilltype().equals(IBillType.ASKBILL)) {// ѯ����ת�۸�������
							upPk.add(itemVOsForRw[j].getCupsourcebillrowid());
							if (itemVOsForRw[j].getStatus() == VOStatus.NEW
									|| itemVOsForRw[j].getStatus() == VOStatus.UPDATED) {
								itemTempHForInsOrUpd.put(itemVOsForRw[j]
										.getCupsourcebillrowid(), PuPubVO.getUFDouble_NullAsZero(itemVOsForRw[j]
										.getNnum()));
							} else if (itemVOsForRw[j].getStatus() == VOStatus.DELETED) {
								itemTempHForDel.put(itemVOsForRw[j]
										.getCupsourcebillrowid(), PuPubVO.getUFDouble_NullAsZero(itemVOsForRw[j]
										.getNnum()));
							}
						}
					}
				}
				// ѯ����ת�۸�����������Դ���빺������Ҳ��Ҫ��д
				if (upPk.size() > 0) {
					AskbillItemVO itemT = null;
                    //��ѯѯ���۵���Դ���빺������
					Hashtable result = querySourceInfoForRwPray(dmo, upPk);
					if (result.size() > 0) {
						for (int i = 0; i < upPk.size(); i++) {
							itemT = (AskbillItemVO) result.get(upPk.get(i));
							rowIdsTempForInsOrUpd.add(itemT.getCupsourcebillrowid());
							headIdsTempForInsOrUpd.add(itemT.getCupsourcebillid());
							if (itemTempHForInsOrUpd.get(upPk.get(i)) != null) {
								numTempForInsOrUpd.add(itemTempHForInsOrUpd.get(upPk
										.get(i)));
							} else {
								numTempForDel.add(itemTempHForDel.get(upPk.get(i)));
							}
						}
					}
				}
				// ��д�빺�����ɼ۸������������ֶ�
				// ���������
				if (rowIdsTempForInsOrUpd.size() > 0 && numTempForInsOrUpd.size() > 0
						&& rowIdsTempForInsOrUpd.size() == numTempForInsOrUpd.size()) {
					updateNaccPriceAuditNum(prayBO, paraVo, rowIdsTempForInsOrUpd, IOperation.ADD);
				}
				// ɾ��
				if (rowIdsTempForDel.size() > 0 && numTempForDel.size() > 0
						&& rowIdsTempForDel.size() == numTempForDel.size()) {
					updateNaccPriceAuditNum(prayBO, paraVo, rowIdsTempForDel, IOperation.DELETE);
					
				}
                //V501�޸�ʱ�޸ĵ��ݺ�--���˵��ݺ�
				new nc.bs.pu.pub.GetSysBillCode().returnBillNoWhenModify(priceauditbill, priceauditbillOld, "vpriceauditcode");
			}
		} catch (Exception e) {

			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.doSaveForPriceAuditBill", e);
		} finally {
			// ��ҵ����
			if (isLockSuccess != null && isLockSuccess.booleanValue()) {
				try {
					pubDmo.freePkForVo(priceauditbill);
				} catch (Exception e) {
					nc.bs.pu.pub.PubDMO.throwBusinessException(e);
				}
			}
		}
		return resultV;
	}
	/**
	 * ��ѯѯ���۵���Դ���빺������
	 * @param dmo
	 * @param upPk
	 * @return Hashtable key--ѯ���۵��ӱ�����--caskbill_bid
	 *                    value--ѯ���۵��ӱ�VO--AskbillItemVO
	 * @throws SQLException
	 * @throws BusinessException
	 */
	private Hashtable querySourceInfoForRwPray(AskbillDMO dmo, Vector upPk) throws SQLException, BusinessException {
		Hashtable result = null;
		StringBuffer sqlCondition = new StringBuffer();
//		AskbillItemVO itemT = null;
		for (int i = 0; i < upPk.size(); i++) {
			if (i < upPk.size() - 1) {
				sqlCondition.append("'" + upPk.get(i).toString().trim()
						+ "',");
			} else {
				sqlCondition.append("'" + upPk.get(i).toString().trim()
						+ "'");
			}
		}
		// ��ѯѯ���۵���Դ���빺������
		result = dmo.querySourceInfoForRwPray(sqlCondition.toString());
		return result;
	}
	/**
	 * ��д�빺�����ɼ۸������������ֶ�
	 * @param prayBO--PraybillImpl
	 * @param paraVo--��д����VO
	 * @param rowIdsTemp--��д��Vector
	 * @throws BusinessException
	 */
	private void updateNaccPriceAuditNum(PraybillImpl prayBO, ParaRewriteVO paraVo, Vector rowIdsTemp, int iOperation) throws BusinessException {
		String[] rowIds = null;
		rowIds = new String[rowIdsTemp.size()];
		rowIdsTemp.copyInto(rowIds);
		paraVo.setCBodyIdArray(rowIds);
		prayBO.updateNaccPriceAuditNum(paraVo, new Integer(iOperation).toString());
	}
	/**
	 * �������������ݿ������һ��VO����(ѯ���۵�����)
	 * 
	 * �������ڣ�(2001-8-4)
	 * 
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public Vector updateMyForExcelToBill(Vector v) throws BusinessException {
		Vector itemV = (Vector) v.get(0);
		Vector itemBV = (Vector) v.get(1);
		boolean isNeedCheck = ((UFBoolean)v.get(3)).booleanValue();
		AskbillItemVO[] itemVO = null;
		AskbillItemBVO[] itemBVO = null;
		if (itemV != null && itemV.size() > 0) {
			itemVO = new AskbillItemVO[itemV.size()];
			itemV.copyInto(itemVO);
		}
		if (itemBV != null && itemBV.size() > 0) {
			itemBVO = new AskbillItemBVO[itemBV.size()];
			itemBV.copyInto(itemBVO);
		}
		Vector result = new Vector();
		if (itemBVO == null && itemVO == null) {
			return null;
		}
		// �жϲ���
		// ���б���
		try {
			//���ñ��۵�������Ϣ
			String errorMsg = checkPriceTaxInfo(itemBV,isNeedCheck);
			if( errorMsg == null){
				AskbillDMO dmo = new AskbillDMO();
				dmo.updateMyForExcelToBill(v);
				result.add("success");
			}else {
				result.add("false");
				result.add(errorMsg);
			}
		} catch (Exception e) {
			result.add("false");
//			if(e instanceof PriceExchangeException){
//				result.add(e);
//			}
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.updateMyForExcelToBill", e);
		}
		return result;
	}
	/**
	 * ���ñ��۵�������Ϣ
	 * ���ֻ���ں�˰���ۻ�����˰���ۣ�����������˰�ʼ�������һ�����ۣ����û������˰�ʣ���ȡ����Ĭ��˰�ʣ�
	 * �����˰����˰���۶������ˣ���ô��˰�ʲ�����ʱ������˰�ʣ�˰�ʴ�����������߹�ϵ�Ƿ���ȷ������ȷ���׳��쳣
	 * ������Ӧ˰��ӡ�Ӧ˰�ں�
	 * price = taxprice / (1 + taxrate/100)
	 * taxprice = price * (1 + taxrate/100)
	 * taxrate = 100 * (taxprice / price - 1)
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * @param itemBV
	 * @throws PriceExchangeException
	 * <p>
	 * @author donggq
	 * @time 2008-11-28 ����03:16:38
	 */
	  private String checkPriceTaxInfo(Vector itemBV,boolean isNeedCheck) throws BusinessException{
		  if (itemBV == null || itemBV.size() == 0){
			  return null;
		  }
		  HashMap hmIdMapRate = null;
		  //��Ҫȡ����˰�ʵ�bbid
		  Vector<String> vitembid = new Vector<String>();
		  for (Object object : itemBV) {
			  //˰��Ϊ�գ�������˰����˰����������һ��Ϊ��ʱ��ȡ����˰�ʡ�
			if(PuPubVO.getUFDouble_ZeroAsNull(((AskbillItemBVO)object).getNtaxrate()) == null
					&& ((PuPubVO.getUFDouble_ZeroAsNull(((AskbillItemBVO)object).getNquoteprice()))== null
							|| (PuPubVO.getUFDouble_ZeroAsNull(((AskbillItemBVO)object).getNquotetaxprice()))== null)){
				vitembid.add(((AskbillItemBVO)object).getPrimaryKey());
			}
		  }
		  // ��ȡ˰��
		  try {
			  if(vitembid.size() > 0){
				  hmIdMapRate = new AskbillDMO().getTaxrateByAskBB1id(vitembid.toArray(new String[vitembid.size()]));
			  }
		  } catch (Exception se) {
			  PubDMO.throwBusinessException(se);
		  }
		  //����˰��
		  for (Object object : itemBV) {
			  AskbillItemBVO itembvo = (AskbillItemBVO)object;
			  //˰��Ϊ�գ�������˰����˰����������һ��Ϊ��ʱ��ȡ����˰�ʡ�
			if(PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNtaxrate()) == null
					&& ((PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNquoteprice()))== null
							|| (PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNquotetaxprice()))== null)){
				itembvo.setNtaxrate(PuPubVO.getUFDouble_ZeroAsNull(hmIdMapRate.get(itembvo.getPrimaryKey())));
			}
			if(PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNquoteprice()) != null
					&& PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNquotetaxprice()) != null
					&& PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNtaxrate()) == null){
				//����˰��
				calculateTaxRate(itembvo);
			}else if (PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNquoteprice()) == null
					&& PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNquotetaxprice()) != null
					&& PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNtaxrate()) != null){
				//������˰
				calculatePrice(itembvo);
			}else if (PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNquoteprice()) != null
					&& PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNquotetaxprice()) == null
					&& PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNtaxrate()) != null){
				//���㺬˰
				calculateTaxPrice(itembvo);
			}else if (PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNquoteprice()) != null
					&& PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNquotetaxprice()) != null
					&& PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNtaxrate()) != null){
				if(isNeedCheck ){
					UFDouble price,taxPrice,taxRate,tempTaxPrice;
					price = itembvo.getNquoteprice();
					taxPrice = itembvo.getNquotetaxprice();
					taxRate = itembvo.getNtaxrate();
					tempTaxPrice = price.multiply((taxRate.div(100)).add(1));
					if(tempTaxPrice.compareTo(taxPrice) != 0)
						return "��˰����  != ��˰���� * ( 1 + ˰��/100 )";
				}
			}else if (PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNquoteprice()) != null
          && PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNquotetaxprice()) == null
          && PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNtaxrate()) == null){
			  itembvo.setNquotetaxprice(itembvo.getNquoteprice());
			}else if (PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNquoteprice()) == null
          && PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNquotetaxprice()) != null
          && PuPubVO.getUFDouble_ZeroAsNull(itembvo.getNtaxrate()) == null){
			  itembvo.setNquoteprice(itembvo.getNquotetaxprice());
			}
		}
		  return null;
	  }
	  
	  //���㺬˰����
	  private void calculateTaxPrice(AskbillItemBVO itembvo) {
		  UFDouble price, taxRate;
		  price = itembvo.getNquoteprice();
		  taxRate = itembvo.getNtaxrate();
		  itembvo.setNquotetaxprice(price.multiply((taxRate.div(100)).add(1)));
	  }
	  //������˰����
	  private void calculatePrice(AskbillItemBVO itembvo){
		  UFDouble taxPrice, taxRate;
		  taxPrice = itembvo.getNquotetaxprice();
		  taxRate = itembvo.getNtaxrate();
		  itembvo.setNquoteprice(taxPrice.div((taxRate.div(100)).add(1)));
	  }
	  //����˰��
	  private void calculateTaxRate(AskbillItemBVO itembvo){
		  UFDouble price,taxPrice;
		  taxPrice = itembvo.getNquotetaxprice();
		  price = itembvo.getNquoteprice();
		  itembvo.setNtaxrate((new UFDouble(100)).multiply((taxPrice.div(price)).sub(1)));
	  }

	/**
	 * @���ܣ�������Ա������ȡ����Ա���ڲ�������
	 * @���ߣ���־ƽ �������ڣ�(2001-9-14 10:59:44)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @return java.lang.String
	 * @param psnid
	 *            java.lang.String
	 */
	public String getPkDeptByPkPsnForAsk(String pk_psndoc)
			throws BusinessException {
		String pk_deptdoc = null;
		try {
			AskbillDMO dmo = new AskbillDMO();
			pk_deptdoc = dmo.getPkDeptByPkPsn(pk_psndoc);
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.getPkDeptByPkPsnForAsk", e);
		}
		return pk_deptdoc;
	}

	/**
	 * @���ܣ���ѯѯ�۵���ϸ
	 * @˵���� 1.������ֶ�
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @param status
	 *            boolean[]
	 */
	public VendorVO[] queryVendorDetail(String logCorp,String logUser) throws BusinessException {

		VendorVO[] vendoritems = null;
		try {
			AskbillDMO dmo = new AskbillDMO();
			String datPowerCon = getWherePartForQueryVendor(logCorp,logUser);
			if(datPowerCon != null && datPowerCon.trim().length() > 0){
			  vendoritems = dmo.queryVendorDetail(logCorp,logUser,datPowerCon);
			}else{
			  vendoritems = dmo.queryVendorDetail(logCorp);
			}
			if (vendoritems == null)
				return null;
			if (vendoritems.length <= 0)
				return null;

		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryVendorDetail", e);
		}

		return vendoritems;
	}
	/**
	 * @���ܣ���ѯѯ�۵���ϸ
	 * @˵���� 1.������ֶ�
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @param status
	 *            boolean[]
	 */
	public String[] queryForVendorSelected(String logCorp,String[] vendorMangIDs) throws BusinessException {

		String[] ntotalmarks = null;
		try {
			AskbillDMO dmo = new AskbillDMO();
            //������ʱ��
			nc.bs.scm.pub.TempTableDMO dmoTmpTable = new nc.bs.scm.pub.TempTableDMO();			
			String conditionFroInv = dmoTmpTable.insertTempTable(
				vendorMangIDs,
				nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_ASK_10,
				"cmangid") ;
			ntotalmarks = dmo.queryForVendorDetail(logCorp,vendorMangIDs,conditionFroInv);
			if (ntotalmarks == null || ntotalmarks.length <= 0)
				return null;

		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryForVendorSelected", e);
		}

		return ntotalmarks;
	}
	/**
	 * @���ܣ���ѯѯ�۵���ϸ
	 * @˵���� 1.������ֶ�
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @param status
	 *            boolean[]
	 */
	public VendorVO[] queryVendorDetail(String logCorp) throws BusinessException {

		VendorVO[] vendoritems = null;
		try {
			AskbillDMO dmo = new AskbillDMO();
			vendoritems = dmo.queryVendorDetail(logCorp);
			if (vendoritems == null)
				return null;
			if (vendoritems.length <= 0)
				return null;

		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryVendorDetail", e);
		}

		return vendoritems;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(01-4-18 20:05:25)
	 * @return java.lang.String
	 */
	public String getWherePartForQueryVendor(String logCorp,String logUser) {
		IPowerManageQuery pwerSrv = (IPowerManageQuery) NCLocator.getInstance().lookup(IPowerManageQuery.class.getName());
		UserPowerQueryVO queryVO = new UserPowerQueryVO();
		queryVO.setOrgTypeCode(IOrgType.COMPANY_TYPE);
		queryVO.setCorpPK(logCorp);
		queryVO.setOrgPK(logCorp);
		queryVO.setResouceId(ResourceConst.CUSTOMER_SUPPLIER );
		queryVO.setUserPK(logUser);
		String strSubSqlPower = "";
		try{
			strSubSqlPower = pwerSrv.getSql4UserPower(queryVO);
			if(strSubSqlPower != null && strSubSqlPower.trim().length() > 0){
				strSubSqlPower = " bd_cumandoc.pk_cumandoc in (" + strSubSqlPower + ") ";
			}
		}catch(BusinessException e){
			SCMEnv.out("ע�⣺������������Ȩ�ޱ����ԣ��쳣��Ϣ���£�");/*-=notranslate=-*/
			SCMEnv.out(e);
		}
		 
		return strSubSqlPower;
	}
	/**
	 * @���ܣ���ѯѯ�۵���ϸ
	 * @˵���� 1.������ֶ�
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @param status
	 *            boolean[]
	 */
	public Hashtable querySourceInfoForGenOrder(Vector v)
			throws BusinessException {

		Hashtable result = null;
		String sqlCondition = null;
		String[] caskbill_bids = null;
		if (v.size() > 0) {
			caskbill_bids = new String[v.size()];
			for (int i = 0; i < v.size(); i++) {
				caskbill_bids[i] = (String)v.get(i);
			}
		}
		try {
			nc.bs.scm.pub.TempTableDMO dmoTmpTable = new nc.bs.scm.pub.TempTableDMO();			
			sqlCondition = dmoTmpTable.insertTempTable(
				caskbill_bids,
				nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_ASK_16,
				"caskbill_bid") ;
			AskbillDMO dmo = new AskbillDMO();
			result = dmo.querySourceInfoForGenOrder(sqlCondition);
			if (result == null || result.size() <= 0)
				return null;

		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.querySourceInfoForGenOrder", e);
		}

		return result;
	}

	/**
	 * ���ܣ���ȡ�빺����ID��Ӧ���빺��ҵ���������� ����: ArrayList �빺����ID ���أ�ArrayList
	 * �빺����ID��Ӧ���빺��ҵ���������� ���ߣ���־ƽ ������2004-6-3 19:52:07)
	 * 
	 */
	public Hashtable getBusiIdForOrd(ArrayList listPara)
			throws BusinessException {

		if (listPara == null)
			return null;
		if (listPara.size() == 0)
			return new Hashtable();
		Hashtable aryRslt = null;
		try {
			AskbillDMO dmo = new AskbillDMO();
			aryRslt = dmo.getBusiIdForOrd(listPara);

			if (aryRslt == null || aryRslt.size() != listPara.size()) {
				SCMEnv.out("��ѯ�빺����ID��ȡ��Ӧ���빺��ҵ����������ʱ��������NULL");
				return null;
			}
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.getBusiIdForOrd", e);
		}
		return aryRslt;
	}

	/**
	 * @���ܣ���ѯ��Ч�۸�
	 * @˵���� 1.������ֶ�
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @param status
	 *            boolean[]
	 */
	public Hashtable getEffectAskPrice(EffectPriceParaVO effectPricePara)
			throws BusinessException {

		if (effectPricePara == null) {
			return null;
		}
		PubImpl pubImpl = new PubImpl();
		//��˾
		String pk_corp = effectPricePara.getPk_corp();
		//����
		String data = effectPricePara.getDate().toString();
		//����
		String ccurrencytypeid = effectPricePara.getCcurrencytypeid();
		if (ccurrencytypeid == null) {
//			BusinessCurrencyRateUtil currArith = null;
			try {
//				currArith = new BusinessCurrencyRateUtil(pk_corp);
				ccurrencytypeid = CurrParamQuery.getInstance().getLocalCurrPK(pk_corp);
			} catch (Exception e) {
				// TODO �Զ����� catch ��
				SCMEnv.out(e.getMessage());
				PubDMO.throwBusinessException(
						"nc.bs.pp.AskbillImpl.getEffectAskPrice", e);
			}
		}
		
		// cmangids-->cbaseids
		String[] cmangids = effectPricePara.getCmangid();
		String[] cbaseids = null;
		Object[][] retOb = pubImpl.queryArrayValue("bd_invmandoc",
				"pk_invmandoc", new String[] { "pk_invbasdoc" }, cmangids);
		Hashtable paraForPrice = new Hashtable();
		Vector tempV = new Vector();
		if (retOb != null) {
			String cbaseid = null;
			for (int i = 0; i < retOb.length; i++) {
				if (retOb[i] != null && retOb[i][0] != null) {
					cbaseid = (String) retOb[i][0];
					paraForPrice.put(cbaseid, cmangids[i]);
					tempV.add(cbaseid);
				}
			}
		}
		if (tempV.size() > 0) {
			cbaseids = new String[tempV.size()];
			tempV.copyInto(cbaseids);
		}
		
		boolean isStartCT = false;
		EffectPriceVO[] effectPriceVOsFromCon = null;
		//�жϺ�ͬ�Ƿ�����
		 ICreateCorpQueryService ccSrv = (ICreateCorpQueryService) NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
		 isStartCT = ccSrv.isEnabled(pk_corp, ProductCode.PROD_CT);
        if(isStartCT){
		// ȡ��ͬ���¼�
		EffectPriceParaVO temp = null;
		EffectPriceParaVO[] temps = null;
		Vector tempVV = new Vector();
		if (cbaseids != null && cbaseids.length > 0) {
			for (int i = 0; i < cbaseids.length; i++) {
				temp = new EffectPriceParaVO();
				temp.setPk_corp(pk_corp);
				temp.setDate(new UFDate(data));
				temp.setCcurrencytypeid(ccurrencytypeid);
				temp.setCbaseidOnly(cbaseids[i]);
				tempVV.add(temp);
			}
		}
		if (tempVV.size() > 0) {
			temps = new EffectPriceParaVO[tempVV.size()];
			tempVV.copyInto(temps);
		}
		
		nc.itf.ct.service.ICtToPo_QueryCt tranfer = (nc.itf.ct.service.ICtToPo_QueryCt) nc.bs.framework.common.NCLocator
				.getInstance().lookup(
						nc.itf.ct.service.ICtToPo_QueryCt.class.getName());
		
		// �����صĺ�ͬ�۵���׼��ʽ
		Hashtable resultForConPrice = new Hashtable();
		resultForConPrice = tranfer.getEffectContractPrice(temps);
		Vector tempVVV = new Vector();
		Vector tempVVVV = new Vector();
		EffectPriceVO effectPriceVOsFromConTemp = null;
		Hashtable baseidsT = new Hashtable();
		ChgDocPkVO chgMangId = new ChgDocPkVO();
		ChgDocPkVO[] chgMangIds = null;
		if (cbaseids != null && cbaseids.length > 0) {
			for (int i = 0; i < cbaseids.length; i++) {
				if (resultForConPrice != null
						&& resultForConPrice.get(cbaseids[i]) != null
						&& !baseidsT.containsKey(cbaseids[i])) {
					baseidsT.put(cbaseids[i], cbaseids[i]);
					tempVVV = (Vector) resultForConPrice.get(cbaseids[i]);
					if (tempVVV != null && tempVVV.size() > 0) {

						for (int j = 0; j < tempVVV.size(); j++) {
							effectPriceVOsFromConTemp = (EffectPriceVO) tempVVV
									.get(j);
							//cmangid
							effectPriceVOsFromConTemp
									.setCmangid((String) paraForPrice
											.get(effectPriceVOsFromConTemp
													.getCbaseid()));
							// cvendorbaseid-->cvendormangid(��ͬ��Ҫ�繫˾ת��)
							String cvendorbaseid = effectPriceVOsFromConTemp
							.getCvendorbaseid();
							chgMangId.setDstCorpId(pk_corp);
							//chgMangId.setSrcCorpId(pk_corp);
							chgMangId.setSrcBasId(cvendorbaseid);
							chgMangIds = ChgDataUtil.chgPkCuByCorpBase(new ChgDocPkVO[]{chgMangId});
							if(chgMangIds != null && chgMangIds.length>0){
								chgMangId = chgMangIds[0];
							}
							effectPriceVOsFromConTemp
												.setCvendmangid(chgMangId.getDstManId());

							tempVVVV.add(effectPriceVOsFromConTemp);
						}
					}
				}
			}
		}
		if (tempVVVV.size() > 0) {
			effectPriceVOsFromCon = new EffectPriceVO[tempVVVV.size()];
			tempVVVV.copyInto(effectPriceVOsFromCon);
		}
        }

		String conditionFroInv = null;
		String conditionFroVendor = null;
		String[] vendorMangIds = null;
		EffectPriceVO[] effectPriceVOs = null;

		Hashtable returnH = new Hashtable();
		Vector v = null;

		try {
			//������ʱ��
			nc.bs.scm.pub.TempTableDMO dmoTmpTable = new nc.bs.scm.pub.TempTableDMO();			
			conditionFroInv = dmoTmpTable.insertTempTable(
				cmangids,
				nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_ASK_10,
				"cmangid") ;
             //�жϺ�ͬ�Ƿ�����
			 ccSrv = (ICreateCorpQueryService) NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
			 isStartCT = ccSrv.isEnabled(pk_corp, ProductCode.PROD_VRM);
			 
			VendorVO[] vendorvo = null;
//			if(isStartCT){
				vendorvo =  queryVendorDetail(pk_corp);
			if (vendorvo != null && vendorvo.length > 0) {
				vendorMangIds = new String [vendorvo.length];
				for (int i = 0; i < vendorvo.length; i++) {
					vendorMangIds[i] = vendorvo[i].getCumandoc();
				}
			}
			conditionFroVendor = dmoTmpTable.insertTempTable(
					vendorMangIds,
					nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_PO006,
					"corder_bid") ;
//		   }
			//ƴ�Ӳ�ѯ����
			String conditionA = " po_askbill.dr = 0 and po_askbill_b.dr = 0 and po_askbill_bb1.dr = 0 and po_askbill.pk_corp = '"
					+ pk_corp
					+ "' and (po_askbill_bb1. dvaliddate <='"
					+ data
					+ "'or  po_askbill_bb1.dvaliddate is null ) and (po_askbill_bb1. dinvaliddate >='"
					+ data
					+ "'or  po_askbill_bb1.dinvaliddate is null ) and po_askbill. ccurrencytypeid = '"
					+ ccurrencytypeid
					+ "' and po_askbill_b. cmangid in "
					+ conditionFroInv
					+ "  and po_askbill_bb1.cvendormangid in "
					+ conditionFroVendor
					+ " and po_askbill_bb1.nquoteprice is not null ";
			//po_priceaudit_bb1.cpriceaudit_bid = po_priceaudit_bb1.cquotebill_bid
			//�۸��������еļ۸���������Լ۸�������������������ֶβ�ͬ��������ѯ���۵����ߺ�ͬ
			String conditionB = " po_priceaudit_bb1.cpriceaudit_bid = po_priceaudit_bb1.cquotebill_bid "
				    + "and po_priceaudit.dr = 0 and po_priceaudit_b.dr = 0 and po_priceaudit_bb1.dr = 0 and po_priceaudit. ibillstatus = 3 and po_priceaudit.pk_corp = '"
					+ pk_corp
					+ "'  and (po_priceaudit_bb1. dvaliddate <='"
					+ data
					+ "' or  po_priceaudit_bb1.dvaliddate is null ) and (po_priceaudit_bb1. dinvaliddate >='"
					+ data
					+ "'or  po_priceaudit_bb1.dinvaliddate is null ) and po_priceaudit.ccurrencytypeid = '"
					+ ccurrencytypeid
					+ "' and po_priceaudit_b. cmangid in "
					+ conditionFroInv
					+ " and po_priceaudit_bb1.cvendormangid in "
					+ conditionFroVendor
					+ " and po_priceaudit_bb1.nquoteprice is not null ";
			AskbillDMO dmo = new AskbillDMO();
            //��ѯѯ���۵��ͼ۸������������¼۸�
			effectPriceVOs = dmo.getEffectAskPrice(conditionA, conditionB,
					pk_corp);
			if ((effectPriceVOs == null || effectPriceVOs.length <= 0)
					&& (effectPriceVOsFromCon == null || effectPriceVOsFromCon.length <= 0))
				return null;
            //���óɱ��ۺͼƻ���--����ѯ���۵��ͼ۸������������¼۸�
			if (effectPriceVOs != null && effectPriceVOs.length > 0) {
				for (int i = 0; i < effectPriceVOs.length; i++) {
					if (returnH != null) {
						if (!returnH
								.containsKey(effectPriceVOs[i].getCmangid())) {
							v = new Vector();
						} else {
							v = (Vector) returnH.get(effectPriceVOs[i]
									.getCmangid());
						}
					}
					// costprice
					String cmangid = effectPriceVOs[i].getCmangid();
					Object[][] retObForCostPrice = pubImpl.queryArrayValue(
							"bd_invmandoc", "pk_invmandoc",
							new String[] { "costprice" },
							new String[] { cmangid });
					if (retObForCostPrice != null
							&& retObForCostPrice.length > 0) {
						for (int k = 0; k < retObForCostPrice.length; k++) {
							if (retObForCostPrice[k] != null
									&& retObForCostPrice[k].length > 0
									&& retObForCostPrice[k][0] != null) {
								effectPriceVOs[i].setCostprice(new UFDouble(
										retObForCostPrice[k][0].toString()));
							}
						}
					}
					// planprice
					Object[][] retObForPlanPrice = pubImpl.queryArrayValue(
							"bd_invmandoc", "pk_invmandoc",
							new String[] { "planprice" },
							new String[] { cmangid });
					if (retObForPlanPrice != null
							&& retObForPlanPrice.length > 0) {
						for (int k = 0; k < retObForPlanPrice.length; k++) {
							if (retObForPlanPrice[k] != null
									&& retObForPlanPrice[k].length > 0
									&& retObForPlanPrice[k][0] != null) {
  								effectPriceVOs[i].setPlanprice(new UFDouble(
  								    retObForPlanPrice[k][0].toString()));
							  }
							}
						}
					v.add(effectPriceVOs[i]);
					returnH.put(effectPriceVOs[i].getCmangid(), v);
				}
			}
            //���óɱ��ۺͼƻ���--���Ժ�ͬ�����¼۸�
			if (effectPriceVOsFromCon != null
					&& effectPriceVOsFromCon.length > 0) {

				String cmangid = null;
				Hashtable hTmp1 = null;
				Hashtable hTmp2 = null;
				Vector vCmangid = new Vector();
				if (cmangids != null && cmangids.length > 0) {
					for (int i = 0; i < cmangids.length; i++) {
						vCmangid.add(cmangids[i]);
					}
				}
				hTmp1 = new Hashtable();
				hTmp1 = dmo.getPriceLastForThanVOHash(vCmangid, pk_corp);
				for (int i = 0; i < effectPriceVOsFromCon.length; i++) {
					if (returnH != null) {
						if (!returnH.containsKey(effectPriceVOsFromCon[i]
								.getCmangid())) {
							v = new Vector();
						}else {
							v = (Vector) returnH.get(effectPriceVOsFromCon[i]
							               								.getCmangid());
						}
					} 
					// ��ѯ�ɹ����¼�
					cmangid = effectPriceVOsFromCon[i].getCmangid();
					hTmp2 = new Hashtable();
					if (!hTmp2.containsKey(cmangid)) {
						if (hTmp1 != null && hTmp1.size() > 0) {
							if (hTmp1.get(cmangid) != null
									&& hTmp1.get(cmangid).toString().trim()
											.length() > 0) {
								effectPriceVOsFromCon[i]
										.setLastprice((UFDouble) hTmp1
												.get(cmangid));
								hTmp2.put(cmangid, (UFDouble) hTmp1
										.get(cmangid));
							}
						}
					} else {
						effectPriceVOsFromCon[i].setLastprice((UFDouble) hTmp2
								.get(cmangid));
					}
					// costprice
					Object[][] retObForCostPrice = pubImpl.queryArrayValue(
							"bd_invmandoc", "pk_invmandoc",
							new String[] { "costprice" },
							new String[] { cmangid });
					if (retObForCostPrice != null
							&& retObForCostPrice.length > 0) {
						for (int k = 0; k < retObForCostPrice.length; k++) {
							if (retObForCostPrice[k] != null
									&& retObForCostPrice[k].length > 0
									&& retObForCostPrice[k][0] != null) {
								effectPriceVOsFromCon[i]
										.setCostprice(new UFDouble(
												retObForCostPrice[k][0]
														.toString()));
							}
						}
					}
					// planprice
					Object[][] retObForPlanPrice = pubImpl.queryArrayValue(
							"bd_invmandoc", "pk_invmandoc",
							new String[] { "planprice" },
							new String[] { cmangid });
					if (retObForPlanPrice != null
							&& retObForPlanPrice.length > 0) {
						for (int k = 0; k < retObForPlanPrice.length; k++) {
							if (retObForPlanPrice[k] != null
									&& retObForPlanPrice[k].length > 0
									&& retObForPlanPrice[k][0] != null) {
								effectPriceVOsFromCon[i]
										.setPlanprice(new UFDouble(
												retObForPlanPrice[k][0]
														.toString()));
							}
						}
					}
					v.add(effectPriceVOsFromCon[i]);
					returnH.put(effectPriceVOsFromCon[i].getCmangid(), v);
				}
			}
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.getEffectAskPrice", e);
		}

		return returnH;
	}

	/**
	 * @���ܣ�Ϊ������ѯ�۸���Ϣ
	 * @˵���� 1.������ֶ�
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @param status
	 *            boolean[]
	 */
	public EffectPriceVO[] getEffectPriceForOrder(
			EffectPriceParaVO effectPricePara) throws BusinessException {

		if (effectPricePara == null) {
			return null;
		}

		String ccurrencytypeid = effectPricePara.getCcurrencytypeid();
		// if(ccurrencytypeid == null){
		// BusinessCurrencyRateUtil currArith;
		// try {
		// currArith = new BusinessCurrencyRateUtil(pk_corp);
		// ccurrencytypeid = currArith.getLocalCurrPK();
		// } catch (Exception e) {
		// // TODO �Զ����� catch ��
		// SCMEnv.out(e.getMessage());
		// }
		// }
		
		EffectPriceVO[] effectPriceVOs = null;
		try {
			nc.bs.scm.pub.TempTableDMO dmoTmpTable = new nc.bs.scm.pub.TempTableDMO();		
			//������ʱ��
			String[] cmangids = effectPricePara.getCmangid();
			String conditionFroInv = null;
			conditionFroInv = dmoTmpTable.insertTempTable(
				cmangids,
				nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_ASK_10,
				"cmangid") ;
			String[] cvendormangid = effectPricePara.getCvendormangid();
			String conditionFroVendor = null;
			conditionFroVendor = dmoTmpTable.insertTempTable(
					cvendormangid,
					nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_ASK_15,
					"cvendormangid") ;
			//ƴ�Ӳ�ѯ����
			String condition = " po_priceaudit.ibillstatus = '3' and po_priceaudit.ccurrencytypeid = '"
					+ ccurrencytypeid
					+ "' and po_priceaudit_bb1. cvendormangid in "
					+ conditionFroVendor
					+ " and (po_priceaudit_bb1.border='Y' or po_priceaudit_bb1.border='y') "
					+ " and po_priceaudit_b. cmangid in "
					+ conditionFroInv + " ";
			AskbillDMO dmo = new AskbillDMO();
			effectPriceVOs = dmo.getEffectPriceForOrder(condition);
			if (effectPriceVOs == null || effectPriceVOs.length <= 0)
				return null;
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.getEffectPriceForOrder", e);
		}

		return effectPriceVOs;
	}
	/**
	 * ���ߣ�zx ���ܣ�Ϊ�ɹ������ṩ�۸� ������String[] cmangids, �������ID���� String[]
	 * cvendormangids, ��Ӧ�̹���ID���飬��cmangidsһһ��Ӧ String[] ccurrencyids,
	 * ����ID���飬��cmangidsһһ��Ӧ String sPricePolicy �۸����Ȳ��� 
	 * String[] sRecieptAreas, �ջ�����
	 * String sSendtype    ���˷�ʽ
	 * ���أ�UFDouble[]
	 * ��cmangidsһһ��Ӧ�Ĺ�Ӧ�̴���۸����� ���⣺ ���ڣ�(2002-6-10 13:25:09)
	 * V55 �����ջ����������˷�ʽ modify by donggq 2008-7-28
	 * �ɹ�����ѯ��ʱ����Ҫ���ջ����������䷽ʽҲ��Ϊ�ɹ�ѯ�۵����룬
	 * ƥ�乩Ӧ�̵���Ч�۸�ƥ��۸�ʱ����ϸ���ȣ��ջ����������䷽ʽ���ȼ�����
	 */
	public UFDouble[] queryPriceForPO(String[] cmangids,
			String[] cvendormangids, String[] ccurrencyids,
			String sPricePolicy, String curData, String[] sRecieptAreas,
			String sSendtype)
			throws BusinessException {

		// ������ȷ�Լ��
		if (cmangids == null || cvendormangids == null || ccurrencyids == null
				|| cvendormangids.length == 0 || cmangids.length == 0
				|| ccurrencyids.length == 0 ) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("4004070101",
							"UPP4004070101-000066")/* @res "�����������ȷ!" */);
		}
		if (sPricePolicy == null || sPricePolicy.trim().length() == 0) {
			throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("4004070101",
							"UPP4004070101-000067")/* @res "�۸����Ȳ���Ϊ�գ�" */);
		}
		UFDouble nPrice[] = new UFDouble[cmangids.length];
		UFDouble nPriceInSendtype[] = new UFDouble[cmangids.length];
		UFDouble nPriceInReceipt[] = new UFDouble[cmangids.length];
		UFDouble nPriceInReceiptAndSendtype[] = new UFDouble[cmangids.length];
		StringBuffer conditionFroInv = new StringBuffer();
		boolean ifcontainReceipt = true;
		if(sRecieptAreas == null || sRecieptAreas.length == 0 ){
			ifcontainReceipt= false;
		}
		// �����ջ����������� ��Ϊ��ϸ������׼�� 
		StringBuffer conditionFroInvAndReceipt = new StringBuffer();
		conditionFroInvAndReceipt.append("(");
		conditionFroInv.append("(");
		for (int i = 0; i < cmangids.length; i++) {
			
			if (i < cmangids.length - 1) {
				conditionFroInv.append(" (po_priceaudit_b.cmangid = ");
				conditionFroInv.append("'" + cmangids[i] + "' and po_priceaudit_bb1.cvendormangid = ");
				conditionFroInv.append("'" + cvendormangids[0] + "') or ");
				if(ifcontainReceipt){
					conditionFroInvAndReceipt.append(" (po_priceaudit_b.cmangid = ");
					conditionFroInvAndReceipt.append("'" + cmangids[i] + "' and po_priceaudit_bb1.cvendormangid = ");
					conditionFroInvAndReceipt.append("'" + cvendormangids[0] + "' and po_priceaudit_bb1.creceiptareaid = ")
						.append("'"+ sRecieptAreas[i]+"') or ");
				}

			} else {
				conditionFroInv.append(" (po_priceaudit_b.cmangid = ");
				conditionFroInv.append("'" + cmangids[i] + "' and po_priceaudit_bb1.cvendormangid = ");
				conditionFroInv.append("'" + cvendormangids[0] + "') ");
				if(ifcontainReceipt){
					conditionFroInvAndReceipt.append(" (po_priceaudit_b.cmangid = ");
					conditionFroInvAndReceipt.append("'" + cmangids[i] + "' and po_priceaudit_bb1.cvendormangid = ");
					if(sRecieptAreas[i]!=null){
						conditionFroInvAndReceipt.append("'" + cvendormangids[0] + "' and po_priceaudit_bb1.creceiptareaid = ")
						.append("'"+ sRecieptAreas[i]+"')");
					}else{
						conditionFroInvAndReceipt.append("'" + cvendormangids[0] + "' and po_priceaudit_bb1.creceiptareaid is null )");
					}
				}
			}
			
		}
		conditionFroInv.append(")");
		if(ifcontainReceipt){
			conditionFroInvAndReceipt.append(")");
		}
		StringBuffer conditionFroCCurType = new StringBuffer();
		for (int i = 0; i < ccurrencyids.length; i++) {
			if (i < ccurrencyids.length - 1) {
				conditionFroCCurType.append("'" + ccurrencyids[i] + "',");
			} else {
				conditionFroCCurType.append("'" + ccurrencyids[i] + "'");
			}
		}

//		StringBuffer conditionFroVendor = new StringBuffer();
//		for (int i = 0; i < cvendormangids.length; i++) {
//			if (i < cvendormangids.length - 1) {
//				conditionFroVendor.append("'" + cvendormangids[i] + "',");
//			} else {
//				conditionFroVendor.append("'" + cvendormangids[i] + "'");
//			}
//		}
		// ��������
		StringBuffer priceConditons = new StringBuffer();
        priceConditons.append(" and (po_priceaudit_bb1.dvaliddate <= '"+curData+"' or  po_priceaudit_bb1.dvaliddate is null) ");
        priceConditons.append(" and (po_priceaudit_bb1.dinvaliddate >= '"+curData+"' or  po_priceaudit_bb1.dinvaliddate is null) ");
		String condition = " po_priceaudit.ibillstatus = 3 and po_priceaudit.ccurrencytypeid in ( "
				+ conditionFroCCurType.toString()
				+ ") and " + conditionFroInv.toString()
				+ priceConditons.toString();
		
		//  �����ջ����������� ��Ϊ��ϸ������׼��
		String conditionForReceipt = null;
		if(ifcontainReceipt){
			conditionForReceipt = " po_priceaudit.ibillstatus = 3 and po_priceaudit.ccurrencytypeid in ( "
				+ conditionFroCCurType.toString()
				+ ") and " + conditionFroInvAndReceipt.toString()
				+ priceConditons.toString();
		}
        
		// �ɹ�����ѯ��ʱ�����ȿ���ƥ�䷢�˷�ʽ�ļ۸���δƥ�䵽��ȡ����������ͬ�����˷�ʽΪ�յļ۸�
	    String conInSendtype = "";
		try {
			AskbillDMO dmo = new AskbillDMO();
			nPrice = dmo.queryPriceForPO(cmangids, cvendormangids,
					ccurrencyids, condition, sPricePolicy);
			if(ifcontainReceipt){
				// ���뺬���ջ������Ĳ�ѯ��������ѯ��Ӧ�̼۸�
				nPriceInReceipt = dmo.queryPriceForPO(cmangids, cvendormangids,
						ccurrencyids, conditionForReceipt, sPricePolicy);
			}
			if (sSendtype != null && sSendtype.length() > 0) {
				// ��ѯ���з��˷�ʽ�����ļ۸�
				conInSendtype = condition
						+ " and po_priceaudit_bb1.pksendtype = '" + sSendtype
						+ "' ";
				String conInReceiptAndSendtype = null;
				if(ifcontainReceipt){
					conInReceiptAndSendtype = conditionForReceipt
							+ " and po_priceaudit_bb1.pksendtype = '" + sSendtype
							+ "' ";
				}
				// �����з��˷�ʽ���������뷽����ȥ��ѯ
				nPriceInSendtype = dmo.queryPriceForPO(cmangids,
						cvendormangids, ccurrencyids, conInSendtype,
						sPricePolicy);
				if(ifcontainReceipt){
					// �������ջ����������˷�ʽ���������뷽���в�ѯ
					nPriceInReceiptAndSendtype = dmo.queryPriceForPO(cmangids,
							cvendormangids, ccurrencyids, conInReceiptAndSendtype,
							sPricePolicy);
				}
			}
			for (int k = 0; k < nPrice.length; k++) {
				// ����ϸ����ѯ���������ջ�����&���˷�ʽ
				if (null != nPriceInReceiptAndSendtype[k]){
					nPrice[k] = nPriceInReceiptAndSendtype[k];
					continue;
				}
				// ��ѯ�����к����ջ�����
				if (null != nPriceInReceipt[k]){
					nPrice[k] = nPriceInReceipt[k];
					continue;
				}
				// ��ѯ�����к��з��˷�ʽ
				if (null != nPriceInSendtype[k] ) {
					nPrice[k] = nPriceInSendtype[k];
					continue;
				}
			}
			if (nPrice == null || nPrice.length <= 0)
				return null;
// try {
// AskbillDMO dmo = new AskbillDMO();
//			nPrice = dmo.queryPriceForPO(cmangids, cvendormangids,
//					ccurrencyids, condition, sPricePolicy);
//			if (nPrice == null || nPrice.length <= 0)
//				return null;
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryPriceForPO", e);
		}

		return nPrice;
	}
	
	/**
	 * ���ߣ�zx ���ܣ�Ϊ�ɹ������ṩ�۸� ������String[] cmangids, �������ID���� String[]
	 * cvendormangids, ��Ӧ�̹���ID���飬��cmangidsһһ��Ӧ String[] ccurrencyids,
	 * ����ID���飬��cmangidsһһ��Ӧ String sPricePolicy �۸����Ȳ��� 
	 * String[] sRecieptAreas, �ջ�����
	 * String sSendtype    ���˷�ʽ
	 * ���أ�UFDouble[]
	 * ��cmangidsһһ��Ӧ�Ĺ�Ӧ�̴���۸����� ���⣺ ���ڣ�(2002-6-10 13:25:09)
	 * V55 �����ջ����������˷�ʽ modify by donggq 2008-7-28
	 * �ɹ�����ѯ��ʱ����Ҫ���ջ����������䷽ʽҲ��Ϊ�ɹ�ѯ�۵����룬
	 * ƥ�乩Ӧ�̵���Ч�۸�ƥ��۸�ʱ����ϸ���ȣ��ջ����������䷽ʽ���ȼ�����
	 */
	public UFDouble[][] queryPriceForPOOrder(String[] cmangids,
			String[] cvendormangids, String[] ccurrencyids,
			String sPricePolicy, String curData, String[] sRecieptAreas,
			String sSendtype)
			throws BusinessException {

		// ������ȷ�Լ��
		if (cmangids == null || cvendormangids == null || ccurrencyids == null
				|| cvendormangids.length == 0 || cmangids.length == 0
				|| ccurrencyids.length == 0 ) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("4004070101",
							"UPP4004070101-000066")/* @res "�����������ȷ!" */);
		}
		if (sPricePolicy == null || sPricePolicy.trim().length() == 0) {
			throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("4004070101",
							"UPP4004070101-000067")/* @res "�۸����Ȳ���Ϊ�գ�" */);
		}
		UFDouble[][] nPriceAndTaxrate = new UFDouble[cmangids.length][2];
		StringBuffer conditionFroInv = new StringBuffer();
		int iLen = cmangids.length;
		String sTempTableName = null;
		if(iLen > 100){
	    ArrayList listTempTableValue = new ArrayList();
	    for (int i = 0; i < iLen; i++) {
	      ArrayList listElement = new ArrayList();
	      listElement.add(cmangids[i]);
	      listElement.add(cvendormangids[0]);
	      listTempTableValue.add(listElement);
	    }
	    HashMap hmapBody = new HashMap();
	      // ������ʱ��
	        try {
            sTempTableName = new nc.bs.scm.pub.TempTableDMO()
                .getTempStringTable(
                    nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_PO026,
                    new String[] { "cmangid", "cvendormangid" },
                    new String[] { "char(20) not null ",
                        "char(20) not null " }, null,
                    listTempTableValue);
          }
          catch (Exception e) {
            //��־�쳣
            nc.vo.scm.pub.SCMEnv.out(e);
            //���淶�׳��쳣
            nc.bs.scm.pub.ScmPubDMO.throwBusinessException(e);
          }
//	        conditionFroInv.append(" (po_priceaudit_b.cmangid = " + sTempTableName +".cmangid");
//	        conditionFroInv.append(" and po_priceaudit_bb1.cvendormangid = " + sTempTableName +".cvendormangid ) ");
	    
		}else{
		  conditionFroInv.append("(");
  		for (int i = 0; i < cmangids.length; i++) {
  			
  			if (i < cmangids.length - 1) {
  				conditionFroInv.append(" (po_priceaudit_b.cmangid = ");
  				conditionFroInv.append("'" + cmangids[i] + "' and po_priceaudit_bb1.cvendormangid = ");
  				conditionFroInv.append("'" + cvendormangids[0] + "') or ");
  
  			} else {
  				conditionFroInv.append(" (po_priceaudit_b.cmangid = ");
  				conditionFroInv.append("'" + cmangids[i] + "' and po_priceaudit_bb1.cvendormangid = ");
  				conditionFroInv.append("'" + cvendormangids[0] + "') ");
  			}
  			
  		}
  		conditionFroInv.append(")");
		}
		StringBuffer conditionFroCCurType = new StringBuffer();
		String[] ccurrencyidUnique = PuUtils.getUniqueString(ccurrencyids);
		for (int i = 0; i < ccurrencyidUnique.length; i++) {
			if (i < ccurrencyidUnique.length - 1) {
				conditionFroCCurType.append("'" + ccurrencyidUnique[i] + "',");
			} else {
				conditionFroCCurType.append("'" + ccurrencyidUnique[i] + "'");
			}
		}

		// ��������
		StringBuffer priceConditons = new StringBuffer();
        priceConditons.append(" and (po_priceaudit_bb1.dvaliddate <= '"+curData+"' or  po_priceaudit_bb1.dvaliddate is null) ");
        priceConditons.append(" and (po_priceaudit_bb1.dinvaliddate >= '"+curData+"' or  po_priceaudit_bb1.dinvaliddate is null) ");
		String condition = " po_priceaudit.ibillstatus = 3 " + SqlMethod.formInSQL("po_priceaudit.ccurrencytypeid", ccurrencyidUnique);//and po_priceaudit.ccurrencytypeid in ( "
		if(sTempTableName == null){
//				+ conditionFroCCurType.toString()
		  condition+= (" and " + conditionFroInv.toString());
		}
		condition += priceConditons.toString();
		
		//  �����ջ����������� ��Ϊ��ϸ������׼��
		String conditionForReceipt = null;
		try {
			AskbillDMO dmo = new AskbillDMO();
			nPriceAndTaxrate = dmo.queryPriceForPOOrder(cmangids, cvendormangids,
					ccurrencyids, condition, sPricePolicy, sRecieptAreas,
					sSendtype,sTempTableName);
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryPriceForPO", e);
		}

		return nPriceAndTaxrate;
	}

	/**
	 * @���ܣ���Ӧ�̴����ϵ�ļ۸��ѯ
	 * @˵���� 1.������ֶ�
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @param status
	 *            boolean[]
	 */
	public EffectPriceVO[] getPriceForVendor(EffectPriceParaVO effectPricePara)
			throws BusinessException {

		if (effectPricePara == null) {
			return null;
		}
		String pk_corp = effectPricePara.getPk_corp();
		String[] cvendormangid = effectPricePara.getCvendormangid();
		EffectPriceVO[] effectPriceVOs = null;
		String[] cmangids = effectPricePara.getCmangid();
		String conditionFroInv = null;
		String conditionFroVendor = null;
		try {
			//������ʱ��
			nc.bs.scm.pub.TempTableDMO dmoTmpTable = new nc.bs.scm.pub.TempTableDMO();			
			conditionFroInv = dmoTmpTable.insertTempTable(
				cmangids,
				nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_ASK_10,
				"cmangid") ;
			conditionFroVendor = dmoTmpTable.insertTempTable(
					cvendormangid,
					nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_ASK_15,
					"cvendormangid") ;
            //ƴ�Ӳ�ѯ����
			String conditionA = " po_askbill.pk_corp = '" + pk_corp
					+ "'  and po_askbill_bb1. cvendormangid in "
					+ conditionFroVendor + " and po_askbill_b. cmangid in "
					+ conditionFroInv + " ";
			String conditionB = " po_priceaudit. ibillstatus = '3' and po_priceaudit.pk_corp = '"
					+ pk_corp
					+ "' and po_priceaudit_bb1.cvendormangid in"
					+ conditionFroVendor
					+ " and po_priceaudit_b. cmangid in "
					+ conditionFroInv + " ";
			AskbillDMO dmo = new AskbillDMO();
			effectPriceVOs = dmo.getPriceForVendor(conditionA, conditionB);
			if (effectPriceVOs == null || effectPriceVOs.length <= 0)
				return null;
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.getPriceForVendor", e);
		}

		return effectPriceVOs;
	}

	/**
	 * �������������ݿ���ɾ��VO��������(ѯ���۵�)
	 * 
	 * �������ڣ�(2001-6-7)
	 * 
	 * @param vos
	 *            AskbillVO[]
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public boolean discardAskbillVOsMy(Vector v) throws BusinessException {
		Vector tempV = new Vector();
		AskbillHeaderVO headerVO = null;
		AskbillItemVO[] itemVO = null;
		AskbillVO askbillVO = null;
		AskbillVO[] askbillVOs = null;
		boolean isSuccess = false;
		PubDMO pubDmo = null;
		UFBoolean isLockSuccess = null;
		Vector tempVV = new Vector();
		if (v == null || (v != null && v.size() == 0)) {
			return false;
		}
		try {
			String[] billIds = new String[v.size()];
			Hashtable h = new Hashtable();
			for (int i = 0; i < v.size(); i++) {
				tempV = (Vector) v.get(i);
				headerVO = new AskbillHeaderVO();
				if (tempV != null && tempV.get(0) != null) {
					headerVO = (AskbillHeaderVO) tempV.get(0);
					billIds[i] = headerVO.getPrimaryKey();
					// h.put(billIds[i], headerVO.getVaskbillcode());
					h.put(billIds[i], headerVO.getCaskbillid());
					askbillVO = new AskbillVO();
					askbillVO.setParentVO(headerVO);
					askbillVO.setChildrenVO((AskbillItemVO[])tempV.get(1));
					tempVV.add(askbillVO);
				}
			}
			if(tempVV.size() > 0){
				askbillVOs = new AskbillVO[tempVV.size()];
				tempVV.copyInto(askbillVOs);
			}
			//����
			pubDmo = new PubDMO();
			isLockSuccess = pubDmo.lockPkForVos(askbillVOs);
			if (isLockSuccess != null) {
			    //�жϲ���	
				checkTimeStampsWhenDelForAskBill(askbillVOs);
			}
				AskbillDMO dmo = new AskbillDMO();
				// һ����ȡ��ѯ�۵�״̬�����ж�
				// hStatus = dmo.getAskBillStatusHash(billIds);
				// for (int i = 0; i < billIds.length; i++) {
				// status = (String) hStatus.get(billIds[i]);
				// code = (String) h.get(billIds[i]);
				// if (status == null || status.equals("1")){
				// String[] value = new String[]{code};
				// throw new
				// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000000",null,value)/*@res
				// "�����Ѿ����ϵĵ��ݣ�"+ CommonConstant.BEGIN_MARK + code +
				// CommonConstant.END_MARK + "�������ٴ����ϣ�"*/ );
				// }
				// if (status.equals("2")){
				// String[] value = new String[]{code};
				// throw new
				// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000001",null,value)/*@res
				// "�����Ѿ������ĵ��ݣ�"+ CommonConstant.BEGIN_MARK + code +
				// CommonConstant.END_MARK + "���������ϣ�"*/ );
				// }
				// if (status.equals("3")){
				// String[] value = new String[]{code};
				// throw new
				// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000002",null,value)/*@res
				// "�����Ѿ��б��۵ĵ��ݣ�" + CommonConstant.BEGIN_MARK + code +
				// CommonConstant.END_MARK + "���������ϣ�"*/);
				// }
				// if (status.equals("4")){
				// String[] value = new String[]{code};
				// throw new
				// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000003",null,value)/*@res
				// "�����Ѿ�ȷ�ϵĵ��ݣ�"+ CommonConstant.BEGIN_MARK + code +
				// CommonConstant.END_MARK + "���������ϣ�"*/ );
				// }
				// }
				// ����
				Vector vHid = new Vector();
				int iLen = v.size();
				// ��д�빺����
				Vector rowIdsTemp = new Vector();
				Vector headIdsTemp = new Vector();
				Vector numTemp = new Vector();
				String[] rowIds = null;
				String[] headIds = null;
				UFDouble[] num = null;
				AskbillItemVO[] itemVOsFOrRw = null;
				for (int i = 0; i < iLen; i++) {
					tempV = (Vector) v.get(i);
					headerVO = new AskbillHeaderVO();
					headerVO = (AskbillHeaderVO) tempV.get(0);
					if (headerVO != null && headerVO.getPrimaryKey() != null) {
						if (!vHid.contains(headerVO.getPrimaryKey())) {
							vHid.addElement(headerVO.getPrimaryKey());
							itemVOsFOrRw = (AskbillItemVO[]) tempV.get(1);
							if (itemVOsFOrRw != null && itemVOsFOrRw.length > 0) {
								rowIds = new String[itemVOsFOrRw.length];
								num = new UFDouble[itemVOsFOrRw.length];
								for (int j = 0; j < itemVOsFOrRw.length; j++) {
									if (itemVOsFOrRw[j].getCupsourcebilltype() != null
											&& itemVOsFOrRw[j]
													.getCupsourcebilltype()
													.equals(IBillType.PRAYBILL)) {
										rowIdsTemp.add(itemVOsFOrRw[j]
												.getCupsourcebillrowid());
										headIdsTemp.add(itemVOsFOrRw[j]
												.getCupsourcebillid());
										numTemp.add(null);
									}
								}
							}
						}
					}
				}
				if (vHid.size() > 0) {
					String[] saHId = new String[vHid.size()];
					vHid.copyInto(saHId);
					dmo.deleHeadBodyByHIds(saHId);
				}
				// ���˵��ݺ�
				nc.bs.pu.pub.GetSysBillCode rtnBillCode = new nc.bs.pu.pub.GetSysBillCode();
				for (int i = 0; i < iLen; i++) {
					tempV = (Vector) v.get(i);
					headerVO = new AskbillHeaderVO();
					headerVO = (AskbillHeaderVO) tempV.get(0);
					itemVO = (AskbillItemVO[]) tempV.get(1);
					askbillVO = new AskbillVO();
					askbillVO.setParentVO(headerVO);
					askbillVO.setChildrenVO(itemVO);
					rtnBillCode.returnBillNo(askbillVO);
				}
				ParaRewriteVO paraVo = new ParaRewriteVO();
				// ��д�빺����(���ϵ�ѯ�۵�������������ѯ�۵�)
				PraybillImpl prayBO = new PraybillImpl();
				if (rowIdsTemp.size() > 0 && numTemp.size() > 0
						&& rowIdsTemp.size() == numTemp.size()) {
					rowIds = new String[rowIdsTemp.size()];
					rowIdsTemp.copyInto(rowIds);
					headIds = new String[headIdsTemp.size()];
					headIdsTemp.copyInto(headIds);
					num = new UFDouble[numTemp.size()];
					numTemp.copyInto(num);
					paraVo.setCBodyIdArray(rowIds);
					paraVo.setCHeadIdArray(headIds);
					paraVo.setDNumArray(num);
					prayBO.updateNaccAskNum(paraVo, new Integer(IOperation.DELETE).toString());
				}
			
			// ����ɾ���ɹ���־
			isSuccess = true;
			// }
			// else
			// throw new
			// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000316")/*@res
			// "���ڽ�����ز��������Ժ�����"*/);
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.discardAskbillVOsMy", e);
		}finally {
			if (isLockSuccess != null && isLockSuccess.booleanValue()) {
				try {
					pubDmo.freePkForVos(askbillVOs);
				} catch (Exception e) {
					nc.bs.pu.pub.PubDMO.throwBusinessException(e);
				}
			}
		}
		return isSuccess;
	}
	/**
	 * �Ƚϵ��ݵ�ts�Ƿ�һ��,ֻ���ͷ.
	 *
	 * <br>�˷���Ŀǰֻ��������ʱ��ʱ������.
	 */
	public void checkTimeStampsWhenDel(PriceauditVO[] voBills) throws nc.vo.pub.BusinessException{

		if (voBills == null || voBills.length == 0)
			return ;

		// ��Ӧ���ݵı�ͷ���ݿ�ʱ���
		HashMap hmDBTS = new HashMap();
		// ��ͷ����
		ArrayList alHeadIDs = new ArrayList();

		String sHeadTs = null;
		String sHeadID = null;
		String sDbHeadTs = null;

		for (int i = 0; i < voBills.length; i++){
			sHeadID = voBills[i].getParentVO().getPrimaryKey();
			sHeadTs = ((PriceauditHeaderVO)voBills[i].getParentVO()).getTs();

			if (sHeadID == null || sHeadTs == null){
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("4004070105","UPP4004070105-000094")/*@res "��鵥��ʱ���������PK��ʱ������ݲ�����!"*/);		
				
			} 
			alHeadIDs.add(sHeadID);
		}

		// ��ѯ���������ݿ��е�ts
		try {
			AskbillDMO dmo = new AskbillDMO();
			hmDBTS = dmo.queryBillHeadTsBatch(alHeadIDs);
		} catch (Exception e){
			if (e instanceof BusinessException)
				throw (BusinessException)e;
			else
				throw new BusinessException(e);
		}

		// �Ƚ�ʱ���
		String sBillCode = null;
		for (int i = 0; i < voBills.length; i++) {
			sHeadID = voBills[i].getParentVO().getPrimaryKey();
			sHeadTs = ((PriceauditHeaderVO)voBills[i].getParentVO()).getTs();

			if (hmDBTS.containsKey(sHeadID)) {
				sDbHeadTs = (String) hmDBTS.get(sHeadID);

				if (!sDbHeadTs.equals(sHeadTs)) {
					if (sBillCode != null)
						sBillCode += (", " + ((PriceauditHeaderVO)voBills[i].getParentVO()).getVpriceauditcode());
					else
						sBillCode = ((PriceauditHeaderVO)voBills[i].getParentVO()).getVpriceauditcode();

				}
			} else {
				if (sBillCode != null)
					sBillCode += (", " + ((PriceauditHeaderVO)voBills[i].getParentVO()).getVpriceauditcode());
				else
					sBillCode = ((PriceauditHeaderVO)voBills[i].getParentVO()).getVpriceauditcode();
			}
		}

		if (sBillCode != null)
			throw new BusinessException(
					nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("4004070105","UPP4004070105-000093")/*@res "���е��ݿ����ѱ��޸Ļ�ɾ��!"*/
					+ "\n"
					+ sBillCode);
	}
	/**
	 * �Ƚϵ��ݵ�ts�Ƿ�һ��,ֻ���ͷ.
	 *
	 * <br>�˷���Ŀǰֻ��������ʱ��ʱ������.
	 */
	public void checkTimeStampsWhenDelForAskBill(AskbillVO[] voBills) throws nc.vo.pub.BusinessException{

		if (voBills == null || voBills.length == 0)
			return ;

		// ��Ӧ���ݵı�ͷ���ݿ�ʱ���
		HashMap hmDBTS = new HashMap();
		// ��ͷ����
		ArrayList alHeadIDs = new ArrayList();

		String sHeadTs = null;
		String sHeadID = null;
		String sDbHeadTs = null;

		for (int i = 0; i < voBills.length; i++){
			sHeadID = voBills[i].getParentVO().getPrimaryKey();
			sHeadTs = ((AskbillHeaderVO)voBills[i].getParentVO()).getTs();

			if (sHeadID == null || sHeadTs == null){
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("4004070101","UPP4004070105-000098")/*@res "��鵥��ʱ���������PK��ʱ������ݲ�����!"*/);		
				
			} 
			alHeadIDs.add(sHeadID);
		}

		// ��ѯ���������ݿ��е�ts
		try {
			AskbillDMO dmo = new AskbillDMO();
			hmDBTS = dmo.queryBillHeadTsBatchForAskBill(alHeadIDs);
		} catch (Exception e){
			if (e instanceof BusinessException)
				throw (BusinessException)e;
			else
				throw new BusinessException(e);
		}

		// �Ƚ�ʱ���
		String sBillCode = null;
		for (int i = 0; i < voBills.length; i++) {
			sHeadID = voBills[i].getParentVO().getPrimaryKey();
			sHeadTs = ((AskbillHeaderVO)voBills[i].getParentVO()).getTs();

			if (hmDBTS.containsKey(sHeadID)) {
				sDbHeadTs = (String) hmDBTS.get(sHeadID);

				if (!sDbHeadTs.equals(sHeadTs)) {
					if (sBillCode != null)
						sBillCode += (", " + ((AskbillHeaderVO)voBills[i].getParentVO()).getVaskbillcode());
					else
						sBillCode = ((AskbillHeaderVO)voBills[i].getParentVO()).getVaskbillcode();

				}
			} else {
				if (sBillCode != null)
					sBillCode += (", " + ((AskbillHeaderVO)voBills[i].getParentVO()).getVaskbillcode());
				else
					sBillCode = ((AskbillHeaderVO)voBills[i].getParentVO()).getVaskbillcode();
			}
		}

		if (sBillCode != null)
			throw new BusinessException(
					nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("4004070101","UPP4004070101-000097")/*@res "���е��ݿ����ѱ��޸Ļ�ɾ��!"*/
					+ "\n"
					+ sBillCode);
	}
	/**
	 * �������������ݿ���ɾ��VO��������(�۸�������)
	 * 
	 * �������ڣ�(2001-6-7)
	 * 
	 * @param vos
	 *            AskbillVO[]
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public boolean discardPriceAuditbillVOsMy(PriceauditMergeVO[] vos)
			throws BusinessException {
		if(vos == null || (vos != null && vos.length == 0)){
			return false;
		}
		Vector v = new Vector();
		for(int i = 0 ; i < vos.length ; i ++){
			Vector tempV = PriceauditMergeVO.convertVOTOBackForImpl(vos[i]);
			v.add(tempV);
		}
		Vector tempV = new Vector();
		PriceauditHeaderVO headerVO = null;
		PriceauditBVO[] itemVO = null;
		PriceauditVO askbillVO = null;
		PriceauditVO[] askbillVOs = null;
		boolean isSuccess = false;
		PubDMO pubDmo = null;
		UFBoolean isLockSuccess = null;
		Vector tempVV = new Vector();
		try {
			String[] billIds = new String[v.size()];
			Hashtable h = new Hashtable();
			for (int i = 0; i < v.size(); i++) {
				tempV = (Vector) v.get(i);
				headerVO = new PriceauditHeaderVO();
				if (tempV != null && tempV.get(0) != null) {
					headerVO = (PriceauditHeaderVO) tempV.get(0);
					billIds[i] = headerVO.getPrimaryKey();
					// h.put(billIds[i], headerVO.getVaskbillcode());
					h.put(billIds[i], headerVO.getPrimaryKey());
					askbillVO = new PriceauditVO();
					askbillVO.setParentVO(headerVO);
					askbillVO.setChildrenVO((PriceauditBVO[])tempV.get(1));
					tempVV.add(askbillVO);
				}
			}
			if(tempVV.size() > 0){
				askbillVOs = new PriceauditVO[tempVV.size()];
				tempVV.copyInto(askbillVOs);
			}
			//����
			pubDmo = new PubDMO();
			isLockSuccess = pubDmo.lockPkForVos(askbillVOs);
			if (isLockSuccess != null) {
			//�жϲ���	
				checkTimeStampsWhenDel(askbillVOs);
			}
			
			
				AskbillDMO dmo = new AskbillDMO();
				StringBuffer conditionForAdd = new StringBuffer();
				if (billIds != null && billIds.length > 0) {
					for (int i = 0; i < billIds.length; i++) {
						if (i < billIds.length - 1) {
							conditionForAdd
									.append("'" + billIds[i] + "'" + ",");
						} else {
							conditionForAdd.append("'" + billIds[i] + "'");
						}
					}
					// �Ѿ����ɶ����ļ۸���������������
					dmo.CheckIsGenOrder(conditionForAdd.toString());
				}

				// һ����ȡ��ѯ�۵�״̬�����ж�
				// hStatus = dmo.getAskBillStatusHash(billIds);
				// for (int i = 0; i < billIds.length; i++) {
				// status = (String) hStatus.get(billIds[i]);
				// code = (String) h.get(billIds[i]);
				// if (status == null || status.equals("1")){
				// String[] value = new String[]{code};
				// throw new
				// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000000",null,value)/*@res
				// "�����Ѿ����ϵĵ��ݣ�"+ CommonConstant.BEGIN_MARK + code +
				// CommonConstant.END_MARK + "�������ٴ����ϣ�"*/ );
				// }
				// if (status.equals("2")){
				// String[] value = new String[]{code};
				// throw new
				// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000001",null,value)/*@res
				// "�����Ѿ������ĵ��ݣ�"+ CommonConstant.BEGIN_MARK + code +
				// CommonConstant.END_MARK + "���������ϣ�"*/ );
				// }
				// if (status.equals("3")){
				// String[] value = new String[]{code};
				// throw new
				// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000002",null,value)/*@res
				// "�����Ѿ��б��۵ĵ��ݣ�" + CommonConstant.BEGIN_MARK + code +
				// CommonConstant.END_MARK + "���������ϣ�"*/);
				// }
				// if (status.equals("4")){
				// String[] value = new String[]{code};
				// throw new
				// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000003",null,value)/*@res
				// "�����Ѿ�ȷ�ϵĵ��ݣ�"+ CommonConstant.BEGIN_MARK + code +
				// CommonConstant.END_MARK + "���������ϣ�"*/ );
				// }
				// }
				// ����
				Vector vHid = new Vector();
				int iLen = v.size();
				// ��д�빺����
				Vector rowIdsTemp = new Vector();
				Vector headIdsTemp = new Vector();
				Vector numTemp = new Vector();
				Vector upPk = new Vector();
				Vector upHeaderPk = new Vector();
				String[] rowIds = null;
				String[] headIds = null;
				UFDouble[] num = null;
				PriceauditBVO[] itemVOsForDel = null;
				for (int i = 0; i < iLen; i++) {
					tempV = (Vector) v.get(i);
					headerVO = new PriceauditHeaderVO();
					headerVO = (PriceauditHeaderVO) tempV.get(0);
					if (headerVO != null && headerVO.getPrimaryKey() != null) {
						if (!vHid.contains(headerVO.getPrimaryKey())) {
							if (!dmo
									.checkIsPriceSorce(headerVO.getPrimaryKey())) {// �Ƿ���Ϊ�����۸��������ı�����Դ
								vHid.addElement(headerVO.getPrimaryKey());
							}
							itemVOsForDel = (PriceauditBVO[]) tempV.get(1);
							if (itemVOsForDel != null
									&& itemVOsForDel.length > 0) {
								rowIds = new String[itemVOsForDel.length];
								num = new UFDouble[itemVOsForDel.length];
								for (int j = 0; j < itemVOsForDel.length; j++) {
									if (itemVOsForDel[j].getCupsourcebilltype() != null
											&& itemVOsForDel[j]
													.getCupsourcebilltype()
													.equals(IBillType.PRAYBILL)) {
										rowIdsTemp.add(itemVOsForDel[j]
												.getCupsourcebillrowid());
										headIdsTemp.add(itemVOsForDel[j]
												.getCupsourcebillid());
										numTemp.add(null);
									} else if (itemVOsForDel[j]
											.getCupsourcebilltype() != null
											&& itemVOsForDel[j]
													.getCupsourcebilltype()
													.equals(IBillType.ASKBILL)) {
										upPk.add(itemVOsForDel[j]
												.getCupsourcebillrowid());
										upHeaderPk.add(itemVOsForDel[j]
												.getCupsourcebillid());
									}
								}
							}
						}
					}
				}
				// ѯ����ת�۸�����������Դ���빺������Ҳ��Ҫ��д
				if (upPk.size() > 0) {
					Hashtable result = null;
					StringBuffer sqlCondition = new StringBuffer();
					AskbillItemVO itemT = null;
					for (int i = 0; i < upPk.size(); i++) {
						if (i < upPk.size() - 1) {
							sqlCondition.append("'" + upPk.get(i) + "',");
						} else {
							sqlCondition.append("'" + upPk.get(i) + "'");
						}
					}
					// ȡѯ���۵�����Դ������Ϣ
					if (sqlCondition.length() > 0) {
						result = dmo
								.findItemByPrimaryKeyForPriceAudit(sqlCondition
										.toString());
					}
					if (result != null && result.size() > 0) {
						for (int i = 0; i < upPk.size(); i++) {
							itemT = (AskbillItemVO) result.get(upPk.get(i));
							rowIdsTemp.add(itemT.getCupsourcebillrowid());
							headIdsTemp.add(itemT.getCupsourcebillid());
							numTemp.add(null);
						}
					}
				}
				if (vHid.size() > 0) {
					String[] saHId = new String[vHid.size()];
					vHid.copyInto(saHId);
					dmo.deleHeadBodyByHIdsForPriceAudit(saHId);
				}
				// ���˵��ݺ�
				nc.bs.pu.pub.GetSysBillCode rtnBillCode = new nc.bs.pu.pub.GetSysBillCode();
				for (int i = 0; i < iLen; i++) {
					tempV = (Vector) v.get(i);
					headerVO = new PriceauditHeaderVO();
					headerVO = (PriceauditHeaderVO) tempV.get(0);
					itemVO = (PriceauditBVO[]) tempV.get(1);
					askbillVO = new PriceauditVO();
					askbillVO.setParentVO(headerVO);
					askbillVO.setChildrenVO(itemVO);
					rtnBillCode.returnBillNo(askbillVO);
				}
				// ��дѯ���۵���ɱ�־
				if (upHeaderPk.size() > 0) {
					StringBuffer sqlCondition = new StringBuffer();
					Vector result = new Vector();
					for (int i = 0; i < upPk.size(); i++) {
						if (i < upPk.size() - 1) {
							sqlCondition.append("'" + upHeaderPk.get(i) + "',");
						} else {
							sqlCondition.append("'" + upHeaderPk.get(i) + "'");
						}
					}
					if (sqlCondition != null
							&& sqlCondition.toString().trim().length() > 0) {
						result = dmo
								.checkIsCanEffectPriceForRWAskBill(sqlCondition
										.toString());
					}
					if ((result != null && result.size() == 0)
							|| result == null) {
						ReWriteAskBillStatus(upHeaderPk, 1);
					}
				}
				ParaRewriteVO paraVo = new ParaRewriteVO();
				// ��д�빺����(���ϵ�ѯ�۵�������������ѯ�۵�)
				PraybillImpl prayBO = new PraybillImpl();
				if (rowIdsTemp.size() > 0 && numTemp.size() > 0
						&& rowIdsTemp.size() == numTemp.size()) {
					rowIds = new String[rowIdsTemp.size()];
					rowIdsTemp.copyInto(rowIds);
					headIds = new String[headIdsTemp.size()];
					headIdsTemp.copyInto(headIds);
					num = new UFDouble[numTemp.size()];
					numTemp.copyInto(num);
					paraVo.setCBodyIdArray(rowIds);
					paraVo.setCHeadIdArray(headIds);
					paraVo.setDNumArray(num);
					prayBO.updateNaccPriceAuditNum(paraVo, new Integer(IOperation.DELETE).toString());
				}
			
			// ����ɾ���ɹ���־
			isSuccess = true;
			// }
			// else
			// throw new
			// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000316")/*@res
			// "���ڽ�����ز��������Ժ�����"*/);
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.discardPriceAuditbillVOsMy", e);
		} finally {
			if (isLockSuccess != null && isLockSuccess.booleanValue()) {
				try {
					pubDmo.freePkForVos(askbillVOs);
				} catch (Exception e) {
					nc.bs.pu.pub.PubDMO.throwBusinessException(e);
				}
			}
		}
		return isSuccess;
	}
	/**
	 * �������������ݿ���ɾ��VO��������(�۸�������)
	 * 
	 * �������ڣ�(2001-6-7)
	 * 
	 * @param vos
	 *            AskbillVO[]
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public boolean discardPriceAuditbillVOsMy(Vector v)
			throws BusinessException {
		Vector tempV = new Vector();
		PriceauditHeaderVO headerVO = null;
		PriceauditBVO[] itemVO = null;
		PriceauditVO askbillVO = null;
		PriceauditVO[] askbillVOs = null;
		boolean isSuccess = false;
		PubDMO pubDmo = null;
		UFBoolean isLockSuccess = null;
		Vector tempVV = new Vector();
		try {
			String[] billIds = new String[v.size()];
			Hashtable h = new Hashtable();
			for (int i = 0; i < v.size(); i++) {
				tempV = (Vector) v.get(i);
				headerVO = new PriceauditHeaderVO();
				if (tempV != null && tempV.get(0) != null) {
					headerVO = (PriceauditHeaderVO) tempV.get(0);
					billIds[i] = headerVO.getPrimaryKey();
					// h.put(billIds[i], headerVO.getVaskbillcode());
					h.put(billIds[i], headerVO.getPrimaryKey());
					askbillVO = new PriceauditVO();
					askbillVO.setParentVO(headerVO);
					askbillVO.setChildrenVO((PriceauditBVO[])tempV.get(1));
					tempVV.add(askbillVO);
				}
			}
			if(tempVV.size() > 0){
				askbillVOs = new PriceauditVO[tempVV.size()];
				tempVV.copyInto(askbillVOs);
			}
			//����
			pubDmo = new PubDMO();
			isLockSuccess = pubDmo.lockPkForVos(askbillVOs);
			if (isLockSuccess != null) {
			//�жϲ���	
				checkTimeStampsWhenDel(askbillVOs);
			}
			
			
				AskbillDMO dmo = new AskbillDMO();
				StringBuffer conditionForAdd = new StringBuffer();
				if (billIds != null && billIds.length > 0) {
					for (int i = 0; i < billIds.length; i++) {
						if (i < billIds.length - 1) {
							conditionForAdd
									.append("'" + billIds[i] + "'" + ",");
						} else {
							conditionForAdd.append("'" + billIds[i] + "'");
						}
					}
					// �Ѿ����ɶ����ļ۸���������������
					dmo.CheckIsGenOrder(conditionForAdd.toString());
				}

				// һ����ȡ��ѯ�۵�״̬�����ж�
				// hStatus = dmo.getAskBillStatusHash(billIds);
				// for (int i = 0; i < billIds.length; i++) {
				// status = (String) hStatus.get(billIds[i]);
				// code = (String) h.get(billIds[i]);
				// if (status == null || status.equals("1")){
				// String[] value = new String[]{code};
				// throw new
				// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000000",null,value)/*@res
				// "�����Ѿ����ϵĵ��ݣ�"+ CommonConstant.BEGIN_MARK + code +
				// CommonConstant.END_MARK + "�������ٴ����ϣ�"*/ );
				// }
				// if (status.equals("2")){
				// String[] value = new String[]{code};
				// throw new
				// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000001",null,value)/*@res
				// "�����Ѿ������ĵ��ݣ�"+ CommonConstant.BEGIN_MARK + code +
				// CommonConstant.END_MARK + "���������ϣ�"*/ );
				// }
				// if (status.equals("3")){
				// String[] value = new String[]{code};
				// throw new
				// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000002",null,value)/*@res
				// "�����Ѿ��б��۵ĵ��ݣ�" + CommonConstant.BEGIN_MARK + code +
				// CommonConstant.END_MARK + "���������ϣ�"*/);
				// }
				// if (status.equals("4")){
				// String[] value = new String[]{code};
				// throw new
				// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000003",null,value)/*@res
				// "�����Ѿ�ȷ�ϵĵ��ݣ�"+ CommonConstant.BEGIN_MARK + code +
				// CommonConstant.END_MARK + "���������ϣ�"*/ );
				// }
				// }
				// ����
				Vector vHid = new Vector();
				int iLen = v.size();
				// ��д�빺����
				Vector rowIdsTemp = new Vector();
				Vector headIdsTemp = new Vector();
				Vector numTemp = new Vector();
				Vector upPk = new Vector();
				Vector upHeaderPk = new Vector();
				String[] rowIds = null;
				String[] headIds = null;
				UFDouble[] num = null;
				PriceauditBVO[] itemVOsForDel = null;
				for (int i = 0; i < iLen; i++) {
					tempV = (Vector) v.get(i);
					headerVO = new PriceauditHeaderVO();
					headerVO = (PriceauditHeaderVO) tempV.get(0);
					if (headerVO != null && headerVO.getPrimaryKey() != null) {
						if (!vHid.contains(headerVO.getPrimaryKey())) {
							if (!dmo
									.checkIsPriceSorce(headerVO.getPrimaryKey())) {// �Ƿ���Ϊ�����۸��������ı�����Դ
								vHid.addElement(headerVO.getPrimaryKey());
							}
							itemVOsForDel = (PriceauditBVO[]) tempV.get(1);
							if (itemVOsForDel != null
									&& itemVOsForDel.length > 0) {
								rowIds = new String[itemVOsForDel.length];
								num = new UFDouble[itemVOsForDel.length];
								for (int j = 0; j < itemVOsForDel.length; j++) {
									if (itemVOsForDel[j].getCupsourcebilltype() != null
											&& itemVOsForDel[j]
													.getCupsourcebilltype()
													.equals(IBillType.PRAYBILL)) {
										rowIdsTemp.add(itemVOsForDel[j]
												.getCupsourcebillrowid());
										headIdsTemp.add(itemVOsForDel[j]
												.getCupsourcebillid());
										numTemp.add(null);
									} else if (itemVOsForDel[j]
											.getCupsourcebilltype() != null
											&& itemVOsForDel[j]
													.getCupsourcebilltype()
													.equals(IBillType.ASKBILL)) {
										upPk.add(itemVOsForDel[j]
												.getCupsourcebillrowid());
										upHeaderPk.add(itemVOsForDel[j]
												.getCupsourcebillid());
									}
								}
							}
						}
					}
				}
				// ѯ����ת�۸�����������Դ���빺������Ҳ��Ҫ��д
				if (upPk.size() > 0) {
					Hashtable result = null;
					StringBuffer sqlCondition = new StringBuffer();
					AskbillItemVO itemT = null;
					for (int i = 0; i < upPk.size(); i++) {
						if (i < upPk.size() - 1) {
							sqlCondition.append("'" + upPk.get(i) + "',");
						} else {
							sqlCondition.append("'" + upPk.get(i) + "'");
						}
					}
					// ȡѯ���۵�����Դ������Ϣ
					if (sqlCondition.length() > 0) {
						result = dmo
								.findItemByPrimaryKeyForPriceAudit(sqlCondition
										.toString());
					}
					if (result != null && result.size() > 0) {
						for (int i = 0; i < upPk.size(); i++) {
							itemT = (AskbillItemVO) result.get(upPk.get(i));
							rowIdsTemp.add(itemT.getCupsourcebillrowid());
							headIdsTemp.add(itemT.getCupsourcebillid());
							numTemp.add(null);
						}
					}
				}
				if (vHid.size() > 0) {
					String[] saHId = new String[vHid.size()];
					vHid.copyInto(saHId);
					dmo.deleHeadBodyByHIdsForPriceAudit(saHId);
				}
				// ���˵��ݺ�
				nc.bs.pu.pub.GetSysBillCode rtnBillCode = new nc.bs.pu.pub.GetSysBillCode();
				for (int i = 0; i < iLen; i++) {
					tempV = (Vector) v.get(i);
					headerVO = new PriceauditHeaderVO();
					headerVO = (PriceauditHeaderVO) tempV.get(0);
					itemVO = (PriceauditBVO[]) tempV.get(1);
					askbillVO = new PriceauditVO();
					askbillVO.setParentVO(headerVO);
					askbillVO.setChildrenVO(itemVO);
					rtnBillCode.returnBillNo(askbillVO);
				}
				// ��дѯ���۵���ɱ�־
				if (upHeaderPk.size() > 0) {
					StringBuffer sqlCondition = new StringBuffer();
					Vector result = new Vector();
					for (int i = 0; i < upPk.size(); i++) {
						if (i < upPk.size() - 1) {
							sqlCondition.append("'" + upHeaderPk.get(i) + "',");
						} else {
							sqlCondition.append("'" + upHeaderPk.get(i) + "'");
						}
					}
					if (sqlCondition != null
							&& sqlCondition.toString().trim().length() > 0) {
						result = dmo
								.checkIsCanEffectPriceForRWAskBill(sqlCondition
										.toString());
					}
					if ((result != null && result.size() == 0)
							|| result == null) {
						ReWriteAskBillStatus(upHeaderPk, 1);
					}
				}
				ParaRewriteVO paraVo = new ParaRewriteVO();
				// ��д�빺����(���ϵ�ѯ�۵�������������ѯ�۵�)
				PraybillImpl prayBO = new PraybillImpl();
				if (rowIdsTemp.size() > 0 && numTemp.size() > 0
						&& rowIdsTemp.size() == numTemp.size()) {
					rowIds = new String[rowIdsTemp.size()];
					rowIdsTemp.copyInto(rowIds);
					headIds = new String[headIdsTemp.size()];
					headIdsTemp.copyInto(headIds);
					num = new UFDouble[numTemp.size()];
					numTemp.copyInto(num);
					paraVo.setCBodyIdArray(rowIds);
					paraVo.setCHeadIdArray(headIds);
					paraVo.setDNumArray(num);
					prayBO.updateNaccPriceAuditNum(paraVo, new Integer(IOperation.DELETE).toString());
				}
			
			// ����ɾ���ɹ���־
			isSuccess = true;
			// }
			// else
			// throw new
			// BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000316")/*@res
			// "���ڽ�����ز��������Ժ�����"*/);
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.discardPriceAuditbillVOsMy", e);
		} finally {
			if (isLockSuccess != null && isLockSuccess.booleanValue()) {
				try {
					pubDmo.freePkForVos(askbillVOs);
				} catch (Exception e) {
					nc.bs.pu.pub.PubDMO.throwBusinessException(e);
				}
			}
		}
		return isSuccess;
	}
	/**
	 * @����ѯ�۵�(ѯ��ά��)
	 */
	public AskbillHeaderVO[] queryHeadersForPriceAudit(ConditionVO[] conds,
			String pk_corp) throws BusinessException {
		AskbillHeaderVO[] headers = null;
//		String SQLItems = " and (1=1) ";
		try {
			String strSQL = getSQLForCHG(conds, pk_corp);
			AskbillDMO dmo = new AskbillDMO();
			if(strSQL != null && strSQL.trim().length() > 0){
			headers = dmo.findAllHeadersByCondSQLMyForAskToPrice(strSQL
					);
	        }
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryHeadersForPriceAudit", e);
		}
		return headers;
	}
	/**
	 * ����������ѯ�۸���������ͷVO
	 * @param conds
	 * @param pk_corp
	 * @return
	 * @throws BusinessException
	 */
	public PriceauditHeaderVO[] queryHeadersForPriceAudit2(ConditionVO[] conds,
			String pk_corp) throws BusinessException {
		PriceauditHeaderVO[] headers = null;
//		String SQLItems = " and (1=1) ";
		try {
			String strSQL = getSQLForPriceAuditHeaderVOsMy(conds, pk_corp,null,true);
			AskbillDMO dmo = new AskbillDMO();
			if(strSQL != null && strSQL.trim().length() > 0){
				headers = dmo.findAllHeadersByCondSQLForPriceAudit(strSQL
						);
	        }
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryHeadersForPriceAudit", e);
		}
		return headers;
	}

	/**
	 * @����ѯ�۵�(ѯ��ά��)
	 */
	public Vector queryBodysForPriceAudit(ConditionVO[] conds, String pk_corp,
			String key) throws BusinessException {
		AskbillItemVO[] itemVOs = null;
		AskbillItemBVO[] itemBVOs = null;
		AskbillHeaderVO header = new AskbillHeaderVO();
		header.setPrimaryKey(key);
//		String SQLItems = " and (1=1) ";
		Vector v = new Vector();
		try {
			AskbillDMO dmo = new AskbillDMO();
			itemVOs = dmo.findItemsForHeaderMyArray(
					new AskbillHeaderVO[] { header }, null);
			itemBVOs = dmo.findItemsBForHeaderMyArray(
					new AskbillHeaderVO[] { header }, null,null);
			v.add(itemVOs);
			v.add(itemBVOs);
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryBodysForPriceAudit", e);
		}
		return v;
	}

	/**
	 * @����ѯ�۵�(ѯ��ά��)
	 */
	public Vector queryAllInquireMy(String strSQL) throws BusinessException {
		AskbillHeaderVO[] headers = null;
		AskbillItemVO[] itemVOs = null;
		AskbillItemBVO[] itemBVOs = null;
		AskbillItemVendorVO[] itemVendorVOs = null;
		Vector v = new Vector();
//		String SQLItems = " and (1=1) ";
		try {
			if(strSQL == null || strSQL.trim().length() == 0){
				return v;
			}
//			String strSQL = getSQLForInquireHeaderVOsMy(conds, pk_corp, status);
			AskbillDMO dmo = new AskbillDMO();
			headers = dmo.findAllHeadersByCondSQLMy(strSQL);
			if (headers != null && headers.length > 0) {
				// ֻ��ѯ��һ�ŵ��ݵĵ�����
				itemVOs = dmo.findItemsForHeaderMyArray(
						new AskbillHeaderVO[] { headers[0] }, null);
				itemBVOs = dmo.findItemsBForHeaderMyArray(
						new AskbillHeaderVO[] { headers[0] }, null,null);
				itemVendorVOs = dmo.findItemsVendorForHeaderMyArray(
						new AskbillHeaderVO[] { headers[0] }, null);
				// ������
				// askbills = (AskbillVO[]) getVosWithFreeDealed(askbills,
				// "cmangid", "vfree0");
				// ��֯���ݷ���
				v.add(headers);
				v.add(itemVOs);
				v.add(itemBVOs);
				v.add(itemVendorVOs);
			}
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryAllInquireMy", e);
		}

		return v;
	}

	/**
	 * @����ѯ�۵�(ѯ��ά��)
	 */
	public Vector queryAllInquireMyForExcel(String pk_purorg, String pk_corp,
			String cmangid, String cvendormangid) throws BusinessException {
		AskbillHeaderVO[] headers = null;
		AskbillItemVO[] itemVOs = null;
		AskbillItemBVO[] itemBVOs = null;
		AskbillItemVendorVO[] itemVendorVOs = null;
		Vector v = null;
		Vector result = new Vector();
		String SQLItems = " and ( po_askbill_b.dr = 0 ) ";
		if (cmangid != null && cmangid.trim().length() > 0) {
			SQLItems += " and po_askbill_b.cmangid = '" + cmangid + "'";
		}
		if (cvendormangid != null
				&& cvendormangid.trim().length() > 0) {
			SQLItems += " and po_askbill_b.cvendormangid = '" + cvendormangid
					+ "'";
		}

		try {
			String strSQL = getSQLForInquireHeaderVOsMyForExcel(pk_purorg,
					pk_corp);
			AskbillDMO dmo = new AskbillDMO();
			headers = dmo.findAllHeadersByCondSQLMy(strSQL + SQLItems);
			if (headers != null && headers.length > 0) {
				for (int i = 0; i < headers.length; i++) {
					v = new Vector();
					// ֻ��ѯ��һ�ŵ��ݵĵ�����
					itemVOs = dmo.findItemsForHeaderMyArray(
							new AskbillHeaderVO[] { headers[0] }, SQLItems);
					itemBVOs = dmo.findItemsBForHeaderMyArray(
							new AskbillHeaderVO[] { headers[0] }, SQLItems,null);
					itemVendorVOs = dmo.findItemsVendorForHeaderMyArray(
							new AskbillHeaderVO[] { headers[0] }, SQLItems);
					// ��֯���ݷ���
					v.add(headers);
					v.add(itemVOs);
					v.add(itemBVOs);
					v.add(itemVendorVOs);
					result.add(v);
				}
			}
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryAllInquireMyForExcel", e);
		}

		return result;
	}

	/**
	 * @����ѯ�۵�(ѯ��ά��)
	 */
	public Vector queryAllForPriceAudit(String strSQL) throws BusinessException {
		PriceauditHeaderVO[] headers = null;
		PriceauditBVO[] itemVOs = null;
		PriceauditBb1VO[] itemBVOs = null;
		Vector v = new Vector();
//		String SQLItems = " and (1=1) ";
		try {
//			if(conds == null){
//				return v;
//			}
//			String strSQL = getSQLForPriceAuditHeaderVOsMy(conds, pk_corp,
//					status,true);
			AskbillDMO dmo = new AskbillDMO();
			headers = dmo.findAllHeadersByCondSQLForPriceAudit(strSQL
					);
			if (headers != null && headers.length > 0) {
				// ֻ��ѯ��һ�ŵ��ݵĵ�����
				itemVOs = dmo.findItemsForHeaderPriceAudit(
						new PriceauditHeaderVO[] { headers[0] }, null);
				itemBVOs = dmo.findItemsBForHeaderPriceAudit(
						new PriceauditHeaderVO[] { headers[0] }, null);
				// ������
				// askbills = (AskbillVO[]) getVosWithFreeDealed(askbills,
				// "cmangid", "vfree0");
				// ��֯���ݷ���
				v.add(headers);
				v.add(itemVOs);
				v.add(itemBVOs);
			}
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryAllForPriceAudit", e);
		}

		return v;
	}
	
	public Vector queryAllForPriceAudit(ConditionVO[] conds, String pk_corp,
			UFBoolean[] status) throws BusinessException {
		PriceauditHeaderVO[] headers = null;
		PriceauditBVO[] itemVOs = null;
		PriceauditBb1VO[] itemBVOs = null;
		Vector v = new Vector();
		//�Ƿ����ɹ��������� �ǲ���ѡ���ˡ��񡱣����ѡ���ˡ��񡱣�����Ҫ��cpriceauditid not in()����
		boolean bNotGenOrder = isSelectNotGenorder(conds); 
//		String SQLItems = " and (1=1) ";
		try {
			if(conds == null){
				return v;
			}
			String strSQL = getSQLForPriceAuditHeaderVOsMy(conds, pk_corp,
					status,true);
			//"�Ƿ����ɹ��������� "���ѡ���ˡ��񡱣�����Ҫ��cpriceauditid not in()����
			if(bNotGenOrder){
				String subinSql = strSQL;
				strSQL = StringUtil.replaceAllString(strSQL," and isnull(po_priceaudit_bb1.norderbill,0)>0 " ," " );
				strSQL += " and po_priceaudit.cpriceauditid not in(select distinct po_priceaudit.cpriceauditid "+strSQL+")";
			}
			AskbillDMO dmo = new AskbillDMO();
			headers = dmo.findAllHeadersByCondSQLForPriceAudit(strSQL
					);
			if (headers != null && headers.length > 0) {
				// ֻ��ѯ��һ�ŵ��ݵĵ�����
				itemVOs = dmo.findItemsForHeaderPriceAudit(
						new PriceauditHeaderVO[] { headers[0] }, null);
				itemBVOs = dmo.findItemsBForHeaderPriceAudit(
						new PriceauditHeaderVO[] { headers[0] }, null);
				// ������
				// askbills = (AskbillVO[]) getVosWithFreeDealed(askbills,
				// "cmangid", "vfree0");
				// ��֯���ݷ���
				v.add(headers);
				v.add(itemVOs);
				v.add(itemBVOs);
			}
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryAllForPriceAudit", e);
		}

		return v;
	}
	/**
	 * �Ƿ����ɹ��������� �ǲ���ѡ���ˡ��񡱣����ѡ���ˡ��񡱣��򷵻�true�����򣬷���ture
	 * @param conds
	 * @return
	 */
	private boolean isSelectNotGenorder(ConditionVO[] conds){
		if(conds == null){
			return false;
		}
		for (ConditionVO cond : conds) {
			if(cond.getFieldCode().equalsIgnoreCase("norderbill")){
				//�Ƿ����ɹ��ɹ�����
				if(cond.getValue().equals("2"))
					return true;
				else
					return false;
			}
		}
		return false;
	}
	/**
	 * ���Ӵ�������ѯ����
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * @param strSQL
	 * @return
	 * @throws BusinessException
	 * <p>
	 * @author donggq
	 * @time 2008-8-5 ����05:10:23
	 */
	public Vector  queryAllForPriceAudit(ConditionVO[] conds, String pk_corp, UFBoolean[] status,String strOpr) throws BusinessException {
		ArrayList alret = PuUtils.filterConditionByIsWaitAudit(conds);
		boolean iswaitaudit = false;
		ConditionVO[] fildcondvos = null;
		if(alret!=null){
			iswaitaudit = (Boolean)alret.get(0);
			fildcondvos = (ConditionVO[])alret.get(1);
		}
		if(iswaitaudit){

			PriceauditHeaderVO[] headers = null;
			PriceauditHeaderVO[] oldheaders = null;
			PriceauditBVO[] itemVOs = null;
			PriceauditBb1VO[] itemBVOs = null;
			Vector v = new Vector();
			//�Ƿ����ɹ��������� �ǲ���ѡ���ˡ��񡱣����ѡ���ˡ��񡱣�����Ҫ��cpriceauditid not in()����
			boolean bNotGenOrder = isSelectNotGenorder(conds);
//			String SQLItems = " and (1=1) ";
			try {
				if(conds == null){
					return v;
				}
				String strSQL = getSQLForPriceAuditHeaderVOsMy(conds, pk_corp,
						status,true);
				//"�Ƿ����ɹ��������� "���ѡ���ˡ��񡱣�����Ҫ��cpriceauditid not in()����
				if(bNotGenOrder){
					String subinSql = strSQL;
					strSQL = StringUtil.replaceAllString(strSQL, " and isnull(po_priceaudit_bb1.norderbill,0)>0 "," " );
					strSQL += " and po_priceaudit.cpriceauditid not in(select distinct po_priceaudit.cpriceauditid "+subinSql+")";
				}
				AskbillDMO dmo = new AskbillDMO();
				oldheaders = dmo.findAllHeadersByCondSQLForPriceAudit(strSQL
						);
				headers = (PriceauditHeaderVO[])PuUtils.filterHeadByIsWaitAudit(oldheaders, ScmConst.PO_PriceAudit, strOpr);
				if (headers != null && headers.length > 0) {
					// ֻ��ѯ��һ�ŵ��ݵĵ�����
					itemVOs = dmo.findItemsForHeaderPriceAudit(
							new PriceauditHeaderVO[] { headers[0] }, null);
					itemBVOs = dmo.findItemsBForHeaderPriceAudit(
							new PriceauditHeaderVO[] { headers[0] }, null);
					// ������
					// askbills = (AskbillVO[]) getVosWithFreeDealed(askbills,
					// "cmangid", "vfree0");
					// ��֯���ݷ���
					v.add(headers);
					v.add(itemVOs);
					v.add(itemBVOs);
				}
			} catch (Exception e) {
				/* ���òɹ����÷������淶�׳��쳣 */
				PubDMO.throwBusinessException(
						"nc.bs.pp.AskbillImpl.queryAllForPriceAudit", e);
			}

			return v;
		
		}else{
			return queryAllForPriceAudit(fildcondvos, pk_corp, status);
		}
	}
	
	/**
	 * ���Ӵ�������ѯ����
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * @param strSQL
	 * @return
	 * @throws BusinessException
	 * <p>
	 * @author donggq
	 * @time 2008-8-5 ����05:10:23
	 */
	public Vector  queryAllForPriceAudit(String sCommenWhere, String pk_corp,String strOpr,boolean iswaitaudit) throws BusinessException {
		if(iswaitaudit){
			PriceauditHeaderVO[] headers = null;
			PriceauditHeaderVO[] oldheaders = null;
			PriceauditBVO[] itemVOs = null;
			PriceauditBb1VO[] itemBVOs = null;
			Vector v = new Vector();
//			String SQLItems = " and (1=1) ";
			try {
				if(sCommenWhere == null || sCommenWhere.trim().length() == 0 
						|| sCommenWhere.trim().equals("1=1")){
					return v;
				}
//				String strSQL = getSQLForPriceAuditHeaderVOsMy(conds, pk_corp,
//						status,true);
				AskbillDMO dmo = new AskbillDMO();
				oldheaders = dmo.findAllHeadersByCondSQLForPriceAudit(sCommenWhere);
				headers = (PriceauditHeaderVO[])PuUtils.filterHeadByIsWaitAudit(oldheaders, ScmConst.PO_PriceAudit, strOpr);
				if (headers != null && headers.length > 0) {
					// ֻ��ѯ��һ�ŵ��ݵĵ�����
					itemVOs = dmo.findItemsForHeaderPriceAudit(
							new PriceauditHeaderVO[] { headers[0] }, null);
					itemBVOs = dmo.findItemsBForHeaderPriceAudit(
							new PriceauditHeaderVO[] { headers[0] }, null);
					v.add(headers);
					v.add(itemVOs);
					v.add(itemBVOs);
				}
			} catch (Exception e) {
				/* ���òɹ����÷������淶�׳��쳣 */
				PubDMO.throwBusinessException(
						"nc.bs.pp.AskbillImpl.queryAllForPriceAudit", e);
			}

			return v;
		
		}else{
			return queryAllForPriceAudit(sCommenWhere);
		}
	}

	/**
	 * ��ȡѯ�۵�ά����ѯ���� ״̬Լ�������ɻ򷢳� �������ڣ�(2001-09-09)
	 * 
	 * @return String strSQL ���ظ�ʽΪ��" from ... where ... "
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	private String getSQLForInquireHeaderVOsMy(ConditionVO[] conds,
			String pk_corp, UFBoolean[] status) throws BusinessException {

		String strSQL = " ";
    	ConditionVO[] condsTemp = conds;
         //���û��������¶�������������Ȩ����������
	     ArrayList listRet = OrderPubDMO.dealCondVosForPower(conds);
	     conds = (ConditionVO[]) listRet.get(0);
	     String strDataPowerSql = (String) listRet.get(1);
		
		StringBuffer from = new StringBuffer(" from ");
		from.append("po_askbill ");

		// ��"("Ŀ���ǰѹ�˾�����ŵ������
		// ��ͬ�� where (...) and pk_corp = ... and (״̬Լ��,ҵ��Լ����...)
		// �Ӷ���֤����ֻ��pk_corp �����˾��
		StringBuffer where = new StringBuffer(" where (  ");
		where.append(" po_askbill.dr = 0 ");
		where.append(" and po_askbill_b.dr = 0 ");
		Vector vTableName = new Vector();
		// �����ɾ�������������õ�
		from
				.append("inner JOIN po_askbill_b ON po_askbill.caskbillid = po_askbill_b.caskbillid ");
		vTableName.addElement("po_askbill_b");
		from
				.append("LEFT OUTER JOIN po_askbill_bb1 ON po_askbill_b.caskbillid = po_askbill_bb1.caskbillid and po_askbill_b.caskbill_bid = po_askbill_bb1.caskbill_bid");
		vTableName.addElement("po_askbill_bb1");
		//liuys add for �׸ڿ�ҵ
		from
		.append(" LEFT OUTER JOIN bd_cumandoc ON po_askbill_bb1.cvendorbaseid = bd_cumandoc.pk_cubasdoc ");
		ArrayList ary = null;
		for (int i = 0; i < condsTemp.length; i++) {
			ary = new ArrayList();
			ary = getSQLForFromOfAskbill(condsTemp[i], from.toString(),
					vTableName);
			// �ȴ������
			from.append((String) ary.get(0));
			vTableName = (Vector) ary.get(1);
		}
		
		if (conds != null) {
			//ArrayList ary = null;
			for (int i = 0; i < conds.length; i++) {
//				ary = new ArrayList();
//				ary = getSQLForFromOfAskbill(conds[i], from.toString(),
//						vTableName);
//				// �ȴ������
//				from.append((String) ary.get(0));
//				vTableName = (Vector) ary.get(1);
				
				
				
				// �ٴ����������
				if(i == 0){
				  where.append(getSQLForWhereOfAskbillForFirstConditon(conds[i], pk_corp));
				}else{
				 where.append(getSQLForWhereOfAskbill(conds[i], pk_corp));
				}

			}
		}
		where.append(" )");
	    //�������ݾ�Ȩ������
	    if(strDataPowerSql != null && strDataPowerSql.trim().length() > 0){
			where.append(" and (" + strDataPowerSql + ") ");
		}
		// ��˾Լ��
		if (pk_corp != null && pk_corp.trim().length() > 0) {
			where.append(" and po_askbill.pk_corp = '");
			where.append(pk_corp + "' ");
		}
		// ������Լ��
		
		// where.append( "and po_askbill_bb1.dr = 0 ");
		// String freeCondition = "( 1=1 ";
		// String sendCondition = " ( 1=1 ";
		// String quoteCondition = " ( 1=1 ";
		// String endCondition = " ( 1=1 ";

		// ״̬
		StringBuffer strCond = new StringBuffer("");
		if(status != null){
		UFBoolean[] bStatus = status;
		for (int i = 0; i < bStatus.length; i++) {
			if (bStatus[i] != null && bStatus[i].toString().trim().length() > 0
					&& bStatus[i].booleanValue() ) {
				if (strCond.toString().trim().length() > 0) {
					strCond.append("or ");
				}
				strCond.append(" ibillstatus =  ");
				if (i == 0) {
					strCond.append(0);
				} else if (i == 1) {
					strCond.append(2);
				} else if (i == 2) {
					strCond.append(3);
				} else if (i == 3) {
					strCond.append(4);
				}
				strCond.append(" ");
			}
		}
		}
		// if (status[0].booleanValue()){
		// freeCondition += " and po_askbill.ibillstatus = " +
		// IAskBillStatus.FREE + " ";
		// }
		// if (status[1].booleanValue()){
		// sendCondition += " and po_askbill.ibillstatus = " +
		// IAskBillStatus.SENDING + " ";
		// }
		// if (status[2].booleanValue()){
		// quoteCondition += " and po_askbill.ibillstatus = " +
		// IAskBillStatus.QUOTED + " ";
		// }
		// if (status[3].booleanValue()){
		// endCondition += " and po_askbill.ibillstatus = " +
		// IAskBillStatus.DEPOSED + " ";
		// }
		// freeCondition += " ) ";
		// sendCondition += " ) ";
		// quoteCondition += " ) ";
		// endCondition += " ) ";
		if (strCond.length() > 0) {
			where.append("and (" + strCond.toString() + ") ");
		}
		strSQL += from;
		strSQL += where;

		// String sql = "select DISTINCT po_askbill.pk_corp,
		// po_askbill.vaskbillcode, po_askbill.cvendormangid,
		// po_askbill.cdeptid, po_askbill.cemployeeid,
		// po_askbill.ccurrencytypeid, po_askbill.dclosedate,
		// po_askbill.caskpsn, po_askbill.daskdate, po_askbill.cquotepsn,
		// po_askbill.dquotedate, po_askbill.ctermprotocolid,
		// po_askbill.caccountyear, po_askbill.ibillstatus, po_askbill.vdef1,
		// po_askbill.vdef2, po_askbill.vdef3, po_askbill.vdef4,
		// po_askbill.vdef5, po_askbill.vdef6, po_askbill.vdef7,
		// po_askbill.vdef8, po_askbill.vdef9, po_askbill.vdef10,
		// po_askbill.caskbillid, po_askbill.cvendorbaseid ";
		// sql += strSQL;
		// SCMEnv.out("��ѯ����Ϊ�� " + sql);
		return strSQL;
	}

	/**
	 * ��ȡѯ�۵�ά����ѯ���� ״̬Լ�������ɻ򷢳� �������ڣ�(2001-09-09)
	 * 
	 * @return String strSQL ���ظ�ʽΪ��" from ... where ... "
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	private String getSQLForInquireHeaderVOsMyForExcel(String pk_purorg,
			String pk_corp) throws BusinessException {

		String strSQL = " ";
		StringBuffer from = new StringBuffer(" from ");
		from.append("po_askbill ");

		// ��"("Ŀ���ǰѹ�˾�����ŵ������
		// ��ͬ�� where (...) and pk_corp = ... and (״̬Լ��,ҵ��Լ����...)
		// �Ӷ���֤����ֻ��pk_corp �����˾��
		StringBuffer where = new StringBuffer(" where (  ");
        //������Լ��
		where.append(" po_askbill.dr = 0 ");
		where.append(" and po_askbill_b.dr = 0 ");
		
		Vector vTableName = new Vector();
		// �����ɾ�������������õ�
		from
				.append("inner JOIN po_askbill_b ON po_askbill.caskbillid = po_askbill_b.caskbillid ");
		vTableName.addElement("po_askbill_b");
		from
				.append("LEFT OUTER JOIN po_askbill_bb1 ON po_askbill.caskbillid = po_askbill_bb1.caskbillid and po_askbill_b.caskbill_bid = po_askbill_bb1.caskbill_bid");
		vTableName.addElement("po_askbill_bb1");
		where.append(" )");

		// ��˾Լ��
		if (pk_corp != null && pk_corp.trim().length() > 0) {
			where.append(" and po_askbill.pk_corp = '");
			where.append(pk_corp + "' ");
		}
		// �ɹ���֯Լ��
		if (pk_purorg != null && pk_purorg.trim().length() > 0) {
			where.append(" and po_askbill.pk_purorg = '");
			where.append(pk_purorg + "' ");
		}
		
		// where.append( "and po_askbill_bb1.dr = 0 ");
		String freeCondition = "( po_askbill.ibillstatus > 0 ";
		String sendCondition = " ( po_askbill.ibillstatus > 0 ";
		String quoteCondition = " ( po_askbill.ibillstatus > 0 ";
		String endCondition = " ( po_askbill.ibillstatus > 0 ";

		// ״̬
		freeCondition += " and po_askbill.ibillstatus = " + IAskBillStatus.FREE
				+ " ";
		sendCondition += " and po_askbill.ibillstatus = "
				+ IAskBillStatus.SENDING + " ";
		quoteCondition += " and po_askbill.ibillstatus = "
				+ IAskBillStatus.QUOTED + " ";

		freeCondition += " ) ";
		sendCondition += " ) ";
		quoteCondition += " ) ";
		endCondition += " ) ";
		where.append("  and  (  " + freeCondition + " or " + sendCondition
				+ "  or " + quoteCondition + " or " + endCondition + " ) ");
		strSQL += from;
		strSQL += where;

		// String sql = "select DISTINCT po_askbill.pk_corp,
		// po_askbill.vaskbillcode, po_askbill.cvendormangid,
		// po_askbill.cdeptid, po_askbill.cemployeeid,
		// po_askbill.ccurrencytypeid, po_askbill.dclosedate,
		// po_askbill.caskpsn, po_askbill.daskdate, po_askbill.cquotepsn,
		// po_askbill.dquotedate, po_askbill.ctermprotocolid,
		// po_askbill.caccountyear, po_askbill.ibillstatus, po_askbill.vdef1,
		// po_askbill.vdef2, po_askbill.vdef3, po_askbill.vdef4,
		// po_askbill.vdef5, po_askbill.vdef6, po_askbill.vdef7,
		// po_askbill.vdef8, po_askbill.vdef9, po_askbill.vdef10,
		// po_askbill.caskbillid, po_askbill.cvendorbaseid ";
		// sql += strSQL;
		// SCMEnv.out("��ѯ����Ϊ�� " + sql);
		return strSQL;
	}

	/**
	 * ��ȡѯ�۵�ά����ѯ���� ״̬Լ�������ɻ򷢳� �������ڣ�(2001-09-09)
	 * 
	 * @return String strSQL ���ظ�ʽΪ��" from ... where ... "
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	private String getSQLForCHG(ConditionVO[] conds, String pk_corp)
			throws BusinessException {
        
		
		String strSQL = " ";
		ConditionVO[] condsTemp = conds;
		 //���û��������¶�������������Ȩ����������
	     ArrayList listRet = OrderPubDMO.dealCondVosForPower(conds);
	     conds = (ConditionVO[]) listRet.get(0);
	     String strDataPowerSql = (String) listRet.get(1);
	     
		StringBuffer from = new StringBuffer(" from ");
		from.append("po_askbill ");

		// ��"("Ŀ���ǰѹ�˾�����ŵ������
		// ��ͬ�� where (...) and pk_corp = ... and (״̬Լ��,ҵ��Լ����...)
		// �Ӷ���֤����ֻ��pk_corp �����˾��
		StringBuffer where = new StringBuffer(" where ( ");
		where.append("  po_askbill.dr = 0 ");
		where.append(" and po_askbill_b.dr = 0 ");
		where.append( " and po_askbill_bb1.dr = 0 ");
		Vector vTableName = new Vector();
		// �����ɾ�������������õ�
		from
				.append("inner JOIN po_askbill_b ON po_askbill.caskbillid = po_askbill_b.caskbillid ");
		vTableName.addElement("po_askbill_b");
		from
				.append("LEFT OUTER JOIN po_askbill_bb1 ON po_askbill_b.caskbillid = po_askbill_bb1.caskbillid and po_askbill_b.caskbill_bid = po_askbill_bb1.caskbill_bid");
		vTableName.addElement("po_askbill_bb1");
		
		ArrayList ary = null;
		for (int i = 0; i < condsTemp.length; i++) {
			ary = new ArrayList();
			ary = getSQLForFromOfAskbill(condsTemp[i], from.toString(),
					vTableName);
			// �ȴ������
			from.append((String) ary.get(0));
			vTableName = (Vector) ary.get(1);
		}
		
		if (conds != null) {
			//ArrayList ary = null;
			for (int i = 0; i < conds.length; i++) {
				ary = new ArrayList();
				ary = getSQLForFromOfAskbill(conds[i], from.toString(),
						vTableName);
				// �ȴ������
				from.append((String) ary.get(0));
				vTableName = (Vector) ary.get(1);
				// �ٴ����������
				if(i == 0){
				 where.append(getSQLForWhereOfAskbillForFirstConditon(conds[i], pk_corp));
				}else{
				 where.append(getSQLForWhereOfAskbill(conds[i], pk_corp));
				}
			}
		}
		where.append(" )");
		
          //�������ݾ�Ȩ������
		  if(strDataPowerSql != null && strDataPowerSql.trim().length() > 0){
			where.append(" and (" + strDataPowerSql + ") ");
		  }

		// ��˾Լ��
		if (pk_corp != null && pk_corp.trim().length() > 0) {
			where.append(" and po_askbill.pk_corp = '");
			where.append(pk_corp + "' ");
		}
		// ������Լ��
		
		where.append("and po_askbill.ibillstatus = " + IAskBillStatus.QUOTED
				+ " ");
		// �Ѿ����ɼ۸����������빺��,��������ѯ���۵�,���ǲ�����ѯ���۵��ٴ����ɼ۸�������
		// where.append( "and po_askbill_b.cupsourcebillrowid in( select
		// cpraybill_bid ");
		// where.append( "from po_praybill_b inner join po_praybill on
		// po_praybill_b.cpraybillid = po_praybill.cpraybillid ");
		// where.append( "where (po_praybill_b.npriceauditbill = null or
		// po_praybill_b.npriceauditbill = 0) ");
		// where.append( "and po_praybill_b.dr = 0 and po_praybill.dr = 0 and
		// po_praybill.ibillstatus = 3)");
		strSQL += from;
		strSQL += where;

		// String sql = "select DISTINCT po_askbill.pk_corp,
		// po_askbill.vaskbillcode, po_askbill.cvendormangid,
		// po_askbill.cdeptid, po_askbill.cemployeeid,
		// po_askbill.ccurrencytypeid, po_askbill.dclosedate,
		// po_askbill.caskpsn, po_askbill.daskdate, po_askbill.cquotepsn,
		// po_askbill.dquotedate, po_askbill.ctermprotocolid,
		// po_askbill.caccountyear, po_askbill.ibillstatus, po_askbill.vdef1,
		// po_askbill.vdef2, po_askbill.vdef3, po_askbill.vdef4,
		// po_askbill.vdef5, po_askbill.vdef6, po_askbill.vdef7,
		// po_askbill.vdef8, po_askbill.vdef9, po_askbill.vdef10,
		// po_askbill.caskbillid, po_askbill.cvendorbaseid ";
		// sql += strSQL;
		// SCMEnv.out("��ѯ����Ϊ�� " + sql);
		
		
		return strSQL;
	}

	/**
	 * ��ȡѯ�۵�ά����ѯ���� ״̬Լ�������ɻ򷢳� �������ڣ�(2001-09-09)
	 * 
	 * @return String strSQL ���ظ�ʽΪ��" from ... where ... "
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	private String getSQLForPriceAuditHeaderVOsMy(ConditionVO[] conds,
			String pk_corp, UFBoolean[] status,boolean isContainFrom) throws BusinessException {

		String strSQL = " ";
		ConditionVO[] condsTemp = conds;
	    //���û��������¶�������������Ȩ����������
	    ArrayList listRet = OrderPubDMO.dealCondVosForPower(conds);
	    conds = (ConditionVO[]) listRet.get(0);
	    String strDataPowerSql = (String) listRet.get(1);
	    
		StringBuffer from = new StringBuffer(" from ");
		from.append("po_priceaudit ");

		// ��"("Ŀ���ǰѹ�˾�����ŵ������
		// ��ͬ�� where (...) and pk_corp = ... and (״̬Լ��,ҵ��Լ����...)
		// �Ӷ���֤����ֻ��pk_corp �����˾��
		StringBuffer where = new StringBuffer(" ");
		if(isContainFrom){
			where.append(" where (  ");
		}else{
			where.append("and (");
		}
		where.append("  po_priceaudit.dr = 0 ");
		where.append(" and po_priceaudit_b.dr = 0 ");
		Vector vTableName = new Vector();
		// �����ɾ�������������õ�
		from
				.append("inner JOIN po_priceaudit_b ON po_priceaudit.cpriceauditid = po_priceaudit_b.cpriceauditid ");
		vTableName.addElement("po_priceaudit_b");
		from
				.append("LEFT OUTER JOIN po_priceaudit_bb1 ON po_priceaudit_b.cpriceauditid = po_priceaudit_bb1.cpriceauditid and po_priceaudit_b.cpriceaudit_bid = po_priceaudit_bb1.cpriceaudit_bid");
		vTableName.addElement("po_priceaudit_bb1");
		ArrayList ary = null;
		for (int i = 0; i < condsTemp.length; i++) {
			ary = new ArrayList();
			ary = getSQLForFromOfPriceAuditbill(condsTemp[i], from.toString(),
					vTableName);
			// �ȴ������
			from.append((String) ary.get(0));
			vTableName = (Vector) ary.get(1);
		}
		if (conds != null) {
			//ArrayList ary = null;
			for (int i = 0; i < conds.length; i++) {
//				ary = new ArrayList();
//				ary = getSQLForFromOfPriceAuditbill(conds[i], from.toString(),
//						vTableName);
//				// �ȴ������
//				from.append((String) ary.get(0));
//				vTableName = (Vector) ary.get(1);
				// �ٴ����������
				if(i == 0){
				 where.append(getSQLForWhereOfAskbillForFirstConditon(conds[i], pk_corp));
				}else{
				 where.append(getSQLForWhereOfAskbill(conds[i], pk_corp));
				}
                
			}
		}
		where.append(" )");
	    //�������ݾ�Ȩ������
		if(strDataPowerSql != null && strDataPowerSql.trim().length() > 0){
		   where.append(" and (" + strDataPowerSql + ") ");
		}
		// ��˾Լ��
		if (pk_corp != null && pk_corp.trim().length() > 0) {
			where.append(" and po_priceaudit.pk_corp = '");
			where.append(pk_corp + "' ");
		}
		// ������Լ��
		
		// where.append( "and po_priceaudit_bb1.dr = 0 ");
		// ״̬
		StringBuffer strCond = new StringBuffer("");
		if(status != null){
		for (int i = 0; i < status.length; i++) {
			if (status[i].booleanValue() ) {
				if (strCond.toString().trim().length() > 0) {
					strCond.append("or ");
				}
				strCond.append("po_priceaudit.ibillstatus = ");
				if(i == 0){
				     strCond.append(0);
				}else if(i == 1){
				     strCond.append(2);
				}
				else if( i == 2){
					strCond.append(4);
				}else if( i == 3){
					strCond.append(3);
				}		
				strCond.append(" ");
			}
		}
		if(strCond.toString().trim().length()!=0)
			where.append("and (" + strCond.toString() + ")");
		}
		if(isContainFrom){
			strSQL += from;
		}
		strSQL += where;

		String sql = "select DISTINCT  po_priceaudit.pk_corp, po_priceaudit.vaskbillcode, po_priceaudit.cvendormangid, po_priceaudit.cdeptid, po_priceaudit.cemployeeid, po_priceaudit.ccurrencytypeid, po_priceaudit.dclosedate, po_priceaudit.caskpsn, po_priceaudit.daskdate, po_priceaudit.cquotepsn, po_priceaudit.dquotedate, po_priceaudit.ctermprotocolid, po_priceaudit.caccountyear, po_priceaudit.ibillstatus, po_priceaudit.vdef1, po_priceaudit.vdef2, po_priceaudit.vdef3, po_priceaudit.vdef4, po_priceaudit.vdef5, po_priceaudit.vdef6, po_priceaudit.vdef7, po_priceaudit.vdef8, po_priceaudit.vdef9, po_priceaudit.vdef10, po_priceaudit.caskbillid, po_priceaudit.cvendorbaseid ";
		sql += strSQL;
		SCMEnv.out("��ѯ����Ϊ�� " + sql);
		return strSQL;
	}

	/**
	 * ��ȡָ������ѯ�۵�VO�Ĺ�����ϵ(SQL����е� "from ...") ע�⣺�˷�����ǰ�������Ǳ�ͷ�������������!!
	 * �������ڣ�(2001-10-23 9:21:33)
	 * 
	 * @return java.lang.String
	 * @param cond
	 *            nc.vo.pub.query.ConditionVO
	 * @param from
	 *            java.lang.String
	 * @param vTableName
	 *            Vector
	 */
	private ArrayList getSQLForFromOfAskbill(ConditionVO cond, String from,
			Vector vTableName) throws BusinessException {
		ArrayList ary = new ArrayList();
		StringBuffer fromNew = new StringBuffer(" ");
		try {
			// ��Ӧ��
			if ("bd_cubasdoc.custcode".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_cubasdoc")) {
					fromNew
							.append("LEFT OUTER JOIN bd_cubasdoc ON po_askbill_bb1.cvendorbaseid = bd_cubasdoc.pk_cubasdoc ");
					vTableName.addElement("bd_cubasdoc");
				}
			} else
			// ����
			if ("bd_deptdoc.deptcode".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_deptdoc")) {
					fromNew
							.append("LEFT OUTER JOIN bd_deptdoc ON po_askbill.cdeptid = bd_deptdoc.pk_deptdoc ");
					vTableName.addElement("bd_deptdoc");
				}
			} else
			// ҵ��Ա
			if ("bd_psndoc.psncode".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_psndoc")) {
					fromNew
							.append("LEFT OUTER JOIN bd_psndoc ON po_askbill.cemployeeid = bd_psndoc.pk_psndoc ");
					vTableName.addElement("bd_psndoc");
				}
			} else
			// ����(����)
			if ("bd_currtype.currtypecode".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_currtype")) {
					fromNew
							.append("LEFT OUTER JOIN bd_currtype ON po_askbill.ccurrencytypeid = bd_currtype.pk_currtype ");
					vTableName.addElement("bd_currtype");
				}
			} else
			// ����(����)
			if ("bd_currtype.pk_currtype".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_currtype")) {
					fromNew
							.append("LEFT OUTER JOIN bd_currtype ON po_askbill.ccurrencytypeid = bd_currtype.pk_currtype ");
					vTableName.addElement("bd_currtype");
				}
			} else
			// ����Э��(���ݿ������ޱ���,czp09-10,������������Ϊ��ѯ����)
			// ����Э��ı���Ϊ bd_payterm.termid czp 10-23
			if ("bd_payterm.termid".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_payterm")) {
					fromNew
							.append("LEFT OUTER JOIN bd_payterm ON po_askbill.ctermprotocolid = bd_payterm.pk_payterm ");
					vTableName.addElement("bd_payterm");
				}
			} else
			// �������
			if ("bd_invbasdoc.invcode".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_invbasdoc")) {
					fromNew
							.append("LEFT OUTER JOIN bd_invbasdoc ON po_askbill_b.cbaseid = bd_invbasdoc.pk_invbasdoc ");
					vTableName.addElement("bd_invbasdoc");
				}
			} else
			// ѯ���˱���
			if ("sm_user.user_code1".equals(cond.getFieldCode())) {
				if (!vTableName.contains("sm_user1")) {
					fromNew
							.append("LEFT OUTER JOIN sm_user as sm_user1 ON po_askbill.caskpsn = sm_user1.cUserId ");
					vTableName.addElement("sm_user1");
				}
			} else
			// �����˱���
			if ("sm_user.user_code2".equals(cond.getFieldCode())) {
				if (!vTableName.contains("sm_user2")) {
					fromNew
							.append("LEFT OUTER JOIN sm_user as sm_user2 ON po_askbill.cquotepsn = sm_user2.cUserId ");
					vTableName.addElement("sm_user2");
				}
			} else
			// �ɹ���֯(ѯ�۵���ͷ)
			if ("bd_purorg.code".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_purorg")) {
					fromNew
							.append("LEFT OUTER JOIN bd_purorg ON po_askbill.pk_purorg = bd_purorg.pk_purorg ");
					vTableName.addElement("bd_purorg");
				}
			}
		} catch (Exception e) {
			/* ���쳣�����׳� */
			SCMEnv.out(e.getMessage());
			SCMEnv
					.out("��ȡָ������ѯ�۵�VO�Ĺ�����ϵʱ����(AskbillBO.getSQLForFromOfAskbill())");
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.getSQLForFromOfAskbill", e);
		}
		ary.add(fromNew.toString());
		ary.add(vTableName);

		return ary;
	}

	/**
	 * ��ȡָ������ѯ�۵�VO�Ĺ�����ϵ(SQL����е� "from ...") ע�⣺�˷�����ǰ�������Ǳ�ͷ�������������!!
	 * �������ڣ�(2001-10-23 9:21:33)
	 * 
	 * @return java.lang.String
	 * @param cond
	 *            nc.vo.pub.query.ConditionVO
	 * @param from
	 *            java.lang.String
	 * @param vTableName
	 *            Vector
	 */
	private ArrayList getSQLForFromOfPriceAuditbill(ConditionVO cond,
			String from, Vector vTableName) throws BusinessException {
		ArrayList ary = new ArrayList();
		StringBuffer fromNew = new StringBuffer(" ");
		try {
			// ��Ӧ��
			if ("bd_cubasdoc.custcode".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_cubasdoc")) {
					fromNew
							.append("LEFT OUTER JOIN bd_cubasdoc ON po_priceaudit_bb1.cvendorbaseid = bd_cubasdoc.pk_cubasdoc ");
					vTableName.addElement("bd_cubasdoc");
				}
			} else
			// ����
			if ("bd_deptdoc.deptcode".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_deptdoc")) {
					fromNew
							.append("LEFT OUTER JOIN bd_deptdoc ON po_priceaudit.cdeptid = bd_deptdoc.pk_deptdoc ");
					vTableName.addElement("bd_deptdoc");
				}
			} else
			// ҵ��Ա
			if ("bd_psndoc.psncode".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_psndoc")) {
					fromNew
							.append("LEFT OUTER JOIN bd_psndoc ON po_priceaudit.cemployeeid = bd_psndoc.pk_psndoc ");
					vTableName.addElement("bd_psndoc");
				}
			} else
			// ����(����)
			if ("bd_currtype.currtypecode".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_currtype")) {
					fromNew
							.append("LEFT OUTER JOIN bd_currtype ON po_priceaudit.ccurrencytypeid = bd_currtype.pk_currtype ");
					vTableName.addElement("bd_currtype");
				}
			} else
			// ����(����)
			if ("bd_currtype.pk_currtype".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_currtype")) {
					fromNew
							.append("LEFT OUTER JOIN bd_currtype ON po_priceaudit.ccurrencytypeid = bd_currtype.pk_currtype ");
					vTableName.addElement("bd_currtype");
				}
			} else
			// ����Э��(���ݿ������ޱ���,czp09-10,������������Ϊ��ѯ����)
			// ����Э��ı���Ϊ bd_payterm.termid czp 10-23
			if ("bd_payterm.termid".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_payterm")) {
					fromNew
							.append("LEFT OUTER JOIN bd_payterm ON po_priceaudit.ctermprotocolid = bd_payterm.pk_payterm ");
					vTableName.addElement("bd_payterm");
				}
			} else
			// �������
			if ("bd_invbasdoc.invcode".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_invbasdoc")) {
					fromNew
							.append("LEFT OUTER JOIN bd_invbasdoc ON po_priceaudit_b.cbaseid = bd_invbasdoc.pk_invbasdoc ");
					vTableName.addElement("bd_invbasdoc");
				}
			} else
			// ѯ���˱���
			if ("sm_user.user_code1".equals(cond.getFieldCode())) {
				if (!vTableName.contains("sm_user1")) {
					fromNew
							.append("LEFT OUTER JOIN sm_user as sm_user1 ON po_priceaudit.caskpsn = sm_user1.cUserId ");
					vTableName.addElement("sm_user1");
				}
			} else
			// �����˱���
			if ("sm_user.user_code2".equals(cond.getFieldCode())) {
				if (!vTableName.contains("sm_user2")) {
					fromNew
							.append("LEFT OUTER JOIN sm_user as sm_user2 ON po_priceaudit.cquotepsn = sm_user2.cUserId ");
					vTableName.addElement("sm_user2");
				}
			} else
			// �ɹ���֯(ѯ�۵���ͷ)
			if ("bd_purorg.code".equals(cond.getFieldCode())) {
				if (!vTableName.contains("bd_purorg")) {
					fromNew
							.append("LEFT OUTER JOIN bd_purorg ON po_priceaudit.pk_purorg = bd_purorg.pk_purorg ");
					vTableName.addElement("bd_purorg");
				}
			}
		} catch (Exception e) {
			/* ���쳣�����׳� */
			SCMEnv.out(e.getMessage());
			SCMEnv
					.out("��ȡָ������ѯ�۵�VO�Ĺ�����ϵʱ����(AskbillBO.getSQLForFromOfAskbill())");
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.getSQLForFromOfPriceAuditbill", e);
		}
		ary.add(fromNew.toString());
		ary.add(vTableName);

		return ary;
	}

	/**
	 * ��ȡָ����������VO�Ĳ�ѯ���(SQL����е� "where ...") �������ڣ�(2001-10-23 9:21:33)
	 * 
	 * @return java.lang.String
	 * @param cond
	 *            nc.vo.pub.query.ConditionVO
	 * @param from
	 *            java.lang.String
	 * @param vTableName
	 *            Vector
	 */
	private String getSQLForWhereOfAskbill(ConditionVO cond, String pk_corp)
			throws BusinessException {

		StringBuffer whereNew = new StringBuffer(" ");
		try {

			/* ���⴦�� */

			/* ѯ���˱��� */
			if ("sm_user.user_code1".equals(cond.getFieldCode())) {
				cond.setFieldCode("sm_user1.user_code");
			}
			/* �����˱��� */
			if ("sm_user.user_code2".equals(cond.getFieldCode())) {
				cond.setFieldCode("sm_user2.user_code");
			}
			/* ���̵���������� */
			if ("bd_areacl.areaclcode".equals(cond.getFieldCode())
					&& cond.getValue() != null
					&& !"".equals(cond.getValue().trim())) {
				/* ˢ������VO��ֵ */
				String strAreaclCodeSet = "";
				strAreaclCodeSet = getAreaClassCodeSet(cond, pk_corp);
				cond.setValue(strAreaclCodeSet);
				/* ˢ������VO�Ĳ����� */
				cond.setOperaCode("in");
			}
			/* ���������� */
			if ("bd_invcl.invclasscode".equals(cond.getFieldCode())
					&& cond.getValue() != null
					&& !"".equals(cond.getValue().trim())) {
				/* ˢ������VO��ֵ */
				String strInvClassCodeSet = "";
				strInvClassCodeSet = getInvClassCodeSet(cond, pk_corp);
				cond.setValue(strInvClassCodeSet);
				/* ˢ������VO�Ĳ����� */
				cond.setOperaCode("in");
			}
			
			if (cond.getFieldCode().equalsIgnoreCase("islinkorder")){
				//��ͬ��Чʱ�Ƿ����������Ϊ��ִ������
				return " and po_priceaudit_b.cpriceaudit_bid=po_priceaudit_b.cpriceaudit_bid ";
			}
			if (cond.getFieldCode().equalsIgnoreCase("po_priceaudit_b.cbaseid")
					&& cond.getValue().indexOf("select") != -1){
				return " and po_priceaudit_b.cbaseid in ("+cond.getValue()+")";
			}
			if(cond.getFieldCode().equalsIgnoreCase("ISNULL(ngenct, 0)")){
				//�ɹ���ͬ���ռ۸�������ʱʹ��
				if(cond.getOperaCode().equals("="))
					return " and isnull(po_priceaudit_bb1.ngenct,0)=0 ";
				else if(cond.getOperaCode().equals("<>"))
					return " and isnull(po_priceaudit_bb1.ngenct,0)<>0 ";
				else 
					return " ";
			}
			//���˵� 1 = 1 �����
			if(cond.getFieldCode().equalsIgnoreCase("1")){
				return " ";
			}
			if(cond.getFieldCode().equalsIgnoreCase("po_priceaudit.ibillstatus")){
				//ת��ʱû�г�������������״̬��ʹ������һ��conditionVO�ķ�����״̬Ϊ������ͨ��
				return " and po_priceaudit.ibillstatus = 3 ";
			}
			if (cond.getFieldCode().equalsIgnoreCase("iswaitaudit")){
				return " ";
			}
			if(cond.getFieldCode().equalsIgnoreCase("norderbill")){
				//�Ƿ����ɹ��ɹ�����
				if(cond.getValue().equals("1")
						|| cond.getValue().equals("2"))
					return " and isnull(po_priceaudit_bb1.norderbill,0)>0 ";
				else if (cond.getValue().equals("0"))
					return " ";
			}
			/* ͳһ���� */

			whereNew.append(getWhereByFieldCondVOMy(cond.getFieldCode(), cond,
					new UFBoolean(false)));

		} catch (Exception e) {
			SCMEnv.out("��ȡָ�������۸�������VO�Ĳ�ѯ���ʱ����,���ܵ��²�ѯ�������ȷ");
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.getSQLForWhereOfAskbill", e);
		}
		return whereNew.toString();
	}
	/**
	 * ��ȡָ����������VO�Ĳ�ѯ���(SQL����е� "where ...") �������ڣ�(2001-10-23 9:21:33)
	 * 
	 * @return java.lang.String
	 * @param cond
	 *            nc.vo.pub.query.ConditionVO
	 * @param from
	 *            java.lang.String
	 * @param vTableName
	 *            Vector
	 */
	private String getSQLForWhereOfAskbillForFirstConditon(ConditionVO cond, String pk_corp)
			throws BusinessException {

		StringBuffer whereNew = new StringBuffer(" ");
		try {

			/* ���⴦�� */

			/* ѯ���˱��� */
			if ("sm_user.user_code1".equals(cond.getFieldCode())) {
				cond.setFieldCode("sm_user1.user_code");
			}
			/* �����˱��� */
			if ("sm_user.user_code2".equals(cond.getFieldCode())) {
				cond.setFieldCode("sm_user2.user_code");
			}
			/* ���̵���������� */
			if ("bd_areacl.areaclcode".equals(cond.getFieldCode())
					&& cond.getValue() != null
					&& !"".equals(cond.getValue().trim())) {
				/* ˢ������VO��ֵ */
				String strAreaclCodeSet = "";
				strAreaclCodeSet = getAreaClassCodeSet(cond, pk_corp);
				cond.setValue(strAreaclCodeSet);
				/* ˢ������VO�Ĳ����� */
				cond.setOperaCode("in");
			}
			/* ���������� */
			if ("bd_invcl.invclasscode".equals(cond.getFieldCode())
					&& cond.getValue() != null
					&& !"".equals(cond.getValue().trim())) {
				/* ˢ������VO��ֵ */
				String strInvClassCodeSet = "";
				strInvClassCodeSet = getInvClassCodeSet(cond, pk_corp);
				cond.setValue(strInvClassCodeSet);
				/* ˢ������VO�Ĳ����� */
				cond.setOperaCode("in");
			}

			if (cond.getFieldCode().equalsIgnoreCase("islinkorder")){
				//��ͬ��Чʱ�Ƿ����������Ϊ��ִ������
				return " and po_priceaudit_b.cpriceaudit_bid=po_priceaudit_b.cpriceaudit_bid ";
			}
			if (cond.getFieldCode().equalsIgnoreCase("po_priceaudit_b.cbaseid")
					&& cond.getValue().indexOf("select") != -1){
				return " and po_priceaudit_b.cbaseid in ("+cond.getValue()+")";
			}
			if(cond.getFieldCode().equalsIgnoreCase("ISNULL(ngenct, 0)")){
				//�ɹ���ͬ���ռ۸�������ʱʹ��
				if(cond.getOperaCode().equals("="))
					return " and isnull(po_priceaudit_bb1.ngenct,0)=0 ";
				else if(cond.getOperaCode().equals("<>"))
					return " and isnull(po_priceaudit_bb1.ngenct,0)<>0 ";
				else 
					return " ";
			}
			//���˵� 1 = 1 �����
			if(cond.getFieldCode().equalsIgnoreCase("1")){
				return " ";
			}
			if(cond.getFieldCode().equalsIgnoreCase("po_priceaudit.ibillstatus")){
				//ת��ʱû�г�������������״̬��ʹ������һ��conditionVO�ķ�����״̬Ϊ������ͨ��
				return " and po_priceaudit.ibillstatus = 3 ";
			}
			if (cond.getFieldCode().equalsIgnoreCase("iswaitaudit")){
				return " ";
			}
			if(cond.getFieldCode().equalsIgnoreCase("norderbill")){
				//�Ƿ����ɹ��ɹ�����
				if(cond.getValue().equals("1") 
						||cond.getValue().equals("2"))
					return " and isnull(po_priceaudit_bb1.norderbill,0)>0 ";
				else if (cond.getValue().equals("0"))
					return " ";
			}
			/* ͳһ���� */

			whereNew.append(getWhereByFieldCondVOMy(cond.getFieldCode(), cond,
					new UFBoolean(false)));

		} catch (Exception e) {
			SCMEnv.out("��ȡָ������������VO�Ĳ�ѯ���ʱ����,���ܵ��²�ѯ�������ȷ");
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.getSQLForWhereOfAskbillForFirstConditon", e);
		}
		return whereNew.toString();
	}
	/**
	 * ��ȡָ����������VO�Ĳ�ѯ���(SQL����е� "where ...") �������ڣ�(2001-10-23 9:21:33)
	 * 
	 * @return java.lang.String
	 * @param cond
	 *            nc.vo.pub.query.ConditionVO
	 * @param from
	 *            java.lang.String
	 * @param vTableName
	 *            Vector
	 */
	private String getSQLForWhereOfAskbillForReport(ConditionVO cond, String pk_corp,String[] pk_corps)
			throws BusinessException {

		StringBuffer whereNew = new StringBuffer(" ");
		try {

			/* ���⴦�� */

			/* ѯ���˱��� */
			if ("sm_user.user_code1".equals(cond.getFieldCode())) {
				cond.setFieldCode("sm_user1.user_code");
			}
			/* �����˱��� */
			if ("sm_user.user_code2".equals(cond.getFieldCode())) {
				cond.setFieldCode("sm_user2.user_code");
			}
			/* ���̵���������� */
			if ("bd_areacl.areaclcode".equals(cond.getFieldCode())
					&& cond.getValue() != null
					&& !"".equals(cond.getValue().trim())) {
				/* ˢ������VO��ֵ */
				String strAreaclCodeSet = "";
				strAreaclCodeSet = getAreaClassCodeSetForReport(cond, pk_corp,pk_corps);
				cond.setValue(strAreaclCodeSet);
				/* ˢ������VO�Ĳ����� */
				cond.setOperaCode("in");
			}
			/* ���������� */
			if ("bd_invcl.invclasscode".equals(cond.getFieldCode())
					&& cond.getValue() != null
					&& !"".equals(cond.getValue().trim())) {
				/* ˢ������VO��ֵ */
				String strInvClassCodeSet = "";
				strInvClassCodeSet = getInvClassCodeSet(cond, pk_corp);
				cond.setValue(strInvClassCodeSet);
				/* ˢ������VO�Ĳ����� */
				cond.setOperaCode("in");
			}

			/* ͳһ���� */
			if("bd_cubasdoc.custcode".equals(cond.getFieldCode()) 
				|| 	"bd_deptdoc.deptcode".equals(cond.getFieldCode())
				||  "bd_psndoc.psncode".equals(cond.getFieldCode())
				||  "bd_invbasdoc.invcode".equals(cond.getFieldCode())){
              if(cond.getValue() != null &&  cond.getValue().trim().length() > 0 && !"null".equalsIgnoreCase(cond.getValue().trim()) && !cond.getValue().trim().startsWith("(select")){
			     whereNew.append(getWhereByFieldCondVOMy(cond.getFieldCode(), cond,
					new UFBoolean(false)));
             }
			}else{
				 whereNew.append(getWhereByFieldCondVOMy(cond.getFieldCode(), cond,
							new UFBoolean(false)));
			}

		} catch (Exception e) {
			SCMEnv.out("��ȡָ������������VO�Ĳ�ѯ���ʱ����,���ܵ��²�ѯ�������ȷ");
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.getSQLForWhereOfAskbillForReport", e);
		}
		return whereNew.toString();
	}
	/**
	 * ��ȡ��Ӧ�̵���������봮����: "('abc','bcd',...)"
	 */
	private String getAreaClassCodeSet(ConditionVO cond, String pk_corp)
			throws BusinessException {
		String strAreaClassCodeSet = "(";
		try {
			/* ���ù��÷�����ȡ�����ӷ������ */
			nc.bs.pu.pub.PubDMO pubdmo = new nc.bs.pu.pub.PubDMO();
			String[] saAreaClassCode = pubdmo.getVendorCodeByAreaClassCode(
					pk_corp, cond.getValue(), cond.getOperaCode());
			nc.bs.scm.pub.TempTableDMO dmoTmpTable = new nc.bs.scm.pub.TempTableDMO();			
			
			if (saAreaClassCode != null && saAreaClassCode.length > 0) {
				strAreaClassCodeSet += dmoTmpTable.insertTempTable(
						saAreaClassCode,
					nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_ASK_13,
					"areaclasscode") ;
			}
			/* �����÷����쳣��� */
			else {
				strAreaClassCodeSet += "'";
				strAreaClassCodeSet += cond.getValue();
				strAreaClassCodeSet += "'";
			}
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.getAreaClassCodeSet", e);
		}
		strAreaClassCodeSet += ") ";
		return strAreaClassCodeSet;
	}
	/**
	 * ��ȡ��Ӧ�̵���������봮����: "('abc','bcd',...)"
	 */
	private String getAreaClassCodeSetForReport(ConditionVO cond, String pk_corp, String[] pk_corps)
			throws BusinessException {
		String strAreaClassCodeSet = "(";
		try {
			/* ���ù��÷�����ȡ�����ӷ������ */
			nc.bs.pu.pub.PubDMO pubdmo = new nc.bs.pu.pub.PubDMO();
			String[] saAreaClassCode = pubdmo.getVendorCodeByAreaClassCodeForAskReport(
					pk_corp, cond.getValue(), cond.getOperaCode(),pk_corps);
			nc.bs.scm.pub.TempTableDMO dmoTmpTable = new nc.bs.scm.pub.TempTableDMO();	
			if (saAreaClassCode != null && saAreaClassCode.length > 0) {
				strAreaClassCodeSet += dmoTmpTable.insertTempTable(
						saAreaClassCode,
					nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_ASK_13,
					"areaclasscode") ;
			}
			/* �����÷����쳣��� */
			else {
				strAreaClassCodeSet += "'";
				strAreaClassCodeSet += cond.getValue();
				strAreaClassCodeSet += "'";
			}
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.getAreaClassCodeSetForReport", e);
		}
		strAreaClassCodeSet += ") ";
		return strAreaClassCodeSet;
	}
	/**
	 * ��ȡ���������봮����: "('abc','bcd',...)"
	 */
	private String getInvClassCodeSet(ConditionVO cond, String pk_corp)
			throws BusinessException {
		String strInvClassCodeSet = "(";
		try {
			/* ���ù��÷�����ȡ�����ӷ������ */
			nc.bs.ps.cost.CostanalyseDMO ddmo = new nc.bs.ps.cost.CostanalyseDMO();
			String saInvClassCode[] = ddmo.getSubInvClassCode(cond.getValue(),
					cond.getOperaCode());
			nc.bs.scm.pub.TempTableDMO dmoTmpTable = new nc.bs.scm.pub.TempTableDMO();	
			if (saInvClassCode != null && saInvClassCode.length > 0) {
						
				strInvClassCodeSet += dmoTmpTable.insertTempTable(
					saInvClassCode,
					nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_ASK_10,
					"cmangid") ;
			}
			/* �����÷����쳣��� */
			else {
				strInvClassCodeSet += "'";
				strInvClassCodeSet += cond.getValue();
				strInvClassCodeSet += "'";
			}
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.getInvClassCodeSet", e);
		}
		strInvClassCodeSet += ") ";
		return strInvClassCodeSet;
	}

	/**
	 * @���ܣ�����ĳһ����VO����ָ��ȫ���ֶε�������
	 * @���ߣ���־ƽ �������ڣ�(2001-9-6 20:33:04)
	 * @return java.lang.String
	 * @param field
	 *            java.lang.String : �ֶε�ȫ�� �� "����.�ֶ���"
	 * @param vo
	 *            nc.vo.pub.query.ConditionVO
	 */
	public String getWhereByFieldCondVOMy(String field, ConditionVO vo,
			UFBoolean ufbNum) throws BusinessException {
		boolean isNum = ufbNum == null ? false : ufbNum.booleanValue();
		StringBuffer where = new StringBuffer(" ");
		where.append((vo.getLogic()) ? "and " : "or ");
		where.append((vo.getNoLeft()) ? "" : "( ");
		where.append(field + " ");
		if ("like".equalsIgnoreCase(vo.getOperaCode().trim())) {
			where.append(" ");
			if (isNum) {
				// ��ֵ�Ͱ�"like"���ͳ�"<="
				where.append("<= ");
				where.append(vo.getValue());
			} else {
				where.append("like ");
				where.append("'%");
				where.append(vo.getValue());
				where.append("%' ");
			}
		} else {
			where.append(vo.getOperaCode());
			if (!isNum) {
				where.append("'");
			}
			where.append(vo.getValue());
			if (!isNum) {
				where.append("'");
			}
		}
		where.append((vo.getNoRight()) ? " " : " ) ");
		return where.toString();
	}
	/**
	 * @���ܣ�����ĳһ����VO����ָ��ȫ���ֶε�������
	 * @���ߣ���־ƽ �������ڣ�(2001-9-6 20:33:04)
	 * @return java.lang.String
	 * @param field
	 *            java.lang.String : �ֶε�ȫ�� �� "����.�ֶ���"
	 * @param vo
	 *            nc.vo.pub.query.ConditionVO
	 */
	public String getWhereByFieldCondVOMyForFirstCondition(String field, ConditionVO vo,
			UFBoolean ufbNum) throws BusinessException {
		boolean isNum = ufbNum == null ? false : ufbNum.booleanValue();
		StringBuffer where = new StringBuffer(" ");
//		where.append((vo.getLogic()) ? "and " : "or ");
		where.append((vo.getNoLeft()) ? "" : "( ");
		where.append(field + " ");
		if ("like".equalsIgnoreCase(vo.getOperaCode().trim())) {
			where.append(" ");
			if (isNum) {
				// ��ֵ�Ͱ�"like"���ͳ�"<="
				where.append("<= ");
				where.append(vo.getValue());
			} else {
				where.append("like ");
				where.append("'%");
				where.append(vo.getValue());
				where.append("%' ");
			}
		} else {
			where.append(vo.getOperaCode());
			if (!isNum) {
				where.append("'");
			}
			where.append(vo.getValue());
			if (!isNum) {
				where.append("'");
			}
		}
		where.append((vo.getNoRight()) ? " " : " ) ");
		return where.toString();
	}
	
	public Vector queryAllInquireMy(ConditionVO[] conds, String pk_corp,
			UFBoolean[] status,String userid) throws BusinessException {
		AskbillHeaderVO[] headers = null;
		AskbillItemVO[] itemVOs = null;
		AskbillItemBVO[] itemBVOs = null;
		AskbillItemVendorVO[] itemVendorVOs = null;
		Vector v = new Vector();
//		String SQLItems = " and (1=1) ";
		try {
			if(conds == null){
				return v;
			}
			AskbillDMO dmo = new AskbillDMO();
			//liuys add for �׸ڿ�ҵ ���ݵ�¼���빩Ӧ�̹�����ѯ
			String refWhereSql = "";
		    if(userid != null){
		    	Object o = dmo.queryUserAndCust(userid,pk_corp);
		    	ArrayList custcodes = (ArrayList)o;
		    	//ֻ�е���ѯ�������Ĺ�Ӧ��ʱ����������ȥ��ѯ����,Ϊ����ƴ��sql
				if(custcodes != null && custcodes.size() > 0 ){
			    	refWhereSql = " and bd_cumandoc.dr=0 and bd_cumandoc.pk_cumandoc in(";
					for(int i=0;i<custcodes.size();i++){
						Object[] obj = (Object[])custcodes.get(i);
						refWhereSql += "'"+obj[0];
						if(i == custcodes.size()-1)
							refWhereSql += "')";
						else
							refWhereSql += "',";
					}
				}
		    }
			String strSQL = getSQLForInquireHeaderVOsMy(conds, pk_corp, status);
			strSQL += refWhereSql;
			headers = dmo.findAllHeadersByCondSQLMy(strSQL);
			if (headers != null && headers.length > 0) {
				// ֻ��ѯ��һ�ŵ��ݵĵ�����
				itemVOs = dmo.findItemsForHeaderMyArray(
						new AskbillHeaderVO[] { headers[0] }, null);
				itemBVOs = dmo.findItemsBForHeaderMyArray(
						new AskbillHeaderVO[] { headers[0] }, null,refWhereSql);
				itemVendorVOs = dmo.findItemsVendorForHeaderMyArray(
						new AskbillHeaderVO[] { headers[0] }, null);
				// ������
				// askbills = (AskbillVO[]) getVosWithFreeDealed(askbills,
				// "cmangid", "vfree0");
				// ��֯���ݷ���
				v.add(headers);
				v.add(itemVOs);
				v.add(itemBVOs);
				v.add(itemVendorVOs);
			}
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryAllInquireMy", e);
		}

		return v;
	}

	/**
	 * ͨ���������VO����(ѯ���۵�)
	 * 
	 * �������ڣ�(2001-6-7)
	 * 
	 * @return nc.vo.pp.ask.AskbillVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public Vector findByPrimaryKeyForAskBillForDataPower(String key,ConditionVO[] conds)
			throws BusinessException {
		AskbillHeaderVO[] headers = null;
		AskbillItemVO[] itemVOs = null;
		AskbillItemBVO[] itemBVOs = null;
		AskbillItemVendorVO[] itemVendorVOs = null;
		Vector v = new Vector();
//		ArrayList listPowerVos = new ArrayList();
		try {
			AskbillDMO dmo = new AskbillDMO();
//			String strPowerWherePart = null;
			String condition = " from po_askbill where po_askbill.caskbillid = '"
				+ key + "'";
			//v5.1����Ȩ�޿���
			 //���û��������¶�������������Ȩ����������
		    ArrayList listRet = OrderPubDMO.dealCondVosForPower(conds);
		    conds = (ConditionVO[]) listRet.get(0);
		    String strDataPowerSql = (String) listRet.get(1);
			if (strDataPowerSql != null && strDataPowerSql.length() > 0) {
				condition += strDataPowerSql;
			}
			headers = dmo
					.findAllHeadersByCondSQLMy(condition);
			if (headers != null && headers.length > 0) {
				itemVOs = dmo.findItemsForHeaderMyArray(
						new AskbillHeaderVO[] { headers[0] }, null);
				itemBVOs = dmo.findItemsBForHeaderMyArray(
						new AskbillHeaderVO[] { headers[0] }, null,null);
				itemVendorVOs = dmo.findItemsVendorForHeaderMyArray(
						new AskbillHeaderVO[] { headers[0] }, null);
				// ��֯���ݷ���
				v.add(headers[0]);
				v.add(itemVOs);
				v.add(itemBVOs);
				v.add(itemVendorVOs);
			}

		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.findByPrimaryKeyForAskBillForDataPower", e);
		}
		return v;
	}
	/**
	 * ͨ���������VO����(ѯ���۵�)
	 * 
	 * �������ڣ�(2001-6-7)
	 * 
	 * @return nc.vo.pp.ask.AskbillVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public Vector findByPrimaryKeyForAskBill(String key)
			throws BusinessException {
		AskbillHeaderVO[] headers = null;
		AskbillItemVO[] itemVOs = null;
		AskbillItemBVO[] itemBVOs = null;
		AskbillItemVendorVO[] itemVendorVOs = null;
		Vector v = new Vector();
//		ArrayList listPowerVos = new ArrayList();
		try {
			AskbillDMO dmo = new AskbillDMO();
//			String strPowerWherePart = null;
			String condition = " from po_askbill where po_askbill.caskbillid = '"
				+ key + "'";
			headers = dmo
					.findAllHeadersByCondSQLMy(condition);
			if (headers != null && headers.length > 0) {
				itemVOs = dmo.findItemsForHeaderMyArray(
						new AskbillHeaderVO[] { headers[0] }, null);
				itemBVOs = dmo.findItemsBForHeaderMyArray(
						new AskbillHeaderVO[] { headers[0] }, null,null);
				itemVendorVOs = dmo.findItemsVendorForHeaderMyArray(
						new AskbillHeaderVO[] { headers[0] }, null);
				// ��֯���ݷ���
				v.add(headers[0]);
				v.add(itemVOs);
				v.add(itemBVOs);
				v.add(itemVendorVOs);
			}

		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.findByPrimaryKeyForAskBill", e);
		}
		return v;
	}
	/**
	 * ͨ���������VO����(�۸�������)
	 * 
	 * �������ڣ�(2001-6-7)
	 * 
	 * @return nc.vo.pp.ask.AskbillVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public Vector findByPrimaryKeyForPriceAuditBill(String key)
			throws BusinessException {
		PriceauditHeaderVO[] headers = null;
		PriceauditBVO[] itemVOs = null;
		PriceauditBb1VO[] itemBVOs = null;
//		String SQLItems = " and (1=1) ";
		Vector v = new Vector();
		try {
			AskbillDMO dmo = new AskbillDMO();
			headers = dmo
					.findAllHeadersByCondSQLForPriceAudit(" FROM po_priceaudit left outer JOIN po_priceaudit_bb1 ON po_priceaudit.cpriceauditid = po_priceaudit_bb1.cpriceauditid where   po_priceaudit.dr = 0  and po_priceaudit.cpriceauditid = '"
							+ key + "'");
			if (headers != null && headers.length > 0) {
				itemVOs = dmo.findItemsForHeaderPriceAudit(
						new PriceauditHeaderVO[] { headers[0] }, null);
				itemBVOs = dmo.findItemsBForHeaderPriceAudit(
						new PriceauditHeaderVO[] { headers[0] }, null);
				// ��֯���ݷ���
				v.add(headers[0]);
				v.add(itemVOs);
				v.add(itemBVOs);
			}

		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.findByPrimaryKeyForPriceAuditBill", e);
		}
		return v;
	}
	/**
	 * ͨ���������VO����(�۸�������)
	 * 
	 * �������ڣ�(2001-6-7)
	 * 
	 * @return nc.vo.pp.ask.AskbillVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public Vector findByPrimaryKeyForPriceAuditBill(String key,ConditionVO[] conds)
			throws BusinessException {
		PriceauditHeaderVO[] headers = null;
		PriceauditBVO[] itemVOs = null;
		PriceauditBb1VO[] itemBVOs = null;
//		ArrayList listPowerVos = new ArrayList();
//		String SQLItems = " and (1=1) ";
		Vector v = new Vector();
//		String strPowerWherePart = null;
		try {
			AskbillDMO dmo = new AskbillDMO();
			String condition = " FROM po_priceaudit left outer JOIN po_priceaudit_bb1 ON po_priceaudit.cpriceauditid = po_priceaudit_bb1.cpriceauditid where   po_priceaudit.dr = 0  and po_priceaudit.cpriceauditid = '"
				+ key + "'";
			
            //v5.1����Ȩ�޿���
			 //���û��������¶�������������Ȩ����������
		    ArrayList listRet = OrderPubDMO.dealCondVosForPower(conds);
		    conds = (ConditionVO[]) listRet.get(0);
		    String strDataPowerSql = (String) listRet.get(1);
			if (strDataPowerSql != null && strDataPowerSql.length() > 0) {
				condition += strDataPowerSql;
			}
			headers = dmo
			.findAllHeadersByCondSQLForPriceAudit(condition);
			if (headers != null && headers.length > 0) {
				itemVOs = dmo.findItemsForHeaderPriceAudit(
						new PriceauditHeaderVO[] { headers[0] }, null);
				itemBVOs = dmo.findItemsBForHeaderPriceAudit(
						new PriceauditHeaderVO[] { headers[0] }, null);
				// ��֯���ݷ���
				v.add(headers[0]);
				v.add(itemVOs);
				v.add(itemBVOs);
			}

		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.findByPrimaryKeyForPriceAuditBill", e);
		}
		return v;
	}

	/**
	 * ��������:��ѯѯ�۵����� �������: ArrayList(0) ѯ�۵�ͷ����[] ArrayList(1) ѯ�۵�ͷʱ���[]
	 * ����ֵ:ArrayList(0),ѯ�۵���[](�������Ѵ���)
	 */
	public Vector queryAllBodysForAskBill(ArrayList aryPara)
			throws BusinessException {
		if (aryPara == null || aryPara.size() < 2) {
			SCMEnv.out("�����������ȷ: ����Ϊ�ջ�����ĸ�������");
			return null;
		}
		String[] saHId = (String[]) aryPara.get(0);
		String[] saHTs = (String[]) aryPara.get(1);
		String[] strSQLItems = (String[]) aryPara.get(2);
		String[] strSQLItems2 = (String[]) aryPara.get(3);
		if (saHId == null || saHId.length < 1 || saHTs == null
				|| saHTs.length < 1 || saHId.length != saHTs.length) {
			SCMEnv.out("�����������ȷ��ѯ�۵�ͷID��ѯ�۵�ͷʱ��ز�ƥ��");
			return null;
		}
		AskbillDMO dmo = null;
		AskbillHeaderVO[] headers = null;
		AskbillItemVO[] itemVOs = null;
		AskbillItemBVO[] itemBVOs = null;
		AskbillItemVendorVO[] itemVendorVOs = null;
//		String SQLItems = " and (1=1) ";
		Vector v = null;
		Vector result = new Vector();
		try {
			dmo = new AskbillDMO();
			for (int i = 0; i < saHId.length; i++) {
				v = new Vector();
				headers = dmo
						.findAllHeadersByCondSQLMy(" from po_askbill where  po_askbill.caskbillid = '"
								+ saHId[i] + "'");
				if (headers != null && headers.length > 0) {
					itemVOs = dmo.findItemsForHeaderMyArray(
							new AskbillHeaderVO[] { headers[0] }, strSQLItems[0]);
					itemBVOs = dmo.findItemsBForHeaderMyArray(
							new AskbillHeaderVO[] { headers[0] }, strSQLItems2[0],null);
					itemVendorVOs = dmo.findItemsVendorForHeaderMyArray(
							new AskbillHeaderVO[] { headers[0] }, null);
					// ��֯���ݷ���
					v.add(headers[0]);
					v.add(itemVOs);
					v.add(itemBVOs);
					v.add(itemVendorVOs);
					result.add(v);
				}
			}
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryAllBodysForAskBill", e);
		}
		return result;
	}

	/**
	 * ��������:��ѯѯ�۵����� �������: ArrayList(0) ѯ�۵�ͷ����[] ArrayList(1) ѯ�۵�ͷʱ���[]
	 * ����ֵ:ArrayList(0),ѯ�۵���[](�������Ѵ���)
	 */
	public Vector queryAllBodysForPriceAudit(ArrayList aryPara)
			throws BusinessException {
		if (aryPara == null || aryPara.size() < 2) {
			SCMEnv.out("�����������ȷ: ����Ϊ�ջ�����ĸ�������");
			return null;
		}
		String[] saHId = (String[]) aryPara.get(0);
		String[] saHTs = (String[]) aryPara.get(1);
		if (saHId == null || saHId.length < 1 || saHTs == null
				|| saHTs.length < 1 || saHId.length != saHTs.length) {
			SCMEnv.out("�����������ȷ��ѯ�۵�ͷID��ѯ�۵�ͷʱ��ز�ƥ��");
			return null;
		}
		AskbillDMO dmo = null;
		PriceauditHeaderVO[] headers = null;
		PriceauditBVO[] itemVOs = null;
		PriceauditBb1VO[] itemBVOs = null;
//		String SQLItems = " and (1=1) ";

		Vector v = new Vector();
		try {
			dmo = new AskbillDMO();
			for (int i = 0; i < saHId.length; i++) {
				headers = dmo
						.findAllHeadersByCondSQLForPriceAudit(" po_priceaudit.cpriceauditid = '"
								+ saHId[i] + "'");
				if (headers != null && headers.length > 0) {
					itemVOs = dmo.findItemsForHeaderPriceAudit(
							new PriceauditHeaderVO[] { headers[0] }, null);
					itemBVOs = dmo.findItemsBForHeaderPriceAudit(
							new PriceauditHeaderVO[] { headers[0] }, null);
					// ��֯���ݷ���
					v.add(headers[0]);
					v.add(itemVOs);
					v.add(itemBVOs);
				}
			}
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryAllBodysForPriceAudit", e);
		}
		return v;
	}
	
	public Vector queryAllBodysForPriceAudit2(ArrayList aryPara)
		throws BusinessException {
		if (aryPara == null || aryPara.size() < 2) {
			SCMEnv.out("�����������ȷ: ����Ϊ�ջ�����ĸ�������");
			return null;
		}
		String[] saHId = (String[]) aryPara.get(0);
		String[] saHTs = (String[]) aryPara.get(1);
		ConditionVO[] conds = (ConditionVO[]) aryPara.get(2);
		String pk_corp = (String) aryPara.get(3);
		String strSQL = getSQLForPriceAuditHeaderVOsMy(conds, pk_corp,null,false);
		String sGenctWhere ="";
		if(strSQL.indexOf("ISNULL(ngenct, 0)") != -1){
			sGenctWhere = " and "+strSQL.substring(strSQL.indexOf("ISNULL(ngenct, 0)"),strSQL.indexOf("ISNULL(ngenct, 0)")+23);
		}
		if (saHId == null || saHId.length < 1 || saHTs == null
				|| saHTs.length < 1 || saHId.length != saHTs.length) {
			SCMEnv.out("�����������ȷ��ѯ�۵�ͷID��ѯ�۵�ͷʱ��ز�ƥ��");
			return null;
		}
		AskbillDMO dmo = null;
		PriceauditHeaderVO[] headers = null;
		PriceauditBVO[] itemVOs = null;
		PriceauditBb1VO[] itemBVOs = null;
//String SQLItems = " and (1=1) ";
		Vector result = new Vector();
		try {
			dmo = new AskbillDMO();
			for (int i = 0; i < saHId.length; i++) {
				Vector v = new Vector();
				headers = dmo
				.findAllHeadersByCondSQLForPriceAudit(" from po_priceaudit where  po_priceaudit.cpriceauditid = '"
						+ saHId[i] + "'");
				if (headers != null && headers.length > 0) {
					itemVOs = dmo.findItemsForHeaderPriceAudit(
							new PriceauditHeaderVO[] { headers[0] }, strSQL);
					itemBVOs = dmo.findItemsBForHeaderPriceAudit(
							new PriceauditHeaderVO[] { headers[0] }, " and po_priceaudit_bb1.border = 'Y' "+sGenctWhere+" "+strSQL);
					// ��֯���ݷ���
					if(itemBVOs != null){
						v.add(headers[0]);
						v.add(itemVOs);
						v.add(itemBVOs);
						result.add(v);
					}
				}
			}
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryAllBodysForPriceAudit", e);
		}
		return result;
	}

	/**
	 * ���ߣ���ά�� ���ܣ����漰��������ʱ��ǰ̨��Ҫˢ�������ˣ��������ڣ�ts������״̬ ������ ���أ� ���⣺ ���ڣ�(2004-5-13
	 * 13:21:13) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param key
	 *            java.lang.String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public ArrayList queryForAudit(String key) throws BusinessException {
		ArrayList arr = new ArrayList();
		try {
			AskbillDMO dmo = new AskbillDMO();
			arr = dmo.queryForAudit(key);
			Hashtable result = new Hashtable();
			// �ӱ�
			result = dmo.queryTsForPriceAudit(key, new Integer(IPosition.BODY).toString());
			if (result.size()  > 0) {
				arr.add(result);
			} else {
				arr.add(null);
			}
			// ���ӱ�
			result = dmo.queryTsForPriceAudit(key, new Integer(IPosition.BBODY).toString());
			if (result.size() > 0) {
				arr.add(result);
			} else {
				arr.add(null);
			}

		} catch (Exception e) {
			SCMEnv.out(e);
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryForAudit", e);
		}
		return arr;
	}

	/**
	 * @��дѯ���۵���ɱ�־
	 */
	public void ReWriteAskBillStatus(Vector v, int operateType)
			throws BusinessException {
		AskbillDMO dmo = null;
		try {
			dmo = new AskbillDMO();
			dmo.ReWriteAskBillStatus(v, operateType);
		} catch (SystemException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.ReWriteAskBillStatus", e);
		} catch (NamingException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.ReWriteAskBillStatus", e);
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.ReWriteAskBillStatus", e);
		}
	}

	/**
	 * @�۸��������Ƿ�����Ϊ�����۸��������ļ۸���Դ
	 */
	public boolean checkIsPriceSorce(String cpriceauditid)
			throws BusinessException {
		AskbillDMO dmo = null;
		boolean isPriceSorce = false;
		try {
			dmo = new AskbillDMO();
			isPriceSorce = dmo.checkIsPriceSorce(cpriceauditid);
		} catch (SystemException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.checkIsPriceSorce", e);
		} catch (NamingException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.checkIsPriceSorce", e);
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.checkIsPriceSorce", e);
		}
		return isPriceSorce;
	}

	/**
	 * @���ݴ��������ȷ���Ƿ������Ч�۸�
	 */
	public Hashtable checkIsEffectPrice(EffectPriceParaVO effectPricePara)
			throws BusinessException {
		if (effectPricePara == null) {
			return null;
		}
		AskbillDMO dmo = null;
		String[] pk_corps = effectPricePara.getPkcoprs();
		String pk_corp = effectPricePara.getPk_corp();
		String data = effectPricePara.getDate().toString();
		String[] cmangids = effectPricePara.getCmangid();
		Hashtable result = new Hashtable();
		Vector cmangidsV = new Vector();
		Vector corpsV = new Vector();

		for (int i = 0; i < cmangids.length; i++) {
			if (!cmangidsV.contains(cmangids[i])) {
				cmangidsV.add(cmangids[i]);
			}
		}
		if (cmangidsV.size() > 0) {
			cmangids = new String[cmangidsV.size()];
			cmangidsV.copyInto(cmangids);
		}

		for (int i = 0; i < pk_corps.length; i++) {
			if (!corpsV.contains(pk_corps[i])) {
				corpsV.add(pk_corps[i]);
			}
		}
		if (corpsV.size() > 0) {
			pk_corps = new String[corpsV.size()];
			corpsV.copyInto(pk_corps);
		}
		// cmangids-->cbaseids
		String[] cbaseids = null;
		PubImpl pubImpl = new PubImpl();
		Object[][] retOb = pubImpl.queryArrayValue("bd_invmandoc",
				"pk_invmandoc", new String[] { "pk_invbasdoc" }, cmangids);
		Hashtable paraForPrice = new Hashtable();
		Vector tempV = new Vector();
		if (retOb != null) {
			String cbaseid = null;
			for (int i = 0; i < retOb.length; i++) {
				if (retOb[i] != null && retOb[i][0] != null) {
					cbaseid = (String) retOb[i][0];
					paraForPrice.put(cbaseid, cmangids[i]);
					tempV.add(cbaseid);
				}
			}
		}

		if (tempV.size() > 0) {
			cbaseids = new String[tempV.size()];
			tempV.copyInto(cbaseids);
		}
		
		
        
		// ȡ��ͬ���¼�
		EffectPriceParaVO temp = null;
		EffectPriceParaVO[] temps = null;
		Vector tempVV = new Vector();
		// cbaseids = new
		// String[]{"0001AA10000000002FYA","0001V5100000000005N0","0001ST1000000000AFWL"};
		if (cbaseids != null && cbaseids.length > 0) {
			for (int i = 0; i < cbaseids.length; i++) {
				temp = new EffectPriceParaVO();
				temp.setCbaseidOnly(cbaseids[i]);
				temp.setPk_corp(pk_corp);
				temp.setDate(new UFDate(data));
				tempVV.add(temp);
			}
		}
		if (tempVV.size() > 0) {
			temps = new EffectPriceParaVO[tempVV.size()];
			tempVV.copyInto(temps);
		}
		// String condition = " where po_priceaudit_bb1. border = 'Y' and
		// po_priceaudit_bb1.nquotetaxprice is not null and
		// po_priceaudit_bb1.nquotetaxprice is not null and
		// po_priceaudit_b.cmangid in("+conditionFroInv.toString()+") and
		// po_priceaudit_bb1.pk_corp in("+ conditionFroCorp.toString() +") and
		// po_priceaudit_bb1.dinvaliddate >='"+data+"'";

		try {
			dmo = new AskbillDMO();
			result = dmo.checkIsEffectPrice(cbaseids, pk_corps, data);
			Hashtable resultForConPrice = new Hashtable();
			boolean isStartCT = false;
			//�жϺ�ͬ�Ƿ�����
			 ICreateCorpQueryService ccSrv = (ICreateCorpQueryService) NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
	         isStartCT = ccSrv.isEnabled(pk_corp, ProductCode.PROD_CT);
			if(isStartCT){
			nc.itf.ct.service.ICtToPo_QueryCt tranfer = (nc.itf.ct.service.ICtToPo_QueryCt) nc.bs.framework.common.NCLocator
					.getInstance().lookup(
							nc.itf.ct.service.ICtToPo_QueryCt.class.getName());
			// �����صĺ�ͬ�۵���׼��ʽ
			resultForConPrice = tranfer.isEffectContract(temps);
			}
			if (resultForConPrice != null && resultForConPrice.size() > 0) {
				for (int i = 0; i < tempV.size(); i++) {
					if (tempV.get(i) != null
							&& tempV.get(i).toString().trim().length() > 0
							&& resultForConPrice.containsKey(tempV.get(i))) {
						if (result != null && result.size() > 0
								&& result.containsKey(tempV.get(i))) {
							if (paraForPrice.get(tempV.get(i)) != null
									&& paraForPrice.get(tempV.get(i))
											.toString().trim().length() > 0
									&& result.get(paraForPrice
											.get(tempV.get(i))) != null
									&& "N".equals(result.get(
											paraForPrice.get(tempV.get(i)))
											.toString().trim())
									&& resultForConPrice.get(tempV.get(i)) != null
									&& "true".equals(resultForConPrice.get(tempV.get(i))
											.toString().trim())) {
								result.put(paraForPrice.get(tempV.get(i)),
										new UFBoolean(true));
							}
						} else if (result != null && result.size() > 0
								&& !result.containsKey(tempV.get(i))) {
							if (paraForPrice.get(tempV.get(i)) != null
									&& paraForPrice.get(tempV.get(i))
											.toString().trim().length() > 0
									&& resultForConPrice.get(tempV.get(i)) != null
									&& resultForConPrice.get(tempV.get(i))
											.toString().trim().length() > 0
									&& "Y".equals(resultForConPrice.get(tempV.get(i))
											.toString().trim())) {
								result.put(paraForPrice.get(tempV.get(i)),
										new UFBoolean(true));
							}
						} else if (result != null && result.size() == 0) {
							if (paraForPrice.get(tempV.get(i)) != null
									&& paraForPrice.get(tempV.get(i))
											.toString().trim().length() > 0
									&& resultForConPrice.get(tempV.get(i)) != null
									&& resultForConPrice.get(tempV.get(i))
											.toString().trim().length() > 0
									&& "Y".equals(resultForConPrice.get(tempV.get(i))
											.toString().trim())) {
								result.put(paraForPrice.get(tempV.get(i)),
										new UFBoolean(true));
							}
						}
					}
				}
			}
		} catch (SystemException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.checkIsEffectPrice", e);
		} catch (NamingException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.checkIsEffectPrice", e);
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.checkIsEffectPrice", e);
		} catch (Exception e) {
			// TODO �Զ����� catch ��

			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.checkIsEffectPrice", e);
		}
		return result;
	}

	/**
	 * @��д���ɶ�������
	 */
	public void reWriteGenOrderNums(String[] addForOrder, String[] delForOrder)
			throws BusinessException {
		if ((addForOrder == null || (addForOrder != null && addForOrder.length == 0))
				&& (delForOrder == null || (delForOrder != null && delForOrder.length == 0))) {
			return;
		}
		AskbillDMO dmo = null;
		StringBuffer conditionForAdd = new StringBuffer();
		if (addForOrder != null && addForOrder.length > 0) {
			for (int i = 0; i < addForOrder.length; i++) {
				if (i < addForOrder.length - 1) {
					conditionForAdd.append("'" + addForOrder[i] + "'" + ",");
				} else {
					conditionForAdd.append("'" + addForOrder[i] + "'");
				}
			}
		}
		StringBuffer conditionForDel = new StringBuffer();
		if (delForOrder != null && delForOrder.length > 0) {

			for (int i = 0; i < delForOrder.length; i++) {
				if (i < delForOrder.length - 1) {
					conditionForDel.append("'" + delForOrder[i] + "'" + ",");
				} else {
					conditionForDel.append("'" + delForOrder[i] + "'");
				}
			}
		}
		try {
			dmo = new AskbillDMO();
			if (addForOrder != null && addForOrder.length > 0) {
				dmo.reWriteGenOrderNums(conditionForAdd.toString(), 0);
			}
			if (delForOrder != null && delForOrder.length > 0) {
				dmo.reWriteGenOrderNums(conditionForDel.toString(), 1);
			}
		} catch (SystemException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.reWriteGenOrderNums", e);
		} catch (NamingException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.reWriteGenOrderNums", e);
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.reWriteGenOrderNums", e);
		}

	}

	/**
	 * @ѯ���۵��Ƿ���Ϊ�۸��������ļ۸���Դ
	 */
	public boolean checkIsCanPriceSorce(Vector caskbillids)
			throws BusinessException {
		boolean isCanPriceSorce = false;
		String condition = null;
		String[] caskBillIds = null;
		AskbillDMO dmo = null;
		caskBillIds = new String [caskbillids.size()];
		for (int i = 0; i < caskbillids.size(); i++) {
			caskBillIds[i] = (String)caskbillids.get(i);
		}
		try {
			nc.bs.scm.pub.TempTableDMO dmoTmpTable = new nc.bs.scm.pub.TempTableDMO();			
			condition = dmoTmpTable.insertTempTable(
				caskBillIds,
				nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_ASK_11,
				"caskbillid") ;
			dmo = new AskbillDMO();
			isCanPriceSorce = dmo.checkIsCanEffectPrice(condition);
		} catch (SystemException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.checkIsCanPriceSorce", e);
		} catch (NamingException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.checkIsCanPriceSorce", e);
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.checkIsCanPriceSorce", e);
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.checkIsCanPriceSorce", e);
		}
		return isCanPriceSorce;
	}

	/**
	 * @���ܣ�Ϊ��Ӧ�̴���۸���ѯ����
	 * @˵���� 1.������ֶ�
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @param status
	 *            boolean[]
	 */
	public VendorInvPriceVO[] queryForVendorInvPrice(ConditionVO[] conds,
			String pk_corp) throws BusinessException {

		VendorInvPriceVO[] askbillitemsFromAsk = null;
		VendorInvPriceVO[] askbillitemsFromPriceAudit = null;
		VendorInvPriceVO[] askbillitemsResult = null;
		Vector v = new Vector();
		String strSQL = "";
		// ���۵������� 0--ȫ����1--ѯ���۵���2--�۸�������
		int quoteBillType = 0;
		try {
			AskbillDMO dmo = new AskbillDMO();
			for (int i = 0; i < conds.length - 1; i++) {
				// ���۵�������
				if ("quotetype".equals(conds[i].getFieldCode())) {
					if (conds[i].getValue() != null) {
						if (conds[i]
								.getValue()
								.equals(
										nc.bs.ml.NCLangResOnserver
												.getInstance().getStrByID(
														"4004070205",
														"UPP4004070205-000012")/*
																				 * @res
																				 * "ѯ���۵�"
																				 */)) {
							quoteBillType = 1;
							strSQL = getSQLForVendorInvPriceFromAsk(conds,
									pk_corp);
						} else if (conds[i]
								.getValue()
								.equals(
										nc.bs.ml.NCLangResOnserver
												.getInstance().getStrByID(
														"4004070205",
														"UPP4004070205-000013")/*
																				 * @res
																				 * "�۸�������"
																				 */)) {
							quoteBillType = 2;
							strSQL = getSQLForVendorInvPriceFromPriceAudit(
									conds, pk_corp);
						}
					}
				}
			}

			// ����Ȩ�޿���
			// if(conds[conds.length - 1].getFieldName().equals("����Ա")){
			// sOperator = conds[conds.length - 1].getValue();
			// ss =
			// nc.bs.scm.datapower.ScmDps.getSubSql("po_askbill","po_askbill",sOperator,new
			// String[]{pk_corp});
			// if(ss != null && ss.trim().length() > 0) strSQL += " and " + ss +
			// " ";
			// ss =
			// nc.bs.scm.datapower.ScmDps.getSubSql("po_askbill_b","po_askbill_b",sOperator,new
			// String[]{pk_corp});
			// if(ss != null && ss.trim().length() > 0) strSQL += " and " + ss +
			// " ";
			// }
			if (quoteBillType == 0) {
				strSQL = getSQLForVendorInvPriceFromAsk(conds, pk_corp);
				askbillitemsFromAsk = dmo.findAskItemsForVendorInvPrice(strSQL);
				strSQL = getSQLForVendorInvPriceFromPriceAudit(conds, pk_corp);
				askbillitemsFromPriceAudit = dmo
						.findPriceAuditItemsForVendorInvPrice(strSQL);
			} else if (quoteBillType == 1) {
				strSQL = getSQLForVendorInvPriceFromAsk(conds, pk_corp);
				askbillitemsFromAsk = dmo.findAskItemsForVendorInvPrice(strSQL);
			} else if (quoteBillType == 2) {
				strSQL = getSQLForVendorInvPriceFromPriceAudit(conds, pk_corp);
				askbillitemsFromPriceAudit = dmo
						.findPriceAuditItemsForVendorInvPrice(strSQL);
			}
			if ((askbillitemsFromAsk == null || (askbillitemsFromAsk != null && askbillitemsFromAsk.length <= 0))
					&& (askbillitemsFromPriceAudit == null || (askbillitemsFromPriceAudit != null && askbillitemsFromPriceAudit.length <= 0)))
				return null;
			if (askbillitemsFromAsk != null && askbillitemsFromAsk.length > 0) {
				for (int i = 0; i < askbillitemsFromAsk.length; i++) {
					v.add(askbillitemsFromAsk[i]);
				}
			}
			if (askbillitemsFromPriceAudit != null
					&& askbillitemsFromPriceAudit.length > 0) {
				for (int i = 0; i < askbillitemsFromPriceAudit.length; i++) {
					v.add(askbillitemsFromPriceAudit[i]);
				}
			}
			if (v.size() > 0) {
				askbillitemsResult = new VendorInvPriceVO[v.size()];
				v.copyInto(askbillitemsResult);
			}

		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryForVendorInvPrice", e);
		}

		return askbillitemsResult;
	}

	/**
	 * ��ȡ ѯ����ϸ��ѯ�ۻ��� ��ѯ���� ״̬Լ���������� ˵���� 1.�����������ݿ��й�ʽ���ں�̨һ���Թ������ 2.�����������ñ����� #ѯ����
	 * sm_user1.user_name #������ sm_user2.user_name 3.�����Ļ������ݱ� bd_cubasdoc,
	 * bd_cumandoc, bd_invbasdoc, bd_invmandoc, bd_taxitems, bd_payterm,
	 * bd_deptdoc, bd_psndoc, bd_measdoc, bd_currtype, sm_user as sm_user1,
	 * sm_user as sm_user2 �������ڣ�(2001-09-10-18)
	 * 
	 * @return String strSQL ���ظ�ʽΪ��" from ... where ... "
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @param status
	 *            boolean[]
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	private String getSQLForVendorInvPriceFromAsk(ConditionVO[] conds,
			String pk_corp) throws BusinessException {

		String strSQL = " ";
		ArrayList listPowerVos = new ArrayList();
		StringBuffer from = new StringBuffer(" from ");
		StringBuffer priceAuditCon = new StringBuffer(" ");
		StringBuffer priceAuditConForVendor = new StringBuffer(" ");
		StringBuffer priceAuditConForQuoteBillCode = new StringBuffer(" ");
		from.append("po_askbill ");

		// ��"("Ŀ���ǰѹ�˾�����ŵ������
		// ��ͬ�� where (...) and pk_corp = ... and (״̬Լ��,ҵ��Լ����...)
		// �Ӷ���֤����ֻ��pk_corp �����˾��
		StringBuffer where = new StringBuffer(" where ( ");
		where.append(" po_askbill_b.dr = 0 ");
		where.append(" and po_askbill_bb1.dr = 0 ");
		where.append(" and po_askbill.dr = 0 ");
		// ����״̬Ϊ����
		where.append(" and (po_askbill.ibillstatus = 3 or po_askbill.ibillstatus = 4) ");
		Vector vTableName = new Vector();
		from
				.append(" inner join po_askbill_b  ON po_askbill_b.caskbillid = po_askbill.caskbillid and po_askbill.dr = 0 and po_askbill_b.dr = 0 and po_askbill.pk_corp ='"
						+ pk_corp + "' ");
		vTableName.addElement("po_askbill");
		from
				.append(" LEFT OUTER JOIN po_askbill_bb1  ON po_askbill_bb1.caskbillid = po_askbill_b.caskbillid and po_askbill_bb1.caskbill_bid = po_askbill_b.caskbill_bid and po_askbill_bb1.dr = 0 ");
		vTableName.addElement("po_askbill_bb1");
		 from.append("LEFT OUTER JOIN bd_cubasdoc ON po_askbill_bb1.cvendorbaseid = bd_cubasdoc.pk_cubasdoc ");
		 vTableName.addElement("bd_cubasdoc");
		// from.append(
		// "LEFT OUTER JOIN bd_cumandoc ON po_askbill_bb1.cvendormangid =
		// bd_cumandoc.pk_cumandoc ");
		// vTableName.addElement("bd_cumandoc");
		// from.append(
		// "LEFT OUTER JOIN bd_currtype ON po_askbill.ccurrencytypeid =
		// bd_currtype.pk_currtype ");
		// vTableName.addElement("bd_currtype");
		 from.append("LEFT OUTER JOIN bd_invbasdoc ON po_askbill_b.cbaseid = bd_invbasdoc.pk_invbasdoc ");
		 vTableName.addElement("bd_invbasdoc");
		 from.append("LEFT OUTER JOIN bd_invcl ON bd_invbasdoc.pk_invcl= bd_invcl.pk_invcl");
		 vTableName.addElement("bd_invcl");
		// from.append(
		// "LEFT OUTER JOIN bd_invmandoc ON po_askbill_b.cmangid =
		// bd_invmandoc.pk_invmandoc ");
		// vTableName.addElement("bd_invmandoc");
		// from.append(
		// "LEFT OUTER JOIN bd_measdoc ON bd_invbasdoc.pk_measdoc=
		// bd_measdoc.pk_measdoc ");
		// vTableName.addElement("bd_measdoc");

		if (conds != null) {
			nc.bs.ps.cost.CostanalyseDMO ddmo =  null;
//			ArrayList ary = null;
			for (int i = 0; i < conds.length - 1; i++) {
				String sOpera = conds[i].getOperaCode().trim();
				String sValue = conds[i].getValue();
				if ("like".equalsIgnoreCase(sOpera)
						&& !"bd_invcl.invclasscode".equals(
								conds[i].getFieldCode()))
					sValue = "%" + sValue + "%";
				// �ٴ����������
                //����Ȩ������VO
				 if("IS".equalsIgnoreCase(conds[i].getOperaCode().trim()) && "NULL".equalsIgnoreCase(conds[i].getValue().trim())
						|| conds[i].getValue().trim().indexOf(VariableConst.PREFIX_OF_DATAPOWER_SQL) >= 0){
					listPowerVos.add(conds[i]);
				}
				// ������Ч����
				else if ("dvaliddate".equals(conds[i].getFieldCode())) {
					where.append(" and po_askbill_bb1.dvaliddate  "
							+ conds[i].getOperaCode() + "'");
					where.append(conds[i].getValue());
					where.append("' ");
				}
				// ����ʧЧ����
				else if ("dinvaliddate".equals(conds[i].getFieldCode())) {
					where.append(" and po_askbill_bb1.dinvaliddate "
							+ conds[i].getOperaCode() + "'");
					where.append(conds[i].getValue());
					where.append("' ");
				}
				// ��Ӧ������
				else if ("bd_cubasdoc.custname".equals(conds[i].getFieldCode()) && sValue != null && sValue.trim().length() > 0 && !"null".equalsIgnoreCase(sValue.trim()) && !sValue.trim().startsWith("(select")) {

					if (pk_corp != null) {
						where
								.append(" and po_askbill_bb1.cvendormangid in (select pk_cumandoc from bd_cumandoc,bd_cubasdoc ");
						where
								.append("where bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ");
						where.append("and bd_cumandoc.pk_corp = '");
						where.append(pk_corp + "' ");
						where.append("and bd_cubasdoc.custname ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
						
						priceAuditConForVendor
						.append(" and po_priceaudit_bb1.cvendormangid in (select pk_cumandoc from bd_cumandoc,bd_cubasdoc ");
						priceAuditConForVendor
						.append("where bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ");
						priceAuditConForVendor.append("and bd_cumandoc.pk_corp = '");
						priceAuditConForVendor.append(pk_corp + "' ");
						priceAuditConForVendor.append("and bd_cubasdoc.custname ");
						priceAuditConForVendor.append(sOpera);
						priceAuditConForVendor.append("'");
						priceAuditConForVendor.append(sValue);
						priceAuditConForVendor.append("') ");
					} else {
						where
								.append(" and po_askbill_bb1.cvendormangid in (select pk_cumandoc from bd_cumandoc,bd_cubasdoc ");
						where
								.append(" where bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ");
						where.append("and bd_cubasdoc.custname ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
						
						priceAuditConForVendor
						.append(" and po_priceaudit_bb1.cvendormangid in (select pk_cumandoc from bd_cumandoc,bd_cubasdoc ");
						priceAuditConForVendor
						.append(" where bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ");
						priceAuditConForVendor.append("and bd_cubasdoc.custname ");
						priceAuditConForVendor.append(sOpera);
						priceAuditConForVendor.append("'");
						priceAuditConForVendor.append(sValue);
						priceAuditConForVendor.append("') ");
					}
				}
				// ��Ӧ�̱���
				else if ("bd_cubasdoc.custcode".equals(conds[i].getFieldCode()) && sValue != null && sValue.trim().length() > 0 && !"null".equalsIgnoreCase(sValue.trim()) && !sValue.trim().startsWith("(select")) {

					if (pk_corp != null) {
						where
								.append(" and  po_askbill_bb1.cvendormangid in (select pk_cumandoc from bd_cumandoc,bd_cubasdoc ");
						where
								.append("where bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ");
						where.append("and bd_cumandoc.pk_corp = '");
						where.append(pk_corp + "' ");
						where.append("and bd_cubasdoc.custcode ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
						
						priceAuditConForVendor
						.append(" and  po_priceaudit_bb1.cvendormangid in (select pk_cumandoc from bd_cumandoc,bd_cubasdoc ");
						priceAuditConForVendor
						.append("where bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ");
						priceAuditConForVendor.append("and bd_cumandoc.pk_corp = '");
						priceAuditConForVendor.append(pk_corp + "' ");
						priceAuditConForVendor.append("and bd_cubasdoc.custcode ");
						priceAuditConForVendor.append(sOpera);
						priceAuditConForVendor.append("'");
						priceAuditConForVendor.append(sValue);
						priceAuditConForVendor.append("') ");
					} else {
						where
								.append(" and  po_askbill_bb1.cvendormangid in (select pk_cumandoc from bd_cumandoc,bd_cubasdoc ");
						where
								.append(" where bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ");
						where.append("and bd_cubasdoc.custcode ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
						
						priceAuditConForVendor
						.append(" and  po_priceaudit_bb1.cvendormangid in (select pk_cumandoc from bd_cumandoc,bd_cubasdoc ");
						priceAuditConForVendor
						.append(" where bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ");
						priceAuditConForVendor.append("and bd_cubasdoc.custcode ");
						priceAuditConForVendor.append(sOpera);
						priceAuditConForVendor.append("'");
						priceAuditConForVendor.append(sValue);
						priceAuditConForVendor.append("') ");
					}
				}
                //����
				else if ("currency".equals(conds[i].getFieldCode())) {

					
						where.append(" and po_askbill.ccurrencytypeid in (select pk_currtype from bd_currtype ");
						where.append(" where pk_currtype ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
				}
				/* ������� */
				else if ("bd_invbasdoc.invcode".equals(conds[i].getFieldCode()) && sValue != null && sValue.trim().length() > 0 && !"null".equalsIgnoreCase(sValue.trim()) && !sValue.trim().startsWith("(select")) {

					if (pk_corp != null) {
						where
								.append(" and po_askbill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ");
						where
								.append("where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ");
						where.append("and bd_invmandoc.pk_corp = '");
						where.append(pk_corp + "' ");
						where.append("and bd_invbasdoc.invcode ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
					} else {
						where
								.append(" and po_askbill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ");
						where
								.append("where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ");
						where.append("and bd_invbasdoc.invcode ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
					}
				}
				/* ������� */
				else if ("bd_invbasdoc.invname".equals(conds[i].getFieldCode()) && sValue != null && sValue.trim().length() > 0 && !"null".equalsIgnoreCase(sValue.trim()) && !sValue.trim().startsWith("(select")) {
					if (pk_corp != null) {
						where
								.append(" and po_askbill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ");
						where
								.append("where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ");
						where.append("and bd_invmandoc.pk_corp = '");
						where.append(pk_corp + "' ");
						where.append("and bd_invbasdoc.invname ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
					} else {
						where
								.append(" and po_askbill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ");
						where
								.append("where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ");
						where.append("and bd_invbasdoc.invname ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
					}
				}
				// �����
				/* ���������� */
				else if ("bd_invcl.invclasscode"
						.equals(conds[i].getFieldCode())
						&& conds[i].getValue() != null
						&& conds[i].getValue().trim().length() > 0 
						&& !"null".equalsIgnoreCase(sValue.trim()) 
						&& !sValue.trim().startsWith("(select")) {
					try {
						String unitCode = null;
						ddmo = new nc.bs.ps.cost.CostanalyseDMO();
						String sClassCode[] = ddmo.getSubInvClassCode(sValue,
								sOpera);
						sOpera = "=";
						if (sClassCode != null && sClassCode.length > 0) {
							sValue = "(";
							for (int j = 0; j < sClassCode.length; j++) {
								if (j < sClassCode.length - 1)
									sValue += "C.invclasscode " + sOpera + " '"
											+ sClassCode[j] + "' or ";
								else
									sValue += "C.invclasscode " + sOpera + " '"
											+ sClassCode[j] + "')";
							}
							if (unitCode != null
									&& unitCode.trim().length() > 0) {
								where
										.append(" and po_askbill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '"
												+ unitCode
												+ "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and "
												+ sValue + ")");
							} else {
								where
										.append(" and po_askbill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where "
												+ "A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and "
												+ sValue + ")");
							}
						} else {
							if (unitCode != null
									&& unitCode.trim().length() > 0) {
								where
										.append(" and po_askbill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '"
												+ unitCode
												+ "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode "
												+ sOpera + " '" + sValue + "')");
							} else {
								where
										.append(" and po_askbill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where "
												+ "A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode "
												+ sOpera + " '" + sValue + "')");
							}
						}
					} catch (Exception e) {
						SCMEnv.out(e);
						PubDMO.throwBusinessException(
								nc.bs.ml.NCLangResOnserver.getInstance()
										.getStrByID("40040101",
												"UPP40040101-000008")/*
																		 * @res
																		 * "��ȡ��������ӱ���ʱ����:��ѯ������ܲ���ȷ"
																		 */, e);
					}
				}
				// ������˰�۸�
				else if ("nordertaxprice".equals(conds[i].getFieldCode())) {
					priceAuditCon.append(" and po_priceaudit_bb1.nordertaxprice "
							+ conds[i].getOperaCode() + "'");
					priceAuditCon.append(conds[i].getValue());
					priceAuditCon.append("' ");
				}
				// ������˰�۸�
				else if ("norderprice".equals(conds[i].getFieldCode())) {
					priceAuditCon.append(" and po_priceaudit_bb1.norderprice "
							+ conds[i].getOperaCode() + "'");
					priceAuditCon.append(conds[i].getValue());
					priceAuditCon.append("' ");
					
				}
				// ���۵�������
				// else if(conds[i].getFieldCode().equals("quotetype")){
				// if (conds[i].getValue() != null) {
				// if (conds[i].getValue().equals("ѯ���۵�")) {
				// quoteBillType = 1;
				// }
				// else if(conds[i].getValue().equals("�۸�������")){
				// quoteBillType = 2;
				// }
				// }
				// }
				// ���۵��ݺ�
				else if ("vquotebillcode".equals(conds[i].getFieldCode())) {
					priceAuditConForQuoteBillCode.append(" and po_priceaudit_bb1.vquotebillcode "
							+ conds[i].getOperaCode() + "'");
					priceAuditConForQuoteBillCode.append(conds[i].getValue());
					priceAuditConForQuoteBillCode.append("' ");
					
					where.append(" and po_askbill.vaskbillcode "
							+ conds[i].getOperaCode() + "'");
					where.append(conds[i].getValue());
					where.append("' ");
				}
				// �۸���������
				else if ("vpriceauditcode".equals(conds[i].getFieldCode())) {
					priceAuditCon.append(" and po_priceaudit.vpriceauditcode "
							+ conds[i].getOperaCode() + "'");
					priceAuditCon.append(conds[i].getValue());
					priceAuditCon.append("' ");
				}
				// �Ƿ�ɶ���
				else if ("border".equals(conds[i].getFieldCode())) {
					if (conds[i].getValue() != null) {
						if ("��".equals(conds[i].getValue())) {
							priceAuditCon.append(" and po_priceaudit_bb1.border "
									+ conds[i].getOperaCode() + "'Y' ");
						} else if ("��".equals(conds[i].getValue())) {
							priceAuditCon.append(" and po_priceaudit_bb1.border "
									+ conds[i].getOperaCode() + "'N' ");
						}
					}
				}
				 //�Ƿ������ʷ�۸� since5.3 modify by donggq
				else if ("bhistoryprice".equals(conds[i].getFieldCode())){
				  if (conds[i].getValue() != null){
				    if("��".equals(conds[i].getValue())){
				      String date = conds[i].getFieldName();
				      where.append("and (po_askbill_bb1. dvaliddate <='"
                  + date
                  + "'or  po_askbill_bb1.dvaliddate is null ) and (po_askbill_bb1. dinvaliddate >='"
                  + date
                  + "'or  po_askbill_bb1.dinvaliddate is null )");
				    }
				  }
				}				
			}
		}
		where.append(" ) ");
		if(listPowerVos.size() > 0){
			ConditionVO[] voaCondPower = new ConditionVO[listPowerVos.size()];
			listPowerVos.toArray(voaCondPower);
//			for(int i = 0 ; i < voaCondPower.length ; i ++){
//				voaCondPower[i].setNoLeft(false);
//				voaCondPower[i].setNoRight(false);
//			}
			String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);
			//���Ǳ�׼���ֶ��滻��
//			strPowerWherePart = StringUtil.replace(strPowerWherePart, "po_order.cdeptid_incsub", "po_order.cdeptid");
//			strPowerWherePart = StringUtil.replace(strPowerWherePart, "po_praybill.cdeptid_incsub", "po_praybill.cdeptid");
			//
			where.append(" and (" + strPowerWherePart + ") ");
		}
        
		// ��˾Լ��
		if (pk_corp != null && pk_corp.trim().length() > 0) {
			where.append(" and po_askbill.pk_corp = '");
			where.append(pk_corp);
			where.append("' ");
		}
		if(priceAuditCon.toString().trim().length() == 0){
    		from
    		.append(" LEFT OUTER JOIN (select  po_priceaudit_bb1.cquotebill_bb1id,po_priceaudit_bb1.cquotebillid,po_priceaudit_bb1.vquotebillcode,po_priceaudit.vpriceauditcode,po_priceaudit_bb1.border,po_priceaudit_bb1.nordernum,po_priceaudit_bb1.norderprice,po_priceaudit_bb1.nordertaxprice,po_priceaudit_bb1.norgmny,po_priceaudit_bb1.norgtaxmny,po_priceaudit_bb1.norgsummny,po_priceaudit_bb1.cpriceauditid,po_priceaudit_bb1.iprior from po_priceaudit left outer join po_priceaudit_bb1 ON po_priceaudit_bb1.cpriceauditid = po_priceaudit.cpriceauditid and fpricetype = 1 and po_priceaudit.dr = 0 and po_priceaudit_bb1.dr = 0 and po_priceaudit.pk_corp = '"
    				+ pk_corp
    				+ "')  as po_priceaudit_bb1 ON po_askbill_bb1.caskbillid = po_priceaudit_bb1.cquotebillid and po_askbill_bb1.caskbill_bb1id = po_priceaudit_bb1.cquotebill_bb1id ");
		}else if(priceAuditCon.toString().trim().length() > 0){
  		  if(PuPubVO.getString_TrimZeroLenAsNull(priceAuditConForVendor)!= null && PuPubVO.getString_TrimZeroLenAsNull(priceAuditConForQuoteBillCode)!= null){
  		    from
  		    .append(" inner  JOIN (select  po_priceaudit_bb1.cquotebill_bb1id,po_priceaudit_bb1.cquotebillid,po_priceaudit_bb1.vquotebillcode,po_priceaudit.vpriceauditcode,po_priceaudit_bb1.border,po_priceaudit_bb1.nordernum,po_priceaudit_bb1.norderprice,po_priceaudit_bb1.nordertaxprice,po_priceaudit_bb1.norgmny,po_priceaudit_bb1.norgtaxmny,po_priceaudit_bb1.norgsummny,po_priceaudit_bb1.cpriceauditid ,po_priceaudit_bb1.iprior from po_priceaudit inner join po_priceaudit_bb1 ON po_priceaudit_bb1.cpriceauditid = po_priceaudit.cpriceauditid and fpricetype = 1 and po_priceaudit.dr = 0 and po_priceaudit_bb1.dr = 0 and po_priceaudit.pk_corp = '"
  		        + pk_corp
  		        + "'" + priceAuditCon.toString() + " " + priceAuditConForVendor.toString() + " '"+ priceAuditConForQuoteBillCode.toString() + ")  as po_priceaudit_bb1 ON po_askbill_bb1.caskbillid = po_priceaudit_bb1.cquotebillid and po_askbill_bb1.caskbill_bb1id = po_priceaudit_bb1.cquotebill_bb1id ");
  		  }else{
  		    from
          .append(" inner  JOIN (select  po_priceaudit_bb1.cquotebill_bb1id,po_priceaudit_bb1.cquotebillid,po_priceaudit_bb1.vquotebillcode,po_priceaudit.vpriceauditcode,po_priceaudit_bb1.border,po_priceaudit_bb1.nordernum,po_priceaudit_bb1.norderprice,po_priceaudit_bb1.nordertaxprice,po_priceaudit_bb1.norgmny,po_priceaudit_bb1.norgtaxmny,po_priceaudit_bb1.norgsummny,po_priceaudit_bb1.cpriceauditid ,po_priceaudit_bb1.iprior from po_priceaudit inner join po_priceaudit_bb1 ON po_priceaudit_bb1.cpriceauditid = po_priceaudit.cpriceauditid and fpricetype = 1 and po_priceaudit.dr = 0 and po_priceaudit_bb1.dr = 0 and po_priceaudit.pk_corp = '"
              + pk_corp
              + "'" + priceAuditCon.toString() + ")  as po_priceaudit_bb1 ON po_askbill_bb1.caskbillid = po_priceaudit_bb1.cquotebillid and po_askbill_bb1.caskbill_bb1id = po_priceaudit_bb1.cquotebill_bb1id ");
		  }
		}
		vTableName.addElement("po_priceaudit_bb1");
		strSQL += from;
		strSQL += where;

		SCMEnv.out("��ѯ����Ϊ�� " + strSQL);

		return strSQL;
	}

	/**
	 * ��ȡ ѯ����ϸ��ѯ�ۻ��� ��ѯ���� ״̬Լ���������� ˵���� 1.�����������ݿ��й�ʽ���ں�̨һ���Թ������ 2.�����������ñ����� #ѯ����
	 * sm_user1.user_name #������ sm_user2.user_name 3.�����Ļ������ݱ� bd_cubasdoc,
	 * bd_cumandoc, bd_invbasdoc, bd_invmandoc, bd_taxitems, bd_payterm,
	 * bd_deptdoc, bd_psndoc, bd_measdoc, bd_currtype, sm_user as sm_user1,
	 * sm_user as sm_user2 �������ڣ�(2001-09-10-18)
	 * 
	 * @return String strSQL ���ظ�ʽΪ��" from ... where ... "
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @param status
	 *            boolean[]
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	private String getSQLForVendorInvPriceFromPriceAudit(ConditionVO[] conds,
			String pk_corp) throws BusinessException {

		String strSQL = " ";
		ArrayList listPowerVos = new ArrayList();
		// ���۵������� 0--ȫ����1--ѯ���۵���2--�۸�������
		StringBuffer from = new StringBuffer(" from ");
		from.append("po_priceaudit ");

		// ��"("Ŀ���ǰѹ�˾�����ŵ������
		// ��ͬ�� where (...) and pk_corp = ... and (״̬Լ��,ҵ��Լ����...)
		// �Ӷ���֤����ֻ��pk_corp �����˾��
		StringBuffer where = new StringBuffer(" where (  ");
		where.append(" po_priceaudit_bb1.dr = 0 ");
		where.append(" and po_priceaudit_b.dr = 0 ");
		where.append(" and po_priceaudit.dr = 0 ");
		Vector vTableName = new Vector();
		from
				.append(" inner join  po_priceaudit_b  ON po_priceaudit_b.cpriceauditid = po_priceaudit.cpriceauditid and po_priceaudit.dr = 0  and  po_priceaudit_b.dr = 0 and po_priceaudit.pk_corp = '"
						+ pk_corp + "' ");
		vTableName.addElement("po_priceaudit");
		from
				.append(" LEFT OUTER JOIN po_priceaudit_bb1  ON po_priceaudit_b.cpriceauditid = po_priceaudit_bb1.cpriceauditid and po_priceaudit_b.cpriceaudit_bid = po_priceaudit_bb1.cpriceaudit_bid and po_priceaudit_bb1.dr = 0 ");
		vTableName.addElement("po_priceaudit_bb1");
		 from.append("LEFT OUTER JOIN bd_cubasdoc ON po_priceaudit_bb1.cvendorbaseid = bd_cubasdoc.pk_cubasdoc ");
		 vTableName.addElement("bd_cubasdoc");
		// from.append(
		// "LEFT OUTER JOIN bd_cumandoc ON po_priceaudit_bb1.cvendormangid =
		// bd_cumandoc.pk_cumandoc ");
		// vTableName.addElement("bd_cumandoc");
		// from.append(
		// "LEFT OUTER JOIN bd_currtype ON po_priceaudit.ccurrencytypeid =
		// bd_currtype.pk_currtype ");
		// vTableName.addElement("bd_currtype");
		 from.append("LEFT OUTER JOIN bd_invbasdoc ON po_priceaudit_b.cbaseid = bd_invbasdoc.pk_invbasdoc ");
		 vTableName.addElement("bd_invbasdoc");
		 from.append("LEFT OUTER JOIN bd_invcl ON bd_invbasdoc.pk_invcl= bd_invcl.pk_invcl");
		 vTableName.addElement("bd_invcl");
		// from.append(
		// "LEFT OUTER JOIN bd_invmandoc ON po_priceaudit_b.cmangid =
		// bd_invmandoc.pk_invmandoc ");
		// vTableName.addElement("bd_invmandoc");
		// from.append(
		// "LEFT OUTER JOIN bd_measdoc ON bd_invbasdoc.pk_measdoc=
		// bd_measdoc.pk_measdoc ");
		// vTableName.addElement("bd_measdoc");

		if (conds != null) {
//			ArrayList ary = null;
			nc.bs.ps.cost.CostanalyseDMO ddmo = null;
			for (int i = 0; i < conds.length - 1; i++) {
				String sOpera = conds[i].getOperaCode().trim();
				String sValue = conds[i].getValue();
				if ("like".equalsIgnoreCase(sOpera)
						&& !"bd_invcl.invclasscode".equals(
								conds[i].getFieldCode()))
					sValue = "%" + sValue + "%";
				// �ٴ����������
                //����Ȩ������VO
				 if("IS".equalsIgnoreCase(conds[i].getOperaCode().trim()) && "NULL".equalsIgnoreCase(conds[i].getValue().trim())
						|| sValue.trim().indexOf(VariableConst.PREFIX_OF_DATAPOWER_SQL) >= 0){
					listPowerVos.add(conds[i]);
				}
				// ������Ч����
				 else  if ("dvaliddate".equals(conds[i].getFieldCode())) {
					where.append(" and po_priceaudit_bb1.dvaliddate  "
							+ conds[i].getOperaCode() + "'");
					where.append(conds[i].getValue());
					where.append("' ");
				}
				// ����ʧЧ����
				else if ("dinvaliddate".equals(conds[i].getFieldCode())) {
					where.append(" and po_priceaudit_bb1.dinvaliddate "
							+ conds[i].getOperaCode() + "'");
					where.append(conds[i].getValue());
					where.append("' ");
				}
				// ���ֱ���
				else if ("currency".equals(conds[i].getFieldCode())) {
					where
							.append(" and po_priceaudit.ccurrencytypeid in (select pk_currtype from bd_currtype ");
					where.append("where bd_currtype.pk_currtype ");
					where.append(sOpera);
					where.append("'");
					where.append(sValue);
					where.append("') ");
				}
				// ��Ӧ������
				else if ("bd_cubasdoc.custname".equals(conds[i].getFieldCode()) && sValue != null && sValue.trim().length() > 0 && !"null".equalsIgnoreCase(sValue.trim()) && !sValue.trim().startsWith("(select")) {

					if (pk_corp != null) {
						where
								.append(" and po_priceaudit_bb1.cvendormangid in (select pk_cumandoc from bd_cumandoc,bd_cubasdoc ");
						where
								.append("where bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ");
						where.append("and bd_cumandoc.pk_corp = '");
						where.append(pk_corp + "' ");
						where.append("and bd_cubasdoc.custname ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
					} else {
						where
								.append(" and  po_priceaudit_bb1.cvendormangid in (select pk_cumandoc from bd_cumandoc,bd_cubasdoc ");
						where
								.append(" where bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ");
						where.append("and bd_cubasdoc.custname ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
					}
				}
				// ��Ӧ�̱���
				else if ("bd_cubasdoc.custcode".equals(conds[i].getFieldCode()) && sValue != null && sValue.trim().length() > 0 && !"null".equalsIgnoreCase(sValue.trim()) && !sValue.trim().startsWith("(select")) {

					if (pk_corp != null) {
						where
								.append(" and po_priceaudit_bb1.cvendormangid in (select pk_cumandoc from bd_cumandoc,bd_cubasdoc ");
						where
								.append("where bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ");
						where.append("and bd_cumandoc.pk_corp = '");
						where.append(pk_corp + "' ");
						where.append("and bd_cubasdoc.custcode ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
					} else {
						where
								.append(" and po_priceaudit_bb1.cvendormangid in (select pk_cumandoc from bd_cumandoc,bd_cubasdoc ");
						where
								.append(" where bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ");
						where.append("and bd_cubasdoc.custcode ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
					}
				}
				/* ������� */
				else if ("bd_invbasdoc.invcode".equals(conds[i].getFieldCode()) && sValue != null && sValue.trim().length() > 0 && !"null".equalsIgnoreCase(sValue.trim()) && !sValue.trim().startsWith("(select")) {

					if (pk_corp != null) {
						where
								.append(" and po_priceaudit_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ");
						where
								.append("where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ");
						where.append("and bd_invmandoc.pk_corp = '");
						where.append(pk_corp + "' ");
						where.append("and bd_invbasdoc.invcode ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
					} else {
						where
								.append(" and po_priceaudit_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ");
						where
								.append("where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ");
						where.append("and bd_invbasdoc.invcode ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
					}
				}
				/* ������� */
				else if ("bd_invbasdoc.invname".equals(conds[i].getFieldCode()) && sValue != null && sValue.trim().length() > 0 && !"null".equalsIgnoreCase(sValue.trim()) && !sValue.trim().startsWith("(select")) {
					if (pk_corp != null) {
						where
								.append(" and po_priceaudit_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ");
						where
								.append("where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ");
						where.append("and bd_invmandoc.pk_corp = '");
						where.append(pk_corp + "' ");
						where.append("and bd_invbasdoc.invname ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
					} else {
						where
								.append(" and po_priceaudit_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ");
						where
								.append("where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ");
						where.append("and bd_invbasdoc.invname ");
						where.append(sOpera);
						where.append("'");
						where.append(sValue);
						where.append("') ");
					}
				}
				// �����
				/* ���������� */
				else if ("bd_invcl.invclasscode"
						.equals(conds[i].getFieldCode())
						&& conds[i].getValue() != null
						&& conds[i].getValue().length() > 0) {
					try {
						String unitCode = null;
						ddmo = new nc.bs.ps.cost.CostanalyseDMO();
						String sClassCode[] = ddmo.getSubInvClassCode(sValue,
								sOpera);
						sOpera = "=";
						if (sClassCode != null && sClassCode.length > 0) {
							sValue = "(";
							for (int j = 0; j < sClassCode.length; j++) {
								if (j < sClassCode.length - 1)
									sValue += "C.invclasscode " + sOpera + " '"
											+ sClassCode[j] + "' or ";
								else
									sValue += "C.invclasscode " + sOpera + " '"
											+ sClassCode[j] + "')";
							}
							if (unitCode != null
									&& unitCode.trim().length() > 0) {
								where
										.append(" and po_priceaudit_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '"
												+ unitCode
												+ "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and "
												+ sValue + ")");
							} else {
								where
										.append(" and po_priceaudit_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where "
												+ "A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and "
												+ sValue + ")");
							}
						} else {
							if (unitCode != null
									&& unitCode.trim().length() > 0) {
								where
										.append(" and po_priceaudit_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '"
												+ unitCode
												+ "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode "
												+ sOpera + " '" + sValue + "')");
							} else {
								where
										.append(" and po_priceaudit_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where "
												+ "A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode "
												+ sOpera + " '" + sValue + "')");
							}
						}
					} catch (Exception e) {
						SCMEnv.out(e);
						PubDMO.throwBusinessException(
								nc.bs.ml.NCLangResOnserver.getInstance()
										.getStrByID("40040101",
												"UPP40040101-000008")/*
																		 * @res
																		 * "��ȡ��������ӱ���ʱ����:��ѯ������ܲ���ȷ"
																		 */, e);
					}
				}
				// ������˰�۸�
				else if ("nordertaxprice".equals(conds[i].getFieldCode())) {
					where.append(" and po_priceaudit_bb1.nordertaxprice "
							+ conds[i].getOperaCode() + "'");
					where.append(conds[i].getValue());
					where.append("' ");
				}
				// ������˰�۸�
				else if ("norderprice".equals(conds[i].getFieldCode())) {
					where.append(" and po_priceaudit_bb1.norderprice "
							+ conds[i].getOperaCode() + "'");
					where.append(conds[i].getValue());
					where.append("' ");
				}
				// ���۵�������
				// else if(conds[i].getFieldCode().equals("quotetype")){
				// if (conds[i].getValue() != null) {
				// if (conds[i].getValue().equals("ѯ���۵�")) {
				// quoteBillType = 1;
				// }
				// else if(conds[i].getValue().equals("�۸�������")){
				// quoteBillType = 2;
				// }
				// }
				// }
				// ���۵��ݺ�
				else if ("vquotebillcode".equals(conds[i].getFieldCode())) {
					where.append(" and po_priceaudit_bb1.vquotebillcode "
							+ conds[i].getOperaCode() + "'");
					where.append(conds[i].getValue());
					where.append("' ");
				}
				// �۸���������
				else if ("vpriceauditcode".equals(conds[i].getFieldCode())) {
					where.append(" and po_priceaudit.vpriceauditcode "
							+ conds[i].getOperaCode() + "'");
					where.append(conds[i].getValue());
					where.append("' ");
				}
				// �Ƿ�ɶ���
				else if ("border".equals(conds[i].getFieldCode())) {
					if (conds[i].getValue() != null) {
						if ("��".equals(conds[i].getValue())) {
							where.append(" and po_priceaudit_bb1.border "
									+ conds[i].getOperaCode() + "'Y' ");
//							where.append(conds[i].getValue());
//							where.append("' ");
						} else if ("��".equals(conds[i].getValue())) {
							where.append(" and po_priceaudit_bb1.border "
									+ conds[i].getOperaCode() + "'N' ");
//							where.append(conds[i].getValue());
//							where.append("' ");
						}
					}
				}
				 //�Ƿ������ʷ�۸� since5.3 modify by donggq
        else if ("bhistoryprice".equals(conds[i].getFieldCode())){
          if (conds[i].getValue() != null){
            if("��".equals(conds[i].getValue())){
              String date = conds[i].getFieldName();
              where.append(" and (po_priceaudit_bb1. dvaliddate <='"
                  + date
                  + "'or  po_priceaudit_bb1.dvaliddate is null ) and (po_priceaudit_bb1. dinvaliddate >='"
                  + date
                  + "'or  po_priceaudit_bb1.dinvaliddate is null )");
            }
          }
        }
			}
		}
		where.append(" )");
		if(listPowerVos.size() > 0){
			ConditionVO[] voaCondPower = new ConditionVO[listPowerVos.size()];
			listPowerVos.toArray(voaCondPower);
//			for(int i = 0 ; i < voaCondPower.length ; i ++){
//				voaCondPower[i].setNoLeft(false);
//				voaCondPower[i].setNoRight(false);
//			}
			String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);
			//���Ǳ�׼���ֶ��滻��
//			strPowerWherePart = StringUtil.replace(strPowerWherePart, "po_order.cdeptid_incsub", "po_order.cdeptid");
//			strPowerWherePart = StringUtil.replace(strPowerWherePart, "po_praybill.cdeptid_incsub", "po_praybill.cdeptid");
			//
			where.append(" and (" + strPowerWherePart + ") ");
		}

		// ��˾Լ��
		if (pk_corp != null && pk_corp.trim().length() > 0) {
			where.append(" and po_priceaudit.pk_corp = '");
			where.append(pk_corp);
			where.append("' ");
		}
		// ״̬Լ��
		
		// ����״̬Ϊ����
		//where.append("and po_priceaudit.ibillstatus = 3 ");
		//������Դ�����Ǽ۸�������
		where.append("and  po_priceaudit_bb1.fpricetype = 2 ");
		//�Լ����Լ����۱�������ͨ��
		where.append("and ((po_priceaudit.ibillstatus = 3 and po_priceaudit_bb1.cpriceaudit_bb1id = po_priceaudit_bb1.cquotebill_bb1id ) ");
		where.append("or (po_priceaudit_bb1.cpriceaudit_bb1id <> po_priceaudit_bb1.cquotebill_bb1id )) ");

		
		strSQL += from;
		strSQL += where;

		SCMEnv.out("��ѯ����Ϊ�� " + strSQL);

		return strSQL;
	}

	/**
	 * ���ߣ���־ƽ ���ܣ��жϵ���VO�Ƿ��в�����������,�����׳��쳣,���򷵻�new UFBoolean(true) ������ ���أ�UFBoolean
	 * ���⣺BusinessException , ����˵����1) ���� AggregatedVO ����ʵ�ֱ�ͷ����� getPrimaryKey()
	 * ���� 2) �����ݱ�ͷ�����ʱ������Ʊ����Ϊ ��ts�� 3) ���ݱ�����ϲ㵥�������������Ʊ���Ϊ ��cupsourcebilltype�� 4)
	 * ���ݱ�����ϲ㵥��ͷ����TS�������Ʊ���Ϊ ��cupsourcehts������cupsourcebts�� 5)
	 * ����������Ҫ�жϵĵ�������ʱ�漰���༰������ [1].ClassNameConst �м�����Ӧ���������������
	 * [2].queryHBTsArrayByHBIDArray() �м�����Ӧ���ݿ�������� [3].�������м�����Ӧ�ĵ������ʹ���
	 * �������ڣ�(2002-4-13 14:44:40) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-06-07 wyf ȥ��ί�ⷢƱ����ش���
	 * 2002-07-22 czp �ӻر���������������ʱ����
	 */
	public UFBoolean checkVoNoChangedForAudit(AggregatedValueObject[] vo)
			throws Exception {
		// ����ֵ
		UFBoolean ufbReslt = new UFBoolean(false);
		// û�����ݷ��� new UFBoolean(false)
		if (vo == null || vo.length <= 0)
			return ufbReslt;
		Vector v = PriceauditMergeVO.convertVOTOBackForImpl(vo[0]);
		PriceauditHeaderVO headerVO = (PriceauditHeaderVO) v.get(0);
		PriceauditBVO[] itemVO = (PriceauditBVO[]) v.get(1);
		PriceauditBb1VO[] itemBVO = (PriceauditBb1VO[]) v.get(2);
		ufbReslt = checkVoNoChanged(headerVO, itemVO, itemBVO,
				ClassNameConst._28_AGGR_CLASS_NAME_);
		return ufbReslt;
	}

	/**
	 * ���ߣ���־ƽ ���ܣ��жϵ���VO�Ƿ��в�����������,�����׳��쳣,���򷵻�new UFBoolean(true) ������ ���أ�UFBoolean
	 * ���⣺BusinessException , ����˵����1) ���� AggregatedVO ����ʵ�ֱ�ͷ����� getPrimaryKey()
	 * ���� 2) �����ݱ�ͷ�����ʱ������Ʊ����Ϊ ��ts�� 3) ���ݱ�����ϲ㵥�������������Ʊ���Ϊ ��cupsourcebilltype�� 4)
	 * ���ݱ�����ϲ㵥��ͷ����TS�������Ʊ���Ϊ ��cupsourcehts������cupsourcebts�� 5)
	 * ����������Ҫ�жϵĵ�������ʱ�漰���༰������ [1].ClassNameConst �м�����Ӧ���������������
	 * [2].queryHBTsArrayByHBIDArray() �м�����Ӧ���ݿ�������� [3].�������м�����Ӧ�ĵ������ʹ���
	 * �������ڣ�(2002-4-13 14:44:40) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-06-07 wyf ȥ��ί�ⷢƱ����ش���
	 * 2002-07-22 czp �ӻر���������������ʱ����
	 */
	public UFBoolean checkVoNoChanged(CircularlyAccessibleValueObject headerVO,
			CircularlyAccessibleValueObject[] itemVO,
			CircularlyAccessibleValueObject[] itemBVO, String strClassName)
			throws Exception {
		UFBoolean isLockSuccess = null;
//		PubDMO pubDmo = new PubDMO();
		String[] saBillids = null;
        try{
		long beginTime = System.currentTimeMillis();

		// ����ֵ
		UFBoolean ufbReslt = new UFBoolean(false);
		// û�����ݷ��� new UFBoolean(false)
		if (headerVO == null || itemVO == null || itemVO.length <= 0
				)
			return ufbReslt;
		// ��֯ checkTsNoChanged() �Ĳ���
		String sBillType = null;
		String[] saBillid = null;
		String[] saTsh = null;
		String[] saBill_bid = null;
		String[] saTsb = null;
		String[] saBill_bb1id = null;
		String[] saTsBB1 = null;
		// �ݲ�֧�����ӱ�
//		String[] saBill_bbid = null;
//		String[] saTsBb = null;
		String[] saTshFroPray = null;
		String[] saBillidForPray = new String[1];
//		String[] saTshForPray = new String[1];
//		String strTmpIdForPray = null;
//		String strTmpTsForPray = null;
		String[] saBill_bidForPray = null;
		String[] saTsbForPray = null;
		
		Vector  tempV = new Vector();
		Vector  resultV = new Vector();
		String[]  tempS = null;
		AskbillDMO dmo = new AskbillDMO();
		
		// ��������
		if(ClassNameConst._29_AGGR_CLASS_NAME_.equals(strClassName)){
			 sBillType = BillTypeConst.PO_ASK;
			 
			 
		}else if (ClassNameConst._28_AGGR_CLASS_NAME_.equals(strClassName)){
			 sBillType = BillTypeConst.PO_PRICEAUDIT;
			 for (int i = 0; i < itemVO.length; i++) {
				 if (itemVO[i].getAttributeValue("cupsourcebilltype") != null
							&& itemVO[i].getAttributeValue("cupsourcebilltype")
									.toString().trim().equals(BillTypeConst.PO_ASK)){
					 tempV.add((String) itemVO[i]
						.getAttributeValue("cupsourcebillrowid"));
				 }
			}
			if(tempV.size() > 0){
				tempS = new String[tempV.size()];
				tempV.copyInto(tempS);
                //������ʱ��
				nc.bs.scm.pub.TempTableDMO dmoTmpTable = new nc.bs.scm.pub.TempTableDMO();			
				String conditionFroInv = dmoTmpTable.insertTempTable(
					tempS,
					nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_ASK_12,
					"cupsourcebillrowid") ;
				resultV = dmo.getDataForCheckTs(conditionFroInv);
				if(resultV != null && resultV.get(0) != null && resultV.get(1) != null && resultV.get(2) != null && resultV.get(3) != null){
				 
				 saTshFroPray = (String[])resultV.get(0);
				 saTsbForPray = (String[])resultV.get(1);
				 saBillidForPray = (String[])resultV.get(2);
				 saBill_bidForPray = (String[])resultV.get(3);
				 saBillids = (String[])resultV.get(4);
				 
				
					isLockSuccess = lockPkForVoForAskBill(headerVO,saBillids);
					if (isLockSuccess != null && isLockSuccess.booleanValue() ) { 
						checkTsNoChanged(IBillType.PRAYBILL, saBillidForPray, saTshFroPray, saBill_bidForPray, saTsbForPray, null,
								null);
					}
				}
		    }
		}
		
		
		// ����������־
		boolean bIsNew = (headerVO.getPrimaryKey() == null || headerVO
				.getPrimaryKey().trim().length() == 0);
		// ����������ĵ���
		if (bIsNew) {
			saBillid = null;
			saTsh = null;
			if (itemVO == null || itemVO.length <= 0)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver
						.getInstance().getStrByID("4004pub",
								"UPP4004pub-000118")/* @res "�޿ɲ����������쳣" */);
			// ���õ�������
			boolean bIsHaveRef = false;
			for (int i = 0; i < itemVO.length; i++) {
				if (itemVO[i].getAttributeValue("cupsourcebilltype") != null
						&& !"".equals(itemVO[i].getAttributeValue("cupsourcebilltype")
								.toString().trim())) {
					sBillType = (String) itemVO[i]
							.getAttributeValue("cupsourcebilltype");
					bIsHaveRef = true;
					break;
				}
			}
			// ������е����о������ƣ��򷵻� new UFBoolean(true)
			if (!bIsHaveRef)
				return new UFBoolean(true);
			// �����ϲ�ID����
			Vector vUpId = new Vector();
			// �����ϲ�TS����
			Vector vUpTs = new Vector();
			// ��ͷ�ϲ�ID��TS��ʼ��
			saBillid = new String[1];
			saTsh = new String[1];
			String strTmpId = null;
			String strTmpTs = null;
			
			for (int i = 0; i < itemVO.length; i++) {
				strTmpId = (String) itemVO[i]
						.getAttributeValue("cupsourcebillrowid");
				// ������ϲ���ID����Ϊ��ת�������
				if (strTmpId != null && !"".equals(strTmpId.trim())) {
					strTmpTs = (String) itemVO[i]
							.getAttributeValue("cupsourcebts");
					if (strTmpTs == null || "".equals(strTmpTs.trim())) {
						SCMEnv.out("���ݴ���δ��ȡ�ɲ��յ���ת��ı���ʱ���");
						return new UFBoolean(false);
					}
					// �����ϲ�ID����
					vUpId.addElement(strTmpId);
					// �����ϲ�TS����
					vUpTs.addElement(strTmpTs);
					// ��ͷ���ϲ�ID��TSֻ�踳һ��ֵ
					if (saBillid[0] == null || saBillid[0].trim().length() == 1) {
						saBillid[0] = (String) itemVO[i]
								.getAttributeValue("cupsourcebillid");
						saTsh[0] = (String) itemVO[i]
								.getAttributeValue("cupsourcehts");
						if (saTsh[0] == null || "".equals(saTsh[0].trim())) {
							SCMEnv.out("���ݴ���δ��ȡ�ɲ��յ���ת��ı�ͷʱ���");
							return new UFBoolean(false);
						}
					}
				}
			}
			// �����ϲ�ID���鼰�ϲ�TS����
			if (vUpId.size() > 0 && vUpTs.size() > 0
					&& vUpId.size() == vUpTs.size()) {
				saBill_bid = new String[vUpId.size()];
				saTsb = new String[vUpTs.size()];
				vUpId.copyInto(saBill_bid);
				vUpTs.copyInto(saTsb);
			} else {
				SCMEnv.out("���ݴ������ϲ�ת��ĵ��ݣ����ϲ�ID���ϲ�TS��ƥ��");
				return new UFBoolean(false);
			}

		}
		// ������޸ĵĵ���
		else {
			saBillid = new String[1];
			saTsh = new String[1];
			// ��ͷID
			saBillid[0] = headerVO.getPrimaryKey();
			// ��ͷTS
			saTsh[0] = (String) headerVO.getAttributeValue("ts");
			
			if (saTsh[0] == null || "".equals(saTsh[0].trim())) {
				SCMEnv.out("���ݴ���δ��ȡ�޸ı��浥�ݵı�ͷʱ���");
				return new UFBoolean(false);
			}
			// ����ID , ����TS
			// �޸ĵı���ID����
			Vector vBid = new Vector();
			Vector vBts = new Vector();
			Vector vBBid = new Vector();
			Vector vBBts = new Vector();
			if (itemVO != null && itemVO.length > 0) {
				for (int i = 0; i < itemVO.length; i++) {
					if (!(itemVO[i].getStatus() == nc.vo.pub.VOStatus.NEW)) {
						if (itemVO[i].getPrimaryKey() != null
								&& itemVO[i].getPrimaryKey().trim().length() > 0) {
							vBid.addElement(itemVO[i].getPrimaryKey());
							vBts.addElement(itemVO[i].getAttributeValue("ts"));
						}
					}
				}
				if (vBid.size() <= 0) {
					SCMEnv.out("���ݴ���δ��ȡ�ɲ��յ���ת��ı���ʱ���");
					return new UFBoolean(false);
				}
				saBill_bid = new String[vBid.size()];
				saTsb = new String[vBid.size()];
				for (int i = 0; i < vBid.size(); i++) {
					saBill_bid[i] = (String) vBid.elementAt(i);
					saTsb[i] = (String) vBts.elementAt(i);
				}
			}
			if (itemBVO != null && itemBVO.length > 0) {
				for (int i = 0; i < itemBVO.length; i++) {
					if (!(itemBVO[i].getStatus() == nc.vo.pub.VOStatus.NEW)) {
						if (itemBVO[i].getPrimaryKey() != null
								&& itemBVO[i].getPrimaryKey().trim().length() > 0) {
							vBBid.addElement(itemBVO[i].getPrimaryKey());
							vBBts
									.addElement(itemBVO[i]
											.getAttributeValue("ts"));
						}
					}
				}
				if (vBid.size() <= 0 || vBBid.size() <= 0) {
					SCMEnv.out("���ݴ���δ��ȡ�ɲ��յ���ת��ı���ʱ���");
					return new UFBoolean(false);
				}
				saBill_bb1id = new String[vBBid.size()];
				saTsBB1 = new String[vBBts.size()];
				vBBid.toArray(saBill_bb1id);
				vBBts.toArray(saTsBB1);
			}
		}
		// ���ò�����鷽��
		checkTsNoChanged(sBillType, saBillid, saTsh, saBill_bid, saTsb, saBill_bb1id,
				saTsBB1);
		// �������ͨ���򷵻� new UFBoolean(true)

		long endTime = System.currentTimeMillis();
		SCMEnv.out("���ŵ��ݲ����������ʱ�䣺" + (endTime - beginTime));
		
        }catch (Exception e) {
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.checkVoNoChanged", e);
		} finally {
			// ��ҵ����
			if (isLockSuccess != null && isLockSuccess.booleanValue()) {
				try {
					freePkForVoForAskBill(headerVO,saBillids);
				} catch (Exception e) {
					nc.bs.pu.pub.PubDMO.throwBusinessException(e);
				}
			}
		}
		return new UFBoolean(true);
	}
	/**
	 * ���ߣ���־ƽ
	 * ���ܣ�������
	 * ������AggregatedValueObject
	 * ���أ�UFBoolean
	 * ���⣺
	 * �������ڣ�(2002-4-15 10:27:42)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 2004-02-19	WYF		�޸�bean.remove()�Ĵ���
	 */
	public UFBoolean lockPkForVoForAskBill(CircularlyAccessibleValueObject headerVO,String[] saPk) throws Exception {

		boolean isLockSucc = false;
//		nc.bs.pub.lock.LockBOAccess boLock = new nc.bs.pub.lock.LockBOAccess();
		try {

			//����Ա
			String sCuser = null;
			sCuser = (String) headerVO.getAttributeValue("cuserid");
			SCMEnv.out("����Ϊ����Ա[ID:" + sCuser + "]����NCҵ����...");
			if (sCuser == null || "".equals(sCuser.trim()))
				throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("4004pub","UPP4004pub-000119")/*@res "��ȡ����Ա�������ܽ�����ز�����"*/);
			if (saPk != null) {
//				isLockSucc = boLock.lockPKArray(saPk, sCuser, "");
				isLockSucc = LockTool.setLockForPks(saPk, sCuser);
				if (isLockSucc) {
					SCMEnv.out("Ϊ����Ա[ID:" + sCuser + "]����NCҵ�����ɹ���");
				} else {
					throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("4004pub","UPP4004pub-000129")/*@res "���ڲ������������Ժ�����"*/);
				}
			}
			//����Ҫ���������
			else {
				SCMEnv.out("Ϊ����Ա[ID:" + sCuser + "]����NCҵ����ʧ�ܡ�ʧ��ԭ�򣺲���Ҫ���������!");
			}
		} catch (Exception e) {
			SCMEnv.out(e);
			throw	e ;
		}
		return new UFBoolean(isLockSucc);
	}
	/**
	 * ���ߣ���־ƽ
	 * ���ܣ��ͷŵ�����
	 * ������AggregatedValueObject
	 * ���أ�void
	 * ���⣺
	 * �������ڣ�(2002-4-15 10:27:42)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 2004-02-19	WYF		�޸�bean.remove()�Ĵ���
	 */
	public void freePkForVoForAskBill(CircularlyAccessibleValueObject headerVO,String[] saPk) throws Exception {
		//����Ա
		String sCuser = (String)headerVO.getAttributeValue("cuserid");
		SCMEnv.out("����Ϊ����Ա[ID:" + sCuser + "]�ͷ�NCҵ����...");
		if (sCuser == null || "".equals(sCuser.trim()))
			throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("4004pub","UPP4004pub-000119")/*@res "��ȡ����Ա�������ܽ�����ز�����"*/);
		try {
			if (saPk != null) {
				LockTool.releaseLockForPks(saPk, sCuser);
			}
			SCMEnv.out("Ϊ����Ա[ID:" + sCuser + "]�ͷ�NCҵ�����ɹ�������");
		} catch (Exception e) {
			SCMEnv.out("Ϊ����Ա[ID:" + sCuser + "]�ͷ�NCҵ�����쳣������");
			throw	e ;
		}
	}
	/**
	 * ���ߣ���־ƽ ���ܣ�����һ�鶩��ID����ID��ѯ�����ӱ�ID������Ӧ��ʱ������鷵���Ƿ�ʱ��������˱仯 ������ String sBilltype
	 * �������ͣ�{20,21,22,23,25,27,29,2A,45,47,4T,61,62} String[] saBillid ���ݱ�ͷID����
	 * String[] saBillid ���ݱ�ͷ��Ӧ��TS���� String[] saBill_bid �����ӱ�ID���� String[]
	 * saBillid �����ӱ��Ӧ��TS���� String[] saBill_bbid �������ӱ�ID���� String[] saBillid
	 * �������ӱ��Ӧ��TS���� ע�⣺ ���� sBillType ����Ϊ�� �������䲻�����Ʊ���һ�ŵ��ݵ�ID����ID�����ӱ�ID��������������ͬʱΪ��
	 * ���أ�����κ�һ��TS�б仯�����쳣��������ͨ��ͬʱ���� new UFBoolean(true) ���⣺BusinessException
	 * SQLException ���ڣ�(2002-4-9 9:27:51) �޸����ڣ� �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public UFBoolean checkTsNoChanged(String sBillType, String[] saBillid,
			String[] saTsh, String[] saBill_bid, String[] saTsb,
			String[] saBill_bbid, String[] saTsBb) throws BusinessException {
		boolean b[] = { false, false, false };
		// �Ϸ��Լ��
		if (saBillid != null && saBillid.length > 0) {
			b[0] = true;
			if (saTsh == null || saTsh.length <= 0) {
				throw new BusinessException(nc.bs.ml.NCLangResOnserver
						.getInstance().getStrByID("4004pub",
								"UPP4004pub-000116")/*
													 * @res
													 * "PubDMO.checkTsChanged()��������"
													 */);
			}
		}
		if (saBill_bid != null && saBill_bid.length > 0) {
			b[1] = true;
			if (saTsb == null || saTsb.length <= 0) {
				throw new BusinessException(nc.bs.ml.NCLangResOnserver
						.getInstance().getStrByID("4004pub",
								"UPP4004pub-000116")/*
													 * @res
													 * "PubDMO.checkTsChanged()��������"
													 */);
			}
		}
		if (saBill_bbid != null && saBill_bbid.length > 0) {
			b[2] = true;
			if (saTsBb == null || saTsBb.length <= 0) {
				throw new BusinessException(nc.bs.ml.NCLangResOnserver
						.getInstance().getStrByID("4004pub",
								"UPP4004pub-000116")/*
													 * @res
													 * "PubDMO.checkTsChanged()��������"
													 */);
			}
		}
		// ˢ��TS
		AskbillDMO dmo = null;
		Object[] saObj = null;
		try {
			dmo = new AskbillDMO();
			saObj = dmo.queryHBTsArrayByHBIDArray(sBillType, saBillid,
					saBill_bid, saBill_bbid);
		} catch (SystemException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.checkTsNoChanged", e);
		} catch (NamingException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.checkTsNoChanged", e);
		}
		if (saObj == null || saObj.length <= 0) {

			// ****************ljq begin *********************/

			// throw new
			// BusinessException("pubDMO.queryHBTsArrayByHBIDArray()����");
			SCMEnv.out("û�в�ѯ����Ӧ��ʱ������ݲ�����go on��");
			return new UFBoolean(true);
		}

		// ****************ljq end ***********************/
		// �Ƚ�TS
		String[] saTmp = null;
		if (b[0]) {
			saTmp = (String[]) saObj[0];
			if (saTmp == null || saTmp.length <= 0)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver
						.getInstance().getStrByID("4004pub",
								"UPP4004pub-000117")/* @res "����������������ˢ�½�������" */);
			for (int i = 0; i < saTmp.length; i++) {
				if (!saTsh[i].equals(saTmp[i])) {
					throw new BusinessException(nc.bs.ml.NCLangResOnserver
							.getInstance().getStrByID("4004pub",
									"UPP4004pub-000117")/* @res "����������������ˢ�½�������" */);
				}
			}
		}
		if (b[1]) {
			saTmp = (String[]) saObj[1];
			if (saTmp == null || saTmp.length <= 0)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver
						.getInstance().getStrByID("4004pub",
								"UPP4004pub-000117")/* @res "����������������ˢ�½�������" */);
			for (int i = 0; i < saTmp.length; i++) {
				if (saTsb[i] != null && !saTsb[i].equals(saTmp[i])) {
					throw new BusinessException(nc.bs.ml.NCLangResOnserver
							.getInstance().getStrByID("4004pub",
									"UPP4004pub-000117")/* @res "����������������ˢ�½�������" */);
				}
			}
		}
		if (b[2]) {
			saTmp = (String[]) saObj[2];
			if (saTmp == null || saTmp.length <= 0)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver
						.getInstance().getStrByID("4004pub",
								"UPP4004pub-000117")/* @res "����������������ˢ�½�������" */);
			for (int i = 0; i < saTmp.length; i++) {
				if (saTsBb[i] != null && !saTsBb[i].equals(saTmp[i])) {
					throw new BusinessException(nc.bs.ml.NCLangResOnserver
							.getInstance().getStrByID("4004pub",
									"UPP4004pub-000117")/* @res "����������������ˢ�½�������" */);
				}
			}
		}
		return new UFBoolean(true);
	}
	/**
	 * ���빺�����ɶ������Ʒ�ʽ��ѡ��Ϊ�������۸������������ɡ�,��ѯ�����������빺���С�
	 * 
	 * �������ڣ�(2001-6-7)
	 * 
	 * @return nc.vo.pp.ask.AskbillItemVO
	 * @param key
	 *            String
	 * @throws Exception
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	public Hashtable queryIsGenPriceAudit(ArrayList prayRowIds)
			throws Exception {
		if (prayRowIds == null
				|| (prayRowIds != null && prayRowIds.size() == 0)) {
			return null;
		}
		Hashtable result = new Hashtable();
		Hashtable resultForReturn = new Hashtable();
		AskbillDMO dmo = null;
		try {
			dmo = new AskbillDMO();
			result = dmo.queryIsGenPriceAudit(prayRowIds);
		} catch (SystemException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryIsGenPriceAudit", e);
		} catch (NamingException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryIsGenPriceAudit", e);
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryIsGenPriceAudit", e);
		}
		for (int i = 0; i < prayRowIds.size(); i++) {
			if (result.containsKey(prayRowIds.get(i))) {
				resultForReturn.put(prayRowIds.get(i), new UFBoolean(true));
			} else {
				resultForReturn.put(prayRowIds.get(i), new UFBoolean(false));
			}
		}
		return resultForReturn;
	}

	/**
	 * ���ݱ�������ȡ�ñ���ID ���裺��������Ψһ �������ڣ�(2001-10-27 13:30:59)
	 * 
	 * @return java.lang.String
	 * @param currname
	 *            java.lang.String
	 */
	public String queryCurrIDByCurrName(String currname)
			throws BusinessException {
		String currid = null;
		if (currname == null)
			return null;
		if ("".equals(currname.trim()))
			return null;
		try {
			AskbillDMO dmo = new AskbillDMO();
			currid = dmo.getCurrtypePkByCurrName(currname);
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			SCMEnv.out(e.getMessage());
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryCurrIDByCurrName", e);
		}

		return currid;
	}

	/**
	 * @ѯ�۵��Ƿ�ִ�����빺
	 * @���ߣ�����
	 * @������String[] saRowId �빺��������
	 * @����ֵ��UFBoolean[] uaExistAfter ע��true��ʾ�빺���д��ں������ݣ�false��ʾ������
	 *                  �������ڣ�(2005-08-09 15:04:05)
	 * @return nc.vo.pub.lang.UFBoolean[]
	 */
	public UFBoolean[] queryIfExecPrayForAsk(String[] saRowId)
			throws BusinessException {
		UFBoolean[] uaExistAfter = null;
		if (saRowId == null && saRowId.length == 0) {
			return uaExistAfter;
		}
		try {
			AskbillDMO dmo = new AskbillDMO();
			uaExistAfter = dmo.queryIfExecPray(saRowId);
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			nc.bs.pu.pub.PubDMO.throwBusinessException(e);
		}
		return uaExistAfter;
	}

	/**
	 * @����ʱѡ��Ӧ�̵�������
	 * @���ߣ�����
	 * @������String[] saRowId �빺��������
	 * @����ֵ��UFBoolean[] uaExistAfter ע��true��ʾ�빺���д��ں������ݣ�false��ʾ������
	 *                  �������ڣ�(2005-08-09 15:04:05)
	 * @return nc.vo.pub.lang.UFBoolean[]
	 */
	public Hashtable queryEmailAddrForAskSend(String[] cvendorIds)
			throws BusinessException {
		Hashtable result = new Hashtable();
		if (cvendorIds == null
				|| (cvendorIds != null && cvendorIds.length == 0)) {
			return result;
		}
		try {
			AskbillDMO dmo = new AskbillDMO();
			result = dmo.queryEmailAddrForAskSend(cvendorIds);
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			nc.bs.pu.pub.PubDMO.throwBusinessException(e);
		}
		return result;
	}

	/**
	 * ��������:�����ɶ����ļ۸���������������
	 * 
	 * @throws BusinessException
	 */
	public void CheckIsGenOrder(String condition) throws BusinessException {
		try {
			AskbillDMO dmo = new AskbillDMO();
			dmo.CheckIsGenOrder(condition);
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			nc.bs.pu.pub.PubDMO.throwBusinessException(e);
		}
	}

	/**
	 * @���ܣ����ִ�м۶Ա�VO[]����ִ�м۱䶯VO[]
	 * @˼·�� 1.���ݲ�ѯ�������˳���ͬ���ID x �� [po_askbill_b.cmangid] 2.���ݲ�ѯ�������˳���ͬ�ı���ID y ��
	 *      [po_askbill.ccurrencytypeid] 3.������ѭ����ȡ��������Ϣ��¼ x*y �� 4.���ڼ� pDates ��
	 *      pDates[i],pDates[i+1] Ϊ������Ŀͳ��
	 * @���� 0.���ִ��� ����ֻ��Ϊ��ѯ���� 1.��ѯ���������Ĵ��������ID 2.����ID��ȡ��Ӧ�Ĵ����Ϣ #�������ƹ���ͺ� #������
	 *      #������ #�ο��ɱ� #�ƻ��� #���¼� 3.���������ID + ��ѯ����(������) -> ���ִ�м۶Աȱ���VO[] (���ڼ�ѭ��)
	 *      4.�á����������ID+����ID������ͳ��->��߼ۡ���ͼۡ�ƽ����
	 * @return stockexecs StockExecVO[]
	 * @param paravo
	 *            QuoteConParaVO
	 * @throws BusinessException
	 */
	public StockExecVO[] queryStockStatVOsMy(StatParaVO paravo)
			throws BusinessException {

		ConditionVO[] conds = paravo.getConds();
		String pk_corp = paravo.getPk_corp();
		String[] pk_corps = paravo.getPk_corps();
//		UFBoolean[] status = paravo.getStatus();
		UFBoolean isInTax = paravo.getIsInTax();
		String dataSource = paravo.getDataSource();
		UFDate[] pDates = paravo.getPeriods();
		String linkSign = paravo.getLinkSign();
		Integer periodType = paravo.getPeriodType();
		StockExecVO[] stockexecs = null;
		StockExecVO stockexec = null;
		StockExecHeaderVO header = null;
		StockExecItemVO[] quoteconitems = null;
		String[] cmangids = null;
		String[] cbaseids = null;
		String[] ccurrids = null;
		Vector v = new Vector();
		ArrayList aryInvIDs = null;
		ArrayList aryInvInfos = null;
//		ArrayList aryPrice = null;
		String currtypename = null;
		String pk_currtype = null;
//		String ss = null;
//		String sOperator = null;
		try {
			AskbillDMO dmo = new AskbillDMO();
			String strSQL = "";
			UFDate bDate = pDates[0];
			UFDate eDate = pDates[pDates.length - 1];
			getCorpConditions(pk_corps);
			strSQL = getSQLForPurExecVOsMy(conds, pk_corp,pk_corps, dataSource, isInTax,
					bDate, eDate);
			// ����Ȩ�޿���
//			if (conds[conds.length - 1].getFieldName().equals("����Ա")) {
//				sOperator = conds[conds.length - 1].getValue();
//				if (dataSource.equals("Invoice")) {
//					ss = nc.bs.scm.datapower.ScmDps.getSubSql("po_invoice",
//							"po_invoice", sOperator, new String[] { pk_corp });
//					if (ss != null && ss.trim().length() > 0)
//						strSQL += " and " + ss + " ";
//					ss = nc.bs.scm.datapower.ScmDps
//							.getSubSql("po_invoice_b", "po_invoice_b",
//									sOperator, new String[] { pk_corp });
//					if (ss != null && ss.trim().length() > 0)
//						strSQL += " and " + ss + " ";
//				} else {
//					ss = nc.bs.scm.datapower.ScmDps.getSubSql("po_order",
//							"po_order", sOperator, new String[] { pk_corp });
//					if (ss != null && ss.trim().length() > 0)
//						strSQL += " and " + ss + " ";
//					ss = nc.bs.scm.datapower.ScmDps.getSubSql("po_order_b",
//							"po_order_b", sOperator, new String[] { pk_corp });
//					if (ss != null && ss.trim().length() > 0)
//						strSQL += " and " + ss + " ";
//				}
//			}
			// ��ͬ�Ĵ��
//			if (dataSource.equals("Invoice")) {
//				aryInvIDs = dmo.findInvIDsForPurExecVOMyInvoice(strSQL);
//			} else {
//				aryInvIDs = dmo.findInvIDsForPurExecVOMyOrder(strSQL);
//			}
//			if (aryInvIDs == null)
//				return null;
//			if (aryInvIDs.get(0) == null)
//				return null;
//			if (aryInvIDs.get(1) == null)
//				return null;
//			cmangids = (String[]) aryInvIDs.get(0);
//			cbaseids = (String[]) aryInvIDs.get(1);
//			// ��ͬ�ı���
//			if (dataSource.equals("Invoice")) {
//				ccurrids = dmo.findCurrIDsForPurExecVOsMyInvoice(strSQL);
//			} else {
//				ccurrids = dmo.findCurrIDsForPurExecVOsMyOrder(strSQL);
//			}
//			if (ccurrids == null)
//				return null;
//			if (ccurrids.length <= 0)
//				return null;
			//Ч���޸�
            //��ͬ�Ĵ��
			if ("Invoice".equals(dataSource)) {
				aryInvIDs = dmo.findInvIDsForPurExecVOMyInvoiceForBantch(strSQL,"4004070304");
			} else {
				aryInvIDs = dmo.findInvIDsForPurExecVOMyOrderForBantch(strSQL,"4004070304");
			}
			if (aryInvIDs == null)
				return null;
			if (aryInvIDs.get(0) == null)
				return null;
			if (aryInvIDs.get(1) == null)
				return null;
			if (aryInvIDs.get(2) == null)
				return null;
			cmangids = (String[]) aryInvIDs.get(0);
			cbaseids = (String[]) aryInvIDs.get(1);
			ccurrids = (String[]) aryInvIDs.get(2);
			//Ч���޸�--������ѯ
			Hashtable resultForInv = dmo.getInvInfosByPkForBantch(cbaseids,"4004070304");
			
			//��ͬ�ı���
			if (ccurrids == null)
				return null;
			if (ccurrids.length <= 0)
				return null;
			
            //Ч���޸�--������ѯ
			Hashtable resultForCurtype = dmo.getCurrtypenameByPkForBantch(ccurrids,"4004070304");
			Hashtable resultForLastPrice = new Hashtable();
			if ("Invoice".equals(dataSource)) {
				resultForLastPrice = dmo.getPriceLastInvoiceForBantch(cmangids, ccurrids, isInTax.booleanValue(), strSQL,"4004070304");
			} else if ("Order".equals(dataSource)) {
				resultForLastPrice = dmo.getPriceLastOrderForBantch(cmangids, ccurrids, isInTax.booleanValue(), strSQL,"4004070304");
			}
			Hashtable resultForOtherPrice = new Hashtable();
			if ("Invoice".equals(dataSource)) {
				resultForOtherPrice = dmo.getPricesForQuoteConVOInvoiceForBantch(cmangids, ccurrids, isInTax.booleanValue(), strSQL,"4004070304");
			} else if ("Order".equals(dataSource)) {
				resultForOtherPrice = dmo.getPricesForQuoteConVOOrderForBantch(cmangids, ccurrids, isInTax.booleanValue(), strSQL,"4004070304");
			}
//			UFDate periodFrom = null;
//			UFDate periodTo = null;
			Vector tempResult = null;
			UFDouble taxratio = null;
			for (int i = 0; i < cmangids.length; i++) {
				// ��������
				
					quoteconitems = null;
					tempResult = new Vector();
					for(int m = 0 ; m < pk_corps.length ; m ++){
					String strSQLT = getSQLForPurExecVOsMy(conds, pk_corps[m],new String[]{pk_corps[m]}, dataSource, isInTax,
							bDate, eDate);
					quoteconitems = dmo.findItemsForStockExecVOMy(dataSource,
							isInTax.booleanValue(), cmangids[i], ccurrids[i],
							strSQLT, pDates, linkSign, periodType.intValue(),"4004070304");
					if(quoteconitems != null && quoteconitems.length > 0){
						for(int n = 0 ; n < quoteconitems.length ; n ++){
							tempResult.add(quoteconitems[n]);
						}
					}
					}
					if(tempResult.size() > 0){
						quoteconitems = new StockExecItemVO[tempResult.size()];
						tempResult.copyInto(quoteconitems);
					}
					if (quoteconitems != null) {
						// ���ñ�ͷ
						header = new StockExecHeaderVO();
						// ����
						pk_currtype = ccurrids[i];
						//currtypename = dmo.getCurrtypenameByPk(pk_currtype);
//						if (pk_currtype == null
//								|| pk_currtype.trim().equals("")) {
//							SCMEnv
//									.out("ȡ����ʱ����[nc.bs.pp.ask.AskbillBO.queryStockVarVOsMy(...)]");
//							throw new RemoteException(
//									nc.bs.ml.NCLangResOnserver.getInstance()
//											.getStrByID("40040701",
//													"UPP40040701-000005")/*
//																			 * @res
//																			 * "ȡ����ʱ����[nc.bs.pp.ask.AskbillBO.queryStockVarVOsMy(...)]"
//																			 */);
//						}
						if(resultForCurtype.get(ccurrids[i]) != null){
							currtypename = (String)resultForCurtype.get(ccurrids[i]);
						}
						if (pk_currtype == null || "".equals(pk_currtype.trim())) {
							SCMEnv.out("ȡ����ʱ����[nc.bs.pp.ask.AskbillBO.queryStockVarVOsMy(...)]");
							throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000005")/*@res "ȡ����ʱ����[nc.bs.pp.ask.AskbillBO.queryStockVarVOsMy(...)]"*/);
						}
						header.setCurrtypename(currtypename);
						header.setPk_currtype(pk_currtype);

						// �������
//						aryInvInfos = dmo.getInvInfosByPk(cbaseids[i]);
						if(resultForInv.get(cbaseids[i]) != null){
							aryInvInfos = (ArrayList)resultForInv.get(cbaseids[i]);
						}
						if (aryInvInfos == null) {
							header.setInvcode(null);
							header.setInvname(null);
							header.setInvspec(null);
							header.setInvtype(null);
						} else {
							header.setInvcode((String) aryInvInfos.get(0));
							header.setInvname((String) aryInvInfos.get(1));
							header.setInvspec((String) aryInvInfos.get(2));
							header.setInvtype((String) aryInvInfos.get(3));
						}
						// ������
//						if (aryInvInfos == null || aryInvInfos.get(4) == null) {
//							header.setMeasname(null);
//						} else {
//							String pk_measdoc = (String) aryInvInfos.get(4);
//							String measname = null;
//							measname = dmo.getMeasnameByPk(pk_measdoc);
//							header.setMeasname(measname);
//						}
						String measname = (String) aryInvInfos.get(6);
						header.setMeasname(measname);
						// ˰��
//						if (aryInvInfos == null || aryInvInfos.get(5) == null) {
//							header.setTaxratio(new UFDouble(0));
//						} else {
//							String pk_taxitems = (String) aryInvInfos.get(5);
//							UFDouble taxratio = null;
//							taxratio = dmo.getTaxratioByPk(pk_taxitems);
//							if (taxratio == null) {
//								header.setTaxratio(new UFDouble(0));
//							} else {
//								header.setTaxratio(taxratio);
//							}
//						}
						if(aryInvInfos.get(7) != null && aryInvInfos.get(7).toString().trim().length() > 0){
							taxratio = new UFDouble(aryInvInfos.get(7).toString());
							header.setTaxratio(taxratio);
						}else{
							header.setTaxratio(new UFDouble(0));
						}
						// ���ñ�β

						// �ο��ɱ����ƻ���
//						aryPrice = dmo.getCostPlanPriceForThanVO(cmangids[i]);
//						if (aryPrice == null) {
//							header.setCostprice(null);
//							header.setPlanprice(null);
//						} else {
//							header.setCostprice((UFDouble) aryPrice.get(0));
//							header.setPlanprice((UFDouble) aryPrice.get(1));
//						}
						if (aryInvInfos.get(7) == null) {
							header.setCostprice(null);
						}
						if (aryInvInfos.get(8) == null) {
							header.setPlanprice(null);
						} 
                        if(aryInvInfos.get(7) != null && aryInvInfos.get(7).toString().trim().length() > 0
                        		){
							header.setCostprice(new UFDouble(aryInvInfos.get(7).toString()));
                        }
                        if(aryInvInfos.get(8) != null && aryInvInfos.get(8).toString().trim().length() > 0){
							header.setPlanprice(new UFDouble(aryInvInfos.get(8).toString()));
						}
						// ���¼�
						UFDouble newprice = null;
//						if (dataSource.equals("Invoice")) {
//							newprice = dmo
//									.getPriceLastInvoice(cmangids[i],
//											ccurrids[j],
//											isInTax.booleanValue(), strSQL);
//						} else if (dataSource.equals("Order")) {
//							newprice = dmo
//									.getPriceLastOrder(cmangids[i],
//											ccurrids[j],
//											isInTax.booleanValue(), strSQL);
//						}
                        if(resultForLastPrice.get(cmangids[i]+ccurrids[i]) != null ){
							newprice = new UFDouble(resultForLastPrice.get(cmangids[i]+ccurrids[i]).toString());
						}
						header.setLastprice(newprice);

						// ���ֵ����Сֵ��ƽ��ֵ
						ArrayList ary = null;
//						if (dataSource.equals("Invoice")) {
//							ary = dmo.getPricesForQuoteConVOInvoice(
//									cmangids[i], ccurrids[j], isInTax
//											.booleanValue(), strSQL);
//						} else if (dataSource.equals("Order")) {
//							ary = dmo
//									.getPricesForQuoteConVOOrder(cmangids[i],
//											ccurrids[j],
//											isInTax.booleanValue(), strSQL);
//						}
						if(resultForOtherPrice.get(cmangids[i]+ccurrids[i]) != null ){
							ary = (ArrayList)resultForOtherPrice.get(cmangids[i]+ccurrids[i]);
						}
						if (ary == null) {
							header.setMaxprice(null);
							header.setMinprice(null);
							header.setAvgprice(null);
						} else {
							header.setMaxprice((UFDouble) ary.get(0));
							header.setMinprice((UFDouble) ary.get(1));
							header.setAvgprice((UFDouble) ary.get(2));
						}
						// ����һ�ű���
						stockexec = new StockExecVO();
						// //��̬�п�����������
						// stockexec.setV_StruDataAllPKsForItems((Vector)aryitems.get(1));
						// //��̬�� key ֵ�ṹ��ϣ��
						// stockexec.setH_StruDataAllKey((Hashtable)aryitems.get(2));
						stockexec.setParentVO(header);
						stockexec.setChildrenVO(quoteconitems);
						v.addElement(stockexec);
					}

			}
			if (v.size() > 0) {
				stockexecs = new StockExecVO[v.size()];
				v.copyInto(stockexecs);
			}
		} catch (Exception e) {
			/* ���òɹ����÷������淶�׳��쳣 */
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryStockStatVOsMy", e);
		}

		return stockexecs;
	}

	/**
	 * ��ȡ��˾����
	 * @param pk_corps
	 */
	private void getCorpConditions(String[] pk_corps) throws BusinessException{
		String corpCon = null;
		if(pk_corps != null && pk_corps.length > 0){
			nc.bs.scm.pub.TempTableDMO dmoTmpTable = null;
			try {
				dmoTmpTable = new nc.bs.scm.pub.TempTableDMO();
				corpCon = dmoTmpTable.insertTempTable(
						pk_corps,
						nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_ASK_14,
						"pk_corp") ;
			} catch (SystemException e) {
				// TODO �Զ����� catch ��
				PubDMO.throwBusinessException(
						"nc.bs.pp.AskbillImpl.getCorpConditions", e);
			} catch (NamingException e) {
				// TODO �Զ����� catch ��
				PubDMO.throwBusinessException(
						"nc.bs.pp.AskbillImpl.getCorpConditions", e);
			} catch (Exception e) {
				// TODO �Զ����� catch ��
				PubDMO.throwBusinessException(
						"nc.bs.pp.AskbillImpl.getCorpConditions", e);
			}			
			
		}
	}

	/**
	 * ��ȡ ��Ӧ�̡�ҵ�����͡�ҵ��Ա������ִ�м۶ԱȲ�ѯ���� Ĭ�������� 1.״̬Լ���������ϡ����� 2.�Ǻ충�����Ǻ췢Ʊ(��������Ʊ���Ǹ�)
	 * 3.�������ڷǿա���Ʊ���ڷǿ� ˵���� 1.�����������ݿ��й�ʽ���ں�̨һ���Թ������ 2.�����Ļ������ݱ� bd_cubasdoc,
	 * bd_cumandoc, bd_invbasdoc, bd_invmandoc, bd_taxitems, bd_payterm,
	 * bd_deptdoc, bd_psndoc, bd_measdoc, bd_currtype bd_busitype 4.����� order by
	 * ��������DMO�д��� �������ڣ�(2001-09-10-18)
	 * 
	 * @return String strSQL ���ظ�ʽΪ��" from ... where ... "
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @param dataSource
	 *            String
	 * @param isInTax
	 *            boolean
	 * @param bDate
	 *            UFDate
	 * @param eDate
	 *            UFDate
	 * @exception BusinessException
	 *                �쳣˵����
	 * @throws BusinessException
	 */
	private String getSQLForPurExecVOsMy(ConditionVO[] conds, String pk_corp,String[] pk_corps,
			String dataSource, UFBoolean isInTax, UFDate bDate, UFDate eDate)
			throws BusinessException {
		String strSQL = "";
		if ("Order".equals(dataSource)) {
			strSQL = getSQLForPurExecVOsOrderMy(conds,pk_corp, pk_corps, isInTax, bDate,
					eDate);
		} else if ("Invoice".equals(dataSource)) {
			strSQL = getSQLForPurExecVOsInvoiceMy(conds, pk_corp,pk_corps, isInTax,
					bDate, eDate);
		}
		return strSQL;
	}

	/**
	 * ��ȡ ��Ӧ�̡�ҵ�����͡�ҵ��Ա������ִ�м۶ԱȲ�ѯ���� Ĭ�������� 1.״̬Լ���������ϡ����������޶�
	 * 2.�Ǻ충�����Ǻ췢Ʊ(��������Ʊ���Ǹ�) 3.�������ڷǿա���Ʊ���ڷǿ� ˵���� 1.�����������ݿ��й�ʽ���ں�̨һ���Թ������
	 * 2.�����Ļ������ݱ� bd_cubasdoc, bd_cumandoc, bd_invbasdoc, bd_invmandoc,
	 * bd_taxitems, bd_payterm, bd_deptdoc, bd_psndoc, bd_measdoc, bd_currtype
	 * bd_busitype 4.����� order by ��������DMO�д��� �������ڣ�(2001-09-10-18)
	 * 
	 * @return String strSQL ���ظ�ʽΪ��" from ... where ... "
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @param dataSource
	 *            String
	 * @param isInTax
	 *            boolean
	 * @param bDate
	 *            UFDate
	 * @param eDate
	 *            UFDate
	 * @exception BusinessException
	 *                �쳣˵����
	 * @throws BusinessException
	 */
	private String getSQLForPurExecVOsOrderMy(ConditionVO[] conds,
			 String pk_corp,String[] pk_corps, UFBoolean isInTax, UFDate bDate, UFDate eDate)
			throws BusinessException {
		String strSQL = " ";
		ArrayList listPowerVos = new ArrayList();
		StringBuffer pkConditions = getCorpsConditon(pk_corps);
		StringBuffer from = new StringBuffer(" from ");
		from.append("po_order_b ");
		StringBuffer where = new StringBuffer(" where (  ");
		where.append("  po_order.dr = 0 ");
		where.append(" and po_order_b.dr = 0 ");
		where
				.append(" and (po_order.forderstatus = 3 or po_order.forderstatus = 5) ");
		where.append(" and po_order_b.iisactive <> 3 ");
		Vector vTableName = new Vector();
		from
				.append("LEFT OUTER JOIN po_order  ON po_order_b.corderid = po_order.corderid ");
		vTableName.addElement("po_order");
		from
				.append("LEFT OUTER JOIN bd_cubasdoc ON po_order.cvendorbaseid = bd_cubasdoc.pk_cubasdoc ");
		vTableName.addElement("bd_cubasdoc");
//		from
//				.append("LEFT OUTER JOIN bd_cumandoc ON po_order.cvendormangid = bd_cumandoc.pk_cumandoc ");
//		vTableName.addElement("bd_cumandoc");
		from
				.append("LEFT OUTER JOIN bd_deptdoc ON po_order.cdeptid = bd_deptdoc.pk_deptdoc ");
		vTableName.addElement("bd_deptdoc");
		from
				.append("LEFT OUTER JOIN bd_psndoc ON po_order.cemployeeid = bd_psndoc.pk_psndoc ");
		vTableName.addElement("bd_psndoc");
		from
				.append("LEFT OUTER JOIN bd_busitype ON po_order.cbiztype = bd_busitype.pk_busitype ");
		vTableName.addElement("bd_busitype");
		// ���������Ҫ �μ� isContainCurrtype()
		// from.append( "LEFT OUTER JOIN bd_currtype ON
		// po_order_b.ccurrencytypeid = bd_currtype.pk_currtype ");
		// vTableName.addElement("bd_currtype");
		from
				.append("LEFT OUTER JOIN bd_invbasdoc ON po_order_b.cbaseid = bd_invbasdoc.pk_invbasdoc ");
		vTableName.addElement("bd_invbasdoc");
//		from
//				.append("LEFT OUTER JOIN bd_invmandoc ON po_order_b.cmangid = bd_invmandoc.pk_invmandoc ");
//		vTableName.addElement("bd_invmandoc");
		from
				.append("LEFT OUTER JOIN bd_measdoc ON bd_invbasdoc.pk_measdoc= bd_measdoc.pk_measdoc ");
		vTableName.addElement("bd_measdoc");
		from
				.append("LEFT OUTER JOIN bd_taxitems ON bd_invbasdoc.pk_taxitems = bd_taxitems.pk_taxitems ");
		vTableName.addElement("bd_taxitems");
		from
		.append("LEFT OUTER JOIN bd_corp ON bd_corp.pk_corp = po_order_b.pk_corp ");
        vTableName.addElement("bd_corp");

		if (conds != null) {
//			ArrayList ary = null;
			for (int i = 0; i < conds.length - 1; i++) {
				// �������(����) �μ� isContainCurrtype()
				 //����Ȩ������VO
				 if("IS".equalsIgnoreCase(conds[i].getOperaCode().trim()) && "NULL".equalsIgnoreCase(conds[i].getValue().trim())
						|| conds[i].getValue().trim().indexOf(VariableConst.PREFIX_OF_DATAPOWER_SQL) >= 0){
					listPowerVos.add(conds[i]);
				}
				 else if ("bd_currtype.pk_currtype".equals(conds[i].getFieldCode())) {
					if (!vTableName.contains("bd_currtype")) {
						from
								.append("LEFT OUTER JOIN bd_currtype ON po_order_b.ccurrencytypeid = bd_currtype.pk_currtype ");
						vTableName.addElement("bd_currtype");
					}
				}
                   //�ٴ����������
				 if(!("IS".equalsIgnoreCase(conds[i].getOperaCode().trim()) && "NULL".equalsIgnoreCase(conds[i].getValue().trim())
							|| conds[i].getValue().trim().indexOf(VariableConst.PREFIX_OF_DATAPOWER_SQL) >= 0)){
					conds[i].setFieldCode(nc.vo.pub.util.StringUtil
							.replaceAllString(conds[i].getFieldCode(),
									"po_askbill", "po_order"));
					where.append(getSQLForWhereOfAskbillForReport(conds[i], pk_corp,pk_corps));
				 }
                
			}
		}
		where.append(" )");
		if(listPowerVos.size() > 0){
			ConditionVO[] voaCondPower = new ConditionVO[listPowerVos.size()];
			listPowerVos.toArray(voaCondPower);
//			for(int i = 0 ; i < voaCondPower.length ; i ++){
//				voaCondPower[i].setNoLeft(false);
//				voaCondPower[i].setNoRight(false);
//			}
			String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);
			//
			where.append(" and (" + strPowerWherePart + ") ");
		}
		// ��˾Լ��
//		if (pk_corps != null && pk_corps.length > 0) {
//			// where.append(" and " +
//			// PuPubVO.getORWhereByValues("po_order_b.pk_corp",saQueryCorp)+ "
//			// ");
//		}
		// ״̬Լ��
		
		if(pkConditions != null && pkConditions.toString().trim().length() > 0){
			where.append(" and po_order.pk_corp in "+pkConditions.toString()+" ");
		}
		// �����Ǻ충��
		if (isInTax.booleanValue()) {
			// ԭ�Ҽ�˰�ϼƷǸ�
			where
					.append("and not( coalesce(po_order_b.noriginaltaxpricemny,0) < 0) ");
		} else {
			// ԭ�ҽ��Ǹ�
			where
					.append("and not( coalesce(po_order_b.noriginalcurmny,0) < 0) ");
		}
		// �������ڷǿ�
		where.append("and po_order.dorderdate is not null ");
		// ���������ڸ����ڼ���
		where.append("and po_order.dorderdate >= '" + bDate.toString() + "' ");
		where.append("and po_order.dorderdate <= '" + eDate.toString() + "' ");

		strSQL += from;
		strSQL += where;

		SCMEnv.out("��ѯ����Ϊ�� " + strSQL);

		return strSQL;
	}

	/**
	 * ��ȡ��˾����
	 * @param pk_corps
	 * @return
	 */
	private StringBuffer getCorpsConditon(String[] pk_corps) throws BusinessException  {
		StringBuffer pkConditions = new StringBuffer(" ");
		String corpCon = null;
		if(pk_corps != null && pk_corps.length > 0){
			nc.bs.scm.pub.TempTableDMO dmoTmpTable = null;
			try {
				dmoTmpTable = new nc.bs.scm.pub.TempTableDMO();
				corpCon = dmoTmpTable.insertTempTable(
						pk_corps,
						nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU_ASK_14,
						"pk_corp") ;
			} catch (SystemException e) {
				// TODO �Զ����� catch ��
				PubDMO.throwBusinessException(
						"nc.bs.pp.AskbillImpl.getCorpsConditon", e);
			} catch (NamingException e) {
				// TODO �Զ����� catch ��
				PubDMO.throwBusinessException(
						"nc.bs.pp.AskbillImpl.getCorpsConditon", e);
			} catch (Exception e) {
				// TODO �Զ����� catch ��
				PubDMO.throwBusinessException(
						"nc.bs.pp.AskbillImpl.getCorpsConditon", e);
			}			
			pkConditions.append(corpCon);
		}
		return pkConditions;
	}

	/**
	 * ��ȡ ��Ӧ�̡�ҵ�����͡�ҵ��Ա������ִ�м۶ԱȲ�ѯ���� Ĭ�������� 1.״̬Լ���������ϡ����� 2.�Ǻ충�����Ǻ췢Ʊ(��������Ʊ���Ǹ�)
	 * 3.�������ڷǿա���Ʊ���ڷǿ� ˵���� 1.�����������ݿ��й�ʽ���ں�̨һ���Թ������ 2.�����Ļ������ݱ� bd_cubasdoc,
	 * bd_cumandoc, bd_invbasdoc, bd_invmandoc, bd_taxitems, bd_payterm,
	 * bd_deptdoc, bd_psndoc, bd_measdoc, bd_currtype bd_busitype 4.����� order by
	 * ��������DMO�д��� �������ڣ�(2001-09-10-18)
	 * 
	 * @return String strSQL ���ظ�ʽΪ��" from ... where ... "
	 * @param conds
	 *            ConditionVO[]
	 * @param pk_corp
	 *            String
	 * @param dataSource
	 *            String
	 * @param isInTax
	 *            boolean
	 * @param bDate
	 *            UFDate
	 * @param eDate
	 *            UFDate
	 * @exception BusinessException
	 *                �쳣˵����
	 * @throws BusinessException
	 */
	private String getSQLForPurExecVOsInvoiceMy(ConditionVO[] conds,
			 String pk_corp, String[] pk_corps,UFBoolean isInTax, UFDate bDate, UFDate eDate)
			throws BusinessException {
		String strSQL = " ";
		ArrayList listPowerVos = new ArrayList();
		StringBuffer pkConditions = getCorpsConditon(pk_corps);
		StringBuffer from = new StringBuffer(" from ");
		from.append("po_invoice_b ");
		StringBuffer where = new StringBuffer(" where (  ");
		where.append("  po_invoice.dr = 0 ");
		where.append(" and po_invoice_b.dr = 0 ");
		where.append(" and po_invoice.ibillstatus = 3 ");
		Vector vTableName = new Vector();
		from
				.append("LEFT OUTER JOIN po_invoice  ON po_invoice_b.cinvoiceid = po_invoice.cinvoiceid ");
		vTableName.addElement("po_invoice");
		from
				.append("LEFT OUTER JOIN bd_cubasdoc ON po_invoice.cvendorbaseid = bd_cubasdoc.pk_cubasdoc ");
		vTableName.addElement("bd_cubasdoc");
//		from
//				.append("LEFT OUTER JOIN bd_cumandoc ON po_invoice.cvendormangid = bd_cumandoc.pk_cumandoc ");
//		vTableName.addElement("bd_cumandoc");
		from
				.append("LEFT OUTER JOIN bd_deptdoc ON po_invoice.cdeptid = bd_deptdoc.pk_deptdoc ");
		vTableName.addElement("bd_deptdoc");
		from
				.append("LEFT OUTER JOIN bd_psndoc ON po_invoice.cemployeeid = bd_psndoc.pk_psndoc ");
		vTableName.addElement("bd_psndoc");
		from
				.append("LEFT OUTER JOIN bd_busitype ON po_invoice.cbiztype = bd_busitype.pk_busitype ");
		vTableName.addElement("bd_busitype");
		// ���������Ҫ �μ� isContainCurrtype()
		// from.append( "LEFT OUTER JOIN bd_currtype ON
		// po_invoice_b.ccurrencytypeid = bd_currtype.pk_currtype ");
		// vTableName.addElement("bd_currtype");
		from
				.append("LEFT OUTER JOIN bd_invbasdoc ON po_invoice_b.cbaseid = bd_invbasdoc.pk_invbasdoc ");
		vTableName.addElement("bd_invbasdoc");
//		from
//				.append("LEFT OUTER JOIN bd_invmandoc ON po_invoice_b.cmangid = bd_invmandoc.pk_invmandoc ");
//		vTableName.addElement("bd_invmandoc");
		from
				.append("LEFT OUTER JOIN bd_measdoc ON bd_invbasdoc.pk_measdoc= bd_measdoc.pk_measdoc ");
		vTableName.addElement("bd_measdoc");
		from
				.append("LEFT OUTER JOIN bd_taxitems ON bd_invbasdoc.pk_taxitems = bd_taxitems.pk_taxitems ");
		vTableName.addElement("bd_taxitems");
		from
		.append("LEFT OUTER JOIN bd_corp ON bd_corp.pk_corp = po_invoice_b.pk_corp ");
        vTableName.addElement("bd_corp");

		if (conds != null) {
//			ArrayList ary = null;
			for (int i = 0; i < conds.length - 1; i++) {
				// �������(����) �μ� isContainCurrtype()
				 //����Ȩ������VO
				if("IS".equalsIgnoreCase(conds[i].getOperaCode().trim()) && "NULL".equalsIgnoreCase(conds[i].getValue().trim())
						|| conds[i].getValue().trim().indexOf(VariableConst.PREFIX_OF_DATAPOWER_SQL) >= 0){
					listPowerVos.add(conds[i]);
				}
				if ("bd_currtype.pk_currtype".equals(conds[i].getFieldCode())) {
					if (!vTableName.contains("bd_currtype")) {
						from
								.append("LEFT OUTER JOIN bd_currtype ON po_invoice_b.ccurrencytypeid = bd_currtype.pk_currtype ");
						vTableName.addElement("bd_currtype");
					}
				}
				// �ٴ����������
				conds[i].setFieldCode(nc.vo.pub.util.StringUtil
						.replaceAllString(conds[i].getFieldCode(),
								"po_askbill", "po_invoice"));
				if("bd_deptdoc.deptcode".equals(conds[i].getFieldCode()) 
						|| "bd_psndoc.psncode".equals(conds[i].getFieldCode())
						|| "bd_invbasdoc.invcode".equals(conds[i].getFieldCode())
						|| "bd_cubasdoc.custcode".equals(conds[i].getFieldCode())){
				if(!("IS".equalsIgnoreCase(conds[i].getOperaCode().trim()) && "NULL".equalsIgnoreCase(conds[i].getValue().trim())
						|| conds[i].getValue().trim().indexOf(VariableConst.PREFIX_OF_DATAPOWER_SQL) >= 0)  && 
						conds[i].getValue().trim().length() > 0 && !"null".equalsIgnoreCase(conds[i].getValue().trim()) && !conds[i].getValue().trim().startsWith("(select")
		            	){
				         where.append(getSQLForWhereOfAskbillForReport(conds[i], pk_corp,pk_corps));
 				        }
				} else{
					where.append(getSQLForWhereOfAskbillForReport(conds[i], pk_corp,pk_corps));
				}             
			}
		}
		where.append(" )");
		if(listPowerVos.size() > 0){
			ConditionVO[] voaCondPower = new ConditionVO[listPowerVos.size()];
			listPowerVos.toArray(voaCondPower);
//			for(int i = 0 ; i < voaCondPower.length ; i ++){
//				voaCondPower[i].setNoLeft(false);
//				voaCondPower[i].setNoRight(false);
//			}
			String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);
			//
			where.append(" and (" + strPowerWherePart + ") ");
		}
		// ��˾Լ��
//		if (pk_corp != null && pk_corp.trim().length() > 0) {
//			where.append(" and po_invoice.pk_corp = '");
//			where.append(pk_corp + "' ");
//		}
		// ״̬Լ��
		
		if(pkConditions != null && pkConditions.toString().trim().length() > 0){
			where.append(" and po_invoice.pk_corp in "+pkConditions.toString()+" ");
		}
		// ��Ʊ�Ǻ췢Ʊ
		if (isInTax.booleanValue()) {
			// ԭ�Ҽ�˰�ϼƷǸ�
			where
					.append("and not( coalesce(po_invoice_b.noriginalsummny,0) < 0) ");
		} else {
			// ԭ�ҽ��Ǹ�
			where
					.append("and not( coalesce(po_invoice_b.noriginalcurmny,0) < 0) ");
		}
		// ��Ʊ���ڷǿ�
		where.append("and po_invoice.dinvoicedate is not null ");
		// ��Ʊ�����ڸ����ڼ���
		where.append("and po_invoice.dinvoicedate >= '" + bDate.toString()
				+ "' ");
		where.append("and po_invoice.dinvoicedate <= '" + eDate.toString()
				+ "' ");

		strSQL += from;
		strSQL += where;

		SCMEnv.out("��ѯ����Ϊ�� " + strSQL);

		return strSQL;
	}
	/**
	 * @���ܣ���ѯѯ�۵���ϸ
	 * @˵����
			 1.������ֶ�
	 * @param  conds    ConditionVO[]
	 * @param  pk_corp  String
	 * @param  status   boolean[]
	 */
	public AskbillMergeVO queryDetailVOMy(String strSQL,String strSQLForFree,UFBoolean[] status) throws BusinessException{

		AskbillMergeVO detail = null;
		AskbillHeaderVO header = null;
		AskbillItemMergeVO[] askbillitems = null;
		AskbillItemMergeVO[] askbillitemsForFree = null;
//		String sOperator = null;
//		String ss= null;
		Vector ResultV = new Vector();
		AskbillItemMergeVO[] result = null;
//		String SQLItems = " and (1=1) ";
		try {
			AskbillDMO dmo = new AskbillDMO();
//			String strSQL = "";
			if(status[1].booleanValue() || status[2].booleanValue() || status[3].booleanValue()){
//			  strSQL = getSQLForDetailVOMy(conds, pk_corp,status);
			  askbillitems = dmo.findAskItemsForDetailVOMy(strSQL);
			}
//			String strSQLForFree = "";
			if(status[0].booleanValue()){
//				strSQLForFree = getSQLForDetailVOMyForFree(conds, pk_corp,status);
				askbillitemsForFree = dmo.findAskItemsForDetailVOMyForFree(strSQLForFree);
			}
			if ((askbillitems == null || askbillitems.length <= 0) && (askbillitemsForFree == null || askbillitemsForFree.length <= 0)) return null;
			if(askbillitems != null && askbillitems.length > 0){
			  for(int i = 0 ; i < askbillitems.length ; i ++){
				ResultV.add(askbillitems[i]);
		    	}
			}
			if(askbillitemsForFree != null && askbillitemsForFree.length > 0){
			  for(int i = 0 ; i < askbillitemsForFree.length ; i ++){
				ResultV.add(askbillitemsForFree[i]);
			  	}
			}
			if(ResultV.size() > 0){
				result = new AskbillItemMergeVO[ResultV.size()];
				ResultV.copyInto(result);
			}
			//�õ�����VO
			detail = new AskbillMergeVO();
			header = new AskbillHeaderVO();
			detail.setParentVO(header);
			detail.setChildrenVO(result);
			//����������
			//detail = (AskbillVO) getVosWithFreeDealed(new AggregatedValueObject[]{detail},"cmangid","vfree0")[0];

		} catch (Exception e) {
			/*���òɹ����÷������淶�׳��쳣*/
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryDetailVOMy", e);
		}

		return detail;
	}
	public AskbillMergeVO queryDetailVOMy(ConditionVO[] conds, String pk_corp, UFBoolean[] status) throws BusinessException{

		AskbillMergeVO detail = null;
		AskbillHeaderVO header = null;
		AskbillItemMergeVO[] askbillitems = null;
		AskbillItemMergeVO[] askbillitemsForFree = null;
//		String sOperator = null;
//		String ss= null;
		Vector ResultV = new Vector();
		AskbillItemMergeVO[] result = null;
//		String SQLItems = " and (1=1) ";
		try {
			AskbillDMO dmo = new AskbillDMO();
			String strSQL = "";
			if(status[1].booleanValue() || status[2].booleanValue() || status[3].booleanValue()){
			  strSQL = getSQLForDetailVOMy(conds, pk_corp,status);
			  askbillitems = dmo.findAskItemsForDetailVOMy(strSQL);
			}
			String strSQLForFree = "";
			if(status[0].booleanValue()){
				strSQLForFree = getSQLForDetailVOMyForFree(conds, pk_corp,status);
				askbillitemsForFree = dmo.findAskItemsForDetailVOMyForFree(strSQLForFree);
			}
			
			 //����Ȩ�޿���
//			if(conds[conds.length - 1].getFieldName().equals("����Ա")){
//				 sOperator = conds[conds.length - 1].getValue(); 
//				 ss = nc.bs.scm.datapower.ScmDps.getSubSql("po_askbill","po_askbill",sOperator,new String[]{pk_corp});
//				if(ss != null && ss.trim().length() > 0) strSQL += " and " + ss + " ";
//				 ss = nc.bs.scm.datapower.ScmDps.getSubSql("po_askbill_b","po_askbill_b",sOperator,new String[]{pk_corp});
//					if(ss != null && ss.trim().length() > 0) strSQL += " and " + ss + " ";
//			}	
			
			
			if ((askbillitems == null || askbillitems.length <= 0) && (askbillitemsForFree == null || askbillitemsForFree.length <= 0)) return null;
			if(askbillitems != null && askbillitems.length > 0){
			  for(int i = 0 ; i < askbillitems.length ; i ++){
				ResultV.add(askbillitems[i]);
		    	}
			}
			if(askbillitemsForFree != null && askbillitemsForFree.length > 0){
			  for(int i = 0 ; i < askbillitemsForFree.length ; i ++){
				ResultV.add(askbillitemsForFree[i]);
			  	}
			}
			if(ResultV.size() > 0){
				result = new AskbillItemMergeVO[ResultV.size()];
				ResultV.copyInto(result);
			}
			//�õ�����VO
			detail = new AskbillMergeVO();
			header = new AskbillHeaderVO();
			detail.setParentVO(header);
			detail.setChildrenVO(result);
			//����������
			//detail = (AskbillVO) getVosWithFreeDealed(new AggregatedValueObject[]{detail},"cmangid","vfree0")[0];

		} catch (Exception e) {
			/*���òɹ����÷������淶�׳��쳣*/
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryDetailVOMy", e);
		}

		return detail;
	}
	/**
	 * ��ȡ ѯ����ϸ��ѯ�ۻ��� ��ѯ����  ״̬Լ����������
	 * ˵����
			1.�����������ݿ��й�ʽ���ں�̨һ���Թ������
			2.�����������ñ�����
			  #ѯ���� sm_user1.user_name
			  #������ sm_user2.user_name
			3.�����Ļ������ݱ�
			  bd_cubasdoc,  bd_cumandoc,
			  bd_invbasdoc, bd_invmandoc,
			  bd_taxitems,  bd_payterm,
			  bd_deptdoc,   bd_psndoc,
			  bd_measdoc,   bd_currtype,
			  sm_user as sm_user1,
			  sm_user as sm_user2
	 * �������ڣ�(2001-09-10-18)
	 * @return String strSQL ���ظ�ʽΪ��" from ... where ... "
	 * @param conds ConditionVO[]
	 * @param pk_corp String
	 * @param status  boolean[]
	 * @exception BusinessException �쳣˵����
	 */
	private String getSQLForDetailVOMy(
		ConditionVO[] conds,
		String pk_corp,
		UFBoolean[] status)
		throws BusinessException {

		String strSQL = " ";
		ArrayList listPowerVos = new ArrayList();
		StringBuffer from = new StringBuffer(" from ");
		from.append("po_askbill ");

		//��"("Ŀ���ǰѹ�˾�����ŵ������
		//��ͬ�� where (...) and pk_corp = ... and (״̬Լ��,ҵ��Լ����...)
		//�Ӷ���֤����ֻ��pk_corp �����˾��
		StringBuffer where = new StringBuffer(" where (  ");
		where.append(" po_askbill_b.dr = 0 ");
		where.append(" and po_askbill.dr = 0 ");
        //״̬Լ��
		if( ( status[1].booleanValue() || status[2].booleanValue() || status[3].booleanValue())){
		   where.append("and po_askbill_bb1.dr = 0 ");
		}
		Vector vTableName = new Vector();
		from.append(
			"inner JOIN po_askbill_b  ON po_askbill_b.caskbillid = po_askbill.caskbillid ");
		vTableName.addElement("po_askbill_b");
		from.append(
		"left outer JOIN po_askbill_bb1  ON po_askbill_b.caskbillid = po_askbill_bb1.caskbillid and po_askbill_b.caskbill_bid = po_askbill_bb1.caskbill_bid ");
	    vTableName.addElement("po_askbill_bb1");
		from.append(
			"LEFT OUTER JOIN bd_cubasdoc ON po_askbill_bb1.cvendorbaseid = bd_cubasdoc.pk_cubasdoc ");
		vTableName.addElement("bd_cubasdoc");
		from.append(
			"LEFT OUTER JOIN bd_cumandoc ON po_askbill_bb1.cvendormangid = bd_cumandoc.pk_cumandoc ");
		vTableName.addElement("bd_cumandoc");
		from.append(
			"LEFT OUTER JOIN bd_deptdoc ON po_askbill.cdeptid = bd_deptdoc.pk_deptdoc ");
		vTableName.addElement("bd_deptdoc");
		from.append(
			"LEFT OUTER JOIN bd_psndoc ON po_askbill.cemployeeid = bd_psndoc.pk_psndoc ");
		vTableName.addElement("bd_psndoc");
		from.append(
			"LEFT OUTER JOIN bd_currtype ON po_askbill.ccurrencytypeid = bd_currtype.pk_currtype ");
		vTableName.addElement("bd_currtype");
		from.append(
			"LEFT OUTER JOIN bd_invbasdoc ON po_askbill_b.cbaseid = bd_invbasdoc.pk_invbasdoc ");
		vTableName.addElement("bd_invbasdoc");
		from.append(
			"LEFT OUTER JOIN bd_invmandoc ON po_askbill_b.cmangid = bd_invmandoc.pk_invmandoc ");
		vTableName.addElement("bd_invmandoc");
		from.append(
			"LEFT OUTER JOIN bd_measdoc ON bd_invbasdoc.pk_measdoc= bd_measdoc.pk_measdoc ");
		vTableName.addElement("bd_measdoc");
		from.append(
			"LEFT OUTER JOIN bd_taxitems ON bd_invbasdoc.pk_taxitems = bd_taxitems.pk_taxitems ");
		vTableName.addElement("bd_taxitems");
		from.append(
			"LEFT OUTER JOIN sm_user sm_user1 ON po_askbill.caskpsn = sm_user1.cUserId ");
		vTableName.addElement("sm_user1");
		from.append(
			"LEFT OUTER JOIN sm_user sm_user2 ON po_askbill.cquotepsn = sm_user2.cUserId ");
		vTableName.addElement("sm_user2");
		from.append(
			"LEFT OUTER JOIN bd_payterm ON po_askbill.ctermprotocolid = bd_payterm.pk_payterm ");
		vTableName.addElement("bd_payterm");

		if (conds != null) {
//			ArrayList ary = null;
			for (int i = 0; i < conds.length-1; i++) {
				//ary = new ArrayList();
				//ary = getSQLForFromOfAskbill(conds[i],from,vTableName) ;
				////�ȴ������
				//from.append( (String) ary.get(0));
				//vTableName = (Vector) ary.get(1);

				//�ٴ����������

				//ѯ�۵�״̬(ֱ�Ӵ���)
//				if (conds[i].getFieldCode().equals("po_askbill.ibillstatus")) {
//					if (conds[i].getValue() != null) {
//						if (conds[i].getValue().equals("����")) {
//							where.append((conds[i].getLogic() ? " and " : " or "));
//							where.append( (!conds[i].getNoLeft() ? "(" : ""));
//							where.append( " po_askbill.ibillstatus ");
//							where.append( conds[i].getOperaCode());
//							where.append( IAskBillStatus.FREE);
//							where.append( (!conds[i].getNoRight() ? ") " : " "));
//						} else
//							if (conds[i].getValue().equals("����")) {
//								where.append((conds[i].getLogic() ? " and " : " or "));
//								where.append( (!conds[i].getNoLeft() ? "(" : ""));
//								where.append( " po_askbill.ibillstatus ");
//								where.append( conds[i].getOperaCode());
//								where.append( IAskBillStatus.SENDING);
//								where.append( (!conds[i].getNoRight() ? ") " : " "));
//							} else
//								if (conds[i].getValue().equals("����")) {
//									where.append((conds[i].getLogic() ? " and " : " or "));
//									where.append( (!conds[i].getNoLeft() ? "(" : ""));
//									where.append( " po_askbill.ibillstatus ");
//									where.append( conds[i].getOperaCode());
//									where.append( IAskBillStatus.QUOTED);
//									where.append( (!conds[i].getNoRight() ? ") " : " "));
//								} else
//									if (conds[i].getValue().equals("���")) {
//										where.append((conds[i].getLogic() ? " and " : " or "));
//										where.append( (!conds[i].getNoLeft() ? "(" : ""));
//										where.append( " po_askbill.ibillstatus ");
//										where.append( conds[i].getOperaCode());
//										where.append( IAskBillStatus.CONFIRM);
//										where.append( (!conds[i].getNoRight() ? ") " : " "));
//									}
//					}
//				}
                //����Ȩ������VO
				if("IS".equalsIgnoreCase(conds[i].getOperaCode().trim()) && "NULL".equalsIgnoreCase(conds[i].getValue().trim())
								|| conds[i].getValue().trim().indexOf(VariableConst.PREFIX_OF_DATAPOWER_SQL) >= 0){
							listPowerVos.add(conds[i]);
						} 
				else {
					if (
						("bd_cubasdoc.custcode".equals(conds[i].getFieldCode()) && conds[i].getValue() != null && conds[i].getValue().trim().length() > 0 && !"null".equalsIgnoreCase(conds[i].getValue().trim()) && !conds[i].getValue().trim().startsWith("(select"))
						||("bd_deptdoc.deptcode".equals(conds[i].getFieldCode()) && conds[i].getValue() != null && conds[i].getValue().trim().length() > 0 && !"null".equalsIgnoreCase(conds[i].getValue().trim()) && !conds[i].getValue().trim().startsWith("(select"))
						||("bd_psndoc.psncode".equals(conds[i].getFieldCode()) && conds[i].getValue() != null && conds[i].getValue().trim().length() > 0 && !"null".equalsIgnoreCase(conds[i].getValue().trim()) && !conds[i].getValue().trim().startsWith("(select"))
						||("bd_invbasdoc.invcode".equals(conds[i].getFieldCode()) && conds[i].getValue() != null && conds[i].getValue().trim().length() > 0 && !"null".equalsIgnoreCase(conds[i].getValue().trim()) && !conds[i].getValue().trim().startsWith("(select"))
					   ) {
                         where.append(getSQLForWhereOfAskbill(conds[i], pk_corp));
					}else{
						where.append(getSQLForWhereOfAskbill(conds[i], pk_corp));
					}
				}
                 
			}
		}
		where.append(" )");
		if(listPowerVos.size() > 0){
			ConditionVO[] voaCondPower = new ConditionVO[listPowerVos.size()];
			listPowerVos.toArray(voaCondPower);
			String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);
			//
			where.append(" and (" + strPowerWherePart + ") ");
		}
		//��˾Լ��
		if (pk_corp != null && pk_corp.trim().length() > 0) {
			where.append(" and po_askbill.pk_corp = '");
			where.append(pk_corp);
			where.append("' ");
		}
		
		
		//���û��ѡ��״̬����Ϊ��Ĭ�ϵ�����״̬
//		if (where.toString().indexOf("po_askbill.ibillstatus") < 0) {
//			where.append("and ( po_askbill.ibillstatus = ");
//			where.append(IAskBillStatus.FREE);
//			where.append( " ");
//			where.append("or po_askbill.ibillstatus = ");
//			where.append(IAskBillStatus.SENDING);
//			where.append(" ");
//			where.append("or po_askbill.ibillstatus = ");
//			where.append(IAskBillStatus.QUOTED);
//			where.append(" ");
//			where.append("or po_askbill.ibillstatus = ");
//			where.append(IAskBillStatus.CONFIRM);
//			where.append( " ");
//			where.append(" ) ");
//		}
		//����������
		where.append("and ( ");
//		if (status[0].booleanValue()) {
//			where.append("po_askbill.ibillstatus = " + IAskBillStatus.FREE + " or ");
//		}
		if (status[1].booleanValue()) {
			where.append("po_askbill.ibillstatus = " + IAskBillStatus.SENDING + " or ");
		}
		if (status[2].booleanValue()) {
			where.append("po_askbill.ibillstatus = " + IAskBillStatus.QUOTED + " or ");
		}
		if (status[3].booleanValue()) {
			where.append("po_askbill.ibillstatus = " + IAskBillStatus.CONFIRM + " or ");
		}
		if ("or".equals(where.substring(where.toString().length() - 3, where.toString().length()).trim()))
			where = new StringBuffer(where.toString().substring(0, where.toString().length() - 3));
		where.append(") ");

		strSQL += from;
		strSQL += where;

		SCMEnv.out("��ѯ����Ϊ�� " + strSQL);

		return strSQL;
	}
	/**
	 * ��ȡ ѯ����ϸ��ѯ�ۻ��� ��ѯ����  ״̬Լ����������
	 * ˵����
			1.�����������ݿ��й�ʽ���ں�̨һ���Թ������
			2.�����������ñ�����
			  #ѯ���� sm_user1.user_name
			  #������ sm_user2.user_name
			3.�����Ļ������ݱ�
			  bd_cubasdoc,  bd_cumandoc,
			  bd_invbasdoc, bd_invmandoc,
			  bd_taxitems,  bd_payterm,
			  bd_deptdoc,   bd_psndoc,
			  bd_measdoc,   bd_currtype,
			  sm_user as sm_user1,
			  sm_user as sm_user2
	 * �������ڣ�(2001-09-10-18)
	 * @return String strSQL ���ظ�ʽΪ��" from ... where ... "
	 * @param conds ConditionVO[]
	 * @param pk_corp String
	 * @param status  boolean[]
	 * @exception BusinessException �쳣˵����
	 */
	private String getSQLForDetailVOMyForFree(
		ConditionVO[] conds,
		String pk_corp,
		UFBoolean[] status)
		throws BusinessException {

		String strSQL = " ";
		ArrayList listPowerVos = new ArrayList();
		StringBuffer from = new StringBuffer(" from ");
		from.append("po_askbill ");

		//��"("Ŀ���ǰѹ�˾�����ŵ������
		//��ͬ�� where (...) and pk_corp = ... and (״̬Լ��,ҵ��Լ����...)
		//�Ӷ���֤����ֻ��pk_corp �����˾��
		StringBuffer where = new StringBuffer(" where (  ");
		where.append(" po_askbill_b.dr = 0 ");
		where.append(" and po_askbill.dr = 0 ");
        //״̬Լ��
//		if( ( status[1].booleanValue() == true || status[2].booleanValue() == true || status[3].booleanValue() == true)){
//		   where.append("and po_askbill_bb1.dr = 0 ");
//		}
		Vector vTableName = new Vector();
		from.append(
			"inner JOIN po_askbill_b  ON po_askbill_b.caskbillid = po_askbill.caskbillid ");
		vTableName.addElement("po_askbill_b");
//		from.append(
//		"left outer JOIN po_askbill_bb1  ON po_askbill_b.caskbillid = po_askbill_bb1.caskbillid and po_askbill_b.caskbill_bid = po_askbill_bb1.caskbill_bid ");
//	    vTableName.addElement("po_askbill_bb1");
//		from.append(
//			"LEFT OUTER JOIN bd_cubasdoc ON po_askbill_bb1.cvendorbaseid = bd_cubasdoc.pk_cubasdoc ");
//		vTableName.addElement("bd_cubasdoc");
//		from.append(
//			"LEFT OUTER JOIN bd_cumandoc ON po_askbill_bb1.cvendormangid = bd_cumandoc.pk_cumandoc ");
//		vTableName.addElement("bd_cumandoc");
		from.append(
			"LEFT OUTER JOIN bd_deptdoc ON po_askbill.cdeptid = bd_deptdoc.pk_deptdoc ");
		vTableName.addElement("bd_deptdoc");
		from.append(
			"LEFT OUTER JOIN bd_psndoc ON po_askbill.cemployeeid = bd_psndoc.pk_psndoc ");
		vTableName.addElement("bd_psndoc");
		from.append(
			"LEFT OUTER JOIN bd_currtype ON po_askbill.ccurrencytypeid = bd_currtype.pk_currtype ");
		vTableName.addElement("bd_currtype");
		from.append(
			"LEFT OUTER JOIN bd_invbasdoc ON po_askbill_b.cbaseid = bd_invbasdoc.pk_invbasdoc ");
		vTableName.addElement("bd_invbasdoc");
		from.append(
			"LEFT OUTER JOIN bd_invmandoc ON po_askbill_b.cmangid = bd_invmandoc.pk_invmandoc ");
		vTableName.addElement("bd_invmandoc");
		from.append(
			"LEFT OUTER JOIN bd_measdoc ON bd_invbasdoc.pk_measdoc= bd_measdoc.pk_measdoc ");
		vTableName.addElement("bd_measdoc");
		from.append(
			"LEFT OUTER JOIN bd_taxitems ON bd_invbasdoc.pk_taxitems = bd_taxitems.pk_taxitems ");
		vTableName.addElement("bd_taxitems");
		from.append(
			"LEFT OUTER JOIN sm_user sm_user1 ON po_askbill.caskpsn = sm_user1.cUserId ");
		vTableName.addElement("sm_user1");
		from.append(
			"LEFT OUTER JOIN sm_user sm_user2 ON po_askbill.cquotepsn = sm_user2.cUserId ");
		vTableName.addElement("sm_user2");
		from.append(
			"LEFT OUTER JOIN bd_payterm ON po_askbill.ctermprotocolid = bd_payterm.pk_payterm ");
		vTableName.addElement("bd_payterm");

		if (conds != null) {
//			ArrayList ary = null;
			for (int i = 0; i < conds.length-1; i++) {
				//ary = new ArrayList();
				//ary = getSQLForFromOfAskbill(conds[i],from,vTableName) ;
				////�ȴ������
				//from.append( (String) ary.get(0));
				//vTableName = (Vector) ary.get(1);

				//�ٴ����������

				//ѯ�۵�״̬(ֱ�Ӵ���)
//				if (conds[i].getFieldCode().equals("po_askbill.ibillstatus")) {
//					if (conds[i].getValue() != null) {
//						if (conds[i].getValue().equals("����")) {
//							where.append((conds[i].getLogic() ? " and " : " or "));
//							where.append( (!conds[i].getNoLeft() ? "(" : ""));
//							where.append( " po_askbill.ibillstatus ");
//							where.append( conds[i].getOperaCode());
//							where.append( IAskBillStatus.FREE);
//							where.append( (!conds[i].getNoRight() ? ") " : " "));
//						} 
////						else
////							if (conds[i].getValue().equals("����")) {
////								where.append((conds[i].getLogic() ? " and " : " or "));
////								where.append( (!conds[i].getNoLeft() ? "(" : ""));
////								where.append( " po_askbill.ibillstatus ");
////								where.append( conds[i].getOperaCode());
////								where.append( IAskBillStatus.SENDING);
////								where.append( (!conds[i].getNoRight() ? ") " : " "));
////							} else
////								if (conds[i].getValue().equals("����")) {
////									where.append((conds[i].getLogic() ? " and " : " or "));
////									where.append( (!conds[i].getNoLeft() ? "(" : ""));
////									where.append( " po_askbill.ibillstatus ");
////									where.append( conds[i].getOperaCode());
////									where.append( IAskBillStatus.QUOTED);
////									where.append( (!conds[i].getNoRight() ? ") " : " "));
////								} else
////									if (conds[i].getValue().equals("���")) {
////										where.append((conds[i].getLogic() ? " and " : " or "));
////										where.append( (!conds[i].getNoLeft() ? "(" : ""));
////										where.append( " po_askbill.ibillstatus ");
////										where.append( conds[i].getOperaCode());
////										where.append( IAskBillStatus.CONFIRM);
////										where.append( (!conds[i].getNoRight() ? ") " : " "));
////									}
//					}
//				}
			  //������ڹ�Ӧ�̼�����Ȩ�ޣ��������˴�ѭ�������ٲ�ѯ�����м�������Ϊ�˲�ѯû�й�Ӧ����Ϣ��
			  if("bd_cubasdoc.custcode".equals(conds[i].getFieldCode())){
			    continue;
			  }
                //����Ȩ������VO
				if("IS".equalsIgnoreCase(conds[i].getOperaCode().trim()) && "NULL".equalsIgnoreCase(conds[i].getValue().trim())
								|| conds[i].getValue().trim().indexOf(VariableConst.PREFIX_OF_DATAPOWER_SQL) >= 0){
							listPowerVos.add(conds[i]);
						} 
				else {
					if (
						//(conds[i].getFieldCode().equals("bd_cubasdoc.custcode") && conds[i].getValue() != null && conds[i].getValue().toString().trim().length() > 0 && !conds[i].getValue().toString().trim().equalsIgnoreCase("null") && !conds[i].getValue().toString().trim().startsWith("(select"))
						("bd_deptdoc.deptcode".equals(conds[i].getFieldCode()) && conds[i].getValue() != null && conds[i].getValue().trim().length() > 0 && !"null".equalsIgnoreCase(conds[i].getValue().trim()) && !conds[i].getValue().trim().startsWith("(select"))
						||("bd_psndoc.psncode".equals(conds[i].getFieldCode()) && conds[i].getValue() != null && conds[i].getValue().trim().length() > 0 && !"null".equalsIgnoreCase(conds[i].getValue().trim()) && !conds[i].getValue().trim().startsWith("(select"))
						||("bd_invbasdoc.invcode".equals(conds[i].getFieldCode()) && conds[i].getValue() != null && conds[i].getValue().trim().length() > 0 && !"null".equalsIgnoreCase(conds[i].getValue().trim()) && !conds[i].getValue().trim().startsWith("(select"))
					   ) {
                         where.append(getSQLForWhereOfAskbill(conds[i], pk_corp));
					}else{
						where.append(getSQLForWhereOfAskbill(conds[i], pk_corp));
					}
				}
                 
			}
		}
		where.append(" )");
		if(listPowerVos.size() > 0){
			ConditionVO[] voaCondPower = new ConditionVO[listPowerVos.size()];
			listPowerVos.toArray(voaCondPower);
			String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);
			//
			where.append(" and (" + strPowerWherePart + ") ");
		}
		//��˾Լ��
		if (pk_corp != null && pk_corp.trim().length() > 0) {
			where.append(" and po_askbill.pk_corp = '");
			where.append(pk_corp);
			where.append("' ");
		}
		
		
		//���û��ѡ��״̬����Ϊ��Ĭ�ϵ�����״̬
//		if (where.toString().indexOf("po_askbill.ibillstatus") < 0) {
//			where.append("and ( po_askbill.ibillstatus = ");
//			where.append(IAskBillStatus.FREE);
//			where.append( " ");
//			where.append("or po_askbill.ibillstatus = ");
//			where.append(IAskBillStatus.SENDING);
//			where.append(" ");
//			where.append("or po_askbill.ibillstatus = ");
//			where.append(IAskBillStatus.QUOTED);
//			where.append(" ");
//			where.append("or po_askbill.ibillstatus = ");
//			where.append(IAskBillStatus.CONFIRM);
//			where.append( " ");
//			where.append(" ) ");
//		}
		//����������
		where.append("and ( ");
		if (status[0].booleanValue()) {
			where.append("po_askbill.ibillstatus = " + IAskBillStatus.FREE + " or ");
		}
//		if (status[1].booleanValue()) {
//			where.append("po_askbill.ibillstatus = " + IAskBillStatus.SENDING + " or ");
//		}
//		if (status[2].booleanValue()) {
//			where.append("po_askbill.ibillstatus = " + IAskBillStatus.QUOTED + " or ");
//		}
//		if (status[3].booleanValue()) {
//			where.append("po_askbill.ibillstatus = " + IAskBillStatus.CONFIRM + " or ");
//		}
		if ("or".equals(where.substring(where.toString().length() - 3, where.toString().length()).trim()))
			where = new StringBuffer(where.toString().substring(0, where.toString().length() - 3));
		where.append(") ");

		strSQL += from;
		strSQL += where;

		SCMEnv.out("��ѯ����Ϊ�� " + strSQL);

		return strSQL;
	}
	/**
	 * @���ܣ���ѯѯ�۵�ͳ�ƻ���VO
	 */
	public AskbillMergeVO queryStatisVOMy(
		ConditionVO[] conds,
		String pk_corp,
		UFBoolean[] status,
		String[] groups,
		String priceType)
		throws BusinessException {

		AskbillMergeVO statis = null;
		AskbillHeaderVO header = null;
		AskbillItemMergeVO[] askbillitems = null;
		AskbillItemMergeVO[] askbillitemsForFree = null;
		AskbillItemMergeVO[] result = null;
		String sOperator = null;
		String ss= null;
		Vector ResultV = new Vector();
		try {
			AskbillDMO dmo = new AskbillDMO();
			String strSQL = "";
			 //����Ȩ�޿���
			if("����Ա".equals(conds[conds.length - 1].getFieldName())){
				 sOperator = conds[conds.length - 1].getValue(); 
				 ss = nc.bs.scm.datapower.ScmDps.getSubSql("po_askbill","po_askbill",sOperator,new String[]{pk_corp});
				if(ss != null && ss.trim().length() > 0) strSQL += " and " + ss + " ";
				 ss = nc.bs.scm.datapower.ScmDps.getSubSql("po_askbill_b","po_askbill_b",sOperator,new String[]{pk_corp});
					if(ss != null && ss.trim().length() > 0) strSQL += " and " + ss + " ";
			}	
			//strSQL += " and nnotaxprice is not null and nnotaxprice <> 0 ";
			//strSQL += " and nintaxprice is not null and nintaxprice <> 0 ";
			if(status[1].booleanValue() || status[2].booleanValue() || status[3].booleanValue()){
				
				  strSQL = getSQLForDetailVOMy(conds, pk_corp,status);
				  askbillitems = dmo.findAskItemsForStatisVOMy(strSQL, groups, priceType);
				}
				String strSQLForFree = "";
				if(status[0].booleanValue()){
					strSQLForFree = getSQLForDetailVOMyForFree(conds, pk_corp,status);
					askbillitemsForFree = dmo.findAskItemsForStatisVOMyForFree(strSQLForFree, groups, priceType);
				}
			if ((askbillitems == null || askbillitems.length <= 0) && (askbillitemsForFree == null || askbillitemsForFree.length <= 0)) return null;
			if(askbillitems != null && askbillitems.length > 0){
			  for(int i = 0 ; i < askbillitems.length ; i ++){
				ResultV.add(askbillitems[i]);
		    	}
			}
			if(askbillitemsForFree != null && askbillitemsForFree.length > 0){
			  for(int i = 0 ; i < askbillitemsForFree.length ; i ++){
				ResultV.add(askbillitemsForFree[i]);
			  	}
			}
			if(ResultV.size() > 0){
				result = new AskbillItemMergeVO[ResultV.size()];
				ResultV.copyInto(result);
			}
			//�õ�����VO
			statis = new AskbillMergeVO();
			header = new AskbillHeaderVO();
			statis.setParentVO(header);
			statis.setChildrenVO(result);

		} catch (Exception e) {
			/*���òɹ����÷������淶�׳��쳣*/
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryStatisVOMy", e);
		}

		return statis;
	}
	/**
	 * @���ܣ���Ӧ�̱��۶Ա�VO[]
	 * @˼·��
	 		1.���ݲ�ѯ�������˳���ͬ���ID   x �� [po_askbill_b.cmangid]
	 		2.���ݲ�ѯ�������˳���ͬ�ı���ID y �� [po_askbill.ccurrencytypeid]
	 		3.������ѭ����ȡ��������Ϣ��¼ x*y ��
	 * @����
	 		0.���ִ���
	 		  ����ֻ��Ϊ��ѯ����
			1.��ѯ���������Ĵ��������ID
			2.����ID��ȡ��Ӧ�Ĵ����Ϣ
			  #�������ƹ���ͺ�
			  #������
			  #������
			  #�ο��ɱ�
			  #�ƻ���
			  #���¼�
			3.���������ID + ��ѯ����(������) -> ��Ӧ�̱��۶Աȱ���VO[] (���ڼ�ѭ��)
			4.�á����������ID+����ID������ͳ��->��߼ۡ���ͼۡ�ƽ����
	 * @return quotecons QuoteConVO[]
	 * @param  paravo    QuoteConParaVO
	 */
	public QuoteConVO[] queryQuoteConVOsMy(StatParaVO paravo) throws BusinessException {

		ConditionVO[] conds = paravo.getConds();
		String pk_corp = paravo.getPk_corp();
		String[] pk_corps = paravo.getPk_corps();
		UFBoolean[] status = paravo.getStatus();
		UFBoolean isInTax = paravo.getIsInTax();
		String valueType = paravo.getValueType();
		UFDate[] pDates = paravo.getPeriods();
		String linkSign = paravo.getLinkSign();
		Integer periodType = paravo.getPeriodType();
		QuoteConVO[] quotecons = null;
		QuoteConVO quotecon = null;
		QuoteConHeaderVO header = null;
		QuoteConItemVO[] quoteconitems = null;
		String[] cmangids = null;
		String[] cbaseids = null;
		String[] ccurrids = null;
		Vector v = new Vector();
		ArrayList aryInvIDs = null;
		ArrayList aryInvInfos = null;
		ArrayList aryPrice = null;
		String currtypename = null;
		String pk_currtype = null;
//		String ss=null;
//		String sOperator = null;
		try {
			AskbillDMO dmo = new AskbillDMO();
			String strSQL = "";
			UFDate bDate = pDates[0];
			UFDate eDate = pDates[pDates.length - 1];
			strSQL = getSQLForStockVarVOsMy(conds, pk_corp, pk_corps, status, isInTax, bDate, eDate);
			 //����Ȩ�޿���
//			if(conds[conds.length - 1].getFieldName().equals("����Ա")){
//				 sOperator = conds[conds.length - 1].getValue(); 
//				 ss = nc.bs.scm.datapower.ScmDps.getSubSql("po_askbill","po_askbill",sOperator,new String[]{pk_corp});
//				if(ss != null && ss.trim().length() > 0) strSQL += " and " + ss + " ";
//				 ss = nc.bs.scm.datapower.ScmDps.getSubSql("po_askbill_b","po_askbill_b",sOperator,new String[]{pk_corp});
//					if(ss != null && ss.trim().length() > 0) strSQL += " and " + ss + " ";
//			}	
			//��ͬ�Ĵ��
			aryInvIDs = dmo.findInvIDsForStockVarVOMy(strSQL,"4004070301");
			if (aryInvIDs == null)
				return null;
			if (aryInvIDs.get(0) == null)
				return null;
			if (aryInvIDs.get(1) == null)
				return null;
			cmangids = (String[]) aryInvIDs.get(0);
			cbaseids = (String[]) aryInvIDs.get(1);
			//��ͬ�ı���
			ccurrids = dmo.findCurrIDsForStockVarVOsMy(strSQL,"4004070301");
			if (ccurrids == null)
				return null;
			if (ccurrids.length <= 0)
				return null;
//			UFDate periodFrom = null;
//			UFDate periodTo = null;
			ArrayList aryitems = null;
			for (int i = 0; i < cmangids.length; i++) {
				//��������
				for (int j = 0; j < ccurrids.length; j++) {
					quoteconitems = null;
					aryitems =
						dmo.findItemsForQuoteConVOMy(valueType, isInTax.booleanValue(), cmangids[i], ccurrids[j], strSQL, pDates, linkSign, periodType.intValue());
					if (aryitems != null) {
						quoteconitems = (QuoteConItemVO[]) aryitems.get(0);
						//���ñ�ͷ
						header = new QuoteConHeaderVO();
						//����
						pk_currtype = ccurrids[j];
						currtypename = dmo.getCurrtypenameByPk(pk_currtype);
						if (pk_currtype == null || "".equals(pk_currtype.trim())) {
							SCMEnv.out("ȡ����ʱ����[nc.bs.pp.ask.AskbillBO.queryStockVarVOsMy(...)]");
							throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000005")/*@res "ȡ����ʱ����[nc.bs.pp.ask.AskbillBO.queryStockVarVOsMy(...)]"*/);
						}
						header.setCurrtypename(currtypename);
						header.setPk_currtype(pk_currtype);

						//�������
						aryInvInfos = dmo.getInvInfosByPk(cbaseids[i]);
						if (aryInvInfos == null) {
							header.setInvcode(null);
							header.setInvname(null);
							header.setInvspec(null);
							header.setInvtype(null);
						} else {
							header.setInvcode((String) aryInvInfos.get(0));
							header.setInvname((String) aryInvInfos.get(1));
							header.setInvspec((String) aryInvInfos.get(2));
							header.setInvtype((String) aryInvInfos.get(3));
						}
						//������
						if (aryInvInfos == null || aryInvInfos.get(4) == null) {
							header.setMeasname(null);
						} else {
							String pk_measdoc = (String) aryInvInfos.get(4);
							String measname = null;
							measname = dmo.getMeasnameByPk(pk_measdoc);
							header.setMeasname(measname);
						}
						//˰��
						if (aryInvInfos == null || aryInvInfos.get(5) == null) {
							header.setTaxratio(new UFDouble(0));
						} else {
							String pk_taxitems = (String) aryInvInfos.get(5);
							UFDouble taxratio = null;
							taxratio = dmo.getTaxratioByPk(pk_taxitems);
							if (taxratio == null) {
								header.setTaxratio(new UFDouble(0));
							} else {
								header.setTaxratio(taxratio);
							}
						}
						//���ñ�β

						//�ο��ɱ����ƻ���
						aryPrice = dmo.getCostPlanPriceForThanVO(cmangids[i]);
						if (aryPrice == null) {
							header.setCostprice(null);
							header.setPlanprice(null);
						} else {
							header.setCostprice((UFDouble) aryPrice.get(0));
							header.setPlanprice((UFDouble) aryPrice.get(1));
						}
						//���¼�
						UFDouble newprice = null;
						newprice = dmo.getPriceLastQuoted(cmangids[i], ccurrids[j], isInTax, strSQL);
						header.setLastprice(newprice);

						//���ֵ����Сֵ��ƽ��ֵ
						ArrayList ary = dmo.getPricesForQuoteConVO(cmangids[i], ccurrids[j], isInTax.booleanValue(), strSQL);
						header.setMaxprice((UFDouble) ary.get(0));
						header.setMinprice((UFDouble) ary.get(1));
						header.setAvgprice((UFDouble) ary.get(2));

						//����һ�ű���
						quotecon = new QuoteConVO();
						//��̬�п�����������
						quotecon.setV_StruDataAllPKsForItems((Vector) aryitems.get(1));
						//��̬�� key ֵ�ṹ��ϣ��
						quotecon.setH_StruDataAllKey((Hashtable) aryitems.get(2));
						quotecon.setParentVO(header);
						quotecon.setChildrenVO(quoteconitems);
						v.addElement(quotecon);
					}
				}
			}
			if (v.size() > 0) {
				quotecons = new QuoteConVO[v.size()];
				v.copyInto(quotecons);
			}
		} catch (Exception e) {
			/*���òɹ����÷������淶�׳��쳣*/
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryQuoteConVOsMy", e);
		}

		return quotecons;
	}
	/**
	 * ��ȡ ������۱䶯���� ��ѯ����
	 * Ĭ��������
			1.״̬Լ����������
			2.���۲�Ϊ��,���ݽ���Ҫ��
			  #������ʾ��˰��������˵���˰����Ϊ�յļ�¼
			  #������ʾ��˰��������˵���˰����Ϊ�յļ�¼
			3.ѯ�������շ�����
			4.�������ڲ�Ϊ��
	 * ˵����
			1.�����������ݿ��й�ʽ���ں�̨һ���Թ������
			2.�����������ñ�����
			  #ѯ���� sm_user1.user_name
			  #������ sm_user2.user_name
			3.�����Ļ������ݱ�
			  bd_cubasdoc,  bd_cumandoc,
			  bd_invbasdoc, bd_invmandoc,
			  bd_taxitems,  bd_payterm,
			  bd_deptdoc,   bd_psndoc,
			  bd_measdoc,   bd_currtype,
			  sm_user as sm_user1,
			  sm_user as sm_user2
			4.order by ��������DMO�д���
			  order by po_askbill.dquotedate
	 * �������ڣ�(2001-09-10-18)
	 * @return String strSQL ���ظ�ʽΪ��" from ... where ... "
	 * @param conds ConditionVO[]
	 * @param pk_corp String
	 * @param status  boolean[]
	 * @param isInTax boolean
	 * @exception BusinessException �쳣˵����
	 */
	private String getSQLForStockVarVOsMy(ConditionVO[] conds, String pk_corp, String[] pk_corps,  UFBoolean[] status, UFBoolean isInTax, UFDate bDate, UFDate eDate) throws BusinessException {

		String strSQL  = " ";
		ArrayList listPowerVos = new ArrayList();
		StringBuffer pkConditions = getCorpsConditon(pk_corps);
		StringBuffer from = new StringBuffer(" from ");
		from.append( "po_askbill ");

		StringBuffer where = new StringBuffer( " where (  ");
        //״̬Լ��
		where.append( " po_askbill_bb1.dr = 0 ");
		where.append( " and po_askbill_b.dr = 0 ");
		where.append( " and po_askbill.dr = 0 ");
		Vector vTableName = new Vector();
		from.append( "inner JOIN po_askbill_b  ON po_askbill_b.caskbillid = po_askbill.caskbillid ");
		vTableName.addElement("po_askbill_b");
		from.append( "left outer  JOIN po_askbill_bb1  ON po_askbill_b.caskbillid = po_askbill_bb1.caskbillid and  po_askbill_b.caskbill_bid = po_askbill_bb1.caskbill_bid ");
		vTableName.addElement("po_askbill_b");
		from.append( "LEFT OUTER JOIN bd_cubasdoc ON po_askbill_bb1.cvendorbaseid = bd_cubasdoc.pk_cubasdoc ");
		vTableName.addElement("bd_cubasdoc");
//		from.append( "LEFT OUTER JOIN bd_cumandoc ON po_askbill_bb1.cvendormangid = bd_cumandoc.pk_cumandoc ");
//		vTableName.addElement("bd_cumandoc");
		from.append( "LEFT OUTER JOIN bd_deptdoc ON po_askbill.cdeptid = bd_deptdoc.pk_deptdoc ");
		vTableName.addElement("bd_deptdoc");
		from.append( "LEFT OUTER JOIN bd_psndoc ON po_askbill.cemployeeid = bd_psndoc.pk_psndoc ");
		vTableName.addElement("bd_psndoc");
		//���������Ҫ �μ� isContainCurrtype()
		//from.append( "LEFT OUTER JOIN bd_currtype ON po_askbill.ccurrencytypeid = bd_currtype.pk_currtype ");
		//vTableName.addElement("bd_currtype");
		from.append( "LEFT OUTER JOIN bd_invbasdoc ON po_askbill_b.cbaseid = bd_invbasdoc.pk_invbasdoc ");
		vTableName.addElement("bd_invbasdoc");
//		from.append( "LEFT OUTER JOIN bd_invmandoc ON po_askbill_b.cmangid = bd_invmandoc.pk_invmandoc ");
//		vTableName.addElement("bd_invmandoc");
		from.append( "LEFT OUTER JOIN bd_measdoc ON bd_invbasdoc.pk_measdoc= bd_measdoc.pk_measdoc ");
		vTableName.addElement("bd_measdoc");
		from.append( "LEFT OUTER JOIN bd_taxitems ON bd_invbasdoc.pk_taxitems = bd_taxitems.pk_taxitems ");
		vTableName.addElement("bd_taxitems");
		from.append( "LEFT OUTER JOIN sm_user sm_user1 ON po_askbill.caskpsn = sm_user1.cUserId ");
		vTableName.addElement("sm_user1");
		from.append( "LEFT OUTER JOIN sm_user sm_user2 ON po_askbill.cquotepsn = sm_user2.cUserId ");
		vTableName.addElement("sm_user2");
		from.append( "LEFT OUTER JOIN bd_payterm ON po_askbill.ctermprotocolid = bd_payterm.pk_payterm ");
		vTableName.addElement("bd_payterm");
		from.append( "LEFT OUTER JOIN bd_corp ON po_askbill.pk_corp = bd_corp.pk_corp ");
		vTableName.addElement("bd_payterm");

		if (conds != null) {
			ArrayList ary = null;
			for (int i = 0; i < conds.length-1; i++){
				//������� �μ� isContainCurrtype()
				ary = new ArrayList();
				ary = getSQLForFromOfAskbill(conds[i],from.toString(),vTableName) ;
				//�ȴ������
				from.append( (String) ary.get(0));
				vTableName = (Vector) ary.get(1);

				//�ٴ����������
                //����Ȩ������VO
				 if("IS".equalsIgnoreCase(conds[i].getOperaCode().trim()) && "NULL".equalsIgnoreCase(conds[i].getValue().trim())
						|| conds[i].getValue().trim().indexOf(VariableConst.PREFIX_OF_DATAPOWER_SQL) >= 0){
					listPowerVos.add(conds[i]);
				}
				//״̬����
				 else if ("po_askbill.ibillstatus".equals(conds[i].getFieldCode())) {
					if (conds[i].getValue() != null){
						if ("����".equals(conds[i].getValue())) {
							where.append(  (conds[i].getLogic()?" and " : " or ") + (!conds[i].getNoLeft() ? "(" :"") + " po_askbill.ibillstatus " + conds[i].getOperaCode() + IAskBillStatus.QUOTED + (!conds[i].getNoRight() ? ") " :" "));
						}else
						if ("���".equals(conds[i].getValue())) {
							where.append(  (conds[i].getLogic()?" and " : " or ") + (!conds[i].getNoLeft() ? "(" :"") + " po_askbill.ibillstatus " + conds[i].getOperaCode() + IAskBillStatus.CONFIRM + (!conds[i].getNoRight() ? ") " :" "));
						}
					}
				}else{
						   where.append( getSQLForWhereOfAskbillForReport(conds[i], pk_corp, pk_corps));
				
				}
                  
			}
		}
		where.append( " )");
		if(listPowerVos.size() > 0){
			ConditionVO[] voaCondPower = new ConditionVO[listPowerVos.size()];
			listPowerVos.toArray(voaCondPower);
//			for(int i = 0 ; i < voaCondPower.length ; i ++){
//				voaCondPower[i].setNoLeft(false);
//				voaCondPower[i].setNoRight(false);
//			}
			String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);
			//
			where.append(" and (" + strPowerWherePart + ") ");
		}
		//��˾Լ��
//		if (pk_corp != null && pk_corp.trim().length() > 0) {
//			where.append( " and po_askbill.pk_corp = '");
//			where.append(   pk_corp + "' ");
//		}
		
		if(pkConditions != null && pkConditions.toString().trim().length() > 0){
			where.append(" and po_askbill.pk_corp in "+pkConditions.toString()+" ");
		}
		//���û��ѡ��״̬����Ϊ��Ĭ�ϵ�����״̬
		if (where.toString().indexOf("po_askbill.ibillstatus") < 0){
			where.append( "and ( po_askbill.ibillstatus = " + IAskBillStatus.QUOTED +" ");
			where.append( "or po_askbill.ibillstatus = " + IAskBillStatus.CONFIRM +" ");
			where.append(  "   ) ");
		}
		//����������
		where.append( "and ( ");
		if (status[0].booleanValue()){
			where.append( "po_askbill.ibillstatus = " + IAskBillStatus.QUOTED + " or ");
		}
		if (status[1].booleanValue()){
			where.append( "po_askbill.ibillstatus = " + IAskBillStatus.CONFIRM + " or ");
		}
		if ("or".equals(where.substring(where.length() -3 , where.length()).trim()))
		    where = new StringBuffer(where.toString().substring(0,where.toString().length() - 3));
		where.append( ") ");
		//���˱���Ϊ��
		if (isInTax.booleanValue()){
			where.append( "and po_askbill_bb1.nquotetaxprice is not null ");
		}else{
			where.append( "and po_askbill_bb1.nquoteprice is not null ");
		}
        //��Ӧ�̲�Ϊ��
		where.append( "and po_askbill_bb1.cvendorbaseid is not null  ");
		//�������ڲ�Ϊ��
		where.append( "and po_askbill.dquotedate is not null ");
		//��������
		where.append( "and po_askbill.dquotedate >= '"+ bDate.toString() +"' ");
		where.append( "and po_askbill.dquotedate <= '"+ eDate.toString() +"' ");

		strSQL += from;
		strSQL += where;


		SCMEnv.out("��ѯ����Ϊ�� " + strSQL);

		return strSQL;
	}
	/**
	 * @���ܣ���Ӧ�̡�ҵ�����͡�ҵ��Ա�����ű��۶Ա�VO[]
	 * @˼·��
	 		1.���ݲ�ѯ�������˳���ͬ���ID   x �� [po_askbill_b.cmangid]
	 		2.���ݲ�ѯ�������˳���ͬ�ı���ID y �� [po_askbill.ccurrencytypeid]
	 		3.������ѭ����ȡ��������Ϣ��¼ x*y ��
	 * @����
	 		0.���ִ���
	 		  ����ֻ��Ϊ��ѯ����
			1.��ѯ���������Ĵ��������ID
			2.����ID��ȡ��Ӧ�Ĵ����Ϣ
			  #�������ƹ���ͺ�
			  #������
			  #������
			  #�ο��ɱ�
			  #�ƻ���
			  #���¼�
			3.���������ID + ��ѯ����(������) -> ��Ӧ�̡�ҵ�����͡�ҵ��Ա�����ű��۶Աȱ���VO[] (���ڼ�ѭ��)
			4.�á����������ID+����ID������ͳ��->��߼ۡ���ͼۡ�ƽ����
	 * @return quotecons QuoteConVO[]
	 * @param  paravo    QuoteConParaVO
	 */
	public QuoteConVO[] queryPurExecVOsMy(StatParaVO paravo) throws BusinessException {

		ConditionVO[] conds = paravo.getConds();
		String pk_corp = paravo.getPk_corp();
		String[] pk_corps = paravo.getPk_corps();
//		UFBoolean[] status = paravo.getStatus();
		UFBoolean isInTax = paravo.getIsInTax();
		Integer statType = paravo.getStatType();
		String dataSource = paravo.getDataSource();
		UFDate[] pDates = paravo.getPeriods();
		String linkSign = paravo.getLinkSign();
		Integer periodType = paravo.getPeriodType();
		QuoteConVO[] quotecons = null;
		QuoteConVO quotecon = null;
		QuoteConHeaderVO header = null;
		QuoteConItemVO[] quoteconitems = null;
		String[] cmangids = null;
		String[] cbaseids = null;
		String[] ccurrids = null;
		Vector v = new Vector();
		ArrayList aryInvIDs = null;
		ArrayList aryInvInfos = null;
//		ArrayList aryPrice = null;
		String currtypename = null;
		String pk_currtype = null;
		String ss=null;
		String sOperator = null;
		try {
			AskbillDMO dmo = new AskbillDMO();
			String strSQL = "";
			UFDate bDate = pDates[0];
			UFDate eDate = pDates[pDates.length - 1];
			strSQL = getSQLForPurExecVOsMy(conds, pk_corp,pk_corps, dataSource, isInTax, bDate, eDate);
			 //����Ȩ�޿���
			if("����Ա".equals(conds[conds.length - 1].getFieldName())){
				 sOperator = conds[conds.length - 1].getValue(); 
				 if ("Invoice".equals(dataSource)) {
				 ss = nc.bs.scm.datapower.ScmDps.getSubSql("po_order","po_order",sOperator,new String[]{pk_corp});
				if(ss != null && ss.trim().length() > 0) strSQL += " and " + ss + " ";
				 ss = nc.bs.scm.datapower.ScmDps.getSubSql("po_order_b","po_order_b",sOperator,new String[]{pk_corp});
					if(ss != null && ss.trim().length() > 0) strSQL += " and " + ss + " ";
				 }
				 else{
				 	ss = nc.bs.scm.datapower.ScmDps.getSubSql("po_invoice","po_invoice",sOperator,new String[]{pk_corp});
					if(ss != null && ss.trim().length() > 0) strSQL += " and " + ss + " ";
				 ss = nc.bs.scm.datapower.ScmDps.getSubSql("po_invoice_b","po_invoice_b",sOperator,new String[]{pk_corp});
					if(ss != null && ss.trim().length() > 0) strSQL += " and " + ss + " ";
				 }
			}	
			//��ͬ�Ĵ��
			if ("Invoice".equals(dataSource)) {
				aryInvIDs = dmo.findInvIDsForPurExecVOMyInvoiceForBantch(strSQL,"4004070303");
			} else {
				aryInvIDs = dmo.findInvIDsForPurExecVOMyOrderForBantch(strSQL,"4004070303");
			}
			if (aryInvIDs == null)
				return null;
			if (aryInvIDs.get(0) == null)
				return null;
			if (aryInvIDs.get(1) == null)
				return null;
			if (aryInvIDs.get(2) == null)
				return null;
			cmangids = (String[]) aryInvIDs.get(0);
			cbaseids = (String[]) aryInvIDs.get(1);
			ccurrids = (String[]) aryInvIDs.get(2);
			//Ч���޸�--������ѯ
			Hashtable resultForInv = dmo.getInvInfosByPkForBantch(cbaseids,"4004070303");
			
			//��ͬ�ı���
//			if (dataSource.equals("Invoice")) {
//				ccurrids = dmo.findCurrIDsForPurExecVOsMyInvoice(strSQL);
//			} else {
//				ccurrids = dmo.findCurrIDsForPurExecVOsMyOrder(strSQL);
//			}
			if (ccurrids == null)
				return null;
			if (ccurrids.length <= 0)
				return null;
			
            //Ч���޸�--������ѯ
			Hashtable resultForCurtype = dmo.getCurrtypenameByPkForBantch(ccurrids,"4004070303");
			Hashtable resultForLastPrice = new Hashtable();
			if ("Invoice".equals(dataSource)) {
				resultForLastPrice = dmo.getPriceLastInvoiceForBantch(cmangids, ccurrids, isInTax.booleanValue(), strSQL,"4004070303");
			} else if ("Order".equals(dataSource)) {
				resultForLastPrice = dmo.getPriceLastOrderForBantch(cmangids, ccurrids, isInTax.booleanValue(), strSQL,"4004070303");
			}
			Hashtable resultForOtherPrice = new Hashtable();
			if ("Invoice".equals(dataSource)) {
				resultForOtherPrice = dmo.getPricesForQuoteConVOInvoiceForBantch(cmangids, ccurrids, isInTax.booleanValue(), strSQL,"4004070303");
			} else if ("Order".equals(dataSource)) {
				resultForOtherPrice = dmo.getPricesForQuoteConVOOrderForBantch(cmangids, ccurrids, isInTax.booleanValue(), strSQL,"4004070303");
			}
//			UFDate periodFrom = null;
//			UFDate periodTo = null;
			ArrayList aryitems = null;
			UFDouble taxratio = null;
			for (int i = 0; i < cmangids.length; i++) {
				//��������
//				for (int j = 0; j < ccurrids.length; j++) {
					quoteconitems = null;
					aryitems =
						dmo.findItemsForPurExecVOMy(
							statType.intValue(),
							dataSource,
							isInTax.booleanValue(),
							cmangids[i],
							ccurrids[i],
							strSQL,
							pDates,
							linkSign,
							periodType.intValue());
					if (aryitems != null) {
						quoteconitems = (QuoteConItemVO[]) aryitems.get(0);
						//���ñ�ͷ
						header = new QuoteConHeaderVO();
						//����
						pk_currtype = ccurrids[i];
//						currtypename = dmo.getCurrtypenameByPk(pk_currtype);
						if(resultForCurtype.get(ccurrids[i]) != null){
							currtypename = (String)resultForCurtype.get(ccurrids[i]);
						}
						if (pk_currtype == null || "".equals(pk_currtype.trim())) {
							SCMEnv.out("ȡ����ʱ����[nc.bs.pp.ask.AskbillBO.queryStockVarVOsMy(...)]");
							throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000005")/*@res "ȡ����ʱ����[nc.bs.pp.ask.AskbillBO.queryStockVarVOsMy(...)]"*/);
						}
						header.setCurrtypename(currtypename);
						header.setPk_currtype(pk_currtype);

						//�������
						if(resultForInv.get(cbaseids[i]) != null){
						aryInvInfos = (ArrayList)resultForInv.get(cbaseids[i]);
						}
						if (aryInvInfos == null) {
							header.setInvcode(null);
							header.setInvname(null);
							header.setInvspec(null);
							header.setInvtype(null);
							header.setMeasname(null);
							
						} else {
							header.setInvcode((String) aryInvInfos.get(0));
							header.setInvname((String) aryInvInfos.get(1));
							header.setInvspec((String) aryInvInfos.get(2));
							header.setInvtype((String) aryInvInfos.get(3));
							header.setMeasname((String) aryInvInfos.get(6));
						}
						//������
//						if (aryInvInfos == null || aryInvInfos.get(4) == null) {
//							header.setMeasname(null);
//						} else {
//							String pk_measdoc = (String) aryInvInfos.get(4);
//							String measname = null;
//							measname = dmo.getMeasnameByPk(pk_measdoc);
//							header.setMeasname(measname);
//						}
//						String measname = (String) aryInvInfos.get(6);
//						header.setMeasname((String) aryInvInfos.get(6));
						//˰��
//						if (aryInvInfos == null || aryInvInfos.get(5) == null) {
//							header.setTaxratio(new UFDouble(0));
//						} else {
//							String pk_taxitems = (String) aryInvInfos.get(5);
//							UFDouble taxratio = null;
//							taxratio = dmo.getTaxratioByPk(pk_taxitems);
//							if (taxratio == null) {
//								header.setTaxratio(new UFDouble(0));
//							} else {
//								header.setTaxratio(taxratio);
//							}
//						}
						if(aryInvInfos != null && aryInvInfos.get(7) != null && aryInvInfos.get(7).toString().trim().length() > 0){
							taxratio = new UFDouble(aryInvInfos.get(7).toString());
							header.setTaxratio(taxratio);
						}else{
							header.setTaxratio(new UFDouble(0));
						}
						//���ñ�β

						//�ο��ɱ����ƻ���
//						aryPrice = dmo.getCostPlanPriceForThanVO(cmangids[i]);
						if (aryInvInfos != null && aryInvInfos.get(8) == null) {
							header.setCostprice(null);
						}
						if (aryInvInfos != null && aryInvInfos.get(9) == null) {
							header.setPlanprice(null);
						} 
                        if(aryInvInfos != null && aryInvInfos.get(8) != null && aryInvInfos.get(8).toString().trim().length() > 0
                        		){
							header.setCostprice(new UFDouble( aryInvInfos.get(8).toString()));
                        }
                        if(aryInvInfos != null && aryInvInfos.get(9) != null && aryInvInfos.get(9).toString().trim().length() > 0){
							header.setPlanprice(new UFDouble(aryInvInfos.get(9).toString()));
						}
						//���¼�
						UFDouble newprice = null;
						SCMEnv.out("���¼�#############################");
//						if (dataSource.equals("Invoice")) {
//							newprice = dmo.getPriceLastInvoice(cmangids[i], ccurrids[j], isInTax.booleanValue(), strSQL);
//						} else if (dataSource.equals("Order")) {
//							newprice = dmo.getPriceLastOrder(cmangids[i], ccurrids[j], isInTax.booleanValue(), strSQL);
//						}
						if(resultForLastPrice.get(cmangids[i]+ccurrids[i]) != null ){
							newprice = new UFDouble(resultForLastPrice.get(cmangids[i]+ccurrids[i]).toString());
						}
						header.setLastprice(newprice);

						//���ֵ����Сֵ��ƽ��ֵ
						SCMEnv.out("���ֵ����Сֵ��ƽ��ֵ#############################");
						ArrayList ary = null;
//						if (dataSource.equals("Invoice")) {
//							ary = dmo.getPricesForQuoteConVOInvoice(cmangids[i], ccurrids[j], isInTax.booleanValue(), strSQL);
//						} else if (dataSource.equals("Order")) {
//							ary = dmo.getPricesForQuoteConVOOrder(cmangids[i], ccurrids[j], isInTax.booleanValue(), strSQL);
//						}
						if(resultForOtherPrice.get(cmangids[i]+ccurrids[i]) != null ){
							ary = (ArrayList)resultForOtherPrice.get(cmangids[i]+ccurrids[i]);
						}
						if (ary == null) {
							header.setMaxprice(null);
							header.setMinprice(null);
							header.setAvgprice(null);
						} else {
							header.setMaxprice((UFDouble) ary.get(0));
							header.setMinprice((UFDouble) ary.get(1));
							header.setAvgprice((UFDouble) ary.get(2));
						}

						//����һ�ű���
						quotecon = new QuoteConVO();
						//��̬�п�����������
						quotecon.setV_StruDataAllPKsForItems((Vector) aryitems.get(1));
						//��̬�� key ֵ�ṹ��ϣ��
						quotecon.setH_StruDataAllKey((Hashtable) aryitems.get(2));
						quotecon.setParentVO(header);
						quotecon.setChildrenVO(quoteconitems);
						v.addElement(quotecon);
					}
//				}
			}
			if (v.size() > 0) {
				quotecons = new QuoteConVO[v.size()];
				v.copyInto(quotecons);
			}
		} catch (Exception e) {
			/*���òɹ����÷������淶�׳��쳣*/
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryPurExecVOsMy", e);
		}

		return quotecons;
	}
	/**
	 * ��ȡָ����������VO�Ĳ�ѯ���(SQL����е� "where ...")
	 * �������ڣ�(2001-10-23 9:21:33)
	 * @return java.lang.String
	 * @param cond nc.vo.pub.query.ConditionVO
	 * @param from java.lang.String
	 * @param vTableName Vector
	 */
//	private String getSQLForWhereOfAskbill(ConditionVO cond, String pk_corp) throws BusinessException {
//
//		StringBuffer whereNew = new StringBuffer(" ");
//		try {
//
//			/*���⴦��*/
//
//			/*ѯ���˱���*/
//			if (cond.getFieldCode().equals("sm_user.user_code1")) {
//				cond.setFieldCode("sm_user1.user_code");
//			}
//			/*�����˱���*/
//			if (cond.getFieldCode().equals("sm_user.user_code2")) {
//				cond.setFieldCode("sm_user2.user_code");
//			}
//			/*���̵����������*/
//			if (cond.getFieldCode().equals("bd_areacl.areaclcode") && cond.getValue() != null && !cond.getValue().trim().equals("")) {
//				/*ˢ������VO��ֵ*/
//				String strAreaclCodeSet = "";
//				strAreaclCodeSet = getAreaClassCodeSet(cond, pk_corp);
//				cond.setValue(strAreaclCodeSet);
//				/*ˢ������VO�Ĳ�����*/
//				cond.setOperaCode("in");
//			}
//			/*����������*/
//			if (cond.getFieldCode().equals("bd_invcl.invclasscode") && cond.getValue() != null && !cond.getValue().trim().equals("")) {
//				/*ˢ������VO��ֵ*/
//				String strInvClassCodeSet = "";
//				strInvClassCodeSet = getInvClassCodeSet(cond, pk_corp);
//				cond.setValue(strInvClassCodeSet);
//				/*ˢ������VO�Ĳ�����*/
//				cond.setOperaCode("in");
//			}
//
//			/*ͳһ����*/
//
//			whereNew.append(getWhereByFieldCondVOMy(cond.getFieldCode(), cond, new UFBoolean(false)));
//
//		} catch (Exception e) {
//			SCMEnv.out("��ȡָ������������VO�Ĳ�ѯ���ʱ����,���ܵ��²�ѯ�������ȷ");
//			/* ���òɹ����÷������淶�׳��쳣 */
//			PubDMO.throwBusinessException(
//					"nc.bs.pp.AskbillImpl.delete(InvoiceVO)", e);
//		}
//		return whereNew.toString();
//	}
	/**
	 * @���ܣ���ѯ������۱䶯VO[]
	 * @˼·��
	 		1.���ݲ�ѯ�������˳���ͬ���ID   x �� [po_askbill_b.cmangid]
	 		2.���ݲ�ѯ�������˳���ͬ�ı���ID y �� [po_askbill.ccurrencytypeid]
	 		3.������ѭ����ȡ��������Ϣ��¼ x*y ��
	 * @����
	 		0.���ִ���
	 		  ����ֻ��Ϊ��ѯ����
			1.��ѯ���������Ĵ��������ID
			2.����ID��ȡ��Ӧ�Ĵ����Ϣ
			  #�������ƹ���ͺ�
			  #������
			  #������
			  #�ο��ɱ�
			  #�ƻ���
			  #���¼�
			3.���������ID + ��ѯ���� -> ������۱䶯����VO[]
			4.ƽ���ۡ���߼ۡ���ͼ���UI����
	 * @return stockvar StockVarVO
	 * @param  conds    ConditionVO[]
	 * @param  pk_corp  String
	 * @param  status   boolean[]
	 */
	public StockVarVO[] queryStockVarVOsMy(
			StatParaVO paravo)
		throws BusinessException {
		ConditionVO[] conds = paravo.getConds();
		String pk_corp = paravo.getPk_corp();
		String[] pk_corps = paravo.getPk_corps();
		UFBoolean[] status = paravo.getStatus();
		UFBoolean isInTax = paravo.getIsInTax();
		UFDate bDate = paravo.getBeginDate();
		UFDate eDate = paravo.getEndDate();
		StockVarVO[] stockvars = null;
		StockVarVO stockvar = null;
		StockVarHeaderVO header = null;
		StockVarItemVO[] stockvaritems = null;
		String[] cmangids = null;
		String[] cbaseids = null;
		String[] ccurrids = null;
		Vector v = new Vector();
		ArrayList aryInvIDs = null;
		ArrayList aryInvInfos = null;
		ArrayList aryPrice = null;
		String currtypename = null;
		String pk_currtype = null;
		try {
			AskbillDMO dmo = new AskbillDMO();
			String strSQL = "";
			strSQL = getSQLForStockVarVOsMy(conds, pk_corp, pk_corps, status, isInTax, bDate, eDate);
			//��ͬ�Ĵ��
			aryInvIDs = dmo.findInvIDsForStockVarVOMy(strSQL,"4004070302");
			if (aryInvIDs == null)
				return null;
			if (aryInvIDs.get(0) == null)
				return null;
			if (aryInvIDs.get(1) == null)
				return null;
			cmangids = (String[]) aryInvIDs.get(0);
			cbaseids = (String[]) aryInvIDs.get(1);
			//��ͬ�ı���
			ccurrids = dmo.findCurrIDsForStockVarVOsMy(strSQL,"4004070302");
			if (ccurrids == null)
				return null;
			if (ccurrids.length <= 0)
				return null;
			for (int i = 0; i < cmangids.length; i++) {
				//��������
				for (int j = 0; j < ccurrids.length; j++) {
					stockvaritems = dmo.findItemsForStockVarVOMy(cmangids[i], strSQL, ccurrids[j]);
					if (stockvaritems != null) {
						//���ñ�ͷ
						header = new StockVarHeaderVO();
						//����
						pk_currtype = ccurrids[j];
						currtypename = dmo.getCurrtypenameByPk(pk_currtype);
						if (pk_currtype == null || "".equals(pk_currtype.trim())) {
							SCMEnv.out("ȡ����ʱ����[nc.bs.pp.ask.AskbillBO.queryStockVarVOsMy(...)]");
							throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040701","UPP40040701-000005")/*@res "ȡ����ʱ����[nc.bs.pp.ask.AskbillBO.queryStockVarVOsMy(...)]"*/);
						}
						header.setCurrtypename(currtypename);
						header.setPk_currtype(pk_currtype);

						//�������
						aryInvInfos = dmo.getInvInfosByPk(cbaseids[i]);
						if (aryInvInfos == null) {
							header.setInvcode(null);
							header.setInvname(null);
							header.setInvspec(null);
							header.setInvtype(null);
						} else {
							header.setInvcode((String) aryInvInfos.get(0));
							header.setInvname((String) aryInvInfos.get(1));
							header.setInvspec((String) aryInvInfos.get(2));
							header.setInvtype((String) aryInvInfos.get(3));
						}
						//������
						if (aryInvInfos == null || aryInvInfos.get(4) == null) {
							header.setMeasname(null);
						} else {
							String pk_measdoc = (String) aryInvInfos.get(4);
							String measname = null;
							measname = dmo.getMeasnameByPk(pk_measdoc);
							header.setMeasname(measname);
						}
						//˰��
						if (aryInvInfos == null || aryInvInfos.get(5) == null) {
							header.setTaxratio(new UFDouble(0));
						} else {
							String pk_taxitems = (String) aryInvInfos.get(5);
							UFDouble taxratio = null;
							taxratio = dmo.getTaxratioByPk(pk_taxitems);
							if (taxratio == null) {
								header.setTaxratio(new UFDouble(0));
							} else {
								header.setTaxratio(taxratio);
							}
						}
						//���ñ�β

						//�ο��ɱ����ƻ���
						aryPrice = dmo.getCostPlanPriceForThanVO(cmangids[i]);
						if (aryPrice == null) {
							header.setCostprice(null);
							header.setPlanprice(null);
						} else {
							header.setCostprice((UFDouble) aryPrice.get(0));
							header.setPlanprice((UFDouble) aryPrice.get(1));
						}
						//���¼�
						UFDouble newprice = null;
						newprice = dmo.getPriceLastQuoted(cmangids[i], ccurrids[j], isInTax, strSQL);
						header.setLastprice(newprice);

						stockvar = new StockVarVO();
						stockvar.setParentVO(header);
						stockvar.setChildrenVO(stockvaritems);
						v.addElement(stockvar);
					}
				}
			}
			if (v.size() > 0) {
				stockvars = new StockVarVO[v.size()];
				v.copyInto(stockvars);
			}
		} catch (Exception e) {
			/*���òɹ����÷������淶�׳��쳣*/
			PubDMO.throwBusinessException(
					"nc.bs.pp.AskbillImpl.queryStockVarVOsMy", e);
		}

		return stockvars;
	}
	/**
	 * 
	 * �����������������빺��ת�����ɹ��������棬ѡ��Ӧ��ʱ�ӹ�Ӧ�̼۸����ѡ��Ĭ�Ϲ�Ӧ��ʹ�á�
	 * ѡȡ������ͬһ������й�Ӧ���Ƿ񶩻�Ϊ���ǡ���������Ч�۸��¼��
	 *         �������ȼ���ߵĹ�Ӧ�̴��뵽�빺���еĹ�Ӧ����Ŀ��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * @param loginDate ��¼����
	 * @param pk_corp ���������˾
	 * @param sInvBasids �����������id����
	 * @return hashmap<pk_invbasdoc,defaultVendor>
	 * @throws BusinessException
	 * <p>
	 * @author donggq
	 * @time 2008-2-29 ����03:06:52
	 */
	public HashMap getDefaultVendors(String loginDate,String pk_corp,String[] sInvBasids)throws BusinessException{
	  //�������
	  if(loginDate == null || loginDate.length() <= 0 || 
	      pk_corp == null || pk_corp .length() <= 0 ||
	      sInvBasids == null ||sInvBasids.length <= 0){
	    return null;
	  }
	  try{
  	  AskbillDMO dmo = new AskbillDMO();
  	  return dmo.getDefaultVendors(loginDate, pk_corp, sInvBasids);
	  }catch( Exception e){
	    /*���òɹ����÷������淶�׳��쳣*/
      PubDMO.throwBusinessException(
          "nc.bs.pp.AskbillImpl.queryStockVarVOsMy", e);
	  }
	  return null;
	}
	
	public void linkOrder(ParaCtToPoRewriteVO[] reWriteData)throws BusinessException{
		//����Ϊ�빺����Ϊ�ɹ�����׼����VO
		ArrayList<ParaCtToPoRewriteVO> alForPOVO = new ArrayList<ParaCtToPoRewriteVO>();
		//����Ϊ�빺����Ϊί�ⶩ��׼����VO
		ArrayList<ParaCtToPoRewriteVO> alForSCVO = new ArrayList<ParaCtToPoRewriteVO>();
		//����û�е��ݵ�Ϊ�ɹ�����׼����VO
		ArrayList<ParaCtToPoRewriteVO> alNoUpForPOVO = new ArrayList<ParaCtToPoRewriteVO>();
		//����Ϊ�빺����Ϊί�ⶩ��׼����VO
		ArrayList<ParaCtToPoRewriteVO> alNoUpForSCVO = new ArrayList<ParaCtToPoRewriteVO>();
		
		HashMap<String, Vector<String>> hmUpsourceInfo = new HashMap<String, Vector<String>>();
		try{
			AskbillDMO dmo = new AskbillDMO();
			//��ѯ���ε�����Ϣ
			hmUpsourceInfo = dmo.queryBillUpsource(reWriteData);
		}catch(Exception e){
			SCMEnv.out(e);
			throw new BusinessException(e);
		}
		//������������빺���ݣ������ε��빺����Ϣ���õ�����VO��
		if(null != hmUpsourceInfo||hmUpsourceInfo.size() == 0){
			for (ParaCtToPoRewriteVO paraCtToPoRewriteVO : reWriteData) {
				ParaCtToPoRewriteVO tempVO = (ParaCtToPoRewriteVO)paraCtToPoRewriteVO.clone();
				if(hmUpsourceInfo.containsKey(tempVO.getSourceRowbid())){
					tempVO.setSourceHeadid(hmUpsourceInfo.get(tempVO.getSourceRowbid()).get(0));
					tempVO.setSourceRowid(hmUpsourceInfo.get(tempVO.getSourceRowbid()).get(1));
					tempVO.setSourceRowbid(null);
					if(tempVO.getIsSC()){
						//ί��
						alForSCVO.add(tempVO);
					}else{
						alForPOVO.add(tempVO);
					}
				}else{
					if(tempVO.getIsSC()){
						//ί��
						alForSCVO.add(tempVO);
					}else{
						alForPOVO.add(tempVO);
					}
				}
			}
		}else{
			//����VO��û�����ε���
			for (ParaCtToPoRewriteVO paraCtToPoRewriteVO : reWriteData) {
				if(paraCtToPoRewriteVO.getIsSC()){
					//ί��
					alForSCVO.add(paraCtToPoRewriteVO);
				}else{
					alForPOVO.add(paraCtToPoRewriteVO);
				}
			}
		}
		if(alForPOVO.size() > 0){
			new OrderImpl().reWriteCTByPrOrPp(ScmConst.PO_Order,
					alForPOVO.toArray(new ParaCtToPoRewriteVO[alForPOVO
							.size()]));
		}
		if(alForSCVO.size() > 0){
			new OrderImpl().reWriteCTByPrOrPp(ScmConst.SC_Order, alForSCVO
					.toArray(new ParaCtToPoRewriteVO[alForSCVO.size()]));
		}
	}
}
