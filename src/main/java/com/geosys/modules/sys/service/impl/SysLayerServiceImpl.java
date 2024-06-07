package com.geosys.modules.sys.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geosys.common.utils.Constant;
import com.geosys.common.utils.Messages;
import com.geosys.common.utils.PageUtils;
import com.geosys.common.utils.Query;
import com.geosys.modules.sys.dao.SysLayerDao;
import com.geosys.modules.sys.entity.SysLayerEntity;
import com.geosys.modules.sys.service.SysLayerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;


@Service("SysLayerService")
public class SysLayerServiceImpl extends ServiceImpl<SysLayerDao, SysLayerEntity> implements SysLayerService {
//    private static final String docPrefix = "/sdms/doc/";
//
//    @Value("${geosys.file_path}")
//    private String sdms_file_path;
//
//    @Value("${geosys.las_cmd}")
//    private String las_cmd;
//
//	@Value("${geosys.shp2pgsql_cmd}")
//    private String shp2pgsql_cmd;
//
//    @Value("${geosys.ogr2ogr_cmd}")
//    private String ogr2ogr_cmd;
//
//    @Value("${dynamic.datasource.shp.driver-class-name}")
//    private String driver;
//
//    @Value("${dynamic.datasource.shp.url}")
//    private String url;
//
//    @Value("${dynamic.datasource.shp.username}")
//    private String username;
//
//    @Value("${dynamic.datasource.shp.password}")
//	private String password;
//
//    @Value("${geosys.apollo}")
//    private Boolean apollo;
//
//    @Value("${geosys.apollo_host}")
//    private String apollo_host;
//
//    @Value("${geosys.apollo_service}")
//    private String apollo_service;
//
//    @Value("${geosys.apollo_micro_host}")
//    private String apollo_micro_host;
//
//    @Value("${geosys.apollo_ecw_path}")
//    private String apollo_ecw_path;
//
//    @Value("${geosys.potree_micro_host}")
//    private String potree_micro_host;
//
//    @Value("${geosys.odm_micro_host}")
//    private String odm_micro_host;
//
//    @Value("${minio.fileBucket}")
//    private String fileBucket;
//
//    @Value("${minio.root}")
//    private String root;
//
//
//    // 图片默认缩放比率
//    private static final double DEFAULT_SCALE = 0.02d;
//
//    private final static String WGS84 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = null;
        if (params.get("title") != null) {
            title = params.get("title").equals("") ? null : (String) params.get("title");
        }

        Long createUserId = null;
        if (params.get("createUserId") != null) {
            createUserId = params.get("createUserId").equals("") ? null : Long.valueOf((String)params.get("createUserId"));
        }

        String category = null;
        if (params.get("category") != null) {
            category = params.get("category").equals("") ? null : (String) params.get("category");
        }
        Long userId = (Long) params.get("userId");
        Long companyId = (Long) params.get("companyId");
        String role = (String) params.get("role");
        String choose = (String)params.get("choose");
        boolean isPublic = Boolean.parseBoolean((String)params.get("isPublic"));
        IPage<SysLayerEntity> page = baseMapper.queryPage((Page<SysLayerEntity>) new Query<SysLayerEntity>()
                .getPage(params), userId, companyId, createUserId, isPublic, choose, title, role, category);

        return new PageUtils(page);
    }

    @Override
    public List<SysLayerEntity> queryList(Map<String, Object> params) {
        return baseMapper.queryList(params);
    }

    @Override
    public SysLayerEntity getById(Long layerId) {
        return baseMapper.getById(layerId);
    }

    @Override
    public List<SysLayerEntity> listByProject(Map<String, Object> params) {
        return baseMapper.listByProject(params);
    }

    @Override
    public List<SysLayerEntity> listByWorkspace(Map<String, Object> params) {
        return baseMapper.listByWorkspace(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveLayer(SysLayerEntity layer) {
        baseMapper.saveLayer(layer);

        return layer.getLayerId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLayer(SysLayerEntity layer) {
        baseMapper.updateById(layer);

        //保存图层与用户关系
        ///sysUserLayerService.saveOrUpdate(layer.getLayerId(), layer.getUserIds());
    }

//    @Override
//    public void deleteLayer(Long[] layerIds) {
//        for (Long layerId : layerIds) {
//            SysLayerEntity layer = getById(layerId);
//            if (layer != null) {
//                /*String category = layer.getCategory();
//                String url = layer.getUrl();
//                String path = "";
//                if (url != null) {
//                    path = url.replace("/sdms/doc/", Constant.prefix);
//                }
//
//                //删除对应的结果文件夹
//                switch (category) {
//                    case "SHAPEFILE": {
//                        //删除对应的shp表
//                        String tableName = url.substring(url.lastIndexOf("/") + 1);
//                        shpService.delTable(tableName);
//                        break;
//                    }
//                    case "TRAJECTORY_MEDIA":
//                    case "MEDIA": {
//                        if (url != null) {
//                            //删除
//                            FileUtil.del(path);
//
//                            //检查结果文件夹是否为空 为空则结果文件夹删除
//                            File folder = new File(path).getParentFile();
//                            if (folder.list().length == 0) {
//                                FileUtil.del(folder);
//                            }
//                        }
//
//                        break;
//                    }
//                    case "OBJECT":
//                    case "CSVGSM":
//                    case "TRAJECTORY": {
//                        //获取结果文件夹路径并删除
//                        File folder = new File(path).getParentFile();
//                        fileService.delete(folder);
//                        break;
//                    }
//                    case "POINT CLOUD": {
//                        //获取结果文件夹路径并删除
//                        File folder = new File(path.substring(0, path.indexOf("/tiles/tileset.json"))).getParentFile();
//                        fileService.delete(folder);
//                        break;
//                    }
//                    case "GEOTIFF": {
//                        if (layer.getType().equals("WMTS")) {
//                            String delete_url = apollo_micro_host + "/file/delete";
//                            //删除apollo服务上的ecw
//                            HttpRequest.post(delete_url).body(layer.getParameter()).execute();
//                        } else {
//                            //获取结果文件夹路径并删除
//                            File folder = new File(path.substring(0, path.indexOf("/{z}/{x}/{reverseY}.png")));
//                            fileService.delete(folder);
//                        }
//                        break;
//                    }
//                    case "PANORAMA":
//                    case "VIDEO": {
//                        //删除
//                        fileService.delete(new File(path));
//                        break;
//                    }
//                }*/
//                //删除图层
//                removeById(layerId);
//                //删除项目-图层关系
//                sysProjectLayerService.remove(new QueryWrapper<SysProjectLayerEntity>().eq("layer_id", layerId));
//                //删除工作空间-图层关系
//                sysWorkspaceLayerService.remove(new QueryWrapper<SysWorkspaceLayerEntity>().eq("layer_id", layerId));
//
//                //更新发布存储空间
//                double size = -layer.getSize();
//                sysCompanyService.updatePublished(layer.getCompanyId(), size);
//                /*Boolean personalTrial = sysCompanyService.getPersonalTrial(layer.getCompanyId());
//
//                if (personalTrial) {
//                    sysUserService.updatePublished(layer.getUserId(), size);
//                } else {
//                    sysCompanyService.updatePublished(layer.getCompanyId(), size);
//                }*/
//            }
//        }
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateProjectLayerSettings(Map<String, Object> params) {
//        //params中图层信息
//        String json = JSONObject.toJSONString(params.get("layers"));
//        List<SysProjectLayerEntity> projectLayerList = JSONObject.parseArray(json, SysProjectLayerEntity.class);
//        Long projectId = Long.valueOf((Integer) params.get("projectId"));
//
//        for (SysProjectLayerEntity projectLayer : projectLayerList) {
//            projectLayer.setProjectId(projectId);
//        }
//        //分割为size为500的list
//        List<List<SysProjectLayerEntity>> splits = Lists.partition(projectLayerList, 500);
//        //分段批量更新
//        for (List<SysProjectLayerEntity> split : splits) {
//            sysProjectLayerService.updateBatch(split);
//        }
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateWorkspaceLayerSettings(Map<String, Object> params) {
//        String json = JSONObject.toJSONString(params.get("layers"));
//        List<SysWorkspaceLayerEntity> workspaceLayerList = JSONObject.parseArray(json, SysWorkspaceLayerEntity.class);
//        Long workspaceId = Long.valueOf((Integer) params.get("workspaceId"));
//
//        for (SysWorkspaceLayerEntity workspaceLayer : workspaceLayerList) {
//            workspaceLayer.setWorkspaceId(workspaceId);
//        }
//        List<List<SysWorkspaceLayerEntity>> splits = Lists.partition(workspaceLayerList, 500);
//
//        //分段批量更新
//        for (List<SysWorkspaceLayerEntity> split : splits) {
//            sysWorkspaceLayerService.updateBatch(split);
//        }
//    }
//
//    @Override
//    public List<Map> listPotree(Map<String, Object> params) {
//        return baseMapper.listPotree(params);
//    }
//
//    @Override
//    public String title(File file) {
//        String title;
//        String path = file.getAbsolutePath().replace("\\", "/");
//        if (file.isFile()) {
//            title = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
//        } else {
//            title = path.substring(path.lastIndexOf("/") + 1);
//        }
//        return title;
//    }
//
//    @Override
//    @Async
//    public void thumbnail(String thumbnailPath, List<String> objects, Long progressId) throws IOException, MessagingException,
//            ServerException, InsufficientDataException, NoSuchAlgorithmException, InternalException, InvalidResponseException,
//            XmlParserException, InvalidKeyException, ErrorResponseException {
//
//        ProgressEntity progress = progressService.getById(progressId);
//
//        for (String object : objects) {
//            String url = minioUtil.getUrl(fileBucket, object);
//
//            //缩略图路径
//            String thumbnail = thumbnailPath + object.substring(object.lastIndexOf("/") + 1);
//
//            BufferedImage img = ImageIO.read(new URL(url));
//            int iWidth = img.getWidth();
//            int iHeight = img.getHeight();
//
//            double widthScale = 64.0 / (double) iWidth;
//            double heightScale = 64.0 / (double) iHeight;
//            double thumbScale = Math.min(1.0, Math.max(widthScale, heightScale));
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            Thumbnails.of(new URL(url)).scale(thumbScale).toOutputStream(baos);
//
//            minioUtil.upload(new ByteArrayInputStream(baos.toByteArray()), fileBucket, thumbnail, "image/jpeg");
//
//            //更新进度
//            progress.setComplete(progress.getComplete() + 1);
//            progressService.updateById(progress);
//
//            if (progress.getComplete().equals(progress.getTotal())) {
//                progress.setMessage("x_progress_publish_completed");
//                progress.setState(1);
//                progress.setEndTime(DateUtil.date());
//                progressService.updateById(progress);
//            }
//        }
//        //完成后发送邮件
//        send(sysUserService.getById(progress.getUserId()), progress.getName());
//    }
//
//    @Override
//    @Async
//    public void createApolloMedia(String path, Long progressId) throws MessagingException {
//        //删除资源树重新发布
//        String delete_url = apollo_micro_host + "/file/source/delete1";
//        JSONObject delete_json = new JSONObject();
//        delete_json.put("type", "media");
//        delete_json.put("path", path);
//        HttpRequest.post(delete_url).body(delete_json.toJSONString()).execute();
//
//        //发布到apollo
//        String url = apollo_micro_host + "/file/source/create1";
//        String[] paths = path.split("/");
//
//        StringBuilder p = new StringBuilder();
//
//        for (int i = 1; i < paths.length; i++) {
//            JSONObject json_body = new JSONObject();
//            p.append("/").append(paths[i]);
//            if (i == paths.length - 1) {
//                json_body.put("path", p);
//                json_body.put("dirValue", true);
//                json_body.put("type", "media");
//            } else {
//                json_body.put("path", p);
//                json_body.put("dirValue", false);
//                json_body.put("type", "media");
//            }
//            HttpRequest.post(url).body(json_body.toJSONString()).execute();
//        }
//
//        ProgressEntity progress = progressService.getById(progressId);
//        progress.setComplete(100);
//        progress.setMessage("x_progress_publish_completed");
//        progress.setState(1);
//        progress.setEndTime(DateUtil.date());
//        progressService.updateById(progress);
//
//        //完成后发送邮件
//        send(sysUserService.getById(progress.getUserId()), progress.getName());
//    }
//
//    @Override
//    @Async
//    public void handlerLAS(Long progressId, String lasPath, String txtPath, String txtFolder, String tilesPath, String potreePath,
//                           String epsg, Boolean isPotree, Boolean is3dtiles, double xOffset, double yOffset, double zOffset,
//                           boolean isUsingStandardWgs) throws IOException, InterruptedException, MessagingException, SQLException {
//
//        //las转potree
//    	ProgressEntity progress = progressService.getById(progressId);
//        if (isPotree) {
//            progress.setComplete(10);
//            progress.setMessage("x_progress_create_potree");
//            progressService.updateById(progress);
//
//            //las转potree
//            potreePath = "/" + fileBucket + "/" + potreePath;
//            //tempPotreeFolder = tempPotreeFolder.replace(Constant.prefix, "/").replace("\\", "/");
//            String convert_request = "{\"lasPath\":\"" + lasPath.replace(root, "").replace("\\", "/") + "\",\"potreePath\":\"" + potreePath + "\"}";
//
//            //convert
//            String convert_url = potree_micro_host + "/file/convert1";
//            HttpRequest.post(convert_url).body(convert_request).timeout(3000000).execute();
//
//            //上传到minio
//            /*File[] files = new File(tempPotreeFolder).listFiles();
//            if (files != null) {
//                for (File potree : files) {
//                    minioUtil.upload(new FileInputStream(potree), fileBucket, potreePath + potree.getName(), "");
//                }
//            }*/
//        }
//
//        //las转3dtiles
//        if (is3dtiles) {
//            SqliteDB sqliteDB;
//
//            //处理las文件 转换为txt文件 cmd命令
//            String[] cmd = {Constant.LAStools_cmd_path + las_cmd, "-i", lasPath.replace("\\", "/"), "-o", txtPath.replace("\\", "/"), "-parse", "xyzRGBic"};
//            Process process = Runtime.getRuntime().exec(cmd);
//            int flag = fileService.handlerInfoProcess(process);
//
//            if (flag == 1) {
//                System.out.println("The following command has got a non-zero return value: " + Arrays.toString(cmd));
//
//                //更新进度
//                progress.setMessage("x_progress_create_txt_failed");
//                progress.setState(1);
//                progress.setEndTime(DateUtil.date());
//                progressService.updateById(progress);
//
//                //发送失败邮件
//                sendFailed(sysUserService.getById(progress.getUserId()), progress.getName());
//                return;
//            } else {
//                //计算边界
//                sqliteDB = importIntoSqlite(txtPath, txtFolder);
//                String boundingBox = sqliteDB.getBoundingBox(epsg);
//
//                //更新
//                baseMapper.updatePtGeom(boundingBox, progress.getLayerId());
//                sqliteDB.KillConnection(txtFolder + "tmp.db");
//            }
//
//            progress.setComplete(50);
//            progress.setMessage("x_progress_create_tiles");
//            progressService.updateById(progress);
//
//            //将txt文件转换为3dtiles文件 第三方jar包
//            Convert convert = new Convert(epsg);
//            boolean isSuccess = convert.doProcess(txtFolder, tilesPath, xOffset, yOffset, zOffset, isUsingStandardWgs);
//
//            //上传到minio
//           /* List<File> files = FileUtil.loopFiles(tempTileFolder);
//            for (File file : files) {
//                String name = file.getAbsolutePath().replace(tempTileFolder.replace("/", "\\"), "");
//                String object = tilesPath + name.replace("\\", "/");
//                minioUtil.upload(new FileInputStream(file), fileBucket, object, "");
//            }*/
//        }
//
//        //更新进度
//        progress.setComplete(100);
//        progress.setMessage("x_progress_publish_completed");
//        progress.setState(1);
//        progress.setEndTime(DateUtil.date());
//        progressService.updateById(progress);
//
//        //完成后发送邮件
//        send(sysUserService.getById(progress.getUserId()), progress.getName());
//
//        //删除临时文件夹
//        FileUtil.del(new File(txtFolder));
//    }
//
//    @Override
//    @Async
//    public void handlerGeoTiff(List<String> objects, Long userId, Long companyId, String resultPrefix, String epsg, String title,
//                               SysLayerEntity layer) throws
//            IOException, InterruptedException, MessagingException, ServerException, ErrorResponseException, NoSuchAlgorithmException,
//            InsufficientDataException, InternalException, InvalidResponseException, XmlParserException, InvalidKeyException {
//
//        for (String object : objects) {
//            double size = ossService.size(fileBucket, object);
//            String filename = ossService.getObjectName(object);
//
//            //参数设置
//            String layerTitle = title;
//            if(objects.size() > 1) {
//                layerTitle += "_" + filename;
//            }
//            layer.setLayerId(null);
//            layer.setTitle(layerTitle);
//            layer.setSize(size);
//            layer.setSrc(object);
//
//            //获取geometry信息
//            File file = new File(root + "/" + fileBucket + "/" + object);
//            String geometry = getGeomInfo(file);
//            boolean isProjTiff = false;
//            if (geometry == null) {
//                //没有获取到则设置坐标系生成带坐标的tif文件
//                File projTiff = createProjTiff(file, epsg);
//                file = projTiff;
//                if (projTiff != null) {
//                    geometry = getGeomInfo(projTiff);
//                    object = object.substring(0, object.lastIndexOf("/")) + projTiff.getName();
//                    isProjTiff = true;
//                }
//            }
//            layer.setGeometry(geometry);
//
//            //保存图层
//            saveLayer(layer);
//
//            //新建图层发布进度
//            ProgressEntity progress = new ProgressEntity();
//            progress.setName(layerTitle);
//            progress.setUserId(userId);
//            progress.setStartTime(DateUtil.date());
//            progress.setComplete(0);
//            progress.setTotal(100);
//            progress.setState(0);
//            progress.setMessage("x_progress_create_geotiff");
//            progress.setLayerId(layer.getLayerId());
//            progressService.save(progress);
//
//            if (apollo) {
//                apollo(progress.getProgressId(), object, layer, isProjTiff);
//            } else {
//                String result = root + "/" + fileBucket + "/" + resultPrefix + filename + "_" + RandomStringUtils.randomAlphanumeric(6) + "/";
//                File resultFolder = new File(result);
//                resultFolder.mkdirs();
//
//                gdal2tiles(progress.getProgressId(), epsg, file, result, layer, isProjTiff);
//            }
//            updatePublished(size, companyId, userId);
//        }
//    }
//
//    @Override
//    public void gdal2tiles(Long progressId, String epsg, File file, String outputPath, SysLayerEntity layer, boolean isProjTiff) throws IOException, InterruptedException, MessagingException {
//        ProgressEntity progress = progressService.getById(progressId);
//        //tif处理
//        String tif_name = file.getName().substring(0, file.getName().indexOf("."));
//        String rgb_path = outputPath  + tif_name + "_RGB.tif";
//
//        // String rgb_cmd = Constant.gdal_cmd_path + "gdal_translate -mask 4 \"" + file.getAbsolutePath() + "\" \"" + rgb_path + "\"";
//        String[] rgb_cmd = {Constant.gdal_cmd_path + "gdal_translate", "-mask", "4", file.getAbsolutePath(), rgb_path};
//        // Process pro_rbg = Runtime.getRuntime().exec(rgb_cmd, null, new File(Constant.gdal_cmd_path));
//        Process pro_rbg = Runtime.getRuntime().exec(rgb_cmd);
//        fileService.handlerInfoProcess(pro_rbg);
//
//        String rgba_path = outputPath + tif_name + "_RGBA.tif";
//        // String rgba_cmd = Constant.gdal_cmd_path + "gdalwarp -dstalpha \"" + rgb_path + "\" \"" + rgba_path + "\"";
//        String[] rgba_cmd = {Constant.gdal_cmd_path + "gdalwarp", "-dstalpha", rgb_path, rgba_path};
//        // Process pro_rgba = Runtime.getRuntime().exec(rgba_cmd, null, new File(Constant.gdal_cmd_path));
//
//        Process pro_rgba = Runtime.getRuntime().exec(rgba_cmd);
//        fileService.handlerInfoProcess(pro_rgba);
//
//        //删除临时文件
//        FileUtil.del(rgb_path);
//        FileUtil.del(rgb_path + ".msk");
//
//        //处理分片
//        try {
//            // String cmd = "python \"" + Constant.gdal_cmd_path + "gdal2tiles.py\" -s EPSG:" + epsg + " -r lanczos -w none" + " \"" + rgba_path + "\" \"" + outputPath + "\"";
//        	String[] cmd = {"python", Constant.gdal_cmd_path + "gdal2tiles.py", "-s", "EPSG:" + epsg, "-r", "lanczos", "-w", "none", rgba_path, outputPath};
//        	// Process process = Runtime.getRuntime().exec(cmd, null, new File(Constant.gdal_cmd_path));
//        	Process process = Runtime.getRuntime().exec(cmd);
//
//        	/// If using GDAL 1 or 2, which is often installed by linux repo by default
//        	int flag = fileService.handlerInfoProcess(process);
//
//        	if(flag == 1) {
//        		FileUtil.del(rgba_path);
//        		throw new Exception("gdal2tiles.py failed to implement");
//        	} else {
//        		FileUtil.del(rgba_path);
//
//        		progress.setMessage("x_progress_publish_completed");
//                progress.setState(1);
//                progress.setEndTime(DateUtil.date());
//                progressService.updateById(progress);
//
//                //处理切片层级
//                File output = new File(outputPath);
//                String[] level = output.list();
//                if (level != null) {
//                    Arrays.sort(level);
//                    String min = level[0];
//                    String max = level[level.length - 2];
//                    layer.setParameter("{\"maximumLevel\":" + max + ",\"minimumLevel\":" + min + "}");
//                    layer.setGeometry(null);
//                    updateById(layer);
//                }
//
//                //完成后发送邮件
//                send(sysUserService.getById(progress.getUserId()), progress.getName());
//        	}
//        } catch (Exception e) {
//        	e.printStackTrace();
//
//            //删除临时文件
//            FileUtil.del(rgba_path);
//
//            //删除图层及result
//            Long[] layerIds = {progress.getLayerId()};
//            deleteLayer(layerIds);
//
//            //更新进度
//            progress.setMessage("x_progress_create_geotiff_failed");
//            progress.setState(1);
//            progress.setEndTime(DateUtil.date());
//            progressService.updateById(progress);
//
//            //发送失败邮件
//            sendFailed(sysUserService.getById(progress.getUserId()), progress.getName());
//        }
//
//        if (isProjTiff) {
//            FileUtil.del(file);
//        }
//    }
//
//    private void apollo(Long progressId, String tiffPath, SysLayerEntity layer, boolean isProjTiff) throws MessagingException {
//        ProgressEntity progress = progressService.getById(progressId);
//        progress.setComplete(20);
//        progressService.updateById(progress);
//
//        //compress
//        String compress_url = apollo_micro_host + "/file/compress1";
//        String ecwPath = "/" + tiffPath.substring(0, tiffPath.lastIndexOf(".")) + ".ecw";
//        String compress_request = "{\"tiffPath\":\"" + "/file/" + tiffPath + "\", \"ecwPath\":\"" + ecwPath + "\"}";
//        HttpResponse compress_response = HttpRequest.post(compress_url).body(compress_request).timeout(300000).execute();
//        int compress_status = compress_response.getStatus();
//
//        if (compress_status == 200) {
//            progress.setComplete(80);
//            progressService.updateById(progress);
//
//            //publish
//            String publish_url = apollo_micro_host + "/file/publish";
//            HttpResponse publish_response = HttpRequest.post(publish_url).body(compress_response.body()).execute();
//
//            JSONObject jsonObject = JSONObject.parseObject(publish_response.body());
//
//            if (jsonObject.getInteger("code") == 0) {
//                //发布成功 获取返回的parameter
//                String parameter = jsonObject.getString("parameter");
//                layer.setParameter(parameter);
//                layer.setType("WMTS");
//                layer.setUrl(apollo_host + "/erdas-iws/ogc/wmts/" + apollo_service);
//                layer.setGeometry(null);
//                updateById(layer);
//
//                //更新进度
//                progress.setComplete(100);
//                progress.setMessage("x_progress_publish_completed");
//                progress.setState(1);
//                progress.setEndTime(DateUtil.date());
//                progressService.updateById(progress);
//
//                //发送邮件
//                send(sysUserService.getById(progress.getUserId()), progress.getName());
//            } else {
//                progress.setMessage("x_progress_create_geotiff_failed");
//                progress.setState(1);
//                progress.setEndTime(DateUtil.date());
//                progressService.updateById(progress);
//                //发送失败邮件
//                sendFailed(sysUserService.getById(progress.getUserId()), progress.getName());
//            }
//        } else {
//            //更新进度
//            progress.setMessage("x_progress_create_geotiff_failed");
//            progress.setState(1);
//            progress.setEndTime(DateUtil.date());
//            progressService.updateById(progress);
//
//            //发送失败邮件
//            sendFailed(sysUserService.getById(progress.getUserId()), progress.getName());
//        }
//        if (isProjTiff) {
//            FileUtil.del(new File(root + "/" + fileBucket + "/" + tiffPath));
//        }
//    }
//
//    @Override
//    @Async
//    public void handlerPano(Long progressId, List<String> objects, Boolean isBlurFace, String resultPath, String lasPath, File location, Long layerId, String filePath, String eoFormat, String epsg) throws MessagingException, IOException, InterruptedException, SQLException {
//        SqliteDB sqliteDB = null;
//        boolean depthMapFlag = false;
//        //当lasPath=null 或 location不存在时， 不需要进行深度图处理
//        //当lasPath存在时，先生成临时txt并存入sqlite
//        if (lasPath != null && location != null) {
//            //点云转txt
//            String txtPath = resultPath + "las_temp.txt";
//			String[] cmd = { Constant.LAStools_cmd_path + las_cmd, "-i", lasPath, "-o", txtPath, "-parse", "xyzRGBic" };
//            Process process = Runtime.getRuntime().exec(cmd, null, new File(Constant.LAStools_cmd_path));
//
//            fileService.handlerInfoProcess(process);
//
//            //txt数据存入sqlite
//            sqliteDB = importIntoSqlite(txtPath, resultPath);
//
//            //删除txt
//            FileUtil.del(txtPath);
//
//            depthMapFlag = true;
//        }
//
//        if (location != null) {
//            //从文件中读取CSV数据
//            CsvReader reader = CsvUtil.getReader();
//            CsvData data = reader.read(location);
//            List<CsvRow> locationList = data.getRows();
//
//            List<SysPanoEntity> panoList = new ArrayList<>();
//
//            for(String object : objects) {
//                SysPanoEntity pano = new SysPanoEntity();
//                for (int i = 1; i < locationList.size(); i++) {
//                    String panoName = object.substring(object.lastIndexOf("/") + 1);
//                    pano.setPanoName(panoName);
//
//                    //遍历获取参数
//                    if (locationList.get(i).get(0).equals(panoName)) {
//                        double x = Double.parseDouble(locationList.get(i).get(2));
//                        double y = Double.parseDouble(locationList.get(i).get(3));
//                        double z = Double.parseDouble(locationList.get(i).get(4));
//
//                        if (depthMapFlag) {
//                            if (x != -1 && y != -1 && z != -1) {
//                                //深度图处理
//                                String depthUrl = depthMap(x, y, z, panoName, sqliteDB, resultPath);
//                                pano.setDepthUrl(depthUrl.replace(root + "/", docPrefix));
//                            }
//                        }
//
//                        //转换坐标
//                        ProjCoordinate projCoordinate = coordinateToWGS84(epsg, x, y, z);
//                        pano.setZ(z);
//                        String geom = "POINT(" + projCoordinate.x + " " + projCoordinate.y + ")";
//                        pano.setGeom(geom);
//
//                        double pan, tilt, roll;
//                        double rotation_x, rotation_y, rotation_z;
//                        if(eoFormat.equals("OSlam")) {
//                            pan = Double.parseDouble(locationList.get(i).get(7));
//                            tilt = Double.parseDouble(locationList.get(i).get(6));
//                            roll = Double.parseDouble(locationList.get(i).get(5));
//
//                            rotation_x = roll;
//                            rotation_y = -tilt;
//                            rotation_z = pan;
//                        } else {
//                            pan = Double.parseDouble(locationList.get(i).get(5));
//                            tilt = Double.parseDouble(locationList.get(i).get(6));
//                            roll = Double.parseDouble(locationList.get(i).get(7));
//
//                            rotation_x = roll * Math.PI / 180;
//                            rotation_y = -tilt * Math.PI / 180;
//                            rotation_z = (-pan - 90) * Math.PI / 180;
//                        }
//
//                        pano.setRotationX(rotation_x);
//                        pano.setRotationY(rotation_y);
//                        pano.setRotationZ(rotation_z);
//
//                        pano.setUrl(resultPath.replace(root + "/", docPrefix));
//                        pano.setLayerId(layerId);
//
//                        /// Debug check what happened to the pano under eo
//                        panoList.add(pano);
//                    }
//                }
//            }
//            //批量insert
//            sysPanoService.insertBatch(panoList);
//
//            //设置边界
//            setPanoBoundary(layerId);
//        }
//
//        ProgressEntity progress = progressService.getById(progressId);
//        int origin = progress.getComplete();
//
//        //处理全景图片
//        for (String object : objects) {
//            String name = ossService.getObjectName(object);
//            System.out.println(name + " start");
//            //处理全景图片
//            try {
//                Panorama panorama = new Panorama(root + "/" + fileBucket + "/" + object, isBlurFace, resultPath);
//                panorama.process();
//                System.out.println(name + " end");
//
//                progress.setComplete(++origin);
//                progressService.updateById(progress);
//
//                if (origin == progress.getTotal()) {
//                    progress.setMessage("x_progress_publish_completed");
//                    progress.setState(1);
//                    progress.setEndTime(DateUtil.date());
//                    progressService.updateById(progress);
//                }
//            } catch (Exception e) {
//                //更新进度
//                e.printStackTrace();
//
//                if (e.getMessage().contains("timed out")) {
//                    progress.setMessage("x_progress_create_pano_timeout");
//                } else {
//                    progress.setMessage(e.getMessage());
//                }
//                progress.setState(1);
//                progress.setEndTime(DateUtil.date());
//                progressService.updateById(progress);
//
//                //删除已发布的图层及result
//                Long[] layerIds = {progress.getLayerId()};
//                deleteLayer(layerIds);
//
//                //发送失败邮件
//                sendFailed(sysUserService.getById(progress.getUserId()), progress.getName());
//
//                return;
//            }
//        }
//
//        //完成后发送邮件
//        send(sysUserService.getById(progress.getUserId()), progress.getName());
//
//        if (sqliteDB != null) {
//            sqliteDB.KillConnection(resultPath + "tmp.db");
//        }
//    }
//
//    @Override
//    @Async
//    public void handlerGSM(Long progressId, String csvPath, String resultFolder, String resultPath, String epsg) throws IOException, MessagingException {
//    	//更新进度
//        ProgressEntity progress = progressService.getById(progressId);
//
//    	// Create folder
//    	File resultFolderFile = new File(resultFolder);
//        if (!resultFolderFile.exists()) {
//        	resultFolderFile.mkdirs();
//        }
//
//        CsvReader reader = CsvUtil.getReader();
//        CsvData inputCSV = reader.read(FileUtil.file(csvPath));
//        List<CsvRow> inputList = inputCSV.getRows();
//
//        int rowID;
//        CsvRow headingRow = inputList.get(0);
//
//        // Create writer for result csv
//        FileWriter fw = new FileWriter(resultPath);
//
//        // Write the entire header row
//        String headingRowStr = "";
//        for(int i=0; i<headingRow.size(); i++) {
//        	if(i!=0)
//        		headingRowStr += ",";
//        	headingRowStr += headingRow.get(i);
//        }
//        headingRowStr += "\n";
//        fw.write(headingRowStr);
//
//        String rowToWrite = "";
//        for(rowID=1; rowID<inputList.size(); rowID++) {
//        	CsvRow dataRow = inputList.get(rowID);
//        	if(epsg.equals("4326")) {
//        		rowToWrite = dataRow.get(0)+","+dataRow.get(1)+","+dataRow.get(2)+","+dataRow.get(3)+","+dataRow.get(4)+","+dataRow.get(5);
//        		for(int ii=6; ii<dataRow.size(); ii++) {
//        			rowToWrite += ","+dataRow.get(ii);
//        		}
//        		rowToWrite += "\n";
//        		fw.write(rowToWrite);
//        	}
//        	else {
//        		double x = Double.parseDouble(dataRow.get(0));
//        		double y = Double.parseDouble(dataRow.get(1));
//        		double z = Double.parseDouble(dataRow.get(2));
//
//        		ProjCoordinate projCoordinate = coordinateToWGS84(epsg, x, y, z);
//        		// fw.write(projCoordinate.x +","+ projCoordinate.y +","+ projCoordinate.z +","+dataRow.get(3)+","+dataRow.get(4)+","+dataRow.get(5)+"\n");
//        		rowToWrite = projCoordinate.x +","+ projCoordinate.y +","+ projCoordinate.z +","+dataRow.get(3)+","+dataRow.get(4)+","+dataRow.get(5);
//        		for(int ii=6; ii<dataRow.size(); ii++) {
//        			rowToWrite += ","+dataRow.get(ii);
//        		}
//        		rowToWrite += "\n";
//        		fw.write(rowToWrite);
//        	}
//        }
//        fw.close();
//
//        progress.setComplete(100);
//        progress.setMessage("x_progress_publish_completed");
//        progress.setState(1);
//        progress.setEndTime(DateUtil.date());
//        progressService.updateById(progress);
//
//        send(sysUserService.getById(progress.getUserId()), progress.getName());
//    }
//
//    @Override
//    @Async
//    public void generateDEM(String resultPath, String tiffPath, String epsg, long progressId, long layerId) throws IOException, InterruptedException, MessagingException {
//    	SysLayerEntity layer = this.getById(layerId);
//    	ProgressEntity progress = progressService.getById(progressId);
//        //tif处理
//    	File file = new File(tiffPath);
//        // String tif_name = file.getName().substring(0, file.getName().indexOf("."));
//        String maskedTiffPath = resultPath + File.separator + "msk.tif";
//        // String msk_cmd = Constant.gdal_cmd_path + "gdal_translate -mask 2 " + file.getAbsolutePath() + " " + maskedTiffPath + "";
//		String[] msk_cmd = { Constant.gdal_cmd_path+"gdal_translate", "-mask", "2", file.getAbsolutePath(), maskedTiffPath };
//        System.out.println(msk_cmd);
//
//        Process pro_msk = Runtime.getRuntime().exec(msk_cmd, null, new File(Constant.gdal_cmd_path));
//        fileService.handlerInfoProcess(pro_msk);
//
//        try {
//        	progress.setComplete(20);
//
//        	Geotiff2Terrain gt = new Geotiff2Terrain(maskedTiffPath, resultPath, epsg);
//        	// layer.setGeometry(gt.getExtentGeom());
//        	gt.setProgressEntity(progress);
//        	gt.setProgressService(progressService);
//        	// this.updateLayer(layer);
//            gt.getHeightMap();
//
//            progress.setComplete(100);
//            progress.setMessage("x_progress_publish_completed");
//            progress.setState(1);
//            progress.setEndTime(DateUtil.date());
//            progressService.updateById(progress);
//
//            //完成后发送邮件
//            send(sysUserService.getById(progress.getUserId()), progress.getName());
//            FileUtil.del(maskedTiffPath);
//        }
//        catch(Exception e) {
//        	/// TODO: Write the exception's message to system log
//        	System.out.println(e.getMessage());
//
//        	//删除图层及result
//            Long[] layerIds = {progress.getLayerId()};
//            deleteLayer(layerIds);
//
//        	progress.setMessage("x_progress_create_geotiff_failed");
//            progress.setState(1);
//            progress.setEndTime(DateUtil.date());
//            progressService.updateById(progress);
//
//            sendFailed(sysUserService.getById(progress.getUserId()), progress.getName());
//            // FileUtil.del(maskedTiffPath);
//        }
//        /*
//        finally {
//        	FileUtil.del(maskedTiffPath);
//        }
//        */
//    }
//
//	@Override
//    @Async
//	public void handlerSHP(Long progressId, String shpPath, String tableTitle, String type) throws IOException, InterruptedException, MessagingException {
//        String host = url.substring(url.indexOf("//") + 2, url.lastIndexOf(":"));
//        String port = url.substring(url.lastIndexOf(":") + 1, url.lastIndexOf("/"));
//        String dbname = url.substring(url.lastIndexOf("/") + 1);
//
//        String dim_value;
//        if (type.contains("3D")) {
//            dim_value = "3";
//        } else {
//            dim_value = "2";
//        }
//
//		String[] shp2pgsql_exec = { Constant.gdal_cmd_path + ogr2ogr_cmd, "-f", "PostgreSQL", "-t_srs", "EPSG:4326", "-dim", dim_value, "PG: host="
//                + host + " port=" + port + " dbname=" + dbname + " user="
//                + username + " password=" + password, shpPath, "-nln", tableTitle };
//
//        System.out.println(Arrays.toString(shp2pgsql_exec));
//
//        Process process = Runtime.getRuntime().exec(shp2pgsql_exec, null, new File(Constant.gdal_cmd_path));
//
//        int flag = fileService.handlerInfoProcess(process);
//
//        ProgressEntity progress = progressService.getById(progressId);
//
//        if (flag == 1) {
//            System.out.println("检查shapefile文件");
//
//            //删除图层
//            Long[] layerIds = {progress.getLayerId()};
//            deleteLayer(layerIds);
//
//            //更新进度
//            progress.setMessage("x_progress_convert_postgres_failed");
//            progress.setState(1);
//            progress.setEndTime(DateUtil.date());
//            progressService.updateById(progress);
//
//            //发送失败邮件
//            sendFailed(sysUserService.getById(progress.getUserId()), progress.getName());
//            return;
//        }
//
//        //更新进度
//        progress.setComplete(100);
//        progress.setMessage("x_progress_publish_completed");
//        progress.setState(1);
//        progress.setEndTime(DateUtil.date());
//        progressService.updateById(progress);
//
//        //完成后发送邮件
//        send(sysUserService.getById(progress.getUserId()), progress.getName());
//	}
//
//    private void send(SysUserEntity user, String title) throws MessagingException {
//        String subject = "Publishing Layer on SDMS Success/图层发布成功";
//        String content = mailUtils.publishMailTemplate(user.getUsername(), title);
//        mailUtils.send(user, subject, content);
//    }
//
//    private void sendFailed(SysUserEntity user, String title) throws MessagingException {
//        String subject = "Publishing Layer on SDMS Failed/图层发布失败";
//        String content = mailUtils.publishFailedMailTemplate(user.getUsername(), title);
//        mailUtils.send(user, subject, content);
//    }
//
//    private File saveAsFile(String path, String text) {
//        File file = new File(path);
//        try {
//            FileWriter fileWriter = new FileWriter(file);
//            fileWriter.write(text);
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return file;
//    }
//
//    private void executeSqlScript(File file) throws ClassNotFoundException, SQLException, IOException {
//        Class.forName(driver);
//        Connection conn = DriverManager.getConnection(url, username, password);
//        ScriptRunner runner = new ScriptRunner(conn);
//        FileReader fileReader = new FileReader(file);
//        try {
//            runner.setStopOnError(true);
//            runner.runScript(fileReader);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            fileReader.close();
//        }
//        runner.closeConnection();
//        conn.close();
//    }
//
//    @Override
//    public void updatePublished(double size, Long companyId, Long userId) {
//        //更新发布空间容量
//        if (sysCompanyService.getPersonalTrial(companyId)) {
//            sysUserService.updatePublished(userId, size);
//        } else {
//            //size = sysCompanyService.getPublished(companyId) + size;
//            sysCompanyService.updatePublished(companyId, size);
//        }
//    }
//
//	@Override
//	public void thumbnailVideo(String result, List<String> objects, Long progressId) throws IOException,
//            InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException,
//            XmlParserException, InsufficientDataException, InternalException, MessagingException {
//
//        ProgressEntity progress = progressService.getById(progressId);
//
//        for (String object : objects) {
//            String thumbnail = result + object.substring(object.lastIndexOf("/") + 1);
//
//            InputStream inputStream = minioUtil.getObject(fileBucket, object);
//
//            FFmpegFrameGrabber grabber;
//            InputStream img;
//
//            try {
//                grabber = new FFmpegFrameGrabber(inputStream, 0);
//                grabber.start();
//
//                //视频总帧数
//                int videoLength = grabber.getLengthInFrames();
//
//                Frame frame = null;
//                int i = 0;
//                while (i < videoLength) {
//                    // 过滤前5帧,因为前5帧可能是全黑的
//                    frame = grabber.grabFrame();
//                    if (i > 5 && frame.image != null) {
//                        break;
//                    }
//                    i++;
//                }
//
//                // 绘制图片
//                Java2DFrameConverter converter = new Java2DFrameConverter();
//                BufferedImage bufferedImage = converter.getBufferedImage(frame);
//                ByteArrayOutputStream os = new ByteArrayOutputStream();
//                ImageIO.write(bufferedImage, "png", os);
//                img = new ByteArrayInputStream(os.toByteArray());
//
//                //压缩
//                ByteArrayOutputStream thumbnailOs = new ByteArrayOutputStream();
//                Thumbnails.of(img).scale(DEFAULT_SCALE).toOutputStream(thumbnailOs);
//
//                //上传至minio
//                minioUtil.upload(new ByteArrayInputStream(thumbnailOs.toByteArray()), fileBucket, thumbnail, "image/jpeg");
//
//                grabber.stop();
//                grabber.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            //更新进度
//            progress.setComplete(progress.getComplete() + 1);
//            progressService.updateById(progress);
//
//            if (progress.getComplete().equals(progress.getTotal())) {
//                progress.setMessage("x_progress_publish_completed");
//                progress.setState(1);
//                progress.setEndTime(DateUtil.date());
//                progressService.updateById(progress);
//            }
//        }
//
//        //发送邮件
//		send(sysUserService.getById(progress.getUserId()), progress.getName());
//	}
//
//    @Override
//    public String querySrc(Long layerId) {
//        return baseMapper.querySrc(layerId);
//    }
//
//    @Override
//    public String queryUrl(Long LayerId) {
//        return baseMapper.queryUrl(LayerId);
//    }
//
//    @Override
//    public String queryParam(Long layerId) {
//        return baseMapper.queryParam(layerId);
//    }
//
//    @Override
//    public void updateParam(Long layerId, String param) {
//        baseMapper.updateParam(layerId, param);
//    }
//
//    @Override
//    public List<SysLayerEntity> search(Map<String, Object> params) {
//        if (params.get("category") != null) {
//            String categories = (String) params.get("category");
//            params.replace("category", categories.replace(",", "','"));
//        }
//        return baseMapper.search(params);
//    }
//
//    @Override
//    public String getGeomInfo(File file) throws IOException, InterruptedException {
//        String[] info = { Constant.gdal_cmd_path + "gdalinfo", file.getAbsolutePath(), "-json" };
//        Process pro = Runtime.getRuntime().exec(info, null, new File(Constant.gdal_cmd_path));
//
//        String result = fileService.processInfo(pro);
//
//        JSONObject json = JSONObject.parseObject(result);
//
//        return json.getString("wgs84Extent");
//    }
//
//    private File createProjTiff(File file, String epsg) throws IOException, InterruptedException {
//        String projFilePath = file.getParent() + File.separator + file.getName().substring(0, file.getName().lastIndexOf(".")) + "_proj.tif";
//        String[] proj = {Constant.gdal_cmd_path + "gdalwarp", "-t_srs", "EPSG:" + epsg, file.getAbsolutePath(), projFilePath};
//        Process proj_process = Runtime.getRuntime().exec(proj, null, new File(Constant.gdal_cmd_path));
//        int flag = fileService.handlerInfoProcess(proj_process);
//
//        if(flag == 0) {
//            return new File(projFilePath);
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public void setMediaBoundary(Long layerId) {
//        baseMapper.setMediaBoundary(layerId);
//    }
//
//    @Override
//    public void setPanoBoundary(Long layerId) {
//        baseMapper.setPanoBoundary(layerId);
//    }
//
//    @Override
//    public Integer sumSize(Long companyId, Long projectId) {
//        return baseMapper.sumSize(companyId, projectId);
//    }
//
//    /**
//     * Run OpenDroneMap via micro service
//     */
//    @Override
//    @Async
//    public void handlerODM(Long progressId, String rootPath, String odmImageFolderSubPath, String salt, Long companyId, Long userId, String title) {
//    	ProgressEntity progress = progressService.getById(progressId);
//
//    	String odm_micro_url = odm_micro_host + "/run";
//    	String reqJsonStr = "{\"mainPath\":\"" + rootPath + "\",\"subPath\":\"" + odmImageFolderSubPath + "\"}";
//
//    	HttpResponse odm_response = HttpRequest.post(odm_micro_url).body(reqJsonStr).timeout(Integer.MAX_VALUE).execute();
//    	int odm_status = odm_response.getStatus();
//
//    	if(odm_status == 200) {
//    		progress.setComplete(50);
//            progress.setMessage("x_progress_create_tiles");
//            progressService.updateById(progress);
//
//    		String respJsonStr = odm_response.body();
//    		JSONObject respJson = JSONObject.parseObject(respJsonStr);
//
//    		String resultFolderName = respJson.getString("output");
//
//    		// List paths to different types of outcomes
//    		/*
//    		 * What coordinate system are the outputs? Are the 3D outputs geo-referenced?
//    		 * (Like Pix4D, pointcloud georeference by with random UTM, 3D object no)
//    		 * Where and how to read the AT of the photos?
//    		 */
//    		String rawImgPath = rootPath + "/result/" + resultFolderName + "/project/images";
//
//    		String projTxtPath = rootPath + "/result/" + resultFolderName + "/project/odm_georeferencing/proj.txt";
//    		String centralCoordTxtPath = rootPath + "/result/" + resultFolderName + "/project/odm_georeferencing/odm_georeferencing_model_geo.txt";
//
//    		String orthoPath = rootPath + "/result/" + resultFolderName + "/project/odm_orthophoto/odm_orthophoto.tif";
//    		String pointcloudPath = rootPath + "/result/" + resultFolderName + "/project/odm_georeferencing/odm_georeferenced_model.las";
//
//    		String dsmPath = rootPath + "/result/" + resultFolderName + "/project/odm_dem/dsm.tif";
//    		String dtmPath = rootPath + "/result/" + resultFolderName + "/project/odm_dem/dtm.tif";
//
//    		// Two objs. Another one is in folder 'odm_texturing_25d', but seems like it is a dsm, not using it
//    		String objPath = rootPath + "/result/" + resultFolderName + "/project/odm_texturing/odm_textured_model_geo.obj";		// No geolocation although ODM announced to have
//
//    		/// ---------------- The following segment will be regarded as highly duplicated in coding. Generalize the code when possible later.
//    		/*
//    		 * 1. Publish media layer, should be the only one without concern for coordinate system
//    		 */
//    		try {
//				publishPhotoLayer(rawImgPath, title+"_media", companyId, userId);
//			} catch (ImageProcessingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (MessagingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//
//    	}
//    	else {
//    		progress.setMessage("x_progress_create_geotiff_failed");
//            progress.setState(1);
//            progress.setEndTime(DateUtil.date());
//            progressService.updateById(progress);
//    	}
//    }
//
//
//    /***************************************************私有方法**************************************************/
//
//    /**
//     * txt数据导入sqlite
//     * @param outputPath
//     * @return
//     * @throws SQLException
//     * @throws IOException
//     */
//    private SqliteDB importIntoSqlite(String txtPath, String outputPath) throws SQLException, IOException {
//        SqliteDB sqliteDB = new SqliteDB();
//        System.out.println("Creating a temporary database in the output folder");
//        sqliteDB.createConnection(outputPath + "tmp.db");
//        sqliteDB.createDataTable();
//
//        System.out.println("Import.....");
//        BufferedReader br = null;
//        try {
//            double minZ = 1.7976931348623157E308D;
//            br = new BufferedReader(new FileReader(txtPath));
//
//            List<PanoPointEntity> panoPointList = new ArrayList<>();
//
//            String line;
//            while((line = br.readLine()) != null) {
//                String[] valueArray = line.split(" ");
//                double x = Double.parseDouble(valueArray[0]);
//                double y = Double.parseDouble(valueArray[1]);
//                double z = (Double.parseDouble(valueArray[2]) + 0.0D) * 1.0D;
//                if (Double.parseDouble(valueArray[2]) < minZ) {
//                    minZ = Double.parseDouble(valueArray[2]);
//                }
//
//                panoPointList.add(new PanoPointEntity(x, y, z));
//
//                if (panoPointList.size() % 1000 == 0) {
//                    sqliteDB.importIntoDatabase(panoPointList);
//                    panoPointList = new ArrayList<>();
//                }
//            }
//            sqliteDB.importIntoDatabase(panoPointList);
//        } catch (NumberFormatException e) {
//            throw new IOException("Invalid coordinate or color value in source files.", e);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (br != null) {
//                    br.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("Import over.....");
//
//        System.out.println("Creating database index");
//        sqliteDB.createIndexes();
//        System.out.println("Creating database index over");
//
//        return sqliteDB;
//    }
//
//    /**
//     * 深度图处理
//     * @param _x
//     * @param _y
//     * @param _z
//     * @param fileName
//     * @param sqliteDB
//     */
//    private String depthMap (double _x, double _y, double _z, String fileName, SqliteDB sqliteDB, String output) {
//        short[][] depthMap = new short[1024][512];
//        for(int i = 0; i < 1024; i++)
//            Arrays.fill(depthMap[i], Short.MAX_VALUE);
//
//        List<PanoPointEntity> panoPoints = sqliteDB.queryByDistance(_x, _y, _z);
//
//        for (PanoPointEntity panoPoint : panoPoints) {
//            double x = panoPoint.getX();
//            double y = panoPoint.getY();
//            double z = panoPoint.getZ();
//
//            double dist = Math.pow(x - _x, 2) + Math.pow(y - _y, 2) + Math.pow(z - _z, 2);
//
//            dist = Math.sqrt(dist);
//            // Get heading and pitch
//            double heading = Math.atan2((x - _x), (y-_y));
//            double pitch = Math.acos((z -_z)/dist);
//
//            if(heading < 0)
//                heading += 2 * Math.PI;
//            int headingIdx = (int) Math.floor(1024 * heading / (2 * Math.PI));
//            int pitchIdx = (int) Math.floor(512 * pitch / Math.PI);
//
//            int distInMM = (int) Math.round(dist * 1000) - Short.MAX_VALUE - 1;
//            short distMMShort = (short) distInMM;
//            if(distMMShort < depthMap[headingIdx][pitchIdx])
//                depthMap[headingIdx][pitchIdx] = distMMShort;
//        }
//
//        for(int size = 1; size < 6; size++) {
//            int initI = size;
//
//            Map<String, Short> depthHelper = new HashMap<String, Short>();
//
//            if(size == 4)
//                initI = 256;
//            else if(size == 5)
//                initI = 384;
//            for(int i = initI; i < 512; i++) {
//                for(int j = 0; j < 1024; j++) {
//                    int depth = depthMap[j][i];
//                    if(depth == Short.MAX_VALUE) {
//                        short maxDepth = Short.MAX_VALUE;
//
//                        double totalChecks = 0;
//                        double hitChecks = 0;
//                        double totalDepth = 0;
//                        double totalDepthWeight = 0;
//                        for(int x = j - size; x <= j + size; x++) {
//                            for(int y = i - size; y <= i + size; y++) {
//                                if(y < 0 || y >= 512)
//                                    continue;
//
//                                totalChecks++;
//                                int x_ = x;
//                                if(x_ < 0)
//                                    x_ += 1024;
//                                else if(x_ >= 1024)
//                                    x_ -= 1024;
//
//                                try {
//                                    if(depthMap[x_][y]<Short.MAX_VALUE) {
//                                        hitChecks++;
//                                        if(size >= 2) {
//                                            double theWeight = 1 / (Math.pow((j - x), 2) + Math.pow((i - y), 2));
//                                            totalDepth += depthMap[x_][y] * theWeight;
//                                            totalDepthWeight += theWeight;
//                                        }
//                                    }
//
//                                    if(depthMap[x_][y] < maxDepth)
//                                        maxDepth = depthMap[x_][y];
//                                }
//                                catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                        // In the size 4 mode, force ground filling...
//                        // Just with the nearest first, but not robustly correct of course...
//                        if(hitChecks / (totalChecks-1) >= 0.4 || size >= 4) {
//                            if(size >= 3) {
//                                double tempDepth = totalDepth/totalDepthWeight;
//                                depthHelper.put(j + "-" + i, (short) Math.round(maxDepth));
//                            }
//                            else
//                                depthHelper.put(j + "-" + i, maxDepth);
//                        }
//                    }
//                }
//            }
//
//            Iterator mit = depthHelper.entrySet().iterator();
//            while (mit.hasNext()) {
//                Map.Entry pair = (Map.Entry)mit.next();
//                short depth = (short) pair.getValue();
//                if(depth != Short.MAX_VALUE) {
//                    if(depth == Short.MIN_VALUE)
//                        depth += 1;
//
//                    String strKey = (String) pair.getKey();
//                    String[] strIdx = strKey.split("-");
//                    depthMap[Integer.parseInt(strIdx[0])][Integer.parseInt(strIdx[1])] = depth;
//                }
//
//                mit.remove(); // avoids a ConcurrentModificationException
//            }
//        }
//
//        byte[] depthList = new byte[1024*512*2];
//
//        /// TODO: Duplicate eliminator, using Short.MIN_VALUE as identifier or duplication count
//        short depthCompare = Short.MIN_VALUE;
//        short depthCount = Short.MIN_VALUE;		// Transfer from 0 to Short.MIN_VALUE
//        int insertKey = 0;
//
//        short[] depthListDebug = new short[1024 * 512 * 2];
//
//        for(int i = 0; i < 512; i++) {
//            for(int j = 0; j < 1024; j++) {
//                short depth = depthMap[j][i];
//
//                // Check with the candidate depth value to see whether identical
//                if(depth == depthCompare)
//                    depthCount++;
//                else {
//                    int identicalCount = depthCount-Short.MIN_VALUE;
//
//                    if(identicalCount > 3) {		// Write as: val / short.min / count
//                        // Note that we now only accept 65535 identical values in row. For more identicals, start another data record
//                        int seed = 65535;
//                        int valGroups = (identicalCount / seed) + 1;	// How many rows it needs to go
//
//                        // To be continue
//                        for(int k = 0; k < valGroups; k++) {
//                            short identifierNum = Short.MIN_VALUE;
//                            short partCount;
//                            if(k != valGroups - 1)
//                                partCount = Short.MAX_VALUE;
//                            else {
//                                int pcInt = identicalCount - 65535 * (valGroups-1);
//                                partCount = (short) (pcInt - 32768);
//                            }
//
//                            /*depthList[insertKey] = (byte)((depthCompare >> 8) & 0xff);
//                            depthList[insertKey+1] = (byte)(depthCompare & 0xff);
//                            depthList[insertKey+2] = (byte)((identifierNum >> 8) & 0xff);
//                            depthList[insertKey+3] = (byte)(identifierNum & 0xff);
//                            depthList[insertKey+4] = (byte)((partCount >> 8) & 0xff);
//                            depthList[insertKey+5] = (byte)(partCount & 0xff);*/
//
//                            depthList[insertKey+5] = (byte)((partCount >> 8) & 0xff);
//                            depthList[insertKey+4] = (byte)(partCount & 0xff);
//                            depthList[insertKey+3] = (byte)((identifierNum >> 8) & 0xff);
//                            depthList[insertKey+2] = (byte)(identifierNum & 0xff);
//                            depthList[insertKey+1] = (byte)((depthCompare >> 8) & 0xff);
//                            depthList[insertKey] = (byte)(depthCompare & 0xff);
//
//                            depthListDebug[(insertKey / 2)] = depthCompare;
//                            depthListDebug[(insertKey / 2) + 1] = -32768;
//                            depthListDebug[(insertKey / 2) + 2] = partCount;
//                            insertKey += 6;
//                        }
//                    }
//                    else {	// Just write the value as it be
//                        for(int k = 0; k < identicalCount; k++) {
//                            // Short to big endian byte
//                            /*depthList[insertKey] = (byte)((depthCompare >> 8) & 0xff);
//                            depthList[insertKey+1] = (byte)(depthCompare & 0xff);*/
//
//                            depthList[insertKey+1] = (byte)((depthCompare >> 8) & 0xff);
//                            depthList[insertKey] = (byte)(depthCompare & 0xff);
//
//                            depthListDebug[insertKey/2] = depthCompare;
//                            insertKey += 2;
//                        }
//                    }
//                    depthCount = Short.MIN_VALUE+1;
//                    depthCompare = depth;
//                }
//            }
//        }
//
//        //depthCompare
//        /*depthList[insertKey] = (byte)((depthCompare >> 8) & 0xff);
//        depthList[insertKey+1] = (byte)(depthCompare & 0xff);*/
//        depthList[insertKey+1] = (byte)((depthCompare >> 8) & 0xff);
//        depthList[insertKey] = (byte)(depthCompare & 0xff);
//
//        byte[] depthListTrim = new byte[insertKey + 2];
//        for(int i = 0; i < insertKey + 2; i++) {
//            depthListTrim[i] = depthList[i];
//        }
//
//        String depth = output + "depth/";
//
//        File directoryDepth = new File(depth);
//        if (!directoryDepth.exists())
//            directoryDepth.mkdirs();
//
//        String depthFile = depth + fileName + ".bin";
//        try (FileOutputStream fos = new FileOutputStream(depthFile)) {
//            fos.write(depthListTrim);
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//
//        return depthFile;
//    }
//
//    /**
//     * 转换WGS84坐标
//     * @param epsg
//     * @param longitude
//     * @param latitude
//     * @param height
//     * @return
//     * @throws IOException
//     */
//    private ProjCoordinate coordinateToWGS84(String epsg, double longitude, double latitude, double height) throws IOException {
//        Proj4FileReader proj4FileReader = new Proj4FileReader();
//
//        CRSFactory crsFactory = new CRSFactory();
//
//        //获取源坐标系参数
//        String[] coordinateParam = proj4FileReader.readParametersFromFile("epsg", epsg);
//
//        //源坐标系
//        CoordinateReferenceSystem sourceCoordinate = crsFactory.createFromParameters(epsg, coordinateParam);
//
//        //目标坐标系
//        CoordinateReferenceSystem targetCoordinate = crsFactory.createFromParameters("4326", WGS84);
//
//        //创建坐标系转换对象
//        CoordinateTransformFactory transformFactory = new CoordinateTransformFactory();
//        CoordinateTransform transform = transformFactory.createTransform(sourceCoordinate, targetCoordinate);
//
//        //坐标转换
//        ProjCoordinate projCoordinate = new ProjCoordinate(longitude, latitude, height);
//        transform.transform(projCoordinate, projCoordinate);
//
//        return  projCoordinate;
//    }
//
//    /**
//     * 路径转换
//     * @param path
//     * @return
//     */
//    private String pathConvert(String path) {
//        String finalPath = null;
//
//        if (path.contains(Constant.prefix)) {
//            finalPath = path.replace(Constant.prefix, "/sdms/doc/");
//        }
//
//        finalPath = finalPath.replace("\\", "/");
//
//        return finalPath;
//    }
//
//    /**
//     * 读取图片坐标信息公共方法
//     * @param file
//     * @param fms
//     * @throws IOException
//     * @throws ImageProcessingException
//     */
//    public void readCoordinate(File file, SysMediaEntity fms) throws IOException, ImageProcessingException, ParseException {
//        FileInputStream inputStream = new FileInputStream(file);
//
//        //获取图片的经纬度坐标
//        Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
//
//        for (Directory directory : metadata.getDirectories()) {
//            for (Tag tag : directory.getTags()) {
//                switch (tag.getTagName()) {
//                    case "GPS Altitude":
//                        //海拔
//                        String altitude = tag.getDescription();
//                        fms.setHeight(Double.valueOf(altitude.substring(0, altitude.length() - 7)));
//                        break;
//                    case "GPS Latitude":
//                        //纬度
//                        String latitude = tag.getDescription();
//                        fms.setLatitude(convert(latitude));
//                        break;
//                    case "GPS Longitude":
//                        //经度
//                        String longitude = tag.getDescription();
//                        fms.setLongitude(convert(longitude));
//                        break;
//                    case "Date/Time Digitized":
//                    case "Date/Time Original":
//                        fms.setPublishTime(date(tag.getDescription()));
//                        break;
//                }
//            }
//        }
//        if (fms.getLatitude() == null || fms.getLongitude() == null) {
//            //根据具体情况设置默认坐标信息
//            defaultCoordinate(fms);
//        }
//
//        //没有时间则默认当前时间
//        if (fms.getPublishTime() == null) {
//            fms.setPublishTime(DateUtil.date());
//        }
//
//        fms.setOmega((double) -1);
//        fms.setPhi((double) -1);
//        fms.setKappa((double) -1);
//
//        inputStream.close();
//    }
//
//    /**
//     * 设置默认坐标信息公共方法
//     * @param fms
//     */
//    private void defaultCoordinate(SysMediaEntity fms) {
//        fms.setLatitude((double) -1);
//        fms.setLongitude((double) -1);
//        fms.setHeight((double) -1);
//        fms.setOmega((double) -1);
//        fms.setPhi((double) -1);
//        fms.setKappa((double) -1);
//    }
//
//    private Double convert(String string) {
//
//        double degrees = Double.parseDouble(string.substring(0, string.indexOf("°")));
//        double minutes = Double.parseDouble(string.substring(string.indexOf("°") + 2, string.indexOf("'")));
//        double seconds = Double.parseDouble(string.substring(string.indexOf("'") + 2, string.length() - 1));
//
//        return degrees + minutes / 60 + seconds / 3600;
//    }
//
//    private Date date(String string) throws ParseException {
//        String date = string.substring(0, 10);
//        String time = string.substring(11, string.length());
//
//        date = date.replace(":", "-");
//
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        String formatDate = date + " " + time;
//
//        return format.parse(formatDate);
//    }
//
//    /**
//     * 读取图片时间信息
//     * @param file
//     * @param fms
//     * @throws IOException
//     * @throws ImageProcessingException
//     * @throws ParseException
//     */
//    private void readTime(File file, SysMediaEntity fms) throws IOException, ImageProcessingException, ParseException {
//        FileInputStream inputStream = new FileInputStream(file);
//
//        //获取图片的经纬度坐标
//        Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
//
//        boolean isEXIFHasDate = false;
//        for (Directory directory : metadata.getDirectories()) {
//            for (Tag tag : directory.getTags()) {
//                switch (tag.getTagName()) {
//                    case "Date/Time Digitized":
//                    case "Date/Time Original":
//                        fms.setPublishTime(date(tag.getDescription()));
//                        isEXIFHasDate = true;
//                        break;
//                }
//            }
//        }
//
//        if(!isEXIFHasDate) {
//            fms.setPublishTime(new Date(file.lastModified()));
//        }
//
//        inputStream.close();
//    }
//
//
//    /**
//     * May need a function to get epsg from UTM number
//     */
//    private void getEpsgFromProjTxt (String projTxtPath) {
//    	try (Stream<String> stream = Files.lines(Paths.get(projTxtPath))) {
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    }
//
//
//    /**
//     * Publish media layer like in SysLayerController.java
//     * @throws IOException
//     * @throws ParseException
//     * @throws ImageProcessingException
//     * @throws MessagingException
//     */
//    private void publishPhotoLayer (String filePath, String title, Long companyId, Long userId) throws IOException, ImageProcessingException, ParseException, MessagingException {
//        // Long userId = getUserId();
//        // Long companyId = getCompanyId(request);
//
//        //参数信息
//        // String path = (String) params.get("path");
//        String epsg = "4326";
//        boolean isPrivate = true;
//
//        String[] filePathToUrlMid = filePath.split(sdms_file_path);
//        String fileUrl = filePathToUrlMid[1];
//
//        //获取图片
//        File imageFile = new File(filePath);
//        FileFilter fileFilter = files -> files.getName().endsWith(".JPG")
//                || files.getName().endsWith(".TIF")
//                || files.getName().endsWith(".jpg")
//                || files.getName().endsWith(".tif")
//                || files.getName().endsWith(".jfif")
//                || files.getName().endsWith(".JFIF");
//        List<File> fileList = fileService.listFile(imageFile, fileFilter, Messages.get("x_media_file_error"));
//        String filename = title(imageFile);
//
//        //获取要发布文件的大小
//        double size = fileService.publishSize(fileList, "media");
//        //判断空间是否足够
//        if (!fileService.isPublish(size, companyId, userId)){
//            // return R.error(Messages.get("x_publish_size_out_error"));
//        	/// Should there be an error about the
//        }
//
//        //新增图层
//        SysLayerEntity layer = new SysLayerEntity();
//
//        //图片处理结果存放路径
//        String resultPath = filePath + "_" + RandomStringUtils.randomAlphanumeric(6);
//        String thumbnail_url = "";
//        if (apollo) {
//            thumbnail_url = apollo_host + "/erdas-iws/erdas/imagex/" + apollo_service +"?request=image&type=jpg&style=default&sizex=512&sizey=512&transparent=true&type=webp&layers=";
//        } else {
//            new File(resultPath).mkdirs();
//            layer.setUrl(pathConvert(resultPath + "/thumbnail"));
//        }
//
//        layer.setUserId(userId);
//        layer.setTitle(title);
//        layer.setType("PHOTO");
//        layer.setCategory("PHOTO");
//        layer.setParameter(epsg);
//        layer.setCompanyId(companyId);
//        layer.setIsPrivate(isPrivate);
//        layer.setSize(size);
//        layer.setSrc(fileUrl);
//        //除发布OBJ,GeoTiff图层使用saveLayer外，其余发布图层均使用save
//        save(layer);
//
//        //新建图层发布进度
//        ProgressEntity progress = new ProgressEntity();
//        progress.setName(title);
//        progress.setUserId(userId);
//        progress.setStartTime(DateUtil.date());
//        progress.setTotal(fileList.size());
//        progress.setState(0);
//        progress.setMessage("x_progress_create_thumbnail");
//        progress.setLayerId(layer.getLayerId());
//        progressService.save(progress);
//
//        //更新发布空间容量
//        updatePublished(size, companyId, userId);
//
//        //查找改路径下是否存在EO.csv的文件
//        /// How to read image AT from ODM?
//
//        List<SysMediaEntity> mediaList = new ArrayList<>();
//
//
//            for (File file : fileList) {
//                //photo信息
//                SysMediaEntity media = new SysMediaEntity();
//                media.setLayerId(layer.getLayerId());
//                media.setTitle(file.getName());
//                media.setUrl(pathConvert(file.getAbsolutePath()));
//                media.setImgWidth(0);
//                media.setImgHeight(0);
//                media.setType("photo");
//
//                if(!file.isHidden() && file.isFile()) {
//                    //读取图片自带的坐标信息
//                    readCoordinate(file, media);
//
//                    //缩略图路径
//                    String thumbnail = "";
//                    if (apollo) {
//                        thumbnail = thumbnail_url + file.getAbsolutePath().replace(Constant.prefix, "/media/").replace("\\", "/");
//                    } else {
//                        thumbnail = pathConvert(resultPath + "/thumbnail/" + file.getName());
//                    }
//                    media.setThumbnail(thumbnail);
//
//                    mediaList.add(media);
//                }
//            }
//
//        sysMediaService.insertBatch(mediaList);
//
//        //设置边界
//        setMediaBoundary(layer.getLayerId());
//
//        if (apollo) {
//            createApolloMedia(fileUrl, progress.getProgressId());
//        } else {
//            //异步生成缩略图 更新进度 完成后发送邮件
//            //thumbnail(resultPath, fileList, progress.getProgressId());
//        }
//    }
}
