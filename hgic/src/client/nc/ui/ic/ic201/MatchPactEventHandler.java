package nc.ui.ic.ic201;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.ui.hg.ic.pub.HgICPubHealper;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.bill.BillModel;
import nc.vo.hg.ic.ic201.PactVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.BillRowNoVO;
import nc.vo.scm.pub.smart.ObjectUtils;

public class MatchPactEventHandler implements ActionListener{
	
	private ToftPanel tp = null;
	private MatchPactDLG parent = null;
	private GeneralBillVO tmpgbillvo = new GeneralBillVO();
	private Map<String, UFDateTime> pactTsInfor = new HashMap<String, UFDateTime>();
	
	
	public MatchPactEventHandler(ToftPanel tp,MatchPactDLG dlg){
		super();
		this.tp = tp;
		parent = dlg;
	}
	
	public GeneralBillVO getNewGbillvo(){
		return tmpgbillvo;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try{
			if(e.getSource()==parent.getBnOk()){
				onOK();
			}else if(e.getSource() == parent.getBnCancel()){
				onCancel();
			}else if(e.getSource() == parent.getBnMatch()){
				onMatch();
			}else if(e.getSource() == parent.getBnCanMatch()){
				onCancelMatch();
			}
			else if(e.getSource() == parent.getBnAutoMatch()){
				onAutoMathc();
			}
		}catch(Exception ee){
			ee.printStackTrace();
			showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(ee.getMessage()));
		}
		
	}
	
	private void onMatch() throws Exception{
		//У��  ������� �Ƿ� ȫ��ƥ��
		//��ƥ��������  matchinfor
		//����pactinfor ��  ��  ��ͬ�Ŀ�ʹ������
		//���±���  ���� �� δ����ƥ��ĺ�ͬ�� ���


		GeneralBillItemVO gitem = parent.getCurGitem();
		if(gitem==null)
			return;
		//У������    
		Object o = getMatchNum(PuPubVO.getUFDouble_NullAsZero(gitem.getAttributeValue(HgPubConst.NUM_DEF_QUA)));

		if(o == null)
			return;
		Object[] os = (Object[])o;

		List<PactVO> lmatch = null;
		List<PactVO> lpact = null;
		if(os[0]!=null){
			lmatch = (List<PactVO>)os[0];
			parent.getMatchInfor().put(gitem.getPrimaryKey(), lmatch.toArray(new PactVO[0]));
		}

		if(os[1]!=null){
			lpact = (List<PactVO>)os[1];
			parent.getPactInfor().put(gitem.getCinvbasid(), lpact.toArray(new PactVO[0]));
		}
		getPactPanel().setBodyDataVO(parent.getMatchInfor().get(gitem.getPrimaryKey()));
		getPactPanel().execLoadFormula();
		gitem.setAttributeValue("ismatch", UFBoolean.TRUE);
		parent.setMatchNumEditEnable(false);
		parent.setButtonEdit(true, false);
	}
	private void onCancelMatch() throws Exception {

		GeneralBillItemVO gitem = parent.getCurGitem();
		if (gitem == null)
			return;
		if (!PuPubVO.getUFBoolean_NullAs(gitem.getAttributeValue("ismatch"),
				UFBoolean.FALSE).booleanValue())
			throw new ValidationException("δƥ���ͬ");

		PactVO[] pactvos = HgICPubHealper.combinMatchPactVOs(parent
				.getMatchInfor().get(gitem.getPrimaryKey()), parent
				.getPactInfor().get(gitem.getCinvbasid()));

		parent.getPactInfor().put(gitem.getCinvbasid(), pactvos);
		parent.getMatchInfor().remove(gitem.getPrimaryKey());

		getPactPanel().setBodyDataVO(pactvos);
		getPactPanel().execLoadFormula();
		gitem.setAttributeValue("ismatch", UFBoolean.FALSE);
		parent.setMatchNumEditEnable(true);
		parent.setButtonEdit(false, false);
	}
	
	private Object getMatchNum(UFDouble nnum) throws Exception{
		//		List<PactVO> lpvo = new ArrayList<PactVO>();
		CircularlyAccessibleValueObject [] vos  = getPactPanel().getBodyValueVOs(PactVO.class.getName());
		if(vos==null||vos.length==0){
			showErrorMessage("�޿�ƥ���ͬ");
			return null;
		}

		PactVO[] pvos = (PactVO[])vos;

		List<PactVO> lmatch = new ArrayList<PactVO>();
		List<PactVO> lpact = new ArrayList<PactVO>();

		UFDouble  nsumnum = UFDouble.ZERO_DBL;
		PactVO pvo2 = null;
		for(PactVO pvo:pvos){
			if(PuPubVO.getUFDouble_NullAsZero(pvo.getNnum()).equals(UFDouble.ZERO_DBL)){
				lpact.add(pvo);
				continue;
			}

			pvo2 = (PactVO)ObjectUtils.serializableClone(pvo);
			pvo2.setNusenum(pvo2.getNnum());
			lmatch.add(pvo2);

			if(!PuPubVO.getUFDouble_NullAsZero(pvo.getNusenum()).equals(UFDouble.ZERO_DBL)){
//				pvo.setNusenum(PuPubVO.getUFDouble_NullAsZero(pvo.getNordernum()).sub(PuPubVO.getUFDouble_NullAsZero(pvo.getNusenum())));
				pvo.setNnum(UFDouble.ZERO_DBL);
				lpact.add(pvo);
			}

			nsumnum = nsumnum.add(pvo2.getNnum());
		}

		if(nnum.equals(nsumnum))
			return new Object[]{lmatch,lpact};

		throw new ValidationException("�������δƥ���꣬δƥ������Ϊ"+nnum.sub(nsumnum).toString());

	}
	
	private void onAutoMathc() throws Exception{
		GeneralBillItemVO gitem = parent.getCurGitem();
		if(gitem==null)
			return;
		
		UFDouble nallnum = PuPubVO.getUFDouble_NullAsZero(gitem.getAttributeValue(HgPubConst.NUM_DEF_QUA));
		if(nallnum.equals(UFDouble.ZERO_DBL)){
			showErrorMessage("�����ϸ�����Ϊ0������Ҫƥ��");
			return;
		}
		
		int rowcount = getPactPanel().getRowCount();
		if(rowcount<=0){
			showErrorMessage("�޴�ƥ���ͬ");
			return;
		}
		
//		UFDouble nmatchnum = UFDouble.ZERO_DBL;
		UFDouble nusenum = null;
		for(int i=0;i<rowcount;i++){
			nusenum = PuPubVO.getUFDouble_NullAsZero(getPactPanel().getValueAt(i, "nusenum"));
			if(nusenum.equals(UFDouble.ZERO_DBL))
				continue;
			if(nusenum.doubleValue()>=nallnum.doubleValue()){
				getPactPanel().setValueAt(nusenum.sub(nallnum), i, "nusenum");
				getPactPanel().setValueAt(nallnum, i, "nnum");
				break;
			}			
			
			getPactPanel().setValueAt(UFDouble.ZERO_DBL, i, "nusenum");
			getPactPanel().setValueAt(nusenum, i, "nnum");
			nallnum = nallnum.sub(nusenum);
		}
			
			
		onMatch();
	}
	
	private void onOK() throws Exception{//��ƥ��������
		//У���Ƿ�ƥ����ȫ     �Ƿ����һ��ȫ��ƥ���ꣿ��������      ���� ����ƥ��
		if(parent.getMatchInfor().size()==0){
			showWarnMessage("δƥ���ͬ");
//			parent.closeOK();
			return;
		}
		GeneralBillHeaderVO head =parent.getCurGHeader();
		GeneralBillItemVO[] items = parent.getAllGitems();
		
		//����ƥ����Ϣ  ������н��в���
		List<GeneralBillItemVO> litem = new ArrayList<GeneralBillItemVO>();
		PactVO[] tmppvos = null;
		List<GeneralBillItemVO> ltmp = null;	
		String corp = head.getPk_corp();
		String calbody =head.getPk_calbody();
		for(GeneralBillItemVO item:items){
			item.setPk_bodycalbody(calbody);
			item.setAttributeValue("pk_corp",corp);
			tmppvos = parent.getMatchInfor().get(item.getPrimaryKey());
			if(tmppvos == null||tmppvos.length==0)
				continue;
			ltmp = HgICPubHealper.splitGItemsOnPactMatch(item, tmppvos);
			if(ltmp==null||ltmp.size()==0)
				continue;
			litem.addAll(ltmp);
		}			
		
		if(litem.size()==0)
			return;
		
		parent.getCurGHeader().setStatus(VOStatus.UNCHANGED);
		parent.getCurGHeader().setTempValue("pacttsinfor", pactTsInfor);
		parent.getCurGHeader().setTempValue("cuserid", ClientEnvironment.getInstance().getUser().getPrimaryKey());
		tmpgbillvo.setParentVO(parent.getCurGHeader());
		GeneralBillItemVO[] bodys = litem.toArray(new GeneralBillItemVO[0]);
		BillRowNoVO.setVOsRowNoByRule(bodys, ScmConst.m_purchaseIn, "crowno");
		tmpgbillvo.setChildrenVO(bodys);
		
		//����  ��������
		Object o = HgICPubHealper.splitGItemsOnPactMatch(tmpgbillvo);
		if(o == null){
			showErrorMessage("�����쳣");
			return;
		}
		
		Object[] os = (Object[])o;
		String headts = PuPubVO.getString_TrimZeroLenAsNull(os[0]);
//		generalbilli
		
		tmpgbillvo.setChildrenVO((GeneralBillItemVO[])os[1]);
		tmpgbillvo.getHeaderVO().setTs(headts);

		parent.closeOK();
	}
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��������Ӧ�̶�Ӧ����ĺ�ͬ����Ϣ  ��װ��pactvo��
	 * 2010-12-9����10:24:42
	 * @param cvendormangid
	 * @param items
	 * @return
	 * @throws Exception
	 */
	public Map<String,PactVO[]> loadPactInfor(String cvendormangid,GeneralBillItemVO[] items) throws Exception{
		if(PuPubVO.getString_TrimZeroLenAsNull(cvendormangid)==null||items == null||items.length == 0){
			return null;
		}
		
		List<String> linvman = new ArrayList<String>();
		for(GeneralBillItemVO item:items){
			if(linvman.contains(item.getCinvmanid()))
				continue;
			linvman.add(item.getCinvmanid());			
		}
		if(linvman.size()==0)
			return null;
		String[] cinvmanids = linvman.toArray(new String[0]);
		PactVO[] pacts = HgICPubHealper.queryPactsByVendor(tp,parent.getCurGHeader().getCbiztypeid(),cvendormangid, cinvmanids);
		if(pacts == null || pacts.length == 0)
			return null;
		Map<String,Object> infor = new HashMap<String,Object>();
		pactTsInfor.clear();
		List ldata = null;
		for(PactVO pvo:pacts){
		        pvo.setNusenum(pvo.getNordernum().sub(PuPubVO.getUFDouble_NullAsZero(pvo.getNaccumstorenum())));
		        if(infor.containsKey(pvo.getCbaseid()))
					ldata = (List)infor.get(pvo.getCbaseid());
				else
					ldata = new ArrayList();
				ldata.add(pvo);
				infor.put(pvo.getCbaseid(), ldata);
				pactTsInfor.put(pvo.getPrimaryKey(), pvo.getTs());
				
				
			}
		
		Map<String,PactVO[]> infor2 = new HashMap<String, PactVO[]>();
		for(String key:infor.keySet()){
			ldata = (List)infor.get(key);
			infor2.put(key, (PactVO[])ldata.toArray(new PactVO[0]));
		}
		
		infor = null;//�ͷ�
		
		return infor2;
	}
	
	private void onCancel() throws Exception{
		parent.closeCancel();
	}
	private BillModel getPactPanel(){
		return parent.getBillListPanel().getBodyBillModel();
	}
	
	private void showErrorMessage(String msg){
		parent.showErrorMessage(msg);
	}
	private void showWarnMessage(String msg){
		parent.showWarningMessage(msg);
	}
	private void showHintMessage(String msg){
		parent.showHintMessage(msg);
	}

}
