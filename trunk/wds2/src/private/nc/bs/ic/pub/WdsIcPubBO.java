package nc.bs.ic.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.bs.wl.pub.WdsWlPubBO;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wl.pub.WdsWlPubTool;

public class WdsIcPubBO {
	private BaseDAO m_dao = null;

	private BaseDAO getDao() {
		if (m_dao == null) {
			m_dao = new BaseDAO();
		}
		return m_dao;
	}

//	/**
//	 * 
//	 * @���ߣ�zhf
//	 * @˵�������ɽ������Ŀ У���λ�ռ��Ƿ���
//	 * @ʱ�䣺2011-3-31����04:17:10
//	 * @param bodys
//	 * @param iszc
//	 * @throws ValidationException
//	 */
//	private void checkSpaceIsEnable(TbGeneralBVO[] bodys, String cwhid,
//			String cspaceid) throws Exception {}
	private void doSaveTrayInfor(List<TbGeneralBBVO>lallbbvos) throws BusinessException{
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
		UFDouble geb_bsnumd = PuPubVO.getUFDouble_NullAsZero(body.getGeb_bsnum());
//				WdsWlPubTool.INTEGER_ZERO_VALUE);
		int k = 0;
		ArrayList<TbGeneralBBVO> lbbvos = new ArrayList<TbGeneralBBVO>();
		TbGeneralBBVO tbgbbvo = null;
		if(iszc){
			while (geb_bsnumd.doubleValue() > iVolumn) {
				tbgbbvo = new TbGeneralBBVO();
				// ����
				tbgbbvo.setGebb_vbatchcode(body.getGeb_vbatchcode());
				// ��д����
				tbgbbvo.setGebb_lvbatchcode(body.getGeb_backvbatchcode());
				// �к�
				tbgbbvo.setGebb_rowno(k + 1 + "0");
				// ���ⵥ��������
				tbgbbvo.setPwb_pk(body.getGeb_cgeneralbid());//------------------
				// ������
				tbgbbvo.setGebb_hsl(body.getGeb_hsl());
				// �˻���������
				tbgbbvo.setPk_invbasdoc(body.getGeb_cinvbasid());
				// ��ⵥ�ӱ�����
				tbgbbvo.setGeb_pk(body.getGeb_pk());//-----------------
				// ����
				tbgbbvo.setGebb_nprice(body.getGeb_nprice());
				// ���
				tbgbbvo.setGebb_nmny(body.getGeb_nmny());
				// ����ʵ�ʴ������
				tbgbbvo.setGebb_num(PuPubVO.getUFDouble_NullAsZero(iVolumn));
				geb_bsnumd = geb_bsnumd.sub(iVolumn);
				// ��������
				tbgbbvo.setCdt_pk(tmpTrays[k]);
				tbgbbvo.setDr(0);
				tbgbbvo.setGeb_pk(body.getGeb_pk());
				lbbvos.add(tbgbbvo);
				k++;
			}
		}

		TbGeneralBBVO tbgbbvo1 = new TbGeneralBBVO();
		// ����
		tbgbbvo1.setGebb_vbatchcode(body.getGeb_vbatchcode());
		// ��д����
		tbgbbvo1.setGebb_lvbatchcode(body.getGeb_backvbatchcode());
		// �к�
		tbgbbvo1.setGebb_rowno(k + 1 + "0");
		// ���ⵥ��������
		tbgbbvo1.setPwb_pk(body.getGeb_cgeneralbid());
		// ������
		tbgbbvo1.setGebb_hsl(body.getGeb_hsl());
		// �˻���������
		tbgbbvo1.setPk_invbasdoc(body.getGeb_cinvbasid());
		// ��ⵥ�ӱ�����
		tbgbbvo1.setGeb_pk(body.getGeb_pk());
		// ����
		tbgbbvo1.setGebb_nprice(body.getGeb_nprice());
		// ���
		tbgbbvo1.setGebb_nmny(body.getGeb_nmny());
		// ����ʵ�ʴ������
		tbgbbvo1.setGebb_num(PuPubVO.getUFDouble_NullAsZero(geb_bsnumd));
		// ��������
		tbgbbvo1.setCdt_pk(tmpTrays[k]);
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
	 * @param bodys
	 * @throws BusinessException
	 */
	public Object autoInTray(TbGeneralBVO[] bodys, String cwhid, String cspaceid)
			throws BusinessException {
		if (bodys == null || bodys.length == 0)
			throw new BusinessException("�����������Ϊ��");

		Map<String, Integer> retInfor = new HashMap<String, Integer>();
		boolean iszc = WdsWlPubTool.isZc(cwhid);
		// WdsWlPubBO wlbo = new WdsWlPubBO();
		if (!iszc) {// �ֲ� ����״̬=0 �����Ƿ� һ��װ�� �� һ�����
			String sql = "select count(0) from bd_cargdoc_tray where cdt_traystatus=0 and isnull(dr,0)=0 and pk_cargdoc = '"
					+ cspaceid + "'";
			int index = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql,
					WdsPubResulSetProcesser.COLUMNPROCESSOR), -1);
			if (index <= 0)
				throw new ValidationException("�ֲ�û��������������");
//			return null;
		}

		String[] tmpTrays = null;//�ܷ�ָ������ı���λ�ڵ����е�����
		int iVolumn = 0;//һ�����̿��÷Ŷ��ٻ�Ʒ
		int iAllVolumn = 0;//����ʣ������ɻ�Ʒ����
		
		List<TbGeneralBBVO> lallbbvos = new ArrayList<TbGeneralBBVO>();
		// �ܲ��ж�
		for (TbGeneralBVO body : bodys) {
			// ��ȡ����������������Ϣ
			tmpTrays = WdsWlPubBO.getTrayInfor(cspaceid, body
					.getGeb_cinvbasid());
			if (tmpTrays == null || tmpTrays.length == 0) {
				retInfor.put(body.getGeb_cinvbasid(), -1);// û�����õ�������
				continue;
			}
			// ���������ݻ�
			iVolumn = WdsWlPubBO.getTrayVolumeByInvbasid(body
					.getGeb_cinvbasid());
			iAllVolumn = iVolumn * tmpTrays.length;// ���ݻ�
			// �ж� ���ݻ�����С�� ��ǰ��Ʒ��Ӧ�ո�����
			UFDouble nRes = body.getGeb_bsnum().sub(
					PuPubVO.getUFDouble_NullAsZero(iAllVolumn));
			if (nRes.doubleValue() > 0) {
				retInfor.put(body.getGeb_cinvbasid(), 1);// Ҫ�ŵĻ��ﳬ�����̳�����
				continue;
			} else if (nRes.equals(WdsWlPubTool.DOUBLE_ZERO)) {// �����������㱾������

				lallbbvos.addAll(shareNumToTrays(tmpTrays, body, iVolumn, iszc));
			} else {// ���̴´����� ���ʹ������ ����ʹ���Ƿ������ȼ� �����̱��룿
				lallbbvos.addAll(shareNumToTrays(tmpTrays, body, iVolumn, iszc));
			}
		}
		
		if(lallbbvos == null || lallbbvos.size()==0)
			throw new BusinessException("���ݴ����쳣");
		
		//����������Ϣ(��ɾ���)
		doSaveTrayInfor(lallbbvos);	
		return retInfor;	
	}
}
