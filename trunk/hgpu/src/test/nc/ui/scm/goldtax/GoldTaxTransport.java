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
 * ��˰�����࣬�ṩ��˰�ϲ�����֡��������ļ��ȷ���
 * 
 * @author ��ǿ��
 * @since 2008-8-27
 */
class GoldTaxTransport {
    /** ��˰�ļ�ע���е�ǰ׺ */
    private static final String COMMENT_PREFIX = "//";
    /** ��˰VO�пͻ�ID�������� */
    private static final String ATTR_CUSTOMER_ID = "customerId";
    /** ��˰VO�д���������� */
    private static final String ATTR_INVENTORY = "invBaseId";
    /** ��˰VO�д������������ */
    private static final String ATTR_INVCLASS = "invClassId";
    /** ��˰VO�м۸��������� */
    private static final String ATTR_PRICE = "price";

    /** ��˰VO�������������� */
    private static final String ATTR_QUANTITY = "number";
    /** ��˰VO�н���������� */
    private static final String ATTR_MONEY = "money";
    /** ��˰VO����˰����������� */
    private static final String ATTR_NOTAXMONEY = "noTaxMny";

    /** ��˾���� */
    private String corp = null;
    /** ����˰������Ϣ */
    private Configuration conf = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public GoldTaxTransport(String corp) {
        this.corp = corp;
    }

    /**
     * @return ����˰������Ϣ
     */
    Configuration getConf() {
        if (null == conf) {
            // װ�ز���
            conf = Configuration.load(corp);
        }
        return conf;
    }

    /**
     * @param conf ����˰������Ϣ
     */
    void setConf(Configuration conf) {
        this.conf = conf;
    }

    /**
     * �ϲ��ͷֵ������˰VO
     * 
     * @param goldTaxVOs ������Ľ�˰VO����
     * @return �����Ľ�˰VO����
     */
    public GoldTaxVO[] mergeAndSplit(GoldTaxVO[] goldTaxVOs) {
        // ���Ƚ��б�ͷ�ϲ�
        goldTaxVOs = mergeHead(goldTaxVOs);
        // �ٽ��б���ϲ�
        goldTaxVOs = mergeBody(goldTaxVOs);
        // �ٽ��зֵ�
        goldTaxVOs = split(goldTaxVOs);

        // ���¼���һЩ�ֶ�ֵ
        reCalcFields(goldTaxVOs);

        return goldTaxVOs;
    }

    /**
     * ���տͻ��ϲ���ͷ��Ϣ��������Ժϲ���
     * ��ϲ���ľۺ�VO������ֵȡ�ϲ��ı�ͷVO���е�һ����ͷ���ڵľۻ��ͷ�ϲ��Ľ�˰VO�ı�������һ�ϲ�Ϊһ������ŵ��ϲ���ľۺ�VO��ֵ��
     * ��ͷVO�úϲ������ɵ�VO������VO����Ϊ�������кϲ������б�ͷVO��Ӧ�ı���VO������
     * 
     * @param goldTaxVOs ��˰VO����
     * @return �ϲ���ͷ��Ľ�˰VO����
     */
    private GoldTaxVO[] mergeHead(GoldTaxVO[] goldTaxVOs) {
        // ���û�ж൥�ϲ�������Ҫ�ϲ���ͷ��ֱ�ӷ���
        if (!getConf().isMergeMutiBill()) {
            return goldTaxVOs;
        }

        Map<String, List<GoldTaxVO>> mapOfCust = new HashMap<String, List<GoldTaxVO>>();
        GoldTaxHeadVO[] allHeadVOs = new GoldTaxHeadVO[goldTaxVOs.length];
        // ��ͷ���ͻ�����
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

        // �ϲ���ͷ
        GoldTaxHeadVO[] mergedHeadVO = null;
        DefaultVOMerger merger = new DefaultVOMerger();
        merger.setGroupingAttr(new String[]{ATTR_CUSTOMER_ID});
        try {
            mergedHeadVO = (GoldTaxHeadVO[]) merger.mergeByGroup(allHeadVOs);
        } catch (BusinessException e) {
            SCMEnv.error("�ϲ������쳣", e);
            throw new BusinessRuntimeException(e.getMessage(), e);
        }

        // �µĺϲ���ľۺ�VO����
        GoldTaxVO[] mergedTaxVO = new GoldTaxVO[mergedHeadVO.length];
        for (int i = 0; i < mergedHeadVO.length; i++) {
            GoldTaxHeadVO taxHeadVO = mergedHeadVO[i];

            // ���ܺϲ��˱�ͷ�ı���VO
            List<GoldTaxBodyVO> bodyVOsOfCust = new ArrayList<GoldTaxBodyVO>();
            // ���а����ĵ��ݺ�
            Set<String> codes = new HashSet<String>();
            List<GoldTaxVO> taxVoOfCust = mapOfCust.get(taxHeadVO.getCustomerId());
            for (GoldTaxVO taxVO : taxVoOfCust) {
                bodyVOsOfCust.addAll(Arrays.asList(taxVO.getChildrenVO()));
                codes.add(taxVO.getParentVO().getCode());
            }

            // �µĵ��ݺ�
            String newCode = "";
            for (String code : codes) {
                newCode += code;
            }

            // �µľۺ�VO
            GoldTaxVO taxVO = simplyCopy(taxVoOfCust.get(0));
            // ��ͷVOΪ�ϲ���ı�ͷ
            taxVO.setParentVO(taxHeadVO);
            // �����µ��ݺ�
            taxHeadVO.setCode(newCode);
            // ����VOΪ�����ϲ������б�ͷVO��Ӧ�ı���VO����
            taxVO.setChildrenVO(bodyVOsOfCust.toArray(new GoldTaxBodyVO[bodyVOsOfCust.size()]));
            mergedTaxVO[i] = taxVO;
        }

        return mergedTaxVO;
    }

    /**
     * ��ÿ����˰VO�ı�����кϲ�
     * 
     * @param goldTaxVOs
     * @return �ϲ������Ľ�˰VO����
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

        // ���û�кϲ��ֶΣ�����Ҫ�ϲ���ֱ�ӷ���
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
                SCMEnv.error("�ϲ������쳣", e);
                throw new BusinessRuntimeException(e.getMessage(), e);
            }
        }
        return goldTaxVOs;
    }

    /**
     * ���¼���һЩ�ֶ�ֵ���������������
     * 
     * @param taxBodyVOs Ҫ���¼��㵥�۵Ľ�˰����VO����
     */
    private void reCalcFields(GoldTaxVO[] taxVOs) {
        for (GoldTaxVO goldTaxVO : taxVOs) {
            for (GoldTaxBodyVO bodyVO : goldTaxVO.getChildrenVO()) {
                // ��˰����
                bodyVO.setPrice(bodyVO.getMoney().div(bodyVO.getNumber(), bodyVO.getMoney().getPower()));
                // ��˰����
                bodyVO.setNoTaxPrice(bodyVO.getNoTaxMny().div(bodyVO.getNumber(), bodyVO.getNoTaxMny().getPower()));
                if (null != bodyVO.getTaxRate()) {
                    bodyVO.setTaxMny(bodyVO.getNoTaxMny().multiply(bodyVO.getTaxRate(), bodyVO.getNoTaxMny().getPower()));
                }
            }
        }
    }

    /**
     * �Խ�˰������˰����޶���������зֵ�
     * 
     * @param taxVOs ���ֵ��Ľ�˰VO����
     * @return �ֵ���Ľ�˰VO����
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
                    // ����
                    GoldTaxVO[] splitedVO = splitRow(curTax, rownum);
                    splited.add(splitedVO[0]);
                    orgTaxes.add(0, splitedVO[1]);
                    split = true;
                    break;
                } else if (needSplitMny(sumMny.add(bodyVO.getNoTaxMny()))) {
                    // ����
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
                // ����Ҫ�ֵ���ֱ�ӷ����ѷֵ��б�
                splited.add(curTax);
            }
        }
        return splited.toArray(new GoldTaxVO[splited.size()]);
    }

    /**
     * ���ݸ����������ж��Ƿ���Ҫ�ֵ�
     * 
     * @param rownum ����
     * @return �����Ҫ�ֵ�����true�����򷵻�false
     */
    boolean needSplitRow(int rownum) {
        int maxRows = getConf().getMaxRows();
        return maxRows > 0 && rownum > maxRows;
    }

    /**
     * ���ݸ����������ֵ�
     * 
     * @param rownum
     *            ����
     * @return ����Ϊ2�Ľ�˰VO���飬��һ��Ԫ��Ϊ�ֳ���VO���ڶ���Ԫ��Ϊʣ�µ�VO
     */
    GoldTaxVO[] splitRow(GoldTaxVO taxVO, int rownum) {
        // �ֳ���VO
        GoldTaxBodyVO[] bodyVOs1 = new GoldTaxBodyVO[rownum];
        System.arraycopy(taxVO.getChildrenVO(), 0, bodyVOs1, 0, bodyVOs1.length);
        GoldTaxHeadVO headVO1 = simplyCopy(taxVO.getParentVO());
        GoldTaxVO taxVO1 = simplyCopy(taxVO);
        taxVO1.setParentVO(headVO1);
        taxVO1.setChildrenVO(bodyVOs1);
        // ʣ���VO
        GoldTaxBodyVO[] bodyVOs2 = new GoldTaxBodyVO[taxVO.getChildrenVO().length - rownum];
        System.arraycopy(taxVO.getChildrenVO(), rownum, bodyVOs2, 0, bodyVOs2.length);
        GoldTaxHeadVO headVO2 = simplyCopy(taxVO.getParentVO());
        GoldTaxVO taxVO2 = simplyCopy(taxVO);
        taxVO2.setParentVO(headVO2);
        taxVO2.setChildrenVO(bodyVOs2);
        // ���ص�VO����
        GoldTaxVO[] splited = new GoldTaxVO[2];
        splited[0] = taxVO1;
        splited[1] = taxVO2;
        return splited;
    }

    /**
     * ���ݸ����Ľ���ж��Ƿ���Ҫ�ֵ�
     * 
     * @param money Ҫ�жϵ���˰���
     * @return �����Ҫ�ֵ�����true�����򷵻�false
     */
    boolean needSplitMny(UFDouble money) {
        UFDouble maxNoTaxMny = getConf().getMaxNoTaxMny();
        return (null != maxNoTaxMny) 
                && maxNoTaxMny.compareTo(UFDouble.ZERO_DBL) > 0
                && money.compareTo(maxNoTaxMny) > 0;
    }

    /**
     * ���ݸ����ķֵ�
     * 
     * @param rownum
     *            ����
     * @return ����Ϊ2�Ľ�˰VO���飬��һ��Ԫ��Ϊ�ֳ���VO���ڶ���Ԫ��Ϊʣ�µ�VO
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
                // ���޶����ʱ�����Ƿ����Ϊ��������������������� 1 ʱ������ȥС��
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
            // ���������������㣬���ʾ����Ҫ��������ֻ��Ҫ���зֵ��Ϳ�����
            return splitRow(taxVO, splitRownum);
        }
        // �ֳ���VO
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
        // ʣ���VO
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
        // ���ص�VO����
        GoldTaxVO[] splited = new GoldTaxVO[2];
        splited[0] = taxVO1;
        splited[1] = taxVO2;
        return splited;
    }

    /**
     * �򵥿���
     * 
     * @param <T> ����Ҫ���޲����Ĺ��췽��
     * @param o �������Ķ���
     * @return ���������Ե��¶���
     */
    @SuppressWarnings("unchecked")
    private static <T> T simplyCopy(T o) {
        T newObj = null;
        try {
            newObj = (T) o.getClass().newInstance();
            PropertyUtils.copyProperties(newObj, o);
        } catch (Exception e) {
            // ��Ϊ��˽�з�����֪�����п����Ķ������޲����Ĺ��췽����
            // ��������ͬ���͵Ķ��󿽱����ԣ����ᷢ���쳣�����Բ��׳���
            SCMEnv.error("�򵥿��������쳣", e);
        }
        return newObj;
    }

    /**
     * ����˰VOд��ָ���ļ�
     * 
     * @param goldTaxVOs ��˰VO����
     * @param filename Ҫд����ļ���
     */
    public void saveToFiles(GoldTaxVO[] goldTaxVOs, String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            BufferedOutputStream os = new BufferedOutputStream(fos);
            // �����һ��
            if (goldTaxVOs.length > 0) {
                os.write(joinString(getAggregatedString(goldTaxVOs[0])).getBytes());
                os.write("\r\n".getBytes());
            }
            for (GoldTaxVO taxVO : goldTaxVOs) {
                // �����ͷ
                os.write(joinString(getHeadString(taxVO)).getBytes());
                os.write("\r\n".getBytes());
                // �������
                for (GoldTaxBodyVO bodyVO : taxVO.getChildrenVO()) {
                    os.write(joinString(getBodyString(bodyVO)).getBytes());
                    os.write("\r\n".getBytes());
                }
            }
            os.close();
        } catch (IOException e) {
            SCMEnv.error("д�뵽�ļ������쳣", e);
            throw new BusinessRuntimeException("д�뵽�ļ������쳣", e);
        }
    }

    /**
     * �Ӹ������ļ��ж�ȡ��˰VO
     * 
     * @param filename �ļ���
     * @return ��ȡ�Ľ�˰VO����
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

                // �ļ���һ�У��ۺ�VO��Ϣ
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
            SCMEnv.error("��ȡ�ļ������쳣���� " + lineNum + " �У�" + e.getMessage(), e);
            throw new BusinessRuntimeException("��ȡ�ļ������쳣���� " + lineNum + " �У�" + e.getMessage(), e);
        }
        return taxVOs.toArray(new GoldTaxVO[taxVOs.size()]);
    }

    /**
     * ���������ַ����Ƿ�����ע����
     * 
     * @param line
     * @return
     */
    private boolean isComment(String line) {
        return line.startsWith(COMMENT_PREFIX);
    }

    /**
     * �����ַ����������õķָ������ַ�����������Ϊһ���ַ���
     * 
     * @param strs �����ӵ��ַ�������
     * @return ���Ӻ���ַ���
     * @see #splitString(String)
     */
    private String joinString(String[] strs) {
        StringBuffer buf = new StringBuffer();
        for (String str : strs) {
            if (buf.length() > 0) {
                buf.append(getConf().getSplit());
            }
            // ���Ϊ������ո�
            buf.append(isEmpty(str) ? " " : str);
        }
        return buf.toString();
    }

    /**
     * �ָ��ַ����������õķָ��������ַ����ָ�Ϊ�ַ�������
     * 
     * @param line Ҫ�ָ���ַ���
     * @return �ָ����ַ�������
     * @see #joinString(String[])
     */
    private String[] splitString(String line) {
        return line.split(getConf().getSplit());
    }

    /**
     * ���ۺ�VO����ת��Ϊ�ַ�����ʾ��һ���ֶ�һ���ַ������������ļ�ʱ��������˳��
     * 
     * @param taxVO Ҫת���Ľ�˰�ۺ�VO
     * @return ����������ļ����ַ�������
     */
    private String[] getAggregatedString(GoldTaxVO taxVO) {
        return new String[]{
                taxVO.getBillIdentifier(),          // ����ʶ
                taxVO.getBillName(),                // ������
                taxVO.getSellCorpName(),            // ���۹�˾����
        };
    }

    /**
     * ����ͷ����ת��Ϊ�ַ�����ʾ��һ���ֶ�һ���ַ������������ļ�ʱ����������˳��
     * 
     * @param taxVO Ҫת���Ľ�˰��ͷVO���ڵĽ�˰VO
     * @return ����������ļ����ַ�������
     */
    private String[] getHeadString(GoldTaxVO taxVO) {
        GoldTaxHeadVO headVO = taxVO.getParentVO();
        return new String[]{
                headVO.getCode(),                   // ���ݺ�
                "" + taxVO.getChildrenVO().length,  // ��Ʒ����
                headVO.getCustomerName(),           // ��������
                headVO.getTaxPayerId(),             // ����˰��
                headVO.getSaleAddrPhone(),          // ������ַ�绰
                headVO.getAccount(),                // ���������ʺ�
                headVO.getMemo(),                   // ��ע
                headVO.getChecker(),                // ������
                headVO.getPayee(),                  // �տ���
                headVO.getRowInvName(),             // �嵥����Ʒ����
                toStr(headVO.getBillDate()),        // ��������
                headVO.getSellAccount(),            // ��������
        };
    }

    private GoldTaxHeadVO getHeadVO(String[] content) {
        // ��ͷ��Ŀ�ĸ���
        final int HEAD_CONT_LEN = 13;
        if (content.length != HEAD_CONT_LEN) {
            SCMEnv.error("��ͷ��Ŀ������");
            throw new BusinessRuntimeException("��ͷ��Ŀ�����ԡ�" + content.length + "����Ӧ���� " + HEAD_CONT_LEN + " ��");
        }

        GoldTaxHeadVO headVO = new GoldTaxHeadVO();
        int pos = 0;
        headVO.setCode(content[pos++]);             // ���ݺ�
        pos++;                                      // ��Ʒ����
        headVO.setCustomerName(content[pos++]);     // ��������
        headVO.setTaxPayerId(content[pos++]);       // ����˰��
        headVO.setSaleAddrPhone(content[pos++]);    // ������ַ�绰
        headVO.setAccount(content[pos++]);          // ���������ʺ�
        headVO.setMemo(content[pos++]);             // ��ע
        headVO.setChecker(content[pos++]);          // ������
        headVO.setPayee(content[pos++]);            // �տ���
        headVO.setRowInvName(content[pos++]);       // �嵥����Ʒ����
        headVO.setBillDate(toUFDate(content[pos++]));// ��������
        headVO.setSellAccount(content[pos++]);      // ��������
        headVO.setTaxBillNo(content[pos++]);        // ��˰Ʊ��
        return headVO;
    }

    /**
     * �ӱ�ͷ���ַ��������еõ���Ʒ����
     * 
     * @param headContent ��ͷ�����ַ�������
     * @return ��Ʒ����
     */
    private int getRownum(String[] headContent) {
        try {
            return Integer.parseInt(headContent[1]);
        } catch (NumberFormatException e) {
            throw new BusinessRuntimeException("��ͷ�� 2 ����Ŀ�Ǳ������������������֣������ǡ�" + headContent[1] + "��");
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
            SCMEnv.error("���ڸ�ʽ���󣬲����ϸ�ʽ��yyyyMMdd", e);
            throw new BusinessRuntimeException("���ڸ�ʽ���󣬲����ϸ�ʽ��yyyyMMdd", e);
        }
    }

    /**
     * ����������ת��Ϊ�ַ�����ʾ��һ���ֶ�һ���ַ������������ļ�ʱ����������˳��
     * 
     * @param bodyVO Ҫת���Ľ�˰����VO
     * @return ����������ļ����ַ�������
     */
    private String[] getBodyString(GoldTaxBodyVO bodyVO) {
        return new String[]{
                // ��������
                getConf().isMergeInventory() ? bodyVO.getInvName() : bodyVO.getInvClassName(),
                bodyVO.getQuoteUnitName(),              // ������λ
                bodyVO.getInvSpec(),                    // ���
                getString(bodyVO.getNumber()),          // ����
                //modify  by zhw 2011--10-26  �����˰���
                getString(bodyVO.getNoTaxMny()),        // ���
                getString(bodyVO.getTaxRate()),         // ˰��
                "4001",                                 // ��Ʒ˰Ŀ���̶�Ϊ4001
                getString(bodyVO.getDiscountMny())      // �ۿ۽��
                //spf del begin
//              getString(getConf().getDiscountMode().format(bodyVO.getDiscountRate())),    // �ۿ���
//              getString(bodyVO.getPrice()),           // ����
//              "1",                                    // �۸�ʽ���̶�Ϊ 1
                //spf del end
        };
    }

    private GoldTaxBodyVO getBodyVO(String[] content) {
        // ������Ŀ�ĸ���
        final int BODY_CONT_LEN = 13;
        if (content.length != BODY_CONT_LEN) {
            SCMEnv.error("������Ŀ������");
            throw new BusinessRuntimeException("������Ŀ�����ԡ�" + content.length + "����Ӧ���� " + BODY_CONT_LEN + " ��");
        }

        GoldTaxBodyVO bodyVO = new GoldTaxBodyVO();
        int pos = 0;
        // ��������
        if (getConf().isMergeInventory()) {
            bodyVO.setInvName(content[pos++]);
        } else {
             bodyVO.setInvClassName(content[pos++]);
        }
        bodyVO.setQuoteUnitId(content[pos++]);              // ������λ
        bodyVO.setInvSpec(content[pos++]);                  // ���
        bodyVO.setNumber(toUFDouble(content[pos++]));       // ����
        bodyVO.setMoney(toUFDouble(content[pos++]));        // ���
        bodyVO.setTaxRate(toUFDouble(content[pos++]));      // ˰��
        bodyVO.setTaxItems(content[pos++]);                 // ��Ʒ˰Ŀ
        bodyVO.setDiscountMny(toUFDouble(content[pos++]));  // �ۿ۽��
        bodyVO.setTaxMny(toUFDouble(content[pos++]));       // ˰��
        bodyVO.setDiscountTaxMny(toUFDouble(content[pos++]));// �ۿ�˰��
        bodyVO.setDiscountRate(toUFDouble(content[pos++])); // �ۿ���
        bodyVO.setPrice(toUFDouble(content[pos++]));        // ����
        bodyVO.setPriceMode(toUFDouble(content[pos++]));    // �۸�ʽ
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
