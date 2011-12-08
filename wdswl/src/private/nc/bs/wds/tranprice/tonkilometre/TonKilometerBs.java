package nc.bs.wds.tranprice.tonkilometre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.BsNotNullCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.tranprice.tonkilometre.TranspriceHVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author Administrator �ֹ����˼۱��̨У����
 */
public class TonKilometerBs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * 
	 * @throws DAOException
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ ����ǰУ�� �Ѿ��������ĵ����Ƿ���� ���ڷ�Χ����
	 * @ʱ�䣺2011-5-24����01:05:16
	 */
	public void beforApprove(AggregatedValueObject vo) throws BusinessException {
		if (vo == null)
			return;
		TranspriceHVO hvo = (TranspriceHVO) vo.getParentVO();
		UFBoolean fisbigflour= PuPubVO.getUFBoolean_NullAs(hvo.getFisbigflour(),UFBoolean.FALSE);//�Ƿ�����
		StringBuffer sql = new StringBuffer();
		sql.append("select * from wds_transprice_h where isnull(dr,0)=0  ");
		sql.append(" and vbillstatus='1'");// ����ͨ����
		sql.append(" and reserve1='"+hvo.getReserve1()+"'");//�ֿ�
		sql.append(" and carriersid='"+hvo.getCarriersid()+"'");//������ 	
		sql.append(" and fisbigflour='"+fisbigflour+"'");//�Ƿ����� 	
		sql.append(" and pk_billtype='" + WdsWlPubConst.WDSI + "'");
		sql.append(" and( (dstartdate<='" + hvo.getDstartdate()
				+ "' and denddate>='" + hvo.getDstartdate() + "')");
		sql.append(" or (dstartdate<='" + hvo.getDenddate()
				+ "' and denddate>='" + hvo.getDenddate() + "')");
		sql.append(" or (dstartdate>='" + hvo.getDstartdate()
				+ "' and denddate<='" + hvo.getDenddate() + "'))");
		List<TranspriceHVO> list = (ArrayList<TranspriceHVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(TranspriceHVO.class));
		if (list.size() > 0) {
			TranspriceHVO oldHvo = list.get(0);
			throw new BusinessException("���Ѿ���������ͬ�ֿ����ͬ��������ͬ�Ĵ���� �ֹ����˼۱�������ڽ���:\n���ݱ��="
					+ oldHvo.getVbillno() +"\n�˼۱���="+oldHvo.getVpricecode()+"\n�˼�����="+oldHvo.getVpricename()+"\n��ʼ����=" + oldHvo.getDstartdate()
					+ "\n��ֹ����=" + oldHvo.getDenddate());
		}

	}
//	public void beforeSaveCheck(AggregatedValueObject vo) throws Exception{
//		//������У��
//		BsNotNullCheck.FieldNotNull(new SuperVO[]{(SuperVO)vo.getParentVO()},new String[]{"vbillno","pk_billtype","reserve1","carriersid","dstartdate","denddate"},new String[]{"���ݺ�","��������","�����ֿ�","������","��ʼ����","��������"});
//		BsNotNullCheck.FieldNotNull((SuperVO[])vo.getChildrenVO(),new String[]{"ntransprice","pk_replace"},new String[]{"�˼�","�ջ����"});
//		// Ψһ�Ե�У�� 
//	    //���ݺŹ�˾��Ψһ
//		BsUniqueCheck.FieldUniqueCheck((SuperVO)vo.getParentVO(), new String[]{"vbillno","pk_billtype","pk_corp"}, "[  ���ݺ�  ] �����ݿ����Ѿ�����");			
//		//��ʼ���ڴ��ڽ�ֹ���ڵ�У��
//		if(vo.getParentVO()!=null){
//			TranspriceHVO hvo=(TranspriceHVO) vo.getParentVO();
//			if(hvo.getDstartdate().after(hvo.getDenddate())){
//				throw new BusinessException("��ʼ���ڲ��ܴ��ڽ�ֹ����");
//			}
//		}
//		//�����ֿ� ������ �������� �Ƿ�ԭ�Ϸ� ���Ψһ  
//		//  ��� ��ʼ���� �� ��ֹ���ڲ��ܽ���
//		BsUniqueCheck.FieldUniqueCheckInment((SuperVO)vo.getParentVO(),new String[]{"reserve1","fisbigflour","carriersid","pk_billtype"}, "dstartdate","denddate"," �Ѿ����� �˸� [�����ֿ�]  [������] [�Ƿ�����] �µ�����ڼ�ε��˼۱� ");		
//		//�ջ����Ӧ�÷�Χ����Ψһ��У��
//		validateBodyRePlace(vo.getChildrenVO(),new String[]{"pk_replace","ifw"},new String[]{"�ջ����","Ӧ�÷�Χ"});	
//	}
	/*
	 * �ջ���� Ӧ�÷�Χ����Ψһ��У��
	 */
	private void validateBodyRePlace(CircularlyAccessibleValueObject[] chs,String[] fields,String[] displays) throws Exception{
		if(chs==null || chs.length==0){
			return;
		}
		int num =chs.length;
		if(fields == null || fields.length == 0){
			return;
		}
		if(num>0){
			ArrayList<String> list = new ArrayList<String>();
			for(int i = 0 ;i<num; i++){
				String key = "";
				for(String str : fields){
					Object o1 =chs[i].getAttributeValue(str);
					key = key + ","+String.valueOf(o1);
				}
				String dis="";
				for(int j=0;j<displays.length;j++){
					   dis=dis+"[ "+displays[j]+" ]";
					}
					
				if(list.contains(key)){							
					throw new BusinessException("��["+(i+1)+"]�б����ֶ� "+dis+" �����ظ�!");
				}else{
					list.add(key);
				}
				//���Ӧ�� ��Χ  Ϊ ȫ�� �鿴����վ���ջ�վ��ͬ����� Ӧ�÷�Χ�Ƿ���ڰ���
				// Ӧ�÷�Χ ȫ�� �� 0 ,������ 1,�ֲ�  2  
				if("0".equals(chs[i].getAttributeValue(fields[1]).toString())){
					String[] strs=key.split(",");
					
					if(list.contains(","+strs[1]+","+"1") || list.contains(","+strs[1]+","+"2")){
						throw new Exception("��["+(i+1)+"]�б����ֶ� "+dis+" ����[ Ӧ�÷�Χ ] �İ���!");
					}
				}
				//���Ӧ�� ��Χ  Ϊ ������ �� �ֲ� �鿴����վ���ջ�վ��ͬ����� Ӧ�÷�Χ�Ƿ���ڰ���
				if("1".equals(chs[i].getAttributeValue(fields[1]).toString()) || "2".equals(chs[i].getAttributeValue(fields[1]).toString()) ){
                    
					String[] strs=key.split(",");
					
					if(list.contains( ","+strs[1]+","+"0") ){
						throw new Exception("��["+(i+1)+"]�б����ֶ� "+dis+" ����[ Ӧ�÷�Χ ] �İ���!");
					}
				}
				
			}
		}	
	}

}
