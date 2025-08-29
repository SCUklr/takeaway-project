package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        // 原始密码（明文）
        String rawPassword = employeeLoginDTO.getPassword();
        // 对明文密码进行md5加密
        String password = DigestUtils.md5DigestAsHex(rawPassword.getBytes());

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码比对（数据库中的密码理论上是md5加密后的密文）
        boolean match = password.equals(employee.getPassword());
        // 兼容历史数据：如果数据库中仍然存储的是明文密码，则直接比对明文
        if (!match) {
            match = rawPassword.equals(employee.getPassword());
        }

        if (!match) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        System.out.println("当前线程的id：" + Thread.currentThread().getId());
        // DTO转化为Employee实体
        Employee employee = new Employee();

        // DTO属性拷贝,把DTO拷贝给employee，前提是属性名一致
        BeanUtils.copyProperties(employeeDTO, employee);

        // 设置账号状态，默认正常状态 1正常 0锁定
        employee.setStatus(StatusConstant.ENABLE);

        // 设置密码，默认密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        // 设置当前记录别的创建时间和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 设置当前记录创建人和修改人id
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);



    }

    /*
    * 分页查询
    * @param employeePageQueryDTO
    * @return
    * */

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //Select * FROM employee limit 0, 10
        //新插件：mybatis的page-helper
        //开始分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        // page加工为PageResult
        long total = page.getTotal();
        List<Employee> record = page.getResult();

        return new PageResult(total, record);
    }

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        //update employee set status = ? where id = ?
        //传到Mapper中进行修改状态SQL执行
        //创建一个Employee对象,加了@Builder注解后
        Employee employee = Employee.builder()
                        .status(status)
                                .id(id)
                                        .build();
        employeeMapper.update(employee);
    }

}
