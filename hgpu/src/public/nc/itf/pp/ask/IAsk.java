package nc.itf.pp.ask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import nc.ui.pub.ClientEnvironment;
import nc.vo.pp.ask.AskbillHeaderVO;
import nc.vo.pp.ask.AskbillMergeVO;
import nc.vo.pp.ask.EffectPriceParaVO;
import nc.vo.pp.ask.EffectPriceVO;
import nc.vo.pp.ask.PriceauditHeaderVO;
import nc.vo.pp.ask.VendorInvPriceVO;
import nc.vo.pp.ask.VendorVO;
import nc.vo.pp.price.QuoteConVO;
import nc.vo.pp.price.StatParaVO;
import nc.vo.pp.price.StockExecVO;
import nc.vo.pp.price.StockVarVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;

public interface IAsk {
//	/**
//	 * �������������ݿ������һ��VO����
//	 *
//	 * �������ڣ�(2001-8-4)
//	 * @return Vector
//	 * @param key Vector
//	 * @exception BusinessException �쳣˵����
//	 */
//	public abstract Vector insertMy(Vector v)
//			throws BusinessException;
//	/**
//	 * �������������ݿ������һ��VO����
//	 *
//	 * �������ڣ�(2001-8-4)
//	 * @return Vector
//	 * @param key Vector
//	 * @exception BusinessException �쳣˵����
//	 */
//	public abstract Vector updateMy(Vector v)
//			throws BusinessException;
	 /**
	 * @���ܣ�������Ա������ȡ����Ա���ڲ�������
	 * @���ߣ���־ƽ
	 * �������ڣ�(2001-9-14 10:59:44)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 *
	 * @return java.lang.String
	 * @param psnid java.lang.String
	 */
	public abstract String getPkDeptByPkPsnForAsk(String pk_psndoc) throws BusinessException;
	 /**
	 * @���ܣ���ѯѯ�۵���ϸ
	 * @���ߣ���־ƽ
	 * �������ڣ�(2001-9-14 10:59:44)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 *
	 * @return java.lang.String
	 * @param psnid java.lang.String
	 */
	public abstract VendorVO[] queryVendorDetail(String logCorp, String logUser) throws BusinessException;
	/**
	 * �������������ݿ���ɾ��VO�������顣
	 *
	 * �������ڣ�(2001-8-4)
	 * @return Vector
	 * @param key Vector
	 * @exception BusinessException �쳣˵����
	 */
	public abstract boolean discardAskbillVOsMy(Vector v)
			throws BusinessException;
	/**
	 * @����ѯ�۵�(ѯ��ά��)
	 */
	public abstract Vector  queryAllInquireMy(String strSQL) throws BusinessException; 
	public abstract Vector  queryAllInquireMy(ConditionVO[] conds, String pk_corp, UFBoolean[] status,String userid) throws BusinessException;
	/**
	 * ͨ���������VO����
	 *
	 * �������ڣ�(2001-6-7)
	 * @return nc.vo.pp.ask.AskbillVO
	 * @param key String
	 * @exception BusinessException �쳣˵����
	 */
	public abstract Vector findByPrimaryKeyForAskBill(String key) throws BusinessException;
	/**
	 * ��������:��ѯѯ�۵�����
	 * �������: ArrayList(0)	ѯ�۵�ͷ����[]
	 			 ArrayList(1)	ѯ�۵�ͷʱ���[]
	 * ����ֵ:ArrayList(0),ѯ�۵���[](�������Ѵ���)
	 */
	public abstract Vector findByPrimaryKeyForAskBillForDataPower(String key,ConditionVO[] conds) throws BusinessException;
	/**
	 * ��������:��ѯѯ�۵�����
	 * �������: ArrayList(0)	ѯ�۵�ͷ����[]
	 			 ArrayList(1)	ѯ�۵�ͷʱ���[]
	 * ����ֵ:ArrayList(0),ѯ�۵���[](�������Ѵ���)
	 */
	public abstract Vector queryAllBodysForAskBill(ArrayList aryPara)
		throws BusinessException ;
	/**
	 * ��������:��ѯѯ�۵�����
	 * �������: ArrayList(0)	ѯ�۵�ͷ����[]
	 			 ArrayList(1)	ѯ�۵�ͷʱ���[]
	 * ����ֵ:ArrayList(0),ѯ�۵���[](�������Ѵ���)
	 */
	public abstract Vector queryAllBodysForPriceAudit(ArrayList aryPara)
		throws BusinessException ;
//	/**
//	 * �������������ݿ������һ��VO����
//	 *
//	 * �������ڣ�(2001-8-4)
//	 * @return Vector
//	 * @param key Vector
//	 * @exception BusinessException �쳣˵����
//	 */
//	public abstract Vector insertMyForPriceAudit(Vector v)
//			throws BusinessException;
//	/**
//	 * �������������ݿ������һ��VO����
//	 *
//	 * �������ڣ�(2001-8-4)
//	 * @return Vector
//	 * @param key Vector
//	 * @exception BusinessException �쳣˵����
//	 */
//	public abstract Vector updateMyForPriceAudit(Vector v)
//			throws BusinessException;
	/**
	 * @���ܣ���ѯѯ�۵���ϸ
	 * @˵����
			 1.������ֶ�
	 * @param  conds    ConditionVO[]
	 * @param  pk_corp  String
	 * @param  status   boolean[]
	 */
	public abstract Hashtable getEffectAskPrice(EffectPriceParaVO effectPricePara) throws BusinessException;
	/**
	 * @����ѯ�۵�(ѯ��ά��)
	 */
	public abstract Vector  queryAllForPriceAudit(String strSQL) throws BusinessException ;
	
	/**
	 * 
	 * ��ѯ�۸��������������˴�������ѯ����
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * @param conds
	 * @param pk_corp
	 * @param status
	 * @param strOpr
	 * @return
	 * @throws BusinessException
	 * <p>
	 * @author donggq
	 * @time 2008-8-25 ����10:47:06
	 */
	public abstract Vector  queryAllForPriceAudit(ConditionVO[] conds, String pk_corp, UFBoolean[] status,String strOpr) throws BusinessException ;
	
	public abstract Vector  queryAllForPriceAudit(String sCommenWhere, String pk_corp,String strOpr,boolean iswaitaudit) throws BusinessException;
	
	/**
	 * @����ѯ�۵�(ѯ��ά��)
	 */
	public abstract VendorInvPriceVO[]  queryForVendorInvPrice(ConditionVO[] conds, String pk_corp) throws BusinessException ;
	/**
	 * ͨ���������VO����
	 *
	 * �������ڣ�(2001-6-7)
	 * @return nc.vo.pp.ask.AskbillVO
	 * @param key String
	 * @exception BusinessException �쳣˵����
	 */
	public abstract Vector findByPrimaryKeyForPriceAuditBill(String key) throws BusinessException;
	/**
	 * ͨ���������VO����
	 *
	 * �������ڣ�(2001-6-7)
	 * @return nc.vo.pp.ask.AskbillVO
	 * @param key String
	 * @exception BusinessException �쳣˵����
	 */
	public abstract Vector findByPrimaryKeyForPriceAuditBill(String key,ConditionVO[] conds) throws BusinessException;
	/**
	 * �������������ݿ���ɾ��VO��������
	 *
	 * �������ڣ�(2001-6-7)
	 * @param vos AskbillVO[]
	 * @param key String
	 * @exception BusinessException �쳣˵����
	 */
	public abstract boolean discardPriceAuditbillVOsMy(Vector v) throws BusinessException;
	/**
	 * ���ߣ���ά�� ���ܣ����漰��������ʱ��ǰ̨��Ҫˢ�������ˣ��������ڣ�ts������״̬ ������ ���أ� ���⣺ ���ڣ�(2004-5-13
	 * 13:21:13) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param key
	 *            java.lang.String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public abstract ArrayList queryForAudit(String key) throws BusinessException;
	
	public abstract Vector  queryAllForPriceAudit(ConditionVO[] conds, String pk_corp, UFBoolean[] status) throws BusinessException ;
	/**
	 * @����ѯ�۵�(ѯ��ά��)
	 */
	public abstract Vector  queryBodysForPriceAudit(ConditionVO[] conds, String pk_corp, String key) throws BusinessException ;
	/**
	 * @����ѯ�۵�(ѯ��ά��)
	 */
	public abstract AskbillHeaderVO[]  queryHeadersForPriceAudit(ConditionVO[] conds, String pk_corp) throws BusinessException ;
	/**
	 * @���ܣ���ѯѯ�۵���ϸ
	 * @˵����
			 1.������ֶ�
	 * @param  conds    ConditionVO[]
	 * @param  pk_corp  String
	 * @param  status   boolean[]
	 */
	public abstract Hashtable querySourceInfoForGenOrder(Vector v) throws BusinessException;
	/**
	 * ���ܣ���ȡ�빺����ID��Ӧ���빺��ҵ����������
	 * ����: ArrayList �빺����ID
	 * ���أ�ArrayList �빺����ID��Ӧ���빺��ҵ����������
	 * ���ߣ���־ƽ
	 * ������2004-6-3 19:52:07)
	 *
	 */
	public abstract Hashtable getBusiIdForOrd(ArrayList listPara) throws BusinessException;
	/**
	 * ���ݱ�������ȡ�ñ���ID
	 * ���裺��������Ψһ
	 * �������ڣ�(2001-10-27 13:30:59)
	 * @return java.lang.String
	 * @param currname java.lang.String
	 */
	public abstract String queryCurrIDByCurrName(String currname) throws BusinessException ;
	/**
	 * @ѯ�۵��Ƿ�ִ�����빺
	 * @���ߣ�����
	 * @������String[] saRowId		�빺��������
	 * @����ֵ��UFBoolean[] uaExistAfter  ע��true��ʾ�빺���д��ں������ݣ�false��ʾ������
	 * �������ڣ�(2005-08-09 15:04:05)
	 * @return nc.vo.pub.lang.UFBoolean[]
	 */
	public abstract UFBoolean[] queryIfExecPrayForAsk(String[] saRowId) throws BusinessException;
	/**
	 * �������������ݿ������һ��VO����
	 *
	 * �������ڣ�(2001-8-4)
	 * @param key String
	 * @exception BusinessException �쳣˵����
	 */
	public abstract Vector updateMyForExcelToBill(Vector v) throws BusinessException;
	/**
	 * @����ʱѡ��Ӧ�̵�������
	 * @���ߣ�����
	 * @������String[] saRowId		�빺��������
	 * @����ֵ��UFBoolean[] uaExistAfter  ע��true��ʾ�빺���д��ں������ݣ�false��ʾ������
	 * �������ڣ�(2005-08-09 15:04:05)
	 * @return nc.vo.pub.lang.UFBoolean[]
	 */
	public Hashtable queryEmailAddrForAskSend(String[] cvendorIds) throws BusinessException;
	/**
	 * @���ܣ����ݹ�Ӧ�̡���������ֲ�ѯĬ�ϼ۸���Ϣ������Ϣ�ɲɹ��ṩ�ӿ�;�ɹ�������ί�ⶩ��ȡĬ�ϼ۸�ʱʹ��
	 * @˵����
			 1.������ֶ�
	 * @param   String[] cvendmangid--��Ӧ��
	 * @param   String[] cmangid--���
	 * @param   String ccurrencytypeid--����   
	 * @return	VO����--|-- cvendmangid--��Ӧ��
                        |--cmangid--���
                        |-- ccurrencytypeid--����
                        |-- nquoteprice --��˰����
                        |-- nquotetaxprice --��˰����
                        |-- deliverdays --������
                        |-- dvalitdate--������Ч����
                        |-- dinvalitdate --����ʧЧ����
	 * @throws	BusinessException
	 * @since	5.0

	 */
	public EffectPriceVO[] getEffectPriceForOrder(EffectPriceParaVO effectPricePara) throws BusinessException;
	/**
	 * ���ߣ�zx
	 * ���ܣ�Ϊ�ɹ������ṩ�۸�
	 * ������String[] cmangids,		�������ID����
	 *		String[] cvendormangids,	��Ӧ�̹���ID���飬��cmangidsһһ��Ӧ
	 *		String[] ccurrencyids,		����ID���飬��cmangidsһһ��Ӧ
	 		String sPricePolicy 	�۸����Ȳ���
	 		String curData 	������ǰ����
	 		String[] sRecieptAreas, �ջ�����
			String sSendtype    ���˷�ʽ
	 * ���أ�UFDouble[]		��cmangidsһһ��Ӧ�Ĺ�Ӧ�̴���۸�����
	 * ���⣺
	 * ���ڣ�(2002-6-10 13:25:09)
	 */
	public UFDouble[] queryPriceForPO(String[] cmangids,
			String[] cvendormangids,
			String[] ccurrencyids,
			String sPricePolicy,
			String curData, 
			String[] sRecieptAreas,
			String sSendtype) throws BusinessException;
	/**
	 * ���빺�����ɶ������Ʒ�ʽ��ѡ��Ϊ�������۸������������ɡ�,��ѯ�����������빺���С�
	 *
	 * �������ڣ�(2001-6-7)
	 * @return nc.vo.pp.ask.AskbillItemVO
	 * @param key String
	 * @exception java.sql.SQLException �쳣˵����
	 */
	//public Hashtable queryIsGenPriceAudit(ArrayList prayRowIds) throws BusinessException;
	/**
	 * @��д���ɶ�������
	 */
	public void  reWriteGenOrderNums(String[] addForOrder,String[] delForOrder) throws BusinessException;
	/**
	 * ��������:�����ɶ����ļ۸���������������
	 * @throws BusinessException 
	 */
	public void CheckIsGenOrder (String condition)  throws  BusinessException;
	/**
	 * @���ܣ���ѯѯ�۵���ϸ
	 * @˵����
			 1.������ֶ�
	 * @param  conds    ConditionVO[]
	 * @param  pk_corp  String
	 * @param  status   boolean[]
	 */
	public AskbillMergeVO queryDetailVOMy(String strSQL,String strSQLForFree,UFBoolean[] status) throws BusinessException;
	
	public AskbillMergeVO queryDetailVOMy(ConditionVO[] conds, String pk_corp, UFBoolean[] status) throws BusinessException;
	/**
	 * @���ܣ���ѯѯ�۵�ͳ�ƻ���VO
	 */
	public AskbillMergeVO queryStatisVOMy(
		ConditionVO[] conds,
		String pk_corp,
		UFBoolean[] status,
		String[] groups,
		String priceType)
		throws BusinessException ;
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
	public QuoteConVO[] queryQuoteConVOsMy(StatParaVO paravo) throws BusinessException ;
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
			throws BusinessException ;
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
	public QuoteConVO[] queryPurExecVOsMy(StatParaVO paravo) throws BusinessException ;
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
		throws BusinessException;
	/**
	 * �������޸�ѯ���۵�
	 * 
	 * �������ڣ�(2001-8-4)
	 * 
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public Vector doSaveForAskBill(Vector v) throws BusinessException ;
	/**
	 * �������޸ļ۸�������
	 * 
	 * �������ڣ�(2001-8-4)
	 * 
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public Vector doSaveForPriceAuditBill(Vector v) throws BusinessException ;
	/**
	 * @���ܣ���ѯ��������
	 * @˵���� 
	 * @param logCorp String--��½��˾
	 * @param vendorMangIDs String[]--��Ӧ�̹���ID����
	 * 
	 */
	public String[] queryForVendorSelected(String logCorp,String[] vendorMangIDs) throws BusinessException;
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
  public HashMap getDefaultVendors(String loginDate,String pk_corp,String[] sInvBasids)throws BusinessException;
//  /**
//   * ���ݴ����where sql ��ѯ�۸�������
//   * <p>
//   * <b>examples:</b>
//   * <p>
//   * ʹ��ʾ��
//   * <p>
//   * <b>����˵��</b>
//   * @param strSQL
//   * @return
//   * @throws BusinessException
//   * <p>
//   * @author donggq
//   * @time 2008-8-5 ����05:10:23
//   */
//  public Vector queryAllForPriceAudit( String strSQL) throws BusinessException;
	public  PriceauditHeaderVO[] queryHeadersForPriceAudit2(ConditionVO[] p0, String p1) throws Exception;
	
	public  Vector queryAllBodysForPriceAudit2(ArrayList  p0) throws Exception;
	/**
	 * 
	 * תΪ�������ӵĽӿڷ�������Ҫ�Ƿ��ؼ۸����Ӧ��˰��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * ���ܣ�Ϊ�ɹ������ṩ�۸�
	 * ������String[] cmangids,		�������ID����
	 *		String[] cvendormangids,	��Ӧ�̹���ID���飬��cmangidsһһ��Ӧ
	 *		String[] ccurrencyids,		����ID���飬��cmangidsһһ��Ӧ
	 		String sPricePolicy 	�۸����Ȳ���
	 		String curData 	������ǰ����
	 		String[] sRecieptAreas, �ջ�����
			String sSendtype    ���˷�ʽ
	 * ���أ�UFDouble[][]		��cmangidsһһ��Ӧ�Ĺ�Ӧ�̴��[i][0]�۸�\[0][1]˰������
	 * @throws BusinessException
	 * <p>
	 * @author donggq
	 * @time 2008-8-27 ����09:24:35
	 */
	public UFDouble[][] queryPriceForPOOrder(String[] cmangids,
			String[] cvendormangids,
			String[] ccurrencyids,
			String sPricePolicy,
			String curData, 
			String[] sRecieptAreas,
			String sSendtype) throws BusinessException;
}
