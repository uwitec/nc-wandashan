package nc.bs.ic.tpyd;
import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.CombinVO;

public class TpydBO {

	private BaseDAO dao = null;
	private StockInvOnHandBO sbo = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	StockInvOnHandBO getStockBO() {
		if (sbo == null) {
			sbo = new StockInvOnHandBO(getBaseDAO());
		}
		return sbo;
	}

	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ ������Ļ�д ���Ƴ������̽���״̬�ĸ��� �����������γ��µ��ִ���
	 * @ʱ�䣺2011-7-20����09:18:29
	 * @throws Exception
	 */
	public void writeBack(AggregatedValueObject vo) throws Exception {
		if(vo.getChildrenVO()==null ||vo.getChildrenVO().length==0){
			throw new Exception("����Ϊ��");
		}
		SuperVO[] vos = (SuperVO[]) vo.getChildrenVO();
		valuteOutCdt(vos);
		valuteInCdt(vos);
		//��ֹ������У��
		afterValute(vos);
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 *       ���ƶ����������У��,��ֹ�Ƴ����̳��ָ����
	 * @ʱ�䣺2011-7-21����09:59:03
	 * @param vos
	 * @throws Exception 
	 */
	private synchronized void afterValute(SuperVO[] vos) throws Exception{
	 if(vos==null ||vos.length==0)
		 throw new Exception("��������Ϊ��");
	 SuperVO[] newVos = CombinVO.combinVoByFields(vos,new String[] {"pk_trayout" }, new int[] {nc.vo.wl.pub.IUFTypes.UFD, nc.vo.wl.pub.IUFTypes.UFD },new String[] { "nmovenum", "nmoveassnum" });
	 int size1=newVos.length;
	 String pk_trayout=null;//�Ƴ���������
	 UFDouble dnum=null;// ��ȡ��ǰ���̵Ŀ��������
	 UFDouble dbnum =null;// ��ȡ��ǰ���̵Ŀ�渨����
	 StockInvOnHandVO svo=null;//�ִ���vo(�����γɴ��״̬)
	 for(int i=0;i<size1;i++){
		    pk_trayout=PuPubVO.getString_TrimZeroLenAsNull(newVos[i].getAttributeValue("pk_trayout"));		 
			Object o = getBaseDAO().retrieveByClause(StockInvOnHandVO.class," isnull(tb_warehousestock.dr,0)=0 and  tb_warehousestock.pplpt_pk='"+ pk_trayout + "'");
			if(o==null){
				throw new Exception("����Ϊ ["+newVos[i].getAttributeValue("outtraycode")+"]���Ƴ����̴���Ϊ�ջ������Ѿ�������");	
			}				
			List list = (List) o;
			if(list==null&& list.size()==0)
				throw new Exception("����Ϊ ["+newVos[i].getAttributeValue("outtraycode")+"]���Ƴ����̴���Ϊ�ջ������Ѿ�������");	 
			svo = (StockInvOnHandVO) list.get(0);
			
			dnum = PuPubVO.getUFDouble_NullAsZero(svo.getWhs_stocktonnage());						
			dbnum = PuPubVO.getUFDouble_NullAsZero(svo.getWhs_stockpieces());
			if (dnum.doubleValue() < 0 || dbnum.doubleValue() < 0) {
				throw new Exception("����Ϊ  ["+ newVos[i].getAttributeValue("outtraycode")+ "]�������Ƴ�����������ǰ���̵Ĵ���");
			}
	 }		
}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *  
     * ���������̵�У�� ͬһ�����̣���������� ���� ������ǰ���̵�����
		      
	 * @ʱ�䣺2011-7-20����03:15:57
	 * @param vos
	 * @throws Exception
	 */
	private void valuteInCdt(SuperVO[] vos) throws Exception {	
		SuperVO[] newVos1 = CombinVO.combinVoByFields(vos,new String[] {"pk_trayin"}, new int[] {nc.vo.wl.pub.IUFTypes.UFD, nc.vo.wl.pub.IUFTypes.UFD },new String[] { "nmovenum", "nmoveassnum" });
		int size1 = newVos1.length;
		UFDouble rnum =null;//������������(������)
		UFDouble rbnum =null;//������������(������)
		String pk_trayin =null;//������������		
		String pk_trayout=null;//�Ƴ���������	
		Integer cdt_traystatus =null;//����״̬ 0 ��ʾδռ��, 1��ʾ��ռ��
		StockInvOnHandVO hvo = null;//�ִ���vo(�����γɴ��״̬)
		for (int i = 0; i < size1; i++) {			
			 rnum=PuPubVO.getUFDouble_NullAsZero(newVos1[i].getAttributeValue("nmovenum"));		
			 rbnum=PuPubVO.getUFDouble_NullAsZero(newVos1[i].getAttributeValue("nmoveassnum"));
			 pk_trayout=PuPubVO.getString_TrimZeroLenAsNull(newVos1[i].getAttributeValue("pk_trayout"));			
			 pk_trayin=PuPubVO.getString_TrimZeroLenAsNull(newVos1[i].getAttributeValue("pk_trayin"));		
			// ����ǰ��У��
			// ��ѯ��λ������Ϣ��鿴���������Ƿ�Ϊ��,���Ϊ��,����������			
			String sql = " select cdt_traystatus  from bd_cargdoc_tray w where isnull(w.dr,0)=0 and w.cdt_pk='"+ pk_trayin + "'";
			Object o = getBaseDAO().executeQuery(sql,WdsPubResulSetProcesser.COLUMNPROCESSOR);
			if(o==null){
				throw new Exception("����Ϊ ["+newVos1[i].getAttributeValue("intarycode")+"]�����̲�����" );
			}
		    cdt_traystatus = PuPubVO.getInteger_NullAs(o, -1);
			if (cdt_traystatus != StockInvOnHandVO.stock_state_null) {
				throw new Exception("����Ϊ  ["+newVos1[i].getAttributeValue("intarycode")+ "]�������Ѿ���ռ��");	
			}
		    // ������������̵��� �γ��ִ�����¼		
		    // ���Ȳ鴦�Ƴ������̵��ִ���vo
			Object o1 = getBaseDAO().retrieveByClause(StockInvOnHandVO.class," isnull(tb_warehousestock.dr,0)=0 and  tb_warehousestock.pplpt_pk='"+ pk_trayout + "'");
			if(o1==null){
				throw new Exception("����Ϊ ["+newVos1[i].getAttributeValue("outtraycode")+"]���Ƴ����̴���Ϊ�ջ������Ѿ�������");
			}           			    
			List list = (List) o1;
			if(list==null || list.size()==0){
				throw new Exception("����Ϊ ["+newVos1[i].getAttributeValue("outtraycode")+"]���Ƴ����̴���Ϊ�ջ������Ѿ�������");	
			}
			StockInvOnHandVO old = (StockInvOnHandVO) list.get(0);
			hvo = (StockInvOnHandVO) old.clone();
			if(hvo==null){
				throw new Exception("����Ϊ ["+newVos1[i].getAttributeValue("outtraycode")+"]���Ƴ����̴���Ϊ�ջ������Ѿ�������");	
			}
		 	hvo.setPrimaryKey(null);
			hvo.setPplpt_pk(pk_trayin);
			hvo.setWhs_omnum(rnum);
			hvo.setWhs_oanum(rbnum);
			hvo.setWhs_stocktonnage(rnum);
			hvo.setWhs_stockpieces(rbnum);
			hvo.setWhs_status(0);
			getBaseDAO().insertVO(hvo);
			//����λ������Ϣ��  ��Ӧ����״̬��Ϊռ��
		    String sql1="update bd_cargdoc_tray set cdt_traystatus=1 where isnull(dr,0)=0 and cdt_pk='"+pk_trayin+"'";
			getBaseDAO().executeUpdate(sql1);		
			}			
		}
		
	


	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ ���Ƴ����̵�У��
	 * 
	 * @ʱ�䣺2011-7-20����11:11:25
	 * @param vos
	 *            ����vo����
	 */
	private void valuteOutCdt(SuperVO[] vos) throws Exception{
		// ���Ƴ����̱�����ͬ�ĺϲ���һ�� ���������� ���
		SuperVO[] newVos = CombinVO.combinVoByFields(vos,new String[] {"pk_trayout" }, new int[] {nc.vo.wl.pub.IUFTypes.UFD, nc.vo.wl.pub.IUFTypes.UFD },new String[] { "nmovenum", "nmoveassnum" });
		
		UFDouble rnum =null;//�ƶ�����(������)
		UFDouble rbnum=null;//�ƶ�����(������)
		String pk_trayout=null;//�Ƴ���������
		UFDouble dnum=null;// ��ȡ��ǰ���̵Ŀ��������
		UFDouble dbnum =null;// ��ȡ��ǰ���̵Ŀ�渨����
		StockInvOnHandVO svo=null;//�ִ���vo(�����γɴ��״̬)
		if (newVos != null && newVos.length != 0) {
			// У���Ƴ�����
			int size = newVos.length;
			for (int i = 0; i < size; i++) {
				// ����ǰ��У��
				// ���Ƴ����̵�У��,��ѯ���״̬,�鿴���̵�ǰ�ִ����Ƿ� �Ƴ�����
				// ���Ƴ����̵ĸ�����У��
				pk_trayout=PuPubVO.getString_TrimZeroLenAsNull(newVos[i].getAttributeValue("pk_trayout"));			
				rnum=PuPubVO.getUFDouble_NullAsZero(newVos[i].getAttributeValue("nmovenum"));		
				rbnum=PuPubVO.getUFDouble_NullAsZero(newVos[i].getAttributeValue("nmoveassnum"));				
				Object o = getBaseDAO().retrieveByClause(StockInvOnHandVO.class," isnull(tb_warehousestock.dr,0)=0 and  tb_warehousestock.pplpt_pk='"+ pk_trayout + "'");
				if(o==null){
					throw new Exception("����Ϊ ["+newVos[i].getAttributeValue("outtraycode")+"]���Ƴ����̴���Ϊ�ջ������Ѿ�������");	
				}				
				List list = (List) o;
				if(list==null || list.size()==0)
					throw new Exception("����Ϊ ["+newVos[i].getAttributeValue("outtraycode")+"]���Ƴ����̴���Ϊ�ջ������Ѿ�������");	 
				svo = (StockInvOnHandVO) list.get(0);
				
				dnum = PuPubVO.getUFDouble_NullAsZero(svo.getWhs_stocktonnage());						
				dbnum = PuPubVO.getUFDouble_NullAsZero(svo.getWhs_stockpieces());
				if (dnum.doubleValue() < rnum.doubleValue()|| dbnum.doubleValue() < rbnum.doubleValue()) {
					throw new Exception("����Ϊ  ["+ newVos[i].getAttributeValue("outtraycode")+ "]�������Ƴ�����������ǰ���̵Ĵ���");
				} else {
					// ��ǰ���̴������Ƴ�����
					// �Ӵ��״̬�н��Ƴ���������
					svo.setWhs_stockpieces(dnum.sub(rnum));
					svo.setWhs_stocktonnage(dbnum.sub(rbnum));
					if (svo.getWhs_stockpieces().doubleValue() == 0&& svo.getWhs_stocktonnage().doubleValue() == 0) {
						svo.setWhs_status(1);
						//������̴���Ϊ�յĻ�������λ������Ϣ��,��Ӧ���̵�״̬��Ϊδռ��״̬					
						String sql1="update bd_cargdoc_tray set cdt_traystatus="+StockInvOnHandVO.stock_state_null+" where isnull(dr,0)=0 and cdt_pk='"+pk_trayout+"'";
						getBaseDAO().executeUpdate(sql1);	
					}
					getStockBO().updateWarehousestock(svo);
				}
			}		
		}
	}
}

/**
 * 
 * @throws Exception
 * @���ߣ�lyf
 * @˵�������ɽ������Ŀ �����ƶ� ������д�� ��� �Ƴ������Ƿ��Ѿ��仯���󶨴��û�иı�,�������û�б仯,�ƶ�����û�г�����; ���
 *             Ŀ�������ǿ��ã��󶨴��û�иı䣬��ȻΪ��,�ƶ�����û�г�����;
 *             ���ͨ������ȥ�Ƴ����̿��������������������ȫ���ߣ���ı�״̬Ϊ����; �����������ӿ���������ı�״̬Ϊռ��;
 * @ʱ�䣺2011-4-10����06:59:34
 */
// public void writeBack(AggregatedValueObject obj ) throws Exception{
// if(obj == null || obj.getParentVO()==null
// || obj.getChildrenVO() == null
// || obj.getChildrenVO().length ==0){
// return ;
// }
// TpydHVO hvo = (TpydHVO )obj.getParentVO();
// TpydBVO[] bvos = (TpydBVO[])obj.getChildrenVO();
// StringBuffer sql = new StringBuffer();
// sql.append("select count(0) from tb_warehousestock where isnull(dr,0)=0 and pk_corp='"+hvo.getPk_corp()+"'");
// sql.append(" and pk_cargdoc=? and pplpt_pk=? and pk_invmandoc=? and whs_stocktonnage=? and whs_stockpieces=?");
// SQLParameter para = new SQLParameter() ;
// int i=1;
// for(TpydBVO bvo :bvos){
// para.addParam(hvo.getPk_cargedoc());
// para.addParam(bvo.getPk_trayout());
// para.addParam(bvo.getPk_invmandoc());
// para.addParam(bvo.getNoutnum());
// para.addParam(bvo.getNoutassnum());
// Integer count
// =PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), para,
// WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
// if(count == 0){
// throw new BusinessException("�ڱ����"+i+"�У��Ƴ�������Ϣ�󶨴���������̿�������Ѿ��ı�");
// }
//			
// para.clearParams();
// para.addParam(hvo.getPk_cargedoc());
// para.addParam(bvo.getPk_trayin());
// para.addParam(bvo.getPk_invmandoc());
// para.addParam(PuPubVO.getUFDouble_NullAsZero(bvo.getNinnum()));
// para.addParam(PuPubVO.getUFDouble_NullAsZero(bvo.getNinassnum()));
// count =PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(),
// para, WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
// if(count == 0){
// throw new BusinessException("�ڱ����"+i+"�У�����������Ϣ�󶨴���ı����������ռ��");
// }
// i++;
// para.clearParams();
// }
//		
// String upStockInv =
// "update tb_warehousestock set whs_stocktonnage=coalesce(whs_stocktonnage,0)+?,whs_stockpieces=coalesce(whs_stockpieces,0)+?"
// +
// " where pk_cargdoc=? and pplpt_pk=? and pk_invmandoc=? ";
// String upCargeTary =" update bd_cargdoc_tray set cdt_traystatus=? " +
// " where pk_cargdoc=? and cdt_pk=?";
// for(TpydBVO bvo :bvos){
// UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNoutnum());
// // UFDouble noutassnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNoutassnum());
// UFDouble nmovenum = PuPubVO.getUFDouble_NullAsZero(bvo.getNmovenum());
// UFDouble nmoveassnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNmoveassnum());
// para.addParam(nmovenum.multiply(-1));
// para.addParam(nmoveassnum.multiply(-1));
// para.addParam(hvo.getPk_cargedoc());
// para.addParam(bvo.getPk_trayout());
// para.addParam(bvo.getPk_invmandoc());
// getBaseDAO().executeUpdate(upStockInv,para);
// para.clearParams();
// if((nmovenum.sub(noutnum)).doubleValue()==0){//��������ƿգ���ı�����״̬Ϊ��
// para.addParam(0);
// para.addParam(hvo.getPk_cargedoc());
// para.addParam(bvo.getPk_trayout());
// getBaseDAO().executeUpdate(upCargeTary,para);
// para.clearParams();
// }
// para.addParam(nmovenum);
// para.addParam(nmoveassnum);
// para.addParam(hvo.getPk_cargedoc());
// para.addParam(bvo.getPk_trayin());
// para.addParam(bvo.getPk_invmandoc());
// getBaseDAO().executeUpdate(upStockInv,para);
// para.clearParams();
// para.addParam(1);
// para.addParam(hvo.getPk_cargedoc());
// para.addParam(bvo.getPk_trayin());
// getBaseDAO().executeUpdate(upCargeTary,para);
// para.clearParams();
//			
// }
// }
// }
