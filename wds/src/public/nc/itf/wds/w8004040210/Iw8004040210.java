package nc.itf.wds.w8004040210;

import java.util.List;

import nc.itf.wds.w8000.Iw8000;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.wds.w8004040210.MyEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.wds.w8004040210.TbGeneralBBVO;
import nc.vo.wds.w8004040210.TbGeneralHVO;

public interface Iw8004040210 extends Iw8000 {
	public int delTbGeneralBBVO(List items) throws BusinessException;

	public int insertTbGeneralBBVO(TbGeneralBBVO[] items)
			throws BusinessException;

	// public int insertTbGeneralBBVO1(List items) throws BusinessException;

	// public void delAndInsertTbGeneralBBVO(List items1, List items2)
	// throws BusinessException;

	public void delAndInsertTbGeneralBBVO(List items1, TbGeneralBBVO[] items2)
			throws BusinessException;

	public int updateBdCargdocTray(List bcts) throws BusinessException;

	public AggregatedValueObject saveBD(AggregatedValueObject billVO,
			Object userObj) throws Exception;

	// 回写（保存和签字）
	public void pushsavesign8004040210(String actionName, String billType,
			String currentDate, AggregatedValueObject vo,
			TbGeneralHVO tbGeneralHVO) throws Exception;
	// 回写（取消签字和删除）
	public void canceldelete8004040210(String actionName1,String actionName2, String billType,
			String currentDate, AggregatedValueObject vo,
			TbGeneralHVO tbGeneralHVO) throws Exception;
}
