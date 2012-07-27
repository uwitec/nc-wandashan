package nc.vo.dm.db.deal;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
/**
 * ��������vo
 * @author mlr
 */
public class DbDealVO extends SuperVO{	
	/**
	 * alter table  to_bill_b add(ndealnum number(20,8))//�������������ֶ�  �Ѱ�������
	 * alter table  to_bill_b add(ndealnumb number(20,8))//�������������ֶ�  �Ѱ��Ÿ�����
	 */	
	private static final long serialVersionUID = -5056426465421488586L;
	public static String[] num_fields = new String[]{"num","nassnum","nnum","nassistnum","ndealnum","ndealnumb"};//���ΰ�������  ��������     �����Ѱ�������
	private UFDouble num;//���ΰ�������
	private UFDouble nassnum;//���ΰ��Ÿ�����
	///-----------------------------���������ֶ�
	private String cincorpid;//���빫˾
	private String cincbid;//��������֯
	private Integer fallocflag;//�������ͱ�־
	private String vnote;//��ע
	private String vcode ;//���ݺ�
	private UFDate dbilldate;//��������
	
	//----------------------------------���������ֶ�
    private UFDouble nnum;//��������
    private UFDouble nassistnum;//����������
    private UFDouble ndealnum ;//�����Ѿ����ŵ�����
    private UFDouble ndealnumb;//�����Ѿ����� �ĸ�����
    private String coutdeptid;//��������
    private String coutcorpid;//������˾
    private String coutcbid;//���������֯
    private String coutwhid;//�����ֿ�
    private String cinwhid;//����ֿ�
    private String coutpsnid;//��������ҵ��Ա
    private UFBoolean bretractflag;//�Ƿ��˻�
    private String pk_sendtype;//���˷�ʽ
    private String vreceiveaddress;//�ջ���ַ
    private String ccustomerid;//�����ر��� 
    private String ctakeoutinvid;//�������id
    private String cinvbasid;//�������id
    private String cprojectphase;//��Ŀ�׶�
    private String cprojectid;//��Ŀ����
    private String cbillid;// ��ͷid
    private String cbill_bid;//����id
    private String vbatch;//���κ�
    private String crowno;//�к�
    private String ctypecode;//��������
    private String castunitid;//��������λ
    private String creceieveid;//�ջ���λ
    private UFDouble nchangerate;//������
    private String ctakeoutspaceid;//�����λ
    private String dvalidate;//ʧЧ����
    private String cquoteunitid;//���ۼ�����λID		
    private UFDouble nquoteunitrate;//���ۼ�����λ������
    private UFBoolean flargess;//�Ƿ���Ʒ
    private String cvendorid;//��Ӧ��

    //--------------------------������Ϣ
    private String vdef1;//���״̬
    private UFDouble nstorenumout ;//���������
    private UFDouble anstorenumout;//��渨����
	private UFDouble ndrqstorenumout;//������������ mlr
	private UFDouble ndrqarrstorenumout;//������ mlr

	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ����ϲ�
	 * @ʱ�䣺2011-7-8����09:53:42
	 * @param body2
	 */
	public void combin(DbDealVO body2){
		for(String name:num_fields){
			setAttributeValue(name, PuPubVO.getUFDouble_NullAsZero(getAttributeValue(name)).
					add(PuPubVO.getUFDouble_NullAsZero(body2.getAttributeValue(name))));
		}
	}
	
	public UFDouble getNdrqstorenumout() {
		return ndrqstorenumout;
	}
	public void setNdrqstorenumout(UFDouble ndrqstorenumout) {
		this.ndrqstorenumout = ndrqstorenumout;
	}
	public UFDouble getNdrqarrstorenumout() {
		return ndrqarrstorenumout;
	}
	public void setNdrqarrstorenumout(UFDouble ndrqarrstorenumout) {
		this.ndrqarrstorenumout = ndrqarrstorenumout;
	}

	public transient static String[] m_headNames = new String[]{
		"h.cincorpid",
		"h.cincbid",
		"h.fallocflag",
		"h.ctypecode",
		"h.vnote",

	};
	public transient static String[] m_bodyNames = new String[]{
		"b.nnum",
		"b.nassistnum",
		"b.ndealnum",
		"b.ndealnumb",		
		"b.coutdeptid",
		"b.coutcorpid",
		"b.coutcbid",
		"b.coutwhid",		
		"b.cinwhid",
		"b.coutpsnid",
		"b.bretractflag", 
		"b.pk_sendtype", 
		"b.vreceiveaddress", 
//		"b.ccustomerid", 
		"b.ctakeoutinvid", 
		"b.cinvbasid", 
		"b.cprojectphase",
		"b.cprojectid",
		"b.cbillid",
		"b.cbill_bid",
	    "b.vbatch",
		"b.vcode",
		"b.crowno",
		"b.ctypecode",
		"b.castunitid",
		"b.creceieveid",
		"b.nchangerate",
		"b.ctakeoutspaceid",
		"b.dvalidate",
		"b.cquoteunitid",
	//	"b.nexchangeotoarate",
		"b.nquoteunitrate",
		"b.flargess",
		"b.cvendorid",
	};
	
	
	
	public UFDouble getNassnum() {
		return nassnum;
	}
	public void setNassnum(UFDouble nassnum) {
		this.nassnum = nassnum;
	}
	public UFDouble getNstorenumout() {
		return nstorenumout;
	}
	public void setNstorenumout(UFDouble nstorenumout) {
		this.nstorenumout = nstorenumout;
	}
	public UFDouble getAnstorenumout() {
		return anstorenumout;
	}
	public void setAnstorenumout(UFDouble anstorenumout) {
		this.anstorenumout = anstorenumout;
	}
	public UFDouble getNnum() {
		return nnum;
	}
	public void setNnum(UFDouble nnum) {
		this.nnum = nnum;
	}
	@Override
	public String getPKFieldName() {
		return null;
	}
	@Override
	public String getParentPKFieldName() {
		return null;
	}
	
	
	public String getVdef1() {
		return vdef1;
	}
	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}
	@Override
	public String getTableName() {
		return null;
	}
	public String getVnote() {
		return vnote;
	}
	public UFDouble getNum() {
		return num;
	}
	public void setNum(UFDouble num) {
		this.num = num;
	}
	public String getCincorpid() {
		return cincorpid;
	}
	public void setCincorpid(String cincorpid) {
		this.cincorpid = cincorpid;
	}
	public String getCincbid() {
		return cincbid;
	}
	public void setCincbid(String cincbid) {
		this.cincbid = cincbid;
	}
	public Integer getFallocflag() {
		return fallocflag;
	}
	public void setFallocflag(Integer fallocflag) {
		this.fallocflag = fallocflag;
	}
	public UFDouble getNassistnum() {
		return nassistnum;
	}
	public void setNassistnum(UFDouble nassistnum) {
		this.nassistnum = nassistnum;
	}
	public UFDouble getNdealnum() {
		return ndealnum;
	}
	public void setNdealnum(UFDouble ndealnum) {
		this.ndealnum = ndealnum;
	}
	public UFDouble getNdealnumb() {
		return ndealnumb;
	}
	public void setNdealnumb(UFDouble ndealnumb) {
		this.ndealnumb = ndealnumb;
	}
	public String getCoutdeptid() {
		return coutdeptid;
	}
	public void setCoutdeptid(String coutdeptid) {
		this.coutdeptid = coutdeptid;
	}
	public String getCoutcorpid() {
		return coutcorpid;
	}
	public void setCoutcorpid(String coutcorpid) {
		this.coutcorpid = coutcorpid;
	}
	public String getCoutcbid() {
		return coutcbid;
	}
	public void setCoutcbid(String coutcbid) {
		this.coutcbid = coutcbid;
	}
	public String getCoutwhid() {
		return coutwhid;
	}
	public void setCoutwhid(String coutwhid) {
		this.coutwhid = coutwhid;
	}
	public String getCinwhid() {
		return cinwhid;
	}
	public void setCinwhid(String cinwhid) {
		this.cinwhid = cinwhid;
	}
	public String getCoutpsnid() {
		return coutpsnid;
	}
	public void setCoutpsnid(String coutpsnid) {
		this.coutpsnid = coutpsnid;
	}
	public UFBoolean getBretractflag() {
		return bretractflag;
	}
	public void setBretractflag(UFBoolean bretractflag) {
		this.bretractflag = bretractflag;
	}
	public String getPk_sendtype() {
		return pk_sendtype;
	}
	public void setPk_sendtype(String pk_sendtype) {
		this.pk_sendtype = pk_sendtype;
	}
	public String getVreceiveaddress() {
		return vreceiveaddress;
	}
	public void setVreceiveaddress(String vreceiveaddress) {
		this.vreceiveaddress = vreceiveaddress;
	}
	public String getCcustomerid() {
		return ccustomerid;
	}
	public void setCcustomerid(String ccustomerid) {
		this.ccustomerid = ccustomerid;
	}
	public String getCtakeoutinvid() {
		return ctakeoutinvid;
	}
	public void setCtakeoutinvid(String ctakeoutinvid) {
		this.ctakeoutinvid = ctakeoutinvid;
	}
	public String getCinvbasid() {
		return cinvbasid;
	}
	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}
	public String getCprojectphase() {
		return cprojectphase;
	}
	public void setCprojectphase(String cprojectphase) {
		this.cprojectphase = cprojectphase;
	}
	public String getCprojectid() {
		return cprojectid;
	}
	public void setCprojectid(String cprojectid) {
		this.cprojectid = cprojectid;
	}

	public String getCbillid() {
		return cbillid;
	}
	public void setCbillid(String cbillid) {
		this.cbillid = cbillid;
	}
	public String getCbill_bid() {
		return cbill_bid;
	}
	public void setCbill_bid(String cbill_bid) {
		this.cbill_bid = cbill_bid;
	}
	public String getVbatch() {
		return vbatch;
	}
	public void setVbatch(String vbatch) {
		this.vbatch = vbatch;
	}
	public String getVcode() {
		return vcode;
	}
	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
	public String getCrowno() {
		return crowno;
	}
	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}
	public String getCtypecode() {
		return ctypecode;
	}
	public void setCtypecode(String ctypecode) {
		this.ctypecode = ctypecode;
	}
	public String getCastunitid() {
		return castunitid;
	}
	public void setCastunitid(String castunitid) {
		this.castunitid = castunitid;
	}
	public String getCreceieveid() {
		return creceieveid;
	}
	public void setCreceieveid(String creceieveid) {
		this.creceieveid = creceieveid;
	}
	public UFDouble getNchangerate() {
		return nchangerate;
	}
	public void setNchangerate(UFDouble nchangerate) {
		this.nchangerate = nchangerate;
	}
	public String getCtakeoutspaceid() {
		return ctakeoutspaceid;
	}
	public void setCtakeoutspaceid(String ctakeoutspaceid) {
		this.ctakeoutspaceid = ctakeoutspaceid;
	}
	public String getDvalidate() {
		return dvalidate;
	}
	public void setDvalidate(String dvalidate) {
		this.dvalidate = dvalidate;
	}
	public String getCquoteunitid() {
		return cquoteunitid;
	}
	public void setCquoteunitid(String cquoteunitid) {
		this.cquoteunitid = cquoteunitid;
	}
	public UFDouble getNquoteunitrate() {
		return nquoteunitrate;
	}
	public void setNquoteunitrate(UFDouble nquoteunitrate) {
		this.nquoteunitrate = nquoteunitrate;
	}
	public UFBoolean getFlargess() {
		return flargess;
	}
	public void setFlargess(UFBoolean flargess) {
		this.flargess = flargess;
	}
	public String getCvendorid() {
		return cvendorid;
	}
	public void setCvendorid(String cvendorid) {
		this.cvendorid = cvendorid;
	}
	public void setVnote(String vnote) {
		this.vnote = vnote;
	}
	public UFDate getDbilldate() {
		return dbilldate;
	}
	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}
	
	
	
}
