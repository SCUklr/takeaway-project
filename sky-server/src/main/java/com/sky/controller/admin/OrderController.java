package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.vo.OrderVO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.dto.OrdersCancelDTO;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api(tags = "订单管理接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /** 订单搜索 */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO dto){
        PageResult pageResult = orderService.conditionSearch(dto);
        return Result.success(pageResult);
    }

    /** 各状态订单数量统计 */
    @GetMapping("/statistics")
    @ApiOperation("各状态订单数量统计")
    public Result<OrderStatisticsVO> statistics(){
        OrderStatisticsVO vo = orderService.statistics();
        return Result.success(vo);
    }

    /** 订单详情 */
    @GetMapping("/details/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> details(@PathVariable("id") Long id){
        return Result.success(orderService.details(id));
    }

    /** 接单 */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirm(@RequestBody OrdersConfirmDTO dto){
        orderService.confirm(dto);
        return Result.success();
    }

    /** 派送订单 */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result delivery(@PathVariable Long id){
        orderService.delivery(id);
        return Result.success();
    }

    /** 完成订单 */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable Long id){
        orderService.complete(id);
        return Result.success();
    }

    /** 拒单 */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result rejection(@RequestBody OrdersRejectionDTO dto) throws Exception {
        orderService.rejection(dto);
        return Result.success();
    }

    /** 取消订单 */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO dto) throws Exception {
        orderService.cancel(dto);
        return Result.success();
    }
}
