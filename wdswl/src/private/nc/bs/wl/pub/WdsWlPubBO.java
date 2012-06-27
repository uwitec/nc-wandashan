package nc.bs.wl.pub;

import java.util.HashMap;
import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.LoginInforVO;

public class WdsWlPubBO {
	
	private  BaseDAO m_dao = null;
	
	private  BaseDAO getDao(){
		if(m_dao == null){
			m_dao = new BaseDAO();
		}
		return m_dao;
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ݵ�ǰ��½�û� ��ȡ�û��� ����Ϣ �����ֿ�  ������λ���ִ���Ա�� �ȵ�
	 * tb_stockstaff �ֿ���Ա��
	 * @ʱ�䣺2011-3-23����05:02:45
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  LoginInforVO getLogInfor(String userid)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select cuserid loguser,"); //��Աid
		sql.append(" pk_stordoc whid,");// �ֿ�id
		sql.append(" pk_cargdoc spaceid ");//��λ��Ϣ
		sql.append(" from tb_stockstaff");
		sql.append(" where isnull(dr,0)=0 ");
		sql.append(" and pk_corp='"+SQLHelper.getCorpPk()+"'");
		sql.append(" and  cuserid='" + userid +"'");
	    LoginInforVO lif=(LoginInforVO)getDao().executeQuery(sql.toString(),new BeanProcessor(LoginInforVO.class));
		return lif;
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ  ��ȡָ���ֿ�Ļ�λ��Ϣ
	 * @ʱ�䣺2011-3-23����05:12:03
	 * @param cwhid
	 * @return
	 * @throws Exception
	 */
	public  String[] getSpaceByWhid(String cwhid) throws Exception{
		String sql = "select pk_cargdoc from bd_cargdoc where pk_stordoc = '"+cwhid+"'";
		Object o = getDao().executeQuery(sql, WdsPubResulSetProcesser.ARRAYROCESSOR);
		return (String[])o;
	}
	
    /**
     * 
     * @���ߣ�zhf
     * @˵�������ɽ������Ŀ ����ָ���ֿ��ȡ������Ϣ
     * @ʱ�䣺2011-3-23����05:12:32
     * @param cwhid
     * @return
     * @throws Exception
     */
	public  String[] getCustomManIDByWhid(String cwhid) throws Exception{
		//�ֿ�Ϳ�����һ�Զ�Ĺ�ϵ
	
		String sql="select pk_cubsdoc  from   bd_stordoc join  tb_storcubasdoc on bd_stordoc.pk_stordoc=tb_storcubasdoc.pk_stordoc and  bd_stordoc.pk_stordoc='"+cwhid+"';  ";
		Object o=getDao().executeQuery(sql, WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
		return (String[])o;
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ݵ�ǰ��½��Ա����Ǳ���Ա���ظ����λ������Ǳ���Ա���������ֿ����еĻ�λ��Ϣ
	 * @ʱ�䣺2011-3-23����05:12:57
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  String[] getSpaceByLogUser(String userid) throws BusinessException{
		
	    String sql="select  st_type  from   tb_stockstaff   where cuserid='"+userid+"' ";
	    Object o=getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR);
	    if(o !=null){
	    	Integer i=(Integer)o;
	    	if(i.intValue()==0){
	    		String sql1="select pk_cargdoc from tb_stockstaff where cuserid='"+userid+"' and isnull(dr,0)=0 and pk_corp='"+SQLHelper.getCorpPk()+"'";
	    		String o1=PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql1, WdsPubResulSetProcesser.COLUMNPROCESSOR));
	    		if(o1 == null)
	    			throw new BusinessException("�ñ���Աδָ����λ");
	    		return new String[]{o1};
	    	}else{
	    		String sql2="select bd.pk_cargdoc  from  tb_stockstaff tb join bd_cargdoc bd  on tb.pk_stordoc=bd.pk_stordoc and tb.cuserid='"+userid+"'";
	    		Object o2=getDao().executeQuery(sql2, WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
	    		
	    		return (String[])o2;
	    		
	    	}
	    	
	    }
	    
		return null;
	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-4-23����04:23:59
	 * @param ��� ��λ�µĴ������id
	 * @return
	 * @throws BusinessException
	 */
	public String[] getInvBasdocIDsBySpaceID(String cspaceid) throws BusinessException{
		String sql = "select pk_invmandoc from tb_spacegoods where isnull(dr,0)=0 and pk_cargdoc = '"+cspaceid+"'";
		List ldata = (List)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
		if(ldata == null||ldata.size()==0)
			return null;
		return (String[])ldata.toArray(new String[0]);
	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-4-23����04:23:59
	 * @param ��� ��λ�µĴ������id
	 * @return
	 * @throws BusinessException
	 */
	public String[] getInvbasdocIDsBySpaceID(String cspaceid) throws BusinessException{
		String sql = "select pk_invbasdoc from tb_spacegoods where isnull(dr,0)=0 and pk_cargdoc = '"+cspaceid+"'";
		List ldata = (List)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
		if(ldata == null||ldata.size()==0)
			return null;
		return (String[])ldata.toArray(new String[0]);
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ݵ�ǰ��½��Ա��ȡ
	 * @ʱ�䣺2011-3-23����05:13:04
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  String[] getCustomManIDByLogUser(String userid) throws Exception{
		return null;
	}

	private  java.util.Map<String, Integer> trayVolumnInfor = null;
	
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ��ȡָ����λ���ָ�������ȫ������
	 * @ʱ�䣺2011-3-31����04:34:27
	 * @param cspaceid
	 * @param cinvbasid
	 * @return
	 * @throws BusinessException
	 */
	public  String[] getTrayInfor(String cspaceid,String cinvbasid,String notIn,String oldCdt) throws BusinessException{
		String key = cspaceid+cinvbasid;
		String[] trays = null;
		List ldata = null;
		//<��λ�ʹ��id,�������������̵�����>
		java.util.Map<String,String[]> trayInfor = new HashMap<String, String[]>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select cdt_pk from bd_cargdoc_tray ");//���̵���
		sql.append(" where pk_cargdoc='"+cspaceid+"'");
		sql.append(" and cdt_invbasdoc='"+cinvbasid+"'");
		sql.append(" and isnull(dr,0)=0  ");
		sql.append(" and ((cdt_traystatus=0 "+notIn+")"+oldCdt+") order by  cdt_traycode," +
				" substr(cdt_traycode,length(cdt_traycode),1),substr(cdt_traycode,length(cdt_traycode)-1,1) desc");//���ﵽ�⣬���ϵ���ѡ������
		ldata = (List)getDao().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNLISTROCESSOR);	
		if(ldata == null || ldata.size() == 0)
			return null;
		trays = ((List<String>)ldata).toArray(new String[0]);
		trayInfor.put(key, trays);
		return trayInfor.get(key);
	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵������ȡ�ֲ�����id
	 * @ʱ�䣺2011-3-31����04:34:27
	 * @param cspaceid
	 * @param cinvbasid
	 * @return
	 * @throws BusinessException
	 */
	public  String getFcTrayInfor(String cspaceid) throws BusinessException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select cdt_pk from bd_cargdoc_tray ");//���̵���
		sql.append(" where pk_cargdoc='"+cspaceid+"'");
		sql.append(" and isnull(dr,0)=0  ");		
		return PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR));
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ��ȡ�����������
	 * @ʱ�䣺2011-3-31����04:59:46
	 * @param cinvbasid
	 * @return
	 * @throws BusinessException
	 */
	public  Integer getTrayVolumeByInvbasid(String cinvbasid) throws BusinessException{
		int volumn = 0;
		if(trayVolumnInfor == null||!trayVolumnInfor.containsKey(cinvbasid)){
			String sql = "select tray_volume from wds_invbasdoc where pk_invbasdoc='"+cinvbasid+"'";
			volumn = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR), 1);
			if(volumn == -1)
				throw new BusinessException("�ô����������δ����");
			if(trayVolumnInfor == null)
				trayVolumnInfor = new HashMap<String, Integer>();
			trayVolumnInfor.put(cinvbasid, volumn);
		}
		return trayVolumnInfor.get(cinvbasid);
	}

}
