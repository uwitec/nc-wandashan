package nc.bs.wds.ic.zgjz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.util.SQLHelper;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.ic.zgjz.ZgjzBVO;
import nc.vo.wds.ic.zgjz.ZgjzHVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class ZgjzAuditBo {
	
private BaseDAO baseDAO = null;
	
	private BaseDAO  getBaseDAO(){
		if(baseDAO == null){
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	String pk_corp = null;
	ZgjzHVO curhead = null;
	ZgjzBVO[] curbodys = null;
	
	/**
	 * 
	 * @���ߣ�lyf:�����ݹ������������Զ�����
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-15����04:04:42
	 * @param vo
	 * @param infor:��¼����������¼���ڣ���ǰ��˾������ϵͳ����
	 * @return
	 * @throws BusinessException 
	 */
	public void onAudit(AggregatedValueObject vo,ArrayList<String> infor) throws BusinessException{
		if(vo == null ){
			return ;
		}
		pk_corp = infor.get(2);
		if(pk_corp == null){
			pk_corp = SQLHelper.getCorpPk();
		}
		ZgjzHVO head_last = (ZgjzHVO) vo.getParentVO();
		UFDate dbegindate_last = head_last.getDbegindate();//���¿�ʼ����
		UFDate denddate_last = head_last.getDenddate();//���½�������
		if(dbegindate_last == null){
			throw new BusinessException("��ʼ���ڲ���Ϊ��");
		}
		if(denddate_last == null){//�����ֹ���ڲ��գ�Ĭ��Ϊϵͳ����
			denddate_last = new UFDate(System.currentTimeMillis());
			head_last.setDenddate(denddate_last);
		}
		//���ȸ��±��³�����Ƿ����
		ZgjzBO  bo = new ZgjzBO();
		vo =bo.refeshDeducNum(vo, infor);
		//���������ݹ�����
		curhead = head_last;
		curbodys = (ZgjzBVO[])vo.getChildrenVO();
		HYBillVO billVo = new HYBillVO();
		ZgjzHVO parent = getHead(head_last,denddate_last);
		ZgjzBVO[] children = getBodys();
		billVo.setParentVO(parent);
		billVo.setChildrenVO(children);
		new HYPubBO_Client().saveBill(billVo);
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * ����������ݱ�ͷ
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-19����06:37:15
	 * @param head
	 * @return
	 * @throws BusinessException 
	 */
	public ZgjzHVO getHead(ZgjzHVO head,UFDate denddate_last) throws BusinessException{
		ZgjzHVO head_new = new ZgjzHVO();
		String[] names = head_new.getAttributeNames();
		for(String name:names){
			head_new.setAttributeValue(name, head.getAttributeValue(name));
		}
		UFDate dendDate = denddate_last.getDateAfter(29);
		int year = dendDate.getYear();
		int month = dendDate.getMonth();
		int day = getDefaultDay();
		dendDate = new UFDate(year+"-"+month+"-"+day);
		head_new.setDbegindate(denddate_last.getDateAfter(1));//��ʼ����
		head_new.setDenddate(dendDate);//��ֹ����
		head_new.setVoperatorid(head.getVapproveid());//�Ƶ���--��ǰ������
		head_new.setDmakedate(head.getDapprovedate());//�Ƶ�����--��������
		head_new.setDbilldate(head.getDapprovedate());//��������
		head_new.setPrimaryKey(null);//�������
		head_new.setDr(0);
		head_new.setTs(null);
		head_new.setStatus(IBillStatus.FREE);
		return head_new;
	}
	/**
	 * 
	 * @����:lyf ��ñ���
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-19����08:14:21
	 * @param bodys
	 * @return
	 * @throws BusinessException 
	 */
	public ZgjzBVO[] getBodys() throws BusinessException{
		Map<String, ZgjzBVO> map = new HashMap<String, ZgjzBVO>();
		//�����÷������֣��ϸ��£�����֮ǰ���ݹ�δ����������ͱ��������ݹ�����
		//1.��ñ��¶��ϸ����ݹ�δ������ɵ���,ԭ��
		//����ǰǷ������ = ����ǰǷ�����������������ݣ�-���³�����Ƿ���������������ݣ�-���������������������ݣ�>0
		map = getLeftNum(map);
		//2.��ѯ�����µ��ݹ�������������ERP�������ݹ��������ⵥ
		map = getNewNum(map);
		return map.values().toArray(new ZgjzBVO[0]);
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ:������ʣ���ݹ������������� 
	 * @ʱ�䣺2011-12-20����10:33:59
	 * @param map
	 * @param bodys
	 */
	public Map<String, ZgjzBVO> getLeftNum(Map<String, ZgjzBVO> map){
		if(map == null){
			map = new HashMap<String, ZgjzBVO>();
		}
		if(curbodys == null || curbodys.length ==0){
			return map;
		}
		
		for(ZgjzBVO body:curbodys){
			//����ǰǷ����������
			UFDouble nlastnum = PuPubVO.getUFDouble_NullAsZero(body.getNlastnum());
			UFDouble nlastassnum = PuPubVO.getUFDouble_NullAsZero(body.getNlastassnum());
			//���³���������
			UFDouble nreducnum = PuPubVO.getUFDouble_NullAsZero(body.getNreducnum());
			UFDouble nreducassnum = PuPubVO.getUFDouble_NullAsZero(body.getNreducassnum());
			//��������
			UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(body.getNoutnum());
			UFDouble noutassnum = PuPubVO.getUFDouble_NullAsZero(body.getNoutassnum());
			//--�ж�ʣ������
			UFDouble nleftnum = nlastnum.sub(nreducnum).sub(noutnum);
			UFDouble nleftassnum = nlastassnum.sub(nreducassnum).sub(noutassnum);

			if(nleftnum.doubleValue() <=0){
				continue;
			}
			String key = PuPubVO.getString_TrimZeroLenAsNull(body.getPk_invmandoc());
			if(key == null){
				continue;
			}
			if(map.containsKey(body.getPk_invmandoc())){
				ZgjzBVO body_new = map.get(key);
				UFDouble nleftold = PuPubVO.getUFDouble_NullAsZero(body_new.getNlastnum());
				UFDouble nleftassnumold = PuPubVO.getUFDouble_NullAsZero(body_new.getNlastassnum());
				body_new.setNlastnum(nleftold.add(nleftnum));
				body_new.setNlastassnum(nleftassnumold.add(nleftassnum));
			}else{
				ZgjzBVO body_new = new ZgjzBVO();
				body_new.setPk_invmandoc(body.getPk_invmandoc());
				body_new.setPk_invbasdoc(body.getPk_invbasdoc());
				body_new.setNlastnum(nleftnum);
				body_new.setNlastassnum(nleftassnum);
				body_new.setStatus(IBillStatus.FREE);
				map.put(key, body_new);
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @throws BusinessException 
	 * @���ߣ�lyf:��ñ����µ��ݹ���
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-20����10:41:20
	 */
	public Map<String, ZgjzBVO> getNewNum(Map<String, ZgjzBVO> map) throws BusinessException{
		if(map == null){
			map = new HashMap<String, ZgjzBVO>();
		}
		int ilacktype = PuPubVO.getInteger_NullAs(curhead.getIlacktype(), 0);
		StringBuffer sql = new StringBuffer();
		if(ilacktype ==0){//���۱����ݹ���
			sql.append(" select b.cinventoryid, ");//�������id
			sql.append(" sum(b.noutnum) noutnum,  ");//ʵ������
			sql.append(" sum(b.noutassistnum) noutassistnum ");//ʵ��������
			sql.append(" from ic_general_h h ");// --����ⵥ����
			sql.append(" join ic_general_b b ");//--����ⵥ�ӱ�
			sql.append(" on h.cgeneralhid = b.cgeneralhid ");
			sql.append(" where isnull(h.dr,0)=0  and  isnull(b.dr,0)=0 ");
			sql.append(" and h.pk_corp='"+pk_corp+"'");
			sql.append("  and h.cbilltypecode='4C'");//�������ⵥ
			sql.append(" and h.pk_defdoc11='"+WdsWlPubConst.WDS_IC_FLAG_wu+"'");//�������
			sql.append(" and h.dbilldate between '"+curhead.getDbegindate()+"' and '"+curhead.getDenddate()+"' ");
			sql.append(" group by b.cinventoryid ");
		}else if(ilacktype ==1){//ת�ֱֲ����ݹ���
			sql.append(" select b.cinventoryid, ");//�������id
			sql.append(" sum(b.noutnum) noutnum,  ");//ʵ������
			sql.append(" sum(b.noutassistnum) noutassistnum ");//ʵ��������
			sql.append(" from ic_general_h h ");// --����ⵥ����
			sql.append(" join ic_general_b b ");//--����ⵥ�ӱ�
			sql.append(" on h.cgeneralhid = b.cgeneralhid ");
			sql.append(" where isnull(h.dr,0)=0  and  isnull(b.dr,0)=0 ");
			sql.append(" and h.pk_corp='"+pk_corp+"'");
			sql.append(" and h.cbilltypecode='4I'");//�������ⵥ
			sql.append(" and h.pk_defdoc11='"+WdsWlPubConst.WDS_IC_FLAG_wu+"'");//�������
			sql.append(" and h.dbilldate between '"+curhead.getDbegindate()+"' and '"+curhead.getDenddate()+"' ");
			sql.append(" group by b.cinventoryid ");
		}
		Object result = getBaseDAO().executeQuery(sql.toString(),new ArrayListProcessor());
		if(result == null){
			return map;
		}else{
			ArrayList<Object[]>  list = (ArrayList<Object[]>)result;
			for(Object[] obj:list){
				String cinventoryid =PuPubVO.getString_TrimZeroLenAsNull(obj[0]);
				UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(obj[1]);
				UFDouble noutassistnum = PuPubVO.getUFDouble_NullAsZero(obj[2]);
				if(cinventoryid == null)
					continue;
				String key = cinventoryid;
				if(map.containsKey(key)){
					ZgjzBVO body_new = map.get(key);
					UFDouble nleftold = PuPubVO.getUFDouble_NullAsZero(body_new.getNlastnum());
					UFDouble nleftassnumold = PuPubVO.getUFDouble_NullAsZero(body_new.getNlastassnum());
					body_new.setNlastnum(nleftold.add(noutnum));
					body_new.setNlastassnum(nleftassnumold.add(noutassistnum));
				}else{
					ZgjzBVO body_new = new ZgjzBVO();
					body_new.setPk_invmandoc(cinventoryid);
					body_new.setPk_invbasdoc(getPk_invbasdoc(cinventoryid));
					body_new.setNlastnum(noutnum);
					body_new.setNlastassnum(noutassistnum);
					body_new.setStatus(IBillStatus.FREE);
					map.put(key, body_new);
				}
			}
		}
		return map;
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ :���ݴ������id��ô������id
	 * @ʱ�䣺2011-12-20����02:56:41
	 * @param pk_invmandoc
	 * @return
	 * @throws BusinessException
	 */
	private String getPk_invbasdoc(String pk_invmandoc) throws BusinessException{
		String pk_invbasdoc = null;
		if(pk_invmandoc == null || "".equalsIgnoreCase(pk_invmandoc)){
			return pk_invbasdoc;
		}
		String sql = " select pk_invbasdoc from bd_invmandoc where isnull(dr,0)=0 and pk_invmandoc='"+pk_invmandoc+"'";

		pk_invbasdoc = (String)getBaseDAO().executeQuery(sql, new ColumnProcessor());
		return pk_invbasdoc;
	}
	/**
	 * 
	 * @���ߣ�lyf ��ѯ������Ĭ��ֵ
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-20����10:23:43
	 * @throws BusinessException
	 */
	private Integer getDefaultDay() throws BusinessException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select datavale from wds_periodsetting_h ");
		sql.append(" where isnull(dr,0) =0 ");
		sql.append(" and pk_corp='"+pk_corp+"'");
		Object value = getBaseDAO().executeQuery(sql.toString(), new ColumnProcessor());
		return PuPubVO.getInteger_NullAs(value, 20);
	}

}
