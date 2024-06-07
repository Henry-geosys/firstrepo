package com.geosys.modules.file.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geosys.modules.file.entity.ProgressEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 进度
 *
 * @author lee
 * @email wilsonlky@126.com
 * @date 2019-12-23 15:48:08
 */
@Mapper
public interface ProgressDao extends BaseMapper<ProgressEntity> {

    List<Long> getLayerIdByState();

}
