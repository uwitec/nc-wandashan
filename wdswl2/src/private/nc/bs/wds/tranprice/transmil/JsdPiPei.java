package nc.bs.wds.tranprice.transmil;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.tranprice.transmil.TransmilVO;

public class JsdPiPei {
	
	public BaseDAO baseDAO = null;	
	public TransmilVO[] voaImport = null;
	private Map<String ,String> areaid = new HashMap<String, String>();//<��������,����id>
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-5-30����05:21:53
	 * @param voaImport
	 *  EXECL�������ݣ�pk_delocation��ʱ��������� 
	 *   pk_relocation��ʱ��������� 
	 * @return
	 * @throws BusinessException
	 */
	public TransmilVO[] queryAllpipeiData(TransmilVO[] voaImport,String operatorid)throws BusinessException{
		if(voaImport == null){
			return null;
		}
		for(TransmilVO vo : voaImport){
			String pk_delocation = vo.getPk_delocation();//��������
			pk_delocation = pk_delocation.replace(" ", "");
			String pk_relocation = vo.getPk_relocation();//�ջ�����
			pk_relocation = pk_relocation.replace(" ", "");
			if(areaid.containsKey(pk_delocation)){
				vo.setPk_delocation(areaid.get(pk_delocation));
			}else {
				String sql="select  pk_areacl from bd_areacl where areaclname like '"+pk_delocation+"%'";
				String pk_areacl = PuPubVO.getString_TrimZeroLenAsNull(getBaseDAO().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
				if(pk_areacl != null){
					areaid.put(pk_delocation, pk_areacl);
					vo.setPk_delocation(pk_areacl);
				}
			}
			if(areaid.containsKey(pk_relocation)){
				vo.setPk_relocation(pk_relocation);
			}else{
				String sql="select  pk_areacl from bd_areacl where areaclname like '"+pk_relocation+"%'";
				String pk_areacl = PuPubVO.getString_TrimZeroLenAsNull(getBaseDAO().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
				if(pk_areacl != null){
					areaid.put(pk_relocation, pk_areacl);
					vo.setPk_relocation(pk_areacl);
				}
			}
			vo.setFirstretime(new UFDateTime(System.currentTimeMillis()).toString());
			vo.setPk_firstreperson(operatorid);
		}
		return voaImport;
	}
	
	public BaseDAO getBaseDAO() {
		if(baseDAO == null){
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
}
