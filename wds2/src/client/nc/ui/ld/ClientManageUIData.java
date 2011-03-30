package nc.ui.ld;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uap.bd.BDRuntimeException;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.trade.pub.IVOTreeDataByCode;
import nc.vo.ld.InvclVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

public class ClientManageUIData  implements IVOTreeDataByCode {
private SuperVO[] m_ChnlManagerVOs = null;
	public SuperVO[] getTreeVO() {		
			if (m_ChnlManagerVOs == null) {
				IUAPQueryBS queryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				ArrayList list = null;				
				try {
					StringBuffer sql = new StringBuffer() 
					.append("select vcl.pk_invcl, vcl.invclasscode, vcl.invclasslev,vcl.invclassname ")
					.append(" from bd_invcl vcl ")									
					;							
					list = (ArrayList)queryBS.executeQuery(sql.toString(), new BeanListProcessor(InvclVO.class));
				} catch (BusinessException e) {
					Logger.error(e.getMessage(), e);
					throw new BDRuntimeException(e.getMessage(), e);
				}
				
				if (list == null)
					return null;
				m_ChnlManagerVOs = (InvclVO[]) list.toArray(new InvclVO[0]);
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
