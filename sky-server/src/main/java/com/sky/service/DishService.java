package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {
    //新增菜品 对应口味
    public void saveWithFlavor(DishDTO dishDTO);
}
