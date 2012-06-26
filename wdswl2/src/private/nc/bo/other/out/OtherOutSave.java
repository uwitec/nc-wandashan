package nc.bo.other.out;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nc.bs.pub.SuperDMO;
import nc.bs.wds.tray.lock.LockTrayBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.SQLParameter;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.trade.voutils.IFilter;
import nc.vo.wds.ic.cargtray.SmallTrayVO;

/**
 * 
 * @author Administrator
 * ���������̨��
 */
public class OtherOutSave  extends nc.bs.trade.comsave.BillSave {

	private SuperDMO dmo = new SuperDMO();
	private SuperDMO getSuperDMO(){
		if(dmo == null){
			dmo = new SuperDMO();
		}
		return dmo;
	}
	private OtherOutBO dao = null;
	private OtherOutBO getOutBO(){
		if(dao == null)
			dao = new OtherOutBO();
		return dao;
	}
	/**
	 * ����VO���档 ���� ��ɾ��������Ϣ  �ٱ����µ�������Ϣ
	 *    1����vo����  2������Ϣ���� 3��Դ���ݻ�д 4��������ȷ�ϵ�
	 * �������ڣ�(2004-2-27 11:15:29)
	 * @return ArrayList
	 * @param vo nc.vo.pub.AggregatedValueObject

	 */
	public java.util.ArrayList saveBill(nc.vo.pub.AggregatedValueObject billVo)
	throws BusinessException {

		if(billVo==null)
			throw new BusinessException("��������Ϊ��");
		MyBillVO old_billVo = null;
		try {
			old_billVo = (MyBillVO)ObjectUtils.serializableClone(billVo);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new BusinessException(e1);
		}

		//		zhf add
		Map<String, SmallTrayVO[]> trayInfor = (Map<String,SmallTrayVO[]>)((MyBillVO)billVo).getOUserObj();

		//		У��  ������ ��ʵ�����̵��������� ����  ָ��  �������Ϣ
		LockTrayBO lockbo = new LockTrayBO();
		lockbo.checkIsLock((MyBillVO)billVo);
		//		zhf end

		boolean isAdd = false;
		boolean bodyChanged = false;//�����Ƿ��޸�
		TbOutgeneralHVO head = (TbOutgeneralHVO)billVo.getParentVO();
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey())==null)
			isAdd = true;


		TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[])billVo.getChildrenVO();
		//���˵�ɾ����  zhf add
		bodys = (TbOutgeneralBVO[])nc.vo.trade.voutils.VOUtil.filter(bodys, new filterDelLine());

		if(bodys!=null && bodys.length > 0){
			bodyChanged = true;
			for(TbOutgeneralBVO body:bodys){
				body.validationOnSave();
			}
		}

		if(!isAdd && old_billVo.getChildrenVO().length>0){//�޸ı�����ɾ��  �Ѵ��ڵ�������ϸ�ӱ���Ϣ  �� �ظ����̴�����Ϣ
			TbOutgeneralBVO[] bb = (TbOutgeneralBVO[])old_billVo.getChildrenVO();
			for(TbOutgeneralBVO b : bb){
				String wheresql = " general_b_pk = '"+b.getPrimaryKey()+"' and isnull(dr,0)=0";
				List<TbOutgeneralTVO> ltray = (List<TbOutgeneralTVO> )getOutBO().getBaseDAO().retrieveByClause(TbOutgeneralTVO.class, wheresql);
				if(ltray!=null && ltray.size()>0)
					getOutBO().deleteOtherInforOnDelBill(head.getSrl_pk(),ltray);
			}
		}

		//�����  ��д������Դ
		getOutBO().writeBack(old_billVo,IBDACTION.SAVE,isAdd);
		//---------------------------����ǰУ�����----------------------------------------	
		java.util.ArrayList retAry = super.saveBill(old_billVo);

		if(retAry == null || retAry.size() == 0){
			throw new BusinessException("����ʧ��");
		}
		MyBillVO newBillVo = (MyBillVO)retAry.get(1);
		if(newBillVo == null){
			throw new BusinessException("����ʧ��");
		}	
		if(bodyChanged){
			//����������Ϣ��ˮ��
			insertTrayInfor(newBillVo, old_billVo);
			//���¿�����״̬��
			try {
				TbOutgeneralBVO[] newbodys = (TbOutgeneralBVO[])newBillVo.getChildrenVO();
				List<TbOutgeneralTVO> ltray = new ArrayList<TbOutgeneralTVO>();
				for(TbOutgeneralBVO newbody:newbodys){
					ltray.addAll(newbody.getTrayInfor());
				}
				if(ltray.size()>0)
					getOutBO().updateStockOnSaveBill(head.getPk_corp(),head.getSrl_pk(),ltray);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if(e instanceof BusinessException){
					throw (BusinessException)e;
				}
				throw new BusinessException(e);
			}		
		}

		if(trayInfor != null && trayInfor.size()>0){
			//			����  ʵ������
			lockbo.doDelLockTrayInfor(PuPubVO.getString_TrimZeroLenAsNull(retAry.get(0)), head.getSrl_pk(), trayInfor);
		}

		return retAry;
	}

	class filterDelLine implements IFilter{
		public boolean accept(Object o) {
			// TODO Auto-generated method stub
			if(o == null)
				return false;
			SuperVO vo = (SuperVO)o;
			if(vo.getStatus() == VOStatus.DELETED)
				return false;
			return true;
		}

	}

	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �����浥��������ϸ��ˮ�ӱ�
	 * @ʱ�䣺2011-4-7����02:28:26
	 * @param newBillVo  ������ ��浥��vo
	 * @param oldBillVo  ����ǰ��浥��vo
	 * @throws BusinessException
	 */
	private void insertTrayInfor(MyBillVO newBillVo,MyBillVO oldBillVo) throws BusinessException{
		if(newBillVo == null || oldBillVo == null){
			return;
		}
		TbOutgeneralBVO[] newbodys = (TbOutgeneralBVO[])newBillVo.getChildrenVO();
		TbOutgeneralBVO[] oldbodys = (TbOutgeneralBVO[])oldBillVo.getChildrenVO();	
		checkTrayUsed(oldbodys);
		//		String[] tmps = null;
		for(TbOutgeneralBVO old : oldbodys){
			String oldno = old.getCrowno();
			for(TbOutgeneralBVO newbody:newbodys){
				String newno = newbody.getCrowno();
				if(oldno.equals(newno)){
					List<TbOutgeneralTVO> ltraytmp = old.getTrayInfor();
					if(ltraytmp == null || ltraytmp.size() == 0){
						throw new BusinessException("��������Ϣ������������Ϣʧ�ܣ�");
					}
					for(TbOutgeneralTVO tray:ltraytmp){
						tray.setGeneral_b_pk(newbody.getPrimaryKey());
						tray.setGeneral_pk(newbody.getGeneral_pk());
					}
					getSuperDMO().insertList(ltraytmp);
					TbOutgeneralTVO[] bvo = (TbOutgeneralTVO[])getSuperDMO().queryByWhereClause(TbOutgeneralTVO.class, " general_b_pk = '"+newbody.getPrimaryKey()+"' and isnull(dr,0) = 0 ");
					newbody.setTrayInfor(arrayTolist(bvo));
					break;
				}
			}
		}
	}

	public List<TbOutgeneralTVO> arrayTolist(TbOutgeneralTVO[] bvo){
		List<TbOutgeneralTVO> list  = null;
		if(bvo!=null && bvo.length>0){
			list = new ArrayList<TbOutgeneralTVO>();
			for(TbOutgeneralTVO b : bvo){
				list.add(b);
			}
		}
		return list;
	}
	/**
	 * 
	 * @throws BusinessException
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-4-13����09:02:49
	 */
	private void checkTrayUsed(TbOutgeneralBVO[] bodys) throws BusinessException{
		String sql ="select cdt_traycode from bd_cargdoc_tray  " +
		" join tb_warehousestock on tb_warehousestock.pplpt_pk = bd_cargdoc_tray.cdt_pk " +
		" where bd_cargdoc_tray.cdt_pk=? and tb_warehousestock.whs_pk=? " +
		" and (bd_cargdoc_tray.cdt_traystatus="+StockInvOnHandVO.stock_state_null+" or  " +
		" coalesce(tb_warehousestock.whs_stocktonnage,0)<? " +
		" and tb_warehousestock.whs_stocktonnage>0)";
		SQLParameter para = new SQLParameter();
		for(TbOutgeneralBVO bvo:bodys){
			List<TbOutgeneralTVO> tray = bvo.getTrayInfor();
			for(int i=0;i<tray.size();i++){
				String cdp_pk =tray.get(i).getCdt_pk();
				String whs_pk=tray.get(i).getWhs_pk();
				UFDouble  noutnum = PuPubVO.getUFDouble_NullAsZero(tray.get(i).getNoutnum());
				para.addParam(cdp_pk);
				para.addParam(whs_pk);
				para.addParam(noutnum);
				Object o =getOutBO().getBaseDAO().executeQuery(sql, para, WdsPubResulSetProcesser.COLUMNPROCESSOR);
				if(o!=null){
					throw new BusinessException("��"+(i+1)+"�У�ָ��������:"+o.toString()+"\n�ѱ�����ʹ�ã�����������������ָ��");
				}
				para.clearParams();
			}
		}

	}
}