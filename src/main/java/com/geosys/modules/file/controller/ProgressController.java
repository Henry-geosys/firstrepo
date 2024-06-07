//package com.geosys.modules.file.controller;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.geosys.common.utils.Messages;
//import com.geosys.common.utils.PageUtils;
//import com.geosys.common.utils.R;
//import com.geosys.modules.file.entity.ProgressEntity;
//import com.geosys.modules.file.service.ProgressService;
//import com.geosys.modules.sys.controller.AbstractController;
//import com.geosys.modules.sys.entity.SysLayerEntity;
//import com.geosys.modules.sys.service.SysCompanyService;
//import com.geosys.modules.sys.service.SysLayerService;
//import com.geosys.modules.sys.service.SysUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.NoSuchMessageException;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.PostConstruct;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//
///**
// * 进度
// *
// * @author lee
// * @email wilsonlky@126.com
// * @date 2019-12-23 15:48:08
// */
//@RestController
//@RequestMapping("/progress")
//public class ProgressController extends AbstractController {
//    @Autowired
//    private ProgressService progressService;
//    @Autowired
//    private SysLayerService sysLayerService;
//    @Autowired
//    private SysCompanyService sysCompanyService;
//    @Autowired
//    private SysUserService sysUserService;
//
//    @Value("${geosys.isMarkedShutdown}")
//    private Boolean isMarkedShutdown;
//
//    /**
//     * 列表
//     */
//    @GetMapping("/page")
//    //@RequiresPermissions("sys:progress:list")
//    public R page(@RequestParam Map<String, Object> params){
//        PageUtils page = progressService.queryPage(params);
//
//        return R.ok().put("page", page);
//    }
//
//    /**
//     * 列表
//     * @return
//     */
//    @GetMapping("/list")
//    public R list() {
//        List<ProgressEntity> progressList = progressService.list(new QueryWrapper<ProgressEntity>()
//                                                                    .eq("user_id", getUserId())
//                                                                    .ne("complete", -1)
//                                                                    .ne("total", -1));
//        progressList.sort((a1,a2)-> a2.getEndTime().compareTo(a1.getEndTime()));
//
//        for (ProgressEntity progress : progressList) {
//            try {
//                progress.setMessage(Messages.get(progress.getMessage()));
//            } catch (NoSuchMessageException ignored) {
//            }
//        }
//        return R.ok().put("data", progressList);
//
//    }
//
//
//    /**
//     * 信息
//     */
//    @GetMapping("/info/{progressId}")
//    //@RequiresPermissions("sys:progress:info")
//    public R info(@PathVariable("progressId") Long progressId){
//		ProgressEntity progress = progressService.getById(progressId);
//
//        return R.ok().put("data", progress);
//    }
//
//    /**
//     * 保存
//     */
//    @PostMapping("/save")
//    //@RequiresPermissions("sys:progress:save")
//    public R save(@RequestBody ProgressEntity progress){
//        progressService.save(progress);
//
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @PutMapping("/update")
//    //@RequiresPermissions("sys:progress:update")
//    public R update(@RequestBody ProgressEntity progress){
//        if (progress.getState() == 1) {
//            progress.setState(2);
//        }
//        progress.setMessage(null);
//        progressService.updateById(progress);
//
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @DeleteMapping("/delete")
//    //@RequiresPermissions("sys:progress:delete")
//    public R delete(@RequestBody Long[] progressIds){
//        progressService.removeByIds(Arrays.asList(progressIds));
//
//        return R.ok();
//    }
//
//    /**
//     * 清除已完成已读的进度条
//     * @return
//     */
//    @DeleteMapping("/clean")
//    public R clean() {
//        progressService.remove(new QueryWrapper<ProgressEntity>().eq("state", 2).eq("user_id", getUserId()));
//
//        return R.ok();
//    }
//
//    /**
//     * 取消正在发布的进度
//     * 删除进度信息及资源信息
//     * @return
//     */
//    @DeleteMapping("/cancel")
//    public R cancel(@RequestBody ProgressEntity progress) {
//        Long layerId = progress.getLayerId();
//
//        SysLayerEntity layer = sysLayerService.getById(layerId);
//
//        // In some cases the layer has already been removed. If so, skip the related operations if it is found to be null
//        try {
//	        //更新发布存储空间
//	        Boolean personalTrial = sysCompanyService.getPersonalTrial(layer.getCompanyId());
//	        double size = -layer.getSize();
//	        if (personalTrial) {
//	            sysUserService.updatePublished(layer.getUserId(), size);
//	        } else {
//	            sysCompanyService.updatePublished(layer.getCompanyId(), size);
//	        }
//
//	        //删除对应的图层信息
//	        sysLayerService.removeById(layerId);
//        }
//        catch(NullPointerException e) { ; }
//
//        //删除进度信息
//        progressService.removeById(progress);
//
//        return R.ok();
//    }
//
//    /**
//     * 启动时遍历progress数据库
//     */
//    @PostConstruct
//    private void init(){
//        if (isMarkedShutdown) {
//            //修改状态信息
//            progressService.update(new UpdateWrapper<ProgressEntity>()
//                    .set("message", "x_progress_error_shutdown_not_delete")
//                    .set("state", 1)
//                    .eq("state", 0));
//
//            //删除shutdown进度对应的图层/资源以及结果文件
//            Long[] layerIds = progressService.getLayerIdByState().toArray(new Long[0]);
//            if (layerIds.length != 0) {
//                sysLayerService.deleteLayer(layerIds);
//                //修改状态信息
//                progressService.update(new UpdateWrapper<ProgressEntity>()
//                        .set("message", "x_progress_error_shutdown")
//                        .eq("message", "x_progress_error_shutdown_not_delete"));
//            }
//        }
//    }
//}
