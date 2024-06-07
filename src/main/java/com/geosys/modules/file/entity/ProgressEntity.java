package com.geosys.modules.file.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 进度
 *
 * @author lee
 * @email wilsonlky@126.com
 * @date 2019-12-23 15:48:08
 */
@Data
@TableName("sys_progress")
public class ProgressEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * progressId
	 */
	@TableId
	private Long progressId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 已完成
	 */
	private Integer complete;
	/**
	 * 总数
	 */
	private Integer total;
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * userId
	 */
	private Long userId;
	/**
	 * 状态：0 正在进行；1 已完成但未读；2 已完成已读
	 */
	private Integer state;
	/**
	 * 提示消息
	 */
	private String message;
	/**
	 * 图层/资源id
	 */
	private Long layerId;

	private String uid;
}
