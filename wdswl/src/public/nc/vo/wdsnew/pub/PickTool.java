package nc.vo.wdsnew.pub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.util.SQLHelper;
import nc.ui.scm.util.ObjectUtils;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.wds.ic.cargtray.BdCargdocTrayVO;
import nc.vo.wds.invbasdoc.InvbasdocVO;
import nc.vo.wds.transfer.TransferBVO;
import nc.vo.zmpub.pub.tool.ZmPubTool;

/**
 * ���ɽ��������ⵥ�Զ����
 * 
 * @author mlr
 */
public class PickTool implements Serializable {
	private static final long serialVersionUID = -6131447795689577612L;
	// �����
	private Map<String, List<StockInvOnHandVO>> mpick = new HashMap<String, List<StockInvOnHandVO>>();
	private BillStockBO1 stock = null;
	private BaseDAO dao = null;

	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	private BillStockBO1 getStock() {
		if (stock == null) {
			stock = new BillStockBO1();
		}
		return stock;
	}

	/**
	 * �÷���ǰ̨ ����Զ�̵��� (��ѯ���ݿ�) ����Զ����
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 1 ���� �ֿ� ��λ ��� ��ѯ�ִ��� �����Ƚ��ȳ�ԭ�� ���
	 * 
	 *             2������� �γɼ���� ��������� ��һ��map key=���ⵥ����id ��value=�ִ���vo
	 * 
	 *             3���¹�������� ��ⵥ����
	 * 
	 *             4 ��������
	 * @ʱ�䣺2012-6-20����09:56:08
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param bvos
	 * @return
	 * @throws Exception
	 */
	public TbGeneralBVO[] autoPick2(String pk_stordoc, String pk_cargdoc,
			TbGeneralBVO[] bvos) throws Exception {
		if (pk_stordoc == null || pk_stordoc.length() == 0)
			throw new Exception("���ֿ�Ϊ��");
		if (pk_cargdoc == null || pk_cargdoc.length() == 0)
			throw new Exception("����λΪ��");
		// ��ռ����
		mpick.clear();
		if (bvos == null || bvos.length == 0)
			return null;
		for (int i = 0; i < bvos.length; i++) {
			//��ͷ�� Ĭ�ϻ�λ  �������ʱ  Ӧ�� ������ʵ�ʻ�λ���
			String pk_car=PuPubVO.getString_TrimZeroLenAsNull(bvos[i].getGeb_space());
			if(pk_car!=null){
				pk_cargdoc=pk_car;
			}
			pick(pk_stordoc, pk_cargdoc, bvos[i], i);
			
		}
		return createBill(bvos);
	}

	/**
	 * ���ݼ���� ���¹������ⵥ������Ϣ
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-6-20����10:43:38
	 * @return
	 * @throws Exception
	 */
	private TbGeneralBVO[] createBill(TbGeneralBVO[] bvos) throws Exception {
		List<TbGeneralBVO> list = new ArrayList<TbGeneralBVO>();// ������¹����ı�������
		for (int i = 0; i < bvos.length; i++) {
			// ȡ�����еļ����
			List<StockInvOnHandVO> li = mpick.get(i + "");
			// ���û���ִ��� ���б��ֲ���
			if (li == null || li.size() == 0) {
				list.add(bvos[i]);
			} else {
				// ���� ���ݼ���� ���¹��� ���β��к�ı���
				for (int j = 0; j < li.size(); j++) {
					TbGeneralBVO vo = (TbGeneralBVO) ObjectUtils
							.serializableClone(bvos[i]);
					// vo.setGeb_vbatchcode(li.get(j).getWhs_batchcode());//
					// ��������
					// vo.setGeb_proddate(new
					// UFDate(getDate(li.get(j).getWhs_batchcode())));// ������������
					// vo.setCdt_pk(li.get(j).getSs_pk());// ���ô��״̬
					vo.setAttributeValue("geb_bsnum", li.get(j)
							.getAttributeValue("whs_omnum"));// ����Ӧ�ո�����
					vo.setAttributeValue("geb_banum", li.get(j)
							.getAttributeValue("whs_oanum"));// ����ʵ�ո�����
					// ���û�����Ϣ
					vo.setAttributeValue("geb_customize4", li.get(j)
							.getAttributeValue("pplpt_pk"));// ���û���id
					list.add(vo);
				}
			}
		}
		throw new StockException(list.toArray(new TbGeneralBVO[0]));
	}

	/**
	 * ǰ̨����Զ�̵��� (��ѯ���ݿ�)
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ ���м�� ���� �ֿ� ��λ ��� ��ѯ�ִ������ ���������� ��������
	 * @ʱ�䣺2012-6-20����10:04:23
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param i
	 * @param tbOutgeneralBVO
	 * @throws Exception
	 */
	private void pick(String pk_stordoc, String pk_cargdoc, TbGeneralBVO vo,
			int i) throws Exception {
		StringBuffer error = new StringBuffer();// ��ż��������Ϣ
		check(vo);
		// ȥ��λ���ܰ� ���� ��� +��λ ��ѯ����
		BdCargdocTrayVO[] tvos = queryCat(pk_stordoc, pk_cargdoc,
				vo.geb_cinventoryid,vo.getGeb_customize4());
		sort1(tvos);// �����ܱ��� �Ӵ�С����
		if (tvos == null || tvos.length == 0)
			error.append("�����  ��� û�л��� ,��ȥ��λ���ܰ�  ���û���   && ");
		// ȥ�ִ��� ���� ��� +��λ + ���ܱ��� ��ѯ �������д��
		// �����ִ��� vo ���û�λ ��� ���� ������������ ����ʣ�����������
		StockInvOnHandVO[] stocks = setCatVolume(tvos, vo, pk_stordoc);
		if (stocks == null || stocks.length == 0) {
			if (error.toString().length() == 0) {
				error.append("����� ��� û�п��û���  &&");
			}
		}
		sort(stocks);// �����ܱ����ɴ�С����
		StockInvOnHandVO[] stocks1=spitNum(stocks);//���˵���������Ϊ���
		if(stocks!=null && stocks.length>0){
			if(stocks1==null || stocks1.length==0){
				error.append("����� ��� û�п��û���  &&");
			}
		}
		// ���ܷ���
		// ��ʼ���
		// ������� ��������
		spiltNum(stocks1, vo, i, error);
	}
    /**
     * 
     * @���ߣ�zhf
     * @˵�������ɽ������Ŀ 
     * @ʱ�䣺2012-9-12����03:05:05
     * @param stocks
     * @return
     */
	private StockInvOnHandVO[] spitNum(StockInvOnHandVO[] stocks) {
		if(stocks==null || stocks.length==0)
			return null;
		List<StockInvOnHandVO> list=new ArrayList<StockInvOnHandVO>();
		for(int i=0;i<stocks.length;i++){
		   if(PuPubVO.getUFDouble_NullAsZero(stocks[i].getWhs_stockpieces()).doubleValue()>0){
			   list.add(stocks[i]);
		   }	
		}
		return list.toArray(new StockInvOnHandVO[0]);
	}

	private void sort1(BdCargdocTrayVO[] tvos) {
		if (tvos == null || tvos.length == 0)
			return;
		VOUtil.descSort(tvos, new String[] { "cdt_traycode" });
	}

	/**
	 * �����ܱ����ɴ�С����
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-11����02:48:55
	 * @param stocks1
	 */
	private void sort(StockInvOnHandVO[] stocks1) {
		if (stocks1 == null || stocks1.length == 0)
			return;
		VOUtil.descSort(stocks1, new String[] { "whs_customize3" });

	}

	/**
	 * �������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-6-20����10:25:33
	 * @param stocks
	 *            �ִ���
	 * @param vo
	 *            ���ⵥ����
	 * @throws Exception
	 */
	private void spiltNum(StockInvOnHandVO[] vos, TbGeneralBVO vo, int index,
			StringBuffer error) throws Exception {

		UFDouble zbnum = PuPubVO.getUFDouble_NullAsZero(vo.getGeb_bsnum());// ȡ����ⵥӦ�ո�����
		UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(vo.getGeb_banum());// ���ʵ������
		vo.setVnote(null);
	    if(noutnum.doubleValue()>zbnum.doubleValue()){
	    	throw new Exception("������ �� "+(index+1)+"�� ʵ���������ܴ���Ӧ������ ");
	    }
		
		if (vos == null || vos.length == 0) {
			mpick.put("" + index, null);// ����ִ���Ϊ�� ����м��������Ϊ��
			vo.setVnote(error.toString());
			return;
		}
		if (zbnum.doubleValue() == 0) {
			mpick.put("" + index, null);// �����ⵥӦ�ո�����Ϊ0 ����м��������Ϊ��
			error.append(" Ӧ����������Ϊ�� && ");
			vo.setVnote(error.toString());
			return;
		}
		if (noutnum.doubleValue() > 0) {
//			mpick.put("" + index, null);// �����ⵥʵ��������ֵ ���ٲ��� �Զ���� ����м��������Ϊ��
//			error.append(" ����ʵ�������Ĳ��ܲ����Զ���� && ");
			zbnum=noutnum;
			vo.setVnote(error.toString());
		//	return;
		}
		// ���з���
		// �� ���κ� ��С���� ���η���
		List<StockInvOnHandVO> list = new ArrayList<StockInvOnHandVO>();
		for (int i = 0; i < vos.length; i++) {
			UFDouble bnum = PuPubVO.getUFDouble_NullAsZero(vos[i]
					.getAttributeValue("whs_stockpieces")); // ���ܿ�������
			if (zbnum.doubleValue() > bnum.doubleValue()) {
				if (i == vos.length - 1) {
					vos[i].setAttributeValue("whs_omnum", zbnum);// ����Ӧ������(������)
					vos[i].setAttributeValue("whs_oanum", bnum);// ����ʵ������
				} else {
					zbnum = zbnum.sub(bnum);
					vos[i].setAttributeValue("whs_omnum", bnum);// ����Ӧ������(������)
					vos[i].setAttributeValue("whs_oanum", bnum);// ����ʵ������(������)
				}
				list.add(vos[i]);
			} else if (zbnum.doubleValue() < bnum.doubleValue()) {
				vos[i].setAttributeValue("whs_omnum", zbnum);// ����Ӧ������ (������)
				vos[i].setAttributeValue("whs_oanum", zbnum);// ����ʵ������(������)
				list.add(vos[i]);
				break;
			} else {
				vos[i].setAttributeValue("whs_omnum", zbnum);// ����Ӧ������ (������)
				vos[i].setAttributeValue("whs_oanum", zbnum);// ����ʵ������(������)
				list.add(vos[i]);
				break;
			}
		}
		mpick.put(index + "", list);
		vo.setVnote(error.toString());
		updateStock1(list);
	}
	/**
	 * ���� ������Ϣ �õ� �ִ�����Ϣ
	 * 
	 * ѭ������ ������Ϣ ���ȥ�ִ��� ������+�ֿ� +��λ +��� ά�� ��ѯ����ռ���� �����ִ���vo ���û���ռ���� �� ������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-11����02:12:35
	 * @param tvos
	 * @param vo2
	 * @return
	 * @throws Exception
	 */
	private StockInvOnHandVO[] setCatVolume(BdCargdocTrayVO[] tvos,
			TbGeneralBVO vo2, String pk_stordoc) throws Exception {
		if (tvos == null || tvos.length == 0)
			return null;
		StockInvOnHandVO[] vos = new StockInvOnHandVO[tvos.length];// �����ִ���vo
		for (int i = 0; i < tvos.length; i++) {
			String pk_cargdoc = tvos[i].getPk_cargdoc();
			String cdtpk = tvos[i].getPrimaryKey();
			String pk_invmandoc = tvos[i].getCdt_invmandoc();
			String wheresql = " pk_cargdoc = '" + pk_cargdoc
					+ "' and pplpt_pk='" + cdtpk + "' and pk_invmandoc ='"
					+ pk_invmandoc
					+ "' and isnull(dr,0)=0 and  whs_stockpieces > 0 ";
			StockInvOnHandVO vo = new StockInvOnHandVO();
			vo.setPk_cargdoc(pk_cargdoc);
			vo.setPplpt_pk(cdtpk);
			vo.setPk_invmandoc(pk_invmandoc);
			StockInvOnHandVO[] stocks = (StockInvOnHandVO[]) getStock()
					.queryStockCombin(new StockInvOnHandVO[] { vo });
			if (stocks == null || stocks.length == 0) {
				// whs_stockpieces ���ܿ�������
				// whs_omnum ����Ѿ�ռ���ֶ�
				// whs_oanum ����Ѿ�ռ���ֶ�
				vos[i] = createStockVO(tvos[i], vo2, pk_stordoc);
			} else {
				vos[i] = createStockVO(tvos[i], stocks[0], vo2, pk_stordoc);
			}
		}
		return vos;
	}

	/**
	 * ���ݻ�����Ϣ ���� �ִ���vo
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-11����05:17:34
	 * @param bdCargdocTrayVO
	 * @param stockInvOnHandVO
	 * @param pk_stordoc
	 * @return
	 * @throws Exception
	 */
	private StockInvOnHandVO createStockVO(BdCargdocTrayVO vo,
			StockInvOnHandVO st, TbGeneralBVO vo2, String pk_stordoc)
			throws Exception {
		StockInvOnHandVO stock = new StockInvOnHandVO();
		stock.setPk_corp(SQLHelper.getCorpPk());
		stock.setPplpt_pk(vo.getPrimaryKey());// ���û���
		stock.setWhs_batchcode(vo2.getGeb_vbatchcode());// �������κ�
		stock.setCreadate(new UFDate(getDate(vo2.getGeb_vbatchcode())));
		stock.setSs_pk(vo2.getCdt_pk());//���ô��״̬
		stock.setPk_cargdoc(vo.getPk_cargdoc());// ���û�λ
		stock.setPk_customize1(pk_stordoc);// ���òֿ�
		stock.setPk_invbasdoc(vo.getCdt_invbasdoc()); // ���û��ܰ󶨵Ĵ��
		stock.setPk_invmandoc(vo.getCdt_invmandoc());
		stock.setWhs_customize3(vo.getCdt_traycode());// ���û��ܱ���
		UFDouble yboxnum = PuPubVO.getUFDouble_NullAsZero(st
				.getWhs_stockpieces());// �����Ѿ�ռ����
		stock.setWhs_stocktonnage(yboxnum);// ���û����Ѿ�ռ����(��)
		Integer size = vo.getNsize();// ��ȡ��������������
		if (size == null || size <= 0) {
			throw new Exception("���ܱ��� ��" + vo.getCdt_traycode() + " ����û������ ");
		}
		UFDouble boxnum = getInvVolume(vo.getCdt_invmandoc());// ��ȡĳ����������̴�Ŵ��������
		UFDouble zboxnum = boxnum.multiply(size);// ������ܵ�������
		UFDouble kboxnum = zboxnum.sub(yboxnum);// ������̿�������
		// ����ȡ������
		Integer tnum = (kboxnum.div(boxnum)).intValue();// ��ÿ���������
		stock.setWhs_stockpieces(boxnum.multiply(tnum));// ���û��ܿ����������䣩
		return stock;
	}

	/**
	 * ���ݻ�����Ϣ ���� �ִ���vo
	 * 
	 * @param vo2
	 * @param stockInvOnHandVO
	 * @param pk_stordoc
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-11����04:32:25
	 * @param bdCargdocTrayVO
	 * @return
	 * @throws Exception
	 */
	private StockInvOnHandVO createStockVO(BdCargdocTrayVO vo,
			TbGeneralBVO vo2, String pk_stordoc) throws Exception {
		StockInvOnHandVO stock = new StockInvOnHandVO();
		stock.setPk_corp(SQLHelper.getCorpPk());
		stock.setPplpt_pk(vo.getPrimaryKey());// ���û���
		stock.setWhs_batchcode(vo2.getGeb_vbatchcode());// �������κ�
		stock.setCreadate(new UFDate(getDate(vo2.getGeb_vbatchcode())));
		stock.setSs_pk(vo2.getCdt_pk());//���ô��״̬
		stock.setPk_cargdoc(vo.getPk_cargdoc());// ���û�λ
		stock.setWhs_customize3(vo.getCdt_traycode());// ���û��ܱ���
		stock.setPk_customize1(pk_stordoc);// ���òֿ�
		stock.setPk_invbasdoc(vo.getCdt_invbasdoc()); // ���û��ܰ󶨵Ĵ��
		stock.setPk_invmandoc(vo.getCdt_invmandoc());
		stock.setWhs_stocktonnage(new UFDouble(0));// ���û����Ѿ�ռ����(��)
		Integer size = vo.getNsize();// ��ȡ��������������
		if (size == null || size <= 0) {
			throw new Exception("���ܱ��� ��" + vo.getCdt_traycode() + " ����û������ ");
		}
		UFDouble boxnum = getInvVolume(vo.getCdt_invmandoc());// ��ȡĳ����������̴�Ŵ��������
		stock.setWhs_stockpieces(boxnum.multiply(size));// ���û��ܿ����������䣩
		return stock;
	}

	/**
	 * ��ȡĳ������� �����������䣩
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-11����04:51:17
	 * @param cdt_invmandoc
	 * @return
	 * @throws Exception
	 */
	public UFDouble getInvVolume(String cdt_invmandoc) throws Exception {
		String wheresql = " pk_invmandoc = '" + cdt_invmandoc
				+ "' and isnull(dr,0)=0 ";
		List list = (List) getDao().retrieveByClause(InvbasdocVO.class,
				wheresql);
		if (list == null || list.size() == 0)
			throw new Exception("�������û��ά����Ӧ�����Ϣ");
		InvbasdocVO vo = (InvbasdocVO) list.get(0);
		

		if (PuPubVO.getUFDouble_NullAsZero(vo.getTray_volume()).doubleValue() <= 0) {
			String invode = (String) ZmPubTool
					.execFomular(
							"invcode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,pk_invbasdoc)",
							new String[] { "pk_invbasdoc" }, new String[] { vo
									.getPk_invbasdoc() });

			throw new Exception("������룺" + invode + " û��ά������������Ϣ");
		}
		return PuPubVO.getUFDouble_NullAsZero(vo.getTray_volume());
	}

	/**
	 * ȥ��λ����� ��ѯ������Ϣ
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-11����01:22:46
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param geb_cinventoryid
	 * @param cdtpk 
	 * @return
	 * @throws Exception
	 */
	public  BdCargdocTrayVO[] queryCat(String pk_stordoc, String pk_cargdoc,
			String geb_cinventoryid, String cdtpk) throws Exception {
		String wheresql = " pk_cargdoc='" + pk_cargdoc
				+ "' and  cdt_invmandoc ='" + geb_cinventoryid
				+ "' and isnull(dr,0)=0 ";
		if(cdtpk!=null && cdtpk.length()>0){
			wheresql =wheresql+" and cdt_pk ='"+cdtpk+"'";
		}

		List list = (List) getDao().retrieveByClause(BdCargdocTrayVO.class,
				wheresql);
		if (list == null || list.size() == 0)
			return null;
		return (BdCargdocTrayVO[]) list.toArray(new BdCargdocTrayVO[0]);
	}

	/**
	 * �Զ�������ǰ �����������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-11����12:00:46
	 * @param vo
	 * @throws Exception
	 */
	private void check(TbGeneralBVO vo) throws Exception {
		if (PuPubVO.getUFDouble_NullAsZero(vo.getGeb_bsnum()).doubleValue() <= 0) {
			throw new Exception("Ӧ�� ���������������");
		}
		if (PuPubVO.getUFDouble_NullAsZero(vo.getGeb_snum()).doubleValue() <= 0) {
			throw new Exception("Ӧ�� ���������������");
		}
		if (vo.getGeb_vbatchcode() == null
				|| vo.getGeb_vbatchcode().length() == 0) {
			throw new Exception("���β���Ϊ��");
		}
		if (vo.getCdt_pk() == null || vo.getCdt_pk().length() == 0) {
			throw new Exception("���״̬����Ϊ��");
		}
	}

	/**
	 * �÷���ǰ̨ ����Զ�̵��� (��ѯ���ݿ�) �����Զ����
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 1 ���� �ֿ� ��λ ��� ��ѯ�ִ��� �����Ƚ��ȳ�ԭ�� ���
	 * 
	 *             2������� �γɼ���� ��������� ��һ��map key=���ⵥ����id ��value=�ִ���vo
	 * 
	 *             3���¹�������� ���ⵥ����
	 * 
	 *             4 ��������
	 * @ʱ�䣺2012-6-20����09:56:08
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param bvos
	 * @return
	 * @throws Exception
	 */
	public TbOutgeneralBVO[] autoPick(String pk_stordoc, String pk_cargdoc,
			TbOutgeneralBVO[] bvos) throws Exception {
		if (pk_stordoc == null || pk_stordoc.length() == 0)
			throw new Exception("����ֿ�Ϊ��");
		if (pk_cargdoc == null || pk_cargdoc.length() == 0)
			throw new Exception("�����λΪ��");
		// ��ռ����
		mpick.clear();
		if (bvos == null || bvos.length == 0)
			return null;
		for (int i = 0; i < bvos.length; i++) {
			pick(pk_stordoc, pk_cargdoc, bvos[i], i);
		}
		return createBill(bvos);
	}

	/**
	 * ���ݼ���� ���¹������ⵥ������Ϣ
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-6-20����10:43:38
	 * @return
	 * @throws Exception
	 */
	private TransferBVO[] createBill(TransferBVO[] bvos) throws Exception {
		List<TransferBVO> list = new ArrayList<TransferBVO>();// ������¹����ı�������
		for (int i = 0; i < bvos.length; i++) {
			// ȡ�����еļ����
			List<StockInvOnHandVO> li = mpick.get(i + "");
			// ���û���ִ��� ���б��ֲ���
			if (li == null || li.size() == 0) {
				list.add(bvos[i]);
			} else {
				// ���� ���ݼ���� ���¹��� ���β��к�ı���
				for (int j = 0; j < li.size(); j++) {
					TransferBVO vo = (TransferBVO) ObjectUtils
							.serializableClone(bvos[i]);
					vo.setVbatchcode(li.get(j).getWhs_batchcode());// ��������
					vo.setVuserdef7(getDate(li.get(j).getWhs_batchcode()));// ������������
					vo.setVuserdef9(li.get(j).getSs_pk());// ���ô��״̬
					vo.setVuserdef8(li.get(j).getPplpt_pk());// ���û���id
					vo.setAttributeValue("nshouldoutassistnum", li.get(j)
							.getAttributeValue("whs_omnum"));// ����Ӧ��������
					vo.setAttributeValue("noutassistnum", li.get(j)
							.getAttributeValue("whs_oanum"));// ����ʵ��������
					list.add(vo);
				}
			}
		}
		throw new StockException(list.toArray(new TransferBVO[0]));
	}

	/**
	 * ���ݼ���� ���¹������ⵥ������Ϣ
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-6-20����10:43:38
	 * @return
	 * @throws Exception
	 */
	private TbOutgeneralBVO[] createBill(TbOutgeneralBVO[] bvos)
			throws Exception {
		List<TbOutgeneralBVO> list = new ArrayList<TbOutgeneralBVO>();// ������¹����ı�������
		for (int i = 0; i < bvos.length; i++) {
			// ȡ�����еļ����
			List<StockInvOnHandVO> li = mpick.get(i + "");
			// ���û���ִ��� ���б��ֲ���
			if (li == null || li.size() == 0) {
				list.add(bvos[i]);
			} else {
				// ���� ���ݼ���� ���¹��� ���β��к�ı���
				for (int j = 0; j < li.size(); j++) {
					TbOutgeneralBVO vo = (TbOutgeneralBVO) ObjectUtils
							.serializableClone(bvos[i]);
					vo.setVbatchcode(li.get(j).getWhs_batchcode());// ��������
					vo.setVuserdef7(getDate(li.get(j).getWhs_batchcode()));// ������������
					vo.setVuserdef9(li.get(j).getSs_pk());// ���ô��״̬
					vo.setVuserdef8(li.get(j).getPplpt_pk());// ���û���id
					vo.setAttributeValue("nshouldoutassistnum", li.get(j)
							.getAttributeValue("whs_omnum"));// ����Ӧ��������
					vo.setAttributeValue("noutassistnum", li.get(j)
							.getAttributeValue("whs_oanum"));// ����ʵ��������
					list.add(vo);
				}
			}
		}
		throw new StockException(list.toArray(new TbOutgeneralBVO[0]));
	}

	/**
	 * ��������ʧЧ����
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-6-28����02:07:45
	 * @param whs_batchcode
	 * @param row
	 * @throws UifException
	 */
	private String getDate(String va) {
		// ������κ������ʽ��ȷ�͸��������ڸ�ֵ
		String year = va.substring(0, 4);
		String month = va.substring(4, 6);
		String day = va.substring(6, 8);
		String startdate = year + "-" + month + "-" + day;
		return startdate;
	}

	/**
	 * ǰ̨����Զ�̵��� (��ѯ���ݿ�)
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ ���м�� ���� �ֿ� ��λ ��� ��ѯ�ִ������ ���������� ��������
	 * @ʱ�䣺2012-6-20����10:04:23
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param i
	 * @param tbOutgeneralBVO
	 * @throws Exception
	 */
	private void pick(String pk_stordoc, String pk_cargdoc, TransferBVO vo,
			int i) throws Exception {
		//��������ʾ��Ϣ ���
		vo.setVuserdef14(null);
		// ������ѯ����
		String whereSql = " pk_customize1 = '" + pk_stordoc + "' "
				+ " and  pk_cargdoc = '" + vo.getPk_defdoc2() + "' "
				+ " and pk_invmandoc='" + vo.getCinventoryid() + "'"
				+ " and isnull(dr,0)=0 " + " and pk_corp='"
				+ SQLHelper.getCorpPk() + "'" + " and whs_stockpieces >0 ";// �������������0
		String vbantcode = PuPubVO.getString_TrimZeroLenAsNull(vo
				.getVbatchcode());// ������κ�
		String sspk = PuPubVO.getString_TrimZeroLenAsNull(vo.getVuserdef9());// ��ô��״̬

		String cdtpk = PuPubVO.getString_TrimZeroLenAsNull(vo.getVuserdef8());// ��ȡ����id

		if (sspk != null) {
			whereSql = whereSql + " and ss_pk='" + sspk + "'";
		}
		if (vbantcode != null) {
			whereSql = whereSql + " and whs_batchcode='" + vbantcode + "'";
		}
		if (cdtpk != null) {
			whereSql = whereSql + " and pplpt_pk='" + cdtpk + "'";

		}
		// ��ѯ�ִ���
		StockInvOnHandVO[] stocks = (StockInvOnHandVO[]) getStock().queryStock(
				whereSql);
		// ��ʼ���
		// ������� ��������
		spiltNum(stocks, vo, i);
	}

	/**
	 * ǰ̨����Զ�̵��� (��ѯ���ݿ�)
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ ���м�� ���� �ֿ� ��λ ��� ��ѯ�ִ������ ���������� ��������
	 * @ʱ�䣺2012-6-20����10:04:23
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param i
	 * @param tbOutgeneralBVO
	 * @throws Exception
	 */
	private void pick(String pk_stordoc, String pk_cargdoc, TbOutgeneralBVO vo,
			int i) throws Exception {
		//��������ʾ��Ϣ ���
		vo.setVuserdef14(null);
		// ������ѯ����
		String whereSql = " pk_customize1 = '" + pk_stordoc + "' "
				+ " and  pk_cargdoc = '" + pk_cargdoc + "' "
				+ " and pk_invmandoc='" + vo.getCinventoryid() + "'"
				+ " and isnull(dr,0)=0 " + " and pk_corp='"
				+ SQLHelper.getCorpPk() + "'" + " and whs_stockpieces >0 ";// �������������0
		String vbantcode = PuPubVO.getString_TrimZeroLenAsNull(vo
				.getVbatchcode());// ������κ�
		String sspk = PuPubVO.getString_TrimZeroLenAsNull(vo.getVuserdef9());// ��ô��״̬
		String cdtpk = PuPubVO.getString_TrimZeroLenAsNull(vo.getVuserdef8());// ��ȡ����id

		if (sspk != null) {
			whereSql = whereSql + " and ss_pk='" + sspk + "'";
		}
		if (vbantcode != null) {
			whereSql = whereSql + " and whs_batchcode='" + vbantcode + "'";
		}
		if (cdtpk != null) {
			whereSql = whereSql + " and pplpt_pk='" + cdtpk + "'";

		}
		// ��ѯ�ִ���
		StockInvOnHandVO[] stocks = (StockInvOnHandVO[]) getStock().queryStock(
				whereSql);
		sortout(stocks);//���ⰴ ������С����  �����ܱ�����С��������
		// ��ʼ���
		// ������� ��������
		spiltNum(stocks, vo, i);
	}
    /**
     * ���ⰴ ������С����  �����ܱ�����С��������
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ 
     * @ʱ�䣺2012-9-13����11:18:39
     * @param stocks
     * @throws BusinessException 
     */
	private void sortout(StockInvOnHandVO[] stocks) throws BusinessException {
       if(stocks==null || stocks.length==0)
    	   return ;
       //���û��ܱ���
       for(int i=0;i<stocks.length;i++){
    	   String cdtpk=stocks[i].getPplpt_pk();
    	   String cdtcode=(String) ZmPubTool.execFomular("code->getColValue(bd_cargdoc_tray,cdt_traycode,cdt_pk,cdt_pk)", new String[]{"cdt_pk"}, new String[]{cdtpk});
    	   stocks[i].setWhs_customize3(cdtcode);
       }
       VOUtil.ascSort(stocks,new String[]{"whs_batchcode","whs_customize3"});//�����κ�  �ͻ��ܱ�������	
	}

	/**
	 * �������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-6-20����10:25:33
	 * @param stocks
	 *            �ִ���
	 * @param vo
	 *            ���ⵥ����
	 * @throws Exception
	 */
	private void spiltNum(StockInvOnHandVO[] vos, TransferBVO vo, int index)
			throws Exception {
		StringBuffer error = new StringBuffer();// ���������� ������ʾ��Ϣ
		UFDouble zbnum = PuPubVO.getUFDouble_NullAsZero(vo
				.getNshouldoutassistnum());// ȡ�ó��ⵥӦ��������
		UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(vo.getNoutnum());// ���ʵ������
		if (vos == null || vos.length == 0) {
			mpick.put("" + index, null);// ����ִ���Ϊ�� ����м��������Ϊ��
			error.append(" û���ִ��� &&");
			vo.setVuserdef14(error.toString());
			return;
		}
		if (zbnum.doubleValue() == 0) {
			mpick.put("" + index, null);// ������ⵥӦ��������Ϊ0 ����м��������Ϊ��
			error.append(" Ӧ����������Ϊ�� &&");
			vo.setVuserdef14(error.toString());
			return;
		}
		if (noutnum.doubleValue() > 0) {
			mpick.put("" + index, null);// ������ⵥʵ��������ֵ ���ٲ��� �Զ���� ����м��������Ϊ��
			error.append("����ʵ���������ܲ����Զ���� ");
			vo.setVuserdef14(error.toString());
			return;
		}
		// ���з���
		// �� ���κ� ��С���� ���η���
		List<StockInvOnHandVO> list = new ArrayList<StockInvOnHandVO>();
		for (int i = 0; i < vos.length; i++) {
			UFDouble bnum = PuPubVO.getUFDouble_NullAsZero(vos[i]
					.getAttributeValue("whs_stockpieces"));
			if (zbnum.doubleValue() > bnum.doubleValue()) {
				if (i == vos.length - 1) {
					vos[i].setAttributeValue("whs_omnum", zbnum);// ����Ӧ������(������)
					vos[i].setAttributeValue("whs_oanum", bnum);// ����ʵ������
				} else {
					zbnum = zbnum.sub(bnum);
					vos[i].setAttributeValue("whs_omnum", bnum);// ����Ӧ������(������)
					vos[i].setAttributeValue("whs_oanum", bnum);// ����ʵ������(������)
				}
				list.add(vos[i]);
			} else if (zbnum.doubleValue() < bnum.doubleValue()) {
				vos[i].setAttributeValue("whs_omnum", zbnum);// ����Ӧ������ (������)
				vos[i].setAttributeValue("whs_oanum", zbnum);// ����ʵ������(������)
				list.add(vos[i]);
				break;
			} else {
				vos[i].setAttributeValue("whs_omnum", zbnum);// ����Ӧ������ (������)
				vos[i].setAttributeValue("whs_oanum", zbnum);// ����ʵ������(������)
				list.add(vos[i]);
				break;
			}
		}
		mpick.put(index + "", list);
		updateStock(list);
	}

	/**
	 * �������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-6-20����10:25:33
	 * @param stocks
	 *            �ִ���
	 * @param vo
	 *            ���ⵥ����
	 * @throws Exception
	 */
	private void spiltNum(StockInvOnHandVO[] vos, TbOutgeneralBVO vo, int index)
			throws Exception {
        StringBuffer error=new StringBuffer();//���������� ������ʾ��Ϣ
		UFDouble zbnum = PuPubVO.getUFDouble_NullAsZero(vo
				.getNshouldoutassistnum());// ȡ�ó��ⵥӦ��������
		UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(vo.getNoutnum());// ���ʵ������
		if (vos == null || vos.length == 0) {
			mpick.put("" + index, null);// ����ִ���Ϊ�� ����м��������Ϊ��
			error.append(" û���ִ��� &&");
			vo.setVuserdef14(error.toString());
			return;
		}
		if (zbnum.doubleValue() == 0) {
			mpick.put("" + index, null);// ������ⵥӦ��������Ϊ0 ����м��������Ϊ��
			error.append(" Ӧ����������Ϊ�� &&");
			vo.setVuserdef14(error.toString());
			return;
		}
		if (noutnum.doubleValue() > 0) {
			mpick.put("" + index, null);// ������ⵥʵ��������ֵ ���ٲ��� �Զ���� ����м��������Ϊ��
			error.append("����ʵ���������ܲ����Զ���� ");
			vo.setVuserdef14(error.toString());
			return;
		}
		// ���з���
		// �� ���κ� ��С���� ���η���
		List<StockInvOnHandVO> list = new ArrayList<StockInvOnHandVO>();
		for (int i = 0; i < vos.length; i++) {
			UFDouble bnum = PuPubVO.getUFDouble_NullAsZero(vos[i]
					.getAttributeValue("whs_stockpieces"));
			if (zbnum.doubleValue() > bnum.doubleValue()) {
				if (i == vos.length - 1) {
					vos[i].setAttributeValue("whs_omnum", zbnum);// ����Ӧ������(������)
					vos[i].setAttributeValue("whs_oanum", bnum);// ����ʵ������
				} else {
					zbnum = zbnum.sub(bnum);
					vos[i].setAttributeValue("whs_omnum", bnum);// ����Ӧ������(������)
					vos[i].setAttributeValue("whs_oanum", bnum);// ����ʵ������(������)
				}
				list.add(vos[i]);
			} else if (zbnum.doubleValue() < bnum.doubleValue()) {
				vos[i].setAttributeValue("whs_omnum", zbnum);// ����Ӧ������ (������)
				vos[i].setAttributeValue("whs_oanum", zbnum);// ����ʵ������(������)
				list.add(vos[i]);
				break;
			} else {
				vos[i].setAttributeValue("whs_omnum", zbnum);// ����Ӧ������ (������)
				vos[i].setAttributeValue("whs_oanum", zbnum);// ����ʵ������(������)
				list.add(vos[i]);
				break;
			}
		}
		mpick.put(index + "", list);
		updateStock(list);
	}

	/**
	 * �������ִ���
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-7-23����04:38:38
	 * @param list
	 * @throws Exception
	 */
	private void updateStock1(List<StockInvOnHandVO> lis) throws Exception {
		List<StockInvOnHandVO> list = (List<StockInvOnHandVO>) ObjectUtils
				.serializableClone(lis);
		if (list == null || list.size() == 0)
			return;
		for (int i = 0; i < list.size(); i++) {
			StockInvOnHandVO vo = list.get(i);
			if (vo == null)
				return;
			UFDouble uf1 = PuPubVO.getUFDouble_NullAsZero(vo.getWhs_oanum());
			vo.setWhs_stockpieces(uf1);
		}
		getStock().updateStock(list.toArray(new StockInvOnHandVO[0]));
	}

	/**
	 * ��������ִ���
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-7-23����04:38:38
	 * @param list
	 * @throws Exception
	 */
	private void updateStock(List<StockInvOnHandVO> lis) throws Exception {
		List<StockInvOnHandVO> list = (List<StockInvOnHandVO>) ObjectUtils
				.serializableClone(lis);
		if (list == null || list.size() == 0)
			return;
		for (int i = 0; i < list.size(); i++) {
			StockInvOnHandVO vo = list.get(i);
			if (vo == null)
				return;
			UFDouble uf1 = PuPubVO.getUFDouble_NullAsZero(vo.getWhs_oanum());
			vo.setWhs_stockpieces(new UFDouble(0).sub(uf1));
		}
		getStock().updateStock(list.toArray(new StockInvOnHandVO[0]));
	}

	/**
	 * �������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ zhf modify���״̬���������⴦�� ����֧��
	 * @ʱ�䣺2012-6-20����11:08:10
	 * @param vos
	 * @param row
	 * @param zbnum
	 */
	public void spiltNum(List<StockInvOnHandVO> vos, int row, UFDouble zbnum,
			boolean isfromic) {
		// List<StockInvOnHandVO> list=new ArrayList<StockInvOnHandVO>();
		int len = vos.size();
		for (int i = 0; i < len; i++) {
			UFDouble bnum = PuPubVO.getUFDouble_NullAsZero(vos.get(i)
					.getAttributeValue("whs_stockpieces"));

			// Ϊzhf���״̬���������⴦�� ����֧��
			if (PuPubVO.getUFDouble_NullAsZero(zbnum).equals(new UFDouble(0.0))
					&& !isfromic) {
				vos.get(i).setAttributeValue("whs_omnum", bnum);// ����Ӧ������ //
				// (������)
				vos.get(i).setAttributeValue("whs_oanum", bnum);// ����ʵ������(������)
				continue;
			}

			if (zbnum.doubleValue() > bnum.doubleValue()) {
				if (i == len - 1) {
					vos.get(i).setAttributeValue("whs_omnum", zbnum);// ����Ӧ������
					// //
					// (������)
					vos.get(i).setAttributeValue("whs_oanum", bnum);// ����ʵ������(������)
				} else {
					zbnum = zbnum.sub(bnum);
					vos.get(i).setAttributeValue("whs_omnum", bnum);// ����Ӧ������ //
					// (������)
					vos.get(i).setAttributeValue("whs_oanum", bnum);// ����ʵ������(������)
				}
			} else {
				vos.get(i).setAttributeValue("whs_omnum", zbnum);// ����Ӧ������ (������)
				vos.get(i).setAttributeValue("whs_oanum", zbnum);// ����ʵ������(������)
				break;
			}
		}
	}

	/**
	 * �÷���ǰ̨ ����Զ�̵��� (��ѯ���ݿ�)
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 1 ���� �ֿ� ��λ ��� ��ѯ�ִ��� �����Ƚ��ȳ�ԭ�� ���
	 * 
	 *             2������� �γɼ���� ��������� ��һ��map key=���ⵥ����id ��value=�ִ���vo
	 * 
	 *             3���¹�������� ���ⵥ����
	 * 
	 *             4 ��������
	 * @ʱ�䣺2012-6-20����09:56:08
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param bvos
	 * @return
	 * @throws Exception
	 */
	public TransferBVO[] autoPick1(String pk_stordoc, String pk_cargdoc,
			TransferBVO[] bvos) throws Exception {
		if (pk_stordoc == null || pk_stordoc.length() == 0)
			throw new Exception("����ֿ�Ϊ��");
		if (pk_cargdoc == null || pk_cargdoc.length() == 0)
			throw new Exception("�����λΪ��");
		// ��ռ����
		mpick.clear();
		if (bvos == null || bvos.length == 0)
			return null;
		for (int i = 0; i < bvos.length; i++) {
			//��ͷ�� Ĭ�ϻ�λ  �������ʱ  Ӧ�� ������ʵ�ʻ�λ���
			String pk_car=PuPubVO.getString_TrimZeroLenAsNull(bvos[i].getPk_defdoc2());
			if(pk_car!=null){
				pk_cargdoc=pk_car;
			}
			pick(pk_stordoc, pk_cargdoc, bvos[i], i);
		}
		return createBill(bvos);
	}

	/**
	 * �������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-6-20����11:08:10
	 * @param vos
	 * @param row
	 * @param zbnum
	 */
	public void spiltNum(List<StockInvOnHandVO> vos, int row, UFDouble zbnum) {
		spiltNum(vos, row, zbnum, true);
	}
}
