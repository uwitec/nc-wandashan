package nc.bs.ic.other.in;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wl.pub.WdsWlPubConst;

public class BackCheckTool implements IBDBusiCheck {
	private BaseDAO dao = null;
	private BaseDAO getBaseDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		// TODO Auto-generated method stub
//		��������У��  ���ɵ��ݺ�   ���+���β����ظ�
		
		if(intBdAction == IBDACTION.SAVE){
			TbGeneralHVO head = (TbGeneralHVO)vo.getParentVO();
			if(PuPubVO.getString_TrimZeroLenAsNull(head.getGeh_billcode())==null)
				throw new BusinessException("���ݺ�Ϊ��");
			TbGeneralBVO[] bodys = (TbGeneralBVO[])vo.getChildrenVO();
			//У����+���κŲ����ظ�
//			BaseDAO dao = new BaseDAO();
			String sql = " select count(0) from tb_general_b where geb_cinvbasid = ? and geb_vbatchcode = ? and isnull(dr,0)=0";
			SQLParameter parameter = null;
			for(TbGeneralBVO body:bodys){
				parameter = new SQLParameter();
				parameter.addParam(body.getGeb_cinvbasid());
				parameter.addParam(body.getGeb_vbatchcode());
				int index = PuPubVO.getInteger_NullAs(getBaseDao().executeQuery(sql, parameter, WdsPubResulSetProcesser.COLUMNPROCESSOR),-1);
				if(index>0)
					throw new BusinessException("���κţ�"+body.getGeb_vbatchcode()+"�ظ�");
			}
		}
		
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub
		//���ɴ��״̬��Ϣ   ����ô�������δ��״̬�Ѵ��� ���ֲ���
		
		if(intBdAction == IBDACTION.SAVE){
			TbGeneralHVO head = (TbGeneralHVO)billVo.getParentVO();
			if(PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey())==null){
				//������״̬������  ά���ִ���
				insertInvBatchState((TbGeneralBVO[])billVo.getChildrenVO(),head.getPk_cargdoc());
			}else{
				//ǰ̨����  ������ⵥ�޸�ʱ ���в���  ���  ����  �������޸�
			}
				
		}
	}
	
	private void insertInvBatchState(TbGeneralBVO[] bodys,String cargdocPK) throws BusinessException{
		if(bodys == null || bodys.length == 0){
			return;
		}

		//��ȡ������ϸ��Ϣ
		String[] bids = new String[bodys.length];
		for(int i=0;i<bodys.length;i++){
			bids[i] = bodys[i].getGeb_pk();
		}

		String whereSql = " isnull(dr,0)=0 and geb_pk in "+new TempTableUtil().getSubSql(bids);

		Collection<TbGeneralBBVO> bbodys = (Collection<TbGeneralBBVO>)getBaseDao().retrieveByClause(TbGeneralBBVO.class, whereSql);

		if(bbodys == null || bbodys.size() ==0)
			throw new BusinessException("��ȡ���������Ϣ�쳣");

		StockInvOnHandVO[] tbWarehousestockVO = new StockInvOnHandVO[bbodys.size()];

		Iterator<TbGeneralBBVO> it = bbodys.iterator();
		TbGeneralBBVO tbbbvo = null;
		StockInvOnHandVO tbWarehousestockVO1 = null;
		ArrayList<StockInvOnHandVO> ldata = new ArrayList<StockInvOnHandVO>();
		while(it.hasNext()){
			tbWarehousestockVO1 = new StockInvOnHandVO();



			// ��������

			tbWarehousestockVO1.setPplpt_pk(tbbbvo.getCdt_pk());

			// dr
			tbWarehousestockVO1.setDr(0);
			// ������

			tbWarehousestockVO1.setWhs_stockpieces(tbbbvo
					.getGebb_num());
			tbWarehousestockVO1.setWhs_oanum(tbbbvo.gebb_num);

			// ������

			tbWarehousestockVO1
			.setWhs_hsl(tbbbvo.getGebb_hsl());

			// ����

			tbWarehousestockVO1.setWhs_nprice(tbbbvo
					.getGebb_nprice());

			// ���

			tbWarehousestockVO1.setWhs_nmny(tbbbvo
					.getGebb_nmny());

			// ������

			tbWarehousestockVO1.setWhs_stocktonnage(PuPubVO.getUFDouble_NullAsZero(tbbbvo
					.getGebb_num()).multiply(
							tbbbvo.getGebb_hsl()
					));
			tbWarehousestockVO1.setWhs_omnum(PuPubVO.getUFDouble_NullAsZero(tbbbvo
					.getGebb_num()).multiply(
							tbbbvo.getGebb_hsl()
					));
			// �����״̬(Ĭ�Ϻ���)
			tbWarehousestockVO1.setSs_pk(WdsWlPubConst.default_inv_state);
			// ����״̬
			tbWarehousestockVO1.setWhs_status(0);
			// ����
			tbWarehousestockVO1.setWhs_type(1);
			//

			tbWarehousestockVO1.setPk_bodysource(tbbbvo
					.getPwb_pk());

			// �����������

			tbWarehousestockVO1.setPk_invbasdoc(tbbbvo
					.getPk_invbasdoc());

			// ���κ�

			tbWarehousestockVO1.setWhs_batchcode(tbbbvo
					.getGebb_vbatchcode());

			// ��д���κ�
			if (null != tbbbvo.getGebb_lvbatchcode()
					&& !"".equals(tbbbvo.getGebb_lvbatchcode())) {
				tbWarehousestockVO1.setWhs_lbatchcode(tbbbvo
						.getGebb_lvbatchcode());
			} else {
				tbWarehousestockVO1.setWhs_lbatchcode("2009");
			}
			tbWarehousestockVO1.setPk_cargdoc(cargdocPK);
			// ��Դ���ݱ��������� ���������
			tbWarehousestockVO1.setPk_bodysource(tbbbvo
					.getGebb_pk());
			
			ldata.add(tbWarehousestockVO1);

		}

		if(ldata.size()>0){
			getBaseDao().insertVOArray(ldata.toArray(new StockInvOnHandVO[0]));
		}
	}
	

}
