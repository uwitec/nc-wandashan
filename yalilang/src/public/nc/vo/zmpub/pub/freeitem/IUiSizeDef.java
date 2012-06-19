package nc.vo.zmpub.pub.freeitem;

/**
 * @author wnj
 *
 * TODO 功能概述（必须）
 * 
 * 	界面控件尺寸，布局 标准定义 FROM 3.1
 * 
 * 修改记录：
 * 
 */
public interface IUiSizeDef {
    
    public int BTN_WIDTH=85;//两个汉字的按钮长度
    public int BTN_WIDTH_3W=100;//三个个汉字的按钮长度
    public int BTN_HEIGHT=25;//按钮高度
    public int BTN_RIGHT_MARGIN=20;//按钮离窗体右边界的PIXEL数
    public int BTN_INTERVAL=8;//按钮间隔PIXEL数
    public int BTN_INTERVAL_V=10;//按钮垂直间隔PIXEL数
    public int BTN_X_CANCEL=(BTN_WIDTH+BTN_RIGHT_MARGIN);//CANCEL的X坐标=DLG_WIDTH-BTN_X_CANCEL
    public int BTN_X_OK=(BTN_WIDTH+BTN_WIDTH+BTN_INTERVAL+BTN_RIGHT_MARGIN);//OK的X坐标=DLG_WIDTH-BTN_X_OK
    public int BTN_X_3=(BTN_WIDTH+BTN_WIDTH+BTN_INTERVAL+BTN_WIDTH+BTN_INTERVAL+BTN_RIGHT_MARGIN);//OK的X坐标=DLG_WIDTH-BTN_X_OK
    


}
