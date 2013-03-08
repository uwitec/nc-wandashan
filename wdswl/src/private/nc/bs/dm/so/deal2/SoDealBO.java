package nc.bs.dm.so.deal2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.SoDealHeaderVo;
import nc.vo.dm.so.deal2.SoDealVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.voutils.IFilter;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class SoDealBO {

	private BaseDAO m_dao = null;

	private BaseDAO getDao() {
		if (m_dao == null) {
			m_dao = new BaseDAO();
		}
		return m_dao;
	}

	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-3-29����02:08:02
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public SoDealVO[] doQuery(String whereSql,String queryStore,String pk_storedoc) throws Exception {
		SoDealVO[] datas = null;
		// ʵ�ֲ�ѯ���˼ƻ����߼�
		StringBuffer sql = new StringBuffer();
		sql.append("select  ");
		String[] names = SoDealVO.m_headNames;
		for (String name : names) {
			sql.append(name + ", ");
		}
		names = SoDealVO.m_bodyNames;
		for (String name : names) {
			sql.append(name + ", ");
		}
		sql.append(" 'aaa' ");
		sql.append(" from so_sale h  " );
		sql.append(" inner join so_saleorder_b b on h.csaleid = b.csaleid");
		sql.append(" where ");
		sql.append("  isnull(h.dr,0)=0  and isnull(b.dr,0)=0  ");		
		sql.append(" and  coalesce(h.bisclose,'N') = 'N' " );		
		if (whereSql != null && whereSql.length() > 0) {
			sql.append(" and " + whereSql);
		}
		sql.append(" and h.creceiptcustomerid in(");
		sql.append(" select tb_storcubasdoc.pk_cumandoc  ");
		sql.append(" from wds_storecust_h ");
		sql.append(" join tb_storcubasdoc ");
		sql.append(" on wds_storecust_h.pk_wds_storecust_h = tb_storcubasdoc.pk_wds_storecust_h ");
		sql.append("  where isnull(wds_storecust_h.dr,0)=0");
		sql.append("  and isnull(tb_storcubasdoc.dr,0)=0");
		if(!WdsWlPubTool.isZc(pk_storedoc)){
		sql.append("  and wds_storecust_h.pk_stordoc ='"+pk_storedoc+"'");
		}
		if(queryStore!=null && queryStore.length()>0){
			sql.append("  and wds_storecust_h.pk_stordoc ='"+queryStore+"'");	
		}
		sql.append(" )");
		Object o = getDao().executeQuery(sql.toString(),
				new BeanListProcessor(SoDealVO.class));
		if (o != null) {
			ArrayList<SoDealVO> list = (ArrayList<SoDealVO>) o;
			datas = list.toArray(new SoDealVO[0]);
		}
		setStock(datas);
		return datas;
	}
	String sql=
		" select  wds_storecust_h.pk_stordoc "+
		" from wds_storecust_h "+
		" join tb_storcubasdoc "+
		" on wds_storecust_h.pk_wds_storecust_h = tb_storcubasdoc.pk_wds_storecust_h "+
		"  where isnull(wds_storecust_h.dr,0)=0"+
		"  and isnull(tb_storcubasdoc.dr,0)=0";
		/**
		 * ���÷����ֿ�
		 * @���ߣ�mlr
		 * @˵�������ɽ������Ŀ 
		 * @ʱ�䣺2012-7-31����12:35:56
		 * @param datas
		 * @throws DAOException 
		 */
		private void setStock(SoDealVO[] datas)  {
			if(datas==null || datas.length==0)
				return ;
			for(int i=0;i<datas.length;i++){
				//'0001B11000000000W46O'
				String pk_cub=datas[i].getCreceiptcustomerid();
				String sql1="";
				sql1=sql+" and tb_storcubasdoc.pk_cumandoc = '"+pk_cub+"'";
				String pk_storc=null;
				try {
					pk_storc = PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql1,new ColumnProcessor()));
				} catch (DAOException e) {
					e.printStackTrace();
				}
				datas[i].setCbodywarehouseid(pk_storc);
				String pk_isxn=datas[i].getPk_defdoc11();
				if(pk_isxn==null || pk_isxn.equals(Wds2WlPubConst.so_virtual_value_no)){
					datas[i].setIsxnap(new UFBoolean(false));
				}else{
					datas[i].setIsxnap(new UFBoolean(true));
				}
			}		
		}

	
	class FielterMinNum implements IFilter{
		private SoDealCol col = null;
		public FielterMinNum(SoDealCol col){
			super();
			this.col = col;
		}

		public boolean accept(Object o) {
			// TODO Auto-generated method stub
			if(!(o instanceof SoDealBillVO))
				return false;
			SoDealBillVO bill = (SoDealBillVO)o;
			//���ⰲ�Ų�������С������
			if(PuPubVO.getUFBoolean_NullAs(bill.getHeader().getIsonsell(), UFBoolean.FALSE).booleanValue()){
				return true;
			}
			String ccustid = PuPubVO.getString_TrimZeroLenAsNull(bill.getHeader().getCcustomerid());
			String pk_store = PuPubVO.getString_TrimZeroLenAsNull(bill.getHeader().getCbodywarehouseid());
			if(ccustid == null||pk_store == null)
			    return false;
			UFDouble nminnum = null;
			try {
				nminnum = col.getMinSendNumForCust(ccustid,pk_store);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Logger.debug("��ȡ�ͻ���С���������ó����쳣���ͻ�id��"+ccustid);
				Logger.debug(e);
				return false;
			}
			
			bill.getHeader().setNminnum(nminnum);//��������С������
			
			UFDouble nallnum = WdsWlPubTool.DOUBLE_ZERO;
			SoDealVO[] bodys = bill.getBodyVos();
			if(bodys == null || bodys.length ==0){
				Logger.info("�ͻ�ID��"+ccustid+"���η�����Ʒ����Ϊ��,�����з������š�");
				return false;
			}
				
			for(SoDealVO body:bodys){
				nallnum = nallnum.add(PuPubVO.getUFDouble_NullAsZero(body.getNnumber())).sub(PuPubVO.getUFDouble_NullAsZero(body.getNtaldcnum()));
			}
			UFDouble npackNum =PuPubVO.getUFDouble_NullAsZero(bodys[0].getNpacknumber()) ;
			if(WdsWlPubConst.sale_send_isass && npackNum.doubleValue()>0){
				nallnum = nallnum.multiply(bodys[0].getNnumber()).div(npackNum);
			}
			if(nallnum.compareTo(nminnum)>0)
				return true;	
			Logger.info("�ͻ�ID��"+ccustid+"���η�����Ϊ"+nallnum+",������С������"+nminnum+"����,���β����з������š�");
			return false;
		}		
	}
	
	
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-7-8����02:03:17
	 * @param bills
	 * @param lpara
	 * @return
	 * @throws Exception
	 */
	public Object doDeal(SoDealBillVO[] bills,ArrayList lpara) throws Exception{
		if(bills == null || bills.length == 0)
			return null;		
		Logger.init(WdsWlPubConst.wds_logger_name);
		Logger.info("##########################################################");
		Logger.info("���ۼƻ����ţ������ſͻ�����"+bills.length+"--------------");
//		������С������  �ֲֿ��̰�  �ڵ� ά����  ÿ���ͻ�����С������
		SoDealCol dealCol = new SoDealCol();
		SoDealBillVO[] newbills = (SoDealBillVO[])VOUtil.filter(bills, new FielterMinNum(dealCol));
		if(newbills==null || newbills.length == 0)
			return null;
		Logger.info("�����ſͻ�����Ϊ"+newbills.length);
		Logger.info("���ݿ��������а���....");
//		�Ա���ͬһ�ͻ�ͬһ����Ʒ�����ٴν��кϲ�			
		dealCol.setData(newbills, lpara);
		Object o = dealCol.col();
		return o;
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-3-25����03:58:14
	 * @param ldata
	 * @param infor
	 *            :��¼�ˣ���¼��˾����¼����
	 * @throws Exception
	 */
	public void doDeal1(List<SoDealVO> ldata, List<String> infor)
			throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;
		// 3.���ۼƻ�����vo---�����۶���
		// 3.1��  ����վ �ͻ� �ֵ�
		CircularlyAccessibleValueObject[][] datas = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (ldata
						.toArray(new SoDealVO[0])),
				WdsWlPubConst.SO_PLAN_DEAL_SPLIT_FIELDS);
		if (datas == null || datas.length == 0)
			return;
		int len = datas.length;
		SoDealVO[] tmpVOs = null;
		SoDealBillVO[] planBillVos = new SoDealBillVO[len];
		for (int i = 0; i < len; i++) {
			tmpVOs = (SoDealVO[]) datas[i];
			planBillVos[i] = new SoDealBillVO();
			planBillVos[i].setParentVO(getPlanHead(tmpVOs[0]));
			planBillVos[i].setChildrenVO(tmpVOs);
		}
		//3.2�������ݽ��������������˵�
		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_operator = infor.get(0);
		paraVo.m_coId = infor.get(1);
		paraVo.m_currentDate = infor.get(2);
		AggregatedValueObject[] orderVos = (AggregatedValueObject[]) PfUtilTools
				.runChangeDataAry(WdsWlPubConst.WDS4, WdsWlPubConst.WDS5,
						planBillVos, paraVo);
		//3.3 ���������˵�����ű������������˵�
		if (orderVos == null || orderVos.length == 0) {
			return;
		}
		PfUtilBO pfbo = new PfUtilBO();
		for (AggregatedValueObject bill : orderVos) {
			pfbo.processAction(WdsWlPubConst.DM_PLAN_TO_ORDER_SAVE,
					WdsWlPubConst.WDS5, infor.get(2), null, bill, null);
		}
	}
	private SoDealHeaderVo getPlanHead(SoDealVO dealVo) {
		if (dealVo == null)
			return null;
		SoDealHeaderVo head = new SoDealHeaderVo();
		String[] names = head.getAttributeNames();
		for (String name : names) {
			head.setAttributeValue(name, dealVo.getAttributeValue(name));
		}
		return head;
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-3-25����03:58:14
	 * @param ldata
	 * @param infor
	 *            :��¼�ˣ���¼��˾����¼����
	 * @throws Exception
	 */
	public void doHandDeal(List<SoDealVO> ldata, List<String> infor)
			throws Exception {
//		����У��
		
		SoDealBO dealbo = new SoDealBO();
		dealbo.doDeal1(ldata, infor);		
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ������ ͬһ�ͻ�ͬ��Ʒ�ֵ�  �ϲ�     �ݲ�ʹ��  ��Ϊ�漰�� ������д������  ���ܺϲ�
	 * @ʱ�䣺2011-7-8����09:56:09
	 * @param bills
	 */
	private void combinData(SoDealBillVO[] bills){
		if(bills == null || bills.length == 0)
			return;
//		SoDealBillVO bill = null;
		Map<String,SoDealVO> dataMap = new HashMap<String, SoDealVO>();
		SoDealVO[] bodys = null;
		SoDealVO tmpBody  =  null;
		String key = null;
		for(SoDealBillVO bill:bills){
			bodys = bill.getBodyVos();
			dataMap.clear();
			for(SoDealVO body:bodys){
				key = WdsWlPubTool.getString_NullAsTrimZeroLen(body.getCinvbasdocid());
				if(dataMap.containsKey(key)){
					tmpBody = dataMap.get(key);
					body.combin(tmpBody);
				}
				dataMap.put(key, body);
			}
			bill.setChildrenVO(dataMap.values().toArray(new SoDealVO[0]));
		}
	}

}
