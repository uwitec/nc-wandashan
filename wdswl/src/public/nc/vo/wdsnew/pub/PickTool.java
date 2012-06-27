package nc.vo.wdsnew.pub;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.wdsnew.pub.BillStockBO1;
import nc.jdbc.framework.util.SQLHelper;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
/**
 * ���ɽ�������ⵥ�Զ����
 * @author mlr
 */
public class PickTool implements Serializable{
	private static final long serialVersionUID = -6131447795689577612L;
	//�����
	private Map<String,List<StockInvOnHandVO>> mpick=new HashMap<String, List<StockInvOnHandVO>>();	
	private BillStockBO1 stock=null;
	private BillStockBO1 getStock(){
		if(stock==null){
			stock=new BillStockBO1();
		}
		return stock;
	}
	/**
	 * �÷���ǰ̨ ����Զ�̵��� (��ѯ���ݿ�)
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * 	1 ����  �ֿ� ��λ  ���  ��ѯ�ִ���  �����Ƚ��ȳ�ԭ��  ���

	    2������� �γɼ����  ���������  ��һ��map  key=���ⵥ����id  ��value=�ִ���vo

	    3���¹��������  ���ⵥ����  

	    4 �������� 
	 * @ʱ�䣺2012-6-20����09:56:08
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param bvos
	 * @return
	 * @throws Exception 
	 */
	public TbOutgeneralBVO[] autoPick(String pk_stordoc,String pk_cargdoc ,TbOutgeneralBVO[] bvos) throws Exception{
		if(pk_stordoc==null || pk_stordoc.length()==0)
			throw new Exception("����ֿ�Ϊ��");
		if(pk_cargdoc==null || pk_cargdoc.length()==0)
			throw new Exception("�����λΪ��");		
		//��ռ����
		mpick.clear();
		if(bvos==null || bvos.length==0)
			return null;
		for(int i=0;i<bvos.length;i++){
			pick(pk_stordoc,pk_cargdoc,bvos[i]);
		}	
		return createBill(bvos);
	}
	/**
	 * ���ݼ���� ���¹������ⵥ������Ϣ
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-6-20����10:43:38
	 * @return
	 * @throws Exception 
	 */
	private  TbOutgeneralBVO[] createBill(TbOutgeneralBVO[] bvos) throws Exception {
		List<TbOutgeneralBVO> list=new ArrayList<TbOutgeneralBVO>();//������¹����ı�������
		for(int i=0;i<bvos.length;i++){
		   String pk=bvos[i].getPrimaryKey();	
		   //ȡ�����еļ����
		   List<StockInvOnHandVO> li= mpick.get(pk);	
		   //���û���ִ��� ���б��ֲ���
		   if(li==null|| li.size()==0){
			   list.add(bvos[i]);
		   }else{
			//����  ���ݼ���� ���¹���  ���β��к�ı���
			 for(int j=0;j<li.size();j++){
				 TbOutgeneralBVO vo=(TbOutgeneralBVO) ObjectUtils.serializableClone(bvos[i]);
				 vo.setVbatchcode(li.get(j).getWhs_batchcode());//��������
				 vo.setAttributeValue("nshouldoutassistnum", li.get(j).getAttributeValue("whs_omnum"));//����Ӧ��������
				 vo.setAttributeValue("noutassistnum", li.get(j).getAttributeValue("whs_oanum"));//����ʵ��������
				 list.add(vo);
			 }   
		   }	   
		}
		return list.toArray(new TbOutgeneralBVO[0]);		
	}
	/**
	 * ǰ̨����Զ�̵��� (��ѯ���ݿ�)
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *       ���м�� ���� �ֿ�  ��λ  ���  ��ѯ�ִ������ 
	 *       ����������  ��������
	 * @ʱ�䣺2012-6-20����10:04:23
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param tbOutgeneralBVO
	 * @throws Exception 
	 */
	private void pick(String pk_stordoc, String pk_cargdoc,
			TbOutgeneralBVO vo) throws Exception {
		//������ѯ����
		String whereSql= " pk_customize1 = '"+pk_stordoc+"' " +
				        " and  pk_cargdoc = '"+pk_cargdoc+"' "+
						" and pk_invmandoc='"+vo.getCinventoryid()+"'"+
						" and isnull(dr,0)=0 "+
						" and pk_corp='"+SQLHelper.getCorpPk()+"'"+
		                " and whs_stockpieces >0 ";//�������������0
		//��ѯ�ִ���
		StockInvOnHandVO[] stocks=(StockInvOnHandVO[]) getStock().queryStock(whereSql);
		//��ʼ���
		   //�������  ��������
		   spiltNum(stocks,vo);				
	}
	/**
	 * �������  
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-6-20����10:25:33
	 * @param stocks �ִ��� 
	 * @param vo  ���ⵥ����
	 */
	private void spiltNum(StockInvOnHandVO[] vos, TbOutgeneralBVO vo) {
		
	    UFDouble zbnum=PuPubVO.getUFDouble_NullAsZero(vo.getNshouldoutassistnum());//ȡ�ó��ⵥӦ��������
	    if(vos==null|| vos.length==0){
	    	mpick.put(vo.getPrimaryKey(), null);//����ִ���Ϊ��   ����м��������Ϊ��
	    	return;
	    }	
		if(zbnum.doubleValue()==0){
			mpick.put(vo.getPrimaryKey(), null);//������ⵥʵ��������Ϊ0  ����м��������Ϊ��
			return;
		}	
		//���з���  
		//�� ���κ�  ��С���� ���η���
		List<StockInvOnHandVO> list=new ArrayList<StockInvOnHandVO>();
		for(int i=0;i<vos.length;i++){
			UFDouble bnum=PuPubVO.getUFDouble_NullAsZero(vos[i].getAttributeValue("whs_stockpieces"));	
			if(zbnum.doubleValue()>bnum.doubleValue()){
				if (i == vos.length - 1) {
					vos[i].setAttributeValue("whs_omnum", zbnum);// ����Ӧ������(������)		
					vos[i].setAttributeValue("whs_oanum", bnum);//����ʵ������
				} else {
					zbnum = zbnum.sub(bnum);
					vos[i].setAttributeValue("whs_omnum", bnum);// ����Ӧ������(������)																	
					vos[i].setAttributeValue("whs_oanum", bnum);// ����ʵ������(������)
				}
				list.add(vos[i]);
			}else if(zbnum.doubleValue()<bnum.doubleValue()){
				vos[i].setAttributeValue("whs_omnum", zbnum);//����Ӧ������ (������)
				vos[i].setAttributeValue("whs_oanum", zbnum);//����ʵ������(������)
				list.add(vos[i]);
				break;
			}else{
				vos[i].setAttributeValue("whs_omnum", zbnum);//����Ӧ������ (������)
				vos[i].setAttributeValue("whs_oanum", zbnum);//����ʵ������(������)
				list.add(vos[i]);
				break;
			}		
		}
		mpick.put(vo.getPrimaryKey(), list);
	}
	/**
	    * ������� 
	    * @���ߣ�mlr
	    * @˵�������ɽ������Ŀ 
	    * @ʱ�䣺2012-6-20����11:08:10
	    * @param vos
	    * @param row
	    * @param zbnum
	    */
		public  void spiltNum(List<StockInvOnHandVO> vos, int row, UFDouble zbnum) {
			List<StockInvOnHandVO> list=new ArrayList<StockInvOnHandVO>();
			for(int i=0;i<vos.size();i++){
				UFDouble bnum=PuPubVO.getUFDouble_NullAsZero(vos.get(i).getAttributeValue("whs_stockpieces"));	
				if(zbnum.doubleValue()>bnum.doubleValue()){
					if (i == vos.size() - 1) {
						vos.get(i).setAttributeValue("whs_omnum", zbnum);// ����Ӧ������																		// (������)
						vos.get(i).setAttributeValue("whs_oanum", bnum);// ����ʵ������(������)
					} else {
						zbnum = zbnum.sub(bnum);
						vos.get(i).setAttributeValue("whs_omnum", bnum);// ����Ӧ������																	// (������)
						vos.get(i).setAttributeValue("whs_oanum", bnum);// ����ʵ������(������)
					}
					list.add(vos.get(i));
				}else if(zbnum.doubleValue()<bnum.doubleValue()){
					vos.get(i).setAttributeValue("whs_omnum", zbnum);//����Ӧ������ (������)
					vos.get(i).setAttributeValue("whs_oanum", zbnum);//����ʵ������(������)
					list.add(vos.get(i));
					break;
				}else{
					vos.get(i).setAttributeValue("whs_omnum", zbnum);//����Ӧ������ (������)
					vos.get(i).setAttributeValue("whs_oanum", zbnum);//����ʵ������(������)
					list.add(vos.get(i));
					break;
				}		
			}		
		}
}
