package nc.ui.scm.goldtax;

import static nc.vo.jcom.lang.StringUtil.isEmpty;
import static nc.vo.jcom.lang.StringUtil.isEmptyWithTrim;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.goldtax.Configuration;
import nc.vo.scm.goldtax.GoldTaxBodyVO;
import nc.vo.scm.goldtax.GoldTaxHeadVO;
import nc.vo.scm.goldtax.GoldTaxVO;
import nc.vo.scm.merge.DefaultVOMerger;
import nc.vo.scm.pub.SCMEnv;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 金税工具类，提供金税合并、拆分、导出到文件等方法
 * 
 * @author 蒲强华
 * @since 2008-8-27
 */
class GoldTaxTransport {
    /** 金税文件注释行的前缀 */
    private static final String COMMENT_PREFIX = "//";
    /** 金税VO中客户ID属性名称 */
    private static final String ATTR_CUSTOMER_ID = "customerId";
    /** 金税VO中存货属性名称 */
    private static final String ATTR_INVENTORY = "invBaseId";
    /** 金税VO中存货类属性名称 */
    private static final String ATTR_INVCLASS = "invClassId";
    /** 金税VO中价格属性名称 */
    private static final String ATTR_PRICE = "price";

    /** 金税VO中数量属性名称 */
    private static final String ATTR_QUANTITY = "number";
    /** 金税VO中金额属性名称 */
    private static final String ATTR_MONEY = "money";
    /** 金税VO中无税金额属性名称 */
    private static final String ATTR_NOTAXMONEY = "noTaxMny";

    /** 公司主键 */
    private String corp = null;
    /** 传金税配置信息 */
    private Configuration conf = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public GoldTaxTransport(String corp) {
        this.corp = corp;
    }

    /**
     * @return 传金税配置信息
     */
    Configuration getConf() {
        if (null == conf) {
            // 装载参数
            conf = Configuration.load(corp);
        }
        return conf;
    }

    /**
     * @param conf 传金税配置信息
     */
    void setConf(Configuration conf) {
        this.conf = conf;
    }

    /**
     * 合并和分单处理金税VO
     * 
     * @param goldTaxVOs 待处理的金税VO数组
     * @return 处理后的金税VO数组
     */
    public GoldTaxVO[] mergeAndSplit(GoldTaxVO[] goldTaxVOs) {
        // 则先进行表头合并
        goldTaxVOs = mergeHead(goldTaxVOs);
        // 再进行表体合并
        goldTaxVOs = mergeBody(goldTaxVOs);
        // 再进行分单
        goldTaxVOs = split(goldTaxVOs);

        // 重新计算一些字段值
        reCalcFields(goldTaxVOs);

        return goldTaxVOs;
    }

    /**
     * 按照客户合并表头信息，如果可以合并，
     * 则合并后的聚合VO的属性值取合并的表头VO的中第一个表头所在的聚会表头合并的金税VO的表体数组一合并为一个数组放到合并后的聚合VO的值，
     * 表头VO用合并后生成的VO，表体VO数组为包含所有合并的所有表头VO对应的表体VO的数组
     * 
     * @param goldTaxVOs 金税VO数组
     * @return 合并表头后的金税VO数组
     */
    private GoldTaxVO[] mergeHead(GoldTaxVO[] goldTaxVOs) {
        // 如果没有多单合并，则不需要合并表头，直接返回
        if (!getConf().isMergeMutiBill()) {
            return goldTaxVOs;
        }

        Map<String, List<GoldTaxVO>> mapOfCust = new HashMap<String, List<GoldTaxVO>>();
        GoldTaxHeadVO[] allHeadVOs = new GoldTaxHeadVO[goldTaxVOs.length];
        // 表头按客户分组
        for (int i = 0; i < goldTaxVOs.length; i++) {
            GoldTaxVO taxVO = goldTaxVOs[i];
            allHeadVOs[i] = taxVO.getParentVO();
            List<GoldTaxVO> list = mapOfCust.get(taxVO.getParentVO().getCustomerId());
            if (null == list) {
                list = new ArrayList<GoldTaxVO>();
                mapOfCust.put(taxVO.getParentVO().getCustomerId(), list);
            }
            list.add(taxVO);
        }

        // 合并表头
        GoldTaxHeadVO[] mergedHeadVO = null;
        DefaultVOMerger merger = new DefaultVOMerger();
        merger.setGroupingAttr(new String[]{ATTR_CUSTOMER_ID});
        try {
            mergedHeadVO = (GoldTaxHeadVO[]) merger.mergeByGroup(allHeadVOs);
        } catch (BusinessException e) {
            SCMEnv.error("合并发生异常", e);
            throw new BusinessRuntimeException(e.getMessage(), e);
        }

        // 新的合并后的聚合VO数组
        GoldTaxVO[] mergedTaxVO = new GoldTaxVO[mergedHeadVO.length];
        for (int i = 0; i < mergedHeadVO.length; i++) {
            GoldTaxHeadVO taxHeadVO = mergedHeadVO[i];

            // 汇总合并了表头的表体VO
            List<GoldTaxBodyVO> bodyVOsOfCust = new ArrayList<GoldTaxBodyVO>();
            // 所有包含的单据号
            Set<String> codes = new HashSet<String>();
            List<GoldTaxVO> taxVoOfCust = mapOfCust.get(taxHeadVO.getCustomerId());
            for (GoldTaxVO taxVO : taxVoOfCust) {
                bodyVOsOfCust.addAll(Arrays.asList(taxVO.getChildrenVO()));
                codes.add(taxVO.getParentVO().getCode());
            }

            // 新的单据号
            String newCode = "";
            for (String code : codes) {
                newCode += code;
            }

            // 新的聚合VO
            GoldTaxVO taxVO = simplyCopy(taxVoOfCust.get(0));
            // 表头VO为合并后的表头
            taxVO.setParentVO(taxHeadVO);
            // 设置新单据号
            taxHeadVO.setCode(newCode);
            // 表体VO为包含合并的所有表头VO对应的表体VO数组
            taxVO.setChildrenVO(bodyVOsOfCust.toArray(new GoldTaxBodyVO[bodyVOsOfCust.size()]));
            mergedTaxVO[i] = taxVO;
        }

        return mergedTaxVO;
    }

    /**
     * 对每个金税VO的表体进行合并
     * 
     * @param goldTaxVOs
     * @return 合并表体后的金税VO数组
     */
    private GoldTaxVO[] mergeBody(GoldTaxVO[] goldTaxVOs) {
        List<String> groupAttrs = new ArrayList<String>();
        if (getConf().isMergeInventory()) {
            groupAttrs.add(ATTR_INVENTORY);
        }
        if (getConf().isMergeInvClass()) {
            groupAttrs.add(ATTR_INVCLASS);
        }
        if (getConf().isMergePrice()) {
            groupAttrs.add(ATTR_PRICE);
        }

        // 如果没有合并字段，则不需要合并，直接返回
        if (groupAttrs.size() == 0) {
            return goldTaxVOs;
        }

        String[] groups = groupAttrs.toArray(new String[groupAttrs.size()]);
        String[] sums = new String[]{ATTR_QUANTITY, ATTR_MONEY, ATTR_NOTAXMONEY};

        DefaultVOMerger merger = new DefaultVOMerger();
        merger.setGroupingAttr(groups);
        merger.setSummingAttr(sums);
        for (GoldTaxVO taxVO : goldTaxVOs) {
            try {
                taxVO.setChildrenVO(merger.mergeByGroup(taxVO.getChildrenVO()));
            } catch (BusinessException e) {
                SCMEnv.error("合并发生异常", e);
                throw new BusinessRuntimeException(e.getMessage(), e);
            }
        }
        return goldTaxVOs;
    }

    /**
     * 重新计算一些字段值，比如金额和数量等
     * 
     * @param taxBodyVOs 要总新计算单价的金税表体VO数组
     */
    private void reCalcFields(GoldTaxVO[] taxVOs) {
        for (GoldTaxVO goldTaxVO : taxVOs) {
            for (GoldTaxBodyVO bodyVO : goldTaxVO.getChildrenVO()) {
                // 含税单价
                bodyVO.setPrice(bodyVO.getMoney().div(bodyVO.getNumber(), bodyVO.getMoney().getPower()));
                // 无税单价
                bodyVO.setNoTaxPrice(bodyVO.getNoTaxMny().div(bodyVO.getNumber(), bodyVO.getNoTaxMny().getPower()));
                if (null != bodyVO.getTaxRate()) {
                    bodyVO.setTaxMny(bodyVO.getNoTaxMny().multiply(bodyVO.getTaxRate(), bodyVO.getNoTaxMny().getPower()));
                }
            }
        }
    }

    /**
     * 对金税根据无税金额限额和行数进行分单
     * 
     * @param taxVOs 待分单的金税VO数组
     * @return 分单后的金税VO数组
     */
    GoldTaxVO[] split(GoldTaxVO[] taxVOs) {
        List<GoldTaxVO> orgTaxes = new ArrayList<GoldTaxVO>();
        orgTaxes.addAll(Arrays.asList(taxVOs));
        List<GoldTaxVO> splited = new ArrayList<GoldTaxVO>();

        while (!orgTaxes.isEmpty()) {
            boolean split = false;
            UFDouble sumMny = new UFDouble(0);
            int rownum = 0;
            GoldTaxVO curTax = orgTaxes.remove(0);
            for (GoldTaxBodyVO bodyVO : curTax.getChildrenVO()) {
                if (needSplitRow(rownum + 1)) {
                    // 拆行
                    GoldTaxVO[] splitedVO = splitRow(curTax, rownum);
                    splited.add(splitedVO[0]);
                    orgTaxes.add(0, splitedVO[1]);
                    split = true;
                    break;
                } else if (needSplitMny(sumMny.add(bodyVO.getNoTaxMny()))) {
                    // 拆金额
                    GoldTaxVO[] splitedVO = splitMny(curTax);
                    splited.add(splitedVO[0]);
                    orgTaxes.add(0, splitedVO[1]);
                    split = true;
                    break;
                } else {
                    sumMny = sumMny.add(bodyVO.getNoTaxMny());
                    rownum++;
                }
            }
            if (!split) {
                // 不需要分单，直接放入已分单列表
                splited.add(curTax);
            }
        }
        return splited.toArray(new GoldTaxVO[splited.size()]);
    }

    /**
     * 根据给定的行数判断是否需要分单
     * 
     * @param rownum 行数
     * @return 如果需要分单返回true，否则返回false
     */
    boolean needSplitRow(int rownum) {
        int maxRows = getConf().getMaxRows();
        return maxRows > 0 && rownum > maxRows;
    }

    /**
     * 根据给定的行数分单
     * 
     * @param rownum
     *            行数
     * @return 长度为2的金税VO数组，第一个元素为分出的VO，第二个元素为剩下的VO
     */
    GoldTaxVO[] splitRow(GoldTaxVO taxVO, int rownum) {
        // 分出的VO
        GoldTaxBodyVO[] bodyVOs1 = new GoldTaxBodyVO[rownum];
        System.arraycopy(taxVO.getChildrenVO(), 0, bodyVOs1, 0, bodyVOs1.length);
        GoldTaxHeadVO headVO1 = simplyCopy(taxVO.getParentVO());
        GoldTaxVO taxVO1 = simplyCopy(taxVO);
        taxVO1.setParentVO(headVO1);
        taxVO1.setChildrenVO(bodyVOs1);
        // 剩余的VO
        GoldTaxBodyVO[] bodyVOs2 = new GoldTaxBodyVO[taxVO.getChildrenVO().length - rownum];
        System.arraycopy(taxVO.getChildrenVO(), rownum, bodyVOs2, 0, bodyVOs2.length);
        GoldTaxHeadVO headVO2 = simplyCopy(taxVO.getParentVO());
        GoldTaxVO taxVO2 = simplyCopy(taxVO);
        taxVO2.setParentVO(headVO2);
        taxVO2.setChildrenVO(bodyVOs2);
        // 返回的VO数组
        GoldTaxVO[] splited = new GoldTaxVO[2];
        splited[0] = taxVO1;
        splited[1] = taxVO2;
        return splited;
    }

    /**
     * 根据给定的金额判断是否需要分单
     * 
     * @param money 要判断的无税金额
     * @return 如果需要分单返回true，否则返回false
     */
    boolean needSplitMny(UFDouble money) {
        UFDouble maxNoTaxMny = getConf().getMaxNoTaxMny();
        return (null != maxNoTaxMny) 
                && maxNoTaxMny.compareTo(UFDouble.ZERO_DBL) > 0
                && money.compareTo(maxNoTaxMny) > 0;
    }

    /**
     * 根据给定的分单
     * 
     * @param rownum
     *            行数
     * @return 长度为2的金税VO数组，第一个元素为分出的VO，第二个元素为剩下的VO
     */
    GoldTaxVO[] splitMny(GoldTaxVO taxVO) {
        UFDouble maxMny = getConf().getMaxNoTaxMny();
        UFDouble diffMny = maxMny;
        int splitRownum = 0;
        UFDouble diffNum = null;
        for (int i = 0; i < taxVO.getChildrenVO().length; i++ ) {
            GoldTaxBodyVO bodyVO = taxVO.getChildrenVO()[i];
            if (diffMny.sub(bodyVO.getNoTaxMny()).doubleValue() < 0) {
                diffNum = diffMny.div(bodyVO.getNoTaxPrice(), bodyVO.getNoTaxPrice().getPower());
                // 超限额拆行时数量是否必须为整数，并且数量必须大于 1 时才能舍去小数
                if (getConf().isIntegerQuantityOverMny() && diffNum.doubleValue() > 1) {
                    diffNum = new UFDouble(diffNum.intValue());
                    diffMny = bodyVO.getNoTaxPrice().multiply(diffNum, bodyVO.getNoTaxPrice().getPower());
                }
                splitRownum = i;
                break;
            }
            diffMny = diffMny.sub(bodyVO.getNoTaxMny(), bodyVO.getNoTaxMny().getPower());
        }
        if (diffNum.compareTo(new UFDouble(0)) == 0) {
            // 如果差异的数量是零，则表示不需要拆数量，只需要按行分单就可以了
            return splitRow(taxVO, splitRownum);
        }
        // 分出的VO
        GoldTaxBodyVO splitBody1 = simplyCopy(taxVO.getChildrenVO()[splitRownum]);
        splitBody1.setNumber(diffNum);
        splitBody1.setMoney(splitBody1.getPrice().multiply(splitBody1.getNumber(), splitBody1.getNumber().getPower()));
        splitBody1.setNoTaxMny(diffMny);
        GoldTaxBodyVO[] bodyVOs1 = new GoldTaxBodyVO[splitRownum + 1];
        System.arraycopy(taxVO.getChildrenVO(), 0, bodyVOs1, 0, bodyVOs1.length - 1);
        bodyVOs1[splitRownum] = splitBody1;
        GoldTaxHeadVO headVO1 = simplyCopy(taxVO.getParentVO());
        GoldTaxVO taxVO1 = simplyCopy(taxVO);
        taxVO1.setParentVO(headVO1);
        taxVO1.setChildrenVO(bodyVOs1);
        // 剩余的VO
        GoldTaxBodyVO splitBody2 = simplyCopy(taxVO.getChildrenVO()[splitRownum]);
        splitBody2.setNumber(taxVO.getChildrenVO()[splitRownum].getNumber().sub(diffNum));
        splitBody2.setMoney(taxVO.getChildrenVO()[splitRownum].getMoney().sub(splitBody1.getMoney()));
        splitBody2.setNoTaxMny(taxVO.getChildrenVO()[splitRownum].getNoTaxMny().sub(splitBody1.getNoTaxMny(), splitBody1.getNoTaxMny().getPower()));
        GoldTaxBodyVO[] bodyVOs2 = new GoldTaxBodyVO[taxVO.getChildrenVO().length - splitRownum];
        System.arraycopy(taxVO.getChildrenVO(), splitRownum + 1, bodyVOs2, 1, bodyVOs2.length - 1);
        bodyVOs2[0] = splitBody2;
        GoldTaxHeadVO headVO2 = simplyCopy(taxVO.getParentVO());
        GoldTaxVO taxVO2 = simplyCopy(taxVO);
        taxVO2.setParentVO(headVO2);
        taxVO2.setChildrenVO(bodyVOs2);
        // 返回的VO数组
        GoldTaxVO[] splited = new GoldTaxVO[2];
        splited[0] = taxVO1;
        splited[1] = taxVO2;
        return splited;
    }

    /**
     * 简单拷贝
     * 
     * @param <T> 必须要有无参数的构造方法
     * @param o 被拷贝的对象
     * @return 拷贝了属性的新对象
     */
    @SuppressWarnings("unchecked")
    private static <T> T simplyCopy(T o) {
        T newObj = null;
        try {
            newObj = (T) o.getClass().newInstance();
            PropertyUtils.copyProperties(newObj, o);
        } catch (Exception e) {
            // 因为是私有方法，知道进行拷贝的对象都有无参数的构造方法，
            // 而且是相同类型的对象拷贝属性，不会发生异常，所以不抛出。
            SCMEnv.error("简单拷贝发生异常", e);
        }
        return newObj;
    }

    /**
     * 将金税VO写入指定文件
     * 
     * @param goldTaxVOs 金税VO数组
     * @param filename 要写入的文件名
     */
    public void saveToFiles(GoldTaxVO[] goldTaxVOs, String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            BufferedOutputStream os = new BufferedOutputStream(fos);
            // 输出第一行
            if (goldTaxVOs.length > 0) {
                os.write(joinString(getAggregatedString(goldTaxVOs[0])).getBytes());
                os.write("\r\n".getBytes());
            }
            for (GoldTaxVO taxVO : goldTaxVOs) {
                // 输出表头
                os.write(joinString(getHeadString(taxVO)).getBytes());
                os.write("\r\n".getBytes());
                // 输出表体
                for (GoldTaxBodyVO bodyVO : taxVO.getChildrenVO()) {
                    os.write(joinString(getBodyString(bodyVO)).getBytes());
                    os.write("\r\n".getBytes());
                }
            }
            os.close();
        } catch (IOException e) {
            SCMEnv.error("写入到文件发生异常", e);
            throw new BusinessRuntimeException("写入到文件发生异常", e);
        }
    }

    /**
     * 从给定的文件中读取金税VO
     * 
     * @param filename 文件名
     * @return 读取的金税VO数组
     * @throws Exception
     */
    public GoldTaxVO[] loadFromFile(String filename) {
        List<GoldTaxVO> taxVOs = new ArrayList<GoldTaxVO>();
        GoldTaxVO curTaxVO = null;
        int lineNum = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            int rownum = -1;
            String[] firstLintContent = null;
            for (String line = reader.readLine(); null != line; line = reader.readLine()) {
                lineNum++;
                if (isComment(line) || isEmptyWithTrim(line)) {
                    continue;
                }
                String[] content = splitString(line);

                // 文件第一行，聚合VO信息
                if (null == firstLintContent) {
                    firstLintContent = content;
                    continue;
                }

                if (null == curTaxVO) {
                    curTaxVO = new GoldTaxVO();
                    taxVOs.add(curTaxVO);

                    rownum = getRownum(content);

                    curTaxVO.setParentVO(getHeadVO(content));
                    curTaxVO.setChildrenVO(new GoldTaxBodyVO[rownum]);
                } else {
                    curTaxVO.getChildrenVO()[--rownum] = getBodyVO(content);
                    if (rownum <= 0) {
                        curTaxVO = null;
                    }
                }
            }
        } catch (Exception e) {
            SCMEnv.error("读取文件发生异常，第 " + lineNum + " 行：" + e.getMessage(), e);
            throw new BusinessRuntimeException("读取文件发生异常，第 " + lineNum + " 行：" + e.getMessage(), e);
        }
        return taxVOs.toArray(new GoldTaxVO[taxVOs.size()]);
    }

    /**
     * 给定的行字符串是否属于注释行
     * 
     * @param line
     * @return
     */
    private boolean isComment(String line) {
        return line.startsWith(COMMENT_PREFIX);
    }

    /**
     * 连接字符串，用配置的分隔符将字符串数组连接为一个字符串
     * 
     * @param strs 待连接的字符串数组
     * @return 连接后的字符串
     * @see #splitString(String)
     */
    private String joinString(String[] strs) {
        StringBuffer buf = new StringBuffer();
        for (String str : strs) {
            if (buf.length() > 0) {
                buf.append(getConf().getSplit());
            }
            // 如果为空输出空格
            buf.append(isEmpty(str) ? " " : str);
        }
        return buf.toString();
    }

    /**
     * 分割字符串，用配置的分隔符将行字符串分割为字符串数组
     * 
     * @param line 要分割的字符串
     * @return 分割后的字符串数组
     * @see #joinString(String[])
     */
    private String[] splitString(String line) {
        return line.split(getConf().getSplit());
    }

    /**
     * 将聚合VO数据转换为字符串表示，一个字段一个字符串，导出到文件时按照数组顺序
     * 
     * @param taxVO 要转换的金税聚合VO
     * @return 用于输出到文件的字符串数组
     */
    private String[] getAggregatedString(GoldTaxVO taxVO) {
        return new String[]{
                taxVO.getBillIdentifier(),          // 表单标识
                taxVO.getBillName(),                // 表单名称
                taxVO.getSellCorpName(),            // 销售公司名称
        };
    }

    /**
     * 将表头数据转换为字符串表示，一个字段一个字符串，导出到文件时将按照数组顺序
     * 
     * @param taxVO 要转换的金税表头VO所在的金税VO
     * @return 用于输出到文件的字符串数组
     */
    private String[] getHeadString(GoldTaxVO taxVO) {
        GoldTaxHeadVO headVO = taxVO.getParentVO();
        return new String[]{
                headVO.getCode(),                   // 单据号
                "" + taxVO.getChildrenVO().length,  // 商品行数
                headVO.getCustomerName(),           // 购方名称
                headVO.getTaxPayerId(),             // 购方税号
                headVO.getSaleAddrPhone(),          // 购方地址电话
                headVO.getAccount(),                // 购方银行帐号
                headVO.getMemo(),                   // 备注
                headVO.getChecker(),                // 复核人
                headVO.getPayee(),                  // 收款人
                headVO.getRowInvName(),             // 清单行商品名称
                toStr(headVO.getBillDate()),        // 单据日期
                headVO.getSellAccount(),            // 单据日期
        };
    }

    private GoldTaxHeadVO getHeadVO(String[] content) {
        // 表头项目的个数
        final int HEAD_CONT_LEN = 13;
        if (content.length != HEAD_CONT_LEN) {
            SCMEnv.error("表头项目数不对");
            throw new BusinessRuntimeException("表头项目数不对【" + content.length + "】，应该是 " + HEAD_CONT_LEN + " 个");
        }

        GoldTaxHeadVO headVO = new GoldTaxHeadVO();
        int pos = 0;
        headVO.setCode(content[pos++]);             // 单据号
        pos++;                                      // 商品行数
        headVO.setCustomerName(content[pos++]);     // 购方名称
        headVO.setTaxPayerId(content[pos++]);       // 购方税号
        headVO.setSaleAddrPhone(content[pos++]);    // 购方地址电话
        headVO.setAccount(content[pos++]);          // 购方银行帐号
        headVO.setMemo(content[pos++]);             // 备注
        headVO.setChecker(content[pos++]);          // 复核人
        headVO.setPayee(content[pos++]);            // 收款人
        headVO.setRowInvName(content[pos++]);       // 清单行商品名称
        headVO.setBillDate(toUFDate(content[pos++]));// 单据日期
        headVO.setSellAccount(content[pos++]);      // 单据日期
        headVO.setTaxBillNo(content[pos++]);        // 金税票号
        return headVO;
    }

    /**
     * 从表头的字符串数组中得到商品行数
     * 
     * @param headContent 表头内容字符串数组
     * @return 商品行数
     */
    private int getRownum(String[] headContent) {
        try {
            return Integer.parseInt(headContent[1]);
        } catch (NumberFormatException e) {
            throw new BusinessRuntimeException("表头第 2 个项目是表体行数，必须是数字，现在是【" + headContent[1] + "】");
        }
    }

    private String toStr(UFDate date) {
        if (null == date) {
            return null;
        } else {
            return dateFormat.format(date.toDate());
        }
    }

    private UFDate toUFDate(String str) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            Date date = dateFormat.parse(str);
            return new UFDate(date);
        } catch (ParseException e) {
            SCMEnv.error("日期格式错误，不符合格式：yyyyMMdd", e);
            throw new BusinessRuntimeException("日期格式错误，不符合格式：yyyyMMdd", e);
        }
    }

    /**
     * 将表体数据转换为字符串表示，一个字段一个字符串，导出到文件时将按照数组顺序
     * 
     * @param bodyVO 要转换的金税表体VO
     * @return 用于输出到文件的字符串数组
     */
    private String[] getBodyString(GoldTaxBodyVO bodyVO) {
        return new String[]{
                // 货物名称
                getConf().isMergeInventory() ? bodyVO.getInvName() : bodyVO.getInvClassName(),
                bodyVO.getQuoteUnitName(),              // 计量单位
                bodyVO.getInvSpec(),                    // 规格
                getString(bodyVO.getNumber()),          // 数量
                //modify  by zhw 2011--10-26  输出无税金额
                getString(bodyVO.getNoTaxMny()),        // 金额
                getString(bodyVO.getTaxRate()),         // 税率
                "4001",                                 // 商品税目，固定为4001
                getString(bodyVO.getDiscountMny())      // 折扣金额
                //spf del begin
//              getString(getConf().getDiscountMode().format(bodyVO.getDiscountRate())),    // 折扣率
//              getString(bodyVO.getPrice()),           // 单价
//              "1",                                    // 价格方式，固定为 1
                //spf del end
        };
    }

    private GoldTaxBodyVO getBodyVO(String[] content) {
        // 表体项目的个数
        final int BODY_CONT_LEN = 13;
        if (content.length != BODY_CONT_LEN) {
            SCMEnv.error("表体项目数不对");
            throw new BusinessRuntimeException("表体项目数不对【" + content.length + "】，应该是 " + BODY_CONT_LEN + " 个");
        }

        GoldTaxBodyVO bodyVO = new GoldTaxBodyVO();
        int pos = 0;
        // 货物名称
        if (getConf().isMergeInventory()) {
            bodyVO.setInvName(content[pos++]);
        } else {
             bodyVO.setInvClassName(content[pos++]);
        }
        bodyVO.setQuoteUnitId(content[pos++]);              // 计量单位
        bodyVO.setInvSpec(content[pos++]);                  // 规格
        bodyVO.setNumber(toUFDouble(content[pos++]));       // 数量
        bodyVO.setMoney(toUFDouble(content[pos++]));        // 金额
        bodyVO.setTaxRate(toUFDouble(content[pos++]));      // 税率
        bodyVO.setTaxItems(content[pos++]);                 // 商品税目
        bodyVO.setDiscountMny(toUFDouble(content[pos++]));  // 折扣金额
        bodyVO.setTaxMny(toUFDouble(content[pos++]));       // 税额
        bodyVO.setDiscountTaxMny(toUFDouble(content[pos++]));// 折扣税额
        bodyVO.setDiscountRate(toUFDouble(content[pos++])); // 折扣率
        bodyVO.setPrice(toUFDouble(content[pos++]));        // 单价
        bodyVO.setPriceMode(toUFDouble(content[pos++]));    // 价格方式
        return bodyVO;
    }

    private UFDouble toUFDouble(String str) {
        if (isEmpty(str)) {
            return null;
        } else {
            return new UFDouble(str);
        }
    }

    private static String getString(Object o) {
        return null == o ? " " : o.toString();
    }
}
