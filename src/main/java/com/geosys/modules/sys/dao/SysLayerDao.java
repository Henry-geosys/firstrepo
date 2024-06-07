package com.geosys.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geosys.modules.sys.entity.SysLayerEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 图层管理
 */
@Mapper
public interface SysLayerDao extends BaseMapper<SysLayerEntity> {

    IPage<SysLayerEntity> queryPage(Page page, Long userId, Long companyId, Long createUserId, boolean isPublic, String choose, String title, String role, String category);

    IPage<SysLayerEntity> queryList(Page page, Long userId, Long companyId, String category, String title, String role, String flag);

    List<SysLayerEntity> queryList(Map<String, Object> params);

    SysLayerEntity getById(Long layerId);

    void saveLayer(SysLayerEntity layer);

    void updateLayer(SysLayerEntity layer);

    List<Map> listPotree(Map<String, Object> params);

    List<SysLayerEntity> listByProject(Map<String, Object> params);

    List<SysLayerEntity> listByWorkspace(Map<String, Object> params);

    String querySrc(Long layerId);

    String queryUrl(Long LayerId);

    String queryParam(Long layerId);

    void updateParam(Long layerId, String param);

    List<SysLayerEntity> search(Map<String, Object> params);

    void updatePtGeom(String boundingBox, Long layerId);

    void setMediaBoundary(Long layerId);

    void setPanoBoundary(Long layerId);

    Integer sumSize(Long companyId, Long projectId);
}
