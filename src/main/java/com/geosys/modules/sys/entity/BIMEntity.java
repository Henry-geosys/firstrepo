package com.geosys.modules.sys.entity;

import lombok.Data;

@Data
public class BIMEntity {

    private static final long serialVersionUID = 1L;

    private Long layerId;

    private Long layerOffset;

    String layerPath;

    String bimPath;
}
