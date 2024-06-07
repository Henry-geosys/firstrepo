package com.geosys.modules.file.service.impl;

import com.geosys.modules.file.dao.ProgressDao;
import com.geosys.modules.file.entity.ProgressEntity;
import com.geosys.modules.file.service.ProgressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geosys.common.utils.PageUtils;
import com.geosys.common.utils.Query;



@Service("progressService")
public class ProgressServiceImpl extends ServiceImpl<ProgressDao, ProgressEntity> implements ProgressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProgressEntity> page = this.page(
                new Query<ProgressEntity>().getPage(params),
                new QueryWrapper<ProgressEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<Long> getLayerIdByState() {
        return baseMapper.getLayerIdByState();
    }
}
