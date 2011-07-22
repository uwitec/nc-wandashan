package nc.bs.wl.plan;

import java.util.ArrayList;
import java.util.List;

import sun.net.www.http.Hurryable;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.vo.dm.SendplaninBVO;
import nc.vo.dm.SendplaninVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.VOTool;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * @���ߣ�lyf
 * @˵�������ɽ������Ŀ 
 * ���˼ƻ�¼�루WDS1����̨��
 */
public class PlanCheckinBO {
	//�ƻ�������
	private String planNum="nplannum";
	//�ƻ�������
	private String planBnum="nassplannum";	
	//���ֿ�
	private String pk_inwhouse="pk_inwhouse";
	//����ֿ�
	private String pk_outwhouse="pk_outwhouse";
	BaseDAO dao = null;	
	private BaseDAO getBaseDAO(){
		if(dao==null){
			dao = new BaseDAO();
		}
		return dao;
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * 	����ǰУ��
	 * ��ǰ��¼�˵Ĺ���Ĳֿ��ڵ�ǰ������Ƿ��Ѿ����¼ƻ�
	 * @ʱ�䣺2011-3-23����09:14:56
	 * @param pk_inwhouse =����ֿ�p������pk=��������
	 */
	public void beforeCheck(String  pk_inwhouse,String pk,String date) throws BusinessException{
		AccountCalendar calendar = AccountCalendar.getInstance();
		calendar.setDate(new UFDate(date));//spf add
		UFDate beginDate = calendar.getMonthVO().getBegindate();
		UFDate endDate = calendar.getMonthVO().getEnddate();
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) from wds_sendplanin ");
		sql.append(" where wds_sendplanin.iplantype=0 and dmakedate between '");
		sql.append(beginDate+"' and '" + endDate);
		sql.append("' and pk_inwhouse ='"+pk_inwhouse+"' ");
		sql.append(" and isnull(dr,0)=0");

		
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
		if(pk == null || "".equalsIgnoreCase(pk)){
			if( i>0){
				throw new BusinessException("�õ���ֿ⣬��ǰ������Ѿ����¼ƻ�,ֻ������׷�Ӽƻ�");
			}
		}else{
			if( i>1){
				throw new BusinessException("�õ���ֿ⣬��ǰ������Ѿ����¼ƻ�,ֻ������׷�Ӽƻ�");
			}
		}
		
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 *   ����ǰУ�� �Ƿ��Ѿ������������η��˶���
	 * @ʱ�䣺2011-3-27����09:44:46
	 * @param Ҫ����� ���˼ƻ�����
	 * @throws BusinessException 
	 */
	public void beforeUnApprove(AggregatedValueObject obj) throws BusinessException{
		if(obj ==null){
			return;
		}
		SendplaninVO parent =(SendplaninVO) obj.getParentVO();
		String pk_sendplanin = parent.getPk_sendplanin();
		StringBuffer sql = new StringBuffer();	
		sql.append(" select count(0) ");
		sql.append(" from wds_sendorder ");
		sql.append(" join wds_sendorder_b ");
		sql.append(" on wds_sendorder.pk_sendorder= wds_sendorder_b.pk_sendorder");
		sql.append(" where isnull(wds_sendorder.dr,0)=0 and isnull(wds_sendorder_b.dr,0)=0 ");
		sql.append(" and wds_sendorder_b.csourcebillhid ='"+pk_sendplanin+"'");
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
		if( i>0){
			throw new BusinessException("�������η��˶���������ɾ�����˶��������˲���");
		}
		
	}
	//׷�Ӽƻ�У��
	public void beforeCheck1(String  pk_inwhouse,String pk) throws BusinessException{
		AccountCalendar calendar = AccountCalendar.getInstance();
		UFDate beginDate = calendar.getMonthVO().getBegindate();
		UFDate endDate = calendar.getMonthVO().getEnddate();
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) from wds_sendplanin ");
		sql.append(" where dmakedate between '");
		sql.append(beginDate+"' and '" + endDate);
		sql.append("' and pk_inwhouse ='"+pk_inwhouse+"'");
		sql.append(" and wds_sendplanin.iplantype=0 ");
		sql.append(" and isnull(dr,0)=0");
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);		
			if( i<=0){
				throw new BusinessException("�õ���ֿ⣬��ǰ�����û���¼ƻ�");
			}			
	}
	//��׷�Ӽƻ��ϲ����¼ƻ�
	public void planStats(AggregatedValueObject obj2) throws BusinessException{
		//�������Ķ����¡һ��
		AggregatedValueObject obj=VOTool.aggregateVOClone(obj2);
	   
		SendplaninVO parent =(SendplaninVO) obj.getParentVO();		
		String pk_inwhouse=parent.getPk_inwhouse();	
		SendplaninBVO[] childs=(SendplaninBVO[]) obj.getChildrenVO();
		AccountCalendar calendar = AccountCalendar.getInstance();
		UFDate beginDate = calendar.getMonthVO().getBegindate();
		UFDate endDate = calendar.getMonthVO().getEnddate();		
		StringBuffer sql=new StringBuffer();
		sql.append(" select pk_sendplanin  from");
		sql.append(" wds_sendplanin where wds_sendplanin.vbillstatus=1");
		sql.append(" and isnull(wds_sendplanin.dr,0)=0");
		sql.append(" and dmakedate between '");
		sql.append(beginDate+"' and '" + endDate);
		sql.append("' and pk_inwhouse ='"+pk_inwhouse+"'");

		List<SendplaninBVO> adds=new ArrayList<SendplaninBVO>();
		List<SendplaninBVO> mods=new ArrayList<SendplaninBVO>();
		
	    Object o=getBaseDAO().executeQuery(sql.toString(), new ColumnProcessor());
	    if( o!=null && o instanceof String){      
	        String cond=" pk_sendplanin='"+o+"' and isnull(dr,0)=0";
	    	List<SendplaninBVO> list=(List<SendplaninBVO>) getBaseDAO().retrieveByClause(SendplaninBVO.class, cond);    	
	    	boolean isExist=false;
		    for(int i=0;i<childs.length;i++){
		       if(!isExist&& i>0){		        
		    	 childs[i-1].setPk_sendplanin((String)o);  
		    	 childs[i-1].setPk_sendplanin_b(null);
		         adds.add(childs[i-1]);	       
		       }
		       isExist=false;
		       for(int j=0;j<list.size();j++){
		    	  if(childs[i].getPk_invmandoc().equalsIgnoreCase(list.get(j).getPk_invmandoc())){	    		  
		    		  list.get(j).setNplannum(PuPubVO.getUFDouble_NullAsZero(list.get(j).getNplannum()).add(PuPubVO.getUFDouble_NullAsZero(childs[i].getNplannum())));
		    		  list.get(j).setNassplannum(PuPubVO.getUFDouble_NullAsZero(list.get(j).getNassplannum()).add(PuPubVO.getUFDouble_NullAsZero(childs[i].getNassplannum())));		    		  
		    		  mods.add(list.get(j)); 
		    		  isExist=true;
		    		  break;
		        }
		    	  
		      } 
		    }
	    }
	  for(int i=0;i<adds.size();i++){
	    getBaseDAO().insertVOWithPK(adds.get(i));
	  }	  
		  getBaseDAO().updateVOList(mods); 
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *      ��鷢�˼ƻ��ļƻ�����������Ϊ��
	 * @ʱ�䣺2011-7-8����07:18:43
	 * @param vo
	 * @throws BusinessException
	 */
	public void checkNotAllNull(AggregatedValueObject vo)throws BusinessException{
	    if(vo.getChildrenVO()==null || vo.getChildrenVO().length==0){
	        return;	
	    }
		SuperVO[] vos=(SuperVO[]) vo.getChildrenVO();
		UFDouble  znum=new UFDouble("0.0");
		UFDouble  bznum=new UFDouble("0.0");
		int size=vos.length;
		for(int i=0;i<size;i++){
			UFDouble num=PuPubVO.getUFDouble_NullAsZero(vos[i].getAttributeValue(planNum));
			UFDouble bnum=PuPubVO.getUFDouble_NullAsZero(vos[i].getAttributeValue(planBnum));
			znum=znum.add(num);
			bznum=bznum.add(bnum);
		}
		if(znum.doubleValue()<=0 || bznum.doubleValue()<=0){
			throw new BusinessException("���˼ƻ�[�¼ƻ�]�ļƻ�����������Ϊ��");
		}		
	}	
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *      ��鷢�˼ƻ�׷�Ӽƻ� ����ƻ�������Ϊ�� ��������
	 *            ���û���¼ƻ� ׷�Ӽƻ���������              
	 * @ʱ�䣺2011-7-8����07:18:43
	 * @param vo
	 * @throws BusinessException
	 */
	public void checkForBplan(AggregatedValueObject vo)throws BusinessException{
		//����׷�Ӽƻ�ʱ  �ƻ�����������Ϊ��
		checkNotAllNull(vo);
		//У���Ƿ�����¼ƻ�,����������¼ƻ���������
		SuperVO hvo=(SuperVO) vo.getParentVO();
		AccountCalendar calendar = AccountCalendar.getInstance();
		UFDate beginDate = calendar.getMonthVO().getBegindate();
		UFDate endDate = calendar.getMonthVO().getEnddate();
		//���ֿ�����
		String pk_in=PuPubVO.getString_TrimZeroLenAsNull( hvo.getAttributeValue(pk_inwhouse));
		//����ֿ�����
		String pk_out=PuPubVO.getString_TrimZeroLenAsNull( hvo.getAttributeValue(pk_outwhouse));
		StringBuffer sql=new StringBuffer();
		sql.append(" select pk_sendplanin  from");
		sql.append(" wds_sendplanin where ");
		sql.append(" isnull(wds_sendplanin.dr,0)=0");
		sql.append(" and dmakedate between '");
		sql.append(beginDate+"' and '" + endDate);
		sql.append("' and pk_inwhouse ='"+pk_in+"'");
		sql.append("  and pk_outwhouse='"+pk_out+"'");
		sql.append("  and wds_sendplanin.iplantype=0 ");
		String pk=PuPubVO.getString_TrimZeroLenAsNull(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR));
	    if(pk==null ||pk.trim().length()==0){
	    	throw new BusinessException(" ���»�û���¼ƻ��������׷�Ӽƻ�");
	    }
	}
	
	
	//�� ׷�Ӽƻ�    ��  �¼ƻ� ��ֳ���
	public void unplanStats(AggregatedValueObject obj2) throws BusinessException{
		//�������Ķ����¡һ��
		AggregatedValueObject obj=VOTool.aggregateVOClone(obj2);
	   
		SendplaninVO parent =(SendplaninVO) obj.getParentVO();		
		String pk_inwhouse=parent.getPk_inwhouse();	
		SendplaninBVO[] childs=(SendplaninBVO[]) obj.getChildrenVO();
		AccountCalendar calendar = AccountCalendar.getInstance();
		UFDate beginDate = calendar.getMonthVO().getBegindate();
		UFDate endDate = calendar.getMonthVO().getEnddate();		
		StringBuffer sql=new StringBuffer();
		sql.append(" select pk_sendplanin  from");
		sql.append(" wds_sendplanin where wds_sendplanin.vbillstatus=1");
		sql.append(" and isnull(wds_sendplanin.dr,0)=0");
		sql.append(" and dmakedate between '");
		sql.append(beginDate+"' and '" + endDate);
		sql.append("' and pk_inwhouse ='"+pk_inwhouse+"'");

		
		List<SendplaninBVO> mods=new ArrayList<SendplaninBVO>();
		
	    Object o=getBaseDAO().executeQuery(sql.toString(), new ColumnProcessor());
	    if( o!=null && o instanceof String){      
	        String cond=" pk_sendplanin='"+o+"' and isnull(dr,0)=0";
	    	List<SendplaninBVO> list=(List<SendplaninBVO>) getBaseDAO().retrieveByClause(SendplaninBVO.class, cond);    	
	    	boolean isExist=false;
		    for(int i=0;i<childs.length;i++){
		    
		       for(int j=0;j<list.size();j++){
		    	  if(childs[i].getPk_invmandoc().equalsIgnoreCase(list.get(j).getPk_invmandoc())){	    		  
		    		  list.get(j).setNplannum(PuPubVO.getUFDouble_NullAsZero(list.get(j).getNplannum()).sub(PuPubVO.getUFDouble_NullAsZero(childs[i].getNplannum())));
		    		  list.get(j).setNassplannum(PuPubVO.getUFDouble_NullAsZero(list.get(j).getNassplannum()).sub(PuPubVO.getUFDouble_NullAsZero(childs[i].getNassplannum())));		    		  
		    		 if(list.get(j).getNplannum().doubleValue()<0 || list.get(j).getNassplannum().doubleValue()<0){
		    			throw new BusinessException("���������¼ƻ��Ѿ������˸�׷�Ӽƻ��еĵ�Ʒ");
		    		  }
		    		  mods.add(list.get(j));    		  
		    		  break;
		        }
		    	  
		      } 
		    }
	    }
	  
		  getBaseDAO().updateVOList(mods); 
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
