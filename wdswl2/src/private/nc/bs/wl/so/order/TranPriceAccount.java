package nc.bs.wl.so.order;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.HYPubBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.dm.so.order.SoorderBVO;
import nc.vo.dm.so.order.SoorderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.dm.storebing.TbStorcubasdocVO;
import nc.vo.wds.dm.storetranscorp.StortranscorpBVO;
import nc.vo.wds.tranprice.freight.ZhbzBVO;
import nc.vo.wds.tranprice.freight.ZhbzHVO;
import nc.vo.wds.tranprice.tonkilometre.TranspriceBVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * �����˵��˷Ѻ����̨��
 * 
 * @author Administrator
 * 
 */
public class TranPriceAccount {

	private AggregatedValueObject curBillvo = null;
	// ��ǰ��¼����
	private UFDate date = null;
	
	
	// �ֲ�����̰󶨹�ϵVO
	private TbStorcubasdocVO bingVO = null;
	// �ջ�����
	private String reareaid = null;
	
	//�Ƿ�ԭ�Ϸ�
	private boolean fisbigflour;
	//�Ƿ��ܲ�
	private boolean isZC = true;
	// �Ƿ��㵣
	private boolean isSmall = false;
	
	// �˼۱�����
	private String colType = null;
	// ��ǰʹ�õ� �˼۱��ӱ�
	private TranspriceBVO curTranpriceBvo = null;
	
	// ���ܵ������� ��������
	private ArrayList<UFDouble> totalNum = new ArrayList<UFDouble>();

	private BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ �����˷�
	 * @ʱ�䣺2011-5-19����08:34:49
	 * @param billvo
	 * @param logDate
	 *            ��¼����
	 * @param operatorid
	 *            ����Ա
	 * @return
	 * @throws BusinessException
	 */
	public AggregatedValueObject colTransCost(AggregatedValueObject billvo,
			UFDate logDate, String operatorid) throws BusinessException {
		if (billvo == null || billvo.getChildrenVO() == null
				|| billvo.getChildrenVO().length == 0)
			throw new BusinessException("���������쳣");
		if (logDate == null) {
			this.date = new UFDate(System.currentTimeMillis());
		} else {
			this.date = logDate;
		}
		this.curBillvo = billvo;
		SoorderVO head = (SoorderVO) curBillvo.getParentVO();
		head.setVcolpersonid(operatorid);
		// 1.ȷ���˷Ѽ�������
		getColType();
		// 2.�����˼۱�
		getTranPriceBvo();
		// 3.�����˷ѣ�����ɨ���˷���Ϣ�����˵�
		appendPriceInfor();
		// 4. �����˷�
		doSavePriceInfor();
		return billvo;
	}
	/**
	 * 
	 * @throws BusinessException 
	 * @���ߣ�lyf:����˼۱�
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-7����03:34:45
	 */
	public void getTranPriceBvo() throws BusinessException{
		SoorderVO head = (SoorderVO) curBillvo.getParentVO();
		if(fisbigflour){//����ԭ�Ϸ��˼۱����ֹ����˼۱�
			List<TranspriceBVO> lprice = getBigPriceInfor(colType, head
					.getPk_transcorp(), head.getPk_outwhouse(), reareaid, true);
			if (lprice == null || lprice.size() == 0) {
				throw new BusinessException("δ��ѯ��ƥ����˼۱�");
			}
			curTranpriceBvo = lprice.get(0);
		}else{
			if(isZC){//--�֣ܲ��ֹ����˼۱����������˼۱�
				if (WdsWlPubConst.WDSI.equalsIgnoreCase(colType)) {
					List<TranspriceBVO> lprice = getDsPriceInfor(colType, head
							.getPk_transcorp(), head.getPk_outwhouse(), reareaid, true);
					if (lprice == null || lprice.size() == 0) {
						throw new BusinessException("δ��ѯ��ƥ����˼۱�");
					}
					curTranpriceBvo = lprice.get(0);
				} else {
					List<TranspriceBVO> lprice = getXsPriceInfor(colType, head
							.getPk_transcorp(), head.getPk_outwhouse(), reareaid, true);
					if (lprice == null || lprice.size() == 0) {
						throw new BusinessException("δ��ѯ��ƥ����˼۱�");
					}
					curTranpriceBvo = lprice.get(0);
				}
			}else{//��ѯ�ֲ��˼۱�
				List<TranspriceBVO> lprice = getFCPriceInfor(colType, head
						.getPk_transcorp(), head.getPk_outwhouse(), reareaid, true);
				if (lprice == null || lprice.size() == 0) {
					throw new BusinessException("δ��ѯ��ƥ����˼۱�");
				}
				curTranpriceBvo = lprice.get(0);		
			}
		}
		
	}
	/**
	 * 
	 * @throws BusinessException
	 * @����lyf ���� ���˵� �˷������Ϣ
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-6-10����10:21:09
	 */
	public void appendPriceInfor() throws BusinessException {
		SoorderVO head = (SoorderVO) curBillvo.getParentVO();
		// ������
		head.setNgls(bingVO.getKilometer());
		// �˼�: ʵ�ʼ۸�=ԭʼ�۸�*��1+����������
		UFDouble nrate = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo
				.getNadjrate());
		head
				.setNtranprice((getTransprice().multiply(nrate.div(100).add(1),
						8)));
		if (isSmall&&WdsWlPubConst.WDSI.equalsIgnoreCase(colType)) {
			// �㵣����ֵ
			head.setNadjustprice(curTranpriceBvo.getNpriceadj());
			// �㵣��������
			head.setIadjusttype(curTranpriceBvo.getIadjtype());
		}
		// �����˷�
		setNcolmny();
	}

	/**
	 * �����˷���Ϣ
	 * 
	 * @throws BusinessException
	 */
	protected void doSavePriceInfor() throws BusinessException {
		SoorderVO head = (SoorderVO) curBillvo.getParentVO();
		getBaseDAO().updateVO(
				head,
				new String[] { "ngls", "ntranprice", "nadjustprice",
						"iadjusttype", "ntransmny", "vcolpersonid","custareaid","icoltype"  });
		String sql = "select ts from wds_soorder where pk_soorder = '"
				+ head.getPrimaryKey() + "'";
		String ts = PuPubVO.getString_TrimZeroLenAsNull(getBaseDAO()
				.executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
		head.setTs(new UFDateTime(ts));
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�����ж��Ƿ� ������ɢ���� ��ɢ���ˣ�ȡ��ɢ�����˼�
	 * @ʱ�䣺2011-6-10����10:25:57
	 * @return
	 */
	public UFDouble getTransprice() {
		UFDouble price = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo
				.getNtransprice());
		// �Ƚ� ���������Ƿ������ɢ��������
		if (WdsWlPubConst.WDSI.equalsIgnoreCase(colType)) {
			UFDouble ntotalNum = PuPubVO
					.getUFDouble_NullAsZero(totalNum.get(0));
			UFDouble nsmallnum = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo
					.getNsmallnum());
			UFDouble num1 = ntotalNum.sub(nsmallnum);
			if (num1.doubleValue() <= 0) {
				price = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo
						.getNsmallprice());
			}
		}
		return price;
	}
	/**
	 * 
	 * @throws BusinessException
	 * @���ߣ�lyf
	 * @˵��: �����˷�
	 * @ʱ�䣺2011-5-20����01:22:14
	 */
	void setNcolmny() throws BusinessException {
		SoorderVO head = (SoorderVO) curBillvo.getParentVO();
		UFDouble nnum = null;
		UFDouble ngls = null;
		UFDouble nprice = null;
		if(fisbigflour){
			nnum = PuPubVO.getUFDouble_NullAsZero(totalNum.get(0));
			ngls = PuPubVO.getUFDouble_NullAsZero(head.getNgls());
			nprice = PuPubVO.getUFDouble_NullAsZero(head.getNtranprice());
			UFDouble nadustmny = null;
			Integer iadjstType = PuPubVO.getInteger_NullAs(head
					.getIadjusttype(), 1);
			if (iadjstType == 0) {// ���նֵ���
				nadustmny = PuPubVO.getUFDouble_NullAsZero(
						head.getNadjustprice()).multiply(nnum);
			} else {// ���������
				nadustmny = PuPubVO.getUFDouble_NullAsZero(
						head.getNadjustprice()).multiply(
						PuPubVO.getUFDouble_NullAsZero(totalNum.get(1)));
			}
			head.setNtransmny(nprice.multiply(nnum, 8).multiply(ngls, 8).add(
					nadustmny));
			return;
		}
		if(isZC){
			if (WdsWlPubConst.WDSI.equalsIgnoreCase(colType)) {
				nnum = PuPubVO.getUFDouble_NullAsZero(totalNum.get(0));
				ngls = PuPubVO.getUFDouble_NullAsZero(head.getNgls());
				nprice = PuPubVO.getUFDouble_NullAsZero(head.getNtranprice());
				UFDouble nadustmny = null;
				Integer iadjstType = PuPubVO.getInteger_NullAs(head
						.getIadjusttype(), 1);
				if (iadjstType == 0) {// ���նֵ���
					nadustmny = PuPubVO.getUFDouble_NullAsZero(
							head.getNadjustprice()).multiply(nnum);
				} else {// ���������
					nadustmny = PuPubVO.getUFDouble_NullAsZero(
							head.getNadjustprice()).multiply(
							PuPubVO.getUFDouble_NullAsZero(totalNum.get(1)));
				}
				head.setNtransmny(nprice.multiply(nnum, 8).multiply(ngls, 8).add(
						nadustmny));
			} else {
				nnum = PuPubVO.getUFDouble_NullAsZero(totalNum.get(1));
				nprice = PuPubVO.getUFDouble_NullAsZero(head.getNtranprice());
				head.setNtransmny(nprice.multiply(nnum, 8));
			}
		}else{
			Integer icoltype = PuPubVO.getInteger_NullAs(curTranpriceBvo.getIcoltype(), 0);
			UFDouble nmny = PuPubVO.getUFDouble_NullAsZero(null);
			UFDouble nadjmny = PuPubVO.getUFDouble_NullAsZero(null);
			if(icoltype==0){//�ܷ���
				nmny = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNtransprice());
			}else if(icoltype==1){//ÿ���˼�
				nmny = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNtransprice()).multiply(totalNum.get(1), 8);
			}else if(icoltype ==2){//ÿ���˼�
				nmny = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNtransprice()).multiply(totalNum.get(0), 8);
			}
			UFDouble nsmall = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNsmallnum());
			if(nsmall.sub(totalNum.get(0)).doubleValue()>0){
				Integer iadjtype = PuPubVO.getInteger_NullAs(curTranpriceBvo.getIadjtype(), 0);
				if(iadjtype==0){//�ܷ���
					nadjmny = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNpriceadj());
				}else if(iadjtype==1){//ÿ�䲹���۸�
					nadjmny = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNpriceadj()).multiply(totalNum.get(1), 8);
				}else if(iadjtype ==2){//ÿ�ֲ����۸�
					nadjmny = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNpriceadj()).multiply(totalNum.get(0), 8);
				}else if(iadjtype ==3){//������С�����������ܷ���
					nadjmny = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNpriceadj()).multiply(nsmall, 8);
					nmny =  PuPubVO.getUFDouble_NullAsZero(null);
				}
			}
			head.setNtransmny(nmny.add(nadjmny, 8));
		}

	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵����
	 * @ʱ�䣺2011-6-9����10:39:58
	 * @return
	 */
	public void getColType() throws BusinessException {
		SoorderVO head = (SoorderVO) curBillvo.getParentVO();
		SoorderBVO[] bodys = (SoorderBVO[]) curBillvo.getChildrenVO();
		String pk_transcorp = head.getPk_transcorp();
		if (pk_transcorp == null || "".equalsIgnoreCase(pk_transcorp)) {
			throw new BusinessException("����ѡ�������");
		}
		
		String pk_outwhouse = head.getPk_outwhouse();
		if (pk_outwhouse == null || "".equalsIgnoreCase(pk_outwhouse)) {
			throw new BusinessException("δȡ������վ");
		}
		String pk_sendareal = head.getPk_sendareal();
		if (pk_sendareal == null || "".equalsIgnoreCase(pk_sendareal)) {
			throw new BusinessException("δȡ�÷�������");
		}
		String pk_custman = head.getPk_cumandoc();
		if (pk_custman == null || "".equalsIgnoreCase(pk_custman)) {
			throw new BusinessException("δȡ��������Ϣ");
		}
		// 1��÷ֲ�����̵İ�VO
		getBingVo(pk_outwhouse,pk_sendareal, pk_custman);
		this.reareaid = bingVO.getCustareaid();
		if (reareaid == null || "".equalsIgnoreCase(reareaid)) {
			throw new BusinessException("���ڷֲֿ��̰󶨽ڵ㣬ά��������������");
		}
		head.setCustareaid(reareaid);
		//2 ���ݳ����̲�ѯ ʵ���ۺϱ�׼��������ʵ���ۺ϶���
		// ���˵��������ֳ����ࣺ����ʵ���ۺϣ�������ʵ���ۺ�
		for (SoorderBVO body : bodys) {
			body.setFiszh(UFBoolean.FALSE);
		}
		UFDouble nexNum = PuPubVO.getUFDouble_NullAsZero(null);//����ʵ���ۺϱ�׼�Ĵ������ʵ���ۺ��ʼ���Ķ���
		AggregatedValueObject[] zhbzBills = getZhbz(pk_transcorp);//ÿ�������̿����ж���ۺϱ�׼������ÿ�����ֻ������һ���ۺϱ�׼
		if(zhbzBills !=null && zhbzBills.length>0){
			for(int i=0;i<zhbzBills.length;i++){
				ZhbzHVO hvo = (ZhbzHVO)zhbzBills[i].getParentVO();
				ZhbzBVO[] bvos =(ZhbzBVO[])  zhbzBills[i].getChildrenVO();
				UFDouble standardtune = PuPubVO.getUFDouble_NullAsZero(hvo.getStandardtune());//�ۺ�����(����)
				UFDouble tuneunits = PuPubVO.getUFDouble_NullAsZero(hvo.getTuneunits()).div(1000);//ʵ�ʻ�����= ������/�䣩���� 1000
				ArrayList<SoorderBVO> list = new ArrayList<SoorderBVO>();
				UFDouble nnum = PuPubVO.getUFDouble_NullAsZero(null);//�˵������ڸ��ۺϱ�׼�Ĵ��������������
				UFDouble nminhsl = new UFDouble(1);//�˵������ڸ��ۺϱ�׼�Ĵ����С������
				if(bvos !=null && bvos.length>0 ){
					for(ZhbzBVO bvo:bvos){
						String pk_invmandoc = PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk_invmandoc());
						if(pk_invmandoc == null){
							continue;
						}
						for (SoorderBVO body : bodys) {
							UFBoolean fiszh = PuPubVO.getUFBoolean_NullAs(body.getFiszh(), UFBoolean.FALSE);
							if(fiszh.booleanValue()){
								continue;
							}
							String pk_invmandoc2 = body.getPk_invmandoc();
							if(pk_invmandoc.equalsIgnoreCase(pk_invmandoc2)){
								UFDouble nhgrate = PuPubVO.getUFDouble_NullAsZero(body.getNhgrate());
								if(nhgrate.sub(nminhsl).doubleValue()<0){
									nminhsl = nhgrate;
								}
								nnum = nnum.add(PuPubVO.getUFDouble_NullAsZero(body.getNassoutnum()));
								list.add(body);
							}
						}
					}
				}
				//����������������ʵ�ʻ��������������
				//δ�����Ĳ��֣�������Щ����е���С�ۺ���������
				UFDouble nexAssNum = nnum.sub(standardtune);
				if(nexAssNum.doubleValue()>0){
					UFDouble nds = standardtune.multiply(nminhsl);
					UFDouble nexds = nexAssNum.multiply(tuneunits);
					nexNum = nexNum.add(nds);
					nexNum = nexNum.add(nexds);
					for(int j=0;j<list.size();j++){
						SoorderBVO body = list.get(j);
						body.setFiszh(UFBoolean.TRUE);
					}
				}
				
			}
		}
		// 3���ܳ�����(��)����
		UFDouble ntotalNum = new UFDouble(0);
		UFDouble ntotalAssNUm = new UFDouble(0);
		for (SoorderBVO body : bodys) {
			UFBoolean fiszh = PuPubVO.getUFBoolean_NullAs(body.getFiszh(), UFBoolean.FALSE);
			if( !fiszh.booleanValue()){
				ntotalNum = ntotalNum.add(PuPubVO.getUFDouble_NullAsZero(body
						.getNoutnum()), 8);
			}
			ntotalAssNUm = ntotalAssNUm.add(PuPubVO.getUFDouble_NullAsZero(body
					.getNassoutnum()), 8);			
		}
		ntotalNum = ntotalNum.add(nexNum);
		totalNum.add(ntotalNum);
		totalNum.add(ntotalAssNUm);
		//�ж� �Ƿ�ԭ�Ϸ�,ԭ�Ϸ۷��˰��նֹ����˼۱�
		fisbigflour = PuPubVO.getUFBoolean_NullAs(head.getFisbigflour(), UFBoolean.FALSE).booleanValue();
		if(fisbigflour){
			colType = WdsWlPubConst.WDSI;
			return ;
		}
		// 5.�ж��Ƿ��ܲ�
		if (WdsWlPubConst.WDS_WL_ZC.equalsIgnoreCase(pk_outwhouse)) {
			isZC = true;
			//5.1 �ֿ�������̰󶨵�VO
			List<StortranscorpBVO> stroCorp = getStroCorpVO(pk_outwhouse,
					pk_transcorp, reareaid,true);
			//5.1.1 ���û���������㵣��׼��Ĭ�ϰ��նֹ���������
			if (stroCorp == null || stroCorp.size() == 0) {
				colType = WdsWlPubConst.WDSI;
			} else {
			// 5.1.2����㵣��׼
				Integer ismalltype = stroCorp.get(0).getIsmalltype();//�㵣��׼
				Integer ismallprice = stroCorp.get(0).getIsmallprice();//�˼�����
				UFDouble nsmallnum = PuPubVO.getUFDouble_NullAsZero(stroCorp.get(0).getNsmallnum());
				if (0 == ismalltype) {// ����������Ϊ���ֱ�׼
					if (nsmallnum.sub(totalNum.get(0)).doubleValue() >= 0) {
						isSmall = true;
						if (0 == ismallprice) {
							colType = WdsWlPubConst.WDSI;
						} else if (1 == ismallprice) {
							colType = WdsWlPubConst.WDSJ;
						}
					} else {
						isSmall = false;
						colType = WdsWlPubConst.WDSI;
					}
				} else {// �Ը�������Ϊ���ֱ�׼
					if (nsmallnum.sub(totalNum.get(1)).doubleValue() >= 0) {
						isSmall = true;
						if (0 == ismallprice) {
							colType = WdsWlPubConst.WDSI;
						} else if (1 == ismallprice) {
							colType = WdsWlPubConst.WDSJ;
						}
					} else {
						isSmall = false;
						colType = WdsWlPubConst.WDSI;
					}
				}
			}
		} else {
			isZC = false;
			colType = WdsWlPubConst.WDSK;
		}
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ:���ݳ����̲�ѯʵ���ۺϱ�׼
	 * @ʱ�䣺2011-11-24����07:24:55
	 * @param carriersid ����������
	 * @return
	 * @throws UifException 
	 */
	public AggregatedValueObject[] getZhbz(String carriersid) throws UifException{
		AggregatedValueObject[] bills = null;
		if(carriersid == null || "".equalsIgnoreCase(carriersid)){
			return bills;
		}
		String[] obj = new String[]{HYBillVO.class.getName(),ZhbzHVO.class.getName(),ZhbzBVO.class.getName()}; 
		String strWhere = " carriersid='"+carriersid+"'";
		bills = new HYPubBO().queryBillVOByCondition(obj, strWhere);
		return bills;
	}
	
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ :�����ջ��������С���󣬵ݹ��ȡ�ֲֳ�������Ϣ
	 * @ʱ�䣺2011-11-3����04:10:04
	 * @param pk_outwhouse���ֿ�id
	 * @param pk_transcorp�����乫˾id
	 * @param caredid:�ջ��ص�id
	 * @param isDG :�ݹ�
	 * @return
	 * @throws BusinessException
	 */
	public ArrayList<StortranscorpBVO>  getStroCorpVO(String pk_outwhouse,
			String pk_transcorp, String caredid,boolean isDG) throws BusinessException {
		ArrayList<StortranscorpBVO> obj = null;
		String fatherId = null;
		String sql = "select  * from wds_stortranscorp_b where pk_stordoc='"
				+ pk_outwhouse + "' and pk_wds_tanscorp_h='" + pk_transcorp
				+ "' and isnull(dr,0)=0 and careaid='" + caredid + "'";
		obj = (ArrayList<StortranscorpBVO>) getBaseDAO().executeQuery(sql,
				new BeanListProcessor(StortranscorpBVO.class));
		if ((obj == null ||obj.size() == 0) && isDG) {
			fatherId = getFatherId(caredid);
			if (fatherId == null || "".equalsIgnoreCase(fatherId)) {
				obj = getStroCorpVO(pk_outwhouse, pk_transcorp, fatherId,false);
			} else {
				obj = getStroCorpVO(pk_outwhouse, pk_transcorp, fatherId,true);
			}
		}
		return obj;
	}


	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ ��÷ֲֿ��̰󶨹�ϵ
	 * @ʱ�䣺2011-6-9����11:34:44
	 * @param pk_outwhouse
	 *            ����վ
	 * @param pk_sendareal
	 *  		  ��������         
	 * @param pk_custman
	 *            ����
	 * @throws BusinessException
	 */
	public void getBingVo(String pk_outwhouse, String pk_sendareal,String pk_custman)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select tb_storcubasdoc.* from ");
		sql.append("  wds_storecust_h ");//�ֲֿ��̰�����
		sql.append(" join tb_storcubasdoc ");
		sql.append(" on wds_storecust_h.pk_wds_storecust_h = tb_storcubasdoc.pk_wds_storecust_h ");//�ֲֿ��̰��ӱ�
		sql.append(" where isnull(wds_storecust_h.dr,0)=0 and isnull(tb_storcubasdoc.dr,0)=0");
		sql.append(" and wds_storecust_h.pk_stordoc='"+pk_outwhouse+"'");
		sql.append(" and wds_storecust_h.pk_sendareacl ='"+pk_sendareal+"'");
		sql.append(" and tb_storcubasdoc.pk_cumandoc='"+pk_custman+"'");
		ArrayList<TbStorcubasdocVO> list = (ArrayList<TbStorcubasdocVO>)getBaseDAO().executeQuery(sql.toString(),
				new BeanListProcessor(TbStorcubasdocVO.class));
		if (list == null || list.size()==0) {
			throw new BusinessException("��ά��������ֲֵİ󶨹�ϵ");
		}
		this.bingVO = (TbStorcubasdocVO) list.get(0);
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ:��ѯԭ�Ϸ��˼۱�
	 * @ʱ�䣺2011-6-9����11:26:45
	 * @param pricetype
	 *            ��������
	 * @param pk_transcorp
	 *            ������
	 * @param pk_outwhouse
	 *            �����ֿ�
	 * @param reareaid
	 *            �ջ���
	 * @param isDG
	 *            �Ƿ�����ݹ�
	 * @return
	 * @throws BusinessException
	 */
	public List<TranspriceBVO> getBigPriceInfor(String pricetype,
			String pk_transcorp, String pk_outwhouse, String reareaid,
			boolean isDG) throws BusinessException {
		List<TranspriceBVO> lprice = null;
		StringBuffer sqlb = new StringBuffer();
		sqlb.append("select ");
		String[] names = new TranspriceBVO().getAttributeNames();
		for (String name : names) {
			sqlb.append(" b." + name + ",");
		}
		sqlb.append(" 'aaa'");
		sqlb
				.append("from wds_transprice_h h inner join wds_transprice_b  b on b.pk_wds_transprice_h = h.pk_wds_transprice_h ");
		sqlb
				.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.vbillstatus = "
						+ IBillStatus.CHECKPASS);
		sqlb.append(" and h.pk_billtype = '" + pricetype + "'");// �˼۱���������
		// :�ֹ����˼۱��������˼۱�����ͬһ����������ǵ������Ͳ�ͬ
		sqlb.append(" and h.carriersid='" + pk_transcorp + "'");// ������
		sqlb.append(" and h.reserve1='" + pk_outwhouse + "'");// �����ֿ�
		sqlb.append(" and isnull(h.fisbigflour,'N') = 'Y'");//�Ƿ�ԭ�Ϸ�
		sqlb.append(" and (isnull(b.ifw,0) = 0 or isnull(b.ifw,0) =1) ");// Ӧ�˷�Χ����
		sqlb.append(" and h.dstartdate <= '" + date + "' and h.denddate >= '"
				+ date + "'");
		sqlb.append(" and b.pk_replace='" + reareaid + "'");//�ջ�����
		lprice = (List<TranspriceBVO>) getBaseDAO().executeQuery(
				sqlb.toString(), new BeanListProcessor(TranspriceBVO.class));
		// ���û�л�ȡ��Ӧ�ջ������Ķ��壬��ѯ��û���ϼ��Ķ���
		if ((lprice == null || lprice.size() == 0) && isDG) {
			String pk_fatherarea = getFatherId(reareaid);
			if (pk_fatherarea == null || "".equalsIgnoreCase(pk_fatherarea)) {
				lprice = getDsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, false);
			} else {
				// ������Ͻ��������࣬�ݹ��ѯ
				lprice =getDsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, true);
			}
		}
		return lprice;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-6-9����11:26:45
	 * @param pricetype
	 *            ��������
	 * @param pk_transcorp
	 *            ������
	 * @param pk_outwhouse
	 *            �����ֿ�
	 * @param reareaid
	 *            �ջ���
	 * @param isDG
	 *            �Ƿ�����ݹ�
	 * @return
	 * @throws BusinessException
	 */
	public List<TranspriceBVO> getDsPriceInfor(String pricetype,
			String pk_transcorp, String pk_outwhouse, String reareaid,
			boolean isDG) throws BusinessException {
		List<TranspriceBVO> lprice = null;
		StringBuffer sqlb = new StringBuffer();
		sqlb.append("select ");
		String[] names = new TranspriceBVO().getAttributeNames();
		for (String name : names) {
			sqlb.append(" b." + name + ",");
		}
		sqlb.append(" 'aaa'");
		sqlb
				.append("from wds_transprice_h h inner join wds_transprice_b  b on b.pk_wds_transprice_h = h.pk_wds_transprice_h ");
		sqlb
				.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.vbillstatus = "
						+ IBillStatus.CHECKPASS);
		sqlb.append(" and h.pk_billtype = '" + pricetype + "'");// �˼۱���������
		// :�ֹ����˼۱��������˼۱�����ͬһ����������ǵ������Ͳ�ͬ
		sqlb.append(" and h.carriersid='" + pk_transcorp + "'");// ������
		sqlb.append(" and h.reserve1='" + pk_outwhouse + "'");// �����ֿ�
		sqlb.append(" and isnull(h.fisbigflour,'N') = 'N'");//�Ƿ�ԭ�Ϸ�
		sqlb.append(" and (isnull(b.ifw,0) = 0 or isnull(b.ifw,0) =1) ");// Ӧ�˷�Χ����
		sqlb.append(" and h.dstartdate <= '" + date + "' and h.denddate >= '"
				+ date + "'");
		sqlb.append(" and b.pk_replace='" + reareaid + "'");
		lprice = (List<TranspriceBVO>) getBaseDAO().executeQuery(
				sqlb.toString(), new BeanListProcessor(TranspriceBVO.class));
		// ���û�л�ȡ��Ӧ�ջ������Ķ��壬��ѯ��û���ϼ��Ķ���
		if ((lprice == null || lprice.size() == 0) && isDG) {
			String pk_fatherarea = getFatherId(reareaid);
			if (pk_fatherarea == null || "".equalsIgnoreCase(pk_fatherarea)) {
				lprice = getDsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, false);
			} else {
				// ������Ͻ��������࣬�ݹ��ѯ
				lprice =getDsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, true);
			}
		}
		return lprice;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵������ѯ�����˼۱�
	 * @ʱ�䣺2011-6-10����09:59:35
	 * @param pricetype
	 *            �˼۱�����
	 * @param pk_transcorp
	 *            ������
	 * @param pk_outwhouse
	 *            �����ֿ�
	 * @param reareaid
	 *            �ջ�����
	 * @param isDG
	 *            �Ƿ�ݹ�
	 * @return
	 * @throws BusinessException
	 */
	public List<TranspriceBVO> getXsPriceInfor(String pricetype,
			String pk_transcorp, String pk_outwhouse, String reareaid,
			boolean isDG) throws BusinessException {
		List<TranspriceBVO> lprice = null;
		StringBuffer sqlb = new StringBuffer();
		sqlb.append("select ");
		String[] names = new TranspriceBVO().getAttributeNames();
		for (String name : names) {
			sqlb.append(" b." + name + ",");
		}
		sqlb.append(" 'aaa'");
		sqlb
				.append("from wds_transprice_h h inner join wds_transprice_b  b on b.pk_wds_transprice_h = h.pk_wds_transprice_h ");
		sqlb
				.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and vbillstatus = "
						+ IBillStatus.CHECKPASS);
		sqlb.append(" and h.pk_billtype = '" + pricetype + "'");// �˼۱���������
		// :�ֹ����˼۱��������˼۱�����ͬһ����������ǵ������Ͳ�ͬ
		sqlb.append(" and h.carriersid='" + pk_transcorp + "'");// ������
		sqlb.append(" and h.reserve1='" + pk_outwhouse + "'");// �����ֿ�
		sqlb.append(" and (isnull(b.ifw,0)=0 or isnull(b.ifw,0) =1) ");// Ӧ�˷�Χ����
		if (PuPubVO.getString_TrimZeroLenAsNull(getPricePeriodWhereSql()) != null)// �����������
			sqlb.append(" and " + getPricePeriodWhereSql());
		sqlb.append(" and b.pk_replace='" + reareaid + "'");
		lprice = (List<TranspriceBVO>) getBaseDAO().executeQuery(
				sqlb.toString(), new BeanListProcessor(TranspriceBVO.class));
		// ���û�л�ȡ��Ӧ�ջ������Ķ��壬��ѯ��û���ϼ��Ķ���
		if ((lprice == null || lprice.size() == 0) && isDG) {
			String pk_fatherarea = getFatherId(reareaid);
			if (pk_fatherarea == null || "".equalsIgnoreCase(pk_fatherarea)) {
				lprice = getXsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, false);
			} else {
				// ������Ͻ��������࣬�ݹ��ѯ
				lprice = getXsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, true);
			}
		}
		return lprice;
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵������ѯ�ֲ��˼۱� 
	 * @ʱ�䣺2011-6-10����09:59:35
	 * @param pricetype
	 *            �˼۱�����
	 * @param pk_transcorp
	 *            ������
	 * @param pk_outwhouse
	 *            �����ֿ�
	 * @param reareaid
	 *            �ջ�����
	 * @param isDG
	 *            �Ƿ�ݹ�
	 * @return
	 * @throws BusinessException
	 */
	public List<TranspriceBVO> getFCPriceInfor(String pricetype,
			String pk_transcorp, String pk_outwhouse, String reareaid,
			boolean isDG) throws BusinessException {
		List<TranspriceBVO> lprice = null;
		StringBuffer sqlb = new StringBuffer();
		sqlb.append("select ");
		String[] names = new TranspriceBVO().getAttributeNames();
		for (String name : names) {
			sqlb.append(" b." + name + ",");
		}
		sqlb.append(" 'aaa'");
		sqlb
				.append("from wds_transprice_h h inner join wds_transprice_b  b on b.pk_wds_transprice_h = h.pk_wds_transprice_h ");
		sqlb
				.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and vbillstatus = "
						+ IBillStatus.CHECKPASS);
		sqlb.append(" and h.pk_billtype = '" + pricetype + "'");// �˼۱���������
		sqlb.append(" and h.carriersid='" + pk_transcorp + "'");// ������
		sqlb.append(" and h.reserve1='" + pk_outwhouse + "'");// �����ֿ�
		sqlb.append(" and (isnull(b.ifw,0)=0 or isnull(b.ifw,0) =1) ");// Ӧ�˷�Χ����
		sqlb.append(" and h.nmincase <= " + totalNum.get(0).doubleValue());
		sqlb.append(" and h.nmaxcase > " + totalNum.get(0).doubleValue());
		sqlb.append(" and b.pk_replace='" + reareaid + "'");
		lprice = (List<TranspriceBVO>) getBaseDAO().executeQuery(
				sqlb.toString(), new BeanListProcessor(TranspriceBVO.class));
		// ���û�л�ȡ��Ӧ�ջ������Ķ��壬��ѯ��û���ϼ��Ķ���
		if ((lprice == null || lprice.size() == 0) && isDG) {
			String pk_fatherarea = getFatherId(reareaid);
			if (pk_fatherarea == null || "".equalsIgnoreCase(pk_fatherarea)) {
				lprice = getDsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, false);
			} else {
				// ������Ͻ��������࣬�ݹ��ѯ
				lprice = getDsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, true);
			}
		}
		return lprice;
	}


	/**
	 * 
	 * @���ߣ�lyf
	 * @˵��:�����˼۱� �������
	 * @ʱ�䣺2011-6-10����09:39:39
	 * @return
	 * @throws BusinessException
	 */
	public String getPricePeriodWhereSql() throws BusinessException {
		UFDouble ntotalAssNum = PuPubVO.getUFDouble_NullAsZero(totalNum.get(1));
		UFDouble gls = PuPubVO.getUFDouble_NullAsZero(this.bingVO
				.getKilometer());
		return " h.nmincase <= " + ntotalAssNum.doubleValue()
				+ " and h.nmaxcase >= " + ntotalAssNum.doubleValue()
				+ "  and b.nmindistance <= " + gls.doubleValue()
				+ " and b.nmaxdistance >= " + gls.doubleValue();

	}


	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ ��ȡ������������id
	 * @ʱ�䣺2011-5-19����07:15:32
	 * @param reareaid
	 * @return
	 * @throws BusinessException
	 */
	protected String getFatherId(String reareaid) throws BusinessException {
		String sql = "select pk_fatherarea,areaclname from  bd_areacl where pk_areacl = '"
				+ reareaid + "'";
		ArrayList<Object> list = (ArrayList<Object>)getBaseDAO().executeQuery(sql,
				WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
		if (list != null && list.size()>0) {
			Object[] obj= (Object[])list.get(0);
			String areaclname =PuPubVO.getString_TrimZeroLenAsNull(obj[1]);
			if (areaclname != null) {
				if (areaclname.contains("ʡ")) {
					return null;
				} else {
					return PuPubVO.getString_TrimZeroLenAsNull(obj[0]);
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-5-18����08:56:27
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	protected String getAreaName(String id) throws BusinessException {
		String sql = "select areaclname from  bd_areacl where pk_areacl = '"
				+ id + "'";
		return PuPubVO.getString_TrimZeroLenAsNull(getBaseDAO().executeQuery(
				sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-5-18����08:56:27
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	protected String getAreaCode(String id) throws BusinessException {
		String sql = "select areaclcode from  bd_areacl where pk_areacl = '"
				+ id + "'";
		return PuPubVO.getString_TrimZeroLenAsNull(getBaseDAO().executeQuery(
				sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-5-19����07:48:15
	 * @param areaclcode
	 * @return
	 * @throws BusinessException
	 */
	protected String getAreaIdByCode(String areaclcode)
			throws BusinessException {
		String sql = "select  pk_areacl from  bd_areacl where areaclcode = '"
				+ areaclcode + "'";
		return PuPubVO.getString_TrimZeroLenAsNull(getBaseDAO().executeQuery(
				sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));

	}

}