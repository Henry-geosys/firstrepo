package com.geosys.modules.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geosys.common.utils.PageUtils;
import com.geosys.modules.file.entity.ProgressEntity;

import java.util.List;
import java.util.Map;

/**
 * 进度
 *
 * @author lee
 * @email wilsonlky@126.com
 * @date 2019-12-23 15:48:08
 */
public interface ProgressService extends IService<ProgressEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<Long> getLayerIdByState();
}

