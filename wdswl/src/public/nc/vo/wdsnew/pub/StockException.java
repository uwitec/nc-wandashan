package nc.vo.wdsnew.pub;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.pub.SuperVO;
/**
 * @author mlr
 */
public class StockException extends Exception{
	private static final long serialVersionUID = 8347287898600164256L;
	private SuperVO[] bvos=null;
	
	public StockException(SuperVO[] bvos){
		super();
		this.bvos=bvos;
	}

	public SuperVO[] getBvos() {
		return bvos;
	}

	public void setBvos(TbOutgeneralBVO[] bvos) {
		this.bvos = bvos;
	}
	

}
