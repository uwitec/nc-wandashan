package nc.lm.ui.classinfor;
import java.io.Serializable;
import nc.vo.trade.pub.IBDGetCheckClass2;
/**
 * @author mlr
 * 该对象为 ui 中getUserObject方法返回的对象 
 * 用于记录前后台校验类的路径,便于前后台校验类的实例化
 * 该类必须放到public 包下
 */
public class GetCheck implements Serializable, IBDGetCheckClass2 {
	private static final long serialVersionUID = 428965024363387549L;
	/**
	 * 后台校验类路径
	 */
	public String getCheckClass() {
		return "nc.lm.bs.classinfor.BSCheck";
	}
	/**
	 * 前台校验类路径
	 */
	public String getUICheckClass() {		
		return "nc.lm.ui.classinfor.UICheck";
	}
}
