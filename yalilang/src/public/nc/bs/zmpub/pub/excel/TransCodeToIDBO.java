package nc.bs.zmpub.pub.excel;

import java.util.HashMap;
import nc.bs.dao.BaseDAO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;
import nc.vo.zmpub.excel.CodeToIDInfor;
import nc.vo.zmpub.pub.tool.ResultSetProcessorTool;

public class TransCodeToIDBO {

	//����+��˾ID   ---->  ID       ���ձ�  ԭ����Ϊ����ȫϵͳΨһ   ���ܴ��ڱ��빫˾��Ψһ
	private java.util.Map<String,String> codeIdMap = new HashMap<String, String>();
	
	private static TransCodeToIDBO tb = new TransCodeToIDBO();
	
	private BaseDAO dao = null;

	private BaseDAO getDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	private TransCodeToIDBO(){
		super();
	}
	
	public static TransCodeToIDBO getInstance(){
		return tb;
	}
	
	public void clearCathe(){
		codeIdMap.clear();
	}
	
	
	/**
	 * ��ȡIDֵ
	 * @param infor
	 * @return
	 * @throws BusinessException
	 */
	private String getInforValue(CodeToIDInfor infor)
			throws BusinessException {
		
		String key = null;
		if(infor.getIsCorp().booleanValue()){
//			����+��˾ID  ��Ϊkey
			key = infor.getCodevalue()+infor.getCorpvalue();
		}else{
			key = infor.getCodevalue();
		}
		
		if (codeIdMap.containsKey(key))
			return codeIdMap.get(key);

		String value = null;
		if (infor.getIsBasic().booleanValue()) {//��׼��Ʒ����������ͨ����ʾ��ȡֵ  Ч�ʽϸ�
			String fou = infor.getFomular();
			value = WdsWlPubTool.getString_NullAsTrimZeroLen(WdsWlPubTool
					.execFomular(fou, new String[] { infor.getCodename() },
							new String[] { infor.getCodevalue() }));

		} else {
			String sql = infor.getSelectSql();
			value = WdsWlPubTool.getString_NullAsTrimZeroLen(getDAO()
					.executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
		}

		codeIdMap.put(key, value);
		return value;
	}


	/**
	 * ����ת��ΪID  ����excel����   �����ֶκ�ID�ֶ�  �� ����vo�� ռ��ͬһ���ֶΣ�
	 * ��ת��������Ҫ�������ڴ�����Ĺ�˾���룻ʵ�ʲ���ҪҲ���������ṩ��
	 * �����ת���ı������Ϊ��˾����  Ҳ���Բ��ṩ��˾�����ֶΣ������ݱ�ͷ�й�˾�����޹�˾��������ת��ʱ���ڹ�˾������
	 * �Ļ��������⴫�빫˾�������
	 * @author zhf
	 * @param vos
	 * @param infors
	 * @throws BusinessException
	 */
	protected  void transCodeToID(CircularlyAccessibleValueObject[] vos,CodeToIDInfor[] infors) throws BusinessException{

//		ת����ʼ
		String tmpValue = null;
		CodeToIDInfor corpInfor = null;
		for (CircularlyAccessibleValueObject vo : vos) {

			// ��Ҫȷ����ǰvo�������ڵĹ�˾ �����ǵ��뵽�Ǹ���˾�� ���뵽��ͬ�Ĺ�˾ ��������Ӧ����ͬ��˾����
			// ����ת����˾ ����� ���ݱ������� û��ʵ�ʹ�˾�ֶε� ҲӦ�ṩ���⹫˾�ֶ� �ڴ˴���ʱʹ��
			for (CodeToIDInfor infor : infors) {
				if (infor.isCorpField.booleanValue()) {
					corpInfor = infor;
					infor.setCodevalue(WdsWlPubTool//��ȡ��˾����ֵ
							.getString_NullAsTrimZeroLen(vo
									.getAttributeValue(infor.getCodename())));
					
					tmpValue = getInforValue(infor);//��ȡ��˾IDֵ

//					���湫˾��Ϣ
					corpInfor.setCorpname(infor.getCodename());
					corpInfor.setCorpvalue(tmpValue);
					
					vo.setAttributeValue(infor.getCodename(), tmpValue);// Ϊ��˾�ֶθ���ID
					break;
				}
			}

			for (CodeToIDInfor infor : infors) {
				if (infor.isCorpField.booleanValue())
					continue;
				
				infor.setCorpvalue(corpInfor.getCorpvalue());//��˾IDֵ
//				��ǰ����ֵ
				infor.setCodevalue(WdsWlPubTool.getString_NullAsTrimZeroLen(infor.getCodename()));
				if(PuPubVO.getString_TrimZeroLenAsNull(infor.getCorpname())==null)
					infor.setCorpname(corpInfor.getCorpname());
				
				tmpValue = getInforValue(infor);
				
				vo.setAttributeValue(infor.getCodename(), tmpValue);// Ϊ��˾�ֶθ���ID
			}
		}
	}

}
