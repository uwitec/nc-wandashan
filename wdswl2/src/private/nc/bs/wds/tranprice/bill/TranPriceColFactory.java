package nc.bs.wds.tranprice.bill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.tranprice.bill.SendBillBodyVO;
import nc.vo.wds.tranprice.bill.SendBillVO;
import nc.vo.wds.tranprice.tonkilometre.TranspriceBVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author Administrator �˷Ѻ��� �����㷨��
 */
public class TranPriceColFactory {

	private DsTranPriceCol m_dscol = null;
	private XsTranPriceCol m_xscol = null;
	// ��Դ���ݱ�ͷid �˼۱��ӱ�vo
	private Map<String, TranspriceBVO> tranprice = new HashMap<String, TranspriceBVO>();
	// ���շ�����+�ջ��أ��ջ��ֻ��߿��̣�
	private Map<String, ArrayList<UFDouble>> groupNum = new HashMap<String, ArrayList<UFDouble>>();
	// ��ǰ������
	private SendBillBodyVO curBody = null;
	// ��ǰʹ�õ� �˼۱��ӱ�
	private TranspriceBVO curTranpriceBvo = null;

	private BaseDAO dao = null;

	protected BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public TranPriceColFactory(SendBillVO billvo) {
		super();
		this.billvo = billvo;
	}

	private SendBillVO billvo = null;

	public void setBillVO(SendBillVO bill) {
		billvo = bill;
	}

	public SendBillVO getBillVO() {
		return billvo;
	}

	private UFBoolean issale = UFBoolean.FALSE;// true:���۳��� false:ת�ֲֳ���

	public boolean isSale() {
		return issale.booleanValue();
	}

	public void setIsSale(UFBoolean bsale) {
		issale = bsale;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵���� �˷Ѻ���
	 * @ʱ�䣺2011-6-8����08:11:52
	 * @return
	 * @throws BusinessException
	 */
	public SendBillVO col() throws BusinessException {
		if (billvo == null)
			throw new BusinessException("�������Ϊ��");
		SendBillBodyVO[] bodys = billvo.getBodyVos();
		if (bodys == null || bodys.length == 0) {
			throw new BusinessException("����������Ϊ��");
		}
		// ����ͳ��
		groupByReserver();
		int icoltype = SendBillVO.HAND_COLTYPE;
		for (SendBillBodyVO body : bodys) {
			// ����˼۱�
			icoltype = PuPubVO.getInteger_NullAs(body.getIcoltype(),
					SendBillVO.HAND_COLTYPE);
			if (icoltype == SendBillVO.HAND_COLTYPE)
				continue;
			if (tranprice.containsKey(body.getCsourcebillhid())) {
				curTranpriceBvo = tranprice.get(body.getCsourcebillhid());
			} else {
				curTranpriceBvo = getColBO(icoltype, body).getTranspriceBvo();
				tranprice.put(body.getCsourcebillhid(), curTranpriceBvo);
			}
			curBody = body;
			// ɨ���˼۱��˷Ѻ��㵥
			appendPriceInfor();
			// �����˷�
			setNcolmny();
			// ����
			doSavePriceInfor();
		}
		return billvo;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������շ�����+�ջ��أ��ֲֻ��߾����̣�,����
	 * @ʱ�䣺2011-6-8����07:40:47
	 */
	void groupByReserver() {
		SendBillBodyVO[] bodys = billvo.getBodyVos();
		for (SendBillBodyVO body : bodys) {
			String key = "";
			if (WdsWlPubConst.WDS3.equalsIgnoreCase(body.getCsourcetype())) {
				key = body.getPk_destore() + body.getPk_restore();
			} else {
				key = body.getPk_destore() + body.getPk_trader();
			}
			if (groupNum.containsKey(key)) {
				ArrayList<UFDouble> sumNum = groupNum.get(key);
				UFDouble oldNum = PuPubVO.getUFDouble_NullAsZero(sumNum.get(0));
				UFDouble oldAssNum = PuPubVO.getUFDouble_NullAsZero(sumNum
						.get(1));
				UFDouble newNum = PuPubVO
						.getUFDouble_NullAsZero(body.getNnum());
				UFDouble newAssNum = PuPubVO.getUFDouble_NullAsZero(body
						.getNassnum());
				oldNum = oldNum.add(newNum);
				oldAssNum = oldAssNum.add(newAssNum);
				sumNum.add(0, oldNum);
				sumNum.add(1, oldAssNum);
				groupNum.put(key, sumNum);
			} else {
				ArrayList<UFDouble> sumNum = new ArrayList<UFDouble>();
				sumNum.add(PuPubVO.getUFDouble_NullAsZero(body.getNnum()));
				sumNum.add(PuPubVO.getUFDouble_NullAsZero(body.getNassnum()));
				groupNum.put(key, sumNum);
			}
		}
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ �����˷Ѽ������� ��ȡ��ͬ�� �˷Ѽ���ʵ����
	 * @ʱ�䣺2011-5-19����05:07:40
	 * @param icoltype
	 * @param body
	 * @return
	 */
	private AbstractTranPriceCol getColBO(int icoltype, SendBillBodyVO body) {
		AbstractTranPriceCol cobo = null;
		if (icoltype == SendBillVO.DS_COLTYPE) {
			cobo = getDscol();
		} else if (icoltype == SendBillVO.XS_COLTYPE) {
			cobo = getXscol();
		}
		cobo.setBillvo(billvo);
		cobo.setSendBody(body);
		cobo.setIsSale(getBillVO().isSale());
		cobo.setM_logDate(getBillVO().getM_logDate());
		return cobo;
	}

	public DsTranPriceCol getDscol() {
		if (m_dscol == null)
			m_dscol = new DsTranPriceCol();
		return m_dscol;
	}

	public XsTranPriceCol getXscol() {
		if (m_xscol == null)
			m_xscol = new XsTranPriceCol();
		return m_xscol;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ �����˼���Ϣ �����˼�ɨ����˼���Ϣ���뵽�˷ѵ���
	 * @ʱ�䣺2011-6-8����06:58:47
	 * @throws BusinessException
	 */
	void appendPriceInfor() throws BusinessException {
		if (curBody == null || curTranpriceBvo == null)
			return;
		// �˼۱�����
		curBody.setCpricehid(curTranpriceBvo.getPk_wds_transprice_h());
		curBody.setCpriceid(curTranpriceBvo.getPrimaryKey());
		// �ж��Ƿ���Ҫ �˼۵���
		if (isAdjust()) {
			curBody.setNadjustprice(curTranpriceBvo.getNpriceadj());// �˼۵���ֵ
			curBody.setIadjusttype(curTranpriceBvo.getIadjtype());// �˼۵�������
		}
		// ���۰��޶������޶�
		// ʵ�ʼ۸�=ԭʼ�۸�*��1+����������
		// getTransprice()--�ж���ȡ�����˼ۣ�����ȡ��ɢ�˼�
		UFDouble nrate = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo
				.getNadjrate());
		curBody.setNprice(getTransprice().multiply(nrate.div(100).add(1), 8));
		colAdjust();
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�����ж��Ƿ���Ҫ �˼۵���
	 * @ʱ�䣺2011-6-8����07:19:48
	 * @return
	 */
	public boolean isAdjust() {
		 //�Ƚ� ���������Ƿ������ɢ��������
		String key = "";
		if(WdsWlPubConst.WDS3.equalsIgnoreCase(curBody.getCsourcetype())){
			key =curBody.getPk_destore()+curBody.getPk_restore();
		}else{
			key = curBody.getPk_destore()+curBody.getPk_trader();
		}
		if(groupNum.containsKey(key)){
			ArrayList<UFDouble> sumNum = groupNum.get(key);
			UFDouble nassum = PuPubVO.getUFDouble_NullAsZero(sumNum.get(1));
			UFDouble nadjustnum = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNadjustnum());
			UFDouble num1 = nassum.sub(nadjustnum);
			if(num1.doubleValue()<=0){
				return true;
			}
		}
		return false;
	}

	public UFDouble getTransprice(){
		 UFDouble price = null;
		 price = PuPubVO
			.getUFDouble_NullAsZero(curTranpriceBvo.getNtransprice());
		 //�Ƚ� ���������Ƿ������ɢ��������
		String key = "";
		if(WdsWlPubConst.WDS3.equalsIgnoreCase(curBody.getCsourcetype())){
			key =curBody.getPk_destore()+curBody.getPk_restore();
		}else{
			key = curBody.getPk_destore()+curBody.getPk_trader();
		}
		if(groupNum.containsKey(key)){
			ArrayList<UFDouble> sumNum = groupNum.get(key);
			UFDouble num = PuPubVO.getUFDouble_NullAsZero(sumNum.get(0));
			UFDouble  nsmallnum = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNsmallnum());
			UFDouble num1 = num.sub(nsmallnum);
			if(num1.doubleValue()<=0){
				price = PuPubVO
				.getUFDouble_NullAsZero(curTranpriceBvo.getNsmallprice());
			}
		}
		
		return price;
	 }

	/***************************************************************************
	 * 
	 * @���ߣ�zhf
	 * @˵�����˼۱����˵���ֵ��,��Ҫ�������ֵ
	 * @ʱ�䣺2011-6-8����07:20:25
	 * @throws BusinessException
	 */
	public void colAdjust() throws BusinessException {
		UFDouble nadjustprice = PuPubVO.getUFDouble_NullAsZero(curBody
				.getNadjustprice());
		UFDouble nnum = null;
		if (PuPubVO.getInteger_NullAs(curBody.getIadjusttype(),
				SendBillVO.DS_PRICEUNIT) == SendBillVO.DS_PRICEUNIT) {
			nnum = PuPubVO.getUFDouble_NullAsZero(curBody.getNnum());
		} else {
			nnum = PuPubVO.getUFDouble_NullAsZero(curBody.getNassnum());
		}
		curBody.setNadjustmny(nnum.multiply(nadjustprice, 8));

	}

	/**
	 * 
	 * @throws BusinessException
	 * @���ߣ�zhf
	 * @˵��: �����˷�
	 * @ʱ�䣺2011-5-20����01:22:14
	 */
	void setNcolmny() throws BusinessException {
		int icoltype = SendBillVO.HAND_COLTYPE;
		icoltype = curBody.getIcoltype();
		UFDouble nprice = curBody.getNprice();
		UFDouble nnum = null;
		UFDouble ngls = null;
		if (icoltype == SendBillVO.DS_COLTYPE) {
			nnum = PuPubVO.getUFDouble_NullAsZero(curBody.getNnum());
			String key = curBody.getCsendareaid() + curBody.getCreceiverealid();
			// if(kilometers.containsKey(key)){
			// ngls = kilometers.get(key);
			// }else{
			// ngls =getGls();
			// kilometers.put(key, ngls);
			// }
			ngls = PuPubVO.getUFDouble_NullAsZero(curBody.getNgl());
			curBody.setNcolmny(nprice.multiply(nnum, 8).multiply(ngls, 8));
			curBody.setNmny(nprice.multiply(nnum, 8).multiply(ngls, 8).add(
					curBody.getNadjustmny()));
		} else if (icoltype == SendBillVO.XS_COLTYPE) {
			nnum = PuPubVO.getUFDouble_NullAsZero(curBody.getNassnum());
			curBody.setNcolmny(nprice.multiply(nnum, 8));
			curBody.setNmny(nprice.multiply(nnum, 8).add(
					curBody.getNadjustmny()));
		}
	}

	/**
	 * �����˷���Ϣ
	 * 
	 * @throws BusinessException
	 */
	protected void doSavePriceInfor() throws BusinessException {
		// У�� ���� ������ �˼۱�id �ܽ��;
		curBody.validateOnColSave();
		getDao().updateVO(curBody, SendBillVO.priceInfor_fieldNames);
		String sql = "select ts from wds_transprice_b where pk_wds_transprice_b = '"
				+ curBody.getPrimaryKey() + "'";
		String ts = PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(
				sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
		curBody.setTs(new UFDateTime(ts));
	}

	// /**
	// *
	// * @���ߣ�lyf
	// * @˵�������ɽ������Ŀ
	// * ����̱��� ��ȡ���������Ĺ�����
	// * @ʱ�䣺2011-5-18����07:58:28
	// * @return
	// * @throws BusinessException
	// */
	// public UFDouble getGls() throws BusinessException{
	// String sql = " select mileage from wds_transmil where isnull(dr,0)=0 and
	// pk_delocation='"+curBody.getCsendareaid()+"' and
	// pk_relocation='"+curBody.getCreceiverealid()+"'" ;
	// return PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql,
	// WdsPubResulSetProcesser.COLUMNPROCESSOR));
	// }

}
