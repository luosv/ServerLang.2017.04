package com.ui;

import com.excel.ConfigExcelReader;
import com.excel.ExcelLists;
import com.excel.OptionConfigure;
import com.excel.data.Data;
import com.excel.data.Field;
import com.log.TextAreaLogAppender;
import com.util.LangNumSort;
import com.util.TextFile;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {

    private JScrollPane jScrollPane;
    private JTextArea jTextArea;

    private static final Log log = LogFactory.getLog(MainFrame.class);

    private HashMap<String, Integer> StringNo = new HashMap<>(); // 编号
    private int maxNo = 0;
    private static final String DENG_HAO = "=";

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        jScrollPane = new JScrollPane();
        jTextArea = new JTextArea();

        setTitle("ServerLang 2017.4");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jTextArea.setColumns(20);
        jTextArea.setRows(5);
        jTextArea.setEditable(false);
        jScrollPane.setViewportView(jTextArea);

        JButton jButton1 = new JButton();
        JButton jButton2 = new JButton();
        JButton jButton3 = new JButton();
        JButton jButton4 = new JButton();

        jButton1.setText("导出语言包原始文件");
        jButton1.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // NOI18N
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("将语言包写入项目");
        jButton2.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // NOI18N
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("语言包校验");
        jButton3.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // NOI18N
        jButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("语言翻译检测");
        jButton4.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // NOI18N
        jButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(10, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 145
                                                        , GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton2, GroupLayout.PREFERRED_SIZE, 145
                                                        , GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton3, GroupLayout.PREFERRED_SIZE, 145
                                                        , GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton4, GroupLayout.PREFERRED_SIZE, 145
                                                        , GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED
                                                        , 2, Short.MAX_VALUE)))
                                .addContainerGap(10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButton1)
                                                .addComponent(jButton2)
                                                .addComponent(jButton3)
                                                .addComponent(jButton4)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                                .addContainerGap())
        );
        pack();
    }

    public void initLog() {
        try {
            Thread t;
            t = new TextAreaLogAppender(jTextArea, jScrollPane);
            t.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "绑定日志输出组件错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void init() {
        log.info("update-2017.04.28" + "\n");
        // 原有语言条数
        try {
            Properties properties = new Properties();
            // 以language_cn.properties为准
            String propertiesPath = SystemUtils.USER_DIR + File.separator + "language_cn.properties";
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(propertiesPath)
                    , "utf-8"));
            properties.load(bufferedReader);
            Set keyValue = properties.keySet();
            for (Object value : keyValue) {
                String key = (String) value;
                String property = properties.getProperty(key);
                String no = key.substring("str".length());
                int nn = Integer.parseInt(no);
                StringNo.put(property, nn);
                if (nn > maxNo) {
                    maxNo = nn;
                }
            }
            log.info("读取完原有语言" + StringNo.size() + "条，" + "最大编号" + maxNo + "\n");
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        log.info("====================================");
        log.info("                    导出语言包原始文件");
        log.info("====================================");
        List<String> excelList = ExcelLists.getFileList();
        if (excelList == null || excelList.size() < 1) {
            log.error("当前选择的目录没有excel文件！");
            return;
        }
        maxNo += 1;
        List<String> tab = OptionConfigure.getInstance().getTableCache();
        HashMap<String, String> result = new HashMap<>();
        //去掉老的字符串
        List<String> newResult = new ArrayList<>();
        for (String fileName : excelList) {
            ConfigExcelReader excelReader = new ConfigExcelReader();
            ArrayList<Data> excelContent = excelReader.load(fileName);
            for (Data dataInfo : excelContent) {
                if (!tab.contains(dataInfo.getSheetName().toLowerCase())) {
                    continue;
                }
                if ("guild_question".equals(dataInfo.getSheetName().toLowerCase())) {
                    int index = tab.indexOf(dataInfo.getSheetName().toLowerCase());
                    int k = index;
                    int j = 0;
                    for (int i = index; i <= k + 4; i++, index++) {
                        String fieldStr = OptionConfigure.getInstance().getFieldList().get(index);
                        String id = OptionConfigure.getInstance().getOnlyKey().get(index);
                        if (StringUtils.isBlank(fieldStr) || StringUtils.isBlank(id)) {
                            log.error(dataInfo.getSheetName() + " 没有字段名或者指定唯一KEY关键字！");
                            continue;
                        }
                        int dIndex = -1;
                        for (int o = 0; o < dataInfo.getFields().size(); ++o) {
                            Field f = dataInfo.getFields().get(o);
                            if (f.getName().equalsIgnoreCase(fieldStr)) {
                                dIndex = o;
                                break;
                            }
                        }
                        if (dIndex < 0) {
                            log.error("关键字 " + fieldStr + " 在表" + dataInfo.getSheetName() + " 文件中查找不到！" + "");
                            continue;
                        }
                        if (dataInfo.getData() == null) {
                            continue;
                        }
                        for (String[] data : dataInfo.getData()) {
                            if (data.length < 1 || (data[0] == null)) {
                                continue;
                            }
                            // 日志发现大量“xxx_0=//xxx的值”，原因是数据表末尾存在大量空白行，导致读出的key值为0，在此处判断忽略掉空白行
                            if (StringUtils.isBlank(data[dataInfo.getKey().getCell()]) || "0".equals(data[dataInfo.getKey().getCell()])) {
                                continue;
                            }
                            if (!newResult.contains(data[dIndex])) {
                                newResult.add(data[dIndex]);
                            }
                            if (!StringNo.containsKey(data[dIndex])) {
                                StringNo.put(data[dIndex], maxNo++);
                            }
                            result.put(dataInfo.getSheetName().toLowerCase() + "_" + data[dataInfo.getKey().getCell()]+"_"+j, data[dIndex]);
                            log.info(dataInfo.getSheetName().toLowerCase() + "_" + data[dataInfo.getKey().getCell()]+"_"+j
                                    + "=" + data[dIndex] + "//" + fieldStr + "的值");
                        }
                        j++;
                    }
                }else if("server_world_question".equals(dataInfo.getSheetName().toLowerCase())){
                    int index = tab.indexOf(dataInfo.getSheetName().toLowerCase());
                    int k = index;
                    int j = 0;
                    for (int i = index; i <= k + 4; i++, index++) {
                        String fieldStr = OptionConfigure.getInstance().getFieldList().get(index);
                        String id = OptionConfigure.getInstance().getOnlyKey().get(index);
                        if (StringUtils.isBlank(fieldStr) || StringUtils.isBlank(id)) {
                            log.error(dataInfo.getSheetName() + " 没有字段名或者指定唯一KEY关键字！");
                            continue;
                        }
                        int dIndex = -1;
                        for (int o = 0; o < dataInfo.getFields().size(); ++o) {
                            Field f = dataInfo.getFields().get(o);
                            if (f.getName().equalsIgnoreCase(fieldStr)) {
                                dIndex = o;
                                break;
                            }
                        }
                        if (dIndex < 0) {
                            log.error("关键字 " + fieldStr + " 在表" + dataInfo.getSheetName() + " 文件中查找不到！" + "");
                            continue;
                        }
                        if (dataInfo.getData() == null) {
                            continue;
                        }
                        for (String[] data : dataInfo.getData()) {
                            if (data.length < 1 || (data[0] == null)) {
                                continue;
                            }
                            // 日志发现大量“xxx_0=//xxx的值”，原因是数据表末尾存在大量空白行，导致读出的key值为0，在此处判断忽略掉空白行
                            if (StringUtils.isBlank(data[dataInfo.getKey().getCell()]) || "0".equals(data[dataInfo.getKey().getCell()])) {
                                continue;
                            }
                            if (!newResult.contains(data[dIndex])) {
                                newResult.add(data[dIndex]);
                            }
                            if (!StringNo.containsKey(data[dIndex])) {
                                StringNo.put(data[dIndex], maxNo++);
                            }
                            result.put(dataInfo.getSheetName().toLowerCase() + "_" + data[dataInfo.getKey().getCell()]+"_"+j, data[dIndex]);
                            log.info(dataInfo.getSheetName().toLowerCase() + "_" + data[dataInfo.getKey().getCell()]+"_"+j
                                    + "=" + data[dIndex] + "//" + fieldStr + "的值");
                        }
                        j++;
                    }
                }else {
                    int index = tab.indexOf(dataInfo.getSheetName().toLowerCase());
                    String fieldStr = OptionConfigure.getInstance().getFieldList().get(index);
                    String id = OptionConfigure.getInstance().getOnlyKey().get(index);
                    if (StringUtils.isBlank(fieldStr) || StringUtils.isBlank(id)) {
                        log.error(dataInfo.getSheetName() + " 没有字段名或者指定唯一KEY关键字！");
                        continue;
                    }
                    int dIndex = -1;
                    for (int o = 0; o < dataInfo.getFields().size(); ++o) {
                        Field f = dataInfo.getFields().get(o);
                        if (f.getName().equalsIgnoreCase(fieldStr)) {
                            dIndex = o;
                            break;
                        }
                    }
                    if (dIndex < 0) {
                        log.error("关键字 " + fieldStr + " 在表" + dataInfo.getSheetName() + " 文件中查找不到！" + "");
                        continue;
                    }
                    if (dataInfo.getData() == null) {
                        continue;
                    }
                    for (String[] data : dataInfo.getData()) {
                        if (data.length < 1 || (data[0] == null)) {
                            continue;
                        }
                        // 日志发现大量“xxx_0=//xxx的值”，原因是数据表末尾存在大量空白行，导致读出的key值为0，在此处判断忽略掉空白行
                        if (StringUtils.isBlank(data[dataInfo.getKey().getCell()]) || "0".equals(data[dataInfo.getKey().getCell()])) {
                            continue;
                        }
                        //如果是装备， 则把装备名字也放入到物品中，同时也在装备中加入一个
                        if (dataInfo.getSheetName().equalsIgnoreCase("equip")) {
                            result.put("item_" + data[dataInfo.getKey().getCell()], data[dIndex]);
                        }
                        if (!newResult.contains(data[dIndex])) {
                            newResult.add(data[dIndex]);
                        }
                        if (!StringNo.containsKey(data[dIndex])) {
                            StringNo.put(data[dIndex], maxNo++);
                        }
                        result.put(dataInfo.getSheetName().toLowerCase() + "_" + data[dataInfo.getKey().getCell()], data[dIndex]);
                        log.info(dataInfo.getSheetName().toLowerCase() + "_" + data[dataInfo.getKey().getCell()]
                                + "=" + data[dIndex] + "//" + fieldStr + "的值");
                    }
                }
            }
        }
        if (result.size() < 1) {
            log.error("没有找到任何的结果，请确认数据是否正确！" + "\n");
            return;
        }
        log.info("原始文件有语言" + StringNo.size() + "条！");
        HashMap<String, Integer> NumNew = new HashMap<>();
        for (String str : newResult) {
            if (StringNo.containsKey(str)) {
                NumNew.put(str, StringNo.get(str));
            }
        }
        log.info("去掉多的语言后有" + NumNew.size() + "条！");
        log.info("开始写入文件！");
        try {
            File f = new File("language_cn.properties");
            List<String> langRes = new ArrayList<>();
            List<Map.Entry<String, Integer>> infoIds = new ArrayList<>(NumNew.entrySet());
            Collections.sort(infoIds, new LangNumSort());
            //排名处理
            int lastNum = 0;
            for (Map.Entry<String, Integer> entry : infoIds) {
                lastNum = entry.getValue();
                langRes.add("str" + entry.getValue() + "=" + entry.getKey());
            }
            TextFile.write(langRes, f);
            //记录最后一个键值对
            File keyF = new File("lastKey.properties");
            if (!keyF.exists() || !keyF.isFile()) {
                log.error("文件 lastKey.properties 不存在！");
                int res = JOptionPane.showConfirmDialog(null, "文件 lastKey.properties 不存在！"
                        + " 是否创建？", "error", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    if (keyF.createNewFile()) {
                        log.info("文件 lastKey.properties 创建成功！");
                    } else {
                        log.error("文件 lastKey.properties 创建失败！" + "\n");
                        return;
                    }
                } else {
                    log.error("您可以从其他地方拷贝 lastKey.properties 到项目根目录下，但请确保其正确性！" + "\n");
                    return;
                }
            }
            List<String> lastKeyRes = getLangList("lastKey.properties");
            String lastKeyStr = "str" + lastNum;
            if (lastKeyRes.size() == 0 || !lastKeyRes.get(lastKeyRes.size() - 1).equals(lastKeyStr)) {
                lastKeyRes.add(lastKeyStr);
                TextFile.write(lastKeyRes, keyF);
            }
            File no = new File("languageNo.properties");
            Iterator<Map.Entry<String, String>> site = result.entrySet().iterator();
            langRes.clear();
            while (site.hasNext()) {
                Map.Entry<String, String> en = site.next();
                Integer ns = NumNew.get(en.getValue());
                if (ns == null) {
                    log.error(en.getKey() + "=" + en.getValue() + "没有找到ID号， 在原始的ID号："
                            + StringNo.get(en.getValue()));
                }
                langRes.add(en.getKey() + "=" + ns);
            }
            TextFile.write(langRes, no);
        } catch (IOException ex) {
            log.info(ex.getLocalizedMessage());
        }
        log.info("导出语言包原始文件完成！" + "\n");
    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        log.info("====================================");
        log.info("                      将语言包写入项目");
        log.info("====================================");
        List<String> projectPathList = OptionConfigure.getInstance().getProjectPaths();
        List<String> langFileList = OptionConfigure.getInstance().getLangFiles();
        String langCnFileName = "language_cn.properties";
        String langNoFileName = "languageNo.properties";
        try {
            //读取重新生成的语言文件，并解析
            List<String> newLangCnList = getLangList(langCnFileName);
            List<String> newLangNoList = getLangList(langNoFileName);
            List<String> lastKeyRes = getLangList("lastKey.properties");
            if (lastKeyRes.size() < 2) {
                log.error("文件lastKey.properties中的键值数量小于2，不符合写入要求，因此不做插入操作，请用比对工具！");
            }
            String lastKey = lastKeyRes.get(lastKeyRes.size() - 2);
            String keyStr = newLangCnList.get(newLangCnList.size() - 1);
            keyStr = keyStr.substring(0, keyStr.indexOf("="));
            int addNew = Integer.parseInt(keyStr.substring(3, keyStr.length()))
                    - Integer.parseInt(lastKey.substring(3, lastKey.length()));
            log.info("新增语言条数：" + addNew);
            List<String> newLangList = newLangCnList.subList(newLangCnList.size() - addNew, newLangCnList.size());
            log.info("新增语言：" + newLangList);
            for (String projectPath : projectPathList) {
                for (String langFile : langFileList) {
                    String path = projectPath + File.separator + langFile;
                    List<String> serverLangList = getLangList(path);
                    if (serverLangList.size() == 0 || !serverLangList.get(serverLangList.size() - 1).split(DENG_HAO)[0]
                            .equals(newLangList.get(newLangList.size() - 1).split(DENG_HAO)[0])) {
                        log.info("写入路劲：" + path);
                        serverLangList.addAll(newLangList);
                        TextFile.write(serverLangList, new File(path));
                        log.info("操作完成，共计" + serverLangList.size() + "条语言");
                    } else {
                        log.error(langFile + " 已经是最新了！");
                    }
                }
                //写入languageNo文件
                String noPath = projectPath + File.separator + langNoFileName;
                TextFile.write(newLangNoList, new File(noPath));
                //写入国内语言包
                String cnPath = projectPath + File.separator + langCnFileName;
                TextFile.write(newLangCnList, new File(cnPath));
            }
            log.info("语言包写入项目完成!" + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发生异常了：" + e.toString());
        }
    }

    private void jButton3ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        log.info("====================================");
        log.info("                           语言包校验");
        log.info("====================================");
        List<String> projectPathList = OptionConfigure.getInstance().getProjectPaths();
        List<String> langFileList = OptionConfigure.getInstance().getLangFiles();
        String langNoFileName = "languageNo.properties";
        try {
            for (String projectPath : projectPathList) {
                //languageNo文件
                String noPath = projectPath + File.separator + langNoFileName;
                Properties propNo = getLangProperties(noPath);
                Set keyNoValue = propNo.keySet();
                Set<String> keyCnList = new HashSet<>();
                for (Object aKeyNoValue : keyNoValue) {
                    String noKey = (String) aKeyNoValue;
                    String number = propNo.getProperty(noKey);
                    keyCnList.add("str" + number);
                }
                //language_cn文件
                for (String langFile : langFileList) {
                    String cnPath = projectPath + File.separator + langFile;
                    Properties propCn = getLangProperties(cnPath);
                    List<String> keys = new ArrayList<>();
                    for (String cnKey : keyCnList) {
                        if (propCn.get(cnKey) == null) {
                            keys.add(cnKey);
                        }
                    }
                    log.info("路径：" + cnPath + " 扫描完成！");
                    if (!keys.isEmpty()) {
                        log.info("错漏语言 " + keys.size() + " 条：" + keys);
                    } else {
                        log.info("未发现有错漏语言！");
                    }
                }
            }
            log.info("语言包校验完成！" + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jButton4ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        log.info("====================================");
        log.info("                         语言翻译检测");
        log.info("====================================");
        List<String> projectPathList = OptionConfigure.getInstance().getProjectPaths();
        List<String> langFileList = OptionConfigure.getInstance().getLangFiles();
        String langNoFileName = "languageNo.properties";
        String langCnFileName = "language_cn.properties";
        try {
            for (String projectPath : projectPathList) {
                //languageNo文件
                String noPath = projectPath + File.separator + langNoFileName;
                Properties propNo = getLangProperties(noPath);
                Set keyNoValue = propNo.keySet();
                Set<String> keyCnList = new HashSet<>();
                for (Object aKeyNoValue : keyNoValue) {
                    String noKey = (String) aKeyNoValue;
                    String number = propNo.getProperty(noKey);
                    keyCnList.add("str" + number);
                }
                String cnPath = projectPath + File.separator + langCnFileName;
                Properties propCn = getLangProperties(cnPath);
                //language_*文件
                for (String langFile : langFileList) {
                    if (langFile.equals("language_tw.properties")) {
                        log.info("台湾语言包不做检测...");
                        continue;
                    }
                    String langPath = projectPath + File.separator + langFile;
                    Properties propLang = getLangProperties(langPath);
                    List<String> keys = new ArrayList<>();
                    for (String langKey : keyCnList) {
                        if (propLang.get(langKey) == null || propCn.get(langKey) == null) {
                            continue;
                        }
                        String langCnStr = propCn.get(langKey).toString();
                        String langOtherStr = propLang.get(langKey).toString();
                        if (langCnStr.equals(langOtherStr)) {
                            keys.add(langKey);
                        }
                    }
                    log.info("路径：" + langPath + " 扫描完成！");
                    if (!keys.isEmpty()) {
                        log.info("未翻译语言 " + keys.size() + " 条：" + keys);
                    } else {
                        log.info("所有语言都已翻译过！");
                    }
                }
            }
            log.info("语言翻译检测完成！" + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Properties getLangProperties(String path) {
        Properties prop = new Properties();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
            prop.load(br);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    /**
     * 通过路径得到语言包的list
     *
     * @return langList
     */
    private List<String> getLangList(String path) {
        List<String> langList = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)));
            String tempString;
            while ((tempString = bufferedReader.readLine()) != null) {
                langList.add(tempString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return langList;
    }

}
