package nc.vo.wl.pub.report;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class EXAggregatedValueObject extends AggregatedValueObject{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private CircularlyAccessibleValueObject[] items = null;
    
    private CircularlyAccessibleValueObject head = null;
    
    @Override
    public CircularlyAccessibleValueObject[] getChildrenVO() {
        return items;
    }

    @Override
    public CircularlyAccessibleValueObject getParentVO() {
        return head;
    }

    @Override
    public void setChildrenVO(CircularlyAccessibleValueObject[] arg0) {
        items = arg0;
    }

    @Override
    public void setParentVO(CircularlyAccessibleValueObject arg0) {
        head = arg0;
    }

}
