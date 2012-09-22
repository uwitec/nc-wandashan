package nc.vo.wdsnew.pub;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.bs.zmpub.pub.tool.stock.BillStockBO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.ic.cargtray.BdCargdocTrayVO;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * ���ɽ��Ŀ ҵ�񵥾��ȴ��������� �������� ������� �������� ������� ��ͨ������� �����ִ��� һ���ѯ�ִ��� Ҳͨ�������
 * 
 * @author mlr
 */
public class BillStockBO1 extends BillStockBO {
	private PickTool tool=null;
	public PickTool getTool(){
		if(tool==null){
			tool=new PickTool();
		}
		return tool;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6683129237534928560L;
	/**
	 * �������� ->������ ��Ӧ��ϵ
	 */
	private Map<String, String> typetoChangeclass = new HashMap<String, String>();
	/**
	 * ��������->�ִ��� �����仯����
	 */
	private Map<String, UFBoolean[]> typetosetnum = new HashMap<String, UFBoolean[]>();
	/**
	 * �ִ��� �����仯�ֶ� ��������� ��渨����
	 */
	private String[] changeNums = new String[] { "whs_stocktonnage",
			"whs_stockpieces" };
	/**
	 * �ִ���ʵ���� ·��
	 */
	private String className = "nc.vo.ic.pub.StockInvOnHandVO";
	/**
	 * �ȴ����������Сά�� ά��Ϊ�� ��˾ �ֿ� ��λ ��� ���� ���״̬ �������
	 */
	private String[] def_fields = new String[] { "pk_corp", "pk_customize1",
			"pk_cargdoc", "pplpt_pk","pk_invmandoc", "pk_invbasdoc", "whs_batchcode",
			"ss_pk", "creadate" };

	@Override
	public Map<String, String> getTypetoChangeClass() throws Exception {
		if (typetoChangeclass.size() == 0) {
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_IN,
					"nc.bs.wds.self.changedir.CHGWDS7TOACCOUNTNUM");// ����������Ᵽ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_IN_1,
					"nc.bs.wds.self.changedir.CHGWDS7TOACCOUNTNUM");// �����������ɾ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_ALLO_IN,
					"nc.bs.wds.self.changedir.CHGWDS9TOACCOUNTNUM");// ���������Ᵽ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_ALLO_IN_1,
					"nc.bs.wds.self.changedir.CHGWDS9TOACCOUNTNUM");// ����������ɾ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_OUT,
					"nc.bs.wds.self.changedir.CHGWDS6TOACCOUNTNUM");// �����������Ᵽ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_OUT_1,
					"nc.bs.wds.self.changedir.CHGWDS6TOACCOUNTNUM");// ������������ɾ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_SALE_OUT,
					"nc.bs.wds.self.changedir.CHGWDS8TOACCOUNTNUM");// �������۳��Ᵽ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_SALE_OUT_1,
					"nc.bs.wds.self.changedir.CHGWDS8TOACCOUNTNUM");// �������۳���ɾ��
			typetoChangeclass.put(Wds2WlPubConst.billtype_statusupdate,
					"nc.bs.wds.self.changedir.CHGWS20TOACCOUNTNUM");// ����״̬���������
																	// ״̬�仯ǰ������
			typetoChangeclass.put(Wds2WlPubConst.billtype_statusupdate_1,
					"nc.bs.wds.self.changedir.CHGWS20TOACCOUNTNUM1");// ����״̬���������
																		// ״̬�仯��
																		// ������
			typetoChangeclass.put(Wds2WlPubConst.billtype_statusupdate_2,
					"nc.bs.wds.self.changedir.CHGWS20TOACCOUNTNUM");// ����״̬�����ɾ��
																	// ״̬�仯ǰ������
			typetoChangeclass.put(Wds2WlPubConst.billtype_statusupdate_3,
					"nc.bs.wds.self.changedir.CHGWS20TOACCOUNTNUM1");// ����״̬�����ɾ��
																		// ״̬�仯��
																		// ������
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_ALLO_OUT,
					"nc.bs.wds.self.changedir.CHGWDSHTOACCOUNTNUM");// ����������Ᵽ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_ALLO_OUT_1,
					"nc.bs.wds.self.changedir.CHGWDSHTOACCOUNTNUM");// �����������ɾ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OUT_IN,
					"nc.bs.wds.self.changedir.CHGWDSZTOACCOUNTNUM");// �����˻���Ᵽ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OUT_IN_1,
					"nc.bs.wds.self.changedir.CHGWDSZTOACCOUNTNUM");// �����˻����ɾ��

		}
		return typetoChangeclass;
	}

	@Override
	public Map<String, UFBoolean[]> getTypetosetnum() throws Exception {
		if (typetosetnum.size() == 0) {
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OTHER_IN, new UFBoolean[] {
					new UFBoolean(false), new UFBoolean(false) });
			typetosetnum
					.put(WdsWlPubConst.BILLTYPE_OTHER_IN_1, new UFBoolean[] {
							new UFBoolean(true), new UFBoolean(true) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_ALLO_IN, new UFBoolean[] {
					new UFBoolean(false), new UFBoolean(false) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_ALLO_IN_1, new UFBoolean[] {
					new UFBoolean(true), new UFBoolean(true) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OTHER_OUT, new UFBoolean[] {
					new UFBoolean(true), new UFBoolean(true) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OTHER_OUT_1,
					new UFBoolean[] { new UFBoolean(false),
							new UFBoolean(false) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_SALE_OUT, new UFBoolean[] {
					new UFBoolean(true), new UFBoolean(true) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_SALE_OUT_1,
					new UFBoolean[] { new UFBoolean(false),
							new UFBoolean(false) });
			typetosetnum
					.put(Wds2WlPubConst.billtype_statusupdate, new UFBoolean[] {
							new UFBoolean(true), new UFBoolean(true) });
			typetosetnum.put(Wds2WlPubConst.billtype_statusupdate_1,
					new UFBoolean[] { new UFBoolean(false),
							new UFBoolean(false) });
			typetosetnum.put(Wds2WlPubConst.billtype_statusupdate_2,
					new UFBoolean[] { new UFBoolean(false),
							new UFBoolean(false) });
			typetosetnum
					.put(Wds2WlPubConst.billtype_statusupdate_3,
							new UFBoolean[] { new UFBoolean(true),
									new UFBoolean(true) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_ALLO_OUT, new UFBoolean[] {
					new UFBoolean(true), new UFBoolean(true) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_ALLO_OUT_1,
					new UFBoolean[] { new UFBoolean(false),
							new UFBoolean(false) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OUT_IN, new UFBoolean[] {
					new UFBoolean(false), new UFBoolean(false) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OUT_IN_1, new UFBoolean[] {
					new UFBoolean(true), new UFBoolean(true) });

		}
		return typetosetnum;
	}

	/**
	 * ͨ��where������ѯ�ִ���
	 * 
	 * @param whereSql
	 * @return
	 */
	public SuperVO[] queryStock(String whereSql) throws Exception {
		String clname = getClassName();
		if (clname == null || clname.length() == 0)
			throw new Exception("û��ע���ִ���ʵ����ȫ·��");
		Class cl = Class.forName(clname);
		Collection list = getDao().retrieveByClause(cl, whereSql);
		if (list == null || list.size() == 0)
			return null;
		SuperVO[] vos = (SuperVO[]) list
				.toArray((SuperVO[]) java.lang.reflect.Array.newInstance(cl,
						list.size()));
		return vos;

	}

	@Override
	public String[] getChangeNums() {

		return changeNums;
	}

	@Override
	public String getClassName() {

		return className;
	}

	@Override
	public String[] getDef_Fields() {

		return def_fields;
	}

	@Override
	public String getThisClassName() {

		return this.getClass().getName();
	}

	/**
	 * ���ݴ�����ִ���vo ȡ��ά�� ��ѯ�ִ��� SuperVO[] ���ÿ����ѯά�Ȳ�ѯ�������ִ���(����ѯά�Ⱥϲ���)
	 * 
	 * @throws Exception
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-7-2����12:27:29
	 * 
	 */
	public SuperVO[] queryStockCombin1(SuperVO[] vos,String whereSql) throws Exception {
		return super.queryStockCombin1(vos,whereSql);
	}
	/**
	 * ���ݴ�����ִ���vo ȡ��ά�� ��ѯ�ִ��� SuperVO[] ���ÿ����ѯά�Ȳ�ѯ�������ִ���(����ѯά�Ⱥϲ���)
	 * 
	 * @throws Exception
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-7-2����12:27:29
	 * 
	 */
	public SuperVO[] queryStockCombin(SuperVO[] vos) throws Exception {
		return super.queryStockCombin(vos);
	}
	/**
	 * @author mlr
	 * �ִ������º� У�� �Ƿ񳬻�������
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public void check(SuperVO[] vos1) throws Exception {
		super.check(vos1);
		StockInvOnHandVO[]  vos=(StockInvOnHandVO[]) vos1;
		if (vos == null || vos.length == 0)
			return;
		for (int i = 0; i < vos.length; i++) {
	
			//��ѯ  �����ִ����Ǹ��Ѵ�����
			String pk_cargdoc = vos[i].getPk_cargdoc();
			String cdtpk = vos[i].getPplpt_pk();
			String pk_invmandoc = vos[i].getPk_invmandoc();
			String wheresql = " pk_cargdoc = '" + pk_cargdoc
					+ "' and pplpt_pk='" + cdtpk + "' and pk_invmandoc ='"
					+ pk_invmandoc
					+ "' and isnull(dr,0)=0 and  whs_stockpieces > 0 ";
			StockInvOnHandVO vo = new StockInvOnHandVO();
			vo.setPk_cargdoc(pk_cargdoc);
			vo.setPplpt_pk(cdtpk);
			vo.setPk_invmandoc(pk_invmandoc);
			StockInvOnHandVO[] ols = (StockInvOnHandVO[]) queryStockCombin(new StockInvOnHandVO[] { vo });
			
			
			if (ols == null || ols.length == 0)
				return;
			for (int j = 0; j < ols.length; j++) {
				StockInvOnHandVO stock=ols[j];
				//�����Ƿ񳬻�������
				BdCargdocTrayVO[] bvos=getTool().queryCat(stock.getPk_customize1(), stock.getPk_cargdoc(), stock.getPk_invmandoc(), stock.getPplpt_pk());
				//���ڻ���   ��λ+���� ��Ωһ�� ����ֻȡ��һ��
				if(bvos==null || bvos.length==0)
					continue;
				BdCargdocTrayVO bvo=bvos[0];
				//��ô�� ��һ�����ϵ� �������
				UFDouble boxnum=PuPubVO.getUFDouble_NullAsZero(getTool().getInvVolume(stock.getPk_invmandoc()));
				//����� ���ܵ�������
				UFDouble znum=boxnum.multiply(PuPubVO.getUFDouble_NullAsZero(bvo.getNsize()));
				//���ִ����õ� Ŀǰ�ִ���
				UFDouble  xcnum=PuPubVO.getUFDouble_NullAsZero(stock.getWhs_stockpieces());
				if(xcnum.doubleValue()>znum.doubleValue()){
					String cdtcode=bvo.getCdt_traycode();
					throw new Exception("���ܱ���Ϊ ��"+cdtcode+" �Ļ���  �������");
				}
			}
		}
	}


}
