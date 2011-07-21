package nc.bs.zb.comments;

import java.util.HashMap;
import java.util.Map;

import nc.bs.zb.pub.ZbBsPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidSlvendorVO;
import nc.vo.zb.comments.ISplitNumPara;
import nc.vo.zb.parmset.ParamSetVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class SplitNumCol2 implements AbstractSplitNumCol{
/**
 * ��Χ��Ӧ�̷����㷨�� �㷨������  ȡ�б�������������̯
 * 1����ȡ��Ӧ�̵÷����
 * 2����ȡ��ε���Χ��Ӧ�̷�̯����
 * 3�������ֺܷͷ�̯���������������Ӧ�̵��޶�
 * 4��׽��Ʒ�ָ��ݹ�Ӧ�̱��۷��ɸߵ��ͽ��з���  ���Ǹ�����Ӧ�̵��޶�
 */
	public SplitNumCol2(ISplitNumPara para){
		super();
		setPara(para);
	}
	public SplitNumCol2(){
		super();
//		setPara(para);
	}
	private SplitNumColPara2 para = null;
	public void setPara(ISplitNumPara para){
		this.para = (SplitNumColPara2)para;
	}
	
	public void col() throws BusinessException{
		if(para == null)
			throw new BusinessException("����Ϊ��");
//		����Ʒ��   ������Ӧ�̸��ݵ÷ְ���̯���� ���������������� ע�������ϼ����������ȫ���ۼƵ����һ����Ӧ�̣���֤����ȫ���ֳ�ȥ
		HYBillVO bill = para.getBill();
		if(bill == null)
			throw new BusinessException("����Ϊ��");
		BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[])bill.getChildrenVO();
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("Ʒ������Ϊ��");
		
		BidSlvendorVO[] vendors = null;
		for(BiEvaluationBodyVO body:bodys){
			vendors = body.getBidSlvendorVOs();
			if(vendors == null || vendors.length == 0)
				throw new BusinessException("��Ӧ�������쳣");
			

			split(body, vendors);
		}
	}	
	
	public void split(BiEvaluationBodyVO body, BidSlvendorVO[] vendors)
			throws BusinessException {
		
		
		UFDouble nallnum = PuPubVO.getUFDouble_NullAsZero(body.getNzbnum());
		if(nallnum.equals(UFDouble.ZERO_DBL)){
			throw new BusinessException("�����б�����Ϊ�յ�Ʒ��");
		}
		
		String srate = para.getSplitrate();
		
		int[] rates = ZbPubTool.colBiddingVendorRates(srate);
		Object[] grades = para.getGrades();
		int len = rates.length;
		
		ParamSetVO vo = ZbBsPubTool.getParam();//��ȡ���� �Ƿ��ַܷ���  add by zhw  2011-06-21 
		if(!PuPubVO.getUFBoolean_NullAs(vo.getFiscoltotal(),UFBoolean.FALSE).booleanValue()){
			if(rates.length != vendors.length || rates.length != grades.length)
				throw new BusinessException("����Χ��Ӧ���������͡��������������ò�һ��");
		}
		
		
		//��Ӧ�̷�����Ϣ
		Map<String, UFDouble> vendorNumInfor = new HashMap<String, UFDouble>();
		
		int iall = 0;
        for(int i = 0;i<len;i++){
		iall += rates[i];	
		}
        if(iall == 0)
        	throw new BusinessException("����Ӧ�̷��������������쳣��0");
		
		UFDouble nunitnum = nallnum.div(iall);//��λ����    ע���б���������������С��λ  �ϼ�   �������ȫ�������һλ��Ӧ��
		UFDouble tmpNum = null;
		UFDouble nallsplitnum = UFDouble.ZERO_DBL;
		Object[] os = null;
		for(int i = 0;i<len;i++){
			os = (Object[])grades[i];
			if(i<len-1){//�����һ����Ӧ��
				tmpNum = nunitnum.multiply(rates[i]);
				if(tmpNum.doubleValue()-tmpNum.intValue()>0){
					tmpNum = new UFDouble(tmpNum.intValue()+1);//����С�����ִ��� �ϼ�
				}
				nallsplitnum = nallsplitnum.add(tmpNum);
				vendorNumInfor.put(ZbPubTool.getString_NullAsTrimZeroLen(os[0]), tmpNum);
			}else{
				tmpNum = nallnum.sub(nallsplitnum);
				if(tmpNum.doubleValue()<=0){//���Ӧ��û������    tmpNum  �����ܴ���1
					vendorNumInfor.put(ZbPubTool.getString_NullAsTrimZeroLen(os[0]), tmpNum.add(UFDouble.ONE_DBL));
					tmpNum = vendorNumInfor.get(ZbPubTool.getString_NullAsTrimZeroLen(((Object[])grades[i-1])[0]));
					//��һ����Ӧ�̼�ȥ 1����  �͸����һ����Ӧ��
					vendorNumInfor.put(ZbPubTool.getString_NullAsTrimZeroLen(((Object[])grades[i-1])[0]), tmpNum.sub(UFDouble.ONE_DBL));
				}else{
					vendorNumInfor.put(ZbPubTool.getString_NullAsTrimZeroLen(os[0]), tmpNum);
				}
			}			
		}
		
		
		for(BidSlvendorVO vendor:vendors){
			tmpNum = vendorNumInfor.get(vendor.getCcustmanid());
			if(tmpNum == null)
				throw new BusinessException("��Ӧ�̷����쳣�����ֿ�ֵ");
			vendor.setNzbnum(tmpNum);
			vendor.setNzbmny(tmpNum.multiply(
				PuPubVO.getUFDouble_NullAsZero(body.getNzbprice()), ZbPubConst.MNY_DIGIT));
			//�����б����
			vendor.setNwinpercent(vendor.getNzbnum().div(body.getNzbnum(), ZbPubConst.NUM_DIGIT).multiply(100));
		}
	}
}
