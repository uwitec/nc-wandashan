package nc.ui.to.pubtransfer;

/**
 * �������ڣ�(2004-2-10 9:13:55) ���ߣ����˾� ˵���� 2����ť���塣 3����ť״̬���ơ�
 * 4��������صİ�ť����public��tpֱ�ӷ��ʡ��᲻�������⣿ ���������Է�ֹ������get������
 */

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.scm.extend.IFuncExtend;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.ctrl.GenEditConst;
import nc.ui.to.pub.btn.ITOButtonConst;
import nc.ui.to.pub.ctrl.GenPageBtnCtrl;
import nc.ui.to.pub.plugin.ToPluginButtonMenu;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessException;
import nc.vo.to.pub.ConstVO;

public class ButtonCtrl {

  public ToftPanel m_tp;

  /**
   * �����ǰ�ť������initButton()��ʼ����
   */
  protected ButtonObject m_boBusiType;// ҵ������

  public ButtonObject m_boNew;// ����

  public ButtonObject m_boSave;// ����
  // ά���������޸ģ�ȡ����ɾ��,����

  protected ButtonObject m_boMaintain;

  public ButtonObject m_boUpdate;// �޸�

  public ButtonObject m_boCancel;// ȡ��

  protected ButtonObject m_boDelete;// ɾ��

  protected ButtonObject m_boCopy;// ����
  // �в��������У�ɾ�У������У�ճ���У�������

  protected ButtonObject m_boBillRowMng;

  protected ButtonObject m_boAddLine;// ����

  protected ButtonObject m_boDeleteLine;// ɾ��

  protected ButtonObject m_boInsertLine;// ������

  protected ButtonObject m_boCopyLine;// ������

  protected ButtonObject m_boPasteLine;// ճ����

  protected ButtonObject m_boNewRowNo; // �����к�

  protected ButtonObject m_boPasteLineToTail; // ճ���е���β

  protected ButtonObject m_boCardEdit; // ��Ƭ�༭

  protected ButtonObject m_boRefAddLine; // ��������

  protected ButtonObject m_boDRSQ;// ��������

  protected ButtonObject m_boAudit;// ����
  // ִ�У��������󣬹رգ��򿪣����ᣬ�ⶳ

  protected ButtonObject m_boExecute;

  public ButtonObject m_boSendAudit;// ����

  protected ButtonObject m_boCancelAudit;// ����

  protected ButtonObject m_boClose;// �ر�

  protected ButtonObject m_boOpen;// ��

  protected ButtonObject m_boFreeze;// ����

  protected ButtonObject m_boFresh;// �ⶳ

  public ButtonObject m_boQuery;// ��ѯ
  
  protected ButtonObject m_boRefresh;//ˢ��

  // ���:��λ����ҳ
  protected ButtonObject m_boBrowse;

  protected ButtonObject m_boLocate;// ��λ

  public GenPageBtnCtrl m_PageBtnCtrl;// ��ҳ

  // ��Ƭ��ʾ/�б���ʾ
  protected ButtonObject m_boSwitch;

  // ��ӡ��Ԥ�����ϲ���ʾ����ӡ
  protected ButtonObject m_boPrintMng;

  protected ButtonObject m_boPreview;// Ԥ��

  protected ButtonObject m_boBillCombin;// �ϲ���ʾ

  protected ButtonObject m_boPrint;// ��ӡ
  // �������ܣ��ĵ������˻أ����Ӳ������

  protected ButtonObject m_boAssistMng;

  protected ButtonObject m_boDocument;// �ĵ�����

  protected ButtonObject m_boReturn;// �˻�

  protected ButtonObject m_boStockLock;// ���Ӳ����

  protected ButtonObject m_boSelSettlePath;// ָ������·��

  protected ButtonObject m_boDelSettlePath;// ȡ������·��

  protected ButtonObject m_boSendPlan;// ��������

  protected ButtonObject m_boFillPlan;// ��������

  // ������ѯ�����飬ATP,�ִ���������״̬��ѯ��������ʾ������
  protected ButtonObject m_boAssistQuery;

  protected ButtonObject m_boJointCheck;// ����

  protected ButtonObject m_boAllATP;

  protected ButtonObject m_boAllSP;

  protected ButtonObject m_boAuditStatus;// ����״̬��ѯ

  public ButtonObject m_boOnHandShowHidden; // �ִ�����ʾ/����
  
  public ButtonObject m_stockNumPara;//zhf add ������ˢ��

  // ����ת��
  protected ButtonObject m_boCancelRef;

  protected ButtonObject m_boAskPrice; // ѯ�۰�ť

  public ButtonTree m_boTree = null;

  protected IFuncExtend m_funcExtend = null;
  
  protected ButtonObject m_splitPrint;//�ֵ���ӡ

  /**
   * ButtonCtrl ������ע�⡣
   */
  public ButtonCtrl(
      ButtonTree bt, IFuncExtend funcExtend) {
    super();
    m_boTree = bt;
    m_funcExtend = funcExtend;
    initButtons();
  }

  // ������
  /**
   * ActionCtrl ������ע�⡣
   */
  // public ButtonCtrl(nc.ui.pub.ToftPanel tp,IFuncExtend funcExtend) {
  // super();
  // //����toftpanel���ã�������initButtons���á�
  // m_tp=tp;
  // m_funcExtend = funcExtend;
  // initButtons(tp,m_funcExtend);
  // }
  // /**
  // * �������ڣ�(2004-2-10 11:39:49)
  // * ���ߣ����˾�
  // * ������
  // * ���أ�
  // * ˵������ʼ����ť
  // * @deprecated
  // */
  // protected void initButtons(nc.ui.pub.ToftPanel tp,IFuncExtend funcExtend)
  // {
  //
  // m_boSave = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000001")/*@res
  // "����"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000001")/*@res
  // "����"*/, 2,"����"); /*-=notranslate=-*/
  // m_boSendAudit = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000024")/*@res
  // "����"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000024")/*@res
  // "����"*/, 2,"����"); /*-=notranslate=-*/
  // m_boCancel = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res
  // "ȡ��"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res
  // "ȡ��"*/, 2,"ȡ��"); /*-=notranslate=-*/
  // m_boQuery = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000006")/*@res
  // "��ѯ"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000006")/*@res
  // "��ѯ"*/, 2,"��ѯ"); /*-=notranslate=-*/
  // m_boAudit = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res
  // "����"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000238")/*@res
  // "��������"*/, 2,"����"); /*-=notranslate=-*/
  // m_boCancelAudit = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000028")/*@res
  // "����"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000210")/*@res
  // "ȡ������"*/, 2,"����"); /*-=notranslate=-*/
  // m_boAuditStatus = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000023")/*@res
  // "������״̬"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000023")/*@res
  // "������״̬"*/, 2,"������״̬"); /*-=notranslate=-*/
  // m_boReturn = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000025")/*@res
  // "�˻�"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000025")/*@res
  // "�˻�"*/, 2,"�˻�"); /*-=notranslate=-*/
  // /*/--------------------------------------------------------------------
  // m_pageBtn = new PageCtrlBtn(this);
  // m_pageBtn.getFirst().setName("I<");
  // m_pageBtn.getPre().setName("<");
  // m_pageBtn.getNext().setName(">");
  // m_pageBtn.getLast().setName(">I");
  // *///--------------------------------------------------------------------
  // //��ҳ�ؼ�
  // m_PageBtnCtrl = new GenPageBtnCtrl(tp);
  //
  // m_boBillMng = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000026")/*@res
  // "���ݲ���"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000074")/*@res
  // "����ά������"*/, 2,"���ݲ���"); /*-=notranslate=-*/
  // m_boNew = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000002")/*@res
  // "����"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPP40093010-000000")/*@res
  // "����һ�ŵ���"*/, 2,"����"); /*-=notranslate=-*/
  // m_boCopy = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000043")/*@res
  // "����"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000110")/*@res
  // "���Ƶ�ǰ����"*/, 2,"����"); /*-=notranslate=-*/
  // m_boUpdate = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000045")/*@res
  // "�޸�"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000045")/*@res
  // "�޸�"*/, 2,"�޸�"); /*-=notranslate=-*/
  // m_boDelete = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000039")/*@res
  // "ɾ��"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000039")/*@res
  // "ɾ��"*/, 2,"ɾ��"); /*-=notranslate=-*/
  // //--------------------------------------------------------------------
  // m_boStatusMng = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000027")/*@res
  // "״̬ά��"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000027")/*@res
  // "״̬ά��"*/,2,"״̬ά��"); /*-=notranslate=-*/
  // m_boClose = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000028")/*@res
  // "�ر�"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000120")/*@res
  // "�رյ���"*/,2,"�ر�"); /*-=notranslate=-*/
  // m_boOpen = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000029")/*@res
  // "��"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPP40093010-000001")/*@res
  // "�򿪵���"*/,2,"��"); /*-=notranslate=-*/
  // m_boFreeze = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000030")/*@res
  // "����"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPP40093010-000002")/*@res
  // "���ᵥ��"*/,2,"����"); /*-=notranslate=-*/
  // m_boFresh = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000031")/*@res
  // "�ⶳ"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPP40093010-000003")/*@res
  // "������ĵ��ݽⶳ"*/,2,"�ⶳ"); /*-=notranslate=-*/
  // //---------------------------------------------------------------------
  // m_boPrint = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000007")/*@res
  // "��ӡ"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000007")/*@res
  // "��ӡ"*/, 2,"��ӡ����"); /*-=notranslate=-*/
  // m_boPrintMng = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000030")/*@res
  // "��ӡ"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000030")/*@res
  // "��ӡ"*/, 2,"��ӡ"); /*-=notranslate=-*/
  // m_boPreview = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000032")/*@res
  // "Ԥ��"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000032")/*@res
  // "Ԥ��"*/, 2,"Ԥ��"); /*-=notranslate=-*/
  // //---------------------------------------------------------------------
  // m_boAssistMng = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000036")/*@res
  // "����"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000066")/*@res
  // "���ݵĸ��ָ�������"*/, 2,"����"); /*-=notranslate=-*/
  // m_boLocate = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000033")/*@res
  // "��λ"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000092")/*@res
  // "��λ��ָ������"*/, 2,"��λ"); /*-=notranslate=-*/
  // m_boSwitch = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000034")/*@res
  // "�л�"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000034")/*@res
  // "�л�"*/, 2,"�л�"); /*-=notranslate=-*/
  // // m_boAtp = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000035")/*@res
  // "������ѯ"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000035")/*@res
  // "������ѯ"*/, 2,"������ѯ"); /*-=notranslate=-*/
  // m_boOnHandShowHidden = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("topub","UPP4009-000027")/*@res
  // "�л�"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("topub","UPP4009-000027")/*@res
  // "�ִ�����ʾ/����"*/, 2,"������ʾ/����");/*-=notranslate=-*/
  // m_boAllATP = new ButtonObject("ATP",
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000283")/*@res
  // "��ʾ��ǰ��������п����֯��ATP"*/, 2,"ATP"); /*-=notranslate=-*/
  // m_boAllSP = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0002918")/*@res
  // "�ִ���"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000282")/*@res
  // "��ʾ��ǰ��������вֿ���ִ���"*/, 2,"�ִ���"); /*-=notranslate=-*/
  // m_boDocument = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000036")/*@res
  // "�ĵ�����"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000036")/*@res
  // "�ĵ�����"*/, 2,"�ĵ�����"); /*-=notranslate=-*/
  // m_boJointCheck = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000037")/*@res
  // "����"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000037")/*@res
  // "����"*/, 2,"����"); /*-=notranslate=-*/
  // m_boStockLock = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0001824")/*@res
  // "���Ӳ����"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0001824")/*@res
  // "���Ӳ����"*/, 2,"���Ӳ����"); /*-=notranslate=-*/
  // m_boBillCombin=new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("topub","UPT4009-000007"),nc.ui.ml.NCLangRes.getInstance().getStrByID("topub","UPT4009-000007"),2,"�ϲ���ʾ");
  // //----------------------------------------------------------------------
  // m_boBillRowMng = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPT40093010-000038")/*@res
  // "�в���"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010","UPP40093010-000004")/*@res
  // "�в���"*/, 2,"�в���"); /*-=notranslate=-*/
  // m_boAddLine = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000012")/*@res
  // "����"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000012")/*@res
  // "����"*/, 2,"����"); /*-=notranslate=-*/
  // m_boDeleteLine = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000013")/*@res
  // "ɾ��"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000013")/*@res
  // "ɾ��"*/, 2,"ɾ��"); /*-=notranslate=-*/
  // m_boCopyLine = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000014")/*@res
  // "������"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000014")/*@res
  // "������"*/, 2,"������"); /*-=notranslate=-*/
  // m_boPasteLine = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000015")/*@res
  // "ճ����"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000015")/*@res
  // "ճ����"*/, 2,"ճ����"); /*-=notranslate=-*/
  // m_boInsertLine = new
  // ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000016")/*@res
  // "������"*/,
  // nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000016")/*@res
  // "������"*/, 2,"������"); /*-=notranslate=-*/
  // //���ϲ㰴ť��ϣ�
  // m_boBillMng.addChildButton(m_boNew);
  // m_boBillMng.addChildButton(m_boCopy);
  // m_boBillMng.addChildButton(m_boUpdate);
  // m_boBillMng.addChildButton(m_boDelete);
  //
  // m_boStatusMng.addChildButton(m_boClose);
  // m_boStatusMng.addChildButton(m_boOpen);
  // m_boStatusMng.addChildButton(m_boFreeze);
  // m_boStatusMng.addChildButton(m_boFresh);
  //
  // m_boPrintMng.addChildButton(m_boPrint);
  // m_boPrintMng.addChildButton(m_boPreview);
  //
  // m_boAssistMng.addChildButton(m_boLocate);
  // m_boAssistMng.addChildButton(m_boSwitch);
  // // m_boAssistMng.addChildButton(m_boAtp);
  // m_boAssistMng.addChildButton(m_boOnHandShowHidden);
  // m_boAssistMng.addChildButton(m_boAllATP);
  // m_boAssistMng.addChildButton(m_boAllSP);
  // m_boAssistMng.addChildButton(m_boStockLock);
  // m_boAssistMng.addChildButton(m_boDocument);
  // m_boAssistMng.addChildButton(m_boJointCheck);
  // m_boAssistMng.addChildButton(m_boBillCombin);
  //
  // m_boBillRowMng.addChildButton(m_boAddLine);
  // m_boBillRowMng.addChildButton(m_boDeleteLine);
  // m_boBillRowMng.addChildButton(m_boCopyLine);
  // m_boBillRowMng.addChildButton(m_boPasteLine);
  // m_boBillRowMng.addChildButton(m_boInsertLine);
  //
  // //ȱʡ��
  // ButtonObject[] boThis =
  // new ButtonObject[] {
  // m_boSave,
  // m_boSendAudit,
  // m_boCancel,
  // m_boQuery,
  // m_boAudit,
  // m_boCancelAudit,
  // m_boAuditStatus,
  // m_boReturn,
  // m_PageBtnCtrl.getFirst(),
  // m_PageBtnCtrl.getPre(),
  // m_PageBtnCtrl.getNext(),
  // m_PageBtnCtrl.getLast(),
  // m_boBillMng,
  // m_boStatusMng,
  // m_boPrintMng,
  // m_boAssistMng,
  // m_boBillRowMng
  // };
  //
  // // ��ʼ����չ��----֧����չ��Ͱ�ť��
  // ButtonObject[] boExtend = null;
  // if(funcExtend != null)
  // {
  // boExtend = funcExtend.getExtendButton();
  // }
  //
  // if (boExtend != null && boExtend.length != 0)
  // {
  // //arraycopy(Object src, int srcPos, Object dest, int destPos,int length);
  // m_boaDefault = new ButtonObject[boThis.length + boExtend.length];
  // System.arraycopy(boThis,0,m_boaDefault,0,boThis.length);
  // System.arraycopy(boExtend,0,m_boaDefault,boThis.length,boExtend.length);
  // }
  // else
  // {
  // //arraycopy(Object src, int srcPos, Object dest, int destPos,int length);
  // m_boaDefault = new ButtonObject[boThis.length];
  // System.arraycopy(boThis,0,m_boaDefault,0,boThis.length);
  // }
  // //֧����չ��Ͱ�ť����
  // }
  protected void initButtons() {
    m_boBusiType = m_boTree.getButton(ITOButtonConst.BTN_BUSINESS_TYPE);// ҵ������
    m_boNew = m_boTree.getButton(ITOButtonConst.BTN_ADD);// ����
    m_boSave = m_boTree.getButton(ITOButtonConst.BTN_SAVE);// ����
    m_boMaintain = m_boTree.getButton(ITOButtonConst.BTN_BILL);// ά��
    m_boUpdate = m_boTree.getButton(ITOButtonConst.BTN_BILL_EDIT);// �޸�
    m_boCancel = m_boTree.getButton(ITOButtonConst.BTN_BILL_CANCEL);// ȡ��
    m_boDelete = m_boTree.getButton(ITOButtonConst.BTN_BILL_DELETE);// ɾ��
    m_boCopy = m_boTree.getButton(ITOButtonConst.BTN_BILL_COPY);// ����
    m_boBillRowMng = m_boTree.getButton(ITOButtonConst.BTN_LINE);// �в���
    m_boAddLine = m_boTree.getButton(ITOButtonConst.BTN_LINE_ADD);// ����
    m_boDeleteLine = m_boTree.getButton(ITOButtonConst.BTN_LINE_DELETE);// ɾ��
    m_boInsertLine = m_boTree.getButton(ITOButtonConst.BTN_LINE_INSERT);// ������

    m_boCopyLine = m_boTree.getButton(ITOButtonConst.BTN_LINE_COPY);// ������
    m_boPasteLine = m_boTree.getButton(ITOButtonConst.BTN_LINE_PASTE);// ճ����
    m_boPasteLineToTail = m_boTree
        .getButton(ITOButtonConst.BTN_LINE_PASTE_TAIL); // ճ���е���β
    m_boCardEdit = m_boTree.getButton(ITOButtonConst.BTN_CARD_EDIT); // ��Ƭ�༭
    m_boNewRowNo = m_boTree.getButton(ITOButtonConst.BTN_ADD_NEWROWNO); // �����к�
    m_boAskPrice = m_boTree.getButton(ITOButtonConst.BTN_ASKPRICE); // ѯ��
    m_boRefresh = m_boTree.getButton(ITOButtonConst.BTN_REFRESH);//ˢ��
    m_boRefAddLine = m_boTree.getButton(ITOButtonConst.BTN_REF_ADDLINE);// ��������
    m_boDRSQ = m_boTree.getButton(ITOButtonConst.BTN_DRSQ);// ��������

    m_boAudit = m_boTree.getButton(ITOButtonConst.BTN_AUDIT);// ����
    m_boExecute = m_boTree.getButton(ITOButtonConst.BTN_EXECUTE);// ִ��
    m_boSendAudit = m_boTree.getButton(ITOButtonConst.BTN_EXECUTE_AUDIT);// ����
    m_boCancelAudit = m_boTree
        .getButton(ITOButtonConst.BTN_EXECUTE_AUDIT_CANCEL);// ����
    m_boClose = m_boTree.getButton(ITOButtonConst.BTN_EXECUTE_CLOSE);// �ر�
    m_boOpen = m_boTree.getButton(ITOButtonConst.BTN_EXECUTE_OPEN);// ��
    m_boFreeze = m_boTree.getButton(ITOButtonConst.BTN_EXECUTE_FREEZE);// ����
    m_boFresh = m_boTree.getButton(ITOButtonConst.BTN_EXECUTE_FREEZE_CANCEL);// �ⶳ
    m_boQuery = m_boTree.getButton(ITOButtonConst.BTN_QUERY);// ��ѯ
    m_boBrowse = m_boTree.getButton(ITOButtonConst.BTN_BROWSE);// ���
    m_boLocate = m_boTree.getButton(ITOButtonConst.BTN_BROWSE_LOCATE);// ��λ
    m_boSwitch = m_boTree.getButton(ITOButtonConst.BTN_SWITCH);// �л�
    m_boPrintMng = m_boTree.getButton(ITOButtonConst.BTN_PRINT);// ��ӡ����
    m_boPreview = m_boTree.getButton(ITOButtonConst.BTN_PRINT_PREVIEW);// Ԥ��
    m_boBillCombin = m_boTree.getButton(ITOButtonConst.BTN_COMBIN_SHOW);// �ϲ���ʾ
    m_boPrint = m_boTree.getButton(ITOButtonConst.BTN_PRINT_PRINT);// ��ӡ
    m_boAssistMng = m_boTree.getButton(ITOButtonConst.BTN_ASSIST_FUNC);// ��������
    m_boDocument = m_boTree.getButton(ITOButtonConst.BTN_ASSIST_FUNC_DOCUMENT);// �ĵ�����
    m_boReturn = m_boTree.getButton(ITOButtonConst.BTN_RETURN);// �˻�
    m_boStockLock = m_boTree.getButton(ITOButtonConst.BTN_STOCK_LOCK);// ���Ӳ����
    m_boSelSettlePath = m_boTree.getButton(ITOButtonConst.BTN_M_SELSETTLEPATH);// ָ������·��
    m_boDelSettlePath = m_boTree.getButton(ITOButtonConst.BTN_M_DELSETTLEPATH);// ȡ������·��
    m_boSendPlan = m_boTree.getButton(ITOButtonConst.BTN_SEND_PLAN);// ��������
    m_boFillPlan = m_boTree.getButton(ITOButtonConst.BTN_FILL_PLAN);// ��������
    m_boAssistQuery = m_boTree.getButton(ITOButtonConst.BTN_ASSIST_QUERY);// ������ѯ
    m_boJointCheck = m_boTree
        .getButton(ITOButtonConst.BTN_ASSIST_QUERY_RELATED);// ����
    m_splitPrint = m_boTree.getButton(ITOButtonConst.BTN_SPLITPRINT_PREVIEW);//�ֵ���ӡԤ��
    m_boAllATP = m_boTree.getButton(ITOButtonConst.BTN_ATP);// ������
    m_boAllSP = m_boTree.getButton(ITOButtonConst.BTN_AllSP);// �ִ���
    m_boAuditStatus = m_boTree
        .getButton(ITOButtonConst.BTN_ASSIST_QUERY_WORKFLOW);// ������״̬
    m_boOnHandShowHidden = m_boTree
        .getButton(ITOButtonConst.BTN_ASSIST_FUNC_ONHAND);// �ִ�����ʾ/����
    
//    zhf add
    m_stockNumPara = m_boTree
    .getButton(HgPuBtnConst.STOCK_PARA_REFRESH);// �ִ�����ʾ/����
    
    m_boCancelRef = m_boTree.getButton(ITOButtonConst.BTN_REF_CANCEL);// ����ת��
    m_boCancelRef.setVisible(false);// ���ð�ť���ɼ�
    // ��ҳ�ؼ�
    m_PageBtnCtrl = new GenPageBtnCtrl(m_boTree);

    // ��ʼ����չ��----֧����չ��Ͱ�ť��
    ButtonObject[] boExtend = null;
    if (m_funcExtend != null) {
      boExtend = m_funcExtend.getExtendButton();
    }

    try {
      if (boExtend != null) {
        ButtonObject boExtTop = m_boTree.getExtTopButton();
        m_boTree.addMenu(boExtTop);
        for (int i = 0; i < boExtend.length; i++) {
          if (boExtend != null)
            m_boTree.addChildMenu(boExtTop, boExtend[i]);
        }
      }
      m_boTree = ToPluginButtonMenu.getInstance().addUIPluginMenu(m_boTree,ConstVO.m_sBillTOORDER,"40093009");
    }
    catch (BusinessException be) {
      Debug.error(be.getMessage());
    }
    // ֧����չ��Ͱ�ť����
  }

  /**
   * �������ڣ�(2004-2-10 11:28:46) ���ߣ����˾� ������ int iMode,��ǰ�༭״̬�������������޸ġ������������޶��ȡ�
   * boolean bCanAddLine ���Ƿ�����еı�־ ���أ� ˵�������ñ༭ʱ�İ�ť״̬
   *
   * @param mode
   *          int
   */
  public void setEditStatus(int iMode, boolean bCanAddLine) {
    if (m_tp != null && m_tp instanceof IBillExtendFun) {
      ((IBillExtendFun) m_tp).setExtendBtnsStat(iMode);
    }
    switch (iMode) {
      case GenEditConst.NEW:
        setButtonStatusOfNew();
        break;
      case GenEditConst.UPDATE:
        setButtonStatusOfUpdate(bCanAddLine);
        break;
    }
  }

  /**
   * �������ڣ�(2004-2-10 11:28:46) ���ߣ�lisb ������ int iCurBillStatus
   * ,���ݵ�ǰҵ��״̬�����Ƹġ�ɾ�����ȡ� ���أ� ˵�������ð�ť״̬
   *
   * @param mode
   *          int
   */
  private void setBusinessStatus(int iCurBillStatus, String auditorId) {
    switch (iCurBillStatus) {
      // ����״̬�µĵ��ݣ����󣬶��ᣬ��,�ⶳ������
      case ConstVO.IBillStatus_FREE:
        m_boCopy.setEnabled(true);
        m_boUpdate.setEnabled(true);
        m_boDelete.setEnabled(true);
        m_boAudit.setEnabled(false);
        m_boCancelAudit.setEnabled(false);
        m_boSendAudit.setEnabled(true);
        m_boFreeze.setEnabled(false);
        m_boOpen.setEnabled(false);
        m_boClose.setEnabled(false);
        m_boFresh.setEnabled(false);
        m_boExecute.setEnabled(true);
        m_boReturn.setEnabled(false);
        m_boStockLock.setEnabled(false);
        break;

      // ����״̬�µĵ��ݣ����󣬶���,�رտ��ã���,�ⶳ������
      case ConstVO.IBillStatus_PASSCHECK:
        m_boCopy.setEnabled(true);
        m_boUpdate.setEnabled(false);
        m_boDelete.setEnabled(false);
        m_boReturn.setEnabled(true);
        m_boAudit.setEnabled(false);
        m_boCancelAudit.setEnabled(true);
        m_boOpen.setEnabled(false);
        m_boClose.setEnabled(true);
        m_boSendAudit.setEnabled(false);
        m_boFreeze.setEnabled(true);
        m_boFresh.setEnabled(false);
        m_boExecute.setEnabled(true);
        m_boStockLock.setEnabled(true);
       
        break;

      case ConstVO.IBillStatus_UNCHECKED: // ����δͨ���ĵ���ֻ�ܽ����޸ģ�����
        m_boCopy.setEnabled(true);
        m_boUpdate.setEnabled(true);
        m_boDelete.setEnabled(true);
        m_boSendAudit.setEnabled(false);
        m_boAudit.setEnabled(false);
        m_boCancelAudit.setEnabled(false);
        m_boFreeze.setEnabled(false);
        m_boOpen.setEnabled(false);
        m_boClose.setEnabled(false);
        m_boFresh.setEnabled(false);
        m_boExecute.setEnabled(true);
        m_boReturn.setEnabled(false);
        m_boStockLock.setEnabled(false);

        break;

      case ConstVO.IBillStatus_CHECKING: // ������
        m_boCopy.setEnabled(true);

        // modified by songhy, 2008-08-07, start
        if (auditorId == null) {
          // ��ǰ�����ǡ������С�״̬���ҡ������ˡ�Ϊ�գ����޸ġ�ɾ���������޸ġ��͡�ɾ������ť���ÿ���
          m_boUpdate.setEnabled(true);
          m_boDelete.setEnabled(true);
        }
        else {
          // ��ǰ�����ǡ������С�״̬���ҡ������ˡ��ǿգ����ɸġ�ɾ���������޸ġ��͡�ɾ������ť���ò�����
          m_boUpdate.setEnabled(false);
          m_boDelete.setEnabled(false);
        }
        // modified by songhy, 2008-08-07, end

        m_boSendAudit.setEnabled(false);
        m_boAudit.setEnabled(true);
        m_boCancelAudit.setEnabled(true);
        m_boReturn.setEnabled(false);

        m_boFreeze.setEnabled(false);
        m_boOpen.setEnabled(false);
        m_boClose.setEnabled(false);
        m_boFresh.setEnabled(false);
        m_boExecute.setEnabled(true);
        m_boStockLock.setEnabled(false);
        break;

      // �ر�״̬�µĵ��ݣ��򿪿��ã����󣬶��ᣬ�ⶳ�����á�
      case ConstVO.IBillStatus_CLOSED:
        m_boCopy.setEnabled(false);
        m_boUpdate.setEnabled(false);
        m_boDelete.setEnabled(false);
        m_boSendAudit.setEnabled(false);
        m_boAudit.setEnabled(false);
        m_boCancelAudit.setEnabled(false);
        // m_boAuditStatus.setEnabled(true);
        m_boOpen.setEnabled(true);
        m_boClose.setEnabled(false);
        m_boFreeze.setEnabled(false);
        m_boFresh.setEnabled(false);
        m_boExecute.setEnabled(true);
        m_boReturn.setEnabled(true);
        m_boStockLock.setEnabled(false);
        break;

      // ����״̬�µĵ��ݣ��ⶳ���ã����󣬶���, �򿪲�����
      case ConstVO.IBillStatus_FREEZED:
        m_boCopy.setEnabled(false);
        m_boUpdate.setEnabled(false);
        m_boDelete.setEnabled(false);
        m_boSendAudit.setEnabled(false);
        m_boAudit.setEnabled(false);
        m_boCancelAudit.setEnabled(false);
        m_boOpen.setEnabled(false);
        m_boClose.setEnabled(false);
        m_boFreeze.setEnabled(false);
        m_boFresh.setEnabled(true);
        m_boReturn.setEnabled(false);
        m_boExecute.setEnabled(true);
        m_boStockLock.setEnabled(false);

        break;

    }
  }

  /**
   * �������ڣ�(2004-2-10 11:28:46) ���ߣ����˾� ������ int iMode,��ǰ�༭״̬�� int
   * iCurPanel,��ǰ��ʾ��panel. int iBillTotal,��ǰ�ĵ�������������page down ϵ�С� int
   * iCurNum,��ǰָ��ĵ��ݣ�����page down ϵ�С� int iCurBillStatus ,��ǰҵ��״̬�����Ƹġ�ɾ�����ȡ�
   * boolean bReject ���ܷ�����˻� boolean bLogCorp ,������˾�Ƿ�Ϊ��½��˾ boolean bCopy
   * ���ܷ���е��ݸ��� boolean bPlan,�ܷ���в������š��������ţ���½��˾�Ƿ���ڷ�����˾�� boolean bDelSettlePath
   * ȡ������·���Ƿ����
   *
   * @param auditorId
   *          ������ ���أ� ˵�������ð�ť״̬
   * @param mode
   *          int
   */
  public void setButtonStatusOfBrowse(int iCurPanel, int iBillTotal,
      int iCurNum, int iCurBillStatus, boolean bReject, boolean bPlan3Z,boolean bPlan4331,
      boolean bDelSettlePath, String auditorId,boolean iqueryStatus) {
    if (m_tp != null && m_tp instanceof IBillExtendFun) {
      ((IBillExtendFun) m_tp).setExtendBtnsStat(GenEditConst.BROWSE);
    }

    m_boCancelRef.setVisible(false);
    m_boBusiType.setEnabled(true);
    m_boNew.setEnabled(true);
    m_boSave.setEnabled(false);
    m_boCancel.setEnabled(false);
    m_boQuery.setEnabled(true);
    if(iqueryStatus)
    {m_boRefresh.setEnabled(true);}
    else{
    m_boRefresh.setEnabled(false);
    }
    m_boSwitch.setEnabled(true);
    m_boStockLock.setEnabled(false);
    m_boSelSettlePath.setEnabled(true);
    m_boDelSettlePath.setEnabled(true);

    m_boSendPlan.setEnabled(true);
    m_boFillPlan.setEnabled(true);

    m_boAddLine.setEnabled(false);
    m_boDeleteLine.setEnabled(false);
    m_boCopyLine.setEnabled(false);
    m_boPasteLine.setEnabled(false);
    m_boPasteLineToTail.setEnabled(false);
    m_boInsertLine.setEnabled(false);
    // ��Ӧ��һ���жϵ�ǰ�ĵ����Ƿ���ǩ��
    // ͬʱ�ж��޸ġ�ɾ���Ƿ���ã�����Ӧ�������ǵĺ��档
    m_boBillRowMng.setEnabled(false);
    m_boRefAddLine.setEnabled(false);
    m_boExecute.setEnabled(true);
    // ��������֧��
    m_boDocument.setEnabled(true);
    m_boJointCheck.setEnabled(true);
    // ���Ʒ�ҳ��ť��״̬��
    if (iCurPanel == GenEditConst.LIST) {
      m_PageBtnCtrl.setPageBtnVisible(false);
      m_boBillCombin.setEnabled(false);
    }
    else {
      m_boBillCombin.setEnabled(true);
      m_PageBtnCtrl.setPageBtnVisible(true);
      m_PageBtnCtrl.setPageBtnStatus(iBillTotal, iCurNum);
    }
    // ����ҵ��״̬�ж�
    // �е���
    if (iBillTotal > 0 && iCurNum >= 0) {
      m_boLocate.setEnabled(true);
      m_boPrint.setEnabled(true);
      m_boPreview.setEnabled(true);
      m_boAuditStatus.setEnabled(true);
      setBusinessStatus(iCurBillStatus, auditorId);
      m_boReturn.setEnabled(bReject);
      m_boCopy.setEnabled(true);

      m_boSendPlan.setEnabled(bPlan4331);// ��������
      m_boFillPlan.setEnabled(bPlan3Z);// ��������

      m_boDelSettlePath.setEnabled(bDelSettlePath);

      m_boBrowse.setEnabled(true);
      m_boPrintMng.setEnabled(true);
      m_boAssistMng.setEnabled(true);
      m_boAssistQuery.setEnabled(true);
      //�ֵ���ӡ��ť״̬
      m_splitPrint.setEnabled(true);
    }
    else {
      m_boBillCombin.setEnabled(false);
      m_boLocate.setEnabled(false);
      m_boPrint.setEnabled(false);
      m_boPreview.setEnabled(false);

      m_boCopy.setEnabled(false);
      m_boUpdate.setEnabled(false);
      m_boDelete.setEnabled(false);

      m_boAudit.setEnabled(false);
      m_boCancelAudit.setEnabled(false);
      m_boSendAudit.setEnabled(false);
      m_boAuditStatus.setEnabled(false);

      m_boExecute.setEnabled(false);
      m_boReturn.setEnabled(false);

      m_boSendPlan.setEnabled(false);
      m_boFillPlan.setEnabled(false);
      m_boSelSettlePath.setEnabled(false);
      m_boDelSettlePath.setEnabled(false);
      //�ֵ���ӡ��ť״̬
      m_splitPrint.setEnabled(false);
    }

  }

  private void setButtonStatusOfNew() {
    m_boBusiType.setEnabled(false);
    m_boNew.setEnabled(false);
    m_boBillCombin.setEnabled(false);
    m_boCopy.setEnabled(false);
    m_boUpdate.setEnabled(false);
    m_boSelSettlePath.setEnabled(false);
    m_boDelSettlePath.setEnabled(false);
    m_boSendPlan.setEnabled(false);
    m_boFillPlan.setEnabled(false);
    m_boDelete.setEnabled(false);
    m_boSave.setEnabled(true);
    m_boSendAudit.setEnabled(false);
    m_boSendAudit.setEnabled(false);
    m_boAuditStatus.setEnabled(false);
    m_boCancel.setEnabled(true);
    m_boQuery.setEnabled(false);
    m_boRefresh.setEnabled(false);
    m_boLocate.setEnabled(false);
    m_boSwitch.setEnabled(false);
    m_boStockLock.setEnabled(false);

    m_boAddLine.setEnabled(true);
    // �˻�ʱ��ɾ��
    m_boDeleteLine.setEnabled(true);
    m_boCopyLine.setEnabled(true);
    m_boPasteLine.setEnabled(true);
    m_boPasteLineToTail.setEnabled(true);
    m_boInsertLine.setEnabled(true);

    m_boAudit.setEnabled(false);
    m_boCancelAudit.setEnabled(false);

    m_boBillRowMng.setEnabled(true);
    m_boRefAddLine.setEnabled(true);

    m_boPrint.setEnabled(false);
    m_boPreview.setEnabled(false);
    m_boDocument.setEnabled(false);
    m_boReturn.setEnabled(false);

    // ���Ʒ�ҳ��ť��״̬��
    m_PageBtnCtrl.setPageBtnVisible(false);
    m_boJointCheck.setEnabled(false);

    m_boFreeze.setEnabled(false);
    m_boOpen.setEnabled(false);
    m_boClose.setEnabled(false);
    m_boFresh.setEnabled(false);
    m_boExecute.setEnabled(false);
  }

  private void setButtonStatusOfUpdate(boolean bCanAddLine) {
    m_boBusiType.setEnabled(false);
    m_boNew.setEnabled(false);
    m_boBillCombin.setEnabled(false);
    m_boCopy.setEnabled(false);
    m_boUpdate.setEnabled(false);
    m_boSelSettlePath.setEnabled(false);
    m_boDelSettlePath.setEnabled(false);
    m_boSendPlan.setEnabled(false);
    m_boFillPlan.setEnabled(false);
    m_boDelete.setEnabled(false);
    m_boSave.setEnabled(true);
    m_boSendAudit.setEnabled(false);
    m_boAuditStatus.setEnabled(false);
    m_boCancel.setEnabled(true);
    m_boQuery.setEnabled(false);
    m_boRefresh.setEnabled(false);
    m_boLocate.setEnabled(false);
    m_boSwitch.setEnabled(false);
    m_boStockLock.setEnabled(false);

    m_boAddLine.setEnabled(bCanAddLine);
    // �˻�ʱ��ɾ��
    m_boDeleteLine.setEnabled(true);
    m_boCopyLine.setEnabled(bCanAddLine);
    m_boPasteLine.setEnabled(bCanAddLine);
    m_boPasteLineToTail.setEnabled(bCanAddLine);
    m_boInsertLine.setEnabled(bCanAddLine);

    m_boAudit.setEnabled(false);
    m_boCancelAudit.setEnabled(false);

    m_boBillRowMng.setEnabled(true);
    m_boRefAddLine.setEnabled(true);

    m_boPrint.setEnabled(false);
    m_boPreview.setEnabled(false);

    m_boDocument.setEnabled(false);
    // ���Ʒ�ҳ��ť��״̬��
    m_PageBtnCtrl.setPageBtnVisible(false);
    m_boReturn.setEnabled(false);
    m_boJointCheck.setEnabled(false);

    m_boFreeze.setEnabled(false);
    m_boOpen.setEnabled(false);
    m_boClose.setEnabled(false);
    m_boFresh.setEnabled(false);
    m_boExecute.setEnabled(false);

  }

  public void setButtonStatusOfRef() {
    m_boBusiType.setEnabled(false);
    m_boNew.setEnabled(false);
    m_boBillCombin.setEnabled(false);
    m_boCopy.setEnabled(false);
    m_boUpdate.setEnabled(true);
    m_boSelSettlePath.setEnabled(false);
    m_boDelSettlePath.setEnabled(false);
    m_boSendPlan.setEnabled(false);
    m_boFillPlan.setEnabled(false);
    m_boDelete.setEnabled(false);
    m_boSave.setEnabled(false);
    m_boSendAudit.setEnabled(false);
    m_boAuditStatus.setEnabled(false);
    m_boCancel.setEnabled(false);
    m_boQuery.setEnabled(false);
    m_boRefresh.setEnabled(false);
    m_boLocate.setEnabled(false);
    m_boSwitch.setEnabled(false);
    m_boStockLock.setEnabled(false);

    m_boAddLine.setEnabled(false);
    //�˻�ʱ��ɾ��
    m_boDeleteLine.setEnabled(false);
    m_boCopyLine.setEnabled(false);
    m_boPasteLine.setEnabled(false);
    m_boPasteLineToTail.setEnabled(false);
    m_boInsertLine.setEnabled(false);

    m_boAudit.setEnabled(false);
    m_boCancelAudit.setEnabled(false);

    m_boBillRowMng.setEnabled(false);
    m_boRefAddLine.setEnabled(false);

    m_boPrint.setEnabled(false);
    m_boPreview.setEnabled(false);

    m_boDocument.setEnabled(false);
    //���Ʒ�ҳ��ť��״̬��
    m_PageBtnCtrl.setPageBtnVisible(false);
    m_boReturn.setEnabled(false);
    m_boJointCheck.setEnabled(false);

    m_boFreeze.setEnabled(false);
    m_boOpen.setEnabled(false);
    m_boClose.setEnabled(false);
    m_boFresh.setEnabled(false);
    m_boExecute.setEnabled(false);
    m_boBrowse.setEnabled(false);
    m_boPrintMng.setEnabled(false);
    m_boAssistMng.setEnabled(false);
//    m_boAssistQuery.setEnabled(false);  zhfmodify
    m_boCancelRef.setVisible(true);
  }
}