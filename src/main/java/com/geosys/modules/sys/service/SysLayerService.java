package com.geosys.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geosys.common.utils.PageUtils;
import com.geosys.modules.sys.entity.SysLayerEntity;
import org.apache.commons.math3.exception.InsufficientDataException;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.rmi.ServerException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 项目管理
 */
public interface SysLayerService extends IService<SysLayerEntity> {

    /**
     * 分页
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 列表
     * @param params
     * @return
     */
    List<SysLayerEntity> queryList(Map<String, Object> params);

    /**
     * 根据layerId获取信息
     * @param layerId
     * @return
     */
    SysLayerEntity getById(Long layerId);

    /**
     * 获取项目下的图层列表
     * @param params
     * @return
     */
    List<SysLayerEntity> listByProject(Map<String, Object> params);

    /**
     * 获取工作空间下的图层列表
     * @param params
     * @return
     */
    List<SysLayerEntity> listByWorkspace(Map<String, Object> params);

    /**
     * 保存图层
     * @param layer
     * @return
     */
    Long saveLayer(SysLayerEntity layer);

    /**
     * 更新图层
     * @param layer
     */
    void updateLayer(SysLayerEntity layer);

//    /**
//     * 删除图层
//     * @param layerIds
//     */
//    void deleteLayer(Long[] layerIds);
//
//    /**
//     * 项目-图层个性化设置
//     * @param params
//     */
//    void updateProjectLayerSettings(Map<String, Object> params);
//
//    /**
//     * 工作空间-图层个性化设置
//     * @param params
//     */
//    void updateWorkspaceLayerSettings(Map<String, Object> params);
//
//    /**
//     * potree
//     * @param params
//     * @return
//     */
//    List<Map> listPotree(Map<String, Object> params);
//
//    /**
//     * 获取文件/文件夹名称作为图层名称
//     * @param file
//     * @return
//     */
//    String title(File file);
//
////    /**
////     * 生成缩略图（异步）
////     * @param objects
////     * @throws IOException
////     */
////    void thumbnail(String thumbnail, List<String> objects, Long progressId) throws IOException, MessagingException, ServerException, InsufficientDataException, NoSuchAlgorithmException, InternalException, InvalidResponseException, XmlParserException, InvalidKeyException, ErrorResponseException;
//
//    /**
//     * 在apollo创建资源
//     * @param path
//     */
//    void createApolloMedia(String path, Long progressId) throws MessagingException;
//
//    /**
//     * 异步 点云转3dtiles/potree
//     * @param progressId
//     * @param lasPath 点云文件路径
//     * @param txtPath 生成txt的路径
//     * @param txtFolder txt所在文件夹路径
//     * @param tilesPath 生成的3dtiles路径
//     * @param potreePath 生成的potree路径
//     * @throws IOException
//     * @throws InterruptedException
//     */
//    void handlerLAS(Long progressId, String lasPath, String txtPath, String txtFolder, String tilesPath, String potreePath,
//                    String epsg, Boolean isPotree, Boolean is3dtiles, double xOffset, double yOffset, double zOffset,
//                    boolean isUsingStandardWgs) throws IOException, InterruptedException, MessagingException, SQLException;
////    /**
////     * 异步 处理tiff格式文件
////     * @param objects
////     * @param userId
////     * @param companyId
////     * @param epsg
////     * @param title
////     * @param layer
////     * @throws IOException
////     * @throws InterruptedException
////     * @throws MessagingException
////     * @throws ServerException
////     * @throws ErrorResponseException
////     * @throws NoSuchAlgorithmException
////     * @throws InsufficientDataException
////     * @throws InternalException
////     * @throws InvalidResponseException
////     * @throws XmlParserException
////     * @throws InvalidKeyException
////     */
////    void handlerGeoTiff(List<String> objects, Long userId, Long companyId, String resultPrefix, String epsg,
////                        String title, SysLayerEntity layer) throws IOException, InterruptedException, MessagingException,
////            ServerException, ErrorResponseException, NoSuchAlgorithmException, InsufficientDataException, InternalException,
////            InvalidResponseException, XmlParserException, InvalidKeyException;
//
//    /**
//     * 处理影像分片
//     * @param progressId
//     * @param epsg
//     * @param file
//     * @param outputPath
//     * @param layer
//     * @throws IOException
//     * @throws InterruptedException
//     * @throws MessagingException
//     */
//    void gdal2tiles(Long progressId, String epsg, File file, String outputPath, SysLayerEntity layer,
//                    boolean isProjTiff) throws IOException, InterruptedException, MessagingException;
//
//    /**
//     * 异步 处理全景图片
//     * @param progressId
//     * @param objects
//     * @param isBlurFace
//     * @param resultPath
//     * @param lasPath
//     * @param location
//     * @param layerId
//     * @param filePath
//     * @param eoFormat
//     * @param epsg
//     * @throws MessagingException
//     * @throws IOException
//     * @throws InterruptedException
//     * @throws SQLException
//     */
//    void handlerPano(Long progressId, List<String> objects, Boolean isBlurFace, String resultPath, String lasPath, File location, Long layerId, String filePath, String eoFormat, String epsg) throws MessagingException, IOException, InterruptedException, SQLException;
//
//    /**
//     * 异步处理GSM Marker的csv
//     */
//    void handlerGSM(Long progressId, String csvPath, String resultFolder, String resultPath, String epsg) throws IOException, MessagingException;
//
//    /**
//     * 异步处理地形geotiff
//     * @throws IOException
//     * @throws InterruptedException
//     * @throws MessagingException
//     */
//    void generateDEM(String resultPath, String tiffPath, String epsg, long progressId, long layerId) throws IOException,
//            InterruptedException, MessagingException;
//
//    /**
//     * 异步 处理shapefile
//     * @param progressId
//     * @param shpPath
//     */
//    void handlerSHP(Long progressId, String shpPath, String tableTitle, String type) throws IOException, InterruptedException,
//            MessagingException, SQLException, ClassNotFoundException;
//
//    /**
//     * 更新发布图层的大小
//     * @param size
//     * @param companyId
//     * @param userId
//     */
//    void updatePublished(double size, Long companyId, Long userId);
//
////    /**
////     * 生成视频缩略
////     * @param resultPath
////     * @param objects
////     * @param progressId
////     * @throws MessagingException
////     */
////	void thumbnailVideo(String resultPath, List<String> objects, Long progressId) throws MessagingException, IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InsufficientDataException, InternalException;
//
//    /**
//     * 获取图层的源文件
//     * @param layerId
//     * @return
//     */
//    String querySrc(Long layerId);
//
//    /**
//     * 获取图层url
//     * @param LayerId
//     * @return
//     */
//    String queryUrl(Long LayerId);
//
//    /**
//     * 查询参数
//     * @param layerId
//     * @return
//     */
//    String queryParam(Long layerId);
//
//    /**
//     * 更新参数
//     * @param layerId
//     * @param param
//     */
//    void updateParam(Long layerId, String param);
//
//    /**
//     * 搜索
//     * @param params
//     * @return
//     */
//    List<SysLayerEntity> search(Map<String, Object> params);
//
//    /**
//     * 获取tif文件的geom信息
//     * @param file
//     * @return
//     */
//    String getGeomInfo(File file) throws IOException, InterruptedException;
//
//    /**
//     * 设置边界
//     * @param layerId
//     */
//    void setMediaBoundary(Long layerId);
//
//    /**
//     * 设置边界
//     * @param layerId
//     */
//    void setPanoBoundary(Long layerId);
//
//    /**
//     * 获取资源存储
//     * @param companyId
//     * @return
//     */
//    Integer sumSize(Long companyId, Long projectId);
//
//    void handlerODM(Long progressId, String rootPath, String odmImageFolderSubPath, String salt, Long companyId, Long userId, String title);

}

