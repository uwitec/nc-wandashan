package nc.ui.ld;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uap.bd.BDRuntimeException;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanMappingListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.pub.IVOTreeDataByCode;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.ld.InvclVO;
import nc.vo.ld.TestVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

public class ClientUIData implements IVOTreeDataByCode{
     
	private SuperVO[] m_ChnlManagerVOs = null;
	

	public SuperVO[] getTreeVO() {
		
			if (m_ChnlManagerVOs == null) {
				IUAPQueryBS queryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				ArrayList list = null;
				
				try {
					StringBuffer sql = new StringBuffer() 
					.append("select vcl.pk_invcl, vcl.invclasscode, vcl.invclasslev,vcl.invclassname, ")
					.append(" ice.loadprice, cc.pk_invbasdoc,cc.invname,cc.pk_corp,ice.unloadprice, ")
					.append(" ice.caimaprice, ice.iscaima, ice.joinprice ")
					.append(" from bd_invcl vcl")
					.append(" left join (select bas.pk_invbasdoc, bas.invname, voc.pk_corp, bas.pk_invcl ")
					.append("  from bd_invbasdoc bas")
					.append("  join")
					.append("  bd_invmandoc voc on bas.pk_invbasdoc = voc.pk_invbasdoc ")
					.append("  where voc.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPk_corp()+"' ) cc")
					.append(" on vcl.pk_invcl = cc.pk_invcl")
					.append(" left join ld_inprice ice on cc.pk_invbasdoc = ice.pk_invbasdoc")
					
										
					;
					
							
					list = (ArrayList)queryBS.executeQuery(sql.toString(), new BeanListProcessor(TestVO.class));
					
				// 对存货信息，设置节点树编码
					if(list !=null){
						for(int i=0;i<list.size();i++){
						  TestVO t=(TestVO)list.get(i);
							if(t.getPk_invbasdoc()!=null || t.getPk_invbasdoc()==""){
								t.setInvclassname(t.getInvname());
								t.setInvclasscode(t.getInvclasscode().concat(i+""));
								
							}
							
							
						}
					}

				} catch (BusinessException e) {
					Logger.error(e.getMessage(), e);
					throw new BDRuntimeException(e.getMessage(), e);
				}
				
				if (list == null)
					return null;
				m_ChnlManagerVOs = (TestVO[]) list.toArray(new TestVO[0]);
			}

		return m_ChnlManagerVOs;
	}


	public String getCodeFieldName() {
		// TODO Auto-generated method stub
		return "invclasscode";
	}


	public String getCodeRule() {
		// TODO Auto-generated method stub
		return "1.2.2";
	}


	public String getShowFieldName() {
		// TODO Auto-generated method stub
		return "invclassname";
	}

}
