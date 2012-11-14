package nc.bs.wds.ic.so.out;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.HYPubBO;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.wds.ic.write.back4c.MultiBillVO;
import nc.vo.wds.ic.write.back4c.Writeback4cB1VO;
import nc.vo.wds.ic.write.back4c.Writeback4cB2VO;
import nc.vo.wds.ic.write.back4c.Writeback4cHVO;
import nc.vo.wds.ic.zgjz.ZgjzBVO;
import nc.vo.wds.ic.zgjz.ZgjzHVO;
import nc.vo.wl.pub.WdsWlPubConst;
import org.apache.tools.ant.BuildException;

/**
 * ��ʽ�������۳���ش�����
 * 
 * @author lyf
 * 
 */
public class ChangToWDSO {

	HYPubBO pubbo = null;
	HYPubBO getHypubBO() {
		if (pubbo == null) {
			pubbo = new HYPubBO();
		}
		return pubbo;
	}
	BaseDAO dao = null;
	
	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	private String coperator = null;
	private String pk_corp = null;
	private UFDate logDate = null;

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ ���۳��ⵥǩ�֣��������۳���ش���
	 * @ʱ�䣺2011-10-26����01:14:05
	 * @param billVO
	 * @param coperator
	 *            ����Ա
	 * @param pk_crop
	 *            ��¼��˾
	 * @param date
	 *            ��¼����
	 * @return
	 * @throws BusinessException
	 */
	public void onSign(AggregatedValueObject billVO, String coperator,
			String pk_corp, String date) throws BusinessException {
		if (billVO == null) {
			return;
		}
		this.coperator = coperator;
		this.pk_corp = pk_corp;
		this.logDate = new UFDate(date);		
		//�����۶��� �����ֵ�
		HashMap<String, ArrayList<TbOutgeneralBVO>> map = new HashMap<String, ArrayList<TbOutgeneralBVO>>();
		TbOutgeneralHVO head = null;
		if (billVO instanceof IExAggVO) {
			MyBillVO bill = (MyBillVO) billVO;
			head = (TbOutgeneralHVO) bill.getParentVO();
			TbOutgeneralBVO[] bvos = (TbOutgeneralBVO[]) bill.getTableVO(bill.getTableCodes()[0]);
			for (TbOutgeneralBVO bvo : bvos) {
				String key = PuPubVO.getString_TrimZeroLenAsNull(bvo
						.getCfirstbillhid());
				if (key == null) {
					continue;
				}
				if (map.containsKey(key)) {
					map.get(key).add(bvo);
				} else {
					ArrayList<TbOutgeneralBVO> list = new ArrayList<TbOutgeneralBVO>();
					list.add(bvo);
					map.put(key, list);
				}
			}
		}
		
		if (map.size() == 0) {
			return;
		}
		//�������۳���ش���
		for (String key : map.keySet()) {
			MultiBillVO writeBillVO = getMutiBillvo(key, head, map.get(key));
			// ����Դ��ϸ�ϵ��������ܼӵ����������ϣ�Writeback4cB2VO-->Writeback4cB1VO��
			sumNums(writeBillVO);
			getHypubBO().saveBill(writeBillVO);
		}
	}
	/**
	 * 
	 * @throws BusinessException 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ:���������ݹ����˵��ۼư�������
	 * @ʱ�䣺2011-12-22����09:50:16
	 */
	public void updateZgjzNum(TbOutgeneralHVO head,TbOutgeneralBVO[] xnvo,boolean isAudit) throws BusinessException{
		if(xnvo == null || xnvo.length ==0){
			return ;
		}
		String pk_outwhouse = PuPubVO.getString_TrimZeroLenAsNull(head.getSrl_pk());
		if(pk_outwhouse == null){
			throw new BusinessException("�����ݹ��������ݣ�����ֿⲻ��Ϊ��");
		}
		//���մ�����ܳ�������
		Map<String, UFDouble[]> map = new HashMap<String, UFDouble[]>();
		for(TbOutgeneralBVO bvo:xnvo){
			String pk_invmandoc = PuPubVO.getString_TrimZeroLenAsNull(bvo.getCinventoryid());
			if(pk_invmandoc == null){
				continue;
			}
			UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNoutnum());
			UFDouble noutassistnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNoutassistnum());
			if(map.containsKey(pk_invmandoc)){
				UFDouble noutnum_old = map.get(pk_invmandoc)[0].add(noutnum);
				UFDouble noutassistnum_old = map.get(pk_invmandoc)[1].add(noutassistnum);
				map.get(pk_invmandoc)[0] = noutnum_old;
				map.get(pk_invmandoc)[1] = noutassistnum_old;
			}else{
				UFDouble[]  nums= new UFDouble[2];
				nums[0]=noutnum;
				nums[1]=noutassistnum;
				map.put(pk_invmandoc,nums );
			}
		}
		if(map.size()==0){
			return;
		}
		//��ѯ���ڵ��ݹ����� 
			String strWhere = " pk_outwhouse='"+pk_outwhouse+"' and  isnull(dr,0)=0  and isnull(ilacktype,0)=0 and vbillstatus='"+IBillStatus.FREE+"'";
			SuperVO[] heads = getHypubBO().queryByCondition(ZgjzHVO.class, strWhere);
			if(heads == null || heads.length ==0){
				throw new BusinessException("δ��ѯ���ó���ֿ����������Ƿ������");
			}else if(heads.length >1){
				throw new BusinessException("��ѯ���ó���ֿ��������̬��������Ƿ������");
			}
			ZgjzHVO hvo = (ZgjzHVO)heads[0]; 
			ZgjzBVO[] bodys = (ZgjzBVO[])getHypubBO().queryByCondition(ZgjzBVO.class, " isnull(dr,0)=0 and pk_wds_zgjz_h='"+hvo.getPrimaryKey()+"'");
			Set<String> keys = map.keySet();
			for(String key:keys){
				boolean falge = false;
				for(ZgjzBVO body:bodys){
					String pk_invmandoc =PuPubVO.getString_TrimZeroLenAsNull(body.getPk_invmandoc()) ;
					if(pk_invmandoc == null){
						continue;
					}
					if(key.equalsIgnoreCase(pk_invmandoc)){
						//���³�������
						UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(body.getNoutnum());
						UFDouble noutassnum = PuPubVO.getUFDouble_NullAsZero(body.getNoutassnum());
						UFDouble noutnum_add= map.get(key)[0];
						UFDouble noutassnum_add= map.get(key)[1];
						if(!isAudit){
							noutnum_add = noutnum_add.multiply(-1);
							noutassnum_add = noutassnum_add.multiply(-1);
						}
						body.setNoutnum(noutnum.add(noutnum_add));
						body.setNoutassnum(noutassnum.add(noutassnum_add));
						//У�� ���ܳ���
						UFDouble nlastnum= PuPubVO.getUFDouble_NullAsZero(body.getNlastnum());//�ݹ�Ƿ����
						UFDouble nreducnum_new= PuPubVO.getUFDouble_NullAsZero(body.getNreducnum());//�����
						UFDouble noutnum_new= PuPubVO.getUFDouble_NullAsZero(body.getNoutnum());//������
						if(nlastnum.sub(nreducnum_new).sub(noutnum_new).doubleValue()<0){
							throw new BusinessException("���:"+getInvCode(key)+"�������ݹ�δ��������");
						}
						falge = true;
					}
				}
				if(!falge){
					throw new BusinessException("���:"+getInvCode(key)+"�����ݹ�");
				}
			}
			getHypubBO().updateAry(bodys);
		
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ:��ѯ�������
	 * @ʱ�䣺2011-12-15����07:20:55
	 * @param pk_ivnmandoc
	 * @return
	 */
	public String getInvCode(String pk_ivnmandoc) throws BusinessException{
		String sql =" select invcode from bd_invbasdoc where pk_invbasdoc = " +
				"( select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_ivnmandoc+"' and isnull(dr,0)=0 ) " +
						" and isnull(dr,0)=0 ";
		String value = (String)getBaseDAO().executeQuery(sql, new ColumnProcessor());
		if(value == null){
			value ="";
		}
		return value;
	}


	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ ���۳��ⵥȡ��ǩ��
	 * @ʱ�䣺2011-10-26����01:14:05
	 * @param billVO
	 * @param coperator
	 *            ����Ա
	 * @param pk_crop
	 *            ��¼��˾
	 * @param date
	 *            ��¼����
	 * @return
	 * @throws BusinessException 
	 */
	public void onCanclSign(AggregatedValueObject billVO, String coperator,
			String pk_corp, String date) throws BusinessException{

		if (billVO == null) {
			return;
		}
		this.coperator = coperator;
		this.pk_corp = pk_corp;
		this.logDate = new UFDate(date);
		//��ѯ���۳��ⵥ ���� ���۳���ش���ͷ
		TbOutgeneralHVO head = (TbOutgeneralHVO) billVO.getParentVO();
		StringBuffer bur = new StringBuffer();
		bur.append(" isnull(dr,0)=0  ");
		bur.append(" and pk_corp='"+pk_corp+"'");
		bur.append(" and pk_wds_writeback4c_h in (");
		bur.append(" select distinct pk_wds_writeback4c_h from wds_writeback4c_b2 ");
		bur.append(" where isnull(dr,0)=0 and csourcebillhid='"+head.getPrimaryKey()+"'");
		bur.append(" )");
		Writeback4cHVO[] writeHvos = (Writeback4cHVO[]) getHypubBO()
		.queryByCondition(Writeback4cHVO.class, bur.toString());
		//���������
		if (writeHvos != null && writeHvos.length > 0) {
			for(Writeback4cHVO writeHead:writeHvos){
				//�鿴���۳���ش����Ƿ� �Ѿ�����
				Integer vbillstatus = PuPubVO.getInteger_NullAs(writeHead.getVbillstatus(), IBillStatus.FREE);
				if (vbillstatus == IBillStatus.CHECKPASS) {
					throw new BusinessException("�������۳���̨�˻�д�Ѿ��������,����������");
				}
				//�������۳���ش��� �ۺ�vo
				MultiBillVO writeBillVO = new MultiBillVO();
				String where = " isnull(dr,0)=0 and pk_wds_writeback4c_h = '"
						+ writeHead.getPrimaryKey() + "'";
				//��� ���۳���ش������� 1
				Writeback4cB1VO[] b1vos = (Writeback4cB1VO[]) getHypubBO()
						.queryByCondition(Writeback4cB1VO.class, where);
				//��� ���۳���ش������� 2
				Writeback4cB2VO[] b2vos = (Writeback4cB2VO[]) getHypubBO()
				.queryByCondition(Writeback4cB2VO.class, where);
				//ɾ�� ���۳���ش���  ��Ӧ ���۳������Դ��ϸ
				//�ж��Ƿ� �����������۳���ش���
				boolean isDel =false;
				int count =0;
				if(b2vos !=null ){
					for(Writeback4cB2VO b2vo:b2vos){
						String csourcebillhid = b2vo.getCsourcebillhid();
						if(head.getPrimaryKey().equalsIgnoreCase(csourcebillhid)){
							b2vo.setStatus(VOStatus.DELETED);
							count++;
						}
					}
				}
				//������۳���ش��� ��Դ��ϸ  ���Ǳ�������� ���۳��ⵥ �����۳���ش�������Ϊ����ɾ��
				if(count == b2vos.length)
					isDel = true;
				//���� ���۳���ش��� ��ͷ ����
				writeBillVO.setParentVO(writeHead);
				writeBillVO.setTableVO(writeBillVO.getTableCodes()[0], b1vos);
				writeBillVO.setTableVO(writeBillVO.getTableCodes()[1], b2vos);
				if(isDel){
					getHypubBO().deleteBill(writeBillVO);
				}else{
					//�� ���۳���ش���   ����2 ��Դ��ϸΪ  ������������۳��ⵥ��   ��¼   ����Ϊɾ��״̬
					//��  ���۳���ش���  ����1 ������Ϣ ��Ӧ��  ��Դ��ϸ��Ϊɾ��̬�ļ�¼  �Ļ�����  ����  ������vo״̬Ϊ�޸�̬
					sumNumsDel(writeBillVO);
					//�������۳���ش���
					getHypubBO().saveBill(writeBillVO);
				}
			}
		}else{
			throw new BuildException("��ѯ�������۳���ش������쳣��δ��ѯ������");
		}
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-10-27����10:15:42
	 * @param key  ���۶�������
	 * @param head �������۳����ͷvo
	 * @param list �������۳������vo
	 * @return
	 * @throws BusinessException
	 */
	public MultiBillVO getMutiBillvo(String key, TbOutgeneralHVO head,
			ArrayList<TbOutgeneralBVO> list) throws BusinessException {
	   //�������۳���ش��� �ۺ�vo
		MultiBillVO writeBillVO = new MultiBillVO();
		//�������۶�������  �������۶����ۺ�vo
		SaleOrderVO orderVO = getSaleOrder(key);
		//������ͷ
		SaleorderHVO orderHead = orderVO.getHeadVO();
		//��������
		SaleorderBVO[] orderBods = orderVO.getBodyVOs();
		if (orderBods == null || orderBods.length == 0) {
			throw new BusinessException("��ѯ���۶�������ʧ��");
		}
		// ���ݶ������� ��ѯ���۳���ش��� ��ͷ
		String strWhere = " isnull(dr,0)=0 and csaleid='" + key
				+ "' and pk_corp='" + pk_corp + "'";
		Writeback4cHVO[] writeHvos = (Writeback4cHVO[]) getHypubBO()
				.queryByCondition(Writeback4cHVO.class, strWhere);
		
		if (writeHvos != null && writeHvos.length > 0) {
			//����������۳���ش��� 
			Integer vbillstatus = PuPubVO.getInteger_NullAs(writeHvos[0]
					.getVbillstatus(), IBillStatus.FREE);
			//�鿴�Ƿ��Ѿ�����
			if (vbillstatus == IBillStatus.CHECKPASS) {
				throw new BusinessException("�������۳���̨�˻�д�Ѿ��������,���������");
			}
			//��ѯ�� ���۳���ش�����������
			String where = " isnull(dr,0)=0 and pk_wds_writeback4c_h = '"
					+ writeHvos[0].getPrimaryKey() + "'";
			//���۳��� �ش����� 1(�����۶��� ������id ���ܵ���Ϣ)
			Writeback4cB1VO[] b1vos = (Writeback4cB1VO[]) getHypubBO()
					.queryByCondition(Writeback4cB1VO.class, where);
			//���۳��� �ش����� 2���������۳��ⵥ������ϸ��
			Writeback4cB2VO[] b2vos = (Writeback4cB2VO[]) getHypubBO()
			.queryByCondition(Writeback4cB2VO.class, where);
			int i=0;
			//����  ���۳��� �ش����� 2 ��ʼ�к�
			if(b2vos != null){
				i = 10*b2vos.length;
			}
			//�������۳��� ���� --->  ���۳��� �ش����� 2  ���ݽ���  ����vo״̬Ϊ����
			Writeback4cB2VO[] b2vosNew = getWriteBackB2vo(i,head, list);
			//�� ����  ���۳��� �ش����� 2 ����  ׷�ӵ� �Ѵ��ڵ� ���۳��� �ش����� 2 ������ 
			ArrayList<Writeback4cB2VO> b2list = new ArrayList<Writeback4cB2VO>();
			b2list.addAll(Arrays.asList(b2vos));
			b2list.addAll(Arrays.asList(b2vosNew));
			//�������۳���ش��� ��ͷ
			writeBillVO.setParentVO(writeHvos[0]);
			//�������۳���ش���  ����1
			writeBillVO.setTableVO(writeBillVO.getTableCodes()[0], b1vos);
			//�������۳���ش��� ���� 2
			writeBillVO.setTableVO(writeBillVO.getTableCodes()[1], b2list.toArray(new Writeback4cB2VO[0]));
		} else {
			//������������۳��� �ش���
			//���� ���۳���ش�����ͷ ����vo״̬Ϊ����
			Writeback4cHVO hvo = getWrirteBackHvo(key, orderHead.getVreceiptcode());
		    // �������۳���ش������� 1 	����vo״̬Ϊ����		
			Writeback4cB1VO[] b1vos = getWriteBackB1vo(orderBods);
			//�������۳���ش������� 2  ����vo״̬Ϊ����
			Writeback4cB2VO[] b2vos = getWriteBackB2vo(0,head, list);
			writeBillVO.setParentVO(hvo);
			writeBillVO.setTableVO(writeBillVO.getTableCodes()[0], b1vos);
			writeBillVO.setTableVO(writeBillVO.getTableCodes()[1], b2vos);
		}
		return writeBillVO;
	}
	public SaleOrderVO getSaleOrder(String primarykey) throws BusinessException{
		if(primarykey==null){
			primarykey ="";
		}
		SaleOrderVO bill = new SaleOrderVO();
		String headSql = " select * from so_sale where isnull(dr,0)=0 and csaleid='"+primarykey+"'";
		ArrayList<SaleorderHVO> headList= (ArrayList<SaleorderHVO>)getBaseDAO().executeQuery(headSql, new BeanListProcessor(SaleorderHVO.class));
		if(headList == null || headList.size() ==0){
			throw new BusinessException("��ѯ���۶���ʧ�� ");
		}
		String bodySql = " select * from so_saleorder_b where isnull(dr,0)=0 and csaleid='"+primarykey+"'";
		ArrayList<SaleorderBVO> bodyList= (ArrayList<SaleorderBVO>)getBaseDAO().executeQuery(bodySql, new BeanListProcessor(SaleorderBVO.class));
		if(headList == null || headList.size() ==0){
			throw new BusinessException("��ѯ���۶�����������ʧ�� ");
		}
		bill.setParentVO(headList.get(0));
		bill.setChildrenVO(bodyList.toArray(new SaleorderBVO[0]));
		return bill;
	}
	/**
	 * 
	 * @���ߣ���������һ�����۳����д��ͷ
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-10-26����03:09:05
	 * @return
	 */
	public Writeback4cHVO getWrirteBackHvo(String csaleid, String vbillno) {
		Writeback4cHVO head = new Writeback4cHVO();
		head.setCsaleid(csaleid);// ���۶�������
		head.setVbillno(vbillno);// ���ݺ�
		head.setVbillstatus(IBillStatus.FREE);// ����״̬
		head.setPk_billtype(WdsWlPubConst.WDSO);// ��������
		head.setStatus(VOStatus.NEW);
		head.setPk_corp(pk_corp);
		head.setVoperatorid(coperator);
		head.setDbilldate(logDate);
		head.setDmakedate(logDate);
		return head;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ :�������۶���
	 * @ʱ�䣺2011-10-26����03:31:58
	 * @return
	 */
	public Writeback4cB1VO[] getWriteBackB1vo(SaleorderBVO[] orderBodys) {
		if (orderBodys == null || orderBodys.length == 0) {
			return null;
		}
		Writeback4cB1VO[] b1vos = new Writeback4cB1VO[orderBodys.length];
		for (int i = 0; i < orderBodys.length; i++) {
			b1vos[i] = new Writeback4cB1VO();
			b1vos[i].setCrowno("" + (i + 1) * 10);// �к�
			b1vos[i].setCsaleid(orderBodys[i].getCsaleid());// ���۶�������
			b1vos[i].setCorder_bid(orderBodys[i].getCorder_bid());// ���۶����ֱ�����
			b1vos[i].setPk_invmandoc(orderBodys[i].getCinventoryid());// �������id
			b1vos[i].setPk_invbasdoc(orderBodys[i].getCinvbasdocid());// �������id
			b1vos[i].setUnit(orderBodys[i].getCunitid());// �����������λ
			b1vos[i].setAssunit(orderBodys[i].getCpackunitid());// �����������λ
			b1vos[i].setStatus(VOStatus.NEW);
		}
		return b1vos;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-10-27����09:17:01
	 * @param list
	 * @return
	 */
	public Writeback4cB2VO[] getWriteBackB2vo(int rowNo,TbOutgeneralHVO head,
			ArrayList<TbOutgeneralBVO> list) {
		if (list == null || list.size() == 0) {
			return null;
		}
		Writeback4cB2VO[] b2vos = new Writeback4cB2VO[list.size()];
		for (int i = 0; i < list.size(); i++) {
			b2vos[i] = new Writeback4cB2VO();
			rowNo = rowNo+10;
			b2vos[i].setCrowno("" +rowNo);// �к�
			// ���۶�����Ϣ
			b2vos[i].setCfirsttype(list.get(i).getCfirsttype());
			b2vos[i].setCfirstbillhid(list.get(i).getCfirstbillhid());
			b2vos[i].setCfirstbillbid(list.get(i).getCfirstbillbid());
			b2vos[i].setVfirstbillcode(list.get(i).getVfirstbillcode());
			b2vos[i].setPk_invmandoc(list.get(i).getCinventoryid());
			b2vos[i].setPk_invbasdoc(list.get(i).getCinvbasid());
			b2vos[i].setUnit(list.get(i).getUnitid());
			b2vos[i].setAssunit(list.get(i).getCastunitid());
			// �˵���Ϣ
			b2vos[i].setPk_soorder(list.get(i).getCsourcebillhid());
			b2vos[i].setPk_soorder_b(list.get(i).getCsourcebillbid());
			b2vos[i].setNarrangnum(list.get(i).getNshouldoutnum());
			b2vos[i].setNassarrangnum(list.get(i).getNshouldoutassistnum());
			// �������۳��ⵥ��Ϣ
			b2vos[i].setVsourcebillcode(head.getVbillcode());
			b2vos[i].setCsourcebillhid(list.get(i).getGeneral_pk());
			b2vos[i].setCsourcebillbid(list.get(i).getGeneral_b_pk());
			b2vos[i].setCsourcetype(WdsWlPubConst.BILLTYPE_SALE_OUT);
			b2vos[i].setNoutnum(list.get(i).getNoutnum());
			b2vos[i].setNoutassistnum(list.get(i).getNoutassistnum());
			b2vos[i].setStatus(VOStatus.NEW);
			b2vos[i].setVdef4(head.getPk_cargdoc());//��λ
			b2vos[i].setIsxnap(list.get(i).getIsxnap());//�Ƿ����ⰲ�� liuys add
		}
		return b2vos;
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ ������ϸ�������ܵ�������������
	 * @ʱ�䣺2011-10-27����10:40:34
	 */
	public void sumNumsDel(MultiBillVO writeBillVO) {
		Writeback4cB1VO[] b1vos = (Writeback4cB1VO[]) writeBillVO
				.getTableVO(writeBillVO.getTableCodes()[0]);
		Writeback4cB2VO[] b2vos = (Writeback4cB2VO[]) writeBillVO
				.getTableVO(writeBillVO.getTableCodes()[1]);
		if (b2vos == null || b2vos.length == 0) {
			return;
		} else {
			for (Writeback4cB2VO b2vo : b2vos) {
				String corder_bid = PuPubVO.getString_TrimZeroLenAsNull(b2vo
						.getCfirstbillbid());
				if ( b2vo.getStatus() != VOStatus.DELETED)
					continue;
				UFDouble narrangnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNarrangnum());// �˵�����
				UFDouble nassarrangnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNassarrangnum());
				UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNoutnum());// ʵ�ʳ�������
				UFDouble noutassistnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNoutassistnum());
				for (Writeback4cB1VO b1vo : b1vos) {
					UFDouble nnumber = PuPubVO.getUFDouble_NullAsZero(b1vo
							.getNnumber());// �ۼ��˵�����
					UFDouble npacknumber = PuPubVO.getUFDouble_NullAsZero(b1vo
							.getNpacknumber());
					UFDouble ntotalOutnum = PuPubVO.getUFDouble_NullAsZero(b1vo
							.getNoutnum());// �ۼ�ʵ�ʳ�������
					UFDouble ntotalOutassistnum = PuPubVO
							.getUFDouble_NullAsZero(b1vo.getNoutassistnum());
					String orderid = b1vo.getCorder_bid();
					if (corder_bid.equalsIgnoreCase(orderid)) {
						b1vo.setNnumber(nnumber.sub(narrangnum));
						b1vo.setNpacknumber(npacknumber.sub(nassarrangnum));
						b1vo.setNoutnum(ntotalOutnum.sub(noutnum));
						b1vo.setNoutassistnum(ntotalOutassistnum
								.sub(noutassistnum));
						b1vo.setStatus(VOStatus.UPDATED);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ ������ϸ�������ܵ�������������
	 * @ʱ�䣺2011-10-27����10:40:34
	 */
	public void sumNums(MultiBillVO writeBillVO) {
		Writeback4cB1VO[] b1vos = (Writeback4cB1VO[]) writeBillVO
				.getTableVO(writeBillVO.getTableCodes()[0]);
		Writeback4cB2VO[] b2vos = (Writeback4cB2VO[]) writeBillVO
				.getTableVO(writeBillVO.getTableCodes()[1]);
		if (b2vos == null || b2vos.length == 0) {
			return;
		} else {
			for (Writeback4cB2VO b2vo : b2vos) {
				//ȡ�� ���۳��� Ӧ������  �� ʵ�ʳ�����
				String corder_bid = PuPubVO.getString_TrimZeroLenAsNull(b2vo
						.getCfirstbillbid());
				String primaryKey = PuPubVO.getString_TrimZeroLenAsNull(b2vo.getPrimaryKey());
				if (corder_bid == null || primaryKey != null)
					continue;
				UFDouble narrangnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNarrangnum());// �˵�����
				UFDouble nassarrangnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNassarrangnum());
				UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNoutnum());// ʵ�ʳ�������
				UFDouble noutassistnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNoutassistnum());
				//����  �������� id   �����۳��� �ش� �� ����2 ����ϸ  �� Ӧ������  �� ʵ�ʳ�����    ���ܵ�  ���۳��� �ش� �� ����1 
				for (Writeback4cB1VO b1vo : b1vos) {
					UFDouble nnumber = PuPubVO.getUFDouble_NullAsZero(b1vo
							.getNnumber());// �ۼ��˵�����
					UFDouble npacknumber = PuPubVO.getUFDouble_NullAsZero(b1vo
							.getNpacknumber());
					UFDouble ntotalOutnum = PuPubVO.getUFDouble_NullAsZero(b1vo
							.getNoutnum());// �ۼ�ʵ�ʳ�������
					UFDouble ntotalOutassistnum = PuPubVO
							.getUFDouble_NullAsZero(b1vo.getNoutassistnum());
					String orderid = b1vo.getCorder_bid();
					if (corder_bid.equalsIgnoreCase(orderid)) {
						b1vo.setNnumber(nnumber.add(narrangnum));
						b1vo.setNpacknumber(npacknumber.add(nassarrangnum));
						b1vo.setNoutnum(ntotalOutnum.add(noutnum));
						b1vo.setNoutassistnum(ntotalOutassistnum
								.add(noutassistnum));
						if (PuPubVO.getString_TrimZeroLenAsNull(b1vo
								.getPrimaryKey()) != null) {
							b1vo.setStatus(VOStatus.UPDATED);
						}
					}
				}
			}
		}
	}
}
