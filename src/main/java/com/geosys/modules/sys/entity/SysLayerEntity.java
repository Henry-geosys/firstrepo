package com.geosys.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 图层管理
 */
@Data
@TableName("sys_layer")
public class SysLayerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 图层ID
	 */
	@TableId
	private Long layerId;
	/**
	 * 图层名称
	 */
	private String title;
	/**
	 * url
	 */
	private String url;
	/**
	 * 图层类型
	 */
	private String type;
	/**
	 * 类别
	 */
	private String category;
	/**
	 * 参数{}
	 */
	private String parameter;
	/**
	 * 是否打开
	 */
	private Boolean isAutoOpen;
	/**
	 * geometry
	 */
	private String geometry;
	/**
	 * 海拔高度
	 */
	private Float elevation;
	/**
	 * 查询数据集
	 */
	private String queryDataset;
	/**
	 * 平整范围
	 */
	private String flattenRegion;
	/**
	 * potreeUrl
	 */
	private String potreeUrl;

	/**
	 * userId
	 */
	private Long userId;

	/**
	 * 是否私有 默认true 私有
	 */
	private Boolean isPrivate;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * companyId
	 */
	private Long companyId;
	/**
	 * script4d
	 */
	private String scriptAnim;
	/**
	 * size
	 */
	private Double size;
	/**
	 * 图层源数据（下载用）
	 */
	private String src;
	/**
	 * 图层图标
	 */
	private String billboard;
	/**
	 * 数据采集时间
	 */
	private Date collectDate;

	/**
	 * url2d
	 */
	private String url_2d;

	@TableField(exist = false)
	private Double latitude;
	@TableField(exist = false)
	private Double longitude;
	@TableField(exist = false)
	private Double height;
	@TableField(exist = false)
	private String EPSG;
	@TableField(exist = false)
	private Integer source;
	@TableField(exist = false)
	private String username;
	@TableField(exist = false)
	private String structure;
	@TableField(exist = false)
	private Boolean isOpen;
	@TableField(exist = false)
	private String displayTitle;
	@TableField(exist = false)
	private Integer order;
	@TableField(exist = false)
	private Boolean defaultLayer;
}
