package com.geosys.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 常量
 */
@Component
public class Constant {
	/** 超级管理员ID */
	public static final long SUPER_ADMIN = 1;
    /** 管理员ID */
	public static final long ADMIN = 2;
    /** 项目管理员ID*/
    public static final long PM = 3;
    /** 用户ID*/
    public static final long USER = 4;
    /** 访客ID*/
    public static final long GUEST = 5;
    /** 访客ID*/
    public static final long DM = 6;

    /** 数据权限过滤 */
    public static final String SQL_FILTER = "sql_filter";

    /** 数据文件处理结果存放位置 */
    public static final String result = File.separator+"result"+File.separator;

    /**
     * 当前页码
     */
    public static final String PAGE = "page";
    /**
     * 每页显示记录数
     */
    public static final String LIMIT = "limit";
    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "sidx";
    /**
     * 排序方式
     */
    public static final String ORDER = "order";
    /**
     *  升序
     */
    public static final String ASC = "asc";

    /**
	 * 菜单类型
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 定时任务状态
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 文件存储路径
     */
    public static String prefix;
    @Value("${geosys.file_path}")
    public void setPrefix(String file_path) {
        prefix = file_path;
    }

    /**
     * 转换obj模型插件存放路径
     */
    public static String obj_cmd_path;
    @Value("${geosys.obj_cmd_path}")
    public void setObjCmdPath(String obj_path) {
        obj_cmd_path = obj_path;
    }

    /**
     * 转换点云文件插件存放路径
     */
    public static String LAStools_cmd_path;
    @Value("${geosys.LAStools_cmd_path}")
    public void setLAStoolsCmdPath(String LAStools_path) {
        LAStools_cmd_path = LAStools_path;
    }

    /**
     * 调整3dtiles插件存放路径
     */
    public static String toolset_cmd_path;
    @Value("${geosys.toolset_cmd_path}")
    public void setToolsetCmdPath(String toolset_path) {
        toolset_cmd_path = toolset_path;
    }

    /**
     * ffmpeg插件存放路径
     */
    public static String ffmpeg_cmd_path;
    @Value("${geosys.ffmpeg_cmd_path}")
    public void setFfmpegCmdPath(String ffmpeg_path) {
        Constant.ffmpeg_cmd_path = ffmpeg_path;
    }

    /**
     * gdal插件存放路径
     */
    public static String gdal_cmd_path;
    @Value("${geosys.gdal_cmd_path}")
    public void setgdal_cmd_path(String gdal_path) {
        Constant.gdal_cmd_path = gdal_path;
    }

    /**
     * iFreedo插件存放路径
     */
    public static String iFreedo_cmd_path;
    @Value("${geosys.iFreedo_cmd_path}")
    public void setiFreedo_cmd_path(String iFreedo_path) {
        Constant.iFreedo_cmd_path = iFreedo_path;
    }

    /**
     * postgresql工具集存放路径
     */
     public static String pgsql_cmd_path;
     @Value("${geosys.postgresql_cmd_path}")
     public void setPgsql_cmd_path(String pgsql_path) {
         Constant.pgsql_cmd_path = pgsql_path;
     }

     /**
      * 蒙脸服务器路径
      */
      public static String face_blur_server;
      @Value("${geosys.face_blur_server}")
      public void set_face_blur_server(String face_blur_server) {
          Constant.face_blur_server = face_blur_server;
      }
}
