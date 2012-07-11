package nc.bs.wds.ic.allocation.out;
import nc.bs.pub.SuperDMO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
/**
 * 调拨出库单 后台业务类
 * @author mlr
 *
 */
public class AlloutBO {
	private SuperDMO dmo = new SuperDMO();
	
	public void updateHVO(TbOutgeneralHVO hvo) throws BusinessException{
		if(hvo == null){
			return;
		}
		if(hvo.getPrimaryKey() != null)
			hvo.setStatus(VOStatus.UPDATED);
		else
			hvo.setStatus(VOStatus.NEW);
		dmo.update(hvo);
	}
}
