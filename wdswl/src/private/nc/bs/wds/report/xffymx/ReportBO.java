package nc.bs.wds.report.xffymx;

import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.report.ReportBaseVO;

public class ReportBO {
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	public ReportBaseVO[] doQuery(String whereSql) throws BusinessException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append("   bd_cubasdoc.custcode custcode ,");//���̱���
        sql.append(" bd_cubasdoc.custname custname,");//��������
        sql.append(" bd_invbasdoc.invcode  invcode,");//�������
        sql.append(" bd_invbasdoc.invname  invname ,");//�������
        sql.append(" bd_invbasdoc.invspec invspec,");//���
        
		sql.append(" tb_outgeneral_b.vbatchcode  vbatchcode," );//���κ�
		sql.append(" tb_outgeneral_b.noutnum  noutnum," );//������
		sql.append(" tb_outgeneral_b.noutassistnum noutassistnum," );//������
		sql.append(" tb_outgeneral_b.cinvbasid  cinvbasid," );//���id
		sql.append(" tb_outgeneral_b.cinventoryid  cinventoryid," );//�������id
		sql.append(" wds_invbasdoc.vdef2 vdef2," );//����������
		sql.append(" wds_invbasdoc.vdef1 vdef1," );//�������ID   ���
		sql.append("  wds_invbasdoc.fuesed fuesed," );//�������
		sql.append(" wds_storecust_h.pk_stordoc pk_stordoc," );//�ֿ�
		sql.append("  wds_storecust_h.pk_sendareacl pk_sendareacl," );//������λ
		sql.append("  tb_storcubasdoc.pk_cubasdoc pk_cubasdoc," );//���̻�������
		sql.append("  tb_storcubasdoc. pk_cumandoc  pk_cumandoc ," );//���̹�����
		sql.append("  tb_storcubasdoc.  pk_defdoc pk_defdoc," );//��������
		sql.append("  wds_soorder.pk_transcorp pk_transcorp,");//--������
		sql.append("  wds_soorder.vcardno vcardno," );//����
		sql.append("   wds_soorder.vdriver vdriver" );//˾��
		sql.append("   from tb_outgeneral_h" );
		sql.append("   join tb_outgeneral_b on tb_outgeneral_h.general_pk=tb_outgeneral_b.general_pk " );
		sql.append("   join wds_invbasdoc on wds_invbasdoc.pk_invmandoc= tb_outgeneral_b.cinventoryid " );
		sql.append("   join  tb_storcubasdoc on tb_storcubasdoc.pk_cumandoc=tb_outgeneral_h.ccustomerid " );
		sql.append("   join   wds_storecust_h  on wds_storecust_h.pk_wds_storecust_h=tb_storcubasdoc.pk_wds_storecust_h" );
		sql.append("   join wds_soorder_b on wds_soorder_b.pk_invmandoc=tb_outgeneral_b.cinventoryid" );
		sql.append("   join wds_soorder on wds_soorder.pk_soorder=wds_soorder_b.pk_soorder" );
		sql.append("   join bd_cubasdoc on bd_cubasdoc.pk_cubasdoc=tb_storcubasdoc.pk_cubasdoc  ");
		sql.append("   join bd_invbasdoc on tb_outgeneral_b.cinvbasid=bd_invbasdoc.pk_invbasdoc  ");
		
		sql.append(" where ");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)!=null){
			sql.append(whereSql);
		}
		sql.append(" and isnull(tb_outgeneral_h.dr,0)=0  and " + " isnull(tb_outgeneral_b.dr,0)=0 ");
		sql.append(" and isnull(wds_invbasdoc.dr,0)=0  and " + " isnull(tb_storcubasdoc.dr,0)=0 ");
		sql.append(" and  isnull(wds_storecust_h.dr,0)=0  ");
		sql.append(" and  isnull(wds_soorder_b.dr,0)=0  ");
		sql.append(" and  isnull(wds_soorder.dr,0)=0  ");
	
		
//		sql.append(" group by  bd_cargdoc.csname,bd_stordoc.storname,bd_invbasdoc.invcode,bd_invbasdoc.invname,tb_stockstate.ss_state ");//�������
//		sql.append(" order by  bd_cargdoc.csname,bd_stordoc.storname ");  
		List ldata = (List)getDao().executeQuery(sql.toString(), WdsPubResulSetProcesser.MAPLISTROCESSOR);
		if(ldata == null || ldata.size() == 0){
			return null;
		}
		
		Map dataMap = null;
		int len = ldata.size();
		ReportBaseVO[] vos = new ReportBaseVO[len];
		ReportBaseVO vo = null;
		for(int i=0;i<len;i++){
			vo = new ReportBaseVO();
			dataMap = (Map)ldata.get(i);
			for(String key:keys){
				vo.setAttributeValue(key, dataMap.get(key));
			}
			vos[i] = vo;
		}
		return vos;
		
				
	}
	private String[] keys = new String[]{"vbatchcode","noutnum","noutassistnum","cinvbasid","custcode","custname","invcode","invname","invspec",
			"cinventoryid","vdef2","vdef1","fuesed","pk_stordoc","pk_sendareacl","pk_cubasdoc","pk_cumandoc","pk_transcorp",
			"pk_defdoc","vcardno","vdriver"};

}
