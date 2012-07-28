package nc.ui.wds2.set;

import java.util.HashMap;
import java.util.Map;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pub.BusinessException;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds2.set.OutInSetVO;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubConst;

public class OutInSetHelper {


	private static Map<String,String> typeInfor = null;

	static Map<String,String> getTypeInfor(){
		if(typeInfor == null)
			typeInfor = new HashMap<String, String>();
		return typeInfor;
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ��浥�ݻ�ȡĬ���շ����  �����շ����Ĭ������
	 * @ʱ�䣺2012-7-16����12:18:02
	 * @param bm ���ݽ���ģ����
	 * @param orderFieldName ���������ֶ�����
	 * @param isout �Ƿ����
	 * @return
	 * @throws BusinessException
	 */
	public static String getDefaultOutInTypeID(BillModel bm,String orderFieldName,boolean isout) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(orderFieldName)==null)
			return getDefaultOutInTypeID(Wds2WlPubConst.other, isout);
		if(bm.getItemByKey(orderFieldName) == null)
			return null;
		if(bm.getRowCount() == 0){
			return getDefaultOutInTypeID(Wds2WlPubConst.other, isout);
		}
		if(bm.getRowCount() < 0)
			return null;
		String ordertype = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(0, orderFieldName));
		
		if(ordertype == null)
			return null;
		
		return getDefaultOutInTypeID(ordertype, isout);
	}

	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ݶ����������ͻ�ȡ��浥��Ĭ���շ���𣨻����������˵��������ͣ�
	 * @ʱ�䣺2012-7-13����05:13:23
	 * @param ordertype �˵�����
	 * @param isout �Ƿ����
	 * @return
	 * @throws BusinessException
	 */
	public static String getDefaultOutInTypeID(String ordertype,boolean isout) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(ordertype) == null)
			return null;
		String key = ordertype+isout;
		if(getTypeInfor().containsKey(key)){
			return getTypeInfor().get(key);
		}

		//		�����ݿ��ȡ��Ϣ

		OutInSetVO[] vos = (OutInSetVO[])HYPubBO_Client.queryByCondition(OutInSetVO.class, 
				" pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");

		if(vos == null || vos.length == 0)
			return null;

		for(OutInSetVO vo:vos){
			getTypeInfor().put(getKey(vo.getIbiztype())+isout,isout?vo.getCouttypeid():vo.getCintypeid());
		}

		return getTypeInfor().get(key);
	}

	public final static int tw = 0;//ת�ֲ�
	public final static int tc = 1;//ת��λ
	public final static int so = 2;//���۳�
	public final static int to = 3;//������
	public final static int ti = 4;//������
	public final static int virtual = 5;//����
	public final static int special = 6;//����
	public final static int other = 7;//����
	public final static int back = 8;//�����˻����

	private static String getKey(int ibiztype){
		switch (ibiztype) {
		case tw:
			return WdsWlPubConst.WDS3;
		case tc:
			return WdsWlPubConst.HWTZ;
		case so:
			return WdsWlPubConst.WDS5;
		case to:
			return WdsWlPubConst.WDSH;
		case ti:
			return Wds2WlPubConst.billtype_alloinsendorder;
		case virtual:
			return Wds2WlPubConst.virtual;
		case special:
			return WdsWlPubConst.WDSS;
		case other:
			return Wds2WlPubConst.other;
		case back:
			return ScmConst.m_saleOut;
		default:
			return Wds2WlPubConst.other;
		}
	}

}
