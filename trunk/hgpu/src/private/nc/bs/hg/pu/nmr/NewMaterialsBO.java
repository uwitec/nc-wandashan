package nc.bs.hg.pu.nmr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.itf.uap.bd.inv.IInventoryAssign;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.bd.invdoc.InvbasdocVO;
import nc.vo.bd.loginfo.BDErrLogResult;
import nc.vo.bd.loginfo.ErrLogReturnValue;
import nc.vo.bd.loginfo.ErrlogmsgVO;
import nc.vo.hg.pu.nmr.NewMaterialsVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class NewMaterialsBO {

	private BaseDAO dao = null;

	private BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * �������������� ���䵽������
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-2-25����10:35:26
	 * @param vo
	 * @throws BusinessException
	 */
	public void insertInvbasdoc(NewMaterialsVO vo) throws BusinessException {
		
		if (vo == null)
			throw new BusinessException("��������Ϊ��");
	    
		InvbasdocVO invVO =changeNewMaterialsVOToInvbasdocVO(vo);

		// �������������в�������
		IUifService service = (IUifService) NCLocator.getInstance().lookup(IUifService.class.getName());
		String retBill = service.insert(invVO);
		if (retBill == null) {
			throw new BusinessException("������ʧ��");
		}

		// ���䵽ָ����˾�Ĵ��������
		Object os = shareInvmandoc(retBill);
		
		// �׳������쳣
		getLogMsg(os);
		
		// ���䵽ָ����˾���������ɵ���
		shareCalbody(retBill);
		
		// // ����ƻ���
		updatePrice(retBill, vo);
		
		String sql = " select pk_invmandoc from bd_invmandoc where pk_invbasdoc = '"+ retBill + "' and pk_corp ='" + vo.getPk_corp() + "'";
		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
		if (o == null) {
			throw new BusinessException("����ϵͳ��������쳣");
		}
		
		String str = (String) o;
		String querySQL = "select nnum,pk_planother_b from hg_planother_b  where vreserve1 = '"+ vo.getPk_materials() + "'";
		
		ArrayList al = (ArrayList) getBaseDao().executeQuery(querySQL,HgBsPubTool.ARRAYLISTPROCESSOR);
		
		if (al == null || al.size() == 0)
			return;
		
		int size = al.size();
		
		for (int i = 0; i < size; i++) {
			Object o1 = al.get(i);
			
			if (o1 == null)
				return;
			Object[] ob1 = (Object[]) o1;
			
			if (ob1 == null || ob1.length == 0)
				return;
			
			// ��ƻ���������Ѿ�������ɵĴ�� ���ҽ��Ƿ�ʹ����ʱ���ʸ���ΪN ֻ����ʱ�ƻ�������ʱ���ʱ�������
			String sqlother = " update hg_planother_b set vreserve1 = null, cinventoryid = '"
					+ str+ "',pk_invbasdoc ='"+ retBill+ "',bisuse ='N',nprice ='"+ vo.getNplanprice()
					+ "',"+ " nmny='"+ vo.getNplanprice().multiply(PuPubVO.getUFDouble_NullAsZero(ob1[0]))
					+ "',pk_measdoc='"+ vo.getPk_measdoc()+ "'   where vreserve1 = '"+ vo.getPk_materials()
					+ "' and pk_planother_b = '"+ ob1[1]+ "' and isnull(dr,0)=0 ";
			getBaseDao().executeUpdate(sqlother);
		}

	}

	/**
	 * ��ȡ�ɹ���ʽ �Խ� ͳ��
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-2-25����10:40:51
	 * @param vmi
	 * @return
	 * @throws BusinessException
	 */
	private String getInvbasVmi(Integer vmi) throws BusinessException {
		
		Map<String, String> infor = HgBsPubTool.getDefDoc_PuchType();
		
		if (vmi == 1)
			return infor.get("ͳ��");
		
		return infor.get("�Խ�");
	}

	/**
	 * ���ݻ��������������顢Ŀ�깫˾�������飬����������������Ŀ�깫˾
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-2-23����03:27:13
	 * @param retBill
	 * @throws BusinessException
	 */
	public Object shareInvmandoc(String retBill) throws BusinessException {
		IInventoryAssign service2 = (IInventoryAssign) NCLocator.getInstance()
				.lookup(IInventoryAssign.class);

		String querySQL = "select pk_corp from bd_corp where isnull(dr,0)=0 and length(innercode)>2";// �����ŵ���Ĺ�˾
		Object corp = getBaseDao().executeQuery(querySQL,HgBsPubTool.COLUMNLISTPROCESSOR);
		
		if (corp == null)
			throw new BusinessException("��ȡ��˾ʧ��");
		List<?> list = (List<?>) corp;
		
		if (list == null || list.size() == 0)
			throw new BusinessException("��ȡ��˾ʧ��");
		int size = list.size();
		
		String[] targetPks = new String[size];
		for (int i = 0; i < size; i++) {
			targetPks[i] = PuPubVO.getString_TrimZeroLenAsNull(list.get(i));
		}
		List<String> pkList = new ArrayList<String>();
		pkList.add(retBill);// ������Ĵ����������id
		
		return service2.assignInvbasdocByPkList(targetPks, pkList, null);
	}

	/**
	 * ���ݿ����֯�����������������Ĭ��ģ��ID��������������������
	 * 
	 * @author zhw
	 * @throws BusinessException
	 * @˵�������׸ڿ�ҵ�� 2011-2-23����03:26:58
	 */
	public void shareCalbody(String retBill) throws BusinessException {
		
		String query = "select pk_calbody,pk_corp from bd_calbody where isnull(dr,0)=0 ";
		String sql1 = "select pk_invmandoc,pk_corp from bd_invmandoc where pk_invbasdoc ='"+ retBill + "' and isnull(dr,0) =0";
		
		IInventoryAssign service = (IInventoryAssign) NCLocator.getInstance().lookup(IInventoryAssign.class);
		Object calbody = getBaseDao().executeQuery(query,HgBsPubTool.ARRAYLISTPROCESSOR);
		Object pk_mandoc = getBaseDao().executeQuery(sql1,HgBsPubTool.ARRAYLISTPROCESSOR);
		
		Map<String, String> map = new HashMap<String, String>();
		if (calbody == null)
			throw new BusinessException("��ȡ�����֯ʧ��");
		
		List<?> list = (List<?>) calbody;
		if (list == null || list.size() == 0)
			throw new BusinessException("��ȡ�����֯ʧ��");
		
		int size = list.size();
		
		for (int i = 0; i < size; i++) {
			Object[] o = (Object[]) list.get(i);
			String key = PuPubVO.getString_TrimZeroLenAsNull(o[1]);
			String value = PuPubVO.getString_TrimZeroLenAsNull(o[0]);
			map.put(key, value);
		}
		
		if (pk_mandoc == null)
			throw new BusinessException("��ȡ�������������ʧ��");
		
		List<?> pList = (List<?>) pk_mandoc;
		if (list == null || list.size() == 0)
			throw new BusinessException("��ȡ�������������ʧ��");
		
		int size1 = pList.size();
		String[] targetPks = new String[1];
		List<String> pkList = new ArrayList<String>();

		for (int i = 0; i < size1; i++) {
			Object[] o = (Object[]) pList.get(i);
			String key = PuPubVO.getString_TrimZeroLenAsNull(o[1]);
			String value = PuPubVO.getString_TrimZeroLenAsNull(o[0]);
			targetPks[0] = map.get(key);
			pkList.add(value);
			Object msg = service.assignInvmandoc(targetPks, pkList, null, key);
			
			// �׳��쳣��־
			getLogMsg(msg);
			pkList.clear();
		}
	}

	/**
	 * ����ƻ��۵������������������������
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-3-7����02:53:10
	 * @param pk_invbasdoc
	 * @param vo
	 * @throws BusinessException
	 */
	public void updatePrice(String pk_invbasdoc, NewMaterialsVO vo)
			throws BusinessException {
		
		//����ƻ��۵����������
		String sqlman = "update bd_invmandoc set "+ HgPubConst.INVBAS_NPP_FIELDNAME + "= 0,planprice = "
				+ PuPubVO.getUFDouble_NullAsZero(vo.getNplanprice())+ " where pk_invbasdoc ='" + pk_invbasdoc
				+ "' and isnull(dr,0)=0";
		getBaseDao().executeUpdate(sqlman);
		
		//����ƻ��۵�������������
		String sqlpro = "update bd_produce set jhj = "+ PuPubVO.getUFDouble_NullAsZero(vo.getNplanprice())
				+ ",nbzyj = "+ PuPubVO.getUFDouble_NullAsZero(vo.getNplanprice())
				+ " where pk_invbasdoc ='" + pk_invbasdoc+ "' and isnull(dr,0)=0";
		getBaseDao().executeUpdate(sqlpro);
		
		//������������㷽����������������
		String  sqlinfo = " update bd_produce set usableamount ='"+HgPubConst.usableamount+"',usableamountbyfree='"+HgPubConst.usableamountbyfree+"' where pk_invbasdoc = '"+pk_invbasdoc+"' and isnull(dr,0)=0 ";
		getBaseDao().executeUpdate(sqlinfo);
	}

	/**
	 * ��ȡ�׳����쳣��־
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-4-13����10:28:34
	 * @param os
	 * @throws BusinessException
	 */
	public void getLogMsg(Object os) throws BusinessException {
		
		if (os != null) {
			ErrLogReturnValue errmsg = (ErrLogReturnValue) os;
			
			if (errmsg != null) {
				BDErrLogResult log = errmsg.getErrLogResult();
				
				if(log != null){
					String str = log.getErrorMessage();
					
					if (PuPubVO.getString_TrimZeroLenAsNull(str) != null) {
						throw new BusinessException(str);
					}
					
					ErrlogmsgVO[] logmsgvos = log.getErrlogmsgs();
					if (logmsgvos != null && logmsgvos.length > 0) {
						
						int num = logmsgvos.length;
						for (int i = 0; i < num; i++) {
							String logmsg = logmsgvos[i].getErrormsg();
							if (PuPubVO.getString_TrimZeroLenAsNull(logmsg) != null)
								throw new BusinessException(logmsg);
						}
					}
				}
			}
		}
	}

	public void isHaveChildren(String pk_invcl) throws BusinessException {

		String sql = " select invclasscode from bd_invcl where pk_invcl = '"+ pk_invcl + "'";
		Object oinvcode = getBaseDao().executeQuery(sql,HgBsPubTool.COLUMNPROCESSOR);
		String invcode = PuPubVO.getString_TrimZeroLenAsNull(oinvcode);
		sql = "select count(pk_invcl) from bd_invcl where (invclasscode like '"+ invcode + "%')";
		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);

		if (o != null) {
			int number = 1;
			number = new Integer(o.toString()).intValue();

			if (number > 1)
				throw new BusinessException("�ô���������¼�����");
		}
	}

	/**
	 * ��������������VOת���ɻ�������VO
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-4-27����04:42:05
	 * @param vo��������������VO
	 * @return
	 * @throws BusinessException
	 */
	private InvbasdocVO changeNewMaterialsVOToInvbasdocVO(NewMaterialsVO vo) throws BusinessException{
		
		InvbasdocVO invVO = new InvbasdocVO();
		// �ɹ���ʽ
		invVO.setAttributeValue(HgPubConst.INVBAS_PURTYPE_FIELDNAME, PuPubVO.getString_TrimZeroLenAsNull(getInvbasVmi(vo.getIjjway())));
		// �Ƿ��������
		invVO.setAttributeValue(HgPubConst.INVBAS_VMI_FIELDNAME, PuPubVO
				.getString_TrimZeroLenAsNull(PuPubVO.getUFBoolean_NullAs(vo.getBisdcdx(), UFBoolean.FALSE)));
		// �Ƿ���Ҫ����
		invVO.setAttributeValue(HgPubConst.INVBAS_IMPROT_FIELDNAME, PuPubVO.getString_TrimZeroLenAsNull(vo.getBiszywz()));
		// �Ƿ񽻾�����
		invVO.setAttributeValue(HgPubConst.INVBAS_OLD_FIELDNAME, PuPubVO.getString_TrimZeroLenAsNull(vo.getBisjjwz()));
		
		invVO.setInvcode(vo.getInvcode());// ����
		invVO.setInvname(vo.getVinvname());// ����
		invVO.setInvspec(vo.getVinvspec());// ���
		invVO.setInvtype(vo.getVinvtype());// �ͺ�
		invVO.setPk_measdoc(vo.getPk_measdoc());// ��λ
		invVO.setPk_invcl(vo.getPk_invcl());// ����
		
		invVO.setAttributeValue(HgPubConst.INVBAS_VMT_FIELDNAME, vo.getVmaterial());// ����
		invVO.setAttributeValue(HgPubConst.INVBAS_NPP_FIELDNAME, PuPubVO.getString_TrimZeroLenAsNull(vo.getNplanprice()));// �ƻ���
		invVO.setAttributeValue(HgPubConst.INVBAS_VTS_FIELDNAME, vo.getVtechstan());// ������׼
		invVO.setInvmnecode(vo.getInvmnecode());// ������
		invVO.setPk_taxitems(vo.getPk_taxitems());// ˰Ŀ
		
		invVO.setDiscountflag(PuPubVO.getUFBoolean_NullAs(vo.getDiscountflag(),UFBoolean.FALSE));// �۸��ۿ�
		invVO.setLaborflag(PuPubVO.getUFBoolean_NullAs(vo.getLaborflag(),UFBoolean.FALSE));// Ӧ˰����
		invVO.setPk_corp(HgPubConst.PK_CORP);
		
		invVO.setStoreunitnum(vo.getStoreunitnum());//���ٱ�׼�洢��λ
		invVO.setPk_prodline(vo.getPk_prodline());//��Ʒ������
		invVO.setSealflag(vo.getSealflag());//����־
	    invVO.setIsmngstockbygrswt(vo.getIsmngstockbygrswt());//��ë�ع�����
	    
	    invVO.setGraphid(vo.getGraphid());//ͼ��
	    invVO.setForinvname(vo.getForinvname());//��������
	    invVO.setSetpartsflag(vo.getSetpartsflag());//�Ƿ���׼�
		invVO.setPk_measdoc1(vo.getPk_measdoc1());//����Ĭ�ϵ�λ
		invVO.setPk_measdoc2(vo.getPk_measdoc2());//�ɹ�Ĭ�ϵ�λ
		invVO.setPk_measdoc3(vo.getPk_measdoc3());//���Ĭ�ϵ�λ
		invVO.setPk_measdoc5(vo.getPk_measdoc5());//����Ĭ�ϵ�λ
		
		invVO.setInvpinpai(vo.getInvpinpai());//Ʒ��
		invVO.setWeitunitnum(vo.getWeitunitnum());//���ٱ�׼������λ
		invVO.setLength(PuPubVO.getString_TrimZeroLenAsNull(vo.getLength()));//����
		invVO.setIsstorebyconvert(vo.getIsstorebyconvert());//�����ʽ��
		invVO.setAssistunit(PuPubVO.getUFBoolean_NullAs(vo.getAssistunit(),UFBoolean.FALSE));//�Ƿ񸨼�������
		invVO.setShipunitnum(vo.getShipunitnum());//���ٱ�׼���䵥λ
		invVO.setAutobalancemeas(vo.getAutobalancemeas());//�Զ�ƽ����������
		invVO.setInvbarcode(vo.getInvbarcode());//������
		invVO.setInvshortname(vo.getInvshortname());//������

		invVO.setUnitvolume(vo.getUnitvolume());//��λ����
		invVO.setUnitweight(vo.getUnitweight());////��λ���
		invVO.setWidth(PuPubVO.getString_TrimZeroLenAsNull(vo.getWidth()));//���
		invVO.setHeight(PuPubVO.getString_TrimZeroLenAsNull(vo.getHeight()));//�߶�
		invVO.setPk_measdoc6(vo.getPk_measdoc6());//���ۼ�����λ
		invVO.setIsretail(vo.getIsretail());//�Ƿ�����
		invVO.setStatus(VOStatus.NEW);
		invVO.setDr(0);
		invVO.setCreator(vo.getVapproveid());
		invVO.setCreatetime(vo.getTs());
		
		return invVO;
	}
	
	public void  isExitVcode(NewMaterialsVO vo) throws BusinessException{
		String invcode = vo.getInvcode();
		// У���������������Ƿ���ڸô������Ĵ��

		if (invcode != null) {
			String sql = "select count(*) from bd_invbasdoc where invcode ='"+ invcode + "' and isnull(dr,0)=0";

			int o = PuPubVO.getInteger_NullAs(getBaseDao().executeQuery(sql,HgBsPubTool.COLUMNPROCESSOR), HgPubTool.INTEGER_ZERO_VALUE);

			if (o > 0) {
				throw new BusinessException("��ϵͳ�����иô��!");
			}
		}
	}
	
	public void  isExitVcnameAndVspec(NewMaterialsVO vo) throws BusinessException{
		String invname = vo.getVinvname();
		String invspec = vo.getVinvspec();
		// У���������������Ƿ���ڸ�����+���

		if (invspec != null || invname !=null ) {
			String sql = "select count(*) from bd_invbasdoc where invname ='"+ invname + "' and invspec ='"+invspec+"' and isnull(dr,0)=0";

			int o = PuPubVO.getInteger_NullAs(getBaseDao().executeQuery(sql,HgBsPubTool.COLUMNPROCESSOR), HgPubTool.INTEGER_ZERO_VALUE);

			if (o > 0) {
				throw new BusinessException("��ϵͳ�����иô��!");
			}
		}
	}

}
