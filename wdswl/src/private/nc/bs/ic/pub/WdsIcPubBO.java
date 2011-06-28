package nc.bs.ic.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.pub.pf.IQueryData2;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.bs.wl.pub.WdsWlPubBO;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.dm.order.SendorderBVO;
import nc.vo.dm.order.SendorderVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class WdsIcPubBO {
	private BaseDAO m_dao = null;
	public BaseDAO getDao() {
		if (m_dao == null) {
			m_dao = new BaseDAO();
		}
		return m_dao;
	}
	private WdsWlPubBO pubbo = null;

	public WdsWlPubBO getWdsWLBO(){
		if(pubbo == null){
			pubbo = new WdsWlPubBO();
		}
		return pubbo;
	}
	
	private ArrayList<String> usedTary =null;
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ��ѯ���ⵥ��ȫ�ӱ���Ϣ
	 * @ʱ�䣺2011-4-9����09:03:27
	 * @param whereSql
	 * @return
	 * @throws BusinessException
	 */
	public TbOutgeneralBVO[] queryIcOutBodyInfor(String whereSql) throws BusinessException{
		Collection c = getDao().retrieveByClause(TbOutgeneralBVO.class, whereSql);
		if(c == null || c.size()==0)
			return null;
		TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[])c.toArray(new TbOutgeneralBVO[0]);
		for(TbOutgeneralBVO body:bodys){
			ArrayList ctmp = (ArrayList)getDao().retrieveByClause(TbOutgeneralTVO.class, " general_b_pk = '"+body.getPrimaryKey()+"'");
			if(ctmp == null||ctmp.size()==0)
				continue;
			body.setTrayInfor(ctmp);
		}
		return bodys;
	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ��ѯ��ⵥ��ȫ�ӱ���Ϣ
	 * @ʱ�䣺2011-4-9����09:03:51
	 * @param whereSql
	 * @return
	 * @throws BusinessException
	 */
	public TbGeneralBVO[] queryIcInBillBodyAllData(String whereSql) throws BusinessException{
		Collection c = getDao().retrieveByClause(TbGeneralBVO.class, whereSql);
		if(c == null || c.size()==0)
			return null;
		TbGeneralBVO[] bodys = (TbGeneralBVO[])c.toArray(new TbGeneralBVO[0]);
		Collection ctmp = null;
		Iterator<TbGeneralBBVO> it = null;
		for(TbGeneralBVO body:bodys){
			ctmp = getDao().retrieveByClause(TbGeneralBBVO.class, " geb_pk = '"+body.getPrimaryKey()+"'");
			if(ctmp == null||ctmp.size()==0)
				continue;
			List<TbGeneralBBVO> ltray = new ArrayList<TbGeneralBBVO>();
			it = ctmp.iterator();
			while(it.hasNext()){
				ltray.add(it.next());
			}
			body.setTrayInfor(ltray);
		}
		return bodys;

	}

	public void doSaveTrayInfor(List<TbGeneralBBVO>lallbbvos) throws BusinessException{
		if(lallbbvos == null || lallbbvos.size() == 0)
			return;
		List<String> lbid = new ArrayList<String>();
		String tmp = null;
		for(TbGeneralBBVO body:lallbbvos){
			tmp = body.getGeb_pk();
			if(lbid.contains(tmp))
				continue;
			lbid.add(tmp);
		}
		String sql = " update tb_general_b_b set dr = 1 where geb_pk in "+new TempTableUtil().getSubSql(lbid.toArray(new String[0]));
		getDao().executeUpdate(sql);
		getDao().insertVOArray(lallbbvos.toArray(new TbGeneralBBVO[0]));
	}

	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ������������̯������  ��̯��ʽ һ������ֻ�ܷŻ���һ��������̲�Ϊ������ʹ��
	 *        ������ǣ�һ������ֻ�ܴ���һ�����εĻ���      ����״̬=0 ��  ����״̬=1 ���ڻ���
	 * @ʱ�䣺2011-3-31����07:24:10
	 * @param tmpTrays ���ο�������id����
	 * @param body ��浥����
	 * @param iVolumn ��ǰ���������ݻ�
	 * 
	 */
	private List<TbGeneralBBVO> shareNumToTrays(String[] tmpTrays, TbGeneralBVO body,
			int iVolumn,boolean iszc) {
		//��ǰ����Ҫ�뵽���̵� ������  ���ø�������̯��һ������������  
		UFDouble geb_bsnumd = PuPubVO.getUFDouble_NullAsZero(body.getGeb_bsnum());	    
		int k = 0;
		ArrayList<TbGeneralBBVO> lbbvos = new ArrayList<TbGeneralBBVO>();
		TbGeneralBBVO tbgbbvo = null;
		if(iszc){
			while (geb_bsnumd.doubleValue() > iVolumn) {
				tbgbbvo = new TbGeneralBBVO();
				//��������
				tbgbbvo.setCreadate(body.getGeb_dbizdate());
				//ʧЧ����
				tbgbbvo.setExpdate(body.getGeb_dvalidate());
				// ����
				tbgbbvo.setGebb_vbatchcode(body.getGeb_vbatchcode());
				// ��д����
				tbgbbvo.setGebb_lvbatchcode(body.getGeb_backvbatchcode());
				// �к�
				tbgbbvo.setGebb_rowno((k + 1) + "0");
				//��λ
				tbgbbvo.setPk_cargdoc(body.getGeb_space());
				// ���ⵥ��������
				tbgbbvo.setPwb_pk(body.getGeb_cgeneralbid());//------------------
				// ������
				tbgbbvo.setGebb_hsl(body.getGeb_hsl());
				//�������id
				tbgbbvo.setPk_invmandoc(body.getGeb_cinventoryid());
				// �����������
				tbgbbvo.setPk_invbasdoc(body.getGeb_cinvbasid());
				// ��ⵥ�ӱ�����
				tbgbbvo.setGeb_pk(body.getGeb_pk());//-----------------
				// ����
				tbgbbvo.setGebb_nprice(body.getGeb_nprice());
				// ���
				tbgbbvo.setGebb_nmny(body.getGeb_nmny());
				//
				tbgbbvo.setGebb_num(PuPubVO.getUFDouble_NullAsZero(iVolumn).multiply(PuPubVO.getUFDouble_NullAsZero(body.getGeb_hsl())));
				// ����ʵ�ʴ������
				tbgbbvo.setNinassistnum(PuPubVO.getUFDouble_NullAsZero(iVolumn));
				geb_bsnumd = geb_bsnumd.sub(iVolumn);
				// ��������
				tbgbbvo.setCdt_pk(tmpTrays[k]);
				usedTary.add(tmpTrays[k]);
				tbgbbvo.setDr(0);
				tbgbbvo.setGeb_pk(body.getGeb_pk());
				lbbvos.add(tbgbbvo);
				k++;
				
			}
		}

		TbGeneralBBVO tbgbbvo1 = new TbGeneralBBVO();
		
		//��������
		tbgbbvo1.setCreadate(body.getGeb_dbizdate());
		//ʧЧ����
		tbgbbvo1.setExpdate(body.getGeb_dvalidate());
		// ����
		tbgbbvo1.setGebb_vbatchcode(body.getGeb_vbatchcode());
		// ��д����
		tbgbbvo1.setGebb_lvbatchcode(body.getGeb_backvbatchcode());
		// �к�
		tbgbbvo1.setGebb_rowno(k + 1 + "0");
		//��λ
		tbgbbvo1.setPk_cargdoc(body.getGeb_space());
		// ���ⵥ��������
		tbgbbvo1.setPwb_pk(body.getGeb_cgeneralbid());
		// ������
		tbgbbvo1.setGebb_hsl(body.getGeb_hsl());
		//�������id
		tbgbbvo1.setPk_invmandoc(body.getGeb_cinventoryid());
		// �˻���������
		tbgbbvo1.setPk_invbasdoc(body.getGeb_cinvbasid());
		// ��ⵥ�ӱ�����
		tbgbbvo1.setGeb_pk(body.getGeb_pk());
		// ����
		tbgbbvo1.setGebb_nprice(body.getGeb_nprice());
		// ���
		tbgbbvo1.setGebb_nmny(body.getGeb_nmny());
		// ����ʵ�ʴ������
		tbgbbvo1.setNinassistnum(PuPubVO.getUFDouble_NullAsZero(geb_bsnumd));
		tbgbbvo1.setGebb_num(PuPubVO.getUFDouble_NullAsZero(geb_bsnumd).multiply(PuPubVO.getUFDouble_NullAsZero(body.getGeb_hsl())));

		// ��������
		tbgbbvo1.setCdt_pk(tmpTrays[k]);
		usedTary.add(tmpTrays[k]);
		// DR
		tbgbbvo1.setDr(0);
		lbbvos.add(tbgbbvo1);

		//
		return lbbvos;
	}

	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �Զ���������
	 * @ʱ�䣺2011-3-31����04:07:51
	 * @param bodys cwhid=�ֿ�  cspaceid=��λ
	 * @throws BusinessException
	 */
	public Object autoInTray(TbGeneralBVO[] bodys, String cwhid, String cspaceid)
	throws BusinessException {
		if (bodys == null || bodys.length == 0)
			throw new BusinessException("�����������Ϊ��");
//<�кţ����ܹ�ָ�����̵ľ���ԭ��>
		Map<String, Integer> retInfor = new HashMap<String, Integer>();		
//<�кţ�����>
		Map<String,List<TbGeneralBBVO>> trayInfor= new HashMap<String,List<TbGeneralBBVO>>();
//��ǰ�Ѿ���������ǰռ�е�����id
		usedTary = new ArrayList<String>();
		List<TbGeneralBBVO> ltray = null;
//�ܷ�ָ������ı���λ�ڵ����е�����id
		String[] tmpTrays = null;
//��������
		int iVolumn = 0;
//��ǰ��λ�¿�������*��������
		int iAllVolumn = 0;
		boolean iszc = WdsWlPubTool.isZc(cwhid);
		if (!iszc) {
//�ַܲ����̴�ţ��ֲ�ֻ��һ����������
			String sql = "select count(0) from bd_cargdoc_tray where  isnull(dr,0)=0 and pk_cargdoc = '"
				+ cspaceid + "'";
			int index = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql,
					WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
			if (index <= 0)
				throw new ValidationException("�ֲ�û����������");
			//			return null;
			String fcTaryId = getWdsWLBO().getFcTrayInfor(cspaceid);
			if(fcTaryId == null){
				throw new ValidationException("�ֲ�û����������");
			}
			iVolumn=WdsWlPubConst.XN_CARGDOC_TRAY_VO;
			for (TbGeneralBVO body : bodys) {
				ltray = new ArrayList<TbGeneralBBVO>();
				ltray.addAll(shareNumToTrays(new String[]{fcTaryId}, body, iVolumn, iszc));
				trayInfor.put(body.getGeb_crowno(), ltray);
			}
		}else{
			for (TbGeneralBVO body : bodys) {
				String notIn = " and cdt_pk not in"+getTempTableUtil().getSubSql(usedTary)
				+" and bd_cargdoc_tray.cdt_traycode not like '"+WdsWlPubConst.XN_CARGDOC_TRAY_NAME+"%'";
				String oldCdt= getOldCdt(body);
// ��ȡ����������������Ϣ
				tmpTrays = getWdsWLBO().getTrayInfor(cspaceid, body.getGeb_cinvbasid(),notIn,oldCdt);
				if (tmpTrays == null || tmpTrays.length == 0) {
// û�����õ�������
					retInfor.put(body.getGeb_crowno(), 0);
					continue;
				}
//�Ӵ�������л�ȡ���̴������	
				iVolumn = getWdsWLBO().getTrayVolumeByInvbasid(body.getGeb_cinvbasid());				
//���㵱ǰ��λ�´�Ÿõ�Ʒ������������				
				iAllVolumn = iVolumn * tmpTrays.length;
//���ݻ� a �� ��ǰ��Ʒ��Ӧ�ո������Ƚ�b
// ���	a>b ���ܹ���Ÿõ�Ʒ
// ���   a<b �򲻹���Ÿõ�Ʒ												
				UFDouble nRes = body.getGeb_bsnum().sub(PuPubVO.getUFDouble_NullAsZero(iAllVolumn));
				if(trayInfor.containsKey(body.getGeb_crowno())){
					ltray = trayInfor.get(body.getGeb_crowno());
				}else
					ltray = new ArrayList<TbGeneralBBVO>();
				if (nRes.doubleValue() > 0) {
					retInfor.put(body.getGeb_crowno(), 1);// Ҫ�ŵĻ��ﳬ����ǰ��λ�¿ɴ�Ÿô�������̳�����----------------
					continue;
				} else if (nRes.equals(WdsWlPubTool.DOUBLE_ZERO)) {// �����������㱾������
					ltray.addAll(shareNumToTrays(tmpTrays, body, iVolumn, iszc));
				} else {// ���̴´����� ���ʹ������ ����ʹ���Ƿ������ȼ� �����̱��룿
					ltray.addAll(shareNumToTrays(tmpTrays, body, iVolumn, iszc));
				}

				trayInfor.put(body.getGeb_crowno(), ltray);

			}
		}
		Object[] retObj = new Object[]{trayInfor,retInfor}; 
		return retObj;	
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-4-20����04:10:09
	 * @param body
	 * @return  ��ǰռ�õ�����id
	 * @throws BusinessException 
	 */
	private String getOldCdt(TbGeneralBVO body ) throws BusinessException{
		StringBuffer strWhere =  new StringBuffer(" or cdt_pk in ");
		if(body.getGeb_pk() !=null && !"".equalsIgnoreCase(body.getGeb_pk())){
			String sql ="select cdt_pk  from tb_general_b_b where geb_pk='"+body.getGeb_pk()+"'";
			ArrayList<String> list = (ArrayList<String>)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
			strWhere.append(getTempTableUtil().getSubSql(list));
		}else{
			strWhere.append("('aa')");
		}
		return strWhere.toString();
	}
	
	


	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �������ⵥ ת�ֲ���ʱ  ��ѯ  ���˶���������
	 * @ʱ�䣺2011-4-8����11:06:42
	 * @param swhere  �û���ѯ����
	 * @param cwhid �����ֿ�
	 * @param cuserid ��ǰ��¼�û�
	 * @return
	 * @throws BusinessException
	 */
	public HYBillVO[] querySendOrderForOtherOut(String swhere,String cwhid,String cuserid) throws BusinessException{
		//��ѯ����������  ���˶���
		/**
		 * ������
		 * 1������ͨ��  2���ɳ���������Ϊ0 3����ǰ����ԱȨ�޻�λ 4���������ֻܲ��Ƿֲ�
		 */
		boolean iszc = WdsWlPubTool.isZc(cwhid);
		SendorderVO head = new SendorderVO();

		// ��ȡ��ѯ��
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" select distinct ");
		String[] names = head.getAttributeNames();
		for(String name:names){
			sqlBuffer.append("wds_sendorder."+name+",");
		}
		sqlBuffer.append("'aaa' from wds_sendorder inner join wds_sendorder_b on wds_sendorder.pk_sendorder = wds_sendorder_b.pk_sendorder ");
		sqlBuffer.append(" where isnull(wds_sendorder.dr,0)=0 and isnull(wds_sendorder_b.dr,0)=0 ");
		sqlBuffer.append(" and wds_sendorder.vbillstatus =1");
		sqlBuffer.append(" and (wds_sendorder.denddate>='"+ new UFDate(System.currentTimeMillis()).toString()+"' or wds_sendorder.denddate is null )");

		WdsWlPubBO pubbo = null;
		String[] invids = null;
		if(iszc){			
			pubbo = new WdsWlPubBO();
			String[] spaceids = pubbo.getSpaceByLogUser(cuserid);
			if(spaceids == null || spaceids.length == 0 || spaceids.length>1){
				throw new BusinessException("��ǰ��¼�˷Ǳ���Ա");
			}
			invids = pubbo.getInvBasdocIDsBySpaceID(spaceids[0]);
			if(invids == null || invids.length ==0)
				return null;
		}else{//�����ܲ���Ա��ֻ�ܿ��������ֿ��Ǳ��ֿ��
			sqlBuffer.append(" and wds_sendorder.pk_outwhouse = '" + cwhid + "'");
		}

		sqlBuffer.append("  and coalesce(wds_sendorder_b.ndealnum,0)-coalesce(wds_sendorder_b.noutnum,0)>0  and coalesce(wds_sendorder_b.nassdealnum,0)-coalesce(wds_sendorder_b.nassoutnum,0)>0");
		if(PuPubVO.getString_TrimZeroLenAsNull(swhere)!=null)
			sqlBuffer.append(" and "+swhere);
		//��ѯ���Գ���ı�ͷ
		List<SendorderVO> lhead = (List<SendorderVO>)getDao().executeQuery(sqlBuffer.toString(), new BeanListProcessor(SendorderVO.class));
		if(lhead == null || lhead.size() == 0)
			return null;


		//		��ѯ����    ������ֳܲ��� ��Ҫ����  ��ԱȨ�޴��  �Ա����н��й���
		StringBuffer bodyWhereBuffer = new StringBuffer();
		List<String> lid = new ArrayList<String>();
		for(SendorderVO hvo:lhead){
			if(lid.contains(hvo.getPrimaryKey()))
				continue;
			lid.add(hvo.getPrimaryKey());
		}
		bodyWhereBuffer.append(" pk_sendorder in "+getTempTableUtil().getSubSql((ArrayList)lid));
		if(iszc){//���˿��ԱȨ�޴��
			bodyWhereBuffer.append(" and pk_invbasdoc in "+getTempTableUtil().getSubSql(invids));
		}
		List<SendorderBVO> lbody = (List<SendorderBVO>)getDao().retrieveByClause(SendorderBVO.class, bodyWhereBuffer.toString());

		if(lbody == null || lbody.size() == 0)
			return null;		

		//��ϱ�ͷ����
		return combinToBillVO(lhead,lbody);		
	}
	private TempTableUtil ttutil = null;
	private TempTableUtil getTempTableUtil(){
		if(ttutil == null)
			ttutil = new TempTableUtil();
		return ttutil;
	}
	private HYBillVO[] combinToBillVO(List<SendorderVO> lhead,List<SendorderBVO> lbody){
		if(lhead == null||lhead.size() == 0 ||lbody == null ||lbody.size() == 0){
			return null;
		}
		Map<String,List<SendorderBVO>> bodyInfor = new HashMap<String, List<SendorderBVO>>();
		List<SendorderBVO> ltmpbody = null;
		String key = null;
		for(SendorderBVO body:lbody){
			key = body.getPk_sendorder();
			if(bodyInfor.containsKey(key)){
				ltmpbody = bodyInfor.get(key);
			}
			else 
				ltmpbody = new ArrayList<SendorderBVO>();
			ltmpbody.add(body);
			bodyInfor.put(key, ltmpbody);
		}

		List<HYBillVO> lbill = new ArrayList<HYBillVO>();
		HYBillVO tmpbill = null;
		//		ltmpbody = new ArrayList<SendorderBVO>();
		for(SendorderVO head:lhead){
			tmpbill = new HYBillVO();
			tmpbill.setParentVO(head);
			ltmpbody = bodyInfor.get(head.getPrimaryKey());
			if(ltmpbody == null || ltmpbody.size() == 0)
				continue;
			tmpbill.setChildrenVO(ltmpbody.toArray(new SendorderBVO[0]));
			lbill.add(tmpbill);
		}
		if(lbill.size()>0)
			return lbill.toArray(new HYBillVO[0]);
		return null;
	}

	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ������ⵥ����erp�������ⵥ�Ĳ�ѯ���
	 * @ʱ�䣺2011-4-9����12:10:17
	 * @param wheresql
	 * @param cwhid
	 * @param cuserid
	 * @return
	 * @throws BusinessException
	 */
	public GeneralBillVO[] queryAlloOutBillVoForAlloIn(String wheresql,String cuserid) throws BusinessException{
		StringBuffer sBuffer = new StringBuffer(
				"select distinct head.vbillcode,head.dbilldate ,head.cwarehouseid ,head.cwhsmanagerid ,head.pk_corp ,"
				+ "head.cdptid ,head.pk_calbody ,head.cotherwhid ,head.cothercorpid ,head.cothercalbodyid ,"
				+ "head.cproviderid,head.cdispatcherid ,head.cdilivertypeid ,head.ccustomerid, "
				+ "head.cinventoryid,head.cbizid,head.cbiztype,head.cgeneralhid,head.fallocflag  "
				+ " from ic_general_h head inner join ic_general_b body "
				+ " on head.cgeneralhid = body.cgeneralhid  "
				+ " left outer join ic_general_bb3 bb3 "
				+ " ON body.cgeneralbid = bb3.cgeneralbid AND bb3.dr = 0"
				+ " inner join to_bill_b tobill on body.cfirstbillbid = tobill.cbill_bid"
				+ " WHERE head.cbilltypecode = '4Y'"
				+ " and head.fallocflag <> 0"
				+ " and (tobill.frowstatuflag <> 7 and tobill.frowstatuflag <> 9)"
				+ " and coalesce(head.boutretflag, 'N') = 'N'"
				+ " and ((coalesce(body.noutnum, 0) > 0 "
				+ " and coalesce(body.noutnum, 0) - coalesce(body.ntranoutnum, 0) - coalesce(body.naccumwastnum, 0) > 0)"
				+ " or (coalesce(body.noutnum, 0) < 0"
				+ " and coalesce(body.noutnum, 0) - coalesce(body.ntranoutnum, 0) - coalesce(body.naccumwastnum, 0) < 0))"
				+ " and head.dr = 0"
				+ " and body.dr = 0"
				+ " and tobill.dr = 0"
				+ " and (head.cothercorpid = '1021'  "
				+ " and  (head.cothercorpid = '1021'"
				+ " AND (head.fbillflag = 3 OR head.fbillflag = 4) "
				+ " and COALESCE(head.boutretflag, 'N') = 'N') and (cbilltypecode = '4Y'))");
		if(PuPubVO.getString_TrimZeroLenAsNull(wheresql)!=null)
			sBuffer.append(" and "+wheresql);



		WdsWlPubBO pubbo = new WdsWlPubBO();
		String[] spaceids = pubbo.getSpaceByLogUser(cuserid);
		if(spaceids == null || spaceids.length == 0 || spaceids.length>1){
			throw new BusinessException("��ǰ��¼�˷Ǳ���Ա");
		}
		String[]	invids = pubbo.getInvBasdocIDsBySpaceID(spaceids[0]);
		if(invids == null || invids.length ==0)
			return null;

		sBuffer.append(" and body.cinvbasid in "+getTempTableUtil().getSubSql(invids));

		List<GeneralBillHeaderVO> lhead = (List<GeneralBillHeaderVO>)getDao().executeQuery(sBuffer.toString(), WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
		if(lhead == null || lhead.size() == 0)
			return null;
		//��ѯ����
		return getGeneralBillVosByHeadVos(lhead.toArray(new GeneralBillHeaderVO[0]),"  cinvbasid in "+getTempTableUtil().getSubSql(invids));
	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ݱ�ͷ��ȡ��������  ����������
	 * @ʱ�䣺2011-4-9����11:57:25
	 * @param heads
	 * @param whereSql
	 * @return
	 * @throws BusinessException
	 */
	public GeneralBillVO[] getGeneralBillVosByHeadVos(GeneralBillHeaderVO[] heads,String whereSql) throws BusinessException{
		List<GeneralBillVO> lbillVo = new ArrayList<GeneralBillVO>(); 
		GeneralBillItemVO[] items = null;
		GeneralBillVO billvo = null;
		for(GeneralBillHeaderVO head:heads){
			if(PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey())==null)
				continue;
			items = queryGeneralBodyVos(head.getPrimaryKey(), whereSql);
			if(items == null||items.length == 0)
				continue;
			billvo = new GeneralBillVO();
			billvo.setParentVO(head);
			billvo.setChildrenVO(items);
			lbillVo.add(billvo);
		}
		if(lbillVo.size()>0)
			return lbillVo.toArray(new GeneralBillVO[0]);
		return null;
	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ��ȡ���������Ŀ�浥��������
	 * @ʱ�䣺2011-4-9����11:58:38
	 * @param sHeadID
	 * @param whereSql
	 * @return
	 * @throws BusinessException
	 */
	public GeneralBillItemVO[] queryGeneralBodyVos(String sHeadID,String whereSql) throws BusinessException{
		//��ģ�� ʵ���� ic  ˽����-------��ģ�����

		return (GeneralBillItemVO[])getGeneralQueryBO().queryAllBodyData(sHeadID, whereSql);
	}

	private IQueryData2 generalQuery  = null;
	private IQueryData2 getGeneralQueryBO() throws BusinessException{
		if(generalQuery == null){
			Object o =NewObjectService.newInstance("ic", "nc.bs.ic.pub.bill.GeneralBillDMO");
			if(!(o instanceof IQueryData2))
				throw new BusinessException("ʵ�������˽�в�ѯ����쳣");
			generalQuery = (IQueryData2)o;
		}
		return generalQuery;
	}


}
