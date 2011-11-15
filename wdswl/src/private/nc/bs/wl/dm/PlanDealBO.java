package nc.bs.wl.dm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.trade.business.HYPubBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.dm.PlanDealVO;
import nc.vo.dm.SendplaninBVO;
import nc.vo.dm.SendplaninVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * ���˼ƻ������̨��
 * @author Administrator
 *
 */

public class PlanDealBO {

	private BaseDAO m_dao = null;
	private BaseDAO getDao(){
		if(m_dao == null){
			m_dao = new BaseDAO();
		}
		return m_dao;
	}
	
	private TempTableUtil ttbo = null;
	private TempTableUtil getTempTableUtil(){
		if(ttbo == null)
			ttbo = new TempTableUtil();
		return ttbo;
	}
	private HYPubBO superbo = null;
	private HYPubBO getSuperBO(){
		if(superbo == null)
			superbo = new HYPubBO();
		return superbo;
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * ��ѯ����վ�ǵ�ǰ��¼��Ա�󶨵Ĳֿ�� ���˰���
	 * �����ǰ��¼�����ֵܲ� ���Բ�ѯ���е� ���˰���
	 * 
	 * wds_sendplanin���˼ƻ�����
	 * wds_sendplanin_b ���˼ƻ��ӱ�
	 * @ʱ�䣺2011-3-25����09:16:20
	 * @param wheresql
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PlanDealVO[] doQuery(String whereSql) throws Exception{
		PlanDealVO[] datas = null;
		//ʵ�ֲ�ѯ���˼ƻ����߼� 
		StringBuffer sql = new StringBuffer();
		sql.append("select  ");
		sql.append(" wds_sendplanin.pk_corp ,");
		sql.append(" wds_sendplanin.dmakedate ,");
		sql.append(" wds_sendplanin.voperatorid ,");
		sql.append(" wds_sendplanin.vapprovenote ,");
		sql.append(" wds_sendplanin.pk_billtype ,");
		sql.append(" wds_sendplanin.vbillstatus ,");
		sql.append(" wds_sendplanin.iplantype ,");
		sql.append(" wds_sendplanin. vemployeeid ,");
		sql.append(" wds_sendplanin.pk_busitype ,");
		sql.append(" wds_sendplanin.pk_sendplanin ,");
		sql.append(" wds_sendplanin.dbilldate ,");
		sql.append(" wds_sendplanin.dmakedate,");
		sql.append(" wds_sendplanin.vbillno vbillno,");
		sql.append(" wds_sendplanin.pk_inwhouse ,");
		sql.append(" wds_sendplanin.pk_deptdoc ,");
		sql.append(" wds_sendplanin.pk_outwhouse ,");
		sql.append(" wds_sendplanin.dapprovedate ,");
		sql.append(" wds_sendplanin.vapproveid ,");
		sql.append(" wds_sendplanin_b.pk_sendplanin_b,");
		sql.append(" wds_sendplanin_b.pk_invmandoc,");
		sql.append(" wds_sendplanin_b.pk_invbasdoc,");
		sql.append(" wds_sendplanin_b.unit,");
		sql.append(" wds_sendplanin_b.assunit,");
		sql.append(" wds_sendplanin_b.nplannum nplannum,");//�ƻ�����
		sql.append(" wds_sendplanin_b.nassplannum nassplannum,");//�ƻ�������
		sql.append(" wds_sendplanin_b.ndealnum,");//�Ѱ�������
		sql.append(" wds_sendplanin_b.nassdealnum,");//�Ѱ��Ÿ�����
		sql.append(" coalesce(wds_sendplanin_b.nplannum,0)-coalesce(wds_sendplanin_b.ndealnum,0) nnum,");//���ΰ�������
		sql.append(" coalesce(wds_sendplanin_b.nassplannum,0)-coalesce(wds_sendplanin_b.nassdealnum,0) nassnum,");//���ΰ��Ÿ�����
		sql.append(" wds_sendplanin_b.hsl hsl,");
		sql.append(" wds_sendplanin_b.ts");
		sql.append(" from wds_sendplanin ");
		sql.append(" join wds_sendplanin_b ");
		sql.append(" on wds_sendplanin.pk_sendplanin = wds_sendplanin_b.pk_sendplanin ");
		sql.append(" where "+whereSql);
		Object o = getDao().executeQuery(sql.toString(), new BeanListProcessor(PlanDealVO.class));
		if( o != null){
			ArrayList<PlanDealVO> list = (ArrayList<PlanDealVO>)o;
			datas = list.toArray(new PlanDealVO[0]);
		}
		Arrays.sort(datas, new Comparator(){
			public int compare( Object o1, Object o2){
				 String code1 = ((PlanDealVO)o1).getVbillno();
				 if(code1 == null){
					 code1 = "";
				 }
				 String code2 = ((PlanDealVO)o2).getVbillno();
				 if(code2 == null){
					 code2 = "";
				 }
				 return code1.compareTo(code2);
			}
		});
		// �ѿ�������� �˵� ʱ���Ⱥ� ����
		arrangStornumout(datas);
		arrangStornumin(datas);
		return datas;
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ������վ������Ͱ��ź���������
	 * @ʱ�䣺2011-11-4����10:26:39
	 * @param datas
	 * @throws BusinessException
	 */
	public void arrangStornumout(PlanDealVO[] datas) throws BusinessException{
		if(datas == null || datas.length == 0){
			return ;
		}
		//���շ����ֿ� ����
		Map<String,ArrayList<PlanDealVO>> map = new HashMap<String ,ArrayList<PlanDealVO>>();
		for(PlanDealVO data:datas){
			String key = PuPubVO.getString_TrimZeroLenAsNull(data.getPk_outwhouse());
			if(key == null){
				continue;
			}
			if(map.containsKey(key)){
				map.get(key).add(data);
			}else{
				ArrayList<PlanDealVO> list = new ArrayList<PlanDealVO>();
				list.add(data);
				map.put(key, list);
			}
		}
		//��ѯ�����ֿ� ���д���Ŀ����
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_invmandoc,sum(whs_stocktonnage) num ");
		sql.append(" from tb_warehousestock ");
		sql.append(" where pk_customize1 = ?");
		sql.append(" group by pk_invmandoc ");
		SQLParameter param = new SQLParameter();
		ArrayListProcessor processor = new ArrayListProcessor();
		for(Entry<String,ArrayList<PlanDealVO>> entry:map.entrySet()){
			String key = entry.getKey();
			ArrayList<PlanDealVO> values= entry.getValue();
			param.addParam(key);
			ArrayList<Object> list =(ArrayList<Object>)getDao().executeQuery(sql.toString(), param, processor);
			if(list != null && list.size() >0){
				for(int i=0;i<list.size();i++){
					Object[] obj =(Object[]) list.get(i);
					String pk_invmandoc = PuPubVO.getString_TrimZeroLenAsNull(obj[0]);
					UFDouble num = PuPubVO.getUFDouble_NullAsZero(obj[1]);
					if(pk_invmandoc== null || num.doubleValue() <= 0){//����������Ϊ0�����ټ�������
						continue;
					}
					setNstoreNum(pk_invmandoc,num,values);
				}
			}
			param.clearParams();
		}
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ :
	 * @ʱ�䣺2011-11-4����09:59:52
	 * @param pk_invmandoc
	 * @param num
	 * @param values
	 */
	void setNstoreNum(String pk_invmandoc,UFDouble num,ArrayList<PlanDealVO> values){
		for(int i=0;i<values.size();i++){
			if(num.doubleValue()<=0){
				return;
			}
			if(!pk_invmandoc.equalsIgnoreCase(values.get(i).getPk_invmandoc())){
				continue;
			}
			values.get(i).setNstorenumout(num);
			UFDouble dealNum = PuPubVO.getUFDouble_NullAsZero(values.get(i).getNnum());//���ΰ�������
			num = num.sub(dealNum);
			values.get(i).setNarrstorenumout(num);
		}
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ���ջ�վ������Ͱ��ź���������
	 * @ʱ�䣺2011-11-4����10:26:39
	 * @param datas
	 * @throws BusinessException
	 */
	public void arrangStornumin(PlanDealVO[] datas) throws BusinessException{
		if(datas == null || datas.length == 0){
			return ;
		}
		//���շ����ֿ� ����
		Map<String,ArrayList<PlanDealVO>> map = new HashMap<String ,ArrayList<PlanDealVO>>();
		for(PlanDealVO data:datas){
			String key = PuPubVO.getString_TrimZeroLenAsNull(data.getPk_inwhouse());
			if(key == null){
				continue;
			}
			if(map.containsKey(key)){
				map.get(key).add(data);
			}else{
				ArrayList<PlanDealVO> list = new ArrayList<PlanDealVO>();
				list.add(data);
				map.put(key, list);
			}
		}
		//��ѯ�ջ��ֿ� ���д���Ŀ����
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_invmandoc,sum(whs_stocktonnage) num ");
		sql.append(" from tb_warehousestock ");
		sql.append(" where pk_customize1 = ?");
		sql.append(" group by pk_invmandoc ");
		SQLParameter param = new SQLParameter();
		ArrayListProcessor processor = new ArrayListProcessor();
		for(Entry<String,ArrayList<PlanDealVO>> entry:map.entrySet()){
			String key = entry.getKey();
			ArrayList<PlanDealVO> values= entry.getValue();
			param.addParam(key);
			ArrayList<Object> list =(ArrayList<Object>)getDao().executeQuery(sql.toString(), param, processor);
			Map<String,UFDouble> curStornum = new HashMap<String, UFDouble>();//<�������,��ǰ�����>
			for(PlanDealVO value:values){
				String pk_ivnmandoc = value.getPk_invmandoc();
				if(!curStornum.containsKey(pk_ivnmandoc)){
					if(list != null && list.size() >0){
						for(int i=0;i<list.size();i++){
							Object[] obj =(Object[]) list.get(i);
							String invmanid = PuPubVO.getString_TrimZeroLenAsNull(obj[0]);
							UFDouble num = PuPubVO.getUFDouble_NullAsZero(obj[0]);
							if(pk_ivnmandoc.equalsIgnoreCase(invmanid)){
								curStornum.put(pk_ivnmandoc, num);
							}
						}
					}else{
						curStornum.put(pk_ivnmandoc, new UFDouble(0));
					}
					
				
				}
				UFDouble num = PuPubVO.getUFDouble_NullAsZero(curStornum.get(pk_ivnmandoc));
				value.setNstorenumin(num);
				num = num.add( PuPubVO.getUFDouble_NullAsZero(value.getNnum()));
				value.setNarrstorenumin(num);
				curStornum.put(pk_ivnmandoc, num);
			}
			param.clearParams();
		}
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * �����ΰ�����������д�����˼ƻ������ۼƷ�������
	 * @ʱ�䣺2011-3-25����04:44:08
	 * @param dealnumInfor
	 * @throws BusinessException
	 */
	private void reWriteDealNumForPlan(Map<String,ArrayList<UFDouble>> map) throws BusinessException{
		if(map == null || map.size()==0)
			return;
		for(Entry<String, ArrayList<UFDouble>> entry:map.entrySet()){
			String sql = "update wds_sendplanin_b set ndealnum =coalesce(ndealnum,0)+"
				+entry.getValue().get(0)+" where pk_sendplanin_b='"+entry.getKey()+"'";
			if(getDao().executeUpdate(sql)==0){
				throw new BusinessException("�����쳣���÷��˼ƻ������ѱ�ɾ���������²�ѯ����");
			};
			String sql1 = "update wds_sendplanin_b set nassdealnum =coalesce(nassdealnum,0)+"
				+entry.getValue().get(1)+" where pk_sendplanin_b='"+entry.getKey()+"'";
			if(getDao().executeUpdate(sql1)==0){
				throw new BusinessException("�����쳣���÷��˼ƻ������ѱ�ɾ���������²�ѯ����");
			};
			//���ƻ�������nplannum�����ۼư�������(ndealnum)�Ƚ�
			//����ۼư����������ڼƻ��������׳��쳣

			String sql2="select count(0) from wds_sendplanin_b where pk_sendplanin_b='"+entry.getKey()+ "'and (coalesce(nplannum,0)-coalesce(ndealnum,0))>=0";			
			Object o=getDao().executeQuery(sql2,WdsPubResulSetProcesser.COLUMNPROCESSOR);
			if(o==null){
				throw new BusinessException("�ۼư����������ܴ��ڼƻ�������");
			}
		}
	}
	
	private void checkTs(Map<String,UFDateTime> tsInfor) throws Exception{
		if(tsInfor == null || tsInfor.size() ==0)
			return;
		String sql = "select pk_sendplanin_b,ts from wds_sendplanin_b where pk_sendplanin_b in "+getTempTableUtil().getSubSql(tsInfor.keySet().toArray(new String[0]));
		List ldata = (List)getDao().executeQuery(sql, new ArrayListProcessor());
		if(ldata == null || ldata.size() == 0)
			throw new  ValidationException("�����쳣");
		Object[] os = null;
		int len = ldata.size();
		String key = null;
		String newts = null;
		for(int i=0;i<len;i++){
			os = (Object[])ldata.get(i);
			key = WdsWlPubTool.getString_NullAsTrimZeroLen(os[0]);
			newts = WdsWlPubTool.getString_NullAsTrimZeroLen(os[1]);
			if(!WdsWlPubTool.getString_NullAsTrimZeroLen(tsInfor.get(key)).equalsIgnoreCase(newts)){
				throw new ValidationException("������������,��ˢ�½������²���");
			}
		}
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-3-25����03:58:14
	 * @param ldata
	 * @param infor :��¼�ˣ���¼��˾����¼����
	 * @throws Exception
	 */
	public void doDeal(List<PlanDealVO> ldata, List<String> infor)
			throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;		
		/**
		 * zhf  add  Ӧ���Ƚ��� ����У��  У�鷢�˼ƻ��Ƿ����˸ı�
		 */
		Map<String,UFDateTime> tsInfor = new HashMap<String, UFDateTime>();
		for(PlanDealVO data:ldata){
			tsInfor.put(data.getPrimaryKey(), data.getTs());
		}	
		checkTs(tsInfor);	
		/**
		 * ���ţ����ɷ��˶��� ���˼ƻ��������ɷ��˶���
		 * �ƻ����� �ƻ��к� ���ϲ��ƻ��� 
		 * �ƻ��Ͷ���Ϊ1�Զ��ϵ 
		 * �ֵ����� ����վ �ջ�վ��ͬ �����Ǽƻ�����		 * 
		 *  */
		//��д�ƻ��ۼư�������
		// ���˰���vo---�����˼ƻ�vo
		Map<String,ArrayList<UFDouble>> map = new HashMap<String, ArrayList<UFDouble>>();
		for(int i=0;i<ldata.size();i++){
			String key = ldata.get(i).getPk_sendplanin_b();
			ArrayList<UFDouble> list = new ArrayList<UFDouble>();
			UFDouble num= PuPubVO.getUFDouble_NullAsZero(ldata.get(i).getNnum());
			UFDouble nassnum = PuPubVO.getUFDouble_NullAsZero(ldata.get(i).getNassnum());
			if(map.containsKey(key)){
				UFDouble oldValue =PuPubVO.getUFDouble_NullAsZero(map.get(key).get(0));
				UFDouble oldAssValue =PuPubVO.getUFDouble_NullAsZero(map.get(key).get(1));
				num = num.add(oldValue);
				nassnum = nassnum.add(oldAssValue);
				list.add(num);
				list.add(nassnum);
				map.put(key, list);
			}else{
				list.add(num);
				list.add(nassnum);
				map.put(key, list);
			}
		}
		reWriteDealNumForPlan(map);
		// �� �ƻ��� ����վ �ջ�վ �ֵ�
		CircularlyAccessibleValueObject[][] datas = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (ldata
						.toArray(new PlanDealVO[0])),
				WdsWlPubConst.DM_PLAN_DEAL_SPLIT_FIELDS);
		if (datas == null || datas.length == 0)
			return;
		int len = datas.length;
		PlanDealVO[] tmpVOs = null;
		HYBillVO[] planBillVos = new HYBillVO[len];
		for (int i = 0; i < len; i++) {
			tmpVOs = (PlanDealVO[]) datas[i];
			planBillVos[i] = new HYBillVO();
			planBillVos[i].setParentVO(getPlanHead(tmpVOs[0]));
			planBillVos[i].setChildrenVO(tmpVOs);
		}
		// ���˼ƻ�vo---�����˶���vo
		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_operator = infor.get(0);
		paraVo.m_coId = infor.get(1);
		paraVo.m_currentDate = infor.get(2);
		// ������ ���� ���� ������
		HYBillVO[] orderVos = (HYBillVO[]) PfUtilTools.runChangeDataAry(
				WdsWlPubConst.WDS1,
				WdsWlPubConst.WDS3, planBillVos, paraVo);
		// �ֵ�---�����涩��
		if(orderVos ==null || orderVos.length==0){
			return;
		}
		PfUtilBO pfbo = new PfUtilBO();
		for(HYBillVO bill: orderVos){
			pfbo.processAction(WdsWlPubConst.DM_PLAN_TO_ORDER_SAVE, WdsWlPubConst.WDS3, infor.get(2), null, bill, null);
		}
	}
	private SendplaninVO getPlanHead(PlanDealVO dealVo){
		if(dealVo == null)
			return null;
		SendplaninVO head  = new SendplaninVO();
		String[] names  = head.getAttributeNames();
		for(String name:names){
			head.setAttributeValue(name, dealVo.getAttributeValue(name));
		}
		return head;
	}
	
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �ƻ�¼���йر�
	 * @ʱ�䣺2011-6-25����09:12:02
	 * @param lpara ��һ������Ϊ��ͷid
	 * @return
	 * @throws Exception
	 */
	public  HYBillVO closeRows(List lpara) throws BusinessException{
		if(lpara == null || lpara.size() ==0 || lpara.size() == 1)
			return null;
		HYBillVO newbill = null;
		
		String billid = PuPubVO.getString_TrimZeroLenAsNull(lpara.get(0));
		lpara.remove(0);
		int len = lpara.size();
		
		String sql = "update wds_sendplanin_b set reserve14 = 'Y' where pk_sendplanin_b in "+getTempTableUtil().getSubSql((ArrayList)lpara);
		int size = getDao().executeUpdate(sql);
		if(size !=len)
			throw new BusinessException("����ʧ��");
		newbill = (HYBillVO)getSuperBO().queryBillVOByPrimaryKey(new String[]{HYBillVO.class.getName(),SendplaninVO.class.getName(),SendplaninBVO.class.getName()}, billid);
		
		return newbill;
	}
	
}
