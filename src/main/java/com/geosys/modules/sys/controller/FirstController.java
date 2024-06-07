package com.geosys.modules.sys.controller;


import com.geosys.common.utils.ExcelUtils;
import com.geosys.common.utils.R;
import com.geosys.modules.sys.entity.BIMEntity;
import com.geosys.modules.sys.entity.SysLayerEntity;
import com.geosys.modules.sys.service.SysLayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by pc on 2021/1/22.
 */
@RestController
public class FirstController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ExcelUtils excelUtils;
    @Autowired
    private SysLayerService sysLayerService;

    @RequestMapping("/index")
    public String sayHello() {
        return "index";
    }

    @RequestMapping("convert")
    public R convert(@RequestBody BIMEntity entity) throws IOException {
        Long layerId = entity.getLayerId();
        Long layerOffset = entity.getLayerOffset();
        String layerPath = entity.getLayerPath();
        String bimPath = entity.getBimPath();
        String json = excelUtils.excelToJson(layerPath, bimPath, layerOffset);
        logger.info(json);

//        FileUtil.writeString(json, "F:\\Desktop\\" + layerId + ".txt", Charset.defaultCharset());

        SysLayerEntity layer = new SysLayerEntity();
        layer.setLayerId(layerId);
        layer.setScriptAnim(json);
        sysLayerService.updateById(layer);
        return R.ok().put("layerId", layerId);
    }
}
