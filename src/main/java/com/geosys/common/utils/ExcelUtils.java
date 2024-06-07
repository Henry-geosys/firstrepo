package com.geosys.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
public class ExcelUtils {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final boolean ASC = true;//升序
    private static final boolean DESC = false;//降序

//    @Value("${geosys.layer_offset}")
//    private Long layer_offset;

    public String excelToJson(String layerPath, String bimPath, Long layer_offset) throws IOException {
        JSONObject resultJson = new JSONObject(true);
        JSONObject startTimeJson = new JSONObject(true);
        JSONObject concretJson = new JSONObject(true);
        JSONArray activityArray = new JSONArray();

        //将bim表格转成map数据
        Map<String, String> bimMap = excelToMap(bimPath);

        //读取layer文本数据
        BufferedReader reader = new BufferedReader(new FileReader(layerPath));
        String temp = reader.readLine();//第一行信息，为标题信息，不用，如果需要，注释掉
        String line = null;
        Set<String> tempDateSet = new HashSet<>();
        while ((line = reader.readLine()) != null) {
            String[] items = line.split(",");
            Long layerId = Long.parseLong(items[0]) + layer_offset;
            String bimString = items[1];
            //与bim表格数据作比较
            if (bimMap.containsKey(bimString)) {
                //添加start_time
                String[] valuessString = bimMap.get(bimString).split(":");
                String dateString = valuessString[0];
                String concretValue = valuessString[1];
                logger.info(bimMap.get(bimString));
                logger.info(dateString);
                logger.info(concretValue);
                startTimeJson.append(dateString, layerId);

                //添加activity_list
                if (!tempDateSet.contains(dateString)) {
                    JSONObject tempJson = new JSONObject();
                    tempJson.accumulate("sd", dateString);
                    tempJson.accumulate("ed", dateString);
                    tempJson.accumulate("aed", dateString);
                    tempJson.accumulate("nm", dateString);
                    activityArray.add(tempJson);
                    tempDateSet.add(dateString);
                }
                concretJson.append(concretValue,layerId);


            }
        }
        reader.close();

        startTimeJson = sortJson(startTimeJson, ASC);
        activityArray = sortArray(activityArray, ASC);
        resultJson.accumulate("start_time", startTimeJson);
        resultJson.accumulate("end_time", startTimeJson);
        resultJson.accumulate("actural_end_time", startTimeJson);
        resultJson.accumulate("activity_list", activityArray);
        resultJson.accumulate("concrete",concretJson);

        return resultJson.toString();
    }

    private Map<String, String> excelToMap(String path) throws IOException {
        Map<String, String> map = new HashMap<>();
        //读取excel文件
        XSSFWorkbook workbook = new XSSFWorkbook(path);
        if (workbook.getNumberOfSheets() == 0) return map;
        //组装map数据：第一列为key，第二列为value
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
            XSSFRow row = sheet.getRow(i);
            String key = row.getCell(0).getStringCellValue();
            XSSFCell cell = row.getCell(1);
            XSSFCell cell2 = row.getCell(4);
            if (i == 0) continue;//忽略第一条非日期数据
            String value = DateUtil.formatDate(DateUtil.parse(cell.getStringCellValue(), "yyyy/MM/dd HH:mm:ss"));
            String concretingValue = cell2.getStringCellValue();
            logger.info("concertvalue:"+concretingValue);

            map.put(key, value+":"+concretingValue);
        }

        workbook.close();
        return map;
    }

    /**
     * @param json json数据
     * @param type 排序方式  0：升序， 1：降序
     */
    private JSONObject sortJson(JSONObject json, boolean type) {
        JSONObject resultJson = new JSONObject(true);//null表示不排序，不排序情况下，如果order为true按照加入顺序排序，否则按照hash排序

        String[] keyArray = json.keySet().toArray(new String[0]);
        if (type == ASC) {
            Arrays.sort(keyArray);
        } else {
            Arrays.sort(keyArray, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o2.compareTo(o1);
                }
            });
        }
        for (String key : keyArray) {
            resultJson.accumulate(key, json.get(key));
        }

        return resultJson;
    }

    /**
     * @param array array数据
     * @param type  排序方式  0：升序， 1：降序
     */
    private JSONArray sortArray(JSONArray array, boolean type) {

        List<JSONObject> list = array.toList(JSONObject.class);
        if (type == ASC) {
            list.sort(new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    return o1.getStr("sd").compareTo(o2.getStr("sd"));
                }
            });
        } else {
            list.sort(new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    return o2.getStr("sd").compareTo(o1.getStr("sd"));
                }
            });
        }

        return new JSONArray(list);
    }
}

