package nc.bs.zmpub.pub.excel;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;
import nc.vo.zmpub.excel.CodeToIDInfor;
import nc.vo.zmpub.excel.IDefTran;
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
	
	private Map<String, IDefTran> defTranBoMap = null;
	
	Map<String, IDefTran> getDefTranBoMap(){//��֤  �Զ���ת���� �ǵ���
		if(defTranBoMap == null)
			defTranBoMap = new HashMap<String, IDefTran>();
		return defTranBoMap;
	}
	private IDefTran getDefTranTool(CodeToIDInfor infor) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(infor.getDefTranClassName())==null)
			throw new BusinessException("����["+infor.getCodename()+"]�Զ���ת����δע��");
		
		String className = infor.getDefTranClassName().trim();
		if(getDefTranBoMap().containsKey(className))
			return getDefTranBoMap().get(className);
		try {
			IDefTran tool = (IDefTran)Class.forName(className).newInstance();
			getDefTranBoMap().put(className, tool);
			return tool;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			throw new BusinessException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new BusinessException(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			throw new BusinessException(e);
		}
	}
	
	protected  String defTran(CodeToIDInfor infor) throws BusinessException{
		String value =  null;
		IDefTran tranTool =  getDefTranTool(infor);
		value = tranTool.transCodeToID(infor);
		return value;
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
		if(infor.isDefTran.booleanValue()){
			
		}else if (infor.getIsBasic().booleanValue()) {//��׼��Ʒ����������ͨ����ʾ��ȡֵ  Ч�ʽϸ�
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
